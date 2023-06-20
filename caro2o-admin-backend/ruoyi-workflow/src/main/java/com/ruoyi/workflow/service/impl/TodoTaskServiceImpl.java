package com.ruoyi.workflow.service.impl;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.workflow.domain.CarPackageAudit;
import com.ruoyi.workflow.domain.vo.CarPackageAuditVo;
import com.ruoyi.workflow.service.IBpmnNodeService;
import com.ruoyi.workflow.service.ICarPackageAuditService;
import com.ruoyi.workflow.service.ITodoTaskService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoTaskServiceImpl implements ITodoTaskService {

    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ICarPackageAuditService carPackageAuditService;
    @Autowired
    private IBpmnNodeService bpmnNodeService;
    @Autowired
    private ISysUserService userService;

    @Override
    public TableDataInfo selectTodoList(CarPackageAudit carPackageAudit) {
        // 1. 创建返回分页数据对象
        TableDataInfo info = new TableDataInfo();
        info.setCode(HttpStatus.SUCCESS);
        info.setMsg("查询成功");

        // 2. 获取当前登录用户的 id
        String username = SecurityUtils.getUsername();
        // 3. 基于当前登录用户, 去查询该用户为候选人/负责人的任务数量
        long total = taskService.createTaskQuery()
                .taskCandidateOrAssigned(username)
                .count();
        info.setTotal(total);

        // 4. 如果数量为 0, 直接返回空数据
        if (total == 0) {
            info.setRows(Collections.emptyList());
            return info;
        }

        // 1. 接收前端的分页条件
        PageDomain pageDomain = TableSupport.buildPageRequest();
        // 计算分页开始索引位置 (当前页 - 1) * 每页显示数量
        int startIndex = (pageDomain.getPageNum() - 1) * pageDomain.getPageSize();

        // 5. 基于当前登录用户去查询用户候选人/负责人的任务列表
        List<Task> taskList = taskService.createTaskQuery()
                .taskCandidateOrAssigned(username)
                .orderByTaskCreateTime()
                .asc()
                .listPage(startIndex, pageDomain.getPageSize());

        List<CarPackageAudit> list = new ArrayList<>();
        // 6. 遍历任务列表
        for (Task task : taskList) {
            // 7. 获取该任务的流程实例 id, 查询流程实例对象, 得到业务标识
            ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();
            String businessKey = instance.getBusinessKey();
            // 8. 通过业务标识, 查询审核记录对象
            CarPackageAudit audit = carPackageAuditService.getById(Long.valueOf(businessKey));
            CarPackageAuditVo vo = new CarPackageAuditVo(); // 实际需要的对象
            BeanUtils.copyBeanProp(vo, audit); // 将 audit 中所有同名属性 copy 到 vo 对象

            // 查询创建人
            SysUser sysUser = userService.selectUserById(Long.parseLong(audit.getCreatorId()));
            vo.setCreatorName(sysUser.getNickName());

            // 9. 为审核记录设置当前任务名称/id, 当前审核人
            vo.setTaskId(task.getId());
            vo.setTaskName(task.getName());
            // 通过任务 key 查询候选人
            List<SysUser> users = bpmnNodeService.selectUsersByNodeKey(task.getTaskDefinitionKey());
            List<String> userNames = users.stream()
                    // 将用户对象转换为 用户名
                    .map(SysUser::getNickName)
                    // 将所有用户名收集到 list
                    .collect(Collectors.toList());

            vo.setAuditors(JSON.toJSONString(userNames));

            // 10. 将当前审核记录对象放入返回的集合中
            list.add(vo);
        }
        // 11. 最终返回审核记录列表集合
        info.setRows(list);
        return info;
    }
}

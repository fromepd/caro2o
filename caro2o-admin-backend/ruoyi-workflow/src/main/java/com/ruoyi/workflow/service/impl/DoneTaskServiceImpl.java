package com.ruoyi.workflow.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
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
import com.ruoyi.workflow.domain.vo.HistoricActivity;
import com.ruoyi.workflow.service.IBpmnNodeService;
import com.ruoyi.workflow.service.ICarPackageAuditService;
import com.ruoyi.workflow.service.IDoneTaskService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Comment;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author 20463
 */
@Service
public class DoneTaskServiceImpl implements IDoneTaskService {

    @Autowired
    private TaskService taskService;
    @Autowired
    private ICarPackageAuditService carPackageAuditService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private HistoryService historyService;


    @Override
    public TableDataInfo selectTodoList(CarPackageAudit carPackageAudit) {
        // 1. 接收前端的分页参数
        TableDataInfo info = new TableDataInfo();
        info.setCode(HttpStatus.SUCCESS);
        // 2. 以当前用户作为负责人, 统计任务个数
        String username = SecurityUtils.getUsername();
        long total = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(username)
                .finished()
                .count();

        info.setTotal(total);
        // 3. 如果数量为 0, 返回空数据
        if (total == 0) {
            info.setRows(Collections.emptyList());
            return info;
        }
        // 4. 如果有数据, 构建分页条件
        PageDomain pageDomain = TableSupport.buildPageRequest();
        int startIndex = (pageDomain.getPageNum() - 1) * pageDomain.getPageSize();
        // 5. 以当前用户作为负责人, 查询所有已经完成的任务列表
        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
                // 负责人
                .taskAssignee(username)
                .finished() // 已完成
                // 根据创建实际排序
                .orderByTaskCreateTime()
                // 倒序排列
                .desc()
                // 分页查询
                .listPage(startIndex, pageDomain.getPageSize());

        // 6. 创建一个集合, 用于存储最终返回的数据列表
        List<CarPackageAudit> list = new ArrayList<>();
        for (HistoricTaskInstance task : taskList) {
            // 8. 基于任务对象, 得到流程实例 id, 查询流程实例对象
            HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();

            // 9. 基于流程实例对象, 得到业务标识, 基于业务标识查询审核记录对象
            CarPackageAudit audit = carPackageAuditService.getById(Long.valueOf(instance.getBusinessKey()));

            // 10. 将审核记录对象的数据拷贝到 vo 中, 并补充相应字段(完成时间/任务名称/负责人)
            CarPackageAuditVo vo = new CarPackageAuditVo();
            BeanUtils.copyBeanProp(vo, audit);

            vo.setFinishedTime(task.getEndTime());
            vo.setTaskId(task.getId());
            vo.setTaskName(task.getName());

            // 负责人 => 我的已办, 所以负责人就是当前用户
            vo.setAuditors(SecurityUtils.getLoginUser().getUser().getNickName());

            // 11. 查询任务的创建人并设置到审核记录
            SysUser creator = userService.selectUserById(Long.valueOf(audit.getCreatorId()));
            vo.setCreatorName(creator.getNickName());

            // 12. 将 vo 对象加入返回的集合中
            list.add(vo);
        }
        info.setRows(list);
        // 13. 返回分页数据
        return info;
    }

    @Override
    public List<HistoricActivity> selectHistoryList(String instanceId) {
        // 1. 基于流程实例id查询到历史任务(已完成)列表
        List<HistoricActivityInstance> instanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId) // 流程实例 id 查询
                .activityType("userTask") // 按照活动类型为 userTask
                .orderByHistoricActivityInstanceStartTime() // 按照开始时间排序
                .asc() // 升序排序
                .list(); // 返回列表

        // 2. 遍历历史任务集合, 基于任务id查询批注信息
        List<HistoricActivity> list = new ArrayList<>(instanceList.size());
        for (HistoricActivityInstance instance : instanceList) {
            HistoricActivity activity = new HistoricActivity();
            // 将查出来的数据拷贝到自己的活动对象中
            BeanUtils.copyBeanProp(activity, instance);

            // 批注信息
            List<Comment> comments = taskService.getTaskComments(instance.getTaskId(), "comment");
            if (!CollectionUtils.isEmpty(comments)) {
                // 获取批注信息
                activity.setComment(comments.get(0).getFullMessage());
            }

            // 负责人名称
            SysUser user = userService.selectUserByUserName(instance.getAssignee());
            if (user != null) {
                activity.setAssigneeName(user.getNickName());
            }

            // 将创建的对象加入最终返回的 list 中
            list.add(activity);
        }
        // 3. 将批注信息保存到对象, 最终返回数据
        return list;
    }
}

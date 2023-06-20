package com.ruoyi.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.business.domain.ServiceItem;
import com.ruoyi.business.service.IServiceItemService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.workflow.domain.CarPackageAudit;
import com.ruoyi.workflow.mapper.CarPackageAuditMapper;
import com.ruoyi.workflow.service.IBpmnNodeService;
import com.ruoyi.workflow.service.ICarPackageAuditService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 套餐审核Service业务层处理
 *
 * @author wang
 * @date 2022-11-17
 */
@Service
public class CarPackageAuditServiceImpl extends ServiceImpl<CarPackageAuditMapper, CarPackageAudit> implements ICarPackageAuditService {

    @Autowired
    private IServiceItemService serviceItemService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private IBpmnNodeService bpmnNodeService;
    @Autowired
    private ProcessDiagramGenerator processDiagramGenerator;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private ISysUserService userService;

    /**
     * 查询套餐审核列表
     *
     * @param carPackageAudit 套餐审核
     * @return 套餐审核
     */
    @Override
    public List<CarPackageAudit> selectCarPackageAuditList(CarPackageAudit carPackageAudit) {
        return getBaseMapper().selectCarPackageAuditList(carPackageAudit);
    }

    @Override
    public void audit(Long serviceItemId, String info) {
        // 1. 根据服务项 id 查询服务项对象
        ServiceItem serviceItem = serviceItemService.get(serviceItemId);
        // 2. 校验审核合法性(是套餐 && 状态为初始化), 提示当前无法审核
        if (ServiceItem.CARPACKAGE_NO.equals(serviceItem.getCarPackage())) {
            // 不是套餐
            throw new ServiceException("只有套餐才需要审核");
        }
        if (!ServiceItem.AUDITSTATUS_INIT.equals(serviceItem.getAuditStatus())) {
            throw new ServiceException("只有初始化状态才可以审核");
        }
        // 3. 修改服务项状态为审核中
        serviceItemService.changeStatus(serviceItemId, ServiceItem.AUDITSTATUS_AUDITING);

        // 4. 创建一条审核记录信息, 设置相关数据
        CarPackageAudit audit = new CarPackageAudit();
        // 4.1 将服务项信息转换为 json 字符串设置到审核记录中
        audit.setServiceItemId(serviceItemId);
        audit.setServiceItemName(serviceItem.getName());
        audit.setServiceItemPrice(serviceItem.getDiscountPrice());
        audit.setServiceItemInfo(serviceItem.getInfo());

        // 4.2 获取到当前登录用户, 将其 id 设置到审核记录中
        audit.setCreatorId(SecurityUtils.getUserId() + "");
        // 4.3 将审核状态设置为进行中
        audit.setStatus(CarPackageAudit.PACKAGE_AUDIT_STATUS_PENDING);
        // 设置创建时间
        audit.setCreateTime(new Date());
        audit.setInfo(info);

        // 4.4 保存审核记录信息
        super.save(audit);

        // 5. 构建流程变量(BusinessKey/金额/审核人员)
        Map<String, Object> map = new HashMap<>();
        // 5.1 BusinessKey = 审核记录的 id
        String businessKey = audit.getId() + "";
        // 5.2 金额 = 服务项的折扣金额
        // 存入的数据如果是数字类型, 建议转换为字符串
        map.put("money", serviceItem.getDiscountPrice().longValue() + "");
        // 5.3 TODO 审核人员 = 查询当前流程实例对应的节点列表, 获取到每一个节点的人员列表

        // 6. 基于流程定义 Key 开启流程实例, 得到流程实例对象
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(
                CarPackageAudit.CAR_PACKAGE_DEFINITION_KEY,
                businessKey,
                map
        );

        // 6.1 将流程实例 id 设置到审核记录中
        audit.setInstanceId(instance.getId());
        // 6.2 更新审核记录表
        super.updateById(audit);

        // 7. 设置当前任务的候选人, 基于流程实例查询到当前进行中的任务(list/singleResult) => 任务 API TaskService
        Task currentTask = taskService.createTaskQuery()
                .processInstanceId(instance.getId())
                .singleResult();
        // 7.1 基于任务 key 去查询候选人列表
        List<SysUser> userIdList = bpmnNodeService.selectUsersByNodeKey(currentTask.getTaskDefinitionKey());
        // 7.2 调用 activiti 的 api 为指定任务设置候选人
        for (SysUser user : userIdList) {
            taskService.addCandidateUser(currentTask.getId(), user.getUserName() + "");
        }
    }

    @Override
    public InputStream getProcessingImage(String instanceId) {
        // 1. 基于流程实例 id 查询流程实例对象
        HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(instanceId)
                .singleResult();
        if (instance == null) {
            throw new ServiceException("非法操作");
        }
        // 2. 基于仓库 service 查询 bpmnModel 对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(instance.getProcessDefinitionId());

        // 3. 获取需要高亮的活动 id list
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .list();

        List<String> highLightedActivities = list.stream()
                // 将活动实例对象转换为活动 id
                .map(HistoricActivityInstance::getActivityId)
                // 将 Stream 收集为一个 List 集合
                .collect(Collectors.toList());

        // 模型对象 == 流程文件转换的对象
        return processDiagramGenerator.generateDiagram(bpmnModel,
                // 需要高亮的活动 id 集合, 活动 == 所有运行的节点
                highLightedActivities,
                // 需要高亮的引导线 id 集合
                Collections.emptyList(),
                "宋体",
                "宋体",
                "宋体");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancel(Long id, String reason) {
        // 1. 基于审核记录id查询审核记录对象
        CarPackageAudit audit = getById(id);
        if (audit == null) {
            throw new ServiceException("非法操作");
        }
        // 当前审核记录必须是进行中才可以撤销申请
        if (!CarPackageAudit.PACKAGE_AUDIT_STATUS_PENDING.equals(audit.getStatus())) {
            throw new ServiceException("状态错误, 只有正在进行中的审核才可以撤销");
        }
        // 2. 基于审核记录中的服务项 id, 将服务项状态修改为初始化
        serviceItemService.changeStatus(audit.getServiceItemId(), ServiceItem.AUDITSTATUS_INIT);

        // 3. 将审核记录的状态修改为撤销申请
        audit.setStatus(CarPackageAudit.PACKAGE_AUDIT_STATUS_CANCELED);
        updateById(audit);

        // 4. 调用 activiti 的 api 删除流程实例, 提供删除理由
        runtimeService.deleteProcessInstance(audit.getInstanceId(), reason);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void doAudit(Long id, String taskId, boolean result, String info) {
        // 1. 根据审核记录 id 获取审核记录对象, 并判断状态为审核中才允许审核
        CarPackageAudit audit = getById(id);
        if (!CarPackageAudit.PACKAGE_AUDIT_STATUS_PENDING.equals(audit.getStatus())) {
            throw new ServiceException("非法操作");
        }

        // 2. 基于当前用户名作为候选人/负责人条件查询任务信息, 如果查询不到提示非法操作
        String username = SecurityUtils.getUsername();
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskCandidateOrAssigned(username)
                .singleResult();
        if (task == null) {
            throw new ServiceException("非法操作, 任务不存在");
        }
        // 3. 设置流程变量(审核结果)
        // 流程变量名 == 任务的 key
        taskService.setVariable(taskId, task.getTaskDefinitionKey(),result);
        // 4. 领取任务/完成任务/添加批注
        // 领取任务
        taskService.claim(taskId, username);
        // 添加批注
        taskService.addComment(taskId, task.getProcessInstanceId(), info);
        // 完成任务, 携带流程变量
        taskService.complete(taskId);

        // 5. 基于流程实例查询下一个进行中的任务, 根据是否有下一个任务判断流程是否结束
        Task nextTask = taskService.createTaskQuery()
                .processInstanceId(task.getProcessInstanceId())

                .singleResult();
        if (nextTask != null) {
            // 6. 有下一个任务
            // 6.1. 判断当前审核结果是通过还是拒绝
            if (result) {
                // 6.2. 如果是审核通过, 基于任务 key 查询候选人列表
                List<SysUser> users = bpmnNodeService.selectUsersByNodeKey(nextTask.getTaskDefinitionKey());
                // 6.2.1. 为下一个任务设置候选人
                for (SysUser user : users) {
                    taskService.addCandidateUser(nextTask.getId(), user.getUserName());
                }
            } else {
                // 6.3. 如果是审核拒绝, 为任务设置候选人为发起人
                // 6.3.1. 通过审核记录对象的创建 id, 查询创建人用户对象
                SysUser sysUser = userService.selectUserById(Long.valueOf(audit.getCreatorId()));
                // 6.3.2. 将创建人对象的用户名设置到任务的候选人
                taskService.addCandidateUser(nextTask.getId(), sysUser.getUserName());
                // 6.3.3. 更新服务项的状态为重新调整
                serviceItemService.changeStatus(audit.getServiceItemId(), ServiceItem.AUDITSTATUS_REPLY);
                // 更新审核记录的状态为审核拒绝
                audit.setStatus(CarPackageAudit.PACKAGE_AUDIT_STATUS_REJECTED);
                this.updateById(audit);
            }
        } else {
            // 7. 没有任务, 在我们的流程中代表结果一定为通过, 且流程结束
            // 7.1. 修改服务项状态为审核通过
            serviceItemService.changeStatus(audit.getServiceItemId(), ServiceItem.AUDITSTATUS_APPROVED);
            // 7.2. 修改审核记录状态为审核通过
            audit.setStatus(CarPackageAudit.PACKAGE_AUDIT_STATUS_PASSED);
            this.updateById(audit);
        }
    }

    @Override
    public void updateServiceItem(Long auditId, ServiceItem serviceItem) {
        // 根据 id 更新审核对象中的服务项信息
        // 创建更新条件构造对象， 设置泛型
        // update bus_car_package_audit
        // set service_item_name = ?, service_item_info = ?, service_item_price = ?
        // where id = ?
        super.update(new LambdaUpdateWrapper<CarPackageAudit>()
                // 指定修改 serviceItemName 属性
                .set(CarPackageAudit::getServiceItemName,serviceItem.getName())
                // 指定修改 getServiceItemInfo 属性
                .set(CarPackageAudit::getServiceItemInfo,serviceItem.getInfo())
                // 指定修改 getServiceItemPrice 属性
                .set(CarPackageAudit::getServiceItemPrice,serviceItem.getDiscountPrice())
                // 修改条件为 id = auditId
                .eq(CarPackageAudit::getId, auditId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void reapply(Long id, String taskId) {
        //     1. 基于审核记录id查询审核记录对象
        CarPackageAudit audit = this.getById(id);
        //     2. 验证当前状态是否是审核拒绝, 如果不是审核拒绝, 直接抛出异常
        if (!CarPackageAudit.PACKAGE_AUDIT_STATUS_REJECTED.equals(audit.getStatus())) {
            throw new ServiceException("非法操作");
        }
        //     3. 基于任务 id + 当前用户名查询任务对象, 确认是否是审核人
        String username = SecurityUtils.getUsername();
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskCandidateOrAssigned(username)
                .singleResult();
        if (task == null) {
            throw new ServiceException("非法操作");
        }

        //     5. 如果能查到, 领取并完成任务, 同时设置流程变量
        taskService.claim(taskId, username);
        taskService.complete(taskId);
        //     6. 查询下一个代办任务
        Task nextTask = taskService.createTaskQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult();
        // 设置流程变量
        taskService.setVariable(nextTask.getId(),"money",audit.getServiceItemPrice().longValue());
        //     7. 基于该任务查询候选人列表
        List<SysUser> users = bpmnNodeService.selectUsersByNodeKey(nextTask.getTaskDefinitionKey());
        //     8. 为该任务设置候选人
        for (SysUser user : users) {
            taskService.addCandidateUser(nextTask.getId(), user.getUserName());
        }
        //     9. 重新将服务项/审核记录的状态修改为审核中
        // 修改为审核中
        serviceItemService.changeStatus(audit.getServiceItemId(),ServiceItem.AUDITSTATUS_AUDITING);
        // 进行中
        audit.setStatus(CarPackageAudit.PACKAGE_AUDIT_STATUS_PENDING);
        this.updateById(audit);
    } 
}

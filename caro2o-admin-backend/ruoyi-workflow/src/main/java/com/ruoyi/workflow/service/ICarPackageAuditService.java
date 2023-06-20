package com.ruoyi.workflow.service;

import java.io.InputStream;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.business.domain.ServiceItem;
import com.ruoyi.workflow.domain.CarPackageAudit;

/**
 * 套餐审核Service接口
 *
 * @author wang
 * @date 2022-11-17
 */
public interface ICarPackageAuditService extends IService<CarPackageAudit> {

    /**
     * 查询套餐审核列表
     *
     * @param carPackageAudit 套餐审核
     * @return 套餐审核集合
     */
    public List<CarPackageAudit> selectCarPackageAuditList(CarPackageAudit carPackageAudit);

    /**
     * 服务项审核方法
     *
     * @param serviceItemId 服务项id
     */
    void audit(Long serviceItemId, String info);

    InputStream getProcessingImage(String instanceId);

    /**
     * 撤销审核
     *
     * @param id     审核记录 id
     * @param reason 撤销理由
     */
    void cancel(Long id, String reason);

    /**
     * 审核操作
     *
     * @param id     审核记录id
     * @param taskId 任务id
     * @param result 审核结果
     * @param info   批注信息
     */
    void doAudit(Long id, String taskId, boolean result, String info);

    /**
     * 更新服务项信息
     *
     * @param auditId
     * @param serviceItem 服务项
     */
    void updateServiceItem(Long auditId, ServiceItem serviceItem);

    /**
     * 重新申请审核
     * @param id 审核记录id
     * @param taskId 任务 id
     */
    void reapply(Long id, String taskId);
}

package com.ruoyi.workflow.service;

import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.workflow.domain.CarPackageAudit;
import com.ruoyi.workflow.domain.vo.HistoricActivity;

import java.util.List;

/**
 * @author 20463
 */
public interface IDoneTaskService {
    /**
     * 查询代办任务列表
     *
     * @param carPackageAudit 查询参数
     * @return 返回封装好的分页数据
     */
    TableDataInfo selectTodoList(CarPackageAudit carPackageAudit);

    /**
     * 查询历史记录
     * @param instanceId 流程实例id
     * @return
     */
    List<HistoricActivity> selectHistoryList(String instanceId);

}

package com.ruoyi.workflow.service;

import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.workflow.domain.CarPackageAudit;

public interface ITodoTaskService {
    /**
     * 查询代办任务列表
     *
     * @param carPackageAudit 查询参数
     * @return 返回封装好的分页数据
     */
    TableDataInfo selectTodoList(CarPackageAudit carPackageAudit);
}

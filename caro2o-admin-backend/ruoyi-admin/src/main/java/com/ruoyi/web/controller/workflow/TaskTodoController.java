package com.ruoyi.web.controller.workflow;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.workflow.domain.CarPackageAudit;
import com.ruoyi.workflow.service.ICarPackageAuditService;
import com.ruoyi.workflow.service.ITodoTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 我的代办Controller
 *
 * @author wang
 * @date 2022-11-17
 */
@RestController
@RequestMapping("/workflow/todo")
public class TaskTodoController extends BaseController {
    @Autowired
    private ITodoTaskService todoTaskService;

    /**
     * 查询套餐审核列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:todo:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarPackageAudit carPackageAudit) {
        return todoTaskService.selectTodoList(carPackageAudit);
    }
}

package com.ruoyi.web.controller.workflow;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.workflow.domain.CarPackageAudit;
import com.ruoyi.workflow.domain.vo.HistoricActivity;
import com.ruoyi.workflow.service.IDoneTaskService;
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
@RequestMapping("/workflow/done")
public class TaskDoneController extends BaseController {
    @Autowired
    private IDoneTaskService doneTaskService;

    /**
     * 查询套餐审核列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:done:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarPackageAudit carPackageAudit) {
        return doneTaskService.selectTodoList(carPackageAudit);
    }

    /**
     * 查询历史记录
     */
    @PreAuthorize("@ss.hasPermi('workflow:history:list')")
    @GetMapping("/history")
    public AjaxResult historyList(String instanceId) {
        List<HistoricActivity> list =  doneTaskService.selectHistoryList(instanceId);
        return AjaxResult.success(list);
    }
}

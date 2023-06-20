package com.ruoyi.workflow.controller;

import java.util.List;
import java.util.Arrays;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.workflow.domain.MarketingTask;
import com.ruoyi.workflow.service.IMarketingTaskService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 营销任务Controller
 *
 * @author wby
 * @date 2023-01-29
 */
@RestController
@RequestMapping("/workflow/tasks")
public class MarketingTaskController extends BaseController
{
    @Autowired
    private IMarketingTaskService marketingTaskService;

    /**
     * 查询营销任务列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:tasks:list')")
    @GetMapping("/list")
    public TableDataInfo list(MarketingTask marketingTask)
    {
        startPage();
        List<MarketingTask> list = marketingTaskService.selectMarketingTaskList(marketingTask);
        return getDataTable(list);
    }

    /**
     * 导出营销任务列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:tasks:export')")
    @Log(title = "营销任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MarketingTask marketingTask)
    {
        List<MarketingTask> list = marketingTaskService.selectMarketingTaskList(marketingTask);
        ExcelUtil<MarketingTask> util = new ExcelUtil<MarketingTask>(MarketingTask.class);
        util.exportExcel(response, list, "营销任务数据");
    }

    /**
     * 获取营销任务详细信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:tasks:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(marketingTaskService.getById(id));
    }

    /**
     * 新增营销任务
     */
    @PreAuthorize("@ss.hasPermi('workflow:tasks:add')")
    @Log(title = "营销任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MarketingTask marketingTask)
    {
        return toAjax(marketingTaskService.save(marketingTask));
    }

    /**
     * 修改营销任务
     */
    @PreAuthorize("@ss.hasPermi('workflow:tasks:edit')")
    @Log(title = "营销任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MarketingTask marketingTask)
    {
        return toAjax(marketingTaskService.updateById(marketingTask));
    }

    /**
     * 删除营销任务
     */
    @PreAuthorize("@ss.hasPermi('workflow:tasks:remove')")
    @Log(title = "营销任务", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(marketingTaskService.removeBatchByIds(Arrays.asList(ids)));
    }
}

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
import com.ruoyi.workflow.domain.Deal;
import com.ruoyi.workflow.service.IDealService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 交易明细Controller
 *
 * @author wby
 * @date 2023-02-01
 */
@RestController
@RequestMapping("/workflow/deal")
public class DealController extends BaseController
{
    @Autowired
    private IDealService dealService;

    /**
     * 查询交易明细列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:deal:list')")
    @GetMapping("/list")
    public TableDataInfo list(Deal deal)
    {
        startPage();
        List<Deal> list = dealService.selectDealList(deal);
        return getDataTable(list);
    }

    /**
     * 导出交易明细列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:deal:export')")
    @Log(title = "交易明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Deal deal)
    {
        List<Deal> list = dealService.selectDealList(deal);
        ExcelUtil<Deal> util = new ExcelUtil<Deal>(Deal.class);
        util.exportExcel(response, list, "交易明细数据");
    }

    /**
     * 获取交易明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:deal:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(dealService.getById(id));
    }

    /**
     * 新增交易明细
     */
    @PreAuthorize("@ss.hasPermi('workflow:deal:add')")
    @Log(title = "交易明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Deal deal)
    {
        return toAjax(dealService.save(deal));
    }

    /**
     * 修改交易明细
     */
    @PreAuthorize("@ss.hasPermi('workflow:deal:edit')")
    @Log(title = "交易明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Deal deal)
    {
        return toAjax(dealService.updateById(deal));
    }

    /**
     * 删除交易明细
     */
    @PreAuthorize("@ss.hasPermi('workflow:deal:remove')")
    @Log(title = "交易明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(dealService.removeBatchByIds(Arrays.asList(ids)));
    }


}

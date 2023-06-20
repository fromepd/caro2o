package com.ruoyi.web.controller.workflow;

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
import com.ruoyi.workflow.domain.Marketing;
import com.ruoyi.workflow.service.IMarketingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 营销活动Controller
 *
 * @author wby
 * @date 2023-01-29
 */
@RestController
@RequestMapping("/workflow/marketing")
public class MarketingController extends BaseController
{
    @Autowired
    private IMarketingService marketingService;

    /**
     * 查询营销活动列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:marketing:list')")
    @GetMapping("/list")
    public TableDataInfo list(Marketing marketing)
    {
        startPage();
        List<Marketing> list = marketingService.selectMarketingList(marketing);
        return getDataTable(list);
    }

    /**
     * 导出营销活动列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:marketing:export')")
    @Log(title = "营销活动", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Marketing marketing)
    {
        List<Marketing> list = marketingService.selectMarketingList(marketing);
        ExcelUtil<Marketing> util = new ExcelUtil<Marketing>(Marketing.class);
        util.exportExcel(response, list, "营销活动数据");
    }

    /**
     * 获取营销活动详细信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:marketing:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(marketingService.getById(id));
    }

    /**
     * 新增营销活动
     */
    @PreAuthorize("@ss.hasPermi('workflow:marketing:add')")
    @Log(title = "营销活动", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Marketing marketing)
    {
        return toAjax(marketingService.save(marketing));
    }

    /**
     * 修改营销活动
     */
    @PreAuthorize("@ss.hasPermi('workflow:marketing:edit')")
    @Log(title = "营销活动", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Marketing marketing)
    {
        return toAjax(marketingService.updateById(marketing));
    }

    /**
     * 删除营销活动
     */
    @PreAuthorize("@ss.hasPermi('workflow:marketing:remove')")
    @Log(title = "营销活动", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(marketingService.removeBatchByIds(Arrays.asList(ids)));
    }
}

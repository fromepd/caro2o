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
import com.ruoyi.workflow.domain.MarketingRewards;
import com.ruoyi.workflow.service.IMarketingRewardsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 营销奖励 Controller
 *
 * @author wby
 * @date 2023-01-29
 */
@RestController
@RequestMapping("/workflow/rewards")
public class MarketingRewardsController extends BaseController
{
    @Autowired
    private IMarketingRewardsService marketingRewardsService;

    /**
     * 查询营销奖励 列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:rewards:list')")
    @GetMapping("/list")
    public TableDataInfo list(MarketingRewards marketingRewards)
    {
        startPage();
        List<MarketingRewards> list = marketingRewardsService.selectMarketingRewardsList(marketingRewards);
        return getDataTable(list);
    }

    /**
     * 导出营销奖励 列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:rewards:export')")
    @Log(title = "营销奖励 ", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MarketingRewards marketingRewards)
    {
        List<MarketingRewards> list = marketingRewardsService.selectMarketingRewardsList(marketingRewards);
        ExcelUtil<MarketingRewards> util = new ExcelUtil<MarketingRewards>(MarketingRewards.class);
        util.exportExcel(response, list, "营销奖励 数据");
    }

    /**
     * 获取营销奖励 详细信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:rewards:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(marketingRewardsService.getById(id));
    }

    /**
     * 新增营销奖励 
     */
    @PreAuthorize("@ss.hasPermi('workflow:rewards:add')")
    @Log(title = "营销奖励 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MarketingRewards marketingRewards)
    {
        return toAjax(marketingRewardsService.save(marketingRewards));
    }

    /**
     * 修改营销奖励 
     */
    @PreAuthorize("@ss.hasPermi('workflow:rewards:edit')")
    @Log(title = "营销奖励 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MarketingRewards marketingRewards)
    {
        return toAjax(marketingRewardsService.updateById(marketingRewards));
    }

    /**
     * 删除营销奖励 
     */
    @PreAuthorize("@ss.hasPermi('workflow:rewards:remove')")
    @Log(title = "营销奖励 ", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(marketingRewardsService.removeBatchByIds(Arrays.asList(ids)));
    }
}

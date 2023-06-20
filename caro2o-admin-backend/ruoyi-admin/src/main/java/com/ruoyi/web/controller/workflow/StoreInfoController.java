package com.ruoyi.web.controller.workflow;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.StoreInfo;
import com.ruoyi.workflow.service.IStoreInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 门店信息Controller
 *
 * @author wang
 * @date 2022-11-24
 */
@RestController
@RequestMapping("/workflow/store")
public class StoreInfoController extends BaseController {
    @Autowired
    private IStoreInfoService storeInfoService;

    /**
     * 查询门店信息列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:store:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreInfo storeInfo) {
        startPage();
        List<StoreInfo> list = storeInfoService.selectStoreInfoList(storeInfo);
        return getDataTable(list);
    }

    /**
     * 导出门店信息列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:store:export')")
    @Log(title = "门店信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreInfo storeInfo) {
        List<StoreInfo> list = storeInfoService.selectStoreInfoList(storeInfo);
        ExcelUtil<StoreInfo> util = new ExcelUtil<StoreInfo>(StoreInfo.class);
        util.exportExcel(response, list, "门店信息数据");
    }

    /**
     * 获取门店信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:store:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(storeInfoService.getById(id));
    }

    /**
     * 新增门店信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:store:add')")
    @Log(title = "门店信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StoreInfo storeInfo) {
        return toAjax(storeInfoService.save(storeInfo));
    }

    /**
     * 修改门店信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:store:edit')")
    @Log(title = "门店信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StoreInfo storeInfo) {
        if (storeInfo.getStatus() ==  2) {
            throw new ServiceException("更新时不允许更新状态未删除");
        }
        return toAjax(storeInfoService.updateById(storeInfo));
    }

    /**
     * 删除门店信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:store:remove')")
    @Log(title = "门店信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(storeInfoService.removeBatchById(ids));
    }

}

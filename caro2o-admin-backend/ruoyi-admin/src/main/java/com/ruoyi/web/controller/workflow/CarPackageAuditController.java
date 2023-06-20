package com.ruoyi.web.controller.workflow;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.workflow.domain.CarPackageAudit;
import com.ruoyi.workflow.service.ICarPackageAuditService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * 套餐审核Controller
 *
 * @author wang
 * @date 2022-11-17
 */
@RestController
@RequestMapping("/workflow/carPackageAudit")
public class CarPackageAuditController extends BaseController {
    @Autowired
    private ICarPackageAuditService carPackageAuditService;


    /**
     * 查询套餐审核列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:audit:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarPackageAudit carPackageAudit) {
        startPage();
        List<CarPackageAudit> list = carPackageAuditService.selectCarPackageAuditList(carPackageAudit);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('workflow:audit:resource')")
    @GetMapping("/processing/image")
    public void getProcessingImage(String instanceId, HttpServletResponse resp) throws IOException {
        InputStream in = carPackageAuditService.getProcessingImage(instanceId);
        IOUtils.copy(in, resp.getOutputStream());
    }
    /**
     * 套餐审核接口
     */
    @PreAuthorize("@ss.hasPermi('workflow:audit:start')")
    @PostMapping("/audit")
    public AjaxResult audit(Long id, String info) {
        carPackageAuditService.audit(id, info);
        return AjaxResult.success("发起审核成功");
    }

    @PostMapping("/doAudit")
    public AjaxResult doAudit(Long id, String taskId, boolean result, String info) {
        carPackageAuditService.doAudit(id, taskId, result, info);
        return AjaxResult.success("审核成功");
    }

    @PostMapping("/reapply")
    public AjaxResult reapply(Long id, String taskId) {
        carPackageAuditService.reapply(id, taskId);
        return AjaxResult.success("重新申请成功");
    }


    /**
     * 撤销审核记录
     */
    @PreAuthorize("@ss.hasPermi('workflow:audit:cancel')")
    @PostMapping("/cancel")
    public AjaxResult cancel(Long id, String reason) {
        carPackageAuditService.cancel(id, reason);
        return AjaxResult.success("撤销成功");
    }

    /**
     * 导出套餐审核列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:audit:export')")
    @Log(title = "套餐审核", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CarPackageAudit carPackageAudit) {
        List<CarPackageAudit> list = carPackageAuditService.selectCarPackageAuditList(carPackageAudit);
        ExcelUtil<CarPackageAudit> util = new ExcelUtil<CarPackageAudit>(CarPackageAudit.class);
        util.exportExcel(response, list, "套餐审核数据");
    }

    /**
     * 获取套餐审核详细信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:audit:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(carPackageAuditService.getById(id));
    }

    /**
     * 新增套餐审核
     */
    @PreAuthorize("@ss.hasPermi('workflow:audit:add')")
    @Log(title = "套餐审核", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CarPackageAudit carPackageAudit) {
        return toAjax(carPackageAuditService.save(carPackageAudit));
    }

    /**
     * 修改套餐审核
     */
    @PreAuthorize("@ss.hasPermi('workflow:audit:edit')")
    @Log(title = "套餐审核", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CarPackageAudit carPackageAudit) {
        return toAjax(carPackageAuditService.updateById(carPackageAudit));
    }

    /**
     * 删除套餐审核
     */
    @PreAuthorize("@ss.hasPermi('workflow:audit:remove')")
    @Log(title = "套餐审核", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(carPackageAuditService.removeBatchByIds(Arrays.asList(ids)));
    }
}

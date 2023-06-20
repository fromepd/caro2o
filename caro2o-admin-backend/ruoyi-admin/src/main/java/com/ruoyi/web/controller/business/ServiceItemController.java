package com.ruoyi.web.controller.business;


import com.ruoyi.business.domain.ServiceItem;
import com.ruoyi.business.query.ServiceItemQuery;
import com.ruoyi.business.service.IServiceItemService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.workflow.service.ICarPackageAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 岗位控制器
 * 养修服务单
 */
@RestController
@RequestMapping("business/serviceItem")
public class ServiceItemController extends BaseController {

    @Autowired
    private IServiceItemService serviceItemService;
    @Autowired
    private ICarPackageAuditService carPackageAuditService;

    @GetMapping()
    @PreAuthorize("@ss.hasPermi('business:serviceItem:list')")
    public TableDataInfo query(ServiceItemQuery qo) {
        startPage();
        return getDataTable(serviceItemService.query(qo));
    }

    /**
     * 编辑回显信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('business:serviceItem:query')")
    public AjaxResult get(@PathVariable Long id) {
        // 套餐信息
        ServiceItem serviceItem = serviceItemService.get(id);
        return AjaxResult.success(serviceItem);
    }

    /**
     * 新增
     *
     * @param serviceItem
     * @return
     */
    @PostMapping()
    @PreAuthorize("@ss.hasPermi('business:serviceItem:add')")
    public AjaxResult addSave(@RequestBody ServiceItem serviceItem) {
        serviceItemService.save(serviceItem);
        return AjaxResult.success();
    }

    /**
     * 编辑
     *
     * @param serviceItem
     * @return
     */
    @PutMapping()
    @PreAuthorize("@ss.hasPermi('business:serviceItem:update')")
    public AjaxResult edit(@RequestBody ServiceItem serviceItem) {
        serviceItemService.update(serviceItem);
        return AjaxResult.success();
    }

    /**
     * 编辑
     *
     * @param serviceItem  服务项
     * @return
     */
    @PutMapping("/reEdit")
    @PreAuthorize("@ss.hasPermi('business:serviceItem:update')")
    public AjaxResult reEdit(ServiceItem serviceItem, Long auditId) {
        // 更新服务项
        serviceItemService.updateForAudit(serviceItem);
        // 更新审核记录
        carPackageAuditService.updateServiceItem(auditId,serviceItem);
        return AjaxResult.success();
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPermi('business:serviceItem:remove')")
    public AjaxResult remove(@PathVariable String ids) {
        serviceItemService.deleteBatch(ids);
        return AjaxResult.success();
    }

    /**
     * 上架
     *
     * @param id
     * @return
     */
    @PutMapping("/saleOn/{id}")
    @PreAuthorize("@ss.hasPermi('business:serviceItem:saleOn')")
    public AjaxResult saleOn(@PathVariable Long id) {
        serviceItemService.saleOn(id);
        return AjaxResult.success();
    }

    /**
     * 下架
     *
     * @param id
     * @return
     */
    @PutMapping("/saleOff/{id}")
    @PreAuthorize("@ss.hasPermi('business:serviceItem:saleOff')")
    public AjaxResult saleOff(@PathVariable Long id) {
        serviceItemService.saleOff(id);
        return AjaxResult.success();
    }
}

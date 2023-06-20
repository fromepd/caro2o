package com.ruoyi.web.controller.customer;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.customer.domain.CustomerVisit;
import com.ruoyi.customer.service.ICustomerVisitService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 客户关怀Controller
 *
 * @author xiaoxiao
 * @date 2022-08-26
 */
@RestController
@RequestMapping("/customer/visit")
public class CustomerVisitController extends BaseController {
    @Autowired
    private ICustomerVisitService busCusCareService;

    /**
     * 查询客户关怀列表
     */
    @PreAuthorize("@ss.hasPermi('customer:visit:list')")
    @GetMapping()
    public TableDataInfo list(CustomerVisit customerVisit) {
        startPage();
        List<CustomerVisit> list = busCusCareService.selectCustomerVisitList(customerVisit);
        return getDataTable(list);
    }

    /**
     * 导出客户关怀列表
     */
    @PreAuthorize("@ss.hasPermi('customer:visit:export')")
    @Log(title = "客户关怀", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CustomerVisit customerVisit) {
        List<CustomerVisit> list = busCusCareService.selectCustomerVisitList(customerVisit);
        ExcelUtil<CustomerVisit> util = new ExcelUtil<CustomerVisit>(CustomerVisit.class);
        util.exportExcel(response, list, "客户关怀数据");
    }

    /**
     * 获取客户关怀详细信息
     */
    @PreAuthorize("@ss.hasPermi('customer:visit:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(busCusCareService.selectCustomerVisitById(id));
    }

    /**
     * 新增客户关怀
     */
    @PreAuthorize("@ss.hasPermi('customer:visit:add')")
    @Log(title = "客户关怀", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CustomerVisit customerVisit) {
        return toAjax(busCusCareService.insertCustomerVisit(customerVisit));
    }
}

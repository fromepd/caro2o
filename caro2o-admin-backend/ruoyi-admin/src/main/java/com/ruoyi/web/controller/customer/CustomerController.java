package com.ruoyi.web.controller.customer;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.customer.domain.Customer;
import com.ruoyi.customer.service.ICustomerService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 客户档案Controller
 *
 * @author xiaoxiao
 * @date 2022-08-26
 */
@RestController
@RequestMapping("/customer")
public class CustomerController extends BaseController {
    @Autowired
    private ICustomerService customerService;

    /**
     * 查询客户档案列表
     */
    @PreAuthorize("@ss.hasPermi('customer:list')")
    @GetMapping()
    public TableDataInfo list(Customer customer) {
        startPage();
        List<Customer> list = customerService.selectCustomerList(customer);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('customer:listAll')")
    @GetMapping("/all")
    public AjaxResult listAll() {
        List<Customer> customers = customerService.selectCustomerList(new Customer());
        return AjaxResult.success(customers);
    }

    /**
     * 导出客户档案列表
     */
    @PreAuthorize("@ss.hasPermi('customer:export')")
    @Log(title = "客户档案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Customer customer) {
        List<Customer> list = customerService.selectCustomerList(customer);
        ExcelUtil<Customer> util = new ExcelUtil<Customer>(Customer.class);
        util.exportExcel(response, list, "客户档案数据");
    }

    /**
     * 获取客户档案详细信息
     */
    @PreAuthorize("@ss.hasPermi('customer:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(customerService.selectCustomerById(id));
    }

    /**
     * 新增客户档案
     */
    @PreAuthorize("@ss.hasPermi('customer:add')")
    @Log(title = "客户档案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Customer customer) {
        return toAjax(customerService.insertCustomer(customer));
    }

    /**
     * 修改客户档案
     */
    @PreAuthorize("@ss.hasPermi('customer:edit')")
    @Log(title = "客户档案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Customer customer) {
        return toAjax(customerService.updateCustomer(customer));
    }

}

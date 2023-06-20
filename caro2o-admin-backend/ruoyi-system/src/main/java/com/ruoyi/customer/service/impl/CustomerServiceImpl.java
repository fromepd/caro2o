package com.ruoyi.customer.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.customer.mapper.CustomerMapper;
import com.ruoyi.customer.domain.Customer;
import com.ruoyi.customer.service.ICustomerService;

/**
 * 客户档案Service业务层处理
 *
 * @author xiaoxiao
 * @date 2022-08-26
 */
@Service
public class CustomerServiceImpl implements ICustomerService {
    @Autowired
    private CustomerMapper customerMapper;

    /**
     * 查询客户档案
     *
     * @param id 客户档案主键
     * @return 客户档案
     */
    @Override
    public Customer selectCustomerById(Long id) {
        return customerMapper.selectCustomerById(id);
    }

    /**
     * 查询客户档案列表
     *
     * @param customer 客户档案
     * @return 客户档案
     */
    @Override
    public List<Customer> selectCustomerList(Customer customer) {
        return customerMapper.selectCustomerList(customer);
    }

    /**
     * 新增客户档案
     *
     * @param customer 客户档案
     * @return 结果
     */
    @Override
    public int insertCustomer(Customer customer) {
        customer.setInputUserId(SecurityUtils.getUserId());
        customer.setEntryTime(new Date());
        return customerMapper.insertCustomer(customer);
    }

    /**
     * 修改客户档案
     *
     * @param customer 客户档案
     * @return 结果
     */
    @Override
    public int updateCustomer(Customer customer) {
        return customerMapper.updateCustomer(customer);
    }
}

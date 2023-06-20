package com.ruoyi.customer.mapper;

import java.util.List;
import com.ruoyi.customer.domain.Customer;

/**
 * 客户档案Mapper接口
 * 
 * @author xiaoxiao
 * @date 2022-08-26
 */
public interface CustomerMapper
{
    /**
     * 查询客户档案
     * 
     * @param id 客户档案主键
     * @return 客户档案
     */
    public Customer selectCustomerById(Long id);

    /**
     * 查询客户档案列表
     * 
     * @param customer 客户档案
     * @return 客户档案集合
     */
    public List<Customer> selectCustomerList(Customer customer);

    /**
     * 新增客户档案
     * 
     * @param customer 客户档案
     * @return 结果
     */
    public int insertCustomer(Customer customer);

    /**
     * 修改客户档案
     * 
     * @param customer 客户档案
     * @return 结果
     */
    public int updateCustomer(Customer customer);
}

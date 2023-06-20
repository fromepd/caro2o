package com.ruoyi.customer.mapper;

import java.util.List;
import com.ruoyi.customer.domain.CustomerVisit;

/**
 * 客户关怀Mapper接口
 * 
 * @author xiaoxiao
 * @date 2022-08-26
 */
public interface CustomerVisitMapper
{
    /**
     * 查询客户关怀
     * 
     * @param id 客户关怀主键
     * @return 客户关怀
     */
    public CustomerVisit selectCustomerVisitById(Long id);

    /**
     * 查询客户关怀列表
     * 
     * @param customerVisit 客户关怀
     * @return 客户关怀集合
     */
    public List<CustomerVisit> selectCustomerVisitList(CustomerVisit customerVisit);

    /**
     * 新增客户关怀
     * 
     * @param customerVisit 客户关怀
     * @return 结果
     */
    public int insertCustomerVisit(CustomerVisit customerVisit);

}

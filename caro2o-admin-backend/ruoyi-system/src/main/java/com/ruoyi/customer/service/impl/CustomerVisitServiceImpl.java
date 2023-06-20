package com.ruoyi.customer.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.customer.mapper.CustomerVisitMapper;
import com.ruoyi.customer.domain.CustomerVisit;
import com.ruoyi.customer.service.ICustomerVisitService;

/**
 * 客户关怀Service业务层处理
 *
 * @author xiaoxiao
 * @date 2022-08-26
 */
@Service
public class CustomerVisitServiceImpl implements ICustomerVisitService {
    @Autowired
    private CustomerVisitMapper customerVisitMapper;

    /**
     * 查询客户关怀
     *
     * @param id 客户关怀主键
     * @return 客户关怀
     */
    @Override
    public CustomerVisit selectCustomerVisitById(Long id) {
        return customerVisitMapper.selectCustomerVisitById(id);
    }

    /**
     * 查询客户关怀列表
     *
     * @param customerVisit 客户关怀
     * @return 客户关怀
     */
    @Override
    public List<CustomerVisit> selectCustomerVisitList(CustomerVisit customerVisit) {
        return customerVisitMapper.selectCustomerVisitList(customerVisit);
    }

    /**
     * 新增客户关怀
     *
     * @param customerVisit 客户关怀
     * @return 结果
     */
    @Override
    public int insertCustomerVisit(CustomerVisit customerVisit) {
        customerVisit.setInputUserId(SecurityUtils.getUserId());
        customerVisit.setEntryTime(new Date());
        return customerVisitMapper.insertCustomerVisit(customerVisit);
    }
}

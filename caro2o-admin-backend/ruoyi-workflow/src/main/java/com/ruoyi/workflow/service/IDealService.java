package com.ruoyi.workflow.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.workflow.domain.Deal;

/**
 * 交易明细Service接口
 *
 * @author wby
 * @date 2023-02-01
 */
public interface IDealService extends IService<Deal> {

    /**
     * 查询交易明细列表
     *
     * @param deal 交易明细
     * @return 交易明细集合
     */
    public List<Deal> selectDealList(Deal deal);





}

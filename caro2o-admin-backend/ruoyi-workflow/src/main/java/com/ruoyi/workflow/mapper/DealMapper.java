package com.ruoyi.workflow.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.workflow.domain.Deal;

/**
 * 交易明细Mapper接口
 *
 * @author wby
 * @date 2023-02-01
 */
public interface DealMapper extends BaseMapper<Deal> {

  /**
   * 查询交易明细列表
   *
   * @param deal 交易明细
   * @return 交易明细集合
   */
  public List<Deal> selectDealList(Deal deal);
}

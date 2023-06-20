package com.ruoyi.workflow.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.workflow.domain.MarketingRewards;

/**
 * 营销奖励 Mapper接口
 *
 * @author wby
 * @date 2023-01-29
 */
public interface MarketingRewardsMapper extends BaseMapper<MarketingRewards> {

  /**
   * 查询营销奖励 列表
   *
   * @param marketingRewards 营销奖励 
   * @return 营销奖励 集合
   */
  public List<MarketingRewards> selectMarketingRewardsList(MarketingRewards marketingRewards);
}

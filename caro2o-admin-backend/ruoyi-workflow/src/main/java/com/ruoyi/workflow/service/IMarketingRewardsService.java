package com.ruoyi.workflow.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.workflow.domain.MarketingRewards;

/**
 * 营销奖励 Service接口
 *
 * @author wby
 * @date 2023-01-29
 */
public interface IMarketingRewardsService extends IService<MarketingRewards> {

    /**
     * 查询营销奖励 列表
     *
     * @param marketingRewards 营销奖励 
     * @return 营销奖励 集合
     */
    public List<MarketingRewards> selectMarketingRewardsList(MarketingRewards marketingRewards);

}

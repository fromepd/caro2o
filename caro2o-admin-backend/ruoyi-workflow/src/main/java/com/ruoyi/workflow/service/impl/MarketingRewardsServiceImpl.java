package com.ruoyi.workflow.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.workflow.mapper.MarketingRewardsMapper;
import com.ruoyi.workflow.domain.MarketingRewards;
import com.ruoyi.workflow.service.IMarketingRewardsService;

/**
 * 营销奖励 Service业务层处理
 *
 * @author wby
 * @date 2023-01-29
 */
@Service
public class MarketingRewardsServiceImpl extends ServiceImpl<MarketingRewardsMapper, MarketingRewards> implements IMarketingRewardsService {

    /**
     * 查询营销奖励 列表
     *
     * @param marketingRewards 营销奖励 
     * @return 营销奖励 
     */
    @Override
    public List<MarketingRewards> selectMarketingRewardsList(MarketingRewards marketingRewards) {
        return getBaseMapper().selectMarketingRewardsList(marketingRewards);
    }
}

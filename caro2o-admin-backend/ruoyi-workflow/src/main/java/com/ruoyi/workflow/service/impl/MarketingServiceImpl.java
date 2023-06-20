package com.ruoyi.workflow.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.workflow.mapper.MarketingMapper;
import com.ruoyi.workflow.domain.Marketing;
import com.ruoyi.workflow.service.IMarketingService;

/**
 * 营销活动Service业务层处理
 *
 * @author wby
 * @date 2023-01-29
 */
@Service
public class MarketingServiceImpl extends ServiceImpl<MarketingMapper, Marketing> implements IMarketingService {

    /**
     * 查询营销活动列表
     *
     * @param marketing 营销活动
     * @return 营销活动
     */
    @Override
    public List<Marketing> selectMarketingList(Marketing marketing) {
        return getBaseMapper().selectMarketingList(marketing);
    }
}

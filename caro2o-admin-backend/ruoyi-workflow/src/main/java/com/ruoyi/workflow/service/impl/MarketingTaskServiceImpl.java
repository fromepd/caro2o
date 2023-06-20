package com.ruoyi.workflow.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.workflow.mapper.MarketingTaskMapper;
import com.ruoyi.workflow.domain.MarketingTask;
import com.ruoyi.workflow.service.IMarketingTaskService;

/**
 * 营销任务Service业务层处理
 *
 * @author wby
 * @date 2023-01-29
 */
@Service
public class MarketingTaskServiceImpl extends ServiceImpl<MarketingTaskMapper, MarketingTask> implements IMarketingTaskService {

    /**
     * 查询营销任务列表
     *
     * @param marketingTask 营销任务
     * @return 营销任务
     */
    @Override
    public List<MarketingTask> selectMarketingTaskList(MarketingTask marketingTask) {
        return getBaseMapper().selectMarketingTaskList(marketingTask);
    }
}

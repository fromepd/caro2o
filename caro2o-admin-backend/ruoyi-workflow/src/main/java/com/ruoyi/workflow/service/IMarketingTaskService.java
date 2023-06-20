package com.ruoyi.workflow.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.workflow.domain.MarketingTask;

/**
 * 营销任务Service接口
 *
 * @author wby
 * @date 2023-01-29
 */
public interface IMarketingTaskService extends IService<MarketingTask> {

    /**
     * 查询营销任务列表
     *
     * @param marketingTask 营销任务
     * @return 营销任务集合
     */
    public List<MarketingTask> selectMarketingTaskList(MarketingTask marketingTask);

}

package com.ruoyi.workflow.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.workflow.domain.MarketingTask;

/**
 * 营销任务Mapper接口
 *
 * @author wby
 * @date 2023-01-29
 */
public interface MarketingTaskMapper extends BaseMapper<MarketingTask> {

  /**
   * 查询营销任务列表
   *
   * @param marketingTask 营销任务
   * @return 营销任务集合
   */
  public List<MarketingTask> selectMarketingTaskList(MarketingTask marketingTask);
}

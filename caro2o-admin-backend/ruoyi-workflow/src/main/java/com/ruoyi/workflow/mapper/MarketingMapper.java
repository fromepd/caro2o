package com.ruoyi.workflow.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.workflow.domain.Marketing;

/**
 * 营销活动Mapper接口
 *
 * @author wby
 * @date 2023-01-29
 */
public interface MarketingMapper extends BaseMapper<Marketing> {

  /**
   * 查询营销活动列表
   *
   * @param marketing 营销活动
   * @return 营销活动集合
   */
  public List<Marketing> selectMarketingList(Marketing marketing);
}

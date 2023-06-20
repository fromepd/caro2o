package com.ruoyi.workflow.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.workflow.domain.Purse;

/**
 * 钱包管理Mapper接口
 *
 * @author wby
 * @date 2023-02-01
 */
public interface PurseMapper extends BaseMapper<Purse> {

  /**
   * 查询钱包管理列表
   *
   * @param purse 钱包管理
   * @return 钱包管理集合
   */
  public List<Purse> selectPurseList(Purse purse);
}

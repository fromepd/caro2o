package com.ruoyi.workflow.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.workflow.domain.CarPackageAudit;

/**
 * 套餐审核Mapper接口
 *
 * @author wang
 * @date 2022-11-17
 */
public interface CarPackageAuditMapper extends BaseMapper<CarPackageAudit> {

  /**
   * 查询套餐审核列表
   *
   * @param carPackageAudit 套餐审核
   * @return 套餐审核集合
   */
  public List<CarPackageAudit> selectCarPackageAuditList(CarPackageAudit carPackageAudit);
}

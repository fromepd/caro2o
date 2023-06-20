package com.ruoyi.business.mapper;


import com.ruoyi.business.domain.ServiceItem;
import com.ruoyi.business.query.ServiceItemQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ServiceItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ServiceItem record);

    ServiceItem selectByPrimaryKey(Long id);

    List<ServiceItem> selectAll();

    int updateByPrimaryKey(ServiceItem record);

    List<ServiceItem> selectForList(ServiceItemQuery qo);

    void updateSaleStatus(@Param("id") Long id, @Param("saleStatus") Integer saleStatus);

    void changeAuditStatus(@Param("id") Long id, @Param("auditStatus") Integer auditStatus);
}
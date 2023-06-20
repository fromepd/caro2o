package com.ruoyi.business.mapper;


import com.ruoyi.business.domain.StatementItem;
import com.ruoyi.business.query.StatementItemQuery;

import java.util.List;

public interface StatementItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StatementItem record);

    StatementItem selectByPrimaryKey(Long id);

    List<StatementItem> selectAll();

    int updateByPrimaryKey(StatementItem record);

    List<StatementItem> query(StatementItemQuery qo);

    void deleteByStatementId(Long statementId);
}
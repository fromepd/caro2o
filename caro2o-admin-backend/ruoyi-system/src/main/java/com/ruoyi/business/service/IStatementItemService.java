package com.ruoyi.business.service;


import com.ruoyi.business.domain.StatementItem;
import com.ruoyi.business.query.StatementItemQuery;

import java.util.List;

/**
 * Created by wolfcode-lanxw
 */
public interface IStatementItemService {
    List<StatementItem> query(StatementItemQuery qo);

    void saveItems(List<StatementItem> items);

    /**
     * 删除结算单明细
     * @param id
     */
    void deleteByStatementId(Long id);
}

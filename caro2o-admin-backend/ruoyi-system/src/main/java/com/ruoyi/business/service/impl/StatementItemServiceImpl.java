package com.ruoyi.business.service.impl;


import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.business.domain.Statement;
import com.ruoyi.business.domain.StatementItem;
import com.ruoyi.business.mapper.StatementItemMapper;
import com.ruoyi.business.query.StatementItemQuery;
import com.ruoyi.business.service.IStatementItemService;
import com.ruoyi.business.service.IStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by wolfcode-lanxw
 */
@Service
public class StatementItemServiceImpl implements IStatementItemService {
    @Autowired
    private StatementItemMapper statementItemMapper;
    @Autowired
    private IStatementService statementService;


    @Override
    public List<StatementItem> query(StatementItemQuery qo) {
        return statementItemMapper.query(qo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveItems(List<StatementItem> items) {
        //进行合理性校验
        if (items == null || items.size() <= 0) {
            throw new BusinessException("非法操作");
        }
        //获取伪造的优惠价格数据
        StatementItem tempItem = items.remove(items.size() - 1);
        //获取优惠价格的账单id
        Long statementId = tempItem.getStatementId();
        //获取优惠价格
        BigDecimal disCountPrice = tempItem.getItemPrice();
        //获取账单信息
        Statement statement = statementService.get(statementId);
        //判断账单信息是否存在
        if (Statement.STATUS_PAID.equals(statement.getStatus())) {
            throw new BusinessException("已支付的结算不能进行保存操作");
        }
        //删除之前的明细的数据
        statementItemMapper.deleteByStatementId(statementId);
        //声明
        BigDecimal totalAmount = new BigDecimal(0);
        //定义总数量变量
        BigDecimal totalQuantity = new BigDecimal(0);
        //将剩余的消费信息存入StatementItem表中
        for (StatementItem item : items) {
            //保存数据库中
            statementItemMapper.insert(item);
            totalAmount = totalAmount.add(item.getItemPrice().multiply(item.getItemQuantity()));
            totalQuantity = totalQuantity.add(item.getItemQuantity());
        }
        //判断是否有非法操作
        if (disCountPrice.compareTo(totalAmount) > 0) {
            throw new BusinessException("非法操作");
        }
        //更新结算单的总消费金额,总数量,总折扣金额
        statementService.updateAmount(statementId, totalAmount, totalQuantity, disCountPrice);
    }

    @Override
    public void deleteByStatementId(Long id) {
        statementItemMapper.deleteByStatementId(id);
    }
}

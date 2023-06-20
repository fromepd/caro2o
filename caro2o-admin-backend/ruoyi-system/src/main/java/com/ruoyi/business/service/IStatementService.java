package com.ruoyi.business.service;


import com.ruoyi.business.domain.Statement;
import com.ruoyi.business.query.StatementQuery;

import java.math.BigDecimal;
import java.util.List;

/**
 * 服务结算单服务接口
 */
public interface IStatementService {

    /**
     * 分页
     * @param qo
     * @return
     */
    List<Statement> query(StatementQuery qo);

    /**
     * 查单个
     * @param id
     * @return
     */
    Statement get(Long id);
    /**
     * 保存
     * @param statement
     */
    void save(Statement statement);
    /**
     * 更新
     * @param statement
     */
    void update(Statement statement);
    /**
     *  软删除
     * @param id
     */
    void deleteBatch(Long id);

    /**
     * 查询全部
     * @return
     */
    List<Statement> list();

    /**
     * 更新结算单的总金额,总数量,折扣金额
     * @param statementId
     * @param totalAmount
     * @param totalQuantity
     * @param disCountPrice
     */
    void updateAmount(Long statementId, BigDecimal totalAmount, BigDecimal totalQuantity, BigDecimal disCountPrice);

    /**
     * 支付更新状态
     * @param id
     */
    void pay(Long id);

    /**
     * 从预约单中保存结算单
     * @param statement
     */
    void saveFromAppointment(Statement statement);

    /**
     * 根据预约单ID查询结算单对象
     * @param appointmentId
     * @return
     */
    Statement getByAppointmentId(Long appointmentId);
}

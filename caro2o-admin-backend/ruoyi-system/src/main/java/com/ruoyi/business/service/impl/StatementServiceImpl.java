package com.ruoyi.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.business.domain.Appointment;
import com.ruoyi.business.domain.Statement;
import com.ruoyi.business.mapper.StatementItemMapper;
import com.ruoyi.business.mapper.StatementMapper;
import com.ruoyi.business.query.StatementQuery;
import com.ruoyi.business.service.IAppointmentService;
import com.ruoyi.business.service.IStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class StatementServiceImpl implements IStatementService {

    @Resource
    private StatementMapper statementMapper;
    @Resource
    private StatementItemMapper statementItemMapper;
    @Autowired
    @Lazy
    private IAppointmentService appointmentService;

    @Override
    public List<Statement> query(StatementQuery qo) {
        PageHelper.startPage(qo.getPageNum(), qo.getPageSize());
        return statementMapper.selectForList(qo);
    }


    @Override
    public void save(Statement statement) {
        Statement newObj = new Statement();
        newObj.setCustomerName(statement.getCustomerName());
        newObj.setCustomerPhone(statement.getCustomerPhone());
        newObj.setCarSeries(statement.getCarSeries());
        newObj.setCreateTime(new Date());
        newObj.setLicensePlate(statement.getLicensePlate());
        newObj.setInfo(statement.getInfo());
        newObj.setServiceType(statement.getServiceType());
        newObj.setActualArrivalTime(statement.getActualArrivalTime());
        statementMapper.insert(newObj);
    }

    @Override
    public Statement get(Long id) {
        return statementMapper.selectByPrimaryKey(id);
    }


    @Override
    public void update(Statement statement) {
        Statement currentObj = this.get(statement.getId());
        if (Statement.STATUS_PAID.equals(currentObj.getStatus())) {
            throw new BusinessException("非法操作");
        }
        currentObj.setCustomerName(statement.getCustomerName());
        currentObj.setCustomerPhone(statement.getCustomerPhone());
        currentObj.setCarSeries(statement.getCarSeries());
        currentObj.setLicensePlate(statement.getLicensePlate());
        currentObj.setInfo(statement.getInfo());
        currentObj.setServiceType(statement.getServiceType());
        currentObj.setActualArrivalTime(statement.getActualArrivalTime());
        statementMapper.updateByPrimaryKey(currentObj);
    }

    @Override
    @Transactional
    public void deleteBatch(Long id) {
        Statement statement = this.get(id);
        if (Statement.STATUS_PAID.equals(statement.getStatus())) {
            throw new BusinessException("非法操作");
        }
        //删除结算单
        statementMapper.deleteByPrimaryKey(id);
        //删除结算单明细
        statementItemMapper.deleteByStatementId(id);
        if (statement.getAppointmentId() != null) {
            //修改预约单的状态
            appointmentService.changeStatus(statement.getAppointmentId(), Appointment.STATUS_ARRIVAL);
        }
    }

    @Override
    public List<Statement> list() {
        return statementMapper.selectAll();
    }

    @Override
    public void updateAmount(Long statementId, BigDecimal totalAmount, BigDecimal totalQuantity, BigDecimal disCountPrice) {
        statementMapper.updateAmount(statementId, totalAmount, totalQuantity, disCountPrice);
    }

    @Override
    public void pay(Long id) {

        Long userId = SecurityUtils.getUserId();
        //修改结算单状态
        statementMapper.pay(id, userId, Statement.STATUS_PAID);
        //判断当前结算单是否有对应预约单,如果有修改状态
        Statement statement = this.get(id);
        if (statement.getAppointmentId() != null) {
            appointmentService.changeStatus(statement.getAppointmentId(), Appointment.STATUS_PAID);
        }
    }

    @Override
    public void saveFromAppointment(Statement statement) {
        statementMapper.insert(statement);
    }

    @Override
    public Statement getByAppointmentId(Long appointmentId) {
        return statementMapper.getByAppointmentId(appointmentId);
    }
}

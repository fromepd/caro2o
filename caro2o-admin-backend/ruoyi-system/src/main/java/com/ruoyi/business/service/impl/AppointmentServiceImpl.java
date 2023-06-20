package com.ruoyi.business.service.impl;


import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.business.domain.Appointment;
import com.ruoyi.business.domain.Statement;
import com.ruoyi.system.domain.vo.ReservationVo;
import com.ruoyi.business.mapper.AppointmentMapper;
import com.ruoyi.business.query.AppointmentQuery;
import com.ruoyi.business.service.IAppointmentService;
import com.ruoyi.business.service.IStatementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AppointmentServiceImpl implements IAppointmentService {

    @Resource
    private AppointmentMapper appointmentMapper;
    @Resource
    private IStatementService statementService;

    @Override
    public List<Appointment> query(AppointmentQuery qo) {
        return appointmentMapper.selectForList(qo);
    }


    @Override
    public void save(Appointment appointment) {
        Appointment newObj = new Appointment();
        newObj.setCustomerName(appointment.getCustomerName());
        newObj.setCustomerPhone(appointment.getCustomerPhone());
        newObj.setAppointmentTime(appointment.getAppointmentTime());
        newObj.setCarSeries(appointment.getCarSeries());
        newObj.setCreateTime(new Date());
        newObj.setLicensePlate(appointment.getLicensePlate());
        newObj.setServiceType(appointment.getServiceType());
        newObj.setInfo(appointment.getInfo());
        appointmentMapper.insert(newObj);
    }

    @Override
    public Appointment get(Long id) {
        return appointmentMapper.selectByPrimaryKey(id);
    }


    @Override
    public void update(Appointment appointment) {
        Appointment currentObj = this.get(appointment.getId());//查询单个
        if(!Appointment.STATUS_APPOINTMENT.equals(currentObj.getStatus())){
            //不是预约中
            throw new BusinessException("非预约中的记录不允许编辑");
        }
        currentObj.setCustomerName(appointment.getCustomerName());
        currentObj.setCustomerPhone(appointment.getCustomerPhone());
        currentObj.setAppointmentTime(appointment.getAppointmentTime());
        currentObj.setCarSeries(appointment.getCarSeries());
        currentObj.setLicensePlate(appointment.getLicensePlate());
        currentObj.setServiceType(appointment.getServiceType());
        currentObj.setInfo(appointment.getInfo());
        appointmentMapper.updateByPrimaryKey(currentObj);
    }

    @Override
    public void deleteBatch(String ids) {
        Long[] dictIds = Convert.toLongArray(ids);
        for (Long dictId : dictIds) {
            appointmentMapper.deleteByPrimaryKey(dictId);
        }
    }

    @Override
    public List<Appointment> list() {
        return appointmentMapper.selectAll();
    }

    @Override
    public void arrival(Long id) {
        //合理性校验
        Appointment appointment = this.get(id);
        if(Appointment.STATUS_APPOINTMENT.equals(appointment.getStatus())){
            //处于预约中
            //appointmentMapper.changeStatus(id,Appointment.STATUS_ARRIVAL);
            appointmentMapper.arrival(id,Appointment.STATUS_ARRIVAL,new Date());
        }else{
            throw new BusinessException("非法操作");
        }
    }

    @Override
    public void cancel(Long id) {
        Appointment appointment = this.get(id);
        if(Appointment.STATUS_APPOINTMENT.equals(appointment.getStatus())){
            appointmentMapper.changeStatus(id,Appointment.STATUS_CANCEL);
        }else{
            throw new BusinessException("非法操作");
        }
    }

@Override
@Transactional
public Statement generateStatement(Long appointmentId) {
    Appointment appointment = this.get(appointmentId);
    //合理性校验
    if(Appointment.STATUS_ARRIVAL.equals(appointment.getStatus())||
            Appointment.STATUS_SETTLE.equals(appointment.getStatus())||
            Appointment.STATUS_PAID.equals(appointment.getStatus())){
        //根据预约单id查询结算单对象
        Statement statement = statementService.getByAppointmentId(appointmentId);
        if(statement==null){
            //从预约单中把信息保存到结算单
            statement = new Statement();
            statement.setCustomerName(appointment.getCustomerName());
            statement.setCustomerPhone(appointment.getCustomerPhone());
            statement.setServiceType(appointment.getServiceType());
            statement.setCarSeries(appointment.getCarSeries());
            statement.setAppointmentId(appointmentId);
            statement.setLicensePlate(appointment.getLicensePlate());
            statement.setInfo(appointment.getInfo());
            statement.setActualArrivalTime(appointment.getActualArrivalTime());
            statement.setCreateTime(new Date());
            statementService.saveFromAppointment(statement);
            //修改预约单状态(结算单生成)
            appointmentMapper.changeStatus(appointmentId,Appointment.STATUS_SETTLE);
        }
        return statement;
    }else{
        throw new BusinessException("非法操作");
    }
}

    @Override
    public void changeStatus(Long appointmentId, Integer status) {
        appointmentMapper.changeStatus(appointmentId,status);
    }

    @Override
    public int reservation(ReservationVo reservationVo) {
        reservationVo.setCreateTime(new Date());
        return  appointmentMapper.reservation(reservationVo);
    }
}

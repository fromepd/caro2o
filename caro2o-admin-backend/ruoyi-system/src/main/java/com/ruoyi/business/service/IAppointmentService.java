package com.ruoyi.business.service;


import com.ruoyi.business.domain.Appointment;
import com.ruoyi.business.domain.Statement;
import com.ruoyi.system.domain.vo.ReservationVo;
import com.ruoyi.business.query.AppointmentQuery;

import java.util.List;

/**
 * 养修服务单项服务接口
 */
public interface IAppointmentService {

    /**
     * 分页
     * @param qo
     * @return
     */
    List<Appointment> query(AppointmentQuery qo);

    /**
     * 查单个
     * @param id
     * @return
     */
    Appointment get(Long id);
    /**
     * 保存
     * @param appointment
     */
    void save(Appointment appointment);
    /**
     * 更新
     * @param appointment
     */
    void update(Appointment appointment);
    /**
     *  批量删除
     * @param ids
     */
    void deleteBatch(String ids);

    /**
     * 查询全部
     * @return
     */
    List<Appointment> list();

    /**
     * 客户到店
     * @param id
     */
    void arrival(Long id);
    /**
     * 客户到店
     * @param id
     */
    void cancel(Long id);

    /**
     * 生成结算单
     * @param appointmentId
     * @return
     */
    Statement generateStatement(Long appointmentId);

    void changeStatus(Long appointmentId, Integer status);

    /**
     * 预约服务
     * @param reservationVo
     * @return
     */
    int reservation(ReservationVo reservationVo);
}

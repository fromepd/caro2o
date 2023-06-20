package com.ruoyi.business.mapper;


import com.ruoyi.business.domain.Appointment;
import com.ruoyi.system.domain.vo.ReservationVo;
import com.ruoyi.business.query.AppointmentQuery;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AppointmentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Appointment record);

    Appointment selectByPrimaryKey(Long id);

    List<Appointment> selectAll();

    int updateByPrimaryKey(Appointment record);

    List<Appointment> selectForList(@Param("qo")AppointmentQuery qo);

    void changeStatus(@Param("id") Long id, @Param("status") Integer status);

    void arrival(@Param("id") Long id, @Param("status") Integer status, @Param("date") Date date);


    int reservation(ReservationVo reservationVo);
}
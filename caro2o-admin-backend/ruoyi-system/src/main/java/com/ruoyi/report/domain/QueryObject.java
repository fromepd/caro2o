package com.ruoyi.report.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class QueryObject {
    //开始时间：默认是当前时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;
    //结束时间:默认的开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;

    //判断是年月日周的选择标识
    private Integer dateStatus=0; //0按日 1按周 2按月 3按年

    //预约单还是服务单
    private Integer serviceStatus=0; //查看的是预约还是结算单 0结算单和1预约单
    //用户名
    private String customerName;

    //电话号码
    private String customerPhone;
    
    //页码
    Integer pageNum=1;
    //容量
    Integer pageSize=10;
}

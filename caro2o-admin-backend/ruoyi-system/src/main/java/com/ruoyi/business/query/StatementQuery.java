package com.ruoyi.business.query;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by wolfcode-lanxw
 */
@Setter
@Getter
public class StatementQuery extends QueryObject {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
    public Date getEndTime(){
        if(endTime!=null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endTime);
            calendar.add(Calendar.DAY_OF_MONTH,1);
            return calendar.getTime();
        }else{
            return null;
        }
    }
}

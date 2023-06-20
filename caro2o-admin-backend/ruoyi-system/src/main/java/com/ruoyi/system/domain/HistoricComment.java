package com.ruoyi.system.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lanxw
 */
@Setter
@Getter
public class HistoricComment {
    private String taskName;
    private String comment;
    private String startTime;
    private String endTime;
    private String durationInMillis;
}

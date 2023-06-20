package com.ruoyi.workflow.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.workflow.domain.CarPackageAudit;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CarPackageAuditVo extends CarPackageAudit {

    private String taskId;
    private String taskName;
    private String auditors;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishedTime;
}

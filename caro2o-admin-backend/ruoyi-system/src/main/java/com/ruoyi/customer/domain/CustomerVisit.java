package com.ruoyi.customer.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 客户关怀对象 bus_cus_care
 *
 * @author xiaoxiao
 * @date 2022-08-26
 */
@Getter
@Setter
@ToString
public class CustomerVisit extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 客户关怀id
     */
    private Long id;

    /**
     * 回访客户
     */
    private Long customerId;

    @Excel(name = "回访客户")
    private String customerName;

    /**
     * 回访方式（电话、短信、微信、QQ、上门）
     */
    @Excel(name = "回访方式", dictType = "customer_visit_method")
    private Integer visitMethod;

    /**
     * 回访原因
     */
    @Excel(name = "回访原因")
    private String visitCause;

    /**
     * 回访结果
     */
    @Excel(name = "回访结果")
    private String visitResult;

    /**
     * 回访日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "回访日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date visitDate;

    /**
     * 录入人
     */
    private Long inputUserId;

    @Excel(name = "录入人")
    private String inputUserName;

    /**
     * 录入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "录入时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date entryTime;
}

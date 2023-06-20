package com.ruoyi.customer.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 客户档案对象 bus_cus_record
 *
 * @author xiaoxiao
 * @date 2022-08-26
 */
@Getter
@Setter
@ToString
public class Customer extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 客户档案id
     */
    private Long id;

    /**
     * 客户姓名
     */
    @Excel(name = "客户姓名")
    private String name;

    /**
     * 电话
     */
    @Excel(name = "电话")
    private String phone;

    /**
     * 性别(男/女)
     */
    @Excel(name = "性别", dictType = "sys_user_sex")
    private String sex;

    /**
     * 录入人
     */
    private Long inputUserId;

    @Excel(name = "录入人")
    private String inputUserName;

    /**
     * 录入时间
     */
    @Excel(name = "录入时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date entryTime;
}

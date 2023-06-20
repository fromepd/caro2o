package com.ruoyi.workflow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 套餐审核对象 bus_car_package_audit
 *
 * @author wang
 * @date 2022-11-17
 */
@Getter
@Setter
@TableName("bus_car_package_audit")
public class CarPackageAudit extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 审核流程定义的 key */
    public static final String CAR_PACKAGE_DEFINITION_KEY = "car_package";

    /** 进行中 */
    public static final Integer PACKAGE_AUDIT_STATUS_PENDING = 0;
    /** 审核拒绝 */
    public static final Integer PACKAGE_AUDIT_STATUS_REJECTED = 1;
    /** 审核通过 */
    public static final Integer PACKAGE_AUDIT_STATUS_PASSED = 2;
    /** 撤销审核 */
    public static final Integer PACKAGE_AUDIT_STATUS_CANCELED = 3;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;

    /**
     * 服务单项id
     */
    @Excel(name = "服务单项id")
    @TableField("service_item_id")
    private Long serviceItemId;

    /**
     * 套餐名称
     */
    @Excel(name = "套餐名称")
    @TableField("service_item_name")
    private String serviceItemName;

    /**
     * 套餐备注
     */
    @Excel(name = "套餐备注")
    @TableField("service_item_info")
    private String serviceItemInfo;

    /**
     * 套餐价格
     */
    @Excel(name = "套餐价格")
    @TableField("service_item_price")
    private BigDecimal serviceItemPrice;

    /**
     * 流程实例id
     */
    @Excel(name = "流程实例id")
    @TableField("instance_id")
    private String instanceId;

    /**
     * 创建者
     */
    @Excel(name = "创建者")
    @TableField("creator_id")
    private String creatorId;

    @TableField(exist = false)
    private String creatorName;

    /**
     * 备注
     */
    @Excel(name = "备注")
    @TableField("info")
    private String info;

    /**
     * 状态
     */
    @Excel(name = "状态")
    @TableField("status")
    private Integer status;

    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("serviceItemId", getServiceItemId())
                .append("serviceItemName", getServiceItemName())
                .append("serviceItemInfo", getServiceItemInfo())
                .append("serviceItemPrice", getServiceItemPrice())
                .append("instanceId", getInstanceId())
                .append("creatorId", getCreatorId())
                .append("info", getInfo())
                .append("status", getStatus())
                .append("createTime", getCreateTime())
                .toString();
    }
}

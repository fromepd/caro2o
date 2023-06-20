package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 门店信息对象 bus_store_info
 *
 * @author wang
 * @date 2022-11-24
 */
@TableName("bus_store_info")
public class StoreInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;

    /** 门店名称 */
    @Excel(name = "门店名称")
    @TableField("name")
    private String name;

    /** 门店介绍 */
    @Excel(name = "门店介绍")
    @TableField("intro")
    private String intro;

    /** 经营范围 */
    @Excel(name = "经营范围")
    @TableField("business_scope")
    private String businessScope;

    /** 联系方式 */
    @Excel(name = "联系方式")
    @TableField("contact")
    private String contact;

    /** 门店地址 */
    @Excel(name = "门店地址")
    @TableField("address")
    private String address;

    /** 开店时间 */
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "开店时间", width = 30, dateFormat = "HH:mm:ss")
    @TableField("opening_time")
    private Date openingTime;

    /** 闭店时间 */
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "闭店时间", width = 30, dateFormat = "HH:mm:ss")
    @TableField("closing_time")
    private Date closingTime;

    /** 管理员id */
    @Excel(name = "管理员id")
    @TableField("manager_id")
    private Long managerId;

    /** 管理员名称 */
    @TableField("manager_name")
    private String managerName;

    /** 管理员手机号 */
    @Excel(name = "管理员手机号")
    @TableField("manager_tel")
    private String managerTel;

    /** 状态: 0=营业中, 1=闭店, 2=删除 */
    @Excel(name = "状态: 0=营业中, 1=闭店, 2=删除")
    @TableField("status")
    private Integer status;

    /** 创建人id */
    @Excel(name = "创建人id")
    @TableField("creator_id")
    private Long creatorId;

    /** 创建时间 */
    @TableField("created_time")
    private Date createdTime;

    /** 更新人员id */
    @TableField("updator_id")
    private Long updatorId;

    /** 更新时间 */
    @TableField("updated_time")
    private Date updatedTime;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setIntro(String intro)
    {
        this.intro = intro;
    }

    public String getIntro()
    {
        return intro;
    }
    public void setBusinessScope(String businessScope)
    {
        this.businessScope = businessScope;
    }

    public String getBusinessScope()
    {
        return businessScope;
    }
    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public String getContact()
    {
        return contact;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setOpeningTime(Date openingTime)
    {
        this.openingTime = openingTime;
    }

    public Date getOpeningTime()
    {
        return openingTime;
    }
    public void setClosingTime(Date closingTime)
    {
        this.closingTime = closingTime;
    }

    public Date getClosingTime()
    {
        return closingTime;
    }
    public void setManagerId(Long managerId)
    {
        this.managerId = managerId;
    }

    public Long getManagerId()
    {
        return managerId;
    }
    public void setManagerName(String managerName)
    {
        this.managerName = managerName;
    }

    public String getManagerName()
    {
        return managerName;
    }
    public void setManagerTel(String managerTel)
    {
        this.managerTel = managerTel;
    }

    public String getManagerTel()
    {
        return managerTel;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }
    public void setCreatorId(Long creatorId)
    {
        this.creatorId = creatorId;
    }

    public Long getCreatorId()
    {
        return creatorId;
    }
    public void setCreatedTime(Date createdTime)
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime()
    {
        return createdTime;
    }
    public void setUpdatorId(Long updatorId)
    {
        this.updatorId = updatorId;
    }

    public Long getUpdatorId()
    {
        return updatorId;
    }
    public void setUpdatedTime(Date updatedTime)
    {
        this.updatedTime = updatedTime;
    }

    public Date getUpdatedTime()
    {
        return updatedTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("intro", getIntro())
            .append("businessScope", getBusinessScope())
            .append("contact", getContact())
            .append("address", getAddress())
            .append("openingTime", getOpeningTime())
            .append("closingTime", getClosingTime())
            .append("managerId", getManagerId())
            .append("managerName", getManagerName())
            .append("managerTel", getManagerTel())
            .append("status", getStatus())
            .append("creatorId", getCreatorId())
            .append("createdTime", getCreatedTime())
            .append("updatorId", getUpdatorId())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}

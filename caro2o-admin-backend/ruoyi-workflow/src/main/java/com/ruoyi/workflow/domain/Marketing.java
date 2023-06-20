package com.ruoyi.workflow.domain;

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

/**
 * 营销活动对象 marketing
 *
 * @author wby
 * @date 2023-01-29
 */
@TableName("marketing")
public class Marketing extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;

    /** 活动名称 */
    @Excel(name = "活动名称")
    @TableField("name")
    private String name;

    /** 排序 */
    @Excel(name = "排序")
    @TableField("sort")
    private Long sort;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("creattime")
    private Date creattime;

    /** 终止时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "终止时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("stop_time")
    private Date stopTime;

    /** 显示位置 */
    @Excel(name = "显示位置")
    @TableField("displayposition")
    private Long displayposition;

    /** BANNER图 */
    @Excel(name = "BANNER图")
    @TableField("banner_image")
    private String bannerImage;

    /** H5链接 */
    @Excel(name = "H5链接")
    @TableField("H5_url")
    private String h5Url;

    /** 活动规则 */
    @Excel(name = "活动规则")
    @TableField("rule")
    private Long rule;

    /** 状态 */
    @Excel(name = "状态")
    @TableField("state")
    private Integer state;

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
    public void setSort(Long sort)
    {
        this.sort = sort;
    }

    public Long getSort()
    {
        return sort;
    }
    public void setCreattime(Date creattime)
    {
        this.creattime = creattime;
    }

    public Date getCreattime()
    {
        return creattime;
    }
    public void setStopTime(Date stopTime)
    {
        this.stopTime = stopTime;
    }

    public Date getStopTime()
    {
        return stopTime;
    }
    public void setDisplayposition(Long displayposition)
    {
        this.displayposition = displayposition;
    }

    public Long getDisplayposition()
    {
        return displayposition;
    }
    public void setBannerImage(String bannerImage)
    {
        this.bannerImage = bannerImage;
    }

    public String getBannerImage()
    {
        return bannerImage;
    }
    public void setH5Url(String h5Url)
    {
        this.h5Url = h5Url;
    }

    public String getH5Url()
    {
        return h5Url;
    }
    public void setRule(Long rule)
    {
        this.rule = rule;
    }

    public Long getRule()
    {
        return rule;
    }
    public void setState(Integer state)
    {
        this.state = state;
    }

    public Integer getState()
    {
        return state;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("sort", getSort())
                .append("creattime", getCreattime())
                .append("stopTime", getStopTime())
                .append("displayposition", getDisplayposition())
                .append("bannerImage", getBannerImage())
                .append("h5Url", getH5Url())
                .append("rule", getRule())
                .append("state", getState())
                .toString();
    }
}

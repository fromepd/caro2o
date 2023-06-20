package com.ruoyi.workflow.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 营销任务对象 marketing_task
 *
 * @author wby
 * @date 2023-01-29
 */
@TableName("marketing_task")
public class MarketingTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;

    /** 任务名称 */
    @Excel(name = "任务名称")
    @TableField("name")
    private String name;

    /** 可完成任务 */
    @Excel(name = "可完成任务")
    @TableField("function")
    private String function;

    /** 参与限制 */
    @Excel(name = "参与限制")
    @TableField("join_limit")
    private String joinLimit;

    /** 次数限制 */
    @Excel(name = "次数限制")
    @TableField("tries_limit")
    private Long triesLimit;

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
    public void setFunction(String function)
    {
        this.function = function;
    }

    public String getFunction()
    {
        return function;
    }
    public void setJoinLimit(String joinLimit)
    {
        this.joinLimit = joinLimit;
    }

    public String getJoinLimit()
    {
        return joinLimit;
    }
    public void setTriesLimit(Long triesLimit)
    {
        this.triesLimit = triesLimit;
    }

    public Long getTriesLimit()
    {
        return triesLimit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("function", getFunction())
            .append("joinLimit", getJoinLimit())
            .append("triesLimit", getTriesLimit())
            .toString();
    }
}

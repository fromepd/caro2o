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
 * 钱包管理对象 purse
 *
 * @author wby
 * @date 2023-02-01
 */
@TableName("purse")
public class Purse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;

    /** 用户名 */
    @Excel(name = "用户名")
    @TableField("user_id")
    private Long userId;

    /** 现金余额 */
    @Excel(name = "现金余额")
    @TableField("balance")
    private Long balance = 0L;

    /** 总收入 */
    @Excel(name = "总收入")
    @TableField("revenue")
    private Long revenue = 0L;

    /** 总支出 */
    @Excel(name = "总支出")
    @TableField("expenditure")
    private Long expenditure = 0L;

    @TableField(exist = false)
    private String name;

    /** 状态 */
    @Excel(name = "状态")
    @TableField("rule_purse")
    private Long rulePurse = 0L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setBalance(Long balance)
    {
        this.balance = balance;
    }

    public Long getBalance()
    {
        return balance;
    }
    public void setRevenue(Long revenue)
    {
        this.revenue = revenue;
    }

    public Long getRevenue()
    {
        return revenue;
    }
    public void setExpenditure(Long expenditure)
    {
        this.expenditure = expenditure;
    }

    public Long getExpenditure()
    {
        return expenditure;
    }
    public void setRulePurse(Long rulePurse)
    {
        this.rulePurse = rulePurse;
    }

    public Long getRulePurse()
    {
        return rulePurse;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("balance", getBalance())
            .append("revenue", getRevenue())
            .append("expenditure", getExpenditure())
            .append("rulePurse", getRulePurse())
            .toString();
    }
}

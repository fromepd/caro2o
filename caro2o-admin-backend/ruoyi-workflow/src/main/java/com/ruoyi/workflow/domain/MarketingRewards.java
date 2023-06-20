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
 * 营销奖励 对象 marketing_rewards
 *
 * @author wby
 * @date 2023-01-29
 */
@TableName("marketing_rewards")
public class MarketingRewards extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;

    /** 奖品名称 */
    @Excel(name = "奖品名称")
    @TableField("name")
    private String name;

    /** 是否关联库存商品 */
    @Excel(name = "是否关联库存商品")
    @TableField("related_products")
    private String relatedProducts;

    /** 获奖概率 */
    @Excel(name = "获奖概率")
    @TableField("probability")
    private String probability;

    /** 图片 */
    @Excel(name = "图片")
    @TableField("img")
    private String img;

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
    public void setRelatedProducts(String relatedProducts)
    {
        this.relatedProducts = relatedProducts;
    }

    public String getRelatedProducts()
    {
        return relatedProducts;
    }
    public void setProbability(String probability)
    {
        this.probability = probability;
    }

    public String getProbability()
    {
        return probability;
    }
    public void setImg(String img)
    {
        this.img = img;
    }

    public String getImg()
    {
        return img;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("relatedProducts", getRelatedProducts())
            .append("probability", getProbability())
            .append("img", getImg())
            .toString();
    }
}

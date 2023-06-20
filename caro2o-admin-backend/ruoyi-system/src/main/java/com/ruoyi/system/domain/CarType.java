package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 car_type
 * 
 * @author ruoyi
 * @date 2022-08-22
 */
public class CarType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 车型id */
    private Long id;

    /** 车辆类型 */
    @Excel(name = "车辆类型")
    private String carName;

    /** 车系 */
    @Excel(name = "车系")
    private String carSeries;

    /** 图片 */
    @Excel(name = "图片")
    private String image;

    /** 建议价格 */
    @Excel(name = "建议价格")
    private BigDecimal price;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCarName(String carName) 
    {
        this.carName = carName;
    }

    public String getCarName() 
    {
        return carName;
    }
    public void setCarSeries(String carSeries) 
    {
        this.carSeries = carSeries;
    }

    public String getCarSeries() 
    {
        return carSeries;
    }
    public void setImage(String image) 
    {
        this.image = image;
    }

    public String getImage() 
    {
        return image;
    }
    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("carName", getCarName())
            .append("carSeries", getCarSeries())
            .append("image", getImage())
            .append("price", getPrice())
            .toString();
    }
}

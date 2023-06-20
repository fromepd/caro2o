package com.ruoyi.report.domain;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ResultBean {
    //统计日期
    String statisticalDate;
    //统计收入
    BigDecimal statisticalAmount;
    //统计订单数量
    Long orderCount;
    //电话号码
    private String customerPhone;
}

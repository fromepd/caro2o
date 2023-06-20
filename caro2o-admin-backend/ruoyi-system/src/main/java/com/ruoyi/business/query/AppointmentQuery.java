package com.ruoyi.business.query;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author Eastern unbeaten
 * @Email chenshiyun2011@163.com
 * @Date 2022/9/3 5:13 下午
 */
@Setter
@Getter
public class AppointmentQuery extends QueryObject {
    /**
     * 模糊搜索参数
     */
   private String keyword;
    /**
     * 联系电话
     */
   private String customerPhone;
    /**
     * 车牌
     */
   private String licensePlate;
}

package com.ruoyi.system.domain.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Dengyangbo
 * @date 2022/9/1 16:35
 * @email 13005100647@163.com
 * @description 套餐审核表单提交信息
 */
@Getter
@Setter
@NoArgsConstructor
public class ServiceItemAuditVo {

    Long id;

    Long showOwnerId;

    Long financeId;

    String info;
}

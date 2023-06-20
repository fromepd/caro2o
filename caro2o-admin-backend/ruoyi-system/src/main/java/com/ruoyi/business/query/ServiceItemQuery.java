package com.ruoyi.business.query;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by wolfcode-lanxw
 */
@Setter
@Getter
public class ServiceItemQuery extends QueryObject {
    private String name;
    private Integer carPackage;
    private Integer serviceCatalog;
    private Integer auditStatus;
    private Integer saleStatus;
}

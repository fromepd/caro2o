package com.ruoyi.business.query;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 分页+条件查询基类
 */
@Setter
@Getter
public class QueryObject {
    private int pageNum = 1;
    private int pageSize = 10;

    private Date beginTime;
    private Date endTime;

}

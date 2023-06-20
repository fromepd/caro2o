package com.ruoyi.report.service;

import com.github.pagehelper.PageInfo;
import com.ruoyi.report.domain.QueryObject;
import com.ruoyi.report.domain.ResultBean;

import java.util.List;

public interface IReportService {
    /**
     * 店铺收入：
     * 按照年月日的方式进行统计店铺收入
     * -- 2.5.1.店铺收入汇总
     * -- 统计店铺收入,按照年月月周日的方式进行统计店铺收入,
     * -- 计算规则是已经支付过的结算单,
     * -- 报表字段-统计日期,统计金额
     * -- 高级搜索条件,支持结算单时间 开始--结束查询
     * -- 支持年,月,日方式切换分组查询
     * @return
     */
    PageInfo<ResultBean> storeIncome(QueryObject queryObject);

    /**
     * 店铺消费列表
     * @param queryObject
     * @return
     */
    PageInfo<ResultBean> shopConsumption( QueryObject queryObject);

    /**
     * 客户消费报表
     * @return
     */
    PageInfo<ResultBean> customerConsumeReport(QueryObject queryObject);
}

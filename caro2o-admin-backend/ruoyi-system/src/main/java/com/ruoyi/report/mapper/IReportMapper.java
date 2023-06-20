package com.ruoyi.report.mapper;



import com.ruoyi.report.domain.QueryObject;
import com.ruoyi.report.domain.ResultBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IReportMapper {
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
    List<ResultBean> storeIncome(@Param("queryObject") QueryObject queryObject);
    /* 由于日常统计需要,需要对店铺消费单统计,分别是预约单,结算单,统计下单数量
    报表字段-店铺名称,统计日期,订单数量
    高级搜索条件,支持结算时间 开始--结束查询,支持预约单,结算单下拉切换
     支持年,月,日,周方式切换分组查询
     */
    List<ResultBean> shopConsumption(@Param("queryObject") QueryObject queryObject);

    /**
     * 客户消费报表
     * @return
     */
    List<ResultBean> customerConsumeReport(@Param("queryObject") QueryObject queryObject);
}

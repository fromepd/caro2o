package com.ruoyi.web.controller.report;

import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.report.domain.QueryObject;
import com.ruoyi.report.domain.ResultBean;
import com.ruoyi.report.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    IReportService statementService;

    /**
     * 店铺收入列表
     * @param queryObject
     * @return
     */
    @GetMapping("/storeincome")
    public AjaxResult storeIncome(QueryObject queryObject){
        PageInfo<ResultBean> resultBeanPageInfo = statementService.storeIncome(queryObject);
        return AjaxResult.success(resultBeanPageInfo);
    }

    /*店铺收入列表*/
    @GetMapping("/shopConsumption")
    public AjaxResult shopConsumption(QueryObject queryObject){
        PageInfo<ResultBean> resultBeans = statementService.shopConsumption(queryObject);
        return AjaxResult.success(resultBeans);
    }


    /*用户列表*/
    @GetMapping("/customerConsumeReport")
    public AjaxResult customerConsumeReport(QueryObject queryObject){
        PageInfo<ResultBean> resultBeans  = statementService.customerConsumeReport(queryObject);
        return AjaxResult.success(resultBeans);
    }
}

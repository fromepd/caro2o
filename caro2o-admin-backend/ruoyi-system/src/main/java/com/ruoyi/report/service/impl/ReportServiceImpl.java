package com.ruoyi.report.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.report.domain.QueryObject;
import com.ruoyi.report.domain.ResultBean;
import com.ruoyi.report.mapper.IReportMapper;
import com.ruoyi.report.service.IReportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ReportServiceImpl implements IReportService {
    @Resource
    IReportMapper iReportMapper;

    @Override
    public PageInfo<ResultBean> storeIncome(QueryObject queryObject) {
        //设置分页
        PageHelper.startPage(queryObject.getPageNum(),queryObject.getPageSize());
        //获取信息
        List<ResultBean> resultBeans = iReportMapper.storeIncome(queryObject);
        //返回信息
        return new PageInfo<>(resultBeans);
    }

    @Override
    public PageInfo<ResultBean> shopConsumption(QueryObject queryObject) {
        //设置分页
        PageHelper.startPage(queryObject.getPageNum(),queryObject.getPageSize());
        //获取信息
        List<ResultBean> resultBeans = iReportMapper.shopConsumption(queryObject);
        //返回信息
        return new PageInfo<>(resultBeans);
    }

    @Override
    public PageInfo<ResultBean> customerConsumeReport(QueryObject queryObject) {
        //设置分页
        PageHelper.startPage(queryObject.getPageNum(),queryObject.getPageSize());
        //获取信息
        List<ResultBean> resultBeans = iReportMapper.customerConsumeReport(queryObject);
        //返回信息
        return new PageInfo<>(resultBeans);
    }
}

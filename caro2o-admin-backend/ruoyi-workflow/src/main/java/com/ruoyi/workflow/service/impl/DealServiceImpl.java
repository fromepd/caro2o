package com.ruoyi.workflow.service.impl;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.workflow.domain.Purse;
import io.swagger.annotations.Authorization;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.workflow.mapper.DealMapper;
import com.ruoyi.workflow.domain.Deal;
import com.ruoyi.workflow.service.IDealService;

/**
 * 交易明细Service业务层处理
 *
 * @author wby
 * @date 2023-02-01
 */
@Service
public class DealServiceImpl extends ServiceImpl<DealMapper, Deal> implements IDealService {

    @Autowired
    private IDealService dealService;
    @Autowired
    private ISysUserService userService;

    /**
     * 查询交易明细列表
     *
     * @param deal 交易明细
     * @return 交易明细
     */
    @Override
    public List<Deal> selectDealList(Deal deal) {
        return getBaseMapper().selectDealList(deal);
    }

    @Override
    public boolean save(Deal entity) {
        String idStr = IdWorker.getIdStr();
        entity.setId(idStr);
        SysUser sysUser = userService.selectUserById(entity.getUserId());
        entity.setUsername(sysUser.getNickName());
        LoginUser loginUser = SecurityUtils.getLoginUser();
        entity.setOperate(String.valueOf(loginUser.getUserId()));
        entity.setOperationTime(new Date());
        return super.save(entity);
    }
}

package com.ruoyi.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.workflow.domain.Deal;
import com.ruoyi.workflow.domain.Purse;
import com.ruoyi.workflow.mapper.PurseMapper;
import com.ruoyi.workflow.service.IDealService;
import com.ruoyi.workflow.service.IPurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 钱包管理Service业务层处理
 *
 * @author wby
 * @date 2023-02-01
 */
@Service
public class PurseServiceImpl extends ServiceImpl<PurseMapper, Purse> implements IPurseService {

    @Autowired
    private IDealService dealService;

    @Autowired
    private ISysUserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IPurseService purseService;

    /**
     * 查询钱包管理列表
     *
     * @param purse 钱包管理
     * @return 钱包管理
     */
    @Override
    public List<Purse> selectPurseList(Purse purse) {
        List<Purse> list = list(new LambdaQueryWrapper<Purse>()
                .eq(purse.getRulePurse() != null, Purse::getRulePurse, purse.getRulePurse())
                .eq(purse.getBalance() > 0, Purse::getBalance, purse.getBalance())
        );
        for (Purse purse1 : list) {
            SysUser sysUser = userService.selectUserById(purse1.getUserId());
            purse1.setName(sysUser.getUserName());
        }
        return list;
    }

    @Override
    public List<SysUser> selectUser() {
        List<Purse> list = super.list();
        List<Long> collect = list.stream().map(Purse::getUserId).collect(Collectors.toList());
        return userService.selectUserByIds(collect);
    }

    @Override
    public void frost(Long userId) {
        LambdaUpdateWrapper<Purse> wq = new LambdaUpdateWrapper<>();
        wq.eq(Purse::getUserId, userId).set(Purse::getRulePurse, 1);
        super.update(wq);
    }

    @Override
    public void skype(Deal deal, HttpServletRequest httpServletRequest) {
        LoginUser loginUser = tokenService.getLoginUser(httpServletRequest);
        SysUser user = userService.selectUserById(loginUser.getUserId());
        Purse purse = purseService.getOne(new LambdaQueryWrapper<Purse>().eq(Purse::getUserId, deal.getUserId()));
        deal.setOperate(user.getUserName());
        deal.setOperationTime(new Date());
        long id = IdWorker.getId();
        deal.setId(id + "");
        deal.setUserId(user.getUserId());
        Long newMoney = 0L;
        Long revenue = purse.getRevenue();
        Long expenditure = purse.getExpenditure();
        if (deal.getType() == 0) {
            newMoney = purse.getBalance() + deal.getMoney();
            revenue = purse.getRevenue() + deal.getMoney();
        } else {
            if (purse.getBalance() < deal.getMoney()){
                throw new RuntimeException("余额不足！");
            }
            expenditure = purse.getExpenditure() + deal.getMoney();
            newMoney = purse.getBalance() - deal.getMoney();
        }
        purse.setBalance(newMoney);
        purse.setRevenue(revenue);
        purse.setExpenditure(expenditure);
        dealService.save(deal);
        super.updateById(purse);
    }
}

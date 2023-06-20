package com.ruoyi.workflow.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.workflow.domain.Deal;
import com.ruoyi.workflow.domain.Purse;

import javax.servlet.http.HttpServletRequest;

/**
 * 钱包管理Service接口
 *
 * @author wby
 * @date 2023-02-01
 */
public interface IPurseService extends IService<Purse> {

    /**
     * 查询钱包管理列表
     *
     * @param purse 钱包管理
     * @return 钱包管理集合
     */
    public List<Purse> selectPurseList(Purse purse);

    List<SysUser> selectUser();

    void frost(Long userId);

    void skype(Deal purse,  HttpServletRequest httpServletRequest);
}

package com.ruoyi.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.StoreInfo;
import com.ruoyi.system.mapper.StoreInfoMapper;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.workflow.service.IStoreInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 门店信息Service业务层处理
 *
 * @author wang
 * @date 2022-11-24
 */
@Service
public class StoreInfoServiceImpl extends ServiceImpl<StoreInfoMapper, StoreInfo> implements IStoreInfoService {


    @Autowired
    private ISysUserService userService;
    /**
     * 查询门店信息列表
     *
     * @param storeInfo 门店信息
     * @return 门店信息
     */
    @Override
    public List<StoreInfo> selectStoreInfoList(StoreInfo storeInfo) {

        return getBaseMapper().selectStoreInfoList(storeInfo);
    }



    @Override
    public boolean removeBatchById(Long[] ids) {
        if (ids.length == 0) {
            return false;
        }
        return getBaseMapper().removeBatchById(ids);
    }

    @Override
    public boolean save(StoreInfo entity) {
        Long managerId = entity.getManagerId();
        SysUser sysUser = userService.selectUserById(managerId);
        entity.setManagerName(sysUser.getNickName());
        entity.setManagerTel(sysUser.getPhonenumber());
        int insert = getBaseMapper().insert(entity);
        return insert > 0;
    }
 
    @Override
    public boolean updateById(StoreInfo entity) {
        Long managerId = entity.getManagerId();
        SysUser sysUser = userService.selectUserById(managerId);
        entity.setManagerName(sysUser.getNickName());
        entity.setManagerTel(sysUser.getPhonenumber());
        int updateById = getBaseMapper().updateById(entity);
        return updateById > 0;
    }
}

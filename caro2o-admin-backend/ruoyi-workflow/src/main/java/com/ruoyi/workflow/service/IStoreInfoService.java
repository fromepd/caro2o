package com.ruoyi.workflow.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.StoreInfo;

/**
 * 门店信息Service接口
 *
 * @author wang
 * @date 2022-11-24
 */
public interface IStoreInfoService extends IService<StoreInfo> {

    /**
     * 查询门店信息列表
     *
     * @param storeInfo 门店信息
     * @return 门店信息集合
     */
    public List<StoreInfo> selectStoreInfoList(StoreInfo storeInfo);



    boolean removeBatchById(Long[] ids);






}

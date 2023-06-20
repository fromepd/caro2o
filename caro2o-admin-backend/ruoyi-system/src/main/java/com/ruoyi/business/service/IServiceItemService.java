package com.ruoyi.business.service;

import com.ruoyi.business.domain.ServiceItem;
import com.ruoyi.system.domain.vo.ServiceItemAuditVo;
import com.ruoyi.business.query.ServiceItemQuery;

import java.util.List;

/**
 * 养修服务单项服务接口
 */
public interface IServiceItemService {

    /**
     * 分页
     * @param qo
     * @return
     */
    List<ServiceItem> query(ServiceItemQuery qo);

    /**
     * 查单个
     * @param id
     * @return
     */
    ServiceItem get(Long id);
    /**
     * 保存
     * @param serviceItem
     */
    void save(ServiceItem serviceItem);
    /**
     * 更新
     * @param serviceItem
     */
    void update(ServiceItem serviceItem);
    /**
     *  批量删除
     * @param ids
     */
    void deleteBatch(String ids);

    /**
     * 查询全部
     * @return
     */
    List<ServiceItem> list();

    /**
     * 上架功能
     * @param id
     */
    void saleOn(Long id);

    /**
     * 下架功能
     * @param id
     */
    void saleOff(Long id);

    void changeStatus(Long serviceItemId, Integer status);

    /**
     * 审核记录下重新修改表单
     *
     * @param serviceItem
     */
    void updateForAudit(ServiceItem serviceItem);
}

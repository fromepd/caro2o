package com.ruoyi.business.service.impl;


import com.ruoyi.business.domain.ServiceItem;
import com.ruoyi.business.mapper.ServiceItemMapper;
import com.ruoyi.business.query.ServiceItemQuery;
import com.ruoyi.business.service.IServiceItemService;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author 20463
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ServiceItemServiceImpl implements IServiceItemService {

    @Resource
    private ServiceItemMapper serviceItemMapper;


    @Override
    public List<ServiceItem> query(ServiceItemQuery qo) {
        return serviceItemMapper.selectForList(qo);
    }


    @Override
    public void save(ServiceItem serviceItem) {
        // 对于前台传入的参数,一般需要谨慎处理.
        ServiceItem newObj = new ServiceItem();
        newObj.setName(serviceItem.getName());
        newObj.setOriginalPrice(serviceItem.getOriginalPrice());
        newObj.setDiscountPrice(serviceItem.getDiscountPrice());
        newObj.setCarPackage(serviceItem.getCarPackage());
        newObj.setInfo(serviceItem.getInfo());
        newObj.setCreateTime(new Date());
        newObj.setServiceCatalog(serviceItem.getServiceCatalog());
        // 判断是否套餐
        if (ServiceItem.CARPACKAGE_YES.equals(newObj.getCarPackage())) {
            // 设置状态为初始化
            newObj.setAuditStatus(ServiceItem.AUDITSTATUS_INIT);
        } else {
            // 设置状态为无需审核
            newObj.setAuditStatus(ServiceItem.AUDITSTATUS_NO_REQUIRED);
        }
        serviceItemMapper.insert(newObj);
    }

    @Override
    public ServiceItem get(Long id) {
        return serviceItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateForAudit(ServiceItem serviceItem) {
        ServiceItem oldObj = this.get(serviceItem.getId());
        // 如果不是重新跳转， 不允许修改
        if (!ServiceItem.AUDITSTATUS_REPLY.equals(oldObj.getAuditStatus())) {
            throw new BusinessException("非法操作");
        }
        // 更新属性
        this.doUpdate(oldObj,serviceItem);

        // 更新审核记录


    }

    /**
     * 更新服务的方法
     * @param serviceItemIndb 数据库查询对象
     * @param serviceItemFronted 前端传入的对象
     */
    private void doUpdate(ServiceItem serviceItemIndb,ServiceItem serviceItemFronted) {
        // 将前台的属性设置到当前对象中
        serviceItemIndb.setName(serviceItemFronted.getName());
        serviceItemIndb.setOriginalPrice(serviceItemFronted.getOriginalPrice());
        serviceItemIndb.setDiscountPrice(serviceItemFronted.getDiscountPrice());
        serviceItemIndb.setServiceCatalog(serviceItemFronted.getServiceCatalog());
        serviceItemIndb.setInfo(serviceItemFronted.getInfo());
        serviceItemMapper.updateByPrimaryKey(serviceItemIndb);
    }

    @Override
    public void update(ServiceItem serviceItem) {
        ServiceItem oldObj = this.get(serviceItem.getId());
        // 处于上架状态，处于审核中的状态不能进行修改.
        // 处于上架状态
        if (ServiceItem.SALESTATUS_ON.equals(oldObj.getSaleStatus())
                // 处于审核中
                || ServiceItem.AUDITSTATUS_AUDITING.equals(oldObj.getAuditStatus())) {
            throw new BusinessException("非法操作");
        }
        // 如果是套餐，如果已经审核通过，然后进行修改,变成初始化
        if (ServiceItem.AUDITSTATUS_APPROVED.equals(oldObj.getAuditStatus())) {
            oldObj.setAuditStatus(ServiceItem.AUDITSTATUS_INIT);
        }
        // 把前台的属性设置当当前对象中
        oldObj.setName(serviceItem.getName());
        oldObj.setOriginalPrice(serviceItem.getOriginalPrice());
        oldObj.setDiscountPrice(serviceItem.getDiscountPrice());
        oldObj.setServiceCatalog(serviceItem.getServiceCatalog());
        oldObj.setInfo(serviceItem.getInfo());
        serviceItemMapper.updateByPrimaryKey(oldObj);
    }

    @Override
    public void deleteBatch(String ids) {
        Long[] dictIds = Convert.toLongArray(ids);
        for (Long dictId : dictIds) {
            serviceItemMapper.deleteByPrimaryKey(dictId);
        }
    }

    @Override
    public List<ServiceItem> list() {
        return serviceItemMapper.selectAll();
    }

    @Override
    public void saleOn(Long id) {
        // 合理化校验
        ServiceItem oldObj = this.get(id);
        if (oldObj != null) {
            // 1.如果处于上架状态,不需要做事情
            if (ServiceItem.SALESTATUS_ON.equals(oldObj.getSaleStatus())) {
                return;
            }
            // 2.如果套餐且处于非审核通过,不允许进行上架操作
            // 是套餐
            if (ServiceItem.CARPACKAGE_YES.equals(oldObj.getCarPackage())
                    && !ServiceItem.AUDITSTATUS_APPROVED.equals(oldObj.getAuditStatus())) {
                throw new BusinessException("未审核通过套餐不允许上架");
            }
            // 其他情况都可以进行上架
            serviceItemMapper.updateSaleStatus(id, ServiceItem.SALESTATUS_ON);
        }
    }

    @Override
    public void saleOff(Long id) {
        serviceItemMapper.updateSaleStatus(id, ServiceItem.SALESTATUS_OFF);
    }

    @Override
    public void changeStatus(Long serviceItemId, Integer status) {
        serviceItemMapper.changeAuditStatus(serviceItemId, status);
    }
}

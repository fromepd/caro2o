package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.CarTypeMapper;
import com.ruoyi.system.domain.CarType;
import com.ruoyi.system.service.ICarTypeService;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-08-22
 */
@Service
public class CarTypeServiceImpl implements ICarTypeService 
{
    @Autowired
    private CarTypeMapper carTypeMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public CarType selectCarTypeById(Long id)
    {
        return carTypeMapper.selectCarTypeById(id);
    }

    /**0
     * 查询【请填写功能名称】列表
     * 
     * @param carType 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CarType> selectCarTypeList(CarType carType)
    {
        return carTypeMapper.selectCarTypeList(carType);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param carType 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCarType(CarType carType)
    {
        return carTypeMapper.insertCarType(carType);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param carType 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCarType(CarType carType)
    {
        return carTypeMapper.updateCarType(carType);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteCarTypeByIds(Long[] ids)
    {
        return carTypeMapper.deleteCarTypeByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteCarTypeById(Long id)
    {
        return carTypeMapper.deleteCarTypeById(id);
    }

    @Override
    public List<CarType> selectTypeName() {
        return carTypeMapper.selectTypeName();
    }
}

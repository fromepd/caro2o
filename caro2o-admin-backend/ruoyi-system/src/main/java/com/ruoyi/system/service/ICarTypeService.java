package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.CarType;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2022-08-22
 */
public interface ICarTypeService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public CarType selectCarTypeById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param carType 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CarType> selectCarTypeList(CarType carType);

    /**
     * 新增【请填写功能名称】
     * 
     * @param carType 【请填写功能名称】
     * @return 结果
     */
    public int insertCarType(CarType carType);

    /**
     * 修改【请填写功能名称】
     * 
     * @param carType 【请填写功能名称】
     * @return 结果
     */
    public int updateCarType(CarType carType);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteCarTypeByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteCarTypeById(Long id);

    /**
     * 查询出所有车型
     * @return
     */
    List<CarType> selectTypeName();
}

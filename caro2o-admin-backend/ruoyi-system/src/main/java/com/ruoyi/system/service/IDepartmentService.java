package com.ruoyi.system.service;

import com.ruoyi.system.domain.Department;

import java.util.List;

/**
 * 部门信息 服务层
 *
 * @author wang
 */
public interface IDepartmentService {
    /**
     * 查询部门信息集合
     *
     * @param dept 部门信息
     * @return 部门列表
     */
    List<Department> selectDeptList(Department dept);

    /**
     * 查询所有部门
     *
     * @return 部门列表
     */
    List<Department> selectDeptAll();

    /**
     * 通过部门ID查询部门信息
     *
     * @param deptId 部门ID
     * @return 角色对象信息
     */
    Department selectDeptById(Long deptId);

    /**
     * 删除部门信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    int deleteDeptById(Long deptId);

    /**
     * 批量删除部门信息
     *
     * @param deptIds 需要删除的部门ID
     * @return 结果
     */
    int deleteDeptByIds(Long[] deptIds);

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    int insertDept(Department dept);

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    int updateDept(Department dept);
}

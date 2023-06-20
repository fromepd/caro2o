package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.Department;

import java.util.List;

/**
 * 部门信息 数据层
 *
 * @author wang
 */
public interface DepartmentMapper {
    /**
     * 查询部门数据集合
     *
     * @param dept 部门信息
     * @return 部门数据集合
     */
    public List<Department> selectDeptList(Department dept);

    /**
     * 查询所有部门
     *
     * @return 部门列表
     */
    public List<Department> selectDeptAll();

    /**
     * 通过部门ID查询部门信息
     *
     * @param deptId 部门ID
     * @return 角色对象信息
     */
    public Department selectDeptById(Long deptId);

    /**
     * 删除部门信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int deleteDeptById(Long deptId);

    /**
     * 批量删除部门信息
     *
     * @param deptIds 需要删除的部门ID
     * @return 结果
     */
    public int deleteDeptByIds(Long[] deptIds);

    /**
     * 修改部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(Department dept);

    /**
     * 新增部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int insertDept(Department dept);
}

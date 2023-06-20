package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.Department;
import com.ruoyi.system.mapper.DepartmentMapper;
import com.ruoyi.system.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门信息 服务层处理
 *
 * @author wang
 */
@Service
public class DepartmentServiceImpl implements IDepartmentService {
    @Autowired
    private DepartmentMapper deptMapper;

    /**
     * 查询部门信息集合
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    public List<Department> selectDeptList(Department dept) {
        return deptMapper.selectDeptList(dept);
    }

    /**
     * 查询所有部门
     *
     * @return 部门列表
     */
    @Override
    public List<Department> selectDeptAll() {
        return deptMapper.selectDeptAll();
    }

    /**
     * 通过部门ID查询部门信息
     *
     * @param deptId 部门ID
     * @return 角色对象信息
     */
    @Override
    public Department selectDeptById(Long deptId) {
        return deptMapper.selectDeptById(deptId);
    }

    /**
     * 删除部门信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteDeptById(Long deptId) {
        return deptMapper.deleteDeptById(deptId);
    }

    /**
     * 批量删除部门信息
     *
     * @param deptIds 需要删除的部门ID
     * @return 结果
     */
    @Override
    public int deleteDeptByIds(Long[] deptIds) {
        return deptMapper.deleteDeptByIds(deptIds);
    }

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int insertDept(Department dept) {
        return deptMapper.insertDept(dept);
    }

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int updateDept(Department dept) {
        return deptMapper.updateDept(dept);
    }
}

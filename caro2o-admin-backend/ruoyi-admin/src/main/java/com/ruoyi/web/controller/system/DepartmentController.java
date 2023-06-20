package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.Department;
import com.ruoyi.system.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 部门信息操作处理
 *
 * @author wang
 */
@RestController
@RequestMapping("/system/department")
public class DepartmentController extends BaseController {
    @Autowired
    private IDepartmentService deptService;

    /**
     * 获取部门列表
     */
    @PreAuthorize("@ss.hasPermi('system:department:list')")
    @GetMapping("/list")
    public TableDataInfo list(Department dept) {
        startPage();
        List<Department> list = deptService.selectDeptList(dept);
        return getDataTable(list);
    }

    @Log(title = "部门管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:department:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, Department dept) {
        List<Department> list = deptService.selectDeptList(dept);
        ExcelUtil<Department> util = new ExcelUtil<Department>(Department.class);
        util.exportExcel(response, list, "部门数据");
    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:department:query')")
    @GetMapping(value = "/{deptId}")
    public AjaxResult getInfo(@PathVariable Long deptId) {
        return AjaxResult.success(deptService.selectDeptById(deptId));
    }

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('system:department:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Department dept) {
        return toAjax(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @PreAuthorize("@ss.hasPermi('system:department:edit')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Department dept) {
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @PreAuthorize("@ss.hasPermi('system:department:remove')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptIds}")
    public AjaxResult remove(@PathVariable Long[] deptIds) {
        return toAjax(deptService.deleteDeptByIds(deptIds));
    }

    /**
     * 获取部门选择框列表
     */
    @GetMapping("/optionselect")
    public AjaxResult optionselect() {
        List<Department> depts = deptService.selectDeptAll();
        return AjaxResult.success(depts);
    }
}

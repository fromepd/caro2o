package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.CarType;
import com.ruoyi.system.service.ICarTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 【请填写功能名称】Controller
 *
 * @author ruoyi
 * @date 2022-08-22
 */
@RestController
@RequestMapping("/system/type")
public class CarTypeController extends BaseController {
    @Autowired
    private ICarTypeService carTypeService;

    /**
     * 查询【车型】列表
     */
    @PreAuthorize("@ss.hasPermi('system:type:list')")
    @GetMapping()
    public TableDataInfo list(CarType carType) {
        startPage();
        List<CarType> list = carTypeService.selectCarTypeList(carType);
        return getDataTable(list);
    }

    /**
     * 导出【车型】列表
     */
    @PreAuthorize("@ss.hasPermi('system:type:export')")
    @Log(title = "【查看车型】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CarType carType) {
        List<CarType> list = carTypeService.selectCarTypeList(carType);
        ExcelUtil<CarType> util = new ExcelUtil<CarType>(CarType.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【车型】详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:type:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(carTypeService.selectCarTypeById(id));
    }

    /**
     * 新增【车型】
     */
    @PreAuthorize("@ss.hasPermi('system:type:add')")
    @Log(title = "【新增车型】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CarType carType) {
        return toAjax(carTypeService.insertCarType(carType));
    }

    /**
     * 修改【车型】
     */
    @PreAuthorize("@ss.hasPermi('system:type:edit')")
    @Log(title = "【修改车型】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CarType carType) {
        return toAjax(carTypeService.updateCarType(carType));
    }

    /**
     * 删除【车型】
     */
    @PreAuthorize("@ss.hasPermi('system:type:remove')")
    @Log(title = "【删除车型】", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(carTypeService.deleteCarTypeByIds(ids));
    }


    @GetMapping("/typeName")
    @Anonymous
    public AjaxResult selectTypeName() {
        List<CarType> list = carTypeService.selectTypeName();
        return AjaxResult.success(list);
    }

    @GetMapping("/typeName/{typeName}")
    @Anonymous
    public AjaxResult selectByType(@PathVariable String typeName) {
        CarType carType = new CarType();
        carType.setCarName(typeName);
        List<CarType> list = carTypeService.selectCarTypeList(carType);
        return AjaxResult.success(list);
    }

}

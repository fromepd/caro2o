package com.ruoyi.workflow.controller;

import java.util.List;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.workflow.domain.Deal;
import com.ruoyi.workflow.service.IDealService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.workflow.domain.Purse;
import com.ruoyi.workflow.service.IPurseService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 钱包管理Controller
 *
 * @author wby
 * @date 2023-02-01
 */
@RestController
@RequestMapping("/workflow/purse")
public class PurseController extends BaseController
{
    @Autowired
    private IPurseService purseService;

    @Autowired
    private IDealService dealService;

    /**
     * 查询钱包管理列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:purse:list')")
    @GetMapping("/list")
    public TableDataInfo list(Purse purse)
    {
        startPage();
        List<Purse> list = purseService.selectPurseList(purse);
        return getDataTable(list);
    }

    /**
     * 导出钱包管理列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:purse:export')")
    @Log(title = "钱包管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Purse purse)
    {
        List<Purse> list = purseService.selectPurseList(purse);
        ExcelUtil<Purse> util = new ExcelUtil<Purse>(Purse.class);
        util.exportExcel(response, list, "钱包管理数据");
    }

    /**
     * 获取钱包管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:purse:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(purseService.getById(id));
    }

    /**
     * 新增钱包管理
     */
    @PreAuthorize("@ss.hasPermi('workflow:purse:add')")
    @Log(title = "钱包管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Purse purse)
    {
        return toAjax(purseService.save(purse));
    }

    /**
     * 修改钱包管理
     */
    @PreAuthorize("@ss.hasPermi('workflow:purse:edit')")
    @Log(title = "钱包管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Purse purse) {
        return toAjax(purseService.updateById(purse));
    }

    /**
     * 删除钱包管理
     */
    @PreAuthorize("@ss.hasPermi('workflow:purse:remove')")
    @Log(title = "钱包管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(purseService.removeBatchByIds(Arrays.asList(ids)));
    }

    @GetMapping("/users")
    public AjaxResult users() {
        List<SysUser> user = purseService.selectUser();
        return AjaxResult.success(user);
    }

    @PostMapping("/skype")
    public AjaxResult skype(@RequestBody Deal purse, HttpServletRequest httpServletRequest) {
        purseService.skype(purse, httpServletRequest);
        return AjaxResult.success();
    }

    @PostMapping("/frost")
    public AjaxResult frost(Long userId){
        purseService.frost(userId);
        return AjaxResult.success();
    }
}

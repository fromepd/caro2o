package com.ruoyi.web.controller.workflow;

import java.util.List;
import java.util.Arrays;
import javax.servlet.http.HttpServletResponse;

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
import com.ruoyi.workflow.domain.BpmnNode;
import com.ruoyi.workflow.service.IBpmnNodeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 流程定义节点Controller
 *
 * @author wang
 * @date 2022-11-17
 */
@RestController
@RequestMapping("/workflow/bpmnNode")
public class BpmnNodeController extends BaseController {
    @Autowired
    private IBpmnNodeService bpmnNodeService;

    /**
     * 查询流程定义节点列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:bpmnNode:list')")
    @GetMapping("/list")
    public AjaxResult list(BpmnNode bpmnNode) {
        List<BpmnNode> list = bpmnNodeService.selectBpmnNodeList(bpmnNode);
        return AjaxResult.success(list);
    }

    /**
     * 导出流程定义节点列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:bpmnNode:export')")
    @Log(title = "流程定义节点", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BpmnNode bpmnNode) {
        List<BpmnNode> list = bpmnNodeService.selectBpmnNodeList(bpmnNode);
        ExcelUtil<BpmnNode> util = new ExcelUtil<BpmnNode>(BpmnNode.class);
        util.exportExcel(response, list, "流程定义节点数据");
    }

    /**
     * 获取流程定义节点详细信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:bpmnNode:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(bpmnNodeService.getById(id));
    }

    /**
     * 新增流程定义节点
     */
    @PreAuthorize("@ss.hasPermi('workflow:bpmnNode:add')")
    @Log(title = "流程定义节点", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BpmnNode bpmnNode) {
        return toAjax(bpmnNodeService.save(bpmnNode));
    }

    /**
     * 修改流程定义节点
     */
    @PreAuthorize("@ss.hasPermi('workflow:bpmnNode:edit')")
    @Log(title = "流程定义节点", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BpmnNode bpmnNode) {
        return toAjax(bpmnNodeService.updateById(bpmnNode));
    }

    /**
     * 删除流程定义节点
     */
    @PreAuthorize("@ss.hasPermi('workflow:bpmnNode:remove')")
    @Log(title = "流程定义节点", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bpmnNodeService.removeBatchByIds(Arrays.asList(ids)));
    }
}

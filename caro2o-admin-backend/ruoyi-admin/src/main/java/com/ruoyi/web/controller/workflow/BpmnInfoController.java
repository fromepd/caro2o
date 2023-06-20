package com.ruoyi.web.controller.workflow;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.workflow.domain.BpmnInfo;
import com.ruoyi.workflow.service.IBpmnInfoService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;


/**
 * 流程定义 Controller
 *
 * @author wang
 * @date 2022-11-14
 */
@RestController
@RequestMapping("/workflow/bpmnInfo")
public class BpmnInfoController extends BaseController {
    @Autowired
    private IBpmnInfoService bpmnInfoService;

    /**
     * 查询流程定义列表
     */
    @PreAuthorize("@ss.hasPermi('business:bpmnInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(BpmnInfo bpmnInfo) {
        startPage();
        List<BpmnInfo> list = bpmnInfoService.selectBpmnInfoList(bpmnInfo);
        return getDataTable(list);
    }

    /**
     * 查看流程图/流程文件
     */
    @PreAuthorize("@ss.hasPermi('business:bpmnInfo:view')")
    @GetMapping("/resources/{id}")
    public void viewResource(@PathVariable Long id, String type, HttpServletResponse resp) throws IOException {
        // TODO 参数校验: 非空判断
        InputStream resource = bpmnInfoService.getResourceByType(id, type);
        // 将资源输入流拷贝到响应输出流
        IOUtils.copy(resource, resp.getOutputStream());
    }

    /**
     * 删除流程定义
     */
    @PreAuthorize("@ss.hasPermi('business:bpmnInfo:remove')")
    @Log(title = "删除流程定义", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        if (ids == null || ids.length == 0) {
            return error("请选择要删除的流程定义");
        }
        return toAjax(bpmnInfoService.deleteDefinition(ids));
    }

    /**
     * 支持的后缀
     */
    private static final List<String> SUPPORT_SUFFIXES = Arrays.asList("bpmn", "xml", "zip");

    @PreAuthorize("@ss.hasPermi('business:bpmnInfo:deploy')")
    @Log(title = "流程定义部署", businessType = BusinessType.INSERT)
    @PostMapping("/deploy")
    public AjaxResult deploy(MultipartFile file, BpmnInfo bpmnInfo) throws Exception {
        // 文件类型校验
        // 原始的文件名
        String filename = file.getOriginalFilename();
        // 文件后缀名
        String suffix = FileUtils.getSuffix(filename);
        if (!SUPPORT_SUFFIXES.contains(suffix)) {
            return AjaxResult.error(HttpStatus.BAD_REQUEST, "目前仅支持提交" + SUPPORT_SUFFIXES + "类型的流程文件");
        }

        bpmnInfo.setResourceName(filename);

        // 部署
        bpmnInfoService.deploy(bpmnInfo, file.getInputStream());
        return AjaxResult.success("部署成功");
    }
}

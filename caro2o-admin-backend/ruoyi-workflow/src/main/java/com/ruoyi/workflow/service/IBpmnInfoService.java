package com.ruoyi.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.workflow.domain.BpmnInfo;

import java.io.InputStream;
import java.util.List;

/**
 * 流程定义Service接口
 *
 * @author wang
 * @date 2022-11-14
 */
public interface IBpmnInfoService extends IService<BpmnInfo> {

    /**
     * 查询流程定义列表
     *
     * @param bpmnInfo 流程定义
     * @return 流程定义集合
     */
    List<BpmnInfo> selectBpmnInfoList(BpmnInfo bpmnInfo);

    /**
     * bpmn 部署
     *
     * @param bpmnInfo
     * @param bpmnFile
     */
    void deploy(BpmnInfo bpmnInfo, InputStream bpmnFile);

    /**
     * 批量删除流程定义以及流程信息
     *
     * @param ids
     * @return
     */
    boolean deleteDefinition(Long[] ids);

    /**
     * 基于流程信息id&类型获取资源流对象
     *
     * @param id
     * @param type
     * @return
     */
    InputStream getResourceByType(Long id, String type);
}

package com.ruoyi.workflow.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.workflow.domain.BpmnNode;

/**
 * 流程定义节点Service接口
 *
 * @author wang
 * @date 2022-11-17
 */
public interface IBpmnNodeService extends IService<BpmnNode> {

    /**
     * 查询流程定义节点列表
     *
     * @param bpmnNode 流程定义节点
     * @return 流程定义节点集合
     */
    public List<BpmnNode> selectBpmnNodeList(BpmnNode bpmnNode);

    List<SysUser> selectUsersByNodeKey(String nodeKey);
}

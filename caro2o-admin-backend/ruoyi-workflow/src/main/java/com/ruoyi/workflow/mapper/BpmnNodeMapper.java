package com.ruoyi.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.workflow.domain.BpmnNode;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 流程定义节点Mapper接口
 *
 * @author wang
 * @date 2022-11-17
 */
public interface BpmnNodeMapper extends BaseMapper<BpmnNode> {

    /**
     * 查询流程定义节点列表
     *
     * @param bpmnNode 流程定义节点
     * @return 流程定义节点集合
     */
    public List<BpmnNode> selectBpmnNodeList(BpmnNode bpmnNode);

    /**
     * 批量新增关系
     */
    void insertRelation(@Param("nodeId") Long nodeId, @Param("auditors") List<Long> auditors);

    /**
     * 删除关系
     */
    void deleteRelation(Long id);

    List<Long> selectAuditorsByNodeId(Serializable id);

    List<SysUser> selectUsersByNodeKey(String nodeKey);
}

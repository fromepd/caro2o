package com.ruoyi.workflow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.workflow.domain.BpmnNode;
import com.ruoyi.workflow.mapper.BpmnNodeMapper;
import com.ruoyi.workflow.service.IBpmnNodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 流程定义节点Service业务层处理
 *
 * @author wang
 * @date 2022-11-17
 */
@Service
public class BpmnNodeServiceImpl extends ServiceImpl<BpmnNodeMapper, BpmnNode> implements IBpmnNodeService {

    /**
     * 查询流程定义节点列表
     *
     * @param bpmnNode 流程定义节点
     * @return 流程定义节点
     */
    @Override
    public List<BpmnNode> selectBpmnNodeList(BpmnNode bpmnNode) {
        return getBaseMapper().selectBpmnNodeList(bpmnNode);
    }

    @Override
    public List<SysUser> selectUsersByNodeKey(String nodeKey) {
        return getBaseMapper().selectUsersByNodeKey(nodeKey);
    }

    @Override
    public BpmnNode getById(Serializable id) {
        BpmnNode bpmnNode = super.getById(id);

        // 基于 bpmnNodeId 查询所有用户
        List<Long> auditors = getBaseMapper().selectAuditorsByNodeId(id);
        bpmnNode.setAuditors(auditors);

        return bpmnNode;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(BpmnNode entity) {
        boolean save = super.save(entity);
        if (!CollectionUtils.isEmpty(entity.getAuditors())) {
            // 批量新增审核人员列表
            getBaseMapper().insertRelation(entity.getId(), entity.getAuditors());
        }
        return save;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateById(BpmnNode entity) {
        // 更新节点信息
        boolean update = super.updateById(entity);
        // 将中间表中所有数据删除
        getBaseMapper().deleteRelation(entity.getId());
        // 新增到中间表
        if (!CollectionUtils.isEmpty(entity.getAuditors())) {
            getBaseMapper().insertRelation(entity.getId(), entity.getAuditors());
        }
        return update;
    }
}

package com.ruoyi.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.workflow.domain.BpmnInfo;

import java.util.List;

/**
 * 流程定义Mapper接口
 *
 * @author wang
 * @date 2022-11-14
 */
public interface BpmnInfoMapper extends BaseMapper<BpmnInfo> {
    /**
     * 查询流程定义列表
     *
     * @param bpmnInfo 流程定义
     * @return 流程定义集合
     */
    List<BpmnInfo> selectBpmnInfoList(BpmnInfo bpmnInfo);

    /**
     * 批量删除流程定义
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBpmnInfoByIds(Long[] ids);
}

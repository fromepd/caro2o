package com.ruoyi.workflow.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 流程定义节点对象 bus_bpmn_node
 *
 * @author wang
 * @date 2022-11-17
 */
@Getter
@Setter
@TableName("bus_bpmn_node")
public class BpmnNode extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Excel(name = "主键")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;

    /** 流程信息 */
    @Excel(name = "流程信息")
    @TableField("bpmn_info_id")
    private Long bpmnInfoId;

    /** 节点 */
    @Excel(name = "节点")
    @TableField("node_key")
    private String nodeKey;

    /** 节点描述 */
    @Excel(name = "节点描述")
    @TableField("node_desc")
    private String nodeDesc;

    /** 审核人员列表 */
    @TableField(exist = false) // 让 MyBatis Plus 不要把他当做是一个列
    private List<Long> auditors;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("bpmnInfoId", getBpmnInfoId())
            .append("nodeKey", getNodeKey())
            .append("nodeDesc", getNodeDesc())
            .toString();
    }
}

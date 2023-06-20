package com.ruoyi.workflow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 流程定义对象 bus_bpmn_info
 *
 * @author wang
 * @date 2022-11-14
 */
@Getter
@Setter
@TableName("bus_bpmn_info")
public class BpmnInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 流程名称
     */
    private String bpmnLabel;

    /**
     * 流程类型
     */
    private String bpmnType;

    @TableField(exist = false)
    private String resourceName;

    /**
     * 流程定义 Key
     */
    private String processDefinitionKey;

    /** 流程部署 id */
    private String deploymentId;

    /**
     * 部署时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deployTime;

    /**
     * 版本号
     */
    private Long version;

    /**
     * 描述信息
     */
    private String info;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("bpmnLabel", getBpmnLabel())
                .append("bpmnType", getBpmnType())
                .append("processDefinitionKey", getProcessDefinitionKey())
                .append("deployTime", getDeployTime())
                .append("version", getVersion())
                .append("info", getInfo())
                .toString();
    }
}

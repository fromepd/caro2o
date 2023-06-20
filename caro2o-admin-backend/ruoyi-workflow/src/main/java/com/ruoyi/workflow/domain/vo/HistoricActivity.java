package com.ruoyi.workflow.domain.vo;


import lombok.Getter;
import lombok.Setter;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntityImpl;

/**
 * 历史活动对象
 * @author wby
 * @version 1.0
 * @date 2022/11/22 21:31
 */
@Setter
@Getter
public class HistoricActivity extends HistoricActivityInstanceEntityImpl {

    // 批注信息
    private String comment;
    // 负责人名称
    private String assigneeName;
}

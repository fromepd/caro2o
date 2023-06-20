package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 岗位表 Department
 *
 * @author wang
 */
@Getter
@Setter
public class Department extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Excel(name = "部门ID", cellType = Excel.ColumnType.NUMERIC)
    private Long id;
    @Excel(name = "部门名称")
    private String name;
    @Excel(name = "部门编号")
    private String sn;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("sn", getSn())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}

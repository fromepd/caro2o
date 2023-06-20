package com.ruoyi.workflow.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("department")
public class DepartmentTest {

    private Long id;
    private String name;
    private String sn;
}

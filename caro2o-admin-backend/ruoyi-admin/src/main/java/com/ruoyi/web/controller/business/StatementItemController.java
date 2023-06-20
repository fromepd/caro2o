package com.ruoyi.web.controller.business;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.business.domain.StatementItem;
import com.ruoyi.business.query.StatementItemQuery;
import com.ruoyi.business.service.IStatementItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by wolfcode-lanxw
 * 服务项目结算
 */
@RestController
@RequestMapping("/business/statementItem")
public class StatementItemController extends BaseController {
    @Autowired
    private IStatementItemService statementItemService;

    @GetMapping()
    @PreAuthorize("@ss.hasPermi('business:statementItem:list')")
    public TableDataInfo list(StatementItemQuery qo){
        startPage();
        return getDataTable(statementItemService.query(qo));
    }

    @PostMapping()
    @PreAuthorize("@ss.hasPermi('business:statementItem:save')")
    public AjaxResult save(@RequestBody List<StatementItem> items){
        statementItemService.saveItems(items);
        return AjaxResult.success("保存成功");
    }
}

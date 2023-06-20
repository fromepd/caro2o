package com.ruoyi.web.controller.business;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.business.domain.Statement;
import com.ruoyi.business.query.StatementQuery;
import com.ruoyi.business.service.IStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 岗位控制器
 * 服务结算单
 */
@RestController
@RequestMapping("business/statement")
public class StatementController extends BaseController {

    @Autowired
    private IStatementService statementService;

    /**
     * 列表
     *
     * @param qo
     * @return
     */
    @GetMapping()
    @PreAuthorize("@ss.hasPermi('business:statement:list')")
    public TableDataInfo query(StatementQuery qo) {
        startPage();
        return getDataTable(statementService.query(qo));
    }

    /**
     * 新增
     *
     * @param statement
     * @return
     */
    @PostMapping()
    @PreAuthorize("@ss.hasPermi('business:statement:add')")
    public AjaxResult add(@RequestBody Statement statement) {
        statementService.save(statement);
        return AjaxResult.success();
    }

    /**
     * 编辑
     *
     * @param statement
     * @return
     */
    @PutMapping()
    @PreAuthorize("@ss.hasPermi('business:statement:update')")
    public AjaxResult edit(@RequestBody Statement statement) {
        statementService.update(statement);
        return AjaxResult.success();
    }

    /**
     * 编辑回显信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('business:statement:query')")
    public AjaxResult get(@PathVariable Long id) {
        Statement statement = statementService.get(id);
        return AjaxResult.success(statement);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('business:statement:remove')")
    public AjaxResult remove(@PathVariable Long id) {
        statementService.deleteBatch(id);
        return AjaxResult.success();
    }

    @PutMapping("/pay/{id}")
    @PreAuthorize("@ss.hasPermi('business:statementItem:pay')")
    public AjaxResult pay(@PathVariable Long id){
        statementService.pay(id);
        return AjaxResult.success("支付成功");
    }
}

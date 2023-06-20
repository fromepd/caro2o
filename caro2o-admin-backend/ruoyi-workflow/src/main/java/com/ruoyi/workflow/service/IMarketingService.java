package com.ruoyi.workflow.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.workflow.domain.Marketing;

/**
 * 营销活动Service接口
 *
 * @author wby
 * @date 2023-01-29
 */
public interface IMarketingService extends IService<Marketing> {

    /**
     * 查询营销活动列表
     *
     * @param marketing 营销活动
     * @return 营销活动集合
     */
    public List<Marketing> selectMarketingList(Marketing marketing);

}

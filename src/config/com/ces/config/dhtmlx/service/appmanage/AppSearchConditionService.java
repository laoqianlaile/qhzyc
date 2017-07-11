package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.AppSearchConditionDao;
import com.ces.config.dhtmlx.entity.appmanage.AppSearchCondition;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxComboOption;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.CommonUtil;

@Component
public class AppSearchConditionService extends ConfigDefineDaoService<AppSearchCondition, AppSearchConditionDao>{

    @Override
    @Transactional
    public AppSearchCondition save(AppSearchCondition entity) {
        entity.setTimestamp(System.currentTimeMillis());
        entity.setUserId(CommonUtil.getUser().getId());
        return super.save(entity);
    }
    
    /**
     * qiucs 2014-2-28 
     * <p>描述: 查询用户保存过的高级查询条件</p>
     */
    public Object getUserConditions(String tableId, String componentVersionId) {
        List<DhtmlxComboOption> opts = new ArrayList<DhtmlxComboOption>();
        DhtmlxComboOption option = new DhtmlxComboOption();
        option.setValue("-1");
        option.setText("请选择或输入检索条件");
        option.setSelected(Boolean.TRUE);
        opts.add(option);
        String userId = CommonUtil.getUser().getId();
        String filters = "EQ_tableId=" + tableId + 
        		";EQ_componentVersionId=" + componentVersionId +
        		";EQ_userId=" + userId;
        List<AppSearchCondition> list = find(filters);
        if (null == list || list.isEmpty()) return opts;
        for (AppSearchCondition entity : list) {
            option = new DhtmlxComboOption();
            option.setValue(entity.getId());
            option.setText(entity.getName());
            option.setProp1(entity.getCondition());
            opts.add(option);
        }
        return opts;
    }

}

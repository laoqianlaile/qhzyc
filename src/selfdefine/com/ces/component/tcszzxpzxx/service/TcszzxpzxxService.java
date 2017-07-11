package com.ces.component.tcszzxpzxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TcszzxpzxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public Object getGrid(String plbh){
        String sql = "";

        sql = "select pz,pzbh from t_zz_xpzxx t where t.plbh = '" + plbh + "' and t.qybm = '" + SerialNumberUtil.getInstance().getCompanyCode() + "' order by pzbh desc";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data" , list);
        return dataMap;
    }


    /**
     * 默认权限过滤
     *
     * @return
     */
    public String defaultCode() {
        String code = SerialNumberUtil.getInstance().getCompanyCode();
        String defaultCode = " ";
        if (code != null && !"".equals(code))
            defaultCode = AppDefineUtil.RELATION_AND + " QYBM = '" + code
                    + "' ";
        return defaultCode;
    }

    @Override
    protected String buildCustomerFilter(String tableId,
                                         String componentVersionId, String moduleId, String menuId,
                                         Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
    }
    
}
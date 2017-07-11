package com.ces.component.tcszzddxx.service;

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
public class TcszzddxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

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
                    + "' " + AppDefineUtil.RELATION_AND + " DDZT = '1' " + AppDefineUtil.RELATION_AND
                    + " IS_DELETE <> '1'";
        return defaultCode;
    }

    @Override
    protected String buildCustomerFilter(String tableId,
                                         String componentVersionId, String moduleId, String menuId,
                                         Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
    }

    public Object getDdxxByKhbh(String khbh) {
        String sql = "select ddbh from t_zz_xsddxx where 1=1 "+defaultCode();
        if(null!=khbh&&!"".equals(khbh)){
            sql += " and khbh = '"+khbh+"'";
        }
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("data",maps);
        return resultMap;
    }
}
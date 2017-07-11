package com.ces.component.sctzcx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.ces.component.trace.utils.SerialNumberUtil.getInstance;

@Component
public class SctzcxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
    /**
     * 默认权限过滤
     *
     * @return
     */
    public String defaultCode() {
        String code = getInstance().getCompanyCode();
        String defaultCode = " ";
        if (code != null && !"".equals(code))
            defaultCode = AppDefineUtil.RELATION_AND + " A_QYBM = '" + code
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


    public List<Map<String,Object>> getImages(String tzid){
        String sql = "";
        sql = "select t.* from t_sc_sctzxx t where tzid = '" + tzid + "' and qybm = '" + SerialNumberUtil.getInstance().getCompanyCode() + "'";
        List<Map<String,Object>> map = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return map;
    }

    @Transactional
    public boolean getDeleteByTzid(String tzid){
        boolean bool = false;
        String sql = "";
        sql = "delete from T_SC_TZ where id = '" + tzid.split("_")[0] + "'";
        DatabaseHandlerDao.getInstance().executeSql(sql);
        sql = "delete from t_sc_sctzxx where tzid = '" + tzid.split("_")[0] + "'";
        DatabaseHandlerDao.getInstance().executeSql(sql);
        return bool;
    }

}
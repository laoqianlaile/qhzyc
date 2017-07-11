package com.ces.component.zzjdxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.List;
import java.util.Map;

@Component
public class ZzjdxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
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
                    + "' " + AppDefineUtil.RELATION_AND + " is_delete <> '1'";
        return defaultCode;
    }

    @Override
    protected String buildCustomerFilter(String tableId,
                                         String componentVersionId, String moduleId, String menuId,
                                         Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
    }

    /**根据基地编号获取区域编号
     * @param jdbh
     * @return
     */
    public Object querySsqy(String jdbh){
        String sql = "";
        sql = "select * from t_zz_qyxx t where t.qybm = '" + SerialNumberUtil.getInstance().getCompanyCode() + "'" +
                " and t.jdbh = '" + jdbh + "' and is_delete <> '1'";
        List<Map<String,Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return dataList;
    }
    
}
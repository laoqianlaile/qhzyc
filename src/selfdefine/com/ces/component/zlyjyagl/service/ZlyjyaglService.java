package com.ces.component.zlyjyagl.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.Map;

@Component
public class ZlyjyaglService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    @Override
    protected String buildCustomerFilter(String tableId,
                                         String componentVersionId, String moduleId, String menuId,
                                         Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
      //  StringBuffer filter = new StringBuffer(AppDefineUtil.RELATION_AND+"is_delete='0'");
        return "";
    }

    /**
     * 删除单行数据
     * @param yjyaId
     */
    public void deleteYjya(String yjyaId) {
        String selSql = "select t.yabh from t_zl_yjya t where id = ?";
        Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(selSql,new Object[]{yjyaId});
        String yabh = map.get("YABH") == null ? "" : map.get("YABH").toString();
        String delSql = "update t_zl_yjya set is_delete = '1' where yabh = ?";
        DatabaseHandlerDao.getInstance().executeSql(delSql,new Object[]{yabh});
    }
}
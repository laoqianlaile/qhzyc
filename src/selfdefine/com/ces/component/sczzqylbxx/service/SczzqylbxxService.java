package com.ces.component.sczzqylbxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.DateUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
public class SczzqylbxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        // return AppDefineUtil.RELATION_AND + "DELETE_FLAG=0";
        return AppDefineUtil.RELATION_AND + "QYBM="+ SerialNumberUtil.getInstance().getCompanyCode();
    }

    /**
     * 逻辑删除区域信息
     * @param ids 选中的区域id字符串
     */
    @Transactional
    public void logicDelete(String ids){
        String sql = "UPDATE T_ZZ_QYXX SET IS_DELETE='1' , DELETE_USER='"+ CommonUtil.getCurrentUserId()+"' " +
                " , DELETE_TIME='"+ DateUtil.currentTime()+"' WHERE ID IN ('"+ids.replace(",","','")+"')";
        DatabaseHandlerDao.getInstance().executeSql(sql);
    }

    /**
     * 判断区域下是否有地块
     * @param qybhArray 关联字段区域编号字符串，“,”隔开
     * @return
     */
    public boolean hasChildren(String qybhArray){
        qybhArray = qybhArray.replaceAll(",","','");
        String sql = "SELECT COUNT(T.ID) AS COUNT" +
                "  FROM T_ZZ_DKXX T, T_ZZ_QYXX Q" +
                " WHERE T.QYBH = Q.QYBH" +
                "   AND T.QYBM = Q.QYBM" +
                "   AND Q.ID IN ('"+qybhArray+"')" +
                "   AND Q.QYBM = ?";
        Map map = DatabaseHandlerDao.getInstance().queryForMap(sql,new Object[]{SerialNumberUtil.getInstance().getCompanyCode()});
        int count = Integer.parseInt(map.get("COUNT").toString());
        if(count>0){
            return true;
        }else {
            return false;
        }
    }
}
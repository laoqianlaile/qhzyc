package com.ces.component.sczzqyxx.service;

import ces.sdk.util.StringUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.Map;

@Component
public class SczzqyxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    /**
     * 判断区域编号是否存在
     * @param qybh  区域编号
     * @param jdbh 基地编号
     * @param id    id
     * @return
     */
    public Boolean isQybhExist(String qybh,String jdbh,String id){
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        if(StringUtil.isNotBlank(id)){
            String sql = "SELECT T.ID  FROM T_ZZ_QYXX T WHERE T.QYBH = ? AND T.QYBM = ? AND T.IS_DELETE<>'1' AND T.JDBH = ?";
            Map map = DatabaseHandlerDao.getInstance().queryForMap(sql,new Object[]{qybh,qybm,jdbh});
            if(map.isEmpty())
                return false;
            String queryId = map.get("ID").toString();
            if(!queryId.equals(id))
                return true;
            else
                return false;
        }else{
            String sql = "SELECT COUNT(T.ID) AS NUM  FROM T_ZZ_QYXX T WHERE T.QYBH = ? AND T.QYBM = ? AND T.IS_DELETE<>'1' AND T.JDBH = ?";
            Map map = DatabaseHandlerDao.getInstance().queryForMap(sql,new Object[]{qybh,qybm,jdbh});
            int num = Integer.parseInt(map.get("NUM").toString());
            if(num>0)
                return true;
            else
                return false;
        }
    }
}
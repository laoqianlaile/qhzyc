package com.ces.component.tcszhxx.service;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TcszhxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public Object getZhxx(String id) {
        String sql = "select t.zhbh as qybm,t.qymc from t_qypt_zhgl t where t.auth_parent_id = '" +id+ "'";
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("data",maps);
        return map;
    }

    public Object getIdByZhbh(String zhbh) {
        String sql = "select t.auth_id from t_qypt_zhgl t where t.zhbh = '"+zhbh+"'";
        Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
        return map;
    }

    public Object getSbbh(String zhen_companyCode){
        StringBuilder bh = new StringBuilder(zhen_companyCode);
        StringBuilder sql = new StringBuilder("SELECT T.BH FROM T_QYPT_SBGL T WHERE BH LIKE ? ORDER BY T.BH DESC");
        List<Map<String, Object>> bhList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), new Object[]{zhen_companyCode + "%"});
        if(bhList.size() == 0){
            while(bh.toString().length() < 14){
                bh.append("0");
            }
            bh.append("1");
        }else{
            String oldBh = bhList.get(0).get("BH").toString().substring(9,15).replaceAll("0","");
            while (bh.toString().length() < (15 - oldBh.length())){
                bh.append("0");
            }
            bh.append((Integer.parseInt(oldBh) + 1) + "");
        }
        return bh;
    }

    public Object setQyzt(String id, String type, String zt) {
        String field = "";
        if("sb".equals(type)) {
            field = "SBQYZT";
        } else if("sj".equals(type)) {
            field = "SJQYZT";
        } else {
            return "TYPE-ERROR";
        }
        if("".equals(zt)||null==zt){
            return "ZT-ERROR";
        }
        String sql = "update t_qypt_sbgl set " + field + "='" + zt + "' where id = '" + id + "'";
        int result = DatabaseHandlerDao.getInstance().executeSql(sql);
        return result;
    }
}
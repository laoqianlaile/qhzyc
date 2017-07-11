package com.ces.component.yjyaxz.service;

import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class YjyaxzService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {


    public Object getFieldByYalx(String yalx) {
        List<Map<String,Object>> maps = new ArrayList<Map<String, Object>>();
        String sql = "";
        if (yalx.equals("yyxx")) {
             sql = "select t.show_name, t.column_name\n" +
                    "  from T_XTPZ_COLUMN_DEFINE t\n" +
                    " where t.table_id = (select id\n" +
                    "                       from T_XTPZ_PHYSICAL_TABLE_DEFINE t1\n" +
                    "                      where t1.show_name = 'V_YJYA_YYXX')";

        } else if (yalx.equals("xsqx")) {
            sql = "select t.show_name, t.column_name\n" +
                    "  from T_XTPZ_COLUMN_DEFINE t\n" +
                    " where t.table_id = (select id\n" +
                    "                       from T_XTPZ_PHYSICAL_TABLE_DEFINE t1\n" +
                    "                      where t1.show_name = 'V_YJYA_XSQX')";

        }
        maps = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Iterator<Map<String,Object>> iterator = maps.iterator();
        while (iterator.hasNext()) {
            Map<String,Object> map = iterator.next();
            String colName = map.get("COLUMN_NAME").toString();
            if(colName.contains("ID")||colName.contains("CPZSM")||colName.contains("PCH")||colName.contains("CSLSH")||colName.contains("CPMC")) iterator.remove();
        }
        return maps;
    }

    public Object saveYjya(Map<String, Object> yjyaMap) {
        List<Map<String,Object>> griddata = (List<Map<String,Object>>)yjyaMap.get("griddata");
        Map<String,Object> formData = (Map<String,Object>)yjyaMap.get("formdata");
        String yjyabh = "";
        if (formData.get("yabh").toString().equals("")) {
            yjyabh = SerialNumberUtil.getInstance().getSerialNumber("ZL","YJYABH",true);
        } else {
            yjyabh = formData.get("yabh").toString();
            String delSql = "delete from t_zl_yjya where yabh = ? and qybm = ?";
            DatabaseHandlerDao.getInstance().executeSql(delSql,new Object[] {yjyabh,SerialNumberUtil.getInstance().getCompanyCode()});
        }
        for (Map<String,Object> map : griddata) {
            Map<String,String> dataMap = new HashMap<String, String>();
            dataMap.put("YAMC",formData.get("yamc") == null ? "":formData.get("yamc").toString());
            dataMap.put("YAMS",formData.get("yams") == null ? "":formData.get("yams").toString());
            dataMap.put("YALX",formData.get("yalx") == null ? "":formData.get("yalx").toString());
            dataMap.put("YABH",yjyabh);
            dataMap.put("ZDMC",map.get("zdmc") == null ? "" : map.get("zdmc").toString());
            dataMap.put("XSMC",map.get("xsmc") == null ? "" : map.get("xsmc").toString());
            dataMap.put("ID","");
            dataMap.put("QYBM",SerialNumberUtil.getInstance().getCompanyCode());
            save("T_ZL_YJYA",dataMap,null);
        }
        return "SUCCESS";
    }

    public Object getYaData (String yaid) {
        String sql = " select s.yamc,s.yabh,s.yams,s.zdmc,s.xsmc,s.yalx from t_zl_yjya s,(select v.yabh from t_zl_yjya v where id = ?) va where s.yabh = va.yabh and s.is_delete <> '1'";
        List<Map<String,Object>> rightMapList = DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{yaid});
        String yalx = rightMapList.get(0).get("YALX") == null?"":rightMapList.get(0).get("YALX").toString();
        Map<String,Object> masterFormData = new HashMap<String,Object>() ;
        masterFormData.put("YAMS",rightMapList.get(0).get("YAMS") == null?"":rightMapList.get(0).get("YAMS").toString());
        masterFormData.put("YALX",yalx);
        masterFormData.put("YABH",rightMapList.get(0).get("YABH") == null?"":rightMapList.get(0).get("YABH").toString());
        masterFormData.put("YAMC",rightMapList.get(0).get("YAMC") == null?"":rightMapList.get(0).get("YAMC").toString());
        Iterator<Map<String,Object>> iterator = rightMapList.iterator();
        while (iterator.hasNext()) {
            Map<String,Object> map = iterator.next();
            if (map.get("ZDMC").toString().equals("CPZSM")||map.get("ZDMC").toString().equals("PCH")||map.get("ZDMC").toString().equals("CSLSH")||map.get("ZDMC").toString().equals("CPMC")) {
                iterator.remove();
            }
        }
        List<Map<String,Object>> allFieldMapList = (List<Map<String,Object>>)getFieldByYalx(yalx);

        List<Map<String,Object>> leftMapList = new ArrayList<Map<String, Object>>();
        for (Map<String,Object> allFieldMap : allFieldMapList) {
            boolean notInright = true;
            for (Map<String,Object> rightMap : rightMapList) {
                if(allFieldMap.get("COLUMN_NAME").toString().equals(rightMap.get("ZDMC").toString())) {
                    notInright = false;
                    break;
                }
            }

            if (notInright) {
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("ZDMC",allFieldMap.get("COLUMN_NAME"));
                map.put("XSMC",allFieldMap.get("SHOW_NAME"));
                leftMapList.add(map);
            }
        }
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("leftMapList",leftMapList);
        resultMap.put("rightMapList",rightMapList);
        resultMap.put("masterFormData",masterFormData);
        return resultMap;
    }
}
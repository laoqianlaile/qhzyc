package com.ces.component.trace.code;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaoben   20161018
 */
public class SdzyczzzmxxCode extends CodeApplication {
    @Override
    public String getCodeValue(String name) {
        return null;
    }

    @Override
    public String getCodeName(String value) {
        return null;
    }

    @Override
    public List<Code> getCodeList(String codeTypeCode) {
//        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
//        String sql="select t.ZZZMMC,t.ZZZMBH from t_sdzyc_zzzm t where t.qybm = ? and t.is_delete<>1";
//        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[] {qybm});
//        List<Code> dataList=new ArrayList<Code>();
//        int i = 0, len =maps.size();
//        Code code = null;
//        for (; i < len; i++) {
//            Map<String,Object> dataMap=maps.get(i);
//            code = new Code();
//            code.setCodeTypeCode(codeTypeCode);
//            code.setValue(dataMap.get("ZZZMBH").toString());
//            code.setName(dataMap.get("ZZZMMC").toString());
//            code.setShowOrder(i + 1);
//            dataList.add(code);
//        }
//        return dataList;
        return null;
    }

    @Override
    public Object getCodeTree(String codeTypeCode) {
        return null;
    }

    @Override
    public Object getCodeGrid(String codeTypeCode) {
        Map<String, Object> gcfg = new HashMap<String, Object>();
        gcfg.put("colNames", getColNames());
        gcfg.put("colModel", getColModel());
        gcfg.put("url", "sdzyczzzzcg!searchZzzmxx.json");
        //gcfg.put("panelWidth",190);
        gcfg.put("panelWidth",290);
        gcfg.put("valueField", "ZZZMBH");// 列表中PFSBM列值作为隐藏 值
        gcfg.put("valueField", "ZZZMMC");
        gcfg.put("textField", "ZZZMMC");// 列表中PFSMC列值作为 显示值
        return gcfg;
    }

    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("种子种苗编号");
        list.add("种子种苗名称");
        return list;
    }

    protected List<Map<String, Object>> getColModel() {
        List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String, Object> item =null;

        item = new HashMap<String, Object>();
        item.put("name", "ZZZMBH");
        item.put("width", 100);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "ZZZMMC");
        item.put("width", 100);
        colModel.add(item);

        return colModel;
    }
}

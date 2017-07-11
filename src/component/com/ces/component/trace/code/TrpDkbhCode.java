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
 * Created by wngyu on 16/2/18.
 */
public class TrpDkbhCode extends CodeApplication {
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
//        List<Code> list = new ArrayList<Code>();
//        String sql="select DKBH value,DKMC name from t_zz_dkxx where qybm = '"+ SerialNumberUtil.getInstance().getCompanyCode()+"'";
//        List<Map<String,Object>> dataMap = DatabaseHandlerDao.getInstance().queryForMaps(sql);
//        if(dataMap != null  && !dataMap.isEmpty()){
//            int showOrder = 1;
//            for(Map<String,Object> map :dataMap){
//                Code code = new Code();
//                code.setName(map.get("VALUE").toString());
//                code.setValue(map.get("VALUE").toString());
////                code.setShowOrder(Integer.parseInt(String.valueOf(map.get("SHOW_ORDER"))));
//                list.add(code);
//            }
//        }
//        return list;
        return null;
    }

    @Override
    public Object getCodeTree(String codeTypeCode) {
        return null;
    }

    @Override
    public Object getCodeGrid(String codeTypeCode) {
//        // TODO Auto-generated method stub
        Map<String, Object> gcfg = new HashMap<String, Object>();
        gcfg.put("colNames", getColNames());
        gcfg.put("colModel", getColModel());
        gcfg.put("url", "zztrptj!getJyzdaGrid.json");
        gcfg.put("valueField", "DKBH");// 列表中PFSBM列值作为 显示值
        gcfg.put("textField", "DKBH");// 列表中PFSMC列值作为 隐藏值
        return gcfg;
//        return null;
    }

    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("地块编号");
        list.add("地块名称");

        return list;
    }

    protected List<Map<String, Object>> getColModel() {
        List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String, Object> item =null;

        item = new HashMap<String, Object>();
        item.put("name", "DKBH");
        item.put("width", 100);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "DKMC");
        item.put("width", 100);
        colModel.add(item);

        return colModel;
    }
}

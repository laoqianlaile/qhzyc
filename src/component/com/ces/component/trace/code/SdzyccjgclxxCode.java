package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/22.
 */
public class SdzyccjgclxxCode extends CodeApplication {
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
        gcfg.put("url", "sdzyccjgclxx!getClxxGrid.json");
        gcfg.put("panelWidth",280);
        gcfg.put("valueField", "CPH");// 列表中PFSBM列值作为 显示值
        gcfg.put("textField", "CPH");// 列表中PFSMC列值作为 隐藏值
        return gcfg;
    }

    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("车牌号");
        list.add("车辆类型");
        return list;
    }

    protected List<Map<String, Object>> getColModel() {
        List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String, Object> item =null;

        item = new HashMap<String, Object>();
        item.put("name", "CPH");
        item.put("width", 120);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "CLLX");
        item.put("width", 120);
        colModel.add(item);

        return colModel;
    }
}

package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Neptune on 2016/4/27.
 */
public class SdzycymsydkbhCode extends CodeApplication {
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
        gcfg.put("url", "sdzycymsy!getDkbhGrid.json");
        gcfg.put("gridOptions", "{ajaxGridOptions:{async:false}}");
        gcfg.put("valueField", "DKMJ");// 列表中YMPZ列值作为 隐藏值
        gcfg.put("textField", "DKBH");// 列表中YMBH列值作为 显示值
        gcfg.put("panelWidth", 420);

        return gcfg;
    }

    private Object getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("试验田编号");
        list.add("试验田面积");
        list.add("试验田位置");
        list.add("是否使用");
        return list;
    }

    private Object getColModel() {
        List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String, Object> item =null;

        item = new HashMap<String, Object>();
        item.put("name", "DKBH");
        item.put("width", 140);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "DKMJ");
        item.put("width", 140);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "DKWZ");
        item.put("width", 140);
        colModel.add(item);

        item = new HashMap<String,Object>();
        item.put("name","sfsy");
        item.put("width",80);
        item.put("hidden",true);
        colModel.add(item);
        return colModel;
    }


}

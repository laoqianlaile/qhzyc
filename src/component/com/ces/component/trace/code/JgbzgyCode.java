package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加工包装工艺编码
 * Created by bdz on 2015/6/12.
 */
public class JgbzgyCode extends CodeApplication {
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
        // TODO Auto-generated method stub
        Map<String, Object> gcfg = new HashMap<String, Object>();
        gcfg.put("colNames", getColNames());
        gcfg.put("colModel", getColModel());
        gcfg.put("url", "jgbzgy!getBzgyGrid.json");
        gcfg.put("valueField", "BZGYBH");// 包装工艺名称
        gcfg.put("textField", "BZGYBH");// 包装工艺编码
        gcfg.put("panelWidth", 250);
        return gcfg;
    }
    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("包装工艺编码");
        list.add("包装工艺名称");

        return list;
    }

    protected List<Map<String, Object>> getColModel() {
        List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String, Object> item =null;

        item = new HashMap<String, Object>();
        item.put("name", "BZGYBH");
        item.put("width", 120);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "BZGYMC");
        item.put("width", 120);
        colModel.add(item);

        return colModel;
    }
}

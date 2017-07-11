package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Synge on 2015/8/13.
 */
public class ZzjdxxCode extends CodeApplication {

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
        gcfg.put("url", "zzdyxx!getJdxx.json");
        gcfg.put("gridOptions", "{ajaxGridOptions:{async:false}}");
        gcfg.put("valueField", "JDBH");// 将基地编号设为隐藏值
        gcfg.put("textField", "JDMC");// 将基地名称设为显示值
        return gcfg;
    }

    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("基地编号");
        list.add("基地名称");

        return list;
    }

    protected List<Map<String, Object>> getColModel() {
        List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String, Object> item =null;

        item = new HashMap<String, Object>();
        item.put("name", "JDBH");
        item.put("width", 100);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "JDMC");
        item.put("width", 100);
        colModel.add(item);

        return colModel;
    }
}

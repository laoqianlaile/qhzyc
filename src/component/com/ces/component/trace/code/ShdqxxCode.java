package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Synge on 2015/11/5.
 */
public class ShdqxxCode extends CodeApplication {
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
        gcfg.put("url", "cdxx!getShdqxxGrid.json");
        gcfg.put("valueField", "CDBM");
        gcfg.put("textField", "CDMC");
        gcfg.put("panelWidth", 300);
        gcfg.put("panelHeight", 400);
        gcfg.put("pager","true");
//		gcfg.put("pager",false);
        return gcfg;
    }

    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("地区编码");
        list.add("地区名称");
        return list;
    }

    protected List<Map<String, Object>> getColModel() {
        List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String, Object> item =null;

        item = new HashMap<String, Object>();
        item.put("name", "CDBM");
        item.put("width", 110);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "CDMC");
        item.put("width", 140);
        colModel.add(item);

        return colModel;
    }
}
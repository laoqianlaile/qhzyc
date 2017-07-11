package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiang on 2015/10/23.
 */
public class ZzdkxxCode extends CodeApplication {

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
        gcfg.put("url", "zzdkxx!getCGQZ.json");
//      gcfg.put("gridOptions", "{ajaxGridOptions:{async:false}}");
        gcfg.put("valueField", "MC");
        gcfg.put("textField", "MC");
        return gcfg;

    }


    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("编号");
        list.add("名称");
        list.add("品牌");
        list.add("型号");
        return list;
    }

    protected List<Map<String, Object>> getColModel() {
        List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String, Object> item =null;

        item = new HashMap<String, Object>();
        item.put("name", "BH");
        item.put("width", 80);
        item.put("align", "left");
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "MC");
        item.put("width", 100);
        item.put("align", "left");
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "PP");
        item.put("width", 100);
        item.put("align", "left");
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "XH");
        item.put("width", 100);
        item.put("align", "left");
        colModel.add(item);

        return colModel;
    }
}

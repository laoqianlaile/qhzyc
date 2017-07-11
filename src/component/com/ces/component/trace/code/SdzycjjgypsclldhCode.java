package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 山东中药材精加工药材生产领料单号下拉列表编码
 * Created by Synge on 2015/8/28.
 */
public class SdzycjjgypsclldhCode extends CodeApplication {

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
        gcfg.put("url", "sdzycypscxx!searchLldhData.json");
        gcfg.put("gridOptions", "{ajaxGridOptions:{async:false}}");
        gcfg.put("panelWidth",380);
        gcfg.put("valueField", "LLDH");// 列表中LLDH列值作为 显示值
        gcfg.put("textField", "LLDH");// 列表中列值作为 隐藏值
        return gcfg;
    }

    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("领料单号");
        list.add("领料时间");
        list.add("领料重量");
        return list;
    }

    protected List<Map<String, Object>> getColModel() {
        List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String, Object> item =null;

        item = new HashMap<String, Object>();
        item.put("name", "LLDH");
        item.put("width", 180);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "LLSJ");
        item.put("width", 120);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "LLZZL");
        item.put("width", 120);
        colModel.add(item);

        return colModel;
    }
}



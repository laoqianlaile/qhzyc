package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 山东中药材精加工药材包装生产批次号下拉列表编码
 * Created by Synge on 2015/8/28.
 */
public class SdzycjjgypbzscpchCode extends CodeApplication {

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
        gcfg.put("url", "sdzycypbzxx!searchPchGridData.json");
        gcfg.put("gridOptions", "{ajaxGridOptions:{async:false}}");
        gcfg.put("panelWidth",400);
        gcfg.put("valueField", "SCPCH");// 列表中PFSBM列值作为 显示值
        gcfg.put("textField", "QYPCH");// 列表中PFSMC列值作为 隐藏值
        return gcfg;
    }

    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("生产批次号");
        list.add("生产批次号");
        list.add("饮片名称");
        list.add("药材代码");
        list.add("重量");
        return list;
    }

    protected List<Map<String, Object>> getColModel() {
        List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String, Object> item =null;

        item = new HashMap<String, Object>();
        item.put("name", "QYPCH");
        item.put("width", 150);
        colModel.add(item);


        item = new HashMap<String, Object>();
        item.put("name", "SCPCH");
        item.put("hidden",true);
        item.put("width", 200);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "YPMC");
        item.put("width", 120);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "YCDM");
        item.put("width", 120);
        item.put("hidden",true);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "KC");
        item.put("width", 80);
        colModel.add(item);

        return colModel;
    }
}



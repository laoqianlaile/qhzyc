package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Neptune on 2016/5/25.
 */
public class SdzycjjgypscpchCode extends CodeApplication {

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
        gcfg.put("url", "sdzycjjgypjyjcxx!getYpscpchGrid.json");
        gcfg.put("gridOptions", "{ajaxGridOptions:{async:false}}");
        gcfg.put("valueField", "SCPCH");// 列表中PFSBM列值作为 显示值
        gcfg.put("textField", "QYSCPCH");// 列表中PFSMC列值作为 隐藏值
        gcfg.put("panelWidth", 332);

        return gcfg;
    }

    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("生产批次号");
        list.add("生产批次号");
        list.add("饮片名称");
        list.add("药材代码");
        return list;
    }

    protected List<Map<String, Object>> getColModel() {
        List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String, Object> item =null;

        item = new HashMap<String, Object>();
        item.put("name", "QYSCPCH");
        item.put("width", 84);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "SCPCH");
        item.put("hidden",true);
        item.put("width", 96);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "YPMC");
        item.put("width", 84);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "YCDM");
        item.put("width", 60);
        item.put("hidden",true);
        colModel.add(item);

        return colModel;
    }


}

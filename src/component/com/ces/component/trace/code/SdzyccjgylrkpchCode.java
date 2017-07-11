package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 办公 on 2016/5/3.
 */
public class SdzyccjgylrkpchCode  extends CodeApplication {{
} @Override
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
        gcfg.put("url", "sdzyccscggl!searchGridData.json");
        gcfg.put("panelWidth",600);
        gcfg.put("valueField", "CSPCH");// 列表中PFSBM列值作为 显示值
        gcfg.put("textField", "QYCSPCH");// 列表中PFSMC列值作为 隐藏值
        return gcfg;
    }

    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("采收批次号");
        list.add("采收批次号");
        list.add("药材名称");
        list.add("采收重量");
        list.add("产地名称");
        list.add("药材代码");
        list.add("企业名称");
        list.add("结束时间");

        return list;
    }

    protected List<Map<String, Object>> getColModel() {
        List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String, Object> item =null;

        item = new HashMap<String, Object>();
        item.put("name", "QYCSPCH");
        item.put("width", 140);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "CSPCH");
        item.put("hidden",true);
        item.put("width", 120);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "YCMC");
        item.put("width", 90);
        colModel.add(item);



        item = new HashMap<String, Object>();
        item.put("name", "CSZL");
        item.put("width", 90);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "SSDQ");
        item.put("width", 200);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "YCDM");
        item.put("width", 120);
        item.put("hidden", true);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "QYMC");
        item.put("width", 200);
        item.put("hidden", true);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "JSSJ");
        item.put("width", 140);
        colModel.add(item);

        return colModel;
    }
}

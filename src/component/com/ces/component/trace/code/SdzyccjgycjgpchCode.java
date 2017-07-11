package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 办公 on 2016/5/6.
 */
public class SdzyccjgycjgpchCode extends CodeApplication {

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
        gcfg.put("url", "cjgylllxxxz!searchGridData.json");
        gcfg.put("panelWidth",380);
        gcfg.put("valueField", "PCH");// 列表中PFSBM列值作为 隐藏值
        gcfg.put("textField", "QYPCH");// 列表中PFSMC列值作为 显示值
     /*   gcfg.put("textField","WLZZL");*/
        return gcfg;
    }

    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("企业批次号");
        list.add("原料批次号");
        list.add("加工总重量");
        list.add("原料名称");
        list.add("原料代码");
       /* list.add("物料总重量");*/
        return list;
    }

    protected List<Map<String, Object>> getColModel() {
        List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String, Object> item =null;
        item = new HashMap<String, Object>();
        item.put("name", "QYPCH");
        item.put("width", 120);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "PCH");
        item.put("hidden",true);
        item.put("width", 120);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "LLZZL");
        item.put("width", 120);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "LLMC");
        item.put("width", 120);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "YCDM");
        item.put("hidden",true);
        item.put("width", 120);
        colModel.add(item);

        return colModel;

    }
}


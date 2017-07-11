package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 精加工仓库信息编码业务表
 * Created by wngyu on 15/11/27.
 */
public class SdzycjjgbzpchCode extends CodeApplication {

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
        return  null;
    }

    @Override
    public Object getCodeGrid(String codeTypeCode) {
        Map<String, Object> ckxx = new HashMap<String, Object>();
        ckxx.put("colNames", getColNames());
        ckxx.put("colModel", getColModel());
        ckxx.put("url", "sdzycyprkxx!getJjgBzpch.json");
        ckxx.put("valueField", "BZPCH");
        ckxx.put("textField", "QYBZPCH");
        ckxx.put("panelWidth", 500);
        return ckxx;
    }
    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("包装批次号");
        list.add("包装批次号");
        list.add("生产批次号");
        list.add("企业生产批次号");
        list.add("饮片名称");
        list.add("库存重量");
        list.add("药材代码");
//        list.add("采收批次号");

        return list;
    }

    protected List<Map<String, Object>> getColModel() {
        List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String, Object> item =null;

        item = new HashMap<String, Object>();
        item.put("name", "QYBZPCH");
        item.put("width", 200);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "BZPCH");
        item.put("hidden",true);
        item.put("width", 300);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "PCH");
        item.put("hidden",true);
        item.put("width", 300);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "QYPCH");
        item.put("hidden",true);
        item.put("width", 300);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "YPMC");
        item.put("width", 200);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "KC");
        item.put("width", 200);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "YCDM");
        item.put("hidden", true);
        item.put("width", 200);
        colModel.add(item);

//        item = new HashMap<String, Object>();
//        item.put("name", "CSPCH");
//        item.put("hidden", true);
//        item.put("width", 200);
//        colModel.add(item);

        return colModel;
    }
}


package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 2016/10/21.
 */
public class TcspchxxCode extends CodeApplication{
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
        Map<String , Object> gcfg = new HashMap<String, Object>();
        gcfg.put("colNames",getColNames());
        gcfg.put("colModel",getColModel());
        //gcfg.put("gridOptions","{ajaxGridOptions:{async:false}}");
        gcfg.put("url", "acxmlsjdr!searchGridData.json");
        gcfg.put("valueField","BATCHNO");
        gcfg.put("textField","BATCHNO");
        return gcfg;

    }

    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("商品名称");
        list.add("批次号");
        list.add("生产线");
        list.add("加工时间");
        return list;
    }

    protected List<Map<String, Object>> getColModel() {
        List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String, Object> item =null;

        item = new HashMap<String, Object>();
        item.put("name", "SPMC");
        item.put("width", 80);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "BATCHNO");
        item.put("width", 130);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "LINENAME");
        item.put("width", 80);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "MADEDATE");
        item.put("width", 80);
        colModel.add(item);

        return colModel;
    }
}

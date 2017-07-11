package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Plain on 2016/5/24.
 */
public class SdzycjjgycrkpchCode extends CodeApplication{
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
    public Object getCodeGrid(String codeTypeCode)  {
        Map<String ,Object> gcfg = new HashMap<String, Object>();
        gcfg.put("colNames", getColNames());
        gcfg.put("colModel", getColModel());
        // gcfg.put("url", "");
        gcfg.put("gridOptions", "{ajaxGridOptions:{async:false}}");
        gcfg.put("valueField", "PCH");
        gcfg.put("textField", "QYPCH");
        gcfg.put("panelWidth", 500);
        return gcfg;
    }
    public List<String> getColNames(){
        List<String> dataList = new ArrayList<String>();
        dataList.add("批次号");
        dataList.add("批次号");
        dataList.add("药材名称");
        dataList.add("交易重量");
        dataList.add("交易单价");
        dataList.add("产地");
        dataList.add("药材代码");
        return dataList;
    }
    public List<Map<String ,Object>> getColModel(){
        List<Map<String,Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String,Object> item =null;

        item = new HashMap<String, Object>();
        item.put("name","QYPCH");
        item.put("width",180);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name","PCH");
        item.put("hidden",true);
        item.put("width",80);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name","YCMC");
        item.put("width",80);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name","JYZL");
        item.put("width",80);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name","JYDJ");
        item.put("width",80);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name","CDMC");
        item.put("width",180);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name","YCDM");
        item.put("width",80);
        item.put("hidden",true);
        colModel.add(item);

        return colModel;

    }
}

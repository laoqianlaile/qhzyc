
package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Plain on 2016/5/25.
 */
public class SdzycjjgypjgylpchCode extends CodeApplication {
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
        Map<String , Object>  gcfg = new HashMap<String, Object>();
        gcfg.put("colNames",getColNames());
        gcfg.put("colModel",getColModel());
        gcfg.put("gridOptions","{ajaxGridOptions:{async:false}}");
        gcfg.put("valueField","PCH");
        gcfg.put("textField","QYPCH");
        return gcfg;
    }
    public List<String> getColNames(){
        List<String> list = new ArrayList<String>();
        list.add("领料单号");
        list.add("批次号");
        list.add("原料批次号");
        list.add("药材名称");
        list.add("领料重量");
        list.add("药材代码");
        list.add("采收批次号");
        list.add("产地");
        return list;
    }
    public List<Map<String,Object>> getColModel(){
        List<Map<String,Object>> dataList = new ArrayList<Map<String, Object>>();
        Map<String,Object> item = new HashMap<String, Object>();
        item.put("name","LLDH");
        item.put("width",80);
        dataList.add(item);

        item = new HashMap<String, Object>();
        item.put("name","QYPCH");
        item.put("width",80);
        dataList.add(item);

        item = new HashMap<String, Object>();
        item.put("name","PCH");
        item.put("hidden",true);
        item.put("width",130);
        dataList.add(item);

        item = new HashMap<String, Object>();
        item.put("name","LLMC");
        item.put("width",80);
        dataList.add(item);

        item = new HashMap<String, Object>();
        item.put("name","LLZZL");
        item.put("width",80);
        dataList.add(item);

        item = new HashMap<String, Object>();
        item.put("name","YCDM");
        item.put("width",80);
        item.put("hidden",true);
        dataList.add(item);

        item = new HashMap<String, Object>();
        item.put("name","CSPCH");
        item.put("width",80);
        item.put("hidden",true);
        dataList.add(item);

        item = new HashMap<String, Object>();
        item.put("name","CD");
        item.put("width",80);
        item.put("hidden",true);
        dataList.add(item);
        return dataList;
    }

}

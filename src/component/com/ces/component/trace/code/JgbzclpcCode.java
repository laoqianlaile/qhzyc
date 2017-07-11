package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包装材料批次编码表
 * Created by bdz on 2015/6/14.
 */
public class JgbzclpcCode extends CodeApplication {
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
        //gcfg.put("url", "jgbzclhwxx!getBzclpcGrid.json");
        gcfg.put("valueField", "BZCLPCH");// 列表中BZCLBH列值作为 显示值
        gcfg.put("textField", "BZCLPCH");// 列表中BZCLMC列值作为 隐藏值
        gcfg.put("panelWidth", 300);
        return gcfg;
    }

    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("包装材料批次");
        //list.add("包装材料编号");
        list.add("包装材料名称");
        //list.add("包装材料类型");
        //list.add("包装材料来源");
        list.add("供应商名称");
        //list.add("存储场地编号");
        //list.add("存储场地名称");
        //list.add("岗位");

        return list;
    }

    protected List<Map<String, Object>> getColModel() {
        List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String, Object> item =null;

        item = new HashMap<String, Object>();
        item.put("name", "BZCLPCH");
        item.put("width", 100);
        colModel.add(item);

//        item = new HashMap<String, Object>();
//        item.put("name", "BZCLBH");
//        item.put("width", 100);
//        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "BZCLMC");
        item.put("width", 100);
        colModel.add(item);
        
//        item = new HashMap<String, Object>();
//        item.put("name", "BZCLLX");
//        item.put("width", 100);
//        colModel.add(item);
        
//        item = new HashMap<String, Object>();
//        item.put("name", "BZCLLY");
//        item.put("width", 100);
//        colModel.add(item);
        
        item = new HashMap<String, Object>();
        item.put("name", "GYSMC");
        item.put("width", 100);
        colModel.add(item);
        
//        item = new HashMap<String, Object>();
//        item.put("name", "CCCDBH");
//        item.put("width", 100);
//        colModel.add(item);
        
//        item = new HashMap<String, Object>();
//        item.put("name", "CCCDMC");
//        item.put("width", 100);
//        colModel.add(item);

        return colModel;
    }
}

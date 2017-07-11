package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加工包装工艺明细
 * Created by bdz on 2015/6/15.
 */
public class JgbzgymxCode extends CodeApplication {
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
        gcfg.put("valueField", "SYBZCLBH");// 列表中SYBZCLBH列值作为 显示值
        gcfg.put("textField", "SYBZCL");// 列表中SYBZCL列值作为 隐藏值
        gcfg.put("panelWidth", 200);
        return gcfg;
    }

    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("包装材料编号");
        list.add("包装材料名称");
        return list;
    }

    protected List<Map<String, Object>> getColModel() {
        List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String, Object> item =null;

        item = new HashMap<String, Object>();
        item.put("name", "SYBZCLBH");
        item.put("width", 100);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "SYBZCL");
        item.put("width", 100);
        colModel.add(item);

        return colModel;
    }
}

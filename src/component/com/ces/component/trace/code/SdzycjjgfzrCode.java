package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dearest on 2017/2/10.
 */
public class SdzycjjgfzrCode  extends CodeApplication {
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
        //设置需要显示的列名
        gcfg.put("colNames", getColNames());
        //设置需要显示的字段
        gcfg.put("colModel", getColModel());
        //获得对应的数据
        gcfg.put("url", "zzgzryda!getGzrydaGrid.json");
        gcfg.put("gridOptions", "{ajaxGridOptions:{async:false}}");
        //设置点击是，需要获得的是那个数据
        gcfg.put("valueField", "XM");//控制隐藏数据：Key
        gcfg.put("textField", "XM");//控制显示的字段：value
        gcfg.put("panelWidth", 332);
        return gcfg;
    }

    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("人员编号");
        list.add("姓名");
        list.add("岗位");
        list.add("联系方式");
        return list;
    }

    protected List<Map<String, Object>> getColModel() {
        List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String, Object> item =null;

        item = new HashMap<String, Object>();
        item.put("name", "GZRYBH");
        item.put("width", 96);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "XM");
        item.put("width", 60);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "GW");
        item.put("width", 84);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "LXFS");
        item.put("width", 82);
        colModel.add(item);

        return colModel;
    }
}

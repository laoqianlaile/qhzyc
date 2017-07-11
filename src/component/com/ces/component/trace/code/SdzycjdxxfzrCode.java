package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Neptune on 2016/4/22.
 */
public class SdzycjdxxfzrCode extends CodeApplication {

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
        gcfg.put("url", "zzgzryda!getGzrydaGrid.json");
        gcfg.put("gridOptions", "{ajaxGridOptions:{async:false}}");
        gcfg.put("valueField", "XM");// 列表中PFSBM列值作为 显示值
        gcfg.put("textField", "XM");// 列表中PFSMC列值作为 隐藏值
        gcfg.put("panelWidth", 332);

        return gcfg;
    }

    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("工作人员编号");
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

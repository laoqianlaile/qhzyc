package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 办公 on 2016/5/11.
 */
public class SdzcycjgcjxxCode extends CodeApplication {
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
        gcfg.put("url", "sdzyccjgcjxx!searchcjxx.json");
        gcfg.put("panelWidth",280);
        gcfg.put("valueField", "CJMC");// 列表中PFSBM列值作为 显示值
        gcfg.put("textField", "CJMC");// 列表中PFSMC列值作为 隐藏值
        return gcfg;
    }

    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("车间编号");
        list.add("车间名称");

      /*  list.add("仓库名称");*/

        return list;
    }

    protected List<Map<String, Object>> getColModel() {
        List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String, Object> item =null;

       /* item = new HashMap<String, Object>();
        item.put("name", "CKBH");
        item.put("width", 120);
        colModel.add(item);*/

        item = new HashMap<String, Object>();
        item.put("name", "CJBH");
        item.put("width", 120);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "CJMC");
        item.put("width", 120);
        colModel.add(item);

        return colModel;
    }
}

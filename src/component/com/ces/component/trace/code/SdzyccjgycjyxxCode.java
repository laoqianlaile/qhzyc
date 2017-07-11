package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 办公 on 2016/4/27.
 */
public class SdzyccjgycjyxxCode extends CodeApplication {
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
        gcfg.put("url", "sdzyccjgycyxx!searchycjyxx.json");
        gcfg.put("panelWidth",500);
        gcfg.put("valueField", "JGPCH");// 列表中PFSBM列值作为 隐藏值
        gcfg.put("textField", "QYJGPCH");// 列表中PFSMC列值作为 显示值
        return gcfg;
    }

    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("批次号");
        list.add("批次号");
        list.add("药材名称");
        list.add("原料产地");
        list.add("入库重量");
        list.add("采收批次号");
        list.add("包装规格");
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
        item.put("name", "QYJGPCH");
        item.put("width", 120);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "JGPCH");
        item.put("hidden",true);
        item.put("width", 250);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "YLMC");
        item.put("width", 120);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "YLCD");
        item.put("width", 120);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "JGZZL");
        item.put("width", 120);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "YLPCH");
        item.put("hidden", true);
        item.put("width", 120);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "BZGG");
        item.put("hidden", true);
        item.put("width", 120);
        colModel.add(item);

        return colModel;
    }
}


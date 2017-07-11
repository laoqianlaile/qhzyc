package com.ces.component.trace.code;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Synge on 2015/8/28.
 */
public class SdzycjjgyccgpchCode extends CodeApplication {

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
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        List<Code> list = new ArrayList<Code>();
        List<Map<String,Object>> dataMap = DatabaseHandlerDao.getInstance().queryForMaps("select t.QYPCH  from v_sdzyc_jjg_ylkcglxx t where kc > 0  AND t.qybm='"+qybm+"'  order by create_time desc");
        if(dataMap != null  && !dataMap.isEmpty()){
            int showOrder = 1;
            for(Map<String,Object> map :dataMap){
                Code code = new Code();
                code.setName(map.get("QYPCH").toString());
                code.setValue(map.get("QYPCH").toString());
                list.add(code);
            }
        }
        return list;

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
        gcfg.put("url", "sdzycscllxx!searchQypchGridData.json");
        gcfg.put("panelWidth",450);
        gcfg.put("valueField", "PCH");// 列表中PFSBM列值作为 隐藏值
        gcfg.put("textField", "QYPCH");// 列表中PFSMC列值作为 显示值
        return gcfg;
    }

    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("批次号");
        list.add("原料批次号");
        list.add("原料名称");
        list.add("库存重量");
//        list.add("仓库名称");
        list.add("药材代码");
        list.add("产地");
        //list.add("采收批次号");
        return list;
    }

    protected List<Map<String, Object>> getColModel() {
        List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String, Object> item =null;

        item = new HashMap<String, Object>();
        item.put("name", "QYPCH");
        item.put("width", 120);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "PCH");
        item.put("hidden",true);
        item.put("width", 200);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "YCMC");
        item.put("width", 120);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "KC");
        item.put("width", 120);
        colModel.add(item);

//        item = new HashMap<String, Object>();
//        item.put("name", "SLCK");
//        item.put("width", 120);
//        item.put("hidden",true);
//        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "YCDM");
        item.put("width", 120);
        item.put("hidden",true);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "CD");
        item.put("width", 80);
        colModel.add(item);

//        item = new HashMap<String, Object>();
//        item.put("name", "CSPCH");
//        item.put("width", 120);
//        item.put("hidden",true);
//        colModel.add(item);

        return colModel;
    }
}



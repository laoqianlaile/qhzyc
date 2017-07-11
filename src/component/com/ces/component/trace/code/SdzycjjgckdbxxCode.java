package com.ces.component.trace.code;

import com.ces.component.trace.utils.DataDictionaryUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 精加工仓库信息编码业务表
 * Created by wngyu on 15/11/27.
 */
public class SdzycjjgckdbxxCode extends CodeApplication {

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
        String sql="select t.CKBH,t.CKMC from T_SDZYC_JJG_CKXX t where t.qybm = '"+ SerialNumberUtil.getInstance().getCompanyCode()+"'";
        List<Map<String,Object>> maps= DatabaseHandlerDao.getInstance().queryForMaps(sql);
        List<Code> dataList=new ArrayList<Code>();
        Code code = null;
        int i = 0;
        for (Map<String,Object> map : maps) {
            code = new Code();
            code.setCodeTypeCode(codeTypeCode);
            code.setValue(map.get("CKMC").toString());
            code.setName(map.get("CKMC").toString());
            code.setShowOrder(i + 1);
            dataList.add(code);
            i++;
        }
        return dataList;
    }

    @Override
    public Object getCodeTree(String codeTypeCode) {
        return  null;
    }

    @Override
    public Object getCodeGrid(String codeTypeCode) {
        Map<String, Object> ckxx = new HashMap<String, Object>();
        ckxx.put("colNames", getColNames());
        ckxx.put("colModel", getColModel());
        ckxx.put("url", "sdzycjjgckxx!getCkxx.json");
        ckxx.put("valueField", "CKMC");
        ckxx.put("textField", "CKMC");
        ckxx.put("panelWidth", 300);
        return ckxx;
    }
    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("仓库编码");
        list.add("仓库名称");
        return list;
    }

    protected List<Map<String, Object>> getColModel() {
        List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String, Object> item =null;

        item = new HashMap<String, Object>();
        item.put("name", "CKBH");
        item.put("width", 200);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "CKMC");
        item.put("width", 200);
        colModel.add(item);

        return colModel;
    }
}


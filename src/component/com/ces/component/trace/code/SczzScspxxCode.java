package com.ces.component.trace.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;

public class SczzScspxxCode extends CodeApplication{

	@Override
	public String getCodeValue(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCodeName(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Code> getCodeList(String codeTypeCode) {
//		List<Code>  codeList =  new ArrayList<Code>();
//		Code code = new Code();
//		code.setName(name);
//		code.setValue(value);
//		code.setShowOrder(showOrder);
        String sql="select t.SPMC,t.SPBM from t_common_scspxx t";
        List<Map<String,Object>> maps= DatabaseHandlerDao.getInstance().queryForMaps(sql);
        List<Code> dataList=new ArrayList<Code>();
        Code code = null;
        int i = 0;
        for (Map<String,Object> map : maps) {
            code = new Code();
            code.setCodeTypeCode(codeTypeCode);
            code.setValue(map.get("SPMC").toString());
            code.setName(map.get("SPBM").toString());
            code.setShowOrder(i + 1);
            dataList.add(code);
            i++;
        }
        return dataList;
	}

	@Override
	public Object getCodeTree(String codeTypeCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getCodeGrid(String codeTypeCode) {
		// TODO Auto-generated method stub
		Map<String, Object> gcfg = new HashMap<String, Object>();
		gcfg.put("colNames", getColNames());
		gcfg.put("colModel", getColModel());
		gcfg.put("url", "szzzdpz!getSpxx.json");
		gcfg.put("valueField", "SPBM");
		gcfg.put("textField", "SPMC");
		gcfg.put("panelWidth", 300);
		return gcfg;
	}
	
	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("商品编码");
        list.add("商品名称");
        return list;
    }
	
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;
		
		item = new HashMap<String, Object>();
		item.put("name", "SPBM");
		item.put("width", 200);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "SPMC");
		item.put("width", 200);
		colModel.add(item);

        return colModel;
    }
}

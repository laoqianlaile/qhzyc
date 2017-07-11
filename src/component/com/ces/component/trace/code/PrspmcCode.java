package com.ces.component.trace.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

public class PrspmcCode extends CodeApplication{

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
		// TODO Auto-generated method stub
		return null;
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
		gcfg.put("url", "prrpjcxx!getSpmcGrid.json");
		gcfg.put("valueField", "SPBM");// 列表中PFSBM列值作为 显示值
		gcfg.put("textField", "SPMC");// 列表中PFSMC列值作为 隐藏值
		gcfg.put("panelWidth", 250);
		return gcfg;
	}
	/**
	 * 获取列名
	 * @return
	 */
	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("商品编码");
        list.add("商品代码");
        list.add("商品名称");
        list.add("商品别名");
        
        return list;
    }
	
	/**
	 * 获取字段模板
	 * @return
	 */
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;
		
		item = new HashMap<String, Object>();
		item.put("name", "SPBM");
		item.put("width", 120);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "SPMC");
		item.put("width", 130);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "SPBM2");
		item.put("width", 125);
        item.put("hidden" , true);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "SPDM");
		item.put("width", 125);
        item.put("hidden" , true);
		colModel.add(item);

        return colModel;
    }

}

package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄翔宇 on 15/6/12.
 * 加工原料批次
 */
public class JgylpcCode extends CodeApplication {
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
		gcfg.put("panelWidth", 500);
		gcfg.put("colNames", getColNames());
		gcfg.put("colModel", getColModel());
		//	gcfg.put("url", "jgddxx!getDdxxGrid.json");
		gcfg.put("valueField", "YLPCH");
		gcfg.put("textField", "YLPCH");
		return gcfg;
	}

	protected List<String> getColNames() {
		List<String> list = new ArrayList<String>();
		list.add("原料批次号");
		list.add("进场批次号");
		list.add("原料名称");
		list.add("供应商名称");
		list.add("产地名称");
		return list;
	}

	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = null;

		item = new HashMap<String, Object>();
		item.put("name", "YLPCH");
		item.put("width", 100);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "JCPCH");
		item.put("width", 100);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "YLMC");
		item.put("width", 100);
		colModel.add(item);


		item = new HashMap<String, Object>();
		item.put("name", "GYSMC");
		item.put("width", 100);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "CDMC");
		item.put("width", 100);
		colModel.add(item);

		return colModel;
	}

}

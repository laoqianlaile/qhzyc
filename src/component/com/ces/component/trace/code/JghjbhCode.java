package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄翔宇 on 15/6/13.
 * 加工环节编号
 */
public class JghjbhCode extends CodeApplication {
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
		gcfg.put("url", "jgxxxz!getAllJghjbh.json");
		gcfg.put("valueField", "JGHJMC");
		gcfg.put("textField", "JGHJBH");
		gcfg.put("panelWidth", 400);
		return gcfg;
	}

	protected List<String> getColNames() {
		List<String> list = new ArrayList<String>();
		list.add("加工环节编号");
		list.add("加工环节名称");
		list.add("加工环节说明");
		return list;
	}

	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = null;

		item = new HashMap<String, Object>();
		item.put("name", "JGHJBH");
		item.put("width", 100);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "JGHJMC");
		item.put("width", 150);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "JGHJSM");
		item.put("width", 150);
		colModel.add(item);

		return colModel;
	}

}

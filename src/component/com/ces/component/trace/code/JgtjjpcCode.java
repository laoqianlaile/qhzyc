package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄翔宇 on 15/6/13.
 * 加工添加剂批次
 */
public class JgtjjpcCode extends CodeApplication {
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
//		gcfg.put("url", "jgxxxz!getAllTjjpc.json?qyzt=2");
		gcfg.put("valueField", "TJJPCH");
		gcfg.put("textField", "TJJPCH");
        gcfg.put("panelWidth", 450);
		return gcfg;
	}

	protected List<String> getColNames() {
		List<String> list = new ArrayList<String>();
		list.add("添加剂批次号");
		list.add("添加剂编号");
		list.add("添加剂通用名");
		list.add("添加剂全称");
		list.add("添加剂类型");
		//list.add("岗位");

		return list;
	}

	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;

		item = new HashMap<String, Object>();
		item.put("name", "TJJPCH");
		item.put("width", 100);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "TJJBH");
		item.put("width", 100);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "TJJTYM");
		item.put("width", 100);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "TJJQC");
		item.put("width", 100);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "TJJLX");
		item.put("width", 100);
		colModel.add(item);

		return colModel;
	}

}

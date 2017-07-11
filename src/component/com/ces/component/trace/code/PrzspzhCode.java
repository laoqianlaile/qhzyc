package com.ces.component.trace.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

public class PrzspzhCode extends CodeApplication {

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
		gcfg.put("url", "prjcxx2!getZspzhGrid.json?pfsbm=");
		gcfg.put("valueField", "ZSPZH");// 列表中ZSPZH列值作为 显示值
		gcfg.put("textField", "ZSPZH");// 列表中ZSPZH列值作为 隐藏值
		gcfg.put("panelWidth", 450);
		return gcfg;
	}
	/**
	 * 获取列名
	 * @return
	 */
	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("追溯凭证号");
        list.add("批发商编码");
        list.add("批发商名称");
        list.add("肉品进场日期");
        
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
		item.put("name", "ZSPZH");
		item.put("width", 100);
		colModel.add(item);
		item = new HashMap<String, Object>();
		item.put("name", "PFSBM");
		item.put("width", 100);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "PFSMC");
		item.put("width", 100);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "JCRQ");
		item.put("width", 100);
		colModel.add(item);
		
        return colModel;
    }

}

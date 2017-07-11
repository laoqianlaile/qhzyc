package com.ces.component.trace.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;
/**
 * <h2>上海中信信息发展股份有限公司</h2>
 * 
 * @author Hpu.Plain
 * 
 * @version V1.0
 * 
 * @category 复合框下拉列表数据：<br>生猪养殖-仔猪批次号（仔猪档案）
 * 
 */
public class YzzzpchCode extends CodeApplication {

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
		//设置下拉列表的显示字段名
		gcfg.put("colNames", getColNames());
		//设置下拉列表的对应的数据
		gcfg.put("colModel", getColModel());
		//获得数据
		gcfg.put("url", "yzzzda!getZzdaGrid.json");
		//设置点击是，需要获得的是那个数据
		gcfg.put("valueField", "ZZPCH");//控制隐藏的数据：keyZZPCH
		gcfg.put("textField", "ZZPCH"); //控制显示的数据：value
		gcfg.put("panelWidth", 500);
		return gcfg;
	}
	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("仔猪批次号");
        list.add("品种通用名");
        list.add("品种全称");
        list.add("引进日期");
        return list;
    }
	
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;
		
		item = new HashMap<String, Object>();
		item.put("name", "ZZPCH");
		item.put("width", 100);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "PZTYM");
		item.put("width", 150);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "PZQC");
		item.put("width", 150);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "YJRQ");
		item.put("width", 100);
		colModel.add(item);
        return colModel;
    }

}

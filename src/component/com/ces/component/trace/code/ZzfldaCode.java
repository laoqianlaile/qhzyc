package com.ces.component.trace.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;
/**
 * 上海中信信息发展股份有限公司
 * 
 * @author Hpu.Plain
 * 
 * @version V1.0
 * 
 * @category 复合框下拉列表数据：蔬菜种植-使用肥料（肥料档案）
 * 
 */
public class ZzfldaCode extends CodeApplication{

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
		gcfg.put("url", "zzflda!getFldaGrid.json");
		//设置点击是，需要获得的是那个数据  textField 显示的数据，和隐藏数据
		gcfg.put("valueField", "FLBH");//隐藏数据
		gcfg.put("textField", "FLTYM");//显示数据
		gcfg.put("panelWidth",500);
		return gcfg;
	}
	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("肥料编号");
        list.add("肥料通用名");
        list.add("肥料全称");
        list.add("入库日期");
        return list;
    }
	
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;
		
		item = new HashMap<String, Object>();
		item.put("name", "FLBH");
		item.put("width", 100);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "FLTYM");
		item.put("width", 150);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "FLQC");
		item.put("width", 150);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "RKRQ");
		item.put("width", 100);
		colModel.add(item);
        return colModel;
    }

}

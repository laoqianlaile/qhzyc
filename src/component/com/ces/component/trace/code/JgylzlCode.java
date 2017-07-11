package com.ces.component.trace.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

/**
 * @author Hpu.Plain
 * 
 * 
 * 	原料材料种类复合框下拉列表数据
 *
 */
public class JgylzlCode extends CodeApplication{
	@Override
	public List<Code> getCodeList(String codeTypeCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCodeName(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getCodeTree(String codeTypeCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCodeValue(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getCodeGrid(String codeTypeCode) {
		// TODO Auto-generated method stub
		Map<String, Object> gcfg = new HashMap<String, Object>();
		gcfg.put("colNames", getColNames());
		gcfg.put("colModel", getColModel());
		gcfg.put("url", "jgylzlda!getYlzlGrid.json");
		gcfg.put("valueField", "YLBH");// 列表中BZCLBH列值作为 显示值
		gcfg.put("textField", "YLMC");// 列表中BZCLMC列值作为 隐藏值
        gcfg.put("panelWidth",300);
		return gcfg;
	}
	
	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("原料编号");
        list.add("原料名称");
       // list.add("原料类型");
      //  list.add("原料商品名称");
        
        return list;
    }
	
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;
		
		item = new HashMap<String, Object>();
		item.put("name", "YLBH");
		item.put("width", 150);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "YLMC");
		item.put("width", 150);
		colModel.add(item);
		
//		item = new HashMap<String, Object>();
//		item.put("name", "YLLX");
//		item.put("width", 100);
//		colModel.add(item);
//		
//		item = new HashMap<String, Object>();
//		item.put("name", "YLSPMC");
//		item.put("width", 100);
//		colModel.add(item);
		
        return colModel;
    }
}

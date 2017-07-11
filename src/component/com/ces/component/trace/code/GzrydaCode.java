package com.ces.component.trace.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

public class GzrydaCode extends CodeApplication {

	@Override
	public List<Code> getCodeList(String codeTypeCode) {
		String sql="select t.XM,t.GZRYBH from T_PC_GZRYDA t";
		//尚未添加所属机构过滤
		List<Object[]> list=com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao.getInstance().queryForList(sql);
		List<Code> dataList=new ArrayList<Code>();
		int i = 0, len =list.size();
		Code code = null;
		for (; i < len; i++) {
			Object[] dataMap=list.get(i);
			code = new Code();
			code.setCodeTypeCode(codeTypeCode);
			code.setValue(dataMap[1].toString());
			code.setName(dataMap[0].toString());
			code.setShowOrder(i + 1);
			dataList.add(code);
		}
		return dataList;
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
		gcfg.put("url", "tcsgzry!getGzryda.json"); 
		gcfg.put("valueField", "GZRYBH");
		gcfg.put("textField", "XM");
		gcfg.put("panelWidth",300);
		return gcfg;
	}
	
	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("工作人员编号");
        list.add("姓名");
        list.add("岗位");
        return list;
    }
	
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;
		
		item = new HashMap<String, Object>();
		item.put("name", "GZRYBH");
		item.put("width", 100);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "XM");
		item.put("width", 100);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "GW");
		item.put("width", 100);
		colModel.add(item);

        return colModel;
    }
}

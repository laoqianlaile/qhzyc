package com.ces.component.trace.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ces.sdk.system.bean.OrgInfo;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;
public class PfsmcCode extends CodeApplication  {

	@Override
	public List<Code> getCodeList(String codeTypeCode) {
		//1.数据权限过滤   2.是否排序  3.状态是否判断
		String sql="select * from (select t.JYZMC,t.JYZBM from T_PC_JYZDA  t where t.zt=1 )";
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
		gcfg.put("url", "tcspfs!getJyzdaGrid.json");
		gcfg.put("valueField", "PFSBM");// 列表中PFSBM列值作为 显示值
		gcfg.put("textField", "PFSMC");// 列表中PFSMC列值作为 隐藏值
		return gcfg;
	}
	
	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("批发商编码");
        list.add("批发商名称");
        list.add("经营类型");
        
        return list;
    }
	
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;
		
		item = new HashMap<String, Object>();
		item.put("name", "PFSBM");
		item.put("width", 100);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "PFSMC");
		item.put("width", 100);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "JYLX");
		item.put("width", 100);
		colModel.add(item);
		
        return colModel;
    }
}

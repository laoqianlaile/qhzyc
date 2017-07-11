package com.ces.component.trace.code;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;

public class QyptcpmydCode extends CodeApplication {

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
		String sql="select * from T_COMMON_SJLX_CODE t where lxbm='CPMYD'";

		List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		List<Code> dataList=new ArrayList<Code>();
		int i = 0;
		Code code = null;
		for (Map<String, Object> data : list) {
			code = new Code();
			code.setCodeTypeCode(codeTypeCode);
			code.setValue(String.valueOf(data.get("SJBM")));
			code.setName(String.valueOf(data.get("SJMC")));
			code.setShowOrder(++i);
			dataList.add(code);
		}
		return dataList;
	}

	@Override
	public Object getCodeTree(String codeTypeCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getCodeGrid(String codeTypeCode) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

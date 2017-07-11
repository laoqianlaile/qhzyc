package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄翔宇 on 15/4/27.
 * 企业平台基本服务编码表
 */
public class QyptfjbfwCode extends CodeApplication {

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
		String sql="select t.fwbh,t.fwmc from T_QYPT_FWGL t where t.fwlx = '1'";

		List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		List<Code> dataList=new ArrayList<Code>();
		int i = 0;
		Code code = null;
		for (Map<String, Object> data : list) {
			code = new Code();
			code.setCodeTypeCode(codeTypeCode);
			code.setValue(String.valueOf(data.get("FWBH")));
			code.setName(String.valueOf(data.get("FWMC")));
			code.setShowOrder(++i);
			dataList.add(code);
		}
		return dataList;
	}

	@Override
	public Object getCodeTree(String codeTypeCode) {
		return null;
	}

	@Override
	public Object getCodeGrid(String codeTypeCode) {
		return null;
	}
}

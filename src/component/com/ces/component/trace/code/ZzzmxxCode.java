package com.ces.component.trace.code;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZzzmxxCode extends CodeApplication {

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
		String qybm = SerialNumberUtil.getInstance().getCompanyCode();
		String sql="select t.ZZZMMC,t.ZZZMBH from t_sdzyc_zzzm t where t.qybm = ? and t.is_delete<>1";
		List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[] {qybm});
		List<Code> dataList=new ArrayList<Code>();
		int i = 0, len =maps.size();
		Code code = null;
		for (; i < len; i++) {
			Map<String,Object> dataMap=maps.get(i);
			code = new Code();
			code.setCodeTypeCode(codeTypeCode);
			code.setValue(dataMap.get("ZZZMBH").toString());
			code.setName(dataMap.get("ZZZMMC").toString());
			code.setShowOrder(i + 1);
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
		return null;
	}
	
	protected List<String> getColNames() {
        return null;
    }
	
	protected List<Map<String, Object>> getColModel() {
        return null;
    }
}

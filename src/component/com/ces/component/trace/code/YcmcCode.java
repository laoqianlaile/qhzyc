package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YcmcCode extends CodeApplication{
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
		List<Code> list = new ArrayList<Code>();
		List<Map<String,Object>> dataMap = DatabaseHandlerDao.getInstance().queryForMaps("select ZSSPM value  ,YCMNAME name from t_sdzyc_zycspbm");
		if(dataMap != null  && !dataMap.isEmpty()){
			int showOrder = 1;
			for(Map<String,Object> map :dataMap){
				Code code = new Code();
				code.setName(map.get("NAME").toString());
				code.setValue(map.get("VALUE").toString());
//                code.setShowOrder(Integer.parseInt(String.valueOf(map.get("SHOW_ORDER")) == "null" ? "0":String.valueOf(map.get("SHOW_ORDER"))));
				list.add(code);
			}
		}
		return list;
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

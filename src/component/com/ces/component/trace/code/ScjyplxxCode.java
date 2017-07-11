package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScjyplxxCode extends CodeApplication{
	private static Log log = LogFactory.getLog(ScjyplxxCode.class);

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
		List<Code> list = new ArrayList<Code>();
		String sql ="select t.SPMC from T_SC_JYPLXX t";
		try {
			List<Map<String,Object>> data = DatabaseHandlerDao.getInstance().queryForMaps(sql);
			int i = 0, len = data.size();
			Code code = null;
			for (; i < len; i++) {
				Map sp = data.get(i);
				code = new Code();
				code.setCodeTypeCode(codeTypeCode);
				code.setValue(String.valueOf(sp.get("SPMC")));
				code.setName(String.valueOf(sp.get("SPMC")));
				code.setShowOrder(i + 1);
				list.add(code);
			}
		} catch (Exception e) {
			log.error("获取商品信息失败！", e);
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
		// TODO Auto-generated method stub
		Map<String, Object> gcfg = new HashMap<String, Object>();
		gcfg.put("colNames", getColNames());
		gcfg.put("colModel", getColModel());
		gcfg.put("url", "tcsscjyplxx!getJyplGrid.json");
		gcfg.put("valueField", "SPBM");
		gcfg.put("textField", "SPMC");
		gcfg.put("panelWidth", 250);
        gcfg.put("multiple",true);
		return gcfg;
	}
	
	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("商品编码");
        list.add("商品名称");
        return list;
    }
	
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;
		
		item = new HashMap<String, Object>();
		item.put("name", "SPBM");
		item.put("width", 110);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "SPMC");
		item.put("width", 140);
		colModel.add(item);

        return colModel;
    }
}

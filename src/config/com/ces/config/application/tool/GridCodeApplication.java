/**
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * qiucs 2015-4-7 上午10:26:45
 * <p>描述: 组织编码工具类</p>
 */
package com.ces.config.application.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

/**
 * @author qiucs
 *
 */
public class GridCodeApplication extends CodeApplication {
	
	private static Log log = LogFactory.getLog(GridCodeApplication.class);

	/* (non-Javadoc)
	 * @see com.ces.config.application.CodeApplication#getCodeValue(java.lang.String)
	 */
	@Override
	public String getCodeValue(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ces.config.application.CodeApplication#getCodeName(java.lang.String)
	 */
	@Override
	public String getCodeName(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ces.config.application.CodeApplication#getCodeList()
	 */
	@Override
	public List<Code> getCodeList(String codeTypeCode) {
		List<Code> list = new ArrayList<Code>();
		return list;
	}

	/* (non-Javadoc)
	 * @see com.ces.config.application.CodeApplication#getCodeTree()
	 */
	@Override
	public Object getCodeTree(String codeTypeCode) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ces.config.application.CodeApplication#getCodeTree()
	 */
	@Override
	public Object getCodeGrid(String codeTypeCode) {
		Map<String, Object> gcfg = new HashMap<String, Object>();
		gcfg.put("colNames", getColNames());
		gcfg.put("colModel", getColModel());
		// 如果是动态数据，下拉列表数据从后台获取
		// {contextPath}是项目的上下文点位符
		//gcfg.put("url", "{contextPath}/show-module!combogrid.json?codeTypeCode=" + codeTypeCode);
		// {action}是action点位符 是具体模块的对应的action
		gcfg.put("url", "{action}!test.json?codeTypeCode=" + codeTypeCode);
		// 如果是静态数据，可以直接写死
		//gcfg.put("data", getGridData());
		
		gcfg.put("valueField", "id");  // 列表中id列值作为 隐藏值
		gcfg.put("textField", "name"); // 列表中name列值作为 显示值
		gcfg.put("panelWidth", 800); // 下拉列表面板的宽度设置
		return gcfg;
	}
	
	/**
	 * qiucs 2015-4-7 下午3:12:51
	 * <p>描述: 列表配置信息 </p>
	 * @return List<Map<String,Object>>
	 */
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;

		item = new HashMap<String, Object>();
		item.put("name", "id");
		item.put("key", true);
		item.put("hidden", true);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "code");
		item.put("width", 100);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "simpleCode");
		item.put("width", 100);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "name");
		item.put("width", 160);
		colModel.add(item);
		
        return colModel;
    }
	
	/**
	 * qiucs 2015-4-7 下午3:22:12
	 * <p>描述: 列表表头信息 </p>
	 * @return List<String>
	 */
	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("id");
        list.add("编码");
        list.add("简码");
        list.add("名称");
        return list;
    }
	
	/**
	 * qiucs 2015-4-7 下午3:42:53
	 * <p>描述: 列表数据 </p>
	 * @return Object
	 */
	protected Object getGridData() {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("id", "111_id");
		item.put("code", "111_code");
		item.put("simpleCode", "111_simpleCode");
		item.put("name", "111_name");
		data.add(item);
		return data;
	}

}

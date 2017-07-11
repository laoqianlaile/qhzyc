package com.ces.config.datamodel.page.coral;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ces.config.application.CodeApplication;
import com.ces.config.datamodel.option.OptionModel;
import com.ces.config.datamodel.page.DefaultPageModel;
import com.ces.config.dhtmlx.entity.code.BusinessCode;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.service.code.BusinessCodeService;
import com.ces.config.utils.CodeUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.authority.AuthorityUtil;
import com.ces.xarch.core.web.listener.XarchListener;
/**
 * <p>描述: 代码项下拉框</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2014-7-17 上午10:12:34
 *
 */
public class CodePageModel extends DefaultPageModel {
	
	private static Log log = LogFactory.getLog(CodePageModel.class);
    /** 选中选项参数名称. */
    public static final String P_SELECTED = "P_SELECTED";
    
    public static final String P_EMPTY = "P_EMPTY";
    
    public static final String P_COMBO_TYPE = "P_COMBO_TYPE";
    
    public static final String COMBOBOX  = "combobox";
    public static final String COMBOTREE = "combotree";
    public static final String COMBOGRID = "combogrid";
    
    private String id;
    
    private boolean empty;
    
    private String selected;
    
    private String comboType;
    
    
    public void init() {
    	if (COMBOTREE.equals(getComboType())) {
    		setData(cast2tree());
    	} else if (COMBOGRID.equals(getComboType())) {
			setData(cast2grid());
		} else {
    		setData(cast2options());
    	}
    }
    
    /**
     * qiucs 2014-7-17 
     * <p>描述: 代码项转换成下拉框选项</p>
     * @return Object    返回类型   
     * @throws
     */
    protected Object cast2options() {
        String key = getId();
        String typeName = CodeUtil.getInstance().getCodeTypeMap().get(key);
        if (StringUtil.isEmpty(typeName)) {
            return new ArrayList<OptionModel>();
        }
        List<OptionModel> list = new ArrayList<OptionModel>();
        OptionModel option = new OptionModel();
//        if (isEmpty()) {
//            option.setValue("");
//            option.setText("请选择" + typeName);
//            option.setSelected(StringUtil.isEmpty(getSelected()) ? Boolean.TRUE : Boolean.FALSE);
//            list.add(option);
//        }
        
        //List<Code> codes = CodeUtil.getInstance().getCodeList(key);
        List<Code> codes = AuthorityUtil.getInstance().getCodeAuthority(getMenuId(), getComponentVersionId(), key);
        if (null != codes) {
        	boolean isAuth = "AUTH_USER".equals(key);
        	String sysKey = null;
        	if (isAuth) sysKey = String.valueOf(Integer.MIN_VALUE + 1);
        	for (Code code : codes) {
        		if (isAuth && sysKey.equals(code.getValue())) continue;
                option = new OptionModel();
                option.setValue(code.getValue());
                option.setText(code.getName());
                option.setSelected(isSelected(code.getValue()));
                list.add(option);
            }
        }
        
        return (list);
    }
    
    /**
     * qiucs 2015-1-22 下午2:58:45
     * <p>描述: 编码下拉树 </p>
     * @return Object
     */
    protected Object cast2tree() {
    	Object ct = CodeUtil.getInstance().getCodeType(getId());
    	if (StringUtil.isEmpty(ct)) return new ArrayList<Object>();
    	
    	return CodeUtil.getInstance().getCodeTree(getId());
    }
    
    protected Object cast2grid() {
    	String codeTypeCode = getId();
    	Object gcfg = null;
    	BusinessCode bc = XarchListener.getBean(BusinessCodeService.class).getByCodeTypeCode(codeTypeCode);
    	String className = (null != bc ? bc.getClassName() : null);
    	if (StringUtil.isEmpty(className)) {
    		log.warn("下拉列表未配置，请检查！");
    		return null;
    	}
    	try {
			CodeApplication application = (CodeApplication) Class.forName(className).newInstance();
			gcfg = application.getCodeGrid(codeTypeCode);
			if (null != gcfg) {
				Map<String, Object> gMap = (Map<String, Object>)gcfg;
				if (gMap.containsKey("url")) {
					StringBuilder url = new StringBuilder(gMap.get("url").toString());
					if (url.indexOf("?") > 0) url.append("&");
					else url.append("?");
					url.append("P_tableId=").append(getTableId()).append("&P_menuId=").append(getMenuId()).append("&P_pageType=grid");
					gMap.put("url", url.toString().replace("{action}", "appmanage/show-module"));
				}
			}
		} catch (Exception e) {
			log.error("获取下拉列表配置信息出错！");
		}
    	return gcfg;
    }

    public String getComboType() {
		return comboType;
	}
	public void setComboType(String comboType) {
		this.comboType = comboType;
	}
	public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public boolean isEmpty() {
        return empty;
    }
    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
    public String getSelected() {
        return selected;
    }
    public void setSelected(String selected) {
        this.selected = selected;
    }
    private boolean isSelected(String value) {
        if (StringUtil.isNotEmpty(getSelected()) && getSelected().equals(value)) {
            return true;
        }
        return false;
    }
}

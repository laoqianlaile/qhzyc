package com.ces.component.cyrpjcxx.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ces.component.cyrpjcxx.dao.CyrpjcxxDao;
import com.ces.component.cyrpjcxx.service.CyrpjcxxService;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class CyrpjcxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CyrpjcxxService, CyrpjcxxDao> {

    private static final long serialVersionUID = 1L;
    
    /*
     * (非 Javadoc)   
     * <p>标题: initModel</p>   
     * <p>描述: </p>      
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }
    public Object add(){
    	Map<String,String> dataMap = new HashMap<String,String>();
    	dataMap.put("CYXFDWBM", SerialNumberUtil.getInstance().getCompanyCode());
    	dataMap.put("CYXFDWMC", CompanyInfoUtil.getInstance().getCompanyName("CY", SerialNumberUtil.getInstance().getCompanyCode()));
    	dataMap.put("JCRQ", getService().dateToString(new Date()));
    	dataMap.put("GYSBM", this.getRequest().getParameter("GYSBM"));
    	dataMap.put("GYSMC", this.getRequest().getParameter("GYSMC"));
    	dataMap.put("SPBM", this.getRequest().getParameter("SPBM"));
    	dataMap.put("SPMC", this.getRequest().getParameter("SPMC"));
    	dataMap.put("ZL", this.getRequest().getParameter("ZL"));
    	dataMap.put("DJ", this.getRequest().getParameter("DJ"));
    	dataMap.put("JHPCH", this.getRequest().getParameter("JHPCH"));
    	dataMap.put("ZSPZH", this.getRequest().getParameter("ZSPZH"));
    	dataMap.put("GHSCBM", this.getRequest().getParameter("GHSCBM"));
    	dataMap.put("GHSCMC", this.getRequest().getParameter("GHSCMC"));
    	String jcId = getService().save("T_CY_RPJCXX", dataMap, null);
    	dataMap.clear();
    	setReturnData(jcId);
    	return null;
    }
    
    //获得所有菜品名称
    public Object getAllSpmc(){
    	super.setReturnData(getService().getSpmc());
    	return SUCCESS;
    }
    
    public Object barCodeSave() {
		String barCode = getParameter("barCode");
		setReturnData(getService().barCodeSave(barCode));
		return SUCCESS;
	}
}

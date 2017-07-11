package com.ces.component.yzgzryda.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.yzgzryda.service.YzgzrydaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class YzgzrydaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, YzgzrydaService, TraceShowModuleDao> {

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
    public String getYgbm(){
    	super.setReturnData(SerialNumberUtil.getInstance().getSerialNumber("YZ", "GZRYBH", false));
    	return null;
    }
    public void getGzrydaGrid(){
    	String pageType = getParameter("P_pageType");
    	String qyzt = "";
    	if(!"grid".equals(pageType))
    		qyzt="1";
    	this.setReturnData(getService().getGzryda(qyzt));
    }
    

}
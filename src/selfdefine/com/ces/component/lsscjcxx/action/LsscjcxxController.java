package com.ces.component.lsscjcxx.action;

import java.util.HashMap;
import java.util.Map;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.lsscjcxx.service.LsscjcxxService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class LsscjcxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, LsscjcxxService, TraceShowModuleDao> {

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
    //获取进场编号
    public void getJcbh(){
    	super.setReturnData(getService().getJcbh());
    }
    //根据批发商编码获得进场编号
    public void getJcbhByPfsbm(){
    	String pfsbm = this.getRequest().getParameter("pfsbm");
    	super.setReturnData(getService().getJcbhByPfsbm(pfsbm));
    }
}
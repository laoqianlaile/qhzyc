package com.ces.component.zznyda.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.zznyda.dao.ZznydaDao;
import com.ces.component.zznyda.service.ZznydaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZznydaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZznydaService, ZznydaDao> {

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
    public String getNybh(){
    	super.setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ", "NYBH", false));
    	return null;
    }
    
    /**
     * 获得下拉列表农药数据
     */
    public void getNydaGrid(){
    	setReturnData(getService().getNyda());
    }
    
    public void getNcqData(){
    	String qybm=getRequest().getParameter("qybm");
    	String nybh=getRequest().getParameter("nybh");
    	Object ncq=getService().getNcqData(qybm, nybh);
    	setReturnData(ncq);
    }

}

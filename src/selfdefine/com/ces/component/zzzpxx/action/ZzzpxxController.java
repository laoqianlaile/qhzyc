package com.ces.component.zzzpxx.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzzpxx.service.ZzzpxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;

public class ZzzpxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzzpxxService, TraceShowModuleDao> {

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
    
    public void getZzpch(){
    	setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ", "ZZPCH", false));
    }

	@Override
	public Object save() throws FatalException {
		// TODO Auto-generated method stub
		String result=super.save().toString();
		if(result.equals("none")){
			
		}
		return result;
	}
    
	public void getZpxxGrid(){
		setReturnData(getService().getZpxx());
	}
	
	public String getZpxxFzr(){
		String zzpch=getRequest().getParameter("zzpch");
		setReturnData(getService().getZpxxFzr(zzpch));
		return null;
	}
    

}
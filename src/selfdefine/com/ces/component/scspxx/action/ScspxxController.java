package com.ces.component.scspxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.scspxx.service.ScspxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ScspxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ScspxxService, TraceShowModuleDao> {

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
    
    public String getMaxSpdm(){
    	String sql="select Max(SPDM) from  T_COMMON_SCSPXX";
    	Object data=com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao.getInstance().queryForObject(sql);
    	String dataStr=data.toString();
    	int count=0;
    	if(dataStr==null || "".equals(dataStr)){
    		count=1;
    	}else{
    		count=Integer.parseInt(dataStr);
    		count++;
    	}
    	if(count<10){
    		dataStr="00"+count;
    	}else if(count<100){
    		dataStr="0"+count;
    	}
    	setReturnData(dataStr.toString());
    	return null;
    }

}
package com.ces.component.cscpjcxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.cscpjcxx.service.CscpjcxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.Map;

public class CscpjcxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CscpjcxxService, TraceShowModuleDao> {

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

    public void getJgcpchxx(){
        try {
            String jgtmh = getParameter("jgtmh");
            Map<String,Object> result = getService().getJgcpchxx(jgtmh);
            if(result.isEmpty()){
                super.setReturnData("ERROR");
            }else {
                super.setReturnData(result);
            }
        } catch (Exception e) {
            System.out.print(e);
            super.setReturnData("ERROR");
        }
    }
    
    public void getJyzById(){
    	String id= getParameter("id");
    	setReturnData(getService().getJyzById(id));
    }
}
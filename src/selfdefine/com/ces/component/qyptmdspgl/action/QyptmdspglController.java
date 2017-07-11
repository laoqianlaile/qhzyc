package com.ces.component.qyptmdspgl.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.qyptmdspgl.service.QyptmdspglService;
import com.ces.component.trace.utils.JSON;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;

public class QyptmdspglController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, QyptmdspglService, TraceShowModuleDao> {

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

    public Object checkSpid() {
        String spid = getParameter("spid");
        String mdid = getParameter("mdid");
        String spbm = getParameter("spbm");
        List<String> spidList = JSON.fromJSON(spid, new TypeReference<List<String>>() {});
        List<String> spbmList = JSON.fromJSON(spbm, new TypeReference<List<String>>() {});
        setReturnData(getService().checkSpid(spidList,mdid,spbmList));
        return SUCCESS;
    }

}
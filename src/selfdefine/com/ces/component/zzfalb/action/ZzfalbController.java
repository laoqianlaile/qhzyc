package com.ces.component.zzfalb.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzfalb.service.ZzfalbService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

import java.lang.reflect.Array;

public class ZzfalbController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzfalbService, TraceShowModuleDao> {

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

    public Object deleteZzfa() {
        String ids = getParameter("ids");
        String[] idArray = ids.split(",");
        getService().deleteZzfa(idArray);
        return SUCCESS;
    }

    public Object copyZzfa () {
        String dataId = getParameter("dataId");
        setReturnData(getService().copyZzfa(dataId));
        return SUCCESS;
    }

    public Object preView(){
        String kind = getParameter("kind");
        String datepicker = getParameter("datepicker");
        String id = getParameter("id");
        setReturnData(getService().preView(kind, datepicker, id));
        return SUCCESS;
    }

    public Object getZyxbh(){
        String tablename = getParameter("tablename");
        String id = getParameter("id");
        setReturnData(getService().getZyxbh(id, tablename));
        return SUCCESS;
    }
}
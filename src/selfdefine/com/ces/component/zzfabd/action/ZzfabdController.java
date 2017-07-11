package com.ces.component.zzfabd.action;

import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.utils.JSON;
import com.ces.component.zzfabd.service.ZzfabdService;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ZzfabdController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzfabdService, TraceShowModuleDao> {

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

    public Object getPzxxByPlbh(){
        String plbh = getParameter("plbh");
        setReturnData(getService().getPzxxByPlbh(plbh));
        return SUCCESS;
    }
    //获取种苗种子
    public Object getZmzz(){
        setReturnData(getService().getZmzz());
        return SUCCESS;
    }
    //通过ID获取种苗种子
    public Object getZmzzById(){
        String id = getParameter("id");
        setReturnData(getService().getZmzzById(id));
        return SUCCESS;
    }
    //通过ID获取药材名称
    public Object getYcmcById(){
        String id = getParameter("id");
        setReturnData(getService().getYcmcById(id));
        return SUCCESS;
    }

    //保存主从表方法
    public Object saveFaxx () {
        String faxx = getParameter("faxx");
        Map<String, Object> dataMap = JSON.fromJSON(faxx, new TypeReference<Map<String, Object>>() {});
        setReturnData(getService().saveFaxx(dataMap));
        return SUCCESS;
    }

    public Object getYcmc(){

        setReturnData(getService().getYcmc());
        return null;
    }
}
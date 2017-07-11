package com.ces.component.zztrpxxglcz.action;

import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zztrpxxglcz.service.ZztrpxxglczService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.Map;

/**
 * Created by wngyu on 15/11/16.
 */
public class ZztrpxxglczController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZztrpxxglczService, TraceShowModuleDao> {
    private static final long serialVersionUID = 1L;
    @Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }

    public void getLx(){
        setReturnData(getService().getLx());
    }
    public void getFllx(){
        setReturnData(getService().getFllx());
    }
    public void getBzggdw(){
        setReturnData(getService().getBzggdw());
    }

    public String save(){

        String entityJson = getParameter(E_ENTITY_JSON);
        Map<String, String> map = getService().save(entityJson);
        setReturnData(map);
        return SUCCESS;

    }

    public void gettheFormdata(){
        String id=getParameter("id");
        setReturnData(getService().gettheFormdata(id));
    }
}
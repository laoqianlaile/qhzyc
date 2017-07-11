package com.ces.component.yjyaxz.action;

import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.utils.JSON;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.yjyaxz.service.YjyaxzService;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

public class YjyaxzController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, YjyaxzService, TraceShowModuleDao> {

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

    /**
     * 获取预案字段by预案类型
     * @return
     */
    public Object getFieldByYalx () {
        String yalx = getParameter("yalx");
        setReturnData(getService().getFieldByYalx(yalx));
        return SUCCESS;
    }

    /**
     * 保存修改
     * @return
     */
    public Object saveYjya () {
        String yjya = getParameter("yjya");
        Map<String,Object> yjyaMap = JSON.fromJSON(yjya, new TypeReference<Map<String, Object>>() {});
        setReturnData(getService().saveYjya(yjyaMap));
        return SUCCESS;
    }

    public Object getYaData () {
        String yaid = getParameter("yaid");
        setReturnData(getService().getYaData(yaid));
        return SUCCESS;
    }
}
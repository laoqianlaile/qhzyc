package com.ces.component.tyttgj.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tyttgj.service.TyttgjService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TyttgjController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TyttgjService, TraceShowModuleDao> {

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
    public void getTpTableId(){
        //获得前台传过来的父表ID
        String pTableId = getParameter("pTableId");
        //使用通用上传逻辑表  false 不是用方法中传过去的logicTableCode：逻辑表编码
        setReturnData(getService().getTableId(pTableId,null,false));
    }
}
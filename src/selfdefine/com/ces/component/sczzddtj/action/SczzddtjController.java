package com.ces.component.sczzddtj.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.sczzddtj.service.SczzddtjService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SczzddtjController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SczzddtjService, TraceShowModuleDao> {

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
     * 获取客户类型
     * @return
     */
    public String getKhlx(){

        setReturnData(getService().getKhlx());
        return SUCCESS;
    }

    /**
     * 获取产品名称
     * @return
     */
    public String getCpmc(){

        setReturnData(getService().getCpmc());
        return SUCCESS;
    }

}
package com.ces.component.sczzcstj.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.sczzcstj.service.SczzcstjService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SczzcstjController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SczzcstjService, TraceShowModuleDao> {

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
     获取品种信息
     * @return
     */
    public String getPzxx(){
        setReturnData(getService().getPzxx());
        return SUCCESS;
    }

    /**
     *获取区域名称
     * @return
     */
    public String  getQymc(){
        setReturnData(getService().getQymc());
        return SUCCESS;
    }

    /**
     *获取地块编号
     * @return
     */
    public String getDkbh(){
        setReturnData(getService().getDkbh());
        return SUCCESS;
    }

}
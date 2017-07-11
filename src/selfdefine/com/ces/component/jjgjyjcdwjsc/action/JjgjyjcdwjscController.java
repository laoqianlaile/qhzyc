package com.ces.component.jjgjyjcdwjsc.action;

import com.ces.component.jjgjyjcdwjsc.dao.JjgjyjcdwjscDao;
import com.ces.component.jjgjyjcdwjsc.service.JjgjyjcdwjscService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class JjgjyjcdwjscController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, JjgjyjcdwjscService, JjgjyjcdwjscDao> {

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

}

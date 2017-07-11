package com.ces.component.cjgjyjcdwjsc.action;

import com.ces.component.cjgjyjcdwjsc.dao.CjgjyjcdwjscDao;
import com.ces.component.cjgjyjcdwjsc.service.CjgjyjcdwjscService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class CjgjyjcdwjscController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CjgjyjcdwjscService, CjgjyjcdwjscDao> {

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

package com.ces.component.jjgjyjcdwjscxx.action;

import com.ces.component.jjgjyjcdwjscxx.dao.JjgjyjcdwjscxxDao;
import com.ces.component.jjgjyjcdwjscxx.service.JjgjyjcdwjscxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class JjgjyjcdwjscxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, JjgjyjcdwjscxxService, JjgjyjcdwjscxxDao> {

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

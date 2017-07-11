package com.ces.component.sdzycjjgyphpscxx.action;

import com.ces.component.sdzycjjgyphpscxx.dao.SdzycjjgyphpscxxDao;
import com.ces.component.sdzycjjgyphpscxx.service.SdzycjjgyphpscxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzycjjgyphpscxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycjjgyphpscxxService, SdzycjjgyphpscxxDao> {

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

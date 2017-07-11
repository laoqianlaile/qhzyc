package com.ces.component.qhpfccxx.action;

import com.ces.component.qhpfccxx.dao.QhpfccxxDao;
import com.ces.component.qhpfccxx.service.QhpfccxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class QhpfccxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, QhpfccxxService, QhpfccxxDao> {

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

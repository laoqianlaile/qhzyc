package com.ces.component.qhpfyljydwjsc.action;

import com.ces.component.qhpfyljydwjsc.dao.QhpfyljydwjscDao;
import com.ces.component.qhpfyljydwjsc.service.QhpfyljydwjscService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;

public class QhpfyljydwjscController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, QhpfyljydwjscService, QhpfyljydwjscDao> {

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

    @Override
    public Object destroy() throws FatalException {
        return super.destroy();
    }
}

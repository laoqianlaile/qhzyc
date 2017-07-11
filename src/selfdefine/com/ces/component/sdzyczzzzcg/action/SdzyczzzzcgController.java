package com.ces.component.sdzyczzzzcg.action;

import com.ces.component.sdzyczzzzcg.dao.SdzyczzzzcgDao;
import com.ces.component.sdzyczzzzcg.service.SdzyczzzzcgService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzyczzzzcgController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyczzzzcgService, SdzyczzzzcgDao> {

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

    //获取种子种苗信息
    public void searchZzzmxx(){
        this.setReturnData(getService().getZzzmxx());
    }

}

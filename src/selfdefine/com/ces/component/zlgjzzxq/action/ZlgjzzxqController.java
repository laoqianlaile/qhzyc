package com.ces.component.zlgjzzxq.action;

import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zlgjzzxq.service.ZlgjzzxqService;
import com.ces.xarch.core.entity.StringIDEntity;

/**
 * Created by wngyu on 15/11/24.
 */
public class ZlgjzzxqController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZlgjzzxqService, TraceShowModuleDao> {
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

    public void getScdaxx(){
        String id=getParameter("id");
        setReturnData(getService().getScdaxx(id));

    }
    public void getScdaid(){
        String id=getParameter("id");
        setReturnData(getService().getScdaid(id));
    }
}
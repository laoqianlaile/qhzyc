package com.ces.component.sdzycnjjxx.action;

import com.ces.component.sdzycnjjxx.dao.SdzycnjjxxDao;
import com.ces.component.sdzycnjjxx.service.SdzycnjjxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzycnjjxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycnjjxxService, SdzycnjjxxDao> {

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
     * 根据id获取类型
     * @return
     *
     */
    public Object getLxById(){
        String id = getParameter("id");
        setReturnData(getService().getLxById(id));
        return SUCCESS;
    }
    /**
     * 获取类型
     * @return
     *
     */
    public Object getLx(){
        setReturnData(getService().getLx());
        return SUCCESS;
    }

}

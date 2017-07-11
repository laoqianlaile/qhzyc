package com.ces.component.sdzycflxx.action;

import com.ces.component.sdzycflxx.dao.SdzycflxxDao;
import com.ces.component.sdzycflxx.service.SdzycflxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzycflxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycflxxService, SdzycflxxDao> {

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

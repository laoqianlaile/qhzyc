package com.ces.component.sdzyczzdkxx.action;

import com.ces.component.sdzyczzdkxx.dao.SdzyczzdkxxDao;
import com.ces.component.sdzyczzdkxx.service.SdzyczzdkxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzyczzdkxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyczzdkxxService, SdzyczzdkxxDao> {

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

    public void getJdmcGrid(){
        this.setReturnData(getService().getJdmc());
    }

    /**
     * 根据id 获取地块信息
     */
    public void getDkxx(){
        String id = getParameter("id");
        this.setReturnData(getService().getDkxx(id));
    }

    public void getSbsbh(){
        String cgqz =getParameter("cgqz");
        this.setReturnData(getService().getSbsbh(cgqz));
    }

    public void getJdxx(){
        String jdbh = getParameter("jdbh");
        this.setReturnData(getService().getJdxx(jdbh));
    }
}

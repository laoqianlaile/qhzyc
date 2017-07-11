package com.ces.component.zzjdxx.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzjdxx.service.ZzjdxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZzjdxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzjdxxService, TraceShowModuleDao> {

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

    public void getJdbhLsh(){
        setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ", "ZZJDBH", false));
    }

    /**
     * 获取基地下所属区域
     */
    public void querySsqy(){
        String jdbh = getParameter("jdbh");
        setReturnData(getService().querySsqy(jdbh));
    }
}
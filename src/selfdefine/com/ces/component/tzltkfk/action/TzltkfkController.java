package com.ces.component.tzltkfk.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tzltkfk.service.TzltkfkService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TzltkfkController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TzltkfkService, TraceShowModuleDao> {

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
     * 获取备案流通节点编码
     */
    public void getBaltjdbm(){
        String baltjdbm = SerialNumberUtil.getInstance().getCompanyCode();
        setReturnData(baltjdbm);
    }
}
package com.ces.component.zzcpxxglxz.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzcpxxglxz.service.ZzcpxxglxzService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZzcpxxglxzController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzcpxxglxzService, TraceShowModuleDao> {

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
     * 获取产品编号流水号
     */
    public void getCpbhLsh(){
        setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ", "ZZCPBH", false));
    }

    /**
     * 获取企业编码
     */
    public void getQybm(){
        setReturnData(SerialNumberUtil.getInstance().getCompanyCode());
    }
}
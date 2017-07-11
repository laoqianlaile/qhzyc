package com.ces.component.zzgysxx.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzgysxx.service.ZzgysxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZzgysxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzgysxxService, TraceShowModuleDao> {

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

    public Object getGysbh(){
        setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ","GYSBH",true));
        return SUCCESS;
    }

    public void getQybm(){
        setReturnData(SerialNumberUtil.getInstance().getCompanyCode());
    }

    /**
     *保证供应商名称不能重复
     * @return
     */
    public Object checkGYSMC(){

        String gysmc=getParameter("gysmc");
        String id=getParameter("id");
        setReturnData(getService().checkGYSMC(gysmc,id));

        return SUCCESS;
    }

    /**
     * 根据数据类型获取供应商类型
     * @return
     */
    public Object getGyslx(){
        setReturnData(getService().getGyslx());
        return SUCCESS;
    }

    public Object getGyslxById(){
        String id = getParameter("id");
        setReturnData(getService().getGyslxById(id));
        return SUCCESS;
    }
}
package com.ces.component.sctzcx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.sctzcx.service.SctzcxService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SctzcxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SctzcxService, TraceShowModuleDao> {

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

    public void getQybm(){
        setReturnData(SerialNumberUtil.getInstance().getCompanyCode());
    }
    //获取图片
    public void getImages(){
        String tzid = getParameter("tzid");
        setReturnData(getService().getImages(tzid));
    }

    //列表删除
    public void getDeleteByTzid(){
        String tzid = getParameter("tzid");
        setReturnData(getService().getDeleteByTzid(tzid));
    }

}
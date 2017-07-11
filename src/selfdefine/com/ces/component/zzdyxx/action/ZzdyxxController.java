package com.ces.component.zzdyxx.action;

import com.ces.component.trace.utils.JSON;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzdyxx.service.ZzdyxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class ZzdyxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzdyxxService, TraceShowModuleDao> {

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

    //获取地块信息
    public Object getDkxx() {
        String qybh = getParameter("qybh");
        String dkbh = getParameter("dkbh");
        setReturnData(getService().getDkxx(qybh,dkbh));
        return SUCCESS;
    }

    //获取基地信息

    public Object getJdxx(){
        setReturnData(getService().getJdxx());
        return SUCCESS;
    }

    //获取区域信息
    public Object getQyxx(){
        String jdbh = getParameter("jdbh");
        setReturnData(getService().getQyxx(jdbh));
        return SUCCESS;
    }

    //获取单元流水号
    public Object getDylsh(){
        String dkbh = getParameter("dkbh");
        setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ",dkbh,"DYBH",true));
        return SUCCESS;
    }

    public Object checkBindle () {
        String ids = getParameter("idArr");
        List<String> idArr = JSON.fromJSON(ids, new TypeReference<List<String>>() {});
        setReturnData(getService().checkBindle(idArr));
        return  SUCCESS;
    }
}
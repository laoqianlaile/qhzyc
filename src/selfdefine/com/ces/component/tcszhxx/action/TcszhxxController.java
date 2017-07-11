package com.ces.component.tcszhxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tcszhxx.service.TcszhxxService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcszhxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcszhxxService, TraceShowModuleDao> {

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

    //根据auth_parent_id 获取下拉列表账户信息
    public Object getZhxx() {
        String id = getParameter("id");
        setReturnData(getService().getZhxx(id));
        return SUCCESS;
    }

    //根据账户编号获取其id
    public Object getIdByZhbh() {
        String zhbh = getParameter("zhbh");
        setReturnData(getService().getIdByZhbh(zhbh));
        return SUCCESS;
    }

    //获取设备编号流水号
    public Object getSbbh(){
//        setReturnData(SerialNumberUtil.getInstance().getSerialNumber("COMMON","SBBH",false,true));
        String zhen_companyCode =  getParameter("zhen_companyCode");
        setReturnData(getService().getSbbh(zhen_companyCode));
        return SUCCESS;
    }

    //设置设备启用状态及数据启用状态
    public Object setQyzt(){
        String id = getParameter("id");
        String type = getParameter("type");
        String zt = getParameter("zt");
        setReturnData(getService().setQyzt(id,type,zt));
        return SUCCESS;
    }
}
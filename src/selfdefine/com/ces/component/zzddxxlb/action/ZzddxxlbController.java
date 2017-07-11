package com.ces.component.zzddxxlb.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzddxxlb.service.ZzddxxlbService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZzddxxlbController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzddxxlbService, TraceShowModuleDao> {

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

    public void getCpxxData(){
        setReturnData(getService().getCpxx());
    }

    public void getCpxxByBh(){
        String cpbh = getParameter("cpbh");
        setReturnData(getService().getCpxxByBh(cpbh));
    }

    /**
     * 保存订单基本信息和散货、产品信息
     */
    public void saveDdxx(){
        String entityJson = getParameter("E_entityJson");
        String sdEntitiesJson = getParameter("S_dEntitiesJson");
        String cdEntitiesJson = getParameter("C_dEntitiesJson");
        setReturnData(getService().saveDdxx(entityJson,sdEntitiesJson,cdEntitiesJson));
    }

    /**
     * 根据ID获得对应订单详细信息
     */
    public void searchDdxx(){
        String id = getParameter("id");
        setReturnData(getService().searchDdxxById(id));
    }

    /**
     * 根据订单ID获得对应产品订单信息
     */
    public void searchCpddxx(){
        String pid = getParameter("pid");
        setReturnData(getService().searchCpddxx(pid));
    }

    /**
     * 根据订单ID获得对应散货订单信息
     */
    public void searchShddxx(){
        String pid = getParameter("pid");
        setReturnData(getService().searchShddxx(pid));
    }

    /**
     * 判断是否为客户身份登录并获得客户基本信息
     */
    public void isKhlogin(){
        setReturnData(getService().isKhlogin());
    }

    public String deleteShxxOrCpxx(){
        String type = getParameter("type");
        String id = getParameter("id");
        getService().deleteShxxOrCpxx(id,type);
        return SUCCESS;
    }
}
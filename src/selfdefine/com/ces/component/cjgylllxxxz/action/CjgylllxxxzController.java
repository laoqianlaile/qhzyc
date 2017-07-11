package com.ces.component.cjgylllxxxz.action;

import com.ces.component.cjgylllxxxz.dao.CjgylllxxxzDao;
import com.ces.component.cjgylllxxxz.service.CjgylllxxxzService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class CjgylllxxxzController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CjgylllxxxzService, CjgylllxxxzDao> {

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
     * 获得领料单号下拉框数据
     */
    public void searchGridData(){
        setReturnData(getService().searchylllxxComboGridData());
    }

    /**
     * 根据领原料批次号获取原料领料信息
     */
    public void searcylllxx(){
        String pch= getParameter("pch");
        setReturnData(getService().searchylllxxByllpch(pch));
    }
    public void searchLldhGridData(){
        String lldh=getParameter("lldh");
        setReturnData(getService().searchylllxxxxComboGridData(lldh));
    }

}




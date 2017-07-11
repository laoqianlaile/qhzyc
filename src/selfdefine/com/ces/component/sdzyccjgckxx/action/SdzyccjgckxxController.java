package com.ces.component.sdzyccjgckxx.action;

import com.ces.component.sdzyccjgckxx.dao.SdzyccjgckxxDao;
import com.ces.component.sdzyccjgckxx.service.SdzyccjgckxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzyccjgckxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyccjgckxxService, SdzyccjgckxxDao> {

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
     * 获得客户信息下拉框数据
     */
    public void searchGridData(){
        setReturnData(getService().searchckxxComboGridData());
    }

    public void searchDbCkGridData(){
        String slck = getParameter("slck");
        setReturnData(getService().searchdbckxxComboGridData(slck));
    }
    /**
     * 根据客户编号获得对应的详细信息
     */
    public void searchKhxx(){
        String ckbh = getParameter("ckbh");
        setReturnData(getService().searchKhxxByckbh(ckbh));
    }


}


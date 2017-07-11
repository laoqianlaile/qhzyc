package com.ces.component.tcszzkhxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tcszzkhxx.service.TcszzkhxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

/**
 *
 */
public class TcszzkhxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcszzkhxxService, TraceShowModuleDao> {

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
        setReturnData(getService().searchKhxxComboGridData());
    }

    /**
     * 根据客户编号获得对应的详细信息
     */
    public void searchKhxx(){
        String khbh = getParameter("khbh");
        setReturnData(getService().searchKhxxByKhbh(khbh));
    }
}
package com.ces.component.jjgyccgwjy.action;

import com.ces.component.jjgyccgwjy.dao.JjgyccgwjyDao;
import com.ces.component.jjgyccgwjy.service.JjgyccgwjyService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class JjgyccgwjyController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, JjgyccgwjyService, JjgyccgwjyDao> {

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
     * 通过入库编号将入库信息带入页面中
     */
    public void getDataByrkbh(){
        String rkbh = getParameter("rkbh");
        setReturnData(getService().getDataByrkbh(rkbh));
    }

    /**
     * 更新是否出库字段  1为出库  0 为未出库
     */
    public void processSfck(){
        String rkbh = getParameter("rkbh");
        getService().processSfck(rkbh);
    }

    /**
     * 查询输出值是否大于粗加工的入库重量
     */
    public void searchKcByPch(){
        String pch = getParameter("pch");
        setReturnData(getService().searchKcByPch(pch));
    }
}

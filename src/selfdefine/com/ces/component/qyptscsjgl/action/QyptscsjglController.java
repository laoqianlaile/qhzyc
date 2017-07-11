package com.ces.component.qyptscsjgl.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.qyptscsjgl.service.QyptscsjglService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class QyptscsjglController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, QyptscsjglService, TraceShowModuleDao> {

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
     * 过滤不同企业的基地信息
     * @return
     */
    public Object getJdbm(){
    	String id=getParameter("id");
        setReturnData(getService().getJdbm(id));
        return SUCCESS;
    }
/**
 * 获取生产数据
 * @return
 */
    public void getScsjById(){
    	String id=getParameter("id");
        setReturnData(getService().getScsj(id));
    }
}
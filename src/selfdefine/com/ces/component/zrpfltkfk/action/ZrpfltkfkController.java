package com.ces.component.zrpfltkfk.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zrpfltkfk.service.ZrpfltkfkService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZrpfltkfkController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZrpfltkfkService, TraceShowModuleDao> {

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
     * 获取企业编码
     */
    public void getQybm(){
    	String qybm = SerialNumberUtil.getInstance().getCompanyCode();
    	setReturnData(qybm);
    }

}
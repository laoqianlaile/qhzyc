package com.ces.component.prqyda.action;

import java.util.Map;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.prqyda.service.PrqydaService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class PrqydaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, PrqydaService, TraceShowModuleDao> {

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
     * 根据企业编码获取企业档案
     */
    public void getQydaInfo(){
    	String qybm = SerialNumberUtil.getInstance().getCompanyCode();
    	Map<String,String> map = (Map<String, String>) this.getService().getQydaInfo(qybm);
    	setReturnData(map);
    }
}
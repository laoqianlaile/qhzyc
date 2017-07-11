package com.ces.component.przryda.action;

import java.util.HashMap;
import java.util.Map;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.przryda.service.PrzrydaService;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class PrzrydaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, PrzrydaService, TraceShowModuleDao> {

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
     * 表单初始化信息
     */
    public void getInitInfo(){
    	String qybm = SerialNumberUtil.getInstance().getCompanyCode();
    	String gzrybm = SerialNumberUtil.getInstance().getSerialNumber("PR", "GZRYBH", false);
    	String qymc = CompanyInfoUtil.getInstance().getCompanyName("PR", qybm);
    	Map map = new HashMap();
    	map.put("TZCBM", qybm);
    	map.put("TZCMC", qymc);
    	map.put("GZRYBM", gzrybm);
    	setReturnData(map);
    	
    }
}
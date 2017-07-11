package com.ces.component.csscjcxx.action;

import java.util.HashMap;
import java.util.Map;

import com.ces.component.trace.utils.CompanyInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.csscjcxx.service.CsscjcxxService;
import com.ces.component.lsjcxxxz.service.LsjcxxxzService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class CsscjcxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CsscjcxxService, TraceShowModuleDao> {

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
    @Autowired
    @Qualifier("csscjcxxService")
    @Override
    protected void setService(CsscjcxxService service) {
        super.setService(service);
    }
    /**
     * 获取蔬菜进场编号
     * @return
     */
    public Object getScjcbh(){
    	String scjcbh = SerialNumberUtil.getInstance().getSerialNumber("CS", "SCJCBH", true);
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("scjcbh", scjcbh);
    	setReturnData(map);
    	return null;
    }
    public Object initFormData(){
    	String jhpch = SerialNumberUtil.getInstance().getSerialNumber("CS", "JHPCH",true );
    	String csbm = SerialNumberUtil.getInstance().getCompanyCode();
        String csmc = CompanyInfoUtil.getInstance().getCompanyName("CS",csbm);
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("jhpch", jhpch);
    	map.put("csbm", csbm);
    	map.put("csmc", csmc);
    	setReturnData(map);
    	return null;
    }

}
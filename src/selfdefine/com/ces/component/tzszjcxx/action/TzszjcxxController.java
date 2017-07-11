package com.ces.component.tzszjcxx.action;

import java.util.ArrayList;
import java.util.Map;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tzszjcxx.service.TzszjcxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.sun.xml.bind.v2.schemagen.xmlschema.List;

public class TzszjcxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TzszjcxxService, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;
    private String barCode;
    
    
    public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

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
    
    //根据条码获取生猪产地检疫证及其下的信息
	public void getJcpcxx() {
		try {
			Map<String,Object> result = getService().getJcpcxx(barCode);
			if(result.isEmpty()){
				super.setReturnData("ERROR");
			}else {
				super.setReturnData(result);
			}
		} catch (Exception e) {
			System.out.print(e);
			super.setReturnData("ERROR");
		}
	}
	//获取检疫证号
public void getszjcxx(){
	String szcdjyzh = getParameter("szcdjyzh");
	
   this.setReturnData(getService().getszjcxx(szcdjyzh));
	
	
}
}
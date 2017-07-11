package com.ces.component.tcsprjcspxx.action;

import java.util.HashMap;
import java.util.Map;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tcsprjcspxx.service.TcsprjcspxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcsprjcspxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcsprjcspxxService, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;
    private String zspzh;
    
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
     * 获取弹出式商品信息
     */
    public void getJcspxxGrid(){
    	setReturnData(this.getService().getJcspxx(zspzh));
    }
    public void getJhpch(){
    	setReturnData(this.getService().getJhpch(zspzh));
    }
	public String getZspzh() {
		return zspzh;
	}
	public void setZspzh(String zspzh) {
		this.zspzh = zspzh;
	}
}
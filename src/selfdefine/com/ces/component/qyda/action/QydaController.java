package com.ces.component.qyda.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ces.component.qyda.dao.QydaDao;
import com.ces.component.qyda.entity.QydaEntity;
import com.ces.component.qyda.service.QydaService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.xarch.core.security.entity.SysUser;

public class QydaController extends TraceShowModuleDefineServiceDaoController<QydaEntity, QydaService, QydaDao> {

    private static final long serialVersionUID = 1L;
    private String prefix;
    
    
    public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	/*
     * (非 Javadoc)   
     * <p>标题: initModel</p>   
     * <p>描述: </p>      
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new QydaEntity());
    }
    @Autowired
    @Qualifier("qydaService")
    @Override
    protected void setService(QydaService service) {
        super.setService(service);
    }
    //获取企业名称和企业编码
    public void getQyda(){
    	this.setReturnData(getService().getQyda(prefix));
    }
    
    public void getIdByQybm(){
    	this.setReturnData(getService().getIdByQybm());
    }
}

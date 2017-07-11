package com.ces.component.tzqyda.action;

import org.springframework.security.core.context.SecurityContextHolder;

import com.ces.component.qyda.entity.QydaEntity;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.tzqyda.dao.TzqydaDao;
import com.ces.component.tzqyda.service.TzqydaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.security.entity.SysUser;

public class TzqydaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TzqydaService, TzqydaDao> {

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
        setModel(new StringIDEntity());
    }
    
    //获取企业名称和企业编码
    public void getQyda(){
    	this.setReturnData(getService().getQyda(prefix));
    }
    
    public void getIdByQybm(){
    	this.setReturnData(getService().getIdByQybm());
    }
    
}

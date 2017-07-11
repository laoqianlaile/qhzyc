package com.ces.component.lsqyda.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ces.component.lsqyda.dao.LsqydaDao;
import com.ces.component.lsqyda.entity.LsqydaEntity;
import com.ces.component.lsqyda.service.LsqydaService;
import com.ces.component.qyda.service.QydaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.security.entity.SysUser;

public class LsqydaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, LsqydaService, LsqydaDao> {

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
    @Qualifier("lsqydaService")
    @Override
    protected void setService(LsqydaService service) {
        super.setService(service);
    }
    
    public Object getIdByZzjgdm(){
    	SysUser user = (SysUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	LsqydaEntity entity = getService().getByQybm(SerialNumberUtil.getInstance().getCompanyCode());
//    	LsqydaEntity entity = getService().getByZzjgdm("123");
    	setReturnData(entity);
    	return null;
    }

}

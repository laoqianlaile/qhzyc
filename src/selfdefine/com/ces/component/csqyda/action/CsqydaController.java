package com.ces.component.csqyda.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ces.component.csqyda.dao.CsqydaDao;
import com.ces.component.csqyda.entity.CsqydaEntity;
import com.ces.component.csqyda.service.CsqydaService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.security.entity.SysUser;

public class CsqydaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CsqydaService, CsqydaDao> {

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
    @Qualifier("csqydaService")
    @Override
    protected void setService(CsqydaService service) {
        super.setService(service);
    }
    
    public Object getIdByQybm(){
    	String qybm = SerialNumberUtil.getInstance().getCompanyCode();
    	//LsqydaEntity entity = getService().getByZzjgdm(user.);
    	CsqydaEntity entity = getService().getByQybm(qybm);
    	setReturnData(entity);
    	return null;
    }

}

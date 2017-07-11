package com.ces.component.bddt.action;


import com.ces.component.bddt.dao.BddtDao;
import com.ces.component.bddt.service.BddtService;
import com.ces.config.action.base.ShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class BddtController extends ShowModuleDefineServiceDaoController<StringIDEntity, BddtService, BddtDao> {

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

}

package com.ces.component.yzcdda.action;

import com.ces.component.yzcdda.dao.YzcddaDao;
import com.ces.component.yzcdda.entity.YzcddaEntity;
import com.ces.component.yzcdda.service.YzcddaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class YzcddaController extends TraceShowModuleDefineServiceDaoController<YzcddaEntity, YzcddaService, YzcddaDao> {

    private static final long serialVersionUID = 1L;
    
    /*
     * (非 Javadoc)   
     * <p>标题: initModel</p>   
     * <p>描述: </p>      
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new YzcddaEntity());
    }
    
    public void getQybm(){
    	setReturnData(this.getService().getQybm());
    }
    
    /**
     * 根据当前登录者账号编码获得产地档案
     * @return
     */
    public String getCddaByZhbh(){
    	YzcddaEntity cdda=getService().getByQybm();
    	setReturnData(cdda);
    	return null;
    }

}

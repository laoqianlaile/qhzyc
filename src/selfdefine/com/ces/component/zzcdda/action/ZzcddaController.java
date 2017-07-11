package com.ces.component.zzcdda.action;

import com.ces.component.zzcdda.dao.ZzcddaDao;
import com.ces.component.zzcdda.entity.ZzcddaEntity;
import com.ces.component.zzcdda.service.ZzcddaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZzcddaController extends TraceShowModuleDefineServiceDaoController<ZzcddaEntity, ZzcddaService, ZzcddaDao> {

    private static final long serialVersionUID = 1L;
    
    /*
     * (非 Javadoc)   
     * <p>标题: initModel</p>   
     * <p>描述: </p>      
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ZzcddaEntity());
    }
    /**
     * 根据当前登录者账号编码获得产地档案
     * @return
     */
    public String getCddaByQybm(){
    	ZzcddaEntity cdda=getService().getByQybm();
    	setReturnData(cdda);
    	return null;
    }
    
    public void getQybm(){
    	setReturnData(this.getService().getQybm());
    }
    
    public void getByQybm(){
    	setReturnData(getService().getByQybm());
    }
}

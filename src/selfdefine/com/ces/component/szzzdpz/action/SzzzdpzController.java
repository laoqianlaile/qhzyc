package com.ces.component.szzzdpz.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.szzzdpz.service.SzzzdpzService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SzzzdpzController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SzzzdpzService, TraceShowModuleDao> {

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

    /**
     * 查看品类是否存在品种
     * @return
     */
    public Object hasPz() {
        String plids = getParameter("plids");
        setReturnData(getService().hasPz(plids.split(",")));
        return SUCCESS;
    }
    /**
     * 查看是否有商品名称和商品编码
     * @author zhaoben
     */
    public void getSpxx(){
    	 setReturnData(getService().getSpxx());
    }
    
    public void getSpbm(){
    	String pl = getParameter("pl");
   	 setReturnData(getService().getSpbm(pl));
   }
    
}
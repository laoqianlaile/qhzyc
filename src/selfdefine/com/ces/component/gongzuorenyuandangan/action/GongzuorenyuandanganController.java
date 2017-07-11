package com.ces.component.gongzuorenyuandangan.action;

import com.ces.component.gongzuorenyuandangan.dao.GongzuorenyuandanganDao;
import com.ces.component.gongzuorenyuandangan.entity.GongzuorenyuandanganEntity;
import com.ces.component.gongzuorenyuandangan.service.GongzuorenyuandanganService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;

public class GongzuorenyuandanganController extends TraceShowModuleDefineServiceDaoController<GongzuorenyuandanganEntity, GongzuorenyuandanganService, GongzuorenyuandanganDao> {

    private static final long serialVersionUID = 1L;
    
//    @Override
//    protected void setService(GongzuorenyuandanganService service) {
//    	super.setService(service);
//    }
    
    /*
     * (非 Javadoc)   
     * <p>标题: initModel</p>   
     * <p>描述: </p>      
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new GongzuorenyuandanganEntity());
    }
    
    public void getGzrybh(){
    	String zl = this.getRequest().getParameter("Zl");
    	super.setReturnData(SerialNumberUtil.getInstance().getSerialNumber("PC",zl,false));
    }


    public void checkDuplicate(){
        String id = getParameter("id");
        String sfzh = getParameter("sfzh");
        setReturnData(getService().checkDuplicate(id,sfzh));
    }
}

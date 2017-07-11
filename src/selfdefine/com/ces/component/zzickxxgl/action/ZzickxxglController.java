package com.ces.component.zzickxxgl.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzickxxgl.service.ZzickxxglService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZzickxxglController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzickxxglService, TraceShowModuleDao> {

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
    public void saveicbgb(){
        String XM=getParameter("XM");
        String SFZH=getParameter("SFZH");
        String ICKKH=getParameter("ICKKH");
        String XB=getParameter("XB");
        String QYZT=getParameter("QYZT");
        String GW=getParameter("GW");
        String ID=getParameter("ID");
        String BZ=getParameter("BZ");
        //String QYBM=getParameter("QYBM");
        //System.out.println(XM+"  "+SFZH+"   "+ICKKH);
        setReturnData(getService().saveicbgb(ID,XM,SFZH,ICKKH,XB,QYZT,GW,BZ));
    }

}
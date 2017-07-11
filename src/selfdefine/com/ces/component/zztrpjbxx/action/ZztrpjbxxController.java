package com.ces.component.zztrpjbxx.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zztrpjbxx.service.ZztrpjbxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import org.omg.PortableInterceptor.SUCCESSFUL;

public class ZztrpjbxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZztrpjbxxService, TraceShowModuleDao> {

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
    public void getZztrpbh(){

        setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ","ZZTRPBH",false));
    }

    /**
     * 设置投入品名称不能重复的方法
     * @return
     */
    public Object checkTRPMC(){

        String trpmc=getParameter("trpmc");
        String id=getParameter("id");
        setReturnData(getService().checkTRPMC(trpmc,id));
        return SUCCESS;
    }

    /**
     * 一种通用名只能对应一种类型的方法
     * @return
     */
    public Object checkTYM(){

        String tym=getParameter("tym");
        String lx=getParameter("lx");
        String id=getParameter("id");
        setReturnData(getService().checkTYM(tym,lx,id));

        return SUCCESS;
    }





}
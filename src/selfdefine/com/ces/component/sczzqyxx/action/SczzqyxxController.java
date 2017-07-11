package com.ces.component.sczzqyxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.sczzqyxx.service.SczzqyxxService;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.exception.FatalException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.rest.DefaultHttpHeaders;

import java.util.HashMap;
import java.util.Map;

public class SczzqyxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SczzqyxxService, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;
    private static Log log = LogFactory.getLog(TraceShowModuleDefineServiceDaoController.class);
    
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
    public void getjdxx(){
        Map map = new HashMap();
        map.put("QYBM", SerialNumberUtil.getInstance().getCompanyCode());
        map.put("QYMC2", CompanyInfoUtil.getInstance().getCompanyName("ZZ",SerialNumberUtil.getInstance().getCompanyCode()));
        setReturnData(map);
    }

    /**
     * 区域编号是否存在
     */
    public void isExist(){
        String id = this.getRequest().getParameter("id");
        String qybh = this.getRequest().getParameter("qybh");
        String jdbh = this.getRequest().getParameter("jdbh");
        boolean flag = this.getService().isQybhExist(qybh,jdbh,id);
        if(flag){
            setReturnData(true);
        }else{
            setReturnData(false);
        }
    }
}
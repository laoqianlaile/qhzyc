package com.ces.component.sczzqylbxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.sczzqylbxx.service.SczzqylbxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.exception.FatalException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.rest.DefaultHttpHeaders;

public class SczzqylbxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SczzqylbxxService, TraceShowModuleDao> {

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
    @Override
    public Object destroy() throws FatalException {
        try {
            String ids      = getId();
            getService().logicDelete(ids);
        } catch (Exception e) {
            processException(e, BusinessException.class);
            log.error("删除出错", e);
        }

        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }


    /**
     * 判断区域下面是否有地块
     * @return
     */
    public Object hasChild(){
        //获取区域编号
        String qybhArray = this.getRequest().getParameter("qybhArray");
        setReturnData(this.getService().hasChildren(qybhArray));
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}
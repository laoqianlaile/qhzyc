package com.ces.component.zzccgl.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzccgl.service.ZzccglService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZzccglController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzccglService, TraceShowModuleDao> {

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

    public Object deleteCcgl() {
        String ids = getParameter("ids");
        String[] idArray = ids.split(",");
        getService().deleteCcgl(idArray);
        return SUCCESS;
    }

    public Object getCcxx () {
        String ccid = getParameter("ccid");
        setReturnData(getService().getCcxx(ccid));
        return SUCCESS;
    }

    public void getGridData(){
        String id=getParameter("id");
        setReturnData(getService().getGridData(id));
    }

    /**根据PID获取散货出场信息
     * @return
     */
    public Object getShListByPid(){
        String pid = getParameter("pid");
        setReturnData(getService().getShListByPid(pid));
        return SUCCESS;
    }

}
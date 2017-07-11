package com.ces.component.zzccshxx.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzccshxx.service.ZzccshxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZzccshxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzccshxxService, TraceShowModuleDao> {

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

    public Object getCsxx() {
        setReturnData(getService().getCsxx());
        return SUCCESS;
    }

    //获取采收批次信息
    public Object getCspcxx () {
        String batchcode = getParameter("batchcode");
        setReturnData(getService().getCspcxx(batchcode));
        return SUCCESS;
    }

    /**
     * 分拣散货出场数据加载
     */
    public void getShccByFj(){
        String csid=getParameter("csid");
        setReturnData(getService().getShccByFj(csid));
    }

    /**修改时处理每条数据的库存重量信息
     * @return
     */
    public Object getShccKczl(){
        String pch = getParameter("pch");
        String cczl = getParameter("cczl");
        setReturnData(getService().getShccKczl(pch, cczl));
        return SUCCESS;
    }


}
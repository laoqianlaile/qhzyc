package com.ces.component.sdzycjjgycdbxx.action;

import com.ces.component.sdzycjjgycdbxx.dao.SdzycjjgycdbxxDao;
import com.ces.component.sdzycjjgycdbxx.service.SdzycjjgycdbxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzycjjgycdbxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycjjgycdbxxService, SdzycjjgycdbxxDao> {

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
     * 获取精加工药材调拨批次号下拉列表数据
     */
    public void searchPchGridData(){

        setReturnData(getService().searchYcdbxxData());
    }

    /**
     * 获取负责人信息
     */
    public void searchFzrxx(){
        String rybh = getParameter("rybh");
        setReturnData(getService().searchFzrxx(rybh));
    }

    /**
     * 获取调入仓库下拉框的数据,除去已经选中的调出仓库
     */
     public void getDrckExceptDCCK(){
         String ckbh = getParameter("ckbh");
         setReturnData(getService().getDrckExceptDCCK(ckbh));
     }
}

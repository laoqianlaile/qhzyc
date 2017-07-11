package com.ces.component.sdzycscllxx.action;

import com.ces.component.sdzycscllxx.dao.SdzycscllxxDao;
import com.ces.component.sdzycscllxx.service.SdzycscllxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.exception.FatalException;
import org.apache.struts2.rest.DefaultHttpHeaders;

import java.util.List;
import java.util.Map;

public class SdzycscllxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycscllxxService, SdzycscllxxDao> {

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
     * 获取精加工生产领料原料批次号下拉列表数据
     */
    public void searchPchGridData(){
        setReturnData(getService().searchYlpchxxComboGridData());
    }

    /**
     * 获取精加工生产领料企业批次号下拉框数据
     */
    public void searchQypchGridData(){setReturnData(getService().getJjgYlpchGridData());}

    public void getFlck(){
        String pch= getParameter("pch");
        setReturnData(getService().getFlck(pch));
    }
    @Override
    public Object destroy() throws FatalException {
        return super.destroy();
    }
    public void queryPch(){
        String pch = getParameter("pch");
        setReturnData(getService().searchDataByPch(pch));
    }
    public void searchLlbmxx(){
        setReturnData(getService().getLlbmxx());
    }
}

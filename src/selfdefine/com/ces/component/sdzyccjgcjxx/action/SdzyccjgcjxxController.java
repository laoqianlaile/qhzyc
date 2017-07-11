package com.ces.component.sdzyccjgcjxx.action;

import com.ces.component.sdzyccjgcjxx.dao.SdzyccjgcjxxDao;
import com.ces.component.sdzyccjgcjxx.service.SdzyccjgcjxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzyccjgcjxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyccjgcjxxService, SdzyccjgcjxxDao> {

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

    //获取粗加工车间信息
    public void searchcjxx(){
        this.setReturnData(getService().searchcjxxBycjbh());
    }

    /**
     * 获得供应商信息下拉框数据
     */
    public void searchGridData(){
        setReturnData(getService().searchcdzmxxComboGridData());
    }

    /**
     * 获取产地证明图片名称
     */
    public void searchcdzmxxData(){
        String sctpmc = getParameter("sctpmc");
        setReturnData(getService().searchcdzmxxData(sctpmc));
    }


    /**
     * 根据供应商获取供应商信息信息
     */
    public void searchcdzmxxBylldh(){
        String sctpmc= getParameter("sctpmc");
        setReturnData(getService().searchcdzmxxBylldh(sctpmc));
    }
}

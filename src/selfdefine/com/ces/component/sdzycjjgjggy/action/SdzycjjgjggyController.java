package com.ces.component.sdzycjjgjggy.action;

import com.ces.component.sdzycjjgjggy.dao.SdzycjjgjggyDao;
import com.ces.component.sdzycjjgjggy.service.SdzycjjgjggyService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzycjjgjggyController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycjjgjggyService, SdzycjjgjggyDao> {

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
     * 精加工加工工艺应用药材下拉框
     */
    public void getJggyGrid(){
        this.setReturnData(getService().getJggyGrid());}
    public void searchjggyxx(){
        this.setReturnData(getService().searchjggyxxBygybh());
    }
}

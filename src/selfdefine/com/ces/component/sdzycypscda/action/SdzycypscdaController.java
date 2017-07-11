package com.ces.component.sdzycypscda.action;

import com.ces.component.sdzycypscda.dao.SdzycypscdaDao;
import com.ces.component.sdzycypscda.service.SdzycypscdaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzycypscdaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycypscdaService, SdzycypscdaDao> {

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
     * 饮片生产档案
     */
    public void searchScdaxx(){
       // String pid = getParameter("pid");
        setReturnData(getService().searchTrpscdaxx());
    }

    /**
     * 保存生产档案
     * @return
     */
    public String saveScdaxx(){
        String entityJson = getParameter(E_ENTITY_JSON);
        String dEntitiesJson = getParameter(E_D_ENTITIES_JSON);
       // String pEntitiesJson = getParameter("E_pEntityJson");
        setReturnData(getService().saveTrplyxx(entityJson, dEntitiesJson));
        return SUCCESS;
    }
}

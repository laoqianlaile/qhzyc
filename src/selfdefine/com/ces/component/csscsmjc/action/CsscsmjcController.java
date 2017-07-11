package com.ces.component.csscsmjc.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.csscsmjc.service.CsscsmjcService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.logger.Logger;

public class CsscsmjcController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CsscsmjcService, TraceShowModuleDao> {

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

   /* public String searchZzdata(){
        String barCode = getParameter("barCode");
        setReturnData(getService().searchZzData(barCode));
        return SUCCESS;
    }*/
    @Override
   @Logger(model = "[${P_menuId}]", action = "级联保存", logger = "保存内容：${E_entityJson}|3")
   public Object saveAll() {
       try {
           String entityJson = getParameter(E_ENTITY_JSON);
           String tableId = getParameter(P_TABLE_ID);
           String dEntitiesJson = getParameter(E_D_ENTITIES_JSON);
           String dTableId = getParameter(P_D_TABLE_IDS);
           String xsddhs = getParameter("xsddhs");
           setReturnData(getService().saveAll(xsddhs,tableId, entityJson, dTableId, dEntitiesJson, getMarkParamMap()));
       } catch (Exception e) {
           processException(e, BusinessException.class);
       }

       return NONE;
   }

    public Object getCdmcByCdbm(){
        String cdbm = getParameter("cdbm");
        setReturnData(getService().getCdmcByCdbm(cdbm));
        return SUCCESS;
    }
public String searchjjgjyData(){
    String barCode = getParameter("barCode");
    setReturnData(getService().searchjjgjyData(barCode));
    return  SUCCESS;


}
}
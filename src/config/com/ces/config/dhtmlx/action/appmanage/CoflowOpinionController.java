package com.ces.config.dhtmlx.action.appmanage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.CoflowOpinionDao;
import com.ces.config.dhtmlx.entity.appmanage.CoflowOpinion;
import com.ces.config.dhtmlx.service.appmanage.CoflowOpinionService;

public class CoflowOpinionController extends ConfigDefineServiceDaoController<CoflowOpinion, CoflowOpinionService, CoflowOpinionDao> {

    private static final long serialVersionUID = -7934815734119643790L;
    
    @Override
    protected void initModel() {
        setModel(new CoflowOpinion());
    }

    /*
     * (非 Javadoc)   
     * <p>标题: setService</p>   
     * <p>描述: 注入服务层(Service)</p>   
     * @param service   
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("coflowOpinionService")
    protected void setService(CoflowOpinionService service) {
        super.setService(service);
    }
    
    public Object opinions() {
        try {
            String dataId = getParameter("P_dataId");
            setReturnData(getService().getOpinions(dataId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        
        return NONE;
    }
}

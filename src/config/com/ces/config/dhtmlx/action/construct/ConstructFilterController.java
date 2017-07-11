package com.ces.config.dhtmlx.action.construct;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.construct.ConstructFilterDao;
import com.ces.config.dhtmlx.entity.construct.ConstructFilter;
import com.ces.config.dhtmlx.service.construct.ConstructFilterService;

/**
 * 数据过滤条件Controller
 * 
 * @author wanglei
 * @date 2015-05-20
 */
public class ConstructFilterController extends ConfigDefineServiceDaoController<ConstructFilter, ConstructFilterService, ConstructFilterDao> {

    private static final long serialVersionUID = 1L;

    @Override
    protected void initModel() {
        setModel(new ConstructFilter());
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("constructFilterService")
    protected void setService(ConstructFilterService service) {
        super.setService(service);
    }

    /**
     * 保存数据过滤条件
     * 
     * @return Object
     */
    public Object saveConstructFilter() {
        String topComVersionId = getParameter("P_topComVersionId");
        String componentVersionId = getParameter("P_componentVersionId");
        String tableId = getParameter("P_tableId");
        String rowsValue = getParameter("P_rowsValue");
        try {
            ConstructFilter constructFilter = getService().getConstructFilter(topComVersionId, componentVersionId, tableId);
            if (constructFilter == null) {
                constructFilter = new ConstructFilter();
                constructFilter.setTopComVersionId(topComVersionId);
                constructFilter.setComponentVersionId(componentVersionId);
                constructFilter.setTableId(tableId);
            }
            getService().saveConstructFilter(constructFilter, rowsValue);
            setReturnData("{'success':true}");
        } catch (Exception e) {
            setReturnData("{'success':false}");
            e.printStackTrace();
        }
        return new DefaultHttpHeaders("success").disableCaching();
    }

}

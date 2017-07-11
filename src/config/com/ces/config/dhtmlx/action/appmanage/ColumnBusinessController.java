package com.ces.config.dhtmlx.action.appmanage;

import java.util.Map;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.domain.Specification;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.ColumnBusinessDao;
import com.ces.config.dhtmlx.entity.appmanage.ColumnBusiness;
import com.ces.config.dhtmlx.service.appmanage.ColumnBusinessService;
import com.ces.config.dhtmlx.service.appmanage.TriggerService;
import com.ces.xarch.core.exception.FatalException;
import com.ces.xarch.core.utils.SearchFilter;
import com.ces.xarch.core.utils.SearchHelper;
import com.ces.xarch.core.web.jackson.BeanFilter;
import com.google.common.collect.Maps;
/**
 * 特殊业务字段
 * @author wang
 *
 */
public class ColumnBusinessController extends ConfigDefineServiceDaoController<ColumnBusiness, ColumnBusinessService, ColumnBusinessDao>{

	private static final long serialVersionUID = 1L;

	@Override
	protected void initModel() {
		setModel(new ColumnBusiness());
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
    @Qualifier("columnBusinessService")
    protected void setService(ColumnBusinessService service) {
        super.setService(service);
    }
	
	public Object show()throws FatalException {
		model = getService().findOne(buildEntitySpecification());
		processFilter((BeanFilter)model);
	    return new DefaultHttpHeaders("success").disableCaching();
    }
    
    private  Specification<ColumnBusiness> buildEntitySpecification() throws FatalException {
        Map<String, SearchFilter> filterMap = Maps.newHashMap();
        String id = getId(); // column relation id
        System.out.println("----------id---------"+id);
        filterMap.put("EQ_columnRelationId", new SearchFilter("columnRelationId", SearchFilter.Operator.EQ, id));
        return SearchHelper.buildSpecification(filterMap.values(), getModelClass());
    }
    
    @Override
	public Object create() throws FatalException {
		model = getService().save(model);
		String tableId = model.getTableId();
		getService(TriggerService.class).generateColumnRelationTrigger(tableId);
        return SUCCESS;
	}

	@Override
	public Object update() throws FatalException {
		getService().save(model);
		String tableId = model.getTableId();
		getService(TriggerService.class).generateColumnRelationTrigger(tableId);
		return SUCCESS;
	}
}

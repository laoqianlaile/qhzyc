package com.ces.component.vegetablesdemo.dhtmlx.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.component.vegetablesdemo.dhtmlx.dao.VegetablesDao;
import com.ces.component.vegetablesdemo.dhtmlx.entity.VegetablesEntity;
import com.ces.component.vegetablesdemo.dhtmlx.service.VegetablesService;
import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.xarch.core.exception.FatalException;

public class VegetablesController extends
ConfigDefineServiceDaoController<VegetablesEntity,VegetablesService,VegetablesDao>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	protected void initModel(){
		setModel(new VegetablesEntity());
	}
	@Autowired
    @Qualifier("vegetablesService")
    @Override
    protected void setService(VegetablesService service){
		super.setService(service);
	}
	@Override
	public Object create() throws FatalException {
	    Integer maxShowOrder = getService().getMaxShowOrder(model.getId());
	    int showOrder = 0;
	    if (maxShowOrder == null) {
	        showOrder = 1;
	    } else {
	        showOrder = maxShowOrder + 1;
	    }
	    model.setShowOrder(showOrder);
	    model = getService().save(model);
	    return SUCCESS;
	}
	
	

}

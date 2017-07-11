package com.ces.component.qyptsjzdlxxx.action;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.qyptsjzdlxxx.service.QyptsjzdlxxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class QyptsjzdlxxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, QyptsjzdlxxxService, TraceShowModuleDao> {

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


//    public void getGridData(){
//        String lxbm = getParameter("lxbm");
//        setReturnData(getService().getFlData(lxbm));
//    }
    
    public Object getGridData(){
    	String lxbm = getParameter("lxbm");
		//分页处理
		PageRequest pageRequest = this.buildPageRequest();
		list = getDataModel(getModelTemplate());		
		Page<Object> page = getService().getFlData( pageRequest,lxbm);
        list.setData(page);
		return NONE;
    	
    }
    
}
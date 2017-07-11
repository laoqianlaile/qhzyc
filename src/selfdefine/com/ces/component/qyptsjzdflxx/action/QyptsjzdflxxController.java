package com.ces.component.qyptsjzdflxx.action;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import ces.workflow.core.service.consumer.GetPageConsummer;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.qyptsjzdflxx.service.QyptsjzdflxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.xarch.core.entity.StringIDEntity;

public class QyptsjzdflxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, QyptsjzdflxxService, TraceShowModuleDao> {

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
 * 通过分类编码查看类型信息
 * @author zhaoben
 */
    public Object getGridData(){
//        int pageNumber = Integer.parseInt(getParameter("pageNumber"));
//        int pageSize = Integer.parseInt(getParameter("pageSize")) ;
//        setReturnData(getService().getFlData());
    	String ssxtbm = getParameter("ssxtbm");
		//分页处理
		PageRequest pageRequest = this.buildPageRequest();
		list = getDataModel(getModelTemplate());		
		Page<Object> page = getService().getFlData( pageRequest,ssxtbm);
        list.setData(page);
		return NONE;
   }
}
package com.ces.component.qyptzzfw.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.qyptzzfw.service.QyptzzfwService;
import com.ces.component.trace.utils.JSON;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.struts2.rest.DefaultHttpHeaders;

import java.util.Map;

public class QyptzzfwController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, QyptzzfwService, TraceShowModuleDao> {

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
     * 根据组织UUID获取增值服务信息
     * @return
     */
    public Object getIncrementServiceById() {
        String uuid = this.getRequest().getParameter("uuid");
        setReturnData(getService().getIncrementServiceById(uuid));
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

	/**
	 * 获取所有的增值服务信息
	 * @return
	 */
	public Object getAllIncrementServices() {
		setReturnData(getService().getAllIncrementServices());
		return new DefaultHttpHeaders(SUCCESS).disableCaching();
	}

	/**
	 * 设置增值服务
	 * @return
	 */
	public Object setIncrementService() {
		String services = this.getRequest().getParameter("services");
		Map<String, String> map = JSON.fromJSON(services, new TypeReference<Map<String, String>>() {
		});
		String uuid = this.getRequest().getParameter("uuid");
		getService().setIncrementService(uuid, map);
		return new DefaultHttpHeaders(SUCCESS).disableCaching();
	}

}
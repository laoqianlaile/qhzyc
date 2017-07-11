package com.ces.component.qyptjbfw.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.qyptjbfw.service.QyptjbfwService;
import com.ces.component.trace.utils.JSON;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.struts2.rest.DefaultHttpHeaders;

import java.util.Map;

public class QyptjbfwController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, QyptjbfwService, TraceShowModuleDao> {

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
     * 获取所有基本服务
     * @return
     */
    public Object getAllBaseServices() {
        setReturnData(getService().getAllBaseServices());
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 根据组织ID获取基本服务信息
     * @return
     */
    public Object getBaseServicesByOrgId() {
        String orgId = this.getRequest().getParameter("orgId");
        setReturnData(getService().getBaseServicesByOrgId(orgId));
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 设置基本服务
     * @return
     */
    public Object setBaseService() {
        String services = getParameter("services");
        String zhbh = getParameter("zhbh");
        getService().setBaseService(zhbh, services);
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
     * 根据组织ID获取增值服务信息
     * @return
     */
    public Object getIncrementServiceByOrgId() {
        String orgId = this.getRequest().getParameter("orgId");
        setReturnData(getService().getIncrementServiceByOrgId(orgId));
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 设置增值服务
     * @return
     */
    public Object setIncrementService() {
        String services = this.getRequest().getParameter("services");
        Map<String, String> map = JSON.fromJSON(services, new TypeReference<Map<String, String>>() {});
        String orgId = this.getRequest().getParameter("orgId");
        getService().setIncrementService(orgId, map);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 查看企业申请的服务信息
     * @return
     */
    public Object orgServiceView() {
        String orgId = this.getRequest().getParameter("orgId");
        setReturnData(getService().orgServiceView(orgId));
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 查看基本服务内容
     * @return
     */
    public Object getBaseServiceContent() {
        String serviceId = this.getRequest().getParameter("serviceId");
        setReturnData(getService().getBaseServiceContent(serviceId));
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

	/**
	 * 根据账户的UUID获取基本服务编号
	 * @return
	 */
	public Object getBaseServicesValue() {
		String uuid = getParameter("rowId");//企业列表ID
		setReturnData(getService().getBaseServicesValue(uuid));
		return new DefaultHttpHeaders(SUCCESS).disableCaching();
	}

}
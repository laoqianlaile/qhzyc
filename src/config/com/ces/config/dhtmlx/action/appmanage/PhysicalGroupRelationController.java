package com.ces.config.dhtmlx.action.appmanage;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.PhysicalGroupRelationDao;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalGroupRelation;
import com.ces.config.dhtmlx.service.appmanage.PhysicalGroupRelationService;
import com.ces.xarch.core.exception.FatalException;

public class PhysicalGroupRelationController extends ConfigDefineServiceDaoController<PhysicalGroupRelation, PhysicalGroupRelationService, PhysicalGroupRelationDao> {

	private static final long serialVersionUID = 1L;
	
	@Override
    protected void initModel() {
        setModel(new PhysicalGroupRelation());
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
    @Qualifier("physicalGroupRelationService")
    protected void setService(PhysicalGroupRelationService service) {
        super.setService(service);
    }
    
    /**
     * qiujinwei 2014-12-02 
     * <p>标题: save</p>
     * <p>描述: 保存</p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public Object save() throws FatalException {
        try {
        	String tableIds = getParameter("P_tableIds");
        	String groupId = getParameter("P_groupId");
        	getService().save(tableIds, groupId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * qiujinwei 2014-12-02 
     * <p>标题: delete</p>
     * <p>描述: 根据物理表Id和物理表组Id删除关系</p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public Object delete() throws FatalException {
    	String tableId = getParameter("P_tableId");
    	String groupId = getParameter("P_groupId");
        try {
        	String id = getService().getIdByTableIdAndGroupId(tableId, groupId);
        	getService().delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * qiujinwei 2014-12-09
     * <p>标题: getPhysicalGroupRelationByGroupId</p>
     * <p>描述: 根据物理表组Id获取表关系</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object getPhysicalGroupRelationByGroupId(){
    	String groupId = getParameter("P_groupId");
    	try {
    		setReturnData(getService().getPhysicalGroupRelationByGroupId(groupId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
	
}

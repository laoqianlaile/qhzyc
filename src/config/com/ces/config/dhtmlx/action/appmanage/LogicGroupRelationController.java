package com.ces.config.dhtmlx.action.appmanage;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.LogicGroupRelationDao;
import com.ces.config.dhtmlx.entity.appmanage.LogicGroupRelation;
import com.ces.config.dhtmlx.service.appmanage.LogicGroupRelationService;
import com.ces.xarch.core.exception.FatalException;

public class LogicGroupRelationController extends ConfigDefineServiceDaoController<LogicGroupRelation, LogicGroupRelationService, LogicGroupRelationDao> {

private static final long serialVersionUID = 1L;
	
	@Override
    protected void initModel() {
        setModel(new LogicGroupRelation());
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
    @Qualifier("logicGroupRelationService")
    protected void setService(LogicGroupRelationService service) {
        super.setService(service);
    }
    
    /**
     * qiujinwei 2014-11-28 
     * <p>标题: save</p>
     * <p>描述: 批量保存</p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public Object save() throws FatalException {
        try {
            String codes = getParameter("P_tableCodes"); 
            String groupCode = getParameter("P_groupCode");
            getService().saveAll(codes, groupCode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * qiujinwei 2014-11-28 
     * <p>标题: update</p>
     * <p>描述: 更新父逻辑表</p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public Object update() throws FatalException{
    	try {
    		String parentTableCode = getParameter("P_parentTableCode");
    		setReturnData(getService().update(parentTableCode, getId()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return NONE;
    }
    
    /**
     * qiujinwei 2014-12-08
     * <p>标题: adjustShowOrder</p>
     * <p>描述: 拖拽调整顺序</p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public Object adjustShowOrder() throws FatalException {
        try {
            String sourceIds = getParameter("P_sourceIds");
            String targetId = getParameter("P_targetId");
            String groupCode = getParameter("P_groupCode");
            getService().adjustShowOrder(sourceIds, targetId, groupCode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * qiujinwei 2014-12-09
     * <p>标题: check</p>
     * <p>描述: 检验该逻辑表组下的所有逻辑表父逻辑表是否符合要求</p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public Object check() throws FatalException {
        try {
        	String groupCode = getParameter("P_groupCode");
            setReturnData(getService().check(groupCode));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * 根据逻辑表组编码获取逻辑表组下的逻辑表信息
     * 
     * @param groupCode 逻辑表组编码
     * @return Object
     */
    public Object getLogicTablesByGroupCode(){
    	try {
        	String groupCode = getParameter("P_groupCode");
            setReturnData(getService().getLogicTablesByGroupCode(groupCode));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * 根据逻辑表组编码和父逻辑表编码获取逻辑表下拉单信息
     * 
     * @param groupCode 逻辑表组编码
     * @param parentTableCode 父逻辑表编码
     * @return Object
     */
    public Object getComboOfLogicTable(){
    	try {
        	String groupCode = getParameter("P_groupCode");
        	String parentTableCode = getParameter("P_parentTableCode");
            setReturnData(getService().getComboOfLogicTable(groupCode, parentTableCode));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
}

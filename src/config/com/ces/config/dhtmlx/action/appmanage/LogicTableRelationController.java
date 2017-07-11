package com.ces.config.dhtmlx.action.appmanage;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.LogicTableRelationDao;
import com.ces.config.dhtmlx.entity.appmanage.LogicTableRelation;
import com.ces.config.dhtmlx.service.appmanage.LogicTableRelationService;
import com.ces.xarch.core.exception.FatalException;

public class LogicTableRelationController extends ConfigDefineServiceDaoController<LogicTableRelation, LogicTableRelationService, LogicTableRelationDao> {

	private static final long serialVersionUID = 1L;
	
	@Override
    protected void initModel() {
        setModel(new LogicTableRelation());
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
    @Qualifier("logicTableRelationService")
    protected void setService(LogicTableRelationService service) {
        super.setService(service);
    }
    
    /**
     * qiujinwei 2014-12-22
     * <p>标题: getRelationByCode</p>
     * <p>描述: 根据逻辑表组编码和源逻辑表编码获取表关系</p>
     * @return List<Object[]>    返回类型   
     * @throws
     */
    public void getAllLogicTableRelationList(){
    	try {
    		String tableCode = getParameter("P_tableCode");
        	String groupCode = getParameter("P_groupCode");
        	setReturnData(getService().getRelationByCode(tableCode, groupCode));
		} catch (Exception e) {
			e.printStackTrace();
            throw new RuntimeException(e.getMessage());
		}
    }
    
    /**
     * 源逻辑表字段检索
     * 
     * @return
     * @throws FatalException
     */
    public Object getShowYTableColum() throws FatalException {
        String tableCode = getParameter("Q_tableCode");
        String groupCode = getParameter("Q_groupCode");
        setReturnData(getService().getLogicTableRelationColumn(tableCode, groupCode));
        return new DefaultHttpHeaders("success").disableCaching();
    }
    
    /**
     * 目标逻辑表字段检索
     * 
     * @return
     * @throws FatalException
     */
    public Object getShowMbTableColum() throws FatalException {
    	String tableCode = getParameter("Q_tableCode");
        String groupCode = getParameter("Q_groupCode");
        String relationTableCode = getParameter("Q_relationTableCode");
        setReturnData(getService().getLogicTableRelationColumn(tableCode, groupCode, relationTableCode));
        return new DefaultHttpHeaders("success").disableCaching();
    }
    
    /**
     * 逻辑表关系检索
     * 
     * @return
     * @throws FatalException
     */
    public Object getShowRelationColum() throws FatalException {
        String tableCode = getParameter("Q_tableCode");
        String groupCode = getParameter("Q_groupCode");
        setReturnData(getService().getShowRelationColum(tableCode, groupCode));
        return new DefaultHttpHeaders("success").disableCaching();
    }
    
    /**
     * 保存表关系
     * 
     * @return
     */
    public Object saveColumn() {
        try {
            String rowsValue = getParameter("P_rowsValue");
            String tableCode = getParameter("Q_tableCode");
            String parentTableCode = getParameter("Q_parentTableCode");
            String groupCode = getParameter("Q_groupCode");
            getService().saveColumn(rowsValue, tableCode, parentTableCode, groupCode);
            setReturnData(MessageModel.trueInstance("OK"));
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData(MessageModel.falseInstance("ERROR"));
        }
        return NONE;
    }
    
    /**
     * <p>描述: 根据字段ID查找表关系</p>
     * @param  columnId    设定参数   
     * @return LogicTableRelation   
     * @throws
     */
    public Object checkRelation() {
    	String rowIds = getParameter("P_rowsValue");
    	setReturnData(getService().checkRelation(rowIds));
    	return NONE;
    }

}

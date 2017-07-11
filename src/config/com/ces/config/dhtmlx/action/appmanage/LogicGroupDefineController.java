package com.ces.config.dhtmlx.action.appmanage;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.LogicGroupDefineDao;
import com.ces.config.dhtmlx.entity.appmanage.LogicGroupDefine;
import com.ces.config.dhtmlx.service.appmanage.LogicGroupDefineService;
import com.ces.xarch.core.exception.FatalException;

public class LogicGroupDefineController extends ConfigDefineServiceDaoController<LogicGroupDefine, LogicGroupDefineService, LogicGroupDefineDao> {

	private static final long serialVersionUID = 1L;
	
	@Override
    protected void initModel() {
        setModel(new LogicGroupDefine());
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
    @Qualifier("logicGroupDefineService")
    protected void setService(LogicGroupDefineService service) {
        super.setService(service);
    }
    
    /**
     * qiujinwei 2014-11-14 
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
            getService().adjustShowOrder(sourceIds, targetId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * 获取逻辑表组下拉框选项
     * 
     * @return Object
     */
    public Object comboOfLogicTableGroups() {
        try {
            setReturnData(getService().comboOfLogicTableGroups());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}

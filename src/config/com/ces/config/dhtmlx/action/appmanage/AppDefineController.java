/** 
 * <p>描述: TODO(用一句话描述该文件做什么)</p>
 * <p>公司：上海中信信息发展股份有限公司</p>
 * @author qiucs
 * @date 2013-6-19 上午10:13:50   
 * @version 1.0.2013    
 */ 

package com.ces.config.dhtmlx.action.appmanage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.AppDefineDao;
import com.ces.config.dhtmlx.entity.appmanage.AppDefine;
import com.ces.config.dhtmlx.service.appmanage.AppDefineService;
import com.ces.config.utils.CommonUtil;
import com.ces.xarch.core.exception.FatalException;

public class AppDefineController extends ConfigDefineServiceDaoController<AppDefine, AppDefineService, AppDefineDao> {

    private static final long serialVersionUID = 5936197808352295641L;
    
    private static Log log = LogFactory.getLog(AppDefineController.class);

    @Override
    protected void initModel() {
        setModel(new AppDefine());
    }
    
    /*
     * (非 Javadoc)   
     * <p>描述: 注入服务层(Service)</p>   
     * @param service   
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("appDefineService")
    protected void setService(AppDefineService service) {
        super.setService(service);
    }
    
    /**
     * <p>描述: 根据表ID获得所有模块应用配置情况列表</p>
     * @return Object    返回类型   
     * @throws FatalException
     */
    public Object query() throws FatalException {
        try {
            String tableId = getParameter("P_tableId");
            String menuId  = getParameter("P_menuId");
            String userId  = CommonUtil.getUser().getId();
            setReturnData(getService().query(tableId, menuId, userId));
        } catch (Exception e) {
            log.error("获取应用定义列表出错", e);
        }
        return NONE;
    }
    
    /**
     * qiucs 2014-12-22 上午10:46:34
     * <p>描述: 获取工作流应用定义配置信息 </p>
     * @return Object
     */
    public Object coflowQuery() throws FatalException {
        try {
            String workflowVersionId = getParameter("P_workflowVersionId");
            String userId  = CommonUtil.getUser().getId();
            setReturnData(getService().coflowQuery(workflowVersionId, userId));
        } catch (Exception e) {
            log.error("获取应用定义列表出错", e);
        }
        return NONE;
    }
    
    
    public Object appApplyTo () {
    	
    	try {
			String tableId = getParameter("P_tableId");
			String menuId = getParameter("P_menuId");
			String componentVersionId = getParameter("P_componentVersionId");
			String toMenuIds  = getParameter("P_appApplyToMenuIds");
			
			setReturnData(getService().appApplyTo(tableId, componentVersionId, menuId, toMenuIds));
		} catch (Exception e) {
			log.error("应用定义应用到其他构件上出错", e);
		}
    	
    	return NONE;
    }
    
}

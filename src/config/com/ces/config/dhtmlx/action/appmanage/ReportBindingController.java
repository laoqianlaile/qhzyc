package com.ces.config.dhtmlx.action.appmanage;


import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.ReportBindingDao;
import com.ces.config.dhtmlx.entity.appmanage.ReportBinding;
import com.ces.config.dhtmlx.service.appmanage.ReportBindingService;

public class ReportBindingController extends ConfigDefineServiceDaoController<ReportBinding, ReportBindingService, ReportBindingDao> {

    private static final long serialVersionUID = 187280515880868153L;

    /*
     * (非 Javadoc)   
     * <p>描述: 初始化数据模型</p>      
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ReportBinding());
    }
    
    /*
     * (非 Javadoc)   
     * <p>描述: 注入服务层(Service)</p>   
     * @param service   
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("reportBindingService")
    protected void setService(ReportBindingService service) {
        super.setService(service);
    }
    
    /**
     * <p>描述: 可选模块列表数据</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object defaultBindingModule() {
        String reportId = getParameter("Q_reportId");
        setReturnData(getService().getDefaultBindingModule(reportId));
        return new DefaultHttpHeaders("success").disableCaching();
    }
    
    /**
     * <p>描述: 已选模块列表数据</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object defineBindingModule() {
        String reportId = getParameter("Q_reportId");
        setReturnData(getService().getDefineBindingModule(reportId));
        return new DefaultHttpHeaders("success").disableCaching();
    }
    
    /**
     * <p>描述: 保存模块绑定配置</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object save() {
        try {
            String reportId = getParameter("P_reportId");
            String rowsValue = getParameter("P_rowsValue");
            getService().save(reportId, rowsValue);
        } catch (Exception e) {
            setStatus(false);
            setLogger(e.getMessage());
        }
        
        return new DefaultHttpHeaders("success").disableCaching();
    }
    
    /**
     * liao 2014-5-5 
     * <p>描述: 打印按钮上绑定了报表(去不可用的报表)</p>
     */
    public Object bindedReports() {
        try {
            String tableId  = getParameter("P_tableId");
            String moduleId = getParameter("P_moduleId");
            String componentVersionId = getParameter("P_componentVersionId");
            String menuId = getParameter("P_menuId");
            setReturnData(getService().getBindedReports(tableId, moduleId, componentVersionId, menuId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return SUCCESS;
    }
    
    /**
     * qiucs 2013-10-16 
     * <p>描述: 打印按钮配置</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object bindedReportIds() {
        try {
            String tableId  = getParameter("P_tableId");
            String moduleId = getParameter("P_moduleId");
            setReturnData(getService().getBindedReportIds(tableId, moduleId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return NONE;
    }
}

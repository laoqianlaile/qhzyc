package com.ces.config.dhtmlx.action.appmanage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.ReportPrintSettingDao;
import com.ces.config.dhtmlx.entity.appmanage.ReportPrintSetting;
import com.ces.config.dhtmlx.service.appmanage.ReportPrintSettingService;

public class ReportPrintSettingController extends ConfigDefineServiceDaoController<ReportPrintSetting, ReportPrintSettingService, ReportPrintSettingDao> {

    private static final long serialVersionUID = -5974049368499749549L;

    private static Log log = LogFactory.getLog(ReportPrintSettingController.class);

    @Override
    protected void initModel() {
        setModel(new ReportPrintSetting());
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
    @Qualifier("reportPrintSettingService")
    protected void setService(ReportPrintSettingService service) {
        super.setService(service);
    }
    
    /**
     * qiucs 2013-8-19 
     * <p>标题: save</p>
     * <p>描述: 保存</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object save() {
        try {
            String reportId = getParameter("P_reportId");
            String type = getParameter("P_type");
            String rowsValue = getParameter("P_rowsValue");
            getService().save(reportId, type, rowsValue);
            setReturnData(MessageModel.trueInstance("OK"));
        } catch (Exception e) {
            setReturnData(MessageModel.falseInstance("ERROR"));
            log.error("保存出错", e);
        }
        
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}

package com.ces.config.dhtmlx.action.appmanage;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.ReportTableRelationDao;
import com.ces.config.dhtmlx.entity.appmanage.ReportTableRelation;
import com.ces.config.dhtmlx.service.appmanage.ReportTableRelationService;

public class ReportTableRelationController extends ConfigDefineServiceDaoController<ReportTableRelation, ReportTableRelationService, ReportTableRelationDao> {

    private static final long serialVersionUID = 187280515880868153L;
    
    private static final Log log = LogFactory.getLog(ReportTableRelationController.class);

    /*
     * (非 Javadoc)   
     * <p>标题: initModel</p>   
     * <p>描述: 初始化数据模型</p>      
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ReportTableRelation());
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
    @Qualifier("reportTableRelationService")
    protected void setService(ReportTableRelationService service) {
        super.setService(service);
    }
    
    /**
     * <p>标题: defineTableAndColumn</p>
     * <p>描述: 报表表关系定义列表数据</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object defineTableRelation() {
        String reportId = getParameter("Q_reportId");
        setReturnData(getService().getDefineTableRelation(reportId));
        return new DefaultHttpHeaders("success").disableCaching();
    }
    
    /**
     * <p>标题: save</p>
     * <p>描述: 保存报表表关系配置</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object save() {
        try {
            String reportId = getParameter("P_reportId");
            String rowsValue = getParameter("P_rowsValue");
            getService().save(reportId, rowsValue);
            setReturnData(MessageModel.trueInstance("OK"));
        } catch (Exception e) {
            setReturnData(MessageModel.falseInstance("ERROR"));
            log.error("保存报表表关系配置出错", e);
        }
        
        return new DefaultHttpHeaders("success").disableCaching();
    }
    
    /**
     * qiucs 2013-8-12 
     * <p>标题: check</p>
     * <p>描述: 检查是否配置好表关系</p>
     * @return Object       返回类型   
     *         status:true  配置好
     *         status:false 未配置好
     * @throws
     */
    public Object check() {
        try {
            String reportId = getParameter("P_reportId");
            setReturnData(getService().check(reportId));
        } catch (Exception e) {
            setReturnData(MessageModel.falseInstance("ERROR"));
            log.error("检查是否配置好表关系出错", e);
        }
        
        return new DefaultHttpHeaders("success").disableCaching();
    }
    
    /**
     * qiucs 2015-3-4 下午3:03:31
     * <p>描述: 查找表关系 </p>
     * @return Object
     */
    public Object findTableRelation() {
        try {
            String reportId = getParameter("P_reportId");
            setReturnData(getService().findTableRelation(reportId));
        } catch (Exception e) {
            setReturnData(MessageModel.falseInstance("ERROR"));
            log.error("查找表关系出错", e);
        }
        return new DefaultHttpHeaders("success").disableCaching();
    }
}

package com.ces.config.dhtmlx.action.appmanage;


import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.ReportDataSourceDao;
import com.ces.config.dhtmlx.service.appmanage.ReportDataSourceService;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;

/**
 * <p>描述: 报表数据源定义（包括绑定表及字段）</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2013-8-9 上午10:03:32
 *
 */
public class ReportDataSourceController extends ConfigDefineServiceDaoController<StringIDEntity, ReportDataSourceService, ReportDataSourceDao> {

    private static final long serialVersionUID = 187280515880868153L;

    /*
     * (非 Javadoc)   
     * <p>标题: initModel</p>   
     * <p>描述: 初始化数据模型</p>      
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new StringIDEntity());
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
    @Qualifier("reportDataSourceService")
    protected void setService(ReportDataSourceService service) {
        super.setService(service);
    }
    
    /**
     * <p>标题: tables</p>
     * <p>描述: 获得表定义中所有已创建物理表的表数据</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object tables() {
        setReturnData(getService().getTables());
        return new DefaultHttpHeaders("success").disableCaching();
    }
    
    /**
     * <p>标题: columnsOfTable</p>
     * <p>描述: 表中所对应的字段</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object columnsOfTable() {
        String tableId = getParameter("Q_tableId");
        //String type    = getParameter("Q_type");
        //System.out.println(type);
        setReturnData(getService().getColumnsOfTable(tableId));
        return new DefaultHttpHeaders("success").disableCaching();
    }
    
    public Object columnsOfReport() {
        String reportId = getParameter("Q_reportId");
        //System.out.println(reportId);
        setReturnData(getService().getColumnsOfReport(reportId));
        return new DefaultHttpHeaders("success").disableCaching();
    }
    
    /**
     * <p>标题: defineTableAndColumn</p>
     * <p>描述: 数据源字段列表数据</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object defineTableAndColumn() {
        String reportId = getParameter("Q_reportId");
        setReturnData(getService().getDefineTableAndColumn(reportId));
        return new DefaultHttpHeaders("success").disableCaching();
    }
    
    /**
     * <p>标题: save</p>
     * <p>描述: 保存数据源字段配置</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object save() {
        try {
            String reportId = getParameter("P_reportId");
            String tabRowsValue = getParameter("P_tabRowsValue");
            String colRowsValue = getParameter("P_colRowsValue");
            getService().save(reportId, tabRowsValue, colRowsValue);
            setReturnData(MessageModel.trueInstance("OK"));
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData(MessageModel.falseInstance("ERROR"));
        }
        
        return new DefaultHttpHeaders("success").disableCaching();
    }
    
    /**
     * <p>标题: reportTables</p>
     * <p>描述: 获取报表所有数据源表</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object reportTables() {
        String reportId = getParameter("Q_reportId");
        setReturnData(getService().getReportTables(reportId));
        return null;
    }
    
    /*
     * (非 Javadoc)   
     * <p>标题: processTree</p>   
     * <p>描述: </p>   
     * @throws FatalException   
     * @see com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController#processTree()
     */
    protected void processTree() throws FatalException {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        String reportId = getParameter("P_reportId");
        String id = getId();
        System.out.println("=========id============" + id);
        if ("-1".equals(id)) {
            list.setData(getService().getReportTreeTables(reportId));
        } else {
            list.setData(getService().getReportTreeColumns(reportId, id));
        }
    }
}

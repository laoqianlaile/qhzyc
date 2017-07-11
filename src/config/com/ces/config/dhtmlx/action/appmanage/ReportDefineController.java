package com.ces.config.dhtmlx.action.appmanage;


import java.io.File;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.ReportDefineDao;
import com.ces.config.dhtmlx.entity.appmanage.ReportDefine;
import com.ces.config.dhtmlx.service.appmanage.ReportDefineService;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.xarch.core.exception.FatalException;
import com.ces.xarch.core.logger.Logger;
import com.ces.xarch.core.web.jackson.BeanFilter;

public class ReportDefineController extends ConfigDefineServiceDaoController<ReportDefine, ReportDefineService, ReportDefineDao> {

    private static final long serialVersionUID = 187280515880868153L;
    
    private static Log log = LogFactory.getLog(ReportDefineController.class);

    /*
     * (非 Javadoc)   
     * <p>标题: initModel</p>   
     * <p>描述: 初始化数据模型</p>      
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ReportDefine());
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
    @Qualifier("reportDefineService")
    protected void setService(ReportDefineService service) {
        super.setService(service);
    }
    
    /*
     * (非 Javadoc)   
     * <p>标题: edit</p>   
     * <p>描述: </p>   
     * @return
     * @throws FatalException   
     * @see com.ces.xarch.core.web.struts2.BaseController#edit()
     */
    @Override
    @Logger(model = "修改报表的绑定信息",action = "进入修改", logger = "修改CELL报表")
    public Object edit() throws FatalException {
        String reportId = getParameter("P_reportId");
        model = getService().getByReportId(reportId);
        processFilter((BeanFilter)model);
        
        return new DefaultHttpHeaders("edit").disableCaching();
    }
    
    /**
     * qiucs 2013-8-14 
     * <p>标题: getBindedColumns</p>
     * <p>描述: </p>
     * @return Object    返回类型   
     * @throws
     */
    public Object getBindedColumns() {
        try {
            String reportId = getParameter("P_reportId");
            setReturnData(getService().getBindedColumns(reportId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DefaultHttpHeaders("edit").disableCaching();
    }
    
    /**
     * qiucs 2013-8-14 
     * <p>标题: save</p>
     * <p>描述: 保存CELL报表中字段的绑定信息</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object save() {
        try {
            String reportId = getParameter("P_reportId");
            String fields   = getParameter("P_fields");
            System.out.println(fields);
            getService().save(reportId, fields);
            setReturnData(MessageModel.trueInstance("OK"));
        } catch (Exception e) {
            log.error("保存CELL报表出错", e);
        }
        return new DefaultHttpHeaders("success").disableCaching();
    }
    
    public Object saveAll() {
    	try {
            String reportId = getParameter("P_reportId");
            String fields   = getParameter("P_fields");
            String pageSetting = getParameter("P_pageSetting");
            String printSetting1 = getParameter("P_printSetting1");
            String printSetting2 = getParameter("P_printSetting2");
            String printSetting3 = getParameter("P_printSetting3");
            System.out.println(fields);
            getService().save(reportId, fields, pageSetting, printSetting1, printSetting2, printSetting3);
            setReturnData(MessageModel.trueInstance("OK"));
        } catch (Exception e) {
            log.error("保存CELL报表出错", e);
        }
        return new DefaultHttpHeaders("success").disableCaching();
    }
    
    /**
     * qiucs 2013-8-14 
     * <p>标题: cllUpload</p>
     * <p>描述: cll文件上传</p>
     * @return Object    返回类型   
     * @throws
     */
    @Logger(model ="cll报表上传" ,action ="cll报表文件上传")
    public Object cllUpload() {
        
        try {
            HttpServletRequest request = ServletActionContext.getRequest();
            InputStream is = request.getInputStream();
            String reportId = getParameter("P_reportId");
            File  cllFile = getCllFile(reportId);
            
            getService().cllUpload(cllFile, is);
            setReturnData(MessageModel.trueInstance("OK"));
        } catch (Exception e) {
            log.error("cll报表文件上传出错", e);
            setReturnData(MessageModel.falseInstance("ERROR"));
        }
        
        return new DefaultHttpHeaders("success").disableCaching();
    }
    /**
     * qiucs 2013-8-14 
     * <p>标题: getCllFile</p>
     * <p>描述: CELL报表文件在系统中的绝对路径</p>
     * @param  fileName
     * @return File    返回类型   
     * @throws
     */
    protected File getCllFile(String fileName) {
        String cllPath = CommonUtil.getAppRootPath();    
        cllPath += ConstantVar.UI.DHX_FOLDER + ConstantVar.Report.PATH + fileName + ".cll";
        
        System.out.println("cllPath: " + cllPath);
        
        return new File(cllPath);
    }
    
    
}

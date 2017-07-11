package com.ces.config.dhtmlx.action.appmanage;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.ReportDao;
import com.ces.config.dhtmlx.entity.appmanage.Report;
import com.ces.config.dhtmlx.service.appmanage.ReportDataSourceService;
import com.ces.config.dhtmlx.service.appmanage.ReportService;

public class ReportController extends ConfigDefineServiceDaoController<Report, ReportService, ReportDao> {

    private static final long serialVersionUID = 187280515880868153L;

    /*
     * (非 Javadoc)   
     * <p>标题: initModel</p>   
     * <p>描述: 初始化数据模型</p>      
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new Report());
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
    @Qualifier("reportService")
    protected void setService(ReportService service) {
        super.setService(service);
    }
    
    /**
     * qiucs 2013-10-16 
     * <p>描述: 表对应的所有报表</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object reportOfTable() {
        try {
            String tableId  = getParameter("P_tableId");
            String moduleId = getParameter("P_moduleId");
            setReturnData(getService(ReportDataSourceService.class).getReportByTableId(tableId, moduleId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        
        return NONE;
    }

    /**
     * 下载报表插件
     * 
     * @return Object
     */
    public Object downloadReportPlugin() {
        File file = new File(ServletActionContext.getServletContext().getRealPath("cfg-resource/thirdproduct/ffactivex-setup.exe"));
        HttpServletResponse response = ServletActionContext.getResponse();
        response.reset();
        response.setContentType("application/x-msdownload");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=ffactivex-setup.exe");
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            os = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = bis.read(b, 0, 1024)) > 0) {
                os.write(b, 0, len);
            }
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return NONE;
    }
}

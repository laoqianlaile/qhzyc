package com.ces.config.dhtmlx.action.release;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.release.SystemDownloadDao;
import com.ces.config.dhtmlx.entity.release.SystemDownload;
import com.ces.config.dhtmlx.service.release.SystemDownloadService;

/**
 * 系统下载Controller
 * 
 * @author wanglei
 * @date 2013-11-19
 */
public class SystemDownloadController extends
        ConfigDefineServiceDaoController<SystemDownload, SystemDownloadService, SystemDownloadDao> {

    private static final long serialVersionUID = 8371039330586502438L;

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new SystemDownload());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * 
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     *      StringIDService)
     */
    @Autowired
    @Qualifier("systemDownloadService")
    @Override
    protected void setService(SystemDownloadService service) {
        super.setService(service);
    }
}

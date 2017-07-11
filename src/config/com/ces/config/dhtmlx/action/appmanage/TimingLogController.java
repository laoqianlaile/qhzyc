package com.ces.config.dhtmlx.action.appmanage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.TimingLogDao;
import com.ces.config.dhtmlx.entity.appmanage.TimingLog;
import com.ces.config.dhtmlx.service.appmanage.TimingLogService;
import com.ces.config.utils.TimeManager;
import com.ces.xarch.core.exception.FatalException;

public class TimingLogController extends ConfigDefineServiceDaoController<TimingLog, TimingLogService, TimingLogDao> {

	private static final long serialVersionUID = 1L;
	
	@Override
    protected void initModel() {
        setModel(new TimingLog());
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
    @Qualifier("timingLogService")
    protected void setService(TimingLogService service) {
        super.setService(service);
    }

}

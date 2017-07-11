package com.ces.config.dhtmlx.service.appmanage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.appmanage.TimingLogDao;
import com.ces.config.dhtmlx.entity.appmanage.TimingLog;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

/**
 * 定时任务日志处理层
 * 
 * @author qiujinwei
 */
@Component
public class TimingLogService extends ConfigDefineDaoService<TimingLog, TimingLogDao> {

    /*
     * (非 Javadoc)
     * <p>标题: bindingDao</p>
     * <p>描述: 注入自定义持久层(Dao)</p>
     * @param entityClass
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("timingLogDao")
    @Override
    protected void setDaoUnBinding(TimingLogDao dao) {
        super.setDaoUnBinding(dao);
    }

}

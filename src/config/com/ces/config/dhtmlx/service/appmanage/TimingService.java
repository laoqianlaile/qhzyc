package com.ces.config.dhtmlx.service.appmanage;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.appmanage.TimingDao;
import com.ces.config.dhtmlx.entity.appmanage.TimingEntity;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

/**
 * 定时任务处理层
 * 
 * @author wang
 */
@Component
public class TimingService extends ConfigDefineDaoService<TimingEntity, TimingDao> {

    /*
     * (非 Javadoc)
     * <p>标题: bindingDao</p>
     * <p>描述: 注入自定义持久层(Dao)</p>
     * @param entityClass
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("timingDao")
    @Override
    protected void setDaoUnBinding(TimingDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 定时任务详情列表
     * 
     * @return
     */
    public Object getTimingTasks() {
        return getDao().getTimingTasks();
    }

    /**
     * 获取启动项列表
     * 
     * @return
     */
    public List<TimingEntity> getTimingStart() {
        return getDao().getTimingIsOperates();
    }

    /**
     * 获取任务的cmd命令语句
     * 
     * @param Id
     * @return
     */
    public Object getCommandById(String Id) {
        String command = getDao().getCommandById(Id);
        // String strCMD = "ping 192.168.1.100";
        Runtime rt = Runtime.getRuntime();
        try {
            Process p = rt.exec("cmd /k start  " + command);
            if (p != null) {
                p.destroy();
                p = null;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return command;
    }

    /**
     * 修改操作状态
     * 
     * @param operates
     * @param Id
     */
    public void updTimingOperates(String status, String Id) {
        String stat = "";
        if (status.equals("0")) {
            stat = "1";
        } else if (status.equals("1")) {
            stat = "0";
        }
        getDao().updTimingOperates(stat, Id);
    }

}

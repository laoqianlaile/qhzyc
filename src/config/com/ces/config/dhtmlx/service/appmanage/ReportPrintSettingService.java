package com.ces.config.dhtmlx.service.appmanage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.ReportPrintSettingDao;
import com.ces.config.dhtmlx.entity.appmanage.ReportPrintSetting;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.google.common.collect.Lists;

@Component
public class ReportPrintSettingService extends ConfigDefineDaoService<ReportPrintSetting, ReportPrintSettingDao>{

    /*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("reportPrintSettingDao")
    @Override
    protected void setDaoUnBinding(ReportPrintSettingDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    /**
     * qiucs 2013-8-19 
     * <p>标题: save</p>
     * <p>描述: </p>
     * @param  reportId
     * @param  type
     * @param  rowsValue    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    public void save(String reportId, String type, String rowsValue) {
        // 1. delete
        getDao().deleteByReportIdAndType(reportId, type);
        // 2. save
        List<ReportPrintSetting> list = Lists.newArrayList();
        String[] rowArr = rowsValue.split(";");
        for (int i = 0; i < rowArr.length; i++) {
            ReportPrintSetting setting = new ReportPrintSetting();
            setting.setOneRowValue(reportId, type, rowArr[i]);
            setting.setShowOrder((i+1));
            list.add(setting);
        }
        getDao().save(list);
    }
    
    /**
     * 根据报表ID获取打印设置数据
     * 
     * @param reportId 报表ID
     * @return List<ReportPrintSetting>
     */
    public List<ReportPrintSetting> findByReportId(String reportId) {
        return getDao().findByReportId(reportId);
    }
    
    /**
     * qiucs 2015-7-3 下午1:47:24
     * <p>描述: 获取报表配置信息 </p>
     * @return List<Object[]>
     */
    public List<Object[]> getSetting(String reportId, String type) {
    	return getDao().getSettigs(reportId, type);
    }
}

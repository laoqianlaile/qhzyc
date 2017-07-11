package com.ces.config.dhtmlx.service.appmanage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.AppReportDao;
import com.ces.config.dhtmlx.dao.appmanage.ReportColumnDao;
import com.ces.config.dhtmlx.dao.appmanage.ReportDao;
import com.ces.config.dhtmlx.dao.appmanage.ReportDataSourceDao;
import com.ces.config.dhtmlx.dao.appmanage.ReportDefineDao;
import com.ces.config.dhtmlx.dao.appmanage.ReportPrintSettingDao;
import com.ces.config.dhtmlx.dao.appmanage.ReportTableDao;
import com.ces.config.dhtmlx.dao.appmanage.ReportTableRelationDao;
import com.ces.config.dhtmlx.entity.appmanage.AppReport;
import com.ces.config.dhtmlx.entity.appmanage.Report;
import com.ces.config.dhtmlx.entity.appmanage.ReportColumn;
import com.ces.config.dhtmlx.entity.appmanage.ReportDataSource;
import com.ces.config.dhtmlx.entity.appmanage.ReportDefine;
import com.ces.config.dhtmlx.entity.appmanage.ReportPrintSetting;
import com.ces.config.dhtmlx.entity.appmanage.ReportTable;
import com.ces.config.dhtmlx.entity.appmanage.ReportTableRelation;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;

@Component
public class ReportService extends ConfigDefineDaoService<Report, ReportDao> {
    
    /*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("reportDao")
    @Override
    protected void setDaoUnBinding(ReportDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    @Override
    @Transactional
    public Report save(Report entity) {
        if (StringUtil.isEmpty(entity.getShowOrder())) {
            Integer showOrder = getDao().getMaxShowOrderByParentId(entity.getParentId());
            if (null == showOrder) {showOrder = 0;}
            entity.setShowOrder((showOrder + 1));
        }
        return super.save(entity);
    }

    @Override
    @Transactional
    public void delete(String id) {
        // 1. delete app report
        getDao(AppReportDao.class, AppReport.class).deleteByReportId(id);
        // 2. delete report print setting
        getDao(ReportPrintSettingDao.class, ReportPrintSetting.class).deleteByReportId(id);
        // 3. delete report define
        getDao(ReportDefineDao.class, ReportDefine.class).deleteByReportId(id);
        // 4. delete report table relation
        getDao(ReportTableRelationDao.class, ReportTableRelation.class).deleteByReportId(id);
        // 5. delete report column
        getDao(ReportColumnDao.class, ReportColumn.class).deleteByReportId(id);
        // 6. delete report table
        getDao(ReportTableDao.class, ReportTable.class).deleteByReportId(id);
        // 7. delete report 
        getDao().delete(id);
    }

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据表ID更新报表信息</p>
     * @param  tableId    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    public void updateByTableId(String tableId) {
        // delete report
        getDao(ReportTableDao.class, ReportTable.class).deleteByTableId(tableId);
        //
        getDao(ReportDataSourceDao.class, ReportDataSource.class).deleteByTableId(tableId);
        //
        getDao(ReportColumnDao.class, ReportColumn.class).deleteByTableId(tableId);
        //
        getDao(ReportTableRelationDao.class, ReportTableRelation.class).deleteByTableId(tableId);
        getDao(ReportTableRelationDao.class, ReportTableRelation.class).deleteByRelateTableId(tableId);    
    }

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据字段ID更新报表信息</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    public void deleteByColumnId(String columnId) {
        // delete report
        getDao(ReportPrintSettingDao.class, ReportPrintSetting.class).deleteByColumnId(columnId);
        //
        getDao(ReportDataSourceDao.class, ReportDataSource.class).deleteByColumnId(columnId);
        //
        getDao(ReportColumnDao.class, ReportColumn.class).deleteByColumnId(columnId);
        //
        getDao(ReportTableRelationDao.class, ReportTableRelation.class).deleteByColumnId(columnId);
        getDao(ReportTableRelationDao.class, ReportTableRelation.class).deleteByRelateColumnId(columnId);
    }
}

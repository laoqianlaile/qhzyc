package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ces.config.dhtmlx.dao.appmanage.ReportBindingDao;
import com.ces.config.dhtmlx.entity.appmanage.AppDefine;
import com.ces.config.dhtmlx.entity.appmanage.ReportBinding;
import com.ces.config.dhtmlx.service.authority.AuthorityReportService;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;
import com.google.common.collect.Lists;

@Component
public class ReportBindingService extends ConfigDefineDaoService<ReportBinding, ReportBindingDao> {
    /*
     * (非 Javadoc)   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("reportBindingDao")
    @Override
    protected void setDaoUnBinding(ReportBindingDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    /**
     * <p>描述: </p>
     * @param  reportId
     */
    public Object getDefaultBindingModule(String reportId) {
        return getDao().getDefaultBindingModule(reportId);
    }
    
    /**
     * <p>描述: </p>
     * @param  reportId
     */
    public Object getDefineBindingModule(String reportId) {
        return getDao().getDefineBindingModule(reportId);
    }

    /**
     * <p>描述: 保存</p>
     * @param  reportId
     * @param  rowsValue    设定参数   
     */
    @Transactional
    public void save(String reportId, String rowsValue) {
        Assert.state(StringUtil.isNotEmpty(reportId), "保存时，reportId不能为空！");
        // delete old configuration
        getDao().deleteByReportId(reportId);
        // save current configuration
        List<ReportBinding> list = Lists.newArrayList();
        String[] rowsArr = rowsValue.split(";");
        for (int i = 0; i < rowsArr.length; i++) {
            ReportBinding rds = new ReportBinding();
            rds.setOneRowValue(reportId, rowsArr[i]);
            rds.setShowOrder(Short.parseShort(String.valueOf(i)));
            list.add(rds);
        }
        getDao().save(list);
    }
    
    /**
     * liao 2014-5-5 
     * <p>描述: 根据模块ID获取报表（并去不可用的报表）</p>
     * @param  tableId
     * @param  moduleId
     * @param  menuId
     */
    public List<Object[]> getBindedReports(String tableId, String moduleId,String componentVersionId, String menuId) {
    	List<Object[]> list = new ArrayList<Object[]>();
        List<Object[]> rlt = getDao().getReportByFk(tableId, moduleId);
        if (null == rlt || rlt.isEmpty()) {
            rlt = getDao().getReportByFk(tableId, AppDefine.DEFAULT_DEFINE_ID);
        }
        if (null == rlt || rlt.isEmpty()) return (list);
        List<String> notAuthorityReports = getService(AuthorityReportService.class).notAuthorityReports(tableId, componentVersionId, menuId);
        if(!notAuthorityReports.isEmpty()) {
        	for(Object[] obj : rlt) {
            	if (!notAuthorityReports.isEmpty() && notAuthorityReports.contains(obj[0].toString())) {
                    continue;
                }
            	list.add(obj);
            }
        	return list;
        }
        
        return rlt;
    }
    
    /**
     * liaomingsong 2014-5-4 
     * <p>描述: 根据模块ID获取报表</p>
     * @param  tableId
     * @param  moduleId
     */
    public List<Object[]> getBindedReports(String tableId, String moduleId,String type) {
        List<Object[]> rlt = getDao().getReportByFk(tableId, moduleId);
        if (null == rlt) return (new ArrayList<Object[]>());
            
        return rlt;
    }
    
    /**
     * qiucs 2013-10-16 
     * <p>描述: </p>
     * @param  tableId
     * @param  moduleId
     * @return Object    返回类型   
     * @throws
     */
    public Object getBindedReportIds(String tableId, String moduleId) {
        Object rlt = getDao().getReportIdsByFk(tableId, moduleId);
        if (null == rlt) return (new ArrayList<String>());
        return rlt;
    }
    
    /**
     * 根据表ID和模块ID获取和报表的绑定关系
     * 
     * @param tableId 表ID
     * @param moduleId 模块ID
     * @return List<ReportBinding> 返回类型
     */
    public List<ReportBinding> findByTableIdAndModuleId(String tableId, String moduleId) {
        return getDao().findByTableIdAndModuleId(tableId, moduleId);
    }

    /**
     * qiucs 2013-10-16 
     * <p>描述: </p>
     * @param  tableId
     * @param  moduleId
     * @param  reportIds    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    public void save(String tableId, String moduleId, String reportIds) {
        // delete old configuration
        getDao().deleteByFk(tableId, moduleId);
        // save current configuration
        List<ReportBinding> list = Lists.newArrayList();
        String[] reportIdArr = reportIds.split(",");
        for (int i = 0; i < reportIdArr.length; i++) {
            ReportBinding rds = new ReportBinding();
            rds.setTableId(tableId);
            rds.setModuleId(moduleId);
            rds.setReportId(reportIdArr[i]);
            rds.setShowOrder(Short.parseShort(String.valueOf(i)));
            list.add(rds);
        }
        getDao().save(list);
    }
    
    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据模块ID删除配置</p>
     * @param  moduleId    设定参数   
     */
    public void deleteByModuleId(String moduleId) {
        getDao().deleteByModuleId(moduleId);
    }
}

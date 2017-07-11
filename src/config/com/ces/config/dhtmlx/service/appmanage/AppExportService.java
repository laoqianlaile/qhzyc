package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.AppExportDao;
import com.ces.config.dhtmlx.entity.appmanage.AppExport;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import com.ces.xarch.core.exception.FatalException;
import com.google.common.collect.Lists;

@Component
public class AppExportService extends ConfigDefineDaoService<AppExport, AppExportDao> {

    /*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("appExportDao")
    @Override
    protected void setDaoUnBinding(AppExportDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    /**
     * qiujinwei 2015-04-07 
     * <p>描述: 可选列表字段数据(前台列表数据,区分系统字段)</p>
     * @return Object    返回类型   
     * @throws
     */
    public List<AppExport> findDefaultList(String tableId, String componentVersionId, String menuId, String columnType) {
        List<AppExport> list = new ArrayList<AppExport>();
        List<Object[]> rlt = new ArrayList<Object[]>();
        if (StringUtil.isEmpty(columnType)) {
        	rlt = getDao().getDefaultColumn(tableId, componentVersionId, menuId);
		} else {
			rlt = getDao().getDefaultColumn(tableId, componentVersionId, menuId, columnType);
		}
        if (CollectionUtils.isNotEmpty(rlt)) {
            for (Object[] oArr : rlt) {
                // select t_cd.id as column_id, t_cd.show_name, t_cd.column_name
            	AppExport item = new AppExport();
                item.setId(String.valueOf(UUIDGenerator.uuid()));
                item.setColumnId(String.valueOf(oArr[0]));
                item.setShowName(String.valueOf(oArr[1]));
                item.setColumnName(String.valueOf(oArr[2]));
                list.add(item);
            }
        }
        return list;
    }

	/**
     * qiujinwei 2015-04-07 
     * <p>描述: 已选列表字段数据(前台列表数据,区分系统字段)</p>
     * @return Object    返回类型   
     * @throws
     */
    public List<AppExport> findDefineList(String tableId, String componentVersionId, String menuId, String columnType) {
        List<AppExport> list = new ArrayList<AppExport>();
        List<Object[]> rlt = new ArrayList<Object[]>();
        if (StringUtil.isEmpty(columnType)) {
        	rlt = getDao().getDefineColumn(tableId, componentVersionId, menuId);
		} else {
			rlt = getDao().getDefineColumn(tableId, componentVersionId, menuId, columnType);
		}
        if (CollectionUtils.isNotEmpty(rlt)) {
            for (Object[] oArr : rlt) {
                // select t_ae.id, t_ae.column_id, t_cd.show_name, t_cd.column_name
            	AppExport item = new AppExport();
            	item.setId(String.valueOf(oArr[0]));
                item.setColumnId(String.valueOf(oArr[1]));
                item.setShowName(String.valueOf(oArr[2]));
                item.setColumnName(String.valueOf(oArr[3]));
                list.add(item);
            }
        }
        return list;
    }
    
    /**
     * <p>描述: 保存检索的配置信息</p>
     * @return void    返回类型   
     */
    @Transactional
    public void save(AppExport master, String rowsValue) throws FatalException {
        String tableId = master.getTableId();
        String componentVersionId= master.getComponentVersionId();
        String menuId  = master.getMenuId();
        // 1. delete all search column by table id and menu id
        getDao().deleteByFk(tableId, componentVersionId, menuId);
        // 2. save columns setting
        String[] rowsArr = rowsValue.split(";");
        AppExport entity = null;
        List<AppExport> list = Lists.newArrayList();
        for (int i = 0; i <rowsArr.length; i++) {
        	if (StringUtil.isNotEmpty(rowsArr[i])) {
				String[] params = rowsArr[i].split("\\|");//columnId,columnName
				String columnId = params[0];
				String columnName = params[1];
	            entity = new AppExport();
	            entity.setTableId(tableId);
	            entity.setComponentVersionId(componentVersionId);
	            entity.setMenuId(menuId);
	            entity.setColumnId(columnId);
	            entity.setColumnName(columnName);
	            entity.setShowOrder(new Integer(i + 1));
	            list.add(entity);
			}
        }
        getDao().save(list);
    }
    
    /**
     * qiujinwei 2015-03-09 
     * <p>描述: 根据字段ID删除相关的字段配置信息</p>
     * @param  columnId    字段ID
     */
    @Transactional
    public void deleteByColumnId(String columnId) {
    	getDao().deleteByColumnId(columnId);
    }
}

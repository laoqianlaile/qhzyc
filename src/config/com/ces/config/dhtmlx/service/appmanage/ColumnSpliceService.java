package com.ces.config.dhtmlx.service.appmanage;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.ColumnRelationDao;
import com.ces.config.dhtmlx.dao.appmanage.ColumnSpliceDao;
import com.ces.config.dhtmlx.entity.appmanage.ColumnRelation;
import com.ces.config.dhtmlx.entity.appmanage.ColumnSplice;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;

/**
 * 字段拼接
 * 
 * @author wang
 */
@Component
public class ColumnSpliceService extends ConfigDefineDaoService<ColumnSplice, ColumnSpliceDao> {

	/*
	 * (非 Javadoc) <p>标题: bindingDao</p> <p>描述: 注入自定义持久层(Dao)</p>
	 * 
	 * @param entityClass
	 * 
	 * @see
	 * com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
	 */
	@Autowired
	@Qualifier("columnSpliceDao")
	@Override
	protected void setDaoUnBinding(ColumnSpliceDao dao) {
		super.setDaoUnBinding(dao);
	}
	
	/**
	 * 根据表ID获取该表下所有字段拼接
	 * 
	 * @param tableId 表ID
	 * @return List<ColumnSplice>
	 */
	public List<ColumnSplice> findByTableId(String tableId) {
	    return getDao().findByTableId(tableId);
	}

	@Override
	@Transactional
	public ColumnSplice save(ColumnSplice entity) {
		// 1. save column relation entity
		ColumnRelation relation = null;
		if (StringUtil.isNotEmpty(entity.getId()) && StringUtil.isNotEmpty(entity.getColumnRelationId())) {
			// update
			relation = getDao(ColumnRelationDao.class, ColumnRelation.class).findOne(entity.getColumnRelationId());
		} else {
			// new
			relation = new ColumnRelation();
			Integer maxShowOrder = getService(ColumnRelationService.class).getMaxShowOrder(entity.getTableId());
			int showOrder = 0;
	        if (maxShowOrder == null) {
	            showOrder = 1;
	        } else {
	            showOrder = maxShowOrder + 1;
	        }
			relation.setShowOrder(showOrder);
		}
		if (StringUtil.isEmpty(entity.getFill())) {
		    entity.setFill("0");
		}
		relation.setName(entity.getName());
		relation.setType("0");
		relation.setTableId(entity.getTableId());
		relation = getDao(ColumnRelationDao.class, ColumnRelation.class).save(relation);
		// 2. save column business entity
		entity.setColumnRelationId(relation.getId());
		entity = getDao().save(entity);
		
		return (entity);
	}
	
	/**
	 * qiucs 2013-12-2 
	 * <p>描述: 根据字段ID更新配置</p>
	 * @param  columnId    字段ID  
	 */
	@Transactional
	public void updateByColumnId(String columnId) {
	    getDao().updateByStoreColumnId(columnId);
	    getDao().updateByColumn1Id(columnId);
        getDao().updateByColumn2Id(columnId);
        getDao().updateByColumn3Id(columnId);
        getDao().updateByColumn4Id(columnId);
        getDao().updateByColumn5Id(columnId);
	}


}

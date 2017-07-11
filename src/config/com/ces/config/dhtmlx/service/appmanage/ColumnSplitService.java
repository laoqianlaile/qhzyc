package com.ces.config.dhtmlx.service.appmanage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.ColumnRelationDao;
import com.ces.config.dhtmlx.dao.appmanage.ColumnSplitDao;
import com.ces.config.dhtmlx.entity.appmanage.ColumnRelation;
import com.ces.config.dhtmlx.entity.appmanage.ColumnSplit;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;

/**
 * 字段截取
 * 
 * @author wang
 * 
 */

@Component
public class ColumnSplitService extends	ConfigDefineDaoService<ColumnSplit, ColumnSplitDao> {

	/*
	 * (非 Javadoc) <p>标题: bindingDao</p> <p>描述: 注入自定义持久层(Dao)</p>
	 * 
	 * @param entityClass
	 * 
	 * @see
	 * com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
	 */
	@Autowired
	@Qualifier("columnSplitDao")
	@Override
	protected void setDaoUnBinding(ColumnSplitDao dao) {
		super.setDaoUnBinding(dao);
	}

	@Override
	@Transactional
	public ColumnSplit save(ColumnSplit entity) {
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
		relation.setName(entity.getName());
		relation.setType("1");
		relation.setTableId(entity.getTableId());
		relation = getDao(ColumnRelationDao.class, ColumnRelation.class).save(relation);
		// 2. save column business entity
		entity.setColumnRelationId(relation.getId());
		entity = getDao().save(entity);
		
		return (entity);
	}
	
	/**
	 * qiucs 2013-10-21 
	 * <p>描述: 根据表ID查询截取配置信息</p>
	 * @param  tableId
	 * @return List<ColumnSplit>    返回类型   
	 * @throws
	 */
	public List<ColumnSplit> findByTableId(String tableId) {
	    return getDao().findByTableId(tableId);
	}
	
	/**
	 * qiucs 2013-12-2 
	 * <p>描述: 根据字段ID更新配置</p>
	 * @param  columnId    字段ID   
	 */
	public void updateByColumnId(String columnId) {
	    getDao().updateByFromColumnId(columnId);
	    getDao().updateByToColumnId(columnId);
	}
	
}

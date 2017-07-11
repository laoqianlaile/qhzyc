package com.ces.config.dhtmlx.service.appmanage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.ColumnBusinessDao;
import com.ces.config.dhtmlx.dao.appmanage.ColumnRelationDao;
import com.ces.config.dhtmlx.entity.appmanage.ColumnBusiness;
import com.ces.config.dhtmlx.entity.appmanage.ColumnRelation;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;

@Component
public class ColumnBusinessService extends ConfigDefineDaoService<ColumnBusiness, ColumnBusinessDao> {
    
    /*
     * (非 Javadoc)   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("columnBusinessDao")
    @Override
    protected void setDaoUnBinding(ColumnBusinessDao dao) {
        super.setDaoUnBinding(dao);
    }

    @Override
    @Transactional
	public ColumnBusiness save(ColumnBusiness entity) {
    	// 1. save column relation entity
    	ColumnRelation relation = null;
    	if (StringUtil.isNotEmpty(entity.getId()) && StringUtil.isNotEmpty(entity.getColumnRelationId())) {
    		// update
	    	relation = getDao(ColumnRelationDao.class, ColumnRelation.class).findOne(entity.getColumnRelationId());
    	} else {
    		// new
    		relation = new ColumnRelation();
    	}
    	relation.setName(entity.getName());
    	relation.setType("5");
    	relation.setTableId(entity.getTableId());
    	relation = getDao(ColumnRelationDao.class, ColumnRelation.class).save(relation);
    	// 2. save column business entity
    	entity.setColumnRelationId(relation.getId());
		entity = getDao().save(entity);
		
		return (entity);
	}
    
    /**
     * 根据表ID获取该表下所有的特殊业务
     * 
     * @param tableId 表ID
     * @return List<ColumnBusiness>
     */
    public List<ColumnBusiness> findByTableId(String tableId) {
        return getDao().findByTableId(tableId);
    }
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 根据字段ID删除配置</p>
     * @param  columnId    字段ID   
     */
    @Transactional
    public void updateByColumnId(String columnId) {
        getDao().updateByItemColumnId(columnId);
        getDao().updateByPagesColumnId(columnId);
        getDao().updateByPagenoColumnId(columnId);
    }
	
}

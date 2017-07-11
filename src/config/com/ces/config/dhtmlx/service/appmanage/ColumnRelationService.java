package com.ces.config.dhtmlx.service.appmanage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.ColumnBusinessDao;
import com.ces.config.dhtmlx.dao.appmanage.ColumnOperationDao;
import com.ces.config.dhtmlx.dao.appmanage.ColumnRelationDao;
import com.ces.config.dhtmlx.dao.appmanage.ColumnSpliceDao;
import com.ces.config.dhtmlx.dao.appmanage.ColumnSplitDao;
import com.ces.config.dhtmlx.entity.appmanage.ColumnBusiness;
import com.ces.config.dhtmlx.entity.appmanage.ColumnOperation;
import com.ces.config.dhtmlx.entity.appmanage.ColumnRelation;
import com.ces.config.dhtmlx.entity.appmanage.ColumnSplice;
import com.ces.config.dhtmlx.entity.appmanage.ColumnSplit;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.utils.BeanUtils;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;

@Component
public class ColumnRelationService extends ConfigDefineDaoService<ColumnRelation, ColumnRelationDao>{
	
    /*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("columnRelationDao")
    @Override
    protected void setDaoUnBinding(ColumnRelationDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    private ColumnRelationDao getColumnRelationDao() {
        return getDao(ColumnRelationDao.class, StringIDEntity.class); 
    }
    
	/**
	 * 字段关联定义列表
	 * @param tableId
	 * @return
	 */
	public Object getAllColumnRelations(String tableId){
		return getColumnRelationDao().getAllColumnRelation(tableId);
	}
	
	/**
     * 根据表ID获取该表下所有字段关联
     * 
     * @param tableId 表ID
     * @return List<ColumnRelation>
     */
    public List<ColumnRelation> findByTableId(String tableId) {
        return getDao().findByTableId(tableId);
    }
	
	/**
	 * 字段关联功能下复制字段
	 * @param rId
	 * @param type
	 * @throws FatalException
	 */
	@Transactional
    public void saveCopyColumnServcie(String rId, String type) throws FatalException {
		ColumnRelation entity = getByID(rId);
		if (null == entity) {return;}
		//1.复制字段关系表信息
		ColumnRelation newColumnRelation = new ColumnRelation();
		newColumnRelation.setName(entity.getName()+"(复制)");
		newColumnRelation.setTableId(entity.getTableId());
		newColumnRelation.setType(type);
		getDao().save(newColumnRelation);
		//2.复制相应的字段关系
    	if(type.equals("0")){
    		//拼接
    		ColumnSplice columnSplice = getDao(ColumnSpliceDao.class, ColumnSplice.class).findByColumnRelationId(rId);
    		ColumnSplice newcolumnSplice = new ColumnSplice();
    		
    		BeanUtils.copy(columnSplice, newcolumnSplice);
    		newcolumnSplice.setId("");
    		newcolumnSplice.setColumnRelationId(newColumnRelation.getId());
    		newcolumnSplice.setName(columnSplice.getName()+"(复制)");
    		getDao(ColumnSpliceDao.class, ColumnSplice.class).save(newcolumnSplice);
    	}
    	if(type.equals("1")){
    		//截取
    		ColumnSplit  columnSplit = getDao(ColumnSplitDao.class, ColumnSplit.class).findByColumnRelationId(rId);
    		ColumnSplit newColumnSplit = new ColumnSplit();
    		
    		BeanUtils.copy(columnSplit, newColumnSplit);
    		newColumnSplit.setId("");
    		newColumnSplit.setColumnRelationId(newColumnRelation.getId());
    		newColumnSplit.setName(columnSplit.getName()+"(复制)");
    		getDao(ColumnSplitDao.class, ColumnSplit.class).save(newColumnSplit);
    	}
    	if(type.equals("2") || type.equals("3") || type.equals("4")){
    		//继承、求和、最值
    		ColumnOperation columnOperation = getDao(ColumnOperationDao.class, ColumnOperation.class).findByColumnRelationId(rId);
    		ColumnOperation newColumnOperation = new ColumnOperation();
    		
    		BeanUtils.copy(columnOperation, newColumnOperation);
    		newColumnOperation.setId("");
    		newColumnOperation.setColumnRelationId(newColumnRelation.getId());
    		newColumnOperation.setName(columnOperation.getName()+"(复制)");
    		getDao(ColumnOperationDao.class, ColumnOperation.class).save(newColumnOperation);
    	}if(type.equals("5")){
    		//特殊业务字段
    		ColumnBusiness columnBusiness = getDao(ColumnBusinessDao.class, ColumnBusiness.class).findByColumnRelationId(rId);
    		ColumnBusiness newColumnBusiness = new ColumnBusiness();
    		
    		BeanUtils.copy(columnBusiness, newColumnBusiness);
    		newColumnBusiness.setId("");
    		newColumnBusiness.setColumnRelationId(newColumnRelation.getId());
    		newColumnBusiness.setName(columnBusiness.getName()+"(复制)");
    		getDao(ColumnBusinessDao.class, ColumnBusiness.class).save(newColumnBusiness);
    	}
    }

    /**
     * qiucs 2013-10-22 
     * <p>描述: 删除字段关系及对应的配置</p>
     * @param  entity
     * @return String    返回类型   
     * @throws
     */
    @Transactional
    public String deleteRelation(String id) {
        ColumnRelation entity = getByID(id);
        String tableIds = "";
        if ("0".equals(entity.getType())) {
            ColumnSplice splice = getDao(ColumnSpliceDao.class, ColumnSplice.class).findByColumnRelationId(entity.getId());
            if (null != splice) {
                tableIds = (splice.getTableId());
                getDao(ColumnSpliceDao.class, ColumnSplice.class).delete(splice);
            }
        } else if ("1".equals(entity.getType())) {
            ColumnSplit split = getDao(ColumnSplitDao.class, ColumnSplit.class).findByColumnRelationId(entity.getId());
            if (null != split) {
                tableIds = split.getTableId();
                getDao(ColumnSplitDao.class, ColumnSplit.class).delete(split);
            }
        } else if ("2".equals(entity.getType()) || "3".equals(entity.getType()) || "4".equals(entity.getType())) {
            ColumnOperation operation = getDao(ColumnOperationDao.class, ColumnOperation.class).findByColumnRelationId(entity.getId());
            if (null != operation) {
                tableIds = operation.getTableId() + "," + operation.getOriginTableId();
                getDao(ColumnOperationDao.class, ColumnOperation.class).delete(operation);
            }
        } else if ("5".equals(entity.getType())) {
            ColumnBusiness business = getDao(ColumnBusinessDao.class, ColumnBusiness.class).findByColumnRelationId(entity.getId());
            if (null != business) {
                tableIds = business.getTableId();
                getDao(ColumnBusinessDao.class, ColumnBusiness.class).delete(business);
            }
        }
        getDao().delete(entity);
        return tableIds;
    }
    
    /**
     * qiucs 2013-11-28 
     * <p>描述: 根据表ID删除字段关系</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    public void deleteByTableId(String tableId) {
        //特殊业务
        getDao(ColumnBusinessDao.class, ColumnBusiness.class).deleteByTableId(tableId);
        //字段继承、统计、最值
        getDao(ColumnOperationDao.class, ColumnOperation.class).deleteByTableId(tableId);
        //字段拼接
        getDao(ColumnSpliceDao.class, ColumnSplice.class).deleteByTableId(tableId);
        //字段截取
        getDao(ColumnSplitDao.class, ColumnSplit.class).deleteByTableId(tableId);
        //字段关系汇总列表记录
        getDao().deleteByTableId(tableId);
    }
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 字段ID删除，同步更新字段关联配置</p>
     * @param  columnId    字段ID
     */
    @Transactional
    public void updateByColumnId(String columnId) {
        // 特殊业务
        getService(ColumnBusinessService.class).updateByColumnId(columnId);
        // 字段拼接
        getService(ColumnSpliceService.class).updateByColumnId(columnId);
        // 字段截取
        getService(ColumnSplitService.class).updateByColumnId(columnId);
        // 字段运算：最值、统计、继承
        getService(ColumnOperationService.class).updateByColumnId(columnId);
    }

    /**
	 * liaomingsong 2014-5-14 
	 * <p>描述: 根据tableId 获取字段显示顺序范围内</p>
	 * @param  start  end tableId
	 * @return List<ColumnRelation>    返回类型   
	 * @throws
	 */
	public List<ColumnRelation> getColumnListByShowOrder(Integer start, Integer end, String tableId) {
		return getDao().getByShowOrderBetweenAndTableId(start, end, tableId);
	}

	public Integer getMaxShowOrder(String tableId) {
		return getDao().getMaxShowOrder(tableId);
	}
	
}

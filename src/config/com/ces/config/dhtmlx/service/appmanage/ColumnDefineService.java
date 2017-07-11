package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.datamodel.option.OptionModel;
import com.ces.config.dhtmlx.dao.appmanage.AppColumnDao;
import com.ces.config.dhtmlx.dao.appmanage.AppFilterDao;
import com.ces.config.dhtmlx.dao.appmanage.AppFormElementDao;
import com.ces.config.dhtmlx.dao.appmanage.AppSearchDao;
import com.ces.config.dhtmlx.dao.appmanage.AppSortDao;
import com.ces.config.dhtmlx.dao.appmanage.ColumnDefineDao;
import com.ces.config.dhtmlx.dao.appmanage.ModuleDao;
import com.ces.config.dhtmlx.dao.authority.AuthorityDataDetailCopyDao;
import com.ces.config.dhtmlx.dao.authority.AuthorityDataDetailDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.component.ComponentReserveZoneDao;
import com.ces.config.dhtmlx.entity.appmanage.AppColumn;
import com.ces.config.dhtmlx.entity.appmanage.AppFilter;
import com.ces.config.dhtmlx.entity.appmanage.AppFormElement;
import com.ces.config.dhtmlx.entity.appmanage.AppSearch;
import com.ces.config.dhtmlx.entity.appmanage.AppSort;
import com.ces.config.dhtmlx.entity.appmanage.ColumnDefine;
import com.ces.config.dhtmlx.entity.appmanage.LogicTableDefine;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.appmanage.TableRelation;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.AppUtil;
import com.ces.config.utils.CodeUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.FormUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.TableUtil;
import com.ces.config.utils.WorkflowUtil;
import com.ces.utils.BeanUtils;
import com.ces.xarch.core.exception.BusinessException;
import com.google.common.collect.Lists;

/*
 * 表定义
 */
@Component
public class ColumnDefineService extends ConfigDefineDaoService<ColumnDefine, ColumnDefineDao> {
    /*
     * (非 Javadoc)
     * <p>描述: 注入自定义持久层(Dao)</p>
     * @param entityClass
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("columnDefineDao")
    @Override
    protected void setDaoUnBinding(ColumnDefineDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * 表定义列表操作项
     */
    @Transactional
    public void updateStatus(String columnName, String value, String id) {
        DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();// (DatabaseHandlerDao)getDaoFromContext("databaseHandlerDao");

        String sql = "update t_xtpz_column_define set " + columnName + " = '" + value + "' where id='" + id + "'";
        dao.executeSql(sql);
        deleteRelation(id, columnName, value);
    }

    /*
     * 表定义插入列到数据库
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @Override
    @Transactional
    public ColumnDefine save(ColumnDefine entity) {
    	entity.uploadDataTypeExtend();
    	//
    	if (StringUtil.isEmpty(entity.getId()) || StringUtil.isEmpty(entity.getShowOrder())) {
            Integer maxShowOrder = getDao().getMaxShowOrder(entity.getTableId());
            if (null == maxShowOrder) {
                maxShowOrder = new Integer(0);
            }
            entity.setShowOrder((maxShowOrder + 1));
        }
    	// 系统字段保存
    	if (ColumnDefine.COMMON_TABLE_ID.equals(entity.getTableId())) {
    		entity.setColumnType("1");
    	    entity = getDao().save(entity);
    	    return entity;
    	}
        // 获取表名
        PhysicalTableDefine table = null;
        table = getService(PhysicalTableDefineService.class).getByID(entity.getTableId());
        if (table == null) { //逻辑表列存储
            entity = getDao().save(entity);
            return entity;
		}
        ColumnDefine oldEntity = null;
        boolean isCreated = StringUtil.isNotEmpty(entity.getId());
        if (isCreated) {
        	oldEntity = new ColumnDefine();
        	BeanUtils.copy(getByID(entity.getId()), oldEntity);
        }
        // 保存字段并创建对应的物理表字段
    	entity = save(entity, table);
        if (!isCreated) {
        	updateViews(table.getId());
		}
        
        // 生成表单元素预留区
        saveFormReserveZone(entity, oldEntity, table);
        // 同步索引库结构
        //IndexCommonUtil.syncSchema(table);
        // 更新表单缓存的显示类型
        if (null != oldEntity && (
        		!StringUtil.null2empty(entity.getInputType()).equals(StringUtil.null2empty(oldEntity.getInputType())) ||
        		!StringUtil.null2empty(entity.getCodeTypeCode()).equals(StringUtil.null2empty(oldEntity.getCodeTypeCode())) ||
        		!StringUtil.null2empty(entity.getDataTypeExtend()).equals(StringUtil.null2empty(oldEntity.getDataTypeExtend())) ||
        		!StringUtil.null2empty(entity.getInputOption()).equals(StringUtil.null2empty(oldEntity.getInputOption())) ||
        		!StringUtil.null2zero(entity.getLength()).equals(StringUtil.null2zero(oldEntity.getLength())) ||
        		!entity.getShowName().equals(oldEntity.getShowName()))) {
        	AppUtil.getInstance().updateColumn(entity);
        }
        return entity;
    }

    /**
     * qiucs 2015-4-30 上午11:17:36
     * <p>描述: 保存并生成物理表字段 </p>
     * @return ColumnDefine
     */
    @Transactional
    public ColumnDefine save(ColumnDefine entity, PhysicalTableDefine table) {
        // 判断是否是模板表、视图（公共字段表、模板表、视图不需要创建物理表字段）
        if (!ColumnDefine.COMMON_TABLE_ID.equals(entity.getTableId()) && !ConstantVar.TableClassification.TEMPLATE.equals(table.getClassification())
                && !ConstantVar.TableClassification.VIEW.equals(table.getClassification())) {
            DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();//
            if ("1".equals(entity.getCreated())) {
                // 2-1. get old entity check column name
                ColumnDefine oldEntity = getDao().findOne(entity.getId());
                if (dao.columnExists(table.getTableName(), oldEntity.getColumnName())) {
                    if (!oldEntity.getColumnName().toUpperCase().equals(entity.getColumnName().toUpperCase())) {
                        // 2-1-1. rename column name
                        dao.renameColumn(table.getTableName(), oldEntity.getColumnName().toUpperCase(), entity.getColumnName().toUpperCase());
                    }
                    if (/*!ConstantVar.ColumnName.ID.equals(entity.getColumnName().toUpperCase())
                            && (!oldEntity.getDataType().equals(entity.getDataType()) || oldEntity.getLength().intValue() != entity.getLength().intValue() || !StringUtil
                                    .null2empty(oldEntity.getDefaultValue()).equals(StringUtil.null2empty(entity.getDefaultValue()))) ||
                                    !StringUtil.null2empty(oldEntity.getDataTypeExtend()).equals(StringUtil.null2empty(entity.getDataTypeExtend()))*/
                    		canAlterColumn(oldEntity, entity, table)) {
                        // 2-2. 更新物理表字段
                    	if (entity.getInputOption().equals(FormUtil.CoralInputOption.FLOATINGLABEL)) {
                    		dao.alterColumn(table.getTableName(), entity.getColumnName(), entity.getDataType(), entity.getLength(), Integer.parseInt(StringUtil.null2zero(entity.getPrecision())), entity.getDefaultValue());
						} else {
							dao.alterColumn(table.getTableName(), entity.getColumnName(), entity.getDataType(), entity.getLength(), Integer.parseInt(StringUtil.null2zero(entity.getDataTypeExtend())), entity.getDefaultValue());
						}
                    } else {
						throw new BusinessException("字段不能被修改");
					}
                } else {
                    dao.addColumn(table.getTableName(), entity.getColumnName(), entity.getDataType(), entity.getLength(), Integer.parseInt(StringUtil.null2zero(entity.getDataTypeExtend())), entity.getDefaultValue());
                }
            } else {
                // 2-1. 插入字段或更新字段(更新物理表)
                if (!dao.columnExists(table.getTableName(), entity.getColumnName())) {
                    dao.addColumn(table.getTableName(), entity.getColumnName(), entity.getDataType(), entity.getLength(), Integer.parseInt(StringUtil.null2zero(entity.getDataTypeExtend())), entity.getDefaultValue());
                } else {
                    dao.alterColumn(table.getTableName(), entity.getColumnName(), entity.getDataType(), entity.getLength(), Integer.parseInt(StringUtil.null2zero(entity.getDataTypeExtend())), entity.getDefaultValue());
                }
                // 2-2. 更新是否创建物理表字段标记
                entity.setCreated("1");
            }
            // 给字段增加注释
            dao.commentColumn(table.getTableName(), entity.getColumnName(), entity.getShowName());
        }
        return super.save(entity);
    }
    
    /**
     *  2015-4-30 上午11:16:47
     * <p>描述: 生成表单元素预留区 </p>
     * @return void
     */
    @Transactional
    public void saveFormReserveZone(ColumnDefine entity, ColumnDefine oldEntity, PhysicalTableDefine table) {
    	if (oldEntity != null) {
            if ((FormUtil.CoralInputType.COMBOGRID.equals(oldEntity.getInputType()) && !FormUtil.CoralInputType.COMBOGRID.equals(entity.getInputType()))
                    || (FormUtil.CoralInputOption.TEXTBOXBUTTON.equals(oldEntity.getInputOption()) && !FormUtil.CoralInputOption.TEXTBOXBUTTON.equals(entity.getInputOption()))) {
                // 删除下拉列表按钮预留区、文本框按钮预留区
	            String reserveZoneName = AppDefineUtil.RZ_NAME_PRE.concat(oldEntity.getId());
                getDaoFromContext(ComponentReserveZoneDao.class).deleteReserveZoneByName(reserveZoneName);
            } else if ((!FormUtil.CoralInputType.COMBOGRID.equals(oldEntity.getInputType()) && FormUtil.CoralInputType.COMBOGRID.equals(entity.getInputType()))
                    || (!FormUtil.CoralInputOption.TEXTBOXBUTTON.equals(oldEntity.getInputOption()) && FormUtil.CoralInputOption.TEXTBOXBUTTON.equals(entity.getInputOption()))) {
	            // 新增下拉列表按钮预留区、文本框按钮预留区
	            List<Module> moduleList = getDaoFromContext(ModuleDao.class).getByTableId("%" + table.getId() + "%");
	            if (CollectionUtils.isNotEmpty(moduleList)) {
	                for (Module module : moduleList) {
	                    getService(ModuleService.class).saveReserveZone(module.getComponentVersionId(), AppDefineUtil.getPage(0), table.getShowName(), entity);
	                }
	            }
	        }
        } else {
            if (FormUtil.CoralInputType.COMBOGRID.equals(entity.getInputType()) || FormUtil.CoralInputOption.TEXTBOXBUTTON.equals(entity.getInputOption())) {
	            // 新增下拉列表按钮预留区、文本框按钮预留区
                List<Module> moduleList = getDaoFromContext(ModuleDao.class).getByTableId("%" + table.getId() + "%");
                if (CollectionUtils.isNotEmpty(moduleList)) {
                    for (Module module : moduleList) {
                        getService(ModuleService.class).saveReserveZone(module.getComponentVersionId(), AppDefineUtil.getPage(0), table.getShowName(), entity);
                    }
                }
            }
        }
    }
    
    /**
     * qiucs 2015-7-14 下午5:35:40
     * <p>描述: 判断字段是否可以被修改 </p>
     * @return boolean
     */
    private boolean canAlterColumn(ColumnDefine oldEntity, ColumnDefine newEntity, PhysicalTableDefine table) {
    	if (ConstantVar.ColumnName.ID.equals(newEntity.getColumnName().toUpperCase())) {
    		return false;
    	}
    	if (DatabaseHandlerDao.getInstance().isEmptyData(table.getTableName(), newEntity.getColumnName(), oldEntity.getDataType())) {
    		return true;
    	}
    	String dataType = newEntity.getDataType();
    	int oldLength = Integer.parseInt(StringUtil.null2zero(oldEntity.getLength()));
    	int newLength = Integer.parseInt(StringUtil.null2zero(newEntity.getLength()));
    	if (oldLength > newLength) {
    		//newEntity.setLength(oldEntity.getLength());
    		return false;
    	}
    	if (ConstantVar.DataType.NUMBER.equals(dataType)) {
    		// 浮点型+单位时，精度和单位作为json保存在dataTypeExtend
    		if (StringUtil.isNotEmpty(newEntity.getInputOption()) &&  newEntity.getInputOption().equals(FormUtil.CoralInputOption.FLOATINGLABEL)) { 
    			int oldPrecision = Integer.parseInt(StringUtil.null2zero(oldEntity.getPrecision()));
        		int newPrecesion = Integer.parseInt(StringUtil.null2zero(newEntity.getPrecision()));
        		if (oldPrecision > newPrecesion || 
        				(newLength < oldLength + (newPrecesion - oldPrecision))) {
        			newEntity.setPrecision(oldEntity.getPrecision());
        			newEntity.uploadDataTypeExtend();
        			return false;
        		}
			} else {
				int oldPrecision = Integer.parseInt(StringUtil.null2zero(oldEntity.getDataTypeExtend()));
	    		int newPrecesion = Integer.parseInt(StringUtil.null2zero(newEntity.getDataTypeExtend()));
	    		if (oldPrecision > newPrecesion || 
	    				(newLength < oldLength + (newPrecesion - oldPrecision))) {
	    			newEntity.setDataTypeExtend(oldEntity.getDataTypeExtend());
	    			return false;
	    		}
			}
    	}
		//if (oldLength < newLength) return true;
		//if (!StringUtil.null2empty(oldEntity.getDefaultValue()).equals(StringUtil.null2empty(newEntity.getDefaultValue()))) return true;
    	
    	return true;
    }

    /*
     * qiucs 2015-3-23 下午5:27:03
     * (non-Javadoc)
     * @see com.ces.config.service.base.StringIDConfigDefineDaoService#delete(java.lang.String)
     */
    @Override
    @Transactional
    public void delete(String ids) {
        String[] idArr = ids.split(",");
        // 1. drop columns
        PhysicalTableDefine table = null;
        for (int i = 0; i < idArr.length; i++) {
            // 1-1. get entity by id
            ColumnDefine entity = getByID(idArr[i]);
            if (!entity.getTableId().equals("-C") && entity.getTableId().length() == 32) {
	            if (null == table) {
	                table = TableUtil.getTableEntity(entity.getTableId());
	            }
	            delete(entity, table);
            }
            if (FormUtil.CoralInputType.COMBOGRID.equals(entity.getInputType()) || FormUtil.CoralInputOption.TEXTBOXBUTTON.equals(entity.getInputOption())) {
                String reserveZoneName = AppDefineUtil.RZ_NAME_PRE.concat(entity.getId());
                getDaoFromContext(ComponentReserveZoneDao.class).deleteReserveZoneByName(reserveZoneName);
            }
        }
        // 3. drop column record
        super.delete(ids);
        // 更新视图
        if (table != null) {
        	updateViews(table.getId());
		}
    }
    
    /* 
     * qiucs 2015-3-23 下午5:26:32
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#delete(com.ces.xarch.core.entity.BaseEntity)
     */
    @Transactional
    @Override
	public void delete(ColumnDefine entity) {
    	PhysicalTableDefine table = null;
        // 1-1. get entity by id
        if (!entity.getTableId().equals("-C")) {
            table = TableUtil.getTableEntity(entity.getTableId());
            delete(entity, table);
        }
        if (FormUtil.CoralInputType.COMBOGRID.equals(entity.getInputType()) || FormUtil.CoralInputOption.TEXTBOXBUTTON.equals(entity.getInputOption())) {
            String reserveZoneName = AppDefineUtil.RZ_NAME_PRE.concat(entity.getId());
            getDaoFromContext(ComponentReserveZoneDao.class).deleteReserveZoneByName(reserveZoneName);
        }
        // 3. drop column record
        super.delete(entity);
	}

    /* 
     * qiucs 2015-3-23 下午5:15:45
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#delete(java.lang.Iterable)
     */
    @Transactional
	@Override
	public void delete(Iterable<ColumnDefine> entities) {
        // 1. drop columns
        PhysicalTableDefine table = null;
        for (ColumnDefine entity : entities) {
            // 1-1. get entity by id
            if (!entity.getTableId().equals("-C")) {
	            if (null == table) {
	                table = TableUtil.getTableEntity(entity.getTableId());
	            }
	            delete(entity, table);
            }
        }
        // 3. drop column record
        super.delete(entities);
	}
	
	/**
	 * qiucs 2015-3-23 下午5:15:45
	 * <p>描述: 删除字段相关信息 </p>
	 * @return void
	 */
	@Transactional
	private void delete(ColumnDefine entity, PhysicalTableDefine table) {
		if (null != table) {
            // 1-2. check column is created
            if ("1".equals(entity.getCreated())) {
                // 1-2-1. get table name
                String tableName = table.getTableName();
                // 1-2-2. drop column in table
                DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();// (DatabaseHandlerDao)getDaoFromContext("databaseHandlerDao");
                if (dao.columnExists(tableName, entity.getColumnName()) && table.getTableType().equals("0")) {
                    dao.dropColumn(tableName, entity.getColumnName());
                }
            }
            deleteRelation(entity.getId());
            // 同步索引库结构
            //if (null != table) {
            	//IndexCommonUtil.syncSchema(table);
            //}
        }
	}

	/**
     * 根据字段名称和表Id获取字段
     * 
     * @param columnName 字段名称
     * @param tableId 表Id
     * @return ColumnDefine
     */
    public ColumnDefine findByColumnNameAndTableId(String columnName, String tableId) {
        return getDao().findByColumnNameAndTableId(columnName, tableId);
    }

    /**
     * <p>描述: 获取表中所有字段</p>
     * 
     * @param tableId
     * @return List<ColumnDefine> 返回类型
     * @throws
     */
    public List<ColumnDefine> findByTableId(String tableId) {
        return getDao().findByTableId(tableId);
    }

    /**
     * 根据字段id，查找字段信息
     * 
     * @param id
     * @return
     */
    public ColumnDefine findById(String id) {
        return getDao().findById(id);
    }

    /**
     * <p>描述: 获取表中业务字段</p>
     * 
     * @param tableId
     * @return List<ColumnDefine> 返回类型
     * @throws
     */
    public List<ColumnDefine> findByTableIdAndColumnType(String tableId) {
        return getDao().findByTableIdAndColumnType(tableId, "0");
    }

    /**
     * <p>描述: 获取表中业务字段</p>
     * 
     * @param tableId
     * @return List<ColumnDefine> 返回类型
     * @throws
     */
    public List<ColumnDefine> findByTableIdAndInputType(String tableId, String inputType) {
        return getDao().findByTableIdAndInputType(tableId, inputType);
    }
    
    /**
     * <p>描述: 获取表中业务字段</p>
     * 
     * @param tableId
     * @return List<ColumnDefine> 返回类型
     * @throws
     */
    public List<ColumnDefine> findByTableIdAndInputOption(String tableId, String inputOption) {
        return getDao().findByTableIdAndInputOption(tableId, inputOption);
    }

    /**
     * qiucs 2013-8-27
     * <p>描述: 表字段下拉框数据</p>
     * 
     * @param tableId
     * @param optionValue 下拉框隐藏值：1为字段名称(COLUMN_NAME)， 否则为ID
     * @return Object 返回类型
     * @throws
     */
    public Object getComboOfColumnsByTableId(String tableId, String optionValue) {
        List<OptionModel> opts = Lists.newArrayList();
        List<ColumnDefine> list = getDao().findByTableId(tableId);
        if (null == list || list.isEmpty()) {
            return opts;
        }
        for (ColumnDefine col : list) {
        	OptionModel option = new OptionModel();
            if ("1".equals(optionValue)) {
                option.setValue(col.getColumnName());
            } else {
                option.setValue(col.getId());
            }
            option.setText(col.getShowName());
            option.setProp(StringUtil.null2empty(col.getCodeTypeCode()));
            opts.add(option);
        }

        return opts;
    }

    /**
     * ganmh 2013-9-28
     * <p>描述: 处理待复制的所有列数据</p>
     * 
     * @param templateTable_id 模版表ID
     * @return List<ColumnDefine> 返回类型
     * @throws
     */
    public List<ColumnDefine> proceColumnDefine(String logicTable_code, String newTable_id) {
        List<ColumnDefine> query_cols = new ArrayList<ColumnDefine>();
        List<ColumnDefine> query_cols_clone = new ArrayList<ColumnDefine>();
        List<ColumnDefine> result = new ArrayList<ColumnDefine>();
        query_cols = getDao().findByTableId(logicTable_code);
        if (query_cols.size() > 0) {
            try {
                BeanUtils.copy(query_cols, query_cols_clone);
                for (ColumnDefine columnDefine : query_cols_clone) {
                    columnDefine.setId(null);
                    columnDefine.setTableId(newTable_id);
                    columnDefine.setCreated("0");
                    result.add(columnDefine);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result.size() > 0 ? result : null;
    }

    /**
     * ganmh 2013-9-28
     * <p>描述: 往新表里面插入逻辑表的数据,更新物理新表</p>
     * 
     * @param newTable_id 新表ID
     * @return void 返回类型
     * @throws
     */
    @Transactional
    public MessageModel proceTableCopy(String formTableCode, String toTableId) {
        StringBuffer message = new StringBuffer();
        List<ColumnDefine> cols = proceColumnDefine(formTableCode, toTableId);
        if (null == cols)
            return MessageModel.trueInstance("逻辑表中没有可复制的字段！");
        List<ColumnDefine> oldColumns = findByTableId(toTableId);
        for (ColumnDefine entity : cols) {
            boolean exists = false;
            for (ColumnDefine old : oldColumns) {
                if (old.getColumnName().equals(entity.getColumnName())) {
                    exists = true;
                    entity.setId(old.getId());
                    entity.setCreated(old.getCreated());
                    entity.setColumnType(old.getColumnType());
                    entity.setShowOrder(old.getShowOrder());
                    oldColumns.remove(old);
                    break;
                }
            }
            if (exists) {
                message.append(",").append(entity.getColumnName());
            }
            entity.setTableId(toTableId);
            entity.setShowOrder(null);
            save(entity);
        }
        if (message.length() > 0)
            message.deleteCharAt(0).insert(0, "复制成功！<br/>字段(").append(")").append("已存在！");
        return MessageModel.trueInstance(message.toString());
    }

    /**
     * qiucs 2013-9-29
     * <p>描述: 界面展现组装成过滤字段</p>
     * 
     * @param id
     * @return Object 返回类型
     * @throws
     */
    public Object getFilterColumnNameById(String id) {
        String columnName = getDao().findColumnNameById(id);
        return StringUtil.null2empty(columnName);
    }

    /**
     * qiucs 2014-12-1
     * <p>描述: 界面展现组装成过滤字段(字段标签)</p>
     * 
     * @param id
     * @return Object 返回类型
     * @throws
     */
    public Object getFilterColumnNameByLabel(String columnLabel, String tableId) {
        String columnName = getDao().getColumnNameByColumnLabelAndTableId(columnLabel, tableId);  
        return StringUtil.null2empty(columnName);
    }

    /**
     * qiucs 2013-10-23
     * <p>描述: 获取字段英文名称</p>
     * 
     * @param id
     * @return String 返回类型
     * @throws
     */
    public String getColumnNameById(String id) {
        return getDao().findColumnNameById(id);
    }

    /**
     * <p>描述: 字段复制时检查字段名是否唯一</p>
     * 
     * @param ids
     * @param tableId
     * @author Administrator
     * @date 2013-10-28 09:47:13
     */
    public void checkColumn(String ids, String tableId) {
        String[] idArr = ids.split(",");
        String repeatIds = "";
        for (String id : idArr) {
            String columnName = getDao().findColumnNameById(id);
            Long cnt = getDao().countByColumnNameAndTableId(columnName, tableId);
            if (null != cnt && cnt.longValue() > 0) {
                if (repeatIds != "") {
                    repeatIds += ",";
                }
                repeatIds += id;
            }
        }
        if (StringUtil.isNotEmpty(repeatIds)) {
            throw new RuntimeException(repeatIds);
        }
    }

    /**
     * <p>描述: 字段复制</p>
     * 
     * @param ids
     * @param tableId
     * @author Administrator
     * @date 2013-10-28 09:47:19
     */
    @Transactional
    public void columnCopy(String ids, String tableId) {
        String[] idArr = ids.split(",");
        PhysicalTableDefine table = null;
        LogicTableDefine logicTable = null;
        for (String id : idArr) {
            ColumnDefine columnDefine = getDao().findOne(id);
            if (null == table && null == logicTable) {
                table = getService(PhysicalTableDefineService.class).getByID(columnDefine.getTableId());
                logicTable = getService(LogicTableDefineService.class).getByCode(columnDefine.getTableId());
            }
            if (null != table) {//字段复制入物理表
            	ColumnDefine newColumnDefine = new ColumnDefine();
                BeanUtils.copy(columnDefine, newColumnDefine);
                newColumnDefine.setId("");
                newColumnDefine.setCreated("0");
                newColumnDefine.setRemark("字段复制来源于【" + table.getShowName() + "】");
                newColumnDefine.setTableId(tableId);
                newColumnDefine.setShowOrder(null);
                save(newColumnDefine);
			} else if (null != logicTable) {//字段复制入逻辑表
				ColumnDefine newColumnDefine = new ColumnDefine();
                BeanUtils.copy(columnDefine, newColumnDefine);
                newColumnDefine.setId("");
                newColumnDefine.setCreated("0");
                newColumnDefine.setRemark("字段复制来源于【" + logicTable.getShowName() + "】");
                newColumnDefine.setTableId(tableId);
                newColumnDefine.setShowOrder(null);
                save(newColumnDefine);
			}
        }
    }

    /**
     * qiucs 2013-10-30
     * <p>描述: 字段的数据类型</p>
     * 
     * @param id
     * @return String 返回类型
     * @throws
     */
    public String getColumnDataType(String id) {
        return getDao().findDataTypeById(id);
    }

    /**
     * 取用户型、编码型、部门型对应的下拉框
     * 
     * @param id
     * @param dataType
     * @return
     * @author Administrator
     * @date 2013-11-19 09:35:48
     */
    public Object getValComboOfColumnsById(String id) {
        List<OptionModel> opts = Lists.newArrayList();
        ColumnDefine cd = getDao().findOne(id);
        if (null == cd || StringUtil.isEmpty(cd.getCodeTypeCode())) return opts;
        
		List<Code> clist = CodeUtil.getInstance().getCodeList(cd.getCodeTypeCode());
		
        if (null == clist || clist.isEmpty()) return opts;
        
        for (Code code : clist) {
            OptionModel option = new OptionModel();
            option.setValue(code.getValue());
            option.setText(code.getName());
            opts.add(option);
        }
        
        return opts;
    }

    /**
     * qiucs 2013-12-2
     * <p>描述: 删除或更新被删除字段的相关信息</p>
     * 
     * @param id 字段ID
     */
    @Transactional
    protected void deleteRelation(String id) {
        // 清理表关系
        getService(TableRelationService.class).deleteByColumnId(id);
        // 清理逻辑表关系
        getService(LogicTableRelationService.class).deleteByColumnId(id);
        // 更新字段关联配置
        getService(ColumnRelationService.class).updateByColumnId(id);
        // 应用定义
        getService(AppDefineService.class).deleteByColumnId(id);
        // excel导出字段定义
        getService(AppExportService.class).deleteByColumnId(id);
        // 报表定义
        getService(ReportService.class).deleteByColumnId(id);
        // 工作流定义
        //getService(WorkflowDefineService.class).deleteByColumnId(id);
        // 数据权限
        getDaoFromContext(AuthorityDataDetailDao.class).deleteByColumnId(id);
        getDaoFromContext(AuthorityDataDetailCopyDao.class).deleteByColumnId(id);
    }

    /**
     * wangmi 2013-12-10
     * <p>描述: 根据tableId 获取最大显示排序</p>
     * 
     * @param tableId
     * @return String 返回类型
     * @throws public Object getMaxShowOrderService(String tableId){
     *             Integer maxShowOrder = getDao().getMaxShowOrder(tableId);
     *             return null == maxShowOrder ? "" : maxShowOrder;
     *             }
     */

    /**
     * wangmi 2013-12-10
     * <p>描述: 根据tableId 获取字段显示顺序范围内</p>
     * 
     * @param start end tableId
     * @return List<ColumnDefine> 返回类型
     * @throws
     */
    public List<ColumnDefine> getColumnListByShowOrder(Integer start, Integer end, String tableId) {
        return getDao().getByShowOrderBetweenAndTableId(start, end, tableId);
    }
    
    /**
     * qiucs 2014-9-24 
     * <p>描述: 绑定工作流时，为表创建流程实例ID和创建流程的用户ID</p>
     */
    public void createCoflowRelateColumns(String tableId) {
        Integer showOrder = getDao().getMaxShowOrder(tableId);
        ColumnDefine column = getDao().findByColumnNameAndTableId(WorkflowUtil.C_PROCESS_INSTANCE_ID, tableId);
        if (null == column) {
            column = new ColumnDefine();
            column.setShowName("流程实例ID");
            column.setShowOrder(++showOrder);
            column.setColumnName(WorkflowUtil.C_PROCESS_INSTANCE_ID);
            column.setColumnType("1");// 0-业务字段 1-系统字段
            column.setDataType(ConstantVar.DataType.NUMBER); // 字段数据类型
            column.setLength(18); // 字段长度
            column.setTableId(tableId);
            column.setDefaultValue("0");
            column.setCreated("0");
            column.setRemark("绑定工作流时，系统自动创建的字段");
            column = save(column);
        }
        column     = getDao().findByColumnNameAndTableId(WorkflowUtil.C_REGISTER_USER_ID, tableId);
        if (null == column) {
            column = new ColumnDefine();
            column.setShowName("创建流程的用户ID");
            column.setShowOrder(++showOrder);
            column.setColumnName(WorkflowUtil.C_REGISTER_USER_ID);
            column.setColumnType("1");// 0-业务字段 1-系统字段
            column.setDataType(ConstantVar.DataType.CHAR); // 字段数据类型
            column.setLength(32); // 字段长度
            column.setTableId(tableId);
            column.setCreated("0");
            column.setRemark("绑定工作流时，系统自动创建的字段");
            column = save(column);
        }
        column     = getDao().findByColumnNameAndTableId(WorkflowUtil.C_BELONG_WORKFLOW_CODE, tableId);
        if (null == column) {
            column = new ColumnDefine();
            column.setShowName("所属流程编码");
            column.setShowOrder(++showOrder);
            column.setColumnName(WorkflowUtil.C_BELONG_WORKFLOW_CODE);
            column.setColumnType("1");// 0-业务字段 1-系统字段
            column.setDataType(ConstantVar.DataType.CHAR); // 字段数据类型
            column.setLength(100); // 字段长度
            column.setTableId(tableId);
            column.setCreated("0");
            column.setRemark("绑定工作流时，系统自动创建的字段");
            column = save(column);
        }
    }

    /**
     * qiucs 2014-1-3
     * <p>描述: 编码类型删除时，级联更新字段code_type_id</p>
     * 
     * @param codeTypeCode 设定参数
     */
    @Transactional
    public void updateByCodeTypeCode(String codeTypeCode) {
        getDao().updateByCodeTypeCode(codeTypeCode);
    }

    /**
     * qiucs 2014-6-24
     * <p>描述: 字段标签MAP<COLUMN_LABEL, COLUMN_NAME></p>
     * 
     * @param @param tableId
     * @return Map<String,String> 返回类型
     * @throws
     */
    public Map<String, String> getColumnLabelMap(String tableId) {
        Map<String, String> cols = new HashMap<String, String>();
        List<ColumnDefine> list = findByTableId(tableId);
        for (ColumnDefine entity : list) {
            if (StringUtil.isEmpty(entity.getColumnLabel()))
                continue;
            cols.put(entity.getColumnLabel().toLowerCase(), entity.getColumnName());
        }
        return cols;
    }

    /**
     * qiucs 2014-6-25
     * <p>描述: 建表时，自动创建公共字段表中的字段</p>
     * 
     * @param tableId 设定参数
     */
    @Transactional
    public void addCommonColumns(String tableId) {
        List<ColumnDefine> cols = findByTableId(ColumnDefine.COMMON_TABLE_ID);
        Integer showOrder = 1;
        for (int i = 0; i < cols.size(); i++) {
            ColumnDefine entity = new ColumnDefine();
            BeanUtils.copy(cols.get(i), entity);

            entity.setId("");
            entity.setTableId(tableId);
            entity.setShowOrder(++showOrder);
            entity.setRemark("来自公共字段表");

            save(entity);
        }
    }

    /**
     * 修改字段标签的值
     * 
     * @param oldColumnLabel 旧的字段标签Code
     * @param newColumnLabel 新的字段标签Code
     */
    @Transactional
    public void batchUpdateColumnLabel(String oldColumnLabel, String newColumnLabel) {
        getDao().batchUpdateColumnLabel(oldColumnLabel, newColumnLabel);
    }

    /**
     * 将业务表字段复制到逻辑表
     * 
     * @param logicId 逻辑表ID
     * @param columnIds 复制的字段IDs
     */
    @Transactional
    public void copyToLogic(String logicCode, String columnIds) {
        List<ColumnDefine> columnDefineList = findByTableId(logicCode);
        Set<String> columnNameSet = new HashSet<String>();
        if (CollectionUtils.isNotEmpty(columnDefineList)) {
            for (ColumnDefine columnDefine : columnDefineList) {
                columnNameSet.add(columnDefine.getColumnName());
            }
        }
        String[] columnIdArray = columnIds.split(",");
        for (String columnId : columnIdArray) {
            ColumnDefine columnDefine = getDao().findById(columnId);
            if (columnNameSet.contains(columnDefine.getColumnName())) {
                continue;
            }
            ColumnDefine dest = new ColumnDefine();
            BeanUtils.copy(columnDefine, dest);
            dest.setId(null);
            dest.setTableId(logicCode);
            dest.setCreated("0");
            getDao().save(dest);
        }
    }
    
    /**
     * qiucs 2014-9-5 
     * <p>描述: 获取修改字段</p>
     * @param  tableId
     * @return List<ColumnDefine>    返回类型   
     * @throws
     */
    public List<ColumnDefine> getUpdateColumns(String tableId) {
        return getDao().findByUpdateColumns(tableId);
    }
    
    /**
     * qiucs 2014-10-15 
     * <p>描述: 根据表ID获取字段标签</p>
     * @return List<Object>    返回类型   
     */
    public List<Object> getColumnLabelsByTableId(String tableId) {
    	List<Object> list = getDao().getColumnLabelsByTableId(tableId);
    	Object obj = null;
    	for (int i = 0, len = list.size(); i < len; i++) {
    		obj = list.get(i);
    		if (null == obj) continue;
    		list.set(i, String.valueOf(obj).toLowerCase());
    	}
        return list;
    }
    
    /**
     * qiucs 2014-11-26 
     * <p>描述: 根据字段标签与表ID获取字段名称</p>
     */
    public String getColumnNameByColumnLabelAndTableId(String columnLabel, String tableId) {
        return getDao().getColumnNameByColumnLabelAndTableId(columnLabel, tableId);
    }
    
    /**
     * qiujinwei 2014-12-15 
     * <p>描述: 根据字段名称与表ID获取字段ID</p>
     */
    public String getColumnIdByTableIdAndColumnName (String tableId, String columnName){
    	return  getDao().getColumnIdByTableIdAndColumnName(tableId, columnName);
    }
    
    /**
     * qiucs 2014-12-16 
     * <p>描述: 根据字段标签与表ID获取字段名称</p>
     */
    @Transactional
    public void copyFromLogicTable(String logicTableCode, PhysicalTableDefine toTable) {
    	List<ColumnDefine> logicTableColumnList = findByTableId(logicTableCode);
    	List<ColumnDefine> toTableColumnList    = findByTableId(toTable.getId());
    	Set<String> columnSet = new HashSet<String>();
    	for (ColumnDefine entity : toTableColumnList) columnSet.add(entity.getColumnName());
    	// 
    	for (ColumnDefine entity : logicTableColumnList) {
    		// 过滤重复
    		if (columnSet.contains(entity.getColumnName())) continue;
    		// 生成字段
    		ColumnDefine distEntity = new ColumnDefine();
    		BeanUtils.copy(entity, distEntity);
    		distEntity.setId(null);
    		distEntity.setTableId(toTable.getId());
    		distEntity.setRemark("由工作流定义生成");
    		// 保存字段
    		save(distEntity, toTable);
    	}
    }
    
    /**
     * qiucs 2015-1-22 下午6:16:37
     * <p>描述: 根据表ID删除字段 </p>
     * @return void
     */
    @Transactional
    public void deleteByTableId(String tableId) {
    	getDao().deleteByTableId(tableId);
    }
    
    /**
     * 批量修改逻辑表编码
     * @param oldLogicTableCode 旧的逻辑表编码
     * @param newLogicTableCode 新的逻辑表编码
     */
    @Transactional
    public void batchUpdateTableCode(String oldLogicTableCode, String newLogicTableCode){
    	getDao().batchUpdateTableCode(oldLogicTableCode, newLogicTableCode);
    }
    
    /**
     * qiucs 2015-4-30 下午5:14:32
     * <p>描述: 查找两张表中相同字段标签的字段 </p>
     * @return Map<String,String> 
     *         key: tableId对应的表的字段
     *         val: masterTableId对应的表的字段
     */
    public Map<String, String> getInheritColumnMap(String tableId, String masterTableId) {
    	Map<String, String> colMap = new HashMap<String, String>();
    	List<Object[]> list = getDao().getInheritColumnList(tableId, masterTableId);
    	Object[] obj = null;
    	for (int i = 0, len = list.size(); i < len; i++) {
    		obj = list.get(i);
    		colMap.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
    	}
    	return colMap;
    }
    
    /**
     * 更新视图字段
     * @param id 物理表ID
     */
    @Transactional
    public void updateViews(String id) {
    	List<TableRelation> relations = getService(TableRelationService.class).findByTableId(id);
        List<TableRelation> mRelations = getService(TableRelationService.class).findByRelateTableId(id);
        if (!relations.isEmpty()) {
			for (TableRelation tableRelation : relations) {
				String tableId = tableRelation.getTableId();
				String mTableId = tableRelation.getRelateTableId();
				if (!getService(PhysicalTableDefineService.class).getByLogicTableCode(tableId + "'" + mTableId).isEmpty()) {
					String rowsValue = "";
					Map<String, List<String>> map = TableUtil.getTableRelation(tableId, mTableId);
					if (!map.isEmpty()) {
						for (int i = 0; i < map.get(tableId).size(); i++) {
							rowsValue += ";" + map.get(tableId).get(i) + "'" + map.get(mTableId).get(i);
						}
						getService(TableRelationService.class).createViewByRelation(rowsValue.substring(1), tableId, mTableId, Boolean.TRUE);
					}
				} 
				if (!getService(PhysicalTableDefineService.class).getByLogicTableCode(mTableId + "'" + tableId).isEmpty()) {
					String rowsValue = "";
					Map<String, List<String>> map = TableUtil.getTableRelation(tableId, mTableId);
					if (!map.isEmpty()) {
						for (int i = 0; i < map.get(tableId).size(); i++) {
							rowsValue += ";" + map.get(tableId).get(i) + "'" + map.get(mTableId).get(i);
						}
						getService(TableRelationService.class).createViewByRelation(rowsValue.substring(1), tableId, mTableId, Boolean.TRUE);
					}
				} 
			}
		}
        if (!mRelations.isEmpty()) {
			for (TableRelation tableRelation : mRelations) {
				String tableId = tableRelation.getTableId();
				String mTableId = tableRelation.getRelateTableId();
				if (!getService(PhysicalTableDefineService.class).getByLogicTableCode(tableId + "'" + mTableId).isEmpty()) {
					String rowsValue = "";
					Map<String, List<String>> map = TableUtil.getTableRelation(tableId, mTableId);
					if (!map.isEmpty()) {
						for (int i = 0; i < map.get(tableId).size(); i++) {
							rowsValue += ";" + map.get(tableId).get(i) + "'" + map.get(mTableId).get(i);
						}
						getService(TableRelationService.class).createViewByRelation(rowsValue.substring(1), tableId, mTableId, Boolean.TRUE);
					}
				} 
				if (!getService(PhysicalTableDefineService.class).getByLogicTableCode(mTableId + "'" + tableId).isEmpty()) {
					String rowsValue = "";
					Map<String, List<String>> map = TableUtil.getTableRelation(tableId, mTableId);
					if (!map.isEmpty()) {
						for (int i = 0; i < map.get(tableId).size(); i++) {
							rowsValue += ";" + map.get(tableId).get(i) + "'" + map.get(mTableId).get(i);
						}
						getService(TableRelationService.class).createViewByRelation(rowsValue.substring(1), tableId, mTableId, Boolean.TRUE);
					}
				} 
			}
		}
    }
    
    /**
     * qiujinwei 2015-06-23
     * <p>描述: 判断字段是否有数据</p>
     */
    public MessageModel dataExist(String columnName, String tableId) {
    	boolean flag = false;
    	if (TableUtil.getTableEntity(tableId) == null) return new MessageModel(Boolean.FALSE, "没值");
    	String sql = "select count(" + columnName + ") from " + TableUtil.getTableName(tableId);
    	List list = DatabaseHandlerDao.getInstance().queryForList(sql);
    	if (list != null) {
    		if (Integer.parseInt(list.get(0).toString()) > 0) flag = true;
		}
    	if (flag) {
    		return new MessageModel(Boolean.TRUE, "有值");
		}
    	return new MessageModel(Boolean.FALSE, "没值");
    }
    
    /**
     * qiujinwei 2015-08-13
     * <p>描述: 更新字段操作项同步应用定义</p>
     */
    @Transactional
    public void deleteRelation (String id, String columnName, String value) {
    	if (value.equals("0")) {
	    	if (columnName.equals("searchable")) {
	    		getDao(AppFilterDao.class, AppFilter.class).deleteByColumnId(id);
	    		getDao(AppSearchDao.class, AppSearch.class).deleteByColumnId(id);
	    		AppUtil.getInstance().deleteColumn(getByID(id), AppUtil.APP_SEARCH);
			} else if (columnName.equals("inputable")) {
				getDao(AppFormElementDao.class, AppFormElement.class).deleteByColumnId(id);
				AppUtil.getInstance().deleteColumn(getByID(id), AppUtil.APP_FORM_ELEMENT);
			} else if (columnName.equals("listable")) {
				getDao(AppColumnDao.class, AppColumn.class).deleteByColumnId(id);
				AppUtil.getInstance().deleteColumn(getByID(id), AppUtil.APP_COLUMN);
			} else if (columnName.equals("sortable")) {
				getDao(AppSortDao.class, AppSort.class).deleteByColumnId(id);
				AppUtil.getInstance().deleteColumn(getByID(id), AppUtil.APP_SORT);
			}
    	}
    }
}

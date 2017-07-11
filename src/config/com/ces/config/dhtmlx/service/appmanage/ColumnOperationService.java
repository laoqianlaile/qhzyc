package com.ces.config.dhtmlx.service.appmanage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.ColumnOperationDao;
import com.ces.config.dhtmlx.dao.appmanage.ColumnRelationDao;
import com.ces.config.dhtmlx.dao.appmanage.PhysicalTableDefineDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.appmanage.ColumnOperation;
import com.ces.config.dhtmlx.entity.appmanage.ColumnRelation;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;

/**
 * 字段继承、求和、最值计算
 * 
 * @author wang
 */

@Component
public class ColumnOperationService extends ConfigDefineDaoService<ColumnOperation, ColumnOperationDao> {

	/*
	 * (非 Javadoc) <p>标题: bindingDao</p> <p>描述: 注入自定义持久层(Dao)</p>
	 * 
	 * @param entityClass
	 * 
	 * @see
	 * com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
	 */
	@Autowired
	@Qualifier("columnOperationDao")
	@Override
	protected void setDaoUnBinding(ColumnOperationDao dao) {
		super.setDaoUnBinding(dao);
	}

	@Override
	@Transactional
	public ColumnOperation save(ColumnOperation entity) {
		// 1. save column relation entity
		String rtype = entity.getType();
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

		if (rtype.equals("0")) {
			relation.setType("2");
		} else if (rtype.equals("1")) {
			relation.setType("3");
		} else if (rtype.equals("2")) {
			relation.setType("4");
		}
		relation.setTableId(entity.getTableId());
		relation = getDao(ColumnRelationDao.class, ColumnRelation.class).save(
				relation);
		// 2. save column business entity
		entity.setColumnRelationId(relation.getId());
		entity = getDao().save(entity);
		return (entity);
	}

	/**
	 * 生成继承、求和、最值触发器
	 * @param entity
	 */
	public void generateOperationTrigger(ColumnOperation entity) {
		// 1.获取当前表名
		PhysicalTableDefine tableDefine = getDao(PhysicalTableDefineDao.class,PhysicalTableDefine.class).findOne(entity.getTableId());
		String tableName = tableDefine.getTableName();
		//源表
		String OriginTableName = getDao(PhysicalTableDefineDao.class,PhysicalTableDefine.class).findOne(entity.getOriginTableId()).getTableName();
			//getDao().getOriginTableByName(entity.getOriginTableId());
		//源表字段
		String OriginColumnName = getDao().getOperationById(entity.getOriginColumnId());
		//目标表字段
		String ColumnName = getDao().getOperationById(entity.getColumnId());
		
		//源表表  、目标表触发器的upate条件
		String originCondition = "";
		String goalCondition = "";
		//求和、最值功能下源表触发器条件
		String originOperCondition = "";
		String goalOperCondition = "";
		
		//求和、最值功能下目标表触发器条件
		String mboriginOperCond = "";
		String mbgoalOperCond = "";
		
		List<Object[]> relation = getDao().getOriginTableRelation(entity.getOriginTableId());		
		for(int i=0;i<relation.size();i++){
			Object[] objects = (Object[])relation.get(i);
			String[] str = new String[2];
				str[0] = String.valueOf(objects[3]);
				str[1] = String.valueOf(objects[6]);
				originCondition += "go_tn."+str[0]+"=:n_v."+str[1]+" and ";
				goalCondition += "go_tn."+str[1]+"=:n_v."+str[0]+" and ";
				//otn.file_no=:new.bgqx and otn.F_ID=:new.id ; 
				originOperCondition += "otn."+str[1]+"=:new."+str[1]+" and ";
				goalOperCondition += str[0]+"=:new."+str[1]+" and ";
				
				mboriginOperCond += "otn."+str[1]+"=:new."+str[0]+" and ";
				mbgoalOperCond += str[0]+"=:new."+str[0]+" and ";
				
				
		}
		//继承功能下触发器的条件
		String orginCond = originCondition.substring(0,originCondition.lastIndexOf("and"));
		String goalCcond = goalCondition.substring(0,goalCondition.lastIndexOf("and"));
		//求和、最值功能下源表触发器条件
		String originOperCond = originOperCondition.substring(0,originOperCondition.lastIndexOf("and"));
		String goalOperCond = goalOperCondition.substring(0,goalOperCondition.lastIndexOf("and"));
		
		//求和、最值功能下源表触发器条件
		String mbOrigOperCond = mboriginOperCond.substring(0,mboriginOperCond.lastIndexOf("and"));
		String mbGoOperCond = mbgoalOperCond.substring(0,mbgoalOperCond.lastIndexOf("and"));
		
		//generate 继承触发器
		if(entity.getType().equals("0")){
			//originTable trigger
			String triggerName = "TRIGGER_JC_" + entity.getId().substring(15);
			DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
			String sql ="create or replace trigger " + triggerName+"_O" 
					  + " \n before update  OF "+OriginColumnName+"	on  " +OriginTableName
					  + " \n REFERENCING OLD AS O_V NEW AS N_V "  
					  + " \n for each row "
					  + " \n begin " 
					  + " \n  UPDATE "+tableName+" go_tn set go_tn."+ColumnName+" = :n_v."+OriginColumnName
					  +	" where "+orginCond+";"
					  + "\n end;" ;	
			dao.executeSql("BEGIN EXECUTE IMMEDIATE('" + sql + "'); END;");
			
			//goalTable trigger
			String sql_t ="create or replace trigger " + triggerName+"_T" 
			  + " \n before update  OF "+ColumnName+"	on  " +tableName
			  + " \n REFERENCING OLD AS O_V NEW AS N_V "  
			  + " \n for each row "
			  + " \n begin " 
			  + " \n  UPDATE "+OriginTableName+" go_tn set go_tn."+OriginColumnName+" = :n_v."+ColumnName
			  +	" where "+goalCcond+";"
			  + "\n end;" ;	

			dao.executeSql("BEGIN EXECUTE IMMEDIATE('" + sql_t + "'); END;");
			System.out.println("sql=====trigger========:" + sql_t);
			
		}else if(entity.getType().equals("1")){
			//generate  求和触发器
			String triggerName = "TRIGGER_QH_" + entity.getId().substring(15);
			DatabaseHandlerDao dao = (DatabaseHandlerDao) getDaoFromContext("databaseHandlerDao");
			String q_sql ="create or replace trigger "+triggerName+"_O"
					+" \n after update or insert OF "+OriginColumnName+" on " +OriginTableName
					+" \n for each row " 
					+" \n DECLARE" 
					+" \n PRAGMA AUTONOMOUS_TRANSACTION;" 
					+" \n totalNum number(10); " 
					+" \n begin " 
					+" \n if inserting or UPDATING then " 
					+" \n    select sum("+OriginColumnName+") into totalNum from "+OriginTableName +" otn" 
					+" \n    where  "+originOperCond+";" 
					+" \n    totalNum := totalNum + (:new."+OriginColumnName+" - :old."+OriginColumnName+");" 
					+" \n    UPDATE "+tableName+" set "+ColumnName+" = totalNum" 
					+" \n    where "+goalOperCond+";" 
					+" \n    commit;" 
					+" \n end if;" 
					+" \n end;" ;	
			dao.executeSql("BEGIN EXECUTE IMMEDIATE('" + q_sql + "'); END;");
			System.out.println("========sql===========:"+q_sql);
			
			//goalTable trigger
			String q1_sql ="create or replace trigger "+triggerName+"_T"
					+" \n after update or insert OF "+ColumnName+" on " +tableName
					+" \n for each row " 
					+" \n DECLARE" 
					+" \n PRAGMA AUTONOMOUS_TRANSACTION;" 
					+" \n totalNum number(10); " 
					+" \n begin " 
					+" \n if inserting or UPDATING then " 
					+" \n    select sum("+OriginColumnName+") into totalNum from "+OriginTableName +" otn" 
					+" \n    where  "+mbOrigOperCond+";" 
					+" \n    totalNum := totalNum + (:new."+ColumnName+" - :old."+ColumnName+");" 
					+" \n    UPDATE "+tableName+" set "+ColumnName+" = totalNum" 
					+" \n    where "+mbGoOperCond+";" 
					+" \n    commit;" 
					+" \n end if;" 
					+" \n end;" ;	
			dao.executeSql("BEGIN EXECUTE IMMEDIATE('" + q1_sql + "'); END;");
			System.out.println("========q1_sql===========:"+q1_sql);
			
			/**
			  if inserting or UPDATING then 
		      	UPDATE T_PS_ARCHIVE_TEST01 set archive_NO = (select sum(file_no) from t_ps_test_orgient
		      	where T_PS_ARCHIVE_TEST01.ID = t_ps_test_orgient.f_Id);
		    	end if;
			 */
		}else if(entity.getType().equals("2")){
			//generate  最值触发器
			
			if(entity.getOperator().equals("0")){
				//generate 最小值触发器
				String triggerName = "TRIGGER_ZX_" + entity.getId().substring(15);
				DatabaseHandlerDao dao = (DatabaseHandlerDao) getDaoFromContext("databaseHandlerDao");
				String q_sql ="create or replace trigger "+triggerName+"_S"
						+" \n after update or insert OF "+OriginColumnName+" on " +OriginTableName
						+" \n for each row " 
						+" \n DECLARE" 
						+" \n PRAGMA AUTONOMOUS_TRANSACTION;" 
						+" \n totalNum number(10); " 
						+" \n begin " 
						+" \n if inserting or UPDATING then " 
						+" \n    select MIN("+OriginColumnName+") into totalNum from "+OriginTableName +" otn" 
						+" \n    where  "+originOperCond+";" 
						//+" \n    totalNum := totalNum + (:new."+OriginColumnName+" - :old."+OriginColumnName+");" 
						+" \n    UPDATE "+tableName+" set "+ColumnName+" = totalNum" 
						+" \n    where "+goalOperCond+";" 
						+" \n    commit;" 
						+" \n end if;" 
						+" end;" ;	
				dao.executeSql("BEGIN EXECUTE IMMEDIATE('" + q_sql + "'); END;");
				System.out.println("========sql===========:"+q_sql);
				
				
			}else if(entity.getOperator().equals("1")){
				//generate 最大值触发器
				String triggerName = "TRIGGER_ZD_" + entity.getId().substring(15);
				DatabaseHandlerDao dao = (DatabaseHandlerDao) getDaoFromContext("databaseHandlerDao");
				String q_sql ="create or replace trigger "+triggerName+"_B"
						+" \n after update or insert OF "+OriginColumnName+" on " +OriginTableName
						+" \n for each row " 
						+" \n DECLARE" 
						+" \n PRAGMA AUTONOMOUS_TRANSACTION;" 
						+" \n totalNum number(10); " 
						+" \n begin " 
						+" \n if inserting or UPDATING then " 
						+" \n    select MAX("+OriginColumnName+") into totalNum from "+OriginTableName +" otn" 
						+" \n    where  "+originOperCond+";" 
						//+" \n    totalNum := totalNum + (:new."+OriginColumnName+" - :old."+OriginColumnName+");" 
						+" \n    UPDATE "+tableName+" set "+ColumnName+" = totalNum" 
						+" \n    where "+goalOperCond+";" 
						+" \n    commit;" 
						+" \n end if;" 
						+" end;" ;	
				dao.executeSql("BEGIN EXECUTE IMMEDIATE('" + q_sql + "'); END;");
				System.out.println("========sql===========:"+q_sql);
			}
		}
		

	}
	/**
	 * 查询源表和关联表记录数目
	 * @param tableId, rId
	 */
	@Transactional
	public long getTotalTableRelation(String tableId,String rId){
		return getDao().getTotalTableRelation(tableId,tableId);
	}
	
	/**
     * 根据tableId获取继承、求和、最值
     * @param tableId 表ID
     * @return List<ColumnOperation> 返回类型
     */
    public List<ColumnOperation> findByTableId(String tableId) {
        return getDao().findByTableId(tableId);
    }
    
	/**
     * 根据表ID获取该表下所有继承、求和、最值 （该方法供构件固化导出使用）
     * 
     * @param tableId 表ID
     * @return List<Object[]>
     */
	public List<Object[]> findForExport(String tableId) {
	    return getDao().findForExport(tableId);
	}
	
	/**
	 * qiucs 2013-12-2 
	 * <p>描述: 根据字段ID更新配置</p>
	 * @param  columnId    字段ID   
	 */
	@Transactional
	public void updateByColumnId(String columnId) {
	    getDao().updateByColumnId(columnId);
	    getDao().updateByOriginColumnId(columnId);
	}
    
    /**
     * qiucs 2014-9-17 
     * <p>描述: 获取指定类型的字段关联信息</p>
     * @param  tableId
     * @param  type
     * @return List<ColumnOperation>    返回类型   
     * @throws
     */
    public List<ColumnOperation> findByTableId(String tableId, String type) {
        return getDao().findByTableId(tableId, type);
    }
    
    /**
     * qiucs 2014-9-19 
     * <p>描述: 继承(tableId作为父表)</p>
     * @param  tableId
     * @return List<ColumnOperation>    返回类型   
     * @throws
     */
    public List<ColumnOperation> findByOriginTableId(String tableId) {
        return getDao().findByOriginTableId(tableId);
    }
}

package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.appmanage.LogicTableRelationDao;
import com.ces.config.dhtmlx.entity.appmanage.ColumnDefine;
import com.ces.config.dhtmlx.entity.appmanage.LogicTableRelation;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.xarch.core.exception.FatalException;
import com.google.common.collect.Lists;

/**
 * 逻辑表在逻辑表组中的表关系表处理层
 * 
 * @author qiujinwei
 */
@Component
public class LogicTableRelationService extends ConfigDefineDaoService<LogicTableRelation, LogicTableRelationDao> {
	
	/*
     * (非 Javadoc)
     * <p>标题: bindingDao</p>
     * <p>描述: 注入自定义持久层(Dao)</p>
     * @param entityClass
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("logicTableRelationDao")
    @Override
    protected void setDaoUnBinding(LogicTableRelationDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    /**
     * qiujinwei 2014-12-22
     * <p>标题: getRelationByCode</p>
     * <p>描述: 根据逻辑表组编码和源逻辑表编码获取表关系</p>
     * @return List<Object[]>    返回类型   
     * @throws
     */
    public Object getRelationByCode(String tableCode, String groupCode){
    	List<Object[]> relation = getDao().getRelationByCode(tableCode, groupCode);
    	List<String[]> list = new ArrayList<String[]>();
    	for (int i = 0; i < relation.size(); i++) {
			Object[] objects = relation.get(i);
			String[] str = new String[7];
			str[0] = String.valueOf(objects[0]);
			str[1] = String.valueOf(objects[1]) + "(" + String.valueOf(objects[2]) + ")";
			str[2] = String.valueOf(objects[3]) + "(" + String.valueOf(objects[4]) + ")";
			str[3] = String.valueOf(objects[5]) + "(" + String.valueOf(objects[6]) + ")";
			str[4] = String.valueOf(objects[7]) + "(" + String.valueOf(objects[8]) + ")";
			str[5] = String.valueOf(objects[6]);
			list.add(str);
		}
    	return list;
    }
    
    /**
     * 源逻辑表字段检索
     * 
     * @return
     * @throws FatalException
     */
    public Object getLogicTableRelationColumn(String tableCode, String groupCode){
    	List<ColumnDefine> relationColumns = getService(ColumnDefineService.class).findByTableId(tableCode);
    	List<ColumnDefine> sysCols = getService(ColumnDefineService.class).findByTableId("-C");
    	List<String[]> list = new ArrayList<String[]>();
    	List<Object[]> relations = getDao().getRelationByCode(tableCode, groupCode);
    	Set<String> set = new HashSet<String>();
    	for (int i = 0; i < relations.size(); i++) {
			set.add(String.valueOf(relations.get(i)[4]));
		}
    	for (int i = 0; i < sysCols.size(); i++) {
    		ColumnDefine column = sysCols.get(i);
			if (set.size() > 0 && set.contains(column.getColumnName())) {continue;}
			String[] str = new String[2];
			str[0] = column.getId();
			str[1] = "系统字段-" + column.getShowName() + "(" + column.getColumnName() + ")";
			list.add(str);
		}
    	for (int i = 0; i < relationColumns.size(); i++) {
			ColumnDefine column = relationColumns.get(i);
			if (set.size() > 0 && set.contains(column.getColumnName())) {continue;}
			String[] str = new String[2];
			str[0] = column.getId();
			str[1] = column.getShowName() + "(" + column.getColumnName() + ")";
			list.add(str);
		}
    	return list;
    }
    
    /**
     * 目标逻辑表字段检索
     * 
     * @return
     * @throws FatalException
     */
    public Object getLogicTableRelationColumn(String tableCode, String groupCode, String relationTableCode){
    	List<ColumnDefine> relationColumns = getService(ColumnDefineService.class).findByTableId(relationTableCode);
    	List<ColumnDefine> sysCols = getService(ColumnDefineService.class).findByTableId("-C");
    	List<String[]> list = new ArrayList<String[]>();
    	List<Object[]> relations = getDao().getRelationByCode(tableCode, groupCode);
    	Set<String> set = new HashSet<String>();
    	for (int i = 0; i < relations.size(); i++) {
			set.add(String.valueOf(relations.get(i)[8]));
		}
    	for (int i = 0; i < sysCols.size(); i++) {
    		ColumnDefine column = sysCols.get(i);
			if (set.size() > 0 && set.contains(column.getColumnName())) {continue;}
			String[] str = new String[2];
			str[0] = column.getId();
			str[1] = "系统字段-" + column.getShowName() + "(" + column.getColumnName() + ")";
			list.add(str);
		}
    	for (int i = 0; i < relationColumns.size(); i++) {
			ColumnDefine column = relationColumns.get(i);
			if (set.size() > 0 && set.contains(column.getColumnName())) {continue;}
			String[] str = new String[2];
			str[0] = column.getId();
			str[1] = column.getShowName() + "(" + column.getColumnName() + ")";
			list.add(str);
		}
    	return list;
    }
    
    /**
     * 逻辑表关系检索
     * 
     * @return
     * @throws FatalException
     */
    public Object getShowRelationColum(String tableCode, String groupCode){
    	List<Object[]> relation = getDao().getRelationByRelationCode(tableCode, groupCode);
    	List<String[]> list = new ArrayList<String[]>();
    	for(int i=0;i<relation.size();i++){
			Object[] objects = (Object[])relation.get(i);
			String[] str = new String[3];
			str[0] = String.valueOf(objects[9])+"'"+String.valueOf(objects[10]);
			str[1] = String.valueOf(objects[3])+" ("+String.valueOf(objects[4])+")";
			str[2] = String.valueOf(objects[7])+" ("+String.valueOf(objects[8])+")";
			list.add(str);
		}
		return list;
    }
    
    /**
	 * 保存表关系
	 * @param rowsValue
	 * @param tableId
	 * @param mTableId
	 * @throws FatalException
	 */
    @Transactional
    public void saveColumn(String rowsValue,String tableCode,String parentCode, String groupCode) throws FatalException {
    	//先删除表关系
    	delTableRelation(tableCode, parentCode, groupCode);
        String[] columnIdArr = rowsValue.split(";");
        LogicTableRelation app = null;
        List<LogicTableRelation> list = Lists.newArrayList();
        for (int i = 0; i <columnIdArr.length; i++) {
            String oneRowValue = columnIdArr[i];
        	if(oneRowValue.indexOf("'") != -1){
	            app = new LogicTableRelation();
	            app.setGroupCode(groupCode);
	            app.setTableCode(tableCode);
	            app.setParentTableCode(parentCode);
	            app.setOneRowValue(oneRowValue);
	            list.add(app);
            }
        }
        getDao().save(list);
    }
    
    /**
	 * 删除表关系
	 * @param tableId
	 * @return
	 */
    @Transactional
	public void delTableRelation(String tableCode,String parentCode, String groupCode){
	    getDao().delTableRelationList(tableCode, parentCode, groupCode);
	}
    
    /**
     * 查询逻辑表组的表关系
     * @param groupCode
     * @return
     */
    public List<LogicTableRelation> getTableRelationsByGroupCode(String groupCode) {
        return getDao().getTableRelationsByGroupId(groupCode);
    }
    
    /**
     * 查询逻辑表组的表关系
     * @return LogicTableRelation
     */
    public LogicTableRelation getTableRelation(String groupCode, String tableCode, String columnId, String parentTableCode, String parentColumnId) {
        return getDao().getTableRelation(groupCode, tableCode, columnId, parentTableCode, parentColumnId);
    }
    
    /**
	 * 根据字段Id删除表关系
	 * @param columnId
	 * @return
	 */
    @Transactional
	public void deleteByColumnId(String columnId){
	    getDao().deleteByColumnId(columnId);
	}
    
    /**
     * <p>描述: 根据字段ID查找表关系</p>
     * @param  columnId    设定参数   
     * @return LogicTableRelation   
     * @throws
     */
    public MessageModel checkRelation(String rowsValue) {
    	String[] ids = rowsValue.split(",");
    	for (int i = 0; i < ids.length; i++) {
    		if (!getDao().getByColumnId(ids[i]).isEmpty()) {
    			return new MessageModel(Boolean.FALSE, "所选字段已绑定逻辑表关系，不能删除!");
			}
		}
    	return new MessageModel(Boolean.TRUE, "success");
    }
    
    /**
     * 批量修改逻辑表编码
     * @param oldLogicTableCode 旧的逻辑表编码
     * @param newLogicTableCode 新的逻辑表编码
     */
    @Transactional
    public void batchUpdateTableCode(String oldLogicTableCode, String newLogicTableCode) {
    	getDao().batchUpdateTableCode(oldLogicTableCode, newLogicTableCode);
    }
    
    /**
     * 批量修改父逻辑表编码
     * @param oldLogicTableCode 旧的逻辑表编码
     * @param newLogicTableCode 新的逻辑表编码
     */
    @Transactional
    public void batchUpdateParentTableCode(String oldLogicTableCode, String newLogicTableCode) {
    	getDao().batchUpdateParentTableCode(oldLogicTableCode, newLogicTableCode);
    }
}

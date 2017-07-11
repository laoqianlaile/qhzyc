package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.appmanage.ColumnDefineDao;
import com.ces.config.dhtmlx.dao.appmanage.PhysicalTableDefineDao;
import com.ces.config.dhtmlx.dao.appmanage.TableRelationDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.appmanage.ColumnDefine;
import com.ces.config.dhtmlx.entity.appmanage.LogicGroupRelation;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.appmanage.TableRelation;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxComboOption;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.TableUtil;
import com.ces.utils.BeanUtils;
import com.ces.xarch.core.exception.FatalException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
public class TableRelationService extends
		ConfigDefineDaoService<TableRelation, TableRelationDao> {

	/*
	 * (非 Javadoc) <p>标题: bindingDao</p> <p>描述: 注入自定义持久层(Dao)</p>
	 * 
	 * @param entityClass
	 * 
	 * @see
	 * com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
	 */
	@Autowired
	@Qualifier("tableRelationDao")
	@Override
	protected void setDaoUnBinding(TableRelationDao dao) {
		super.setDaoUnBinding(dao);
	}

	/*
	 * (非 Javadoc) <p>描述: 删除时，同时删除建立起来了相应索引</p>
	 * 
	 * @param ids
	 * 
	 * @see
	 * com.ces.config.dhtmlx.servie.base.ConfigDefineDaoService#delete(java.
	 * lang.String)
	 */
	@Override
	@Transactional
	public void delete(String ids) {
		String[] idArr = ids.split(",");
		for (String id : idArr) {
			TableRelation entity = getByID(id);
			String oTableName = TableUtil.getTableName(entity.getTableId());
			String dTableName = TableUtil.getTableName(entity
					.getRelateTableId());
			String oColumnName = getService(ColumnDefineService.class)
					.getColumnNameById(entity.getColumnId());
			String dColumnName = getService(ColumnDefineService.class)
					.getColumnNameById(entity.getRelateColumnId());
			dropIndexByColumnNames(oTableName, oColumnName);
			dropIndexByColumnNames(dTableName, dColumnName);
			delete(entity);
			// 移除表关系缓存
			TableUtil.removeTableRelation(entity.getTableId(),
					entity.getRelateTableId());
			// 删除表关系视图
			dropRelationView(entity.getTableId(), entity.getRelateTableId());
		}
	}

	/**
	 * 保存表关系
	 * 
	 * @param rowsValue
	 * @param tableId
	 * @param mTableId
	 * @throws FatalException
	 */
	@Transactional
	public void saveColumn(String rowsValue, String tableId, String mTableId)
			throws FatalException {
		// 先删除表关系
		delTableRelation(tableId, mTableId, Boolean.FALSE);
		String[] columnIdArr = rowsValue.split(";");
		TableRelation app = null;
		List<TableRelation> list = Lists.newArrayList();
		for (int i = 0; i < columnIdArr.length; i++) {
			String oneRowValue = columnIdArr[i];
			if (oneRowValue.indexOf("'") != -1) {
				app = new TableRelation();
				app.setTableId(tableId);
				app.setRelateTableId(mTableId);
				app.setOneRowValue(oneRowValue);
				list.add(app);
			}
		}
		getDao().save(list);
		if (getService(PhysicalTableDefineService.class).getByID(mTableId)
				.getTableType().equals("0")) {
			// 创建索引
			createIndex(tableId, mTableId, rowsValue);
		}
		// 移除表关系缓存
		TableUtil.removeTableRelation(tableId, mTableId);
		//创建/更新视图
		if (getService(PhysicalTableDefineService.class).getByLogicTableCode(tableId + "'" + mTableId).isEmpty()) {
			createViewByRelation(rowsValue, tableId, mTableId, Boolean.FALSE);
		} else {
			StringBuffer viewRowsValue = new StringBuffer();
			for (int i = 0; i < columnIdArr.length; i++) {
				String oneRowValue = columnIdArr[i];
				if (oneRowValue.indexOf("'") != -1) {
					String[] columnsValue = oneRowValue.split("'");
					viewRowsValue.append(";" + columnsValue[0] + "'" + columnsValue[1]);
				}
			}
			createViewByRelation(viewRowsValue.toString().substring(1), tableId, mTableId, Boolean.FALSE);
		}
	}

	/**
	 * 删除表关系
	 * 
	 * @param tableId
	 * @return
	 */
	@Transactional
	public void delTableRelation(String tableId, String relateTableId, boolean isDelete) {
		getDao().delTableRelationList(tableId, relateTableId);
		// 删除索引
		dropRelateIndex(tableId, relateTableId);
		// 删除视图(保存时不清视图)
		if (isDelete) {
			dropRelationView(tableId, relateTableId);
		}
	}

	/**
	 * 查询目标表下拉框数据
	 * 
	 * @param tableId
	 * @return
	 */
	public Object getCheckMuTable(String tableId) {
		return getDao().checkMbTable(tableId);
	}
	
	/**
	 * 查询目标表下拉框数据，只显示当前物理表组下的表
	 * 
	 * @param tableId
	 * @param groupId
	 * @return
	 */
	public Object getCheckMuTable(String tableId, String groupId) {
		return getDao().checkMbTable(tableId, groupId);
	}

	/**
	 * 源表字段检索
	 * 
	 * @param tableId
	 * @return
	 */
	public Object getAllTableRelations(String tableId) {
		return getDao().getAllTableRelations(tableId);
	}

	/**
	 * 表关系列表
	 * 
	 * @param tableId
	 * @return
	 */
	public Object getRelationByTableId(String tableId) {
		List<Object[]> relation = getDao().getAllTableRelations(tableId);
		List<String[]> list = Lists.newArrayList();
		String preTableName = "";
		for (int i = 0; i < relation.size(); i++) {
			Object[] objects = (Object[]) relation.get(i);
			String[] str = new String[7];
			String curTableName = String.valueOf(objects[5]);

			str[0] = String.valueOf(objects[0]);
			str[2] = String.valueOf(objects[3]) + " ("
					+ String.valueOf(objects[4]) + ")";
			str[4] = String.valueOf(objects[7]) + " ("
					+ String.valueOf(objects[8]) + ")";
			if ("".equals(preTableName) || !preTableName.equals(curTableName)) {
				str[1] = String.valueOf(objects[1])
						+ " ("
						+ String.valueOf(objects[2]) + ")";
				str[3] = String.valueOf(objects[5])
						+ " ("
						+ String.valueOf(objects[6]) + ")";
			} else {
				str[1] = "";
				str[3] = "";
			}
			str[5] = String.valueOf(objects[9]);
			str[6] = String.valueOf(objects[10]);
			list.add(str);
			preTableName = curTableName;
		}
		return list;
	}

	/**
	 * 新增表关系 源表列表 --update
	 * 
	 * @param tableId
	 * @return
	 */
	public Object getAddyTable(String tableId, String relatedTableId) {
		List<Object[]> relation = getDao().getAddyTableColumn(tableId);
		// 查询关联关系
		List<Object[]> relation_del = getDao().getRelationColumn(tableId,
				relatedTableId);
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < relation_del.size(); i++) {
			set.add(relation_del.get(i)[5].toString());
		}
		List<String[]> list = Lists.newArrayList();
		for (int i = 0; i < relation.size(); i++) {
			Object[] objects = (Object[]) relation.get(i);
			if (set.contains(objects[0].toString()))
				continue;
			String[] str = new String[2];
			str[0] = String.valueOf(objects[0]);
			str[1] = String.valueOf(objects[1]) + " ("
					+ String.valueOf(objects[2]) + ")";
			// str[2] = String.valueOf(objects[3]);
			list.add(str);
		}
		return list;
	}

	/**
	 * 下拉框检索
	 * 
	 * @param tableId
	 * @return
	 */
	public Object getShowCheckDefineCoumn(String tableId) {
		List<Object[]> relation = getDao().getCheckDefineCoumn(tableId);
		List<String[]> list = Lists.newArrayList();
		for (int i = 0; i < relation.size(); i++) {
			Object[] objects = (Object[]) relation.get(i);
			String[] str = new String[2];
			str[0] = String.valueOf(objects[0]);
			str[1] = String.valueOf(objects[1]) + " ("
					+ String.valueOf(objects[2]) + ")";
			list.add(str);
		}
		return list;
	}

	/**
	 * 源表和目标表检索
	 * 
	 * @param tableId
	 * @return
	 */
	public Object getShowRelationColumnName(String tableId, String relTableId) {
		List<Object[]> relation = getDao().getRelationColumn(tableId,
				relTableId);
		List<String[]> list = Lists.newArrayList();
		for (int i = 0; i < relation.size(); i++) {
			Object[] objects = (Object[]) relation.get(i);
			String[] str = new String[3];
			str[0] = String.valueOf(objects[5]) + "'"
					+ String.valueOf(objects[6]);
			str[1] = String.valueOf(objects[1]) + " ("
					+ String.valueOf(objects[2]) + ")";
			str[2] = String.valueOf(objects[3]) + " ("
					+ String.valueOf(objects[4]) + ")";
			list.add(str);
		}
		return list;
	}

	/**
	 * 目标表字段检索 --update
	 * 
	 * @param tableId
	 * @return
	 * */
	public Object getShowMbColumnName(String tableId) {
		List<Object[]> relation = getDao().getmbTableColumn(tableId);
		List<String[]> list = Lists.newArrayList();
		for (int i = 0; i < relation.size(); i++) {
			Object[] objects = (Object[]) relation.get(i);
			String[] str = new String[2];
			str[0] = String.valueOf(objects[0]);
			str[1] = String.valueOf(objects[1]) + " ("
					+ String.valueOf(objects[2]) + ")";
			list.add(str);
		}
		return list;
	}

	/**
	 * 源表字段检索
	 * 
	 * @param tableId
	 * @return
	 */
	public Object getShowYColumnName(String tableId, String relatedTableId) {
		List<Object[]> relation = getDao().getyTableColumn(tableId);
		List<Object[]> relation_del = null;
		if (null != relatedTableId) {
			relation_del = getDao().getRelationColumn(relatedTableId, tableId);
		}
		Set<String> set = new HashSet<String>();
		if (relation_del.size() > 0) {
			for (int i = 0; i < relation_del.size(); i++) {
				set.add(relation_del.get(i)[6].toString());
			}
		}
		List<String[]> list = Lists.newArrayList();
		for (int i = 0; i < relation.size(); i++) {
			Object[] objects = (Object[]) relation.get(i);
			if (set.size() > 0 && set.contains(objects[0].toString())) {
				continue;
			}
			String[] str = new String[2];
			str[0] = String.valueOf(objects[0]);
			str[1] = String.valueOf(objects[1]) + " ("
					+ String.valueOf(objects[2]) + ")";
			list.add(str);
		}
		return list;
	}

	/**
	 * 删除表关系
	 * 
	 * @param tableId
	 * @return
	 */
	public void delYmTableRelation(String Id) {
		getDao().delymTableRelation(Id);
	}

	/**
	 * qiucs 2013-8-26
	 * <p>
	 * 标题: getComboOfRelateTables
	 * </p>
	 * <p>
	 * 描述: 根据表ID获取关系表下拉框数据
	 * </p>
	 * 
	 * @param tableId
	 * @param includeMe
	 *            0-不包含自身表 1-包含自身表
	 * @return Object 返回类型
	 * @throws
	 */
	public Object getComboOfRelateTables(String tableId, String includeMe) {
		List<DhtmlxComboOption> opts = Lists.newArrayList();
		DhtmlxComboOption option = null;
		if ("1".equals(includeMe)) {
			PhysicalTableDefine tab = getService(
					PhysicalTableDefineService.class).getByID(tableId);
			option = new DhtmlxComboOption();
			option.setValue(tableId);
			// option.setText(tab.getText());
			option.setText(tab.getShowName());
			opts.add(option);
		}

		List<Object[]> list = getDao().findRelateTablesByTableId(tableId);
		if (null == list || list.isEmpty()) {
			return opts;
		}
		for (Object[] obj : list) {
			option = new DhtmlxComboOption();
			option.setValue(String.valueOf(obj[0]));
			option.setText(String.valueOf(obj[1]));
			opts.add(option);
		}
		return opts;
	}

	/**
	 * qiucs 2013-9-24
	 * <p>
	 * 标题: getRelateTableColumns
	 * </p>
	 * <p>
	 * 描述: 关联表字段
	 * </p>
	 * 
	 * @param tableId
	 * @param relateTableId
	 * @return Map<String,List<String>> 返回类型
	 * @throws
	 */
	public Map<String, List<String>> getTableRelateColumns(String tableId,
			String relateTableId) {
		Map<String, List<String>> map = Maps.newHashMap();
		try {
			List<Object[]> rlt = getDao().getTableRelationColumns(tableId,
					relateTableId);
			if (null == rlt || rlt.isEmpty()) {
				String temp = tableId;
				tableId = relateTableId;
				relateTableId = temp;
				rlt = getDao().getTableRelationColumns(tableId, relateTableId);
			}
			if (null != rlt && !rlt.isEmpty()) {
				List<String> orgin = Lists.newArrayList();
				List<String> relate = Lists.newArrayList();
				for (Object[] obj : rlt) {
					orgin.add(StringUtil.null2empty(obj[0]));
					relate.add(StringUtil.null2empty(obj[1]));
				}
				map.put(tableId, orgin);
				map.put(relateTableId, relate);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return map;
	}

	/**
	 * 根据表ID获取表关系
	 * 
	 * @param tableId
	 *            表ID
	 * @return List<TableRelation>
	 */
	public List<TableRelation> findByTableId(String tableId) {
		return getDao().findByTableId(tableId);
	}

	/**
	 * 根据关联表ID获取表关系
	 * 
	 * @param relateTableId
	 *            关联表ID
	 * @return List<TableRelation>
	 */
	public List<TableRelation> findByRelateTableId(String relateTableId) {
		return getDao().findByRelateTableId(relateTableId);
	}

	/**
	 * 查询源表和关联表记录数目
	 * 
	 * @param tableId
	 *            , rId
	 */
	@Transactional
	public Object getTotalTableRelation(String tableId, String rId) {
		return getDao().getTotalTableRelation(tableId, rId);
	}

	/**
	 * 根据表ID查询关联表的信息
	 * 
	 * @param tableId
	 * */
	@Transactional
	public Object queryRelationTableByTableID(String tableId) {
		List<DhtmlxComboOption> opts = Lists.newArrayList();
		DhtmlxComboOption option = null;
		List<Object[]> list = getDao().queryRelationTableByTableID(tableId);
		if (null == list || list.isEmpty()) {
			return opts;
		}

		option = new DhtmlxComboOption();
		option.setValue("");
		option.setText("请选择");
		opts.add(option);
		for (Object[] obj : list) {
			option = new DhtmlxComboOption();
			option.setValue(String.valueOf(obj[0]));
			option.setText(String.valueOf(obj[1]));
			opts.add(option);
		}
		return opts;
	}

	/**
	 * gmh 2013-10-21 用于字段关联查询两表之间的关联字段
	 * 
	 * @param child_tid
	 * @param parent_tid
	 * */
	@Transactional
	public Map<String, List<String>> queryRelationCols(String child_tid,
			String parent_tid) {
		List<Object[]> cols = getDao().queryRelationCols(child_tid, parent_tid);
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		List<String> child_cols = new ArrayList<String>();
		List<String> parent_cols = new ArrayList<String>();
		if (null == cols) {
			return null;
		}
		for (Object[] obj : cols) {
			child_cols.add(obj[0].toString());
			parent_cols.add(obj[1].toString());
		}
		result.put("child_cols", child_cols);
		result.put("parent_cols", parent_cols);
		return result;
	}

	/**
	 * qiucs 2013-10-23
	 * <p>
	 * 描述: 创建索引统一入口
	 * </p>
	 * 
	 * @param tableId
	 * @param relateTableId
	 * @param allColumnIds
	 *            设定参数
	 */
	@Transactional
	protected void createIndex(String tableId, String relateTableId,
			String allColumnIds) {
		try {
			// search table name by table id
			String tableName = TableUtil.getTableName(tableId);
			String relateTableName = TableUtil.getTableName(relateTableId);

			String[] allColumnArr = allColumnIds.split(";");
			DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
			for (int i = 0; i < allColumnArr.length; i++) {
				if (StringUtil.isEmpty(allColumnArr[i]))
					continue;
				String[] columnArr = allColumnArr[i].split("'");
				String columnName = getService(ColumnDefineService.class)
						.getColumnNameById(columnArr[0]);
				String relateColumnName = getService(ColumnDefineService.class)
						.getColumnNameById(columnArr[1]);
				//
				dao.createOneColumnIndex(ConstantVar.IndexPrefix.RELATION,
						tableName, columnName);
				dao.createOneColumnIndex(ConstantVar.IndexPrefix.RELATION,
						relateTableName, relateColumnName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * qiucs 2013-10-23
	 * <p>
	 * 描述: 删除表关系中的索引
	 * </p>
	 * 
	 * @param oTableId
	 * @param dTableId
	 */
	@Transactional
	protected void dropRelateIndex(String oTableId, String dTableId) {
		try {
			// search table name by table id
			String oTableName = TableUtil.getTableName(oTableId);
			String dTableName = TableUtil.getTableName(dTableId);
			Map<String, List<String>> rMap = TableUtil.getTableRelation(
					oTableId, dTableId);
			if (null != rMap && !rMap.isEmpty()) {
				List<String> oList = rMap.get(oTableId);
				List<String> dList = rMap.get(dTableId);
				dropIndexByColumnNames(oTableName,
						oList.toArray(new String[oList.size()]));
				dropIndexByColumnNames(dTableName,
						dList.toArray(new String[dList.size()]));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * qiucs 2013-10-23
	 * <p>
	 * 描述: 删除指定表指定列的索引
	 * </p>
	 * 
	 * @param tableName
	 * @param columnNames
	 */
	@Transactional
	protected void dropIndexByColumnNames(String tableName,
			String... columnNames) {
		DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
		for (String columnName : columnNames) {
			dao.dropOneColumnIndex(ConstantVar.IndexPrefix.RELATION, tableName,
					columnName);
		}
	}

	/**
	 * qiucs 2013-11-28
	 * <p>
	 * 描述: 根据表ID删除表关系
	 * </p>
	 * 
	 * @param tableId
	 *            设定参数
	 */
	@Transactional
	public void deleteByTableId(String tableId) {
		getDao().deleteByTableId(tableId);
	}

	/**
	 * qiucs 2013-11-28
	 * <p>
	 * 描述: 根据字段ID删除表关系
	 * </p>
	 * 
	 * @param columnId
	 *            设定参数
	 */
	@Transactional
	public void deleteByColumnId(String columnId) {
		getDao().deleteByColumnId(columnId);
	}

	public List<Object[]> findDocumentTableByTableId(String tableId) {
		return getDao().findDocumentTableByTableId(tableId);
	}

	/**
	 * qiujinwei 2014-12-09
	 * <p>
	 * 描述: 验证表关系是否存在
	 * </p>
	 * 
	 * @param tableId
	 *            设定参数
	 */
	public MessageModel getRelationsOfIds(String ids, String groupId,
			String codes) {
		String[] idArr = ids.split(",");
		String[] codeArr = codes.split(",");
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < idArr.length; i++) {
			map.put(codeArr[i + 1], idArr[i]);
		}
		String logicGroupCode = getService(PhysicalGroupDefineService.class)
				.getByID(groupId).getLogicGroupCode();
		List<LogicGroupRelation> list = getService(
				LogicGroupRelationService.class).getByGroupCode(logicGroupCode);
		for (LogicGroupRelation entity : list) {
			if (map.get(entity.getParentTableCode()) != null
					&& map.get(entity.getTableCode()) != null) {
				Map<String, List<String>> resultMap = TableUtil
						.getTableRelation(map.get(entity.getParentTableCode()),
								map.get(entity.getTableCode()));
				if (resultMap == null || resultMap.isEmpty()) {
					return new MessageModel(false, getService(
							PhysicalTableDefineService.class).getByID(
							map.get(entity.getParentTableCode())).getShowName()
							+ "与"
							+ getService(PhysicalTableDefineService.class)
									.getByID(map.get(entity.getTableCode()))
									.getShowName() + "没有表关系!");
				}
			}
		}
		return new MessageModel(true, "success");
	}

	/**
	 * qiujinwei 2014-12-29
	 * <p>
	 * 描述: 根据表关系自动创建数据库视图
	 * </p>
	 * @param rowsValue
	 * @param tableId
	 * @param mTableId     设定参数
	 */
	@Transactional
	public void createViewByRelation(String rowsValue, String tableId,String mTableId, boolean isCreated){
		if (isCreated) {
			StringBuffer sql = new StringBuffer("select ");
			String tableName = TableUtil.getTableName(tableId);
	    	String mTableName = TableUtil.getTableName(mTableId);
	    	String logicTableCode = tableId + "'" + mTableId;
			PhysicalTableDefine view = getDaoFromContext(PhysicalTableDefineDao.class).getByLogicTableCode(tableId + "'" + mTableId).get(0);
			List<ColumnDefine> list = getService(ColumnDefineService.class).findByTableId(tableId);
	    	List<ColumnDefine> mList = getService(ColumnDefineService.class).findByTableId(mTableId);
	    	sql.append("a.id" + DatabaseHandlerDao.getSeperator() + "'_'" + DatabaseHandlerDao.getSeperator() + "b.id as ID ");
	    	//creat_time update_time delete_time 字段只留一个，取最晚时间，is_delete 字段有1留1
	    	sql.append(", case when a." + ConstantVar.ColumnName.IS_DELETE + "='1' then '1' ")
	    	   .append(" when b." + ConstantVar.ColumnName.IS_DELETE + "='1' then '1' else '0' end as IS_DELETE");
	    	sql.append(madeSqlOfComparteDate(ConstantVar.ColumnName.CREATE_TIME));
	    	sql.append(madeSqlOfComparteDate(ConstantVar.ColumnName.UPDATE_TIME));
	    	sql.append(madeSqlOfComparteDate(ConstantVar.ColumnName.DELETE_TIME));
	    	for (int i = 0; i < list.size(); i++) {
	    		if (list.get(i).getColumnName().equals(ConstantVar.ColumnName.CREATE_TIME) ||
    				list.get(i).getColumnName().equals(ConstantVar.ColumnName.UPDATE_TIME) || 
    				list.get(i).getColumnName().equals(ConstantVar.ColumnName.DELETE_TIME) || 
    				list.get(i).getColumnName().equals(ConstantVar.ColumnName.IS_DELETE)) continue;
				sql.append(",a." + list.get(i).getColumnName() + " as A_" + list.get(i).getColumnName());
			}
	    	for (int i = 0; i < mList.size(); i++) {
	    		if (mList.get(i).getColumnName().equals(ConstantVar.ColumnName.CREATE_TIME) ||
	    			mList.get(i).getColumnName().equals(ConstantVar.ColumnName.UPDATE_TIME) || 
	    			mList.get(i).getColumnName().equals(ConstantVar.ColumnName.DELETE_TIME) || 
	    			mList.get(i).getColumnName().equals(ConstantVar.ColumnName.IS_DELETE)) continue;
	    		sql.append(",b." + mList.get(i).getColumnName() + " as B_" + mList.get(i).getColumnName());
			}
	    	sql.append(" from ").append(tableName).append(" a ")
	    	   .append(" left join ").append(mTableName).append(" b on 1=1 ");
	    	String[] columnIdArr = rowsValue.split(";");
	    	for (int i = 0; i < columnIdArr.length; i++) {
	    		if(columnIdArr[i].indexOf("'") != -1){
	    			String[] columnsValue = columnIdArr[i].split("'");
	    			sql.append(" and a." + columnsValue[0] + "= b." + columnsValue[1]);
	    		}
			}
	    	DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
	    	dao.createView(view.getTableName(), sql.toString());
	    	createCoflowView(view.getTableName(), view.getShowName(), list, mList, logicTableCode);
		} else{
    	StringBuffer sql = new StringBuffer("select ");
    	String tableName = TableUtil.getTableName(tableId);
    	String mTableName = TableUtil.getTableName(mTableId);
    	String viewName = getViewName(tableName, mTableName);
    	String showName = TableUtil.getTableText(tableId) + "-" + TableUtil.getTableText(mTableId);
    	String logicTableCode = tableId + "'" + mTableId;//记录视图对应的两张关系表ID
    	List<ColumnDefine> list = getService(ColumnDefineService.class).findByTableId(tableId);
    	List<ColumnDefine> mList = getService(ColumnDefineService.class).findByTableId(mTableId);
    	sql.append("a.id" + DatabaseHandlerDao.getSeperator() + "'_'" + DatabaseHandlerDao.getSeperator() + "b.id as ID ");
    	//creat_time update_time delete_time 字段只留一个，取最晚时间，is_delete 字段有1留1
    	sql.append(", case when a." + ConstantVar.ColumnName.IS_DELETE + "='1' then '1' ")
    	   .append(" when b." + ConstantVar.ColumnName.IS_DELETE + "='1' then '1' else '0' end as IS_DELETE");
    	sql.append(madeSqlOfComparteDate(ConstantVar.ColumnName.CREATE_TIME));
    	sql.append(madeSqlOfComparteDate(ConstantVar.ColumnName.UPDATE_TIME));
    	sql.append(madeSqlOfComparteDate(ConstantVar.ColumnName.DELETE_TIME));
    	for (int i = 0; i < list.size(); i++) {
    		if (list.get(i).getColumnName().equals(ConstantVar.ColumnName.CREATE_TIME) ||
				list.get(i).getColumnName().equals(ConstantVar.ColumnName.UPDATE_TIME) || 
				list.get(i).getColumnName().equals(ConstantVar.ColumnName.DELETE_TIME) || 
				list.get(i).getColumnName().equals(ConstantVar.ColumnName.IS_DELETE)) continue;
			sql.append(",a." + list.get(i).getColumnName() + " as A_" + list.get(i).getColumnName());
		}
    	for (int i = 0; i < mList.size(); i++) {
    		if (mList.get(i).getColumnName().equals(ConstantVar.ColumnName.CREATE_TIME) ||
    			mList.get(i).getColumnName().equals(ConstantVar.ColumnName.UPDATE_TIME) || 
    			mList.get(i).getColumnName().equals(ConstantVar.ColumnName.DELETE_TIME) || 
    			mList.get(i).getColumnName().equals(ConstantVar.ColumnName.IS_DELETE)) continue;
    		sql.append(",b." + mList.get(i).getColumnName() + " as B_" + mList.get(i).getColumnName());
		}
    	sql.append(" from ").append(tableName).append(" a ")
    	   .append(" left join ").append(mTableName).append(" b on 1=1 ");
    	String[] columnIdArr = rowsValue.split(";");
    	for (int i = 0; i < columnIdArr.length; i++) {
    		if(columnIdArr[i].indexOf("'") != -1){
    			String[] columnsValue = columnIdArr[i].split("'");
    			String columnName = getService(ColumnDefineService.class).getColumnNameById(columnsValue[0]);
    			String relationColumnName = getService(ColumnDefineService.class).getColumnNameById(columnsValue[1]);
    			sql.append(" and a." + columnName + "= b." + relationColumnName);
    		}
		}
    	DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
    	dao.createView(viewName, sql.toString());
    	createCoflowView(viewName, showName, list, mList, logicTableCode);
		}
    }
	
	/**
	 * qiujinwei 2014-12-29
	 * <p>
	 * 描述: 在表定义中，生成相应记录
	 * </p>
	 * @param viewName
	 * @param showName
	 * @param list     
	 * @param mList  	设定参数
	 */
	@Transactional
    private PhysicalTableDefine createCoflowView(String viewName, String showName, List<ColumnDefine> list, List<ColumnDefine> mList, String logicTableCode ) {
    	boolean isCreate = false;
    	PhysicalTableDefine entity = getService(PhysicalTableDefineService.class).getByTableName(viewName.toUpperCase());
    	List<ColumnDefine> viewColumnList = new ArrayList<ColumnDefine>();
    	List<ColumnDefine> oldCols = new ArrayList<ColumnDefine>();
    	Integer showOrder = 0;
    	if (null == entity) {
    		entity = new PhysicalTableDefine();
    		entity.setTableTreeId("-VA");
    		entity.setClassification("V");
    		entity.setCreated("1");
    		entity.setTableType("1");
    		entity.setTablePrefix(viewName.substring(0, 5));
    		entity.setTableCode(viewName.substring(5));
    		entity.setLogicTableCode(logicTableCode);
    		entity.setRemark("由表关系定义自动生成");
    		entity.setShowName(showName);
    		getService(PhysicalTableDefineService.class).save(entity);
    		isCreate = true;
    	} else {
        	// 获取旧的字段
    		oldCols = getDaoFromContext(ColumnDefineDao.class).findByTableId(entity.getId());
    		for (int i = 0; i < oldCols.size(); i++) {
				if (oldCols.get(i).getColumnName().equals(ConstantVar.ColumnName.ID)) {
					oldCols.remove(i);
					continue;
				}
				if (oldCols.get(i).getColumnName().equals(ConstantVar.ColumnName.CREATE_TIME)) {
					oldCols.remove(i);
					continue;
				}
				if (oldCols.get(i).getColumnName().equals(ConstantVar.ColumnName.UPDATE_TIME)) {
					oldCols.remove(i);
					continue;
				}
				if (oldCols.get(i).getColumnName().equals(ConstantVar.ColumnName.DELETE_TIME)) {
					oldCols.remove(i);
					continue;
				}
				if (oldCols.get(i).getColumnName().equals(ConstantVar.ColumnName.IS_DELETE)) {
					oldCols.remove(i);
					continue;
				}
			}
    	}
    	if (isCreate) {
    		ColumnDefine autoColumn = newViewColumnDefine(entity.getId());
    		autoColumn.setShowOrder(++showOrder);
    		viewColumnList.add(autoColumn);
    		//creat_time update_time delete_time 字段只留一个，取最晚时间，is_delete 字段有1留1
    		ColumnDefine creatTime = newViewColumnDefine(entity.getId(), "创建时间", ConstantVar.ColumnName.CREATE_TIME);
    		viewColumnList.add(creatTime);
    		ColumnDefine updateTime = newViewColumnDefine(entity.getId(), "修改时间", ConstantVar.ColumnName.UPDATE_TIME);
    		viewColumnList.add(updateTime);
    		ColumnDefine deleteTime = newViewColumnDefine(entity.getId(), "删除时间", ConstantVar.ColumnName.DELETE_TIME);
    		viewColumnList.add(deleteTime);
    		ColumnDefine isDelete = newViewColumnDefine(entity.getId(), "删除标识", ConstantVar.ColumnName.IS_DELETE);
    		viewColumnList.add(isDelete);
		}
    	//A表列重命名
		String tableShowName = TableUtil.getTableText(list.get(0).getTableId());
		for (int i = 0; i < list.size(); i++) {
			ColumnDefine col = list.get(i);
    		ColumnDefine viewColumn = null;
    		if (col.getColumnName().equals(ConstantVar.ColumnName.CREATE_TIME) ||
				col.getColumnName().equals(ConstantVar.ColumnName.UPDATE_TIME) ||
				col.getColumnName().equals(ConstantVar.ColumnName.DELETE_TIME) ||
				col.getColumnName().equals(ConstantVar.ColumnName.IS_DELETE)) {
				continue;
			}
    		if (!isCreate) {
    			for (int j = 0; j < oldCols.size(); j++) {
    				if (oldCols.get(j).getColumnName().equals("A_" + col.getColumnName())) {
    					viewColumn = new ColumnDefine();
    	    			BeanUtils.copy(col, viewColumn);
    	    			viewColumn.setId(oldCols.get(j).getId());
    	    			viewColumn.setTableId(entity.getId());
    	    			viewColumn.setColumnName("A_" + col.getColumnName());
    	    			viewColumn.setRemark("来源于 " + tableShowName);
    					oldCols.remove(j);
    				}
				}
    		}
    		if (null == viewColumn || isCreate) {
    			viewColumn = new ColumnDefine();
    			BeanUtils.copy(col, viewColumn);
    			viewColumn.setId(null);
    			viewColumn.setTableId(entity.getId());
    			viewColumn.setColumnName("A_" + col.getColumnName());
    			viewColumn.setRemark("来源于 " + tableShowName);
    		}
    		viewColumn.setShowOrder(++showOrder);
    		viewColumnList.add(viewColumn);
    	}
    	//B表列重命名
    	String mTableShowName = TableUtil.getTableText(mList.get(0).getTableId());
    	for (int i = 0; i < mList.size(); i++) {
			ColumnDefine col = mList.get(i);
    		ColumnDefine viewColumn = null;
    		if (col.getColumnName().equals(ConstantVar.ColumnName.CREATE_TIME) ||
				col.getColumnName().equals(ConstantVar.ColumnName.UPDATE_TIME) ||
				col.getColumnName().equals(ConstantVar.ColumnName.DELETE_TIME) ||
				col.getColumnName().equals(ConstantVar.ColumnName.IS_DELETE)) {
				continue;
			}
    		if (!isCreate) {
    			for (int j = 0; j < oldCols.size(); j++) {
    				if (oldCols.get(j).getColumnName().equals("B_" + col.getColumnName())) {
    					viewColumn = new ColumnDefine();
    	    			BeanUtils.copy(col, viewColumn);
    	    			viewColumn.setId(oldCols.get(j).getId());
    	    			viewColumn.setTableId(entity.getId());
    	    			viewColumn.setColumnName("B_" + col.getColumnName());
    	    			viewColumn.setRemark("来源于 " + tableShowName);
    					oldCols.remove(j);
    				}
				}
    		}
    		if (null == viewColumn || isCreate) {
    			viewColumn = new ColumnDefine();
    			BeanUtils.copy(col, viewColumn);
    			viewColumn.setId(null);
    			viewColumn.setTableId(entity.getId());
    			viewColumn.setColumnName("B_" + col.getColumnName());
    			viewColumn.setRemark("来源于 " + mTableShowName);
    		}
    		viewColumn.setShowOrder(++showOrder);
    		viewColumnList.add(viewColumn);
    	}
    	// 保存新添加的字段
    	getService(ColumnDefineService.class).save(viewColumnList);
    	// 删除不存在的字段
    	getService(ColumnDefineService.class).delete(oldCols);
    	return entity;
    }
    
    /**
     * qiujinwei 2014-12-29 
     * <p>描述: 创建一个唯一字段实例 </p>
     * @return ColumnDefine
     */
    private ColumnDefine newViewColumnDefine(String tableId) {
    	ColumnDefine entity = new ColumnDefine();
    	
    	entity.setTableId(tableId);
    	entity.setColumnName(ConstantVar.ColumnName.ID);
    	entity.setShowName("ID由（A表ID_B表ID）组成");
    	entity.setColumnType("0");
    	entity.setLength(65);
    	entity.setDataType(ConstantVar.DataType.CHAR);
    	entity.setCreated("1");
    	entity.setInputable("0");
    	entity.setUpdateable("0");
    	entity.setListable("1");
    	entity.setSearchable("1");
    	entity.setSortable("1");
    	entity.setAlign("left");
    	entity.setFilterType("EQ");
    	entity.setWidth(80);
    	entity.setRemark("由表定义自动创建");
    	return entity;
    }
    
    /**
     * qiujinwei 2015-02-12 
     * <p>描述: 创建一个业务字段实例 </p>
     * @return ColumnDefine
     */
    private ColumnDefine newViewColumnDefine(String tableId, String showName, String columnName) {
    	ColumnDefine entity = new ColumnDefine();
    	
    	entity.setTableId(tableId);
    	entity.setColumnName(columnName);
    	entity.setShowName(showName);
    	entity.setColumnType("0");
    	entity.setLength(65);
    	entity.setDataType(ConstantVar.DataType.CHAR);
    	entity.setCreated("1");
    	entity.setInputable("0");
    	entity.setUpdateable("0");
    	entity.setListable("1");
    	entity.setSearchable("1");
    	entity.setSortable("1");
    	entity.setAlign("left");
    	entity.setFilterType("EQ");
    	entity.setWidth(80);
    	entity.setRemark("由表定义自动创建");
    	return entity;
    }
    
    /**
     * qiujinwei 2015-01-13 
     * <p>描述: 删除表关系对应的视图 </p>
     * @return 
     */
    @Transactional
    private void dropRelationView(String tableId, String relateTableId){
    	DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
    	String viewName1 = getDao(PhysicalTableDefineDao.class,PhysicalTableDefine.class).getTableNameByLogicTableCode(tableId + "'" + relateTableId);
    	String viewName2 = getDao(PhysicalTableDefineDao.class,PhysicalTableDefine.class).getTableNameByLogicTableCode(relateTableId + "'" + tableId);
    	if (StringUtil.isNotEmpty(viewName1)) {
    		dao.dropView(viewName1);
		} else if (StringUtil.isNotEmpty(viewName2)) {
			dao.dropView(viewName2);
		}
    	getService(PhysicalTableDefineService.class).updateExistsViews();
		getService(PhysicalTableDefineService.class).deleteNotExistsViews();
    }
    
    /**
     * qiujinwei 2015-02-12 
     * <p>描述: 构造sql比较两关系表中的日期列，取大值 </p>
     * @return String
     */
    private String madeSqlOfComparteDate(String columnName){
    	StringBuffer sql = new StringBuffer();
		sql.append(", case when a." + columnName + " > b." + columnName + " then a." + columnName);
		sql.append(" when a." + columnName + " < b." + columnName + " then b." + columnName + " end as " + columnName);
    	return sql.toString();
    }
    
    /**
     * qiujinwei 2015-04-14 
     * <p>描述: 构造视图名</p>
     * @return String
     */
    private String getViewName(String tableName, String mTableName){
    	String viewName = "V_" + tableName.substring(2) + "_" + mTableName.substring(2);
    	if (viewName.length() > 30) {
			int step = viewName.length() - 30;
			if (step % 2 == 1) {
				viewName = "V_" + tableName.substring(2, tableName.length() - ((step + 1) / 2)) + "_" + mTableName.substring(2, mTableName.length() - ((step - 1) / 2));
			} else {
				viewName = "V_" + tableName.substring(2, tableName.length() - step / 2) + "_" + mTableName.substring(2, mTableName.length() - step / 2);
			}
		}
    	return viewName;
    }
    
    /**
     * 根据主表ID获取从表ID
     * @parma tableId
     * @rekturn String
     */
    public String getSlaveTableId(String tableId){
    	return getDao().findByTableId(tableId).get(0).getRelateTableId();
    }
    
    /**
     * 根据从表ID获取主表ID
     * @parma sTableId
     * @rekturn String
     */
    public String getMainTableId(String sTableId){
    	return getDao().findByRelateTableId(sTableId).get(0).getTableId();
    }
    
    /**
     * 获取所有建立了字段关系的表id
     * @rekturn List<Object>
     */
    public List<String> getAllTableId(){
    	return getDao().getAllTableId();
    }
}

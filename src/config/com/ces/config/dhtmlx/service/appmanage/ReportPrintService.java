package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.appmanage.ReportColumnDao;
import com.ces.config.dhtmlx.dao.appmanage.ReportPrintDao;
import com.ces.config.dhtmlx.dao.appmanage.ReportPrintSettingDao;
import com.ces.config.dhtmlx.dao.appmanage.ReportTableDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.appmanage.Report;
import com.ces.config.dhtmlx.entity.appmanage.ReportColumn;
import com.ces.config.dhtmlx.entity.appmanage.ReportPrintSetting;
import com.ces.config.dhtmlx.entity.appmanage.ReportTable;
import com.ces.config.dhtmlx.json.entity.appmanage.GroupData;
import com.ces.config.dhtmlx.json.entity.appmanage.PrintCell;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.CodeUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.StringUtil;

@Component
public class ReportPrintService extends ConfigDefineDaoService<Report,ReportPrintDao> {
    /* (non-Javadoc)
	 * @see com.ces.config.dhtmlx.servie.base.ConfigService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
	 * @author Administrator
	 * @date 2013-10-10 17:37:56
	 */
	@Override
	@Autowired
	@Qualifier("reportPrintDao")
	protected void setDaoUnBinding(ReportPrintDao dao) {
		super.setDaoUnBinding(dao);
	}
	/**
	 * <p>描述: 取得报表数据</p>
	 * @param reportId
	 * @return
	 * @author Administrator
	 * @date 2013-10-29  11:34:52
	 */
	public Map<String, ReportTable> getReportTables(String reportId){
		List<ReportTable> list=getDao(ReportTableDao.class, ReportTable.class).findByReportId(reportId);
		Map<String, ReportTable> rtmap = new HashMap<String, ReportTable>();
		for (ReportTable reportTable : list) {
			rtmap.put(reportTable.getTableId(), reportTable);
		}
		return rtmap;
	}
	/**
	 * <p>描述: 取得报表字段名和字段类型</p>
	 * @param reportId
	 * @return
	 * @author Administrator
	 * @date 2013-10-29  12:39:13
	 */
	public Map<String, String> getDataTypes(String reportId){
		List<Object[]> dataTypeList=getDao(ReportColumnDao.class,ReportColumn.class).findDataTypeByReportId(reportId);
		Map<String, String> dtmap = new HashMap<String, String>();
		for (Object[] objects : dataTypeList) {
			dtmap.put(StringUtil.null2empty(objects[0]), StringUtil.null2empty(objects[1]));
		}
		return dtmap;
	}
	/**
	 * <p>描述: 取得报表字段名和编码类型</p>
	 * @param reportId
	 * @return
	 * @author Administrator
	 * @date 2013-10-30  09:59:04
	 */
	public Map<String, String> getCodeTypes(String reportId){
		List<Object[]> codeTypeList=getDao(ReportColumnDao.class,ReportColumn.class).findCodeTypeByReportId(reportId);
		Map<String, String> ctmap=new HashMap<String,String>();
		for (Object[] objects : codeTypeList) {
			ctmap.put(StringUtil.null2empty(objects[0]), StringUtil.null2empty(objects[1]));
		}
		return ctmap;
	}
	/**
	 * <p>描述: 取得所有打印数据</p>
	 * @param reportId
	 * @param tableId
	 * @param rowIds
	 * @param timestamp
	 * @return
	 * @author Administrator
	 * @date 2013-10-28  09:39:27
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getData(String reportId,String tableId,String rowIds,String timestamp){	
		Map<String, String> dtmap=getDataTypes(reportId);	
		Map<String, String> ctmap=getCodeTypes(reportId);
		Map<String, ReportTable> rtmap = getReportTables(reportId);
		boolean isProcess=false;
		List<Object[]> selectCol=getDao(ReportColumnDao.class, ReportColumn.class).getLocationSettigs(reportId);
		StringBuffer selects=new StringBuffer(" select ");	
		StringBuffer columns=new StringBuffer("");
		StringBuffer datatypes=new StringBuffer("");
		StringBuffer codetypes=new StringBuffer("");
		List<Object[]> settingList=getDao(ReportPrintSettingDao.class, ReportPrintSetting.class).getSettigs(reportId);
		StringBuffer orders=new StringBuffer();
		StringBuffer ands=new StringBuffer();
		int i = 0, lsize = 0;
		for (i = 0, lsize = settingList.size(); i < lsize; i++) {
			Object[] setObj=settingList.get(i);
			if("1".equals(setObj[0]) && null!=setObj[1]){
				if(!"".equals(setObj[1])){
						if(!ands.toString().contains(rtmap.get(setObj[2]).getTableAlias()+"."+setObj[3])){
							ands.append(rtmap.get(setObj[2]).getTableAlias()).append(".").append(setObj[3]).append("='").append(setObj[1]).append("' and ");
						}
				}
			} else if("2".equals(setObj[0]) || "3".equals(setObj[0])) {
				if(!"".equals(setObj[1]) && null!=setObj[1]){					
					if("2".equals(setObj[0])){
						selects.append(rtmap.get(setObj[2]).getTableAlias()).append(".").append(setObj[3]).append(",");
						columns.append(setObj[3]).append(",");
						datatypes.append(dtmap.get(setObj[3])).append(",");
						codetypes.append(ctmap.get(setObj[3])).append(",");
					}
					if(!orders.toString().contains(rtmap.get(setObj[2]).getTableAlias()+"."+setObj[3].toString())){
						orders.append(rtmap.get(setObj[2]).getTableAlias()).append(".").append(setObj[3]).append(" "+setObj[1]+" ").append(",");
					}
				}
			}
		}
		for (i = 0, lsize = selectCol.size(); i < lsize; i++) {
			Object[] col=selectCol.get(i);
			if(!selects.toString().contains(rtmap.get(col[2]).getTableAlias()+"."+col[3])){
				selects.append(rtmap.get(col[2]).getTableAlias()).append(".").append(col[3]+" as "+col[4]).append(",");
				columns.append(col[3]).append(",");
				datatypes.append(dtmap.get(col[3])).append(",");
				codetypes.append(ctmap.get(col[3])).append(",");
			}
		}
		List<ReportColumn> colList=getDao(ReportColumnDao.class, ReportColumn.class).findByReportId(reportId);
		Map<String, String> colrtmap=new HashMap<String, String>();
		for (ReportColumn reportColumn : colList) {
			colrtmap.put(reportColumn.getColumnId(), reportColumn.getColumnName());
		}
		Map<String, String> relationrtmap=new HashMap<String, String>();
		List<Object[]> relationList=getDao().getReportRelation(reportId);
		for (Object[] objects : relationList) {
			relationrtmap.put(objects[0]+","+objects[2], objects[1]+","+objects[3]);
		}
		Set<String> set = rtmap.keySet();
		Iterator<String> it = set.iterator();
		StringBuffer from = new StringBuffer(" from "+rtmap.get(tableId).getTableName()+" "+rtmap.get(tableId).getTableAlias());
		while(it.hasNext()) {
	            String key = it.next();
	            if(!key.equals(tableId)){
	            	from.append(" join "+rtmap.get(key).getTableName()+" "+rtmap.get(key).getTableAlias());
	            if(relationrtmap.containsKey(tableId+","+rtmap.get(key).getTableId())){
	            	String[] val=relationrtmap.get(tableId+","+rtmap.get(key).getTableId()).split(",");
	            	from.append(" on "+rtmap.get(tableId).getTableAlias()+"."+colrtmap.get(val[0])+" = "+rtmap.get(key).getTableAlias()+"."+colrtmap.get(val[1]));
	            }else if(relationrtmap.containsKey(rtmap.get(key).getTableId()+","+tableId)){
	            	String[] value=relationrtmap.get(rtmap.get(key).getTableId()+","+tableId).split(",");
	            	from.append(" on "+rtmap.get(key).getTableAlias()+"."+colrtmap.get(value[0])+" = "+rtmap.get(tableId).getTableAlias()+"."+colrtmap.get(value[1]));
	            }
	        }
		}
		StringBuffer where=new StringBuffer();
		if(StringUtil.isNotEmpty(rowIds)){
			where.append(" where ");
			if(rowIds.contains(",")){
				String ids="'"+rowIds.replace(",", "','")+"'";
				where.append(rtmap.get(tableId).getTableAlias()).append(".id in (").append(ids).append(")");
			}else{
				where.append(rtmap.get(tableId).getTableAlias()).append(".id='").append(rowIds).append("'");
			}
        } else {
            String filter = CommonUtil.getQueryFilter(tableId + timestamp);
            if (StringUtil.isNotEmpty(filter)) {
                where.append(" where ").append(processFilter(filter, rtmap.get(tableId).getTableAlias()));
            }
        }
		if(!ands.toString().isEmpty()){
			if("and ".equals(ands.substring(ands.length()-4, ands.length()))){
				ands.delete(ands.length()-4, ands.length());
			}
			if(where.toString().isEmpty()){
				where.append(" where ").append(ands);
			}else{
				where.append(" and ").append(ands);
			}			
		}
		if(StringUtil.isNotEmpty(selects) && ",".equals(selects.substring(selects.length()-1, selects.length()))){
			selects.delete(selects.length()-1, selects.length());
		}
		if(StringUtil.isNotEmpty(columns) && ",".equals(columns.substring(columns.length()-1, columns.length()))){
			columns.delete(columns.length()-1, columns.length());
		}
		/*if(StringUtil.isNotEmpty(datatypes) && ",".equals(datatypes.substring(datatypes.length()-1, datatypes.length()))){
			datatypes.delete(datatypes.length()-1, datatypes.length());
			String dtString=datatypes.toString();
			if(dtString.contains(ConstantVar.DataType.ENUM) || dtString.contains(ConstantVar.DataType.USER) || dtString.contains(ConstantVar.DataType.PART)){
				isProcess=true;
			}
		}
		if(StringUtil.isNotEmpty(codetypes) && ",".equals(codetypes.substring(codetypes.length()-1,	codetypes.length()))){
			codetypes.delete(codetypes.length()-1, codetypes.length());
		}*/
		isProcess = (codetypes.toString().matches("(,*\\w+,*)+"));
		
		String sql=selects.toString()+" "+from.toString()+" "+where.toString();		
		if(StringUtil.isNotEmpty(orders)){
			if(",".equals(orders.substring(orders.length()-1, orders.length()))){
				orders.delete(orders.length()-1, orders.length());
			}
			sql+=" order by "+orders.toString();
		}
		
		if(StringUtil.isEmpty(columns)) return null;
		
		List<Object[]> dataList = null; 		
		
		if (columns.toString().indexOf(",") == -1) {
			dataList = getSpecialProcessData(sql);
		} else {
			dataList = DatabaseHandlerDao.getInstance().queryForList(sql);
		}
		
		if (isProcess) {
			dataList = getProcessData(dataList, datatypes.toString(), codetypes.toString());
		}
		
		return dataList;
	}
	
	/**
	 * qiucs 2015-6-24 下午3:03:22
	 * <p>描述: 过滤条件添加表别名 </p>
	 * @return String
	 */
	private static String processFilter(String filterSql, String tableAlias) {
		StringBuilder sb = new StringBuilder(100);

		// System.out.println(filterSql);
		Pattern pat = Pattern.compile("\\s+(?i)and\\s+|\\)+\\s*(?i)and\\s+\\(*|\\s+(?i)or\\s+|\\)+\\s*(?i)or\\s*\\(+|\\(+|\\)+");
		Matcher mat = pat.matcher(filterSql); 
		int start = 0, end = 0;
		String filter = null;
		while (mat.find()) {
			start  = mat.start();
			filter = filterSql.substring(end, start);
			if (StringUtil.isNotEmpty(filter)) {
				filter = processFilterItem(filter, tableAlias);
				sb.append(filter);
			}
			end = mat.end();
			sb.append(mat.group());
		}
		
		filter = filterSql.substring(end);
		if (StringUtil.isNotEmpty(filter)) {
			filter = processFilterItem(filter, tableAlias);
			sb.append(filter);
		}
		// System.out.println("print filter: " + sb);
		return sb.toString();
	}
	
	/**
	 * qiucs 2015-6-24 下午3:04:26
	 * <p>描述: 单个条件添加别名处理 </p>
	 * @return String
	 */
	private static String processFilterItem(String item, String tableAlias) {
		StringBuilder sb = new StringBuilder(100);

		Pattern pat = Pattern.compile(">=|<=|<>|!=|=|(?i)like|(?i)\\s+in\\s*|<|>|(?i) is null|(?i) is not null");
		Matcher mat = pat.matcher(item); 
		String col = null, val = null, op = null;
		int start = 0, end = 0, len = item.length();
		if (mat.find()) {
			start = mat.start();
			col = item.substring(end, start);
			end = mat.end();
			val = item.substring(end, len);
			op  = StringUtil.null2empty(mat.group());
			
			col = StringUtil.null2empty(col).trim();
			val = StringUtil.null2empty(val).trim();
			
			if (col.matches("\\d+")) {
				sb.append(item);
			} else {
				if (col.startsWith("t_.") || col.startsWith("T_.")) {
					col = col.replaceFirst("t_.|T_.", "");
				} else if (StringUtil.isNotEmpty(col)) {
					sb.append(tableAlias).append(".").append(col);
				}
				sb.append(" ").append(op).append(" ").append(val);
			}
		} else {
			sb.append(item);
		}
		
		// System.out.println(sb);
		return sb.toString();
		
	}
	/**
	 * <p>描述: 处理打印数据报表只有一个字段</p>
	 * @param sql
	 * @return
	 * @author Administrator
	 * @date 2013-10-30  15:13:07
	 */
	public List<Object[]> getSpecialProcessData(String sql){
		@SuppressWarnings("unchecked")
		List<Object[]> vals = DatabaseHandlerDao.getInstance().queryForList(sql);
		List<Object[]> newVals=new ArrayList<Object[]>();
		for(int j=0;j<vals.size();j++){
			Object[] object=new Object[1];
			object[0]=StringUtil.null2empty(vals.get(j));		
			newVals.add(object);
		}
		return newVals;		
	}
	/**
	 * <p>描述: 处理打印数据编码、用户、部门类型的转化</p>
	 * @param reportId
	 * @param tableId
	 * @param rowIds
	 * @param columnFilter
	 * @return
	 * @author Administrator
	 * @date 2013-10-29  12:04:01
	 */
	public List<Object[]> getProcessData(List<Object[]> dataList, String datatypes, String codetypes){
		String[] ctArr=codetypes.split(",");
		int len = dataList.size(), ctlen = ctArr.length, i = 0;
		Map<Integer, String> ctMap = new HashMap<Integer, String>();
		for (; i < ctlen; i++) {
			if (StringUtil.isNotEmpty(ctArr[i])) ctMap.put(i, ctArr[i]);
		}
		if (!ctMap.isEmpty()) {
			Integer[] keyArr = ctMap.keySet().toArray(new Integer[0]);
			Integer key;
			String ctc, val;
			int j, keylen = keyArr.length;
			for (i = 0, len = dataList.size(); i < len; i++) {
				for (j = 0; j < keylen; j++ ) {
					key =keyArr[j];
					ctc = ctMap.get(key);
					val = StringUtil.null2empty(dataList.get(i)[key]);
					if (StringUtil.isNotEmpty(val)) {
						val = CodeUtil.getInstance().getCodeName(ctc, val);
					}
					if (StringUtil.isNotEmpty(val))  {
						dataList.get(i)[key] = val;
					}
				}
			}
		}
		return dataList;
	}
	/**
	 * <p>描述: 取得分组和排序设置数据</p>
	 * @param reportId
	 * @return
	 * @author Administrator
	 * @date 2013-10-28  09:40:24
	 */
	public List<Object[]> getGroupSetting(String reportId){
		List<Object[]> groupList= getService(ReportPrintSettingService.class).getSetting(reportId, "2");
		List<Object[]> group=new ArrayList<Object[]>();
		String previousSettings="";
		String currentSettings="";		
		for (int i = 0; i < groupList.size(); i++) {
			Object[] obj=groupList.get(i);
			if("2".equals(obj[0].toString()) || "3".equals(obj[0].toString())){
				currentSettings=obj[3].toString();
			}
			if(!currentSettings.equals(previousSettings) && !"".equals(currentSettings)){
				group.add(obj);
				previousSettings=currentSettings;
			}
		}
		return group;		
	}

	/**
	 * <p>描述: 取得符合条件的最终打印数据</p>
	 * @param reportId
	 * @param tableId
	 * @param rowIds
	 * @param columnFilter
	 * @return
	 * @author Administrator
	 * @date 2013-10-28  09:42:05
	 */
	public Object getPrintData(String reportId,String tableId,String rowIds,String timestamp){
		List<GroupData> groupData=new ArrayList<GroupData>();
		try{
			List<Object[]> groupList = getService(ReportPrintSettingService.class).getSetting(reportId, "2");
			List<Object[]> locationSettingList=getDao(ReportColumnDao.class, ReportColumn.class).getIndexSettigs(reportId);
			List<Object[]> dataObj=getData(reportId,tableId,rowIds,timestamp);
			int[] rowIndex=new int[locationSettingList.size()];
			int[] colIndex=new int[locationSettingList.size()];
			for (int i = 0; i < locationSettingList.size(); i++) {
				Object[] locObj=locationSettingList.get(i);
				rowIndex[i]=Integer.valueOf(locObj[0].toString());
				colIndex[i]=Integer.valueOf(locObj[1].toString());
			}
			PrintCell cell = null;
			if (null==dataObj) {
				groupData=null;				
			} else {
				if (groupList.size()>0) {			
					String previous="";
					List<List<PrintCell>> dataList=new ArrayList<List<PrintCell>>();
					List<PrintCell> group=new ArrayList<PrintCell>();
					for (int j = 0; j < dataObj.size(); j++) {
						String current="";					
						List<PrintCell> data=new ArrayList<PrintCell>();					
						GroupData gData=new GroupData();
						Object[] objects=dataObj.get(j);					
						for (int i = 0; i < groupList.size(); i++) {
							if(""!=current){
								current += ",";
							}
							current += objects[i];
						}
						if(current.equals(previous)){
							for (int i = groupList.size() ; i < objects.length; i++) {
								cell = new PrintCell();
								cell.setVal(StringUtil.null2empty(objects[i]));
								cell.setRow(rowIndex[i-groupList.size()]);
								cell.setCol(colIndex[i-groupList.size()]);
								data.add(cell);
							}
							dataList.add(data);
							if(j==dataObj.size()-1){
								gData=new GroupData();
								gData.setGroup(group);
								gData.setData(dataList);
								groupData.add(gData);
								continue;
							}else{
								continue;
							}						
						}else{
							if(!dataList.isEmpty()){
								gData.setGroup(group);
								gData.setData(dataList);
								groupData.add(gData);
								dataList=new ArrayList<List<PrintCell>>();
								group=new ArrayList<PrintCell>();
							}
							previous=current;				
							for (int i = 0; i < groupList.size(); i++) {	
								if (StringUtil.isEmpty(groupList.get(i)[4])) continue;
								if (StringUtil.isEmpty(groupList.get(i)[5])) continue;
								cell = new PrintCell();
								cell.setVal(StringUtil.null2empty(objects[i]));
								cell.setRow(Integer.valueOf(groupList.get(i)[4].toString()));
								cell.setCol(Integer.valueOf(groupList.get(i)[5].toString()));
								group.add(cell);
							}
							for (int i = groupList.size(); i < objects.length; i++) {
								cell = new PrintCell();
								cell.setVal(StringUtil.null2empty(objects[i]));
								cell.setRow(rowIndex[i-groupList.size()]);
								cell.setCol(colIndex[i-groupList.size()]);
								data.add(cell);
							}
							dataList.add(data);
						}
						if(j==dataObj.size()-1){
							gData=new GroupData();
							gData.setGroup(group);
							gData.setData(dataList);
							groupData.add(gData);
						}			
					}					
				}else{
					List<List<PrintCell>> dataList=new ArrayList<List<PrintCell>>();			
					for (int j = 0; j < dataObj.size(); j++) {	
						List<PrintCell> data=new ArrayList<PrintCell>();	
						Object[] objects=dataObj.get(j);
						for (int i = groupList.size() ; i < objects.length; i++) {
							cell = new PrintCell();
							cell.setVal(StringUtil.null2empty(objects[i]));
							cell.setRow(rowIndex[i-groupList.size()]);
							cell.setCol(colIndex[i-groupList.size()]);
							data.add(cell);
						}
						dataList.add(data);
					}			
					GroupData gData=new GroupData();
					gData.setData(dataList);
					groupData.add(gData);							
				}
			}
		} catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException(e.getMessage());
	    }
		return groupData;		
	}
	
}

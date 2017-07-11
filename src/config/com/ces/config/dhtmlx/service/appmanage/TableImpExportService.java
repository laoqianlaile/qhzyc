package com.ces.config.dhtmlx.service.appmanage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.AppExportDao;
import com.ces.config.dhtmlx.dao.appmanage.ColumnDefineDao;
import com.ces.config.dhtmlx.dao.appmanage.PhysicalTableDefineDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.appmanage.AppExport;
import com.ces.config.dhtmlx.entity.appmanage.ColumnDefine;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.entity.label.ColumnLabel;
import com.ces.config.dhtmlx.service.label.ColumnLabelService;
import com.ces.config.utils.CodeUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.TableUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.service.StringIDService;

@Component
public class TableImpExportService extends StringIDService<StringIDEntity> {
    
    private static Log log = LogFactory.getLog(TableImpExportService.class);
	
	private static int tableRowNo = 1;		//初始化表sheet行数
	private static int columnRowNo = 1;		//初始化字段sheet行数
	
    private static String showNames = "";	//记录表下的显示字段名称
    private static String columnNames = "";	//记录表下的字段名称
	/**
     * wangmi 2013-12-17 
     * <p>导入表到数据库</p>
     * @param  
     * @return 
     */
    @Transactional
    public String impTableExcelService(String classification,  String tableTreeId, String filePath) throws Exception {
    	
    	String tableMessage = "";
    	//1.解析EXCEL 2003
    	Workbook work = null;
    	File file = new File(filePath);
        FileInputStream is = new FileInputStream(file);  
        POIFSFileSystem fs = new POIFSFileSystem(is);     
        work = new HSSFWorkbook(fs); 
        
        //查询分类下的所有表信息
        List<PhysicalTableDefine> tableList = getDao(PhysicalTableDefineDao.class, PhysicalTableDefine.class).getByClassification(classification);
        String tableNames = "";
    	String names = "";
    	for (PhysicalTableDefine tList : tableList) {
    		tableNames += tList.getTableName().toUpperCase()+",";
    		names += tList.getShowName()+",";
		}
    	
    	Map<String, String> tableMap = new HashMap<String, String>();
    	
        //2.获取Excel内容
    	
    	//获取字段sheet
    	Sheet sheet = work.getSheetAt(0);
		//获取到Excel文件中的所有行数  
		int rows = sheet.getPhysicalNumberOfRows();
		 for (int r = 1; r < rows; r++) {
			// 定义 row  
         	String value = "";
            Row row = sheet.getRow(r);
            if (null != row) {
            	//获取到Excel文件中的所有的列 
                int cells = row.getPhysicalNumberOfCells();
                for (short c = 0; c < cells; c++) { 
                	//获取到列的值
                	value +=  getCellValue(row.getCell(c))+",";//每列的值集合列以”,“号隔开
                    //System.out.print(getCellValue(row.getCell(c))+"                ");  
                }
                String[] valueArray = value.split(",");
                if(!valueArray[0].equals("null")){
            		//导入表到数据库
            		tableMap = impTableExcel(valueArray,tableNames,names,classification,tableTreeId);
            		if(!tableMap.get("tableName").equals("")){
            			tableMessage += tableMap.get("tableName")+";";
            		}else{
            		    showNames = "";	
            		    columnNames = "";	
	            		//获取字段sheet
	            		Sheet Colulmnsheet = work.getSheetAt(1);
	            		for (int cr = 1; cr < Colulmnsheet.getPhysicalNumberOfRows(); cr++) {
	            			Row crow = Colulmnsheet.getRow(cr);
	            			if (null != crow) {
	            				String Columnvalues = "";
	            				//int Columncells = crow.getPhysicalNumberOfCells();
	            				int Columncells = 8;
	            				for (short cc = 0; cc < Columncells; cc++) { 
	                            	//获取到列的值
	            					Columnvalues +=  getCellValue(crow.getCell(cc))+",";//每列的值集合列以”,“号隔开
	                                //System.out.print(getCellValue(crow.getCell(cc))+"                ");  
	                            }
	            				String[] ColumnValueArray = Columnvalues.split(",");
	            				//判断当前字段的第一列不为空且当前表的是否对应
	            				if(!ColumnValueArray[0].equals("null") && ColumnValueArray[0].toUpperCase().equals(valueArray[1].toUpperCase())){
	            					//导入表到数据库
	                        		String columnMessage = impColumnExcel(ColumnValueArray,tableMap);
	                        		
	                        		if(!columnMessage.equals("")){
	                        			tableMessage = columnMessage;
	                        			//excel导入失败  删除成功的导入的部分
	                        			getService(PhysicalTableDefineService.class).delete(tableMap.get(valueArray[1]));
	                        			return tableMessage;
	                        		}
	            				}
	            			}
	            		}
            		}
                }
            }
		 }
    	
    	return tableMessage;
    }
    
    
    /**
     * wangmi 2013-12-19 
     * <p>导入表到数据库</p>
     * @param  
     * @return 
     */
    private Map<String, String> impTableExcel(String[] valueArray, String tableNames, String names, String classification,  String tableTreeId){
    	Map<String, String> ecxelTableMap = new HashMap<String, String>();
    	try {
    		if(!valueArray[0].equals("null")){
           	 	//验证表的唯一性
	   			if (tableNames.indexOf(valueArray[1].toUpperCase()) ==-1 && names.indexOf(valueArray[0].toUpperCase()) ==-1
	           			&& StringUtil.isNotEmpty(valueArray[0])) {
	   				
	   				if(!"null".equals(valueArray[0])){
	   					PhysicalTableDefine newTable  = new PhysicalTableDefine();
	               		newTable.setShowName(valueArray[0]);
	               		newTable.setTableTreeId(tableTreeId);
	               		newTable.setTableCode(valueArray[1]);
	               		newTable.setTablePrefix(valueArray[3]);
	               		newTable.setClassification(classification);
	               		newTable.setTableType("0");
	               		newTable.setCreated("0");
	               		getService(PhysicalTableDefineService.class).save(newTable);
	               		ecxelTableMap.put(valueArray[1], newTable.getId());
	               		ecxelTableMap.put("tableName", "");
	               	}
	   			}else{
	   				ecxelTableMap.put("tableName", valueArray[0]);
	   				return ecxelTableMap;
	   			}
    		}
		} catch (Exception e) {
			log.error("导入表出错！", e);  
		}
    	
    	return ecxelTableMap;
    }
    
    
    /**
     * wangmi 2013-12-19 
     * <p>导入字段到数据库  </p>
     * @param  
     * @return 
     */
    
    public String impColumnExcel(String[] valueArray,Map<String, String> tableMap){
    	Map<String, String> columnLabelMap = getColumnLabel(false);
        String columnMessage = "";
    	//判断表下显示名称是否有重复
    	if(showNames.indexOf(valueArray[1])!= -1){
    		columnMessage = "导入的EXCEL中 ：表   "+valueArray[0]+", 字段显示名称: ' "+valueArray[1]+" ' 字段已存在，请修改!";
    		return columnMessage;
    	}
    	//判断表下字段名称是否有重复
    	if(columnNames.indexOf(valueArray[2])!=-1){
    		columnMessage = "导入的EXCEL中 ：表   "+valueArray[0]+", 字段英文名称: ' "+valueArray[2]+" ' 字段已存在，请修改!";
    		return columnMessage;
    	}
    	
    	try {
    		if(!valueArray[0].equals("null")){
            	ColumnDefine newColumnDefine = new ColumnDefine();
            	newColumnDefine.setTableId(tableMap.get(valueArray[0]));
            	newColumnDefine.setShowName(valueArray[1]);
            	newColumnDefine.setColumnName(valueArray[2]);
            	
            	if(!valueArray[3].equals("null")){
            		newColumnDefine.setDataType(getDataTypeName(valueArray[3]));
            	}else{
            		columnMessage = "导入的EXCEL中 ：表   "+valueArray[0]+",  ' "+valueArray[1]+" ' 字段，数据类型编码不能为空,请设值!";
            		return columnMessage;
            	}
            	
            	if(!"null".equals(valueArray[4])){
            		newColumnDefine.setCodeTypeCode(valueArray[4]);
            	}
            	
            	if(!"null".equals(valueArray[5])){
            		newColumnDefine.setLength(Integer.valueOf(valueArray[5]));
            	}else{
            		columnMessage =  "导入的EXCEL中 ：表  "+valueArray[0]+",  ' "+valueArray[1]+" ' 字段，数据类型的长度不能为空,请设值!";
            		return columnMessage;
            	}
            	
            	newColumnDefine.setColumnType("0");
            	newColumnDefine.setInputable("1");
            	newColumnDefine.setUpdateable("1");
            	newColumnDefine.setSearchable("0");
            	newColumnDefine.setListable("1");
            	newColumnDefine.setSearchable("0");
            	newColumnDefine.setAlign("left");
            	newColumnDefine.setFilterType("LIKE");
            	newColumnDefine.setWidth(80);
            	if(valueArray.length > 6){
                	if(!"null".equals(valueArray[6])){
                		newColumnDefine.setDefaultValue(valueArray[6]);
                	}
            	}
            	if(valueArray.length > 7){
                    if(!"null".equals(valueArray[7])){
                        newColumnDefine.setColumnLabel(columnLabelMap.get(valueArray[7]));
                    }
                }
            	newColumnDefine.setRemark("该字段来源于Excel导入!");
            	newColumnDefine.setCreated("0");
            	getService(ColumnDefineService.class).save(newColumnDefine);
            	//记录表的显示字段名称和字段名称
            	showNames += valueArray[1] + ",";
            	columnNames += valueArray[2] + ",";
        	}
		} catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();  
		}
    	
    	return columnMessage;
    }
    
    
    
	/**
     * wangmi 2013-12-17 
     * <p>判断从Excel文件中解析出来数据的格式  </p>
     * @param  
     * @return 
     */
    private static String getCellValue(Cell cell){  
        String value = null;  
        if (null != cell) {  
	        //简单的查检列类型  
	        switch(cell.getCellType()){  
	            case Cell.CELL_TYPE_STRING://字符串  
	                value = cell.getRichStringCellValue().getString();  
	                break;  
	            case Cell.CELL_TYPE_NUMERIC://数字  
	                long dd = (long)cell.getNumericCellValue();  
	                value = dd+"";  
	                break;  
	            case Cell.CELL_TYPE_BLANK:  // 空值
	                value = "";  
	                break;     
	            case Cell.CELL_TYPE_FORMULA:   // 公式型
	                value = String.valueOf(cell.getCellFormula());  
	                break;  
	            case Cell.CELL_TYPE_BOOLEAN://boolean型值  
	                value = String.valueOf(cell.getBooleanCellValue());  
	                break;  
	            case Cell.CELL_TYPE_ERROR:  // 故障
	                value = String.valueOf(cell.getErrorCellValue());  
	                break;  
	            default:  
	                break;  
	        } 
        }
        return value;  
    }
    
	/**
     * wangmi 2013-12-18 
     * <p>导出表到excel  </p>
     * @param  
     * @return 
     */
    @Transactional
    public String exportTableExcelService(String ids) {
    	tableRowNo=1;
        columnRowNo=1;
        HttpServletRequest request = ServletActionContext.getRequest();
    	try {  
	 		 	//获取excel表临时文件路径
    			String filePath = ServletActionContext.getServletContext().getRealPath("WEB-INF/template/TABLE.xls");
	 		 	FileInputStream is = new FileInputStream(filePath);
	            HSSFWorkbook workbook = new HSSFWorkbook(is);
	            
	            for (int k = 0; k < workbook.getNumberOfSheets(); k++) {  
	            	//获取sheet
	            	Sheet sheet = workbook.getSheetAt(k); 
	                //获取到Excel文件中的所有行数  
	                int rows = sheet.getPhysicalNumberOfRows();  
	                for (int r = 1; r < rows; r++) {
	                	HSSFRow row = (HSSFRow) sheet.getRow(r);  
			            sheet.removeRow(row);
	                }
	            }
            	//判断是否选中个表
                if(ids.indexOf(",") !=-1){
    	    		String[] idsArray = ids.split(",");
    	    		for(int i = 0; i <idsArray.length; i++){
    	    			String Id = idsArray[i];
    	    			writeExcelTable(Id,workbook);
    	    		}
                }else{
                	writeExcelTable(ids,workbook);
                } 
	            //系统临时路径
	            String tempPath= System.getProperty("java.io.tmpdir") + File.separator + "TABLE.xls";
	            String newFilePath = tempPath.replace("\\", "/");
	            FileOutputStream os = new FileOutputStream(newFilePath);  
	            workbook.write(os);  
	            is.close();  
	            os.close(); 
	           
	            //下载导出的表内容
		        String newFileName = "";
		        try {
		            if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
		            	newFileName = new String("物理表".getBytes("UTF-8"), "ISO8859-1");
		            } else {
		            	newFileName = java.net.URLEncoder.encode("物理表", "UTF-8");
		            }
		        } catch (UnsupportedEncodingException e) {
		            e.printStackTrace();
		        }
	            HttpServletResponse response = ServletActionContext.getResponse();
	            response.reset();
	            response.setContentType("application/vnd.ms-excel");   //下载文版类型
	            response.setHeader("Content-Disposition", "attachment;filename="+newFileName+ ".xls");
	            FileInputStream fis = null;
	            BufferedInputStream bis = null;
	            OutputStream oStream = null;
	            try {
	                fis = new FileInputStream(newFilePath);
	                bis = new BufferedInputStream(fis);
	                oStream = response.getOutputStream();
	                byte[] b = new byte[1024];
	                int len;
	                while ((len = bis.read(b, 0, 1024)) > 0) {
	                    oStream.write(b, 0, len);
	                }
	                oStream.flush();
	            } catch (FileNotFoundException e) {
	                e.printStackTrace();
	            } catch (IOException e) {
	                e.printStackTrace();
	            } finally {
	                if (bis != null) {
	                    try {
	                        bis.close();
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                }
	                if (fis != null) {
	                    try {
	                        fis.close();
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                }
	                if (oStream != null) {
	                    try {
	                        oStream.close();
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                }
	            }
	            //删除文件
	            File file = new File(tempPath);
		        file.delete();
	            
	        } catch (Exception e) {   
	            e.printStackTrace();  
	        } 
    	
    	return null;
    }
    
    /**
     * wangmi 2013-12-18 
     * <p>导出表和字段信息到excel  </p>
     * @param  
     * @return 
     */
    public void writeExcelTable(String id,HSSFWorkbook workbook){
        Map<String, String> columnLabelMap = getColumnLabel(true);
    	Sheet sheet;
        HSSFRow row;
    	//1.导出表信息
    	List<PhysicalTableDefine> tabelList = getService(PhysicalTableDefineService.class).find("EQ_id=" + id);
    	String tName = "";
    	for(PhysicalTableDefine physicalTableDefine : tabelList){
    		tName = physicalTableDefine.getTableName();
    		
    		sheet = workbook.getSheetAt(0); 
        	row = (HSSFRow) sheet.createRow(tableRowNo);
    		row.createCell(0).setCellValue(physicalTableDefine.getShowName());
    		row.createCell(1).setCellValue(physicalTableDefine.getTableName());
    		tableRowNo = tableRowNo+1;
    	}
    	//2.导出字段信息
    	//获取该表下的所有字段信息
    	List<ColumnDefine> columnList = getDao(ColumnDefineDao.class, ColumnDefine.class).findByTableId(id);
		for(ColumnDefine columnDefine : columnList){
			sheet = workbook.getSheetAt(1); 
        	row = (HSSFRow) sheet.createRow(columnRowNo);
        	//ID不写入
        	if(!columnDefine.getColumnName().toUpperCase().equals("ID")){
	        	row.createCell(0).setCellValue(tName);
	        	row.createCell(1).setCellValue(columnDefine.getShowName());
	        	row.createCell(2).setCellValue(columnDefine.getColumnName());
	        	String dataType = columnDefine.getDataType();
	        	if(dataType != null){
	        		row.createCell(3).setCellValue(getDataType(dataType));
	        	}
	        	row.createCell(4).setCellValue(columnDefine.getCodeTypeCode());
	        	row.createCell(5).setCellValue(columnDefine.getLength());
	        	row.createCell(6).setCellValue(columnDefine.getDefaultValue());
	        	if (columnDefine.getColumnLabel() != null) {
	        	    row.createCell(7).setCellValue(columnLabelMap.get(columnDefine.getColumnLabel()));
	        	}
	        	columnRowNo = columnRowNo+1;
        	}
		}
    }
    
    /**
     * 获取字段标签Map
     * 
     * @param flag true:code为key，name为value； false：name为key，code为value
     * @return Map<String, String>
     */
    private Map<String, String> getColumnLabel(boolean flag) {
        List<ColumnLabel> columnLabelList = getService(ColumnLabelService.class).getAllColumnLabel();
        Map<String, String> columnLabelMap = new HashMap<String, String>();
        if (CollectionUtils.isNotEmpty(columnLabelList)) {
            for (ColumnLabel columnLabel : columnLabelList) {
                if (flag) {
                    columnLabelMap.put(columnLabel.getCode(), columnLabel.getName());
                } else {
                    columnLabelMap.put(columnLabel.getName(), columnLabel.getCode());
                }
            }
        }
        return columnLabelMap;
    }
    
	/**
	 * wangmi 2013-12-24 
	 * <p>标题: getDataType</p>
	 * <p>描述: 获取字段数据类型</p>
	 * @param  classification
	 * @return String    返回类型   
	 * @throws
	 */
	public static String getDataType(String dataType) {
	    
	    if((ConstantVar.DataType.PART).toLowerCase().equals(dataType.toLowerCase())){
	    	return "部门型";
	    }else if((ConstantVar.DataType.USER).toLowerCase().equals(dataType.toLowerCase())){
	    	return "用户型";
	    }else if((ConstantVar.DataType.ENUM).toLowerCase().equals(dataType.toLowerCase())){
	    	return "编码型";
	    }else if((ConstantVar.DataType.NUMBER).toLowerCase().equals(dataType.toLowerCase())){
	    	return "数字型";
	    }else if((ConstantVar.DataType.DATE).toLowerCase().equals(dataType.toLowerCase())){
	    	return "日期型";
	    }else if((ConstantVar.DataType.CHAR).toLowerCase().equals(dataType.toLowerCase())){
	    	return "字符型";
	    }
	    return null;
	}
	
	/**
	 * wangmi 2013-12-24 
	 * <p>标题: getDataType</p>
	 * <p>描述: 获取字段数据类型插到数据库  默认返回字符型</p>
	 * @param  classification
	 * @return String    返回类型   
	 * @throws
	 */
	public static String getDataTypeName(String dataTypeName) {
	    
	    if("部门型".equals(dataTypeName)){
	    	return (ConstantVar.DataType.PART).toLowerCase();
	    }else if("用户型".equals(dataTypeName)){
	    	return (ConstantVar.DataType.USER).toLowerCase();
	    }else if("编码型".equals(dataTypeName)){
	    	return (ConstantVar.DataType.ENUM).toLowerCase();
	    }else if("数字型".equals(dataTypeName)){
	    	return (ConstantVar.DataType.NUMBER).toLowerCase();
	    }else if("日期型".equals(dataTypeName)){
	    	return (ConstantVar.DataType.DATE).toLowerCase();
	    }else if("字符型".equals(dataTypeName)){
	    	return (ConstantVar.DataType.CHAR).toLowerCase();
	    }
	    return (ConstantVar.DataType.CHAR).toLowerCase();
	}
	
	/**
     * qiujinwei 2015-03-09 
     * <p>导出表的指定列信息到excel  </p>
     * @param  
     * @return 
     */
    @Transactional
    public String exportTableExcelService(String tableId, String componentVersionId, String menuId, String ids, String filter) {
    	tableRowNo=0;
        HttpServletRequest request = ServletActionContext.getRequest();
    	try {  
	 		 	//获取excel表临时文件路径
    			String filePath = ServletActionContext.getServletContext().getRealPath("WEB-INF/template/temp.xls");
	 		 	FileInputStream is = new FileInputStream(filePath);
	            HSSFWorkbook workbook = new HSSFWorkbook(is);
	            
	            for (int k = 0; k < workbook.getNumberOfSheets(); k++) {  
	            	//获取sheet
	            	Sheet sheet = workbook.getSheetAt(k); 
	                //获取到Excel文件中的所有行数  
	                int rows = sheet.getPhysicalNumberOfRows();  
	                for (int r = 1; r < rows; r++) {
	                	HSSFRow row = (HSSFRow) sheet.getRow(r);  
			            sheet.removeRow(row);
	                }
	            }
                writeExcelColumn(tableId, componentVersionId, menuId, ids, filter, workbook);
	            //系统临时路径
	            String tempPath= System.getProperty("java.io.tmpdir") + File.separator + "TABLE.xls";
	            String newFilePath = tempPath.replace("\\", "/");
	            FileOutputStream os = new FileOutputStream(newFilePath);  
	            workbook.write(os);  
	            is.close();  
	            os.close(); 
	           
	            //下载导出的表内容
		        String newFileName = "exportTable";
//		        try {
//		            if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
//		            	newFileName = new String("导出表".getBytes("UTF-8"), "ISO8859-1");
//		            } else {
//		            	newFileName = java.net.URLEncoder.encode("导出表", "UTF-8");
//		            }
//		        } catch (UnsupportedEncodingException e) {
//		            e.printStackTrace();
//		        }
	            HttpServletResponse response = ServletActionContext.getResponse();
	            response.reset();
	            response.setContentType("application/vnd.ms-excel");   //下载文版类型
	            response.setHeader("Content-Disposition", "attachment;filename="+newFileName+ ".xls");
	            FileInputStream fis = null;
	            BufferedInputStream bis = null;
	            OutputStream oStream = null;
	            try {
	                fis = new FileInputStream(newFilePath);
	                bis = new BufferedInputStream(fis);
	                oStream = response.getOutputStream();
	                byte[] b = new byte[1024];
	                int len;
	                while ((len = bis.read(b, 0, 1024)) > 0) {
	                    oStream.write(b, 0, len);
	                }
	                oStream.flush();
	            } catch (FileNotFoundException e) {
	                e.printStackTrace();
	            } catch (IOException e) {
	                e.printStackTrace();
	            } finally {
	                if (bis != null) {
	                    try {
	                        bis.close();
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                }
	                if (fis != null) {
	                    try {
	                        fis.close();
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                }
	                if (oStream != null) {
	                    try {
	                        oStream.close();
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                }
	            }
	            //删除文件
	            File file = new File(tempPath);
		        file.delete();
	            
	        } catch (Exception e) {   
	            e.printStackTrace();  
	        } 
    	
    	return null;
    }
    
    /**
     * wangmi 2013-12-18 
     * <p>导出表和字段信息到excel  </p>
     * @param  
     * @return 
     */
    public void writeExcelColumn(String tableId, String componentVersionId, String menuId, String ids, String filter, HSSFWorkbook workbook){
    	Sheet sheet = workbook.getSheetAt(0);
        HSSFRow row;
    	//1.导出列信息
        List<Object[]> rlt = getDao(AppExportDao.class, AppExport.class).getDefineColumn(tableId, componentVersionId, menuId);//t_ae.id, t_ae.column_id, t_cd.show_name, t_cd.column_name
        String tableName = TableUtil.getTableName(tableId);
        StringBuffer cols = new StringBuffer();
        StringBuffer colNames = new StringBuffer();
        StringBuffer columnIds = new StringBuffer();
        for (Object[] oArr : rlt) {
			cols.append("," + oArr[3]);
			colNames.append("," + oArr[2]);
			columnIds.append("," + oArr[1]);
		}
        StringBuffer sql = new StringBuffer("select " + cols.toString().substring(1) + " from " + tableName + " where 1=1 ");
        if (StringUtil.isNotEmpty(ids)) {
			sql.append(" and id in('" + ids.replace(",", "','") + "')");
		}
        if (StringUtil.isNotEmpty(filter)) {
			sql.append(" and " + filter);
		}
        List<Object[]> listArr = null;
        List<Object> list = null;
        String[] columnId = columnIds.toString().substring(1).split(",");
        String[] colName = colNames.toString().substring(1).split(",");
        if (colName.length == 1) {
        	list = DatabaseHandlerDao.getInstance().queryForList(sql.toString());
		} else {
			listArr = DatabaseHandlerDao.getInstance().queryForList(sql.toString());
		}
    	row = (HSSFRow) sheet.createRow(tableRowNo);
        for (int i = 0; i < colName.length; i++) {
    		row.createCell(i).setCellValue(colName[i]);
    		ColumnDefine entity = getService(ColumnDefineService.class).getByID(columnId[i]);
    		if (StringUtil.isNotEmpty(entity.getCodeTypeCode())) {
    			List<Code> codeList = CodeUtil.getInstance().getCodeList(entity.getCodeTypeCode());
    			if (codeList != null && codeList.get(0) != null) {
    				if (colName.length == 1) {
    					for (int j = 0; j < list.size(); j++) {
    						if (StringUtil.isNotEmpty(list.get(j)) && list.get(j).toString().contains(",")) {
    							StringBuffer value = new StringBuffer();
    							String[] items = list.get(j).toString().split(",");
    							for (int k = 0; k < items.length; k++) {
    								for (Code code : codeList) {
    	        						if (code.getValue().equals(StringUtil.null2empty(items[k]))) {
    	        							value.append("," + code.getName());
    	        							break;
    	    							}
    	        					}
								}
    							list.remove(j);
    							list.add(j, value.substring(1).toString());
							} else {
								for (Code code : codeList) {
	        						if (code.getValue().equals(StringUtil.null2empty(list.get(j)))) {
	        							list.remove(j);
	        							list.add(j, code.getName());
	        							break;
	    							}
	        					}
							}
						}
        			} else {
        				for (int j = 0; j < listArr.size(); j++) {
        					if (StringUtil.isNotEmpty(listArr.get(j)[i]) && listArr.get(j)[i].toString().contains(",")) {
    							StringBuffer value = new StringBuffer();
    							String[] items = listArr.get(j)[i].toString().split(",");
    							for (int k = 0; k < items.length; k++) {
    								for (Code code : codeList) {
    	        						if (code.getValue().equals(StringUtil.null2empty(items[k]))) {
    	        							value.append("," + code.getName());
    	        							break;
    	    							}
    	        					}
								}
    							listArr.get(j)[i] = value.substring(1).toString();
							} else {
								for (Code code : codeList) {
	        						if (code.getValue().equals(StringUtil.null2empty(listArr.get(j)[i]))) {
	        							listArr.get(j)[i] = code.getName();
	        							break;
	    							}
	        					}
							}
						}
        			}
				}
			}
		}
        tableRowNo = tableRowNo+1;
        if (CollectionUtils.isNotEmpty(list) || CollectionUtils.isNotEmpty(listArr)) {
        	if (colName.length == 1) {
        		for (Object rowValue : list) {
                	row = (HSSFRow) sheet.createRow(tableRowNo);
                    row.createCell(0).setCellValue(StringUtil.null2empty(rowValue));
                	tableRowNo = tableRowNo+1;
        		}
			} else {
	            for (Object[] rowValue : listArr) {
	            	row = (HSSFRow) sheet.createRow(tableRowNo);
	                for (int i = 0; i < colName.length; i++) {
	                	row.createCell(i).setCellValue(StringUtil.null2empty(rowValue[i]));
	        		}
	            	tableRowNo = tableRowNo+1;
	    		}
			}
		}
    }

}


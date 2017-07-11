
package com.ces.config.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

/**
 * <p>描述:Excel工具类</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @date 2014-4-22 
 */

public class ExcelUtil {

	public static Workbook getWorkbook(File file,String fileName) throws IOException, InvalidFormatException {
		return getWorkbook(new FileInputStream(file), fileName);
	}

	public static Workbook getWorkbook(File file) throws IOException, InvalidFormatException {
		return getWorkbook(new FileInputStream(file), file.getName());
	}
	
	public static Workbook getWorkbook(InputStream stream, String fileName) throws IOException, InvalidFormatException {
		Workbook excel;
		if (fileName == null || !"".equals(fileName)) {
			throw new IllegalArgumentException("filename is empty");
		} else if (fileName.toLowerCase().endsWith("xls")) {
			excel = new HSSFWorkbook(new POIFSFileSystem(stream));
		} else if (fileName.toLowerCase().endsWith("xlsx")) {
			excel = new XSSFWorkbook(stream);
		} else {
			throw new IllegalArgumentException("File:" + fileName + " is not excel");
		}
		return excel;
	}

	/**
	 * 取出sheet中row行col列的内容
	 */
	public static String getExcelContent(Sheet sheet, int row, int col) {
		return getContent(getCell(getRow(sheet, row), col));
	}

	public static String getExcelContent(Sheet sheet, int row, char col) {
		return getContent(getCell(getRow(sheet, row), col));
	}

	/**
	 * 取出sheet中row行前maxCol列的内容
	 */
	public static String[] getRow(Sheet sheet, int row, int maxCol) {
		String[] rowData = new String[maxCol];
		for (int col = 0; col < maxCol; ++col) {
			rowData[col] = getExcelContent(sheet, row, col).trim();
		}
		return rowData;
	}

	/**
	 * 取sheet行数
	 */
	public static int getRowCount(Sheet sheet) {
		return sheet.getLastRowNum();
	}

	/**
	 * 取cell内容
	 * 
	 * @return
	 */
	private static String getContent(Cell cell) {
		String content = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				content = formatter.format(cell.getDateCellValue());
			} else {
				BigDecimal big = new BigDecimal(cell.getNumericCellValue());
				content = big.toString();
			}
			break;
		case Cell.CELL_TYPE_STRING:
			content = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_FORMULA:
			content = cell.getCellFormula();
			break;
		case Cell.CELL_TYPE_BLANK:
			content = cell.toString();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			content = String.valueOf(cell.getBooleanCellValue());
			break;
		
		}
		return content;
	}

	/**
	 * 取出sheet第row行，不存在则创建
	 */
	public static Row getRow(Sheet sheet, int row) {
		if (sheet.getRow(row) == null) {
			sheet.createRow(row);
		}
		return sheet.getRow(row);
	}

	/**
	 * 取出sheet第row行第cell列单元格，不存在则创建
	 */
	public static Cell getCell(Row row, int col) {
		if (row.getCell(col) == null) {
			row.createCell(col);
		}
		return row.getCell(col);
	}

	public static Cell getCell(Row row, char col) {
		return getCell(row, col - 'A');
	}

	public static Cell getCell(Sheet sheet, int row, int col) {
		Row r = getRow(sheet, row);
		return getCell(r, col);
	}

	public static Cell getCell(Sheet sheet, int row, char col) {
		return getCell(sheet, row, col - 'A');
	}

	/**
	 * 取出workbook第sheet个表格，不存在则创建
	 */
	public static Sheet getSheet(Workbook excel, int index) {
		try {
			Sheet sheet = excel.getSheetAt(index);
			if (sheet == null) {
				return excel.createSheet("comment");
			}
		} catch (IllegalArgumentException e) {
		}
		return excel.getSheetAt(index);
	}

	public static void updateFormula(HSSFWorkbook wb) {
		Cell c = null;
		FormulaEvaluator eval = null;
		eval = new HSSFFormulaEvaluator(wb);
		Row r = null;
		Sheet s = null;
		for (int n = 0; n < wb.getNumberOfSheets() - 1; n++) {
			s = wb.getSheetAt(n);
			for (int j = s.getFirstRowNum(); j < s.getLastRowNum(); j++) {
				r = s.getRow(j);
				if (r == null) {
					continue;
				}
				for (int i = r.getFirstCellNum(); i < r.getLastCellNum(); i++) {
					c = r.getCell(i);
					if (c == null) {
						continue;
					}
					if (c.getCellType() == Cell.CELL_TYPE_FORMULA)
						eval.evaluateFormulaCell(c);
				}
			}
		}
	}
	
	public static HSSFCellStyle getExcellStyle(HSSFWorkbook wb){
	    //设置字体
	    HSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)12);
        //设置对齐方式
	    HSSFCellStyle style = wb.createCellStyle();       
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中对齐
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中对齐       
        style.setFillBackgroundColor(HSSFColor.BLACK.index);
        style.setFillForegroundColor(HSSFColor.BLACK.index); 
        //设置边框
        style.setBorderBottom((short)1);
        style.setBorderTop((short)1);
        style.setBorderLeft((short)1);
        style.setBorderRight((short)1);
        style.setFont(font);
        
        return style;
	}
}
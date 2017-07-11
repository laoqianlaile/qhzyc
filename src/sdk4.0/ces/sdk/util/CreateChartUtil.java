package ces.sdk.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class CreateChartUtil {


	/**
	 * 把PNG 图片写入到excel??
	 * 
	 * @param excelPath
	 * @param imagePath
	 * @throws Exception
	 */
	public void writeImg(String excelPath, String imagePath, int rows, int cells,String title) throws Exception {
		FileOutputStream fileOut = null;
		BufferedImage bufferImg = null;
		try {
			// 先把读进来的图片放到??个ByteArrayOutputStream中，以便产生ByteArray
			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			bufferImg = ImageIO.read(new File(imagePath));
			ImageIO.write(bufferImg, "jpg", byteArrayOut);

			// 创建??个工作薄
			HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(excelPath)));
			HSSFSheet sheet1 = wb.getSheetAt(0);
			HSSFRow row = sheet1.createRow(1);
			row.createCell(1);
			row.createCell(2);
			row.createCell(3);
			row.createCell(4);
			row.createCell(5);
			row.createCell(6);
			row.createCell(7);
			Region region = new Region(1, (short) 1, 1, (short) 7);
			sheet1.addMergedRegion(region);
			HSSFCellStyle setBorder = wb.createCellStyle();
			setBorder.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
			HSSFFont font = wb.createFont();
			font.setFontName("黑体");
			font.setFontHeightInPoints((short) 13);//设置字体大小
			setBorder.setFont(font);
			row.getCell(1).setCellStyle(setBorder);
			row.getCell(1).setCellValue(title);
			HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
			HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 512, 255,
					(short) 0, 4, (short) rows, cells);
			// 插入图片
			patriarch.createPicture(anchor, wb.addPicture(
					byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));

			fileOut = new FileOutputStream(excelPath);
			// 写入excel文件
			wb.write(fileOut);
			fileOut.close();

		} catch (IOException io) {
			io.printStackTrace();
			System.out.println("io erorr :  " + io.getMessage());
		} finally {
			if (fileOut != null) {

				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void writeTable(String excelPath, List<String[]> datas) throws Exception {
		OutputStream os = null;
		try {
			// 创建??个工作薄
			HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(excelPath)));
			HSSFCellStyle setBorder = wb.createCellStyle();
			setBorder.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边??
			setBorder.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边??
			setBorder.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边??
			setBorder.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边??
			setBorder.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
			HSSFFont font = wb.createFont();
			font.setFontName("黑体");
			font.setFontHeightInPoints((short) 10);//设置字体大小
			setBorder.setFont(font);
			HSSFSheet sheet = wb.getSheetAt(0);
			Region region = null;
			for (int i = 0; i < datas.size(); i++) {
				String[] cells = datas.get(i);
				int m = 0;
				for (int j = 0; j < cells.length; j++) {
					region = new Region(30+i, (short) m, 30+i, (short) (m+1));
					sheet.addMergedRegion(region);
					m = m +2;
				}
			}
			
			for (int i = 0; i < datas.size(); i++) {
				HSSFRow row = sheet.createRow(30+i);
				String[] cells = datas.get(i);
				for (int j = 0; j < cells.length; j++) {
					HSSFCell cell1 = row.createCell(j*2+1);
					cell1.setCellStyle(setBorder);
					HSSFCell cell = row.createCell(j*2);
					cell.setCellStyle(setBorder);
					cell.setCellValue(cells[j]);
				}
			}
			os = new FileOutputStream(new File(excelPath));
			wb.write(os);

		} catch (IOException io) {
			io.printStackTrace();
			System.out.println("io erorr :  " + io.getMessage());
		} finally {
			os.close();
		}
	}
	
	public static void writeTable_(OutputStream os, List<String[]> datas) throws Exception {
		try {
			// 创建??个工作薄
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFCellStyle setBorder = wb.createCellStyle();
			setBorder.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边??
			setBorder.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边??
			setBorder.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边??
			setBorder.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边??
			setBorder.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
			HSSFFont font = wb.createFont();
			font.setFontName("黑体");
			font.setFontHeightInPoints((short) 10);//设置字体大小
			setBorder.setFont(font);
			HSSFSheet sheet = wb.createSheet();
//			sheet.createFreezePane(1, 3);// 冻结  
			
			for (int i = 0; i < datas.size(); i++) {
				HSSFRow row = sheet.createRow(i);
				String[] cells = datas.get(i);
				for (int j = 0; j < cells.length; j++) {
					HSSFCell cell = row.createCell(j);
					cell.setCellStyle(setBorder);
					cell.setCellValue(cells[j]);
				}
				HSSFCell cell = row.createCell(cells.length);
				cell.setCellStyle(setBorder);
			}
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
			sheet.autoSizeColumn(8);
			sheet.autoSizeColumn(9);
			//sheet.autoSizeColumn(3);
			wb.write(os);

		} catch (IOException io) {
			io.printStackTrace();
			System.out.println("io erorr :  " + io.getMessage());
		} finally {
			os.close();
		}
	}
	/**
	 * 
	 * @param excelPath
	 * @param isQueryDocSize
	 * @param head
	 * @param tabledata
	 * @param yTitle
	 * @param ySize
	 * @param flag
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public void writeTable(String excelPath, boolean isQueryDocSize,String[] head, String[][] tabledata,
			List<String[]> yTitle, int ySize, String flag) throws Exception {
		OutputStream os = null;
		try {
			// 创建??个工作薄
			HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(excelPath)));
			HSSFCellStyle setBorder = wb.createCellStyle();
			setBorder.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边??
			setBorder.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边??
			setBorder.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边??
			setBorder.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边??
			setBorder.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
			setBorder.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中 
			HSSFFont font = wb.createFont();
			font.setFontName("黑体");
			font.setFontHeightInPoints((short) 10);//设置字体大小
			setBorder.setFont(font);
			HSSFSheet sheet = wb.getSheetAt(0);
			int c1 = ySize==0?0:ySize-1;
			Region region = null;
			//设置单元格的样式 ????
			int rl = 0;
			int cl = 0;
			if("2".equals(flag) || "3".equals(flag)){
				rl += 2;
			}else{
				rl += 1;
			}
			rl += 1;
			if(ySize == 0){
				
			}else if(ySize == 1){
				String[] index0 = yTitle.get(0);
				rl += index0.length;
			}else if(ySize == 2){
				String[] index0 = yTitle.get(0);
				String[] index1 = yTitle.get(1);
				rl += index0.length*index1.length;
			}else if(ySize == 3){
				String[] index0 = yTitle.get(0);
				String[] index1 = yTitle.get(1);
				String[] index2 = yTitle.get(2);
				rl += index0.length*index1.length*index2.length;
			}else{
				String[] index0 = yTitle.get(0);
				String[] index1 = yTitle.get(1);
				String[] index2 = yTitle.get(2);
				String[] index3 = yTitle.get(3);
				rl += index0.length*index1.length*index2.length*index3.length;
			}
			if(ySize == 0){
				cl += 1;
			}else{
				cl += ySize;
			}
			
			if("2".equals(flag)){
				if(isQueryDocSize){
					cl += head.length*3;
				}else{
					cl += head.length*2;
				}
			}else if("3".equals(flag)){
				if(isQueryDocSize){
					cl += head.length*4;
				}else{
					cl += head.length*3;
				}
			}else{
				cl += head.length;
			}
			rl += 30;
			for (int i = 30; i < rl; i++) {
				HSSFRow row = sheet.createRow(i);
				for (int j = 0; j < cl; j++) {
					if(i == 30){
						sheet.setColumnWidth(j, 3000);
					}
					HSSFCell cell = row.createCell(j);
					cell.setCellStyle(setBorder);
				}
			}
			//设置单元格的样式 结束
			//合并单元?? 以及处理表头数据  ????
			if("2".equals(flag) || "3".equals(flag)){
				region = new Region(30, (short) 0, 31, (short) c1);
				sheet.addMergedRegion(region);
			}else{
				region = new Region(30, (short) 0, 30, (short) c1);
				sheet.addMergedRegion(region);
			}
			
			int n = c1;
			for (int i = 0; i < head.length; i++) {
				if(isQueryDocSize){
					if("2".equals(flag)){
						region = new Region(30, (short) (n+1), 30, (short) (n+3));
						sheet.addMergedRegion(region);
						n = n+3;
					}else if("3".equals(flag)){
						region = new Region(30, (short) (n+1), 30, (short) (n+4));
						sheet.addMergedRegion(region);
						n = n+4;
					}
				}else{
					if("2".equals(flag)){
						region = new Region(30, (short) (n+1), 30, (short) (n+2));
						sheet.addMergedRegion(region);
						n = n+2;
					}else if("3".equals(flag)){
						region = new Region(30, (short) (n+1), 30, (short) (n+3));
						sheet.addMergedRegion(region);
						n = n+3;
					}
				}
			}
			int m = 0;
			int m2 = 0;
			if("2".equals(flag) || "3".equals(flag)){
				n = 32;
				m = 32;
				m2 = 32;
			}else{
				n = 31;
				m = 31;
				m2 = 31;
			}
			int rowStart = 0;
			int cellStart = 0;
			if(ySize == 0){
				if("1".equals(flag)){
				}else if("2".equals(flag)){
					if(isQueryDocSize){
						for (int i = 0; i < head.length; i++) {
							sheet.getRow(30).getCell(i*3+1).setCellValue(head[i]);
							sheet.getRow(31).getCell(i*3+1).setCellValue("文件");
							sheet.getRow(31).getCell(i*3+2).setCellValue("电子全文");
							sheet.getRow(31).getCell(i*3+3).setCellValue("电子全文大小");
						}
					}else{
						for (int i = 0; i < head.length; i++) {
							sheet.getRow(30).getCell(i*2+1).setCellValue(head[i]);
							sheet.getRow(31).getCell(i*2+1).setCellValue("文件");
							sheet.getRow(31).getCell(i*2+2).setCellValue("电子全文");
						}
					}
					sheet.getRow(32).getCell(0).setCellValue("总计");
					rowStart = 32;
					cellStart = 1;
				}else{
					if(isQueryDocSize){
						for (int i = 0; i < head.length; i++) {
							sheet.getRow(30).getCell(i*4+1).setCellValue(head[i]);
							sheet.getRow(31).getCell(i*4+1).setCellValue("案卷");
							sheet.getRow(31).getCell(i*4+2).setCellValue("文件");
							sheet.getRow(31).getCell(i*4+3).setCellValue("电子全文");
							sheet.getRow(31).getCell(i*4+4).setCellValue("电子全文大小");
						}
					}else{
						for (int i = 0; i < head.length; i++) {
							sheet.getRow(30).getCell(i*3+1).setCellValue(head[i]);
							sheet.getRow(31).getCell(i*3+1).setCellValue("案卷");
							sheet.getRow(31).getCell(i*3+2).setCellValue("文件");
							sheet.getRow(31).getCell(i*3+3).setCellValue("电子全文");
						}
					}
					sheet.getRow(32).getCell(0).setCellValue("总计");
					rowStart = 32;
					cellStart = 1;
				}
			}else if(ySize == 1){
				if("1".equals(flag)){
					for (int i = 0; i < head.length; i++) {
						sheet.getRow(30).getCell(i+1).setCellValue(head[i]);
					}
					String[] index0 = yTitle.get(0);
					for (int i = 0; i < index0.length; i++) {
						sheet.getRow(31+i).getCell(0).setCellValue(index0[i]);
					}
					sheet.getRow(31+index0.length).getCell(0).setCellValue("总计");
					rowStart = 31;
					cellStart = 1;
				}else if("2".equals(flag)){
					if(isQueryDocSize){
						for (int i = 0; i < head.length; i++) {
							sheet.getRow(30).getCell(i*3+1).setCellValue(head[i]);
							sheet.getRow(31).getCell(i*3+1).setCellValue("文件");
							sheet.getRow(31).getCell(i*3+2).setCellValue("电子全文");
							sheet.getRow(31).getCell(i*3+3).setCellValue("电子全文大小");
						}
					}else{
						for (int i = 0; i < head.length; i++) {
							sheet.getRow(30).getCell(i*2+1).setCellValue(head[i]);
							sheet.getRow(31).getCell(i*2+1).setCellValue("文件");
							sheet.getRow(31).getCell(i*2+2).setCellValue("电子全文");
						}
					}
					String[] index0 = yTitle.get(0);
					for (int i = 0; i < index0.length; i++) {
						sheet.getRow(32+i).getCell(0).setCellValue(index0[i]);
					}
					sheet.getRow(32+index0.length).getCell(0).setCellValue("总计");
					rowStart = 32;
					cellStart = 1;
				}else{
					if(isQueryDocSize){
						for (int i = 0; i < head.length; i++) {
							sheet.getRow(30).getCell(i*4+1).setCellValue(head[i]);
							sheet.getRow(31).getCell(i*4+1).setCellValue("案卷");
							sheet.getRow(31).getCell(i*4+2).setCellValue("文件");
							sheet.getRow(31).getCell(i*4+3).setCellValue("电子全文");
							sheet.getRow(31).getCell(i*4+4).setCellValue("电子全文大小");
						}
					}else{
						for (int i = 0; i < head.length; i++) {
							sheet.getRow(30).getCell(i*3+1).setCellValue(head[i]);
							sheet.getRow(31).getCell(i*3+1).setCellValue("案卷");
							sheet.getRow(31).getCell(i*3+2).setCellValue("文件");
							sheet.getRow(31).getCell(i*3+3).setCellValue("电子全文");
						}
					}
					String[] index0 = yTitle.get(0);
					for (int i = 0; i < index0.length; i++) {
						sheet.getRow(32+i).getCell(0).setCellValue(index0[i]);
					}
					sheet.getRow(32+index0.length).getCell(0).setCellValue("总计");
					rowStart = 32;
					cellStart = 1;
				}
			}else if(ySize == 2){
				String[] index0 = yTitle.get(0);
				String[] index1 = yTitle.get(1);
				for (int i = 0; i < index0.length; i++) {
					region = new Region(n, (short) 0, n+index1.length-1, (short) 0);
					sheet.addMergedRegion(region);
					n = n+index1.length;
				}
				region = new Region(n, (short) 0, n, (short) c1);
				sheet.addMergedRegion(region);
				//处理表头数据
				if("1".equals(flag)){
					for (int i = 0; i < head.length; i++) {
						sheet.getRow(30).getCell(i+ySize).setCellValue(head[i]);
					}
					for (int i = 0; i < index0.length; i++) {
						sheet.getRow(31+i*index1.length).getCell(0).setCellValue(index0[i]);
						for (int j = 0; j < index1.length; j++) {
							sheet.getRow(31+i*index1.length+j).getCell(1).setCellValue(index1[j]);
						}
					}
					sheet.getRow(31+index0.length*index1.length).getCell(0).setCellValue("总计");
					rowStart = 31;
					cellStart = 2;
				}else if("2".equals(flag)){
					if(isQueryDocSize){
						for (int i = 0; i < head.length; i++) {
							sheet.getRow(30).getCell(i*3+ySize).setCellValue(head[i]);
							sheet.getRow(31).getCell(i*3+ySize).setCellValue("文件");
							sheet.getRow(31).getCell(i*3+ySize+1).setCellValue("电子全文");
							sheet.getRow(31).getCell(i*3+ySize+2).setCellValue("电子全文大小");
						}
					}else{
						for (int i = 0; i < head.length; i++) {
							sheet.getRow(30).getCell(i*2+ySize).setCellValue(head[i]);
							sheet.getRow(31).getCell(i*2+ySize).setCellValue("文件");
							sheet.getRow(31).getCell(i*2+ySize+1).setCellValue("电子全文");
						}
					}
					for (int i = 0; i < index0.length; i++) {
						sheet.getRow(32+i*index1.length).getCell(0).setCellValue(index0[i]);
						for (int j = 0; j < index1.length; j++) {
							sheet.getRow(32+i*index1.length+j).getCell(1).setCellValue(index1[j]);
						}
					}
					sheet.getRow(32+index0.length*index1.length).getCell(0).setCellValue("总计");
					rowStart = 32;
					cellStart = 2;
				}else{
					if(isQueryDocSize){
						for (int i = 0; i < head.length; i++) {
							sheet.getRow(30).getCell(i*4+ySize).setCellValue(head[i]);
							sheet.getRow(31).getCell(i*4+ySize).setCellValue("案卷");
							sheet.getRow(31).getCell(i*4+ySize+1).setCellValue("文件");
							sheet.getRow(31).getCell(i*4+ySize+2).setCellValue("电子全文");
							sheet.getRow(31).getCell(i*4+ySize+3).setCellValue("电子全文大小");
						}
					}else{
						for (int i = 0; i < head.length; i++) {
							sheet.getRow(30).getCell(i*3+ySize).setCellValue(head[i]);
							sheet.getRow(31).getCell(i*3+ySize).setCellValue("案卷");
							sheet.getRow(31).getCell(i*3+ySize+1).setCellValue("文件");
							sheet.getRow(31).getCell(i*3+ySize+2).setCellValue("电子全文");
						}
					}
					for (int i = 0; i < index0.length; i++) {
						sheet.getRow(32+i*index1.length).getCell(0).setCellValue(index0[i]);
						for (int j = 0; j < index1.length; j++) {
							sheet.getRow(32+i*index1.length+j).getCell(1).setCellValue(index1[j]);
						}
					}
					sheet.getRow(32+index0.length*index1.length).getCell(0).setCellValue("总计");
					rowStart = 32;
					cellStart = 2;
				}
			}else if(ySize == 3){
				String[] index0 = yTitle.get(0);
				String[] index1 = yTitle.get(1);
				String[] index2 = yTitle.get(2);
				for (int i = 0; i < index0.length; i++) {
					region = new Region(n, (short) 0, n+(index1.length*index2.length)-1, (short) 0);
					sheet.addMergedRegion(region);
					for (int j = 0; j < index1.length; j++) {
						region = new Region(m, (short) 1, m+index2.length-1, (short) 1);
						sheet.addMergedRegion(region);
						m = m+index2.length;
					}
					n = n+(index1.length*index2.length);
				}
				region = new Region(n, (short) 0, n, (short) c1);
				sheet.addMergedRegion(region);
				
				//处理表头数据
				if("1".equals(flag)){
					for (int i = 0; i < head.length; i++) {
						sheet.getRow(30).getCell(i+ySize).setCellValue(head[i]);
					}
					for (int i = 0; i < index0.length; i++) {
						sheet.getRow(31+i*index1.length*index2.length).getCell(0).setCellValue(index0[i]);
						for (int j = 0; j < index1.length; j++) {
							sheet.getRow(31+i*index1.length*index2.length+j*index2.length).getCell(1).setCellValue(index1[j]);
							for (int j2 = 0; j2 < index2.length; j2++) {
								sheet.getRow(31+i*index1.length*index2.length+j*index2.length+j2).getCell(2).setCellValue(index2[j2]);
							}
						}
					}
					sheet.getRow(31+index0.length*index1.length*index2.length).getCell(0).setCellValue("总计");
					rowStart = 31;
					cellStart = 3;
				}else if("2".equals(flag)){
					if(isQueryDocSize){
						for (int i = 0; i < head.length; i++) {
							sheet.getRow(30).getCell(i*3+ySize).setCellValue(head[i]);
							sheet.getRow(31).getCell(i*3+ySize).setCellValue("文件");
							sheet.getRow(31).getCell(i*3+ySize+1).setCellValue("电子全文");
							sheet.getRow(31).getCell(i*3+ySize+2).setCellValue("电子全文大小");
						}
					}else{
						for (int i = 0; i < head.length; i++) {
							sheet.getRow(30).getCell(i*2+ySize).setCellValue(head[i]);
							sheet.getRow(31).getCell(i*2+ySize).setCellValue("文件");
							sheet.getRow(31).getCell(i*2+ySize+1).setCellValue("电子全文");
						}
					}
					for (int i = 0; i < index0.length; i++) {
						sheet.getRow(32+i*index1.length*index2.length).getCell(0).setCellValue(index0[i]);
						for (int j = 0; j < index1.length; j++) {
							sheet.getRow(32+i*index1.length*index2.length+j*index2.length).getCell(1).setCellValue(index1[j]);
							for (int j2 = 0; j2 < index2.length; j2++) {
								sheet.getRow(32+i*index1.length*index2.length+j*index2.length+j2).getCell(2).setCellValue(index2[j2]);
							}
						}
					}
					sheet.getRow(32+index0.length*index1.length*index2.length).getCell(0).setCellValue("总计");
					rowStart = 32;
					cellStart = 3;
				}else{
					if(isQueryDocSize){
						for (int i = 0; i < head.length; i++) {
							sheet.getRow(30).getCell(i*4+ySize).setCellValue(head[i]);
							sheet.getRow(31).getCell(i*4+ySize).setCellValue("案卷");
							sheet.getRow(31).getCell(i*4+ySize+1).setCellValue("文件");
							sheet.getRow(31).getCell(i*4+ySize+2).setCellValue("电子全文");
							sheet.getRow(31).getCell(i*4+ySize+3).setCellValue("电子全文大小");
						}
					}else{
						for (int i = 0; i < head.length; i++) {
							sheet.getRow(30).getCell(i*3+ySize).setCellValue(head[i]);
							sheet.getRow(31).getCell(i*3+ySize).setCellValue("案卷");
							sheet.getRow(31).getCell(i*3+ySize+1).setCellValue("文件");
							sheet.getRow(31).getCell(i*3+ySize+2).setCellValue("电子全文");
						}
					}
					for (int i = 0; i < index0.length; i++) {
						sheet.getRow(32+i*index1.length*index2.length).getCell(0).setCellValue(index0[i]);
						for (int j = 0; j < index1.length; j++) {
							sheet.getRow(32+i*index1.length*index2.length+j*index2.length).getCell(1).setCellValue(index1[j]);
							for (int j2 = 0; j2 < index2.length; j2++) {
								sheet.getRow(32+i*index1.length*index2.length+j*index2.length+j2).getCell(2).setCellValue(index2[j2]);
							}
						}
					}
					sheet.getRow(32+index0.length*index1.length*index2.length).getCell(0).setCellValue("总计");
					rowStart = 32;
					cellStart = 3;
				}
			}else if(ySize == 4){
				String[] index0 = yTitle.get(0);
				String[] index1 = yTitle.get(1);
				String[] index2 = yTitle.get(2);
				String[] index3 = yTitle.get(3);
				for (int i = 0; i < index0.length; i++) {
					region = new Region(n, (short) 0, n+(index1.length*index2.length*index3.length)-1, (short) 0);
					sheet.addMergedRegion(region);
					for (int j = 0; j < index1.length; j++) {
						region = new Region(m, (short) 1, m+(index2.length*index3.length)-1, (short) 1);
						sheet.addMergedRegion(region);
						for (int j2 = 0; j2 < index2.length; j2++) {
							region = new Region(m2, (short) 2, m2+index3.length-1, (short) 2);
							sheet.addMergedRegion(region);
							m2 = m2+index3.length;
						}
						m = m+(index2.length*index3.length);
					}
					n = n+(index1.length*index2.length*index3.length);
				}
				region = new Region(n, (short) 0, n, (short) c1);
				sheet.addMergedRegion(region);
				
				//处理表头数据
				if("1".equals(flag)){
					for (int i = 0; i < head.length; i++) {
						sheet.getRow(30).getCell(i+ySize).setCellValue(head[i]);
					}
					for (int i = 0; i < index0.length; i++) {
						sheet.getRow(31+i*index1.length*index2.length*index3.length).getCell(0).setCellValue(index0[i]);
						for (int j = 0; j < index1.length; j++) {
							sheet.getRow(31+i*index1.length*index2.length*index3.length+j*index2.length*index3.length).getCell(1).setCellValue(index1[j]);
							for (int j2 = 0; j2 < index2.length; j2++) {
								sheet.getRow(31+i*index1.length*index2.length*index3.length+j*index2.length*index3.length+j2*index3.length).getCell(2).setCellValue(index2[j2]);
								for (int k = 0; k < index3.length; k++) {
									sheet.getRow(31+i*index1.length*index2.length*index3.length+j*index2.length*index3.length+j2*index3.length+k).getCell(3).setCellValue(index3[k]);
								}
							}
						}
					}
					sheet.getRow(31+index0.length*index1.length*index2.length*index3.length).getCell(0).setCellValue("总计");
					rowStart = 31;
					cellStart = 4;
				}else if("2".equals(flag)){
					if(isQueryDocSize){
						for (int i = 0; i < head.length; i++) {
							sheet.getRow(30).getCell(i*3+ySize).setCellValue(head[i]);
							sheet.getRow(31).getCell(i*3+ySize).setCellValue("文件");
							sheet.getRow(31).getCell(i*3+ySize+1).setCellValue("电子全文");
							sheet.getRow(31).getCell(i*3+ySize+2).setCellValue("电子全文大小");
						}
					}else{
						for (int i = 0; i < head.length; i++) {
							sheet.getRow(30).getCell(i*2+ySize).setCellValue(head[i]);
							sheet.getRow(31).getCell(i*2+ySize).setCellValue("文件");
							sheet.getRow(31).getCell(i*2+ySize+1).setCellValue("电子全文");
						}
					}
					for (int i = 0; i < index0.length; i++) {
						sheet.getRow(32+i*index1.length*index2.length*index3.length).getCell(0).setCellValue(index0[i]);
						for (int j = 0; j < index1.length; j++) {
							sheet.getRow(32+i*index1.length*index2.length*index3.length+j*index2.length*index3.length).getCell(1).setCellValue(index1[j]);
							for (int j2 = 0; j2 < index2.length; j2++) {
								sheet.getRow(32+i*index1.length*index2.length*index3.length+j*index2.length*index3.length+j2*index3.length).getCell(2).setCellValue(index2[j2]);
								for (int k = 0; k < index3.length; k++) {
									sheet.getRow(32+i*index1.length*index2.length*index3.length+j*index2.length*index3.length+j2*index3.length+k).getCell(3).setCellValue(index3[k]);
								}
							}
						}
					}
					sheet.getRow(32+index0.length*index1.length*index2.length*index3.length).getCell(0).setCellValue("总计");
					rowStart = 32;
					cellStart = 4;
				}else{
					if(isQueryDocSize){
						for (int i = 0; i < head.length; i++) {
							sheet.getRow(30).getCell(i*4+ySize).setCellValue(head[i]);
							sheet.getRow(31).getCell(i*4+ySize).setCellValue("案卷");
							sheet.getRow(31).getCell(i*4+ySize+1).setCellValue("文件");
							sheet.getRow(31).getCell(i*4+ySize+2).setCellValue("电子全文");
							sheet.getRow(31).getCell(i*4+ySize+3).setCellValue("电子全文大小");
						}
					}else{
						for (int i = 0; i < head.length; i++) {
							sheet.getRow(30).getCell(i*3+ySize).setCellValue(head[i]);
							sheet.getRow(31).getCell(i*3+ySize).setCellValue("案卷");
							sheet.getRow(31).getCell(i*3+ySize+1).setCellValue("文件");
							sheet.getRow(31).getCell(i*3+ySize+2).setCellValue("电子全文");
						}
					}
					for (int i = 0; i < index0.length; i++) {
						sheet.getRow(32+i*index1.length*index2.length*index3.length).getCell(0).setCellValue(index0[i]);
						for (int j = 0; j < index1.length; j++) {
							sheet.getRow(32+i*index1.length*index2.length*index3.length+j*index2.length*index3.length).getCell(1).setCellValue(index1[j]);
							for (int j2 = 0; j2 < index2.length; j2++) {
								sheet.getRow(32+i*index1.length*index2.length*index3.length+j*index2.length*index3.length+j2*index3.length).getCell(2).setCellValue(index2[j2]);
								for (int k = 0; k < index3.length; k++) {
									sheet.getRow(32+i*index1.length*index2.length*index3.length+j*index2.length*index3.length+j2*index3.length+k).getCell(3).setCellValue(index3[k]);
								}
							}
						}
					}
					sheet.getRow(32+index0.length*index1.length*index2.length*index3.length).getCell(0).setCellValue("总计");
					rowStart = 32;
					cellStart = 4;
				}
			}
			//合并单元??  结束
			//处理单元格数??     ????
			if(ySize == 0){
				HSSFRow row = null;
				HSSFCell cell = null;
				for (int i = 0; i < tabledata.length; i++) {
					row = sheet.getRow(rowStart++);
					for (int j = 0; j < tabledata[i].length; j++) {
						cell = row.getCell(cellStart+j);
						if(Util.notNull(tabledata[i][j]) && tabledata[i][j].contains("B")){
							cell.setCellValue(getDocSize(tabledata[i][j]));
						}else{
							cell.setCellValue(tabledata[i][j]);
						}
					}
				}
			}else{
				String[] count = new String[tabledata[0].length];
				for (int i = 0; i < count.length; i++) {
					count[i] = "0";
				}
				String t = null;
				for (int i = 0; i < tabledata.length; i++) {
					for (int j = 0; j < tabledata[i].length; j++) {
						String data = tabledata[i][j];
						if(Util.isNull(data)){
							continue;
						}
						if(tabledata[i][j].contains("B")){
							data = data.substring(0, data.length()-1);
							t = count[j];
							if(t.contains("B")){
								t = t.substring(0,t.length()-1);
							}
							count[j] = String.valueOf(Long.parseLong(data)+Long.parseLong(t))+"B";
						}else{
							count[j] = String.valueOf(Long.parseLong(data)+Long.parseLong(count[j]));
						}
					}
				}
				String[][] datas = new String[tabledata.length+1][tabledata[0].length];
				for (int i = 0; i < tabledata.length; i++) {
					datas[i] = tabledata[i];
				}
				datas[tabledata.length] = count;
				HSSFRow row = null;
				HSSFCell cell = null;
				for (int i = 0; i < datas.length; i++) {
					row = sheet.getRow(rowStart++);
					for (int j = 0; j < datas[i].length; j++) {
						cell = row.getCell(cellStart+j);
						if(Util.notNull(datas[i][j]) && datas[i][j].contains("B")){
							cell.setCellValue(getDocSize(datas[i][j]));
						}else{
							cell.setCellValue(datas[i][j]);
						}
					}
				}
			}
			
			os = new FileOutputStream(new File(excelPath));
			wb.write(os);
		} catch (IOException io) {
			io.printStackTrace();
			System.out.println("io erorr :  " + io.getMessage());
		} finally {
			os.close();
		}
	}

	private String getDocSize(String docSize){
    	long size = 0;
		if(Util.notNull(docSize)){
			size = Long.parseLong(docSize.substring(0,docSize.length()-1));
		}
		String sizeStr = "";
		if(size < 1024){
			sizeStr = size+"B";
		}else if(size < 1024 * 1024){
			size = (size/1024)+(size%1024 == 0?0:1);
			sizeStr = size+"KB";
		}else if(size < 1024 * 1024 * 1024){
			size = (size/(1024*1024))+(size%1024 == 0?0:1);
			sizeStr = size+"M";
		}else{
			size = (size/(1024*1024*1024))+(size%1024 == 0?0:1);
			sizeStr = size+"G";
		}
    	return sizeStr;
    }
	
	/**
	 * 创建??个空的excel
	 * @param excelPath
	 * @return
	 * @throws Exception
	 */
	public void createExcel(String excelPath){
		try{
			HSSFWorkbook wb = new HSSFWorkbook();// 建立新HSSFWorkbook对象
			wb.createSheet("sheet");
			FileOutputStream fileOut = new FileOutputStream(excelPath);
			wb.write(fileOut);// 把Workbook对象输出到文件workbook.xls??
			fileOut.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private void isChartPathExist(String chartPath) {
		File file = new File(chartPath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

}

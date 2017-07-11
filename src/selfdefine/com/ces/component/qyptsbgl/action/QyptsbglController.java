package com.ces.component.qyptsbgl.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.qyptsbgl.service.QyptsbglService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.xarch.core.entity.StringIDEntity;
@Results({@Result(name = "downLoad", type = "stream", params = {
        "contentType", "application/octet-stream",
        "inputName", "inputStream",
        "contentDisposition","attachment;filename=\"${downfileName}\"",
        "bufferSize","4096" })})
public class QyptsbglController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, QyptsbglService, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;
    /*************************************下载 begin************************************/
    private String downfileName;
    private InputStream inputStream;

    public String getDownloadFileName(){
        return downfileName;
    }
    public String getDownfileName() {
        return downfileName;
    }

    public void setDownfileName(String downfileName) {
        this.downfileName = downfileName;
    }

    public InputStream getInputStream() throws FileNotFoundException {
        return inputStream;
    }
    /*************************************下载 end************************************/
    
    /*************************************上传 begin**********************************/
    private List<File> imageUpload;
    private List<String> imageUploadFileName;

    public List<String> getImageUploadFileName() {
        return imageUploadFileName;
    }
    public void setImageUploadFileName(List<String> imageUploadFileName) {
        this.imageUploadFileName = imageUploadFileName;
    }
    public List<File> getImageUpload() {
        return imageUpload;
    }
    public void setImageUpload(List<File> imageUpload) {
        this.imageUpload = imageUpload;
    }
    /*************************************上传 end**********************************/
    /*
     * (非 Javadoc)   
     * <p>标题: initModel</p>   
     * <p>描述: </p>      
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }
    /**
     * 下载地块导入模版
     * @return
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public String downLoad() throws FileNotFoundException, UnsupportedEncodingException {
        String id = getParameter("id");
        // 取到服务器上传文件存放的路径
        String path = ServletActionContext.getServletContext().getRealPath("/WEB-INF/template");
        downfileName = "设备.xls";
        String FilePath = path + File.separator + downfileName;
        // 取到上传文件的完整路径
        String agent = ServletActionContext. getRequest().getHeader("User-agent");
        //如果浏览器是IE浏览器，就得进行编码转换
        if(agent.contains("MSIE")){
            downfileName = URLEncoder.encode(downfileName, "UTF-8");
        }else{
            this.downfileName = new String(downfileName.getBytes(),"ISO-8859-1");
        }
        inputStream = new FileInputStream(new File(FilePath));
        return "downLoad";
    }
    
    
    public Object ImportXls() throws IOException{
        String realpath = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
        String fileNameWithStamp = "";
        if (imageUpload != null && imageUpload.size() != 0 && !imageUpload.isEmpty()) {
            for (int i = 0; i < imageUpload.size(); i++) {
                if (i != imageUpload.size() - 1) {
                    continue;
                }
                fileNameWithStamp = System.currentTimeMillis() + "_" + imageUploadFileName.get(i);
                File destFile = new File(realpath + "/" + fileNameWithStamp);
                try {
                    FileUtils.copyFile(imageUpload.get(i), destFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 构造 Workbook 对象，execelFile 是传入文件路径(获得Excel工作区)
        Workbook book = null;
        try {
            // Excel 2003获取方法
            book = new HSSFWorkbook(new FileInputStream(realpath + "/" + fileNameWithStamp));
        } catch (Exception ex) {
            // Excel 2007获取方法
//                ex.printStackTrace();
//                book = new XSSFWorkbook(new FileInputStream(realpath + "/" + fileNameWithStamp));
        }
        if (book == null) {
            book = new XSSFWorkbook(new FileInputStream(realpath + "/" + fileNameWithStamp));
        }

        // 读取表格的第一个sheet页
        Sheet sheet = book.getSheetAt(0);
        // 定义 row、cell
        Row row;
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        row = sheet.getRow(0);
        if (!"编号".equals(String.valueOf(row.getCell(0)))||
                !"设备识别号".equals(String.valueOf(row.getCell(1)))||
                !"类别".equals(String.valueOf(row.getCell(2)))||
                !"品牌".equals(String.valueOf(row.getCell(3)))||
                !"型号".equals(String.valueOf(row.getCell(4)))||
                !"所属企业".equals(String.valueOf(row.getCell(5)))||
                !"所属单位".equals(String.valueOf(row.getCell(6)))|| 
                !"启用日期".equals(String.valueOf(row.getCell(7)))||
                !"使用年限".equals(String.valueOf(row.getCell(8)))||
        	    !"秤厂编码".equals(String.valueOf(row.getCell(9)))||
        	    !"IP地址".equals(String.valueOf(row.getCell(10)))||
        	    !"用户名".equals(String.valueOf(row.getCell(11)))||
        	    !"口令".equals(String.valueOf(row.getCell(12)))||
        	    !"联系方式".equals(String.valueOf(row.getCell(13)))||
        	    !"维护人员".equals(String.valueOf(row.getCell(14)))){
            Map<String,String> resultMap = new HashMap<String, String>();
            resultMap.put("RESULT","ERROR");
            resultMap.put("MSG","导入模版格式不正确！！！");
            setReturnData(resultMap);
            return ERROR;
        }
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        List<String> nameList = new ArrayList<String>();
        nameList.add("BH");
        nameList.add("SBSBH");
        nameList.add("LB");
        nameList.add("PP");
        nameList.add("XH");
        nameList.add("SSQY");
        nameList.add("SSDW");
        nameList.add("QYRQ");
        nameList.add("SYNX");
        nameList.add("CCBH");
        nameList.add("IPDZ");
        nameList.add("YHM");
        nameList.add("KL");
        nameList.add("LXFS");
        nameList.add("WHRY");
        String cell;
        // 总共有多少列,从0开始
        for (int i = sheet.getRow(0).getFirstCellNum() ; i < sheet.getRow(0).getLastCellNum(); i++) {
            // 处理空列
            if (sheet.getRow(0).getCell(i) == null) {
                continue;
            }
            for (int j = sheet.getFirstRowNum() + 1; j <= sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                // 处理空列
                if (row.getCell(i) == null) {
                    continue;
                }
                if (dataList.size() < j) {
                    dataList.add(new HashMap<String, String>());
                    dataList.get(j - 1).put("BH","");
                    dataList.get(j - 1).put("SBSBH","");
                    dataList.get(j - 1).put("LB","");
                    dataList.get(j - 1).put("PP","");
                    dataList.get(j - 1).put("XH","");
                    dataList.get(j - 1).put("SSQY","");
                    dataList.get(j - 1).put("SSDW","");
                    dataList.get(j - 1).put("QYRQ","");
                    dataList.get(j - 1).put("SYNX","");
                    dataList.get(j - 1).put("CCBH","");
                    dataList.get(j - 1).put("IPDZ","");
                    dataList.get(j - 1).put("YHM","");
                    dataList.get(j - 1).put("KL","");
                    dataList.get(j - 1).put("LXFS","");
                    dataList.get(j - 1).put("WHRY","");
                }
                dataList.get(j - 1).put(nameList.get(i), String.valueOf(row.getCell(i)));
                //通过 row.getCell(j).toString() 获取单元格内容
            }
        }
        setReturnData(getService().importXls(dataList));
        return SUCCESS;
    }

}
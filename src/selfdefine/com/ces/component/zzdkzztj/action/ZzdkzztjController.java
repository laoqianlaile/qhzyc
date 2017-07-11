package com.ces.component.zzdkzztj.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzdkzztj.service.ZzdkzztjService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.exception.FatalException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.rest.DefaultHttpHeaders;

import java.io.*;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.*;

@Results({@Result(name = "downLoad", type = "stream", params = {
        "contentType", "application/octet-stream",
        "inputName", "inputStream",
        "contentDisposition","attachment;filename=\"${downfileName}\"",
        "bufferSize","4096" })})
public class ZzdkzztjController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzdkzztjService, TraceShowModuleDao> {

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

    private static Log log = LogFactory.getLog(ZzdkzztjController.class);
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
    public void getDkbhById(){
        String id = getParameter("id");
        setReturnData(getService().getDkbhById(id));
    }

    public void getZzdkbh(){
        String jdbh = getParameter("jdbh");
        setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ",jdbh,"ZZDKBH",true));
    }

    @Override
    public Object destroy() throws FatalException {
        try {
            // 1. 获取表ID, ID
            String ids      = getId();
            getService().logicDelete(ids);
        } catch (Exception e) {
            processException(e, BusinessException.class);
            log.error("删除出错", e);
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 保证 IC卡编号不能重复
     * @return
     */
    public Object checkICKBH(){
        String ICKBH=getParameter("ickbh");
        String id=getParameter("id");
        setReturnData(getService().checkICKBH(ICKBH,id));
        return SUCCESS;
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
        downfileName = "地块导入模版.xls";
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
        if (!"序号".equals(String.valueOf(row.getCell(0))) ||
                !"所属基地编号".equals(String.valueOf(row.getCell(1))) ||
                !"所属区域编号".equals(String.valueOf(row.getCell(2))) ||
                !"地块名称".equals(String.valueOf(row.getCell(3))) ||
                !"面积（亩）".equals(String.valueOf(row.getCell(4))) ||
                !"地块负责人编号".equals(String.valueOf(row.getCell(5))) ||
                !"传感器组".equals(String.valueOf(row.getCell(6)))) {
            Map<String,String> resultMap = new HashMap<String, String>();
            resultMap.put("RESULT","ERROR");
            resultMap.put("MSG","导入模版格式不正确！！！");
            setReturnData(resultMap);
            return ERROR;
        }
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        List<String> nameList = new ArrayList<String>();
        nameList.add("XH");
        nameList.add("JDBH");
        nameList.add("QYBH");
        nameList.add("DKMC");
        nameList.add("MJ");
        nameList.add("DKFZRBH");
        nameList.add("CGQZ");
        String cell;
        // 总共有多少列,从0开始(i表示列，j表示行)
        for (int i = sheet.getRow(0).getFirstCellNum() ; i < sheet.getRow(0).getLastCellNum(); i++) {
            // 处理空列
            if (sheet.getRow(0).getCell(i) == null) {
                continue;
            }
            for (int j = sheet.getFirstRowNum() + 1; j <= sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);

                // 处理空列
                if (row == null || row.getCell(i) == null) {
                    continue;
                }
                if (dataList.size() < j) {
                    dataList.add(new HashMap<String, String>());
                    dataList.get(j - 1).put("QYBM",qybm);
                    dataList.get(j - 1).put("JDBH","");
                    dataList.get(j - 1).put("QYBH","");
                    dataList.get(j - 1).put("DKMC","");
                    dataList.get(j - 1).put("MJ","");
                    dataList.get(j - 1).put("DKFZRBH","");
                    dataList.get(j - 1).put("CGQZ","");
                }
                dataList.get(j - 1).put(nameList.get(i), String.valueOf(row.getCell(i)));
                //通过 row.getCell(j).toString() 获取单元格内容
            }
        }
        setReturnData(getService().importXls(dataList));
        return SUCCESS;
    }
}
package com.ces.component.sdzycyprkxx.action;

import com.ces.component.sdzycyprkxx.dao.SdzycyprkxxDao;
import com.ces.component.sdzycyprkxx.service.SdzycyprkxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.UUIDGenerator;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.exception.FatalException;
import org.apache.commons.io.FileUtils;
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
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Results({@Result(name = "downLoad", type = "stream", params = {
        "contentType", "application/octet-stream",
        "inputName", "inputStream",
        "contentDisposition","attachment;filename=\"${downfileName}\"",
        "bufferSize","4096" })})
public class SdzycyprkxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycyprkxxService, SdzycyprkxxDao> {


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
     * 精加工饮片入库
     */
    public void getJjgyprkGrid(){
        this.setReturnData(getService().getJjgyprkGrid());
    }
    /**
     * 精加工按包装饮片入库
     */
    public void getJjgypbzrkGrid(){
        this.setReturnData(getService().getJjgypbzrkGrid());
    }

    /**
     * 获取精加工饮片交易中批次号下拉框中的数据
     */
    public void getPch(){
        this.setReturnData(getService().getPch());
    }

    /**
     * 获取精加工饮片交易中企业批次号下拉框中的数据
     */
    public void getJjgQypch(){this.setReturnData(getService().findJjgQypchGridData());}
    /**
     * 获取精加工饮片交易中企业包装批次号下拉框中的数据
     */
    public void getJjgBzpch(){this.setReturnData(getService().findJjgBzpchGridData());}
    /**
     * 下载导入模版
     * @return
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public String downLoad() throws FileNotFoundException, UnsupportedEncodingException {
        String id = getParameter("id");
        // 取到服务器上传文件存放的路径
        String path = ServletActionContext.getServletContext().getRealPath("/WEB-INF/template");
        downfileName = "yinpianruku.xls";
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
        if (!"入库日期".equals(String.valueOf(row.getCell(0))) ||
                !"仓库名称".equals(String.valueOf(row.getCell(1))) ||
                !"饮片代码".equals(String.valueOf(row.getCell(2))) ||
                !"饮片名称".equals(String.valueOf(row.getCell(3))) ||
                !"规格型号".equals(String.valueOf(row.getCell(4))) ||
                !"入库重量".equals(String.valueOf(row.getCell(5))) ||
                !"生产批次号".equals(String.valueOf(row.getCell(6))) ||
                !"包装批次号".equals(String.valueOf(row.getCell(7)))){
            Map<String,String> resultMap = new HashMap<String, String>();
            resultMap.put("RESULT","ERROR");
            resultMap.put("MSG","导入模版格式不正确！！！");
            setReturnData(resultMap);
            return ERROR;
        }
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        List<String> nameList = new ArrayList<String>();
        //nameList.add("RQ");
        nameList.add("RKSJ");
        nameList.add("CKMC");
        nameList.add("YCDM");
        nameList.add("YPMC");
        nameList.add("BZGG");
        nameList.add("RKZL");
        nameList.add("QYPCH");
        nameList.add("QYBZPCH");
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
                    dataList.get(j - 1).put("CKMC","");
                    dataList.get(j - 1).put("YCDM","");
                    dataList.get(j - 1).put("YPMC","");
                    dataList.get(j - 1).put("BZGG","");
                    dataList.get(j - 1).put("RKZL","");
                    dataList.get(j - 1).put("QYPCH","");
                    dataList.get(j - 1).put("QYBZPCH","");
                }
                dataList.get(j - 1).put(nameList.get(i), String.valueOf(row.getCell(i)));
                //通过 row.getCell(j).toString() 获取单元格内容

                String jjggysbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCJJGYPRKBH", true);
                dataList.get(j -1).put("RKBH",jjggysbh);
            }
        }
        setReturnData(getService().importXls(dataList));
        return SUCCESS;
    }
public static String stringOfDateTime() {
    return stringOfDateTime(new java.util.Date());
}
    public static String stringOfDateTime(java.util.Date date) {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    /*cpjyxx
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

        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        List<String> nameList = new ArrayList<String>();
        nameList.add("JYSJ");
      *//*  nameList.add("RKSJ");
        nameList.add("SHBZ");
        nameList.add("JHDW");
        nameList.add("DJBH");
        nameList.add("CKMC");
        nameList.add("WLCDM");
        nameList.add("YPMC");*//*

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

                    dataList.get(j - 1).put("CREATE_TIME",stringOfDateTime());
                    dataList.get(j - 1).put("QYBM",qybm);
                    dataList.get(j - 1).put("JYSJ","");
                    dataList.get(j - 1).put("CGF","山东省中医院");

                }
                dataList.get(j - 1).put(nameList.get(i), String.valueOf(row.getCell(i)));
                //通过 row.getCell(j).toString() 获取单元格内容
                String xxddhh = StatisticalCodeUtil.getInstance().getTwentyFivePcm("JJG","SDZYC", "SDZYCJJGYPJYXSDDH");
                String qyxsddh = xxddhh.substring(xxddhh.length()-11,xxddhh.length());

                dataList.get(j -1).put("XSDDH",xxddhh);
                dataList.get(j -1).put("QYXSDDH",qyxsddh);
            }
        }
        setReturnData(getService().importXls(dataList));
        return SUCCESS;
    }*/

   /*
   t_cs_scjcxx
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
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        List<String> nameList = new ArrayList<String>();
        nameList.add("JCRQ");
     *//*   nameList.add("SCPCH");
        nameList.add("SCRQ");*//*
        nameList.add("CDMC");
        nameList.add("XSDDH");
      *//*  nameList.add("CKMC");
        nameList.add("WLCDM");
        nameList.add("YPMC");*//*

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
                    qybm = "000000849";
                    dataList.get(j - 1).put("CREATE_TIME",stringOfDateTime());
                    dataList.get(j - 1).put("CSBM",qybm);
                    dataList.get(j - 1).put("JCRQ","");
                  *//*  dataList.get(j - 1).put("SCPCH","");
                    dataList.get(j - 1).put("SCRQ","");*//*
                    dataList.get(j - 1).put("CDMC","");
                    dataList.get(j - 1).put("XSDDH","");
                    dataList.get(j - 1).put("GYSMC","山东百味堂中药饮片有限公司");
                }
                dataList.get(j - 1).put(nameList.get(i), String.valueOf(row.getCell(i)));
                //通过 row.getCell(j).toString() 获取单元格内容

            }
        }
        setReturnData(getService().importXls(dataList));
        return SUCCESS;
    }*/

    /*public Object ImportXls() throws IOException{
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
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        List<String> nameList = new ArrayList<String>();
        nameList.add("SPMC");
        nameList.add("ZL");
        nameList.add("SCPCH");
        nameList.add("SCRQ");
        nameList.add("CDMC");
        nameList.add("DJ");
       nameList.add("XSDDH");
     *//*   nameList.add("WLCDM");
        nameList.add("YPMC");*//*

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
                    qybm = "000000849";
                    dataList.get(j - 1).put("CREATE_TIME",stringOfDateTime());
                    dataList.get(j - 1).put("SPMC","");
                   dataList.get(j - 1).put("ZL","");
                    dataList.get(j - 1).put("SCPCH","");
                    dataList.get(j - 1).put("SCRQ","");
                    dataList.get(j - 1).put("CDMC","");
                    dataList.get(j - 1).put("DJ","");
                    dataList.get(j - 1).put("XSDDH","");
                }
                dataList.get(j - 1).put(nameList.get(i), String.valueOf(row.getCell(i)));
                //通过 row.getCell(j).toString() 获取单元格内容

            }
        }
        setReturnData(getService().importXls(dataList));
        return SUCCESS;
    }*/
    @Override
    public Object destroy() throws FatalException {
        try {
            String ids      = getId();
            String sqlc="select t.bzpch,t.id from  t_sdzyc_jjg_yprkxx t where id IN ('" + ids.replace(",", "','") + "')";
            List<Map<String,Object>> jgpchList = DatabaseHandlerDao.getInstance().queryForMaps(sqlc);
            for (Map<String,Object> dataMap: jgpchList) {
                String pch = String.valueOf(dataMap.get("BZPCH"));
                String id = String.valueOf(dataMap.get("ID"));
                getService().delete(pch,id);
            }
        } catch (Exception e) {
            processException(e, BusinessException.class);
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();

    }
}

package com.ces.component.sdzycjjgyycrkxx.action;

import com.ces.component.sdzycjjgyycrkxx.dao.SdzycjjgyycrkxxDao;
import com.ces.component.sdzycjjgyycrkxx.service.SdzycjjgyycrkxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Results({@Result(name = "downLoad", type = "stream", params = {
        "contentType", "application/octet-stream",
        "inputName", "inputStream",
        "contentDisposition","attachment;filename=\"${downfileName}\"",
        "bufferSize","4096" })})
public class SdzycjjgyycrkxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycjjgyycrkxxService, SdzycjjgyycrkxxDao> {

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


    public void searchDataByPch(){
        String pch = getParameter("pch");
        setReturnData(getService().searchDataByPch(pch));
    }

//    public void searchGridData(){
//         setReturnData(getService().searchYclComboGridData());
//    }
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
        downfileName = "raw_material.xls";
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
        if (!"批次号".equals(String.valueOf(row.getCell(0))) ||
                !"原料名称".equals(String.valueOf(row.getCell(1))) ||
                !"原料产地".equals(String.valueOf(row.getCell(2))) ||
                !"供应商".equals(String.valueOf(row.getCell(3))) ||
                !"入库重量".equals(String.valueOf(row.getCell(4))) ||
                !"入库时间".equals(String.valueOf(row.getCell(5))) ||
                !"入库负责人".equals(String.valueOf(row.getCell(6))) ||
                !"收料仓库".equals(String.valueOf(row.getCell(7)))){
            Map<String,String> resultMap = new HashMap<String, String>();
            resultMap.put("RESULT","ERROR");
            resultMap.put("MSG","导入模版格式不正确！！！");
            setReturnData(resultMap);
            return ERROR;
        }
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        List<String> nameList = new ArrayList<String>();
        nameList.add("QYPCH");
        nameList.add("YCMC");
        nameList.add("CD");
        nameList.add("GYS");
        nameList.add("RKZL");
        nameList.add("RKSJ");
        nameList.add("FZR");
        nameList.add("SLCK");

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
                    dataList.get(j - 1).put("QYPCH","");
                    dataList.get(j - 1).put("YCMC","");
                    dataList.get(j - 1).put("CD","");
                    dataList.get(j - 1).put("GYS","");
                    dataList.get(j - 1).put("RKZL","");
                    dataList.get(j - 1).put("RKSJ","");
                    dataList.get(j - 1).put("FZR","");
                    dataList.get(j - 1).put("SLCK","");
                }
                dataList.get(j - 1).put(nameList.get(i), String.valueOf(row.getCell(i)));
                //通过 row.getCell(j).toString() 获取单元格内容

                String wgpch = "W"+StatisticalCodeUtil.getInstance().getTwentyFivePcm("JJG", "SDZYC", "SDZYCJJGWGPCH");
                String ylrkbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCJJGYCZCRKBH", true);
                dataList.get(j -1).put("PCH",wgpch);
              //  dataList.get(j -1).put("QYPCH",wgpch);
                dataList.get(j -1).put("RKBH",ylrkbh);
                dataList.get(j -1).put("CGLX","外购");
            }
        }
        setReturnData(getService().importXls(dataList));
        return SUCCESS;
    }
    @Override
    public Object destroy() throws FatalException {
        try {
            String ids      = getId();
            String sqlc="select t.pch,t.id from  t_sdzyc_jjg_yycrkxx t where id IN ('" + ids.replace(",", "','") + "')";
            List<Map<String,Object>> jgpchList = DatabaseHandlerDao.getInstance().queryForMaps(sqlc);
            for (Map<String,Object> dataMap: jgpchList) {
                String pch = String.valueOf(dataMap.get("PCH"));
                String id = String.valueOf(dataMap.get("ID"));
                getService().delete(pch,id);
                getService().sendDelTradeInService(id);
            }
        } catch (Exception e) {
            processException(e, BusinessException.class);
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}

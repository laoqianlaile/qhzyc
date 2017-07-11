package com.ces.config.dhtmlx.action.completecomponent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.completecomponent.CompleteComponentVersionDao;
import com.ces.config.dhtmlx.entity.completecomponent.CompleteComponent;
import com.ces.config.dhtmlx.entity.completecomponent.CompleteComponentVersion;
import com.ces.config.dhtmlx.entity.component.ComponentConfig;
import com.ces.config.dhtmlx.service.completecomponent.CompleteComponentService;
import com.ces.config.dhtmlx.service.completecomponent.CompleteComponentVersionService;
import com.ces.config.utils.ComponentFileUtil;
import com.ces.config.utils.ExcelUtil;
import com.ces.config.utils.FileUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.ZipUtil;
import com.ces.utils.DateUtils;

/**
 * 成品构件版本Controller
 * 
 * @author wanglei
 * @date 2014-02-18
 */
public class CompleteComponentVersionController extends
        ConfigDefineServiceDaoController<CompleteComponentVersion, CompleteComponentVersionService, CompleteComponentVersionDao> {

    private static final long serialVersionUID = -2992443061601279590L;

    private static int tableRowNo = 1; // 初始化表sheet行数

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new CompleteComponentVersion());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("completeComponentVersionService")
    @Override
    protected void setService(CompleteComponentVersionService service) {
        super.setService(service);
    }

    /**
     * 导入成品构件
     * 
     * @return Object
     */
    public Object uploadHandler() {
        HttpServletRequest request = ServletActionContext.getRequest();
        // 1、上传文件
        // struts2 请求包装
        MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper) request;
        Enumeration<String> names = wrapper.getFileParameterNames();
        String name = "";
        while (names.hasMoreElements()) {
            name = names.nextElement();
        }
        // 获取上传文件名
        String fileName = wrapper.getFileNames(name)[0];
        // 获取上传文件
        File file = wrapper.getFiles(name)[0];
        // 新的文件名
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + fileName.substring(fileName.lastIndexOf("."), fileName.length());
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            // 建立文件输出流
            fos = new FileOutputStream(ComponentFileUtil.getCompleteCompPath() + newFileName);
            // 建立文件上传流
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            for (int len = 0; (len = fis.read(buffer)) > 0;) {
                fos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            request.getSession().setAttribute("message", "上传失败！");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 2、解包
        String tempPath = ComponentFileUtil.getCompleteCompUncompressPath() + newFileName.substring(0, newFileName.lastIndexOf("."));
        try {
            ZipUtil.unzipFile(new File(ComponentFileUtil.getCompleteCompPath() + newFileName), tempPath);
        } catch (Exception e1) {
            uploadError("解析构件包错误！", newFileName, tempPath);
            return new DefaultHttpHeaders(SUCCESS).disableCaching();
        }

        // 3、解析component-config.xml文件
        File configFile = new File(tempPath + "/component-config.xml");
        ComponentConfig componentConfig = null;
        try {
            componentConfig = ComponentFileUtil.parseConfigFile(configFile);
        } catch (DocumentException e) {
            uploadError("解析component-config.xml错误！", newFileName, tempPath);
            return new DefaultHttpHeaders(SUCCESS).disableCaching();
        }
        componentConfig.setPackageFileName(newFileName);

        // 4、校验成品构件包
        String message = validateCompleteComponent(tempPath, componentConfig);
        if (StringUtil.isNotEmpty(message)) {
            uploadError(message, newFileName, tempPath);
            return new DefaultHttpHeaders(SUCCESS).disableCaching();
        }

        String completeComponentConfigKey = request.getParameter("completeComponentConfigKey");
        request.getSession().setAttribute(completeComponentConfigKey, componentConfig);

        // dhtmlxVault上传完成时需要
        String fileId = request.getParameter("sessionId").toString().trim();
        request.getSession().setAttribute("FileUpload.Progress." + fileId, "-1");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 上传成品构件有问题时的处理
     * 
     * @param message 错误消息
     * @param newFileName 成品构件文件路径
     * @param tempPath 成品构件解包文件目录
     */
    private void uploadError(String message, String newFileName, String tempPath) {
        HttpServletRequest request = ServletActionContext.getRequest();
        deleteCompleteComponentFile(ComponentFileUtil.getCompleteCompPath() + newFileName, tempPath);
        request.getSession().setAttribute("message", message);
        // dhtmlxVault上传完成时需要
        String fileId = request.getParameter("sessionId").toString().trim();
        request.getSession().setAttribute("FileUpload.Progress." + fileId, "-1");
    }

    /**
     * 校验成品构件信息
     * 
     * @param tempPath 成品构件解包文件目录
     * @param componentConfig 成品构件配置
     * @return String 校验结果
     */
    private String validateCompleteComponent(String tempPath, ComponentConfig componentConfig) {
        StringBuffer message = new StringBuffer();
        if (StringUtil.isEmpty(componentConfig.getCode())) {
            message.append("code不能为空，");
        }
        if (StringUtil.isEmpty(componentConfig.getName())) {
            message.append("name不能为空，");
        }
        if (StringUtil.isEmpty(componentConfig.getAlias())) {
            message.append("alias不能为空，");
        }
        if (StringUtil.isEmpty(componentConfig.getVersion())) {
            message.append("version不能为空，");
        }
        if (StringUtil.isEmpty(componentConfig.getType())) {
            message.append("type不能为空，");
        }
        if (("0".equals(componentConfig.getType()) || "1".equals(componentConfig.getType())) && StringUtil.isEmpty(componentConfig.getViews())) {
            message.append("views不能为空，");
        }
        if (StringUtil.isEmpty(componentConfig.getUrl())) {
            message.append("url不能为空，");
        }
        CompleteComponent completeComponent = getService(CompleteComponentService.class).getCompleteComponentByName(componentConfig.getName());
        if (completeComponent != null) {
            if (!completeComponent.getAlias().equals(componentConfig.getAlias())) {
                message.append("构件显示名称错误，");
            }
            if (!completeComponent.getType().equals(componentConfig.getType())) {
                message.append("构件类型错误，");
            }
        }
        if (message.length() > 0) {
            message.deleteCharAt(message.length() - 1);
        }
        return message.toString();
    }

    /**
     * 上传的成品构件校验失败
     * 
     * @return Object
     */
    public Object validateFailure() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String completeComponentConfigKey = request.getParameter("completeComponentConfigKey");
        ComponentConfig componentConfig = (ComponentConfig) request.getSession().getAttribute(completeComponentConfigKey);

        if (componentConfig == null) {
            setReturnData("上传失败");
        } else {
            String packageName = componentConfig.getPackageFileName().substring(0, componentConfig.getPackageFileName().lastIndexOf("."));
            // 删除临时文件
            deleteCompleteComponentFile(ComponentFileUtil.getCompleteCompPath() + componentConfig.getPackageFileName(),
                    ComponentFileUtil.getCompleteCompUncompressPath() + packageName);
            request.getSession().removeAttribute(completeComponentConfigKey);
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 保存成品构件版本
     * 
     * @return Object
     */
    public Object saveCompleteComponentVersion() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String completeComponentConfigKey = request.getParameter("completeComponentConfigKey");
        ComponentConfig componentConfig = (ComponentConfig) request.getSession().getAttribute(completeComponentConfigKey);
        if (componentConfig == null) {
            setReturnData("{'success':false,'message':'上传失败！'}");
        } else {
            try {
                getService().saveComponentConfig(componentConfig);
            } catch (Exception e) {
                e.printStackTrace();
                setReturnData("{'success':false,'message':'保存成品构件失败！'}");
                request.getSession().removeAttribute(completeComponentConfigKey);
                return new DefaultHttpHeaders(SUCCESS).disableCaching();
            }
            String packageName = componentConfig.getPackageFileName().substring(0, componentConfig.getPackageFileName().lastIndexOf("."));
            // 2、删除解压的文件
            FileUtil.deleteFile(ComponentFileUtil.getCompleteCompUncompressPath() + packageName);
            setReturnData("{'success':true,'message':'构件导入成功！'}");
        }
        request.getSession().removeAttribute(completeComponentConfigKey);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 删除解压的临时文件和上传的文件
     * 
     * @param newFileName 成品构件包名次
     * @param tempPath 成品构件解压的临时目录
     */
    private void deleteCompleteComponentFile(String packagePath, String uncompressPath) {
        FileUtil.deleteFile(packagePath);
        FileUtil.deleteFile(uncompressPath);
    }

    /**
     * 下载成品构件版本
     * 
     * @return Object
     * @throws Exception
     */
    public Object downloadCompleteComponent() throws Exception {
        tableRowNo = 2;
        String[] completeComponentVersionIds = getParameter("id").split(",");
        String componentDirPath = ComponentFileUtil.getCompressTempPath() + "selfdefine/" + new Random().nextInt(1000);
        // 1、准备构件打包的目录
        File componentDir = new File(componentDirPath);
        if (!componentDir.exists()) {
            componentDir.mkdirs();
        }
        String fileName = "构件包";
        fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        // 获取excel模板表文件路径
        String templateFilePath = ServletActionContext.getServletContext().getRealPath("WEB-INF/template/component_download_bill.xls");
        File template_file = new File(templateFilePath);// 模板文件路径
        String explortFileRealName = "构件清单表" + ".xls";
        File exportFile = new File(componentDirPath, "/" + explortFileRealName); // 输出文件目的路径
        // 打开生成的xls文件，对内容进行替换
        FileUtil.copyExcel(template_file, exportFile); // 用模板替换新建的文件
        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(exportFile));
        // 先创建工作簿对象
        HSSFWorkbook workbook2003 = new HSSFWorkbook(fs);
        HSSFSheet sheet = workbook2003.getSheetAt(0); // 打开第一张sheet
        // 6、拷贝到构件包文件路径下
        for (int i = 0; i < completeComponentVersionIds.length; i++) {
            String completeComponentVersionId = completeComponentVersionIds[i];
            CompleteComponentVersion completeComponentVersion = getService().getByID(completeComponentVersionId);
            String completeComponentPath = ComponentFileUtil.getCompleteCompPath() + completeComponentVersion.getPath();
            FileUtil.copyFile(completeComponentPath, componentDirPath + "/" + completeComponentVersion.getComponent().getAlias() + ".zip");

            HSSFRow row = (HSSFRow) sheet.createRow(tableRowNo + i);
            row.createCell(0).setCellValue(completeComponentVersion.getComponent().getName());
            row.getCell(0).setCellStyle(ExcelUtil.getExcellStyle(workbook2003));
            row.createCell(1).setCellValue(completeComponentVersion.getComponent().getAlias());
            row.getCell(1).setCellStyle(ExcelUtil.getExcellStyle(workbook2003));
            row.createCell(2).setCellValue(completeComponentVersion.getVersion());
            row.getCell(2).setCellStyle(ExcelUtil.getExcellStyle(workbook2003));
            row.createCell(3).setCellValue(DateUtils.toString(completeComponentVersion.getImportDate(), DateUtils.DATE_PATTERN));
            row.getCell(3).setCellStyle(ExcelUtil.getExcellStyle(workbook2003));
            row.createCell(4).setCellValue(ComponentType.fromValue(completeComponentVersion.getComponent().getType()).toText());
            row.getCell(4).setCellStyle(ExcelUtil.getExcellStyle(workbook2003));
            row.createCell(5).setCellValue(completeComponentVersion.getViews());
            row.getCell(5).setCellStyle(ExcelUtil.getExcellStyle(workbook2003));
            row.createCell(6).setCellValue(completeComponentVersion.getRemark());
            row.getCell(6).setCellStyle(ExcelUtil.getExcellStyle(workbook2003));
        }
        FileUtil.saveFileStream(workbook2003, exportFile);// 保存excel数据文件
        // 5、压缩zip包
        String zipPath = componentDirPath + ".zip";
        File componentZip = new File(zipPath);
        ZipUtil.zip(componentZip, "", componentDir);
        HttpServletResponse response = ServletActionContext.getResponse();
        response.reset();
        response.setContentType("application/x-msdownload");
        response.setContentLength((int) componentZip.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".zip");
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            fis = new FileInputStream(componentZip);
            bis = new BufferedInputStream(fis);
            os = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = bis.read(b, 0, 1024)) > 0) {
                os.write(b, 0, len);
            }
            os.flush();
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
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileUtil.deleteDir(ComponentFileUtil.getCompressTempPath() + "selfdefine/");
        }
        return NONE;
    }

    /**
     * 删除成品构件版本
     * 
     * @return Object
     */
    public Object deleteCompleteComponentVersion() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String ids = request.getParameter("ids");
        String[] completeComponentVersionIds = ids.split(",");
        for (String completeComponentVersionId : completeComponentVersionIds) {
            CompleteComponentVersion completeComponentVersion = getService().getByID(completeComponentVersionId);
            FileUtil.deleteFile(ComponentFileUtil.getCompleteCompPath() + completeComponentVersion.getPath());
            getService().delete(completeComponentVersion);
        }
        setReturnData("删除成功！");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 更改构件分类
     * 
     * @return Object
     */
    public Object changeArea() {
        String completeComponentVersionIds = getParameter("completeComponentVersionIds");
        String areaId = getParameter("areaId");
        String[] versionIds = completeComponentVersionIds.split(",");
        for (String versionId : versionIds) {
            CompleteComponentVersion completeComponentVersion = getService().getByID(versionId);
            if (completeComponentVersion != null) {
                completeComponentVersion.setAreaId(areaId);
                getService().save(completeComponentVersion);
            }
        }
        setReturnData("更改构件分类成功！");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取上传成品构件的情况
     * 
     * @return Object
     */
    public Object getUploadMessage() {
        HttpServletRequest request = ServletActionContext.getRequest();
        Object message = request.getSession().getAttribute("message");
        request.getSession().setAttribute("message", "");
        if (StringUtil.isNotEmpty(message)) {
            setReturnData("{'success':false, 'message':'" + message + "'}");
        } else {
            String completeComponentConfigKey = request.getParameter("completeComponentConfigKey");
            ComponentConfig componentConfig = (ComponentConfig) request.getSession().getAttribute(completeComponentConfigKey);
            // 是否存在旧的成品构件版本
            boolean existOldCompleteComponentVersion = false;
            CompleteComponent completeComponent = getService(CompleteComponentService.class).getCompleteComponentByName(componentConfig.getName());
            if (completeComponent != null) {
                CompleteComponentVersion oldCompleteComponentVersion = getService().getByComponentIdAndVersion(completeComponent.getId(),
                        componentConfig.getVersion());
                if (oldCompleteComponentVersion != null) {
                    existOldCompleteComponentVersion = true;
                }
            }
            setReturnData("{'success':true, 'existOldCompleteComponentVersion':" + existOldCompleteComponentVersion + "}");
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * dhtmlxVault获取ID方法
     * 
     * @return Object
     */
    public Object getIdHandler() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String id = request.getSession().getId().toString();
        try {
            PrintWriter writer = response.getWriter();
            writer.println(id);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.getSession().setAttribute("FileUpload.Progress." + id, "0");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * dhtmlxVault获取信息方法
     * 
     * @return Object
     */
    public Object getInfoHandler() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            PrintWriter writer = response.getWriter();
            writer.println(request.getSession().getAttribute("FileUpload.Progress." + request.getParameter("sessionId").toString().trim()));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    protected enum ComponentType {
        YMGJ("页面构件", "1"), LJGJ("逻辑构件", "2"), ZDYGJ("自定义构件", "3"), ZHGJ("组合构件", "4");

        private final String text;

        private final String property;

        ComponentType(String text, String property) {
            this.text = text;
            this.property = property;
        }

        public String toText() {
            return this.text;
        }

        public String toProperty() {
            return this.property;
        }

        public static ComponentType fromValue(String text) {
            for (ComponentType type : values()) {
                if (type.toProperty().equals(text)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("not support value: " + text);
        }

    }
}

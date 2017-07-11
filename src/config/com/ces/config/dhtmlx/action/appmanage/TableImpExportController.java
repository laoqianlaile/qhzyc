package com.ces.config.dhtmlx.action.appmanage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.service.appmanage.TableImpExportService;
import com.ces.config.utils.CommonUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;
import com.ces.xarch.core.web.struts2.StringIDDefineServiceController;

/**
 * @data 2013-12-16
 * @author wang
 */
public class TableImpExportController extends StringIDDefineServiceController<StringIDEntity, TableImpExportService> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }

    /*
     * (非 Javadoc) <p>描述: 注入服务层(Service)</p>
     * @param service
     * @see
     * com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch
     * .core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("tableImpExportService")
    protected void setService(TableImpExportService service) {
        super.setService(service);
    }

    /**
     * <p>标题: getParameter</p>
     * <p>描述: 处理从request中取得参数值</p>
     * 
     * @return String 返回类型
     * @throws
     */
    protected String getParameter(String param) {
        HttpServletRequest request = ServletActionContext.getRequest();
        return request.getParameter(param);
    }

    public static String filePath = "";

    /**
     * wangmi 2013-12-16
     * <p>描述:excel 解析并插到数据库</p>
     * 
     * @param
     * @return
     */
    public Object impTableExcel() throws FatalException {
        try {
            String classification = getParameter("classification");
            String tableTreeId = getParameter("tableTreeId");
            String tableMessage = getService().impTableExcelService(classification, tableTreeId, filePath);

            if (!tableMessage.equals("")) {
                if (tableMessage.indexOf(";") != -1) {
                    setReturnData(new MessageModel(1, tableMessage));
                } else {
                    setReturnData(new MessageModel(2, tableMessage));
                }
            } else {
                setReturnData(new MessageModel(0, "导入成功!"));
            }

            File file = new File(filePath);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData(new MessageModel(3, "导入失败!"));
            File file = new File(filePath);
            file.delete();
            // throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * wangmi 2013-12-16
     * <p>描述:上传文件</p>
     * 
     * @param
     * @return
     */
    public Object uploadHandler() {
        HttpServletRequest request = ServletActionContext.getRequest();
        // 1、上传文件
        MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper) request;
        Enumeration<String> names = wrapper.getFileParameterNames();
        String name = "";
        while (names.hasMoreElements()) {
            name = names.nextElement();
        }
        // 获取上传文件名
        String fileName = wrapper.getFileNames(name)[0];
        File file = wrapper.getFiles(name)[0];
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String Nprefix = df.format(new Date()) + new Random().nextInt(100);
        String newFileName = Nprefix + fileName.substring(fileName.lastIndexOf("."), fileName.length());
        FileOutputStream fos = null;
        FileInputStream fis = null;
        // 获取文件存放路径
        String filelLocal = System.getProperty("java.io.tmpdir");
        filePath = filelLocal + "/" + newFileName;
        try {
            // 建立文件输出流
            fos = new FileOutputStream(filePath);
            // 建立文件上传流
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            for (int len = 0; (len = fis.read(buffer)) > 0;) {
                fos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        // dhtmlxVault上传完成时需要
        String fileId = request.getParameter("sessionId").toString().trim();
        request.getSession().setAttribute("FileUpload.Progress." + fileId, "-1");
        return null;
    }

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
     * wangmi 2013-12-17
     * <p>表Excel模版下载</p>
     * 
     * @param
     * @return
     */
    public Object downloadExcelLogic() {

        HttpServletRequest request = ServletActionContext.getRequest();
        String newFileName = "";
        try {
            if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
                newFileName = new String("table模版".getBytes("UTF-8"), "ISO8859-1");
            } else {
                newFileName = java.net.URLEncoder.encode("table模版", "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 获取表模版路径
        String tempFilePath = ServletActionContext.getServletContext().getRealPath("WEB-INF/template/TABLE.xls");
        HttpServletResponse response = ServletActionContext.getResponse();
        response.reset();
        response.setContentType("application/vnd.ms-excel"); // 下载文版类型
        response.setHeader("Content-Disposition", "attachment;filename=" + newFileName + ".xls");
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            fis = new FileInputStream(tempFilePath);
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
        }
        return NONE;
    }

    /**
     * wangmi 2013-12-18
     * <p>导出表到excel</p>
     * 
     * @param
     * @return
     */
    public Object exportTableExcel() throws FatalException {
        try {
            String Ids = getParameter("rowIds");
            getService().exportTableExcelService(Ids);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * qiujinwei 2015-03-09
     * <p>导出选中的列到excel</p>
     * 
     * @param
     * @return
     */
    public Object exportExcel(){
    	try {
    		String tableId = getParameter("P_tableId");
            String componentVersionId = getParameter("P_componentVersionId");
            String menuId = getParameter("P_menuId");
            String ids = getParameter("P_ids");
            String timestamp = getParameter("P_timestamp");
            String filter = CommonUtil.getQueryFilter(tableId + timestamp);
            getService().exportTableExcelService(tableId, componentVersionId, menuId, ids, filter);
		} catch (Exception e) {
			e.printStackTrace();
	        setReturnData(new MessageModel(Boolean.FALSE, "ERROR"));
		}
    	return NONE;
    }

}

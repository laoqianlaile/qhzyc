package com.ces.component.sdzycdzjgmdr.action;

import com.ces.component.sdzycdzjgmdr.dao.SdzycdzjgmdrDao;
import com.ces.component.sdzycdzjgmdr.service.SdzycdzjgmdrService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import java.io.*;
import java.net.URLEncoder;
import java.util.Map;
@Results({@Result(name = "downLoad", type = "stream", params = {
        "contentType", "application/octet-stream",
        "inputName", "inputStream",
        "contentDisposition","attachment;filename=\"${downfileName}\"",
        "bufferSize","4096" }),
        @Result(name="nofileexists",location="/500.jsp")}
)
public class SdzycdzjgmdrController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycdzjgmdrService, SdzycdzjgmdrDao> {

    private static final long serialVersionUID = 1L;

    private File uploadFile;
    private String uploadFileFileName;
    private String uploadFileContentType;

    public File getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(File uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getUploadFileFileName() {
        return uploadFileFileName;
    }

    public void setUploadFileFileName(String uploadFileFileName) {
        this.uploadFileFileName = uploadFileFileName;
    }

    public String getUploadFileContentType() {
        return uploadFileContentType;
    }

    public void setUploadFileContentType(String uploadFileContentType) {
        this.uploadFileContentType = uploadFileContentType;
    }
    private String downfileName;
    private InputStream inputStream;
    public InputStream getInputStream() throws FileNotFoundException {
        return inputStream;
    }
    public String getDownloadFileName(){
        return downfileName;
    }
    public String getDownfileName() {
        return downfileName;
    }
    public void setDownfileName(String downfileName) {
        this.downfileName = downfileName;
    }
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

    @Override
    public Object save() throws FatalException {
        Map map = getRequest().getParameterMap();
        setReturnData(getService().saveData(uploadFile,uploadFileFileName,uploadFileContentType,map));
        return null;
    }
    public String downLoad() throws FileNotFoundException, UnsupportedEncodingException {
        String bzpch = getParameter("bzpch");
        // 取到服务器上传文件存放的路径
        String path = ServletActionContext.getServletContext().getRealPath("/spzstpfj/");
        downfileName = bzpch;
        String FilePath = path +"/" +downfileName;
        // 取到上传文件的完整路径
        String agent = ServletActionContext. getRequest().getHeader("User-agent");
        if(agent.contains("MSIE")){
            this.downfileName = URLEncoder.encode(downfileName, "UTF-8");
        }else{
            this.downfileName = new String(downfileName.getBytes(),"ISO-8859-1");
        }
        if(new File(FilePath).exists()== false){

            return "nofileexists";
        }else{
            inputStream = new FileInputStream(new File(FilePath));
            return "downLoad";
        }
    }
}

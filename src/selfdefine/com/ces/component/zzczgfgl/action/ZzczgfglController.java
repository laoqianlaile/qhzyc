package com.ces.component.zzczgfgl.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzczgfgl.service.ZzczgfglService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
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
public class ZzczgfglController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzczgfglService, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;
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

    public String getDownLoad() {
        String id = getParameter("id");
        Map<String,Object> fileName = (Map<String,Object>)getService().getDownLoad(id);
        // 取到服务器上传文件存放的路径
        String path = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
        // 取到上传文件的完整路径
        String FilePath = path + File.separator + String.valueOf(fileName.get("TPBCMC"));
        //存储文件
        String newFilePath = fileName.get("CZGFMC").toString().split("\\.")[0] + "." + fileName.get("TPBCMC").toString().split("\\.")[1];
        File file = new File("e:"+ File.separator + newFilePath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream is = null;
        OutputStream os = null;
        try {
            is=new FileInputStream(file);
            os=new FileOutputStream(FilePath);
            byte[] b=new byte[1024];
            int len=0;
            while((len=is.read(b))!=-1){
                os.write(b,0,len);
                os.flush();
            }
            is.close();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       setReturnData("success");

        return null;
    }


    public String downLoad() throws FileNotFoundException, UnsupportedEncodingException {
        String id = getParameter("id");
        Map<String,Object> fileName = (Map<String,Object>)getService().getDownLoad(id);
        // 取到服务器上传文件存放的路径
        String path = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
        downfileName = String.valueOf(fileName.get("TPBCMC"));
        String FilePath = path + File.separator + downfileName;
        // 取到上传文件的完整路径
        String agent = ServletActionContext. getRequest().getHeader("User-agent");
        //如果浏览器是IE浏览器，就得进行编码转换
        downfileName = String.valueOf(fileName.get("SCTPMC"));
        if(agent.contains("MSIE")){
            this.downfileName = URLEncoder.encode(downfileName, "UTF-8");
        }else{
            this.downfileName = new String(downfileName.getBytes(),"ISO-8859-1");
        }
        if(new File(FilePath).exists()== false){
        	
        	return "nofileexists";
        }else{
        inputStream = new FileInputStream(new File(FilePath));
        return "downLoad";}
    }

}
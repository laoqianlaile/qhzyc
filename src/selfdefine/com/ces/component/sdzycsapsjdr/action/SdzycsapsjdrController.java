package com.ces.component.sdzycsapsjdr.action;

import com.ces.component.sdzycsapsjdr.dao.SdzycsapsjdrDao;
import com.ces.component.sdzycsapsjdr.service.SdzycsapsjdrService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.util.Map;


public class SdzycsapsjdrController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycsapsjdrService, SdzycsapsjdrDao> {
    private final String REAL_PATH = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
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
     * 获得原料入号单号下拉框数据
     */
   // public void searchGridData(){
//        setReturnData(getService().searchylrkxxComboGridData());
//    }

//    public void searcylrkxx() {
//        String pch = getParameter("pch");
//        setReturnData(getService().searchylrkxxBylldh(pch));
//}
    /**
     * 删除图片操作
     */
    public void deleteTp(){
        String id = getParameter("id");
        Map<String,Object> dataMap = getService().searchById(id);
        String jywj = String.valueOf(dataMap.get("YPTP"));
        File oldFile = new File(REAL_PATH+"/"+jywj);
        if(oldFile.exists()){
            oldFile.delete();
        }
        setReturnData( getService().updateBzwj(id));
    }


    @Override
    public Object save() throws FatalException {
        Map map = this.getRequest().getParameterMap();
        setReturnData(getService().saveData(map, uploadFile, uploadFileFileName));
        return null;
    }
}

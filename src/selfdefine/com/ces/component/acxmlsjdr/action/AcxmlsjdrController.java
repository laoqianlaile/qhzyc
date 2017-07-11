package com.ces.component.acxmlsjdr.action;

import com.ces.component.acxmlsjdr.dao.AcxmlsjdrDao;
import com.ces.component.acxmlsjdr.service.AcxmlsjdrService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;

import java.io.File;
import java.util.Map;

public class AcxmlsjdrController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, AcxmlsjdrService, AcxmlsjdrDao> {

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

    public void searchGridData(){
        setReturnData(getService().searchycmcComboGridData());
    }
}

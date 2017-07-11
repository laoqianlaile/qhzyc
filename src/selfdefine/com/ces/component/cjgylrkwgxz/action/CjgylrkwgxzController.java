package com.ces.component.cjgylrkwgxz.action;

import com.ces.component.cjgylrkwgxz.dao.CjgylrkwgxzDao;
import com.ces.component.cjgylrkwgxz.service.CjgylrkwgxzService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class CjgylrkwgxzController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CjgylrkwgxzService, CjgylrkwgxzDao> {

    private static final long serialVersionUID = 1L;
    private InputStream inputStream;
    public InputStream getInputStream() throws FileNotFoundException {
        return inputStream;
    }

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
    private final String REAL_PATH = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
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
    public void searchGridData(){
        setReturnData(getService().searchycmcComboGridData());
    }

    /**
     * 删除图片操作
     */
    public void deleteTp(){
        String id = getParameter("id");
        Map<String,Object> dataMap = getService().searchById(id);
        String jcxxlj = String.valueOf(dataMap.get("JCXXLJ"));
        File oldFile = new File(REAL_PATH+"/"+jcxxlj);
        if(oldFile.exists()){
            oldFile.delete();
        }
        setReturnData( getService().updateJcxx(id));
    }

    @Override
    public Object save() throws FatalException {
        Map map = this.getRequest().getParameterMap();
        setReturnData(getService().saveDate(map));
        return null;
    }



}

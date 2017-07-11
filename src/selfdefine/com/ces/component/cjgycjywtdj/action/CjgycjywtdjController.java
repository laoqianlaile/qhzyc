package com.ces.component.cjgycjywtdj.action;

import com.ces.component.cjgycjywtdj.dao.CjgycjywtdjDao;
import com.ces.component.cjgycjywtdj.service.CjgycjywtdjService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.util.List;
import java.util.Map;

public class CjgycjywtdjController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CjgycjywtdjService, CjgycjywtdjDao> {

    private static final long serialVersionUID = 1L;
    private List<File> imageUpload;
    private List<String> imageUploadFileName;
    private static Log log = LogFactory.getLog(TraceShowModuleDefineServiceDaoController.class);

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

    public  void deleteTp(){
        String id = getParameter("id");
        Map<String,Object> dataMap = getService().searchById(id);
        String jywj = String.valueOf(dataMap.get("JYWJ"));
        File oldFile = new File(REAL_PATH+"/"+jywj);
        if(oldFile.exists()){
            oldFile.delete();
        }
        setReturnData( getService().updateCdzm(id));
    }
    @Override
    public Object save() throws FatalException {
        //复写保存方法，添加图片上传
        Map map = this.getRequest().getParameterMap();
        setReturnData(getService().save(map, imageUpload, imageUploadFileName, REAL_PATH));
        return SUCCESS;
    }
}

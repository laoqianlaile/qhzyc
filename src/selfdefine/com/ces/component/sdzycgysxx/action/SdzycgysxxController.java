package com.ces.component.sdzycgysxx.action;

import com.ces.component.sdzycgysxx.dao.SdzycgysxxDao;
import com.ces.component.sdzycgysxx.service.SdzycgysxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.util.List;
import java.util.Map;

public class SdzycgysxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycgysxxService, SdzycgysxxDao> {

    private static final long serialVersionUID = 1L;
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
    /**
     * 获得供应商信息下拉框数据
     */
    public void searchGridData(){
        setReturnData(getService().searchgysxxComboGridData());
    }

    /**
     * 获取粗加工供应商信息
     */
    public void  searchYlgysxxByGysbh(){
        String gysbh = getParameter("gysbh");
        setReturnData(getService().searchYlgysxxByGysbh(gysbh));
    }

    /**
     * 获取精加工供应商信息
     */
    public void  searchYcgysxxByGysbh(){
        String gysbh = getParameter("gysbh");
        setReturnData(getService().searchYcgysxxByGysbh(gysbh));
    }



    @Override
    public Object save() throws FatalException {
        //复写保存方法，添加图片上传
        Map map = this.getRequest().getParameterMap();
        setReturnData(getService().save(map, imageUpload, imageUploadFileName, REAL_PATH));
        return SUCCESS;
    }

    public void deleteTp(){
        String id = getParameter("id");
        Map<String,Object> dataMap = getService().searchById(id);
        String yyzzwj = String.valueOf(dataMap.get("YYZZWJ"));
        File oldFile = new File(REAL_PATH+"/"+yyzzwj);
        if(oldFile.exists()){
            oldFile.delete();
        }
        setReturnData( getService().updateYyzzwj(id));
    }

}

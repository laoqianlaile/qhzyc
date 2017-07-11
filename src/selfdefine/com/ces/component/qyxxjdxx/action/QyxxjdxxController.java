package com.ces.component.qyxxjdxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.qyxxjdxx.service.QyxxjdxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class QyxxjdxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, QyxxjdxxService, TraceShowModuleDao> {

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

    public Object getJDXX(){
        setReturnData(getService().getJDXX());
        return SUCCESS;
    }

    //保存基地信息以及照片信息
    public Object saveJDXX() throws IOException {
        Map map = this.getRequest().getParameterMap();
        Map newMap = new HashMap();
        Set<Map.Entry> allSet = map.entrySet();
        Iterator<Map.Entry> iter = allSet.iterator();
        while(iter.hasNext()){
            Map.Entry entry = iter.next();
            String[] valueArray = (String[])entry.getValue();
            String value= "";
            for(int i = 0;i<valueArray.length;i++){
                value += valueArray[i];
            }
            newMap.put(entry.getKey(), value);
            value = "";
        }
        List<String> result = getService().saveJDXX(newMap, imageUpload, imageUploadFileName);
        setReturnData(result);
        return SUCCESS;
    }

    public Object deleteImage(){
        String tplj = getParameter("tplj");
        setReturnData(getService().deleteImage(tplj));
        return SUCCESS;
    }

    /**
     * 获取负责人(工作人员)信息
     * @return
     */
    public Object getFzrxx(){
        setReturnData(getService().getFzrxx());
        return SUCCESS;
    }


}
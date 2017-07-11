package com.ces.component.zzxyqk.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzxyqk.service.ZzxyqkService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ZzxyqkController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzxyqkService, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;
    private List<File> imageUpload;
    private List<String> imageUploadFileName;
    private String menuId;

    public String getMenuId() {
        return menuId;
    }
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
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

    //保存信用情况及图片信息
    public Object saveXyqk() throws IOException {
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

        List<String> result = getService().saveXyqk(newMap, imageUpload, imageUploadFileName);
        setReturnData(result);
        return SUCCESS;
    }

    /**
     * 修改时加载数据
     */
    public Object getXyqkData(){
        String id = getParameter("id");
        setReturnData(getService().getXyqkData(id));
        return SUCCESS;
    }

    public Object deleteImage(){
        String tplj = getParameter("tplj");
        setReturnData(getService().deleteImage(tplj));
        return SUCCESS;
    }

}
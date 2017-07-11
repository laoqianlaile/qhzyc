package com.ces.component.zzcpxxgl.action;

import com.ces.component.trace.utils.JSON;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzcpxxgl.service.ZzcpxxglService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.util.*;

public class ZzcpxxglController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzcpxxglService, TraceShowModuleDao> {

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

    /**
     * 根据产品编号获取产品信息
     */
    public void getCpxxByCpbh(){
        String cpbh = getParameter("cpbh");
        setReturnData(getService().getCpxxByCpbh(cpbh));
    }

    /**
     * 根据产品编号获取配料信息
     */
    public void getPlxxByCpbh(){
        String cpbh = getParameter("cpbh");
        setReturnData(getService().getPlxxByCpbh(cpbh));
    }

    /**
     * 包装管理产品名称下拉列表获取数据
     */
    public void getCpxx(){
        setReturnData(getService().getCpxx());
    }

    /**
     * 统一获取数据字典数据
     * @return
     */
    public Object getSjzdSj(){
        String lxbm = getParameter("lxbm");
        setReturnData(getService().getSjzdSj(lxbm));
        return SUCCESS;
    }


    /**
     * 保存产品信息
     */
    public Object saveCpxx(){
        String formData = getParameter("formData");
        String gridData = getParameter("gridData");
        Map<String,String> forDataMap = JSON.fromJSON(formData, new TypeReference<Map<String, String>>() {
        });
        List<Map<String,String>> gridDataList = JSON.fromJSON(gridData, new TypeReference<List<Map<String, String>>>() {
        });
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

        List<String> result = getService().saveCpxx(newMap, imageUpload, imageUploadFileName, gridDataList);
        setReturnData(result);
        return SUCCESS;
//        setReturnData(getService().saveBzxx(map,list));
    }

    /**
     * 查询品种信息
     * @return
     */
    public Object getPzxx(){
        setReturnData(getService().getPzxx());
        return SUCCESS;
    }

    /**
     * 根据品种信息查询品类信息
     * @return
     */
    public Object getPlxxByPzxx(){
        String plbh = getParameter("plbh");
        setReturnData(getService().getPlxxByPzxx(plbh));
        return SUCCESS;
    }

    /**
     * 修改时查询产品信息（包括产品配料信息）
     * @return
     */
    public Object getCpxxById(){
        String id = getParameter("id");
        setReturnData(getService().getCpxxById(id));
        return SUCCESS;
    }

    public Object deleteImage(){
        String tplj = getParameter("tplj");
        setReturnData(getService().deleteImage(tplj));
        return SUCCESS;
    }
   public String getKhxx(){
       setReturnData(getService().getKhxx());
       return SUCCESS;
   }

    public String getMdxx(){
        String khbh=getParameter("khbh");
        setReturnData(getService().getMdxx(khbh));
        return SUCCESS;
    }
}
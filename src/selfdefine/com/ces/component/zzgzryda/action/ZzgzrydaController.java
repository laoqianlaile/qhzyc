package com.ces.component.zzgzryda.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzgzryda.service.ZzgzrydaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ZzgzrydaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzgzrydaService, TraceShowModuleDao> {

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
    public String getYgbm(){
    	super.setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ", "GZRYBH", false));
    	return null;
    }

    /**
     * 获取粗加工负责人信息
     */
    public void getGzrydaGrid(){
    	String dwlx = getParameter("dwlx");
        this.setReturnData(getService().getGzryda(dwlx));
    }

    /**
     * 获取精加工负责人信息
     */
    public void getJjgGzryxxGrid(){
        String dwlx = getParameter("dwlx");
        this.setReturnData(getService().getJjgGzryxx(dwlx));
    }

    /**
     * 根据单位类型确定工作人员岗位下拉框数据
     */
    public void getGwByDwlx(){
        String dwlx = getParameter("dwlx");
        setReturnData(getService().getGwByDwlx(dwlx));
    }

    public void getGwBySjzd(){
        setReturnData(getService().getGwBySjzd());
    }

    public void getXbBySjzd(){
        setReturnData(getService().getXbBySjzd());
    }

    //保存工作人员信息以及照片信息
    public Object saveGzryxx() throws IOException {
        String dwlx = getParameter("dwlx");
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
        newMap.put("DWLX",dwlx);
        List<String> result = getService().saveGzryxx(newMap, imageUpload, imageUploadFileName);
        setReturnData(result);
        return SUCCESS;
    }

    /**
     * 修改时加载数据
     */
    public Object getGzryxxData(){
        String id = getParameter("id");
        setReturnData(getService().getGzryxxData(id));
        return SUCCESS;
    }
    /**
     * 通过流水号获取工作人员
     */
    public void getGzrybhByLsh(){
        setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ","ZZGZRYBH",false));
    }

    public void getGzryxxByGzrybh(){
        String gzrybh = getParameter("gzrybh");
        setReturnData(getService().getGzryxxByGzrybh(gzrybh));
    }
    /**
     * 查重
     */
    public void validYhm(){
        String yhm = getParameter("yhm");
        setReturnData(getService().validYhm(yhm));
    }
    public Object checkICKBH(){
        String ICKBH=getParameter("ickbh");
        String id=getParameter("id");
        setReturnData(getService().checkICKBH(ICKBH,id));
        return SUCCESS;
    }


    public Object checkGZRYMC(){
        String gzryxm =getParameter("gzryxm");
        String dwlx = getParameter("dwlx");
        setReturnData(getService().checkGZRYXM(gzryxm,dwlx));
        return  SUCCESS;

    }


    public Object deleteImage(){
        String tplj = getParameter("tplj");
        setReturnData(getService().deleteImage(tplj));
        return SUCCESS;
    }



}
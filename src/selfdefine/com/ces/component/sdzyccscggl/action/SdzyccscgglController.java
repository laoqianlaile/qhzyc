package com.ces.component.sdzyccscggl.action;

import com.ces.component.sdzyccscggl.dao.SdzyccscgglDao;
import com.ces.component.sdzyccscggl.service.SdzyccscgglService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.exception.FatalException;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;

import java.io.File;
import java.util.*;

public class SdzyccscgglController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyccscgglService, SdzyccscgglDao> {

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
    public void getCspchGrid(){
        this.setReturnData(getService().getCspch());
    }
    public void searchGridData(){
        this.setReturnData(getService().getylrkcspch());
    }

    public void getZzpchGrid(){
        this.setReturnData(getService().getZzpch());
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
        String tpsc = String.valueOf(dataMap.get("TPSC"));
        File oldFile = new File(REAL_PATH+"/"+tpsc);
        if(oldFile.exists()){
            oldFile.delete();
        }
        setReturnData( getService().updateTpsc(id));
    }
    @Override
    public Object destroy() throws FatalException {
        try {
            // 1. 获取表ID, ID
            String ids      = getId();
            String sqlc="select t.zzpch,t.cspch,t.id,t.sfjy from  t_sdzyc_csglxx t where id IN ('" + ids.replace(",", "','") + "')";
            List<Map<String,Object>> jgpchList = DatabaseHandlerDao.getInstance().queryForMaps(sqlc);
            for (Map<String,Object> dataMap: jgpchList) {
                String cspch = String.valueOf(dataMap.get("CSPCH"));
                String zzpch = String.valueOf(dataMap.get("ZZPCH"));
                String sfjy = String.valueOf(dataMap.get("SFJY"));
                String id = String.valueOf(dataMap.get("ID"));
                if (sfjy.equals("0")) {
                    getService().delete(zzpch,id);
                    getService().sendDelPlantService(id);
                }
            }
        } catch (Exception e) {
            processException(e, BusinessException.class);
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    public void queryJyCspch() {
        String pch = getParameter("cspch");
        setReturnData(getService().queryJyCspch(pch));
    }
    public void queryZzpch() {
        String pch = getParameter("pch");
        setReturnData(getService().queryZzpch(pch));
    }
}

package com.ces.component.sdzyccjgycjgxx.action;

import com.ces.component.sdzyccjgycjgxx.dao.SdzyccjgycjgxxDao;
import com.ces.component.sdzyccjgycjgxx.service.SdzyccjgycjgxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.utils.Base64FileUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.exception.FatalException;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SdzyccjgycjgxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyccjgycjgxxService, SdzyccjgycjgxxDao> {

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
     * 获得药材检验下拉框数据
     */
    public void searchGridData(){
        setReturnData(getService().searchycjgxxComboGridData());
    }/**
     * 获得药材检验合格下拉框数据
     */
    public void searchhgGridData(){
        setReturnData(getService().searchycjgxxhgComboGridData());
    }

    /**
     * 获取企业自检批次号
     */
    public void getqyzjpch(){
     setReturnData(getService().setqyzjpch());
    }

    /**
     * 根据药材检验批次号获得加工批次号的详细信息
     */
    public void searchKhxx(){
        String jgpch = getParameter("jgpch");
        setReturnData(getService().searchycjgxxByckbh(jgpch));
    }

    @Override
    public Object save() throws FatalException {
        boolean isAdd = false;
        //复写保存方法，添加图片上传
        Map map = this.getRequest().getParameterMap();
        Map newMap = new HashMap();
        Set<Map.Entry> allSet = map.entrySet();
        Iterator<Map.Entry> iter = allSet.iterator();
        while(iter.hasNext()){
            Map.Entry entry = iter.next();
            String[] valueArray = (String[])entry.getValue();
            String value = "" ;
            for( int i = 0 ; i < valueArray.length ; i ++){
                value = valueArray[i];
            }
            newMap.put(entry.getKey() , value );
        }
        String id = String.valueOf(newMap.get("ID"));
        if(StringUtil.isEmpty(id)){//是新增操作
            String jgpch = StatisticalCodeUtil.getInstance().getTwentyFivePcm("CJG","SDZYC","SDZYCJGPCH");
            String qyjgpch = jgpch.substring(jgpch.length() - 11, jgpch.length());
            if(!(newMap.get("QYBM").equals("000000807")||newMap.get("QYBM").equals("000000824"))){
                newMap.put("QYJGPCH",qyjgpch);
            }
            newMap.put("JGPCH",jgpch);
            isAdd = true ;
        }
        id  = getService().save("T_SDZYC_CJG_YCJGXX" ,newMap ,null);
        String base64 ="";
        if(!StringUtil.isEmpty(id)){//数据保存成功执行文件上传操作
            if( null != this.imageUpload){
                //进行文件上传，则删除历史保存文件
                File oldFile = new File(REAL_PATH+"/"+newMap.get("XCTP"));
                String[] args = imageUploadFileName.get(0).split("\\.");
                String fileNameWithStamp = System.currentTimeMillis()+ UUIDGenerator.uuid()+"."+args[1];
                File newFile = new File(REAL_PATH+"/"+fileNameWithStamp);
                try {
                    if(oldFile.exists()){
                        oldFile.delete();
                    }
                    FileUtils.copyFile(this.imageUpload.get(0),newFile);
                    try {
                        base64= Base64FileUtil.encodeBase64File(imageUpload.get(0).getPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //文件上传成功进行数据保存
                    newMap.put("ID",id);
                    newMap.put("XCTP",fileNameWithStamp);
                    getService().save("T_SDZYC_CJG_YCJGXX" ,newMap ,null);
                    try{
                        newMap.put("base64", base64);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    //上传文件失败删除保存数据和图片
                    getService().delete(id);
                    if(newFile.exists()){
                        oldFile.delete();
                    }
                }

            }
        }
        String sql = "update t_sdzyc_cjg_ylllxx set issc='1' where lldh=?";
        DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{newMap.get("LLDH")});
        if(isAdd){
            getService().sendCreateImprecisionService(newMap);
        }else{
            getService().sendModifyImprecisionService(id,newMap);
        }
        setReturnData(newMap);
        return null;
    }

    public void deleteTp(){
        String id = getParameter("id");
        Map<String,Object> dataMap = getService().searchById(id);
        String xctp = String.valueOf(dataMap.get("XCTP"));
        File oldFile = new File(REAL_PATH+"/"+xctp);
        if(oldFile.exists()){
            oldFile.delete();
        }
        setReturnData( getService().updateXctp(id));
    }
    @Override
    public Object destroy() throws FatalException {
        try {
            // 1. 获取表ID, ID
            String ids      = getId();
            String sqlc="select t.lldh,t.id from  t_sdzyc_cjg_ycjgxx t where id IN ('" + ids.replace(",", "','") + "')";
            List<Map<String,Object>> jgpchList = DatabaseHandlerDao.getInstance().queryForMaps(sqlc);
            for (Map<String,Object> dataMap: jgpchList) {
                String lldh = String.valueOf(dataMap.get("LLDH"));
                String id = String.valueOf(dataMap.get("ID"));
                getService().delete(lldh,id);
                getService().sendDelImprecisionService(id);
            }
        } catch (Exception e) {
            processException(e, BusinessException.class);
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    public void queryPch() {
        String pch = getParameter("pch");
        setReturnData(getService().queryPch(pch));
    }
}

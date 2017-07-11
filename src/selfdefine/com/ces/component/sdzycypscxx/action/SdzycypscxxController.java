package com.ces.component.sdzycypscxx.action;

import com.ces.component.sdzycypscxx.dao.SdzycypscxxDao;
import com.ces.component.sdzycypscxx.service.SdzycypscxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
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

public class SdzycypscxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycypscxxService, SdzycypscxxDao> {

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
     * 获取精加工饮片生产编号下拉列表数据
     */
    public void searchPchGridData(){
        setReturnData(getService().searchYpscbhxxComboGridData());
    }

    /**
     * 获取领料单号下拉列表的数据
     */
    public void searchLldhData(){
        setReturnData(getService().searchLldhData());
    }
    public void searchLlycxxData(){
        String lldh = getParameter("lldh");
        setReturnData(getService().searchLlycxxData(lldh));
    }

    public void getJggymc(){
        String jggy = getParameter("jggy");
        setReturnData(getService().searchJggymc(jggy));
    }
    /**
     * 自定义删除图片
     */
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
    public Object save() throws FatalException {
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
        //获取页面id判断是否是新增 修改
        String id = String.valueOf(newMap.get("ID"));
        if(StringUtil.isEmpty(id)){//是新增操作，则生成饮片生产批次号
            String jjggysbh = StatisticalCodeUtil.getInstance().getTwentyFivePcm("JJG","SDZYC", "SDZYCJJGYPSCPCH");
             String qyscpch= jjggysbh.substring(jjggysbh.length() - 11, jjggysbh.length());
            String jyjg="0";
            newMap.put("SCPCH",jjggysbh);
            newMap.put("JYJG",jyjg);
            newMap.put("QYSCPCH",qyscpch);
            newMap.put("QYBM",SerialNumberUtil.getInstance().getCompanyCode());
            newMap.put("SCRQ",String.valueOf(newMap.get("SCRQ")).substring(0,10));
            id  = getService().save("T_SDZYC_JJG_YPSCXX" ,newMap ,null);
            try {
                if(null !=imageUpload) {
                    newMap.put("base64", getService().encodeBase64File(imageUpload.get(0)));
                    newMap.put("imageName", imageUploadFileName.get(0));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            getService().sendCreatePrecisionService(newMap);
        }else {
            id = getService().save("T_SDZYC_JJG_YPSCXX", newMap, null);
        }
        newMap.put("QYBM",SerialNumberUtil.getInstance().getCompanyCode());
        newMap.put("SCRQ",String.valueOf(newMap.get("SCRQ")).substring(0,10));
        id  = getService().save("T_SDZYC_JJG_YPSCXX" ,newMap ,null);
        String sql = "update t_sdzyc_jjg_scllxx set issc='1' where lldh=?";
        DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{newMap.get("LLDH")});
        newMap.put(AppDefineUtil.C_ID, id);
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
                    FileUtils.copyFile(this.imageUpload.get(0), newFile);
                    //文件上传成功进行数据保存
                    newMap.put("ID",id);
                    newMap.put("XCTP",fileNameWithStamp);
                    getService().save("T_SDZYC_JJG_YPSCXX" ,newMap ,null);

                } catch (IOException e) {
                    //上传文件失败删除保存数据和图片
                    getService().delete(id);
                    if(newFile.exists()){
                        oldFile.delete();
                    }
                }

            }
        }
        setReturnData(newMap);
        return null;
    }
    @Override
    public Object destroy() throws FatalException {
        try {
            // 1. 获取表ID, ID
            String ids      = getId();
            String sqlc="select t.lldh, t.pid from  t_sdzyc_jjg_ypsctl t where pid IN ('" + ids.replace(",", "','") + "')";
            List<Map<String,Object>> jgpchList = DatabaseHandlerDao.getInstance().queryForMaps(sqlc);
            for (Map<String,Object> dataMap: jgpchList) {
                String lldh = String.valueOf(dataMap.get("LLDH"));
                String id = String.valueOf(dataMap.get("PID"));
                getService().delete(lldh,id);
                getService().sendDelPrecisionService(id);
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

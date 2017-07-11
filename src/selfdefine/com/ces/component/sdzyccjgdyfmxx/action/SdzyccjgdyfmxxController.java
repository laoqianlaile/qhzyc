package com.ces.component.sdzyccjgdyfmxx.action;

import com.ces.component.sdzyccjgdyfmxx.dao.SdzyccjgdyfmxxDao;
import com.ces.component.sdzyccjgdyfmxx.service.SdzyccjgdyfmxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.utils.Base64FileUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;
import com.fasterxml.jackson.databind.JsonNode;
import enterprise.entity.TraceEntity;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

@Results({@Result(name = "downLoad", type = "stream", params = {
        "contentType", "application/octet-stream",
        "inputName", "inputStream",
        "contentDisposition","attachment;filename=\"${downfileName}\"",
        "bufferSize","4096" }),
        @Result(name="nofileexists",location="/500.jsp")})
public class SdzyccjgdyfmxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyccjgdyfmxxService, SdzyccjgdyfmxxDao> {

    private static final long serialVersionUID = 1L;
    private String downfileName;
    private InputStream inputStream;
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
    //private final String REAL_PATH_ZSM = ServletActionContext.getServletContext().getRealPath("/zsm");

    public InputStream getInputStream() throws FileNotFoundException {
        return inputStream;
    }
    public String getDownloadFileName(){
        return downfileName;
    }
    public String getDownfileName() {
        return downfileName;
    }

    public void setDownfileName(String downfileName) {
        this.downfileName = downfileName;
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
    public String downLoad() throws FileNotFoundException, UnsupportedEncodingException {
        String bzpch = getParameter("bzpch");
        // 取到服务器上传文件存放的路径
        String path = ServletActionContext.getServletContext().getRealPath("/zsm/");
        downfileName = bzpch+".txt";
        String FilePath = path +"/" +downfileName;
        // 取到上传文件的完整路径
        String agent = ServletActionContext. getRequest().getHeader("User-agent");
        if(agent.contains("MSIE")){
            this.downfileName = URLEncoder.encode(downfileName, "UTF-8");
        }else{
            this.downfileName = new String(downfileName.getBytes(),"ISO-8859-1");
        }
        if(new File(FilePath).exists()== false){

            return "nofileexists";
        }else{
            inputStream = new FileInputStream(new File(FilePath));
            return "downLoad";
        }
    }


    public void demandBzpch(){
        String bzpch=getParameter("bzpch");
        setReturnData(getService().inquirebzpch(bzpch));
    }

    /**
     * 删除图片操作
     */
    public void deleteTp(){
        String id = getParameter("id");
        Map<String,Object> dataMap = getService().searchById(id);
        String jywj = String.valueOf(dataMap.get("BZTP"));
        File oldFile = new File(REAL_PATH+"/"+jywj);
        if(oldFile.exists()){
            oldFile.delete();
        }
        setReturnData( getService().updateBzwj(id));
    }

    /**
     * 重写save 方法  保存  上传的图片 和自动生成的包装编号和二维码生成的txt文件
     * @return
     * @throws FatalException
     */
    public Object save() throws FatalException {
       // String tableName = getService().getTableName(tableId);
       // JsonNode entityNode = JsonUtil.json2node(entityJson);
        //Map<String, String> dataMap =node2map(entityNode);
       // String id = dataMap.get("ID");
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
            newMap.put(String.valueOf(entry.getKey()+""), value );
        }
        String id = String.valueOf(newMap.get("ID"));
        if(StringUtil.isEmpty(id)){//是新增操作
            String ycbzpch = StatisticalCodeUtil.getInstance().getTwentyFivePcm("CJG","SDZYC", "SDZYCCJGBZPCH");
             String qybzpch = ycbzpch.substring(ycbzpch.length() - 11, ycbzpch.length());
            newMap.put("QYBZPCH",qybzpch);
            newMap.put("BZPCH", ycbzpch);
            isAdd = true ;
        }
        newMap.put("QYBM", SerialNumberUtil.getInstance().getCompanyCode());
        id  = getService().save("T_SDZYC_CJG_DYFMXX" ,newMap ,null);
        String updSql ="update t_sdzyc_cjg_ycjgxx set sffm='1' where  jgpch=?";
        DatabaseHandlerDao.getInstance().executeSql(updSql,new String[]{String.valueOf(newMap.get("SCPCH"))});
        String base64 ="";
        if(!StringUtil.isEmpty(id)){//数据保存成功执行文件上传操作
            if( null != this.imageUpload){
                //进行文件上传，则删除历史保存文件
                File oldFile = new File(REAL_PATH+"/"+newMap.get("BZTP"));
                String[] args = imageUploadFileName.get(0).split("\\.");
                String fileNameWithStamp = System.currentTimeMillis()+ UUIDGenerator.uuid()+"."+args[1];
                File newFile = new File(REAL_PATH+"/"+fileNameWithStamp);
                try {
                    if(oldFile.exists()){
                        oldFile.delete();
                    }
                    FileUtils.copyFile(this.imageUpload.get(0), newFile);
                    try {
                        base64= Base64FileUtil.encodeBase64File(imageUpload.get(0).getPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //文件上传成功进行数据保存
                    newMap.put("ID",id);
                    newMap.put("BZTP",fileNameWithStamp);

                    getService().save("T_SDZYC_CJG_DYFMXX" ,newMap ,null);
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
        String zsspm = String.valueOf(newMap.get("YCDM"));
        if(isAdd){
            getService().sendCreateImprecisionPackService(newMap);
        }else{
            getService().sendModifyImprecisionPackService(id,newMap);
        }
        /// 根据包装批次号进行二维码生成。
        if ( isAdd || (null != map  && !map.get("BZSL").equals(newMap.get("BZSL")))){//存在老的数据在判断包装的数据量是否有变化，没有不进行二维码生成，有编号重新生成二维码编码
            int count = Integer.parseInt(String.valueOf(newMap.get("BZSL")));
            getService().deleteOldZsm(String.valueOf(newMap.get("BZPCH")));
            List<TraceEntity> traceEntities = new ArrayList<TraceEntity>();
            boolean firstwrit = false ;
            for(int i = 0 ; i< count ; i++){
                String bzpch = String.valueOf(newMap.get("BZPCH"));
                String zsm = StatisticalCodeUtil.getInstance().getThirtyFiveZsmZyc("CJG", "4",zsspm,"SDZYC","SDZYCCJGBZZSM");
                getService().saveBzzsm(bzpch, zsm,newMap);
                //保存成功后写入txt中，
                try {
                    getService().writText(bzpch, zsm, firstwrit);
                    traceEntities.add(getService().traceEntityInfo(zsm,newMap));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                firstwrit = true;

            }
            getService().sendCreatePreLoadService(traceEntities);
        }
        setReturnData(newMap);
        return null;
    }
    @Override//复写删除方法（删除掉药材检验里面的数据会把药材加工里面的是否检验状态修改成已删除）
    public Object destroy() throws FatalException {
        try {
            // 1. 获取表ID, ID
            String tableId = getParameter(P_TABLE_ID);
            String dTableId = getParameter(P_D_TABLE_IDS);
            String ids = getId();
            String sql="select t.bzpch,t.scpch,t.id from  t_sdzyc_cjg_dyfmxx t where id IN ('" + ids.replace(",", "','") + "')";
            List<Map<String,Object>> jgpchList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
            //执行复写的删除方法


            for (Map<String,Object> dataMap: jgpchList){
                String bzpch =String.valueOf(dataMap.get("BZPCH"));
                String id =String.valueOf(dataMap.get("ID"));
                getService().delete(bzpch,id);
                String Updatesql="update t_sdzyc_cjg_ycjgxx  set sffm='0' where jgpch=?";
                DatabaseHandlerDao.getInstance().executeSql(Updatesql,new Object[]{String.valueOf(dataMap.get("SCPCH"))});
            }
            setReturnData(MessageModel.trueInstance("删除成功！"));
        } catch (Exception e) {
            setReturnData(MessageModel.falseInstance(e.getMessage()));
        }
        return SUCCESS;
    }


}

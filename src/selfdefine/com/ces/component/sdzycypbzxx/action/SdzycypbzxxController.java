package com.ces.component.sdzycypbzxx.action;

import com.ces.component.sdzycypbzxx.dao.SdzycypbzxxDao;
import com.ces.component.sdzycypbzxx.service.SdzycypbzxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.utils.Base64FileUtil;
import com.ces.component.trace.utils.ImageCompressUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;
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
        @Result(name="nofileexists",location="/500.jsp")}
)
public class SdzycypbzxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycypbzxxService, SdzycypbzxxDao> {

    private static final long serialVersionUID = 1L;
    private String downfileName;
    private InputStream inputStream;
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
     *山东中药材精加工药材包装生产批次号下拉列表编码
     */
    public void searchPchGridData(){
        setReturnData(getService().searchBzxxscpchComboGridData());
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
            newMap.put(entry.getKey() , value );
        }
        String id = String.valueOf(newMap.get("ID"));
        String base64 = String.valueOf(newMap.get("BZTP"));
        String imageSuffix = String.valueOf(newMap.get("IMGSUFFIX"));
        newMap.remove("BZTP");
        newMap.remove("IMGSUFFIX");
        //进行文件上传，则删除历史保存文件
        String filename = ImageCompressUtil.getInstance().encordMd5(base64)+"."+imageSuffix;
        File file = new File(REAL_PATH+"/"+filename);
        if(!file.exists()){//文件上传重复，不再进行文件上传操作
            try {
                Base64FileUtil.decoderBase64File(base64,REAL_PATH+"/"+filename);//base64文件上传
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        newMap.put("BZTP",filename);
        newMap.put("QYBM",SerialNumberUtil.getInstance().getCompanyCode());

        String sql = "select id from t_sdzyc_jjg_ypscxx where scpch = ? and qybm = ?";
        Map<String,Object> mapData = DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{String.valueOf(newMap.get("SCPCH")),SerialNumberUtil.getInstance().getCompanyCode()});
        String processId = String.valueOf(mapData.get("ID"));

        if(StringUtil.isEmpty(id)){//是新增操作
            String jjggysbh = StatisticalCodeUtil.getInstance().getTwentyFivePcm("JJG","SDZYC","SDZYCJJGBZPCH");
            newMap.put("BZPCH", jjggysbh);
            newMap.put("QYBZPCH", jjggysbh.substring(jjggysbh.length()-11,jjggysbh.length()));
            id  = getService().save("T_SDZYC_JJG_YPBZXX" ,newMap ,null);
            newMap.put("base64",base64);
            newMap.put("imageName",filename);
            newMap.put("process_id",processId==null?"":processId.toString());
            getService().sendCreatePrecisionPackService(newMap);
            //包装信息保存完成执行zsm信息保存并同步省平台数据
            String zsspm = String.valueOf(newMap.get("YCDM"));
            int count = Integer.parseInt(String.valueOf(newMap.get("BZSL")));
            List<TraceEntity> traceEntities = new ArrayList<TraceEntity>();
            boolean firstwrit = false ;
            //保存二维码数据的同时处理省平台同步所需数据
            for(int i = 0 ; i< count ; i++){
                String bzpch = String.valueOf(newMap.get("BZPCH"));
                String zsm = StatisticalCodeUtil.getInstance().getThirtyFiveZsmYp("JJG", "5",zsspm,"SDZYC","SDZYCJJGBZZSM");
                getService().saveBzzsm(bzpch, zsm,newMap);
                //保存成功后写入txt中，
                getService().writText(bzpch, zsm, firstwrit);
                traceEntities.add(getService().traceEntityInfo(zsm,newMap));
                firstwrit = true;
            }
            getService().sendCreatePreLoadService(traceEntities);
        }else{
            getService().save("T_SDZYC_JJG_YPBZXX" ,newMap ,null);
            newMap.put("base64",base64);
            newMap.put("imageName",filename);
            newMap.put("process_id",processId==null?"":processId.toString());
            getService().sendModifyPrecisionPackService(id,newMap);
        }
        setReturnData(newMap);
        return null;
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
    public void demandBzpch(){
        String bzpch=getParameter("bzpch");
        setReturnData(getService().inquirebzpch(bzpch));
    }
    public void queryScPch() {
        String pch = getParameter("pch");
        setReturnData(getService().queryScPch(pch));
    }
    public void queryPch() {
        String pch = getParameter("pch");
        setReturnData(getService().queryPch(pch));
    }
}

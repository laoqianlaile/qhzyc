package com.ces.component.sdzycjdxx.action;

import com.ces.component.sdzycjdxx.dao.SdzycjdxxDao;
import com.ces.component.sdzycjdxx.service.SdzycjdxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SdzycjdxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycjdxxService, SdzycjdxxDao> {

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
//        String id  = getService().save("T_SDZYC_JDXX" ,newMap ,null);
        String id = String.valueOf(newMap.get("ID"));
        if(StringUtil.isEmpty(id)){//是新增操作
            String jdbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCJDBH", false);
            newMap.put("JDBH",jdbh);
        }
        id = getService().save("T_SDZYC_JDXX" ,newMap ,null);
        newMap.put(AppDefineUtil.C_ID, id);
        if(!StringUtil.isEmpty(id)){//数据保存成功执行文件上传操作
            if( null != this.imageUpload){
                //进行文件上传，则删除历史保存文件
                File oldFile = new File(REAL_PATH+"/"+newMap.get("CDZM"));

                String fileNameWithStamp = System.currentTimeMillis() + "_" + imageUploadFileName.get(0);
                File newFile = new File(REAL_PATH+"/"+fileNameWithStamp);
                try {
                    if(oldFile.exists()){
                        oldFile.delete();
                    }
                    FileUtils.copyFile(this.imageUpload.get(0),newFile);
                    //文件上传成功进行数据保存
                    newMap.put("ID",id);
                    newMap.put("CDZM",fileNameWithStamp);
                    getService().save("T_SDZYC_JDXX" ,newMap ,null);
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

    public void deleteTp(){
        String id = getParameter("id");
        Map<String,Object> dataMap = getService().searchById(id);
        String cdzm = String.valueOf(dataMap.get("CDZM"));
        File oldFile = new File(REAL_PATH+"/"+cdzm);
        if(oldFile.exists()){
            oldFile.delete();
        }
        setReturnData( getService().updateCdzm(id));
    }
}

package com.ces.component.sdzycyljyjcxx.action;

import com.ces.component.sdzycyljyjcxx.dao.SdzycyljyjcxxDao;
import com.ces.component.sdzycyljyjcxx.service.SdzycyljyjcxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SdzycyljyjcxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycyljyjcxxService, SdzycyljyjcxxDao> {

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
    public void getZzpchGrid(){
        this.setReturnData(getService().getZzpch());
    }
    public void getCszzpchGrid(){
        this.setReturnData(getService().getCszzpch());
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
        String jywj = String.valueOf(dataMap.get("JYWJ"));
        File oldFile = new File(REAL_PATH+"/"+jywj);
        if(oldFile.exists()){
            oldFile.delete();
        }
        setReturnData( getService().updateJywj(id));
    }
    @Override//复写删除方法（删除掉药材检验里面的数据会把药材加工里面的是否检验状态修改成已删除）
    public Object destroy() throws FatalException {
        try {
            // 1. 获取表ID, ID
            String tableId = getParameter(P_TABLE_ID);
            String dTableId = getParameter(P_D_TABLE_IDS);
            String ids = getId();
            String sql="select t.zzpch from  t_sdzyc_yljyjcxx t where id IN ('" + ids.replace(",", "','") + "')";
            List<Map<String,Object>> jgpchList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
            //执行复写的删除方法
            getService().delete(tableId, dTableId, ids, false, null);

            for (Map<String,Object> dataMap: jgpchList){
                String Updatesql="update t_zz_scda  set jyjg='0' where zzpch=?";
                DatabaseHandlerDao.getInstance().executeSql(Updatesql,new Object[]{String.valueOf(dataMap.get("ZZPCH"))});
            }
            setReturnData(MessageModel.trueInstance("删除成功！"));
        } catch (Exception e) {
            setReturnData(MessageModel.falseInstance(e.getMessage()));
        }
        return SUCCESS;
    }
}

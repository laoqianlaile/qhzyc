package com.ces.component.zzpzxgym.action;

import com.ces.component.trace.utils.DataDictionaryUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzpzxgym.service.ZzpzxgymService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

import java.io.File;
import java.util.*;

/**
 * 种植新增或修改的页面controller
 * Created by bdz on 2015/8/13.
 */
public class ZzpzxgymController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzpzxgymService, TraceShowModuleDao> {

    private File imageUpload;
    private String imageUploadFileName;

    public File getImageUpload() {
        return imageUpload;
    }

    public void setImageUpload(File imageUpload) {
        this.imageUpload = imageUpload;
    }

    public String getImageUploadFileName() {
        return imageUploadFileName;
    }

    public void setImageUploadFileName(String imageUploadFileName) {
        this.imageUploadFileName = imageUploadFileName;
    }

    @Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }

    /**
     * 获取品种编号
     */
    public void getPzbh(){
        String pzbh = SerialNumberUtil.getInstance().getSerialNumber("ZZ","ZZPZBH",false);
        setReturnData(pzbh);
    }

    /**
     * 获取认证类型
     */
    public void getRzlx(){
        List li = DataDictionaryUtil.getInstance().getDictionaryData("RZLX");
        setReturnData(li);
    }

    /**
     * 保存品种信息
     */
    public Object savePzxx() {
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
        }
        String masterId = getService().savePzxx(newMap,imageUpload,imageUploadFileName);
        setReturnData(masterId);
        return SUCCESS;
    }

	/**
	 * 根据编号查询品种
	 * @return
	 */
	public Object getPzById() {
		String treeNodeId = getParameter("treeNodeId");
		setReturnData(getService().getPzById(treeNodeId));
		return SUCCESS;
	}

}
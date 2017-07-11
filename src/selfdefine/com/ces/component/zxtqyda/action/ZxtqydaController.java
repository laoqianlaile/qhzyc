package com.ces.component.zxtqyda.action;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.Map.Entry;

import com.ces.component.zxtqyda.service.ZxtqydaService;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
@Results({
	//@Result(name="zxtqyda",location="/cfg-resource/coral40/views/component/zxtqyda/zxtqyda.jsp")
})

public class ZxtqydaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZxtqydaService, TraceShowModuleDao>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<File> imageUpload;
	private List<String> imageUploadFileName;
	private File logoImageUpload;
	private String logoImageUploadFileName;
	private String menuId;
	private String xtlx;
	public ZxtqydaController() {
	}

	public String getXtlx(){ return xtlx;}
	public void setXtlx(String xtlx){this.xtlx = xtlx; }
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
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
	@Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }
	@Autowired
	@Override
	protected void setService(ZxtqydaService service){
		super.setService(service);
	}

	//获取企业档案
	public void getQydaByMenuId(){
		String dwlx = getParameter("xtlx");
		super.setReturnData(getService().getQydaByMenu(menuId, dwlx));
	}

	//保存企业档案及图片信息
	public Object saveQyda() throws IOException {
		Map map = this.getRequest().getParameterMap();
		Map newMap = new HashMap();
		Set<Entry> allSet = map.entrySet();
		Iterator<Entry> iter = allSet.iterator();
		while(iter.hasNext()){
			Entry entry = iter.next();
			String[] valueArray = (String[])entry.getValue();
			String value= "";
			for(int i = 0;i<valueArray.length;i++){
				value += valueArray[i];
			}
			newMap.put(entry.getKey(), value);
			value = "";
		}
		newMap.remove("ZZJDDZ");
		List<String> result = getService().saveQyda(newMap,imageUpload,imageUploadFileName,logoImageUpload,logoImageUploadFileName);
		setReturnData(result);
		return SUCCESS;
	}

	//删除图片
	public Object deleteImage(){
		String tplj = getParameter("tplj");
		String status = getParameter("status");
		String type = getParameter("type");
		try {
			tplj = URLDecoder.decode(tplj,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		setReturnData(getService().deleteImage(tplj,type));
		return SUCCESS;
	}

	public File getLogoImageUpload() {
		return logoImageUpload;
	}

	public void setLogoImageUpload(File logoImageUpload) {
		this.logoImageUpload = logoImageUpload;
	}

	public String getLogoImageUploadFileName() {
		return this.logoImageUploadFileName;
	}
	public void setLogoImageUploadFileName(String logoImageUploadFileName) {
		this.logoImageUploadFileName = logoImageUploadFileName;
	}
}
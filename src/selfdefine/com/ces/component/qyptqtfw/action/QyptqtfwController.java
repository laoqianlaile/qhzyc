package com.ces.component.qyptqtfw.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.qyptqtfw.service.QyptqtfwService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.utils.CodeUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.web.struts2.StringIDDefineServiceDaoController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业平台前台服务Controller
 * @author bdz
 *
 */
public class QyptqtfwController extends StringIDDefineServiceDaoController<StringIDEntity, QyptqtfwService, TraceShowModuleDao>{

	private final Log log = LogFactory.getLog(this.getClass());

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String sqsm;//申请说明
	private String xdsj;//续订时间
	private String fwmc;
	
	@Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }
	@Autowired
	@Override
	protected void setService(QyptqtfwService service){
		super.setService(service);
	}
	
	/**
	 * 获取企业基本服务
	 */
	public Object getBasicService(){
		Map<String,String> mapValue = this.getService().getJbfw(SerialNumberUtil.getInstance().getCompanyCode());//"101"应从session中读取
		setReturnData(mapValue);
		return SUCCESS;
	}
	/**
	 * 变更服务对话框获取服务名称
	 * @return
	 */
	public Object getJbfwmc(){
		String fwmc = this.getService().getJbfwmc(SerialNumberUtil.getInstance().getCompanyCode());
		setReturnData(fwmc);
		return SUCCESS;
	}
	/**
	 * 基本服务申请变更
	 */
	public Object applyJbfw(){
		try {
			this.getService().createFwsq(SerialNumberUtil.getInstance().getCompanyCode(), sqsm,"2","","1","基本服务");// "101"应从session中读取,1新增，2变更，3续订
			setReturnData(SUCCESS);
		} catch (Exception e) {
			log.error("基本服务申请变更错误：" + e.getMessage());
			setReturnData(ERROR);
		}
		return SUCCESS;
	}
	/**
	 * 基本服务申请续订
	 */
	public Object conJbfw(){
		try {
			this.getService().createFwsq(SerialNumberUtil.getInstance().getCompanyCode(), sqsm,"3",xdsj,"1","基本服务");// 1新增，2变更，3续订 1基本服务 2增值服务
			setReturnData(SUCCESS);
		} catch (Exception e) {
			log.error("基本服务续订变更错误：" + e.getMessage());
			setReturnData(ERROR);
		}
		return SUCCESS;
	}
	/**
	 * 增值服务下拉框
	 */
	public Object getZzfw(){
		List<Map<String,String>> li = this.getService().getZzfwList();//获取续订时长编码
		setReturnData(toSelectList(li));
		return SUCCESS;
	}
	/**
	 * 增值服务申请
	 */
	public Object sqZzfw(){
		try {
			this.getService().createFwsq(SerialNumberUtil.getInstance().getCompanyCode(), sqsm,"1","","2",fwmc);// 1新增，2变更，3续订 1基本服务 2增值服务
			setReturnData(SUCCESS);
		} catch (Exception e) {
			setReturnData(ERROR);
		}
		return SUCCESS;
	}
	/**
	 * 获取增值服务列表list
	 * @return
	 */
	public Object getZzfwList(){
		//分页处理
		PageRequest pageRequest = this.buildPageRequest();
		list = getDataModel(getModelTemplate());
		
		Page<Object> page = getService().getZzfwPage(pageRequest,SerialNumberUtil.getInstance().getCompanyCode());
        list.setData(page);
		return NONE;
	}
	/**
	 * 基本服务时长编码
	 */
	public Object getJbfwscCode(){
		List<Code> li = CodeUtil.getInstance().getCodeList("xdsc");//获取续订时长编码
		setReturnData(toMapList(li));
		return SUCCESS;
	}
	/**
	 * 转换codeList为List<Map<String,String>> 
	 * Map中存放为text,value
	 * 用于下拉框使用
	 * @param li
	 * @return
	 */
	public List<Map<String,String>> toMapList(List<Code> li){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for(Code code :li){
			Map<String,String> mapValue = new HashMap<String,String>();
			mapValue.put("text", code.getName());
			mapValue.put("value", code.getValue());
			list.add(mapValue);
		}
		return list;
	}
	public List<Map<String,String>> toSelectList(List<Map<String,String>> li){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for(Map<String,String> map :li){
			Map<String,String> mapValue = new HashMap<String,String>();
			mapValue.put("text", map.get("FWMC"));
			mapValue.put("value", map.get("FWMC"));
			list.add(mapValue);
		}
		return list;
	}
	public String getSqsm() {
		return sqsm;
	}
	public void setSqsm(String sqsm) {
		this.sqsm = sqsm;
	}
	public String getXdsj() {
		return xdsj;
	}
	public void setXdsj(String xdsj) {
		this.xdsj = xdsj;
	}
	public String getFwmc() {
		return fwmc;
	}
	public void setFwmc(String fwmc) {
		this.fwmc = fwmc;
	}
}
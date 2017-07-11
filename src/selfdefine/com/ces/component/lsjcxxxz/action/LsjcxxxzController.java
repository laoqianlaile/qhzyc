package com.ces.component.lsjcxxxz.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.component.jclhxz.service.JclhxzService;
import com.ces.component.lsjcxxxz.dao.LsjcxxxzDao;
import com.ces.component.lsjcxxxz.service.LsjcxxxzService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class LsjcxxxzController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, LsjcxxxzService, LsjcxxxzDao> {

    private static final long serialVersionUID = 1L;
    private String zztmh;
    private String pctmh;
    
    public String getZztmh() {
		return zztmh;
	}

	public void setZztmh(String zztmh) {
		this.zztmh = zztmh;
	}

	public String getPctmh() {
		return pctmh;
	}

	public void setPctmh(String pctmh) {
		this.pctmh = pctmh;
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
    
    @Autowired
    @Qualifier("lsjcxxxzService")
    @Override
    protected void setService(LsjcxxxzService service) {
        super.setService(service);
    }
    
    public Object getScjcbh(){
    	String scjcbh = SerialNumberUtil.getInstance().getSerialNumber("LS", "SCJCBH", true);
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("scjcbh", scjcbh);
    	setReturnData(map);
    	return null;
    }
    /**
     * 获得默认页面元素值
     * @return
     */
    public Object initFormData(){
    	String jhpch = SerialNumberUtil.getInstance().getSerialNumber("LS", "JHPCH",true );
    	String lsscbm = SerialNumberUtil.getInstance().getCompanyCode();
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("jhpch", jhpch);
    	map.put("lsscbm", lsscbm);
    	setReturnData(map);
    	return null;
    }
    //根据进场编号获得菜品名称
    public Object getSpmcByJcbh(){
    	String jcbh = this.getRequest().getParameter("jcbh");
    	super.setReturnData(getService().getSpmcByJcbh(jcbh));
    	return SUCCESS;
    }
  //获得所有菜品名称
    public Object getAllSpmc(){
    	super.setReturnData(getService().getSpmc());
    	return SUCCESS;
    }
    
    //根据条码获取种植出场信息
    public void getZzccxx(){
    	try {
			Map<String,Object> result = getService().getZzccxx(zztmh);
			if(result.isEmpty()){
				super.setReturnData("ERROR");
			}else {
				super.setReturnData(result);
			}
		} catch (Exception e) {
			System.out.print(e);
			super.setReturnData("ERROR");
		}
    }
    
    //根据条码获取批菜交易信息
    public void getPcjyxx(){
    	try {
			Map<String,Object> result = getService().getPcjyxx(pctmh);
			Map<String,Object> map = (Map<String,Object>)result.get("lhxx");
			List<Map<String,Object>> listMap = (List<Map<String,Object>>)result.get("jymxxx");
			if(map.isEmpty()||listMap.size() == 0){
				super.setReturnData("ERROR");
			}else {
				super.setReturnData(result);
			}
		} catch (Exception e) {
			System.out.print(e);
			super.setReturnData("ERROR");
		}
    }
}

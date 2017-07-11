package com.ces.component.jclhxz.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.jclhxz.service.JclhxzService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.xarch.core.entity.StringIDEntity;

public class JclhxzController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, JclhxzService, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;
    private String zztmh;//种植出场批次号
    private String pctmh;//批菜交易编号
    
	
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
    @Qualifier("jclhxzService")
    @Override
    protected void setService(JclhxzService service) {
        super.setService(service);
    }
    
    /*public Object initFormData(){
    	String jhpch = SerialNumberUtil.getInstance().getSerialNumber("PC","JHPCH",true);
    	String jclhbh = SerialNumberUtil.getInstance().getSerialNumber("PC","JCLHBH",true);
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("jhpch", jhpch);
    	map.put("jclhbh", jclhbh);
    	setReturnData(map);
    	return null;
    }*/
    
    public void getJclhbh(){//获取进场理货编号
    	String jclhbh = SerialNumberUtil.getInstance().getSerialNumber("PC","JCLHBH",true);
    	super.setReturnData(jclhbh);
    }
    
    public void getJhpch(){//获取进货批次号
    	String jhpch = SerialNumberUtil.getInstance().getSerialNumber("PC","JHPCH",true);
    	super.setReturnData(jhpch);
    }
    
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
package com.ces.component.csjcmxxx.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.csjcmxxx.service.CsjcmxxxService;
import com.ces.component.csscjcxx.service.CsscjcxxService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class CsjcmxxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CsjcmxxxService, TraceShowModuleDao> {

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
    @Qualifier("csjcmxxxService")
    @Override
    protected void setService(CsjcmxxxService service) {
        super.setService(service);
    }
    /**
     * 获取蔬菜零售品证号
     * @return
     */
    public Object getLspzh(){
    	String lspzh = SerialNumberUtil.getInstance().getSerialNumber("CS", "LSPZH", true);
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("lspzh", lspzh);
    	setReturnData(map);
    	return null;
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
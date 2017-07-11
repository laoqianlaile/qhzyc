package com.ces.component.lsrpjc.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.component.lsjcxxxz.service.LsjcxxxzService;
import com.ces.component.lsrpjc.dao.LsrpjcDao;
import com.ces.component.lsrpjc.service.LsrpjcService;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class LsrpjcController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, LsrpjcService, LsrpjcDao> {

    private static final long serialVersionUID = 1L;
    private String tztmh;
    private String prtmh;
    
    public String getTztmh() {
		return tztmh;
	}
    
	public void setTztmh(String tztmh) {
		this.tztmh = tztmh;
	}
	
	public String getPrtmh() {
		return prtmh;
	}
	
	public void setPrtmh(String prtmh) {
		this.prtmh = prtmh;
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
    @Qualifier("lsrpjcService")
    @Override
    protected void setService(LsrpjcService service) {
        super.setService(service);
    }
    
    public Object getAllRpmc(){
    	super.setReturnData(getService().getSpmc());
    	return SUCCESS;
    }
    public Object initFormData(){
    	String jhpch = SerialNumberUtil.getInstance().getSerialNumber("LS", "JHPCH",true );
    	String lspzh = SerialNumberUtil.getInstance().getSerialNumber("LS", "LSPZH",true );
    	String lsscbm = SerialNumberUtil.getInstance().getCompanyCode();
    	String lsscmc = CompanyInfoUtil.getInstance().getCompanyName("LS", SerialNumberUtil.getInstance().getCompanyCode());
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("jhpch", jhpch);
    	map.put("lspzh", lspzh);
    	map.put("lsscbm", lsscbm);
    	map.put("lsscmc", lsscmc);
    	setReturnData(map);
    	return null;
    }

  //根据条码获取屠宰交易信息
    public void getTzjyxx(){
    	try {
			Map<String,Object> result = getService().getTzjyxx(tztmh);
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
    
    //根据条码获取批肉交易信息
    public void getPrjyxx(){
    	try {
			Map<String,Object> result = getService().getPrjyxx(prtmh);
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
    
}

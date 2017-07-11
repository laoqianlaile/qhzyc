package com.ces.component.prrpjcxx.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.prrpjcxx.service.PrrpjcxxService;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.config.utils.ConstantVar;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.exception.FatalException;

public class PrrpjcxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, PrrpjcxxService, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;
    private static Log log = LogFactory.getLog(TraceShowModuleDefineServiceDaoController.class);
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
    /**
     * 新增页面初始化数据获取
     */
    public void getInitformData(){
    	//企业编码
    	String pfscbm = SerialNumberUtil.getInstance().getCompanyCode();
    	//企业名称
    	String pfscmc = CompanyInfoUtil.getInstance().getCompanyName("PR", pfscbm);
    	Map<String, String> map = new HashMap<String, String>();
    	map.put("PFSCBM", pfscbm);
    	map.put("PFSCMC", pfscmc);
    	setReturnData(map);
    }
    /**
     * 获取商品名称列表
     */
    public void getSpmcGrid(){
    	setReturnData(this.getService().getRpspxx());
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
    
    /**
     * 获取供应商名称
     * 
     */
    public void getGysData(){
    	String id= getParameter("id");
    	setReturnData(getService().getGysData(id));
    }
}
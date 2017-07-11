package com.ces.component.xuqiugenzongjuzhengoujian.action;

import java.util.List;
import java.util.Map;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.xuqiugenzongjuzhengoujian.service.XuqiugenzongjuzhengoujianService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class XuqiugenzongjuzhengoujianController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, XuqiugenzongjuzhengoujianService, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;
    
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
    
    
    public void getLSH(){
    	super.setReturnData(SerialNumberUtil.getInstance().getSerialNumber("XQGZJZ","XQGZJZ",false));
    }
    
    @SuppressWarnings("unchecked")
	public void getJcxqmc(){
    	String xqmc = this.getRequest().getParameter("xqmc");
    	String xh = this.getRequest().getParameter("xh");
    	boolean bool;
    	List<Map<String,Object>> list = (List<Map<String, Object>>) getService().getJcxqmc(xqmc,xh);
    	if(list.size()==0){
    		bool = true;//true为没有重复
    	}else{
    		bool = false;//false为有重复
    	}
    	super.setReturnData(bool);
    }
    
    @SuppressWarnings("unchecked")
	public void getPdbcorxg(){
    	String xh = this.getRequest().getParameter("xh");
    	
    	
    	boolean bool ;
    	List<Map<String,Object>> list = (List<Map<String, Object>>) getService().getPdbcorxg(xh);
    	if(list.size()==1){
    		bool = false;//不是保存
    	}else{
    		bool = true;//是保存
    	}
    	super.setReturnData(bool);
    }

}
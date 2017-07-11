package com.ces.component.lssctbxz.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.component.jclhxz.service.JclhxzService;
import com.ces.component.lssctbxz.dao.LssctbxzDao;
import com.ces.component.lssctbxz.service.LssctbxzService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class LssctbxzController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, LssctbxzService, LssctbxzDao> {

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
    
    @Autowired
    @Qualifier("lssctbxzService")
    @Override
    protected void setService(LssctbxzService service) {
        super.setService(service);
    }
    /**
     * 初始化部分表单隐藏数据
     * @return
     */
    public Object initFormData(){
    	String lsscbm = SerialNumberUtil.getInstance().getCompanyCode();
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("lsscbm", lsscbm);
    	setReturnData(map);
    	return SUCCESS;
    }
    
    //根据理货编号获得商品名称列表
    public Object getSpmcByLhbh(){
    	String lhbh = this.getRequest().getParameter("scjcbh");
    	setReturnData(getService().getSpmcByLhbh(lhbh));
    	return SUCCESS;
    }
    
    //获得所有商品名称
    public Object getAllSpmc(){
    	setReturnData(getService().getAllSpmc());
    	return SUCCESS;
    }
    
    //获得所有状态为开启的经营者
    public Object getJyzByZt(){
    	setReturnData(getService().getJyzByZt());
    	return SUCCESS;
    }
    
    //获得所有蔬菜进场编号
    public Object getAllScjcbh(){
    	setReturnData(getService().getAllScjcbh());
    	return SUCCESS;
    }
    
    //根据批发商编码获得蔬菜进场编号
    public Object getScjcbhByPfsbm(){
    	String pfsbm = this.getRequest().getParameter("pfsbm");
    	setReturnData(getService().getScjcbhByJyzbm(pfsbm));
    	return SUCCESS;
    }
    
    //根据蔬菜进场编号获得ID
    public Object getIdByScjcbh(){
    	String scjcbh = this.getRequest().getParameter("scjcbh");
    	setReturnData(getService().getIdByScjcbh(scjcbh));
    	return SUCCESS;
    }
    /**
     * 获取蔬菜零售品证号
     * @return
     */
    public Object getJypzh(){
    	String jypzh = SerialNumberUtil.getInstance().getSerialNumber("LS", "JYPZH", true);
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("jypzh", jypzh);
    	setReturnData(map);
    	return null;
    }
}

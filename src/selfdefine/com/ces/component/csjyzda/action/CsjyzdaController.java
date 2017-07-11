package com.ces.component.csjyzda.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.component.csjyzda.dao.CsjyzdaDao;
import com.ces.component.csjyzda.service.CsjyzdaService;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class CsjyzdaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CsjyzdaService, CsjyzdaDao> {

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
    @Qualifier("csjyzdaService")
    @Override
    protected void setService(CsjyzdaService service) {
        super.setService(service);
    }
    /**
     * 新增时添加经营者编码
     * @return
     */
    public Object getJyzbm(){
    	String jyzbm = SerialNumberUtil.getInstance().getSerialNumber("CS", "JYZBM",true );
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("jyzbm", jyzbm);
    	setReturnData(map);
    	return null;
    }
    /**
     * init表单数据，如备案节点编码
     * @return
     */
    public Object initForm(){
    	String bajdbm = SerialNumberUtil.getInstance().getCompanyCode();
    	String bajdmc = CompanyInfoUtil.getInstance().getCompanyName("LS", SerialNumberUtil.getInstance().getCompanyCode());
    	String gxrq = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("bajdbm", bajdbm);
    	map.put("gxrq", gxrq);
    	map.put("bajdmc", bajdmc);
    	setReturnData(map);
    	return null;
    }
    /**
     * 添加经营者下拉列表数据
     */
    public void getJyzdaGrid(){
		super.setReturnData(getService().getJyzda());
	}
    /**
     * 根据经营者编码获得经营者
     */
    public void getJyzda(){
	 	String jyzbh = this.getRequest().getParameter("jyzbh");
    	super.setReturnData(getService().getJyzdaByJyzbh(jyzbh));
	}

}

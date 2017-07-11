package com.ces.component.prjyzda.action;

import java.util.HashMap;
import java.util.Map;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.prjyzda.service.PrjyzdaService;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class PrjyzdaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, PrjyzdaService, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;
    private String jyzbm;
    
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

    public void getJyzbmAndBaltjdbm(){
    	String jyzbm = SerialNumberUtil.getInstance().getSerialNumber("PR", "JYZBM",true );
    	String baltjdbm = SerialNumberUtil.getInstance().getCompanyCode();
    	String baltjdmc = CompanyInfoUtil.getInstance().getCompanyName("PR", baltjdbm);
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("jyzbm", jyzbm);
    	map.put("baltjdbm", baltjdbm);
    	map.put("baltjdmc", baltjdmc);
    	setReturnData(map);
    }
    /**
     * 获取商品到达地
     */
    public void getJyzSpddd(){
    	this.setReturnData(this.getService().getJyzda(jyzbm));
    }
    public void getJyzdaInfo(){
    	this.setReturnData(this.getService().getJyzdaInfo(jyzbm));
    }

	public String getJyzbm() {
		return jyzbm;
	}

	public void setJyzbm(String jyzbm) {
		this.jyzbm = jyzbm;
	}
}
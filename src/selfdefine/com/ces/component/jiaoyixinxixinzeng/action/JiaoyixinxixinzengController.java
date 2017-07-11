package com.ces.component.jiaoyixinxixinzeng.action;

import com.ces.component.jiaoyixinxixinzeng.dao.JiaoyixinxixinzengDao;
import com.ces.component.jiaoyixinxixinzeng.service.JiaoyixinxixinzengService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.xarch.core.entity.StringIDEntity;

public class JiaoyixinxixinzengController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, JiaoyixinxixinzengService, JiaoyixinxixinzengDao> {

    private static final long serialVersionUID = 1L;
    private String pfsbm;
    private String jclhbh;
    private String spbm;
    private String lssbm;
    private String jclhid;
    

	public String getJclhid() {
		return jclhid;
	}

	public void setJclhid(String jclhid) {
		this.jclhid = jclhid;
	}

	public String getLssbm() {
		return lssbm;
	}

	public void setLssbm(String lssbm) {
		this.lssbm = lssbm;
	}

	public String getSpbm() {
		return spbm;
	}

	public void setSpbm(String spbm) {
		this.spbm = spbm;
	}

	public String getJclhbh() {
		return jclhbh;
	}

	public void setJclhbh(String jclhbh) {
		this.jclhbh = jclhbh;
	}

	public String getPfsbm() {
		return pfsbm;
	}

	public void setPfsbm(String pfsbm) {
		this.pfsbm = pfsbm;
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
    
    @Override
    protected void setService(JiaoyixinxixinzengService service){
    	super.setService(service);
    }
    
    //获取流水号
    public void getLsh(){
    	String zl = this.getRequest().getParameter("Zl");
    	String lsh = SerialNumberUtil.getInstance().getSerialNumber("PC",zl,true);
    	super.setReturnData(lsh);
    }
    
    //根据批发商编码获取进场理货编码
    public void getJclhbhByPfsbm(){
    	super.setReturnData(getService().getJclhbhByPfsbm(pfsbm));
    }
    
    //获取商品名称
    public void getSpmcByPid(){
    	super.setReturnData(getService().getSpmcByPid(jclhid));
    }
    
    public void getPfsmcByJclhbh(){
    	super.setReturnData(getService().getPfsmcByJclhbh(jclhbh));
    }
    
    public void getIdByJclhbh(){
    	super.setReturnData(getService().getIdByJclhbh(jclhbh));
    }
    
    //获取交易凭证及进货批次号
    public void getInfoBySpbm(){
    	super.setReturnData(getService().getInfoBySpbm(spbm,jclhbh));
    }
    
    //获取商品到达地
    public void getDddByLssbm(){
    	super.setReturnData(getService().getDddByLssbm(lssbm));
    }
    
    //获取进货批次号
    public void getJhpch(){
    	super.setReturnData(getService().getJhpch(jclhid, spbm));
    }
    
    //根据理货编号获取产地信息
    public void getCdxxByJclhbh(){
    	super.setReturnData(getService().getCdxxByJclhbh(jclhbh));
    }

    //获得该批次剩余重量
    public void getJclihzl(){
        String jclhbm = getParameter("jclhbh");
        super.setReturnData(getService().getJclihzl(jclhbm));
    }
}

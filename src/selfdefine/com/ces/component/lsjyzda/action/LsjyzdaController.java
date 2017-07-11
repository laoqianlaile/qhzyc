package com.ces.component.lsjyzda.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.lsjyzda.service.LsjyzdaService;
import com.ces.component.tcsjyz.service.TcsjyzService;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class LsjyzdaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, LsjyzdaService, TraceShowModuleDao> {

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
    
    @Override
	protected void setService(LsjyzdaService service){
		super.setService(service);
	}
    /**
     * 新增时添加经营者编码
     * @return
     */
    public Object getJyzbm(){
    	String jyzbm = SerialNumberUtil.getInstance().getSerialNumber("LS", "JYZBM",true );
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
    	map.put("bajdmc", bajdmc);
    	map.put("gxrq", gxrq);
    	setReturnData(map);
    	return null;
    }
    
	public void getJyzdaGrid(){
		String gljyzbm = this.getRequest().getParameter("gljyzbm");
		super.setReturnData(getService().getJyzda(gljyzbm));
	}
    public void getCpJyzda(){
		setReturnData(getService().getCpJyzda());
	}
	public void getPfsGrid(){
		String gljyzbm = this.getRequest().getParameter("gljyzbm");
		super.setReturnData(getService().getPfs(gljyzbm));
	}
	public void getJyzda(){
	 	String jyzbh = this.getRequest().getParameter("jyzbh");
    	super.setReturnData(getService().getJyzdaByJyzbh(jyzbh));
	}

    public void getJyzlx(){
        String jylxValue = getParameter("jylxValue");
        setReturnData(getService().getJyzlx(jylxValue));
    }
}
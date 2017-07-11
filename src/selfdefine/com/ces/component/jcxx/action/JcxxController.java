package com.ces.component.jcxx.action;


import com.ces.component.jcxx.dao.JcxxDao;
import com.ces.component.jcxx.service.JcxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class JcxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, JcxxService, JcxxDao> {

    private static final long serialVersionUID = 1L;
    private String jhpch;
    private String pfsbm;
    private String jclhid;
    private String jclhbh;
    public String getJclhbh() {
		return jclhbh;
	}

	public void setJclhbh(String jclhbh) {
		this.jclhbh = jclhbh;
	}

	public String getJclhid() {
		return jclhid;
	}

	public void setJclhid(String jclhid) {
		this.jclhid = jclhid;
	}
	private String selectedValue;
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

    //根据批发商编码获取进场理货编码
    public void getJclhbhByPfsbm(){
    	super.setReturnData(getService().getJclhbhByPfsbm(pfsbm));
    }

	//获取进场理货PID
	public void getJclhxxpid(){
		String jclhbh = getParameter("jclhbh");
		super.setReturnData(getService().getJclhxxpid(jclhbh));
	}
    
    //根据进场理货编号获取商品名称
    public void getSpmcByPid(){
    	super.setReturnData(getService().getSpmcByPid(jclhid));
    }
    
    //根据进场理货编号获取ID
    public void getIdByJclhbh(){
    	super.setReturnData(getService().getIdByJclhbh(jclhbh));
    }
    
    //根据进场理货编号获取批发商名称
    public void getPfsmcByJclhbh(){
    	super.setReturnData(getService().getPfsmcByJclhbh(jclhbh));
    }

    //根据检测id获取进场理货编号
    public Object getLhbhById() {
        String id = getParameter("id");
        setReturnData(getService().getLhbhById(id));
        return SUCCESS;
    }

    public String getJypzhData(){
    	Object data=getService().getJypzh(jhpch);
    	if(data==null){
    		data="";
    	}
    	setReturnData(data);
    	return null;
    }

	public String getJhpch() {
		return jhpch;
	}

	public void setJhpch(String jhpch) {
		this.jhpch = jhpch;
	}


	public String getSelectedValue() {
		return selectedValue;
	}

	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}

	public String getPfsbm() {
		return pfsbm;
	}
	public void setPfsbm(String pfsbm) {
		this.pfsbm = pfsbm;
	}

}

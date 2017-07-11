package com.ces.component.tzszjyxx.action;

import com.ces.component.tzszjyxx.dao.TzszjyxxDao;
import com.ces.component.tzszjyxx.service.TzszjyxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TzszjyxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TzszjyxxService, TzszjyxxDao> {

    private static final long serialVersionUID = 1L;
    private String jcid;
    private String hzbm;
    private String szcdjyzh;
    
    public String getSzcdjyzh() {
		return szcdjyzh;
	}

	public void setSzcdjyzh(String szcdjyzh) {
		this.szcdjyzh = szcdjyzh;
	}

	public String getHzbm() {
		return hzbm;
	}

	public void setHzbm(String hzbm) {
		this.hzbm = hzbm;
	}

	public String getJcid() {
		return jcid;
	}

	public void setJcid(String jcid) {
		this.jcid = jcid;
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

    public void getSzjcxx() {//根据生猪产地检疫证号获取货主名称和货主编码
    	super.setReturnData(getService().getSzjcxx(szcdjyzh));
    }
    
    public void getJyyGrid() {//获取检疫员下拉列表数据
    	super.setReturnData(getService().getJyyGrid());
    }
    
    public void getSzjyByHzbm() {//根据货主编码获取检疫信息
    	super.setReturnData(getService().getSzjyByHzbm(hzbm));
    }
    
    public void setJyzt() {//更改生猪进场的检疫状态
    	String jyzl = this.getRequest().getParameter("Jyzh");
    	getService().setJyzt(jyzl);
    }
}

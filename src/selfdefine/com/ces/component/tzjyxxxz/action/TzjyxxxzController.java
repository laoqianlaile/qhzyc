package com.ces.component.tzjyxxxz.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ces.component.trace.utils.JSON;
import com.ces.component.tzjyxxxz.dao.TzjyxxxzDao;
import com.ces.component.tzjyxxxz.service.TzjyxxxzService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.config.utils.ConstantVar;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.exception.FatalException;
import com.fasterxml.jackson.core.type.TypeReference;

public class TzjyxxxzController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TzjyxxxzService, TzjyxxxzDao> {

    private static final long serialVersionUID = 1L;
    private String hzbm;
    private String szcdjyzh;
    
    public String getHzbm() {
		return hzbm;
	}

	public void setHzbm(String hzbm) {
		this.hzbm = hzbm;
	}
	
	
	public String getSzcdjyzh() {
		return szcdjyzh;
	}

	public void setSzcdjyzh(String szcdjyzh) {
		this.szcdjyzh = szcdjyzh;
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
    
    public void getJyzhByHzbm() {//根据货主编码获取检疫证号下拉列表数据
    	super.setReturnData(getService().getJyzhByHzbm(hzbm));
    }
    
    public void getRpjyxx(){//根据检疫证号获取货主编码及货主名称
    	super.setReturnData(getService().getRpjyxx(szcdjyzh));
    }
    
    public void getLzxxByJyzh(){//根据检疫证号获取两证信息
    	super.setReturnData(getService().getLzxxByJyzh(szcdjyzh));
    }
    
    public void getZsm(){
    	super.setReturnData(getService().getZsm());
    }
    
    public void setJyzt(){//更改肉品检疫检验明细信息的交易状态
    	String szcdjyzh = this.getRequest().getParameter("Jyzh");
    	String rowDatas = this.getRequest().getParameter("RowData");
    	List<Map<String,String>> list = JSON.fromJSON(rowDatas, new TypeReference<List<Map<String,String>>>(){});
    	List<String> dwcpjyzhList = new ArrayList<String>();
    	for (Map<String,String> map : list) {
    		String dwcpjyzh = map.get("DWCPJYZH");
    		dwcpjyzhList.add(dwcpjyzh);
    	}
    	getService().setJyzt(szcdjyzh,dwcpjyzhList);
    }
}

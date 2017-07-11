package com.ces.component.zztrpcglb.action;

import com.ces.component.trace.utils.DataDictionaryUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zztrpcglb.service.ZztrpcglbService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;

import java.util.Map;

public class ZztrpcglbController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZztrpcglbService, TraceShowModuleDao> {

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

    /**
     * 获得投入品类型下拉框数据
     */
    public void searchTrplx(){
        setReturnData(DataDictionaryUtil.getInstance().getDictionaryData("ZZTRPLX"));
    }

    /**
     * 获得入库数量单位
     */
    public void searchRksldw(){
        setReturnData(DataDictionaryUtil.getInstance().getDictionaryData("RKSLDW"));
    }
    /**
     * 获得入库数量单位
     */
    public void searchBzggdw(){
        setReturnData(DataDictionaryUtil.getInstance().getDictionaryData("ZZBZGGDW"));
    }

    /**
     * 获得物料来源
     */
    public void searchWlly(){setReturnData(DataDictionaryUtil.getInstance().getDictionaryData(("WLLY")));}
    public void searchFllx(){setReturnData(DataDictionaryUtil.getInstance().getDictionaryData(("ZZFLLX")));}
    public void getRkbhlsh(){
        setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ", "ZZRKBH", false));
    }

    public void searchTrpcgxx(){
        String id =  getParameter("id");
        Map<String,Object> map = getService().getTrpCgxx(id);
        setReturnData(map);
    }

    @Override
    public String save() throws FatalException {
        String entityJson = getParameter(E_ENTITY_JSON);
        Map<String, String> map = getService().save(entityJson);
        setReturnData(map);
        return SUCCESS;
    }

    public void searchBzggData(){
        String trpbh=getParameter("trpbh");
        setReturnData(getService().searchBzggData(trpbh));
    }

}
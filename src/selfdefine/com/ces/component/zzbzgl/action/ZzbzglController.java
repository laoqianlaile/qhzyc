package com.ces.component.zzbzgl.action;

import com.ces.component.trace.utils.JSON;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzbzgl.service.ZzbzglService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;

public class ZzbzglController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzbzglService, TraceShowModuleDao> {

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
     * 流水号
     */
    public void getLsh(){
        setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ","ZZBZLSH",false));
    }

    /**
     * 保存包装信息
     */
    public void saveBzxx(){
        String formData = getParameter("formData");
        String gridData = getParameter("gridData");
        Map<String,String> map = JSON.fromJSON(formData, new TypeReference<Map<String,String>>() {});
        List<Map<String,String>> list = JSON.fromJSON(gridData, new TypeReference<List<Map<String, String>>>() {
        });
        setReturnData(getService().saveBzxx(map,list));
    }

    /**
     * 查询包装信息
     */
    public void getBzxx(){
        String id = getParameter("id");
        setReturnData(getService().getBzxx(id));
    }

    /**
     * 查询包装信息配料信息
     */
    public void getBzxxPlxx(){
        String id = getParameter("id");
        setReturnData(getService().getBzxxPlxx(id));
    }

    /**
     * 查询采收信息
     */
    public void getCsgl(){
        String barCode = getParameter("barCode");
        Map<String,Object> dataMap = (Map<String,Object>)getService().getCsgl(barCode);
        if(dataMap.size() == 0){
            setReturnData(false);
        }else{
            setReturnData(dataMap);
        }
    }

    /**
     * 生成产品追溯码
     */
    public void getCpzsm(){
        setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ","ZZCPZSM",false));
    }

    /**
     * 存储打印详细
     */
    public void savePrint(){
        String bzlsh = getParameter("bzlsh");
        String bzxs = getParameter("bzxs");
        String cpzsm = getParameter("cpzsm");
        String id = getParameter("id");
        setReturnData(getService().savePrint(bzlsh,bzxs,cpzsm,id));
    }

    public Object updateKczlForDelete(){
        String ids = getParameter("ids");
        setReturnData(getService().updateKczlForDelete(ids));
        return SUCCESS;
    }
}
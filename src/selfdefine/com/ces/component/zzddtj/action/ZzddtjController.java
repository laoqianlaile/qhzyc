package com.ces.component.zzddtj.action;

import com.ces.component.trace.utils.DataDictionaryUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzddtj.service.ZzddtjService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZzddtjController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzddtjService, TraceShowModuleDao> {

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

    public void searchGridData(){
        PageRequest pageRequest = this.buildPageRequest();
        String khlx=getParameter("khlx");
        String khbh=getParameter("khmc");
        String cpmc=getParameter("cpmc");
        String qssj=getParameter("qssj");
        String jssj=getParameter("jssj");
        setReturnData(getService().searchGridData(khlx, khbh, cpmc, qssj, jssj,pageRequest));

    }
    public List<Map<String,String>> getComboboxTypeData(List<Code> list){
        List<Map<String,String>> dataMap = new ArrayList<Map<String, String>>();
        for(Code code:list){
            Map<String,String>  temMap = new HashMap<String, String>();
            temMap.put("value",code.getValue());
            temMap.put("text",code.getName());
            dataMap.add(temMap);
        }
        return dataMap;
    }

    public void loadKhlx(){
//        setReturnData(getService().loadKhlx());
        setReturnData(getComboboxTypeData(DataDictionaryUtil.getInstance().getDictionaryData("KHLX")));
    }

    public void loadKhmc(){
        String khlx=getParameter("khlx");
        setReturnData(getService().loadKhmc(khlx));
    }

    public void loadCpmc(){
        setReturnData(getService().loadCpmc());
    }

    public void printGrid(){
        String ids=getParameter("ids");
        String[] idarr={};
       // String[] idarr=ids.split(",");
        if(!("".equals(ids))){
            idarr=ids.split(",");
        }

        setReturnData(getService().printGrid(idarr));
    }

}
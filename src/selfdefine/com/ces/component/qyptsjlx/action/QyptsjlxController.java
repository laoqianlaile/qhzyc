package com.ces.component.qyptsjlx.action;

import ces.sdk.util.StringUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.qyptsjlx.service.QyptsjlxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QyptsjlxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, QyptsjlxService, TraceShowModuleDao> {

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
     * 分类编码验重
     */
    public void getIsRepeatByFlbm(){
        String flbm = getParameter("flbm");
        String id = getParameter("id");
        List<Map<String,Object>> list = (List<Map<String,Object>>)getService().getIsRepeatByFlbm(flbm, id);
        if(list.size() == 0){
            setReturnData(true);
        }else{
            setReturnData(false);
        }
    }
    /**
     * 类型编码验重
     */
    public void getIsRepeatByLxbm(){
        String lxbm = getParameter("lxbm");
        String id = getParameter("id");
        List<Map<String,Object>> list = (List<Map<String,Object>>)getService().getIsRepeatByLxbm(lxbm, id);
        if(list.size() == 0){
            setReturnData(true);
        }else{
            setReturnData(false);
        }
    }

    /**
     * 数据编码验重
     */
    public void getIsRepeatBySjbm(){
        String sjbm = getParameter("sjbm");
        String lxbm = getParameter("lxbm");
        String id = getParameter("id");
        List<Map<String,Object>> list = (List<Map<String,Object>>)getService().getIsRepeatBySjbm(lxbm,sjbm, id);
        if(list.size() == 0){
            setReturnData(true);
        }else{
            setReturnData(false);
        }
    }

    /**
     * 顺序级别验重
     */
    public void getIsRepeatBySxjb(){
        String sxjb = getParameter("sxjb");
        String lxbm = getParameter("lxbm");
        String id = getParameter("id");
        List<Map<String,Object>> list = (List<Map<String,Object>>)getService().getIsRepeatBySxjb(lxbm,sxjb, id);
        if(list.size() == 0){
            setReturnData(true);
        }else{
            setReturnData(false);
        }
    }
    /**
     * 所属系统验重
     */
    public void getIsRepeatBySsxtjb(){
        String ssxtbm = getParameter("ssxtbm");
        String ssxtmc = getParameter("ssxtmc");
        List<Map<String,Object>> list = (List<Map<String,Object>>)getService().getIsRepeatBySsxtbm(ssxtbm, ssxtmc);
        if(list.size() == 0){
            setReturnData(true);
        }else{
            setReturnData(false);
        }
    }
    /**
     * 获取数据字典类型名称
     */
    public void getSjzdmc(){
        setReturnData(getService().getSjzdmc());
    }

    /**
     * 根据数据字典类型编码获取相关数据
     */
    public void getSjzdByLxbm(){
        String lxbm = getParameter("lxbm");
        if(!"".equals(lxbm) && lxbm!= null){
            setReturnData(getService().getSjzdByLxbm(lxbm));
        }else{
            setReturnData("");
        }

    }

    /**
     * 保存新增
     */
    public void saveAdd(){
        String sjbm = getParameter("SJBM");
        String sjmc = getParameter("SJMC");
        String lxbm = getParameter("LXBM");
        String sxjb = getParameter("SXJB");
        String id = getParameter("ID");
        Map<String,String> map = new HashMap<String, String>();
        map.put("SJBM",sjbm);
        map.put("SJMC",sjmc);
        map.put("LXBM",lxbm);
        map.put("SXJB",sxjb);
        if("".equals(id)){
            map.put("ID",null);
        }else {
            map.put("ID",id);
        }
        setReturnData(getService().save("t_common_sjlx_code",map,null));
    }


    /**
     * 根据ID删除
     */
    public void deleteById(){
        String ids = getParameter("ids");
        setReturnData(getService().deleteById(ids));
    }

    /**
     * 返回最大顺序级别并+1
     */
    public void getMaxSxjbByLxbm(){
        String lxbm = getParameter("lxbm");
        setReturnData(getService().getMaxSxjbByLxbm(lxbm));
    }
    /**
     * 显示数据字典类型管理的数据
     */
    public void getGridData(){
    	 setReturnData(getService().getGridData());
    }

}
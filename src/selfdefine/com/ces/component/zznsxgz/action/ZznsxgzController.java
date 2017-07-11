package com.ces.component.zznsxgz.action;

import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.utils.JSON;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zznsxgz.service.ZznsxgzService;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.data.domain.PageRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZznsxgzController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZznsxgzService, TraceShowModuleDao> {

    @Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }

    /**
     * 农事项跟踪列表查询
     *
     * @return
     */
    public Object nsxgzSearch() {
        PageRequest pageRequest = this.buildPageRequest();
        Map<String, String> queryParam = new HashMap<String, String>();
        queryParam.put("ygStart", getParameter("ygStart"));
        queryParam.put("ygEnd", getParameter("ygEnd"));
        queryParam.put("wcStart", getParameter("wcStart"));
        queryParam.put("wcEnd", getParameter("wcEnd"));
        queryParam.put("nsxlx", getParameter("nsxlx"));
        queryParam.put("fzr", getParameter("fzr"));
        queryParam.put("qybm", getParameter("qybm"));
        queryParam.put("dkmc", getParameter("dkmc"));
        queryParam.put("pjzt", getParameter("pjzt"));
        queryParam.put("rwzt", getParameter("rwzt"));
        setReturnData(getService().nsxgzSearch(pageRequest, queryParam));
        return SUCCESS;
    }

    /**
     * 获取地块负责人
     *
     * @return
     */
    public Object getFzr() {
        setReturnData(getService().getFzr());
        return SUCCESS;
    }

    /**
     * 编辑负责人和预计时间
     *
     * @return
     */
    public Object updateNsx() {
        String id = getParameter("id");
        String ygsj = getParameter("ygsj");
        String fzr = getParameter("fzr");
        String fzrbh = getParameter("fzrbh");
        String lx = getParameter("lx");
        try {
            getService().updateNsx(id, ygsj, fzr, fzrbh, lx);
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }

    }

    /**
     * 删除农事项
     *
     * @return
     */
    public Object deleteNsx() {
        String ids = getParameter("ids");
        List<Map<String, String>> map = JSON.fromJSON(ids, new TypeReference<List<Map<String, String>>>() {});
        try {
            getService().deleteNsx(map);
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    /**
     * 农事项评分
     *
     * @return
     */
    public Object judgeNsx() {
        String ids = getParameter("ids");
        String[] idArr = JSON.fromJSON(ids, new TypeReference<String[]>() {});
        String idstr = "";
        for (String id : idArr) {
            idstr += id + ",";
        }
        idstr = idstr.substring(0,idstr.length()-1);
        String pj = getParameter("pj");
        try {
            getService().judgeNsx(idstr,pj);
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public Object getCzr(){
        setReturnData(getService().getCzr());
        return SUCCESS;
    }

    public Object sjwhUpdate(){
        String czrbh = getParameter("czrbh");
        String czr = getParameter("czr");
        String kssj = getParameter("kssj");
        String jssj = getParameter("jssj");
        String id = getParameter("id");
        setReturnData(getService().sjwhUpdate(kssj, jssj, czrbh, czr, id));
        return SUCCESS;
    }
}
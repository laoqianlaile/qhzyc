package com.ces.component.zzcc.action;

import com.ces.component.trace.utils.JSON;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzcc.service.ZzccService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;

public class ZzccController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzccService, TraceShowModuleDao> {

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

    public Object deleteCc() {
        String dataId = getParameter("dataId");
        setReturnData(getService().deleteCc(dataId));
        return SUCCESS;
    }

    //删除灌溉投入品信息
    public Object deleteCcTrp () {
        String dataId = getParameter("dataId");
        setReturnData(getService().deleteCcTrp(dataId));
        return SUCCESS;
    }

    /**按批次出场：获取区域编号
     * @return
     */
    public Object getQybh(){
        setReturnData(getService().getQybh());
        return SUCCESS;
    }

    /**按批次出场：根据区域编号获取地块编号
     * @return
     */
    public Object getDkbhByQybh(){
        String qybh = getParameter("qybh");
        setReturnData(getService().getDkbhByQybh(qybh));
        return SUCCESS;
    }

    /**按批次出场：根据地块编号获取生产档案编号
     * @return
     */
    public Object getScdabhByDkbh(){
        String dkbh = getParameter("dkbh");
        setReturnData(getService().getScdabhByDkbh(dkbh));
        return SUCCESS;
    }

    /**按批次出场：根据生产档案编号获取采收批次号
     * @return
     */
    public Object getCspchByScdabh(){
        String scdabh = getParameter("scdabh");
        setReturnData(getService().getCspchByScdabh(scdabh));
        return SUCCESS;
    }

    /**按批次出场：根据采收批次号获取批次信息
     * @return
     */
    public Object getApcccByCspch(){
        String cspch = getParameter("pch");
        setReturnData(getService().getApcccByCspch(cspch));
        return SUCCESS;
    }

    /**按批次出场：获取客户信息
     * @return
     */
    public Object getKhxx(){
        setReturnData(getService().getKhxx());
        return SUCCESS;
    }

    /**按批次出场：根据客户编号获取销售订单编号
     * @return
     */
    public Object getXsddbhByKhbh(){
        String ddbh = getParameter("ddbh");
        String khbh = getParameter("khbh");
        setReturnData(getService().getXsddbhByKhbh(ddbh, khbh));
        return SUCCESS;
    }

    /**按批次出场：获取配送方式
     * @return
     */
    public Object getPsfs(){
        setReturnData(getService().getPsfs());
        return SUCCESS;
    }

    /**获取出场流水号
     * @return
     */
    public Object getCclsh(){
//        setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ","ZZPCCCLSH",false));
        setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ","CCLSH",false));
        return SUCCESS;
    }

    public Object savePcccxx(){
        String formData = getParameter("formData");
        String gridData = getParameter("gridData");
        Map<String, String> formMap = JSON.fromJSON(formData, new TypeReference<Map<String, String>>() {
        });
        List<Map<String, String>> gridList = JSON.fromJSON(gridData, new TypeReference<List<Map<String, String>>>() {
        });
        setReturnData(getService().savePcccxx(formMap, gridList));
        return SUCCESS;
    }

    public Object updatePcccxx(){
        String formData = getParameter("formData");
        String gridData = getParameter("gridData");
        Map<String, String> formMap = JSON.fromJSON(formData, new TypeReference<Map<String, String>>() {
        });
        List<Map<String, String>> gridList = JSON.fromJSON(gridData, new TypeReference<List<Map<String, String>>>() {
        });
        setReturnData(getService().updatePcccxx(formMap, gridList));
        return SUCCESS;
    }

    public Object deletePcccxx(){
        String id = getParameter("id");
        setReturnData(getService().deletePcccxx(id));
        return SUCCESS;
    }



    /**按批次出场获取常用客户
     * @return
     */
    public Object getCykh(){
        String pch = getParameter("pch");
        setReturnData(getService().getCykh(pch));
        return SUCCESS;
    }
}
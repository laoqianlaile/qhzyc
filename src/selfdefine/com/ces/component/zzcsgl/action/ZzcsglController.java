package com.ces.component.zzcsgl.action;

import ces.sdk.util.CommonUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzcsgl.service.ZzcsglService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;

import java.util.*;

public class ZzcsglController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzcsglService, TraceShowModuleDao> {

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
     * 采收新增中保存上面form
     * @return
     */
    public Object saveCsxx(){

        Map map= this.getRequest().getParameterMap();
        Map newMap = new HashMap();
        Set<Map.Entry> allSet = map.entrySet();
        Iterator<Map.Entry> iter = allSet.iterator();
        while(iter.hasNext()){
            Map.Entry entry = iter.next();
            String[] valueArray = (String[])entry.getValue();
            String value= "";
            for(int i = 0;i<valueArray.length;i++){
                value += valueArray[i];
            }
            newMap.put(entry.getKey(), value);
            value = "";
        }


        setReturnData(getService().saveCsxx(newMap));
        return SUCCESS;
    }

    public void searchCsxx(){
        String id=getParameter("id");
        setReturnData(getService().searchCsxx(id));
    }

    /**
     * 获得区域信息下拉列表数据
     */
    public void getQvmc(){
        setReturnData(getService().getQvmc());
    }

    /**
     * 获得地块信息下拉列表信息
     */
    public void getDkbh(){
        String qybh=getParameter("QYBH");
        setReturnData(getService().getDkbh(qybh));
    }

    /**
     * 获得种植单元信息下列表信息
     */
    public void getZzdybh(){
        String dkbh=getParameter("DKBH");
        setReturnData(getService().getZzdybh(dkbh));
    }

    /**
     * 或的生产单元编号信息
     */

    public void getScdabh(){
        String scdabh=getParameter("ZZDYBH");
        setReturnData(getService().getScdabh(scdabh));
    }

    /**
     * 获得品类和品种信息
     */

    public void getPlandPz(){
        String scdabh=getParameter("scdabh");
        setReturnData(getService().getPlandPz(scdabh));
    }

    /**
     *获得品类和品种编号信息
     */

    public void getPlbhandPzbh(){
        String scdabh=getParameter("scdabh");
        setReturnData(getService().getPlbhandPzbh(scdabh));
    }

    /**
     * 获得采收流水号
     */

    public void getCslsh(){
        String qybm=SerialNumberUtil.getInstance().getCompanyCode();
        setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ", qybm, "ZZCSLSH", true));
    }

    @Override
    public Object save() throws FatalException {
        return super.save();
    }

    public Object saveNzwxq(){

        String qybm=getParameter("qybm");
        //String entityJson = getParameter(E_ENTITY_JSON);
        Map map = this.getRequest().getParameterMap();
        System.out.println(map);
        Map newMap = new HashMap();

        //map.remove("CZ");
        Set<Map.Entry> allSet = map.entrySet();
        Iterator<Map.Entry> iter = allSet.iterator();
        while(iter.hasNext()){
            Map.Entry entry = iter.next();
            String[] valueArray = (String[])entry.getValue();
            String value= "";
            for(int i = 0;i<valueArray.length;i++){
                value += valueArray[i];
            }
            newMap.put(entry.getKey(), value);
            value = "";
        }
        Object objMap =  getService().saveNzwxq(newMap,qybm);
        setReturnData(objMap);
        return SUCCESS;
    }

    public Object editNzwxq(){
        String qybm=getParameter("qybm");
        String zlhj=getParameter("zlhj");
        String kczl=getParameter("kczl");
        String id=getParameter("id");
        int ZLHJ=Integer.parseInt(zlhj);
        int KCZL=Integer.parseInt(kczl);
//        //String entityJson = getParameter(E_ENTITY_JSON);
//        //Map map = this.getRequest().getParameterMap();
//        System.out.println(map);
//        Map newMap = new HashMap();
//
//        //map.remove("CZ");
//        Set<Map.Entry> allSet = map.entrySet();
//        Iterator<Map.Entry> iter = allSet.iterator();
//        while(iter.hasNext()){
//            Map.Entry entry = iter.next();
//            String[] valueArray = (String[])entry.getValue();
//            String value= "";
//            for(int i = 0;i<valueArray.length;i++){
//                value += valueArray[i];
//            }
//            newMap.put(entry.getKey(), value);
//            value = "";
//        }
//        Object objMap =  getService().editNzwxq(newMap, qybm, ZLHJ, KCZL, id);
//        setReturnData(objMap);
        setReturnData(getService().editNzwxq(ZLHJ,KCZL,id));
        return SUCCESS;
    }

    public Object deleteNzwxq(){
        String id=getParameter("id");
        getService().deleteNzwxq(id);
        return SUCCESS;
    }

    public void searchNzwxq(){
        String pid=getParameter("pid");
        setReturnData(getService().searchNzwxq(pid));
    }

    public void getZldj(){
        setReturnData(getService().getZldj());
    }

    /**
     * 获得散货出场中列表数据
     */
    public void getShccCslsh(){
        String id=getParameter("id");
        setReturnData(getService().getShccCslsh(id));
    }

    public void getShcc(){
        String cslsh=getParameter("cslsh");
        setReturnData(getService().getShcc(cslsh));
    }

/**
 * 散货出场页面中的保存Form的方法
 */

    public void saveForm(){

        String id=getParameter("ID");
        String cclsh=getParameter("CCLSH");
        String khmc=getParameter("KHMC");
        String xsddh=getParameter("XSDDH");
        String ccsj=getParameter("CCSJ");
        String psfs=getParameter("PSFS");
        String zzl=getParameter("ZZL");
        String cph=getParameter("CPH");
        String pszrr=getParameter("PSZRR");
        String dyzt=getParameter("DYZT");
        String bz=getParameter("BZ");
        String qybm=SerialNumberUtil.getInstance().getCompanyCode();
        String khbh=getParameter("khbh");
        Map<String,String> map=new HashMap<String, String>();
        map.put("ID",id);
        map.put("CCLSH",cclsh);
        map.put("KHMC",khmc);
        map.put("XSDDH",xsddh);
        map.put("CCSJ",ccsj);
        map.put("PSFS",psfs);
        map.put("ZZL",zzl);
        map.put("CPH",cph);
        map.put("PSZRR",pszrr);
        map.put("DYZT",dyzt);
        map.put("BZ",bz);
        map.put("QYBM",qybm);
        map.put("KHBH",khbh);
        setReturnData(getService().saveForm(map));

    }

    /**
     * 散货出场页面中，保存列表中的数据
     */

    public void saveGrid(){
        String ids=getParameter("ids");
        //String ID=getParameter("ID");
        String CSLSH=getParameter("CSLSH");
        String PZ=getParameter("PZ");
        String PZBH=getParameter("PZBH");
        String PCH=getParameter("PCH");
        String CSZZL=getParameter("CSZZL");
        String KCZL=getParameter("KCZL");
        String CCZL=getParameter("CCZL");
        String pid=getParameter("PID");
        //String qybm= SerialNumberUtil.getInstance().getCompanyCode();
        Map<String,String> map=new HashMap<String, String>();
       // map.put("CSLSH",CSLSH);
        //map.put("QYBM",qybm);
        map.put("CSLSH",CSLSH);
        map.put("PZ",PZ);
        map.put("PZBH",PZBH);
        map.put("PCH",PCH);
        map.put("CSZZL",CSZZL);
        map.put("KCZL",KCZL);
        map.put("CCZL",CCZL);
        map.put("PID",pid);
        setReturnData(getService().saveGrid(map, ids));
    }

    /**
     *散货出场的出场重量同步到农作物详情哪张表中，修改其库存重量
     */
    public void editCsnzwxq(){
        String CCZL=getParameter("cczl");
        int cczl=Integer.parseInt(CCZL);
        String id=getParameter("id");
        String pch=getParameter("pch");
        setReturnData(getService().editCsnzwxq(id,pch,cczl));
    }

    /**
     * 将散货出产的重量同步到出场管理那种表中
     */

    public void editCcglandCsgl(){
        String csid=getParameter("csid");
        String id=getParameter("id");
        int cczl=Integer.parseInt(getParameter("cczl"));
        String ZZL=getParameter("zzl");
        int zzl=Integer.parseInt(ZZL);
        setReturnData(getService().editCcglandCsgl(id, zzl, csid, cczl));
    }

    /**
     * 同步库存重量和采收重量到采收管理表中
     */
   public void updateCsgl(){
       String id=getParameter("id");
       int kczl=Integer.parseInt(getParameter("kczl"));
       int zlhj=Integer.parseInt(getParameter("zlhj"));
       setReturnData(getService().updateCsgl(id, kczl, zlhj));
   }

    /**
     * 修改时附表判断.
     */
    public void getOneDataforxG(){
        String id=getParameter("id");
        setReturnData(getService().getOneDataforxG(id));
    }

    /**
     * 修改是同步库存重量和采收重量
     */
    public void updateCsglZl(){
        String id=getParameter("id");
        int kczl=Integer.parseInt(getParameter("kczl"));
        int zlhj=Integer.parseInt(getParameter("zlhj"));
        setReturnData(getService().updateCsglZl(id, kczl, zlhj));    }

    public void getScdabhByDkbh(){
        String dkbh=getParameter("DKBH");
        setReturnData(getService().getScdabhByDkbh(dkbh));

    }

}
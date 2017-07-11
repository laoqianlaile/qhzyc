package com.ces.component.zzcsgl.service;

import ces.sdk.util.CommonUtil;
import com.ces.component.trace.utils.DataDictionaryUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.utils.StringUtil;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ZzcsglService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    @Transactional
    public Object saveCsxx(Map<String,String> map){
        String cslsh;
        Map<String,Object> mapback=new HashMap<String, Object>();
        String qybm=map.get("QYBM");
        if (StringUtil.isEmpty(map.get("ID"))) {
            //获取当前时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String d = df.format(new Date());
            map.put("CSSJ",d);
            Map<String,String> jcjlMap = new HashMap<String, String>();
            jcjlMap.put("CSLSH",map.get("CSLSH"));
            String sql = "select * from t_zz_scda t where t.scdabh = '" + map.get("SCDABH") + "' and t.qybm = '" + SerialNumberUtil.getInstance().getCompanyCode() + "'";
            Map<String,Object> dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
            jcjlMap.put("PID",String.valueOf(dataMap.get("ID")));
            saveOne("T_ZZ_SCJCJL",jcjlMap);
        }
        if(map.get("CSLSH").isEmpty()){
            cslsh=SerialNumberUtil.getInstance().getSerialNumber("ZZ",qybm, "ZZCSLSH", true);
            map.put("CSLSH",cslsh);
            map.put("YCCZL","0");
            String str=saveOne("T_ZZ_CSGL",map);
            mapback.put("ID",str);
            mapback.put("CSLSH",cslsh);
            int i=1;
            return mapback;

        }else{
        //map.put
        map.put("YCCZL","0");
        String str=saveOne("T_ZZ_CSGL",map);
        cslsh=map.get("CSLSH");
        //mapback=new HashMap<String, Object>();
        mapback.put("ID",str);
        mapback.put("CSLSH",cslsh);
        return mapback;}
    }

    public Map<String,Object> searchCsxx(String ID){
        String sql="select * from T_ZZ_CSGL where id='"+ID+"'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    public List<Map<String,Object>> getQvmc(){
        String sql="select QYMC,QYBH from T_ZZ_QYXX T WHERE T.QYBM = ? AND T.IS_DELETE='0'";
        List<Map<String,Object>> list= DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{SerialNumberUtil.getInstance().getCompanyCode()});
        return list;
    }

    public List<Map<String,Object>> getDkbh(String QYBH){
        String sql="select DKBH, DKMC from T_ZZ_DKXX T where T.QYBM=? AND  QYBH= ? AND T.IS_DELETE='0'";
        List<Map<String,Object>> list= DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{SerialNumberUtil.getInstance().getCompanyCode(),QYBH});
        return list;
    }

    public List<Map<String,Object>> getZzdybh(String DKBH){
        String sql="select ZZDYBH, ZZDYMC from T_ZZ_DY T where T.QYBM=? AND DKBH=? AND T.IS_DELETE='0'";
        List<Map<String,Object>> list= DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{SerialNumberUtil.getInstance().getCompanyCode(),DKBH});
        int i=1;
        return list;

    }

    /**
     * 根据种植单元编号查询农事项全部操作过的生产档案
     * @param zzdybh
     * @return
     */
    public List<Map<String,Object>> getScdabh(String zzdybh){
        String sql="select SCDABH,QYGLY from T_ZZ_SCDA T where T.QYBM = '"+SerialNumberUtil.getInstance().getCompanyCode()+"' AND ZZDYBH= '"+zzdybh+"' AND T.IS_DELETE='0'";
//        String sql="select t.scdabh,t.sum1 as QYGLY from   (select count(scdabh) as sum1,scdabh from (\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc from t_zz_scda a,t_zz_scbz b where a.id=b.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc from t_zz_scda a,t_zz_scgg b where a.id=b.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc from t_zz_scda a,t_zz_scsf b where a.id=b.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc from t_zz_scda a,t_zz_scyy b where a.id=b.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc from t_zz_scda a,t_zz_sccs b where a.id=b.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc from t_zz_scda a,t_zz_sccc b where a.id=b.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc from t_zz_scda a,t_zz_scqt b where a.id=b.pid) m where m.QYBM='"+SerialNumberUtil.getInstance().getCompanyCode()+"' AND m.zzdybh = '"+zzdybh+"'  group by scdabh) t\n" +
//                ",\n" +
//                "(select  count(scdabh) as sum2,scdabh from \n" +
//                "(\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc  from t_zz_scda a,t_zz_scbz b, t_zz_czjl c where a.id=b.pid and b.id=c.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc  from t_zz_scda a,t_zz_scgg b ,t_zz_czjl c where a.id=b.pid and b.id=c.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc  from t_zz_scda a,t_zz_scsf b, t_zz_czjl c where a.id=b.pid and b.id=c.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc  from t_zz_scda a,t_zz_scyy b, t_zz_czjl c where a.id=b.pid and b.id=c.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc  from t_zz_scda a,t_zz_sccs b, t_zz_czjl c where a.id=b.pid and b.id=c.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc  from t_zz_scda a,t_zz_sccc b, t_zz_czjl c where a.id=b.pid and b.id=c.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc  from t_zz_scda a,t_zz_scqt b, t_zz_czjl c where a.id=b.pid and b.id=c.pid) \n" +
//                " m where m.QYBM='"+SerialNumberUtil.getInstance().getCompanyCode()+"' AND m.zzdybh = '"+zzdybh+"' group by scdabh) k where t.scdabh=k.scdabh and t.sum1=k.sum2";
        List<Map<String,Object>> list= DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return list;
    }

    public Map<String,Object> getPlandPz(String scdabh){
        String sql="select PL,PZ from T_ZZ_SCDA T where T.QYBM=? AND SCDABH=? AND T.IS_DELETE='0'";
       return DatabaseHandlerDao.getInstance().queryForMap(sql,new Object[]{SerialNumberUtil.getInstance().getCompanyCode(),scdabh});
    }

    public Map<String,Object> getPlbhandPzbh(String scdabh){
        String sql="select PLBH,PZBH from T_ZZ_SCDA T where T.QYBM=? AND SCDABH=? AND T.IS_DELETE='0'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new Object[]{SerialNumberUtil.getInstance().getCompanyCode(),scdabh});
    }

    public Map<String,String> saveNzwxq(Map<String,String> map,String qybm){
        //map.put("QYBM",qybm);
        //String sql="update T_ZZ_CSGL set ZLHJ= "+ZLGJ +"and KCZL="+KCZL+" where ID='"+ID+"'";
        map.remove("___t");
        map.remove("CZ");
        if("".equals(map.get("KCZL"))){
            map.put("KCZL",map.get("ZL"));
        }
        String id=saveOne("T_ZZ_CSNZWXQ",map);
        map.put("ID",id);
        return map;
    }

    public int editNzwxq(int ZLGJ,int KCZL,String ID){
        //map.put("QYBM",qybm);
        String sql="update T_ZZ_CSGL set ZLHJ= "+ZLGJ +", KCZL="+KCZL+" where ID='"+ID+"'";
//        map.remove("___t");
//        map.remove("CZ");

//        String id=saveOne("T_ZZ_CSNZWXQ",map);
//        map.put("ID",id);
        return DatabaseHandlerDao.getInstance().executeSql(sql);
    }

    public void deleteNzwxq(String id){
        String sql="update T_ZZ_CSNZWXQ set is_delete = 1 where id = '"+id+"'";
        DatabaseHandlerDao.getInstance().executeSql(sql);
    }

    public Object searchNzwxq(String pid){
        String sql="select * from T_ZZ_CSNZWXQ where  is_delete <> '1' and PID='"+pid+"' order by ZLDJ ";
        List<Map<String,Object>> list= DatabaseHandlerDao.getInstance().queryForMaps(sql);

        Map<String,Object> mapData = new HashMap<String, Object>();
        mapData.put("data",list);
        String str="1";
        return mapData;
    }

    public List<Code> getZldj(){
        List<Map<String,Object>> dataMap = DatabaseHandlerDao.getInstance().queryForMaps("select sjmc  ,sxjb   from t_common_SJLX_CODE  where LXBM= 'ZZCSZLDJ'");
        Map<String,Object> map=new HashMap<String, Object>();
        List<Code> list=DataDictionaryUtil.getInstance().getDictionaryData("ZZCSZLDJ");
        String str="1";

        return list;
    }

    @Transactional
    public List<Map<String,Object>> getShccCslsh(String id){
        String sql="select PCH,ZL AS CSZZL,KCZL from T_ZZ_CSNZWXQ where PID='"+id+"' and (KCZL > 0) and (ZLDJ <> 4) order by ZLDJ";
        String sql1=" select CSLSH,PZ,PZBH,KCZL as QCZL from t_zz_csgl where id='"+id+"'";
        Map<String,Object> map=DatabaseHandlerDao.getInstance().queryForMap(sql1);
        map.get("CSLSH");
        map.get("PZ");
        map.get("PZBH");
        map.get("QCZL");
        List<Map<String,Object>> list=new ArrayList<Map<String, Object>>();
        list= DatabaseHandlerDao.getInstance().queryForMaps(sql);
        for(int i=0;i<list.size();i++){
            list.get(i).put("CSLSH",map.get("CSLSH"));
            list.get(i).put("PZ",map.get("PZ"));
            list.get(i).put("PZBH",map.get("PZBH"));
            //list.get(i).put("QCZL",map.get("QCZL"));
        }

        return list;
    }

    public Map<String,Object> getShcc(String cslsh){
        String sql="select CSLSH,PZ,PZBH,KCZL as QCZL from t_zz_csgl where CSLSH='"+cslsh+"'";
        Map<String,Object> map= DatabaseHandlerDao.getInstance().queryForMap(sql);
        int i=0;
        return map;
    }

    public Object saveForm(Map<String,String> map){
        if(map.get("CCLSH")==""){
            String cclsh=SerialNumberUtil.getInstance().getSerialNumber("ZZ","CCLSH",false);
            map.remove("CCLSH");
            map.put("CCLSH",cclsh);}
        String id=saveOne("T_ZZ_CCGL",map);

        return map;
    }

    /**
     *
     * @param map
     * @return
     */

    @Transactional
    public Object saveGrid(Map<String,String> map,String ids){
//        String cczl=map.get("CCZL");
//        String kczl=map.get("KCZL");
//        String sql="update T_ZZ_CSGL set CCZL=(to_number(CCZL)-"+cczl")";
        String cczl=map.get("CCZL");
        String pch=map.get("PCH");
        String sql="update T_ZZ_CSNZWXQ SET KCZL =(KCZL-"+cczl+") where pid='"+ids+"' and PCH='"+pch+"'";
        DatabaseHandlerDao.getInstance().executeSql(sql);
        String id= saveOne("T_ZZ_CCSHXX",map);
        return map;

    }

    /**
     *散货出场的出场重量同步到农作物详情，修改其库存重量
     */
    @Transactional
    public int editCsnzwxq(String id,String pch,int cczl){
        String sql="update T_ZZ_CSNZWXQ SET KCZL =(KCZL-"+cczl+") where pid='"+id+"' and PCH='"+pch+"'";
        return DatabaseHandlerDao.getInstance().executeSql(sql);


    }

    /**
     * 散货出场同步到出场管理和采收管理两张表中
     * @param id
     * @param zzl
     * @param csid
     * @param cczl
     * @return
     */
    @Transactional
    public int editCcglandCsgl(String id,int zzl,String csid,int cczl){
        String sql="update T_ZZ_CCGL SET ZZL="+zzl+"where id='"+id+"'";
        String sql1="update T_ZZ_CSGL SET KCZL=(KCZL-"+cczl+"),YCCZL=(YCCZL+"+cczl+") where id='"+csid+"'";
        DatabaseHandlerDao.getInstance().executeSql(sql1);
        return DatabaseHandlerDao.getInstance().executeSql(sql);
    }

    public int updateCsgl(String id,int kczl,int zlhj){
        String sql="update T_ZZ_CSGL set KCZL="+kczl+",ZLHJ="+zlhj+"where id='"+id+"'";
        return DatabaseHandlerDao.getInstance().executeSql(sql);
    }

    public Object getOneDataforxG(String id){
        String sql="select * from t_zz_csnzwxq where id = '"+id+"'";
        return  DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    public Object updateCsglZl(String id,int kczl,int zlhj){
        String sql="update T_ZZ_CSGL set KCZL=KCZL+"+kczl+",ZLHJ="+zlhj+"where id='"+id+"'";
        return DatabaseHandlerDao.getInstance().executeSql(sql);
    }

    /**
     * 根据地块编号查询农事项全部操作过的生产档案
     * @param dkbh
     * @return
     */
    public Object getScdabhByDkbh(String dkbh){
        String sql="select SCDABH,QYGLY from T_ZZ_SCDA T where T.QYBM = '"+SerialNumberUtil.getInstance().getCompanyCode()+"' AND dkbh= '"+dkbh+"' AND T.IS_DELETE='0'";
//        String sql="select t.scdabh,t.sum1 as QYGLY from   (select count(scdabh) as sum1,scdabh from (\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc from t_zz_scda a,t_zz_scbz b where a.id=b.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc from t_zz_scda a,t_zz_scgg b where a.id=b.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc from t_zz_scda a,t_zz_scsf b where a.id=b.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc from t_zz_scda a,t_zz_scyy b where a.id=b.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc from t_zz_scda a,t_zz_sccs b where a.id=b.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc from t_zz_scda a,t_zz_sccc b where a.id=b.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc from t_zz_scda a,t_zz_scqt b where a.id=b.pid) m where m.QYBM='"+SerialNumberUtil.getInstance().getCompanyCode()+"' AND m.dkbh = '"+dkbh+"'  group by scdabh) t\n" +
//                ",\n" +
//                "(select  count(scdabh) as sum2,scdabh from \n" +
//                "(\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc  from t_zz_scda a,t_zz_scbz b, t_zz_czjl c where a.id=b.pid and b.id=c.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc  from t_zz_scda a,t_zz_scgg b ,t_zz_czjl c where a.id=b.pid and b.id=c.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc  from t_zz_scda a,t_zz_scsf b, t_zz_czjl c where a.id=b.pid and b.id=c.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc  from t_zz_scda a,t_zz_scyy b, t_zz_czjl c where a.id=b.pid and b.id=c.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc  from t_zz_scda a,t_zz_sccs b, t_zz_czjl c where a.id=b.pid and b.id=c.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc  from t_zz_scda a,t_zz_sccc b, t_zz_czjl c where a.id=b.pid and b.id=c.pid\n" +
//                "union all\n" +
//                "select a.scdabh,a.ZZDYBH,a.dkbh,a.qybm,a.qygly,b.id,b.nszyxmc  from t_zz_scda a,t_zz_scqt b, t_zz_czjl c where a.id=b.pid and b.id=c.pid) \n" +
//                " m where m.QYBM='"+SerialNumberUtil.getInstance().getCompanyCode()+"' AND m.dkbh = '"+dkbh+"' group by scdabh) k where t.scdabh=k.scdabh and t.sum1=k.sum2";
                List<Map<String,Object>> list= DatabaseHandlerDao.getInstance().queryForMaps(sql);
                return list;
    }

}
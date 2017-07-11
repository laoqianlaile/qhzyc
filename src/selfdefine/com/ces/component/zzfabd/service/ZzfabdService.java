package com.ces.component.zzfabd.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ZzfabdService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {


    public Object getPzxxByPlbh(String plbh) {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select t.pz,t.pzbh from t_zz_xpzxx t where t.plbh = ? and t.qybm = ? and t.is_delete = '0'";
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{plbh,qybm});
        return maps;
    }
    //获取种苗种子
    public Object getZmzz() {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select t.zzzmmc,t.zzzmbh from t_sdzyc_zzzm t where t.qybm = ? and t.is_delete = '0'";
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{qybm});
        return maps;
    }
    //获取药材名称
    public List<Code> getYcmc(){
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        List<Code> list = new ArrayList<Code>();
        List<Map<String,Object>> dataMap = DatabaseHandlerDao.getInstance().queryForMaps("select distinct ZSSPM , YCMNAME  from T_SDZYC_ZYCSPBM t_  where  is_delete = '0' AND  qybm = '"+qybm+"'  AND  qylx = 'ZZ' ");
        if(dataMap != null  && !dataMap.isEmpty()){
            int showOrder = 1;
            for(Map<String,Object> map :dataMap){
                Code code = new Code();
                code.setName(map.get("YCMNAME").toString());
                code.setValue(map.get("ZSSPM").toString());
//                code.setShowOrder(Integer.parseInt(String.valueOf(map.get("SHOW_ORDER")) == "null" ? "0":String.valueOf(map.get("SHOW_ORDER"))));
                list.add(code);
            }
        }
        return list;
    }
    //通过ID获取种苗种子
    public Object getZmzzById(String id) {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select t.zzzmmc,t.zzzmbh from t_zz_fapz t where t.qybm = ? and t.id=?";
        Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql,new Object[]{qybm,id});
        return map;
    }
    //通过ID获取药材名称
    public Object getYcmcById(String id) {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select t.ycmc from t_zz_fapz t where t.qybm = ? and t.id=?";
        Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql,new Object[]{qybm,id});
        return map;
    }

    //种植方案保存
    public Object saveFaxx(Map<String, Object> dataMap) {
        StringBuilder sql ;
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        List<Map<String, String>> bzfas = ((List<Map<String, String>>) dataMap.get("bzfa") == null)?new ArrayList<Map<String, String>>():(List<Map<String, String>>) dataMap.get("bzfa");
        List<Map<String, String>> ggs = ((List<Map<String, String>>) dataMap.get("gg") == null)?new ArrayList<Map<String, String>>():(List<Map<String, String>>) dataMap.get("gg");
        List<Map<String, String>> sfs = ((List<Map<String, String>>) dataMap.get("sf") == null)?new ArrayList<Map<String, String>>():(List<Map<String, String>>) dataMap.get("sf");
        List<Map<String, String>> yys = ((List<Map<String, String>>) dataMap.get("yy") == null)?new ArrayList<Map<String, String>>():(List<Map<String, String>>) dataMap.get("yy");
        List<Map<String, String>> jcs = ((List<Map<String, String>>) dataMap.get("jc") == null)?new ArrayList<Map<String, String>>():(List<Map<String, String>>) dataMap.get("jc");
        List<Map<String, String>> ccs = ((List<Map<String, String>>) dataMap.get("cc") == null)?new ArrayList<Map<String, String>>():(List<Map<String, String>>) dataMap.get("cc");
        List<Map<String, String>> css = ((List<Map<String, String>>) dataMap.get("cs") == null)?new ArrayList<Map<String, String>>():(List<Map<String, String>>) dataMap.get("cs");
        List<Map<String, String>> qts = ((List<Map<String, String>>) dataMap.get("qt") == null)?new ArrayList<Map<String, String>>():(List<Map<String, String>>) dataMap.get("qt");

        List<Map<String, String>> bzfatrps = ((List<Map<String, String>>) dataMap.get("bzfatrp") == null)?new ArrayList<Map<String, String>>():(List<Map<String, String>>) dataMap.get("bzfatrp");
        List<Map<String, String>> ggtrps = ((List<Map<String, String>>) dataMap.get("ggtrp") == null)?new ArrayList<Map<String, String>>():(List<Map<String, String>>) dataMap.get("ggtrp");
        List<Map<String, String>> sftrps = ((List<Map<String, String>>) dataMap.get("sftrp") == null)?new ArrayList<Map<String, String>>():(List<Map<String, String>>) dataMap.get("sftrp");
        List<Map<String, String>> yytrps = ((List<Map<String, String>>) dataMap.get("yytrp") == null)?new ArrayList<Map<String, String>>():(List<Map<String, String>>) dataMap.get("yytrp");
        List<Map<String, String>> jctrps = ((List<Map<String, String>>) dataMap.get("jctrp") == null)?new ArrayList<Map<String, String>>():(List<Map<String, String>>) dataMap.get("jctrp");
        List<Map<String, String>> cctrps = ((List<Map<String, String>>) dataMap.get("cctrp") == null)?new ArrayList<Map<String, String>>():(List<Map<String, String>>) dataMap.get("cctrp");
        List<Map<String, String>> cstrps = ((List<Map<String, String>>) dataMap.get("cstrp") == null)?new ArrayList<Map<String, String>>():(List<Map<String, String>>) dataMap.get("cstrp");
        List<Map<String, String>> qttrps = ((List<Map<String, String>>) dataMap.get("qttrp") == null)?new ArrayList<Map<String, String>>():(List<Map<String, String>>) dataMap.get("qttrp");

        Map<String, String> fa = (Map<String, String>) dataMap.get("fa");
        if(fa.get("ID").equals("")){
            String zzfabh = SerialNumberUtil.getInstance().getSerialNumber("ZZ","ZZFABH",false);
            fa.put("ZZFABH", zzfabh);
        }
        fa.put("QYBM", companyCode);
        //保存主表信息
        String masterId = save("T_ZZ_FAPZ", fa, null);
        //保存播种信息
        for (Map<String, String> bzxx : bzfas) {
            //新增时去掉ID
            if(bzxx.get("ID").startsWith("temp_")){
                bzxx.put("ID","");
            }else{
                if("1".equals(bzxx.get("IS_START"))){
                    updateQsnsx(masterId);
                }
            }
            bzxx.remove("YL");
            bzxx.put("PID",masterId);
            bzxx.remove("COL_012");
            String bzfaId = save("t_zz_bzfa", bzxx, null);
            for (Map<String,String> bzfatrp : bzfatrps) {
                if (bzfatrp.get("NSZYXBH").equals(bzxx.get("NSZYXBH"))) {
                    bzfatrp.put("PID",bzfaId);
                    if(bzfatrp.get("ID").startsWith("temp_")){
                        bzfatrp.put("ID","");
                    }
                    bzfatrp.remove("COL_006");
                    save("t_zz_bzfatrp",bzfatrp,null);
                }
            }
        }
        //保存灌溉信息
        for (Map<String, String> ggxx : ggs) {
            //新增时去掉ID
            if(ggxx.get("ID").startsWith("temp_")){
                ggxx.put("ID","");
            }else{
                if("1".equals(ggxx.get("IS_START"))){
                    updateQsnsx(masterId);
                }
            }
            ggxx.put("PID",masterId);
            ggxx.remove("COL_013");
            String ggId = save("t_zz_gg", ggxx, null);
            for (Map<String,String> ggtrp : ggtrps) {
                if (ggtrp.get("NSZYXBH").equals(ggxx.get("NSZYXBH"))) {
                    ggtrp.put("PID",ggId);
                    if(ggtrp.get("ID").startsWith("temp_")){
                        ggtrp.put("ID","");
                    }
                    ggtrp.remove("COL_006");
                    save("t_zz_ggtrp",ggtrp,null);
                }
            }
        }
        //保存施肥信息
        for (Map<String, String> sfxx : sfs) {
            //新增时去掉ID
            if(sfxx.get("ID").startsWith("temp_")){
                sfxx.put("ID","");
            }else{
                if("1".equals(sfxx.get("IS_START"))){
                    updateQsnsx(masterId);
                }
            }
            sfxx.put("PID",masterId);
            sfxx.remove("COL_011");
            String sfId = save("t_zz_sf", sfxx, null);
            for (Map<String,String> sftrp : sftrps) {
                if (sftrp.get("NSZYXBH").equals(sfxx.get("NSZYXBH"))) {
                    sftrp.put("PID",sfId);
                    if(sftrp.get("ID").startsWith("temp_")){
                        sftrp.put("ID","");
                    }
                    sftrp.remove("COL_006");
                    save("t_zz_sftrp",sftrp,null);
                }
            }
        }
        //保存用药信息
        for (Map<String, String> yyxx : yys) {
            //新增时去掉ID
            if(yyxx.get("ID").startsWith("temp_")){
                yyxx.put("ID","");
            }else{
                if("1".equals(yyxx.get("IS_START"))){
                    updateQsnsx(masterId);
                }
            }
            yyxx.put("PID",masterId);
            yyxx.remove("COL_011");
            String yyId = save("t_zz_yy", yyxx, null);
            for (Map<String,String> yytrp : yytrps) {
                if (yytrp.get("NSZYXBH").equals(yyxx.get("NSZYXBH"))) {
                    yytrp.put("PID",yyId);
                    if(yytrp.get("ID").startsWith("temp_")){
                        yytrp.put("ID","");
                    }
                    yytrp.remove("COL_006");
                    save("t_zz_yytrp",yytrp,null);
                }
            }
        }
        //保存检测信息
//        for (Map<String, String> jcxx : jcs) {
//            //新增时去掉ID
//            if(jcxx.get("ID").startsWith("temp_")){
//                jcxx.put("ID","");
//            }
//            jcxx.put("PID", masterId);
//            jcxx.remove("COL_009");
//            String jcId= save("t_zz_jc", jcxx, null);
//            for (Map<String,String> jctrp : jctrps) {
//                if (jctrp.get("NSZYXBH").equals(jcxx.get("NSZYXBH"))) {
//                    jctrp.put("PID",jcId);
//                    if(jctrp.get("ID").startsWith("temp_")){
//                        jctrp.put("ID","");
//                    }
//                    jctrp.remove("COL_006");
//                    save("t_zz_jctrp",jctrp,null);
//                }
//            }
//        }
        //保存锄草信息
        for (Map<String, String> ccxx : ccs) {
            //新增时去掉ID
            if(ccxx.get("ID").startsWith("temp_")){
                ccxx.put("ID","");
            }else{
                if("1".equals(ccxx.get("IS_START"))){
                    updateQsnsx(masterId);
                }
            }
            ccxx.put("PID", masterId);
            ccxx.remove("COL_011");
            String jcId= save("t_zz_cc", ccxx, null);
            for (Map<String,String> cctrp : cctrps) {
                if (cctrp.get("NSZYXBH").equals(ccxx.get("NSZYXBH"))) {
                    cctrp.put("PID",jcId);
                    if(cctrp.get("ID").startsWith("temp_")){
                        cctrp.put("ID","");
                    }
                    cctrp.remove("COL_006");
                    save("t_zz_cctrp",cctrp,null);
                }
            }
        }
        //保存采收信息
        for (Map<String, String> csxx : css) {
            //新增时去掉ID
            if(csxx.get("ID").startsWith("temp_")){
                csxx.put("ID","");
            }else{
                if("1".equals(csxx.get("IS_START"))){
                    updateQsnsx(masterId);
                }
            }
            csxx.put("PID", masterId);
            csxx.remove("COL_011");
            String jcId= save("t_zz_cs", csxx, null);
            for (Map<String,String> cstrp : cstrps) {
                if (cstrp.get("NSZYXBH").equals(csxx.get("NSZYXBH"))) {
                    cstrp.put("PID",jcId);
                    if(cstrp.get("ID").startsWith("temp_")){
                        cstrp.put("ID","");
                    }
                    cstrp.remove("COL_006");
                    save("t_zz_cstrp",cstrp,null);
                }
            }
        }
        //保存其他信息
        for (Map<String, String> qtxx : qts) {
            //新增时去掉ID
            if(qtxx.get("ID").startsWith("temp_")){
                qtxx.put("ID","");
            }else{
                if("1".equals(qtxx.get("IS_START"))){
                    updateQsnsx(masterId);
                }
            }
            qtxx.put("PID", masterId);
            qtxx.remove("COL_010");
            String jcId= save("t_zz_qt", qtxx, null);
            for (Map<String,String> qttrp : qttrps) {
                if (qttrp.get("NSZYXBH").equals(qtxx.get("NSZYXBH"))) {
                    qttrp.put("PID",jcId);
                    if(qttrp.get("ID").startsWith("temp_")){
                        qttrp.put("ID","");
                    }
                    qttrp.remove("COL_006");
                    save("t_zz_qttrp",qttrp,null);
                }
            }
        }
        return masterId;
    }

    @Transactional
    public Object updateQsnsx(String masterId){
        StringBuilder sql;
        sql = new StringBuilder("UPDATE T_ZZ_BZFA T SET IS_START = '0',IS_START1 = '否' WHERE PID = '" + masterId + "'");
        DatabaseHandlerDao.getInstance().executeSql(sql.toString());
        sql = new StringBuilder("UPDATE T_ZZ_GG T SET IS_START = '0',IS_START1 = '否' WHERE PID = '" + masterId + "'");
        DatabaseHandlerDao.getInstance().executeSql(sql.toString());
        sql = new StringBuilder("UPDATE T_ZZ_SF T SET IS_START = '0',IS_START1 = '否' WHERE PID = '" + masterId + "'");
        DatabaseHandlerDao.getInstance().executeSql(sql.toString());
        sql = new StringBuilder("UPDATE T_ZZ_YY T SET IS_START = '0',IS_START1 = '否' WHERE PID = '" + masterId + "'");
        DatabaseHandlerDao.getInstance().executeSql(sql.toString());
        sql = new StringBuilder("UPDATE T_ZZ_CC T SET IS_START = '0',IS_START1 = '否' WHERE PID = '" + masterId + "'");
        DatabaseHandlerDao.getInstance().executeSql(sql.toString());
        sql = new StringBuilder("UPDATE T_ZZ_CS T SET IS_START = '0',IS_START1 = '否' WHERE PID = '" + masterId + "'");
        DatabaseHandlerDao.getInstance().executeSql(sql.toString());
        sql = new StringBuilder("UPDATE T_ZZ_QT T SET IS_START = '0',IS_START1 = '否' WHERE PID = '" + masterId + "'");
        DatabaseHandlerDao.getInstance().executeSql(sql.toString());
        return true;
    }
}
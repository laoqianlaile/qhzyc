package com.ces.component.trace.utils;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.xarch.core.web.listener.XarchListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Plain on 2016/5/20.
 */

@Component
public class StatisticalCodeUtil {

    public static StatisticalCodeUtil getInstance() {
        return XarchListener.getBean(StatisticalCodeUtil.class);
    }
    /**
     * 根据企业类型获得当前登录用户的14位企业代码
     * @param xtlx
     * @return
     */
    public String getFourteenQydm(String xtlx){
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        return getCpcCode(companyCode,xtlx+"QY");
       // return getHylb(xtlx,true)+getSevenQybm()+getJym(xtlx);
    }

    /**
     * 获得20位商品代码
     * @param xtlx
     * @param zsspm
     * @return
     */
    public String getTwentySpdm(String xtlx ,String zsspm){
        return getHylb(xtlx,false)+//企业行业类别 3位 273 0000784 101052002 2
                getSevenQybm()+//获得 7位企业编码
                zsspm+//获得9位商品代码
                getJym(xtlx);//获得1位校验码
    }
    /**
     * 获得20位商品代码
     * @param xtlx
     * @param zsspm
     * @return
     */
    public String getTwentySpdmZyc(String xtlx ,String zsspm){
        return getHylb(xtlx,false)+//企业行业类别 3位 273 0000784 101052002 2
                getSevenQybm()+//获得 7位企业编码
                zsspm+//获得9位商品代码
                getJym(xtlx);//获得1位校验码
    }
    /**
     * 获得20位商品代码
     * @param xtlx
     * @param zsspm
     * @return
     */
    public String getTwentySpdmYp(String xtlx ,String zsspm){
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        return /*getHylb(xtlx,false)+//企业行业类别 3位 273 0000784 101052002 2
                getSevenQybm()+//获得 7位企业编码*/
                getCpcCode(companyCode,"JJGQY")+//14位cpc
                zsspm.substring(4,zsspm.length())+//获得5位商品代码
                getJym(xtlx);//获得1位校验码
    }
    /**
     * 获得25位批次码
     * @param xtlx
     * @param sysName
     * @param type
     * @return
     */
    public String getTwentyFivePcm(String xtlx,String sysName,String type){
        // 类别 1 + 14位企业代码 + 6位年代轮 + 3位流水 +1位校验码
        return "1"+getFourteenQydm(xtlx)+getSixNdlhm()+SerialNumberUtil.getInstance().getSerialNumber(sysName,type,false)+getJym(xtlx);
    }
    /**
     *  获得35位追溯码
     * @param xtlx 种植：ZZ 粗加工：CJG 精加工：JJG
     * @param packAgeType  4：中药材追溯码（无论包装大小） 5：中药饮片小包装（袋） 6：中药饮片中包装（盒） 7：中药饮片大包装（箱）
     * @param zsspm 追溯商品码
     * @param sysName 系统名称
     * @param type 追溯编码类型
     * @return
     */
    public String getThirtyFiveZsm(String xtlx,String packAgeType,String zsspm,String sysName,String type){
        return packAgeType+getTwentySpdm(xtlx,zsspm)+getSixNdlhm()+SerialNumberUtil.getInstance().getSerialNumber(sysName,type,false)+getJym(xtlx);

    }
    public String getThirtyFiveZsmZyc(String xtlx,String packAgeType,String zsspm,String sysName,String type){
        return packAgeType+getTwentySpdmZyc(xtlx,zsspm)+getSixNdlhm()+SerialNumberUtil.getInstance().getSerialNumber(sysName,type,false)+getJym(xtlx);

    }
    public String getThirtyFiveZsmYp(String xtlx,String packAgeType,String zsspm,String sysName,String type){
        return packAgeType+getTwentySpdmYp(xtlx,zsspm)+getSixNdlhm()+SerialNumberUtil.getInstance().getSerialNumber(sysName,type,false)+getJym(xtlx);

    }
    /*获得企业cpc
  "ZZQY"  "CJGQY"  "YCJYQY"  "JJGQY"
   */

    public String getCpcCode(String companyCode,String dwlx) {
        String sql = "select cpccode from t_sdzyc_qyda where qybm = ? and dwlx = ?";
        Map<String,Object> mapData = DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{companyCode,dwlx});
        String cpccpde = String.valueOf(mapData.get("CPCCODE"));
        return cpccpde;

    }
    /**
     * 获得7位企业编码
     * @return
     */
    public String getSevenQybm(){
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        //companyCode = companyCode.substring(2,companyCode.length());
        companyCode  = getCpcCode(companyCode,"ZZQY");
        companyCode = companyCode.substring(6,companyCode.length()-1);
        return companyCode;
    }

    /**
     * 根据企业类型活动6位或3位行业类别
     * @param xtlx
     * @param isSix true 6位行业类别 false 3位行业类别
     * @return
     */
    public String getHylb(String xtlx,boolean isSix){
        String hylb = "017";//种植企业行业类别 3位
        String jym = "1" ; //获得1位校验码
        if( !"ZZ".equalsIgnoreCase(xtlx)){
            hylb="273";//中药饮片行业类别
        }
        if(isSix)
            return "101"+hylb;
        return hylb;
    }

    /**
     * 根据药材商品信息获得药典中对应的药材商品信息
     * @param zsspm
     * @return
     */
    public Map<String,Object> getZsycspxx(String zsspm){
        String sql = "SELECT YCLYNAME ," +//药材来源
                "YCLYBWNAME ," +  //药材来源部位
                "YCMNAME ," +     //药材名
                "YCXMNAME ," +    //药材商品规格或细名
                "YCLYCODE ," +    //药材来源编码
                "YCLYBWCODE," +   //药材来源部位编码
                "YCMCODE," +      //药材名编码
                "YCXMCODE," +     //药材商品规格或细名编码
                "BMHB," +         //编码合并
                "ZSSPM ," +       //追溯商品码
                "FROM T_SDZYC_YD WHERE IS_DELETE ='0' AND ZSSPM=? ";
        Map<String,Object> dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{zsspm});
        return dataMap;
    }

    /**
     * 获得校1位验码
     * @param xtlx
     * @return
     */
    public String getJym(String xtlx){
        if ( "ZZ".equalsIgnoreCase(xtlx) )
            return "1";
        else if ( "CJG".equalsIgnoreCase(xtlx) )
            return "2";
        else
            return "3";
    }

    /**
     * 获得6位年代轮换码
     * @return
     */
    public String getSixNdlhm(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        return sdf.format(new Date());
    }
}

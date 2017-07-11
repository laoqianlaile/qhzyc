package com.ces.component.bddt.service;

import com.ces.component.bddt.dao.BddtDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;


import com.ces.config.service.base.ShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BddtService extends ShowModuleDefineDaoService<StringIDEntity, BddtDao> {
    /**
     * 查询地址
     * @return
     */
    public List<Map<String,Object>> getAddress(){
        String sql = "";
        //查询1级城市
        sql = " select substr(t.cdbm,0,2) as cdbm,t.cdmc from t_common_cdxx t where t.cdbm like '%0000'";
        List<Map<String,Object>> sslist = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        for(Map<String,Object> ssmap:sslist){
            //查询2级城市
            sql = "select substr(t.cdbm,0,2) as id,substr(t.cdbm,0,4) as cdbm,t.cdmc from t_common_cdxx t where t.cdbm like '"+ssmap.get("CDBM")+"%00' and substr(t.cdbm,3,2) != '00'";
            ssmap.put("ej",DatabaseHandlerDao.getInstance().queryForMaps(sql));
            for(Map<String,Object> sqmap:(List<Map<String,Object>>)ssmap.get("ej")){
                //提取二级城市名
                sqmap.put("CDMC",sqmap.get("CDMC").toString().replace(ssmap.get("CDMC").toString(), ""));
                //查询3级城市
                sql = "select substr(t.cdbm,0,4) as id,substr(t.cdbm,0,6) as cdbm,t.cdmc from t_common_cdxx t where t.cdbm like '"+sqmap.get("CDBM")+"%' and substr(t.cdbm,5,2) != '00'";
                List<Map<String,Object>> qxlist = DatabaseHandlerDao.getInstance().queryForMaps(sql);
                for(Map<String,Object> qxmap:qxlist){
                    //提取三级城市名
                    qxmap.put("CDMC",qxmap.get("CDMC").toString().replace(sqmap.get("CDMC").toString(),"").replace(ssmap.get("CDMC").toString(),""));
                }
                sqmap.put("sj",qxlist);
            }
        }
        return sslist;
    }

    /**
     * 查询当前城市附近市场
     * @param cdbm 当前城市编号
     * @param xtlx 需要查询的系统类型
     * @return
     */
    public List<Map<String,Object>> getShop(String cdbm,String xtlx){
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        Map<String,Object> map = new HashMap<String, Object>();
        //如果cdbm是-1则是全国
        if("-1".equals(cdbm)){
            cdbm="%";
        }
        //如果xtlx是-1则是所有市场
        if("-1".equals(xtlx)){
            //种植场
            list.addAll(getZzShop(cdbm));
            //养殖场
            list.addAll(getYzShop(cdbm));
            //蔬菜批发市场
            list.addAll(getPcShop(cdbm));
            //屠宰场
            list.addAll(getTzShop(cdbm));
            //肉品批发市场
            list.addAll(getPrShop(cdbm));
            //团体采购
            list.addAll(getTtShop(cdbm));
            //超市
            list.addAll(getCsShop(cdbm));
            //零售
            list.addAll(getLsShop(cdbm));
            //餐饮
            list.addAll(getCyShop(cdbm));
            //加工
            list.addAll(getJgShop(cdbm));
            //水产
            list.addAll(getScShop(cdbm));
        }else{
                //sql = "select t.* from t_qypt_zhgl t where t.csxx like '"+cdbm+"%' and t.xtlx = '"+xtlx+"'";
            if("1".equals(xtlx)){
                //种植场
                list = getZzShop(cdbm);
            }else if("2".equals(xtlx)){
                //养殖场
                list = getYzShop(cdbm);
            }else if("3".equals(xtlx)){
                //蔬菜批发市场
                list = getPcShop(cdbm);
            }else if("4".equals(xtlx)){
                //屠宰场
                list = getTzShop(cdbm);
            }else if("5".equals(xtlx)){
                //肉品批发市场
                list = getPrShop(cdbm);
            }else if("6".equals(xtlx)){
                //团体采购
                list = getTtShop(cdbm);
            }else if("7".equals(xtlx)){
                //超市
                list = getCsShop(cdbm);
            }else if("8".equals(xtlx)){
                //零售
                list = getLsShop(cdbm);
            }else if("9".equals(xtlx)){
                //餐饮
                list = getCyShop(cdbm);
            }else if("10".equals(xtlx)){
                //加工
                list = getJgShop(cdbm);
            }else if("11".equals(xtlx)){
                //水产
                list = getScShop(cdbm);
            }
        }
        return list;
    }

    /**
     * 查询当前城市附近种植场
     * @param cdbm 当前城市编号
     * @return
     */
    public List<Map<String,Object>> getZzShop(String cdbm){
        //种植场
        String sql = "";
        String qyxxxx = "";
        Map<String,Object> map = new HashMap<String, Object>();
//      sql = "select b.jd,b.wd,a.qybm,a.zzjdmc as scmc,a.lxdh as lxdh,a.zzjdmj as zzjdmj,a.zzjddz as scdz,a.cdms as ms " +
//                "from t_zz_cdda a,t_qypt_zhgl b where a.qybm = b.zhbh and b.csxx like '"+cdbm+"%'";
        sql = "select a.qymc ,b.qymc as mdmc,b.jd,b.wd,a.qybm,a.zzjdmc as scmc,a.lxdh as lxdh,a.zzjdmj as zzjdmj,a.zzjddz as scdz,a.cdms as ms " +
                "from t_zz_cdda a right join t_qypt_zhgl b on a.qybm = b.zhbh where b.xtlx = '1' and b.csxx like '"+cdbm+"%'";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        for(Map<String,Object> xmap:list){
            qyxxxx = (xmap.get("QYMC")==null?"":"企业名称:"+xmap.get("QYMC").toString()) +
                    "<br/>种植场名称:"+(xmap.get("MDMC")==null?"":xmap.get("MDMC").toString()) +
                    "<br/>市场地址:"+(xmap.get("SCDZ")==null?"":xmap.get("SCDZ").toString()) +
                    "<br/>联系电话:"+(xmap.get("LXDH")==null?"":xmap.get("LXDH").toString()) +
                    "<br/>种植基地面积:"+(xmap.get("ZZJDMJ")==null?"":xmap.get("ZZJDMJ").toString()) +
                    "<br/>市场描述:"+(xmap.get("MS")==null?"":xmap.get("MS").toString());
            xmap.put("QYXXXX",qyxxxx);
            sql = "select rownum,a.*" +
                    "  from (select t.*" +
                    "          from t_common_qytp t" +
                    "         where qybm = '"+(xmap.get("QYBM")==null?"":xmap.get("QYBM").toString())+"'" +
                    "         order by t.uploadtime desc) a" +
                    " where rownum = 1";
            map = DatabaseHandlerDao.getInstance().queryForMap(sql);
            if(map!=null)
            xmap.put("QYTP",map.get("TPLJ"));
        }
        return list;
    }

    /**
     * 查询当前城市附近养殖场
     * @param cdbm 当前城市编号
     * @return
     */
    public List<Map<String,Object>> getYzShop(String cdbm){
        String sql = "";
        String qyxxxx = "";
        Map<String,Object> map = new HashMap<String, Object>();
//        sql = "select b.jd," +
//                "       b.wd," +
//                "       a.qybm," +
//                "       a.yzcmc," +
//                "       a.lxdh," +
//                "       a.yzcdz," +
//                "       a.yzcmj," +
//                "       a.cdms" +
//                "  from t_yz_cdda a, t_qypt_zhgl b" +
//                " where a.qybm = b.zhbh and b.csxx like '"+cdbm+"%'";
        sql = "select a.qymc,b.qymc as mdmc,b.jd," +
                "       b.wd," +
                "       a.qybm," +
                "       a.yzcmc," +
                "       a.lxdh," +
                "       a.yzcdz," +
                "       a.yzcmj," +
                "       a.cdms" +
                "  from t_yz_cdda a right join t_qypt_zhgl b" +
                " on a.qybm = b.zhbh where b.xtlx = '2' and b.csxx like '"+cdbm+"%'";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        for(Map<String,Object> xmap:list){
            qyxxxx = (xmap.get("QYMC")==null?"":"企业名称:"+xmap.get("QYMC").toString()) +
                    "<br/>养殖场名称:"+(xmap.get("MDMC")==null?"":xmap.get("MDMC").toString())+
                    "<br/>市场地址:"+(xmap.get("YZCDZ")==null?"":xmap.get("YZCDZ").toString())+
                    "<br/>联系电话:"+(xmap.get("LXDH")==null?"":xmap.get("LXDH").toString())+
                    "<br/>种植基地面积:"+(xmap.get("YZCMJ")==null?"":xmap.get("YZCMJ").toString())+
                    "<br/>市场描述:"+(xmap.get("CDMS")==null?"":xmap.get("CDMS").toString());
            xmap.put("QYXXXX",qyxxxx);
            sql = "select rownum,a.*" +
                    "  from (select t.*" +
                    "          from t_common_qytp t" +
                    "         where qybm = '"+(xmap.get("QYBM")==null?"":xmap.get("QYBM").toString())+"'" +
                    "         order by t.uploadtime desc) a" +
                    " where rownum = 1";
            map = DatabaseHandlerDao.getInstance().queryForMap(sql);
            if(map != null)
            xmap.put("QYTP",map.get("TPLJ"));
        }
        return list;
    }

    /**
     * 查询当前城市附近蔬菜批发场
     * @param cdbm 当前城市编号
     * @return
     */
    public List<Map<String,Object>> getPcShop(String cdbm){
        String sql = "";
        String qyxxxx = "";
        Map<String,Object> map = new HashMap<String, Object>();
        sql = "select b.qymc as mdmc ,b.jd, b.wd, a.qybm,a.qymc, a.pfscmc,a.jydz, a.lxdh" +
                "  from t_pc_qyda a right join t_qypt_zhgl b" +
                " on a.qybm = b.zhbh where b.xtlx = '3' and b.csxx like '"+cdbm+"%'";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        for(Map<String,Object> xmap:list){
            qyxxxx = (xmap.get("QYMC")==null?"":"企业名称:"+xmap.get("QYMC").toString()) +
                    "<br/>批发市场名称:"+(xmap.get("MDMC")==null?"":xmap.get("MDMC").toString()) +
                    "<br/>经营地址:"+(xmap.get("JYDZ")==null?"":xmap.get("JYDZ").toString()) +
                    "<br/>联系电话:"+(xmap.get("LXDH")==null?"":xmap.get("LXDH").toString());
            xmap.put("QYXXXX",qyxxxx);
            sql = "select rownum,a.*" +
                    "  from (select t.*" +
                    "          from t_common_qytp t" +
                    "         where qybm = '"+(xmap.get("QYBM")==null?"":xmap.get("QYBM").toString())+"'" +
                    "         order by t.uploadtime desc) a" +
                    " where rownum = 1";
            map = DatabaseHandlerDao.getInstance().queryForMap(sql);
            if(map != null)
            xmap.put("QYTP",map.get("TPLJ"));
        }
        return list;
    }

    /**
     * 查询当前城市附近屠宰场
     * @param cdbm 当前城市编号
     * @return
     */
    public List<Map<String,Object>> getTzShop(String cdbm){
        String sql = "";
        String qyxxxx = "";
        Map<String,Object> map = new HashMap<String, Object>();
        sql = "select a.qymc,b.qymc as mdmc,b.jd, b.wd, a.qybm, a.tzcmc, a.jydz, a.lxdh" +
                "  from t_tz_qyda a right join t_qypt_zhgl b" +
                " on a.qybm = b.zhbh where b.xtlx = '4' and b.csxx like '"+cdbm+"%'";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        for(Map<String,Object> xmap:list){
            qyxxxx = (xmap.get("QYMC")==null?"":"企业名称:"+xmap.get("QYMC").toString()) +
                    "<br/>屠宰场名称:"+(xmap.get("MDMC")==null?"":xmap.get("MDMC").toString())+
                    "<br/>经营地址:"+(xmap.get("JYDZ")==null?"":xmap.get("JYDZ").toString())+
                    "<br/>联系电话:"+(xmap.get("LXDH")==null?"":xmap.get("LXDH").toString());
            xmap.put("QYXXXX",qyxxxx);
            sql = "select rownum,a.*" +
                    "  from (select t.*" +
                    "          from t_common_qytp t" +
                    "         where qybm = '"+(xmap.get("QYBM")==null?"":xmap.get("QYBM").toString())+"'" +
                    "         order by t.uploadtime desc) a" +
                    " where rownum = 1";
            map = DatabaseHandlerDao.getInstance().queryForMap(sql);
            if(map != null)
            xmap.put("QYTP",map.get("TPLJ"));
        }
        return list;
    }

    /**
     * 查询当前城市附近肉品批发市场
     * @param cdbm 当前城市编号
     * @return
     */
    public List<Map<String,Object>> getPrShop(String cdbm){
        String sql = "";
        String qyxxxx = "";
        Map<String,Object> map = new HashMap<String, Object>();
        sql = "select b.qymc as mdmc,b.jd, b.wd, a.qybm,a.qymc, a.pfscmc, a.jydz, a.lxdh" +
                "  from t_pr_qyda a right join t_qypt_zhgl b" +
                " on a.qybm = b.zhbh where b.xtlx = '5' and b.csxx like '"+cdbm+"%'";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        for(Map<String,Object> xmap:list){
            qyxxxx = (xmap.get("QYMC")==null?"":"企业名称:"+xmap.get("QYMC").toString()) +
                    "<br/>批发市场名称:"+(xmap.get("MDMC")==null?"":xmap.get("MDMC").toString())+
                    "<br/>经营地址:"+(xmap.get("JYDZ")==null?"":xmap.get("JYDZ").toString())+
                    "<br/>联系电话:"+(xmap.get("LXDH")==null?"":xmap.get("LXDH").toString());
            xmap.put("QYXXXX",qyxxxx);
            sql = "select rownum,a.*" +
                    "  from (select t.*" +
                    "          from t_common_qytp t" +
                    "         where qybm = '"+(xmap.get("QYBM")==null?"":xmap.get("QYBM").toString())+"'" +
                    "         order by t.uploadtime desc) a" +
                    " where rownum = 1";
            map = DatabaseHandlerDao.getInstance().queryForMap(sql);
            if(map != null)
            xmap.put("QYTP",map.get("TPLJ"));
        }
        return list;
    }

    /**
     * 查询当前城市附近团体市场
     * @param cdbm 当前城市编号
     * @return
     */
    public List<Map<String,Object>> getTtShop(String cdbm){
        String sql = "";
        String qyxxxx = "";
        Map<String,Object> map = new HashMap<String, Object>();
        sql = "select b.qymc as mdmc,b.jd, b.wd, a.qybm,a.qymc, a.jydz, a.lxdh" +
                "  from t_tt_qyda a right join t_qypt_zhgl b" +
                " on a.qybm = b.zhbh where b.xtlx = '6' and b.csxx like '"+cdbm+"%'";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        for(Map<String,Object> xmap:list){
            qyxxxx = "<br/>市场名称:"+(xmap.get("MDMC")==null?"":xmap.get("MDMC").toString())+
                    "<br/>市场类型:团体采购"+
                    "<br/>经营地址:"+(xmap.get("JYDZ")==null?"":xmap.get("JYDZ").toString())+
                    "<br/>联系电话:"+(xmap.get("LXDH")==null?"":xmap.get("LXDH").toString());
            xmap.put("QYXXXX",qyxxxx);
            sql = "select rownum,a.*" +
                    "  from (select t.*" +
                    "          from t_common_qytp t" +
                    "         where qybm = '"+(xmap.get("QYBM")==null?"":xmap.get("QYBM").toString())+"'" +
                    "         order by t.uploadtime desc) a" +
                    " where rownum = 1";
            map = DatabaseHandlerDao.getInstance().queryForMap(sql);
            if(map != null)
            xmap.put("QYTP",map.get("TPLJ"));
        }
        return list;
    }

    /**
     * 查询当前城市附近超市
     * @param cdbm 当前城市编号
     * @return
     */
    public List<Map<String,Object>> getCsShop(String cdbm){
        String sql = "";
        String qyxxxx = "";
        Map<String,Object> map = new HashMap<String, Object>();
        sql = "select b.qymc as mdmc,b.jd, b.wd, a.qybm,a.qymc,a.csmc, a.jydz, a.lxdh" +
                "  from t_cs_qyda a right join t_qypt_zhgl b" +
                " on a.qybm = b.zhbh where b.xtlx = '7' and b.csxx like '"+cdbm+"%'";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        for(Map<String,Object> xmap:list){
            qyxxxx = (xmap.get("QYMC")==null?"":"企业名称:"+xmap.get("QYMC").toString()) +
                    "<br/>超市名称:"+(xmap.get("MDMC")==null?"":xmap.get("MDMC").toString())+
                    "<br/>经营地址:"+(xmap.get("JYDZ")==null?"":xmap.get("JYDZ").toString())+
                    "<br/>联系电话:"+(xmap.get("LXDH")==null?"":xmap.get("LXDH").toString());
            xmap.put("QYXXXX",qyxxxx);
            sql = "select rownum,a.*" +
                    "  from (select t.*" +
                    "          from t_common_qytp t" +
                    "         where qybm = '"+(xmap.get("QYBM")==null?"":xmap.get("QYBM").toString())+"'" +
                    "         order by t.uploadtime desc) a" +
                    " where rownum = 1";
            map = DatabaseHandlerDao.getInstance().queryForMap(sql);
            if(map != null)
            xmap.put("QYTP",map.get("TPLJ"));
        }
        return list;
    }

    /**
     * 查询当前城市附近零售市场
     * @param cdbm 当前城市编号
     * @return
     */
    public List<Map<String,Object>> getLsShop(String cdbm){
        String sql = "";
        String qyxxxx = "";
        Map<String,Object> map = new HashMap<String, Object>();
        sql = "select b.qymc as mdmc,b.jd, b.wd, a.qybm,a.qymc,a.lsscmc, a.jydz, a.lxdh" +
                "  from t_ls_qyda a right join t_qypt_zhgl b" +
                " on a.qybm = b.zhbh where b.xtlx = '8' and b.csxx like '"+cdbm+"%'";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        for(Map<String,Object> xmap:list){
            qyxxxx =(xmap.get("QYMC")==null?"":"企业名称:"+xmap.get("QYMC").toString()) +
                    "<br/>零售商场名称:"+(xmap.get("MDMC")==null?"":xmap.get("MDMC").toString())+
                    "<br/>经营地址:"+(xmap.get("JYDZ")==null?"":xmap.get("JYDZ").toString())+
                    "<br/>联系电话:"+(xmap.get("LXDH")==null?"":xmap.get("LXDH").toString());
            xmap.put("QYXXXX",qyxxxx);
            sql = "select rownum,a.*" +
                    "  from (select t.*" +
                    "          from t_common_qytp t" +
                    "         where qybm = '"+(xmap.get("QYBM")==null?"":xmap.get("QYBM").toString())+"'" +
                    "         order by t.uploadtime desc) a" +
                    " where rownum = 1";
            map = DatabaseHandlerDao.getInstance().queryForMap(sql);
            if(map != null)
            xmap.put("QYTP",map.get("TPLJ"));
        }
        return list;
    }

    /**
     * 查询当前城市附近餐饮市场
     * @param cdbm 当前城市编号
     * @return
     */
    public List<Map<String,Object>> getCyShop(String cdbm){
        String sql = "";
        String qyxxxx = "";
        Map<String,Object> map = new HashMap<String, Object>();
        sql = "select b.qymc as mdmc,b.jd, b.wd, a.qybm,a.qymc, a.jydz, a.lxdh" +
                "  from t_cy_qyda a right join t_qypt_zhgl b" +
                " on a.qybm = b.zhbh where b.xtlx = '9' and b.csxx like '"+cdbm+"%'";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        for(Map<String,Object> xmap:list){
            qyxxxx ="<br/>企业名称:"+(xmap.get("QYMC")==null?"":xmap.get("QYMC").toString())+
                    "<br/>企业类型:餐饮"+
                    "<br/>经营地址:"+(xmap.get("JYDZ")==null?"":xmap.get("JYDZ").toString())+
                    "<br/>联系电话:"+(xmap.get("LXDH")==null?"":xmap.get("LXDH").toString());
            xmap.put("QYXXXX",qyxxxx);
            sql = "select rownum,a.*" +
                    "  from (select  t.*" +
                    "          from t_common_qytp t" +
                    "         where qybm = '"+(xmap.get("QYBM")==null?"":xmap.get("QYBM").toString())+"'" +
                    "         order by t.uploadtime desc) a" +
                    " where rownum = 1";
            map = DatabaseHandlerDao.getInstance().queryForMap(sql);
            if(map != null)
            xmap.put("QYTP",map.get("TPLJ"));
        }
        return list;
    }

    /**
     * 查询当前城市附近加工市场
     * @param cdbm 当前城市编号
     * @return
     */
    public List<Map<String,Object>> getJgShop(String cdbm){
        String sql = "";
        String qyxxxx = "";
        Map<String,Object> map = new HashMap<String, Object>();
        sql = "select b.qymc as mdmc,b.jd, b.wd, a.qybm,a.qymc,a.jgcmc, a.jgcdz, a.lxdh" +
                "  from t_jg_jgcda a right join t_qypt_zhgl b" +
                " on a.qybm = b.zhbh where b.xtlx = '10' and b.csxx like '"+cdbm+"%'";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        for(Map<String,Object> xmap:list){
            qyxxxx ="<br/>企业名称:"+(xmap.get("QYMC")==null?"":xmap.get("QYMC").toString())+
                    "<br/>加工厂名称:"+(xmap.get("MDMC")==null?"":xmap.get("MDMC").toString())+
                    "<br/>加工厂地址:"+(xmap.get("JGCDZ")==null?"":xmap.get("JGCDZ").toString())+
                    "<br/>联系电话:"+(xmap.get("LXDH")==null?"":xmap.get("LXDH").toString());
            xmap.put("QYXXXX",qyxxxx);
            sql = "select rownum,a.*" +
                    "  from (select  t.*" +
                    "          from t_common_qytp t" +
                    "         where qybm = '"+(xmap.get("QYBM")==null?"":xmap.get("QYBM").toString())+"'" +
                    "         order by t.uploadtime desc) a" +
                    " where rownum = 1";
            map = DatabaseHandlerDao.getInstance().queryForMap(sql);
            if(map != null)
            xmap.put("QYTP",map.get("TPLJ"));
        }
        return list;
    }

    /**
     * 查询当前城市附近水产市场
     * @param cdbm 当前城市编号
     * @return
     */
    public List<Map<String,Object>> getScShop(String cdbm){
        String sql = "";
        String qyxxxx = "";
        Map<String,Object> map = new HashMap<String, Object>();
        sql = "select b.jd, b.wd,a.qybm,a.qymc,a.lxdz, a.lxdh" +
                "  from t_sc_qyda a right join t_qypt_zhgl b" +
                " on a.qybm = b.zhbh where b.xtlx = '11' and b.csxx like '"+cdbm+"%'";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        for(Map<String,Object> xmap:list){
            qyxxxx ="<br/>市场名称:"+(xmap.get("QYMC")==null?"":xmap.get("QYMC").toString())+
                    "<br/>市场类型:水产"+
                    "<br/>企业地址:"+(xmap.get("LXDZ")==null?"":xmap.get("LXDZ").toString())+
                    "<br/>联系电话:"+(xmap.get("LXDH")==null?"":xmap.get("LXDH").toString());
            xmap.put("QYXXXX",qyxxxx);
            sql = "select rownum,a.*" +
                    "  from (select t.*" +
                    "          from t_common_qytp t" +
                    "         where qybm = '"+(xmap.get("QYBM")==null?"":xmap.get("QYBM").toString())+"'" +
                    "         order by t.uploadtime desc) a" +
                    " where rownum = 1";
            map = DatabaseHandlerDao.getInstance().queryForMap(sql);
            if(map != null)
            xmap.put("QYTP",map.get("TPLJ"));
        }
        return list;
    }

    /**
     * 获取系统类型
     * @return
     */
    public List<Map<String,Object>> getXtlx(){
        String sql = "";
        sql = "select * from T_XTPZ_CODE t where t.code_type_code = 'CSPTXTLX'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql);
    }


}

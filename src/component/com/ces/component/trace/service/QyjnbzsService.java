package com.ces.component.trace.service;

import com.ces.component.trace.dao.QyjnbzsDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.xarch.core.entity.StringIDEntity;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hpsgt on 2016-11-17.
 */
@Component
public class QyjnbzsService extends TraceShowModuleDefineDaoService<StringIDEntity, QyjnbzsDao> {

    public Map<String,Object> searchYpsczsxx(String id){
        Map<String,Object> dataMap = new HashMap<String,Object>();
        List<Map<String,Object>> cjgYcList = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> zzYcList = new ArrayList<Map<String, Object>>();
        Map<String, Object> scMap =new HashMap<String,Object>();
        Map<String, Object> jyMap =new HashMap<String,Object>();
        List<Map<String,Object>> tlList = new ArrayList<Map<String, Object>>();
        String sqljc = "select yy.*,qq.qymc from v_sdzyc_zyyypjc yy, t_sdzyc_qyda qq where yy.id =?  and qq.qybm = yy.csbm ";
        Map<String,Object> jcMap = DatabaseHandlerDao.getInstance().queryForMap(sqljc,new String[]{id});
        if(jcMap!=null && jcMap.size()>0) {
            String bzpch = String.valueOf(jcMap.get("BZPCH"));
            String jhpch = String.valueOf(jcMap.get("JHPCH"));
            String jySql = "select cc.*,qq.qymc from v_sdzyc_jjg_ypjyxx cc, t_sdzyc_qyda qq where qq.qybm = cc.qybm and qq.dwlx='JJGQY' and cc.bzpch=? and cc.qyxsddh = ?";
            jyMap = DatabaseHandlerDao.getInstance().queryForMap(jySql, new String[]{bzpch,jhpch});
            String scpch = String.valueOf(jyMap.get("PCH"));
            String scSql = "select * from t_sdzyc_jjg_ypscxx  where scpch = ? ";
            Map<String, Object> scxxMap = DatabaseHandlerDao.getInstance().queryForMap(scSql, new String[]{scpch});
            String scid =  String.valueOf(scxxMap.get("ID"));
            String sql = "select yy.*,qq.qymc from t_sdzyc_jjg_ypscxx yy, t_sdzyc_qyda qq where yy.id =? and qq.qybm = yy.qybm and qq.dwlx='JJGQY'";
            //生产主表信息
            scMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{scid});
            //根据yycpch（原药材批次号，查询来源信息）查询上游环节信息

            if (scMap == null || scMap.size() == 0) {
                String yycSql = "SELECT DISTINCT\n" +
                        "\tsc.jgpch,\n" +
                        "\tsc.ylmc ycmc,\n" +
                        "\tsc.qypch,\n" +
                        "  sc.qyjgpch,\n" +
                        "\tsc.ylpch,\n" +
                        "\tsc.scrq,\n" +
                        "\trr.rksj,\n" +
                        "\trr.rkzl,\n" +
                        "\trr.rkdjfzr,\n" +
                        "\trr.gys,\n" +
                        "\tqy.qymc\n" +
                        "FROM\n" +
                        "\tt_sdzyc_cjg_ycjgxx sc,\n" +
                        "\tt_sdzyc_cjg_ylrkxx rr,\n" +
                        "\tt_sdzyc_qyda qy\n" +
                        "WHERE qy.qybm = rr.qybm\n" +
                        "AND qy.dwlx = 'CJGQY'\n" +
                        "AND rr.pch = sc.ylpch\n" +
                        "and sc.id = ?";
                Map<String, Object> cjgMap = DatabaseHandlerDao.getInstance().queryForMap(yycSql, new String[]{id});
                //处理是否外购数据
                String cspch = String.valueOf(cjgMap.get("YLPCH"));
                String zzSql = "select zz.qypch,cc.kssj,cc.jssj,cc.cszl,zz.zzsj,zz.jdmc,zz.ycdm,zz.ycmc,cc.qycspch,zz.zzpch from t_sdzyc_csglxx cc, T_ZZ_SCDA zz where cspch=? and cc.zzpch = zz.ZZPCH";
                Map<String, Object> zzMap = DatabaseHandlerDao.getInstance().queryForMap(zzSql, new String[]{cspch});
                cjgYcList.add(cjgMap);
                zzYcList.add(zzMap);
                dataMap.put("cjg", cjgYcList);
                dataMap.put("zz", zzYcList);
                return dataMap;
            }
            String tlSql = "select y.yycpch,y.lldh,y.cspch,y.ycmc,y.ycdm,c.gys,c.cd,c.rksj,c.cgdh,c.qypch,qq.qymc from t_sdzyc_jjg_ypsctl y ,t_sdzyc_jjg_yycrkxx c ,t_sdzyc_qyda qq where y.yycpch=c.pch and qq.qybm = c.qybm and qq.dwlx='JJGQY' and y.pid =?";
            tlList = DatabaseHandlerDao.getInstance().queryForMaps(tlSql,new String []{scid});
        }else{
            String sql = "select yy.*,qq.qymc from t_sdzyc_jjg_ypscxx yy, t_sdzyc_qyda qq where yy.id =? and qq.qybm = yy.qybm and qq.dwlx='JJGQY'";
            //生产主表信息
             scMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{id});
            //根据yycpch（原药材批次号，查询来源信息）查询上游环节信息

            if (scMap == null || scMap.size() == 0) {
                String yycSql = "SELECT DISTINCT\n" +
                        "\tsc.jgpch,\n" +
                        "\tsc.ylmc ycmc,\n" +
                        "\tsc.qypch,\n" +
                        "  sc.qyjgpch,\n" +
                        "\tsc.ylpch,\n" +
                        "\tsc.scrq,\n" +
                        "\trr.rksj,\n" +
                        "\trr.rkzl,\n" +
                        "\trr.rkdjfzr,\n" +
                        "\trr.gys,\n" +
                        "\tqy.qymc\n" +
                        "FROM\n" +
                        "\tt_sdzyc_cjg_ycjgxx sc,\n" +
                        "\tt_sdzyc_cjg_ylrkxx rr,\n" +
                        "\tt_sdzyc_qyda qy\n" +
                        "WHERE qy.qybm = rr.qybm\n" +
                        "AND qy.dwlx = 'CJGQY'\n" +
                        "AND rr.pch = sc.ylpch\n" +
                        "and sc.id = ?";
                Map<String, Object> cjgMap = DatabaseHandlerDao.getInstance().queryForMap(yycSql, new String[]{id});
                //处理是否外购数据
                String cspch = String.valueOf(cjgMap.get("YLPCH"));
                String zzSql = "select zz.qypch,cc.kssj,cc.jssj,cc.cszl,zz.zzsj,zz.jdmc,zz.ycdm,zz.ycmc,cc.qycspch,zz.zzpch from t_sdzyc_csglxx cc, T_ZZ_SCDA zz where cspch=? and cc.zzpch = zz.ZZPCH";
                Map<String, Object> zzMap = DatabaseHandlerDao.getInstance().queryForMap(zzSql, new String[]{cspch});
                cjgYcList.add(cjgMap);
                zzYcList.add(zzMap);
                dataMap.put("cjg", cjgYcList);
                dataMap.put("zz", zzYcList);
                return dataMap;
            }
            String tlSql = "select y.yycpch,y.lldh,y.cspch,y.ycmc,y.ycdm,c.gys,c.cd,c.rksj,c.cgdh,c.qypch,qq.qymc from t_sdzyc_jjg_ypsctl y ,t_sdzyc_jjg_yycrkxx c ,t_sdzyc_qyda qq where y.yycpch=c.pch and qq.qybm = c.qybm and qq.dwlx='JJGQY' and y.pid =?";
            tlList = DatabaseHandlerDao.getInstance().queryForMaps(tlSql,new String []{id});
        }
        //生产用料信息



        //封装数据每个子系统一个map数据
        for(Map<String,Object> map : tlList){
            //根据采购单号及采购批次信息确认任生产批次信息，并查询出药材入库信息。
            String yycpch = String.valueOf(map.get("YYCPCH"));
            String cgdh = String.valueOf(map.get("CGDH"));
            String yycSql = "SELECT DISTINCT aa.pch,aa.xsddh ,aa.ycmc,aa.qypch,pp.jysj,sc.ylpch,sc.scrq,rr.rksj,rr.rkzl,rr.rkdjfzr,rr.gys,qy.qymc from T_SDZYC_CJG_YCJYXXXX aa,T_SDZYC_CJG_YCJYXX pp,t_sdzyc_cjg_ycjgxx sc,t_sdzyc_cjg_ylrkxx rr,t_sdzyc_qyda qy where pp.xsddh = aa.xsddh AND qy.qybm = rr.qybm  AND qy.dwlx='CJGQY' and aa.xsddh like '%"+cgdh+"%' and sc.jgpch=aa.pch and rr.pch=sc.ylpch and aa.pch = ?";
            Map<String,Object> cjgMap = DatabaseHandlerDao.getInstance().queryForMap(yycSql,new String[]{yycpch});
            //处理是否外购数据
            String cspch = String.valueOf(cjgMap.get("YLPCH"));
            String zzSql = "select zz.qypch,cc.kssj,cc.jssj,cc.cszl,zz.zzsj,zz.jdmc,zz.ycdm,zz.ycmc,cc.qycspch,zz.zzpch from t_sdzyc_csglxx cc, T_ZZ_SCDA zz where cspch=? and cc.zzpch = zz.ZZPCH";
            Map<String,Object> zzMap = DatabaseHandlerDao.getInstance().queryForMap(zzSql,new String[]{cspch});
            cjgYcList.add(cjgMap);
            zzYcList.add(zzMap);
        }
        dataMap.put("jcxx",jcMap);
        dataMap.put("jyxx",jyMap);
        dataMap.put("scxx",scMap);
        dataMap.put("ylxx",tlList);
        dataMap.put("cjg",cjgYcList);
        dataMap.put("zz",zzYcList);
        return dataMap;
    }
}

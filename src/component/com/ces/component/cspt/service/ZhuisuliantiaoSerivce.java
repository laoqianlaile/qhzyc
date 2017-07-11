package com.ces.component.cspt.service;

import ces.coral.lang.StringUtil;
import com.ces.component.cspt.dao.ZhuisuliantiaoDao;
import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.xarch.core.service.StringIDDefineDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ZhuisuliantiaoSerivce extends StringIDDefineDaoService<TCsptZsxxEntity, ZhuisuliantiaoDao> {


    @Autowired
    @Override
    public void setDao(ZhuisuliantiaoDao dao) {
        super.setDao(dao);
    }



    /**
     * 同步追溯信息表
     *
     * @param entity
     */
    @Transactional
    public void insert(TCsptZsxxEntity entity) {
        this.getDao().save(entity);
        /*if(StringUtil.isNotBlank(entity.getZsm())){//修改进场是否有交易状态
            updateHasTran(entity.getJhpch(),entity.getJypzh());
		}*/
    }

    /**
     * 根据关联ID获取追溯信息
     *
     * @param refId     关联ID
     * @return  追溯实体类
     */
    public TCsptZsxxEntity getTCsptZsxxEntityByRefId(String refId) {
        return (TCsptZsxxEntity) this.getDao().getTCsptZsxxByRefId(refId);
    }


    /**
     * 正向追溯到加工厂，查询加工厂出场时生成批次号
     *
     * @param zsm 追溯码
     * @return
     */
    public List queryJgccPch(String zsm) {
        String sql = "SELECT T.PCH,T.ZSM FROM T_JG_CPCCXX T,T_JG_XLMXXX X,T_JG_YLHWXX Y WHERE T.DDBH=X.DDBH" +
                " AND Y.YLPCH = X.YLBH AND Y.ZSPZH=? ";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{zsm});
    }

    /**
     * 获取加工产品原材料信息
     *
     * @param zsm
     * @return
     */
    public List<Map<String, Object>> queryJgzsxx(String zsm) {
        String sql = "SELECT H.ZSPZH,T.BZBH,T.CPMC,T.DDBH,Y.YLBH,Y.YLMC,Y.YLPC,T.QYBM,'10' AS XTLX " +
                " FROM T_JG_BZXX T,T_JG_XLMXXX Y,T_JG_YLHWXX H WHERE T.DDBH=Y.DDBH AND T.QYBM=H.QYBM AND T.QYBM=Y.QYBM" +
                " AND Y.YLPC=H.YLPCH AND T.ZSM=? ";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{zsm});
    }


    /**
     * 获取正向节点树
     *
     * @param zzyzpch 企业编码和种植养殖批次号的合成编码
     * @return
     */
    public Map getTree(String zzyzpch) {
        //企业编码
        String qybm = zzyzpch.substring(0, 9);
        //种植养殖批次号
        zzyzpch = zzyzpch.substring(9, zzyzpch.length());
        //系统类型
        String xtlx = getZzYzxtlx(zzyzpch, qybm);
        List<String> jhpchList = new ArrayList();
        List li = new ArrayList();
        Map startNode = new HashMap();
        jhpchList = queryJhpchByZzyzpch(zzyzpch, qybm, xtlx);
        if(jhpchList.size()>0) {
            for (String jhpch : jhpchList) {
                List list = queryListByJhpch(jhpch);
                startNode = getStart(jhpch, list);
                Map child = getChild(startNode, list);
                if (!child.isEmpty()) {
                    li.add(child);
                }
            }
            startNode.put("children", li);
        }
        return startNode;
    }

    /**
     * 获取对应系统类型
     *
     * @param zzyzpch
     * @param qybm
     * @return
     */
    public String getZzYzxtlx(String zzyzpch, String qybm) {
        String sql = "SELECT XTLX FROM T_CSPT_JYXX T WHERE T.ZZYZPCH =? AND T.QYBM =? GROUP BY T.XTLX";
        Map map = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{zzyzpch, qybm});
        return (String) map.get("XTLX");
    }

    /**
     * 根据种植养殖批次号获取进货批次号
     *
     * @param zzyzpch
     * @param qybm
     * @param xtlx
     * @return
     */
    public List<String> queryJhpchByZzyzpch(String zzyzpch, String qybm, String xtlx) {
        String sql = "";
        if ("1".equals(xtlx)) {
            sql += "SELECT T.PCH FROM T_ZZ_CCXX T WHERE T.ZZPCH=? AND T.QYBM = ?";
        } else if ("2".equals(xtlx)) {
            sql += "SELECT T.PCH FROM T_YZ_CLXX T WHERE T.YZPCH=? AND T.QYBM = ?";
        }else if(StringUtil.isBlank(xtlx)){
            return new ArrayList<String>();
        }
        return DatabaseHandlerDao.getInstance().queryForList(sql, new Object[]{zzyzpch, qybm});
    }

    /**
     * 根据jhpch获取某一批次的流通信息
     *
     * @param jhpch
     * @return list
     */
    public List queryListByJhpch(String jhpch) {
        String sql1 = "SELECT T.* FROM T_CSPT_JYXX T WHERE T.JHPCH = ?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql1, new Object[]{jhpch});
    }


    /**
     * 获取起点
     *
     * @param jhpch
     * @param li
     * @return
     */
    public Map getStart(String jhpch, List<Map> li) {
        Map start = new HashMap();
        for (Map m : li) {
            if (m.get("JYPZH") == null && m.get("ZSM") != null) {
                start = m;
            }
        }
        return start;
    }

    /**
     * 递归查询子节点
     *
     * @param jyMap 父节点
     * @param li    剩余节点list
     * @return map父节点
     */
    public Map getChild(Map jyMap, List<Map> li) {
        Map jcMap = new HashMap();//进厂记录 唯一
        List<Map> outList = new ArrayList();//交易记录
        Iterator it = li.iterator();
        while (it.hasNext()) {//获取所有子记录（包括进场出厂记录）
            Map m = (Map) it.next();
            if (jyMap.get("ZSM").equals(m.get("JYPZH"))) {
                if (m.get("ZSM") == null) {//进厂记录
                    jcMap = m;
                } else {//交易记录
                    outList.add(m);
                }
            }
        }
        li.remove(jcMap);
        li.removeAll(outList);
        List childList = new ArrayList();
        if ("10".equals(jcMap.get("XTLX"))) {//判断是否为加工厂
            List<Map> jgCcxx = this.queryJgccPch(jcMap.get("JYPZH").toString());
            for (Map m : jgCcxx) {//记录子节点
                //获取出场批次
                List pcList = queryListByJhpch(m.get("PCH").toString());
                //获取加工初始节点
                Map startNode = getJgStartNode(m.get("PCH").toString(), m.get("ZSM").toString());
                childList.add(getChild(startNode, pcList));
            }
            jcMap.put("children", childList);
        } else {
            for (Map map1 : outList) {
                if (map1.get("JYPZH").equals(jcMap.get("JYPZH"))) {//获取进厂记录对应交易记录
                    Map map = getChild(map1, li);
                    if (!map.isEmpty())
                        childList.add(map);
                }
                jcMap.put("children", childList);
            }
        }
        return jcMap;
    }


    /**
     * 获取加工厂交易对应开始节点
     *
     * @param jhpch 进货批次号
     * @param zsm   追溯码
     * @return
     */
    public Map getJgStartNode(String jhpch, String zsm) {
        String sql = "SELECT T.* FROM T_CSPT_JYXX T WHERE T.JHPCH=? AND T.ZSM=?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{jhpch, zsm});
    }

    /**
     * 获取追溯详细信息
     *
     * @param entity
     * @return
     */
    public Map getZhuisuXxxx(TCsptZsxxEntity entity) {
        Map<String, Map<?, ?>> zsMap = new HashMap();
        if ("1".equals(entity.getXtlx())) {//种植信息
            Map<?, ?> zzCcxx = this.getZzCcxx(entity.getRefId());
            zsMap.put("zzQyxx", getZzQyxx(entity.getQybm()));
            zsMap.put("zzCcxx", zzCcxx);
            zsMap.put("zzZpxx", getZzZpxx((String) zzCcxx.get("ZZPCH"), entity.getQybm()));
        } else if ("2".equals(entity.getXtlx())) {//养殖信息
            Map<?, ?> yzClxx = this.getYzClxx(entity.getRefId());
            zsMap.put("yzQyxx", getYzQyxx(entity.getQybm()));
            zsMap.put("yzClxx", yzClxx);
            zsMap.put("yzJlxx", getYzJlxx((String) yzClxx.get("YZPCH"), entity.getQybm()));
        } else if ("3".equals(entity.getXtlx())) {//蔬菜批发信息
//            Map<?, ?> pcJyxx = getPcJyxx(entity.getRefId());
            Map pcJclhxx = getPcJclhxx(entity.getRefId());
            zsMap.put("pcQyxx", getPcQyxx(entity.getQybm()));
            //zsMap.put("pcJyxx", getPcJyxx((String) pcJclhxx.get("JCLHBH"), (String) pcJclhxx.get("JYPZH")));
            zsMap.put("pcJclhxx", pcJclhxx);
//           zsMap.put("pcJcxx", getPcJcxx((String) pcJclhxx.get("JCLHBH")));
        } else if ("4".equals(entity.getXtlx())) {//屠宰信息
//            Map<?, ?> tzJyxx = getTzJyxx(entity.getRefId());
            Map<?, ?> tzJcxx = getTzJcxx((String) entity.getRefId());
            zsMap.put("tzQyxx", getTzQyxx(entity.getQybm()));
//            zsMap.put("tzJyxx", tzJyxx);
            zsMap.put("tzJcxx", tzJcxx);
//            zsMap.put("tzSzJyxx", getTzSzJyxx((String) tzJyxx.get("SZCDJYZH")));
        } else if ("5".equals(entity.getXtlx())) {//猪肉批发信息
            //Map<?, ?> prJyxx = getRpJyxx(entity.getRefId());
            zsMap.put("rpQyxx", getRpQyxx(entity.getQybm()));
            //zsMap.put("rpJyxx", prJyxx);
            zsMap.put("rpJcxx", getRpJcxx((String) entity.getRefId()));
            //zsMap.put("rpJcxx2", getRpjcxx2((String) entity.getJypzh()));
        } else if ("6".equals(entity.getXtlx())) {//团体采购
            zsMap.put("ttQyxx", getTtQyxx(entity.getQybm()));
            zsMap.put("ttScJcxx", getTtScJcxx(entity.getRefId()));
            zsMap.put("ttRpJcxx", getTtRpJcxx(entity.getRefId()));
            zsMap.put("ttCpJcxx", getTtCpJcxx(entity.getRefId()));
        } else if ("7".equals(entity.getXtlx())) {//超市进场信息
            zsMap.put("csQyxx", getCsQyxx(entity.getQybm()));
            Map<?, ?> csScjcxx = getCsScJcxx(entity.getRefId());//蔬菜进场信息
            Map<?, ?> csRpjcxx = getCsRpJcxx(entity.getRefId());//肉品进场信息
            Map<?, ?> csCpjcxx = getCsCpJcxx(entity.getRefId());//肉品进场信息
            if (!csScjcxx.isEmpty())
                zsMap.put("csScJcxx", csScjcxx);
            else if (!csRpjcxx.isEmpty())
                zsMap.put("csRpJcxx", csRpjcxx);
            else
                zsMap.put("csCpjcxx",csCpjcxx);
        } else if ("8".equals(entity.getXtlx())) {//零售市场
            zsMap.put("lsQyxx", getLsQyxx(entity.getQybm()));
            zsMap.put("lsScJcxx", getLsScJcxx(entity.getRefId()));
            zsMap.put("lsRpJcxx", getLsRpJcxx(entity.getRefId()));
            zsMap.put("lsCpJcxx", getLsCpJcxx(entity.getRefId()));
        } else if ("9".equals(entity.getXtlx())) {//餐饮
            zsMap.put("cyQyxx",getCyQyxx(entity.getQybm()));
            zsMap.put("cyScJcxx",getCyScJcxx(entity.getRefId()));
            zsMap.put("cyRpJcxx",getCyRpJcxx(entity.getRefId()));
            zsMap.put("cyCpJcxx",getCyCpJcxx(entity.getRefId()));
        } else if ("10".equals(entity.getXtlx())) {//加工
            zsMap.put("jgQyxx",getJgQyxx(entity.getQybm()));
            zsMap.put("jgYljcxx",getJgYljcxx(entity.getRefId()));
        }
        return zsMap;
    }


    /**
     * 获取种植档案信息
     *
     * @param qybm 企业编码
     * @return
     */
    public Map getZzQyxx(String qybm) {
        String sql = "SELECT T.LXDH,T.QYMC,T.ZZJDMC as MDMC,T.GSZCDJZH,T.CDMC,T.CDZSH,T.ZZJDMJ,T.ZZJDDZ AS DZ,T.CDMS FROM T_ZZ_CDDA T WHERE T.QYBM='" + qybm + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
     * 获取种植栽培信息
     *
     * @param zzpch
     * @return
     */
    public Map getZzZpxx(String zzpch, String qybm) {
        String sql = "SELECT T.ZPRQ,T.SYCT,T.SYCZ,T.FZR,T.SPMC FROM T_ZZ_ZPXX T WHERE T.ZZPCH = ? AND T.QYBM=?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{zzpch, qybm});
    }

    /**
     * 获取种植施肥信息（分页）
     *
     * @param pageRequest
     * @param zzpch
     * @return
     */
    public Page getZzSfxx(PageRequest pageRequest, String zzpch, String qybm) {
        String sql = "SELECT T.ID,T.SFSJ,T.SYFL,T.FZR FROM T_ZZ_SFXX T WHERE T.ZZPCH='" + zzpch + "' AND T.QYBM='" + qybm + "'";
        return queryPage(pageRequest, sql);
    }


    /**
     * 种植施药信息（分页）
     *
     * @param pageRequest
     * @param zzpch
     * @return
     */
    public Page getZzSyxx(PageRequest pageRequest, String zzpch, String qybm) {
        String sql = "SELECT T.ID,T.SYNY,T.SYSJ,T.FZR FROM T_ZZ_SYXX T WHERE T.ZZPCH='" + zzpch + "' AND T.QYBM='" + qybm + "'";
        return queryPage(pageRequest, sql);
    }


    /**
     * 获取种植出厂信息
     *
     * @param refId
     * @return
     */
    public Map getZzCcxx(String refId) {
        String sql1 = "SELECT T.QYBM,T.ZZPCH,T.FZR ,T.CSRQ,T.CCFS,T.CCRQ,T.CDZMH,T.JCHGZH,T.ZL AS CCZL,T.DDD,T.YSCPH FROM T_ZZ_CCXX T WHERE T.ID='" + refId + "'";
        Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql1);
        String zzpch = map.get("ZZPCH").toString();
        String qybm = map.get("QYBM").toString();
        String sql2 = "SELECT count(t.id) AS CCCS,sum(t.zl) AS ZL from T_ZZ_CCXX t where t.zzpch = '"+zzpch+"' and t.qybm = '" + qybm + "'";
        Map<String,Object> zlMap = DatabaseHandlerDao.getInstance().queryForMap(sql2);
        map.put("ZL",zlMap.get("ZL"));
        return map;
    }

    /**
     * 获取养殖企业信息
     *
     * @param qybm
     * @return
     */
    public Map getYzQyxx(String qybm) {
        String sql = "SELECT T.YZCMC as MDMC,T.QYMC,T.GSZCDJZH,T.LXDH,T.CDMC,T.CDZSH,T.DWFYTJHGZH,T.YZCMJ,T.YZCDZ as DZ,T.CDMS FROM T_YZ_CDDA T WHERE T.QYBM = '" + qybm + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
     * 获取养殖进栏信息
     *
     * @param yzpch
     * @return
     */
    public Map getYzJlxx(String yzpch, String qybm) {
        String sql = "SELECT T.JLRQ,T.SYZS,T.ZZPCH,T.SL,T.FZRBH,T.FZR,T.PZTYM,T.PZQC AS SPMC FROM T_YZ_JLXX T WHERE T.YZPCH='" + yzpch + "' AND T.QYBM='" + qybm + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
     * 养殖出栏信息
     *
     * @param refId
     * @return
     */
    public Map getYzClxx(String refId) {
        String sql1 = "SELECT T.QYBM,T.YSCPH,T.DDD,T.YZPCH,T.FZR,T.SZCDJYZH,T.CLRQ AS CCRQ,T.SL,T.ZZL FROM T_YZ_CLXX T WHERE T.ID='" + refId + "'";
        Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql1);
        String yzpch = map.get("YZPCH").toString();
        String qybm = map.get("QYBM").toString();
        String sql2 = "select count(t.id) as cccs, sum(t.zzl) as zl from t_Yz_Clxx t where t.yzpch = '"+yzpch+"' and t.qybm = '" +qybm+ "'";
        Map<String,Object> zlMap = DatabaseHandlerDao.getInstance().queryForMap(sql2);
        map.put("ZL",zlMap.get("ZL"));
        return map;
    }

    /**
     * 获取养殖饲养信息（分页）
     *
     * @param pageRequest
     * @param yzpch
     * @return
     */
    public Page getYzSlxx(PageRequest pageRequest, String yzpch, String qybm) {
        String sql = "SELECT T.ID,T.SYSL,T.WSSJ,T.FZR FROM T_YZ_SYXX T WHERE T.YZPCH='" + yzpch + "' AND T.QYBM='" + qybm + "'";
        return queryPage(pageRequest, sql);
    }


    /**
     * 获取养殖用药信息（分页）
     *
     * @param pageRequest
     * @param yzpch
     * @return
     */
    public Page getYzYyxx(PageRequest pageRequest, String yzpch, String qybm) {
        String sql = "SELECT T.ID,T.SYSY,T.YYSJ,T.FZR FROM T_YZ_YYXX T WHERE T.YZPCH='" + yzpch + "' AND T.QYBM='" + qybm + "'";
        return queryPage(pageRequest, sql);
    }


    /**
     * 获取批菜交易信息
     *
     * @param qybm
     * @return
     */
    public Map getPcQyxx(String qybm) {
        String sql = "SELECT T.QYMC,T.GSZCDJZH,T.BARQ,T.FRDB,T.JYDZ AS DZ,T.LXDH,T.CZ,T.PFSCMC AS MDMC FROM T_PC_QYDA T WHERE T.QYBM = '" + qybm + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }


    /**
     * 获取批菜进场理货信息
     *
     * @param refId 进厂理货关联ID
     * @return
     */
    public Map getPcJclhxx(String refId) {
        /*String sql = "SELECT T.PFSMC,T.CDZMH,T.YSCPH,T.CDMC,T.SCJD,T.JCHWZL,T.LHZZL,T.LHZJE,J.SPMC,J.ZL,J.DJ,J.JE FROM T_PC_JCLHXX T,T_PC_JCLHMXXX J WHERE T.ID = J.PID AND T.JCLHBH = '"
                + jclhbh + "' AND J.JYPZH = '" + jypzh + "'";*/
        String sql = "SELECT T.JCRQ,T.JCLHBH,J.JYPZH,T.PFSMC as JYZMC,T.CDZMH,T.YSCPH,T.CDMC,T.SCJD,T.JCHWZL,T.LHZZL AS ZL,T.LHZJE,J.SPMC,J.DJ,J.JE" +
                " FROM T_PC_JCLHXX T,T_PC_JCLHMXXX J WHERE T.ID = J.PID AND J.ID=?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{refId});
    }


    /**
     * 获取屠宰企业信息
     *
     * @param qybm
     * @return
     */
    public Map getTzQyxx(String qybm) {
        String sql = "SELECT T.QYMC,T.GSZCDJZH,T.BARQ,T.FRDB,T.JYDZ AS DZ,T.LXDH,T.CZ,T.TZCMC as MDMC FROM T_TZ_QYDA T WHERE T.QYBM = '" + qybm + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }


    /**
     * 获取屠宰生猪进场信息
     *
     * @param refId
     * @return
     */
    public Map getTzJcxx(String refId) {
        String sql = "SELECT T.JYZJCSL,T.HZMC as JYZMC,T.SZJCRQ as JCRQ,T.SZCDJYZH,T.CGJ,T.SJJCSL,T.SJJCZL AS ZL,T.TWSL,T.YZCMC,T.YSCPH,T.JYJG,T.CDMC,T.TZCMC" +
                " FROM T_TZ_SZJCXX T WHERE T.ID= ?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{refId});
    }


    /**
     * 获取猪肉批发企业信息
     *
     * @param qybm
     * @return
     */
    public Map getRpQyxx(String qybm) {
        String sql = "SELECT T.QYMC,T.GSZCDJZH,T.BARQ,T.FRDB,T.JYDZ AS DZ,T.LXDH,T.CZ,T.PFSCMC AS MDMC FROM T_PR_QYDA T WHERE T.QYBM='" + qybm + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }


    /**
     * 获取猪肉批发肉品进场信息
     *
     * @param refId
     * @return
     */
    public Map getRpJcxx(String refId) {
        String sql = "SELECT T.PFSMC AS JYZMC,T.SPMC,T.ZL,T.DJ,T.JE,T.CDMC,T.YSCPH,T.JCRQ FROM T_PR_RPJCXX T WHERE T.ID= ?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{refId});
    }


    /**
     * 获取团体采购企业信息
     *
     * @param qybm
     * @return
     */
    public Map getTtQyxx(String qybm) {
        String sql = "SELECT T.QYMC,T.GSZCDJZH,T.BARQ,T.FRDB,T.JYDZ AS DZ,T.LXDH,T.CZ FROM T_TT_QYDA T WHERE T.QYBM='" + qybm + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
     * 获取团体采购蔬菜进场信息
     *
     * @param refId
     * @return
     */
    public Map getTtScJcxx(String refId) {
        String sql = "SELECT T.GYSMC AS JYZMC,T.SPMC,T.ZL,T.DJ,T.JCRQ FROM T_TT_SCJCXX T WHERE T.ID = '" + refId + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
     * 获取团体采购肉品进场信息
     *
     * @param refId
     * @return
     */
    public Map getTtRpJcxx(String refId) {
        String sql = "SELECT T.GYSMC AS JYZMC,T.SPMC,T.ZL,T.DJ,T.JCRQ FROM T_TT_RPJCXX T WHERE T.ID = '" + refId + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
     * 获取团体采购成品进场信息
     *
     * @param refId
     * @return
     */
    public Map getTtCpJcxx(String refId) {
        String sql = "SELECT T.JGCMC AS JYZMC,T.CPMC AS SPMC,T.JCRQ,T.ZLL AS ZL FROM t_tt_cpjcxx T WHERE T.ID = '" + refId + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
     * 获取超市企业档案
     *
     * @param qybm
     * @return
     */
    public Map getCsQyxx(String qybm) {
        String sql = "SELECT T.CSMC as MDMC,T.QYMC,T.GSZCDJZH,T.BARQ,T.FRDB,T.JYDZ AS DZ,T.LXDH,T.CZ FROM T_CS_QYDA T WHERE T.QYBM='" + qybm + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
     * 获取超市蔬菜进场信息
     *
     * @return refId
     */
    public Map getCsScJcxx(String refId) {
        String sql = "SELECT T.ID,T.GYSMC AS JYZMC,T.JCRQ,T.CDPZH,T.CDMC,S.SPMC,S.ZL, S.DJ, S.JE FROM T_CS_SCJCMXXX S,T_CS_SCJCXX T WHERE T.ID = S.PID AND S.ID = '" + refId + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
     * 获取超市肉品进场信息
     *
     * @param refId
     * @return
     */
    public Map getCsRpJcxx(String refId) {
        String sql = "SELECT T.ID,T.JCRQ,T.GYSMC AS JYZMC,T.SPMC,T.ZL,T.DJ FROM T_Cs_RPJCXX T WHERE T.ID='" + refId + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
     * 获取超市成品进场信息
     *
     * @param refId
     * @return
     */
    public Map getCsCpJcxx(String refId) {
        String sql = "SELECT T.ID,T.JCRQ,T.JGCMC AS JYZMC,T.CPMC as SPMC,T.ZZL AS ZZL FROM t_cs_cpjcxx T WHERE T.ID='" + refId + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
     * 获取零售企业信息
     *
     * @param qybm
     * @return
     */
    public Map getLsQyxx(String qybm) {
        String sql = "SELECT T.LSSCMC AS MDMC,T.QYMC,T.GSZCDJZH,T.BARQ,T.FRDB,T.JYDZ AS DZ,T.LXDH,T.CZ FROM T_LS_QYDA T WHERE T.QYBM='" + qybm + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
     * 获取零售蔬菜进场信息
     *
     * @param refId
     * @return
     */
    public Map getLsScJcxx(String refId) {
        String sql = "SELECT T.ID,T.PFSMC AS JYZMC,T.JCRQ,T.CDPZH,T.CDMC,S.SPMC,S.ZL, S.DJ, S.JE FROM T_LS_SCJCMXXX S,T_LS_SCJCXX T WHERE T.ID = S.PID AND S.ID = '" + refId + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
     * 获取零售肉品进场信息
     *
     * @param refId
     * @return
     */
    public Map getLsRpJcxx(String refId) {
        String sql = "SELECT T.ID,T.JCRQ,T.LSSMC AS JYZMC,T.SPMC,T.ZL,T.DJ FROM T_LS_RPJCXX T WHERE T.ID='" + refId + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
     * 获取零售成品进场信息
     *
     * @param refId
     * @return
     */
    public Map getLsCpJcxx(String refId) {
        String sql = "SELECT T.ID,T.JCRQ,T.JGCMC AS JYZMC,T.CPMC as SPMC,T.ZZL AS ZL FROM t_ls_cpjxcc T WHERE T.ID='" + refId + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
     * 获取餐饮企业信息
     *
     * @param qybm
     * @return
     */
    public Map getCyQyxx(String qybm) {
        String sql = "SELECT T.QYMC,T.LXDH,T.JYDZ AS DZ FROM T_CY_QYDA T WHERE T.QYBM='" + qybm + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
     * 获取餐饮蔬菜进场信息
     *
     * @param refId
     * @return
     */
    public Map getCyScJcxx(String refId) {
        String sql = "SELECT T.ID,T.GYSMC AS JYZMC,T.JCRQ,T.SPMC,T.ZL FROM T_CY_SCJCXX T WHERE T.ID = '" + refId + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
     * 获取餐饮肉品进场信息
     *
     * @param refId
     * @return
     */
    public Map getCyRpJcxx(String refId) {
        String sql = "SELECT T.ID,T.JCRQ,T.GYSMC AS JYZMC,T.SPMC,T.ZL FROM T_CY_RPJCXX T WHERE T.ID='" + refId + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
     * 获取餐饮成品进场信息
     *
     * @param refId
     * @return
     */
    public Map getCyCpJcxx(String refId) {
        String sql = "SELECT T.ID,T.JCRQ,T.JGCMC AS JYZMC,T.CPMC as SPMC,T.ZLL AS ZL FROM t_cy_cpjcxx T WHERE T.ID='" + refId + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
    * 获取加工企业信息
    *
    * @param qybm
    * @return
    */
    public Map getJgQyxx(String qybm) {
        String sql = "SELECT T.QYMC,T.LXDH,T.JGCDZ AS DZ,T.JGCMC AS MDMC FROM t_jg_jgcda T WHERE T.QYBM='" + qybm + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
     * 获取餐饮蔬菜进场信息
     *
     * @param refId
     * @return
     */
    public Map getJgYljcxx(String refId) {
        String sql = "SELECT T.ID,T.GYSMC AS JYZMC,T.JCSJ AS JCRQ,T.YLMC AS SPMC,T.ZL FROM t_jg_ylhwxx T WHERE T.ID = '" + refId + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }







    /**
     * 分页查询
     *
     * @param pageRequest
     * @param sql
     * @return Object
     */
    public Page queryPage(PageRequest pageRequest, String sql) {
        if (StringUtil.isBlank(sql)) {
            return null;
        }
        //查总数
        String count = "select count(*) as count from (" + sql + ")";
        Map map = DatabaseHandlerDao.getInstance().queryForMap(count);
        //总数
        long total = Long.parseLong(map.get("COUNT").toString());
        int begin = pageRequest.getOffset();
        int end = begin + pageRequest.getPageSize();
        if (begin > total) {
            int remainder = (int) (total % pageRequest.getPageSize());
            end = (int) total;
            begin = (int) (total - (remainder == 0 ? pageRequest.getPageSize() : remainder));
        }

        List<Map<String, Object>> content = DatabaseHandlerDao.getInstance().pageMaps(sql, begin, end);
        if (content == null) {
            return null;
        } else {
            return new PageImpl<Map<String, Object>>(content, pageRequest, total);
        }
    }
}

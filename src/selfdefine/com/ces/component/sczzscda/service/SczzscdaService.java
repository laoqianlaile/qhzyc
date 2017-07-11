package com.ces.component.sczzscda.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.PlantEntity;
import enterprise.entity.PlantTask;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class SczzscdaService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
    /**
     * 默认权限过滤
     *
     * @return
     */
    public String defaultCode() {
        String code = SerialNumberUtil.getInstance().getCompanyCode();
        String defaultCode = " ";
        if (code != null && !"".equals(code))
            defaultCode = AppDefineUtil.RELATION_AND + " QYBM = '" + code
                    + "' and is_delete <> '1'";
        return defaultCode;
    }

    @Override
    protected String buildCustomerFilter(String tableId,
                                         String componentVersionId, String moduleId, String menuId,
                                         Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
    }

    /**
     * 获取区域编号信息
     */
    public List<Map<String, Object>> getQybhList() {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from t_zz_qyxx t where t.qybm = ? and t.is_delete = '0'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode});
    }

    /**
     * 根据区域编号获取地块信息
     *
     * @param qybh
     */
    public List<Map<String, Object>> getDkxxByQybh() {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from t_sdzyc_zzdkxx t where t.qybm = ? and t.is_delete = '0'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode});
    }

    /**
     * 根据地块编号获取种植单元信息
     *
     * @param dkbh
     */
    public List<Map<String, Object>> getZzdyBydk(String dkbh) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from t_zz_dy t where t.dkbh = ? and t.qybm = ? and t.is_delete = '0' and t.syzt = 'XG'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{dkbh, companyCode});
    }

    /**
     * 修改时根据地块编号获取种植单元信息
     *
     * @param dkbh
     */
    public List<Map<String, Object>> getZzdyBydkForUpdate(String dkbh, String zzdybh) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from t_zz_dy t where t.dkbh = ? and t.qybm = ? and t.is_delete = '0' and (t.syzt = 'XG' or zzdybh = ?)";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{dkbh, companyCode, zzdybh});
    }

    @Transactional
    public Map<String, Object> getFapzCombobox() {
        Map<String, Object> data = new HashMap<String, Object>();
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from t_zz_fapz t where t.qybm = ? and t.is_delete = '0'";
        List<Map<String, Object>> fapzs = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode});
        data.put("fapzs", fapzs);
        return data;
    }
    public Object gettheFormdata(String id){
        Map<String, Object> dataMap=new HashMap<String, Object>();
        String sql="select * from T_ZZ_SCDA where id='"+id+"'";
        dataMap=DatabaseHandlerDao.getInstance().queryForMap(sql);
        return dataMap;
    }

    /**
     * 获取种植方案配置信息
     *
     * @return
     */
    public Map<String, Object> getZzfapzxx() {
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, Object> bzs = new HashMap<String, Object>();
        Map<String, Object> ggs = new HashMap<String, Object>();
        Map<String, Object> sfs = new HashMap<String, Object>();
        Map<String, Object> yys = new HashMap<String, Object>();
        Map<String, Object> jcs = new HashMap<String, Object>();
        Map<String, Object> css = new HashMap<String, Object>();
        Map<String, Object> ccs = new HashMap<String, Object>();
        Map<String, Object> qts = new HashMap<String, Object>();
        Map<String, Object> bzTrps = new HashMap<String, Object>();
        Map<String, Object> ggTrps = new HashMap<String, Object>();
        Map<String, Object> sfTrps = new HashMap<String, Object>();
        Map<String, Object> yyTrps = new HashMap<String, Object>();
        Map<String, Object> jcTrps = new HashMap<String, Object>();
        Map<String, Object> csTrps = new HashMap<String, Object>();
        Map<String, Object> ccTrps = new HashMap<String, Object>();
        Map<String, Object> qtTrps = new HashMap<String, Object>();
        List<Map<String, Object>> shareList ;

        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from t_zz_fapz t where t.qybm = ? and t.is_delete = '0'";
        List<Map<String, Object>> fapzs = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode});
        for (Map<String, Object> fapz : fapzs) {
            String pid = String.valueOf(fapz.get("ID"));
            String fabh = String.valueOf(fapz.get("ZZFABH"));
            //方案配置播种
            String bzSql = "select * from t_zz_bzfa t where t.pid = ?";
            shareList = DatabaseHandlerDao.getInstance().queryForMaps(bzSql, new Object[]{pid});
            bzs.put(fabh, repetitionList(shareList));
            bzTrps.put(fabh, repetitionTrpList(getTrp(bzs, fabh, "t_zz_bzfatrp"), shareList));
            //方案配置灌溉
            String ggSql = "select * from t_zz_gg t where t.pid = ?";
            shareList = DatabaseHandlerDao.getInstance().queryForMaps(ggSql, new Object[]{pid});
            ggs.put(fabh, repetitionList(shareList));
            ggTrps.put(fabh, repetitionTrpList(getTrp(ggs, fabh, "t_zz_ggtrp"), shareList));
            //方案配置施肥
            String sfSql = "select * from t_zz_sf t where t.pid = ?";
            shareList = DatabaseHandlerDao.getInstance().queryForMaps(sfSql, new Object[]{pid});
            sfs.put(fabh, repetitionList(shareList));
            sfTrps.put(fabh, repetitionTrpList(getTrp(sfs, fabh, "t_zz_sftrp"), shareList));
            //方案配置用药
            String yySql = "select * from t_zz_yy t where t.pid = ?";
            shareList = DatabaseHandlerDao.getInstance().queryForMaps(yySql, new Object[]{pid});
            yys.put(fabh, repetitionList(shareList));
            yyTrps.put(fabh, repetitionTrpList(getTrp(yys, fabh, "t_zz_yytrp"), shareList));
            //方案配置检测
//            String jcSql = "select * from t_zz_jc t where t.pid = ?";
//            shareList = DatabaseHandlerDao.getInstance().queryForMaps(jcSql, new Object[]{pid});
//            jcs.put(fabh, shareList);
//            jcTrps.put(fabh, repetitionTrpList(getTrp(jcs, fabh, "t_zz_jctrp"), shareList));
            //方案配置采收
            String csSql = "select * from t_zz_cs t where t.pid = ?";
            shareList = DatabaseHandlerDao.getInstance().queryForMaps(csSql, new Object[]{pid});
            css.put(fabh, repetitionList(shareList));
            csTrps.put(fabh, repetitionTrpList(getTrp(css, fabh, "t_zz_cstrp"), shareList));
            //方案配置锄草
            String ccSql = "select * from t_zz_cc t where t.pid = ?";
            shareList = DatabaseHandlerDao.getInstance().queryForMaps(ccSql, new Object[]{pid});
            ccs.put(fabh, repetitionList(shareList));
            ccTrps.put(fabh, repetitionTrpList(getTrp(ccs, fabh, "t_zz_cctrp"), shareList));
            //方案配置其它
            String qtSql = "select * from t_zz_qt t where t.pid = ?";
            shareList = repetitionList(DatabaseHandlerDao.getInstance().queryForMaps(qtSql, new Object[]{pid}));
            qts.put(fabh, shareList);
            qtTrps.put(fabh, repetitionTrpList(getTrp(qts, fabh, "t_zz_qttrp"),shareList));
        }
        //方案配置
        data.put("fapzs", fapzs);
        data.put("bz", bzs);
        data.put("gg", ggs);
        data.put("sf", sfs);
        data.put("yy", yys);
        data.put("jc", jcs);
        data.put("qt", qts);
        data.put("cc", ccs);
        data.put("cs", css);
        data.put("bzTrp", bzTrps);
        data.put("ggTrp", ggTrps);
        data.put("sfTrp", sfTrps);
        data.put("yyTrp", yyTrps);
        data.put("jcTrp", jcTrps);
        data.put("qtTrp", qtTrps);
        data.put("ccTrp", ccTrps);
        data.put("csTrp", csTrps);
        return data;
    }

    public List<Map<String,Object>> repetitionList(List<Map<String,Object>> nsxList){
        List<Map<String, Object>> newNsxList = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> map : nsxList){
            newNsxList.add(map);
            for(int i = 1; i < Integer.parseInt(String.valueOf(map.get("CFCS"))) ; i++){
                Map<String, Object> shortMap = new HashMap<String, Object>();
                shortMap.putAll(map);
                shortMap.put("NSXJGSJ",Integer.parseInt(String.valueOf(map.get("NSXJGSJ"))) + i * Integer.parseInt(String.valueOf(map.get("CFJGSJ"))));
                shortMap.put("IS_START","0");
                newNsxList.add(shortMap);
            }
        }
        Map nsxs [] = new Map[newNsxList.size()];
        for(int i = 0; i < newNsxList.size(); i++){
            nsxs[i] = newNsxList.get(i);
        }
        for(int i = 1 ; i < nsxs.length ; i++){
            for(int j = i+1 ; j < nsxs.length ; j++){
                if(Integer.parseInt(String.valueOf(nsxs[i].get("NSXJGSJ"))) > Integer.parseInt(String.valueOf(nsxs[j].get("NSXJGSJ")))){
                    Map<String, Object> shortMap = nsxs[i];
                    nsxs[i] = nsxs[j];
                    nsxs[j] = shortMap;
                }
            }
        }
        nsxList.clear();
        for(int i = 0 ; i < nsxs.length; i++) {
            nsxs[i].put("NSZYXBH",i+1);
            nsxList.add(nsxs[i]);
        }
        return nsxList;
    }

    public List<Map<String,Object>> repetitionTrpList(List<Map<String, Object>> trpList, List<Map<String, Object>> nsxList){
        List<Map<String, Object>> newTrpMap = new ArrayList<Map<String, Object>>();
        for (Map<String,Object> trpMap : trpList){
            for(Map<String, Object> nxsMap : nsxList){
                if(String.valueOf(trpMap.get("PID")).equals(String.valueOf(nxsMap.get("ID")))){
                    if(!String.valueOf(trpMap.get("NSZYXBH")).equals(String.valueOf(nxsMap.get("NSZYXBH")))){
                        Map<String, Object> newMap = new HashMap<String, Object>();
                        newMap.putAll(trpMap);
                        newMap.put("NSZYXBH", String.valueOf(nxsMap.get("NSZYXBH")));
                        newTrpMap.add(newMap);
                    }else {
                        newTrpMap.add(trpMap);
                    }
                }
            }
        }
        return newTrpMap;
    }



    public List<Map<String,Object>> getTrp(Map<String, Object> s, String fabh, String tableName) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        StringBuffer sb = new StringBuffer();
        sb.append("select * from " + tableName + " t where t.pid in ('!'");
//		sb.append("SELECT B.*,A.LX FROM (select TYM,min(LX) LX, min(IS_DELETE) IS_DELETE, min(qybm) qybm from T_ZZ_TRPJBXX"
//				+ " where qybm = "
//				+ companyCode
//				+ " group by TYM) A,"
//				+ tableName
//				+ " B WHERE A.TYM = B.TRPTYM AND A.IS_DELETE <> '1' AND B.IS_DELETE <> '1'  AND B.PID IN ('!'");
        for (Map<String, Object> xmap : (List<Map<String, Object>>) s.get(fabh)) {
            sb.append(",'" + String.valueOf(xmap.get("ID")) + "'");
        }
        sb.append(")");
        if (((List<Map<String, Object>>) s.get(fabh)).size() != 0) {
            return DatabaseHandlerDao.getInstance().queryForMaps(sb.toString());
        } else {
            return new ArrayList<Map<String,Object>>();
        }
    }

    /**
     * 获取投入品用药信息
     */
    public List<Map<String, Object>> getTrpflxx(String fllx) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from t_zz_trpjbxx t where t.qybm = ? and t.fllx = ? and t.is_delete = '0'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode, fllx});
    }

    /**
     * 获取投入品用药信息
     */
    public List<Map<String, Object>> getTrpYyxx() {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from t_zz_trpjbxx t where t.qybm = ? and t.lx = 'NY' and t.is_delete = '0'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode});
    }

    /**
     * 保存生产档案信息
     *
     * @param data
     * @return
     */
    @Transactional
    public String saveScdaxx(Map<String, Object> data) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        List<Map<String, Object>> bzxxs = (List<Map<String, Object>>) data.get("bzxx");
        List<Map<String, Object>> ggxxs = (List<Map<String, Object>>) data.get("ggxx");
        List<Map<String, Object>> sfxxs = (List<Map<String, Object>>) data.get("sfxx");
        List<Map<String, Object>> yyxxs = (List<Map<String, Object>>) data.get("yyxx");
        List<Map<String, Object>> csxxs = (List<Map<String, Object>>) data.get("csxx");
        List<Map<String, Object>> ccxxs = (List<Map<String, Object>>) data.get("ccxx");
        List<Map<String, Object>> qtxxs = (List<Map<String, Object>>) data.get("qtxx");
        Map<String, String> scxx = (Map<String, String>) data.get("scxx");
        String id = scxx.get("zzpch");
        if(StringUtil.isEmpty(id)){//是新增操作
            String zzpch = StatisticalCodeUtil.getInstance().getTwentyFivePcm("ZZ","SDZYC", "SDZYCZZPCH");
            scxx.put("ZZPCH",zzpch);
            scxx.put("QYPCH",zzpch.substring(zzpch.length()-11,zzpch.length()));
        }
        String scdabh = scxx.get("dkbh") + String.valueOf((Calendar.getInstance().get(Calendar.YEAR))).substring(2,4) + SerialNumberUtil.getInstance().getSerialNumber("ZZ", "ZZSCDABH", false);
        scxx.put("SCDABH", scdabh);
        scxx.put("qybm", companyCode);
        //起始农事项时间
        String qsnsxsj = scxx.get("qsnsxsj");
        //        scxx.remove("qsnsxsj");
        //获取地块负责人
        Map<String,Object> dkfzrMap = getDkfzr(scxx.get("dkbh").toString());
        String dkfzr = dkfzrMap.get("DKFZR").toString();
        String dkfzrbh = dkfzrMap.get("DKFZRBH").toString();
        //保存生产主表信息
        String masterId = save("T_ZZ_SCDA", scxx, null);
        //修改种植单元的状态为种植状态
        //String qybh = scxx.get("ssqybh");
        String dkbh = scxx.get("dkbh");
        //String zzdybh = scxx.get("zzdybh");
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String updateSql = "update t_zz_dy t set syzt = 'ZZ' where qybm = ? and dkbh = ?";
        DatabaseHandlerDao.getInstance().executeSql(updateSql, new Object[]{qybm,dkbh});

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        //保存生产播种信息
        for (Map<String, Object> bzxx : bzxxs) {
            Map<String, String> bz = (Map<String, String>) bzxx.get("bz");
            List<Map<String, String>> bzTrp = (List<Map<String, String>>) bzxx.get("bzTrp");
            bz.put("pid", masterId);
            bz.put("fzr",dkfzr);
            bz.put("fzrbh",dkfzrbh);
            bz.remove("OP");
            if ("1".equals(bz.get("qsnsx"))) {
                cpmputeQsxygsj(bz, qsnsxsj, bz.get("czfdsj"));
            }
            String bzId = save("T_ZZ_SCBZ", bz, null);
            for (Map<String, String> bzTrpMap : bzTrp) {
                bzTrpMap.put("pid", bzId);
                bzTrpMap.remove("OP");
                save("T_ZZ_SCBZTRP", bzTrpMap, null);
            }
//            list.add(bzxxs);
        }
        //保存灌溉播种信息
        for (Map<String, Object> ggxx : ggxxs) {
            Map<String, String> gg = (Map<String, String>) ggxx.get("gg");
            List<Map<String, String>> ggTrp = (List<Map<String, String>>) ggxx.get("ggTrp");
            gg.put("pid", masterId);
            gg.put("fzr",dkfzr);
            gg.put("fzrbh",dkfzrbh);
            gg.remove("OP");
            if ("1".equals(gg.get("qsnsx"))) {
                cpmputeQsxygsj(gg, qsnsxsj, gg.get("czfdsj"));
            }
            String ggId = save("T_ZZ_SCGG", gg, null);
            for (Map<String, String> ggTrpMap : ggTrp) {
                ggTrpMap.remove("OP");
                ggTrpMap.put("pid", ggId);
                save("T_ZZ_SCGGTRP", ggTrpMap, null);
            }
//            list.add(gg);
        }
        //保存施肥播种信息
        for (Map<String, Object> sfxx : sfxxs) {
            Map<String, String> sf = (Map<String, String>) sfxx.get("sf");
            List<Map<String, String>> sfTrp = (List<Map<String, String>>) sfxx.get("sfTrp");
            sf.put("pid", masterId);
            sf.put("fzr",dkfzr);
            sf.put("fzrbh",dkfzrbh);
            sf.remove("OP");
            if ("1".equals(sf.get("qsnsx"))) {
                cpmputeQsxygsj(sf, qsnsxsj, sf.get("czfdsj"));
            }
            String sfId = save("T_ZZ_SCSF", sf, null);
            for (Map<String, String> sfTrpMap : sfTrp) {
                sfTrpMap.remove("OP");
                sfTrpMap.put("pid", sfId);
                save("T_ZZ_SCSFTRP", sfTrpMap, null);
            }
        }
        //保存生产用药信息
        for (Map<String, Object> yyxx : yyxxs) {
            Map<String, String> yy = (Map<String, String>) yyxx.get("yy");
            List<Map<String, String>> yyTrp = (List<Map<String, String>>) yyxx.get("yyTrp");
            yy.put("pid", masterId);
            yy.put("fzr",dkfzr);
            yy.put("fzrbh",dkfzrbh);
            yy.remove("OP");
            if ("1".equals(yy.get("qsnsx"))) {
                cpmputeQsxygsj(yy, qsnsxsj, yy.get("czfdsj"));
            }
            String yyId = save("T_ZZ_SCYY", yy, null);
            for (Map<String, String> yyTrpMap : yyTrp) {
                yyTrpMap.remove("OP");
                yyTrpMap.put("pid", yyId);
                save("T_ZZ_SCYYTRP", yyTrpMap, null);
            }
        }
//		//保存生产检测信息
//		for (Map<String, Object> jcxx : jcxxs) {
//			Map<String, String> jc = (Map<String, String>)jcxx.get("jc");
//			List<Map<String, String>> jcTrp = (List<Map<String, String>>)jcxx.get("jcTrp");
//			jc.put("pid", masterId);
//			jc.remove("op");
//			if ("1".equals(jc.get("qsnsx"))) {
//				cpmputeQsxygsj(jc, qsnsxsj, jc.get("czfdsj"));
//			}
//			String jcId = save("T_ZZ_SCJC", jc, null);
//			for (Map<String, String> jcTrpMap : jcTrp) {
//				jcTrpMap.remove("op");
//				jcTrpMap.put("pid", jcId);
//				save("T_ZZ_SCJCTRP", jcTrpMap, null);
//			}
//		}
        //保存生产采收信息
        for (Map<String, Object> csxx : csxxs) {
            Map<String, String> cs = (Map<String, String>) csxx.get("cs");
            List<Map<String, String>> csTrp = (List<Map<String, String>>) csxx.get("csTrp");
            cs.put("pid", masterId);
//            cs.put("fzr",dkfzr);
//            cs.put("fzrbh",dkfzrbh);
            cs.remove("OP");
            if ("1".equals(cs.get("qsnsx"))) {
                cpmputeQsxygsj(cs, qsnsxsj, cs.get("czfdsj"));
            }
            String csId = save("T_ZZ_SCCS", cs, null);
            for (Map<String, String> csTrpMap : csTrp) {
                csTrpMap.remove("OP");
                csTrpMap.put("pid", csId);
                save("T_ZZ_SCCSTRP", csTrpMap, null);
            }
        }
        //保存生产锄草信息
        for (Map<String, Object> ccxx : ccxxs) {
            Map<String, String> cc = (Map<String, String>) ccxx.get("cc");
            List<Map<String, String>> ccTrp = (List<Map<String, String>>) ccxx.get("ccTrp");
            cc.put("pid", masterId);
            cc.put("fzr",dkfzr);
            cc.put("fzrbh",dkfzrbh);
            cc.remove("OP");
            if ("1".equals(cc.get("qsnsx"))) {
                cpmputeQsxygsj(cc, qsnsxsj, cc.get("czfdsj"));
            }
            String ccId = save("T_ZZ_SCCC", cc, null);
            for (Map<String, String> ccTrpMap : ccTrp) {
                ccTrpMap.remove("OP");
                ccTrpMap.put("pid", ccId);
                save("T_ZZ_SCCCTRP", ccTrpMap, null);
            }
        }
        //保存生产其它信息
        for (Map<String, Object> qtxx : qtxxs) {
            Map<String, String> qt = (Map<String, String>) qtxx.get("qt");
            List<Map<String, String>> qtTrp = (List<Map<String, String>>) qtxx.get("qtTrp");
            qt.put("pid", masterId);
            qt.put("fzr",dkfzr);
            qt.put("fzrbh",dkfzrbh);
            qt.remove("OP");
            if ("1".equals(qt.get("qsnsx"))) {
                cpmputeQsxygsj(qt, qsnsxsj, qt.get("czfdsj"));
            }
            String qtId = save("T_ZZ_SCQT", qt, null);
            for (Map<String, String> qtTrpMap : qtTrp) {
                qtTrpMap.remove("OP");
                qtTrpMap.put("pid", qtId);
                save("T_ZZ_SCQTTRP", qtTrpMap, null);
            }
        }

        return masterId;
    }

    /**
     * 根据地块编号获取地块负责人信息
     * @param dkbh
     * @return
     */
    private Map<String, Object> getDkfzr(String dkbh) {
        String sql = "select t.dkfzr,t.dkfzrbh from T_SDZYC_ZZDKXX t where t.dkbh = ? and qybm=?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new Object[]{dkbh,SerialNumberUtil.getInstance().getCompanyCode()});
    }

    /**
     * 计算起始农事项的预估时间
     *
     * @param data
     * @param qsnsxsj
     * @param fdsj
     */
    private void cpmputeQsxygsj(Map<String, String> data, String qsnsxsj, String fdsj) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(qsnsxsj);
            String ygsj1 = format.format(new Date(date.getTime() - Long.parseLong(fdsj) * 24 * 60 * 60 * 1000));
            String ygsj2 = format.format(new Date(date.getTime() + Long.parseLong(fdsj) * 24 * 60 * 60 * 1000));
            String budgettime = format.format(new Date(date.getTime()));
            data.put("ygsj1", ygsj1);
            data.put("ygsj2", ygsj2);
            data.put("budgettime", budgettime);

            //堆算农事项时间
            StringBuilder sql;
            SimpleDateFormat sdf ;
            String tables[] = {"T_ZZ_SCBZ", "T_ZZ_SCGG", "T_ZZ_SCSF", "T_ZZ_SCYY", "T_ZZ_SCCS", "T_ZZ_SCCC", "T_ZZ_SCQT"};
            for (int m = 0; m < tables.length; m++) {
                sql = new StringBuilder("SELECT * FROM " + tables[m] + " T WHERE T.PID = '" + String.valueOf(data.get("pid")) + "' AND T.IS_DELETE <> '1' AND YGSJ1 IS NULL AND YGSJ2 IS NULL");
                List<Map<String, Object>> farmingList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
                for (Map<String, Object> map : farmingList) {
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String operationTime = sdf.format(date);
                    Date czsjDate = sdf.parse(operationTime);
                    String ygsj11 = sdf.format(new Date(czsjDate.getTime() + Long.parseLong(String.valueOf(map.get("NSXJGSJ"))) * 24 * 60 * 60 * 1000 - Long.parseLong(String.valueOf(map.get("CZFDSJ"))) * 24 * 60 * 60 * 1000));
                    String ygsj22 = sdf.format(new Date(czsjDate.getTime() + Long.parseLong(String.valueOf(map.get("NSXJGSJ"))) * 24 * 60 * 60 * 1000 + Long.parseLong(String.valueOf(map.get("CZFDSJ"))) * 24 * 60 * 60 * 1000));
                    String budgettime1 = sdf.format(new Date(czsjDate.getTime() + Long.parseLong(String.valueOf(map.get("NSXJGSJ"))) * 24 * 60 * 60 * 1000));
                    sql = new StringBuilder("UPDATE " + tables[m] + " T SET YGSJ1 = '" + ygsj11 + "',YGSJ2 = '" + ygsj22 + "',BUDGETTIME = '" + budgettime1 + "' WHERE ID = '" + String.valueOf(map.get("ID")) + "'");
                    DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * 同步灌溉信息到农事记录
     * @param scxx    生产信息
     * @param ggxx    灌溉信息
     *//*
    public void syncGgxx(Map<String,String> scxx,Map<String,String> ggxx){
		Map<String, String> map = new HashMap<String, String>();
		map.put("dymc",scxx.get("zzdymc"));
		map.put("dybh",scxx.get("zzdybh"));
		map.put("qybh",scxx.get("ssqybh"));
		map.put("dkbh",scxx.get("dkbh"));
		map.put("qybm",scxx.get("qybm"));
		map.put("ggfs",ggxx.get("ggfs"));
		map.put("sylx",ggxx.get("sylx"));
		map.put("sydj",ggxx.get("sydj"));
		map.put("czlx","gg");
		save("T_ZZ_NSJL", map, null);
	}

	*//**
     * 同步施肥信息到农事记录
     * @param scxx    生产信息
     * @param sfxx    施肥信息
     *//*
	public void syncSfxx(Map<String,String>scxx,Map<String,String>sfxx){
		Map<String, String> map = new HashMap<String, String>();
		map.put("dymc",scxx.get("zzdymc"));
		map.put("dybh",scxx.get("zzdybh"));
		map.put("qybh",scxx.get("ssqybh"));
		map.put("dkbh",scxx.get("dkbh"));
		map.put("qybm",scxx.get("qybm"));
		map.put("sfjd",sfxx.get("sfsd"));
		map.put("flmc",sfxx.get("flmc"));
		map.put("czlx", "sf");
		save("T_ZZ_NSJL", map, null);
	}

	*//**
     * 同步用药信息到农事记录
     * @param scxx
     * @param yyxx
     *//*
	public void syncYyxx(Map<String,String>scxx,Map<String,String>yyxx){
		Map<String, String> map = new HashMap<String, String>();
		map.put("dymc",scxx.get("zzdymc"));
		map.put("dybh",scxx.get("zzdybh"));
		map.put("qybh",scxx.get("ssqybh"));
		map.put("dkbh",scxx.get("dkbh"));
		map.put("qybm",scxx.get("qybm"));
		map.put("ywmc",yyxx.get("yymc"));
		map.put("czlx", "yy");
		save("T_ZZ_NSJL", map, null);
	}*/

    /**
     * 获取生产档案信息
     *
     * @param rowId
     */
    public Map<String, Object> getScdaxxById(String rowId) {
        Map<String, Object> data = new HashMap<String, Object>();
        List<Map<String, Object>> newList;
        List<Map<String, Object>> shareList;
        Map<String, Object> shareMap;
        //生产档案
        String sql = "select * from t_zz_scda t where t.id = ?";
        data.put("scda", DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{rowId}));
        //生产播种
        newList = new ArrayList<Map<String, Object>>();
        shareList = new ArrayList<Map<String, Object>>();
        shareList = getGridData("t_zz_scbz", rowId);
        data.put("scbz", shareList);
        //生产播种投入品
        if (shareList.size() != 0) {
            for (Map<String, Object> xMap : shareList) {
                String qsnsxsj = getQsnsxsj(xMap);
                if (!"".equals(qsnsxsj)) {
                    data.put("qsnsxsj", qsnsxsj);
                }
                newList.addAll(getGridData("t_zz_scbztrp", String.valueOf(xMap.get("ID"))));
            }
        }
        data.put("scbztrp", newList);

        //生产灌溉
        newList = new ArrayList<Map<String, Object>>();
        shareList = new ArrayList<Map<String, Object>>();
        shareList = getGridData("t_zz_scgg", rowId);
        data.put("scgg", shareList);
        //生产灌溉投入品
        if (shareList.size() != 0) {
            for (Map<String, Object> xMap : shareList) {
                String qsnsxsj = getQsnsxsj(xMap);
                if (!"".equals(qsnsxsj)) {
                    data.put("qsnsxsj", qsnsxsj);
                }
                newList.addAll(getGridData("t_zz_scggtrp", String.valueOf(xMap.get("ID"))));
            }
        }
        data.put("scggtrp", newList);

        //生产施肥
        newList = new ArrayList<Map<String, Object>>();
        shareList = new ArrayList<Map<String, Object>>();
        shareList = getGridData("t_zz_scsf", rowId);
        data.put("scsf", shareList);
        //生产施肥投入品
        if (shareList.size() != 0) {
            for (Map<String, Object> xMap : shareList) {
                String qsnsxsj = getQsnsxsj(xMap);
                if (!"".equals(qsnsxsj)) {
                    data.put("qsnsxsj", qsnsxsj);
                }
                newList.addAll(getGridData("t_zz_scsftrp", String.valueOf(xMap.get("ID"))));
            }
        }
        data.put("scsftrp", newList);

        //生产用药
        newList = new ArrayList<Map<String, Object>>();
        shareList = new ArrayList<Map<String, Object>>();
        shareList = getGridData("t_zz_scyy", rowId);
        data.put("scyy", shareList);
        //生产用药投入品
        if (shareList.size() != 0) {
            for (Map<String, Object> xMap : shareList) {
                String qsnsxsj = getQsnsxsj(xMap);
                if (!"".equals(qsnsxsj)) {
                    data.put("qsnsxsj", qsnsxsj);
                }
                newList.addAll(getGridData("t_zz_scyytrp", String.valueOf(xMap.get("ID"))));
            }
        }
        data.put("scyytrp", newList);

        //生产检测
        newList = new ArrayList<Map<String, Object>>();
        shareList = new ArrayList<Map<String, Object>>();
        shareList = getGridData("t_zz_scjc", rowId);
        data.put("scjc", shareList);
        //生产检测投入品
        if (shareList.size() != 0) {
            for (Map<String, Object> xMap : shareList) {
                String qsnsxsj = getQsnsxsj(xMap);
                if (!"".equals(qsnsxsj)) {
                    data.put("qsnsxsj", qsnsxsj);
                }
                newList.addAll(getGridData("t_zz_scjctrp", String.valueOf(xMap.get("ID"))));
            }
        }
        data.put("scjctrp", newList);

        //生产采收
        newList = new ArrayList<Map<String, Object>>();
        shareList = new ArrayList<Map<String, Object>>();
        shareList = getGridData("t_zz_sccs", rowId);
        shareMap = new HashMap<String, Object>();
        shareMap.put("data", shareList);
        data.put("sccs", shareList);
        //生产采收投入品
        if (shareList.size() != 0) {
            for (Map<String, Object> xMap : shareList) {
                String qsnsxsj = getQsnsxsj(xMap);
                if (!"".equals(qsnsxsj)) {
                    data.put("qsnsxsj", qsnsxsj);
                }
                newList.addAll(getGridData("t_zz_sccstrp", String.valueOf(xMap.get("ID"))));
            }
        }
        data.put("sccstrp", newList);

        //生产锄草
        newList = new ArrayList<Map<String, Object>>();
        shareList = new ArrayList<Map<String, Object>>();
        shareList = getGridData("t_zz_sccc", rowId);
        data.put("sccc", shareList);
        //生产锄草投入品
        if (shareList.size() != 0) {
            for (Map<String, Object> xMap : shareList) {
                String qsnsxsj = getQsnsxsj(xMap);
                if (!"".equals(qsnsxsj)) {
                    data.put("qsnsxsj", qsnsxsj);
                }
                newList.addAll(getGridData("t_zz_sccctrp", String.valueOf(xMap.get("ID"))));
            }
        }
        data.put("sccctrp", newList);

        //生产其它
        newList = new ArrayList<Map<String, Object>>();
        shareList = new ArrayList<Map<String, Object>>();
        shareList = getGridData("t_zz_scqt", rowId);
        data.put("scqt", shareList);
        //生产其它投入品
        if (shareList.size() != 0) {
            for (Map<String, Object> xMap : shareList) {
                String qsnsxsj = getQsnsxsj(xMap);
                if (!"".equals(qsnsxsj)) {
                    data.put("qsnsxsj", qsnsxsj);
                }
                newList.addAll(getGridData("t_zz_scqttrp", String.valueOf(xMap.get("ID"))));
            }
        }
        data.put("scqttrp", newList);
        //生产检测记录
        sql = "select * from t_zz_scjcjl t where pid = '" + rowId + "' and is_delete <> '1'";
        data.put("scjcjl", DatabaseHandlerDao.getInstance().queryForMaps(sql));
        return data;
    }

    /**
     * 根据预估时间算农事项起始时间
     *
     * @param xMap
     * @return
     */
    public String getQsnsxsj(Map<String, Object> xMap) {
        String qsnsxsj = "";
        try {
            if ("1".equals(xMap.get("QSNSX"))) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                if (!"".equals(String.valueOf(xMap.get("YGSJ1")))) {
                    Date date = null;
                    date = format.parse(String.valueOf(xMap.get("YGSJ1")));
                    qsnsxsj = format.format(new Date(date.getTime() + Long.parseLong(String.valueOf(xMap.get("CZFDSJ"))) * 24 * 60 * 60 * 1000));

                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return qsnsxsj;
    }

    /**
     * 修改时查询子表数据
     *
     * @param tablename 子（孙子）表
     * @param id        父表ID
     * @return
     */
    public List<Map<String, Object>> getGridData(String tablename, String id) {
        String sql = "select * from " + tablename + " t where t.pid = ? and is_delete <> '1' order by to_number(t.nszyxbh) asc";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{id});
    }


    /**
     * 更新生产档案信息
     *
     * @param data
     * @return
     */
    @Transactional
    public String updateScdaxx(Map<String, Object> data) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        List<Map<String, Object>> bzxxs = (List<Map<String, Object>>) data.get("bzxx");
        List<Map<String, Object>> ggxxs = (List<Map<String, Object>>) data.get("ggxx");
        List<Map<String, Object>> sfxxs = (List<Map<String, Object>>) data.get("sfxx");
        List<Map<String, Object>> yyxxs = (List<Map<String, Object>>) data.get("yyxx");
        List<Map<String, Object>> csxxs = (List<Map<String, Object>>) data.get("csxx");
        List<Map<String, Object>> ccxxs = (List<Map<String, Object>>) data.get("ccxx");
        List<Map<String, Object>> qtxxs = (List<Map<String, Object>>) data.get("qtxx");
        List<Map<String, String>> jcjl = (List<Map<String, String>>) data.get("jcjl");
        Map<String, String> scxx = (Map<String, String>) data.get("scxx");

        String masterId = String.valueOf(scxx.get("ID"));
        String qsnsxsj = String.valueOf(scxx.get("qsnsxsj"));
        scxx.put("QYBM", companyCode);
        scxx.remove("qsnsxsj");
        save("T_ZZ_SCDA", scxx, null);
        List<String> ids;//存储孙子表ID
        List<String> sids;//存储子表ID
        StringBuffer sb;
        //查询地块负责人
        String sql = "select * from t_zz_dkxx t where t.qybm = '" + companyCode + "' and t.dkbh = '" + scxx.get("dkbh") + "'";
        Map<String, Object>  landOperator = DatabaseHandlerDao.getInstance().queryForMap(sql.toString());

        //保存生产播种信息
        sids = new ArrayList<String>();
        for (Map<String, Object> bzxx : bzxxs) {
            Map<String, String> bz = (Map<String, String>) bzxx.get("bz");
            List<Map<String, String>> bzTrp = (List<Map<String, String>>) bzxx.get("bzTrp");
            bz.put("pid", masterId);
            bz.remove("OP");
            if ("1".equals(bz.get("QSNSX"))) {
                cpmputeQsxygsj(bz, qsnsxsj, bz.get("CZFDSJ"));
            }
            if("null".equals(String.valueOf(bz.get("id")))){
                bz.put("fzrbh",String.valueOf(landOperator.get("DKFZRBH")));
                bz.put("fzr",String.valueOf(landOperator.get("DKFZR")));
            }
            String bzId = save("T_ZZ_SCBZ", bz, null);
            ids = new ArrayList<String>();
            for (Map<String, String> bzTrpMap : bzTrp) {
                bzTrpMap.put("pid", bzId);
                bzTrpMap.remove("OP");
                String bztrpId = save("T_ZZ_SCBZTRP", bzTrpMap, null);
                ids.add(bztrpId);
            }
            /****修改时删除孙子表数据****/
            sb = new StringBuffer();
            sb.append("update t_zz_scbztrp t set is_delete = '1' where pid = ? and id not in ('!'");
            for (String id : ids) {
                sb.append(",'" + id + "'");
            }
            sb.append(")");
            DatabaseHandlerDao.getInstance().executeSql(sb.toString(), new Object[]{bzId});
            /****修改时删除子表数据 end****/
            sids.add(bzId);
        }
        /****修改时删除子表数据****/
        sb = new StringBuffer();
        sb.append("update t_zz_scbz t set is_delete = '1' where pid = ? and id not in ('!'");
        for (String id : sids) {
            sb.append(",'" + id + "'");
        }
        sb.append(")");
        DatabaseHandlerDao.getInstance().executeSql(sb.toString(), new Object[]{masterId});
        /****修改时删除子表数据 end****/
        //保存灌溉信息
        sids = new ArrayList<String>();
        for (Map<String, Object> ggxx : ggxxs) {
            Map<String, String> gg = (Map<String, String>) ggxx.get("gg");
            List<Map<String, String>> ggTrp = (List<Map<String, String>>) ggxx.get("ggTrp");
            gg.put("pid", masterId);
            gg.remove("OP");
            if ("1".equals(gg.get("QSNSX"))) {
                cpmputeQsxygsj(gg, qsnsxsj, gg.get("CZFDSJ"));
            }
            if("null".equals(String.valueOf(gg.get("id")))){
                gg.put("fzrbh",String.valueOf(landOperator.get("DKFZRBH")));
                gg.put("fzr",String.valueOf(landOperator.get("DKFZR")));
            }
            String ggId = save("T_ZZ_SCGG", gg, null);
            ids = new ArrayList<String>();
            for (Map<String, String> ggTrpMap : ggTrp) {
                ggTrpMap.remove("OP");
                ggTrpMap.put("pid", ggId);
                ids.add(save("T_ZZ_SCGGTRP", ggTrpMap, null));
            }
            /****修改时删除孙子表数据****/
            sb = new StringBuffer();
            sb.append("update t_zz_scggtrp t set is_delete = '1' where pid = ? and id not in ('!'");
            for (String id : ids) {
                sb.append(",'" + id + "'");
            }
            sb.append(")");
            DatabaseHandlerDao.getInstance().executeSql(sb.toString(), new Object[]{ggId});
            /****修改时删除子表数据 end****/
            sids.add(ggId);
        }
        /****修改时删除子表数据****/
        sb = new StringBuffer();
        sb.append("update t_zz_scgg t set is_delete = '1' where pid = ? and id not in ('!'");
        for (String id : sids) {
            sb.append(",'" + id + "'");
        }
        sb.append(")");
        DatabaseHandlerDao.getInstance().executeSql(sb.toString(), new Object[]{masterId});
        /****修改时删除子表数据 end****/
        //保存施肥播种信息
        sids = new ArrayList<String>();
        for (Map<String, Object> sfxx : sfxxs) {
            Map<String, String> sf = (Map<String, String>) sfxx.get("sf");
            List<Map<String, String>> sfTrp = (List<Map<String, String>>) sfxx.get("sfTrp");
            sf.put("pid", masterId);
            sf.remove("OP");
            if ("1".equals(sf.get("QSNSX"))) {
                cpmputeQsxygsj(sf, qsnsxsj, sf.get("CZFDSJ"));
            }
            if("null".equals(String.valueOf(sf.get("id")))){
                sf.put("fzrbh",String.valueOf(landOperator.get("DKFZRBH")));
                sf.put("fzr",String.valueOf(landOperator.get("DKFZR")));
            }
            String sfId = save("T_ZZ_SCSF", sf, null);
            ids = new ArrayList<String>();
            for (Map<String, String> sfTrpMap : sfTrp) {
                sfTrpMap.remove("OP");
                sfTrpMap.put("pid", sfId);
                ids.add(save("T_ZZ_SCSFTRP", sfTrpMap, null));
            }
            /****修改时删除孙子表数据****/
            sb = new StringBuffer();
            sb.append("update t_zz_scsftrp t set is_delete = '1' where pid = ? and id not in ('!'");
            for (String id : ids) {
                sb.append(",'" + id + "'");
            }
            sb.append(")");
            DatabaseHandlerDao.getInstance().executeSql(sb.toString(), new Object[]{sfId});
            /****修改时删除子表数据 end****/
            sids.add(sfId);
        }
        /****修改时删除子表数据****/
        sb = new StringBuffer();
        sb.append("update t_zz_scsf t set is_delete = '1' where pid = ? and id not in ('!'");
        for (String id : sids) {
            sb.append(",'" + id + "'");
        }
        sb.append(")");
        DatabaseHandlerDao.getInstance().executeSql(sb.toString(), new Object[]{masterId});
        /****修改时删除子表数据 end****/
        //保存生产用药信息
        sids = new ArrayList<String>();
        for (Map<String, Object> yyxx : yyxxs) {
            Map<String, String> yy = (Map<String, String>) yyxx.get("yy");
            List<Map<String, String>> yyTrp = (List<Map<String, String>>) yyxx.get("yyTrp");
            yy.put("pid", masterId);
            yy.remove("OP");
            if ("1".equals(yy.get("QSNSX"))) {
                cpmputeQsxygsj(yy, qsnsxsj, yy.get("CZFDSJ"));
            }
            if("null".equals(String.valueOf(yy.get("id")))){
                yy.put("fzrbh",String.valueOf(landOperator.get("DKFZRBH")));
                yy.put("fzr",String.valueOf(landOperator.get("DKFZR")));
            }
            String yyId = save("T_ZZ_SCYY", yy, null);
            ids = new ArrayList<String>();
            for (Map<String, String> yyTrpMap : yyTrp) {
                yyTrpMap.remove("OP");
                yyTrpMap.put("pid", yyId);
                ids.add(save("T_ZZ_SCYYTRP", yyTrpMap, null));
            }
            /****修改时删除孙子表数据****/
            sb = new StringBuffer();
            sb.append("update t_zz_scyytrp t set is_delete = '1' where pid = ? and id not in ('!'");
            for (String id : ids) {
                sb.append(",'" + id + "'");
            }
            sb.append(")");
            DatabaseHandlerDao.getInstance().executeSql(sb.toString(), new Object[]{yyId});
            /****修改时删除子表数据 end****/
            sids.add(yyId);
        }
        /****修改时删除子表数据****/
        sb = new StringBuffer();
        sb.append("update t_zz_scyy t set is_delete = '1' where pid = ? and id not in ('!'");
        for (String id : sids) {
            sb.append(",'" + id + "'");
        }
        sb.append(")");
        DatabaseHandlerDao.getInstance().executeSql(sb.toString(), new Object[]{masterId});
        /****修改时删除子表数据 end****/
        //保存生产采收信息
        sids = new ArrayList<String>();
        for (Map<String, Object> csxx : csxxs) {
            Map<String, String> cs = (Map<String, String>) csxx.get("cs");
            List<Map<String, String>> csTrp = (List<Map<String, String>>) csxx.get("csTrp");
            cs.put("pid", masterId);
            cs.remove("OP");
            if ("1".equals(cs.get("QSNSX"))) {
                cpmputeQsxygsj(cs, qsnsxsj, cs.get("CZFDSJ"));
            }
            if("null".equals(String.valueOf(cs.get("id")))){
                cs.put("fzrbh",String.valueOf(landOperator.get("DKFZRBH")));
                cs.put("fzr",String.valueOf(landOperator.get("DKFZR")));
            }
            String csId = save("T_ZZ_SCCS", cs, null);
            ids = new ArrayList<String>();
            for (Map<String, String> csTrpMap : csTrp) {
                csTrpMap.remove("OP");
                csTrpMap.put("pid", csId);
                ids.add(save("T_ZZ_SCCSTRP", csTrpMap, null));
            }
            /****修改时删除孙子表数据****/
            sb = new StringBuffer();
            sb.append("update t_zz_sccstrp t set is_delete = '1' where pid = ? and id not in ('!'");
            for (String id : ids) {
                sb.append(",'" + id + "'");
            }
            sb.append(")");
            DatabaseHandlerDao.getInstance().executeSql(sb.toString(), new Object[]{csId});
            /****修改时删除子表数据 end****/
            sids.add(csId);
        }
        /****修改时删除子表数据****/
        sb = new StringBuffer();
        sb.append("update t_zz_sccs t set is_delete = '1' where pid = ? and id not in ('!'");
        for (String id : sids) {
            sb.append(",'" + id + "'");
        }
        sb.append(")");
        DatabaseHandlerDao.getInstance().executeSql(sb.toString(), new Object[]{masterId});
        /****修改时删除子表数据 end****/
        //保存生产锄草信息
        sids = new ArrayList<String>();
        for (Map<String, Object> ccxx : ccxxs) {
            Map<String, String> cc = (Map<String, String>) ccxx.get("cc");
            List<Map<String, String>> ccTrp = (List<Map<String, String>>) ccxx.get("ccTrp");
            cc.put("pid", masterId);
            cc.remove("OP");
            if ("1".equals(cc.get("QSNSX"))) {
                cpmputeQsxygsj(cc, qsnsxsj, cc.get("CZFDSJ"));
            }
            if("null".equals(String.valueOf(cc.get("id")))){
                cc.put("fzrbh",String.valueOf(landOperator.get("DKFZRBH")));
                cc.put("fzr",String.valueOf(landOperator.get("DKFZR")));
            }
            String ccId = save("T_ZZ_SCCC", cc, null);
            ids = new ArrayList<String>();
            for (Map<String, String> ccTrpMap : ccTrp) {
                ccTrpMap.remove("OP");
                ccTrpMap.put("pid", ccId);
                ids.add(save("T_ZZ_SCCCTRP", ccTrpMap, null));
            }
            /****修改时删除孙子表数据****/
            sb = new StringBuffer();
            sb.append("update t_zz_sccctrp t set is_delete = '1' where pid = ? and id not in ('!'");
            for (String id : ids) {
                sb.append(",'" + id + "'");
            }
            sb.append(")");
            DatabaseHandlerDao.getInstance().executeSql(sb.toString(), new Object[]{ccId});
            /****修改时删除子表数据 end****/
            sids.add(ccId);
        }
        /****修改时删除子表数据****/
        sb = new StringBuffer();
        sb.append("update t_zz_sccc t set is_delete = '1' where pid = ? and id not in ('!'");
        for (String id : sids) {
            sb.append(",'" + id + "'");
        }
        sb.append(")");
        DatabaseHandlerDao.getInstance().executeSql(sb.toString(), new Object[]{masterId});
        /****修改时删除子表数据 end****/
        //保存生产其它信息
        sids = new ArrayList<String>();
        for (Map<String, Object> qtxx : qtxxs) {
            Map<String, String> qt = (Map<String, String>) qtxx.get("qt");
            List<Map<String, String>> qtTrp = (List<Map<String, String>>) qtxx.get("qtTrp");
            qt.put("pid", masterId);
            qt.remove("OP");
            if ("1".equals(qt.get("QSNSX"))) {
                cpmputeQsxygsj(qt, qsnsxsj, qt.get("CZFDSJ"));
            }
            if("null".equals(String.valueOf(qt.get("id")))){
                qt.put("fzrbh",String.valueOf(landOperator.get("DKFZRBH")));
                qt.put("fzr",String.valueOf(landOperator.get("DKFZR")));
            }
            String qtId = save("T_ZZ_SCQT", qt, null);
            ids = new ArrayList<String>();
            for (Map<String, String> qtTrpMap : qtTrp) {
                qtTrpMap.remove("OP");
                qtTrpMap.put("pid", qtId);
                ids.add(save("T_ZZ_SCQTTRP", qtTrpMap, null));
            }
            /****修改时删除孙子表数据****/
            sb = new StringBuffer();
            sb.append("update t_zz_scqttrp t set is_delete = '1' where pid = ? and id not in ('!'");
            for (String id : ids) {
                sb.append(",'" + id + "'");
            }
            sb.append(")");
            DatabaseHandlerDao.getInstance().executeSql(sb.toString(), new Object[]{qtId});
            /****修改时删除子表数据 end****/
            sids.add(qtId);
        }
        /****修改时删除子表数据****/
        sb = new StringBuffer();
        sb.append("update t_zz_scqt t set is_delete = '1' where pid = ? and id not in ('!'");
        for (String id : sids) {
            sb.append(",'" + id + "'");
        }
        sb.append(")");
        DatabaseHandlerDao.getInstance().executeSql(sb.toString(), new Object[]{masterId});
        /****修改时删除孙子表数据 end****/
        String tablenames[] = {"t_zz_scbz", "t_zz_scgg", "t_zz_scsf", "t_zz_scyy", "t_zz_scjc", "t_zz_sccs", "t_zz_sccc", "t_zz_scqt"};
        for (int i = 0; i < tablenames.length; i++) {
            String deleteSql = "select * from " + tablenames[i] + " t where t.pid = ? and is_delete = '1'";
            List<Map<String, Object>> delIds = DatabaseHandlerDao.getInstance().queryForMaps(deleteSql, new Object[]{masterId});
            for (Map<String, Object> xMap : delIds) {
                deleteSql = "update " + tablenames[i] + "trp t set is_delete = '1' where pid = ?";
                DatabaseHandlerDao.getInstance().executeSql(deleteSql, new Object[]{String.valueOf(xMap.get("ID"))});
            }
        }
        countYgsj(masterId);
        /******保存检测记录*******/
//        ids = new ArrayList<String>();
//        sb = new StringBuffer();
//        for (Map<String, String> xmap : jcjl) {
//            xmap.remove("OP");
//            ids.add(saveOne("t_zz_scjcjl", xmap));
//        }
//        sb.append("update t_zz_scjcjl t set t.is_delete = '1' where pid = '" + masterId + "' and id not in ('!'");
//        for (String id : ids) {
//            sb.append(",'" + id + "'");
//        }
//        sb.append(")");
//        DatabaseHandlerDao.getInstance().executeSql(sb.toString());
        return masterId;
    }

    @Transactional
    public Object countYgsj(String pid) {
        //若是开始操作起始农事项：堆算农事项时间
        //堆算农事项时间
        String qsnsxczsj = "";
        String sql = "";
        String tables[] = {"t_zz_scbz", "t_zz_scgg", "t_zz_scsf", "t_zz_scyy", "t_zz_scjc", "t_zz_sccs", "t_zz_sccc", "t_zz_scqt"};
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int m = 0; m < tables.length; m++) {
            sql = "select czsj from " + tables[m] + " t where t.pid = '" + pid + "' and t.is_delete <> '1' and t.qsnsx = '1' and t.czsj is not null";
            Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
            if (map.size() != 0) {
                qsnsxczsj = map.get("CZSJ").toString();
            }
        }
        String czsj = qsnsxczsj;
        if (!"".equals(String.valueOf(czsj))) {
            for (int m = 0; m < tables.length; m++) {
                sql = "select * from " + tables[m] + " t where t.pid = '" + pid + "' and t.is_delete <> '1' and czsj is null";
                List<Map<String, Object>> nsxList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
                for (Map<String, Object> map : nsxList) {
                    if (!"".equals(String.valueOf(map.get("NSXJGSJ"))) && !"".equals(String.valueOf(map.get("CZFDSJ"))) && "null".equals(String.valueOf(map.get("CZSJ")))) {
                        try {
                            Date czsjDate = sdf.parse(czsj.split(" ")[0]);
                            String ygsj1 = sdf.format(new Date(czsjDate.getTime() + Long.parseLong(String.valueOf(map.get("NSXJGSJ"))) * 24 * 60 * 60 * 1000 - Long.parseLong(String.valueOf(map.get("CZFDSJ"))) * 24 * 60 * 60 * 1000));
                            String ygsj2 = sdf.format(new Date(czsjDate.getTime() + Long.parseLong(String.valueOf(map.get("NSXJGSJ"))) * 24 * 60 * 60 * 1000 + Long.parseLong(String.valueOf(map.get("CZFDSJ"))) * 24 * 60 * 60 * 1000));
                            String budgettime = sdf.format(new Date(czsjDate.getTime() + Long.parseLong(String.valueOf(map.get("NSXJGSJ"))) * 24 * 60 * 60 * 1000));
                            sql = "update " + tables[m] + " t set ygsj1 = '" + ygsj1 + "',ygsj2 = '" + ygsj2 + "',budgettime = '" + budgettime + "' where id = '" + String.valueOf(map.get("ID")) + "'";
                            DatabaseHandlerDao.getInstance().executeSql(sql);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }


    /**
     * 获取所有的投入品信息
     *
     * @return
     */
    public List<Map<String, Object>> getAllTrpxx() {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from t_zz_scda t where t.qybm = ? and t.is_delete = '0'";
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode});
        for (Map<String, Object> map : dataList) {
            map.put("TEXT", map.get("TRPMC"));
            map.put("VALUE", map.get("TRPBH"));
        }
        return dataList;
    }

    /**
     * 根据通用名获取投入品信息
     *
     * @param tym
     * @return
     */
    public Object getTrpxxByTym(String lx, String tym) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String tableName = "";
        String bzmc = "";
        String tymxx = "";
        if(lx.equals("FL")){
            tableName = "t_sdzyc_flxx";
            bzmc = "flbzmc";
            tymxx = "fltym";
        }else if(lx.equals("ZZ")||lx.equals("zhongmiao")){
            tableName = "t_sdzyc_zzzm";
            bzmc = "zzzmmc";
            tymxx = "zzzmbh";
        }else if(lx.equals("NY")){
            tableName = "t_sdzyc_nyxx";
            bzmc = "nybzmc";
            tymxx = "nytym";
        }else if(lx.equals("NJJ")){
            tableName = "t_sdzyc_njjxx";
            bzmc = "njjmc";
            tymxx = "njjbh";
        }
        String sql = "select distinct t."+bzmc+" as value, t."+bzmc+" as text from "+tableName+" t where t.qybm = ? and t.is_delete = '0'"+"and "+tymxx+"=?";
        if(lx.equals("ZZ")||lx.equals("zhongmiao")||lx.equals("NJJ")){
            sql = "select distinct t."+bzmc+" as value, t."+bzmc+" as text from "+tableName+" t where t.qybm = ? and t.is_delete = '0'"+"and "+tymxx+"=?";
        }
        List<Map<String, Object>> dataMap = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode, tym});
        return dataMap;
    }
    /**
     * 根据投入品通用名编号获取投入品信息
     *
     * @param lx,tymbh
     * @return
     */
    public Object getTrpxxByTymbh(String lx, String tymbh) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String tableName = "";
        String bzmc = "";
        String tym = "";
        if(lx.equals("FL")){
            tableName = "t_sdzyc_flxx";
            bzmc = "flbzmc";
            tym = "flbh";
        }else if(lx.equals("ZZ")||lx.equals("zhongmiao")){
            tableName = "t_sdzyc_zzzm";
            bzmc = "zzzmmc";
            tym = "zzzmbh";
        }else if(lx.equals("NY")){
            tableName = "t_sdzyc_nyxx";
            bzmc = "nybzmc";
            tym = "nybh";
        }
        String sql = "select distinct t."+bzmc+" as value, t."+bzmc+" as text from "+tableName+" t where t.qybm = ? and t.is_delete = '0'"+"and "+tym+"=?";
        List<Map<String, Object>> dataMap = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode, tymbh});
        return dataMap;
    }
    /**
     * 根据投入品信息获取用途
     *
     * @param lx,trpmc
     * @return
     */
    public Object getYtByTrpmc(String lx, String trpmc) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String tableName = "";
        String tym = "";
        if(lx.equals("FL")){
            tableName = "t_sdzyc_flxx";
            tym = "flbzmc";
        }else if(lx.equals("ZZ")||lx.equals("zhongmiao")){
            tableName = "t_sdzyc_zzzm";
            tym = "zzzmmc";
        }else if(lx.equals("NY")){
            tableName = "t_sdzyc_nyxx";
            tym = "nybzmc";
        }else if(lx.equals("NJJ")){
            tableName = "t_sdzyc_njjxx";
            tym = "njjmc";
        }
        String sql = "select distinct t.yt as text from "+tableName+" t where t.qybm = ? and t.is_delete = '0'"+"and "+tym+"=?";
        Map<String, Object> dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{companyCode, trpmc});
        return dataMap;
    }
    public Object getTrpByTym(String tym) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from t_zz_trpjbxx t where t.qybm = ? and t.is_delete = '0' and t.tym = ?";
        List<Map<String, Object>> dataMap = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode, tym});
        return dataMap;
    }

    /**
     * 根据类型获取投入品信息
     *
     * @param lx
     * @return
     */
    public Object getTrpxxByTrplx(String lx) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String tableName = null;
        String bzmc = null;
        if(lx.equals("FL")){
            tableName = "t_sdzyc_flxx";
            bzmc = "flbzmc";
        }else if(lx.equals("ZZ")){
            tableName = "t_sdzyc_zzzm";
            bzmc = "zzzmmc";
        }else if(lx.equals("NY")){
            tableName = "t_sdzyc_nyxx";
            bzmc = "nybzmc";
        }
        String sql = "select distinct t."+bzmc+" as value, t."+bzmc+" as text from "+tableName+" t where t.qybm = ? and t.is_delete = '0'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode});
    }
    /**
     * 获取地块信息
     * @return
     */
    public Object getDkxx() {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select DKMC,DKBH,DKFZRBH,DKFZR,JDBH,JDMC from T_SDZYC_ZZDKXX t where t.qybm = ? and is_delete = '0'";
        List<Map<String, Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode});
        return maps;

    }
    public Object getFzrdk(String dkbh) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select DKMC,DKBH,JDBH,JDMC,DKFZRBH,DKFZR,MJ from T_SDZYC_ZZDKXX t where t.qybm = ? and t.dkbh=? and is_delete='0'";
        Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{companyCode, dkbh});
        return map;
    }
    public Object getJddk(String jdbh) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select DKMC,DKBH from T_SDZYC_ZZDKXX t where t.qybm = ?"+"and jdbh=?";
        List<Map<String, Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode, jdbh});
        return maps;
    }
    /**
     * 获取种子种苗信息
     * @return
     */
    public Object getZzzm(String zzfabh) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select ZZZMBH,ZZZMMC,YCMC,YCDM from T_ZZ_FAPZ t where t.qybm = ?"+"and t.zzfabh=?";
        Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{companyCode, zzfabh});
        return map;
    }
    /**
     * 获取种子来源
     * @return
     */
    public Object getZzly(String codetype) {
        String sql = "select value,name from t_xtpz_code t where t.code_type_code = ?";
        Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{ codetype});
        return map;
    }
    /**
     * 获取药材名称
     * @return
     */
    public Object getYcmc(String ycmc) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select YCMNAME from T_SDZYC_ZYCSPBM t where t.qybm = ?"+"and t.zsspm=?";
        Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{companyCode, ycmc});
        return map;
    }
    /**
     * 获取种植负责人信息
     * @return
     */
    public Object getZzfzr() {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select t.xm,t.gzrybh from t_zz_gzryda t where t.is_delete = '0' and t.qybm = ? and t.qyzt='1' and dwlx='ZZQY'";
        List<Map<String, Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode});
        return maps;
    }

    /**
     * 获取种植负责人
     * @param zzfzr
     * @return
     */
    public Object getZzfzrxx(String zzfzr) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select t.xm,t.gzrybh from t_zz_gzryda t where t.is_delete = '0' and t.qybm = ? and t.qyzt='1' and dwlx='ZZQY'and xm='"+zzfzr+"'";
        List<Map<String, Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode});
        return maps;
    }
    /**
     * 获取基地信息
     * @return
     */
    public Object getJdxx() {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select jdbh,jdmc from t_SDZYC_jdxx t where t.is_delete <> '1' and t.qybm = ?";
        List<Map<String, Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode});
        return maps;
    }

    /**
     * 根据投入品类型获取投入品的通用名
     *
     * @param lx
     * @return
     */
    public List<Map<String, Object>> getTrptymByLx(String lx) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String tableName = "";
        String tym = "";
        String tymbh = "";
        if(lx.equals("FL")){
            tableName = "t_sdzyc_flxx";
            tym = "fltym";
//            tymbh = "flbh";
        }else if(lx.equals("ZZ")||lx.equals("zhongmiao")){
            tableName = "t_sdzyc_zzzm";
            tym = "zzzmmc";
            tymbh = "zzzmbh";
        }else if(lx.equals("NY")){
            tableName = "t_sdzyc_nyxx";
            tym = "nytym";
//            tymbh = "nybh";
        }else if(lx.equals("NJJ")){
            tableName = "t_sdzyc_njjxx";
            tym = "njjmc";
            tymbh = "njjbh";
        }
        String sql = "select distinct t."+tym+" as value, t."+tym+" as text from "+tableName+" t where t.qybm = ? and t.is_delete = '0'";
        if(lx.equals("ZZ")||lx.equals("zhongmiao")||lx.equals("NJJ")){
            sql = "select distinct t."+tymbh+" as value, t."+tym+" as text from "+tableName+" t where t.qybm = ? and t.is_delete = '0'";
        }
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode});
    }
    /**
     * 获取数据字典编码数据
     *
     * @param lxbm
     * @return
     */
    public List<Map<String, Object>> getDataDictionary(String lxbm) {
        String sql = "select sjbm as value,sjmc as text from t_common_SJLX_CODE  where lxbm = ? order by sxjb";
        List<Map<String, Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{lxbm});
        List<Map<String,Object>> dataList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : maps) {
            String value = map.get("VALUE")+"";
            if( !"yimiao".equals(value) && !"siliao".equals(value)){
                dataList.add(map);
            }
        }
        return dataList;
    }
    /**
     * 根据通用名获取种子种苗投入品类型
     *
     * @param lx
     * @return
     */
    public Map<String, Object> getLxByTym(String bh) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select t.lx from t_sdzyc_zzzm t where t.qybm = ? and t.is_delete = '0' and zzzmbh=?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{companyCode,bh});
    }
    /**
     * 根据通用名获取农机具投入品类型
     *
     * @param lx
     * @return
     */
    public Map<String, Object> getNjjLxByTym(String bh) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select t.lx from t_sdzyc_njjxx t where t.qybm = ? and t.is_delete = '0' and njjbh=?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{companyCode,bh});
    }
    /**
     * 根据通用名获取农药投入品类型
     *
     * @param lx
     * @return
     */
    public List<Map<String, Object>> getNyLxByTym(String tym) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select distinct t.lx from t_sdzyc_nyxx t where t.qybm = ? and t.is_delete = '0' and nytym=?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode,tym});
    }
    /**
     * 根据通用名获取肥料投入品类型
     *
     * @param lx
     * @return
     */
    public List<Map<String, Object>> getFlLxByTym(String tym) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select distinct t.lx from t_sdzyc_flxx t where t.qybm = ? and t.is_delete = '0' and fltym=?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode,tym});
    }

    /**
     * 获取种子种苗通用名
     *
     * @return
     */
    public Object getAllTrptym() {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select distinct t.zzzmbh as value, t.zzzmmc as text from t_sdzyc_zzzm t where t.qybm = ? and t.is_delete = '0'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode});
    }
    /**
     * 获取农药通用名
     *
     * @return
     */
    public Object getNyTym() {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select distinct t.nytym as text, t.nytym as value from t_sdzyc_nyxx t where t.qybm = ? and t.is_delete = '0'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode});
    }
    /**
     * 获取肥料通用名
     *
     * @return
     */
    public Object getFlTym() {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select distinct t.fltym as value, t.fltym as text from t_sdzyc_flxx t where t.qybm = ? and t.is_delete = '0'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode});
    }
    /**
     * 获取肥料通用名
     *
     * @return
     */
    public Object getGgTym() {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select distinct t.njjbh as value, t.njjmc as text from t_sdzyc_njjxx t where t.qybm = ? and t.is_delete = '0'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode});
    }
    /**
     * 获取农药标准名称
     *
     * @return
     */
    public Object getNyTrpmc() {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select distinct t.nybzmc as value, t.nybzmc as text from t_sdzyc_nyxx t where t.qybm = ? and t.is_delete = '0'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode});
    }
    /**
     * 获取肥料标准名称
     *
     * @return
     */
    public Object getFlTrpmc() {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select distinct t.flbzmc as value, t.flbzmc as text from t_sdzyc_flxx t where t.qybm = ? and t.is_delete = '0'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode});
    }

    public Object getCkxq(String tableName, String id) {
        String sql = "select b.czr,b.kssj,b.jssj from " + tableName + " a ,t_zz_czjl b where a.id = b.pid and a.id = '" + id + "' order by b.kssj asc";
//		String sql = "select b.czr,b.kssj from " + tableName + " a ,t_zz_czjl b order by b.kssj asc";
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data", dataList);
        return dataMap;
    }

    @Override
    @Transactional
    public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
        String id[] = ids.split(",");
        for (int i = 0; i < id.length; i++) {
            publicDelete("t_zz_scbz", id[i]);
            publicDelete("t_zz_scgg", id[i]);
            publicDelete("t_zz_scsf", id[i]);
            publicDelete("t_zz_scyy", id[i]);
            publicDelete("t_zz_scjc", id[i]);
            publicDelete("t_zz_sccs", id[i]);
            publicDelete("t_zz_sccc", id[i]);
            publicDelete("t_zz_scqt", id[i]);
            String sql = "select * from t_zz_scda t where t.id = '" + id[i] + "'";
            Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
            if (!"".equals(String.valueOf(map.get("ZZDYBH")))) {
                sql = "update t_zz_dy t set syzt = 'XG' where zzdybh = '" + String.valueOf(map.get("ZZDYBH")) + "' and qybm = '" + SerialNumberUtil.getInstance().getCompanyCode() + "'";
                DatabaseHandlerDao.getInstance().executeSql(sql);
            }
        }
        super.delete(tableId, dTableIds, ids, isLogicalDelete, paramMap);
    }

    @Transactional
    public void publicDelete(String tableName, String id) {
        StringBuffer sb = new StringBuffer();
        sb.append("update " + tableName + " set is_delete = '1' where pid = '" + id + "'");
        DatabaseHandlerDao.getInstance().executeSql(sb.toString());
        sb = new StringBuffer();
        sb.append("select * from " + tableName + " t where t.pid = '" + id + "' and is_delete = '1'");
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sb.toString());
        for (Map<String, Object> xMap : dataList) {
            sb = new StringBuffer();
            sb.append("update " + tableName + "trp t set is_delete = '1' where pid = '" + String.valueOf(xMap.get("ID")) + "'");
            DatabaseHandlerDao.getInstance().executeSql(sb.toString());
        }
    }


    /**
     * 获取负责人
     *
     * @return
     */
    public Object getFzr() {
        String sql = "select t.xm as text,t.gzrybh as value from t_zz_gzryda t where t.is_delete <> '1' and t.qybm = '" + SerialNumberUtil.getInstance().getCompanyCode() + "'";
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        for (Map<String, Object> map : dataList) {
            map.put("value", map.get("VALUE"));
            map.put("text", map.get("TEXT"));
        }
        return dataList;
    }


    /**
     * 查询销售去向
     *
     * @return
     */
    public Object getXsqx(String id) {
        String sql = "select * from t_zz_scjcjl t where pid = '" + id + "'";
        List<Map<String, Object>> cslshList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        StringBuffer sb = new StringBuffer();
        sb.append("select h.id,subStr(g.cspch,0,16) as cslsh,h.khmc,h.mdmc,h.xsddh,h.zl as cczl,h.ccsj from t_zz_bzgl f,t_zz_bzglplxx g,(select y.id,y.cspch,y.khmc,y.mdmc,t.xsddh,x.zl,t.ccsj from t_zz_ccgl t,t_zz_ccbzcpxx x,(select c.id as id,c.cspch,a.khmc,b.mdmc,c.is_delete from t_zz_khxx a,t_zz_khmdxx b,t_zz_pcjdxx c where a.id = b.pid and c.jrmd = b.id and a.is_delete <> '1' and b.is_delete <> '1' and c.is_delete <> '1' and c.cspch in (select d.cpzsm from t_zz_bzgl d,t_zz_bzglplxx e where d.id = e.pid and e.is_delete <> '1' and d.is_delete <> '1' and (e.cspch like '!%'");
        for (Map<String, Object> xmap : cslshList) {
            sb.append(" or e.cspch like '" + String.valueOf(xmap.get("CSLSH")) + "%'");
        }
        sb.append("))) y where y.cspch = x.cpzsm and t.id = x.pid and x.is_delete <> '1' and y.is_delete <> '1') h where f.is_delete <> '1' and g.is_delete <> '1' and f.id = g.pid and f.cpzsm = h.cspch group by h.id,subStr(g.cspch,0,16),h.cspch,h.khmc,h.mdmc,h.xsddh,h.zl,h.ccsj");
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sb.toString());
        sb = new StringBuffer();
        sb.append("select x.id,subStr(x.cspch,0,16) as cslsh,z.khmc,y.mdmc,x.xsddh,x.cczl,x.ccsj from (select b.id,a.pch as cspch,c.xsddh,a.cczl,c.ccsj,b.jrmd from t_zz_ccgl c,t_zz_ccshxx a,t_zz_pcjdxx b where a.is_delete <> '1' and b.is_delete <> '1' and c.is_delete <> '1' and c.id = a.pid and a.cpzsm = b.cspch and (a.pch like '!%'");
        for (Map<String, Object> xmap : cslshList) {
            sb.append(" or a.pch like '" + String.valueOf(xmap.get("CSLSH")) + "%'");
        }
        sb.append(")) x ,t_zz_khmdxx y,t_zz_khxx z where y.is_delete <> '1' and z.is_delete <> '1' and x.jrmd = y.id and y.pid = z.id group by x.id,subStr(x.cspch,0,16),z.khmc,y.mdmc,x.xsddh,x.cczl,x.ccsj");
        dataList.addAll(DatabaseHandlerDao.getInstance().queryForMaps(sb.toString()));
        return dataList;
    }

    /**
     * 修改时能否修改生产档案
     *
     * @param id
     * @return
     */
    public Object canUpdateScda(String id) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result", "SUCCESS");
        int ct = 0;
        String sql = "select count(*) as ct from T_ZZ_SCBZ t where t.czsj is not null and t.is_delete <> '1' and t.pid = ?";
        ct += Integer.parseInt(DatabaseHandlerDao.getInstance().queryForObject(sql, new Object[]{id}).toString());
        sql = "select count(*) as ct from T_ZZ_SCGG t where t.czsj is not null and t.is_delete <> '1' and t.pid = ?";
        ct += Integer.parseInt(DatabaseHandlerDao.getInstance().queryForObject(sql, new Object[]{id}).toString());
        sql = "select count(*) as ct from T_ZZ_SCSF t where t.czsj is not null and t.is_delete <> '1' and t.pid = ?";
        ct += Integer.parseInt(DatabaseHandlerDao.getInstance().queryForObject(sql, new Object[]{id}).toString());
        sql = "select count(*) as ct from T_ZZ_SCYY t where t.czsj is not null and t.is_delete <> '1' and t.pid = ?";
        ct += Integer.parseInt(DatabaseHandlerDao.getInstance().queryForObject(sql, new Object[]{id}).toString());
        sql = "select count(*) as ct from T_ZZ_SCCS t where t.czsj is not null and t.is_delete <> '1' and t.pid = ?";
        ct += Integer.parseInt(DatabaseHandlerDao.getInstance().queryForObject(sql, new Object[]{id}).toString());
        sql = "select count(*) as ct from T_ZZ_SCCC t where t.czsj is not null and t.is_delete <> '1' and t.pid = ?";
        ct += Integer.parseInt(DatabaseHandlerDao.getInstance().queryForObject(sql, new Object[]{id}).toString());
        sql = "select count(*) as ct from T_ZZ_SCQT t where t.czsj is not null and t.is_delete <> '1' and t.pid = ?";
        ct += Integer.parseInt(DatabaseHandlerDao.getInstance().queryForObject(sql, new Object[]{id}).toString());

        if (0!=ct) {
            resultMap.put("result", "ERROR");
        }
        return resultMap;
    }


    public Object preView(String id, String kind) {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        StringBuilder sql = new StringBuilder("select *" +
                "  from (select m.*," +
                "               max(c.czr) as czr," +
                "               max(c.kssj) as kssj," +
                "               max(c.jssj) as jssj," +
                "               max(c.pj) as xj" +
                "          from (select a.id as id," +
                "                       b.id as pid," +
                "                       a.scdabh," +
                "                       b.nszyxmc," +
                "                       b.ygsj1," +
                "                       b.ygsj2," +
                "                       b.czsj," +
                "                       'bz' as lx," +
                "                       b.qsnsx," +
                "                       b.nsxjgsj,b.czfdsj" +
                "                  from t_zz_scda a, t_zz_scbz b" +
                "                 where a.id = b.pid" +
                "                   and a.is_delete <> '1'" +
                "                   and b.is_delete <> '1') m" +
                "          left join t_zz_czjl c" +
                "            on m.pid = c.pid" +
                "           and c.is_delete <> '1'" +
                "           and c.jssj is not null" +
                "         where m.id = ?" +
                "         group by m.id," +
                "                  m.pid," +
                "                  m.scdabh," +
                "                  m.nszyxmc," +
                "                  m.ygsj1," +
                "                  m.ygsj2," +
                "                  m.czsj," +
                "                  'bz'," +
                "                  m.qsnsx," +
                "                  m.nsxjgsj," +
                "				   m.czfdsj" +
                "        union" +
                /**************************************灌溉************************************/
                "        select m.*," +
                "               max(c.czr) as czr," +
                "               max(c.kssj) as kssj," +
                "               max(c.jssj) as jssj," +
                "               max(c.pj) as xj" +
                "          from (select a.id as id," +
                "                       b.id as pid," +
                "                       a.scdabh," +
                "                       b.nszyxmc," +
                "                       b.ygsj1," +
                "                       b.ygsj2," +
                "                       b.czsj," +
                "                       'gg' as lx," +
                "                       b.qsnsx," +
                "                       b.nsxjgsj,b.czfdsj" +
                "                  from t_zz_scda a, t_zz_scgg b" +
                "                 where a.id = b.pid" +
                "                   and a.is_delete <> '1'" +
                "                   and b.is_delete <> '1') m" +
                "          left join t_zz_czjl c" +
                "            on m.pid = c.pid" +
                "           and c.is_delete <> '1'" +
                "           and c.jssj is not null" +
                "         where m.id = ?" +
                "         group by m.id," +
                "                  m.pid," +
                "                  m.scdabh," +
                "                  m.nszyxmc," +
                "                  m.ygsj1," +
                "                  m.ygsj2," +
                "                  m.czsj," +
                "                  'gg'," +
                "                  m.qsnsx," +
                "                  m.nsxjgsj," +
                "				   m.czfdsj" +
                "        union" +
                /*************************************************施肥*************************************/
                "        select m.*," +
                "               max(c.czr) as czr," +
                "               max(c.kssj) as kssj," +
                "               max(c.jssj) as jssj," +
                "               max(c.pj) as xj" +
                "          from (select a.id as id," +
                "                       b.id as pid," +
                "                       a.scdabh," +
                "                       b.nszyxmc," +
                "                       b.ygsj1," +
                "                       b.ygsj2," +
                "                       b.czsj," +
                "                       'sf' as lx," +
                "                       b.qsnsx," +
                "                       b.nsxjgsj,b.czfdsj" +
                "                  from t_zz_scda a, t_zz_scsf b" +
                "                 where a.id = b.pid" +
                "                   and a.is_delete <> '1'" +
                "                   and b.is_delete <> '1') m" +
                "          left join t_zz_czjl c" +
                "            on m.pid = c.pid" +
                "           and c.is_delete <> '1'" +
                "           and c.jssj is not null" +
                "         where m.id = ?" +
                "         group by m.id," +
                "                  m.pid," +
                "                  m.scdabh," +
                "                  m.nszyxmc," +
                "                  m.ygsj1," +
                "                  m.ygsj2," +
                "                  m.czsj," +
                "                  'sf'," +
                "                  m.qsnsx," +
                "                  m.nsxjgsj," +
                "				   m.czfdsj" +
                "        union" +
                /*****************************************用药*******************************/
                "        select m.*," +
                "               max(c.czr) as czr," +
                "               max(c.kssj) as kssj," +
                "               max(c.jssj) as jssj," +
                "               max(c.pj) as xj" +
                "          from (select a.id as id," +
                "                       b.id as pid," +
                "                       a.scdabh," +
                "                       b.nszyxmc," +
                "                       b.ygsj1," +
                "                       b.ygsj2," +
                "                       b.czsj," +
                "                       'yy' as lx," +
                "                       b.qsnsx," +
                "                       b.nsxjgsj,b.czfdsj" +
                "                  from t_zz_scda a, t_zz_scyy b" +
                "                 where a.id = b.pid" +
                "                   and a.is_delete <> '1'" +
                "                   and b.is_delete <> '1') m" +
                "          left join t_zz_czjl c" +
                "            on m.pid = c.pid" +
                "           and c.is_delete <> '1'" +
                "           and c.jssj is not null" +
                "         where m.id = ?" +
                "         group by m.id," +
                "                  m.pid," +
                "                  m.scdabh," +
                "                  m.nszyxmc," +
                "                  m.ygsj1," +
                "                  m.ygsj2," +
                "                  m.czsj," +
                "                  'yy'," +
                "                  m.qsnsx," +
                "                  m.nsxjgsj," +
                "				   m.czfdsj" +
                "        union" +
                /*****************************锄草******************************************/
                "        select m.*," +
                "               max(c.czr) as czr," +
                "               max(c.kssj) as kssj," +
                "               max(c.jssj) as jssj," +
                "               max(c.pj) as xj" +
                "          from (select a.id as id," +
                "                       b.id as pid," +
                "                       a.scdabh," +
                "                       b.nszyxmc," +
                "                       b.ygsj1," +
                "                       b.ygsj2," +
                "                       b.czsj," +
                "                       'cc' as lx," +
                "                       b.qsnsx," +
                "                       b.nsxjgsj,b.czfdsj" +
                "                  from t_zz_scda a, t_zz_sccc b" +
                "                 where a.id = b.pid" +
                "                   and a.is_delete <> '1'" +
                "                   and b.is_delete <> '1') m" +
                "          left join t_zz_czjl c" +
                "            on m.pid = c.pid" +
                "           and c.is_delete <> '1'" +
                "           and c.jssj is not null" +
                "         where m.id = ?" +
                "         group by m.id," +
                "                  m.pid," +
                "                  m.scdabh," +
                "                  m.nszyxmc," +
                "                  m.ygsj1," +
                "                  m.ygsj2," +
                "                  m.czsj," +
                "                  'cc'," +
                "                  m.qsnsx," +
                "                  m.nsxjgsj," +
                "				   m.czfdsj" +
                "        union" +
                /********************************采收************************************/
                "        select m.*," +
                "               max(c.czr) as czr," +
                "               max(c.kssj) as kssj," +
                "               max(c.jssj) as jssj," +
                "               max(c.pj) as xj" +
                "          from (select a.id as id," +
                "                       b.id as pid," +
                "                       a.scdabh," +
                "                       b.nszyxmc," +
                "                       b.ygsj1," +
                "                       b.ygsj2," +
                "                       b.czsj," +
                "                       'cs' as lx," +
                "                       b.qsnsx," +
                "                       b.nsxjgsj,b.czfdsj" +
                "                  from t_zz_scda a, t_zz_sccs b" +
                "                 where a.id = b.pid" +
                "                   and a.is_delete <> '1'" +
                "                   and b.is_delete <> '1') m" +
                "          left join t_zz_czjl c" +
                "            on m.pid = c.pid" +
                "           and c.is_delete <> '1'" +
                "           and c.jssj is not null" +
                "         where m.id = ?" +
                "         group by m.id," +
                "                  m.pid," +
                "                  m.scdabh," +
                "                  m.nszyxmc," +
                "                  m.ygsj1," +
                "                  m.ygsj2," +
                "                  m.czsj," +
                "                  'cs'," +
                "                  m.qsnsx," +
                "                  m.nsxjgsj," +
                "				   m.czfdsj" +
                "        union" +
                /***************************其他**********************************/
                "        select m.*," +
                "               max(c.czr) as czr," +
                "               max(c.kssj) as kssj," +
                "               max(c.jssj) as jssj," +
                "               max(c.pj) as xj" +
                "          from (select a.id as id," +
                "                       b.id as pid," +
                "                       a.scdabh," +
                "                       b.nszyxmc," +
                "                       b.ygsj1," +
                "                       b.ygsj2," +
                "                       b.czsj," +
                "                       'qt' as lx," +
                "                       b.qsnsx," +
                "                       b.nsxjgsj,b.czfdsj" +
                "                  from t_zz_scda a, t_zz_scqt b" +
                "                 where a.id = b.pid" +
                "                   and a.is_delete <> '1'" +
                "                   and b.is_delete <> '1') m" +
                "          left join t_zz_czjl c" +
                "            on m.pid = c.pid" +
                "           and c.is_delete <> '1'" +
                "           and c.jssj is not null" +
                "         where m.id = ?" +
                "         group by m.id," +
                "                  m.pid," +
                "                  m.scdabh," +
                "                  m.nszyxmc," +
                "                  m.ygsj1," +
                "                  m.ygsj2," +
                "                  m.czsj," +
                "                  'qt'," +
                "                  m.qsnsx," +
                "                  m.nsxjgsj," +
                "	               m.czfdsj) x ");
        String beginTimesql = sql.toString();
        if (!"".equals(kind) && !"null".equals(kind) && !"all".equals(kind)) {
            sql.append(" where x.lx = '" + kind + "'");
        }
        beginTimesql += " order by x.qsnsx desc, x.kssj asc, x.nsxjgsj asc";
        sql.append(" order by x.qsnsx desc, x.kssj asc, x.nsxjgsj asc");
        List<Map<String, Object>> farmingList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), new Object[]{id, id, id, id, id, id, id});
        List<Map<String, Object>> beginTimeList = DatabaseHandlerDao.getInstance().queryForMaps(beginTimesql, new Object[]{id, id, id, id, id, id, id});
        sql = new StringBuilder("select t.dkfzr as principal from t_zz_dkxx t where t.is_delete <> '1' and t.dkbh = (select a.dkbh from t_zz_scda a where a.id = ?)");
        Map<String,Object> principalMap = DatabaseHandlerDao.getInstance().queryForMap(sql.toString(), new Object[]{id});
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String beginFarmingTime = sdf.format(new Date((sdf.parse(String.valueOf(beginTimeList.get(0).get("YGSJ1")))).getTime() + Long.parseLong(String.valueOf(farmingList.get(0).get("CZFDSJ"))) * 24 * 60 * 60 * 1000));
            for (int i = 0; i < farmingList.size(); i++) {
                if ("".equals(String.valueOf(farmingList.get(i).get("JSSJ"))) || "null".equals(String.valueOf(farmingList.get(i).get("JSSJ")))) {
                    String operationTime = sdf.format(new Date((sdf.parse(beginFarmingTime)).getTime() + Long.parseLong(String.valueOf(farmingList.get(i).get("NSXJGSJ"))) * 24 * 60 * 60 * 1000));
                    farmingList.get(i).put("OPERATIONTIME", operationTime);
                }
                if ("".equals(String.valueOf(farmingList.get(i).get("CZR"))) || "null".equals(String.valueOf(farmingList.get(i).get("CZR")))) {
                    farmingList.get(i).put("CZR",principalMap.get("PRINCIPAL"));
                }
            }
            if (farmingList.size() != 0)
                farmingList.get(farmingList.size() - 1).put("IS_END", "1");
            return farmingList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return farmingList;
    }
    public Map<String,Object> queryPch(String ids){
        String sql =  "select jyjg,zt from t_zz_scda where id in ('" + ids.replace(",", "','") + "')";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }
}
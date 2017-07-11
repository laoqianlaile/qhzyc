package com.ces.component.farm.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.UUIDGenerator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Synge on 2015/10/19.
 */
@Component
public class FarmScaleService {

    /**
     * 根据地块编号获取生产档案
     * @param ickh
     * @return
     */
    public List<Map<String,Object>> queryScdaByDkbh (String dkbh) {
        String dkexsitSql = "select id from t_zz_dkxx where dkbh = ?";
        List<Map<String,Object>> dkexsitList = DatabaseHandlerDao.getInstance().queryForMaps(dkexsitSql,new Object[]{dkbh});
        if (dkexsitList.size() == 0) {
            return null;
        }
        String sql = "select t.qybm,\n" +
                "       t.scdabh,\n" +
                "       t.zzdybh,\n" +
                "       t.zzdymc,\n" +
                "       t.pl,\n" +
                "       t.pz,\n" +
                "       t.plbh,\n" +
                "       t.pzbh,\n" +
                "       t.dkbh,\n" +
                "       t.dkmc,\n" +
                "       t.ssqymc,\n" +
                "       t.ssqybh\n" +
                "  from T_ZZ_SCDA t \n" +
                " where t.dkbh = ? and t.zt = '1'\n";
        List<Map<String,Object>> mapList = DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{dkbh});
        return mapList;
    }

    /**
     * 获取采收流水号
     * @return
     */
    public Map<String,String> getCslsh (String qybm) {
        String cslsh = SerialNumberUtil.getInstance().getSerialNumber("ZZ",qybm, "ZZCSLSH", true);
        Map<String,String> map = new HashMap<String, String>();
        map.put("CSLSH",cslsh);
        return map;
    }

    /**
     * 保存采收信息
     * @param qybm 企业编码
     * @param cslsh 采收流水号
     * @param qymc 区域名称
     * @param qybh 区域编号
     * @param dkbh 地块编号
     * @param dkmc 地块名称
     * @param zzdybh 种植单元编号
     * @param zzdymc 种植单元名称
     * @param scdabh 生产档案编号
     * @param pl 品类
     * @param plbh 品类编号
     * @param pz 品种
     * @param pzbh 品种编号
     * @param cssj 采收时间
     * @param zldj 质量等级
     * @param dyzs 打印张书
     * @param zl 重量
     * @return
     */
    public String saveCsxx(String qybm, String cslsh, String qymc, String qybh, String dkbh, String dkmc, String zzdybh, String zzdymc, String scdabh, String pl, String plbh, String pz, String pzbh, String cssj, String zldj, String dyzs, String zl, String pch) {
        String masterIdSql = "select id from T_ZZ_CSGL where cslsh = ?";
        Map<String,Object> masterIdMap = DatabaseHandlerDao.getInstance().queryForMap(masterIdSql,new Object[] {cslsh});
        try {
            if (masterIdMap.get("ID") == null) {//新增
                //主表：采收管理新增
                String masterId = UUIDGenerator.uuid();
                String insertMasterSql = "insert into T_ZZ_CSGL (qvmc,id,qybm,cslsh,qybh,dkbh,dkmc,zzdybh,zzdymc,scdabh,pl,plbh,pz,pzbh,cssj,zlhj,kczl) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                Object[] masterParams = new Object[] {qymc,masterId,qybm,cslsh,qybh,dkbh,dkmc,zzdybh,zzdymc,scdabh,pl,plbh,pz,pzbh,cssj,zl,zl};
                DatabaseHandlerDao.getInstance().executeSql(insertMasterSql,masterParams);
                //从表：采收农作物详情
                String detailId = UUIDGenerator.uuid();
                String insertDetailSql = "insert into t_zz_csnzwxq (id,pid,qybm,zldj,dyzs,pch,kczl,zl) values (?,?,?,?,?,?,?,?)";
                Object [] detailParams = new Object[] {detailId,masterId,qybm,zldj,dyzs,pch,zl,zl};
                DatabaseHandlerDao.getInstance().executeSql(insertDetailSql,detailParams);
                //更新检测记录表
                String scidSql = "select * from t_zz_scda t where t.scdabh = '" + scdabh + "' and t.qybm = '" + qybm + "'";
                Map<String,Object> dataMap = DatabaseHandlerDao.getInstance().queryForMap(scidSql);
                String jcjlId = UUIDGenerator.uuid();
                String jcjlSql = "insert into T_ZZ_SCJCJL (id,cslsh,pid) values (?,?,?)";
                DatabaseHandlerDao.getInstance().executeSql(jcjlSql,new Object[]{jcjlId,cslsh,dataMap.get("ID").toString()});
            } else {//更新
                //更新主表
                String masterId = masterIdMap.get("ID").toString();
                String updateMasterSql = "update T_ZZ_CSGL set zlhj = (to_number(zlhj) + to_number(?)),kczl = (to_number(kczl) + to_number(?)) where id = ?";
                Object[] masterParams = new Object[] {zl,zl,masterId};
                DatabaseHandlerDao.getInstance().executeSql(updateMasterSql,masterParams);
                //更新从表
                String detailIdSql = "select id from t_zz_csnzwxq where pch = ?";
                Map<String,Object> detailIdMap = DatabaseHandlerDao.getInstance().queryForMap(detailIdSql,new Object[] {pch});
                if (detailIdMap.get("ID") == null) {//从表新增
                    String detailId = UUIDGenerator.uuid();
                    String insertDetailSql = "insert into t_zz_csnzwxq (id,pid,qybm,zldj,dyzs,pch,kczl,zl) values (?,?,?,?,?,?,?,?)";
                    Object [] detailParams = new Object[] {detailId,masterId,qybm,zldj,dyzs,pch,zl,zl};
                    DatabaseHandlerDao.getInstance().executeSql(insertDetailSql,detailParams);
                } else {//从表更新
                    String detailId = detailIdMap.get("ID").toString();
                    String updateDetailSql = "update t_zz_csnzwxq set zl = (to_number(zl) + to_number(?)),kczl = (to_number(kczl) + to_number(?)) where id = ?";
                    Object[] detailParams = new Object[] {zl,zl,detailId};
                    DatabaseHandlerDao.getInstance().executeSql(updateDetailSql,detailParams);
                }
            }
            return "SUCCESS";
        } catch (Exception e) {
            return "ERROR";
        }
    }

    /**
     * 通过追溯码获取散货小包装产品信息
     * @param cpzsm
     * @return
     */
    public Map<String,Object> getShxbzByCpzsm (String cpzsm) {
        String sql = "select t.cpdj, t.bzgg, t.cpmc, t.cpbh, s.bxq\n" +
                "  from t_zz_bzgl t\n" +
                "  left join t_zz_cpxxgl s\n" +
                "    on t.cpbh = s.cpbh where t.cpzsm=?";
        Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql,new Object[]{cpzsm});
        return map;
    }

}

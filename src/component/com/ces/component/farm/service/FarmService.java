package com.ces.component.farm.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.component.farm.utils.TxtUtils;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.TraceChainUtil;
import com.ces.config.utils.ComponentFileUtil;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ces.component.farm.dto.CktmDto;
import com.ces.component.farm.dto.CptmDto;
import com.ces.component.farm.dto.CscpDto;
import com.ces.component.farm.dto.XstmDto;
import com.ces.component.farm.utils.FarmCommonUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FarmService {

    /**
     * (3)客户信息查询
     *
     * @param pageRequest
     * @param sqydm       市区域代码
     * @param dqqydm      地区区域代码
     * @param khmc        客户名称
     * @param qybm        企业编码
     * @return page
     */
    public Page searchKhxx(String sqydm, String dqqydm, String khmc, String qybm, PageRequest pageRequest) {
        StringBuffer buffer = new StringBuffer("select k.id as khid,k.khmc from t_zz_khxx k,t_zz_cdda cd where k.qybm = cd.qybm and k.is_delete != '1' and cd.is_delete !='1' and k.sfktzh = '1'");
        if (StringUtil.isNotEmpty(sqydm)) {//市区域代码为地区区域代码前两位+0000
            if (sqydm.length() == 6) {//必须为6位
                String qydm = sqydm.substring(0, 2);
                buffer.append(" and k.lsxzqhbm like '").append(qydm).append("%'");
            }
        }
        if (StringUtil.isNotEmpty(dqqydm)) {
            buffer.append(" and k.lsxzqhbm like '%").append(dqqydm).append("%'");
        }
        if (StringUtil.isNotEmpty(khmc)) {
            buffer.append(" and k.khmc like '%").append(khmc).append("%'");
        }
        if (StringUtil.isNotEmpty(qybm)) {
            buffer.append(" and cd.qybm='").append(qybm).append("'");
        }
        return FarmCommonUtil.queryPage(pageRequest, buffer.toString());
    }

    /**
     * (4)出库条码查询
     *
     * @param spzsm
     * @return
     */
    public CktmDto searchCktm(String spzsm,String qybm) {
        CktmDto dto = null;
        String sql;
        if(StringUtil.isEmpty(spzsm)){
            return null;
        }
		//包装产品
        sql = "select bz.cpzsm,bz.cpmc,bz.bzsj,bz.qymc,bz.id from t_zz_bzgl bz where bz.cpzsm = ? and bz.qybm=? and  bz.is_delete <> '1'";
		Map<String,Object> data = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{spzsm,qybm});
		if(!data.isEmpty()&&data.size()>0){
			dto = new CktmDto();
			dto.setCd(data.get("CDMC")==null?"":String.valueOf(data.get("CDMC")));
			dto.setCpmc(data.get("CPMC")==null?"":String.valueOf(data.get("CPMC")));
			dto.setCpzlId(data.get("ID")==null?"":String.valueOf(data.get("ID")));
			dto.setScrq(data.get("CCSJ")==null?"":String.valueOf(data.get("CCSJ")));
			dto.setZsm(data.get("CPZSM")==null?"":String.valueOf(data.get("CPZSM")));
            dto.setCklx("1");//包装出库
			return dto;
		}
		//散货
        sql = "select xq.pch,cs.pz,cs.cssj,cd.cdmc,cs.id from t_zz_csgl cs,t_zz_csnzwxq xq,t_zz_cdda cd where cd.qybm = xq.qybm and xq.qybm=? and xq.pid = cs.id and xq.pch = ?" +
                " and cs.is_delete <> '1' and xq.is_delete <> '1' and cd.is_delete <> '1'";
		Map<String,Object> szData = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{qybm,spzsm});
		if(!szData.isEmpty()&&szData.size()>0){
			dto = new CktmDto();
			dto.setCd(szData.get("CDMC")==null?"":String.valueOf(szData.get("CDMC")));
			dto.setCpmc(szData.get("PZ")==null?"":String.valueOf(szData.get("PZ")));
			dto.setCpzlId(szData.get("ID")==null?"":String.valueOf(szData.get("ID")));
			dto.setScrq(szData.get("CCSJ")==null?"":String.valueOf(szData.get("CCSJ")));
			dto.setZsm(szData.get("CPZSM")==null?"":String.valueOf(szData.get("CPZSM")));
            dto.setCklx("2");//散货出库
			return dto;
		}
        /*if (StringUtil.isNotEmpty(spzsm)) {
            if (spzsm.length() == 12) {//包装产品
                sql = "select bz.cpzsm,bz.cpmc,bz.bzsj,bz.qymc,bz.id from t_zz_bzgl bz where bz.cpzsm = ? and  bz.is_delete != '1'";
                Map<String, Object> data = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{spzsm});
                if (!data.isEmpty() && data.size() > 0) {
                    dto = new CktmDto();
                    dto.setCd(data.get("QYMC") == null ? "" : String.valueOf(data.get("QYMC")));
                    dto.setCpmc(data.get("CPMC") == null ? "" : String.valueOf(data.get("CPMC")));
                    dto.setCpzlId(data.get("ID") == null ? "" : String.valueOf(data.get("ID")));
                    dto.setScrq(data.get("BZSJ") == null ? "" : String.valueOf(data.get("BZSJ")));
                    dto.setZsm(data.get("CPZSM") == null ? "" : String.valueOf(data.get("CPZSM")));
                }
                return dto;
            } else if (spzsm.length() == 18) {//散货
                sql = "select xq.pch,cs.pz,cs.cssj,cd.cdmc,cs.id from t_zz_csgl cs,t_zz_csnzwxq xq,t_zz_cdda cd where cd.qybm = xq.qybm and xq.pid = cs.id and xq.pch = ?" +
                        " and cs.is_delete != '1' and xq.is_delete != '1' and cd.is_delete != '1'";
                Map<String, Object> data = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{spzsm});
                if (!data.isEmpty() && data.size() > 0) {
                    dto = new CktmDto();
                    dto.setCd(data.get("CDMC") == null ? "" : String.valueOf(data.get("CDMC")));
                    dto.setCpmc(data.get("PZ") == null ? "" : String.valueOf(data.get("PZ")));
                    dto.setCpzlId(data.get("ID") == null ? "" : String.valueOf(data.get("ID")));
                    dto.setScrq(data.get("CSSJ") == null ? "" : String.valueOf(data.get("CSSJ")));
                    dto.setZsm(data.get("PCH") == null ? "" : String.valueOf(data.get("PCH")));
                }
                return dto;
            }
        }*/
        return dto;
    }
//	/**
//	 * (5)产品条码确定
//	 * @param spzsm 商品追溯码
//	 * @param ckdId 出库单ID
//	 * @param khId 客户ID
//	 * @param rq 日期
//	 * @return
//	 */
//	public CptmDto searchCkdxx(String spzsm,String khId,String rq){
//		CptmDto dto = null;
//		String tableName = FarmCommonUtil.getInstance().getTableNameByZsm(spzsm);
//		String sql;
//		StringBuffer buffer = new StringBuffer();
//		if(StringUtil.isNotEmpty(tableName)){
//			if("T_ZZ_CCBZCPXX".equals(tableName)){
//				sql = "select cc.id as ckid, bz.cpzsm,bz.cpmc,cc.ccsj,cd.cdmc,bz.id as cpid from t_zz_ccgl cc,t_zz_ccbzcpxx bz,t_zz_cdda cd,t_zz_khxx kh" +
//						" where cc.id = bz.pid and cc.qybm = cd.qybm and cc.khbh = kh.khbh";
//				buffer.append(sql);
//				if(StringUtil.isNotEmpty(spzsm)){
//					buffer.append(" and bz.cpzsm = '").append(spzsm).append("'");
//				}
//				if(StringUtil.isNotEmpty(khId)){
//					buffer.append(" and kh.id = '").append(khId).append("'");
//				}
//				if(StringUtil.isNotEmpty(rq)){
//					buffer.append(" and cc.ccsj >= '").append(rq).append(" 00:00:00' and cc.ccsj <= '").append(rq).append(" 23:59:59'");
//				}
//				Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(buffer.toString());
//				if(!map.isEmpty()&&map.size()>0){
//					dto = new CptmDto();
//					dto.setCpckId(map.get("CKID")==null?"":String.valueOf(map.get("CKID")));
//					dto.setZsm(map.get("CPZSM")==null?"":String.valueOf(map.get("CPZSM")));
//					dto.setCpmc(map.get("CPMC")==null?"":String.valueOf(map.get("CPMC")));
//					dto.setScrq(map.get("CCSJ")==null?"":String.valueOf(map.get("CCSJ")));
//					dto.setCd(map.get("CDMC")==null?"":String.valueOf(map.get("CDMC")));
//					dto.setCpzlId(map.get("CPID")==null?"":String.valueOf(map.get("CPID")));
//				}
//			}
//			if("T_ZZ_CCSHXX".equals(tableName)){
//				sql = "select cc.id as ckid, sh.cpzsm,sh.pz,cc.ccsj,cd.cdmc,sh.id as cpid from t_zz_ccgl cc,t_zz_ccshxx sh,t_zz_cdda cd,t_zz_khxx kh" +
//				" where cc.id = sh.pid and cc.qybm = cd.qybm and cc.khbh = kh.khbh";
//				buffer.setLength(0);
//				buffer.append(sql);
//				if(StringUtil.isNotEmpty(spzsm)){
//					buffer.append(" and sh.cpzsm = '").append(spzsm).append("'");
//				}
//				if(StringUtil.isNotEmpty(khId)){
//					buffer.append(" and kh.id = '").append(khId).append("'");
//				}
//				if(StringUtil.isNotEmpty(rq)){
//					buffer.append(" and cc.ccsj >= '").append(rq).append(" 00:00:00' and cc.ccsj <= '").append(rq).append(" 23:59:59'");
//				}
//				Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(buffer.toString());
//				if(!map.isEmpty()&&map.size()>0){
//					dto = new CptmDto();
//					dto.setCpckId(map.get("CKID")==null?"":String.valueOf(map.get("CKID")));
//					dto.setZsm(map.get("CPZSM")==null?"":String.valueOf(map.get("CPZSM")));
//					dto.setCpmc(map.get("PZ")==null?"":String.valueOf(map.get("PZ")));
//					dto.setScrq(map.get("CCSJ")==null?"":String.valueOf(map.get("CCSJ")));
//					dto.setCd(map.get("CDMC")==null?"":String.valueOf(map.get("CDMC")));
//					dto.setCpzlId(map.get("CPID")==null?"":String.valueOf(map.get("CPID")));
//				}
//			}
//		}
//		return dto;
//	}

    /**
     * (6)出库信息更新
     *
     * @param ckId
     * @param cpzlId
     * @param zl
     * @param sl
     * @param khId
     * @return
     */
    @Transactional
    public CptmDto updateCkxx(String spzsm, String ckdId, String ckId, String cpzlId, String zl, String sl, String qyId, String khId, String qybm) {
        CptmDto dto = null;
        if (StringUtil.isEmpty(spzsm)) {
            return dto;
        }
        if (StringUtil.isEmpty(zl)) {
            zl = "0";
        }
        if (StringUtil.isEmpty(sl)) {
            sl = "0";
        }

        //查询包装产品
        String bzcpSql = "select t.id from t_zz_bzgl t where t.cpzsm = ? and t.is_delete != '1'";
        Map<String, Object> bzData = DatabaseHandlerDao.getInstance().queryForMap(bzcpSql, new Object[]{spzsm});
        if (!bzData.isEmpty() && bzData.size() > 0) {
            dto = new CptmDto();
            insertCcbzcp(ckdId,spzsm,sl,zl,qybm);
            //更新包装管理库存重量
            String updateBzgl = "update t_zz_bzgl t set t.cpsl = t.cpsl-" + Integer.valueOf(sl) + " where t.is_delete != '1' and t.cpzsm ='" + spzsm + "'";
            DatabaseHandlerDao.getInstance().executeSql(updateBzgl);
            //更新出场总重量:产品按件计算，不更新总重量 2015.09.23 13:10 刘雪、黄翔宇
//						String updateCcgl = "update t_zz_ccgl t set t.zzl = t.zzl +"+Float.valueOf(zl)+" where t.id = '"+ckdId+"'";
//						DatabaseHandlerDao.getInstance().executeSql(updateCcgl);
            dto.setCkdId(ckdId);
            return dto;
        }
        //查询散货信息
        String szSql = "select t.id from t_zz_csnzwxq t where t.pch = ? and t.is_delete != '1'";
        Map<String, Object> szData = DatabaseHandlerDao.getInstance().queryForMap(szSql, new Object[]{spzsm});
        if (!szData.isEmpty() && szData.size() > 0) {
            dto = new CptmDto();
            insertCcsh(spzsm,zl,ckdId,qybm);
            //更新采收管理库存重量、已出场重量
            String updateCsgl = "update t_zz_csgl t set t.kczl = t.kczl-" + Float.valueOf(zl) + ",t.ycczl = t.ycczl+" + Float.valueOf(zl) + " where t.id = (select t.pid from t_zz_csnzwxq t where t.pch='" + spzsm + "')";
            DatabaseHandlerDao.getInstance().executeSql(updateCsgl);
            //更新出场总重量
            String updateCcgl = "update t_zz_ccgl t set t.zzl = t.zzl +" + Float.valueOf(zl) + " where t.id = '" + ckdId + "'";
            DatabaseHandlerDao.getInstance().executeSql(updateCcgl);
            dto.setCkdId(ckdId);
            return dto;
        }

        return dto;

    }

    /**
     * 插入出场信息
     * @param khId
     * @return ccId
     */
    @Transactional
    public String insertCcxx(String khId){
        //查询客户信息
        String khSql = "select t.* from t_zz_khxx t where t.id = ? and t.is_delete <> '1'";
        Map<String, Object> khData = DatabaseHandlerDao.getInstance().queryForMap(khSql, new Object[]{khId});
        //添加主表数据
        String ccId = UUIDGenerator.uuid();
        String mainSql = "insert into t_zz_ccgl (id,create_user,create_time,is_delete,cclsh,SFDH,khmc,xsddh,ccsj,psfs,zzl,cph,pszrr,dyzt,bz,qybm,khbh,is_delivered) " +
                "values ('" + ccId + "','"
                + "','"
                + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) + "','"
                + "0','"
                +SerialNumberUtil.getInstance().getSerialNumberByQybm("ZZ", khData.get("QYBM").toString(),"CCLSH",false)+"','"
                +"ZZ" + SerialNumberUtil.getInstance().getSerialNumberByQybm("ZZ", khData.get("QYBM").toString(),"CCPCH",true)+"','"
                + (khData.get("KHMC") == null ? "" : String.valueOf(khData.get("KHMC"))) + "','"
                + "','"
                + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) + "','',0,'','','','','"
                + (khData.get("QYBM") == null ? "" : String.valueOf(khData.get("QYBM"))) + "','"
                + (khData.get("KHBH") == null ? "" : String.valueOf(khData.get("KHBH"))) + "','"
                + "0')";
        DatabaseHandlerDao.getInstance().executeSql(mainSql);
        return ccId;
    }

    /**
     * 新增包装出场产品
     * @param spzsm
     * @param sl
     * @param zl
     */
    @Transactional
    public void insertCcbzcp(String ckdId,String spzsm,String sl,String zl,String qybm){
        //查询产品信息
        String cpSql = "select bz.cpzsm,bz.cpmc,bz.cpbh,bz.cpdj,bz.bzsj,bz.qymc,bz.id,bz.bzlsh from t_zz_bzgl bz " +
                " where bz.cpzsm = ? and bz.is_delete <> '1'";
        Map<String, Object> cpData = DatabaseHandlerDao.getInstance().queryForMap(cpSql, new Object[]{spzsm});
        if (StringUtil.isNotEmpty(ckdId)) {
            String zspch = SerialNumberUtil.getInstance().getSerialNumberByQybm("ZZ", qybm, "ZZZSPCH", true);
            String uuid = UUIDGenerator.uuid();
            //插入数据
            String insertSql = "insert into t_zz_ccbzcpxx (id,create_user,create_time,is_delete,bzlsh,cpbh,cpmc,cpdj,ccjs,pid,zl,cpzsm,qybm,zspch) values" +
                    " ('" + uuid + "','','" +
                    (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date()) + "','0','"
                    + (cpData.get("BZLSH") == null ? "" : String.valueOf(cpData.get("BZLSH"))) + "','"
                    + (cpData.get("CPBH") == null ? "" : String.valueOf(cpData.get("CPBH"))) + "','"
                    + (cpData.get("CPMC") == null ? "" : String.valueOf(cpData.get("CPMC"))) + "','"
                    + (cpData.get("CPDJ") == null ? "" : String.valueOf(cpData.get("CPDJ"))) + "',"
                    + Integer.valueOf(sl) + ",'"
                    + ckdId + "',"
                    + Float.valueOf(zl) + ",'"
                    + spzsm + "','" +
                    qybm+"','"+zspch+"')";
            DatabaseHandlerDao.getInstance().executeSql(insertSql);

            /****************同步追溯信息*****************/
            TCsptZsxxEntity entity = new TCsptZsxxEntity();
            entity.setZsm(spzsm);
            entity.setJhpch(zspch);
            entity.setQybm(qybm);
            entity.setQymc(CompanyInfoUtil.getInstance().getCompanyName("ZZ", qybm));
            entity.setXtlx("1");
            entity.setRefId(uuid);
//        entity.setZZYZPCH(shxxMap.get("PCH"));
            TraceChainUtil.getInstance().syncZsxx(entity);
            /****************同步追溯信息*****************/
        }
    }

    /**
     * 插入出场散货信息
     * @param spzsm
     * @param zl
     * @param ccid
     * @param qybm
     */
    @Transactional
    public void insertCcsh(String spzsm,String zl,String ccid,String qybm){
        //查询产品信息
        String shSql = "SELECT CS.ID,XQ.PCH,CS.PZ,CS.PZBH,CS.CSSJ,XQ.ZL,XQ.KCZL FROM T_ZZ_CSGL CS,T_ZZ_CSNZWXQ XQ " +
                " WHERE XQ.PID = CS.ID AND XQ.PCH = ? AND CS.IS_DELETE != '1' AND XQ.IS_DELETE != '1'";
        Map<String, Object> shData = DatabaseHandlerDao.getInstance().queryForMap(shSql, new Object[]{spzsm});
        String zspch = SerialNumberUtil.getInstance().getSerialNumberByQybm("ZZ",qybm, "ZZZSPCH", true);
        String uuid = UUIDGenerator.uuid();
        String zsm = SerialNumberUtil.getInstance().getSerialNumber("ZZ", qybm, "ZZCPZSM", true);
        String insertSql = "INSERT INTO T_ZZ_CCSHXX (ID,CREATE_USER,CREATE_TIME,IS_DELETE,CSLSH,PZ,PZBH,QYBM,CSZZL,CCZL,CJZDBH,PID,PCH,KCZL,YJZT,BYJR,YJSJ,CPZSM,zspch) VALUES" +
                " ('" + uuid + "','','" +
                (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) + "','0','"
                + spzsm + "','"
                + (shData.get("PZ") == null ? "" : String.valueOf(shData.get("PZ"))) + "','"
                + (shData.get("PZBH") == null ? "" : String.valueOf(shData.get("PZBH"))) + "','"
                + qybm + "',"
                + (shData.get("ZL") == null ? "0" : String.valueOf(shData.get("ZL"))) + ","
                + Float.valueOf(zl) + ",'"
                + "','"
                + ccid + "','"
                + spzsm + "',"
                + (shData.get("KCZL") == null ? "0" : String.valueOf(shData.get("CKZL"))) + ",'"
                + "0','','','" + zsm + "','"+zspch+"')";
        DatabaseHandlerDao.getInstance().executeSql(insertSql);

        /****************同步追溯信息*****************/
        TCsptZsxxEntity entity = new TCsptZsxxEntity();
        entity.setZsm(zsm);
        entity.setJhpch(zspch);
        entity.setQybm(qybm);
        entity.setQymc(CompanyInfoUtil.getInstance().getCompanyName("ZZ", qybm));
        entity.setXtlx("1");
        entity.setRefId(uuid);
//        entity.setZZYZPCH(shxxMap.get("PCH"));
        TraceChainUtil.getInstance().syncZsxx(entity);
        /****************同步追溯信息*****************/
    }
//	/**
//	 * (7)出库信息产品信息查询
//	 * @param qyId 企业ID
//	 * @param rq 日期 YYYY-MM-DD
//	 * @param khId 客户ID
//	 * @param pageRequest
//	 * @return page
//	 */
//	public Page searchCkcpxx(String qyId,String rq,String khId, PageRequest pageRequest){
//		Page page = null;
//		//判断是否有出厂单
//		String sql = "select t.id from t_zz_ccgl t, t_zz_cdda c, t_zz_khxx k where t.qybm = c.qybm and t.khbh = k.khbh";
//		if(StringUtil.isNotEmpty(qyId)){
//			sql += " and c.id = '"+qyId+"'";
//		}
//		if(StringUtil.isNotEmpty(rq)){
//			sql +=" and t.ccsj >= '"+rq+" 00:00:00' and t.ccsj <= '"+rq+" 23:59:59'";
//		}
//		if(StringUtil.isNotEmpty(khId)){
//			sql += " and k.id = '"+khId+"'";
//		}
//		List list = DatabaseHandlerDao.getInstance().queryForList(sql);
//		if(list!=null&&list.size()>0){
//			//包装产品
//			String bzSql = "select bz.id,bz.cpzsm,bz.cpmc,bz.zl from t_zz_ccbzcpxx bz,t_zz_ccgl cc ,t_zz_khxx kh,t_zz_cd cd where kh.khbh=cc.khbh and cd.qybh=cc.qybm and cc.id=bz.pid";
//			StringBuffer buffer = new StringBuffer(bzSql);
//			if(StringUtil.isNotEmpty(qyId)){
//				buffer.append(" and cd.id = '").append(qyId).append("'");
//			}
//			if(StringUtil.isNotEmpty(rq)){
//				buffer.append(" and cc.ccsj >= '").append(rq).append(" 00:00:00' and cc.ccsj <= '").append(rq).append(" 23:59:59'");
//			}
//			if(StringUtil.isNotEmpty(khId)){
//				buffer.append(" and kh.id = '").append(khId).append("'");
//			}
//			//排序
//			buffer.append(" and bz.is_delete != '1' and cc.is_delete != '1' and kh.is_delete != '1' order by bz.create_time desc");
//			Page bzPage = FarmCommonUtil.getInstance().queryPage(pageRequest, buffer.toString());
//			
//			//散货
//			String shSql = "select sh.id,sh.cpzsm,sh.pz as cpmc,sh.cczl as zl from t_zz_ccshxx sh,t_zz_ccgl cc ,t_zz_khxx kh,t_zz_cd cd where kh.khbh=cc.khbh and cd.qybh=cc.qybm and cc.id=sh.pid";
//			if(StringUtil.isNotEmpty(qyId)){
//				buffer.append(" and cd.id = '").append(qyId).append("'");
//			}
//			if(StringUtil.isNotEmpty(rq)){
//				buffer.append(" and cc.ccsj >= '").append(rq).append(" 00:00:00' and cc.ccsj <= '").append(rq).append(" 23:59:59'");
//			}
//			if(StringUtil.isNotEmpty(khId)){
//				buffer.append(" and kh.id = '").append(khId).append("'");
//			}
//			//排序
//			buffer.append(" and sh.is_delete != '1' and cc.is_delete != '1' and kh.is_delete != '1' order by sh.create_time desc");
//			Page shPage = FarmCommonUtil.getInstance().queryPage(pageRequest, buffer.toString());
//			
//			List contentList = new ArrayList();
//			List bzContentList = bzPage.getContent();
//			List shContentList = shPage.getContent();
//			//合并
//			if(bzContentList!=null&&bzContentList.size()>0){
//				for(int i=0;i<bzContentList.size();i++){
//					contentList.add(bzContentList.get(i));
//				}
//			}
//			if(shContentList!=null&&shContentList.size()>0){
//				for(int j=0;j<shContentList.size();j++){
//					contentList.add(shContentList.get(j));
//				}
//			}
//			
//			if(contentList!=null&&contentList.size()>0){
//				page = new PageImpl(contentList);
//			}
//		}else{
//			page = null;
//			//String insertSql = "insert into t_zz_ccgl (id,create_time,cclsh,kkmc,ccsj,qybm,khbh) values ('"+ SerialNumberUtil.getInstance().getSerialNumber("ZZ","CCLSH",false)+"')";
//		}
//		
//		return page;
//	}

    /**
     * (8)出库单出库产品信息
     *
     * @param ckdId
     * @param pageRequest
     * @return
     */
    public Page searchCkdckcpxx(String ckdId, PageRequest pageRequest) {
        Page page = null;
        if (StringUtil.isNotEmpty(ckdId)) {
            //包装产品
            String bzSql = "select t.id,t.cpzsm,t.cpmc,t.ccjs as js from t_zz_ccbzcpxx t where 1=1";
            StringBuffer buffer = new StringBuffer();
            buffer.append(bzSql).append(" and t.pid ='").append(ckdId).append("'");
            buffer.append(" and t.is_delete != '1' order by t.create_time desc");
            Page bzPage = FarmCommonUtil.queryPage(pageRequest, buffer.toString());
            //散货
            String shSql = "select t.id,t.cpzsm,t.pz as cpmc,t.cczl as zl from t_zz_ccshxx t where t.is_delete != '1'";
            buffer.setLength(0);
            buffer.append(shSql).append(" and t.pid = '").append(ckdId).append("'");
            buffer.append("order by t.create_time desc");
            Page shPage = FarmCommonUtil.queryPage(pageRequest, buffer.toString());

            List contentList = new ArrayList();
            List bzContentList = bzPage.getContent();
            List shContentList = shPage.getContent();
            //合并
            if (bzContentList != null && bzContentList.size() > 0) {
                for (int i = 0; i < bzContentList.size(); i++) {
                    contentList.add(bzContentList.get(i));
                }
            }
            if (shContentList != null && shContentList.size() > 0) {
                for (int j = 0; j < shContentList.size(); j++) {
                    contentList.add(shContentList.get(j));
                }
            }

            if (contentList != null && contentList.size() > 0) {
                page = new PageImpl(contentList);
            }
        }


        return page;
    }

    /**
     * (9)出库单上产品删除
     *
     * @param cpId
     * @return
     */
    public boolean deleteCkdcp(String cpId) {
        boolean result = false;
        if (StringUtil.isNotEmpty(cpId)) {
            //包装产品
//			String sql = "delete from t_zz_ccbzcpxx t where t.id in ('"+cpId.replace(",", "'")+"')";
            String sql = "update t_zz_ccbzcpxx t set t.is_delete = '1' where t.id in ('" + cpId.replace(",", "'") + "')";
            DatabaseHandlerDao.getInstance().executeSql(sql);
            //散货
            String shSql = "update t_zz_ccshxx t set t.is_delete = '1' where t.id in ('" + cpId.replace(",", "'") + "')";
            DatabaseHandlerDao.getInstance().executeSql(shSql);
            result = true;
        }
        return result;
    }

    /**
     * (10)销售条码查询
     *
     * @param spzsm 商品追溯码
     * @return
     */
    public CktmDto searchXstm(String spzsm) {
        CktmDto dto = new CktmDto();
        String sql;

        if (StringUtil.isNotEmpty(spzsm)) {
            if (spzsm.length() == 20) {//包装产品
                sql = "select bz.cpzsm,bz.cpmc,bz.bzsj,bz.qymc,bz.id from t_zz_bzgl bz where bz.cpzsm = ? and  bz.is_delete != '1'";
                Map<String, Object> data = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{spzsm});
                if (!data.isEmpty() && data.size() > 0) {
                    dto = new CktmDto();
                    dto.setCd(data.get("QYMC") == null ? "" : String.valueOf(data.get("QYMC")));
                    dto.setCpmc(data.get("CPMC") == null ? "" : String.valueOf(data.get("CPMC")));
                    dto.setCpzlId(data.get("ID") == null ? "" : String.valueOf(data.get("ID")));
                    dto.setScrq(data.get("BZSJ") == null ? "" : String.valueOf(data.get("BZSJ")));
                    dto.setZsm(data.get("CPZSM") == null ? "" : String.valueOf(data.get("CPZSM")));
                }
                return dto;
            } else if (spzsm.length() == 17) {//散货
                sql = "select xq.pch,cs.pl,cs.cssj,cd.cdmc,cs.id from t_zz_csgl cs,t_zz_csnzwxq xq,t_zz_cdda cd where cd.qybm = xq.qybm and xq.pid = cs.id and xq.pch = ?" +
                        " and cs.is_delete != '1' and xq.is_delete != '1' and cd.is_delete != '1'";
                Map<String, Object> data = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{spzsm});
                if (!data.isEmpty() && data.size() > 0) {
                    dto = new CktmDto();
                    dto.setCd(data.get("CDMC") == null ? "" : String.valueOf(data.get("CDMC")));
                    dto.setCpmc(data.get("PL") == null ? "" : String.valueOf(data.get("PL")));
                    dto.setCpzlId(data.get("ID") == null ? "" : String.valueOf(data.get("ID")));
                    dto.setScrq(data.get("CSSJ") == null ? "" : String.valueOf(data.get("CSSJ")));
                    dto.setZsm(data.get("PCH") == null ? "" : String.valueOf(data.get("PCH")));
                }
                return dto;
            }
        }
        return dto;
//		//包装产品
//		sql = "select bz.cpzsm,bz.cpmc,cc.ccsj,cd.cdmc,bz.id from t_zz_ccbzcpxx bz,t_zz_ccgl cc,t_zz_cdda cd where bz.pid = cc.id and cc.qybm = cd.qybm";
//		if(StringUtil.isNotEmpty(spzsm)){
//			buffer.append(sql).append(" and bz.cpzsm = ?");
//			Map<String,Object> data = DatabaseHandlerDao.getInstance().queryForMap(buffer.toString(), new Object[]{spzsm});
//			if(!data.isEmpty()&&data.size()>0){
//				dto.setCd(data.get("CDMC")==null?"":String.valueOf(data.get("CDMC")));
//				dto.setCpmc(data.get("CPMC")==null?"":String.valueOf(data.get("CPMC")));
//				dto.setCpzlId(data.get("ID")==null?"":String.valueOf(data.get("ID")));
//				dto.setScrq(data.get("CCSJ")==null?"":String.valueOf(data.get("CCSJ")));
//				dto.setZsm(data.get("CPZSM")==null?"":String.valueOf(data.get("CPZSM")));
//				return dto;
//			}
//		}
//		
//		//散货
//		sql = "select sh.cpzsm, sh.pz, cc.ccsj, cd.cdmc, sh.id from t_zz_ccshxx sh, t_zz_ccgl cc, t_zz_cdda cd where sh.pid = cc.id and cc.qybm = cd.qybm";
//		if(StringUtil.isNotEmpty(spzsm)){
//			buffer.setLength(0);//清空buffer
//			buffer.append(sql).append(" and sh.cpzsm = ?");
//			Map<String,Object> szData = DatabaseHandlerDao.getInstance().queryForMap(buffer.toString(), new Object[]{spzsm});
//			if(!szData.isEmpty()&&szData.size()>0){
//				dto.setCd(szData.get("CDMC")==null?"":String.valueOf(szData.get("CDMC")));
//				dto.setCpmc(szData.get("PZ")==null?"":String.valueOf(szData.get("PZ")));
//				dto.setCpzlId(szData.get("ID")==null?"":String.valueOf(szData.get("ID")));
//				dto.setScrq(szData.get("CCSJ")==null?"":String.valueOf(szData.get("CCSJ")));
//				dto.setZsm(szData.get("CPZSM")==null?"":String.valueOf(szData.get("CPZSM")));
//				return dto;
//			}
//		}
//		
//		return dto;
    }

    /**
     * 11 销售条码确定
     *
     * @param spzsm
     * @param mdId
     * @return
     */
    public XstmDto xstmqd(String spzsm, String mdId) {
        XstmDto dto = null;
        String sql;
        if (StringUtil.isNotEmpty(spzsm) && StringUtil.isNotEmpty(mdId)) {
            String rq = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
            if (spzsm.length() == 20) {//包装产品
                sql = "select t.id from t_zz_pcjdxx t where t.is_delete != '1' and t.jrmd ='"
                        + mdId + "' and t.jdrq >= '"
                        + rq + " 00:00:00' and t.jdrq <= '"
                        + rq + " 23:59:59' and t.cpmc in ( select bz.cpmc from t_zz_bzgl bz where bz.cpzsm = '"
                        + spzsm + "' and bz.is_delete != '1')";
                Map<String, Object> data = DatabaseHandlerDao.getInstance().queryForMap(sql);
                String dataSql = "select bz.id,bz.cpmc,bz.bzsj,bz.qymc from t_zz_bzgl bz where bz.cpzsm = '"
                        + spzsm + "' and bz.is_delete != '1'";
                Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(dataSql);
                if (!data.isEmpty() && data.size() > 0) {
                    dto = new XstmDto();
                    dto.setZsm(spzsm);
                    dto.setCd(map.get("QYMC") == null ? "" : String.valueOf(map.get("QYMC")));
                    dto.setCpmc(map.get("CPMC") == null ? "" : String.valueOf(map.get("CPMC")));
                    dto.setCpxxId(data.get("ID") == null ? "" : String.valueOf(data.get("ID")));
                    dto.setCpzlId(map.get("ID") == null ? "" : String.valueOf(map.get("ID")));
                    dto.setScrq(map.get("BZSJ") == null ? "" : String.valueOf(map.get("BZSJ")));
                    return dto;
                } else {
                    dto = new XstmDto();
                    String jdId = UUIDGenerator.uuid();
                    String insertSql = "insert into t_zz_pcjdxx (id,create_user,create_time,is_delete,cspch,jdrq,jrmd,jrsl,jrzl,cpmc) values ('"
                            + jdId + "','"
                            + "','"
                            + (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date()) + "','"
                            + "0','"
                            + spzsm + "','"
                            + (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date()) + "','"
                            + mdId + "','"
                            + "0','"
                            + "0','"
                            + (map.get("CPMC") == null ? "" : String.valueOf(map.get("CPMC"))) + "')";
                    DatabaseHandlerDao.getInstance().executeSql(insertSql);
                    dto = new XstmDto();
                    dto.setZsm(spzsm);
                    dto.setCd(map.get("QYMC") == null ? "" : String.valueOf(map.get("QYMC")));
                    dto.setCpmc(map.get("CPMC") == null ? "" : String.valueOf(map.get("CPMC")));
                    dto.setCpxxId(jdId);
                    dto.setCpzlId(map.get("ID") == null ? "" : String.valueOf(map.get("ID")));
                    dto.setScrq(map.get("SCRQ") == null ? "" : String.valueOf(map.get("SCRQ")));
                    return dto;
                }
            } else if (spzsm.length() == 17) {//散货
                sql = "select t.id from t_zz_pcjdxx t where t.is_delete != '1' and t.jrmd ='"
                        + mdId + "' and t.jdrq >= '"
                        + rq + " 00:00:00' and t.jdrq <= '"
                        + rq + " 23:59:59' and t.cpmc in ( select cs.pz from t_zz_csgl cs,t_zz_csnzwxq xq where cs.id = xq.pid and xq.pch = '"
                        + spzsm + "' and cs.is_delete != '1' and xq.is_delete != '1')";
                Map<String, Object> data = DatabaseHandlerDao.getInstance().queryForMap(sql);
                String dataSql = "select cs.id,cs.pz,cs.cssj,cd.qymc from t_zz_csgl cs,t_zz_csnzwxq xq ,t_zz_cdda cd where cs.id = xq.pid and cs.qybm = cd.qybm and xq.pch = '"
                        + spzsm + "' and cs.is_delete != '1' and xq.is_delete != '1' and cd.is_delete != '1'";
                Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(dataSql);
                if (!data.isEmpty() && data.size() > 0) {
                    dto = new XstmDto();
                    dto.setZsm(spzsm);
                    dto.setCd(map.get("QYMC") == null ? "" : String.valueOf(map.get("QYMC")));
                    dto.setCpmc(map.get("PZ") == null ? "" : String.valueOf(map.get("PZ")));
                    dto.setCpxxId(data.get("ID") == null ? "" : String.valueOf(data.get("ID")));
                    dto.setCpzlId(map.get("ID") == null ? "" : String.valueOf(map.get("ID")));
                    dto.setScrq(map.get("CSSJ") == null ? "" : String.valueOf(map.get("CSSJ")));
                    return dto;
                } else {
                    String jdId = UUIDGenerator.uuid();
                    String insertSql = "insert into t_zz_pcjdxx (id,create_user,create_time,is_delete,cspch,jdrq,jrmd,jrsl,jrzl,cpmc) values ('"
                            + jdId + "','"
                            + "','"
                            + (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date()) + "','"
                            + "0','"
                            + spzsm + "','"
                            + (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date()) + "','"
                            + mdId + "','"
                            + "0','"
                            + "0','"
                            + (map.get("PZ") == null ? "" : String.valueOf(map.get("PZ"))) + "')";
                    DatabaseHandlerDao.getInstance().executeSql(insertSql);
                    dto = new XstmDto();
                    dto.setZsm(spzsm);
                    dto.setCd(map.get("QYMC") == null ? "" : String.valueOf(map.get("QYMC")));
                    dto.setCpmc(map.get("PZ") == null ? "" : String.valueOf(map.get("PZ")));
                    dto.setCpxxId(jdId);
                    dto.setCpzlId(map.get("ID") == null ? "" : String.valueOf(map.get("ID")));
                    dto.setScrq(map.get("CSSJ") == null ? "" : String.valueOf(map.get("CSSJ")));
                    return dto;
                }
            }
        }
        return dto;
    }

    /**
     * (12)销售信息更新
     *
     * @param cpxsId 产品销售ID
     * @param cpzlId 产品种类ID
     * @param zl     重量
     * @param sl     数量
     * @return
     */
    public void updateXscp(String cpxsId, String cpzlId, String zl, String sl) {
        if (StringUtil.isEmpty(zl)) zl = "0";
        if (StringUtil.isEmpty(sl)) sl = "0";
        String sql = "select * from t_zz_pcjdxx t where t.id = ? and t.is_delete != '1'";
        Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{cpxsId});
        if (!map.isEmpty() && map.size() > 0) {
            String updateSql = "update t_zz_pcjdxx set jrsl = jrsl+" + Integer.valueOf(sl) + ",jrzl = jrzl+" + Float.valueOf(zl) + " where id ='" + cpxsId + "'";
            DatabaseHandlerDao.getInstance().executeSql(updateSql);
        }
//		String updateSql;
//		//包装产品
//		String sql = "select t.id from t_zz_ccbzcpxx t where t.id = ? and t.pid= ? and t.is_delete != '1'";
//    	List bzList = DatabaseHandlerDao.getInstance().queryForList(sql, new Object[]{cpzlId,cpckId});
//    	if(bzList!=null && bzList.size()>0){
//    		try{
//    			updateSql = "update t_zz_ccbzcpxx set zl = zl+"+Float.valueOf(zl)+",ccjs = ccjs+"+Integer.valueOf(sl)
//				+",kcjs = kcjs -"+Integer.valueOf(sl)+" where id = '"+cpzlId+"' and pid ='"+cpckId+"'";
//				DatabaseHandlerDao.getInstance().executeSql(updateSql);
//				result = true;
//				return result;
//    		}catch(Exception exception){
//    			return result;
//    		}
//    	}
//    	//散货
//    	String shSql = "select t.id from t_zz_ccshxx t where t.id = ? and t.pid= ?  and t.is_delete != '1'";
//    	List shList = DatabaseHandlerDao.getInstance().queryForList(shSql, new Object[]{cpzlId,cpckId});
//    	if(shList!=null && shList.size()>0){
//    		try{
//    			updateSql = "update t_zz_ccshxx set cczl = cczl+"+Float.valueOf(zl)+",kczl = kczl-"+Float.valueOf(zl)+" where id = '"+cpzlId+"' and pid ='"+cpckId+"'";
//    			DatabaseHandlerDao.getInstance().executeSql(updateSql);
//				result = true;
//    		}catch(Exception exception){
//    			return result;
//    		}
//    		return result;
//    	}
//    	
//    	return result;

    }


    /**
     * (13)销售单产品信息查询
     *
     * @param qyId        企业ID
     * @param mdId        门店ID
     * @param pageRequest
     * @return page
     */
    public Page searchXsdcpxx(String qyId, String mdId, PageRequest pageRequest) {
        String rq = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        String sql = "select t.id,t.cpmc,t.cspch,t.jrsl as js,t.jrzl as zl from t_zz_pcjdxx t where t.jrmd = '" + mdId + "' and t.jdrq >='" + rq + " 00:00:00' and t.jdrq <='" + rq + " 23:59:59' and t.is_delete != '1' order by t.create_time desc";
        Page page = FarmCommonUtil.queryPage(pageRequest, sql);

//		//包装产品
//		String sql = "select cc.id, bz.cpzsm,bz.cpmc,bz.zl from t_zz_cdda cd,t_zz_ccgl cc,t_zz_ccbzcpxx bz,t_zz_khxx kh,t_zz_xsddxx dd"
//			+" where dd.ddbh = cc.xsddh and dd.qybm = cd.qybm and bz.pid = cc.id and dd.khbh = kh.khbh";
//		StringBuffer buffer = new StringBuffer(sql);
//		if(StringUtil.isNotEmpty(qyId)){
//			buffer.append(" and cd.id = '").append(qyId).append("'");
//		}
//		if(StringUtil.isNotEmpty(rq)){
//			buffer.append(" and cc.ccsj >= '").append(rq).append(" 00:00:00' and cc.ccsj <= '").append(rq).append(" 23:59:59'");
//		}
//		if(StringUtil.isNotEmpty(khId)){
//			buffer.append(" and kh.id ='").append(khId).append("'");
//		}
//		//添加订单状态为已完成及排序
//		buffer.append(" and cd.is_delete != '1' and cc.is_delete != '1' and bz.is_delete != '1' and kh.is_delete !='1' and dd.is_delete != '1' order by bz.create_time desc");
//		Page bzPage = FarmCommonUtil.getInstance().queryPage(pageRequest, buffer.toString());
//		
//		//散货
//		String shSql = "select cc.id,sh.cpzsm,sh.pz as cpmc,sh.cczl as zl from t_zz_cdda cd,t_zz_ccgl cc,t_zz_ccshxx sh,t_zz_khxx kh,t_zz_xsddxx dd"
//			+" where dd.ddbh = cc.xsddh and dd.qybm = cd.qybm and sh.pid = cc.id and dd.khbh = kh.khbh";
//		buffer.setLength(0);
//		buffer.append(shSql);
//		if(StringUtil.isNotEmpty(qyId)){
//			buffer.append(" and cd.id = '").append(qyId).append("'");
//		}
//		if(StringUtil.isNotEmpty(rq)){
//			buffer.append(" and cc.ccsj >= '").append(rq).append(" 00:00:00' and cc.ccsj <= '").append(rq).append(" 23:59:59'");
//		}
//		if(StringUtil.isNotEmpty(khId)){
//			buffer.append(" and kh.id ='").append(khId).append("'");
//		}
//		buffer.append(" and cd.is_delete != '1' and cc.is_delete != '1' and sh.is_delete != '1' and kh.is_delete !='1' and dd.is_delete != '1' order by sh.create_time desc");
//		Page shPage = FarmCommonUtil.getInstance().queryPage(pageRequest, buffer.toString());
//		List contentList = new ArrayList();
//		List bzContentList = bzPage.getContent();
//		List shContentList = shPage.getContent();
//		//合并
//		if(bzContentList!=null&&bzContentList.size()>0){
//			for(int i=0;i<bzContentList.size();i++){
//				contentList.add(bzContentList.get(i));
//			}
//		}
//		if(shContentList!=null&&shContentList.size()>0){
//			for(int j=0;j<shContentList.size();j++){
//				contentList.add(shContentList.get(j));
//			}
//		}
//		return new PageImpl(contentList);
        return page;
    }

//	/**
//	 * (14)销售单产品信息查询2
//	 * @param xsdid
//	 * @param pageRequest
//	 * @return
//	 */
//	public Page serchXsdcpxx2(String xsdid,PageRequest pageRequest){
//		//包装产品
//		String sql = "select cc.id, bz.cpzsm,bz.cpmc,bz.zl from t_zz_cdda cd,t_zz_ccgl cc,t_zz_ccbzcpxx bz,t_zz_khxx kh,t_zz_xsddxx dd"
//			+" where dd.ddbh = cc.xsddh and dd.qybm = cd.qybm and bz.pid = cc.id and dd.khbh = kh.khbh";
//		StringBuffer buffer = new StringBuffer(sql);
//		if(StringUtil.isNotEmpty(xsdid)){
//			buffer.append(" and dd.id = '").append(xsdid).append("'");
//		}
//		//排序
//		buffer.append(" and cd.is_delete != '1' and cc.is_delete != '1' and bz.is_delete != '1' and kh.is_delete !='1' and dd.is_delete != '1' order by bz.create_time desc");
//		Page bzPage = FarmCommonUtil.getInstance().queryPage(pageRequest, buffer.toString());
//		
//		//散货
//		String shSql = "select cc.id,sh.cpzsm,sh.pz as cpmc,sh.cczl as zl from t_zz_cdda cd,t_zz_ccgl cc,t_zz_ccshxx sh,t_zz_khxx kh,t_zz_xsddxx dd"
//			+" where dd.ddbh = cc.xsddh and dd.qybm = cd.qybm and sh.pid = cc.id and dd.khbh = kh.khbh";
//		buffer.setLength(0);
//		buffer.append(shSql);
//		if(StringUtil.isNotEmpty(xsdid)){
//			buffer.append(" and dd.id = '").append(xsdid).append("'");
//		}
//		buffer.append(" and cd.is_delete != '1' and cc.is_delete != '1' and sh.is_delete != '1' and kh.is_delete !='1' and dd.is_delete != '1' order by sh.create_time desc");
//		Page shPage = FarmCommonUtil.getInstance().queryPage(pageRequest, buffer.toString());
//		List contentList = new ArrayList();
//		List bzContentList = bzPage.getContent();
//		List shContentList = shPage.getContent();
//		//合并
//		if(bzContentList!=null&&bzContentList.size()>0){
//			for(int i=0;i<bzContentList.size();i++){
//				contentList.add(bzContentList.get(i));
//			}
//		}
//		if(shContentList!=null&&shContentList.size()>0){
//			for(int j=0;j<shContentList.size();j++){
//				contentList.add(shContentList.get(j));
//			}
//		}
//		return new PageImpl(contentList);
//	}

    /**
     * (15)销售单产品删除
     *
     * @param ddcpxxIds 订单产品信息
     * @return
     */
    public boolean delXsdcp(String ddcpxxIds) {
        boolean result = false;
        if (StringUtil.isNotEmpty(ddcpxxIds)) {
            String shsql = "update t_zz_pcjdxx t set t.is_delete = '1' where t.id in ('" + ddcpxxIds.replace(",", "'") + "')";
            DatabaseHandlerDao.getInstance().executeSql(shsql);
            result = true;
        }
        return result;
    }

    /**
     * (16)采收单条码扫码查询
     *
     * @param cspch
     * @return
     */
    public CscpDto searchCstm(String cspch) {
        CscpDto dto = null;
        if (StringUtil.isNotEmpty(cspch)) {
            String sql = "select t.id,t.zldj,c.pz,t.create_time as cssj from t_zz_csnzwxq t,t_zz_csgl c  where c.id = t.pid and t.pch = ? and t.is_delete != '1' and c.is_delete != '1'";
            Map<String, Object> data = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{cspch});
            dto = new CscpDto();
            dto.setCsId(data.get("ID") == null ? "" : String.valueOf(data.get("ID")));
            dto.setCpmc(data.get("ZLDJ") == null ? "" : String.valueOf(data.get("ZLDJ")));
            dto.setCpmc(data.get("PZ") == null ? "" : String.valueOf(data.get("PZ")));
            dto.setCssj(data.get("CREATE_TIME") == null ? "" : String.valueOf(data.get("CSSJ")));
        }
        return dto;
    }

    /**
     * (17)包装秤查询
     *
     * @param qybm
     * @param pageRequest
     * @return
     */
    public Page searchBzc(String qybm, PageRequest pageRequest) {
        if (StringUtil.isEmpty(qybm)) {
            return null;
        }
        //查询称
//		String sql = "select t.id,t.xh from t_qypt_sbgl t,t_zz_cdda cd where t.qybm = cd.qybm and t.lb = 'C' and cd.qybm ='"+qybm+"' and cd.is_delete != '1' and cd.is_delete != '1'";
        String sql = "select * from t_qypt_sbgl t where t.is_delete = '0' and t.lb = 'C' and t.dwbm = '" + qybm + "'";
        Page page = FarmCommonUtil.queryPage(pageRequest, sql);
        return page;
    }

    /**
     * (18)包装秤绑定
     *
     * @param csId
     * @param cId
     * @return
     */
    public boolean bzcbd(String csId, String cId) {
        boolean result = false;
        try {
            String sql = "insert into t_zz_bzcbd (id,create_user,create_time,is_delete,sbid,csid) values ('"
                    + UUIDGenerator.uuid() + "','"
                    + "','"
                    + (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date()) + "','"
                    + "0','"
                    + cId + "','"
                    + csId + "')";
            DatabaseHandlerDao.getInstance().executeSql(sql);
            result = true;
        } catch (Exception exception) {

        }
        return result;
    }

    /**
     * 根据采收批次号获取
     *
     * @param pch 采收批次号
     * @return
     */
    public List queryCpxxByPch(String pch,String qybm) {
        String sql = "SELECT G.CPBH,G.CPMC,G.GG||'kg' as gg,C.PCH\n" +
                "  FROM T_ZZ_CSGL T, T_ZZ_CSNZWXQ C, T_ZZ_CPXXGLPLXX P, T_ZZ_CPXXGL G\n" +
                " WHERE T.ID = C.PID\n" +
                "   AND G.BZXS='SZXLH' AND T.PZBH = P.PZBH\n" +
                "   AND T.QYBM =? AND G.QYBM=?" +
                "   AND P.PID = G.ID\n" +
                "   AND C.PCH= ?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{qybm,qybm,pch});
    }

    /**
     * 通过称ID查询称信息
     *
     * @param cId
     * @return
     */
    public Map queryCxxByCid(String cId) {
        String sql = "SELECT T.ID,T.DWBM,T.CCBH,T.BH FROM T_QYPT_SBGL T WHERE T.ID= ?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{cId});
    }

    /**
     * 查询产品信息
     *
     * @param cpbh 产品编号
     * @param qybm 门店编码
     * @return
     */
    public Map queryCpxxByCpbh(String cpbh, String qybm) {
        String sql = "SELECT T.CPBH,T.CPMC,X.SJMC AS BZXS,T.DJ,C.SJMC AS CPDJ,T.GG,Z.QYMC FROM T_ZZ_CPXXGL T\n" +
                "LEFT JOIN T_QYPT_ZHGL Z ON T.QYBM = Z.ZHBH  LEFT JOIN T_COMMON_SJLX_CODE C\n" +
                " ON T.DJ = C.SJBM AND C.LXBM='DJ'  LEFT JOIN T_COMMON_SJLX_CODE X ON T.BZXS = X.SJBM AND X.LXBM='BZXS' " +
                "  WHERE T.CPBH=? AND T.QYBM=?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{cpbh, qybm});
    }

    /**
     * 查询采收信息
     *
     * @param cspch 采收批次号
     * @param qybm  企业编码
     * @return
     */
    public Map queryCsxxByCspch(String cspch, String qybm) {
        String sql = "SELECT T.CSLSH,C.PCH,T.QVMC,T.DKBH,T.DKMC,T.PZBH,T.PZ FROM T_ZZ_CSGL T,T_ZZ_CSNZWXQ C " +
                " WHERE T.ID = C.PID AND C.PCH=? AND T.QYBM=?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{cspch, qybm});
    }

    /**
     * 保存预包装信息
     *
     * @param cspch 采收批次号
     * @param cpbh  产品编号
     * @param qybm  企业编码
     * @param cId   称ID
     * @return boolean
     */
    @Transactional
    public boolean saveYbzxx(String cspch, String cpbh, String qybm, String cId) {
        boolean result = false;
        Map cpxx = queryCpxxByCpbh(cpbh, qybm);
        Map csxx = queryCsxxByCspch(cspch, qybm);
        if (cpxx == null || cpxx.isEmpty() || csxx == null || csxx.isEmpty()) {
            return false;
        }
        String bzlsh = SerialNumberUtil.getInstance().getSerialNumberByQybm("ZZ", qybm, "ZZBZLSH", false);
        String cpzsm = qybm + SerialNumberUtil.getInstance().getSerialNumberByQybm("ZZ", qybm, "ZZCPZSM", false);
//        String bzpczsm = qybm + SerialNumberUtil.getInstance().getSerialNumberByQybm("ZZ", qybm, "ZZCPZSM", false);
        try {
            String zbId = UUIDGenerator.uuid();
            String sql = "INSERT INTO T_ZZ_BZGL(ID,BZLSH,QYBM,QYMC,CPZSM,CPMC,CPBH,BZXS,CPDJ,BZGG,BZSJ,IS_OUT) VALUES('"
                    + zbId + "','" + bzlsh + "','" + qybm + "','" + cpxx.get("QYMC") + "','" + cpzsm + "','" + cpxx.get("CPMC") + "','"
                    + cpbh + "','" + cpxx.get("BZXS") + "','" + cpxx.get("CPDJ") + "','" + cpxx.get("GG") + "','"
                    + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + "','" + "0')";
            DatabaseHandlerDao.getInstance().executeSql(sql);
            sql = "INSERT INTO T_ZZ_BZGLPLXX(ID,CSLSH,CSPCH,QYMC,DKBH,PZ,PZBH,QYBM,PID) VALUES('"
                    + UUIDGenerator.uuid() + "','" + csxx.get("CSLSH") + "','" + csxx.get("PCH") + "','"
                    + csxx.get("QVMC") + "','" + csxx.get("DKBH") + "','" + csxx.get("PZ") + "','" + csxx.get("PZBH") + "','"
                    + qybm + "','" + zbId + "')";
            DatabaseHandlerDao.getInstance().executeSql(sql);
            createTxt(cId, cpxx, cpzsm);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 下传TXT
     *
     * @param cId
     * @param cpxx
     * @param zsm
     */
    public void createTxt(String cId, Map cpxx, String zsm) {
        String content = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + "\t" + zsm + "\t" + cpxx.get("CPBH") +
                "\t" + cpxx.get("CPDJ") + "\t" + cpxx.get("QYMC");
        String[] cIds = cId.split(",");
        File file = new File(readTxtProp());
        if  (!file .exists()  && !file .isDirectory()){
            file .mkdir();
        }
        for (String id : cIds) {
            Map cxx = queryCxxByCid(id);
            String fileName = cxx.get("DWBM") + "_" + cxx.get("CCBH") + "_" + cxx.get("BH") + "_" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + ".txt";
            File newFile = new File(readTxtProp() + "/"+fileName);
            try {
                TxtUtils.writeTxtFile(content, newFile, null);
                TxtUtils.createFile(newFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取txt.properties里的输出url
     *
     * @return
     */
    public String readTxtProp() {
        Properties prop = new Properties();
        try {
            InputStream in = new FileInputStream(ComponentFileUtil.getConfigPath() + "trace/txt.properties");
            prop.load(in);
            String url = prop.getProperty("downUrl").trim();
            return url;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

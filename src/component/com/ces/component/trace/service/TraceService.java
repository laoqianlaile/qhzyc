package com.ces.component.trace.service;

import ces.sdk.system.bean.OrgUserInfo;
import ces.sdk.system.bean.SystemInfo;
import ces.sdk.system.dao.OrgUserInfoDao;
import ces.sdk.system.facade.RoleInfoFacade;
import ces.sdk.system.facade.SystemFacade;
import ces.sdk.system.factory.SdkDaoFactory;
import ces.sdk.system.factory.SystemFacadeFactory;
import com.ces.component.aquatic.utils.ImageUtil;
import com.ces.component.trace.dao.TraceDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.TableNameUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ComponentFileUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.web.listener.XarchListener;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.*;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static com.ces.component.farm.utils.FarmCommonUtil.queryPage;

/**
 * Created by 黄翔宇 on 15/5/4.
 */
@Component
public class TraceService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceDao> {

    @Override
    @Autowired
    public void setDao(TraceDao dao) {
        super.setDao(dao);
    }


    /**
     * 登陆页面处理
     */
    public Map<String,String> getAllSystemByUser(){
        String userId = CommonUtil.getCurrentUserId();
        RoleInfoFacade roleInfoFacade = CommonUtil.getRoleInfoFacade();
        SystemFacade systemFacade = SystemFacadeFactory.newInstance().createSystemFacade();
        //返回值
        List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
        if (CommonUtil.isSuperRole()) {//超级管理员给予企业平台后台的访问权限
            Map<String, String> data = new HashMap<String, String>();
            Map<String, String> param = new HashMap<String, String>();
            param.put("name", "平台管理系统");
            SystemInfo system = systemFacade.findByCondition(param).get(0);
            data.put("sysShowName", "中信(" + system.getName() + ")");
            data.put("sysName", system.getName());
            data.put("sysCode", system.getCode());
            data.put("companyCode", "1");
            ServletActionContext.getRequest().getSession().setAttribute("_companyCode_", data.get("companyCode"));
            System.out.printf("getCompanyCode获取1: " + ServletActionContext.getRequest().getSession().getAttribute("_companyCode_"));
            return data;
        }
        Map<String, Set<String>> checkedSystemAndRoleByUserId = roleInfoFacade.findCheckedSystemAndRoleByUserId(userId);
        Set<String> systemIds = checkedSystemAndRoleByUserId.get("systemIds");
        List<SystemInfo> systems = new ArrayList<SystemInfo>();
        //用户所拥有的系统
        for (String systemId : systemIds) {
            if (!"1".equals(systemId)) {
                systems.add(systemFacade.findByID(systemId));
            }
        }
        //用户管理的门店
        List<Map<String, Object>> userAdminOrgs = getUserAdminOrgs();
        Map<String, String> data = null;
        //将门店与系统对应
        for (Map<String, Object> userAdminOrg : userAdminOrgs) {
            String mdbh = String.valueOf(userAdminOrg.get("ZHBH"));//企业平台的帐户编号（门店编号）
            String mdmc = String.valueOf(userAdminOrg.get("QYMC"));//企业平台的帐户编号（门店编号）
            String authId = String.valueOf(userAdminOrg.get("AUTH_ID"));//管理平台的orgId
            Map<String, Set<String>> checkedSystemAndRoleByOrgId = roleInfoFacade.findCheckedSystemAndRoleByOrgId(authId);
            Set<String> checkedSystems = checkedSystemAndRoleByOrgId.get("systemIds");
            for (String systemId : systemIds) {
                if (checkedSystems.contains(systemId)) {
                    data = new HashMap<String, String>();
                    SystemInfo system = systemFacade.findByID(systemId);
                    data.put("sysShowName", mdmc + "(" + system.getName() + ")");
                    data.put("sysName", system.getName());
                    data.put("sysCode", system.getCode());
                    data.put("companyCode", mdbh);
                    datas.add(data);
                }
            }
        }
        if (datas.size() > 1)  {
            data = new HashMap<String, String>();
            data.put("forward", "trace");
            return data;
        }
        ServletActionContext.getRequest().getSession().setAttribute("_companyCode_", data.get("companyCode"));
        System.out.printf("getCompanyCode获取2: " + ServletActionContext.getRequest().getSession().getAttribute("_companyCode_"));
        System.out.printf("getCompanyCode获取2id: " + ServletActionContext.getRequest().getSession().getId());
        System.out.printf("");
        return datas.get(0);
    }




    /**
     * 根据系统编码初始话企业档案
     *
     * @param sysCode
     */
    public void initQyda(String sysCode, String companyCode) {
        //获取对应的菜单
        Menu menu = XarchListener.getBean(MenuService.class).getByID(sysCode);
        String tableName = TableNameUtil.getQydaTableName(menu.getCode());
        if (tableName == null) {//是企业平台,或城市管理
            return;
        }
        //查询企业信息
        String querySql = "select * from t_qypt_zhgl t where t.auth_id = (select t.auth_parent_id from t_qypt_zhgl t where t.zhbh = '" + companyCode + "')";
        Map<String, Object> qyxx = DatabaseHandlerDao.getInstance().queryForMap(querySql);
        //查询子系统档案是否已存在信息
        String sql = "select * from " + tableName + " t where t.qybm = ?";
        List<Map<String, Object>> qydas = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode});
        //查询门店信息
        querySql = "select * from t_qypt_zhgl t where t.zhbh = ?";
        Map<String, Object> mdxx = DatabaseHandlerDao.getInstance().queryForMap(querySql, new Object[]{companyCode});
        //同步的数据
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put(TableNameUtil.getLsxzqhdmColumnName(menu.getCode()), String.valueOf(mdxx.get("CSXX")));
        dataMap.put(TableNameUtil.getLsxzqColumnName(menu.getCode()), String.valueOf(mdxx.get("CSMC")));
        if (!qydas.isEmpty()) {//存在该企业的编码，更新行政区划代码
            dataMap.put(AppDefineUtil.C_ID, String.valueOf(qydas.get(0).get(AppDefineUtil.C_ID)));
            save(tableName, dataMap, null);
//            return;
        }
        dataMap.put("qybm", companyCode);
        dataMap.put("qymc", String.valueOf(qyxx.get("QYMC")));
        dataMap.put(TableNameUtil.getMdmcColumnName(menu.getCode()), String.valueOf(mdxx.get("QYMC")));
        if ("ZZ".equalsIgnoreCase(menu.getCode())) {//种植同步单位类型
            dataMap.put("DWLX", String.valueOf(mdxx.get("XTLX")));
        }
        dataMap.remove("QYMC");
        save(tableName, dataMap, null);
    }

    /**
     * 获取用户所管理的企业或门店列表
     *
     * @return
     */
    public List<Map<String, Object>> getUserAdminOrgs() {
        OrgUserInfoDao orgUserInfoDao = SdkDaoFactory.createOrgUserDao();
        Map<String, String> param = new HashMap<String, String>();
        param.put("userId", CommonUtil.getCurrentUserId());
        List<OrgUserInfo> orgUserInfos = orgUserInfoDao.findByCondition(param);
        StringBuffer sql = new StringBuffer();
        sql.append("select * from t_qypt_zhgl t where t.auth_id in (");
        for (OrgUserInfo orgUserInfo : orgUserInfos) {
            sql.append("'").append(orgUserInfo.getOrgId()).append("',");
        }
        sql = sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        return DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
    }

    private static String SPZS_FJLJB = "spzstpfj";

    /**
     * 上传附件图片
     *
     * @param masterTableId
     * @param fileName
     * @param file
     * @param fjlx
     * @throws IOException
     */
    public void upload(String masterTableId, String masterDataId, String fjlx, String fileName, File file, boolean isOne) throws IOException {
        String sql = "select t.id, t.show_name, t.table_name from t_xtpz_physical_table_define t " +
                " where  t.logic_table_code='" + SPZS_FJLJB + "' and (" +
                "exists(select r.id from t_xtpz_table_relation r where r.relate_table_id=t.id and r.table_id=?) or " +
                "exists(select r.id from t_xtpz_table_relation r where r.table_id=t.id and r.relate_table_id=?)" +
                ") ";
        Map<String, Object> data = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{masterTableId, masterTableId});
        String slaveTableName = String.valueOf(data.get("TABLE_NAME"));
        Map<String, String> slaveData = new HashMap<String, String>();
        if (slaveTableName == null || "".equals(slaveTableName) || "null".equals(slaveTableName) || "NULL".equals(slaveTableName)) {
            slaveTableName = getTableName(masterTableId);
            if("T_ZZ_CZGFGL".equals(slaveTableName)){
                slaveData.put("SCR", CommonUtil.getUser().getUsername());
                slaveData.put("CZGFMC", fileName);
                slaveData.put("QYBM", SerialNumberUtil.getInstance().getCompanyCode());
            }
        }
        if (!isOne) {//如果是控制上传一个附件
            sql = "select TPBCMC from " + slaveTableName + " where zbid ='" + masterDataId + "'";
            List<Map<String, Object>> mapList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
            //如果存在就删除，重新保存新的附件
            if (mapList != null && !mapList.isEmpty()) {
                for (Map<String, Object> map : mapList) {
                    File dfile = new File(ComponentFileUtil.getProjectPath() + "/spzstpfj/" + map.get("TPBCMC"));
                    if (dfile.exists()) {
                        FileUtils.deleteQuietly(dfile);
                    }
                }
                //在删除表中记录
                sql = " delete from " + slaveTableName + " where zbid ='" + masterDataId + "'";
                DatabaseHandlerDao.getInstance().executeSql(sql);
            }

        }
        String ext = getExt(fileName);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String tpbcmc = date.getTime() + "." + ext;
        FileUtils.copyFile(file, new File(ComponentFileUtil.getProjectPath() + "/spzstpfj/" + tpbcmc));
        //缩略图
        if(!"T_ZZ_CZGFGL".equals(slaveTableName)){
            ImageUtil.compressPic(file, new File(ComponentFileUtil.getProjectPath() + "/spzstpfj/thumb/" + tpbcmc), 180.0, 0);
            slaveData.put("SCTPDX", String.valueOf(FileUtils.sizeOf(file)));
        }
        slaveData.put("SCTPMC", fileName);
        slaveData.put("SCTPGS", ext);
        slaveData.put("TPBCMC", tpbcmc);
        slaveData.put("ZBID", masterDataId);
        slaveData.put("FJLX", StringUtil.isEmpty(fjlx) ? "0" : fjlx);
        slaveData.put("SCSJ", simpleDateFormat.format(date));
        save(slaveTableName, slaveData, null);
    }

    /**
     * 获取文件名的后缀
     *
     * @param fileName
     * @return
     */
    private String getExt(String fileName) {
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        return ext;
    }


    /**
     * 获取平台管理系统——企业申请统计数据
     */
    public Object getQysqtj(String csrq, String jzrq) {
        String sql = "";
        if ("week".equals(csrq)) {
            sql = "select substr(t.sqsj,0,10) as rq,count(*) as sqsl" +
                    " from t_qypt_zhsqgl t" +
                    " where substr(t.sqsj,0,10) <= to_char(sysdate,'yyyy-mm-dd')" +
                    " and substr(t.sqsj,0,10) > to_char(sysdate-7,'yyyy-mm-dd')" +
                    " group by substr(t.sqsj,0,10)" +
                    " order by rq ";
        } else if ("month".equals(csrq)) {
            sql = "select substr(t.sqsj,0,10) as rq,count(*) as sqsl" +
                    " from t_qypt_zhsqgl t" +
                    " where substr(t.sqsj,0,10) <= to_char(sysdate,'yyyy-mm-dd')" +
                    " and substr(t.sqsj,0,10) > to_char(sysdate-30,'yyyy-mm-dd')" +
                    " group by substr(t.sqsj,0,10)" +
                    " order by rq ";
        } else {
            sql = "select substr(t.sqsj,0,10) as rq,count(*) as sqsl" +
                    " from t_qypt_zhsqgl t" +
                    " where substr(t.sqsj,0,10) <= '" + jzrq + "'" +
                    " and substr(t.sqsj,0,10) >= '" + csrq + "'" +
                    " group by substr(t.sqsj,0,10)" +
                    " order by rq ";
        }
        List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return list;
    }

    /**
     * 获取平台管理系统——企业新增统计数据
     */
    public Object getQyxztj(String csrq, String jzrq) {
        String sql = "";
        if ("week".equals(csrq)) {
            sql = "select substr(t.zhcjsj,0,10) as rq,count(*) as xzsl" +
                    " from t_qypt_zhgl t" +
                    " where substr(t.zhcjsj,0,10) <= to_char(sysdate,'yyyy-mm-dd')" +
                    " and substr(t.zhcjsj,0,10) > to_char(sysdate-7,'yyyy-mm-dd')  and t.auth_parent_id = '-1'" +
                    " group by substr(t.zhcjsj,0,10)" +
                    " order by rq ";
        } else if ("month".equals(csrq)) {
            sql = "select substr(t.zhcjsj,0,10) as rq,count(*) as xzsl" +
                    " from t_qypt_zhgl t" +
                    " where substr(t.zhcjsj,0,10) <= to_char(sysdate,'yyyy-mm-dd')" +
                    " and substr(t.zhcjsj,0,10) > to_char(sysdate-30,'yyyy-mm-dd')  and t.auth_parent_id = '-1'" +
                    " group by substr(t.zhcjsj,0,10)" +
                    " order by rq ";
        } else {
            sql = "select substr(t.zhcjsj,0,10) as rq,count(*) as xzsl" +
                    " from t_qypt_zhgl t" +
                    " where substr(t.zhcjsj,0,10) <= '" + jzrq + "'" +
                    " and substr(t.zhcjsj,0,10) >= '" + csrq + "'  and t.auth_parent_id = '-1'" +
                    " group by substr(t.zhcjsj,0,10)" +
                    " order by rq ";
        }
        List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return list;
    }

    /**
     * 企业编码和子系统档案的门店名称
     *
     * @param sysName
     * @return
     */
    public Map<String, String> getQybmAndQymc(String sysName) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String companyName = CompanyInfoUtil.getInstance().getCompanyName(sysName, companyCode);
        Map<String, String> map = new HashMap<String, String>();
        map.put("qybm", companyCode);
        map.put("qymc", companyName);
        return map;
    }
    /**
     * 获取数据字典编码数据
     *
     * @param lxbm
     * @return
     */
    public List<Map<String, Object>> getXtpzcode(String lxbm) {
        String sql = "select  value,name as text from t_xtpz_code  where code_type_code = ? order by show_order";
        List<Map<String, Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{lxbm});
        for (Map<String, Object> map : maps) {
            map.put("value", map.get("VALUE"));
            map.put("text", map.get("TEXT"));
        }
        return maps;
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
        for (Map<String, Object> map : maps) {
            map.put("value", map.get("VALUE"));
            map.put("text", map.get("TEXT"));
        }
        return maps;
    }

    public Object getZzdplOrxpzxx(String plbh) {
        //1.数据权限过滤   2.是否排序  3.状态是否判断
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from (select t.PL,t.PLBH from T_ZZ_DPZXX  t where t.qybm='" + qybm + "' ";
        if (!"".equals(plbh) && null != plbh) {
            sql += " and plbh='" + plbh + "'";
        }
        sql += ")";
        List<Object[]> list = DatabaseHandlerDao.getInstance().queryForList(sql);
        List<Code> dataList = new ArrayList<Code>();
        int i = 0, len = list.size();
        Code code = null;
        for (; i < len; i++) {
            Object[] dataMap = list.get(i);
            code = new Code();
            code.setValue(dataMap[1].toString());
            code.setName(dataMap[0].toString());
            code.setShowOrder(i + 1);
            dataList.add(code);
        }
        return dataList;
    }

    public Object getXplData(String plbh) {
        //1.数据权限过滤   2.是否排序  3.状态是否判断
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from (select t.PZ,t.PZBH from T_ZZ_XPZXX  t where t.qybm='" + qybm + "' ";
        if (!"".equals(plbh) && null != plbh) {
            sql += " and plbh='" + plbh + "'";
        }
        sql += ")";
        List<Object[]> list = DatabaseHandlerDao.getInstance().queryForList(sql);
        List<Code> dataList = new ArrayList<Code>();
        int i = 0, len = list.size();
        Code code = null;
        for (; i < len; i++) {
            Object[] dataMap = list.get(i);
            code = new Code();
            code.setValue(dataMap[1].toString());
            code.setName(dataMap[0].toString());
            code.setShowOrder(i + 1);
            dataList.add(code);
        }
        return dataList;
    }


    public Object canDelete(String mk, String ids) {
        StringBuffer sql;
        sql = new StringBuffer();
        String id[] = ids.split(",");
        if ("JDXX".equals(mk)) {
            //基地信息
            sql.append("select * from t_zz_jdxx a where  a.id in('!'");
            for (int i = 0; i < id.length; i++) {
                sql.append(",'" + id[i] + "'");
            }
            sql.append(") and exists(select 1 from t_zz_qyxx b where a.jdbh = b.jdbh and a.qybm = b.qybm and b.is_delete <> '1')");
        } else if ("GZRYXX".equals(mk)) {
            //工作人员
            sql.append("select * from t_zz_gzryda a where a.id in('!'");
            for (int i = 0; i < id.length; i++) {
                sql.append(",'" + id[i] + "'");
            }
            sql.append(") and (exists(select 1 from t_zz_jdxx b where a.gzrybh = b.fzrbh and a.qybm = b.qybm and b.is_delete <> '1') or exists(select 1 from t_zz_qyxx c where a.gzrybh = c.fzrbh and a.qybm = c.qybm and c.is_delete <> '1') or exists(select 1 from t_zz_dy d where a.gzrybh = d.glybh and a.qybm = d.qybm and d.is_delete <> '1') or exists(select 1 from t_zz_dkxx e where a.gzrybh = e.dkfzrbh and a.qybm = e.qybm and e.is_delete <> '1') or exists(select 1 from T_ZZ_TRPCKJBXX g where a.gzrybh = g.sjlyrbh and a.qybm = g.qybm and g.is_delete <> '1'))");
        } else if ("CPGL".equals(mk)) {
            //产品管理
            sql.append("select * from t_zz_cpxxgl a where a.id in('!'");
            for (int i = 0; i < id.length; i++) {
                sql.append(",'" + id[i] + "'");
            }
            sql.append(") and (exists(select 1 from t_zz_bzgl b where a.cpbh = b.cpbh and a.qybm = b.qybm and b.is_delete <> '1') or exists(select 1 from t_zz_xsddxx c,t_zz_ddcpxx d where c.id = d.pid and a.qybm = c.qybm and a.cpbh = d.cpmc and d.is_delete <> '1') or exists(select 1 from t_zz_ccgl e,t_zz_ccbzcpxx f where e.id = f.pid and a.qybm = e.qybm and a.cpbh = f.cpbh and f.is_delete <> '1'))");
        } else if ("SCDA".equals(mk)) {
            //生产档案
            sql.append("select * from(select * from (" +
                    " select a.id,a.scdabh,a.qybm from t_zz_scda a left join t_zz_scbz b on a.id = b.pid where b.pid <> '1' and b.czsj is not null" +
                    " union select a.id,a.scdabh,a.qybm from t_zz_scda a left join t_zz_scgg b on a.id = b.pid where b.pid <> '1' and b.czsj is not null" +
                    " union select a.id,a.scdabh,a.qybm from t_zz_scda a left join t_zz_scsf b on a.id = b.pid where b.pid <> '1' and b.czsj is not null" +
                    " union select a.id,a.scdabh,a.qybm from t_zz_scda a left join t_zz_scyy b on a.id = b.pid where b.pid <> '1' and b.czsj is not null" +
                    " union select a.id,a.scdabh,a.qybm from t_zz_scda a left join t_zz_sccs b on a.id = b.pid where b.pid <> '1' and b.czsj is not null" +
                    " union select a.id,a.scdabh,a.qybm from t_zz_scda a left join t_zz_sccc b on a.id = b.pid where b.pid <> '1' and b.czsj is not null" +
                    " union select a.id,a.scdabh,a.qybm from t_zz_scda a left join t_zz_scqt b on a.id = b.pid where b.pid <> '1' and b.czsj is not null) x" +
                    " union select a.id,a.scdabh,a.qybm from t_zz_scda a" +
                    " where (exists(select 1 from t_zz_csgl z where a.scdabh = z.scdabh and a.qybm = z.qybm and z.is_delete <> '1')))y" +
                    " where y.id in(");
            for (int i = 0; i < id.length; i++) {
                sql.append("'" + id[i] + "'");
                if(i < (id.length -1)){
                    sql.append(",");
                }
            }
            sql.append(")");
        } else if ("BZGL".equals(mk)) {
            //包装管理
            sql.append("select a.id,a.bzlsh from t_zz_bzgl a,t_zz_bzgldymx b where a.id = b.pid and a.id in('!'");
            for (int i = 0; i < id.length; i++) {
                sql.append(",'" + id[i] + "'");
            }
            sql.append(") group by a.id,a.bzlsh");
        }
        List<Map<String,Object>> resultList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
        return resultList;
    }

    /**
     * 是否重复IC卡编号
     *
     * @param ickbh IC卡编号
     * @return
     */
    public Object isRepeatIckbh(String ickbh) {
        String sql = "select * from t_zz_gzryda a, t_zz_dkxx b where (a.ickbh = '" + ickbh + "' and a.is_delete <> '1') or (b.ickbh = '" + ickbh + "' and b.is_delete <> '1')";
        List<Map<String, Object>> listData = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return listData;
    }

    /**
     * 校验面积
     *
     * @param mj 当前模块面积
     * @return
     */
    public Object checkMj(String mj, String mk, String bh, String sbh) {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String sql = "";
        dataMap.put("result", "SUCCESS");
        try {
            Double.parseDouble(mj);
        } catch (Exception e) {
            dataMap.put("result", "ERROR");
            dataMap.put("msg", "面积输入错误，请重新输入！");
        }
        if (!dataMap.get("result").equals("ERROR")) {
            if ("qy".equals(mk)) {
                sql = "select avg(a.jdmj) as zmj,sum(b.qymj) as fmj from t_zz_jdxx a,t_zz_qyxx b where a.jdbh = b.jdbh and a.qybm = ? and b.qybm = a.qybm and a.jdbh = ? and a.is_delete <> '1' and b.is_delete <> '1' and b.qybh != '" + sbh + "'";
                dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{qybm, bh});
                if("null".equals(String.valueOf(dataMap.get("ZMJ"))) && "null".equals(String.valueOf(dataMap.get("FMJ")))){
                    sql = "select avg(a.jdmj) as zmj from t_zz_jdxx a where a.qybm = ? and a.jdbh = ? and a.is_delete <> '1'";
                    dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{qybm, bh});
                    if("null".equals(String.valueOf(dataMap.get("ZMJ")))){
                        dataMap.put("msg", "请先录入基地面积");
                        dataMap.put("result", "ERROR");
                    }else if (Double.parseDouble(mj) > Double.parseDouble(String.valueOf(dataMap.get("ZMJ")))) {
                        double tem = Double.parseDouble(String.valueOf(dataMap.get("ZMJ")));
                        dataMap.put("msg", "可使用面积为"+tem+"亩");
                        dataMap.put("result", "ERROR");
                    }
                }else if (Double.parseDouble(mj) + Double.parseDouble(String.valueOf(dataMap.get("FMJ"))) > Double.parseDouble(String.valueOf(dataMap.get("ZMJ")))) {
                    BigDecimal tem = (new BigDecimal(String.valueOf(dataMap.get("ZMJ")))).subtract(new BigDecimal(String.valueOf(dataMap.get("FMJ"))));
                    dataMap.put("msg","可使用面积为"+tem+"亩");
                    dataMap.put("result", "ERROR");
                }
            } else if ("dk".equals(mk)) {
                sql = "select avg(a.qymj) as zmj,sum(b.mj) as fmj from t_zz_qyxx a,t_zz_dkxx b where a.qybh = b.qybh and a.qybm = ? and b.qybm = a.qybm and a.qybh = ? and a.is_delete <> '1' and b.is_delete <> '1' and b.dkbh != '" + sbh + "'";
                dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{qybm, bh});
                //
                if("null".equals(String.valueOf(dataMap.get("ZMJ"))) && "null".equals(String.valueOf(dataMap.get("FMJ")))){
                    sql = "select avg(a.qymj) as zmj from t_zz_qyxx a where a.qybm = ? and a.qybh = ? and a.is_delete <> '1'";
                    dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{qybm, bh});
                    if("null".equals(String.valueOf(dataMap.get("ZMJ")))){
                        dataMap.put("msg", "请先录入区域面积");
                        dataMap.put("result", "ERROR");
                    }else if (Double.parseDouble(mj) > Double.parseDouble(String.valueOf(dataMap.get("ZMJ")))) {
                        double tem = Double.parseDouble(String.valueOf(dataMap.get("ZMJ")));
                        dataMap.put("msg", "可使用面积为"+tem+"亩");
                        dataMap.put("result", "ERROR");
                    }
                }else if (Double.parseDouble(mj) + Double.parseDouble(String.valueOf(dataMap.get("FMJ"))) > Double.parseDouble(String.valueOf(dataMap.get("ZMJ")))) {
                    BigDecimal tem = (new BigDecimal(String.valueOf(dataMap.get("ZMJ")))).subtract(new BigDecimal(String.valueOf(dataMap.get("FMJ"))));
                    dataMap.put("msg", "可使用面积为"+tem+"亩");
                    dataMap.put("result", "ERROR");
                }
            } else if ("dy".equals(mk)) {
                sql = "select avg(a.mj) as zmj,sum(b.zzdymj) as fmj from t_zz_dkxx a,t_zz_dy b where a.dkbh = b.dkbh and a.qybm = ? and b.qybm = a.qybm and a.dkbh = ? and a.is_delete <> '1' and b.is_delete <> '1' and b.zzdybh != '" + sbh + "'";
                dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{qybm, bh});
                if("null".equals(String.valueOf(dataMap.get("ZMJ"))) && "null".equals(String.valueOf(dataMap.get("FMJ")))){
                    sql = "select avg(a.mj) as zmj from t_zz_dkxx a where a.qybm = ? and a.dkbh = ? and a.is_delete <> '1'";
                    dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{qybm, bh});

                    if("null".equals(String.valueOf(dataMap.get("ZMJ")))){
                        dataMap.put("msg", "请先录入地块面积");
                        dataMap.put("result", "ERROR");
                    }else if (Double.parseDouble(mj) > Double.parseDouble(String.valueOf(dataMap.get("ZMJ")))) {

                        double tem = Double.parseDouble(String.valueOf(dataMap.get("ZMJ")));
                        dataMap.put("msg", "可使用面积为"+tem+"亩");
                        dataMap.put("result", "ERROR");
                    }
                }else if (Double.parseDouble(mj) + Double.parseDouble(String.valueOf(dataMap.get("FMJ"))) > Double.parseDouble(String.valueOf(dataMap.get("ZMJ")))) {

                    //可用地块面积
                    BigDecimal tem = (new BigDecimal(String.valueOf(dataMap.get("ZMJ")))).subtract(new BigDecimal(String.valueOf(dataMap.get("FMJ"))));
                    dataMap.put("msg","可使用面积为"+tem+"亩");
                    dataMap.put("result", "ERROR");
                }
            }
        }
        return dataMap;
    }


    /**
     * 根据类型查询预估时间内的农事项
     * @return
     */
    public Object getYgNsx(String lx){
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String sDate = sdf.format(date);
        //String sql = "SELECT A.DKMC,A.ZZDYMC,B.* FROM T_ZZ_SCDA A,T_ZZ_" + lx + " B WHERE  A.QYBM = '" + qybm + "' AND A.ID = B.PID AND TO_DATE(B.YGSJ1,'yyyy-mm-dd') <= TO_DATE('" + sDate + "','yyyy-mm-dd') AND TO_DATE(B.YGSJ2,'yyyy-mm-dd') >= TO_DATE('" + sDate + "','yyyy-mm-dd') AND A.IS_DELETE <> '1' AND B.IS_DELETE <> '1'";
        String sql = "SELECT A.DKMC,A.ZZDYMC,B.* FROM T_ZZ_SCDA A,T_ZZ_" + lx + " B WHERE  A.QYBM = '" + qybm + "' AND A.ID = B.PID AND TO_DATE(B.YGSJ1,'yyyy-mm-dd') <= TO_DATE('" + sDate + "','yyyy-mm-dd') AND TO_DATE(B.YGSJ2,'yyyy-mm-dd') >= TO_DATE('" + sDate + "','yyyy-mm-dd') AND A.IS_DELETE <> '1' AND B.IS_DELETE <> '1' and B.id not in (SELECT B.ID FROM T_ZZ_SCDA A, T_ZZ_"+lx+" B,T_ZZ_CZJL C WHERE A.ID=B.PID AND B.ID=C.PID AND A.QYBM= '"+qybm+"' AND C.JSSJ IS NOT NULL   )";

        List<Map<String,Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",dataList);
        return dataMap;
    }


    /**
     * 统计分拣与包装
     * @return
     */
    public Object getCount(){
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        Map<String,Object> dataMap = new HashMap<String, Object>();
        String sql = "select count(*) as fjtj from t_zz_csgl t where t.qybm = '" + qybm + "' and t.is_delete <> '1'";
        dataMap.put("FJTJ",DatabaseHandlerDao.getInstance().queryForMap(sql).get("FJTJ"));
        sql = "select count(*) as fjtj from t_zz_bzgl t where t.qybm = '" + qybm + "' and t.is_delete <> '1'";
        dataMap.put("BZTJ",DatabaseHandlerDao.getInstance().queryForMap(sql).get("BZTJ"));
        return dataMap;
    }

    public Object getParentId(String pid,String lx){
        String sql="select pid from t_zz_"+lx+" where id ='"+pid+"'";
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap=DatabaseHandlerDao.getInstance().queryForMap(sql);
        // String se= DatabaseHandlerDao.getInstance().executeSql(sql);
        return dataMap;
    }




}

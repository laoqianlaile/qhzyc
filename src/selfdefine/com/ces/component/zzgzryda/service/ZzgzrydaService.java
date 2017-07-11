package com.ces.component.zzgzryda.service;

import ces.sdk.system.bean.UserInfo;
import ces.sdk.util.MD5;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.database.Database;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import com.ces.xarch.core.entity.StringIDEntity;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.*;

@Component
public class ZzgzrydaService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setDao(TraceShowModuleDao dao) {
        super.setDao(dao);
    }

    /**
     * 根据单位类型确定工作人员岗位下拉框数据
     * @param dwlx
     * @return
     */
    public  Object getGwByDwlx(String dwlx){
        String sql = "";
        String bm = "SDZYCRYGWLX";//种植人员岗位编码
        if("JJGQY".equalsIgnoreCase(dwlx)){//精加工
            bm = "SDZYCJJGRYGWLX";
        }else if("CJGQY".equalsIgnoreCase(dwlx)){//粗加工
            bm = "SDZYCCJGRYGWLX";
        }
        sql = "select name as VALUE,name as TEXT from t_xtpz_code where code_type_code=? ORDER BY show_order asc";
        List<Map<String,Object>> list =  DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{bm});
        return list;
    }

    public Object getGwBySjzd() {
        String sql = "";
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        sql = "select sjmc as value,sjmc as text from t_common_sjlx_code t where t.lxbm = 'GW' and t.is_delete <> '1' order by t.sxjb asc";
        list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return list;
    }

    public Object getXbBySjzd() {
        String sql = "";
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        sql = "select sjmc as value,sjmc as text from t_common_sjlx_code t where t.lxbm = 'XB' and t.is_delete <> '1' order by t.sxjb asc";
        list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return list;
    }


    public Map<String, Object> getGzryda(String dwlx ) {
        String sql = "select T.GZRYBH,T.XM,T.GW,T.LXFS from T_ZZ_GZRYDA T where QYZT='1' " +defaultCode();
        if(!StringUtil.isEmpty(dwlx)) {
         sql += " and dwlx = '"+dwlx +"'" ;
        }
        sql += " ORDER BY T.GZRYBH DESC";
        List<Map<String, String>> list = (List<Map<String, String>>) entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return getResultData(list);
    }

    /**
     * 获取精加工人员信息
     * @return
     */
    public Map<String,Object> getJjgGzryxx(String dwlx){
//        String code = SerialNumberUtil.getInstance().getCompanyCode();
//        String sql = " select * from T_ZZ_GZRYDA t_  where  QYBM = '"+code+"' and is_delete = '0'  AND  DWLX='JJGQY' order by QYZT ASC,GZRYBH DESC";
//        List<Map<String, String>> list = (List<Map<String, String>>) entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
//        return getResultData(list);
        String sql = "select T.GZRYBH,T.XM,T.GW,T.LXFS from T_ZZ_GZRYDA T where QYZT='1' " +defaultCode();
        if(!StringUtil.isEmpty(dwlx)) {
            sql += " and dwlx = '"+dwlx +"'" ;
        }
        sql += " ORDER BY T.GZRYBH DESC";
        List<Map<String, String>> list = (List<Map<String, String>>) entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return getResultData(list);
    }

    public Map<String, Object> getResultData(List<Map<String, String>> data) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", data);
        return result;
    }

    /**
     * 默认权限过滤
     *
     * @return
     */
    public String defaultCode() {
        String code = SerialNumberUtil.getInstance().getCompanyCode();
        String defaultCode = " ";
        if (code != null && !"".equals(code))
            defaultCode = AppDefineUtil.RELATION_AND + " QYBM = '" + code + "' and is_delete = '0' ";
        return defaultCode;
    }

    @Override
    protected String buildCustomerFilter(String tableId,
                                         String componentVersionId, String moduleId, String menuId,
                                         Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
//        String dwlx =getb
        return defaultCode()+AppDefineUtil.RELATION_AND +" DWLX='"+paramMap.get("menuCode")+"'";
    }

    /**
     * 查重
     */
    public Object validYhm(String yhm) {
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("loginName", yhm);
        List<UserInfo> dataList = CommonUtil.getUserInfoFacade().findByCondition(dataMap);
        for (Iterator iterator = dataList.iterator(); iterator.hasNext(); ) {
            UserInfo user = (UserInfo) iterator.next();
            if ("1".equals(user.getStatus())) {
                iterator.remove();
            }
        }
        return dataList;
    }

    @Transactional
    public List<String> saveGzryxx(Map<String, Object> map, List<File> imageUpload, List<String> imageUploadFileName) {
        //获取旧数据
        String sql = "";
        Map<String, Object> oldMap = new HashMap<String, Object>();
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String realpath = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
        List<String> fileNamesWithStamp = new ArrayList<String>();
        Map<String, String> dataMap = new HashMap<String, String>();
        if (map.get("id").toString() != null && !"".equals(map.get("id").toString())) {
            sql = "select * from t_zz_gzryda t where id = '" + map.get("id").toString() + "'";
            oldMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
            dataMap.put("ZP", String.valueOf(oldMap.get("ZP")));
            dataMap.put("TPLJ", String.valueOf(oldMap.get("TPLJ")));
        }
        if (imageUpload != null && imageUpload.size() != 0 && !imageUpload.isEmpty()) {
            for (int i = 0; i < imageUpload.size(); i++) {
                if (i != imageUpload.size() - 1) {
                    continue;
                }
                String[] args = imageUploadFileName.get(0).split("\\.");
                String fileNameWithStamp = System.currentTimeMillis()+ UUIDGenerator.uuid()+"."+args[1];
                fileNamesWithStamp.add(fileNameWithStamp);
                File destFile = new File(realpath + "/" + fileNameWithStamp);
                try {
                    FileUtils.copyFile(imageUpload.get(i), destFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Date now = new Date();
                DateFormat df = DateFormat.getDateTimeInstance();
                String date = df.format(now);
                dataMap.put("ZP", imageUploadFileName.get(i));
                dataMap.put("TPLJ", fileNameWithStamp);
            }
        }
        dataMap.put("QYBM", qybm);
        dataMap.put("ID", String.valueOf(map.get("id")));
        dataMap.put("GZRYBH", String.valueOf(map.get("gzrybh")));
        dataMap.put("GW", String.valueOf(map.get("gw")));
        dataMap.put("XM", String.valueOf(map.get("xm")));
        dataMap.put("SFZH", String.valueOf(map.get("sfzh")));
        dataMap.put("XB", String.valueOf(map.get("xb")));
        dataMap.put("NN", String.valueOf(map.get("nn")));
        dataMap.put("LXFS", String.valueOf(map.get("lxfs")));
        dataMap.put("TC", String.valueOf(map.get("tc")));
        dataMap.put("ICKBH", String.valueOf(map.get("ickbh")));
        dataMap.put("QYZT", String.valueOf(map.get("qyzt")));
        dataMap.put("YHM", String.valueOf(map.get("yhm")));
        dataMap.put("MM", String.valueOf(map.get("mm")));
        dataMap.put("DWLX", String.valueOf(map.get("DWLX")));
        dataMap.put("MM",new MD5().getMD5ofStr("000000"));//添加默认密码
//        dataMap.put("SFKTZH",String.valueOf(map.get("sfktzh")));
        saveOne("t_zz_gzryda", dataMap);
        /****保存图片end*****/
        if (oldMap.get("TPLJ") != null && !oldMap.get("TPLJ").equals(dataMap.get("TPLJ"))) {
            //删除旧图片
//            FileUtil.deleteFile(realpath + "/" + oldMap.get("TPLJ"));
        }
        fileNamesWithStamp.clear();
        fileNamesWithStamp.add(dataMap.get("ID"));
        return fileNamesWithStamp;
    }


    /**
     * 修改时加载数据
     */
    public Object getGzryxxData(String id) {
        String sql = "";
        sql = "select * from t_zz_gzryda t where id = '" + id + "' and t.is_delete <> '1'";
        Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
        return map;
    }

    @Override
    @Transactional
    public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
        String id[] = ids.split(",");
        for (int i = 0; i < id.length; i++) {
            String sql = "select *from t_zz_gzryda t where t.id='" + id[i] + "'";
            Map<String, Object> dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
            UserInfo userInfo;
            userInfo = CommonUtil.getUserInfoFacade().findByLoginName(String.valueOf(dataMap.get("YHM")));
            if (userInfo != null) {
                CommonUtil.getUserInfoFacade().delete(userInfo.getId());
            }
        }
        super.delete(tableId, dTableIds, ids, isLogicalDelete, paramMap);
//        String realpath = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
//        String sql = "";
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        String id[] = ids.split(",");
//        sql = "select * from t_zz_gzryda t where t.is_delete <> '1' and 1 = 1";
//        for (int i = 0; i < id.length; i++) {
////            sql += "or id = '" + id[i] + "'";
////            list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
//        }
//        for (Map<String, Object> map : list) {
//            //删除旧图片
////            FileUtil.deleteFile(realpath + "/" + String.valueOf(map.get("TPLJ")));
//        }

    }

//    public Object validYhm(String yhm){
//        Map<String,String> dataMap = new HashMap<String, String>();
//        dataMap.put("loginName",yhm);
//        List<UserInfo> dataList = CommonUtil.getUserInfoFacade().findByCondition(dataMap);
////        for(UserInfo user:dataList){
////            if("1".equals(user.getStatus())){
////                dataList.remove(user);
////            }
////        }
//        return dataList;
//    }

    public Object getGzryxxByGzrybh(String gzrybh) {
        String sql = "SELECT * FROM T_ZZ_GZRYDA T WHERE GZRYBH = '" + gzrybh + "' AND QYBM = '" + SerialNumberUtil.getInstance().getCompanyCode() + "'";
        Map<String, Object> dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
        return dataMap;
    }
/**
 * 用来判断种植人员名称是否存在
 *
 */
    public Object checkGZRYXM(String gzryxm,String dwlx){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String sql="SELECT count(t.xm) as XM from  t_zz_gzryda t where t.xm='"+ gzryxm +"' and t.is_delete='0' and t.dwlx='"+dwlx+"'";
        dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
        if (!"0".equals(String.valueOf(dataMap.get("XM")))) {
            dataMap.put("msg", "该人员已经存在，请重新输入！");
            dataMap.put("result", "ERROR");
        }
        return dataMap;
    }

    /**
     * 用来检验IC卡用户输入的IC卡编号是否重复
     *
     * @param ickbh IC卡编号
     * @return
     */
    public Object checkICKBH(String ickbh, String id) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String sql = "select count(t.ickbh) as ICKBH from T_ZZ_GZRYDA t where t.is_delete <> '1' and t.ickbh ='" + ickbh + "'";
        if (!"".equals(id)) {
            sql = "select count(t.ickbh) as ICKBH from T_ZZ_GZRYDA t where t.is_delete <> '1' and t.ickbh ='" + ickbh + "' and id <> '" + id + "'";
        }
        dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
        if (!"0".equals(String.valueOf(dataMap.get("ICKBH")))) {
            dataMap.put("msg", "IC卡编号存在，请重新输入！");
            dataMap.put("result", "ERROR");
        }
        return dataMap;
    }

    public Object deleteImage(String tplj){
        String sql = "update t_zz_gzryda t set t.zp = '',t.tplj = '' where t.tplj = ?";
        int i = DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{tplj});
        return i;
    }

}
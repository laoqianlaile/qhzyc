package com.ces.component.zzdkzztj.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ZzdkzztjService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	public String  getDkbhById(String id){
		String sql = "select dkbh from t_zz_dkxx where id = ?";
		Map<String,Object> dataMap= DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{id});
		if(dataMap!=null && !dataMap.isEmpty()){
			return  dataMap.get("DKBH").toString();
		}
		return null;
	}

	public void logicDelete(String ids){
		String id[] = ids.split(",");
		StringBuilder sql = new StringBuilder("SELECT T.CGQZ FROM T_ZZ_DKXX T WHERE T.ID IN ('!'");
		for(int i = 0; i < id.length ; i++){
			sql.append(",'" + id[i] + "'");
		}
		sql.append(")");
		List<Map<String,Object>> cgqzsList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
		for(Map<String,Object> cgqzMap:cgqzsList){
			String cgqs[] = String.valueOf(cgqzMap.get("CGQZ")).split(",");
			sql = new StringBuilder("UPDATE T_QYPT_SBGL T SET T.SYZT = '0' WHERE T.SBSBH IN ('!'");
			for(int i = 0 ; i < cgqs.length ; i++){
				sql.append(",'" + cgqs[i] + "'");
			}
			sql.append(")");
			DatabaseHandlerDao.getInstance().executeSql(sql.toString());
		}
		String deleteSql = "update t_zz_dkxx set is_delete=1  where id IN ('" + ids.replace(",", "','") + "')";
		DatabaseHandlerDao.getInstance().executeSql(deleteSql);
	}
	/**
	 * 默认权限过滤
	 * @return
	 */
	public  String defaultCode(){
		String code= SerialNumberUtil.getInstance().getCompanyCode();
		String  defaultCode=" ";
		if(code!=null && !"".equals(code) )
			defaultCode= AppDefineUtil.RELATION_AND+" QYBM = '"+code+"' " +AppDefineUtil.RELATION_AND+" is_delete <> '1'";
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
	 * 用来检验IC卡用户输入的IC卡编号是否重复
	 *
	 * @param ickbh IC卡编号
	 * @return
	 */
	public Object checkICKBH(String ickbh, String id) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		String sql = "select count(t.ickbh) as ICKBH from T_ZZ_DKXX t where t.is_delete <> '1' and t.ickbh ='" + ickbh + "'";
		if (!"".equals(id)) {
			sql = "select count(t.ickbh) as ICKBH from T_ZZ_DKXX t where t.is_delete <> '1' and t.ickbh ='" + ickbh + "' and id <> '" + id + "'";
		}
		dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
		if (!"0".equals(String.valueOf(dataMap.get("ICKBH")))) {
			dataMap.put("msg", "IC卡编号存在，请重新输入！");
			dataMap.put("result", "ERROR");
		}
		return dataMap;

	}

	/**保存导入数据
	 * @param dataList
	 * @return
	 */
	@Transactional
	public Object importXls(List<Map<String,String>> dataList){
        Map<String,String> resultMap = new HashMap<String, String>();
        Map<String,Object> shareMap;
        if(dataList.size() == 0){
            resultMap.put("RESULT","ERROR");
            resultMap.put("MSG","模版里无数据！");
            return resultMap;
        }
        resultMap.put("RESULT","SUCCESS");
        resultMap.put("MSG","成功导入数据！");
        StringBuilder sql ;
        for(Map<String,String> map:dataList){
            //校验基地编号与区域编号
            sql = new StringBuilder("SELECT COUNT(1) AS CT,MAX(B.JDMC) AS JDMC,MAX(B.QYMC) AS QYMC FROM T_ZZ_JDXX A,T_ZZ_QYXX B WHERE A.QYBM = B.QYBM AND A.IS_DELETE <> '1' AND B.IS_DELETE <> '1' AND A.QYBM = ? AND A.JDBH = B.JDBH AND B.JDBH =? AND B.QYBH = ?");
            shareMap = DatabaseHandlerDao.getInstance().queryForMap(String.valueOf(sql),new Object[]{ map.get("QYBM"), map.get("JDBH"), map.get("QYBH")});
            if(Integer.parseInt(String.valueOf(shareMap.get("CT"))) != 1){
                resultMap.put("RESULT","ERROR");
                resultMap.put("MSG","序号:" + map.get("XH") + "数据基地编号或区域编号不存在，请检查数据！");
                break;
            }
            map.put("JDMC",String.valueOf(shareMap.get("JDMC")));
            map.put("QYMC",String.valueOf(shareMap.get("QYMC")));
            //插入地块编号
            map.put("DKBH",SerialNumberUtil.getInstance().getSerialNumber("ZZ",map.get("JDBH"),"ZZDKBH",true));
           //校验面积
            Double mj = 0.0;
            for(Map<String, String> mjMap:dataList){
                try {
                    if(String.valueOf(map.get("QYBH")).equals(String.valueOf(mjMap.get("QYBH")))){
                        mj += Double.parseDouble(map.get("MJ"));
                    }
                }catch (Exception e){
                    resultMap.put("RESULT","ERROR");
                    resultMap.put("MSG","序号:" + map.get("XH") + "数据面积格式不正确，请检查数据！");
                    break;
                }
            }
            if("SUCCESS".equals(resultMap.get("RESULT"))){
                if(Double.parseDouble(String.valueOf(mj)) == 0.0){
                    resultMap.put("RESULT","ERROR");
                    resultMap.put("MSG","序号:" + map.get("XH") + "数据面积为0，请检查数据！");
                    break;
                }
            }
            sql = new StringBuilder("SELECT AVG(A.QYMJ) AS ZMJ,SUM(B.MJ) AS FMJ FROM T_ZZ_QYXX A,T_ZZ_DKXX B WHERE A.QYBH = B.QYBH AND A.QYBM = ? AND B.QYBM = A.QYBM AND A.QYBH = ? AND A.IS_DELETE <> '1' AND B.IS_DELETE <> '1' AND B.DKBH != ?");
            Map<String,Object> dataMap = DatabaseHandlerDao.getInstance().queryForMap(String.valueOf(sql), new Object[]{map.get("QYBM"), map.get("QYBH"), map.get("DKBH")});
            if("null".equals(String.valueOf(dataMap.get("ZMJ"))) && "null".equals(String.valueOf(dataMap.get("FMJ")))){
                sql = new StringBuilder("SELECT AVG(A.QYMJ) AS ZMJ FROM T_ZZ_QYXX A WHERE A.QYBM = ? AND A.QYBH = ? AND A.IS_DELETE <> '1'");
                dataMap = DatabaseHandlerDao.getInstance().queryForMap(String.valueOf(sql), new Object[]{map.get("QYBM"), map.get("QYBH")});
                if("null".equals(String.valueOf(dataMap.get("ZMJ")))){
                    resultMap.put("RESULT","ERROR");
                    resultMap.put("MSG","序号:" + map.get("XH") + "数据所属区域面积为空，请检查数据！");
                    break;
                }else if (mj > Double.parseDouble(String.valueOf(dataMap.get("ZMJ")))) {
                    resultMap.put("RESULT","ERROR");
                    resultMap.put("MSG","序号:" + map.get("XH") + "数据面积数值过大，与其他地块面积之和已超出所属区域，请检查数据！");
                    break;
                }
            }else if (mj + Double.parseDouble(String.valueOf(dataMap.get("FMJ"))) > Double.parseDouble(String.valueOf(dataMap.get("ZMJ")))) {
                resultMap.put("RESULT","ERROR");
                resultMap.put("MSG","序号:" + map.get("XH") + "数据面积数值过大，与其他地块面积之和已超出所属区域，请检查数据！");
                break;
            }
            //校验地块负责人编号
            sql = new StringBuilder("SELECT COUNT(1) AS CT,MAX(T.XM) AS XM FROM T_ZZ_GZRYDA T WHERE T.QYBM = ? AND T.GZRYBH = ? AND T.IS_DELETE <> '1'");
            shareMap = DatabaseHandlerDao.getInstance().queryForMap(String.valueOf(sql), new Object[]{map.get("QYBM"), map.get("DKFZRBH")});
            if(Integer.parseInt(String.valueOf(shareMap.get("CT"))) != 1){
                resultMap.put("RESULT","ERROR");
                resultMap.put("MSG","序号:" + map.get("XH") + "数据负责人编号不存在，请检查数据！");
                break;
            }
            map.put("DKFZR",String.valueOf(shareMap.get("XM")));
        }
        //校验传感器组
        List<String []> cgqzList = new ArrayList<String[]>();
        for(Map<String, String> cgqzMap:dataList){
            if("".equals(String.valueOf(cgqzMap.get("CGQZ")))){
                continue;
            }
            cgqzList.add(String.valueOf(cgqzMap.get("CGQZ")).split(","));
        }
        boolean bool = true;
        ok:
        for(int i = 0; i < cgqzList.size() ; i++){
            String oldCgqz[] = cgqzList.get(i);
            for(int j = i+1; j < cgqzList.size() ; j++){
                        String newCgqz[] = cgqzList.get(j);
                        for(int m = 0; m < oldCgqz.length; m++){
                            for(int n = 0; n < newCgqz.length; n++){
                                if(oldCgqz[m].equals(newCgqz[n])){
                                    bool = false;
                                    break ok;
                                }
                            }
                }
            }
            sql = new StringBuilder("SELECT COUNT(1) AS CT FROM T_QYPT_SBGL T WHERE T.SBSBH IN (");
            for(int j = 0; j < oldCgqz.length; j++){
                sql.append("'" + oldCgqz[j] + "'");
                if(j < (oldCgqz.length - 1)){
                    sql.append(",");
                }
            }
            sql.append(") AND T.IS_DELETE <> '1' AND T.LB = 'CGQ' AND T.SYZT = '0' AND T.DWBM = ?");
            shareMap = DatabaseHandlerDao.getInstance().queryForMap(String.valueOf(sql), new Object[]{SerialNumberUtil.getInstance().getCompanyCode()});
            if(Integer.parseInt(String.valueOf(shareMap.get("CT"))) != oldCgqz.length){
                bool = false;
                break ok;
            }
        }
        if(!bool){
            resultMap.put("RESULT","ERROR");
            resultMap.put("MSG","传感器组数据错误，请检查数据！");
            return resultMap;
        }
        if("SUCCESS".equals(resultMap.get("RESULT"))){
            for(Map<String,String> map:dataList){
                map.remove("XH");
                saveOne("T_ZZ_DKXX", map);
                if(!"".equals(String.valueOf(map.get("CGQZ")))){
                    String cgqz[] = map.get("CGQZ").split(",");
                    sql = new StringBuilder("UPDATE T_QYPT_SBGL T SET T.SYZT='1' WHERE T.SBSBH IN (");
                    for(int i = 0; i < cgqz.length; i++){
                        sql.append("'" + cgqz[i] + "'");
                        if(i < (cgqz.length - 1)){
                            sql.append(",");
                        }
                    }
                    sql.append(") AND T.LB = 'CGQ'");
                    DatabaseHandlerDao.getInstance().executeSql(String.valueOf(sql));
                }
            }
        }
		return resultMap;
	}

}
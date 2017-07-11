package com.ces.component.aquatic.service;

import com.ces.component.aquatic.utils.AquaticCommonUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Component
public class ShxxService {
    private final String TABLE_NAME = " t_sc_jysh ";

    /**
     * 修改商户信息-手机号
     * @param shid 商户ID
     * @param sjh  手机号
     */
    public void updateShxx(String shid ,String sjh){
        String sql = "update "+TABLE_NAME+" t set t.SJH='"+sjh+"' where t.id='"+shid+"'";
        DatabaseHandlerDao.getInstance().executeSql(sql);
    }

    /**
     * 根据传递过来的经营品类编码查询详细信息
     * @param jyplbm 经营品类编码
     * @param shid   商户ID
     *
     */
    public void updateJypl(String shid,String jyplbm){
        //根据经营品类编码获得对应的显示名称
        String sql = "select t.spmc from t_sc_jyplxx t where t.spbm in('"+jyplbm.replaceAll(",","','")+"')";
        List<Map<String,Object>> dataMap=DatabaseHandlerDao.getInstance().queryForMaps(sql);
        String jypl="";
        if( null !=dataMap && !dataMap.isEmpty()){
            int size = dataMap.size();
            for (int i=0 ;i<size;i++){
                Map<String , Object> map = dataMap.get(i);
                jypl+=map.get("SPMC");
                //以“，”分隔
                if(i<size-1){
                    jypl+=",";
                }
            }
        }
        //统一修改经营品类编码和经营品类
        sql = "update "+TABLE_NAME+" t set t.jypl='"+jypl+"', t.jyplbm='"+jyplbm+"' where t.id='"+shid+"'";
        DatabaseHandlerDao.getInstance().executeSql(sql);
    }

    /**
     * 经营品类查询
     * @param pageNumber
     * @param pageSize
     * @return Page
     */
    public Page searchJypl(int pageNumber,int pageSize){
        if(pageNumber == 0){
            pageNumber = 1;
        }
        String sql = "SELECT * FROM T_SC_JYPLXX";
        return AquaticCommonUtil.getInstance().queryPage(new PageRequest(pageNumber, pageSize), sql);
}


}

package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ces.sdk.system.bean.OrgInfo;
import ces.sdk.system.bean.UserInfo;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.appmanage.AppFilterDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.appmanage.AppDefine;
import com.ces.config.dhtmlx.entity.appmanage.AppFilter;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxComboOption;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.CodeUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import com.ces.config.utils.authority.AuthorityUtil;
import com.ces.xarch.core.exception.FatalException;
import com.google.common.collect.Lists;

@Component
public class AppFilterService extends ConfigDefineDaoService<AppFilter, AppFilterDao> {

    /*
     * (非 Javadoc)   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("appFilterDao")
    @Override
    protected void setDaoUnBinding(AppFilterDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * qiucs 2014-12-11 
     * <p>描述: 获取列表过滤条件</p>
     * @return List<AppFilter> 返回类型
     */
    public List<AppFilter> findByFk(String tableId, String componentVersionId, String menuId) {
        return getDao().findByFk(tableId, componentVersionId, menuId);
    }
    
    /**
     * qiucs 2013-10-17 
     * <p>描述: 可选列表字段数据</p>
     * @return List<Object[]>    返回类型   
     */
    public List<Object[]> getDefaultColumn(String tableId, String componentVersionId, String menuId) {
        /*if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
            boolean special = getService(AppDefineService.class).isUseSpecial(tableId, componentVersionId, menuId, AppDefine.TYPE_FILTER);
            if (!special) {
                return getDao().getDefaultColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId);
            }
        }*/
        return getDao().getDefaultColumn(tableId, componentVersionId, menuId);
    }

    /**
     * qiucs 2013-10-17 
     * <p>描述: 已选列表字段数据</p>
     * @return List<Object[]>    返回类型   
     */
    public List<Object[]> getDefineColumn(String tableId, String componentVersionId, String menuId) {
        return getDao().getDefineColumn(tableId, componentVersionId, menuId);
    }
    
    /**
     * qiucs 2014-12-11 
     * <p>描述: 可选排序字段数据(前台列表数据)</p>
     * @return Object    返回类型   
     * @throws
     */
    public List<AppFilter> findDefaultList(String tableId, String componentVersionId, String menuId) {
        List<AppFilter> list = new ArrayList<AppFilter>();
        
        List<Object[]> rlist = getDefaultColumn(tableId, componentVersionId, menuId);
        
        for(Object[] oArr : rlist) {
            // t_cd.id column_id, t_cd.show_name, t_cd.column_name, t_cd.data_type, t_cd.code_type_code
            AppFilter item = new AppFilter();
            item.setId(UUIDGenerator.uuid());
            item.setColumnId(String.valueOf(oArr[0]));
            item.setShowName(String.valueOf(oArr[1]));
            item.setColumnName(String.valueOf(oArr[2]));
            item.setDataType(StringUtil.null2empty(oArr[3]));
            item.setCodeTypeCode(StringUtil.null2empty(oArr[4]));
            list.add(item);
        }
        
        return list;
    }
    
    /**
     * qiucs 2014-12-11 
     * <p>描述: 已选排序字段数据(前台列表数据)</p>
     * @return List<AppSort> 返回类型   
     * @throws
     */
    public List<AppFilter> findDefineList(String tableId, String componentVersionId, String menuId) {
        List<AppFilter> list = new ArrayList<AppFilter>();
        
        List<Object[]> rlist = getDefineColumn(tableId, componentVersionId, menuId);
        if (CollectionUtils.isNotEmpty(rlist)) {
            for(Object[] oArr : rlist) {
                // t_app.id, t_cd.show_name, t_app.filter_type, t_app.value, t_cd.column_name, t_cd.id columnId, // 0~5
                // t_app.relation, t_app.left_parenthesis, t_app.right_parenthesis, t_cd.data_type, t_cd.code_type_code // 6~10
                AppFilter item = new AppFilter();
                item.setId(String.valueOf(oArr[0]));
                item.setShowName(String.valueOf(oArr[1]));
                item.setFilterType(StringUtil.null2empty(oArr[2]));
                item.setValue(StringUtil.null2empty(oArr[3]));
                item.setColumnName(String.valueOf(oArr[4]));
                item.setColumnId(String.valueOf(oArr[5]));
                item.setRelation(StringUtil.null2empty(oArr[6]));
                item.setLeftParenthesis(StringUtil.null2empty(oArr[7]));
                item.setRightParenthesis(StringUtil.null2empty(oArr[8]));
                item.setDataType(StringUtil.null2empty(oArr[9]));
                item.setCodeTypeCode(StringUtil.null2empty(oArr[10]));
                list.add(item);
            }
        }
        return list;
    }
    
    /**
     * qiucs 2013-10-17 
     * <p>描述: 保存列表的配置信息</p>
     * @param  tableId
     * @param  componentVersionId
     * @param  rowsValue
     * @param  @throws FatalException    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    public void save(String tableId, String componentVersionId, String menuId, String rowsValue) throws FatalException {
        // 1. delete all grid column by table id and menu id
        getDao().clear(tableId, componentVersionId, menuId);
        // 2. save grid columns
        String[] rowsArr = rowsValue.split(";");
        AppFilter entity = null;
        List<AppFilter> list = Lists.newArrayList();
        for (int i = 0; i <rowsArr.length; i++) {
            String[] prop = rowsArr[i].split(","); // columnId, columnName, filterType, value
            entity = new AppFilter();
            entity.setShowOrder(i + 1);
            entity.setTableId(tableId);
            entity.setComponentVersionId(componentVersionId);
            entity.setMenuId(menuId);
            entity.setColumnId(prop[0]);
            entity.setRelation(prop[1]);
            entity.setLeftParenthesis(prop[2]);
            entity.setColumnName(prop[3]);
            entity.setFilterType(prop[4]);
            entity.setValue(prop[5]);
            entity.setRightParenthesis(prop.length < 7 ? "" : prop[6]);
            list.add(entity);
        }
        getDao().save(list);
        // 3. update AppDefine
        getService(AppDefineService.class).updateAppDefine(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID, AppDefine.TYPE_FILTER, AppDefine.DEFINE_YES);
        // 4. 
        AuthorityUtil.getInstance().clearAuthority(AuthorityUtil.AUTHORITY_DATA, tableId, componentVersionId);
    }
    
    /**
     * <p>标题: clearColumn</p>
     * <p>描述: 清除列表配置信息</p>
     * @param  tableId
     * @param  componentVersionId
     * @return void    返回类型   
     * @throws FatalException
     */
    @Transactional
    public void clear(String tableId, String componentVersionId, String menuId) throws FatalException {
        // 1. clear all grid columns
        getDao().clear(tableId, componentVersionId, menuId);
        // 2. update AppDefine
        getService(AppDefineService.class).updateAppDefine(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID, AppDefine.TYPE_FILTER, AppDefine.DEFINE_NO);
        // 3. 
        AuthorityUtil.getInstance().clearAuthority(AuthorityUtil.AUTHORITY_DATA, tableId, componentVersionId);
    }
    
    /**
     * qiucs 2013-10-17 
     * <p>描述: 组成查询过滤条件</p>
     * @param  tableId
     * @param  componentVersionId
     * @return String    返回类型   
     * @throws
     */
    public String processColumnFilter(String tableId, String componentVersionId, String menuId) {
        StringBuffer filter = new StringBuffer();
        List<Object[]> list = getDao().getDefineColumn(tableId, componentVersionId, menuId);
        if (null != list && !list.isEmpty()) {
            //  t_ac.id, t_cd.show_name, t_ac.filter_type, t_ac.value, t_cd.column_name, t_cd.id columnId,  0~5
            // t_ac.relation, t_ac.left_parenthesis, t_ac.right_parenthesis, t_cd.data_type, t_cd.code_type_code 6~10
            for (int i = 0; i < list.size(); i++) {
                Object[] obj = list.get(i);
                if (AppDefineUtil.RELATION_OR.equals(StringUtil.null2empty(obj[6]).toUpperCase())) {
                    filter.append(AppDefineUtil.RELATION_OR);
                } else {
                    filter.append(AppDefineUtil.RELATION_AND);
                }
                filter.append(StringUtil.null2empty(obj[7]))
                      .append(AppDefineUtil.processColumnFilter(null, String.valueOf(obj[4]), String.valueOf(obj[2]), StringUtil.null2empty(obj[3])))
                      .append(StringUtil.null2empty(obj[8]));
            }
        }
        return String.valueOf(filter);
    }
    
    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据表ID删除配置</p>
     */
    @Transactional
    public void deleteByTableId(String tableId) {
        getDao().deleteByTableId(tableId);
    }

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据 module id 删除配置</p>
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        getDao().deleteByTableId(componentVersionId);
    }
    
    /**
     * qiucs 2014-12-12 
     * <p>描述: 根据 menu id 删除配置</p>
     */
    @Transactional
    public void deleteByMenuId(String menuId) {
        getDao().deleteByMenuId(menuId);
    }
    
    /**
     * qiucs 2014-12-12 
     * <p>描述: SQL语句检查</p>
     */
    @Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    public MessageModel checkSql(String sql) {
        try {
            DatabaseHandlerDao.getInstance().queryForList(sql);
        } catch (Exception e) {
            return MessageModel.falseInstance("ERROR");
        }
        return MessageModel.trueInstance("OK");
    }

    public Object getSelectOption(String type, String codeTypeCode) throws Exception {
        List<DhtmlxComboOption> opts = Lists.newArrayList();
        if (ConstantVar.DataType.ENUM.equals(type)) {
            List<Code> list = CodeUtil.getInstance().getCodeList(codeTypeCode);
            if (null != list) {
                for (Code code : list) {
                    DhtmlxComboOption option = new DhtmlxComboOption();
                    option.setValue(code.getValue());
                    option.setText(code.getName());
                    opts.add(option);
                }
            }
        } else if (ConstantVar.DataType.USER.equals(type)) {
            @SuppressWarnings({"unchecked"})
            List<UserInfo> ulist = CommonUtil.getUserInfoFacade().findAll();
            if (null != ulist) {
                for (UserInfo info : ulist) {
                    DhtmlxComboOption option = new DhtmlxComboOption();
                    option.setValue(String.valueOf(info.getId()));
                    option.setText(info.getName());
                    opts.add(option);
                }
            }
        } else if (ConstantVar.DataType.PART.equals(type)) {
            List<OrgInfo> olist = CommonUtil.getOrgInfoFacade().findAll();
            if (null != olist) {
                for (OrgInfo orgInfo : olist) {
                    DhtmlxComboOption option = new DhtmlxComboOption();
                    option.setValue(orgInfo.getId());
                    option.setText(orgInfo.getName());
                    opts.add(option);
                }
            }
        }
        return opts;
    }
}

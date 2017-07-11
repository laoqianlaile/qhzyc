package com.ces.config.utils.authority;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.service.authority.AuthorityCodeService;
import com.ces.config.dhtmlx.service.authority.AuthorityComponentButtonService;
import com.ces.config.dhtmlx.service.authority.AuthorityConstructButtonService;
import com.ces.config.dhtmlx.service.authority.AuthorityDataDetailService;
import com.ces.config.dhtmlx.service.authority.AuthorityDataService;
import com.ces.config.dhtmlx.service.authority.AuthorityReportService;
import com.ces.config.dhtmlx.service.authority.AuthorityService;
import com.ces.config.dhtmlx.service.authority.AuthorityTreeService;
import com.ces.xarch.core.web.listener.XarchListener;

/**
 * 默认的权限管理实现
 * 
 * @author wanglei
 * @date 2014-12-16
 */
public class DefaultAuthorityManagerImpl implements AuthorityManager, Serializable {

    private static final long serialVersionUID = -6211655709685887083L;

    /*
     * (non-Javadoc)
     * @see com.ces.config.utils.authority.AuthorityManager#getMenuIds()
     */
    @Override
    public List<String> getMenuIds(String systemId) {
        return XarchListener.getBean(AuthorityService.class).getCurrentUserMenu(systemId);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.utils.authority.AuthorityManager#getTreeDefineIds(java.lang.String, java.lang.String)
     */
    @Override
    public List<String> getTreeDefineIds(String menuId, String componentVersionId) {
        return XarchListener.getBean(AuthorityTreeService.class).getTreeIdsByAuthority(menuId, componentVersionId);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.utils.authority.AuthorityManager#getDataFilter(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public String getDataFilter(String menuId, String componentVersionId, String tableId) {
        return XarchListener.getBean(AuthorityDataService.class).buildAuthorityFilter(tableId, componentVersionId, menuId);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.utils.authority.AuthorityManager#getRelateDataFilter(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public Map<String, Map<String, String>> getRelateDataFilter(String menuId, String componentVersionId, String tableId) {
        // TODO Auto-generated method stub
        return XarchListener.getBean(AuthorityDataService.class).buildRelateAuthorityFilter(tableId, componentVersionId, menuId);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.utils.authority.AuthorityManager#notUsedConstructButtonIds(java.lang.String,
     * java.lang.String)
     */
    @Override
    public List<String> notUsedConstructButtonIds(String menuId, String componentVersionId) {
        return XarchListener.getBean(AuthorityConstructButtonService.class).notAuthorityConstructButtons(menuId, componentVersionId);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.utils.authority.AuthorityManager#notUsedPageComponentButtonIds(java.lang.String,
     * java.lang.String)
     */
    @Override
    public List<String> notUsedPageComponentButtonIds(String menuId, String componentVersionId) {
        return XarchListener.getBean(AuthorityComponentButtonService.class).notAuthorityComponentButtons(menuId, componentVersionId);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.utils.authority.AuthorityManager#notUsedReportIds(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public List<String> notUsedReportIds(String menuId, String componentVersionId, String tableId) {
        return XarchListener.getBean(AuthorityReportService.class).notAuthorityReports(tableId, componentVersionId, menuId);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.utils.authority.AuthorityManager#getCodeAuthority(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public List<Code> getCodeAuthority(String menuId, String componentVersionId, String codeTypeCode) {
        return XarchListener.getBean(AuthorityCodeService.class).getCodeAuthority(menuId, componentVersionId, codeTypeCode);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.utils.authority.AuthorityManager#getAuthorityDeptFilterModelData()
     */
    @Override
    public Map<String, Object> getAuthorityDeptFilterModelData() {
        return XarchListener.getBean(AuthorityDataDetailService.class).getAuthorityDeptFilterModelData();
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.utils.authority.AuthorityManager#authorityFilterConversion(java.util.Map, java.lang.String)
     */
    @Override
    public String authorityFilterConversion(Map<String, Object> map, String filter) {
        return XarchListener.getBean(AuthorityDataDetailService.class).authorityFilterConversion(map, filter);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.utils.authority.AuthorityManager#putAuthorityDeptFilterModelData(java.lang.String,
     * java.lang.Object)
     */
    @Override
    public void putAuthorityDeptFilterModelData(String key, Object value) {
        XarchListener.getBean(AuthorityDataDetailService.class).putAuthorityDeptFilterModelData(key, value);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.utils.authority.AuthorityManager#removeAuthorityDeptFilterModelData(java.lang.String)
     */
    @Override
    public void removeAuthorityDeptFilterModelData(String key) {
        XarchListener.getBean(AuthorityDataDetailService.class).removeAuthorityDeptFilterModelData(key);
    }
}

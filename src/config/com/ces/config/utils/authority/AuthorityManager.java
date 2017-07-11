package com.ces.config.utils.authority;

import java.util.List;
import java.util.Map;

import com.ces.config.dhtmlx.entity.code.Code;

/**
 * 权限管理接口
 * 
 * @author wanglei
 * @date 2014-12-16
 */
public interface AuthorityManager {

    /**
     * 获取有权限的菜单
     * 
     * @param systemId 系统ID
     * @return List<String>
     */
    List<String> getMenuIds(String systemId);

    /**
     * 获取树上有权限的节点
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @return List<String>
     */
    List<String> getTreeDefineIds(String menuId, String componentVersionId);

    /**
     * 获取数据权限的过滤条件
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @param tableId 表ID
     * @return String
     */
    String getDataFilter(String menuId, String componentVersionId, String tableId);

    /**
     * 获取数据权限的过滤条件
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @param tableId 表ID
     * @return String
     */
    Map<String, Map<String, String>> getRelateDataFilter(String menuId, String componentVersionId, String tableId);

    /**
     * 获取不用的组装的按钮（减按钮）
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @return List<String>
     */
    List<String> notUsedConstructButtonIds(String menuId, String componentVersionId);

    /**
     * 获取不用的页面构件的按钮（减按钮）
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 页面构件版本ID
     * @return List<String>
     */
    List<String> notUsedPageComponentButtonIds(String menuId, String componentVersionId);

    /**
     * 获取不用的报表ID（减报表）
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @param tableId 表ID
     * @return List<String>
     */
    List<String> notUsedReportIds(String menuId, String componentVersionId, String tableId);

    /***
     * 获取权限编码
     * 
     * @param menuId 模块ID
     * @param componentVersionId 构件ID
     * @param codeTypeCode 编码code
     * @return List<Code>
     */
    List<Code> getCodeAuthority(String menuId, String componentVersionId, String codeTypeCode);

    /***
     * 获取数据权限部门过滤条件下拉数据
     * 
     * @return Map<String, Object>
     */
    Map<String, Object> getAuthorityDeptFilterModelData();

    /***
     * 添加数据权限部门过滤条件下拉数据
     * 
     * @param key 键
     * @param value 值
     */
    void putAuthorityDeptFilterModelData(String key, Object value);

    /***
     * 移除数据权限部门过滤条件下拉数据
     * 
     * @param key 键
     */
    void removeAuthorityDeptFilterModelData(String key);

    /***
     * 数据权限条件值转换
     * 
     * @param filter
     * @return String
     */
    String authorityFilterConversion(Map<String, Object> map, String filter);
}

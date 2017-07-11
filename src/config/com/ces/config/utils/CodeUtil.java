package com.ces.config.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.collections.CollectionUtils;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.BusinessCode;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.entity.code.CodeType;
import com.ces.config.dhtmlx.service.code.BusinessCodeService;
import com.ces.config.dhtmlx.service.code.CodeService;
import com.ces.config.dhtmlx.service.code.CodeTypeService;
import com.ces.config.utils.authority.AuthorityUtil;
import com.ces.xarch.core.web.listener.XarchListener;

/**
 * 编码模块工具类
 * 
 * @author wanglei
 * @date 2013-08-06
 */
public class CodeUtil {

    private static CodeUtil instance = new CodeUtil();

    /** * 编码存储Map，key为编码类型编码，value为编码类型名称 */
    // private static Map<String, String> codeTypeMap = new HashMap<String, String>();

    /** * 编码存储Map，key为编码类型编码，value为编码列表 */
    // private static Map<String, List<Code>> codeMap = new HashMap<String, List<Code>>();

    /** * 编码类型在Ehcache中的cache名称 */
    private static final String CODE_TYPE = "CODE_TYPE";

    /** * 编码参数在Ehcache中的cache名称 */
    private static final String CODE = "CODE";

    /** * 树型编码参数在Ehcache中的cache名称 */
    private static final String CODE_TREE = "CODE_TREE";

    /** 不使用缓存 */
    public static final String NO_CACHE = "0";

    private CodeUtil() {
    }

    /**
     * 获取CodeUtil实例
     * 
     * @return CodeUtil
     */
    public static CodeUtil getInstance() {
        return instance;
    }

    /**
     * 添加编码类型
     * 
     * @param codeTypeCode 编码类型编码
     * @param codeTypeName 编码类型名称
     */
    public void putCodeType(String codeTypeCode, String codeTypeName) {
        EhcacheUtil.setCache(CODE_TYPE, codeTypeCode, codeTypeName);
    }

    /**
     * 获取编码名称
     * 
     * @param codeTypeCode 编码类型编码
     * @return String
     */
    public String getCodeType(String codeTypeCode) {
        return String.valueOf(EhcacheUtil.getCache(CODE_TYPE, codeTypeCode));
    }

    /**
     * 移除编码类型
     * 
     * @param codeTypeCode 编码类型编码
     */
    public void removeCodeType(String codeTypeCode) {
        EhcacheUtil.removeCache(CODE_TYPE, codeTypeCode);
        removeCode(codeTypeCode);
    }

    /**
     * 获取编码类型map
     * 
     * @return Map<String, String>
     */
    public Map<String, String> getCodeTypeMap() {
        Map<String, String> map = new HashMap<String, String>();
        Cache cache = EhcacheUtil.getCache(CODE_TYPE);
        if (cache != null) {
            Element element = null;
            for (Object key : cache.getKeys()) {
                element = cache.get(key);
                map.put(String.valueOf(key), String.valueOf(element.getObjectValue()));
            }
        }
        return map;
    }

    /**
     * qiucs 2015-3-18 下午1:01:09
     * <p>描述: 判断“编码类型”是否存在 </p>
     * 
     * @return boolean
     */
    public boolean hasCodeType(String codeTypeCode) {
        String name = getCodeType(codeTypeCode);
        return StringUtil.isNotEmpty(name);
    }

    /**
     * 获取编码列表
     * 
     * @param codeTypeCode 编码类型编码
     * @return List<Code>
     */
    @SuppressWarnings("unchecked")
    public List<Code> getCodeList(String codeTypeCode) {
        CodeType entity = XarchListener.getBean(CodeTypeService.class).getCodeTypeByCode(codeTypeCode);
        if (null == entity)
            return null;
        if (NO_CACHE.equals(entity.getIsCache())) {
            if ("1".equals(entity.getIsBusiness())) {
                return getRealBusinessCode(codeTypeCode);
            }
            return XarchListener.getBean(CodeService.class).getByCodeTypeCodeAndParentIdIsNUll(codeTypeCode);
        }
        List<Code> codeList = (List<Code>) EhcacheUtil.getCache(CODE, codeTypeCode);
        if (CollectionUtils.isEmpty(codeList))
            return null;
        Collections.sort(codeList);
        return codeList;
    }

    /***
     * 实时业务编码
     * 
     * @param codeTypeCode 编码类型编码
     * @return List<Code>
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private List<Code> getRealBusinessCode(String codeTypeCode) {
        BusinessCode businessCode = XarchListener.getBean(BusinessCodeService.class).getByCodeTypeCode(codeTypeCode);
        List<Code> codeList = new ArrayList<Code>();
        if (null == businessCode) {
            return null;
        }
        if (BusinessCode.TABLE.equals(businessCode.getBusinessCodeType())) {
            if (StringUtil.isEmpty(businessCode.getTableName()) || StringUtil.isEmpty(businessCode.getCodeNameField())
                    || StringUtil.isEmpty(businessCode.getCodeValueField())) {
                return null;
            }
            StringBuilder sql = new StringBuilder();
            boolean isSingleField = true;
            if (businessCode.getCodeNameField().equalsIgnoreCase(businessCode.getCodeValueField())) {
                sql.append("select distinct ").append(businessCode.getCodeNameField());
            } else {
                sql.append("select distinct ").append(businessCode.getCodeNameField()).append(", ").append(businessCode.getCodeValueField());
                isSingleField = false;
            }
            if (StringUtil.isNotEmpty(businessCode.getShowOrderField())) {
                sql.append(", ").append(businessCode.getShowOrderField());
                isSingleField = false;
            }
            if (StringUtil.isNotEmpty(businessCode.getIdField())) {
                sql.append(", ").append(businessCode.getIdField());
                isSingleField = false;
            }
            if (StringUtil.isNotEmpty(businessCode.getParentIdField())) {
                sql.append(", ").append(businessCode.getParentIdField());
                isSingleField = false;
            }
            sql.append(" from ").append(businessCode.getTableName());
            if (StringUtil.isNotEmpty(businessCode.getShowOrderField())) {
                sql.append(" order by ").append(businessCode.getShowOrderField());
            }
            List list = null;
            if ("0".equals(businessCode.getIsAuth())) {
                list = DatabaseHandlerDao.getInstance().queryForList(sql.toString());
            } else {
                list = AuthDatabaseUtil.queryForList(sql.toString());
            }
            if (CollectionUtils.isNotEmpty(list)) {
                Code code = null;
                int i = 0;
                if (isSingleField) {
                    for (Object obj : list) {
                        code = new Code();
                        code.setCodeTypeCode(codeTypeCode);
                        code.setName(String.valueOf(obj));
                        code.setValue(String.valueOf(obj));
                        code.setShowOrder(i++);
                        codeList.add(code);
                    }
                } else {
                    for (Object[] objs : (List<Object[]>) list) {
                        code = new Code();
                        int j = 0;
                        code.setCodeTypeCode(codeTypeCode);
                        code.setName(String.valueOf(objs[j]));
                        if (businessCode.getCodeNameField().equalsIgnoreCase(businessCode.getCodeValueField())) {
                            code.setValue(String.valueOf(objs[j]));
                        } else {
                            code.setValue(String.valueOf(objs[++j]));
                        }
                        j++;
                        if (StringUtil.isNotEmpty(businessCode.getShowOrderField()) && objs.length > j) {
                            code.setShowOrder(Integer.valueOf(StringUtil.null2zero(objs[j++])));
                        } else {
                            code.setShowOrder(i++);
                        }
                        if (StringUtil.isNotEmpty(businessCode.getIdField()) && objs.length > j) {
                            code.setId(String.valueOf(objs[j++]));
                        }
                        if (StringUtil.isNotEmpty(businessCode.getParentIdField()) && objs.length > j) {
                            code.setParentId(String.valueOf(objs[j++]));
                        }
                        codeList.add(code);
                    }
                }
            }
        } else {
            codeList = getRealJavaCode(businessCode);
        }
        return codeList;
    }

    /***
     * 实时java编码
     * 
     * @param businessCode 业务编码实体
     * @return List<Code>
     */
    private List<Code> getRealJavaCode(BusinessCode businessCode) {
        CodeApplication application = CodeApplicationUtil.getApplicationInstance(businessCode);
        if (null == application) {
        	return null;
        }
        return application.getCodeList(businessCode.getCodeTypeCode());
    }

    /**
     * 添加编码
     * 
     * @param codeTypeCode 编码类型编码
     * @param codeList 编码列表
     */
    public void putCodeList(String codeTypeCode, List<Code> codeList) {
        if (null == codeList) {
            return;
        }
        Vector<Code> codeVector = new Vector<Code>(codeList);
        EhcacheUtil.setCache(CODE, codeTypeCode, codeVector);
        EhcacheUtil.removeAllCache(AuthorityUtil.AUTHORITY_CODE);
    }

    /**
     * 添加编码
     * 
     * @param codeTypeCode 编码类型编码
     * @param code 编码
     */
    @SuppressWarnings("unchecked")
    public void putCode(String codeTypeCode, Code code) {
        Vector<Code> codeVector = (Vector<Code>) EhcacheUtil.getCache(CODE, codeTypeCode);
        if (codeVector == null) {
            codeVector = new Vector<Code>();
            codeVector.add(code);
            putCodeList(codeTypeCode, codeVector);
        } else {
            codeVector.add(code);
        }
        EhcacheUtil.removeAllCache(AuthorityUtil.AUTHORITY_CODE);
    }

    /**
     * 移除编码
     * 
     * @param codeTypeCode 编码类型编码
     * @param code 编码
     */
    @SuppressWarnings("unchecked")
    public void removeCode(String codeTypeCode, Code code) {
        Vector<Code> codeVector = (Vector<Code>) EhcacheUtil.getCache(CODE, codeTypeCode);
        if (codeVector != null) {
            codeVector.remove(code);
        }
    }

    /**
     * 根据编码类型移除编码
     * 
     * @param codeTypeCode 编码类型编码
     */
    public void removeCode(String codeTypeCode) {
        EhcacheUtil.removeCache(CODE, codeTypeCode);
    }

    /**
     * 根据编码名称获取编码值
     * 
     * @param codeTypeCode 编码类型编码
     * @param name 编码名称
     * @return String 编码值
     */
    public String getCodeValue(String codeTypeCode, String name) {
        if (name == null)
            return null;
        List<Code> codeList = getCodeList(codeTypeCode);
        if (CollectionUtils.isNotEmpty(codeList)) {
            for (Code code : codeList) {
                if (name.equals(code.getName())) {
                    return code.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 根据编码值获取编码名称
     * 
     * @param codeTypeCode 编码类型编码
     * @param value 编码值
     * @return String 编码名称
     */
    public String getCodeName(String codeTypeCode, String value) {
        if (value == null)
            return null;
        List<Code> codeList = getCodeList(codeTypeCode);
        if (CollectionUtils.isNotEmpty(codeList)) {
            for (Code code : codeList) {
                if (value.equals(code.getValue())) {
                    return code.getName();
                }
            }
        }
        return null;
    }

    /**
     * qiucs 2015-1-21 下午1:45:28
     * <p>描述: 获取树型编码（目前只有JAVA应用编码才有树型编码） </p>
     * 
     * @return Object
     */
    public Object getCodeTree(String codeTypeCode) {
        Object treeData = EhcacheUtil.getCache(CODE_TREE, codeTypeCode);
        if (null == treeData || "TREE_TEST".equals(codeTypeCode)) {
            treeData = toCodeTree(codeTypeCode);
            putCodeTree(codeTypeCode, treeData);
        }
        return treeData;
    }

    /**
     * qiucs 2015-5-11 下午1:40:24
     * <p>描述: 获取 </p>
     * 
     * @return Object
     */
    private Object toCodeTree(String codeTypeCode) {

        List<Code> list = getCodeList(codeTypeCode);
        if (null == list || list.isEmpty()) {
            return null;
        }
        int len = list.size();
        Map<String, List<Map<String, Object>>> codeMap = new HashMap<String, List<Map<String, Object>>>(len);
        String parentId = null;
        List<Map<String, Object>> sublist = null;
        Map<String, Object> item = null;

        Map<String, String> parentMap = new HashMap<String, String>(len);

        for (Code code : list) {
            parentMap.put(code.getId(), code.getValue());
        }

        for (Code code : list) {
            parentId = code.getParentId();
            if (StringUtil.isEmpty(parentId))
                parentId = "-1";
            else
                parentId = parentMap.get(parentId);
            sublist = codeMap.get(parentId);
            if (null == sublist) {
                sublist = new ArrayList<Map<String, Object>>(26);
                codeMap.put(parentId, sublist);
            }
            item = new HashMap<String, Object>(8);
            item.put("id", code.getValue());
            item.put("name", code.getName());
            item.put("isParent", false);
            sublist.add(item);
        }
        return assembleTreeData("-1", codeMap);
    }

    /**
     * qiucs 2015-5-11 下午1:39:37
     * <p>描述: 封装成树型结构 </p>
     * 
     * @return List<Map<String,Object>>
     */
    private List<Map<String, Object>> assembleTreeData(String parentId, Map<String, List<Map<String, Object>>> codeMap) {
        List<Map<String, Object>> treeData = null;
        List<Map<String, Object>> sublist = codeMap.get(parentId);
        if ("-1".equals(parentId)) {
            treeData = new ArrayList<Map<String, Object>>(26);
            treeData.addAll(sublist);
        }

        String id = null;
        for (Map<String, Object> itemMap : sublist) {
            id = itemMap.get("id").toString();
            if (codeMap.containsKey(id)) {
                itemMap.put("children", codeMap.get(id));
                itemMap.put("isParent", Boolean.TRUE);
                assembleTreeData(id, codeMap);
            }
        }
        return treeData;
    }

    /**
     * qiucs 2015-1-21 下午1:46:32
     * <p>描述: 添加树型编码 </p>
     * 
     * @return void
     */
    public void putCodeTree(String codeTypeCode, Object treeData) {
        EhcacheUtil.setCache(CODE_TREE, codeTypeCode, treeData);
    }

    /**
     * qiucs 2015-1-21 下午1:46:50
     * <p>描述: 移除树型编码 </p>
     * 
     * @return void
     */
    public void removeCodeTree(String codeTypeCode) {
        EhcacheUtil.removeCache(CODE_TREE, codeTypeCode);
    }
}

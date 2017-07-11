package com.ces.config.dhtmlx.service.authority;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.authority.AuthorityCodeDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.authority.AuthorityCode;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.CodeUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.security.entity.SysUser;

/**
 * 编码权限Service
 * 
 * @author luojinkai
 * @date 2015-03-12
 */
@Component
public class AuthorityCodeService extends ConfigDefineDaoService<AuthorityCode, AuthorityCodeDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.BaseDao)
     */
    @Autowired
    @Qualifier("authorityCodeDao")
    @Override
    protected void setDaoUnBinding(AuthorityCodeDao dao) {
        super.setDaoUnBinding(dao);
    }

    public AuthorityCode findByObjectIdAndObjectTypeAndMenuIdAndComponentVersionIdAndCodeTypeCode(AuthorityCode authorutyCode) {
        return getDao().findByObjectIdAndObjectTypeAndMenuIdAndComponentVersionIdAndCodeTypeCode(authorutyCode.getObjectId(), authorutyCode.getObjectType(),
                authorutyCode.getMenuId(), authorutyCode.getComponentVersionId(), authorutyCode.getCodeTypeCode());
    }

    /***
     * 根据数据权限获取当前用户下的编码
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件ID
     * @param codeTypeCode 编码code
     * @return List<Code>
     */
    public List<Code> getCodeAuthority(String menuId, String componentVersionId, String codeTypeCode) {
        List<Code> codeList = null;
        StringBuffer sb = new StringBuffer();
        sb.append("from AuthorityCode t where t.menuId='").append(menuId).append("'").append(" and t.codeTypeCode='").append(codeTypeCode).append("'");
        try {
            SysUser user = CommonUtil.getUser();
            // 1. 当前用户自身的数据权限
            String sql = String.valueOf(sb) + " and t.componentVersionId='" + componentVersionId + "'" + " and t.objectId='" + user.getId()
                    + "' and t.objectType='1' ";
            List<AuthorityCode> list = DatabaseHandlerDao.getInstance().queryEntityForList(sql, AuthorityCode.class);
            if (CollectionUtils.isEmpty(list)) {
                // 2. 当前用户本菜单下的数据权限
                sql = String.valueOf(sb) + " and t.componentVersionId='-1' and t.objectId='" + user.getId() + "' and t.objectType='1' ";
                list = DatabaseHandlerDao.getInstance().queryEntityForList(sql, AuthorityCode.class);
            }
            String roleIds = CommonUtil.getRoleIds();
            if (CollectionUtils.isEmpty(list) && StringUtil.isNotEmpty(roleIds)) {
                // 3. 当前用户拥有的角色的数据权限
                sql = String.valueOf(sb) + " and t.componentVersionId='" + componentVersionId + "' and t.objectId in ('" + roleIds.replace(",", "','")
                        + "') and t.objectType='0' ";
                list = DatabaseHandlerDao.getInstance().queryEntityForList(sql, AuthorityCode.class);
            }
            if (CollectionUtils.isEmpty(list) && StringUtil.isNotEmpty(roleIds)) {
                // 4. 当前用户拥有的角色本菜单下的数据权限
                sql = String.valueOf(sb) + " and t.componentVersionId='-1' and t.objectId in ('" + roleIds.replace(",", "','") + "') and t.objectType='0' ";
                list = DatabaseHandlerDao.getInstance().queryEntityForList(sql, AuthorityCode.class);
            }
            if (CollectionUtils.isNotEmpty(list)) {
                if (list.size() > 0) {
                    AuthorityCode authorityCode = list.get(0);
                    if (StringUtils.isNotEmpty(authorityCode.getCodeJson())) {
                        String[] codes = authorityCode.getCodeJson().split(",");
                        codeList = (List<Code>) CodeUtil.getInstance().getCodeList(codeTypeCode);
                        Code code = null;
                        if (CollectionUtils.isNotEmpty(codeList)) {
                            for (String v : codes) {
                                for (Iterator<Code> iterator = codeList.iterator(); iterator.hasNext();) {
                                    code = iterator.next();
                                    if (v.equals(code.getValue())) {
                                        iterator.remove();
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        codeList = (List<Code>) CodeUtil.getInstance().getCodeList(codeTypeCode);
                    }
                } else {
                    codeList = (List<Code>) CodeUtil.getInstance().getCodeList(codeTypeCode);
                }
            } else {
                codeList = (List<Code>) CodeUtil.getInstance().getCodeList(codeTypeCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return codeList;
    }

    /**
     * 根据角色IDs和菜单IDs获取编码权限
     * 
     * @param roleIds 角色IDs
     * @param menuIds 菜单IDs
     * @return List<AuthorityCode>
     */
    public List<AuthorityCode> getByRoleIdsAndMenuIds(String roleIds, String menuIds) {
        List<AuthorityCode> authorityCodeList = new ArrayList<AuthorityCode>();
        if (StringUtil.isNotEmpty(roleIds) && StringUtil.isNotEmpty(menuIds)) {
            String hql = "from AuthorityCode t where t.objectType='0' and t.objectId in ('" + roleIds.replace(",", "','") + "') and t.menuId in ('"
                    + menuIds.replace(",", "','") + "')";
            authorityCodeList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, AuthorityCode.class);
        }
        return authorityCodeList;
    }

    /**
     * 根据菜单IDs获取用户的编码权限
     * 
     * @param menuIds 菜单IDs
     * @return List<AuthorityCode>
     */
    public List<AuthorityCode> getByMenuIdsOfUser(String menuIds) {
        List<AuthorityCode> authorityCodeList = new ArrayList<AuthorityCode>();
        if (StringUtil.isNotEmpty(menuIds)) {
            String hql = "from AuthorityCode t where t.objectType='1' and t.menuId in ('" + menuIds.replace(",", "','") + "')";
            authorityCodeList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, AuthorityCode.class);
        }
        return authorityCodeList;
    }
}

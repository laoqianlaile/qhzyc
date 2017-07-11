package com.ces.config.dhtmlx.service.authority;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.authority.AuthorityApproveDao;
import com.ces.config.dhtmlx.entity.authority.AuthorityApprove;
import com.ces.config.dhtmlx.entity.authority.AuthorityData;
import com.ces.config.dhtmlx.entity.authority.AuthorityDataCopy;
import com.ces.config.dhtmlx.entity.authority.AuthorityDataDetail;
import com.ces.config.dhtmlx.entity.authority.AuthorityDataDetailCopy;
import com.ces.config.dhtmlx.entity.authority.AuthorityTree;
import com.ces.config.dhtmlx.entity.authority.AuthorityTreeCopy;
import com.ces.config.dhtmlx.entity.authority.AuthorityTreeData;
import com.ces.config.dhtmlx.entity.authority.AuthorityTreeDataCopy;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.authority.AuthorityUtil;

/**
 * 数据权限待审批Service（三权分立）
 * 
 * @author wanglei
 * @date 2015-09-14
 */
@Component
public class AuthorityApproveService extends ConfigDefineDaoService<AuthorityApprove, AuthorityApproveDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.BaseDao)
     */
    @Autowired
    @Qualifier("authorityApproveDao")
    @Override
    protected void setDaoUnBinding(AuthorityApproveDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 获取数据权限待审批的记录（树权限使用）
     * 
     * @param objectId 对象ID: objectType=0时，则为角色KEY，当objectType=1时，则为用户ID
     * @param objectType 类型：0-角色 1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @return AuthorityApprove
     */
    public AuthorityApprove getTreeAuthorityApprove(String objectId, String objectType, String menuId, String componentVersionId) {
        return getDao().getTreeAuthorityApprove(objectId, objectType, menuId, componentVersionId);
    }

    /**
     * 获取数据权限待审批的记录（数据权限、编码权限使用）
     * 
     * @param relateAuthId 关联的权限ID
     * @return AuthorityApprove
     */
    public AuthorityApprove getByRelateAuthId(String relateAuthId) {
        return getDao().getByRelateAuthId(relateAuthId);
    }

    /**
     * 获取数据权限待审批的记录
     * 
     * @return List<AuthorityApprove>
     */
    public List<AuthorityApprove> getAllApprovingAuthorityList() {
        return getDao().getAllApprovingAuthorityList();
    }

    /**
     * 审批数据权限
     * 
     * @param authorityApproveIds
     */
    @Transactional
    public String approveAuthority(String authorityApproveIds, String status) {
        boolean success = true;
        String message = null;
        try {
            if (StringUtil.isNotEmpty(authorityApproveIds)
                    && (ConstantVar.AuthorityApprove.Status.APPROVED_SUCCESS.equals(status) || ConstantVar.AuthorityApprove.Status.APPROVED_BACK.equals(status))) {
                String[] approveIds = authorityApproveIds.split(",");
                AuthorityApprove authorityApprove = null;
                for (String approveId : approveIds) {
                    authorityApprove = getByID(approveId);
                    if (ConstantVar.AuthorityApprove.Status.APPROVED_SUCCESS.equals(status)) {
                        // 审批通过
                        if (ConstantVar.AuthorityApprove.Type.TREE.equals(authorityApprove.getAuthorityType())) {
                            if (ConstantVar.AuthorityApprove.Operate.UPDATE.equals(authorityApprove.getOperate())
                                    || ConstantVar.AuthorityApprove.Operate.NEW.equals(authorityApprove.getOperate())) {
                                List<AuthorityTreeCopy> authorityTreeCopyList = getService(AuthorityTreeCopyService.class).getAuthorityTreeList(
                                        authorityApprove.getObjectId(), authorityApprove.getObjectType(), authorityApprove.getMenuId(),
                                        authorityApprove.getComponentVersionId());
                                if (CollectionUtils.isNotEmpty(authorityTreeCopyList)) {
                                    // 先删后增
                                    getService(AuthorityTreeService.class).deleteAuthorityTree(authorityApprove.getObjectId(),
                                            authorityApprove.getObjectType(), authorityApprove.getMenuId(), authorityApprove.getComponentVersionId());
                                    getService(AuthorityTreeDataService.class).deleteAuthorityTreeData(authorityApprove.getObjectId(),
                                            authorityApprove.getObjectType(), authorityApprove.getMenuId(), authorityApprove.getComponentVersionId());
                                    List<AuthorityTree> authorityTreeList = new ArrayList<AuthorityTree>();
                                    AuthorityTree authorityTree = null;
                                    for (AuthorityTreeCopy authorityTreeCopy : authorityTreeCopyList) {
                                        authorityTree = new AuthorityTree();
                                        authorityTree.setObjectId(authorityTreeCopy.getObjectId());
                                        authorityTree.setObjectType(authorityTreeCopy.getObjectType());
                                        authorityTree.setMenuId(authorityTreeCopy.getMenuId());
                                        authorityTree.setComponentVersionId(authorityTreeCopy.getComponentVersionId());
                                        authorityTree.setTreeNodeId(authorityTreeCopy.getTreeNodeId());
                                        authorityTreeList.add(authorityTree);
                                    }
                                    getService(AuthorityTreeService.class).save(authorityTreeList);
                                    AuthorityTreeDataCopy authorityTreeDataCopy = getService(AuthorityTreeDataCopyService.class).getAuthorityTreeData(
                                            authorityApprove.getObjectId(), authorityApprove.getObjectType(), authorityApprove.getMenuId(),
                                            authorityApprove.getComponentVersionId());
                                    if (authorityTreeDataCopy != null) {
                                        AuthorityTreeData authorityTreeData = new AuthorityTreeData();
                                        authorityTreeData.setObjectId(authorityTreeDataCopy.getObjectId());
                                        authorityTreeData.setObjectType(authorityTreeDataCopy.getObjectType());
                                        authorityTreeData.setMenuId(authorityTreeDataCopy.getMenuId());
                                        authorityTreeData.setComponentVersionId(authorityTreeDataCopy.getComponentVersionId());
                                        authorityTreeData.setControlDataAuth(authorityTreeDataCopy.getControlDataAuth());
                                        getService(AuthorityTreeDataService.class).save(authorityTreeData);
                                    }
                                    // 清除缓存
                                    AuthorityUtil.getInstance().clearAuthority(AuthorityUtil.AUTHORITY_TREE, authorityApprove.getObjectId(),
                                            authorityApprove.getObjectType(), authorityApprove.getMenuId(), authorityApprove.getComponentVersionId());
                                }
                            } else if (ConstantVar.AuthorityApprove.Operate.DELETE.equals(authorityApprove.getOperate())) {
                                // 删除
                                getService(AuthorityTreeCopyService.class).deleteAuthorityTree(authorityApprove.getObjectId(),
                                        authorityApprove.getObjectType(), authorityApprove.getMenuId(), authorityApprove.getComponentVersionId());
                                getService(AuthorityTreeDataCopyService.class).deleteAuthorityTreeData(authorityApprove.getObjectId(),
                                        authorityApprove.getObjectType(), authorityApprove.getMenuId(), authorityApprove.getComponentVersionId());
                                getService(AuthorityTreeService.class).deleteAuthorityTree(authorityApprove.getObjectId(), authorityApprove.getObjectType(),
                                        authorityApprove.getMenuId(), authorityApprove.getComponentVersionId());
                                getService(AuthorityTreeDataService.class).deleteAuthorityTreeData(authorityApprove.getObjectId(),
                                        authorityApprove.getObjectType(), authorityApprove.getMenuId(), authorityApprove.getComponentVersionId());
                            }
                        } else if (ConstantVar.AuthorityApprove.Type.DATA.equals(authorityApprove.getAuthorityType())) {
                            if (ConstantVar.AuthorityApprove.Operate.UPDATE.equals(authorityApprove.getOperate())
                                    || ConstantVar.AuthorityApprove.Operate.NEW.equals(authorityApprove.getOperate())) {
                                AuthorityDataCopy authorityDataCopy = getService(AuthorityDataCopyService.class).getByID(authorityApprove.getRelateAuthId());
                                if (authorityDataCopy != null) {
                                    AuthorityData oldAuthorityData = getService(AuthorityDataService.class).getAuthorityDataByName(
                                            authorityDataCopy.getObjectId(), authorityDataCopy.getObjectType(), authorityDataCopy.getMenuId(),
                                            authorityDataCopy.getComponentVersionId(), authorityDataCopy.getName());
                                    if (oldAuthorityData != null) {
                                        // 修改，先删后增
                                        getService(AuthorityDataService.class).delete(oldAuthorityData);
                                    }
                                    // 新增，将Copy表数据复制到权限表
                                    List<AuthorityDataDetailCopy> authDataDetailCopyList = getService(AuthorityDataDetailCopyService.class)
                                            .getByAuthorityDataId(authorityDataCopy.getId());
                                    if (CollectionUtils.isNotEmpty(authDataDetailCopyList)) {
                                        AuthorityData authorityData = new AuthorityData();
                                        authorityData.setName(authorityDataCopy.getName());
                                        authorityData.setObjectId(authorityDataCopy.getObjectId());
                                        authorityData.setObjectType(authorityDataCopy.getObjectType());
                                        authorityData.setMenuId(authorityDataCopy.getMenuId());
                                        authorityData.setComponentVersionId(authorityDataCopy.getComponentVersionId());
                                        authorityData.setTableId(authorityDataCopy.getTableId());
                                        authorityData.setControlTableIds(authorityDataCopy.getControlTableIds());
                                        authorityData.setShowOrder(authorityDataCopy.getShowOrder());
                                        getService(AuthorityDataService.class).save(authorityData);
                                        List<AuthorityDataDetail> authDataDetailList = new ArrayList<AuthorityDataDetail>();
                                        AuthorityDataDetail authDataDetail = null;
                                        for (AuthorityDataDetailCopy authDataDetailCopy : authDataDetailCopyList) {
                                            authDataDetail = new AuthorityDataDetail();
                                            authDataDetail.setAuthorityDataId(authDataDetailCopy.getAuthorityDataId());
                                            authDataDetail.setTableId(authDataDetailCopy.getTableId());
                                            authDataDetail.setColumnId(authDataDetailCopy.getColumnId());
                                            authDataDetail.setOperator(authDataDetailCopy.getOperator());
                                            authDataDetail.setValue(authDataDetailCopy.getValue());
                                            authDataDetail.setShowOrder(authDataDetailCopy.getShowOrder());
                                            authDataDetail.setRelation(authDataDetailCopy.getRelation());
                                            authDataDetail.setLeftParenthesis(authDataDetailCopy.getLeftParenthesis());
                                            authDataDetail.setRightParenthesis(authDataDetailCopy.getRightParenthesis());
                                            authDataDetailList.add(authDataDetail);
                                        }
                                        getService(AuthorityDataDetailService.class).save(authDataDetailList);
                                        // 清除缓存
                                        AuthorityUtil.getInstance().clearAuthority(AuthorityUtil.AUTHORITY_DATA, authorityData.getObjectId(),
                                                authorityData.getObjectType(), authorityData.getMenuId());
                                        AuthorityUtil.getInstance().clearRelateAuthority(authorityData.getObjectId(), authorityData.getObjectType(),
                                                authorityData.getMenuId());
                                    }
                                }
                            } else if (ConstantVar.AuthorityApprove.Operate.DELETE.equals(authorityApprove.getOperate())) {
                                // 删除
                                AuthorityDataCopy authorityDataCopy = getService(AuthorityDataCopyService.class).getByID(authorityApprove.getRelateAuthId());
                                if (authorityDataCopy != null) {
                                    AuthorityData oldAuthorityData = getService(AuthorityDataService.class).getAuthorityDataByName(
                                            authorityDataCopy.getObjectId(), authorityDataCopy.getObjectType(), authorityDataCopy.getMenuId(),
                                            authorityDataCopy.getComponentVersionId(), authorityDataCopy.getName());
                                    if (oldAuthorityData != null) {
                                        getService(AuthorityDataService.class).delete(oldAuthorityData);
                                    }
                                    getService(AuthorityDataCopyService.class).delete(authorityDataCopy);
                                }
                            }
                        } else if (ConstantVar.AuthorityApprove.Type.CODE.equals(authorityApprove.getAuthorityType())) {
                            if (ConstantVar.AuthorityApprove.Operate.NEW.equals(authorityApprove.getOperate())) {
                                // 新增，将Copy表数据复制到权限表
                            } else if (ConstantVar.AuthorityApprove.Operate.UPDATE.equals(authorityApprove.getOperate())) {
                                // 修改
                            } else if (ConstantVar.AuthorityApprove.Operate.DELETE.equals(authorityApprove.getOperate())) {
                                // 删除
                            }
                        }
                        authorityApprove.setStatus(status);
                        getDao().save(authorityApprove);
                    } else if (ConstantVar.AuthorityApprove.Status.APPROVED_BACK.equals(status)) {
                        // 审批退回
                        authorityApprove.setStatus(status);
                        getDao().save(authorityApprove);
                    }
                }
            }
            // 操作日志
            CommonUtil.addOperateLog("数据权限-三权分立", "审批数据权限", "");
            message = "审批成功！";
        } catch (Exception e) {
            success = false;
            message = "审批失败！";
            e.printStackTrace();
        }
        return "{'success':" + success + ", 'message':'" + message + "'}";
    }

    /**
     * 删除的数据权限待审批的记录（树权限使用）
     * 
     * @param objectId 对象ID: objectType=0时，则为角色KEY，当objectType=1时，则为用户ID
     * @param objectType 类型：0-角色 1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteAuthorityData(String objectId, String objectType, String menuId, String componentVersionId) {
        getDao().deleteAuthorityData(objectId, objectType, menuId, componentVersionId);
    }

    /**
     * 删除的数据权限待审批的记录（数据权限、编码权限使用）
     * 
     * @param relateAuthId 关联的权限ID
     */
    @Transactional
    public void deleteByRelateAuthId(String relateAuthId) {
        getDao().deleteByRelateAuthId(relateAuthId);
    }

    /**
     * 删除的数据权限待审批的记录
     * 
     * @param menuId 菜单ID
     */
    @Transactional
    public void deleteByMenuId(String menuId) {
        getDao().deleteByMenuId(menuId);
    }
}

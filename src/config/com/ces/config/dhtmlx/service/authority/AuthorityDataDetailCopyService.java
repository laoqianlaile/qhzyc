package com.ces.config.dhtmlx.service.authority;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.authority.AuthorityDataDetailCopyDao;
import com.ces.config.dhtmlx.entity.authority.AuthorityDataCopy;
import com.ces.config.dhtmlx.entity.authority.AuthorityDataDetailCopy;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;

/**
 * 数据权限详情Service（三权分立）
 * 
 * @author wanglei
 * @date 2015-07-21
 */
@Component
public class AuthorityDataDetailCopyService extends ConfigDefineDaoService<AuthorityDataDetailCopy, AuthorityDataDetailCopyDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.BaseDao)
     */
    @Autowired
    @Qualifier("authorityDataDetailCopyDao")
    @Override
    protected void setDaoUnBinding(AuthorityDataDetailCopyDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 根据数据权限ID获取数据权限详情
     * 
     * @param authorityDataCopyId 数据权限ID
     * @return List<AuthorityDataDetailCopy>
     */
    public List<AuthorityDataDetailCopy> getByAuthorityDataId(String authorityDataCopyId) {
        return getDao().getByAuthorityDataId(authorityDataCopyId);
    }

    /**
     * 根据数据权限ID和表ID获取数据权限详情
     * 
     * @param authorityDataId 数据权限ID
     * @param tableId 表ID
     * @return List<AuthorityDataDetail>
     */
    public List<AuthorityDataDetailCopy> getByAuthorityDataIdAndTableId(String authorityDataId, String tableId) {
        return getDao().getByAuthorityDataIdAndTableId(authorityDataId, tableId);
    }

    /**
     * 保存数据权限详情
     * 
     * @param authorityDataCopy 数据权限
     * @param rowsValue 数据权限详情
     */
    @Transactional
    public List<AuthorityDataDetailCopy> saveAuthorityDataDetails(AuthorityDataCopy authorityDataCopy, String rowsValue) {
        getDao().deleteByAuthorityDataId(authorityDataCopy.getId());
        String[] controlTableDetails = rowsValue.split("≡");
        List<AuthorityDataDetailCopy> list = new ArrayList<AuthorityDataDetailCopy>();
        for (String controlTableDetail : controlTableDetails) {
            String[] tempStrs = controlTableDetail.split("#");
            String controlTableId = tempStrs[0];
            String[] details = tempStrs[1].split(";");
            AuthorityDataDetailCopy authorityDataDetailCopy = null;
            int i = 0;
            for (String detail : details) {
                if (StringUtil.isNotEmpty(detail)) {
                    // 此处是防止detail字符串已,,,结尾而导致strs数组长度变短，加上", "使得数组长度+1
                    detail = detail + ", ";
                    String[] strs = detail.split(",");
                    authorityDataDetailCopy = new AuthorityDataDetailCopy();
                    authorityDataDetailCopy.setAuthorityDataId(authorityDataCopy.getId());
                    authorityDataDetailCopy.setTableId(controlTableId);
                    authorityDataDetailCopy.setRelation(strs[0]);
                    authorityDataDetailCopy.setLeftParenthesis(strs[1]);
                    authorityDataDetailCopy.setColumnId(strs[2]);
                    authorityDataDetailCopy.setOperator(strs[3]);
                    authorityDataDetailCopy.setValue(strs[4]);
                    authorityDataDetailCopy.setRightParenthesis(strs[5]);
                    authorityDataDetailCopy.setShowOrder(i++);
                    list.add(authorityDataDetailCopy);
                }
            }
        }
        return getDao().save(list);
    }

    /**
     * 删除数据权限并及联删除数据权限详情
     * 
     * @param id 主键ID
     */
    @Override
    @Transactional
    public void delete(String id) {
        String[] idArr = id.split(",");
        for (String authorityDataDetailCopyId : idArr) {
            getDao().delete(authorityDataDetailCopyId);
        }
    }
}

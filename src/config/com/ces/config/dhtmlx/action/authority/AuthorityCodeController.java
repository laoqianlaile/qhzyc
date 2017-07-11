package com.ces.config.dhtmlx.action.authority;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.authority.AuthorityCodeDao;
import com.ces.config.dhtmlx.entity.authority.AuthorityCode;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.service.authority.AuthorityCodeService;
import com.ces.config.dhtmlx.service.code.CodeService;
import com.ces.config.utils.EhcacheUtil;
import com.ces.config.utils.authority.AuthorityUtil;

/**
 * 编码权限Controller
 * 
 * @author luojinkai
 * @date 2015-3-12
 */
public class AuthorityCodeController extends ConfigDefineServiceDaoController<AuthorityCode, AuthorityCodeService, AuthorityCodeDao> {

    private static final long serialVersionUID = 1L;

    @Override
    protected void initModel() {
        setModel(new AuthorityCode());
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("authorityCodeService")
    protected void setService(AuthorityCodeService service) {
        super.setService(service);
    }

    /***
     * 保存
     * 
     * @return Object
     */
    public Object save() {
        String codeIds = getParameter("P_codeIds");
        AuthorityCode authorityCode = getService().findByObjectIdAndObjectTypeAndMenuIdAndComponentVersionIdAndCodeTypeCode(model);
        if (StringUtils.isNotEmpty(codeIds)) {
            String[] cArray = codeIds.split(",");
            String codeJson = parserCodeToJson(cArray, model);
            if (null != authorityCode) {
                authorityCode.setCodeJson(codeJson);
                try {
                    getService().save(authorityCode);
                    setReturnData("保存成功！");
                } catch (Exception e) {
                    setReturnData("保存失败！");
                }

            } else {
                model.setCodeJson(codeJson);
                try {
                    getService().save(model);
                    setReturnData("保存成功！");
                } catch (Exception e) {
                    setReturnData("保存失败！");
                }
            }
        } else {
            if (null != authorityCode) {
                authorityCode.setCodeJson("");
                try {
                    getService().save(authorityCode);
                    setReturnData("保存成功！");
                } catch (Exception e) {
                    setReturnData("保存失败！");
                }
            } else {
                try {
                    getService().save(model);
                    setReturnData("保存成功！");
                } catch (Exception e) {
                    setReturnData("保存失败！");
                }
            }
        }
        EhcacheUtil.removeAllCache(AuthorityUtil.AUTHORITY_CODE);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /***
     * @param codeIds 编码Ids[]
     * @param objectId 对象ID
     * @param objectType 类型
     * @return
     */
    private String parserCodeToJson(String[] codeIds, AuthorityCode authorityCode) {
        // TODO Auto-generated method stub
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < codeIds.length; i++) {
            Code code = getService(CodeService.class).getByID(codeIds[i]);
            sb.append(code.getValue()).append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /***
     * 获取编码权限
     * 
     * @return Object
     */
    public Object getAuthorityCode() {
        setReturnData(getService().findByObjectIdAndObjectTypeAndMenuIdAndComponentVersionIdAndCodeTypeCode(model));
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}

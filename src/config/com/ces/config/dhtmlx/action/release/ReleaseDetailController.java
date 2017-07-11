package com.ces.config.dhtmlx.action.release;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.release.ReleaseDetailDao;
import com.ces.config.dhtmlx.entity.release.ReleaseDetail;
import com.ces.config.dhtmlx.service.release.ReleaseDetailService;

/**
 * 系统发布详情Controller
 * 
 * @author wanglei
 * @date 2015-02-06
 */
public class ReleaseDetailController extends ConfigDefineServiceDaoController<ReleaseDetail, ReleaseDetailService, ReleaseDetailDao> {

    private static final long serialVersionUID = 8371039330586502438L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ReleaseDetail());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("releaseDetailService")
    @Override
    protected void setService(ReleaseDetailService service) {
        super.setService(service);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.action.base.StringIDConfigDefineServiceDaoController#beforeProcessTreeData(java.util.List)
     */
    @Override
    protected List<ReleaseDetail> beforeProcessTreeData(List<ReleaseDetail> data) {
        if (CollectionUtils.isNotEmpty(data)) {
            for (ReleaseDetail releaseDetail : data) {
                releaseDetail.setId(releaseDetail.getNodeId());
            }
        }
        return data;
    }

    /**
     * 获取发布系统或更新包的根节点
     * 
     * @return Object
     */
    public Object getReleaseDetailRootNode() {
        String systemReleaseId = getParameter("systemReleaseId");
        String dataId = getParameter("dataId");
        model = getService().getReleaseDetailRootNode(systemReleaseId, dataId);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}

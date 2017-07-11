package com.ces.component.qyptqtxxlb.action;

import java.util.List;
import java.util.Map;

import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.data.domain.Page;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.qyptqtxxlb.service.QyptqtxxlbService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.config.dhtmlx.json.entity.appmanage.GridData;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;

public class QyptqtxxlbController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, QyptqtxxlbService, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;
    private static Log log = LogFactory.getLog(TraceShowModuleDefineServiceDaoController.class);
    //消息ID
    private String xxId ;
    
    /*
     * (非 Javadoc)   
     * <p>标题: initModel</p>   
     * <p>描述: </p>      
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }
    
    /**
     * 修改消息状态为已阅读
     * @return
     */
    public String changeYdFlag(){
    	this.getService().changeYdFlag(xxId);
    	return NONE;
    }
    @Override
    public Object search() throws FatalException {
        try {
            long start = System.currentTimeMillis();
            // 查询参数
            TraceShowModuleDefineDaoService.SearchParameter parameter = getSearchParameter();
            //
            if (this.FRAME_NAME_DHTMLX.equals(getFrameName())) {
                list = new GridData();
            } else {
                list = getDataModel(getModelTemplate());
                processFilter(list);
            }
            Page<Object> page = getService().search(parameter);
			// 处理列表，未读信息加粗加黑
			List<Object> li = page.getContent();
			for (Object map : li) {
				Map<String, String> m = (Map) map;
				String ydFlag = m.get("YD");
				if ("0".equals(ydFlag)) {
					for (Map.Entry<String, String> entry : m.entrySet()) {
						if (!"ID".equals(entry.getKey())) {
							m.put(entry.getKey(), "<B>" + entry.getValue()
									+ "</B>");
						}
					}
				}
			}
            
            list.setData(page);
            log.debug("search total time: " + (System.currentTimeMillis() - start)  + " 毫秒.");
        } catch (Exception e) {
            log.error("列表查询出错", e);
        }
        
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

	public String getXxId() {
		return xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}


}
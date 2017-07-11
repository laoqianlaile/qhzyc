package com.ces.config.dhtmlx.action.appmanage;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.ReportPrintDao;
import com.ces.config.dhtmlx.entity.appmanage.Report;
import com.ces.config.dhtmlx.service.appmanage.ReportDefineService;
import com.ces.config.dhtmlx.service.appmanage.ReportPrintService;
import com.ces.xarch.core.exception.FatalException;

public class ReportPrintController extends ConfigDefineServiceDaoController<Report, ReportPrintService,ReportPrintDao> {

    private static final long serialVersionUID = 187280515880868153L;

    /*
     * (非 Javadoc)   
     * <p>标题: initModel</p>   
     * <p>描述: 初始化数据模型</p>      
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new Report());
    }
    

    /* (non-Javadoc)
	 * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
	 * @author Administrator
	 * @date 2013-10-10 17:20:36
	 */
	@Override
    @Autowired
    @Qualifier("reportPrintService")
	protected void setService(ReportPrintService service) {
		super.setService(service);
	}

	/**
	 * <p>描述: 取得打印数据</p>
	 * @return
	 * @author Administrator
	 * @date 2013-10-28  09:36:48
	 */
	public Object getReportData() throws FatalException{
		try{
			String reportId = getParameter("P_reportId");
			String tableId  = getParameter("P_tableId");
			String rowIds   = getParameter("P_rowIds");
			String timestamp= getParameter("P_timestamp");
	    	setReturnData(getService().getPrintData(reportId, tableId, rowIds, timestamp));
    	}catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    	return new DefaultHttpHeaders(SUCCESS).disableCaching();    	
    }
	/**
	 * <p>描述: 取得打印设置数据</p>
	 * @return
	 * @author Administrator
	 * @date 2013-10-28  09:35:07
	 */
	public Object getReportDefine(){
		String reportId=getParameter("P_reportId");
		setReturnData(getService(ReportDefineService.class).getByReportId(reportId));
		return new DefaultHttpHeaders(SUCCESS).disableCaching();  		
	}
}

package com.ces.component.sczzpzxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.sczzpzxx.service.SczzpzxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by bdz on 2015/8/12.
 */
public class SczzpzxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SczzpzxxService, TraceShowModuleDao> {

	@Override
	protected void initModel() {
		setModel(new StringIDEntity());
	}

	@Override
	@Autowired
	protected void setService(SczzpzxxService service) {
		super.setService(service);
	}

	/**
	 * 获取品类编号
	 * @return
	 */
	public Object getPlbh() {
		String plbh = getService().getPlbh();
		setReturnData(plbh);
		return SUCCESS;
	}

	/**
	 * 获取品类信息
	 * @author zhaoben
	 */
	public void getPlxx() {
		setReturnData(this.getService().getPlxx());
	}

	/**
	 * 验证品类编号的唯一性
	 * @author zhaoben
	 */
	public void uniqueCheck(){
		String plbh = getParameter("plbh");
		setReturnData(this.getService().uniqueCheck(plbh));
	}
	
	/**
	 * 根据品类获取底下品种信息
	 *
	 * @return
	 */
	public Object getPzxxByPlxx() {
		String plbh = getParameter("plbh");
		setReturnData(getService().getPzxxByPlxx(plbh));
		return SUCCESS;
	}

	/**
	 * 删除品种信息
	 * @return
	 */
	public Object delPzxxByPlxx() {
		String plbh = getParameter("plbh");
		String pzIds = getParameter("checkedPzIds");
		setReturnData(getService().delPzxxByPlxx(plbh, pzIds.split(",")));
		return SUCCESS;
	}
}
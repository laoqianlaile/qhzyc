package com.ces.component.trace.action;

import ces.sdk.system.bean.SystemInfo;
import ces.sdk.system.exception.SystemFacadeException;
import ces.sdk.system.facade.RoleInfoFacade;
import ces.sdk.system.facade.SystemFacade;
import ces.sdk.system.factory.SystemFacadeFactory;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.dao.TraceDao;
import com.ces.component.trace.service.TraceService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.security.entity.SysUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by 黄翔宇 on 15/5/4.
 */
public class TraceController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TraceService, TraceDao> {

	private static Log log = LogFactory.getLog(TraceController.class);

	@Override
	protected void initModel() {
		setModel(new StringIDEntity());
	}

	@Override
	@Autowired
	protected void setService(TraceService service) {
		super.setService(service);
	}

	@Autowired
	private MenuService menuService;

	/**
	 * 获取当前用户所拥有的系统
	 *
	 * @return
	 * @throws SystemFacadeException
	 */
	public Object getAllSystemByUser() throws SystemFacadeException {
		String userId = CommonUtil.getCurrentUserId();
		RoleInfoFacade roleInfoFacade = CommonUtil.getRoleInfoFacade();
		SystemFacade systemFacade = SystemFacadeFactory.newInstance().createSystemFacade();
		//返回值
		List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
		if (CommonUtil.isSuperRole()) {//超级管理员给予企业平台后台的访问权限
			Map<String, String> data = new HashMap<String, String>();
			Map<String, String> param = new HashMap<String, String>();
			param.put("name", "平台管理系统");
			SystemInfo system = systemFacade.findByCondition(param).get(0);
			data.put("sysShowName", "中信(" + system.getName() + ")");
			data.put("sysName", system.getName());
			data.put("sysCode", system.getCode());
			data.put("companyCode", "1");
			datas.add(data);
			setReturnData(datas);
			return SUCCESS;
		}
		Map<String, Set<String>> checkedSystemAndRoleByUserId = roleInfoFacade.findCheckedSystemAndRoleByUserId(userId);
		Set<String> systemIds = checkedSystemAndRoleByUserId.get("systemIds");
		List<SystemInfo> systems = new ArrayList<SystemInfo>();
		//用户所拥有的系统
		for (String systemId : systemIds) {
			if (!"1".equals(systemId)) {
				systems.add(systemFacade.findByID(systemId));
			}
		}
		//用户管理的门店
		List<Map<String, Object>> userAdminOrgs = getService().getUserAdminOrgs();

		//将门店与系统对应
		for (Map<String, Object> userAdminOrg : userAdminOrgs) {
			String mdbh = String.valueOf(userAdminOrg.get("ZHBH"));//企业平台的帐户编号（门店编号）
			String mdmc = String.valueOf(userAdminOrg.get("QYMC"));//企业平台的帐户编号（门店编号）
			String authId = String.valueOf(userAdminOrg.get("AUTH_ID"));//管理平台的orgId
			Map<String, Set<String>> checkedSystemAndRoleByOrgId = roleInfoFacade.findCheckedSystemAndRoleByOrgId(authId);
			Set<String> checkedSystems = checkedSystemAndRoleByOrgId.get("systemIds");
			for (String systemId : systemIds) {
				if (checkedSystems.contains(systemId)) {
					Map<String, String> data = new HashMap<String, String>();
					SystemInfo system = systemFacade.findByID(systemId);
					data.put("sysShowName", mdmc + "(" + system.getName() + ")");
					data.put("sysName", system.getName());
					data.put("sysCode", system.getCode());
					data.put("companyCode", mdbh);
					datas.add(data);
				}
			}
		}
		setReturnData(datas);
		return SUCCESS;
	}

	/**
	 * 初始化企业档案
	 *
	 * @return
	 */
	public Object initQyda() {
		String sysId = getParameter("sysId");
		String companyCode = getParameter("companyCode");
		if (sysId == null || "".equals(sysId) || CommonUtil.isSuperRole()) {
			return ERROR;
		}
		getService().initQyda(sysId, companyCode);
		return SUCCESS;
	}



	//上传文件文件名
	private String fileFileName;
	//上传文件对象
	private File file;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	/**
	 * 上传文件
	 * @return
	 */
	public Object upload() {
		String masterTableId = getParameter("masterTableId");
		String dataId = getParameter("rowId");
		String fjlx = getParameter("fjlx");
		boolean isOne = Boolean.parseBoolean(getParameter("isOne"));
		try {
			getService().upload(masterTableId, dataId, fjlx, fileFileName, file,isOne);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 获取平台管理系统——企业申请统计数据
	 * @return
	 */
	public Object getQysqtj(){
		String csrq = getParameter("csrq");
		String jzrq = getParameter("jzrq");
        setReturnData(getService().getQysqtj(csrq,jzrq));
		return null;
	}

    /**
     * 获取平台管理系统——企业新增统计数据
     * @return
     */
    public Object getQyxztj(){
        String csrq = getParameter("csrq");
        String jzrq = getParameter("jzrq");
        setReturnData(getService().getQyxztj(csrq, jzrq));
        return null;
    }

	/**
	 * 企业编码和子系统档案的门店名称
	 * @return
	 */
	public Object getQybmAndQymc() {
		String sysName = getParameter("sysName");
		setReturnData(getService().getQybmAndQymc(sysName));
		return SUCCESS;
	}
	public Object getXtpzcode() {
		String lxbm = getParameter("lxbm");
		setReturnData(getService().getXtpzcode(lxbm));
		return SUCCESS;
	}
	/**
	 * 获取数据字典编码数据
	 * @return
	 */
	public Object getDataDictionary() {
		String lxbm = getParameter("lxbm");
		setReturnData(getService().getDataDictionary(lxbm));
		return SUCCESS;
	}
	public Object getZzDplOrXplData(){
		String plbh = getParameter("plbh");
		setReturnData(getService().getZzdplOrxpzxx(plbh));
		return SUCCESS;
	}

	public Object getXplData(){
		String plbh = getParameter("plbh");
		setReturnData(getService().getXplData(plbh));
		return SUCCESS;
	}

	/**
	 * 种植修改密码模块
	 */
	public Object changePassword(){
		String ymm=getParameter("YMM");
		String xmm=getParameter("XMM");
		String qrmm=getParameter("QRMM");
		SysUser sysUser= CommonUtil.getUser();
		//sysUser.setPassword(xmm);
		System.out.println(sysUser.getPassword());
		System.out.println(sysUser.getLoginName());
		int i= CommonUtil.getUserInfoFacade().updateUserPasswd(sysUser.getLoginName(), ymm, xmm);
		// 1 原密码不对 3 用户不存在
		setReturnData(i);
		return SUCCESS;
	}

	/**
	 * 判断是否能够删除
	 * @return
	 */
	public Object canDelete(){
		String mk = getParameter("mk");
		String ids = getParameter("ids");
		setReturnData(getService().canDelete(mk,ids));
		return SUCCESS;
	}

	/**
	 * ickbh验重
	 * @return
	 */
	public Object isRepeatIckbh(){
		String ickbh = getParameter("ickbh");
		setReturnData(getService().isRepeatIckbh(ickbh));
		return SUCCESS;
	}

	/**
	 * 较验面积:当前等级面积之和不能大于上一等级面积
	 * @return
	 */
	public Object checkMj(){
		//当前输入面积
		String mj = getParameter("mj");
		//当前模块(表名)
		String mk = getParameter("mk");
		//上级模块编号
		String bh = getParameter("bh");
		//当前模块编号
		String sbh = getParameter("sbh");
		setReturnData(getService().checkMj(mj, mk, bh, sbh));
		return SUCCESS;
	}

	/**
	 * 根据菜单id查询对应的菜单名称
	 * @return
	 */
	public Object getMenuById(){
		String id = getParameter("id");
		setReturnData(menuService.getByID(id));
		return SUCCESS ;
	}

	/**
	 * 根据类型查询预估时间内的农事项
	 * @return
	 */
	public Object getYgNsx(){
		String lx = getParameter("lx");
		setReturnData(getService().getYgNsx(lx));
		return SUCCESS;
	}


	/**
	 * 统计分拣与包装
	 * @return
	 */
	public Object getCount(){
		setReturnData(getService().getCount());
		return SUCCESS;
	}

	/**
	 * 获得生产档案id
	 */
	public Object getParentId(){
		String pid=getParameter("id");
		String lx=getParameter("lx");
		setReturnData(getService().getParentId(pid, lx));
		return  null;
	}

	/**
	 * 获得企业编码公共方法
	 * @return
     */
	public Object getQybm(){
		setReturnData(SerialNumberUtil.getInstance().getCompanyCode());
		return null;
	}

}

package com.ces.component.trace.action;

import ces.coral.lang.StringUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.dao.CsglDao;
import com.ces.component.trace.service.CsglService;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.dhtmlx.service.resource.ResourceService;
import com.ces.config.service.base.StringIDConfigDefineDaoService;
import com.ces.config.utils.CfgCommonUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.authority.AuthorityUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by bdz on 2015/7/17.
 */
public class CsglController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CsglService, CsglDao> {

	private String zl;
	private String spbm;
	private String year;
	private String month;

	@Override
	protected void initModel() {
		setModel(new StringIDEntity());
	}

	@Override
	@Autowired
	protected void setService(CsglService service) {
		super.setService(service);
	}

	/**
	 * 蔬菜来源对比
	 *
	 * @return
	 */
	public Object getSclyCharts() {
		String city = CompanyInfoUtil.getInstance().getCityInfo();
		String spbm = getParameter("spbm");
		setReturnData(getService().getSclyCharts(city, spbm));
		return SUCCESS;
	}

	/**
	 * 肉品来源对比
	 *
	 * @return
	 */
	public Object getRplyCharts() {
		String city = CompanyInfoUtil.getInstance().getCityInfo();
		setReturnData(getService().getRplyCharts(city));
		return SUCCESS;
	}
	/**
	 * 菜肉出场价格统计
	 */
    public void queryCcjgxx() {
		if (StringUtil.isNotBlank(zl) && StringUtil.isNotBlank(spbm)) {
			this.setReturnData(this.getService().queryCcjgtj(year, month, zl, spbm));
		} else {
			this.setReturnData(this.getService().queryCcjgtj(year, month));
		}
	}

	/**
	 * 获得肉品进出对比的市场
	 */
	public Object getAllPrsc(){
		setReturnData(getService().getAllPrsc());
		return SUCCESS;
	}

	/**
	 * 肉品进出场对比
	 * @return
	 */
    public Object getRpjccdbChartByweek() {
        String id = getParameter("id");
        setReturnData(getService().getRpjccdbChartByweek(id));
        return SUCCESS;
    }

	/**
	 * 获得菜品进出对比的市场
	 */
	public Object getAllPcsc(){
		setReturnData(getService().getAllPcsc());
		return SUCCESS;
	}

	/**
     * 菜品进出场对比
     * @return
     */
    public Object getCpjccdbChartByweek() {
        String id = getParameter("id");
        setReturnData(getService().getCpjccdbChartByweek(id));
        return SUCCESS;
    }

	/**
	 * 获取所有的蔬菜商品信息
	 *
	 * @return
	 */
	public Object getAllScFoods() {
		setReturnData(getService().getAllScFoods());
		return SUCCESS;
	}
	/**
	 * 获取所有的蔬菜商品信息
	 *
	 * @return
	 */
	public Object getAllRpFoods() {
		setReturnData(getService().getAllRpFoods());
		return SUCCESS;
	}

	public String getMonth() {
		return month;
	}

    public void setYear(String year) {
        this.year = year;
    }
	public String getZl() {
		return zl;
	}

	public void setZl(String zl) {
		this.zl = zl;
	}

	public String getSpbm() {
		return spbm;
	}

	public void setSpbm(String spbm) {
		this.spbm = spbm;
	}
	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	/**
	 * 获取菜单
	 * @return
	 */
	public Object getMenu() {
        boolean bool;
		String parentMenuId = getParameter("parentMenuId");
        List<String> authResourceMenuIdList = getRootMenu();
        List<Menu> menus = getService(MenuService.class).getMenuByParentId(parentMenuId);
        Iterator<Menu> iterator = menus.iterator();
        while (iterator.hasNext()) {
            Menu menu = iterator.next();
            bool = true;
            for(String authResourceMenuId:authResourceMenuIdList){
                if(authResourceMenuIdList.contains(menu.getId())){
                    bool = false;
                }
            }
            if(bool){
                iterator.remove();
            }
        }
		setReturnData(menus);
		return SUCCESS;
	}



	public List<String> getRootMenu() {
		boolean isSuperRole = CommonUtil.isSuperRole();
		// 是否需要使用权限过滤
		boolean notUseAuth = isSuperRole && !CfgCommonUtil.isReleasedSystem();
		// 获取有权限的菜单ID
		List<String> authResourceMenuIdList = null;
		if (!notUseAuth) {
			authResourceMenuIdList = getAuthResourceMenuIdList(isSuperRole);
		}
		return authResourceMenuIdList;
	}


    /**
     * 获取有权限的菜单ID
     *
     * @return List<String>
     */
    private List<String> getAuthResourceMenuIdList(boolean isSuperRole) {
        List<String> resourceMenuIdList = null;
        if (!isSuperRole) {
            resourceMenuIdList = AuthorityUtil.getInstance().getMenuAuthority(getSystemId());
        } else {
            if (CfgCommonUtil.isReleasedSystem()) {
                resourceMenuIdList = getService(ResourceService.class).getAllCanUseMenuId();
            }
        }
        return resourceMenuIdList;
    }


    /**
     * 获取系统ID
     *
     * @return String
     */
    private String getSystemId() {
        String systemId = getParameter("systemId");
        if (com.ces.config.utils.StringUtil.isEmpty(systemId)) {
            systemId = CommonUtil.getSystemId();
        }
        return systemId;
    }



}

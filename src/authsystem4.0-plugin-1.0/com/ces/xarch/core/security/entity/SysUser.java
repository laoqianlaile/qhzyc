/**
 * <p>Copyright:Copyright(c) 2013</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * <p>包名:com.ces.xarch.core.security.entity</p>
 * <p>文件名:User.java</p>
 * <p>类更新历史信息</p>
 * @todo Reamy(杨木江 yangmujiang@sohu.com) 创建于 2013-04-08 11:57:58
 */
package com.ces.xarch.core.security.entity;

import java.util.List;

import javax.persistence.Transient;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Spring Security3认证用户实体.
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Reamy(杨木江 yangmujiang@sohu.com)
 * @date 2013-04-08  11:57:58
 * @version 1.0.2013.0408
 */
public class SysUser extends StringIDEntity implements UserDetails,CredentialsContainer {
	private static final long serialVersionUID = 2396936135265130787L;

	/** 登录帐号 */
	private String loginName;
	/** 姓名 */
	private String name;
	/** 密码 */
	private String password;
	/** 管辖的组织ID */
	private String adminOrgId;
	/** 所属的组织ID */
	private String belongOrgId;
	/** 电子邮箱 */
	private String email;
	/** 电话 */
	private String telephone;
	/** 手机 */
	private String mobile;
	/** IC卡号 */
	private String icNo;
	/** 工号 */
	private String jobNo;
	/** 职称 */
	private String title;
	/** 排序号 */
	private Long showOrder;
	/** 备注 */
	private String comments;
	/** 是否查询 */
	private String isQuery;
	/** 是否锁定 */
	private String flagAction;
	/** 用户岗位 */
	private String station;
	/** 状态 */
	private String status;
	/** 最后登录时间 */
	private String lastLoginDate;
	/** 最后修改密码时间 */
	private String lastModifyPsd;
	/** 用户允许最大并发数 */
	private String allowclientnum;
	/** 临时用户 */
	private String isTemp;
	/** 有效开始时间 */
	private String tempStart;
	/** 有效结束时间 */
	private String tempEnd;
	/** 扩展字段一 */
	private String flag1;
	/** 扩展字段二 */
	private String flag2;
	/** 扩展字段三 */
	private String flag3;
	/** 扩展字段四 */
	private String flag4;
	/** 五 */
	private String flag5;
	/** 扩展字段六 */
	private String flag6;
	/** 扩展字段七 */
	private String flag7;
	/** 扩展字段八 */
	private String flag8;
	/** 扩展字段九 */
	private String flag9;
	/** 扩展字段十 */
	private String flag10;
	
	/**是否开启 */
	private boolean enabled;
	/** 用户组织(当前访问的用户组织ID) */
	private String parentId;
	/** 树上节点勾选 */
	private boolean checked = false;
	
	/** 用户对应的角色ID */
	private String roleId ;
	
	/** authorities(List<GrantedAuthority>):用户角色列表. */
	private List<GrantedAuthority> authorities;


	private String roleIds;
	
	/**
	 * 设置密码.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-04-08  12:08:33
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-04-08 11:58:25
	 */
	@Transient
	@JsonIgnore
	public List<GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	/**
	 * 设置用户角色列表.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-04-08  14:56:03
	 */
	public void setAuthorities(List<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getPassword()
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-04-08 11:58:25
	 */
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getUsername()
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-04-08 11:58:25
	 */
	@Transient
	public String getUsername() {
		return loginName;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-04-08 11:58:25
	 */
	@Transient
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-04-08 11:58:25
	 */
	@Transient
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-04-08 11:58:25
	 */
	@Transient
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-04-08 11:58:25
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.CredentialsContainer#eraseCredentials()
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-04-08 12:13:39
	 */
	@Transient
	@JsonIgnore
	public void eraseCredentials() {
		authorities = null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-04-28 17:48:06
	 */
	@Override
	@Transient
	@JsonIgnore
	public int hashCode() {
		if (id == null) {
			return super.hashCode();
		}
		
		return id.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-04-28 17:58:26
	 */
	@Override
	@Transient
	@JsonIgnore
	public boolean equals(Object obj) {
		if (obj instanceof SysUser) {
			if (id == null) {
				return (((SysUser)obj).id == null);
			} else {
				return id.equals(((SysUser)obj).id);
			}
		}
		
		return false;
	}


	/**
	 * @return 返回  String loginName
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * @param 设置  String loginName
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * @return 返回  String name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param 设置  String name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return 返回  String email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param 设置  String email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return 返回  String telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param 设置  String telephone
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @return 返回  String mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param 设置  String mobile
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return 返回  String icNo
	 */
	public String getIcNo() {
		return icNo;
	}

	/**
	 * @param 设置  String icNo
	 */
	public void setIcNo(String icNo) {
		this.icNo = icNo;
	}

	/**
	 * @return 返回  String jobNo
	 */
	public String getJobNo() {
		return jobNo;
	}

	/**
	 * @param 设置  String jobNo
	 */
	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	/**
	 * @return 返回  String title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param 设置  String title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return 返回  String comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param 设置  String comments
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return 返回  String isQuery
	 */
	public String getIsQuery() {
		return isQuery;
	}

	/**
	 * @param 设置  String isQuery
	 */
	public void setIsQuery(String isQuery) {
		this.isQuery = isQuery;
	}

	/**
	 * @return 返回  String flagAction
	 */
	public String getFlagAction() {
		return flagAction;
	}

	/**
	 * @param 设置  String flagAction
	 */
	public void setFlagAction(String flagAction) {
		this.flagAction = flagAction;
	}

	/**
	 * @return 返回  String station
	 */
	public String getStation() {
		return station;
	}

	/**
	 * @param 设置  String station
	 */
	public void setStation(String station) {
		this.station = station;
	}

	/**
	 * @return 返回  String status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param 设置  String status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return 返回  String lastLoginDate
	 */
	public String getLastLoginDate() {
		return lastLoginDate;
	}

	/**
	 * @param 设置  String lastLoginDate
	 */
	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	/**
	 * @return 返回  String lastModifyPsd
	 */
	public String getLastModifyPsd() {
		return lastModifyPsd;
	}

	/**
	 * @param 设置  String lastModifyPsd
	 */
	public void setLastModifyPsd(String lastModifyPsd) {
		this.lastModifyPsd = lastModifyPsd;
	}

	/**
	 * @return 返回  String allowclientnum
	 */
	public String getAllowclientnum() {
		return allowclientnum;
	}

	/**
	 * @param 设置  String allowclientnum
	 */
	public void setAllowclientnum(String allowclientnum) {
		this.allowclientnum = allowclientnum;
	}

	/**
	 * @return 返回  String isTemp
	 */
	public String getIsTemp() {
		return isTemp;
	}

	/**
	 * @param 设置  String isTemp
	 */
	public void setIsTemp(String isTemp) {
		this.isTemp = isTemp;
	}

	/**
	 * @return 返回  String tempStart
	 */
	public String getTempStart() {
		return tempStart;
	}

	/**
	 * @param 设置  String tempStart
	 */
	public void setTempStart(String tempStart) {
		this.tempStart = tempStart;
	}

	/**
	 * @return 返回  String tempEnd
	 */
	public String getTempEnd() {
		return tempEnd;
	}

	/**
	 * @param 设置  String tempEnd
	 */
	public void setTempEnd(String tempEnd) {
		this.tempEnd = tempEnd;
	}

	/**
	 * @return 返回  String flag1
	 */
	public String getFlag1() {
		return flag1;
	}

	/**
	 * @param 设置  String flag1
	 */
	public void setFlag1(String flag1) {
		this.flag1 = flag1;
	}

	/**
	 * @return 返回  String flag2
	 */
	public String getFlag2() {
		return flag2;
	}

	/**
	 * @param 设置  String flag2
	 */
	public void setFlag2(String flag2) {
		this.flag2 = flag2;
	}

	/**
	 * @return 返回  String flag3
	 */
	public String getFlag3() {
		return flag3;
	}

	/**
	 * @param 设置  String flag3
	 */
	public void setFlag3(String flag3) {
		this.flag3 = flag3;
	}

	/**
	 * @return 返回  String flag4
	 */
	public String getFlag4() {
		return flag4;
	}

	/**
	 * @param 设置  String flag4
	 */
	public void setFlag4(String flag4) {
		this.flag4 = flag4;
	}

	/**
	 * @return 返回  String flag5
	 */
	public String getFlag5() {
		return flag5;
	}

	/**
	 * @param 设置  String flag5
	 */
	public void setFlag5(String flag5) {
		this.flag5 = flag5;
	}

	/**
	 * @return 返回  String flag6
	 */
	public String getFlag6() {
		return flag6;
	}

	/**
	 * @param 设置  String flag6
	 */
	public void setFlag6(String flag6) {
		this.flag6 = flag6;
	}

	/**
	 * @return 返回  String flag7
	 */
	public String getFlag7() {
		return flag7;
	}

	/**
	 * @param 设置  String flag7
	 */
	public void setFlag7(String flag7) {
		this.flag7 = flag7;
	}

	/**
	 * @return 返回  String flag8
	 */
	public String getFlag8() {
		return flag8;
	}

	/**
	 * @param 设置  String flag8
	 */
	public void setFlag8(String flag8) {
		this.flag8 = flag8;
	}

	/**
	 * @return 返回  String flag9
	 */
	public String getFlag9() {
		return flag9;
	}

	/**
	 * @param 设置  String flag9
	 */
	public void setFlag9(String flag9) {
		this.flag9 = flag9;
	}

	/**
	 * @return 返回  String flag10
	 */
	public String getFlag10() {
		return flag10;
	}

	/**
	 * @param 设置  String flag10
	 */
	public void setFlag10(String flag10) {
		this.flag10 = flag10;
	}

	/**
	 * @return 返回  String parentId
	 */
	@Transient
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param 设置  String parentId
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return 返回  Long showOrder
	 */
	public Long getShowOrder() {
		return showOrder;
	}

	/**
	 * @param 设置  Long showOrder
	 */
	public void setShowOrder(Long showOrder) {
		this.showOrder = showOrder;
	}

	/**
	 * @param 设置  String password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return 返回  String roleId
	 */
	public String getRoleId() {
		return roleId;
	}

	/**
	 * @param 设置  String roleId
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return 返回  String adminOrgId
	 */
	public String getAdminOrgId() {
		return adminOrgId;
	}

	/**
	 * @param 设置  String adminOrgId
	 */
	public void setAdminOrgId(String adminOrgId) {
		this.adminOrgId = adminOrgId;
	}

	/**
	 * @return 返回  String belongOrgId
	 */
	public String getBelongOrgId() {
		return belongOrgId;
	}

	/**
	 * @param 设置  String belongOrgId
	 */
	public void setBelongOrgId(String belongOrgId) {
		this.belongOrgId = belongOrgId;
	}

	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
}

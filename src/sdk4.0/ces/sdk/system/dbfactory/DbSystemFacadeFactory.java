package ces.sdk.system.dbfactory;

import ces.sdk.system.dbfacade.*;
import ces.sdk.system.facade.*;
import ces.sdk.system.factory.SystemFacadeFactory;

public class DbSystemFacadeFactory extends SystemFacadeFactory {


	public OpLogInfoFacade createOpLogInfoFacade() {
		DBOpLogInfoFacade opLogInfoFacade = new DBOpLogInfoFacade();
		return opLogInfoFacade;
	}

	public OrgInfoFacade createOrgInfoFacade() {
		OrgInfoFacade orgFacade = new DBOrgInfoFacade();
		return orgFacade;
	}

	public ResourceInfoFacade createResourceInfoFacade() {
		ResourceInfoFacade facade = new DBResourceInfoFacade();
		return facade;
	}

	public RoleInfoFacade createRoleInfoFacade() {
		RoleInfoFacade facade = new DBRoleInfoFacade();
		return facade;
	}

	public UserInfoFacade createUserInfoFacade() {
		UserInfoFacade facade = new DBUserInfoFacade();
		return facade;
	}

	public void destorySystemFacadeContext(
			SystemFacadeContext systemFacadeContext) {

	}

	public void initSystemFacadeContext(SystemFacadeContext systemFacadeContext) {

	}


	@Override
	public OrgTypeInfoFacade createOrgTypeFacade() {
		OrgTypeInfoFacade orgTypeFacade = new DBOrgTypeInfoFacade();
		return orgTypeFacade;
	}

	@Override
	public SystemFacade createSystemFacade() {
		SystemFacade facade = new DBSystemFacade(); 
		return facade;
	}
}

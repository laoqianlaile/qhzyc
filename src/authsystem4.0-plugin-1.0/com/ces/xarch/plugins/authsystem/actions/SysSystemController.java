package com.ces.xarch.plugins.authsystem.actions;

import com.ces.xarch.core.security.entity.SysSystem;
import com.ces.xarch.plugins.authsystem.service.SysSystemService;
import com.ces.xarch.plugins.core.actions.StringIDDefineServiceAuthSystemController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SysSystemController extends StringIDDefineServiceAuthSystemController<SysSystem, SysSystemService>{
	
	@Override
	protected void initModel() {
		setModel(new SysSystem());
	}
	
	@Override
	@Autowired
	protected void setService(SysSystemService service) {
		super.setService(service);
	}

}

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String dhxResPath = path + com.ces.config.dhtmlx.utils.DhtmlxCommonUtil.DHX_FOLDER;
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>系统配置平台1.0-工作流定义</title>
		<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/appdefinebase.js"></script>
		<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/workflowTree.js"></script>
		<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/workflowDefine.js"></script>
		<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/workflowVersion.js"></script>
		<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/workflowSetting.js"></script>
		<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/workflowDefineTool.js"></script>
		<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/workflowModuleBinded.js"></script>
		<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/workflowFormSetting.js"></script>

	</head>
	<body scroll="no" onload="init()">
<script type="text/javascript">
	
	var _initObj = null;
	
	var app = {tableId: "", menuId: "-1"};
	
	function init() {
		var that = this;
		that.nodeId   = "-1";
		that.nodeType = "0";
		var dhxLayout = new dhtmlXLayoutObject("content", "2U");
		initLayout(dhxLayout, true);
		
		var layoutA = dhxLayout.cells("a");
		var layoutB = dhxLayout.cells("b");
		that.aLayout = layoutA;
		that.bLayout = layoutB;
		layoutA.setWidth(240);
		layoutA.setText("工作流分类树");
		
		initWorkflowTree(that);
		layoutB.hideHeader();
		workflowDefineGrid(that);
	}
	
	/**
	 * 初始化新增工作流页面
	 */
	function initCreateLayout(that) {
		var subLayout = that.bLayout.attachLayout("2E");
		var layoutA = subLayout.cells("a");
		var layoutB = subLayout.cells("b");
		// 2 init layout b cell(grid)	
		initWfGrid(layoutA, that);
		// 3. init layout c cell(form)
		initWfForm(layoutB, that);
		// 4. set create layout flag
		that.createLayout = true;
		// 5. set config layout flag
		that.configLayout = false;
	}
	/**
	 * 工作流页面数据重新加载
	 */
	function reloadCreateLayout(that) {
		// reload grid data
		that.reloadGrid();
		// reset form
		that.resetForm();
	}
	/**
	 * 初始化工作流页面配置
	 */
	function initConfigLayout(that) {
		//
		if (null != that.gtbar) {
			that.gtbar.unload();
			that.gtbar = null;
		}
		if (null != that.grid) {
			that.grid.destructor();
			that.grid = null;
		}
	    if (null != that.ftbar) {
	    	that.ftbar.unload();
			that.ftbar = null;
	    }
		if (null != that.form) {
			that.form.unload();
		    that.form = null;
		}
		
		that.defineInited = that.moduleBindedInited = that.formSettingInited = false;
		
		that.tabbar = that.bLayout.attachTabbar();
		var tabbar = that.tabbar;
	    tabbar.setImagePath(IMAGE_PATH);

	    //tabbar.addTab("tab$01", "第一步：定义工作流", "160px");
	    tabbar.addTab("tab$02", "第一步：绑定工作流", "160px");	
	    tabbar.addTab("tab$03", "第二步：界面绑定", "160px");	    
	    
	    tabbar.setTabActive("tab$02");
	    initModuleBinded(that);
	    //tabbar.setTabActive("tab$01");
	    //initDefine(that);
	    //initFormSetting(that);
	    //initModuleBinded(that);
	    
	    tabbar.attachEvent("onSelect", function(id,last_id){
	    	/*if (id == "tab$01") {
	    		if (that.defineInited) {
	    			that.reloadDefine();
	    		} else {
	    			initDefine(that); // 工作流定义
	    		}
	    	} else */if (id == "tab$02") {
	    		if (that.moduleBindedInited) {
	    			that.reloadModuleBinded();
	    		} else {
	    			initModuleBinded(that);// 工作流与模块绑定
	    		}
	    	} else if (id == "tab$03") {
	    		if (that.formSettingInited) {
	    			that.reloadFormSetting();
	    		} else {
	    			initFormSetting(that);// 工作流与模块绑定
	    		}
	    	}//*/
   			return true;
	   	});
	    // set init config layout flag (load complete)
	    that.configLayout = true;
		// set create layout flag
		that.createLayout = false;
	}
	/**
	 * 工作流配置数据重新加载
	 */
	function reloadConfigLayout(that) {
		//
		/*if ("tab$01" == that.tabbar.getActiveTab()) {
			that.reloadDefine();
		} else*/ if ("tab$02" == that.tabbar.getActiveTab()) {
			that.reloadModuleBinded();
		} else if ("tab$03" == that.tabbar.getActiveTab()) {
			that.reloadFormSetting();
		}
	}
</script>		
	</body>
</html>

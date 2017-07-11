
/**
 * 自定义action访问URI
 */
AppActionURI = function() {
	var actionPrefix = contextPath + "/appmanage/";
	return {
		treeDefine		:actionPrefix + "tree-define",		//档案树定义表
		tableTree	  :actionPrefix + "table-tree",		   //表定义树构造
		tableClassification :actionPrefix + "table-classification",//物理表分类节点
		logicClassification :actionPrefix + "logic-classification",//逻辑表分类节点
		logicTableDefine: actionPrefix + "logic-table-define", //逻辑表定义
		logicGroupDefine: actionPrefix + "logic-group-define", //逻辑表组定义
		logicGroupRelation: actionPrefix + "logic-group-relation", //逻辑表组和逻辑表关系表
		logicTableRelation: actionPrefix + "logic-table-relation", //逻辑表在逻辑表组中的表关系
		physicalTableDefine: actionPrefix + "physical-table-define", //物理表定义
		physicalGroupDefine: actionPrefix + "physical-group-define", //物理表组定义
		physicalGroupRelation: actionPrefix + "physical-group-relation", //物理表组和物理表关系表
		tableDefine   : actionPrefix + "table-define",     // 表定义
		tableRelation : actionPrefix + "table-relation",   // 表关系定义
		typeLabel     : contextPath + "/label/type-label",       // 表标签
		tableLabel    : actionPrefix + "table-label",      // 表标签
		tableImpExport: actionPrefix + "table-imp-export", // 表导入
		columnDefine  : actionPrefix + "column-define",    // 字段定义
		columnLabel   : contextPath + "/label/column-label",     // 字段标签
		columnRelation: actionPrefix + "column-relation",  // 字段关系定义
		columnSplice  : actionPrefix + "column-splice",    // 字段拼接
		columnSplit   : actionPrefix + "column-split",     // 字段截取
		columnOperation: actionPrefix + "column-operation",// 字段求和、最值
		columnInherit : actionPrefix + "column-inherit",   // 字段继承
		columnBusiness: actionPrefix + "column-business",  // 字段特殊业务
		
		appDefine : actionPrefix + "app-define",           // 应用定义列表
		appButton : actionPrefix + "app-button",           // 工具条定义
		appSearch : actionPrefix + "app-search",           // 检索定义
		appGreatSearch : actionPrefix + "app-great-search",           // 高级检索定义
		appSearchCondition: actionPrefix + "app-search-condition", // 高级检索用户条件
		appIntegrationSearch : actionPrefix + "app-integration-search",           // 一体化检索定义
		appSearchPanel: actionPrefix + "app-search-panel",// 检索页面设置
		appGrid   : actionPrefix + "app-grid",             // 列表定义
		appColumn : actionPrefix + "app-column",           // 列表表头定义
		appFilter : actionPrefix + "app-filter",           // 过滤条件定义
		appSort   : actionPrefix + "app-sort",             // 排序定义
		appForm   : actionPrefix + "app-form",             // 界面定义
		appFormElement : actionPrefix + "app-form-element",//界面元素定义
		appReport : actionPrefix + "app-report",//应用定义中报表定义
		showModule: actionPrefix + "show-module",          // 构件展现
		
		report            : actionPrefix + "report",             // 报表定义
		reportDataSource  : actionPrefix + "report-data-source", // 报表数据源定义
		reportTableRelation: actionPrefix + "report-table-relation", // 报表数据源定义
		reportDefine      : actionPrefix + "report-define",      // 报表制作
		reportPrint       : actionPrefix + "report-print",       // 打印报表
		reportPrintSetting: actionPrefix + "report-print-setting", //报表打印设置
		reportTable       : actionPrefix + "report-table",       // 报表对应的表

		treeReserveArea : actionPrefix + "tree-reserve-area",// 树预留区配置
		treeDefine      : actionPrefix + "tree-define",      // 树定义
		
		workflowTree           : actionPrefix + "workflow-tree",          // 工作流分类树
		workflowDefine         : actionPrefix + "workflow-define",        // 工作流定义
		workflowVersion        : actionPrefix + "workflow-version",       // 工作流版本定义
		workflowFormSetting  : actionPrefix + "workflow-form-setting", // 工作流表单个性设置
		workflowButtonSetting: actionPrefix + "workflow-button-setting", // 工作流按钮个性设置
		workflowAssistOpinion: actionPrefix + "workflow-assist-opinion", // 工作流辅助意见设置
		
		module : actionPrefix + "module" // 构件布局定义
	};
}();

/**
 * 工作流流程定义
 * @param that
 */
function initDefine(that) {
	var tbbar = that.tabbar.cells("tab$01");
	tabbar.setContentHTML("tab$01", "<br/>工作流客户端设置工具未集成.");
	// reload binded module
	that.reloadDefine = function() {
		var tbbar = that.tabbar.cells("tab$01");
		tabbar.setContentHTML("tab$01", "<br/>工作流客户端设置工具未集成.");
	};
	// 标记已加载
	that.defineInited = true;
}
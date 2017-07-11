<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="com.ces.config.utils.CommonUtil" %>
<%
    String name = CommonUtil.getUser().getName() ;
    String username = CommonUtil.getUser().getUsername();
%>
<script type="text/javascript">
/***************************************************!
 * @author qiucs 
 * @date   2014-7-15
 * 系统配置平台应用自定义二次开发JS模板 
 ***************************************************/
 
 
(function(subffix) {
/**
 * 二次开发定位自己controller
 * @returns {String}
 **/
window[CFG_actionName(subffix)] = function (ui) {
	// ui.assembleData 就是 configInfo
	return $.contextPath + "/zzczgfgl";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	// ui.assembleData 就是 configInfo
	//console.log("override grid!");
	//ui.getAction = function () { 
	//	return $.contextPath + "/appmanage/show-module";
	//};
};
/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
	ui.beforeInitGrid = function(setting) {
		setting.fitStyle = "width";
		return setting;
	};

	ui.beforeInitGrid = function (setting) {
		for (i in setting.colModel) {
			var model = setting.colModel[i];
            model.formatter = function(value, options, rowObj) {
               // debugger
                var id = rowObj[ui.gcfg.keyColumn],
                        cgridDivId = ui.element.attr("id");
                //获得用户登录名
                var userName = "<%=username%>";
                //获得用户名称
                var name = "<%=name%>";
                //判断value是否是上传人，是则将其显示为用户名称
                if(value==userName){
                    value = name;
                }
                return value;
            };
			/*if (model.formatter) {
				model.formatter = function(value, options, rowObj) {
					var id = rowObj[ui.gcfg.keyColumn],
							cgridDivId = ui.element.attr("id");
					return ui.gcfg.linkContent.replace(/\{1\}/g, cgridDivId).replace(/\{2\}/g, id).replace(/javascript/g,$.contextPath + "/zzczgfgl!downLoad?id="+id+"&");
				};
			}*/
		}
		return setting;
	}
};
/**
 *  二次开发：复写自定义树
 */
function _override_tree (ui) {
	// ui.assembleData 就是 configInfo
	//console.log("override tree!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
};
/**
 *  二次开发：复写自定义工具条
 */
function _override_tbar (ui) {
	// ui.assembleData 就是 configInfo
	//console.log("override tbar!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
};
/**
 *  二次开发：复写自定义布局
 */
function _override_layout (ui) {
	//console.log("override layout!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
};







	/**
	 *  二次开发：复写基本查询区
	 */
	function _override_bsearch (ui) {
//		ui._init = function() {
//			ui.callItemMethod("QVMC", "destroy");
//			ui.getItemJQ("QVMC").textbox({});
//			ui.callItemMethod("DKBH", "destroy");
//			ui.getItemJQ("DKBH").textbox({});
//		};
		// ui.assembleData 就是 configInfo
		//console.log("override search!");
		//ui.bindEvent = function () {
		// 添加onChange事件
		//	  ui.setItemOption("NAME", "onChange", function(e, data) {})
		//};
	};
	/**
	 *  二次开发：复写高级查询区
	 */
	function _override_gsearch (ui) {
		// ui.assembleData 就是 configInfo
		//console.log("override search!");
		//ui.bindEvent = function () {
		// 添加onChange事件
		//	  ui.setItemOption("NAME", "onChange", function(e, data) {})
		//};
	};







	/**
	 * 在此可以复写所有自定义JS类
	 * @param selector
	 * @returns {JQ_override}
	 */
	window[CFG_overrideName(subffix)] = function () {
		//var startTime = new Date().getTime();

		if (this instanceof $.config.cform) {
			_override_form(this);
		} else if (this instanceof $.config.cgrid) {
			_override_grid(this);
		} else if (this instanceof $.config.cbsearch) {
			_override_bsearch(this);
		} else if (this instanceof $.config.cgsearch) {
			_override_gsearch(this);
		} else if (this instanceof $.config.ctree) {
			_override_tree(this);
		} else if (this instanceof $.config.ctbar) {
			_override_tbar(this);
		} else if (this instanceof $.config.clayout) {
			_override_layout(this);
		}

		//console.log("over ride cost time: " + (new Date().getTime() - startTime));
	};


	
	
	
	
})("${timestamp}");
</script>

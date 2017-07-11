<%@page language="java" pageEncoding="UTF-8"%>
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
window[CFG_actionName(subffix)] = function () {
	// this.assembleData 就是 configInfo
	return $.contextPath + "/appmanage/show-module";
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
	
	if (ui.options.number === 1) {
		/**
		 * 绑定自定按钮的事件
		 */
		ui.clickSecondDev = function (id) {
			/**
			 * 从卡内读取用户卡信息在表单上显示
			 */
			if (id == $.config.preButtonId + "dk") {
				if (isSwt) {
					var result = _window("readCard", "userInfo");
					var resultData = JSON.parse(result);
					var value = resultData.value;
					if (resultData.status == "true" || resultData.status == true) {
						ui.setFormData({
							A_LTKH : value.cardNo,
							A_LTKLX : value.cardType,
							A_JYZBM : value.userId,
							B_JYZMC : value.userName,
							B_JYZXZ : value.userType,
							B_GSZCDJZHHSFZH : value.userCertificate,
							B_SJHM : value.userPhone,
							B_FRDB : value.other
						});
						$.message( {message:"读卡成功", cls:"info"});
					} else {
						$.message( {message:"读卡失败，请重试", cls:"warning"});
					}
				} else {
					$.alert("请在程序中操作");
				}
			}
		};
		/*
		ui.addOutputValue("cardView",function(o) {//查看卡信息
			var o = {
				status : false,
				operate : "cardView"
			}
			if (isSwt) {
				var result = _window("readCard", "userInfo");
				var resultData = JSON.parse(result);
				if (resultData.status == "true" || resultData.status == true) {
					o.value = resultData.value.cardNo;
					o.status = true;
				} else {
					$.alert("读卡失败：" + resultData.msg);
				}
			} else {
				$.alert("请在程序环境中读卡");
			}
			return o;
		});
		ui.addCallback("getResultView",function(res) {
			var operate = res.operate;
			var result = res.result;
			var value = res.value;
			if (result == true || result == "true") {
				if (operate = "cardView") {
					ui.setFormData(value);
					$.message( {message:"读卡成功", cls:"info"});
				}
			} else {
				$.message( {message:"读卡失败，请重试", cls:"warning"});
			}
		});
		*/
	}
};
/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
	// ui.assembleData 就是 configInfo
	//console.log("override grid!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
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
	if(ui.options.number == 1){
		ui.processItems = function (data) {
			var btns = [];
			btns.push({
				id:$.config.preButtonId + "dk",
				label: "读卡",
				type : "button"
			});
			for (var i = 0; i < data.length; i++) {
				btns.push(data[i]);
			}
			return btns;
		};
	}
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

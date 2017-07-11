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
window[CFG_actionName(subffix)] = function (ui) {
	// ui.assembleData 就是 configInfo
	return $.contextPath + "/zlaqyj";
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
//	ui.getSelectedRowId();
	ui.clickSecondDev=function(id){
		if(id==$.config.preButtonId +"warning"){
			var ids=ui.getSelectedRowId();
			var idsjson={m:'34',k:'12'};
			//var
//			for(var m=0;m<ids.length;m++){
//				idsjson.append(m,ids[m])
//			}

			var Mapdata= $.loadJson($.contextPath+"/zlaqyj!sendGroupMessage.json?ids="+ids);


		}
	}
	// ui.assembleData 就是 configInfo
	//console.log("override grid!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};

    ui.beforeInitGrid = function (setting) {
        setting.colNames.push("操作");
        setting.colModel.push({'formatter':function(cv, opt, rowData) {
            var yjzt = rowData.YJZT;
			var id=rowData.ID
			var dh=rowData.DH;
			var data=rowData;
			var cpzsm=rowData.CPZSM;
			var xsqx=rowData.XSQX;
			var lxr=rowData.LXR;
			var qymc=rowData.QYMC;
			var pch=rowData.PCH;

            if (yjzt == '0') {
                return "<a href='javascript:void(0);' style = 'color:green' myAttr1="+id+" onclick='$.ns(\"ns" + subffix + "\").opClick(this)'>发送预警信息</a>";
            } else {
                return "<a href='javascript:void(0);' style = 'color:green' onclick='$.ns(\"ns" + subffix + "\").opClick()'>再次预警</a>";
            }
        }});
        return setting;
    };


}
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
	if(2 === ui.options.number){
		ui.processItems = function (data) {
			var btns = [];
			for (var i = 0; i < data.length; i++) {
				if(data[i]==="->"){
					btns.push("","->");
				}
			}
//			btns.push("","->")
			for (var i = 0; i < data.length; i++) {
				btns.push(data[i]);
			}
			btns.push({
				id:$.config.preButtonId +"warning",
				label: "预警",
				type : "button"

			});
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

/***
 * 当前全局函数实现位置
 * 如果有需要的，可打开以下实现体
 * 使用方法： 与开发构件一致
 **/
$.extend($.ns("ns" + subffix), {
    opClick: function (e) {
		var id=$(e).attr('myAttr1');
		var Mapdata= $.loadJson($.contextPath+"/zlaqyj!sendoneMessage.json?id="+id);
    }
});






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

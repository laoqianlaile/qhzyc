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
	return $.contextPath + "/zzfalb";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
    //二次开发按钮

};
/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {

    ui.beforeInitGrid = function(setting) {
        setting.fitStyle = "width";
        return setting;
    };

    ui.clickSecondDev = function (id) {
        if(id==$.config.preButtonId+"delete11"){
            var ids = ui.getSelectedRowId();
            var idStr = ids.join(",");
            if (0 === ids.length) {
                CFG_message( "请选择记录!", "warning");
                return;
            }
            $.confirm("当前记录将从列表移除，确定吗？", function(sure) {
                if (sure) {
                    $.ajax({
                        type : "POST",
                        url : $.contextPath + '/zzfalb!deleteZzfa.json',
                        dataType : 'json',
                        data : {ids:idStr },
                        success : function (data) {
                            CFG_message("删除成功", "success");
                            ui.reload();
                        },
                        error : function (data) {
                            CFG_message("删除失败", "error");
                            ui.reload();
                        }
                    });
                }
            });
        }
        if (id == $.config.preButtonId + "copyZzfa") {
            var dataId = ui.getSelectedRowId();
            if(dataId.length == 0){
                CFG_message( "请选择记录!", "warning");
                return;
            }
            if (dataId.length > 1) {
                CFG_message( "只能选择一条记录!", "warning");
                return;
            }
            $.ajax({
                type : "POST",
                url : $.contextPath + '/zzfalb!copyZzfa.json',
                dataType : 'json',
                data : {dataId : dataId.join(",")},
                success : function (data) {
                    CFG_message( "操作成功!", "success");
                    ui.reload();
                },
                error : function (data) {
                    CFG_message( "操作失败!", "error");
                    ui.reload();
                }
            });
        }
    }
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
	ui.processItems = function (data) {
		var btns = new Array();
		for (var i = 0; i < data.length; i++) {
			btns.push(data[i]);
		}
        btns.push({
            id : $.config.preButtonId + "copyZzfa",
            icon : "icon-copy",
            label : "复制方案",
            type : "button"
        });

        btns.splice(4,0,{
            id:$.config.preButtonId + "delete11",
            icon: "icon-delete",
            label: "删除",
            type : "button"
        });
		return btns;
	};
}
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

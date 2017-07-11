<%@page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
/***************************************************!
 * @date   2016-11-10 16:03:13
 * 系统配置平台自动生成的自定义构件二次开发JS模板
 * 详细的二次开发操作，请查看文档（二次开发手册/自定义构件.docx）
 * 注：请勿修改模板结构（若需要调整结构请联系系统配置平台）
 ***************************************************/
 
 
(function(subffix) {

/**
 * 二次开发定位自己controller
 * 系统默认的controller: jQuery.contextPath + "/appmanage/show-module"
 * @returns {String}
 **/
window[CFG_actionName(subffix)] = function (ui) {
	// ui.assembleData 就是 configInfo
	return jQuery.contextPath + "/cjgjyjcdwjscxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
    initToolbar();
};
    function initToolbar(){
        $.fn['ctbar'].defaults = {
            processData: function(data, pos) {
                var ui =  this;
                if ("top" === pos) {
                    var op = ui.options.op.toString();
                    var poss = "";
                    // 表单
                    if ($.config.contentType.isForm(ui.options.type)) {
                        if (op == '0') {
                            poss =" - 新增";
                        } else if (op == '1') {
                            poss =" - 修改";
                        } else if (op == '2') {
                            poss =" - 详情";
                        }
                        if(data.length == 0){
                            poss =" - 详请";
                        }
                    }

                    var menuObj = $.loadJson($.contextPath + "/trace!getMenuById.json?id="+ui.options.menuId);
                    if(menuObj.name != undefined){
                        if("企业信息" == menuObj.name){
                            menuObj.name = $(".coral-state-active").children().html();
                        }

                        if(ui.options.number ==1
                        ){
                            data.unshift({
                                "type": "html",
                                "content": "<div class='homeSpan'><div><div style='margin-left:25px'> - " + menuObj.name + poss + "</div>",
                                frozen: true
                            });
                        }
                    }
                }

                return data;
            }
        };
    }
/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
    ui.beforeInitGrid = function (setting) {
        var colModel = setting.colModel;
        setting.editableRows="true";
        for (var i = 0; i < colModel.length; i++) {
            var obj = colModel[i];
            if ("BGFJ" == obj.name) {
                setting.colModel[i].formatter = function (value, options, rowObj) {
                    var src =rowObj["BGFJ"];
                    if(!isEmpty(src) && src.indexOf("data:") ==-1){
                        src=$.contextPath + '/spzstpfj/' + src;
                        return "<a class=\"fancybox-buttons\" data-fancybox-group=\"button\" href=\"" + src+"\"><img src='" + src+"' width='200'/></a>";
                    }else{
                        return initTPdiv( rowObj["ID"]);
                    }

                }
            }
        }

        return setting;
    }
};

/**
 *  二次开发：复写自定义工具条
 */
function _override_tbar (ui) {
	// ui.assembleData 就是 configInfo
};

/**
 *  二次开发：复写基本查询区
 */
function _override_bsearch (ui) {
	// ui.assembleData 就是 configInfo
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
};

/**
 *  二次开发：复写自定义布局
 */
function _override_layout (ui) {
	// ui.assembleData 就是 configInfo
};

/**
 *  二次开发：复写自定义树
 */
function _override_tree (ui) {
	// ui.assembleData 就是 configInfo
};

/***
 * 当前构件全局函数实现位置
 * 如果有需要的，可打开以下实现体
 * 使用方法： 与开发构件一致
 **/
//jQuery.extend(jQuery.ns("ns" + subffix), {
//	name : "",
//	click: function() {}
//});









/**
 * 在此可以复写所有自定义JS类
 */
window[CFG_overrideName(subffix)] = function () {
	if (this instanceof jQuery.config.cform) {
		_override_form(this);
	} else if (this instanceof jQuery.config.cgrid) {
		_override_grid(this);
	} else if (this instanceof jQuery.config.cbsearch) {
		_override_bsearch(this);
	} else if (this instanceof jQuery.config.cgsearch) {
		_override_gsearch(this);
	} else if (this instanceof jQuery.config.ctree) {
		_override_tree(this);
	} else if (this instanceof jQuery.config.ctbar) {
		_override_tbar(this);
	} else if (this instanceof jQuery.config.clayout) {
		_override_layout(this);
	}
};

	
	
	
	
})("${timestamp}");
</script>

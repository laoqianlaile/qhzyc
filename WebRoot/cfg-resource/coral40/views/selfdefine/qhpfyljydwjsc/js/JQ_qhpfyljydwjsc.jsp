<%@page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ces.component.trace.utils.SerialNumberUtil" %>
<% String qybm= SerialNumberUtil.getInstance().getCompanyCode();%>
<script type="text/javascript">
/***************************************************!
 * @date   2017-05-25 10:46:53
 * 系统配置平台自动生成的自定义构件二次开发JS模板
 * 详细的二次开发操作，请查看文档（二次开发手册/自定义构件.docx）
 * 注：请勿修改模板结构（若需要调整结构请联系系统配置平台）
 ***************************************************/
 
(function(subffix) {
    var resourceFolder = $.contextPath + "/cfg-resource/coral40/common";
    /**
     * 二次开发定位自己controller
     * 系统默认的controller: jQuery.contextPath + "/appmanage/show-module"
     * @returns {String}
     **/
    window[CFG_actionName(subffix)] = function (ui) {
        // ui.assembleData 就是 configInfo
        return jQuery.contextPath + "/qhpfyljydwjsc";
    };


    /**
     * 二次开发：复写自定义表单
     */
    function _override_form(ui) {
        ui._init = function () {
            if( ui.options.number == 1 ) {
                if (isEmpty(ui.options.dataId)) {
                    var qybm = "<%=qybm%>"
                    ui.setFormData({QYBM: qybm});
                } else {
                    ui.getItemJQ("PCH").combogrid("option", "readonly", true);
                }
            }
        }

        ui.bindEvent = function () {
            ui.setItemOption("PCH" , "onChange", function ( e ,data) {
            var rowData = ui.getItemJQ("PCH").combogrid("grid").grid("getRowData",data.value);
            ui.setFormData({PCH:rowData.PCH,YCMC:rowData.YCMC});
             });
        }

    ui.afterSave = function () {
        ui._assembleReturn(true);
        var ltc_id = $("#mainContent div div")[0].getAttribute("id");
        var gd_id = $("#LTC_" + ltc_id.substr(5, ltc_id.length) + " div ")[0].getAttribute("id");
        $("#gd_" + gd_id.substr(3, gd_id.length)).grid("reload");
    }
    ui.clickAdd = function (op) {
        var cGrid = this.getSelfGrid(), // 对应列表
                jqForm = $("form", this.uiForm),
                formData;
        // 表单检验
        if (!jqForm.form("valid")) return false;
        // 获取表单数据
        formData = jqForm.form("formData", false);
        formData.BGFJ = $(".thumb").attr("src");
        formData.QYBM = "<%=qybm%>";
        // 向列表添加数据
        cGrid.addRowData(formData);
    }
    // 将表单数据及对应从表列表数据一起保存
    ui.clickSaveAll = function () {
        debugger
        var _this = this, dGrid = this.getDetailGrid(), op = "saveAll";
        if (!dGrid) return;
        var jqForm = $("form", this.uiForm),
                rowData = dGrid.toFormData(),
                formData, url, postData;
        // 表单检验
        if (!jqForm.form("valid")) return false;
        // 检验
        if (!rowData.length) {
            CFG_message("请先添加明细列表数据再保存！", "warning");
            return false;
        }
        // 保存前回调方法
        if (_this.processBeforeSave(jqForm, op) === false) return;
        // 获取表单数据
        formData = jqForm.form("formData", false);
        postData = {
            E_entityJson: $.config.toString(formData),
            E_dEntitiesJson: $.config.toString(rowData)
        };
        postData = this.processPostData(postData, CFG_common.P_SAVE_ALL);
        // 向列表添加数据
        // 重置表单数据
        url = this.getAction() + "!saveAll.json?P_tableId=" + this.options.tableId + "&P_D_tableIds=" + dGrid.options.tableId
                + "&P_componentVersionId=" + this.options.componentVersionId
                + "&P_menuId=" + this.options.menuId
                + "&P_menuCode=" + this.getParamValue("menuCode");

        $.ajax({
            url: url,
            type: "post",
            data: postData,
            dataType: "json",
            success: function (rlt) {
                if (rlt.success) {
                    jqForm.form("loadData", rlt.data.master);
                    dGrid.clearGridData();
                    dGrid.addRowData(rlt.data.detail);
                    CFG_message("保存成功！", "success");
                    // 保存后回调方法
                    _this.processAfterSave(rlt.data, op);
                } else {
                    CFG_message("请点击更新按钮确认", "warning");
                }
            },
            error: function () {
                CFG_message("保存主从表数据失败！", "error");
            }
        });
    }
}


/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
    ui.clickCreate = function () {
        var id = "UNSAVE_" + getTimestamp(),
                rowData = {},
                key = this.getKeyColumnName();
        rowData[key] = id;
        ui.uiGrid.grid("addRowData", id, rowData, "last");
        ui.uiGrid.grid("editRow", id);
    }
    ui.beforeInitGrid = function (setting) {
        var colModel = setting.colModel;
        setting.editableRows = "true";
        for (var i = 0; i < colModel.length; i++) {
            var obj = colModel[i];
            if ("BGFJ" == obj.name) {
                setting.colModel[i].formatter = function (value, options, rowObj) {
                    var src = rowObj["BGFJ"];
                    var id = rowObj.ID;
                    if (!isEmpty(src) && src.indexOf("data:") == -1) {
                        src = $.contextPath + '/spzstpfj/' + src;
                        var id =rowObj["ID"];
                        return "<div id=\"tupian${idSuffix}"+id+"\"><a class=\"fancybox-buttons\" data-fancybox-group=\"button\" href=\"" + src + "\"><img src='" + src + "' width='200'/> </a> </div>" + initTPdiv(rowObj["ID"]);
                    } else {
                        return initTPdiv(rowObj["ID"]);
                    }

                }
            }
        }

        return setting;
    }
    // 将列表中的数据转为表单数据集（有对应表单）
    ui.toFormData = function () {
        debugger
        var rData = ui.getRowData();
        var fDataArr = [],len = rData.length, tRow, sRow;
        for (var i = 0; i < len; i++) {
            sRow = rData[i];
            tRow = {};
            tRow['BGMC'] = sRow['BGMC'];
            var bgfj = sRow['BGFJ'];
            if (!isEmpty(bgfj)) {
                if (bgfj.lastIndexOf("src=\"data:image/png;base64") > -1) {
                    bgfj = bgfj.substring(bgfj.lastIndexOf("src=\"data:image/png;base64") + 5, bgfj.lastIndexOf("\" class=\"thumb\"")).trim();
                } else if (bgfj.lastIndexOf("src=\"data:image/jpeg;base64") > -1) {
                    bgfj = bgfj.substring(bgfj.lastIndexOf("src=\"data:image/jpeg;base64") + 5, bgfj.lastIndexOf("\" class=\"thumb\"")).trim();
                } else {
                    bgfj = bgfj.substring(bgfj.lastIndexOf("spzstpfj/") + 9, bgfj.lastIndexOf("\" width")).trim();
                }
                tRow['BGFJ'] = bgfj;
            }
            tRow['ID'] = sRow['ID'];
            tRow['QYBM'] = sRow['QYBM'];
            tRow['BGDX'] = sRow['BGDX'];
            tRow['BGLX'] = sRow['BGLX'];
            fDataArr.push(tRow);
        }
        return fDataArr;
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
    jQuery.extend(jQuery.ns("ns" + subffix), {
        viewImage: function (fileInput,id) {
            debugger
            var view = $("#view${idSuffix}"+id);
            var s=$("#tupian${idSuffix}"+id ).remove()
            if (window.FileReader) {
                var p = $("#preview${idSuffix}"+id);
                var file = fileInput.files;
                var f = file[file.length - 1];

                var ImgSuffix = f.name.substring(f.name.lastIndexOf('.') + 1);

                //图片不能超过2M，
                if (!(f.size < 20971520 && (ImgSuffix == 'jpg' ||ImgSuffix=='jpeg'|| ImgSuffix == 'bmp' || ImgSuffix == 'png'))) {
                    CFG_message("图片必须小于20M，且格式必须为.png, .jpg 或 .bmp！", "error");
                    $("#imageUpload${idSuffix}").val("");
                    return false;
                }
                var fileReader = new FileReader();
                fileReader.onload = (function (file) {
                    return function (e) {

                        var img = new Image();
                        img.src = this.result;
                        img.onload = function() {
                            var canvas = document.getElementById('uploadImg'+id);
                            var cxt = canvas.getContext('2d');
                            var originWidth = img.width;
                            var originHeight = img.height;
                            if ( originWidth >  originHeight  && originWidth>1024) { //如果宽大于高在根据款进行压缩
                                originHeight = Math.round(originHeight*1024/originWidth);
                                originWidth = 1024;
                            }else if(originWidth < originHeight && originHeight>1024){ //如果宽大于高在根据款进行压缩
                                originWidth = Math.round(1024*originWidth/originHeight);
                                originHeight = 1024;
                            }
                            canvas.width = originWidth;
                            canvas.height = originHeight;
                            cxt.drawImage(img, 0, 0, originWidth, originHeight);
                            if("jpeg" == ImgSuffix || "jpg" == ImgSuffix){
                                $.ns('ns' + subffix).uploadImage(canvas.toDataURL("image/jpeg", 0.9), id);
                            }else{
                                $.ns('ns' + subffix).uploadImage(canvas.toDataURL("image/"+ImgSuffix, 0.9), id);
                            }
                            return;
                        }
                    }
                })(f);
                fileReader.readAsDataURL(f);
            }
        },
        uploadImage: function (tplj,id) {
            debugger
            var s=$("#tupian${idSuffix}"+id ).remove()
            var view = $("#view${idSuffix}"+id);
            view.empty();
            var image = new Image();
            image.src = tplj;
            image.className = "thumb";
            image.onload = function (e) {

                var div = document.createElement('div');
                //$(div).append(this);
                $(div).append("<a class=\"fancybox-buttons\" data-fancybox-group=\"button\" href=\"" + tplj+"\"><img src=\""+tplj+"\" class=\"thumb\" /></a>");
                var originWidth = this.width;
                var originHeight = this.height;
                div.style.float = "left";
                div.style.marginRight = "5px";
                div.style.height = "70px";
                if (((originWidth * 100) / originHeight) > 750) {
                    div.style.width = "750px";
                } else {
                    div.style.width = (originWidth * 100) / originHeight + "px";
                }
                image.style.height = div.style.height;
                image.style.width = div.style.width;
                this.style.border = "1px solid #2AC79F",
                        $(div).hover(function () {
                            $(div).find("span").toggle();
                        });
                view.append(div);
                if (view.css("width") != "0px") {
                    $("#chooseImage${idSuffix}"+id).css("margin-top", "58px");
                }
            }
        }
    });









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
                    debugger
                    var menuObj = $.loadJson($.contextPath + "/trace!getMenuById.json?id="+ui.options.menuId);
                    if(menuObj.name != undefined){
                        if("企业信息" == menuObj.name){
                            menuObj.name = $(".coral-state-active").children().html();
                        }
                        debugger
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
    function initTPdiv(id) {
        var html = "<div style=\"margin-bottom: 0px;margin-left:0px\">" +
                "<div class=\"fillwidth colspan2 clearfix\">" +
                "<div class=\"app-inputdiv6\" style=\"width:55%;padding-top:0px\">" +
                "<div id=\"file${idSuffix}"+id+"\">" +
                "<input class=\"inputfile\" type=\"file\" style=\"width:160px;display:none\" id=\"imageUpload${idSuffix}"+id+"\" multiple=\"multiple\" lable=\"预览\"	accept=\"image/*\" name=\"imageUpload\" onchange=\"$.ns('ns" + subffix + "').viewImage(this,'"+id+"')\"/>" +
                "</div>" +
                "<div  id=\"view${idSuffix}"+id+"\"></div>" +
                "<button  id=\"chooseImage${idSuffix}\" class='ctrl-toolbar-element ctrl-init ctrl-init-button coral-button coral-component coral-state-default coral-corner-all coral-button-text-only coral-toolbar-item-component' type='button' onclick=\"$('#imageUpload${idSuffix}"+id+"').click()\">" +
                "<span class=\"coral-button-text \">上传图片</span><canvas id=\"uploadImg"+id+"\" style=\"display:none\"></canvas>" +
                "</button>" +
                "</div>" +
                "</div>" +
                "</div>";
        return html;
    }
	
})("${timestamp}");
</script>

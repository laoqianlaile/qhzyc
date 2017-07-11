<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String resourceFolder = path + "/cfg-resource/coral40/common";
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
    String plbh = request.getParameter("plbh");
    String pl = request.getParameter("pl");
    String treeId = request.getParameter("treeId");
%>
<div id="max${idSuffix}" style="margin-top: 7px">
    <ces:toolbar id="toolbar${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                 data="['->',{'label': '保存', 'id':'save','cls':'save_tb', 'disabled': 'false','type': 'button'},{'label': '关闭', 'id':'close','cls':'return_tb', 'disabled': 'false','type': 'button'}]">
    </ces:toolbar>
    <div class='homeSpan' style="margin-top: -23px;">
        <div>
            <div style='margin-left:20px;width: 150px;' id="nva${idSuffix}"></div>
        </div>
    </div>
    <form id="enterForm${idSuffix}" enctype="multipart/form-data" method="post" class="coralui-form">
        <div class="fillwidth colspan2 clearfix">
            <input type="hidden" name="ID" id="id${idSuffix}">

            <div class="app-inputdiv6">
                <label class="app-input-label" style="width:20%;">品种编号:</label>
                <input id="pzbh${idSuffix}" name="pzbh" data-options="required:true,readonly:'true'"
                       class="coralui-textbox" readonly="readonly"/>
            </div>
            <div class="app-inputdiv6">
                <label class="app-input-label" style="width:20%;">品类:</label>
                <input id="pl${idSuffix}" name="pl" data-options="required:true" class="coralui-textbox"
                       readonly="readonly"/>
            </div>
            <div class="app-inputdiv6" style="display: none;">
                <label class="app-input-label" style="width:20%;">品类编号:</label>
                <input id="plbh${idSuffix}" name="plbh" data-options="required:true" class="coralui-textbox"
                       readonly="readonly"/>
            </div>
            <div class="app-inputdiv6">
                <label class="app-input-label" style="width:20%;">品种:</label>
                <input id="pz${idSuffix}" name="pz" data-options="required:true" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv12">
                <label class="app-input-label" style="width:10%;">认证类型:</label>
                <input id="rzlx${idSuffix}" name="rzlx"
                       data-options="required:true,column:4,data:[{'value':'1','text':'有机食品'},{'value':'2','text':'绿色食品'},{'value':'3','text':'无公害'},{'value':'4','text':'地理标识'}]"
                       class="coralui-checkboxlist"/>
            </div>
            <div class="app-inputdiv12" id="pzjsDiv${idSuffix}">
                <label class="app-input-label" style="width:10%;">品种介绍:</label>
                <textarea id="pzjs${idSuffix}" data-options="required:true" class="coralui-textbox" name="pzjs"
                          style="width:100%"/>
            </div>
        </div>
        <%----------------------------图片begin-------------------------------%>
        <div class="fillwidth colspan1 clearfix">
            <div class="app-inputdiv12">
                <label class="app-input-label" style="float:left">图片:</label>

                <div id="file${idSuffix}">
                    <input class="inputfile" type="file" style="width:160px;display:none"
                           id="imageUpload${idSuffix}" lable="预览"
                           accept="image/*" name="imageUpload" onchange="viewImage(this)"/>
                </div>
                <div id="view${idSuffix}" style="float:left"></div>
                <div style="float:left" id="preview${idSuffix}"></div>
                <button style="margin-left: 0px;float:left"
                        class='ctrl-toolbar-element ctrl-init ctrl-init-button coral-button coral-component coral-state-default coral-corner-all coral-button-text-only coral-toolbar-item-component'
                        type='button' onclick="$('#imageUpload${idSuffix}').click()">
                    <span id="imageCheckSpan${idSuffix}" class="coral-button-text ">选择图片</span>
                </button>
            </div>
        </div>
        <%-----------------------------图片 end--------------------------------%>

    </form>
</div>
<script type="text/javascript">
    var plbh = "<%=plbh%>";
    var pl = "<%=pl%>";
    var treeId = "<%=treeId%>";
    $.extend($.ns("namespaceId${idSuffix}"), {
        toolbarClick: function (e, ui) {
            if (ui.id == "save") {//保存
                $("#enterForm${idSuffix}").form().submit();
                $("#enterForm${idSuffix}").form().addClass("top0form");
            } else if (ui.id == "close") {//关闭
                var configInfo = $("#max${idSuffix}").data('configInfo');
                CFG_clickCloseButton(configInfo);
            }
        }
    });
    function viewImage(fileInput) {
        if (window.FileReader) {
            var p = $("#preview${idSuffix}");
            var v = $("#view${idSuffix}");
            <%--p.empty();--%>
            var files = fileInput.files;
            if (files.length > 0) {
                var f = files[0];
                var ImgSuffix = f.name.substring(f.name.lastIndexOf('.') + 1);
                //图片不能超过2M
                if (!(f.size < 2097152 && (ImgSuffix == 'jpg' || ImgSuffix == 'bmp' || ImgSuffix == 'png'))) {
                    CFG_message("图片必须小于2M，且格式必须为.png, .jpg 或 .bmp！", "error");
                    $('#imageUpload${idSuffix}').val("");
                    return false;
                }
                var fileReader = new FileReader();
                fileReader.onload = (function () {
                    v.empty();
                    p.empty();
                   // var span = document.createElement('span');
                   // span.innerHTML = '<img style = "position:relative;margin:0px 5px 0px 0px;width:100px;height:100px" src = "' + this.result + '" alt = "' + f.name + '" />';
                    //p.append(span);
//                  p.insertBefore(span,null);
					return function (e) {
                    uploadImage(this.result);
                    return;
                }
               })(f);
                fileReader.readAsDataURL(f);
            }
        }
    }
    
        function uploadImage(tplj) {
        var view = $("#view${idSuffix}");
        view.empty();
        var image = new Image();
        image.src = tplj;
        image.className = "thumb";
        image.onload = function (e) {
            var div = document.createElement('div');
            $(div).append(this);
            $(div).append('<div style="display: none;position:absolute;top:5px; margin-top: 0px; margin-left: 6px; width: 20px; height: 20px;"><img onclick = "deleteImage(\'' + this.src + '\')" style = "height:20px" src = "<%=resourceFolder%>/css/images/trace-image/trash_bin.png"></div>');
            var originWidth = this.width;
            var originHeight = this.height;
            div.style.float = "left";
//            div.style.marginTop = "10px";
            div.style.marginRight = "5px";
            div.style.height = "100px";
            div.style.position="relative";
            if(((originWidth * 100) / originHeight) > 750){
                div.style.width = "750px";
            }else{
                div.style.width = (originWidth * 100) / originHeight + "px";
            }
            image.style.height = div.style.height;
            image.style.width = div.style.width;
            $(div).hover(function () {
                $(div).find("div").toggle();
            }); 
            view.append(div);
            if(view.css("width") != "0px"){
                $("#chooseImage${idSuffix}").css("margin-top","0px");
            }
        }
    }

    function deleteImage(src) {
        var strArray = src.split("/");
        var tplj = strArray[strArray.length - 1];
        $.ajax({
            type: "POST",
            url: $.contextPath + '/zzxyqk!deleteImage.json',
            data: {'tplj': tplj},
            dataType: "json",
            success: function (data) {
                    var p = $("#preview${idSuffix}");
                    p.empty();
                    var view = $("#view${idSuffix}");
                    view.empty();
                    $("#imageUpload${idSuffix}").val("");
                    $("#chooseImage${idSuffix}").css("margin-top","0px");
            }
        })
    }
    //
    
    $("#enterForm${idSuffix}").submit(function () {
        if (!$(this).form("valid")) return false;
        var formdata = new FormData(this);
        $.ajax({
            type: 'POST',
            url: "${basePath}zzpzxgym!savePzxx.json",
            data: formdata,
            /**
             *必须false才会自动加上正确的Content-Type
             */
            contentType: false,
            /**
             * 必须false才会避开jQuery对 formdata 的默认处理
             * XMLHttpRequest会对 formdata 进行正确的处理
             */
            processData: false,
            timestamp: false,
            success: function (data) {
                var $tree = $("#" + treeId);
                var selNode = $tree.tree("getSelectedNodes")[0];
                if ($("#id${idSuffix}").val() == "") {//新增
                    $tree.tree("addNodes", selNode, {
                        id: data,
                        name: $("#pz${idSuffix}").textbox("getValue"),
                        pzjd: "pzzjd"
                    }, true);
                    $tree.tree("expandNode", selNode, true, true);
                    $("#max${idSuffix}").data('configInfo').parentConfigInfo.namespace.initPzxx();
                    CFG_clickCloseButton($("#max${idSuffix}").data('configInfo'));
                } else {//修改
                    selNode.name = $("#pz${idSuffix}").textbox("getValue");
                    $tree.tree("updateNode", selNode);
                }
                CFG_message("操作成功！", "success");
            },
            error: function () {
                CFG_message("操作失败！", "error");
            }
        });
        return false;
    });
    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'zzpzxgym.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#max${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone': function () {
                return $('#max${idSuffix}');
            },
            /** 初始化预留区 */
            'initReserveZones': function (configInfo) {
                <%--CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'cdmc', $('#toolbarId${idSuffix}').toolbar("getLength")-1);--%>
            },
            /** 获取返回按钮添加的位置 */
            'setReturnButton': function (configInfo) {
                <%--CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));--%>
            },
            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {
                var pzxx = $.loadJson($.contextPath + "/zzpzxgym!getPzById.json?treeNodeId=" + plbh);
                if ($.isEmptyObject(pzxx)) {//新增品种信息
                    $("#nva${idSuffix}").html(" 品种信息 - 新增");
                    // 按钮权限控制
                    var pzbh = $.loadJson($.contextPath + "/zzpzxgym!getPzbh.json");
//	                var rzlxList = $.loadJson($.contextPath+"/zzpzxgym!getRzlx.json");
                    <%--$("#rzlx${idSuffix}").checkboxlist("option", "data", "[{{value:'CN',text:'中国(CHINA)'},{value:'ES',text:'西班牙(SPAIN)'},{value:'GB',text:'英国(UNITED KINGDOM)'}]");--%>
                    $("#pzbh${idSuffix}").textbox("setValue", pzbh);
                    $("#pl${idSuffix}").textbox("setValue", pl);
                    $("#plbh${idSuffix}").textbox("setValue", plbh);
                } else {//修改品种信息
                    $("#nva${idSuffix}").html(" 品种信息 - 修改");
                    $("#id${idSuffix}").val(pzxx.ID);
                    $("#toolbar${idSuffix}").toolbar("hide", "close");
                    $("#pz${idSuffix}").textbox("setValue", pzxx.PZ);
                    $("#pzbh${idSuffix}").textbox("setValue", pzxx.PZBH);
                    $("#pl${idSuffix}").textbox("setValue", pzxx.PL);
                    $("#plbh${idSuffix}").textbox("setValue", pzxx.PLBH);
                    $("#pzjs${idSuffix}").textbox("setValue", pzxx.PZJS);
                    $("#rzlx${idSuffix}").checkboxlist("setValue", pzxx.RZLX);
                    if (pzxx.TPBCMC) {
                        $("#imageCheckSpan${idSuffix}").text("修改图片");
                        var span = document.createElement('span');
                        span.innerHTML = '<img style = "position:relative;margin:0px 5px 0px 0px;width:100px;height:100px" src = "' + ($.contextPath + '/spzstpfj/' + pzxx.TPBCMC) + '" />';
                        $("#preview${idSuffix}").append(span);
                    }
                }
            }
        });
    });
</script>
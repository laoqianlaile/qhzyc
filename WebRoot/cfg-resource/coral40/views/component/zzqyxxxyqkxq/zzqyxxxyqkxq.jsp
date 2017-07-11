<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String resourceFolder = path + "/cfg-resource/coral40/common";
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    request.setAttribute("gurl", path + "/qyptsjlx!getSjzdByLxbm.json");
    request.setAttribute("turl", path + "/qyptsjlx!getSjzdmc.json");
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<div id="max${idSuffix}" class="fill">
    <div id="ft${idSuffix}" class="toolbarsnav clearfix top0">
        <%--<ces:toolbar id="toolbarId${idSuffix}"--%>
        <%--data="[{\"type\": \"button\",\"id\": \"saveBtm\",\"label\": \"保存\",\"name\":\"保存\",\"onClick\":\"submitForm()\"}]">--%>
        <%--</ces:toolbar>--%>
        <ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                     data="['->',{'label': '返回', 'id':'CFG_closeComponentZone','cls':'return_tb', 'disabled': 'false','type': 'button'}]">
        </ces:toolbar>
        <div class='homeSpan' style="margin-top: -23px;"><div><div style='margin-left:20px;width: 100px;' id="nva${idSuffix}"> -信用情况-新增 </div></div></div>
    </div>
    <form id="enterForm${idSuffix}" action="${basePath}zxtqyda!saveQyda"
          enctype="multipart/form-data" method="post" class="coralui-form">
        <div class="fillwidth colspan2 clearfix">
            <div class="app-inputdiv6">
                <label class="app-input-label" style="width:20%;">奖罚单位:</label>
                <input id="jfdw${idSuffix}" data-options="required:true" name="jfdw" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv6">
                <label class="app-input-label" style="width:20%;">奖罚时间:</label>
                <%--<ces:datepicker id="jfsj${idSuffix}" required="true" name="jfsj" value="2015-10-12"></ces:datepicker>--%>
                <input id="jfsj${idSuffix}" data-options="required:true" name="jfsj" class="coralui-datepicker"/>
            </div>
        </div>
        <div class="fillwidth colspan1 clearfix">
            <div class="app-inputdiv12">
                <label class="app-input-label" style="width:10%;">奖罚内容:</label>
                <textarea id="jfnr${idSuffix}" name="jfnr" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv12" id="lsxzqDiv${idSuffix}">
                <%--<label class="app-input-label" style="width:10%;">ID:</label>--%>
                <input id="id${idSuffix}" type="hidden" name="id" class="coralui-textbox"/>
                <input id="tplj${idSuffix}" type="hidden" name="tplj" class="coralui-textbox"/>
            </div>
        </div>
        <%----------------------------图片begin-------------------------------%>
        <div style="margin-bottom: 50px;">
            <div class="fillwidth colspan3 clearfix">
                <div class="app-inputdiv12">
                    <label class="app-input-label" style="width:10%;">奖罚图片:</label>
                    <div style="margin-left: 100px" id="view${idSuffix}"></div>
                    <div style="margin-left: 100px" id="preview${idSuffix}"></div>


                    <%--<button style="margin-left: 10px" id="chooseImage${idSuffix}"--%>
                            <%--class='ctrl-toolbar-element ctrl-init ctrl-init-button coral-button coral-component coral-state-default coral-corner-all coral-button-text-only coral-toolbar-item-component'--%>
                            <%--type='button' onclick="$('#imageUpload${idSuffix}').click()">--%>
                        <%--<span class="coral-button-text ">选择图片</span>--%>
                    <%--</button>--%>
                </div>
                <div class="app-inputdiv12">
                    <div id="file${idSuffix}">
                        <input class="inputfile" type="file" style="width:160px;display:none"
                               id="imageUpload${idSuffix}" multiple="multiple" lable="预览"
                               accept="image/*" name="imageUpload" onchange="viewImage(this)"/>
                    </div>
                    <label style="width:120px;" id="tip${idSuffix}"/>

                </div>
            </div>
        </div>
        <%-----------------------------图片 end--------------------------------%>
    </form>
</div>
<script type="text/javascript">


    function submitForm() {
        // 表单检验
        if (!$("#enterForm${idSuffix}").form("valid")) {
            return false;
        }
        $("#enterForm${idSuffix}").form().submit();
        // padding-top:0px;
        $("#enterForm${idSuffix}").form().addClass("top0form");
    }

    $("#enterForm${idSuffix}").submit(
            function () {
                var formdata = new FormData(this);
                $.ajax({
                    type: 'POST',
                    url: "${basePath}zzxyqk!saveXyqk.json?",
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
                    async:false,
                    success: function (data) {
                        CFG_message("操作成功!", "success");
                        CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));
                        return false;
                        $("#id${idSuffix}").val(data)
                    },
                    error: function () {
                        CFG_message("操作失败！", "error");
                    }
                })
                this.empty();
                return false;
            });


    function viewImage(fileInput) {
        var view = $("#view${idSuffix}");
        view.empty();
        if (window.FileReader) {
            var p = $("#preview${idSuffix}");
            var file = fileInput.files;
            var f = file[file.length - 1];
            var fileReader = new FileReader();
            fileReader.onload = (function (file) {
                return function (e) {
                    uploadImage(this.result);
                    return;
                }
            })(f);
            fileReader.readAsDataURL(f);
        }
    }

    function uploadImage(tplj) {
        var view = $("#view${idSuffix}");
        view.empty();
        var image = new Image();
        image.src = tplj;
        image.className = "thumb";
        image.style.height = "100px";
        image.style.width = "100px";
        image.onload = function (e) {
            var div = document.createElement('div');
            $(div).append(this);
            $(div).append('<span style="display: none;position: absolute; margin-top: 0px; margin-left: 6px; width: 20px; height: 20px;"><img onclick = "deleteImage(\'' + this.src + '\')" style = "height:20px" src = "<%=resourceFolder%>/css/images/trace-image/trash_bin.png"></span>');
            var originWidth = this.width;
            var originHeight = this.height;
            div.style.float = "left";
//            div.style.marginTop = "10px";
            div.style.marginRight = "5px";
            div.style.height = "100px";
            if(((originWidth * 100) / originHeight) > 750){
                div.style.width = "750px";
            }else{
                div.style.width = (originWidth * 100) / originHeight + "px";
            }
            image.style.height = div.style.height;
            image.style.width = div.style.width;
           // $(div).hover(function () {
                //$(div).find("span").toggle();
            //});
            view.append(div);
            if(view.css("width") != "0px"){
                $("#chooseImage${idSuffix}").css("margin-top","75px");
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
    $.extend($.ns("namespaceId${idSuffix}"), {
        toolbarClick: function (event, button) {
            if (button.id == "saveBtm") {//保存的方法
                $("#trpcgForm${idSuffix}").form().submit();
                submitForm();
            } else if (button.id == "CFG_closeComponentZone" || button.id == "CFG_closeComponentDialog") {//返回 或关闭
                CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));
            }
        }
    });


    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'zzqyxxxyqk.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#max${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone': function () {
                return $('#layoutId${idSuffix}').layout('panel', 'center');
            },
            <%--/** 初始化预留区 */--%>
            <%--'initReserveZones': function (configInfo) {--%>
            <%--CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', $('#toolbarId${idSuffix}').toolbar("getLength") - 1);--%>
            <%--},--%>
            /** 获取返回按钮添加的位置 */
            <%--'setReturnButton': function (configInfo) {--%>
            <%--CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));--%>
            <%--},--%>
            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {
                var rowDataId = CFG_getInputParamValue(configInfo, 'rowDataId'); // 获取构件输入参数
                if (rowDataId !== "" && rowDataId !== null) {
                    $("#enterForm${idSuffix}").form("setReadOnly",true);
                    $("#nva${idSuffix}").html("-信用情况-详情");
                    var jsonData = $.loadJson("${basePath}zzxyqk!getXyqkData.json?id=" + rowDataId);
                    $("#jfdw${idSuffix}").textbox("setValue",
                            (jsonData.JFDW == "null" || jsonData.JFDW == null) ? "" : jsonData.JFDW);
                    $("#jfsj${idSuffix}").datepicker("setDate",
                            (jsonData.JFSJ == "null" || jsonData.JFSJ == null) ? "" : jsonData.JFSJ);
                    $("#jfnr${idSuffix}").textbox("setValue",
                            (jsonData.JFNR == "null" || jsonData.JFNR == null) ? "" : jsonData.JFNR);
                    $("#id${idSuffix}").textbox("setValue",
                            (jsonData.ID == "null" || jsonData.ID == null) ? "" : jsonData.ID);
                    if(jsonData.TPLJ != null && jsonData.TPLJ != ""){
                        var tplj = "${basePath}spzstpfj/" + jsonData.TPLJ;
                        uploadImage(tplj);
                    }
                }

            }

        });
    });
</script>


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
        <ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
            data="['->',{'label': '保存', 'id':'save','cls':'save_tb', 'disabled': 'false','type': 'button'},{'label': '返回', 'id':'CFG_closeComponentZone','cls':'return_tb', 'disabled': 'false','type': 'button'}]"> 
       </ces:toolbar>
        <div class='homeSpan' style="margin-top: -23px;"><div><div style='margin-left:20px;width: 150px;' id="nva${idSuffix}"> -工作人员信息-新增 </div></div></div>
    </div>
    <form id="enterForm${idSuffix}" action="${basePath}"
          enctype="multipart/form-data" method="post" class="coralui-form">
        <div class="fillwidth colspan2 clearfix">
            <div class="app-inputdiv6">
                <label class="app-input-label" style="width:20%;">工作人员编号:</label>
                <input id="gzrybh${idSuffix}" data-options="readonly:true" name="gzrybh" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv6">
                <label class="app-input-label" style="width:20%;">姓名:</label>
                <input id="xm${idSuffix}" data-options="required:true" maxlength="20" name="xm" class="coralui-textbox"/>
            </div>
        </div>
        <div class="fillwidth colspan2 clearfix">
            <div class="app-inputdiv6">
                <label class="app-input-label" style="width:20%;">岗位:</label>
                <input id="gw${idSuffix}" name="gw" data-options="required:true" autocomplete="off" class="coralui-combobox" placeholder="请选择">
            </div>
            <div class="app-inputdiv6">
                <label class="app-input-label" style="width:20%;">身份证号:</label>
                <input id="sfzh${idSuffix}" data-options="validType:'idno'" name="sfzh" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv12" id="lsxzqDiv${idSuffix}" >
                <input id="id${idSuffix}" type="hidden" name="id" class="coralui-textbox"/>
                <input id="tplj${idSuffix}" type="hidden" name="tplj" class="coralui-textbox"/>
            </div>
        </div>
        <div class="fillwidth colspan2 clearfix">
            <div class="app-inputdiv6">
                <label class="app-input-label" style="width:20%;">性别:</label>
                <input id="xb${idSuffix}" name="xb" autocomplete="off" class="coralui-combobox" placeholder="请选择">
            </div>
            <div class="app-inputdiv6">
                <label class="app-input-label" style="width:20%;">年龄:</label>
                <input id="nn${idSuffix}" name="nn" maxlength="50" class="coralui-textbox"/>
            </div>
        </div>
        <div class="fillwidth colspan2 clearfix">
            <div class="app-inputdiv6">
                <label class="app-input-label" style="width:20%;">联系方式:</label>
                <input id="lxfs${idSuffix}" name="lxfs" data-options="validType:'mobile',required:true  "  class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv6">
                <label class="app-input-label" style="width:20%;">特长:</label>
                <input id="tc${idSuffix}" name="tc" maxlength="50" class="coralui-textbox"/>
            </div>
        </div>
        <div class="fillwidth colspan2 clearfix">
            <div class="app-inputdiv6">
                <label class="app-input-label" style="width:20%;">IC卡编号:</label>
                <input id="ickbh${idSuffix}" name="ickbh" maxlength="50" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv6">
                <label class="app-input-label" style="width:20%;">启用状态:</label>
                <div id="qyzt${idSuffix}" class="coralui-radiolist" name="qyzt"
                     data-options="value:'1',column:2,data:[{value:'1',text:'启用'},{value:'2',text:'禁用'}]"></div>
            </div>
        </div>
        <%--<div class="fillwidth colspan2 clearfix">--%>
            <%--<div class="app-inputdiv6">--%>
                <%--<label class="app-input-label" style="width:20%;">用户名:</label>--%>
                <%--<input id="yhm${idSuffix}" data-options="required:true"--%>
                       <%--name="yhm" class="coralui-textbox"/>--%>
            <%--</div>--%>
            <%--<div class="app-inputdiv6">--%>
                <%--<label class="app-input-label" style="width:20%;">密码:</label>--%>
                <%--<input id="mm${idSuffix}" name="mm" type="password" class="coralui-textbox"/>--%>
            <%--</div>--%>

        <%--</div>--%>
        <%--<div class="fillwidth colspan2 clearfix">--%>
            <%--<div class="app-inputdiv6">--%>
                <%--<label class="app-input-label" style="width:20%;">是否开通账户:</label>--%>
                <%--<div id="sfktzh${idSuffix}" class="coralui-radiolist" name="sfktzh"--%>
                     <%--data-options="value:'1',column:2,data:[{value:'1',text:'启用'},{value:'2',text:'禁用'}]"></div>--%>
            <%--</div>--%>
        <%--</div>--%>
        <%----------------------------图片begin-------------------------------%>
        <div style="margin-bottom: 50px;">
            <div class="fillwidth colspan2 clearfix">
                <div class="app-inputdiv6">
                    <label class="app-input-label" style="width:20%;">照片:</label>
                    <div id="file${idSuffix}">
                        <input class="inputfile" type="file" style="width:160px;display:none"
                               id="imageUpload${idSuffix}" multiple="multiple" lable="预览"
                               accept="image/*" name="imageUpload" onchange="viewImage(this)"/>
                    </div>
                    <div style="margin-left: 10%" id="view${idSuffix}"></div>
                    <button style="margin-left: 0px" id="chooseImage${idSuffix}"
                            class='ctrl-toolbar-element ctrl-init ctrl-init-button coral-button coral-component coral-state-default coral-corner-all coral-button-text-only coral-toolbar-item-component'
                            type='button' onclick="$('#imageUpload${idSuffix}').click()">
                        <span class="coral-button-text ">上传图片</span>
                    </button>
                </div>
            </div>
        </div>
        <%-----------------------------图片 end--------------------------------%>
    </form>
</div>

<script type="text/javascript">
    $.extend($.ns("namespaceId${idSuffix}"), {
        toolbarClick : function(event, button) {
           if (button.id == "CFG_closeComponentZone") {
                //CFG_clickReturnButton($('#max${idSuffix}').parent().data('parentConfigInfo'));
                CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));
           }else if(button.id == "save"){
               submitForm();
           }
        }
    });

    $('#gw${idSuffix}').combobox({
        valueField:'VALUE',
        textField:'TEXT'
    });
    $('#xb${idSuffix}').combobox({
        valueField:'VALUE',
        textField:'TEXT'
    });


    function submitForm() {
        // 表单检验

        <%--var yhm=$("#yhm${idSuffix}").textbox("getValue");--%>
      //  alert(yhm);
        <%--if(yhm==""){--%>
            <%--$("#mm${idSuffix}").textbox("setValue","");--%>
            <%--return false;--%>
        <%--}else{--%>
            <%--var yhmJson = $.loadJson($.contextPath + "/zzkhxxgllb!validYhm.json?yhm="+yhm);--%>
            <%--//alert(yhmJson)--%>
            <%--if(yhmJson.length > 0){--%>
                <%--CFG_message("用户名重复", "error");--%>
                <%--$("#yhm${idSuffix}").textbox("setValue","");--%>
                <%--$("#mm${idSuffix}").textbox("setValue","");--%>
                <%--return false;--%>
            <%--}--%>
            <%--$("#mm${idSuffix}").textbox("setValue","000000");--%>
        <%--}--%>
        if (!$("#enterForm${idSuffix}").form("valid")){
            return false;
        }
        $("#enterForm${idSuffix}").form().submit();

    };

    $("#enterForm${idSuffix}").submit(
            function () {
                var ickbh=$("#ickbh${idSuffix}").val();
                var id=$("#id${idSuffix}").val();

                //判断IC卡编号是否重复
                var jsonData = $.loadJson($.contextPath + "/zzgzryda!checkICKBH.json?ickbh="+ickbh+"&id="+id+"");
                if(jsonData.result == "ERROR"){
                    $("#ickbh${idSuffix}").textbox("setValue","");
                    CFG_message(jsonData.msg, "error");
                    return false;
                }

                var formdata = new FormData(this);
                $.ajax({
                    type: 'POST',
                    url: "${basePath}zzgzryda!saveGzryxx.json?",
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
                    async: false,
                    success: function (data) {
                        CFG_message("操作成功！", "success");
                        CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));
                        $("#id${idSuffix}").val(data)
                    },
                    error: function () {
                        CFG_message("操作失败！", "error");
                    }
                });
                this.empty();
                return false;
            }
    );

    function viewImage(fileInput) {
        var view = $("#view${idSuffix}");
//        view.empty();
        if (window.FileReader) {
            var p = $("#preview${idSuffix}");
            var file = fileInput.files;
            var f = file[file.length-1];

            var ImgSuffix = f.name.substring(f.name.lastIndexOf('.')+1);

            //图片不能超过2M，
            if(!(f.size<2097152 && (ImgSuffix=='jpg'||ImgSuffix=='bmp'||ImgSuffix=='png'))){
                CFG_message("图片必须小于2M，且格式必须为.png, .jpg 或 .bmp！", "error");
                $("#imageUpload${idSuffix}").val("");
                return false;
            }
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

    function uploadImage(tplj){
        var view = $("#view${idSuffix}");
        view.empty();
        var image = new Image();
        image.src = tplj;
        image.className = "thumb";
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
            this.style.position = "absolute";
            this.style.border = "1px solid #2AC79F",
            $(div).hover(function () {
                $(div).find("span").toggle();
            });
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
            url: $.contextPath + '/zzgzryda!deleteImage.json',
            data: {'tplj': tplj},
            dataType: "json",
            success: function (data) {
                var view = $("#view${idSuffix}");
                view.empty();
                $("#imageUpload${idSuffix}").val("");
                $("#chooseImage${idSuffix}").css("margin-top","0px");
            }
        })
    }

    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'sdzycjdxx.jsp',
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
               // var yhm = $("#yhm${idSuffix}").textbox("getValue");
                <%--if(yhm=="" || yhm==null){--%>
                    <%--$("#yhm${idSuffix}").textbox("option","readonly",false);--%>
                <%--}--%>
                var gw_data = $.loadJson($.contextPath + "/zzgzryda!getGwBySjzd.json");

                $("#gw${idSuffix}").combobox("reload", gw_data);
                var xb_data = $.loadJson($.contextPath + "/zzgzryda!getXbBySjzd.json");
                $("#xb${idSuffix}").combobox("reload", xb_data);

                var rowDataId = CFG_getInputParamValue(configInfo, 'rowDataId'); // 获取构件输入参数
                if(rowDataId !== "" && rowDataId !== null){
                    $("#nva${idSuffix}").html("-工作人员信息-修改");
                    var jsonData = $.loadJson("${basePath}zzgzryda!getGzryxxData.json?id="+rowDataId);
                    $("#gzrybh${idSuffix}").textbox("setValue",
                            (jsonData.GZRYBH == "null" || jsonData.GZRYBH == null) ? "" : jsonData.GZRYBH);
                    $("#xm${idSuffix}").textbox("setValue",
                            (jsonData.XM == "null" || jsonData.XM == null) ? "" : jsonData.XM);
                    $("#gw${idSuffix}").combobox("setValue",
                            (jsonData.GW == "null" || jsonData.GW == null) ? "" : jsonData.GW);
                    $("#sfzh${idSuffix}").textbox("setValue",
                            (jsonData.SFZH == "null" || jsonData.SFZH == null) ? "" : jsonData.SFZH);
                    $("#xb${idSuffix}").combobox("setValue",
                            (jsonData.XB == "null" || jsonData.XB == null) ? "" : jsonData.XB);
                    $("#nn${idSuffix}").val(
                            (jsonData.NN == "null" || jsonData.NN == null) ? "" : jsonData.NN);
                    $("#lxfs${idSuffix}").textbox("setValue",
                            (jsonData.LXFS == "null" || jsonData.LXFS == null) ? "" : jsonData.LXFS);
                    $("#tc${idSuffix}").textbox("setValue",
                            (jsonData.TC == "null" || jsonData.TC == null) ? "" : jsonData.TC);
                    $("#ickbh${idSuffix}").textbox("setValue",
                            (jsonData.ICKBH == "null" || jsonData.ICKBH == null) ? "" : jsonData.ICKBH);
                    $("#qyzt${idSuffix}").radiolist("setValue",
                            (jsonData.QYZT == "null" || jsonData.QYZT == null) ? "" : jsonData.QYZT);

                    $("#id${idSuffix}").textbox("setValue",
                            (jsonData.ID == "null" || jsonData.ID == null) ? "" : jsonData.ID);
                    <%--$("#sfktzh${idSuffix}").radiolist("setValue",--%>
                            <%--(jsonData.SFKTZH == "null" || jsonData.SFKTZH  == null) ? "" : jsonData.SFKTZH );--%>
                    if(jsonData.TPLJ != null && jsonData.TPLJ != ""){
                        var tplj = "${basePath}spzstpfj/"+jsonData.TPLJ;
                        uploadImage(tplj);
                    }
                }else{
                    var lshData = $.loadJson("${basePath}zzgzryda!getGzrybhByLsh.json");
                    $("#gzrybh${idSuffix}").val(
                            (lshData == "null" || lshData == null) ? "" : lshData);
                }

                <%--$("#yhm${idSuffix}").textbox("setValue",--%>
                        <%--(jsonData.YHM == "null" || jsonData.YHM  == null) ? "" : jsonData.YHM );--%>
                <%--$("#mm${idSuffix}").textbox("setValue",--%>
                        <%--(jsonData.MM == "null" || jsonData.MM  == null) ? "" : jsonData.MM );--%>
                <%--var yhm = $("#yhm${idSuffix}").textbox("getValue");--%>
                <%--var mm=$("#mm${idSuffix}").textbox("getValue");--%>
                <%--if(yhm!=""){--%>
                    <%--$("#yhm${idSuffix}").textbox("option","readonly",true);--%>
                <%--}--%>
                <%--if(mm!=""){--%>
                    <%--$("#mm${idSuffix}").textbox("option","readonly",true);--%>
                <%--}--%>

            }
        });
	

        function readIcCard(){
            var data = __touch__("readCard");
            var json = JSON.parse(data);
            if (json.status == true || json.status == "true")　{
                $("#ickbh${idSuffix}").textbox("setValue",
                        (json.value == "null" || json.value == null) ? "" : json.value);
                CFG_message("操作成功！", "success");
            }
            window.setTimeout(readIcCard,2000);
        }
        if(isSwt){
            window.setTimeout(readIcCard,2000);
        }


    }); /******************用户名  onChange 事件 begin*************************/
    $("#yhm${idSuffix}").textbox({
        onChange:  function (e, data) {
            if(data.value == ""){
                $("#mm${idSuffix}").textbox("setValue","");
            }else{
                var yhmJson = $.loadJson($.contextPath + "/zzkhxxgllb!validYhm.json?yhm="+data.value);
                $("#yhm${idSuffix}").textbox("option","readOnly",true);
                if(yhmJson.length > 0){
                    CFG_message("用户名重复", "error");
                    $("#yhm${idSuffix}").textbox("setValue","");
                    $("#mm${idSuffix}").textbox("setValue","");
                    return false;
                }

                $("#mm${idSuffix}").textbox("setValue","000000");
                $("#mm${idSuffix}").textbox("option","readonly",true);
            }
        }
    });
    /******************用户名  onChange 事件 end*************************/
    /******************密码  onChange 事件 begin*************************/
    $("#mm${idSuffix}").textbox({
        onChange:  function (e, data) {
            if($("#yhm${idSuffix}").textbox("getValue").length > 0){
                if(data.value.length == 0){
                    $("#mm${idSuffix}").textbox("setValue","000000");
                    CFG_message("密码不能为空！", "error");
                }
            }
        }
    });
    /******************密码  onChange 事件 end*************************/





</script>
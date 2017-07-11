<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String resourceFolder = path + "/cfg-resource/coral40/common";
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<style type="text/css">
    .app-input-label {
        float: left;
    }
</style>
<div id="max${idSuffix}" class="fill">
    <div class="fill">
        <div class="toolbarsnav clearfix">
            <ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                         data="['->',{'label': '保存', 'id':'save', 'cls':'save_tb','disabled': 'false','type': 'button'},{'label': '返回', 'id':'CFG_closeComponentZone','cls':'return_tb', 'disabled': 'false','type': 'button'}]">
            </ces:toolbar>
            <div class='homeSpan' style="margin-top: -23px;"><div><div style='margin-left:20px;width: 150px;' id="nva${idSuffix}"> -产品管理-新增 </div></div></div>
        </div>
        <form id="cpForm${idSuffix}" action="${basePath}" style="height:270px"
              enctype="multipart/form-data" method="post" class="coralui-form">
            <div class="app-inputdiv4" style="height:32px;display: none">
                <input id="id${idSuffix}" class="coralui-textbox" data-options="readonly:true" name="ID"/>
            </div>
            <div class="app-inputdiv4" style="height:32px;display: none">
                <input id="qybm${idSuffix}" class="coralui-textbox" data-options="readonly:true" name="QYBM"/>
            </div>
            <div class="app-inputdiv4" style="height:32px;display: none">
                <input id="qymc${idSuffix}" class="coralui-textbox" data-options="readonly:true" name="QYMC"/>
                <input id="tplj${idSuffix}" type="hidden" name="tplj" class="coralui-textbox"/>
            </div>


            <div class="fillwidth colspan3 clearfix">
                <!------------------ 第一排开始---------------->
                <div class="app-inputdiv4" style="height:32px;display: none">
                    <label class="app-input-label">产品编号：</label>
                    <input id="cpbh${idSuffix}" name="CPBH" data-options="readonly:true" class="coralui-textbox"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label">产品名称：</label>
                    <input id="cpmc${idSuffix}" name="CPMC" data-options="required: true" class="coralui-textbox"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label">包装形式：</label>
                    <input id="bzxs${idSuffix}" name="BZXS" data-options="required:true" class="coralui-combobox"/>
                </div>
                <!------------------ 第一排结束---------------->

                <!------------------ 第二排开始---------------->
                <div class="app-inputdiv4">
                    <label class="app-input-label">规格：</label>
                    <input id="gg${idSuffix}" data-options="validType: 'float', texticons:'kg'" name="GG" maxlength="12" class="coralui-textbox"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label">等级：</label>
                    <input id="dj${idSuffix}" name="DJ" data-options="required:true" class="coralui-combobox"/>
                </div>
                <!------------------ 第二排结束---------------->

                <!------------------ 第三排开始---------------->
                <div class="app-inputdiv4">
                    <label class="app-input-label">存储方式：</label>
                    <input id="ccfs${idSuffix}" name="CCFS" class="coralui-combobox"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label">保鲜期：</label>
                    <input id="bxq${idSuffix}" name="BXQ" class="coralui-textbox" data-options="validType:'integer', texticons:'天'"/>
                </div>
                <div class="app-inputdiv8">
                    <label class="app-input-label">说明：</label>
                    <textarea id="sm${idSuffix}" name="SM" class="coralui-textbox"/>
                </div>
                <%----------------------------图片begin-------------------------------%>
                <div style="margin-bottom: 50px;">
                    <div class="fillwidth colspan3 clearfix">
                        <div class="app-inputdiv12">
                            <label class="app-input-label">产品图片:</label>

                            <div style="margin-left: 100px" id="view${idSuffix}"></div>

                            <button style="margin-left: 10px" id="chooseImage${idSuffix}"
                                    class='ctrl-toolbar-element ctrl-init ctrl-init-button coral-button coral-component coral-state-default coral-corner-all coral-button-text-only coral-toolbar-item-component'
                                    type='button' onclick="$('#imageUpload${idSuffix}').click()">
                                <span class="coral-button-text ">上传图片</span>
                            </button>
                        </div>
                        <div class="app-inputdiv12">
                            <div id="file${idSuffix}">
                                <input class="inputfile" type="file" style="width:160px;display:none"
                                       id="imageUpload${idSuffix}" multiple="multiple" lable="预览"
                                       accept="image/*" name="imageUpload" onchange="viewImage(this)"/>
                            </div>
                            <%--<label style="width:120px;" id="tip${idSuffix}"/>--%>

                            <%--<div style="margin-left: 100px" id="preview${idSuffix}"></div>--%>
                        </div>
                    </div>
                </div>
                <%-----------------------------图片 end--------------------------------%>

                <!------------------ 第三排结束---------------->
            </div>
        </form>
        <div class="toolbarsnav clearfix" style="margin-top: -6px">
            <ces:toolbar id="toolbarId1${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick1"
                         data="['->',{'label': '添加', 'id':'add', 'disabled': 'false','type': 'button'},'','','','','']">
            </ces:toolbar>
            <div class='homeSpan' style="margin-top: -23px;"><div><div style='margin-left:20px;width: 150px;' id="nva${idSuffix}"> -产品配料管理 </div></div></div>
        </div>
        <ces:grid id="gridId${idSuffix}" fitStyle="fill" height="500px" rownumbers="true">
            <ces:gridCols>
                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                <ces:gridCol name="PID" hidden="true"></ces:gridCol>
                <ces:gridCol name="DPZ" formatter="text" hidden="true">DPZ</ces:gridCol>
                <ces:gridCol name="XPZ"  formatter="text" hidden="true">PZ</ces:gridCol>
                <ces:gridCol name="DPZBH" formatter="combobox" formatoptions="required: true"
                             width="80">品类</ces:gridCol>
                <ces:gridCol name="XPZBH" formatter="combobox" formatoptions="required: 'true'"
                             width="80">品种</ces:gridCol>
                <ces:gridCol name="ZL" formatter="text"
                             formatoptions="validType: 'float'"
                             width="80">重量（kg）</ces:gridCol>
                <ces:gridCol name="op" fixed="true" width="80" formatter="toolbar"
                             formatoptions="isOverflow:false,onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
            </ces:gridCols>
        </ces:grid>
    </div>
</div>

<script type="text/javascript">
    $.extend($.ns("namespaceId${idSuffix}"), {
        toolbarClick: function (event, button) {
            if (button.id == "CFG_closeComponentZone") {
                //CFG_clickReturnButton($('#max${idSuffix}').parent().data('parentConfigInfo'));
                CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));
            } else if (button.id == "save") {
                submitForm();
            }
        },
        toolbarClick1: function (event, button) {
            if (button.id == "add") {
                var $grid = $("#gridId${idSuffix}");
                var lastsel = generateId("");
                $grid.grid("addRowData", lastsel, {}, "last");
            }
        },
        gridClick: function(e,button){
            if(button.id == "delGridData"){
                $.confirm("确定删除", function (r) {
                    if (r) {
                        $("#" + e.data.gridId).grid("delRowData", e.data.rowId);
                    }
                });
            }
        },
        initform: function () {
            var dpzData = $.loadJson($.contextPath + "/zzcpxxgl!getPzxx.json");
            var $detailGrids = $("#gridId${idSuffix}");
            $detailGrids.grid("setColProp", "DPZBH", {
                "formatoptions": {
                    "required" :true ,
                    "data": dpzData, onChange: function (e, data) {
                        var $xpzbhCombobox = $("#" + e.data.gridId).grid("getCellComponent", e.data.rowId, "XPZBH");
                        var $xpzCombobox = $("#" + e.data.gridId).grid("getCellComponent", e.data.rowId, "XPZ");
                        var $dpzCombobox = $("#" + e.data.gridId).grid("getCellComponent", e.data.rowId, "DPZ");
                        var xpzbhData = $.loadJson($.contextPath + "/zzcpxxgl!getPlxxByPzxx.json?plbh=" + data.value);
                        $xpzbhCombobox.combobox("reload", xpzbhData);
                        $xpzbhCombobox.combobox("setValue", "");
                        $xpzCombobox.textbox("setValue", "");
                        $dpzCombobox.textbox("setValue",data.text);
                    }
                }
            });

            $detailGrids.grid("setColProp", "XPZBH", {
                "formatoptions": {
                    "required" :true ,
                    "data": {}, onChange: function (e, data) {
                        var $grid = $("#" + e.data.gridId);
                        var gridIds =  $grid.grid("getDataIDs");
                        var pzbh = data.value;
                        var rowid = e.data.rowId;
                        $.each(gridIds,function(e,data){
                            if(rowid != data){
                                if( $grid.grid("getRowData",data).XPZBH == pzbh){
                                    CFG_message("品种重复！", "error");
                                    var $xpzCombobox = $grid.grid("getCellComponent",rowid, "XPZ");
                                    $xpzCombobox.textbox("setValue","");
                                    var $Combobox = $grid.grid("getCellComponent", rowid, "XPZBH");
                                    $Combobox.combobox("setValue", "");
                                    return false;
                                }
                            }
                        });
                        var $xpzCombobox = $grid.grid("getCellComponent", e.data.rowId, "XPZ");
                        $xpzCombobox.textbox("setValue", data.text);
                    }
                }
            });
        }
    });

    $('#bzxs${idSuffix}').combobox({
        valueField: 'VALUE',
        textField: 'TEXT'
    });
    $('#dj${idSuffix}').combobox({
        valueField: 'VALUE',
        textField: 'TEXT'
    });
    $('#ccfs${idSuffix}').combobox({
        valueField: 'VALUE',
        textField: 'TEXT'
    });


    function submitForm() {
        if (!$("#cpForm${idSuffix}").form("valid")) {
            return false;
        }
        if (!$("#gridId${idSuffix}").grid("valid")) {
            return false;
        }
        if($('#bxq${idSuffix}').textbox('getValue')<0||$('#gg${idSuffix}').textbox('getValue')<0){
            CFG_message("规格或保鲜期不能输入负数！", "error");
            return false;
        }
        $("#cpForm${idSuffix}").form().submit();
    };

    $("#cpForm${idSuffix}").submit(
            function () {
                var formdata = new FormData(this);
                var griddata = $("#gridId${idSuffix}").grid('getRowData');
                formdata.append("gridData", JSON.stringify(griddata));
                $.ajax({
                    type: 'POST',
                    url: "${basePath}zzcpxxgl!saveCpxx.json?",
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
                        CFG_message("操作成功！", "success");
                        CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));
                        $("#id${idSuffix}").val(data);
                    },
                    error: function () {
                        CFG_message("操作失败！", "error");
                    }
                })
                return false;
            }
    );

    function viewImage(fileInput) {
        <%--var view = $("#view${idSuffix}");--%>
//        view.empty();
        if (window.FileReader) {
            var p = $("#preview${idSuffix}");
            var file = fileInput.files;
            var f = file[file.length - 1];

            var ImgSuffix = f.name.substring(f.name.lastIndexOf('.')+1);
            //图片不能超过2M，
            if(!(f.size<2097152 && (ImgSuffix=='jpg'||ImgSuffix=='bmp'||ImgSuffix=='png'))){
                CFG_message("图片必须小于2M，且格式必须为.png, .jpg 或 .bmp！", "error");
                $('#imageUpload${idSuffix}').val("");
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

    function uploadImage(tplj) {
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
            url: $.contextPath + '/zzcpxxgl!deleteImage.json',
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
            'page': 'zzcpglxz.jsp',
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
            <%--/** 获取返回按钮添加的位置 */--%>
            <%--'setReturnButton': function (configInfo) {--%>
                <%--CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));--%>
            <%--},--%>
            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {
                $.ns("namespaceId${idSuffix}").initform();
                var combobox_data = $.loadJson($.contextPath + "/zzcpxxgl!getSjzdSj.json?lxbm=BZXS,DJ,CCFS");
                $("#bzxs${idSuffix}").combobox("reload", combobox_data.BZXS);
                $("#dj${idSuffix}").combobox("reload", combobox_data.DJ);
                $("#ccfs${idSuffix}").combobox("reload", combobox_data.CCFS);

                var rowDataId = CFG_getInputParamValue(configInfo, 'rowDataId'); // 获取构件输入参数
                if (rowDataId !== "" && rowDataId !== null) {
                    $("#nva${idSuffix}").html("-产品管理-修改");
                    var jsonData = $.loadJson("${basePath}zzcpxxgl!getCpxxById.json?id=" + rowDataId);
                    $("#cpForm${idSuffix}").form("loadData",jsonData.cpxx);
                    if (jsonData.cpxx.TPBCMC != null && jsonData.cpxx.TPBCMC != "") {
                        var tplj = "${basePath}spzstpfj/" + jsonData.cpxx.TPBCMC;
                        uploadImage(tplj);
                    }

                    $.each(jsonData.cpxxplxx, function (e, data) {
                        $("#gridId${idSuffix}").grid("addRowData", data.ID, data);
                        var $xpzbhCombobox = $("#gridId${idSuffix}").grid("getCellComponent",data.ID, "XPZBH");
                        var $xpzCombobox = $("#gridId${idSuffix}").grid("getCellComponent", data.ID, "XPZ");
                        var xpzbhData = $.loadJson($.contextPath + "/zzcpxxgl!getPlxxByPzxx.json?plbh=" + data.DPZBH);
                        $xpzbhCombobox.combobox("reload", xpzbhData);
                        $xpzbhCombobox.combobox("setValue", data.XPZBH);
                        $xpzCombobox.textbox("setValue",  data.XPZ);
                    });
                }
            }
        });
    });


</script>
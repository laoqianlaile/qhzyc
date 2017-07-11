<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page import="com.ces.component.trace.utils.SerialNumberUtil" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String qybm = SerialNumberUtil.getInstance().getCompanyCode();
    request.setAttribute("qybm",qybm);
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
                     data="['->',{'label': '保存', 'id':'saveBtm','cls':'save_tb', 'disabled': 'false','type': 'button'}]">
        </ces:toolbar>
        <div class='homeSpan' style="margin-top: -23px;"><div><div style='margin-left:20px;width: 100px;' id="nva${idSuffix}"> -基地信息</div></div></div>
    </div>
    <form id="enterForm${idSuffix}" enctype="multipart/form-data" method="post" class="coralui-form">
        <div class="fillwidth colspan2 clearfix">
            <div class="app-inputdiv4">
                <label class="app-input-label" style="width:20%;">基地名称:</label>
                <input id="jdmc${idSuffix}" data-options="required:true" name="JDMC" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv4">
                <label class="app-input-label" style="width:20%;">基地编号:</label>
                <input id="jdbh${idSuffix}" data-options="required:true,readonly:true" name="JDBH" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv4">
                <label class="app-input-label" style="width:20%;">基地类型:</label>
                <input id="jdlx${idSuffix}" data-options="required:true" name="JDLX" class="coralui-textbox"/>
            </div>
        </div>
        <div class="fillwidth colspan1 clearfix">
            <div class="app-inputdiv4">
                <label class="app-input-label" style="width:20%;">基地面积:</label>
                <input id="jdmj${idSuffix}" data-options="required:true,buttons:[{'innerRight':[{label:'亩'}]}]" name="JDMJ" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv4">
                <label class="app-input-label" style="width:20%;">所属地区:</label>
                <input id="ssdqbm${idSuffix}" name="SSDQBM" />
            </div>
            <div class="app-inputdiv4" style ="display: none">
                <input id="ssdq${idSuffix}" name="SSDQ" class="coralui-textbox"/>
            </div>


            <div class="app-inputdiv4">
                <label class="app-input-label" style="width:20%;">负责人:</label>
                <input id="fzrbh${idSuffix}" name="FZRBH" />
            </div>
            <div class="app-inputdiv4" style ="display: none">
                <input id="fzr${idSuffix}" name="FZR" class="coralui-textbox"/>
            </div>

        </div>
        <div class="fillwidth colspan2 clearfix">

            <div class="app-inputdiv4">
                <label class="app-input-label" style="width:20%;">联系方式:</label>
                <input id="lxfs${idSuffix}"  name="LXFS" class="coralui-textbox"/>
            </div>

            <div class="app-inputdiv12" id="lsxzqDiv${idSuffix}" >
                <input id="id${idSuffix}" type="hidden" name="ID" class="coralui-textbox"/>
                <input id="tplj${idSuffix}" type="hidden" name="TPLJ" class="coralui-textbox"/>
            </div>

        </div>
        <div class="app-inputdiv12">
                <label class="app-input-label" style="width:71px;">联系地址:</label>
                <input id="lxdz${idSuffix}"  name="LXDZ" class="coralui-textbox"/>
        </div>
        <div class="app-inputdiv12" id="cdmsDiv${idSuffix}">
                <label class="app-input-label" style="width:71px;">备注:</label>
                <textarea id="bz${idSuffix}" name="BZ" data-options="maxlength:50" class="coralui-textbox"/>
            </div>


        <%----------------------------图片begin-------------------------------%>
        <div style="margin-bottom: 50px;">
            <div class="fillwidth colspan1 clearfix">
                <div class="app-inputdiv6">
                    <label class="app-input-label" style="width:71px;">图片:</label>
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




    function submitForm() {
        // 表单检验
        if (!$("#enterForm${idSuffix}").form("valid")) {
            return false;
        }
        $("#enterForm${idSuffix}").form().submit();
        <%--// padding-top:0px;--%>
        <%--$("#enterForm${idSuffix}").form().addClass("top0form");--%>
    }

    $("#enterForm${idSuffix}").submit(
            function () {
                var cdbmValue = $("#ssdqbm${idSuffix}").combogrid("getValue");
                var cdmcValue = $("#ssdq${idSuffix}").textbox("getValue");
                $.ns('namespaceId${idSuffix}').putBmMc(cdbmValue,cdmcValue);

                var formdata = new FormData(this);
                $.ajax({
                    type: 'POST',
                    url: "${basePath}qyxxjdxx!saveJDXX.json?",
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
                        //操作成功时，如果是非江浙沪地区，那么所属地区编码输入框的值与所属地区框的值互换。
                        var cdbmValue = $("#ssdqbm${idSuffix}").combogrid("getValue");
                        var cdmcValue = $("#ssdq${idSuffix}").textbox("getValue");
                        $.ns('namespaceId${idSuffix}').putBmMc(cdbmValue,cdmcValue);
                        $("#id${idSuffix}").val(data);
                        return false;
                    },
                    error: function () {
                        //操作失败时，如果是非江浙沪地区，那么所属地区编码输入框的值与所属地区框的值互换。
                        var cdbmValue = $("#ssdqbm${idSuffix}").combogrid("getValue");
                        var cdmcValue = $("#ssdq${idSuffix}").textbox("getValue");
                        $.ns('namespaceId${idSuffix}').putBmMc(cdbmValue,cdmcValue);
                        CFG_message("操作失败！", "error");
                    }
                })

                return false;
            });




    $.extend($.ns("namespaceId${idSuffix}"), {
        cdmcClick: function () {
            CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "DQTCK", "地区名称", 2, $.ns("namespaceId${idSuffix}"));
        },
        fzrClick: function () {
            CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "FZRTCK", "负责人", 2, $.ns("namespaceId${idSuffix}"));
        },
        toolbarClick: function (event, button) {
            if (button.id == "saveBtm") {//保存的方法
                $("#trpcgForm${idSuffix}").form().submit();
                submitForm();
            } else if (button.id == "CFG_closeComponentZone" || button.id == "CFG_closeComponentDialog") {//返回 或关闭
                CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));
            }
        },
        //负责人弹出框的回调函数
        setFzrxx:function(rowData){
            if(rowData.rowData==null){
                return;
            }

            $("#fzrbh${idSuffix}").combogrid("setValue",rowData.rowData.GZRYBH);
            $("#fzr${idSuffix}").textbox("setValue",rowData.rowData.XM);

        },
        //地区弹出框的回调函数
        setDqxx:function(rowData){
            if(rowData.rowData==null){
                return;
            }
            //if(rowData.rowData.CDBM.indexOf('31') == 0 || rowData.rowData.CDBM.indexOf('32') == 0 || rowData.rowData.CDBM.indexOf('33') == 0){
                $("#ssdqbm${idSuffix}").combogrid("setValue",rowData.rowData.CDBM);
                $("#ssdq${idSuffix}").textbox("setValue",rowData.rowData.CDMC);
            <%--}else{--%>
                <%--$("#ssdqbm${idSuffix}").combogrid("setValue",rowData.rowData.CDMC);--%>
                <%--$("#ssdq${idSuffix}").textbox("setValue",rowData.rowData.CDBM);--%>
            <%--}--%>
        },
        //非江浙沪地区时，所属地区编码框放入名称，所属地区框放入编码。
        putBmMc:function(bm,mc){

            if(bm == null || mc== null ){
                return;
            }
            //if(bm.indexOf('31') != 0 && bm.indexOf('32') != 0 && bm.indexOf('33') != 0){
                $("#ssdqbm${idSuffix}").combogrid("setValue",mc);
                $("#ssdq${idSuffix}").textbox("setValue",bm);

            //}
        }
    });

    function viewImage(fileInput) {
        var view = $("#view${idSuffix}");
        //view.empty();
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
                $("#chooseImage${idSuffix}").css("margin-top","0px");
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
            'page': 'qyxxjdxx.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#max${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone': function () {
                return $('#layoutId${idSuffix}').layout('panel', 'center');
            },

            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {


                //获取数据
                var jsonData = $.loadJson($.contextPath + "/qyxxjdxx!getJDXX.json");



                //上传图片
                if(jsonData.TPLJ != null && jsonData.TPLJ != ""){
                    var tplj = "${basePath}spzstpfj/"+jsonData.TPLJ;
                    uploadImage(tplj);
                }



                /***************************************************地区弹出框 begin*******************************************************/
                var buttonOpts = {
                    id: "combogrid_buttonId",
                    label: "构件按钮",
                    icons: "icon-checkmark-circle",
                    text: false,
                    onClick: $.ns('namespaceId${idSuffix}').cdmcClick
                };

                var _colNames = ["地区编号", "地区名称"];
                var _colModel = [{name: "CDBM", width: 65, sortable: true}, {name: "CDMC", width: 155, sortable: true}];

                $("#ssdqbm${idSuffix}").combogrid({
                    url: $.contextPath + "/cdxx!getShdqxxGrid.json",
                    multiple: false,
//                    sortable: true,
                    colModel: _colModel,
                    colNames: _colNames,
                    enableFilter:true,
                    width: 250,
                    panelWidth:280,
                    textField: "CDMC",
                    valueField: "CDBM",
                    panelHeight: "auto",
//                    height: "auto",
                    buttonOptions: buttonOpts,
                    onChange: function(e,data){
                        $("#ssdq${idSuffix}").textbox("setValue",data.text);
                    }
                });
                /**********************************地区弹出框 end********************************************/

                var buttonOpts2 = {
                    id: "combogrid_buttonId",
                    label: "构件按钮",
                    icons: "icon-checkmark-circle",
                    text: false,
                    onClick: $.ns('namespaceId${idSuffix}').fzrClick
                };

                var _colNames = ["负责人编号", "负责人姓名"];
                var _colModel = [{name: "GZRYBH", width: 65, sortable: true}, {name: "XM", width: 155, sortable: true}];

               $("#fzrbh${idSuffix}").combogrid({
                   url: $.contextPath + "/qyxxjdxx!getFzrxx.json",
                   multiple: false,
                   sortable: true,
                   colModel: _colModel,
                   colNames: _colNames,
                   enableFilter:true,
                   width: 250,
                   panelWidth:280,
                   textField: "XM",
                   valueField: "GZRYBH",
                height: "auto",
                        buttonOptions: buttonOpts2,
                        onChange: function(e,data){
                    $("#fzr${idSuffix}").textbox("setValue",data.text);
                }
            });

                $("#enterForm${idSuffix}").form("loadData",jsonData);
                //debugger
                //页面初始化时，将所属地区编码放入所属地区输入框，将所属地区放入所属地区编码输入框。
               $.ns('namespaceId${idSuffix}').putBmMc(jsonData.SSDQBM,jsonData.SSDQ);

            }


        });




    });
</script>

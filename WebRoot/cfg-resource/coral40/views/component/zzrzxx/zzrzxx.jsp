<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    request.setAttribute("gurl", path + "/zzrzxx!getRzxx.json");
    request.setAttribute("turl", path + "/zzrzxx!getSjzdmc.json");
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<div id="max${idSuffix}" class="fill">
    <ces:layout id="layoutId${idSuffix}" name="" style="width:800px;height:600px;" fit="true">
        <%--<ces:layoutRegion region="west" split="true" minWidth="300" maxWidth="400" style="width:150px;padding:10px;overflow:visible;overflow-x:hidden;overflow-y:scroll;overflow-base-color:white">--%>
            <%--<ces:tree id="treeId${idSuffix}" asyncEnable="true" asyncType="post"--%>
                      <%--data="[{ name:\"数据字典管理\",id:\"-1\",isParent:true}]" asyncUrl="${turl}" asyncAutoParam="ID,NAME"--%>
                      <%--onClick="jQuery.ns('namespaceId${idSuffix}').asyncOnclick" onLoad="asyncExpandnode">--%>
            <%--</ces:tree>--%>
        <%--</ces:layoutRegion>--%>
        <ces:layoutRegion region="center">
            <div class="fill">
                <div class="toolbarsnav clearfix">
                    <ces:toolbar isOverflow="false"
                                 id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '新增', 'id':'add', 'disabled': 'false','type': 'button'},{'label': '修改', 'id':'update', 'disabled': 'false','type': 'button'},{'label': '删除', 'id':'delete', 'disabled': 'false','type': 'button'}]">
                    </ces:toolbar>
                </div>
                <!-- <button class="coral-button coral-component coral-state-default coral-corner-all coral-button-text-only" onClick="alert(1)"><span class="coral-button-text">保存</span></button> -->
                <ces:grid id="gridId${idSuffix}" multiselect="true" rownumbers="false" shrinkToFit="true"
                          forceFit="true" fitStyle="fill" model="grid" url="${gurl}">
                    <ces:gridCols>
                        <ces:gridCol name="CPMC" edittype="text" width="100">产品名称</ces:gridCol>
                        <ces:gridCol name="RZMC" width="180">认证名称</ces:gridCol>
                        <ces:gridCol name="RZJG" width="180">认证机构</ces:gridCol>
                        <ces:gridCol name="RZRQ" width="180">认证日期</ces:gridCol>
                        <ces:gridCol name="ZSBH" width="180">证书编号</ces:gridCol>
                        <ces:gridCol name="YXQ" width="180">有效期</ces:gridCol>
                        <ces:gridCol name="TP" width="180">图片</ces:gridCol>
                        <ces:gridCol name="ID" width="180" hidden="true">ID</ces:gridCol>
                    </ces:gridCols>
                    <ces:gridPager gridId="gridId${idSuffix}"/>
                </ces:grid>
                <div id="hiddenDiv${idSuffix}" style="display: none;">
                    <div id="formDiv${idSuffix}">
                        <ces:toolbar
                                id="formToolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').formToolbarClick"
                                isOverflow="true"
                                data="[{'label': '保存', 'id':'save', 'disabled': 'false','type': 'button'},{'label': '返回', 'id':'CFG_closeComponentZone', 'disabled': 'false','type': 'button'}]">
                        </ces:toolbar>

                        <div class="toolbarsnav clearfix">
                        </div>
                        <%--<ces:form id="form2222${idSuffix}" name="form" action="/zzrzxx!saveAdd.json">--%>
                            <input id="id${idSuffix}" name="id" type="hidden"/>
                            <input id="categoryId${idSuffix}" name="categoryId" type="hidden"/>
                        <form id="enterForm${idSuffix}"
                              enctype="multipart/form-data" method="post">
                                <table style="border-collapse:separate;
                                        border-spacing:10px 10px;align:center;">
                                    <tr>
                                        <td width="100" align="right"><label>产品名称:</label></td>
                                        <td><ces:input id="CPMC_${idSuffix}" name="CPMC" required="true" width="320"/></td>
                                        <td width="100" align="right"><label>认证名称:</label></td>
                                        <td><ces:input id="RZMC_${idSuffix}" name="RZMC" required="true" width="320"/></td>
                                        <td width="100" align="right"><label>认证机构:</label></td>
                                        <td><ces:input id="RZJG_${idSuffix}" name="RZJG" required="true" width="320"/></td>
                                    </tr>
                                    <tr>
                                        <td width="100" align="right"><label>认证日期:</label></td>
                                        <td><ces:datepicker id="RZRQ_${idSuffix}" dateFormat="yyyy-MM-dd" required="true" name="RZRQ" width="320"></ces:datepicker>
                                               </td>
                                        <td width="100" align="right"><label>证书编号:</label></td>
                                        <td><ces:input id="ZSBH_${idSuffix}" name="ZSBH" required="true" width="320"/></td>
                                        <td width="100" align="right"><label>有效期:</label></td>
                                        <td><ces:datepicker id="YXQ_${idSuffix}" dateFormat="yyyy-MM-dd" name="YXQ" required="true"  width="320"></ces:datepicker>
                                                </td><br>
                                    </tr>
                                    <%--<tr>--%>
                                        <%--<td width="100" align="right"><label>图片:</label></td>--%>
                                        <%--<td><input class="inputfile" type="file" single="single" style="width:68px;"--%>
                                                   <%--id="imageUpload${idSuffix}"  lable="预览"--%>
                                                   <%--accept="image/*" name="imageUpload" /></td>--%>
                                        <%--<td><ces:input id="TP_${idSuffix}" name="TP" type="hidden" width="200"/></td>--%>
                                        <%--<td><ces:input id="ID_${idSuffix}" name="ID" type="hidden" width="200"/></td>--%>

                                    <%--</tr>--%>
                                    <%--<div class="app-inputdiv12">--%>
                                        <%--<div id="file${idSuffix}">--%>
                                            <%--<input class="inputfile" type="file" style="width:160px;display:none"--%>
                                                   <%--id="imageUpload${idSuffix}" multiple="multiple" lable="预览"--%>
                                                   <%--accept="image/*" name="imageUpload" onchange="viewImage(this)"/>--%>
                                        <%--</div>--%>

                                        <%--<button style="margin-left: 50px" class='ctrl-toolbar-element ctrl-init ctrl-init-button coral-button coral-component coral-state-default coral-corner-all coral-button-text-only coral-toolbar-item-component'--%>
                                                <%--type='button' onclick="$('#imageUpload${idSuffix}').click()">--%>
                                            <%--<span class="coral-button-text ">选择图片</span>--%>
                                        <%--</button>--%>
                                        <%--<div  style="margin-left: 100px" id="preview${idSuffix}"></div>--%>
                                    <%--</div>--%>
                                    <%--<ces:input id="TP_${idSuffix}" name="TP" type="hidden" width="200"/>--%>
                                    <%--<ces:input id="ID_${idSuffix}" name="ID" type="hidden" width="200"/>--%>

                                </table>
                            <form>
                                <div class="app-inputdiv12">
                                    <div id="file${idSuffix}">

                                        <input class="inputfile" type="file" style="width:160px;display:none"
                                               id="imageUpload${idSuffix}" multiple="multiple" lable="预览"
                                               accept="image/*" name="imageUpload" required="true" onchange="viewImage(this)"/>
                                    </div>

                                    <button style="margin-left: 50px" class='ctrl-toolbar-element ctrl-init ctrl-init-button coral-button coral-component coral-state-default coral-corner-all coral-button-text-only coral-toolbar-item-component'
                                            type='button' onclick="$('#imageUpload${idSuffix}').click()">
                                        <span class="coral-button-text ">选择图片</span>
                                    </button>
                                    <div  style="margin-left: 100px" id="preview${idSuffix}" style="width:160px;height: 160px;"></div>
                                </div>
                                    <ces:input id="TP_${idSuffix}" name="TP" type="hidden" width="200"/>
                                    <ces:input id="ID_${idSuffix}" name="ID" type="hidden" width="200"/>
                        <%--</ces:form>--%>
                    </div>
                </div>
            </div>
        </ces:layoutRegion>
    </ces:layout>
</div>
<script type="text/javascript">
    $.extend($.ns("namespaceId${idSuffix}"), {
        currentTreeNodeId: '',
        asyncOnclick: function (e, treeId, treeNode) {
            if (treeNode.name == "TreeNodeReserve") {
                alert(1);
                CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "TreeNodeReserve", "树节点预留区", "3", $.ns("namespaceId${idSuffix}"));
                return;
            }
            if (treeNode.name == "TreeNodeReserve2") {
                alert(1);
                CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "TreeNodeReserve2", "树节点预留区2", "3", $.ns("namespaceId${idSuffix}"));
                return;
            }
            CFG_clearComponentZone($('#max${idSuffix}').data('configInfo'));
            alert($.ns("namespaceId${idSuffix}").currentTreeNodeId);
            $.ns("namespaceId${idSuffix}").currentTreeNodeId = treeNode.id;
            //自定义url 获得指定的列表数据
            var lxbm = $.ns("namespaceId${idSuffix}").currentTreeNodeId;
            $('#gridId${idSuffix}').grid('option', 'url', "${gurl}");
            $('#gridId${idSuffix}').grid('reload');
        },
        toolbarClick: function (event, button) {
            $("#SJBM_${idSuffix}").textbox("option","onChange",function(e,data){
                var lxbm = $.ns("namespaceId${idSuffix}").currentTreeNodeId;
                var jsonData = $.loadJson($.contextPath + "/qyptsjlx!getIsRepeatBySjbm.json?id=" + $("#ID_${idSuffix}").textbox("getValue") + "&sjbm=" + $("#SJBM_${idSuffix}").textbox("getValue") + "&lxbm="+lxbm);
                if(jsonData === false || jsonData === "false"){
                    $.message( {message:"数据编码重复！", cls:"warning"});
                    $("#SJBM_${idSuffix}").textbox("setValue","");
                }
            });
            $("#SXJB_${idSuffix}").textbox("option","onChange",function(e,data){
                var reg = /^[0-9]*$/;
                var bool = reg.test($("#SXJB_${idSuffix}").textbox("getValue"));
                if(!bool){
                    $.message( {message:"请输入数字！", cls:"warning"});
                    $("#SXJB_${idSuffix}").textbox("setValue","");
                    return;
                }
                var lxbm = $.ns("namespaceId${idSuffix}").currentTreeNodeId;
                var jsonData = $.loadJson($.contextPath + "/qyptsjlx!getIsRepeatBySxjb.json?id=" + $("#ID_${idSuffix}").textbox("getValue") + "&sxjb=" + $("#SXJB_${idSuffix}").textbox("getValue") + "&lxbm=" + lxbm);
                if(jsonData === false || jsonData === "false"){
                    $.message( {message:"顺序级别重复！", cls:"warning"});
                    $("#SXJB_${idSuffix}").textbox("setValue","");
                }
            });
            if(button.id == "add"){
               // var lxbm = $.ns("namespaceId${idSuffix}").currentTreeNodeId;
                //var jsonData = $.loadJson($.contextPath + "/qyptsjlx!getMaxSxjbByLxbm.json?lxbm="+lxbm);
               // $("#SXJB_${idSuffix}").textbox("setValue",jsonData.SXJB);
                //var gGrid = $("#gridId${idSuffix}");
               // var ids = gGrid.grid("option", "selarrrow");

                var div = CFG_getEmbeddedDiv($('#max${idSuffix}').data('configInfo'), "", "新增", function () {
                    $('#hiddenDiv${idSuffix}').append($('#formDiv${idSuffix}'));
                });
                div.append($('#formDiv${idSuffix}'));
                $(":coral-toolbar").toolbar("refresh");
                $("#CPMC_${idSuffix}").textbox("setValue", '');
                $("#RZMC_${idSuffix}").textbox("setValue", '');
                $("#RZJG_${idSuffix}").textbox("setValue", '');
                $("#RZRQ_${idSuffix}").datepicker("option", 'value','');
                $("#ZSBH_${idSuffix}").textbox("setValue", '');
                $("#YXQ_${idSuffix}").datepicker("option", 'value','');
                $("#ID_${idSuffix}").textbox("setValue", '');
                //$("#imageUpload${idSuffix}").textbox("setValue", '');
                //imageUpload${idSuffix}
                var preview = $("#preview${idSuffix}");
                <%--var file = $("#imageUpload${idSuffix}");--%>
                var fileDiv = $("#file${idSuffix}");
                preview.empty();
                //fileDiv.empty();

                document.getElementById('imageUpload${idSuffix}').value='';
                var a=document.getElementById('imageUpload${idSuffix}').value;

            }else if (button.id == "update") {
                var gGrid = $("#gridId${idSuffix}");
                var ids = gGrid.grid("option", "selarrrow");

                if (ids.length == 0) {
                    alert("请至少选择一条记录");
                    return;
                } else if (ids.length > 1) {
                    alert("请选择一条记录");
                    return;
                } else {
                    var div = CFG_getEmbeddedDiv($('#max${idSuffix}').data('configInfo'), "", "修改", function () {
                        $('#hiddenDiv${idSuffix}').append($('#formDiv${idSuffix}'));
                    });
                    rowData = gGrid.grid("getRowData", ids.toString());
                    $("#CPMC_${idSuffix}").textbox("setValue", rowData.CPMC);
                    $("#RZMC_${idSuffix}").textbox("setValue", rowData.RZMC);
                    $("#RZJG_${idSuffix}").textbox("setValue", rowData.RZJG);
                    $("#RZRQ_${idSuffix}").datepicker("setValue", rowData.RZRQ);
                    $("#ZSBH_${idSuffix}").textbox("setValue", rowData.ZSBH);
                    $("#YXQ_${idSuffix}").datepicker("setValue", rowData.YXQ);

                    $("#ID_${idSuffix}").textbox("setValue", rowData.ID);
                    div.append($('#formDiv${idSuffix}'));
                    $(":coral-toolbar").toolbar("refresh");
                }
            }else if(button.id == "delete"){
                var gGrid = $("#gridId${idSuffix}");
                var ids = gGrid.grid("option", "selarrrow");
                if (ids.length == 0) {
                    alert("请至少选择一条记录");
                    return;
                }
                var gGrid = $("#gridId${idSuffix}");
                var ids = gGrid.grid("option", "selarrrow");
                var rowData = "";
                $.each(ids,function(e,data){
                    rowData += gGrid.grid("getRowData", data.toString()).ID+"___";

                });
                var jsonData = $.loadJson($.contextPath +　"/zzrzxx!deleteById.json?ids=" + rowData);
                if(jsonData === true || jsonData=== "true"){
                    CFG_message("删除成功！", "success");
                }else{
                    CFG_message("删除失败！", "error");
                }
                var lxbm = $.ns("namespaceId${idSuffix}").currentTreeNodeId;
                $('#gridId${idSuffix}').grid('option', 'url', "${gurl}");
                $('#gridId${idSuffix}').grid('reload');
            }
        },
        formToolbarClick: function (event, button) {
            if (button.id == "save") {

                var cpmc=$("#CPMC_${idSuffix}").textbox("getValue");
                var rzmc=$("#RZMC_${idSuffix}").textbox("getValue");
                var rzjg=$("#RZJG_${idSuffix}").textbox("getValue");
                var rzrq=$("#RZRQ_${idSuffix}").datepicker("option", 'value');
                var zsbh=$("#ZSBH_${idSuffix}").textbox("getValue");
                var yxq=$("#YXQ_${idSuffix}").datepicker("option", 'value');

                var a=document.getElementById('imageUpload${idSuffix}').value;

                if(a==''||cpmc==''||rzmc==''||cpmc==''||rzjg==''||rzrq==''||zsbh==''||yxq==''){CFG_message("请将信息填写完整！", "error");return}

                $("#enterForm${idSuffix}").form().submit();



            } else if (button.id == "CFG_closeComponentZone") {
                if (window.CFG_clickReturnButton) {
                    CFG_clickReturnButton($('#max${idSuffix}').data('configInfo'));
                    $("#SJBM_${idSuffix}").textbox("setValue", "");
                    $("#SJMC_${idSuffix}").textbox("setValue", "");
                    $("#SXJB_${idSuffix}").textbox("setValue", "");
                    $("#ID_${idSuffix}").textbox("setValue", "");
                    //var lxbm = $.ns("namespaceId${idSuffix}").currentTreeNodeId;
                    $('#gridId${idSuffix}').grid('option', 'url', "${gurl}");
                    $('#gridId${idSuffix}').grid('reload');
                }
            }
        },
    });
    var buttonOpts = {
        id: "combogrid_buttonId",
        label: "构件按钮",
        icons: "icon-checkmark-circle",
        text: false,
        onClick: $.ns('namespaceId${idSuffix}').csxxClick
    };

    var rowData;
    $("#enterForm${idSuffix}").submit(
            function(){
                url = $.contextPath + "/zzrzxx!saveAdd.json";
                //aler($("#imageUpload${idSuffix}").value);
                //var timestamp=new Date();
                //alert(document.getElementById('imageUpload${idSuffix}').value);
                //alert(timestamp);
                var TP = document.getElementById('imageUpload${idSuffix}').value;
                //alert(TP);
                var formdata = new FormData(this);
                $.ajax({
                    type: "post",
                    url: url,
                    data: formdata,
                    contentType: false,
                    /**
                     * 必须false才会避开jQuery对 formdata 的默认处理
                     * XMLHttpRequest会对 formdata 进行正确的处理
                     */
                    processData: false,
                    timestamp: false,

                    dataType: 'json',
                    success: function (res) {
                        $("#ID_${idSuffix}").textbox("setValue", res);
                        CFG_message("保存成功！", "success");
                        var preview = $("#preview${idSuffix}");
                        <%--var file = $("#imageUpload${idSuffix}");--%>
                        var fileDiv = $("#file${idSuffix}");
                       // preview.empty();
                        fileDiv.empty();
                        fileDiv.append('<input class="inputfile" type="file" style="width:160px;display:none;" id="imageUpload${idSuffix}" multiple="multiple" lable="预览" accept="image/*" name="imageUpload" onchange="viewImage(this)"/>');
                        //init();
                    },
                    error: function () {
                        CFG_message("保存失败！", "error");
                    }


                });
                return false;
            }
    );

    $(function () {

        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'zzrzxx.jsp',
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
                // 按钮权限控制
                //alert(configInfo.notAuthorityComponentButtons);
                if (configInfo.notAuthorityComponentButtons) {
                    //$("toolbar").toolbar("refresh");
                    $.each(configInfo.notAuthorityComponentButtons, function (i, v) {
                        if (v == 'add') {
                            $('#toolbarId${idSuffix}').toolbar('disableItem', 'add');
                            //$('#toolbarId${idSuffix}').toolbar('hide', 'add');
                        } else if (v == 'update') {
                            //$('#toolbarId${idSuffix}').toolbar('disableItem', 'update');
                            //$('#toolbarId${idSuffix}').toolbar('hide', 'update');
                        } else if (v == 'delete') {
                            //$('#toolbarId${idSuffix}').toolbar('disableItem', 'delete');
                            $('#toolbarId${idSuffix}').toolbar('hide', 'delete');
                        }
                    });
                }
            }

        });




    });
    document.getElementById('preview${idSuffix}').addEventListener("click", function (e) {
        if (e.target.tagName.toLowerCase() == 'img' && e.target.className == 'thumb') {
            e.target.className = '';
        } else if (e.target.tagName.toLowerCase() == 'img' && e.target.className == '') {
            e.target.className = 'thumb';
        }
    });

    function viewImage(fileInput) {
        if (window.FileReader) {
            var p = $("#preview${idSuffix}");
            var file = fileInput.files;
            for (var i = 0, f; f = file[i]; i++) {
                var ImgSuffix = f.name.substring(f.name.lastIndexOf('.')+1);

                //图片不能超过2M，
                if(!(f.size<2097152 && (ImgSuffix=='jpg'||ImgSuffix=='bmp'||ImgSuffix=='png'))){
                    CFG_message("图片必须小于2M，且格式必须为png,jpg或pmg！", "error");
                    return false;
                }

                var fileReader = new FileReader();
                fileReader.onload = (function (file) {
                    return function (e) {
                        var span = document.createElement('span');
                        span.innerHTML = '<img class = "thumb" src = "' + this.result + '" alt = "' + file.name + '" />';
                        p.append(span);
//                        p.insertBefore(span,null);
                    }
                })(f);
                fileReader.readAsDataURL(f);
            }
        }
    }

</script>

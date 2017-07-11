<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    request.setAttribute("gurl", path + "/zzrzxx!getRzxx.json");
    request.setAttribute("turl", path + "/zzrzxx!getSjzdmc.json");
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
    String resourceFolder = path + "/cfg-resource/coral40/common";
%>
<style type="text/css">
    .coral-textbox, select, .coral-combo {
        width: 320px;
    }
</style>
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
                                data="['->',{'label': '返回', 'id':'CFG_closeComponentZone', 'cls':'return_tb','disabled': 'false','type': 'button'}]">
                            <%--data="[{'label': '保存', 'id':'save', 'disabled': 'false','type': 'button'},{'label': '返回', 'id':'CFG_closeComponentZone', 'disabled': 'false','type': 'button'}]">--%>
                        </ces:toolbar>
                        <div class='homeSpan' style="margin-top: -23px;"><div><div style='margin-left:20px;width: 100px;' id="nva${idSuffix}"> -认证信息-详情 </div></div></div>
                        <div class="toolbarsnav clearfix">
                        </div>
                            <%--<ces:form id="form2222${idSuffix}" name="form" action="/zzrzxx!saveAdd.json">--%>
                        <input id="id${idSuffix}" name="id" type="hidden"/>
                        <input id="categoryId${idSuffix}" name="categoryId" type="hidden"/>
                        <form id="enterForm${idSuffix}"
                              enctype="multipart/form-data" method="post" class="coralui-form">
                            <div class="fillwidth colspan3 clearfix">
                                <div class="app-inputdiv4">
                                    <label class="app-input-label" style="width:26%;">产品名称:</label>
                                    <input id="CPMC_${idSuffix}" name="CPMC" data-options="required:true" class="coralui-textbox"/>
                                </div>
                                <div class="app-inputdiv4">
                                    <label class="app-input-label" style="width:26%;">认证名称:</label>
                                        <%--<ces:datepicker id="jfsj${idSuffix}" required="true" name="jfsj" value="2015-10-12"></ces:datepicker>--%>
                                    <input id="RZMC_${idSuffix}" name="RZMC" data-options="required:true" class="coralui-textbox"/>
                                </div>
                                <div class="app-inputdiv4">
                                    <label class="app-input-label" style="width:26%;">认证机构:</label>
                                        <%--<ces:datepicker id="jfsj${idSuffix}" required="true" name="jfsj" value="2015-10-12"></ces:datepicker>--%>
                                    <input id="RZJG_${idSuffix}" name="RZJG" data-options="required:true" class="coralui-textbox"/>
                                </div>
                                <div class="app-inputdiv4">
                                    <label class="app-input-label" style="width:26%;">认证日期:</label>
                                        <%--<ces:datepicker id="jfsj${idSuffix}" required="true" name="jfsj" value="2015-10-12"></ces:datepicker>--%>
                                    <input id="RZRQ_${idSuffix}" dateFormat="yyyy-MM-dd" name="RZRQ" class="coralui-datepicker"/>
                                </div>
                                <div class="app-inputdiv4">
                                    <label class="app-input-label" style="width:26%;">证书编号:</label>
                                        <%--<ces:datepicker id="jfsj${idSuffix}" required="true" name="jfsj" value="2015-10-12"></ces:datepicker>--%>
                                    <input id="ZSBH_${idSuffix}" name="ZSBH" class="coralui-textbox"/>
                                </div>
                                <div class="app-inputdiv4">
                                    <label class="app-input-label" style="width:26%;">有效期:</label>
                                        <%--<ces:datepicker id="jfsj${idSuffix}" required="true" name="jfsj" value="2015-10-12"></ces:datepicker>--%>
                                    <input id="YXQ_${idSuffix}" dateFormat="yyyy-MM-dd" name="YXQ"  class="coralui-datepicker">
                                </div>
                            </div>
                                <%--<table style="border-collapse:separate;--%>
                                <%--border-spacing:10px 10px;align:center;">--%>
                                <%--<tr>--%>
                                <%--<td width="100" align="right"><label>产品名称:</label></td>--%>
                                <%--<td><input id="CPMC_${idSuffix}" name="CPMC" data-options="required:true" class="coralui-textbox"/></td>--%>
                                <%--<td width="100" align="right"><label>认证名称:</label></td>--%>
                                <%--<td><input id="RZMC_${idSuffix}" name="RZMC" data-options="required:true" class="coralui-textbox"/></td>--%>
                                <%--<td width="100" align="right"><label>认证机构:</label></td>--%>
                                <%--<td><input id="RZJG_${idSuffix}" name="RZJG" data-options="required:true" class="coralui-textbox"/></td>--%>
                                <%--</tr>--%>
                                <%--<tr>--%>
                                <%--<td width="100" align="right"><label>认证日期:</label></td>--%>
                                <%--<td><ces:datepicker id="RZRQ_${idSuffix}" dateFormat="yyyy-MM-dd" name="RZRQ" width="320"></ces:datepicker>--%>
                                <%--</td>--%>
                                <%--<td width="100" align="right"><label>证书编号:</label></td>--%>
                                <%--<td><ces:input id="ZSBH_${idSuffix}" name="ZSBH" width="320"/></td>--%>
                                <%--<td width="100" align="right"><label>有效期:</label></td>--%>
                                <%--<td><ces:datepicker id="YXQ_${idSuffix}" dateFormat="yyyy-MM-dd" name="YXQ"  width="320"></ces:datepicker>--%>
                                <%--</td><br>--%>
                                <%--</tr>--%>
                                <%--&lt;%&ndash;<tr>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;<td width="100" align="right"><label>图片:</label></td>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;<td><input class="inputfile" type="file" single="single" style="width:68px;"&ndash;%&gt;--%>
                                <%--&lt;%&ndash;id="imageUpload${idSuffix}"  lable="预览"&ndash;%&gt;--%>
                                <%--&lt;%&ndash;accept="image/*" name="imageUpload" /></td>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;<td><ces:input id="TP_${idSuffix}" name="TP" type="hidden" width="200"/></td>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;<td><ces:input id="ID_${idSuffix}" name="ID" type="hidden" width="200"/></td>&ndash;%&gt;--%>

                                <%--&lt;%&ndash;</tr>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;<div class="app-inputdiv12">&ndash;%&gt;--%>
                                <%--&lt;%&ndash;<div id="file${idSuffix}">&ndash;%&gt;--%>
                                <%--&lt;%&ndash;<input class="inputfile" type="file" style="width:160px;display:none"&ndash;%&gt;--%>
                                <%--&lt;%&ndash;id="imageUpload${idSuffix}" multiple="multiple" lable="预览"&ndash;%&gt;--%>
                                <%--&lt;%&ndash;accept="image/*" name="imageUpload" onchange="viewImage(this)"/>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;</div>&ndash;%&gt;--%>

                                <%--&lt;%&ndash;<button style="margin-left: 50px" class='ctrl-toolbar-element ctrl-init ctrl-init-button coral-button coral-component coral-state-default coral-corner-all coral-button-text-only coral-toolbar-item-component'&ndash;%&gt;--%>
                                <%--&lt;%&ndash;type='button' onclick="$('#imageUpload${idSuffix}').click()">&ndash;%&gt;--%>
                                <%--&lt;%&ndash;<span class="coral-button-text ">选择图片</span>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;</button>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;<div  style="margin-left: 100px" id="preview${idSuffix}"></div>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;<ces:input id="TP_${idSuffix}" name="TP" type="hidden" width="200"/>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;<ces:input id="ID_${idSuffix}" name="ID" type="hidden" width="200"/>&ndash;%&gt;--%>

                                <%--</table>--%>
                            <form>
                                <div class="app-inputdiv12">
                                    <div style="position:absolute;margin: 4px 0 0 35px;">图片:</div>
                                    <div id="file${idSuffix}">

                                        <input class="inputfile" type="file" style="width:160px;display:none"
                                               id="imageUpload${idSuffix}" multiple="multiple" lable="预览"
                                               accept="image/*" name="imageUpload" onchange="viewImage(this)"/>
                                    </div>
                                    <div  style="margin-left: 88px;display: inline-block;" id="preview${idSuffix}" style="width:160px;height: 160px;"></div>
                                    <%--<button style="margin-top: 0px" id="chooseImage${idSuffix}" class='ctrl-toolbar-element ctrl-init ctrl-init-button coral-button coral-component coral-state-default coral-corner-all coral-button-text-only coral-toolbar-item-component'--%>
                                            <%--type='button' onclick="$('#imageUpload${idSuffix}').click()">--%>
                                        <%--<span class="coral-button-text ">选择图片</span>--%>
                                    <%--</button>--%>

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
                CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "TreeNodeReserve", "树节点预留区", "3", $.ns("namespaceId${idSuffix}"));
                return;
            }
            if (treeNode.name == "TreeNodeReserve2") {
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

                if(!$("#enterForm${idSuffix}").form("valid")){CFG_message("请将信息填写完整！", "error");return}

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
                CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));
            }
        },
        loadImage:function(tplj){
            var image = new Image();
            image.src = tplj;
            image.className = "thumb";
            image.onload = function (e) {
                var div = document.createElement('div');
                $(div).append(this);
                $(div).append('<span style="display: none;position: absolute; margin-top: 6px; margin-left: 6px; width: 20px; height: 20px;"></span>');
                div.style.float = "left";
                div.style.marginTop = "10px";
                div.style.marginRight = "5px";
                div.style.height = "100px";
                if(((this.width * 100) / this.height) > 750){
                    div.style.width = "750px";
                }else {
                    div.style.width = (this.width * 100) / this.height + "px";
                }
                $(this).css("width",div.style.width);
                $(this).css("height",div.style.height);
                div.style.float = "left";
               // $(div).hover(function () {
                   // $(div).find("span").toggle();
               // });
                $("#preview${idSuffix}").append(div);
                if($("#preview${idSuffix}").css("width") != "0px"){
                    $("#chooseImage${idSuffix}").css("margin-top","-17px");
                }
            };
        }
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
                var TP = document.getElementById('imageUpload${idSuffix}').value;
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
                    async:false,
                    dataType: 'json',
                    success: function (res) {
                        $("#ID_${idSuffix}").textbox("setValue", res);
                        CFG_message("保存成功！", "success");
                        var preview = $("#preview${idSuffix}");
                        <%--var file = $("#imageUpload${idSuffix}");--%>
                        var fileDiv = $("#file${idSuffix}");
                        fileDiv.empty();
                        fileDiv.append('<input class="inputfile" type="file" style="width:160px;display:none;" id="imageUpload${idSuffix}" multiple="multiple" lable="预览" accept="image/*" name="imageUpload" onchange="viewImage(this)"/>');
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
            'page': 'zzrzxxxz.jsp',
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
            <%--CFG_setReturnButton(configInfo, $('#formToolbarId${idSuffix}'));--%>
            <%--},--%>
            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {
                /******************************新增 begin***************************/
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
                var preview = $("#preview${idSuffix}");
                <%--var file = $("#imageUpload${idSuffix}");--%>
                var fileDiv = $("#file${idSuffix}");
                preview.empty();

                document.getElementById('imageUpload${idSuffix}').value='';
                var a=document.getElementById('imageUpload${idSuffix}').value;
                /******************************新增 end***************************/

                /******************************修改 begin***************************/
                var rowDataId = CFG_getInputParamValue(configInfo, 'dataId')
                if(rowDataId != ""){
                    $("#enterForm${idSuffix}").form("setReadOnly",true);
                    var url = $.contextPath + "/zzrzxx!getRzxxById.json?id="+rowDataId;
                    var rowDataJson = $.loadJson(url);
                    $("#CPMC_${idSuffix}").textbox("setValue", rowDataJson.CPMC);
                    $("#RZMC_${idSuffix}").textbox("setValue", rowDataJson.RZMC);
                    $("#RZJG_${idSuffix}").textbox("setValue", rowDataJson.RZJG);
                    $("#RZRQ_${idSuffix}").datepicker("setValue", rowDataJson.RZRQ);
                    $("#ZSBH_${idSuffix}").textbox("setValue", rowDataJson.ZSBH);
                    $("#YXQ_${idSuffix}").datepicker("setValue", rowDataJson.YXQ);
                    if(rowDataJson.TP != null && rowDataJson.TP != ""){
                        $.ns("namespaceId${idSuffix}").loadImage(($.contextPath + '/spzstpfj/' + rowDataJson.TP));
                        <%--var span = document.createElement('span');--%>
                        <%--span.innerHTML = '<img class = "thumb" style = "position:relative;margin:10px 5px 0px 0px;width:100px;height:100px" src = "' + ($.contextPath + '/spzstpfj/' + rowDataJson.TP) + '" />';--%>
                        <%--$("#preview${idSuffix}").append(span);--%>

                    }
                    $("#ID_${idSuffix}").textbox("setValue", rowDataJson.ID);
                    div.append($('#formDiv${idSuffix}'));
                    $(":coral-toolbar").toolbar("refresh");
                }
                /******************************修改 end***************************/

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
    <%--document.getElementById('preview${idSuffix}').addEventListener("click", function (e) {--%>
        <%--if (e.target.tagName.toLowerCase() == 'img' && e.target.className == 'thumb') {--%>
            <%--e.target.className = '';--%>
        <%--} else if (e.target.tagName.toLowerCase() == 'img' && e.target.className == '') {--%>
            <%--e.target.className = 'thumb';--%>
        <%--}--%>
    <%--});--%>

    function viewImage(fileInput) {
        if (window.FileReader) {
            var p = $("#preview${idSuffix}");
            p.empty();
            var file = fileInput.files;
            for (var i = 0, f; f = file[i]; i++) {
                var fileReader = new FileReader();
                fileReader.onload = (function (file) {
                    return function (e) {
                        var image = new Image();
                        image.src = this.result;
                        image.className = "thumb";
                        if(((image.width * 100) / image.height) > 750){
                            $(image).css("width","750px");
                        }else{
                            $(image).css("width", ((image.width * 100) / image.height) +  "px");
                        }
                        $(image).css("height","100px");
                        image.onload = function (e) {
                            var div = document.createElement('div');
                            $(div).append(this);
                            var originWidth = this.width;
                            var originHeight = this.height;
                            div.style.float = "left";
                            div.style.marginTop = "10px";
                            div.style.marginRight = "5px";
                            div.style.height = "100px";
                            if(((originWidth * 100) / originHeight) > 750){
                                div.style.width = 750 + "px";
                            }else{
                                div.style.width = (originWidth * 100) / originHeight + "px";
                            }
                            p.append(div);
                            $("#chooseImage${idSuffix}").css("margin-top","-17px");
                        }
                    }
                })(f);
                fileReader.readAsDataURL(f);
            }
        }
    }



    function deleteImage(src) {
        var strArray = src.split("/");
        var tplj = strArray[strArray.length - 1];
        $.ajax({
            type: "POST",
            url: $.contextPath + '/zzrzxx!deleteImage.json',
            data: {'tplj': tplj},
            dataType: "json",
            success: function (data) {
                if (1 == data) {
                    $("#preview${idSuffix}").empty();
                    $("#chooseImage${idSuffix}").css("margin-top","0px");
                }
            }
        })
    }
</script>

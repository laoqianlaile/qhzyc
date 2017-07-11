<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    request.setAttribute("gurl", path + "/qyptsjlx!getSjzdByLxbm.json");
    request.setAttribute("turl", path + "/qyptsjlx!getSjzdmc.json");
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<div id="max${idSuffix}" class="fill">
    <ces:layout id="layoutId${idSuffix}" name="" style="width:800px;height:600px;" fit="true">
    <ces:layoutRegion region="west" split="true" minWidth="300" maxWidth="400" style="width:150px;padding:10px;overflow:visible;overflow-x:hidden;overflow-y:scroll;overflow-base-color:white">
			<ces:tree id="treeId${idSuffix}" asyncEnable="true" asyncType="post"
				data="[{ name:\"数据字典管理\",id:\"-1\",isParent:true,dicym:\"dicym\"}]"
				asyncUrl="${turl}" asyncAutoParam="ID,NAME"
				onClick="jQuery.ns('namespaceId${idSuffix}').asyncOnclick"
				onLoad="asyncExpandnode">
			</ces:tree>
		</ces:layoutRegion>
    <ces:layoutRegion region="center">
    </ces:layoutRegion>
    <%--<ces:layoutRegion region="center">
        <div class="fill" style="display: none" id="rightDiv">
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
                    <ces:gridCol name="SJBM" edittype="text" width="100">分类编码</ces:gridCol>
                    <ces:gridCol name="SJMC" width="180">分类名称</ces:gridCol>
                    <ces:gridCol name="ID" width="180" hidden="true">ID</ces:gridCol>
                    <ces:gridCol name="SXJB" width="180" hidden="true">顺序级别</ces:gridCol>
                </ces:gridCols>
                <ces:gridPager gridId="gridId${idSuffix}"/>
            </ces:grid>
            <div id="hiddenDiv${idSuffix}" style="display: none;">
                <div id="formDiv${idSuffix}">
                    <ces:toolbar
                            id="formToolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').formToolbarClick"
                            isOverflow="true"
                            data="[{'label': '保存', 'id':'save', 'disabled': 'false','type': 'button'},{'label': '保存并新增', 'id':'saveAndCreate', 'disabled': 'false','type': 'button'},{'label': '保存并关闭', 'id':'saveAndClose', 'disabled': 'false','type': 'button'},{'label': '返回', 'id':'CFG_closeComponentZone', 'disabled': 'false','type': 'button'}]">
                    </ces:toolbar>

                    <div class="toolbarsnav clearfix">
                    </div>
                    <ces:form id="form${idSuffix}" name="form" action="/qyptmdgl!saveQyht.json">
                        <input id="id${idSuffix}" name="id" type="hidden"/>
                        <input id="categoryId${idSuffix}" name="categoryId" type="hidden"/>

                        <div>
                            <table style="border-collapse:separate;
                                    border-spacing:10px 10px;">
                                <tr>
                                    <td width="100" align="right"><label>数据编码:</label></td>
                                    <td><ces:input id="SJBM_${idSuffix}" name="SJBM" required="true" width="200"/></td>
                                    <td width="100" align="right"><label>数据名称:</label></td>
                                    <td><ces:input id="SJMC_${idSuffix}" name="SJMC" required="true" width="200"/></td>
                                    <td width="100" align="right"><label>顺序级别:</label></td>
                                    <td><ces:input id="SXJB_${idSuffix}" name="SXJB" required="true" width="200"/></td>
                                    <td><ces:input id="ID_${idSuffix}" name="ID" type="hidden" width="200"/></td>
                                </tr>
                            </table>
                        </div>
                    </ces:form>
                </div>
            </div>
        </div>
    </ces:layoutRegion>--%>
</ces:layout>
</div>
<script type="text/javascript">
	data="[{ name:\"数据字典\",id:\"-1\",isParent:true}]"
    $.extend($.ns("namespaceId${idSuffix}"), {
        currentTreeNodeId: '',
        currentTreeNodeName: '',
        currentTreeId:'treeId${idSuffix}',
        addLeftNode:function(treeNode){
        	var $tree = $("#treeId${idSuffix}");
            var selNode = $tree.tree("getNodes");
            $tree.tree("addNodes",selNode[0],treeNode,true);
        },
        addLeftChildNode:function(treeNode){
        	var $tree = $("#treeId${idSuffix}");
            var selNode = $tree.tree("getSelectedNodes");
            $tree.tree("addNodes",selNode[0],treeNode,true);
        },
        updateLeftNode:function(treeNode){
        	var $tree = $("#treeId${idSuffix}");
        	var selNode  = $tree.tree("getNodes")[0].children;
        	for(var i=0;i<selNode.length;i++){
        		if(selNode[i].id == treeNode.id){
            		selNode[i].name = treeNode.name;
            		$tree.tree("updateNode",selNode[i]);        			
        		}
        	}
        },
        //更新选中的节点下要修改的子节点
        updateLeftChildNode:function(treeNode){
        	var $tree = $("#treeId${idSuffix}");
            var selNode = $tree.tree("getSelectedNodes")[0].children;
            for(var i=0;i<selNode.length;i++){
            	if(selNode[i].id == treeNode.id){
            		selNode[i].name = treeNode.name;
            		$tree.tree("updateNode",selNode[i]);
            	}
            }
            //selNode[0].name = "gengxin";
            //$tree.tree("updateNode",selNode[0]);
        },
        reloadTree : function (){
            var $tree = $("#treeId${idSuffix}");
            $tree.tree("reload",{asyncUrl:"${turl}"});
        },
        deleteLeftTreeNode: function (treeNodeIds) {
		    var $tree = $("#treeId${idSuffix}");
		    var selNode = $tree.tree("getSelectedNodes")[0];
		    var childrenNodes = selNode.children;
		    for (var i in treeNodeIds) {
			    for (var j in childrenNodes) {
				    if(childrenNodes[j].id == treeNodeIds[i]){
					    $tree.tree("removeNode",childrenNodes[j]);
				    }
			    }
		    }
	    },
	    //删除父节点
	    deleteLeftNode: function (treeNodeIds) {
		    var $tree = $("#treeId${idSuffix}");
		    //从根节点获取所有子节点
		    var selNode = $tree.tree("getNodes")[0].children;
		    for(var i in treeNodeIds){
		    	for(var j in selNode)
		    	//遍历子节点，获取其id，一一与参数id比较
		    	if((selNode[j].id == treeNodeIds[i])){
		    		$tree.tree("removeNode",selNode[j]);
		    	}
		    }
	    },
        asyncOnclick: function (e, treeId, treeNode) {
            $.ns("namespaceId${idSuffix}").currentTreeNodeId = treeNode.id;
            $.ns("namespaceId${idSuffix}").currentTreeNodeName = treeNode.name;
            //数据字典页面
            if (treeNode.dicym == "dicym") {
                CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "dicym", treeNode.name, "3", $.ns("namespaceId${idSuffix}"));
                return;
            }
           //数据 字典所属系统节点
            if (treeNode.dicxtjd == "dicxtjd") {
                CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "dicxtjd", treeNode.name, "3", $.ns("namespaceId${idSuffix}"));
                return;
            }
            //数据 字典节点
            if (treeNode.dicjd == "dicjd") {
                CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "dicjd", treeNode.name, "3", $.ns("namespaceId${idSuffix}"));
                return;
            }
            //数据字典子节点
            if (treeNode.diczjd == "diczjd") {
                CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "diczjd", treeNode.name, "3", $.ns("namespaceId${idSuffix}"));
                return;
            }
            CFG_clearComponentZone($('#max${idSuffix}').data('configInfo'));
/*             //alert($.ns("namespaceId${idSuffix}").currentTreeNodeId);
            $.ns("namespaceId${idSuffix}").currentTreeNodeId = treeNode.id;
            //自定义url 获得指定的列表数据
            var lxbm = $.ns("namespaceId${idSuffix}").currentTreeNodeId;
            if(lxbm == "-1"){
                $("#rightDiv").css("display","none"); 
            }
            $('#gridId${idSuffix}').grid('option', 'url', "${gurl}?lxbm=" + lxbm);
            $('#gridId${idSuffix}').grid('reload');*/
        },
        /*toolbarClick: function (event, button) {
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
         var lxbm = $.ns("namespaceId${idSuffix}").currentTreeNodeId;
         var jsonData = $.loadJson($.contextPath + "/qyptsjlx!getMaxSxjbByLxbm.json?lxbm="+lxbm);
         $("#SXJB_${idSuffix}").textbox("setValue",jsonData.SXJB);
         var gGrid = $("#gridId${idSuffix}");
         var ids = gGrid.grid("option", "selarrrow");
         var div = CFG_getEmbeddedDiv($('#max${idSuffix}').data('configInfo'), "", "新增", function () {
         $('#hiddenDiv${idSuffix}').append($('#formDiv${idSuffix}'));
         });
         div.append($('#formDiv${idSuffix}'));
         $(":coral-toolbar").toolbar("refresh");
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
         $("#SJBM_${idSuffix}").textbox("setValue", rowData.SJBM);
         $("#SJMC_${idSuffix}").textbox("setValue", rowData.SJMC);
         $("#SXJB_${idSuffix}").textbox("setValue", rowData.SXJB);
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
         rowData += gGrid.grid("getRowData", data.toString()).ID + "___";
         });
         var jsonData = $.loadJson($.contextPath +"/qyptsjlx!deleteById.json?ids=" + rowData);
         if(jsonData === true || jsonData=== "true"){
         CFG_message("删除成功！", "success");
         }else{
         CFG_message("删除失败！", "error");
         }
         var lxbm = $.ns("namespaceId${idSuffix}").currentTreeNodeId;
         $('#gridId${idSuffix}').grid('option', 'url', "${gurl}?lxbm=" + lxbm);
         $('#gridId${idSuffix}').grid('reload');
         }
         },
         formToolbarClick: function (event, button) {
         if (button.id == "save") {
         if (!$('#form${idSuffix}').form("valid")){CFG_message("请将数据填写完整！", "warning");return ;}else{
         url = $.contextPath + "/qyptsjlx!saveAdd.json";
         $.ajax({
         type: "post",
         url: url,
         data: {
         SJBM: $("#SJBM_${idSuffix}").val(),
         SJMC: $("#SJMC_${idSuffix}").val(),
         SXJB: $("#SXJB_${idSuffix}").val(),
         LXBM: $.ns("namespaceId${idSuffix}").currentTreeNodeId,
         ID:$("#ID_${idSuffix}").val()
         },
         dataType: 'json',
         success: function (res) {
         $("#ID_${idSuffix}").textbox("setValue", res);
         CFG_message("保存成功！", "success");
         },
         error: function () {
         CFG_message("保存失败！", "error");
         }
         });
         }
         } else if (button.id == "CFG_closeComponentZone") {
         if (window.CFG_clickReturnButton) {
         CFG_clickReturnButton($('#max${idSuffix}').data('configInfo'));
         $("#SJBM_${idSuffix}").textbox("setValue", "");
         $("#SJMC_${idSuffix}").textbox("setValue", "");
         $("#SXJB_${idSuffix}").textbox("setValue", "");
         $("#ID_${idSuffix}").textbox("setValue", "");
         var lxbm = $.ns("namespaceId${idSuffix}").currentTreeNodeId;
         $('#gridId${idSuffix}').grid('option', 'url', "${gurl}?lxbm=" + lxbm);
         $('#gridId${idSuffix}').grid('reload');
         }
         }else if(button.id == "saveAndCreate"){
         if (!$('#form${idSuffix}').form("valid")){CFG_message("请将数据填写完整！", "warning");return ;}else{
         url = $.contextPath + "/qyptsjlx!saveAdd.json";
         $.ajax({
         type: "post",
         url: url,
         data: {
         SJBM: $("#SJBM_${idSuffix}").val(),
         SJMC: $("#SJMC_${idSuffix}").val(),
         SXJB: $("#SXJB_${idSuffix}").val(),
         LXBM: $.ns("namespaceId${idSuffix}").currentTreeNodeId,
         ID:$("#ID_${idSuffix}").val()
         },
         dataType: 'json',
         success: function (res) {
         $("#ID_${idSuffix}").textbox("setValue", res);
         CFG_message("保存成功！", "success");
         $("#SJBM_${idSuffix}").textbox("setValue", "");
         $("#SJMC_${idSuffix}").textbox("setValue", "");
         var lxbm = $.ns("namespaceId${idSuffix}").currentTreeNodeId;
         var jsonData = $.loadJson($.contextPath + "/qyptsjlx!getMaxSxjbByLxbm.json?lxbm="+lxbm);
         $("#SXJB_${idSuffix}").textbox("setValue",jsonData.SXJB);
         $("#ID_${idSuffix}").textbox("setValue", "");
         },
         error: function () {
         CFG_message("保存失败！", "error");
         }
         });

         }
         }else if(button.id == "saveAndClose"){
         if (!$('#form${idSuffix}').form("valid")){CFG_message("请将数据填写完整！", "warning");return ;}else{
         url = $.contextPath + "/qyptsjlx!saveAdd.json";

         $.ajax({
         type: "post",
         url: url,
         data: {
         SJBM: $("#SJBM_${idSuffix}").val(),
         SJMC: $("#SJMC_${idSuffix}").val(),
         SXJB: $("#SXJB_${idSuffix}").val(),
         LXBM: $.ns("namespaceId${idSuffix}").currentTreeNodeId,
         ID:$("#ID_${idSuffix}").val()
         },
         dataType: 'json',
         success: function (res) {
         CFG_message("保存成功！", "success");
         },
         error: function () {
         CFG_message("保存失败！", "error");
         }
         });

         }
         //保存之后关闭
         $("#SJBM_${idSuffix}").textbox("setValue", "");
         $("#SJMC_${idSuffix}").textbox("setValue", "");
         $("#SXJB_${idSuffix}").textbox("setValue", "");
         $("#ID_${idSuffix}").textbox("setValue", "");
         CFG_clickReturnButton($('#max${idSuffix}').data('configInfo'));
         var lxbm = $.ns("namespaceId${idSuffix}").currentTreeNodeId;
         $('#gridId${idSuffix}').grid('option', 'url', "${gurl}?lxbm=" + lxbm);
         $('#gridId${idSuffix}').grid('reload');
         }
         },*/
        getTreeNodeId: function (o) {
            return {
                status: true,
                lxbm: $.ns("namespaceId${idSuffix}").currentTreeNodeId,
                lxmc: $.ns("namespaceId${idSuffix}").currentTreeNodeName,
                treeId: $.ns("namespaceId${idSuffix}").currentTreeId 
            };
        }
    });
/*     var buttonOpts = {
        id: "combogrid_buttonId",
        label: "构件按钮",
        icons: "icon-checkmark-circle",
        text: false,
        onClick: $.ns('namespaceId${idSuffix}').csxxClick
    }; */
    var rowData;
    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'qyptsjzdgl.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#max${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone': function () {
                return $('#layoutId${idSuffix}').layout('panel', 'center');
            },
            <%--/** 初始化预留区 */--%>
            /*'initReserveZones': function (configInfo) {*/
            <%--CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', $('#toolbarId${idSuffix}').toolbar("getLength") - 1);--%>
            /*},*/
            <%--/** 获取返回按钮添加的位置 */--%>
            <%--'setReturnButton': function (configInfo) {--%>
            <%--CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));--%>
            <%--},--%>
            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {
            	var $tree = $("#treeId${idSuffix}");
                var nodes = $tree.tree("getNodes");
                for (var i in nodes) {
                    if (nodes[i].name == "数据字典管理") {
                        $tree.tree("expandNode",nodes[i]);
                        CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "dicym", nodes[i].name, "3", $.ns("namespaceId${idSuffix}"));
                    }
                }
                // 按钮权限控制
                //alert(configInfo.notAuthorityComponentButtons);
/*                if (configInfo.notAuthorityComponentButtons) {
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
                }*/
            }

        });



 	configInfo.namespace = $.ns("namespaceId${idSuffix}");
    });
</script>

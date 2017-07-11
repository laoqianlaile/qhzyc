<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String resourceFolder = path + "/cfg-resource/coral40/common";
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    request.setAttribute("turl", path + "/sczzpzxx!getPlxx.json");
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<div id="max${idSuffix}" class="fill">
    <ces:layout id="layoutId${idSuffix}" name="" style="width:800px;height:600px;" fit="true">
        <ces:layoutRegion region="west" split="true" minWidth="300" maxWidth="400" style="width:150px;padding:10px;overflow:visible;overflow-x:hidden;overflow-y:scroll;overflow-base-color:white">
            <ces:tree id="treeId${idSuffix}"
                      asyncEnable="true" asyncType="post"
                      data="[{ name:\"品种\",id:\"-1\",isParent:true,plym:\"plym\"}]"
                       asyncUrl="${turl}"
                      asyncAutoParam="ID,NAME"
                      onClick="jQuery.ns('namespaceId${idSuffix}').asyncOnclick" onLoad="asyncExpandnode"           
                    >
            </ces:tree>
        </ces:layoutRegion>
        <ces:layoutRegion region="center">
        </ces:layoutRegion>
    </ces:layout>
</div>
<script type="text/javascript">
    //data="[{ name:\"品种\",id:\"-1\",isParent:true,plym:\"plym\"}]"
   // var pzxxjson=$.loadJson($.contextPath + "/sczzpzxx!getPlxx.json");
    <%--$('#treeId${idSuffix}').tree({--%>
    <%--data:[{children:pzxxjson,id:-1,name:'品种',pzjd:'plym'}],--%>
    <%--})--%>
    $.extend($.ns("namespaceId${idSuffix}"), {
        currentTreeNodeId: '',
        currentTreeNodeName: '',
        currentTreeId: 'treeId${idSuffix}',
        addLeftNode : function(treeNode) {
            var $tree = $("#treeId${idSuffix}");
            var selNode = $tree.tree("getNodes");
           // var selNode = $tree.tree("getSelectedNodes");
            $tree.tree("addNodes",selNode[0],treeNode,true);
           //$tree.tree("expandNode",selNode[0]);
            //$tree.tree("reload");

        },
        reloadTree : function (){
            var $tree = $("#treeId${idSuffix}");
            $tree.tree("reload");
        },
	    deleteLeftTreeNode: function (treeNodeIds) {
	   //debugger
		    var $tree = $("#treeId${idSuffix}");
		    var selNode = $tree.tree("getSelectedNodes")[0];
		    if(selNode== null){
		    	selNode = $tree.tree("getNodes")[0];
		    }
		    var childrenNodes = selNode.children;
		    for (var i in treeNodeIds) {
			    for (var j in childrenNodes) {
				    if(childrenNodes[j].id == treeNodeIds[i]){
					    $tree.tree("removeNode",childrenNodes[j]);
				    }
			    }
		    }
	    },
        asyncOnclick: function (e, treeId, treeNode) {
            $.ns("namespaceId${idSuffix}").currentTreeNodeId = treeNode.id;
            $.ns("namespaceId${idSuffix}").currentTreeNodeName = treeNode.name;
            //品类页面
            if (treeNode.plym == "plym") {
                CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "plym", treeNode.name, "3", $.ns("namespaceId${idSuffix}"));
                return;
            }
            //品种节点
            if (treeNode.pzjd == "pzjd") {
                CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "pzjd", treeNode.name, "3", $.ns("namespaceId${idSuffix}"));
                return;
            }
            //品种子节点
            if (treeNode.pzzjd = "pzzjd") {
                CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "pzzjd", treeNode.name, "3", $.ns("namespaceId${idSuffix}"));
                return;
            }
            CFG_clearComponentZone($('#max${idSuffix}').data('configInfo'));
            //alert($.ns("namespaceId${idSuffix}").currentTreeNodeId);
            //自定义url 获得指定的列表数据
            //$('#gridId${idSuffix}').grid('option', 'url', "${gurl}?id=" + treeNode.id);
        },
        getTreeNodeId: function (o) {
            return {
                status: true,
                plbh: $.ns("namespaceId${idSuffix}").currentTreeNodeId,
                pl: $.ns("namespaceId${idSuffix}").currentTreeNodeName,
                treeId: $.ns("namespaceId${idSuffix}").currentTreeId 
            };
        }
    });
    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'zzpzxx.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#max${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone': function () {
                return $('#layoutId${idSuffix}').layout('panel', 'center');
            },
            /** 初始化预留区 */
            <%--'initReserveZones' : function(configInfo) {--%>
            <%--CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'cdmc', $('#toolbarId${idSuffix}').toolbar("getLength")-1);--%>
            <%--},--%>
            /** 获取返回按钮添加的位置 */
            <%--'setReturnButton' : function(configInfo) {--%>
            <%--CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));--%>
            <%--},--%>
            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {
                var $tree = $("#treeId${idSuffix}");
                var nodes = $tree.tree("getNodes");
                for (var i in nodes) {
                    if (nodes[i].name == "品种") {
                        $tree.tree("expandNode",nodes[i]);
                        CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "plym", nodes[i].name, "3", $.ns("namespaceId${idSuffix}"));
                    }
                }
            }
        });
        configInfo.namespace = $.ns("namespaceId${idSuffix}");
    });
</script>
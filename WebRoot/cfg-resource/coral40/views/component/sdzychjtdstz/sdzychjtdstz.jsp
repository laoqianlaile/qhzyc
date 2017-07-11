<%@ page import="com.ces.config.utils.authority.AuthorityUtil" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<% String systemId = request.getParameter("systemId");%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head></head>
<body>
<style>

</style>
<div class="">
    <!--head end-->

    <!--main-->
    <div class="main">

        <!--slide left-->
        <div class="slide picScroll-left" id="slide-left">
            <span class="d-top"></span>
            <span class="d-tright"></span>
            <span class="d-bottom"></span>
            <span class="d-bright"></span>
            <div class="hd">
                <ul></ul>
            </div>
            <div class="bd">
                <ul class="picList">
                    <%if( AuthorityUtil.isHomeMenu("8a8a4afc5403e19a0154045cc8c40000","采收管理","8a8a4afc5403e19a0154054e8b2b002a")){
                    %>
                    <li>
                        <div class="pic">
                            <a href="#" onclick="menuClick('采收管理','/cfg-resource/coral40/views/selfdefine/sdzyccscggl/MT_component.jsp?P_moduleId=838384c2543bc1f401543bf2eed800b6&constructId=838384c2543bc1f401543c0060e70172&bindingType=menu&menuId=8a8a4afc5403e19a0154055007450032&componentVersionId=838384c2543bc1f401543c0060e40171&topComVersionId=838384c2543bc1f401543c0060e40171','8a8a4afc5403e19a0154055007450032')">
                                <img src="./cfg-resource/coral40/common/themes/hjtds/css/images/slide-sm/1.png" />
                            </a>
                        </div>
                        <div class="title"><a href="#">采收管理</a></div>
                    </li>
                    <% } if( AuthorityUtil.isHomeMenu(systemId,"原料采购","8a8a4afc5403e19a0154055d9e380046")){
                    %>
                    <li>
                        <div class="pic">
                            <a href="#" onclick="menuClick('原料采购','/cfg-resource/coral40/views/selfdefine/sdzyccjgylrkxx/MT_component.jsp?P_moduleId=408a96535413cf1a01541413d53600b5&constructId=408a96535413cf1a0154141554b700d6&bindingType=menu&menuId=8a8a4afc5403e19a0154055dd7fe0048&componentVersionId=408a96535413cf1a0154141554b400d5&topComVersionId=408a96535413cf1a0154141554b400d5','8a8a4afc5403e19a0154055dd7fe0048')">
                                <img src="./cfg-resource/coral40/common/themes/hjtds/css/images/slide-sm/2.png" />
                            </a>
                        </div>
                        <div class="title"><a href="#">原料采购</a></div>
                    </li>
                    <%}if( AuthorityUtil.isHomeMenu(systemId,"药材交易","8a8a4afc5403e19a0154055eb73f0052")){%>
                    <li>
                        <div class="pic">
                            <a href="#" onclick="menuClick('药材交易','/cfg-resource/coral40/views/selfdefine/sdzyccjgycjyxx/MT_component.jsp?P_moduleId=408a96535413cf1a01541425e85d0151&constructId=408a96535413cf1a01541426a7f20171&bindingType=menu&menuId=8a8a4afc5403e19a0154055e685a0050&componentVersionId=408a96535413cf1a01541426a7f00170&topComVersionId=408a96535413cf1a01541426a7f00170','8a8a4afc5403e19a0154055e685a0050')">
                                <img src="./cfg-resource/coral40/common/themes/hjtds/css/images/slide-sm/3.png" />
                            </a>
                        </div>
                        <div class="title"><a href="#">药材交易</a></div>
                    </li>
                    <%}if( AuthorityUtil.isHomeMenu(systemId,"药材入库","8a8a4afc5403e19a0154055eb73f0052")){%>
                    <li>
                        <div class="pic">
                            <a href="#" onclick="menuClick('药材入库','/cfg-resource/coral40/views/selfdefine/sdzyccjgycrkxx/MT_component.jsp?P_moduleId=408a96535413cf1a015414226b57012b&constructId=408a96535413cf1a0154142338a60147&bindingType=menu&menuId=8a8a4afc5403e19a0154055e3b01004e&componentVersionId=408a96535413cf1a0154142338a40146&topComVersionId=408a96535413cf1a0154142338a40146','8a8a4afc5403e19a0154055e3b01004e')">
                                <img src="./cfg-resource/coral40/common/themes/hjtds/css/images/slide-sm/4.png" />
                            </a>
                        </div>
                        <div class="title"><a href="#">药材入库</a></div>
                    </li>
                    <%--<li>--%>
                        <%--<div class="pic">--%>
                            <%--<a href="#">--%>
                                <%--<img src="./cfg-resource/coral40/common/themes/hjtds/css/images/slide-sm/5.png" />--%>
                            <%--</a>--%>
                        <%--</div>--%>
                        <%--<div class="title"><a href="#">药材入库</a></div>--%>
                    <%--</li>--%>
                    <%}if( AuthorityUtil.isHomeMenu(systemId,"饮片入库","8a8a4afc5403e19a0154056942ee008c")){%>
                    <li>
                        <div class="pic">
                            <a href="#" onclick="menuClick('饮片入库','/cfg-resource/coral40/views/selfdefine/sdzycyprkxx/MT_component.jsp?P_moduleId=8a8a4a2b54177b53015417aee77400ad&constructId=8a8a4a2b54177b53015417b0488900da&bindingType=menu&menuId=8a8a4afc5403e19a01540568d8110088&componentVersionId=8a8a4a2b54177b53015417b0488500d9&topComVersionId=8a8a4a2b54177b53015417b0488500d9','8a8a4afc5403e19a01540568d8110088')">
                                <img src="./cfg-resource/coral40/common/themes/hjtds/css/images/slide-sm/6.png" />
                            </a>
                        </div>
                        <div class="title"><a href="#">饮片入库</a></div>
                    </li>
                    <% }if( AuthorityUtil.isHomeMenu(systemId,"种植任务","8a8a4afc5403e19a0154054e8b2b002a")){
                    %>
                    <li>
                        <div class="pic">
                            <a href="#" onclick="menuClick('种植任务','/cfg-resource/coral40/views/selfdefine/sczzscda/MT_component.jsp?P_moduleId=402881fc4f3ffb80014f4027b8d5011b&constructId=402881fc4f3ffb80014f403746580142&bindingType=menu&menuId=8a8a4afc5403e19a0154054eb322002c&componentVersionId=402881fc4f3ffb80014f4037464f0141&topComVersionId=402881fc4f3ffb80014f4037464f0141','8a8a4afc5403e19a0154054eb322002c')">
                                <img src="./cfg-resource/coral40/common/themes/hjtds/css/images/slide-sm/7.png" />
                            </a>
                        </div>
                        <div class="title"><a href="#">种植任务</a></div>
                    </li>
                    <%}%>
                </ul>
            </div>

            <a class="prev" href="javascript:void(0)"></a>
            <a class="next" href="javascript:void(0)"></a>
        </div>
        <!--slide left end-->

        <!--slide-->
        <div class="slide slideBox" id="slideBox">
            <div class="hd">
                <ul></ul>
            </div>
            <div class="bd">
                <ul>
                    <li><a href="#"><img src="./cfg-resource/coral40/common/themes/hjtds/css/images/slide-lg/2.jpg" style="max-width: 100%;margin-left:0px"/></a></li>
                    <li><a href="#"><img src="./cfg-resource/coral40/common/themes/hjtds/css/images/slide-lg/3.jpg" style="max-width: 100%;margin-left:0px"/></a></li>
                </ul>
            </div>

            <!-- 下面是前/后按钮代码，如果不需要删除即可 -->
            <a class="prev" href="javascript:void(0)"></a>
            <a class="next" href="javascript:void(0)"></a>
        </div>
        <!--slide end-->
    </div>
    <!--main end-->

    <!--footer-->
    <%--<div class="footer">--%>
        <%--<p>COPYRIGHT 2016 上海中信信息发展股份有限公司 ALL RIGHTS RESERVED</p>--%>
    <%--</div>--%>
    <%--<!--footer end-->--%>

    <%--<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>--%>
    <script type="text/javascript" src="./cfg-resource/coral40/common/themes/hjtds/js/jquery.SuperSlide.2.1.1.js"></script>
    <script type="text/javascript">
        $("#slide-left").slide({titCell:".hd ul",mainCell:".bd ul",autoPage:true,effect:"left",vis:6});
        $("#slideBox").slide({titCell:".hd ul",mainCell:".bd ul",autoPage:true,autoPlay:true,effect:"left",vis:1});

        $(function(){
            $('#slide-left li').click(function(event) {
                $(this).addClass('active').siblings().removeClass('active');
            });
        })
    </script>
</div>
</body>
<script type="text/javascript">
    $.extend($.ns("namespaceId${idSuffix}"), {
        currentTreeNodeId : '',
        asyncOnclick : function(e, treeId, treeNode) {
            CFG_clearComponentZone($('#max${idSuffix}').data('configInfo'));
            CFG_addTabbar($('#max${idSuffix}').data('configInfo'), "TabReserve", $('#tabbarDiv${idSuffix}'), '4', $.ns("namespaceId${idSuffix}"));
            /*if (treeNode.name == "TreeNodeReserve") {
             alert(1);
             CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "TreeNodeReserve", "树节点预留区", "3", $.ns("namespaceId${idSuffix}"));
             return;
             }
             alert($.ns("namespaceId${idSuffix}").currentTreeNodeId);
             $.ns("namespaceId${idSuffix}").currentTreeNodeId = treeNode.id;
             $('#gridId${idSuffix}').grid('option', 'url', "${gurl}&Q_EQ_parentId=" + treeNode.id);
             $('#gridId${idSuffix}').grid('reload');
             */
        },
        /* 构件方法和回调函数 */
        getCategoryId : function() {
            alert("调用方法getCategoryId！");
            var o = {
                status : true
            };
            o.categoryId = '1';
            return o;
        },
        refreshGrid : function(o) {
            if (o) {
                alert("调用回到函数refreshGrid(" + o.cbParam1 + ")");
            }
        }
    });

</script>
</html>
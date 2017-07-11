<%@ page contentType="text/html;charset=UTF-8" %>
<!--main-->
<div class="main">
    <!--slide left-->
    <%--<div class="slide picScroll-left" id="slide-left">
        <div class="hd">
            <ul></ul>
        </div>
        <div class="bd" align="center">
            <ul class="picList" style="font-size: 16px">


                <li>
                    <div class="pic">
                        <a href="#" onclick="menuClick('药材采购','/cfg-resource/coral40/views/selfdefine/sdzycjjgyycrkxx/MT_component.jsp?P_moduleId=8a8a4a2b54177b5301541791a55e0000&constructId=8a8a4a2b54177b5301541795b3cf0035&bindingType=menu&menuId=8a8a4afc5403e19a01540567e6be0082&componentVersionId=8a8a4a2b54177b5301541795b3cc0034&topComVersionId=8a8a4a2b54177b5301541795b3cc0034','8a8a4afc5403e19a01540567e6be0082')">
                            <img src="./cfg-resource/coral40/common/themes/hwyy/css/images/slide-sm/4.png" />
                        </a>
                    </div>
                    <div class="title"><a href="#">药材采购</a></div>
                </li>
                <li>
                    <div class="pic">
                        <a href="#" onclick="menuClick('饮片入库','/cfg-resource/coral40/views/selfdefine/sdzycyprkxx/MT_component.jsp?P_moduleId=8a8a4a2b54177b53015417aee77400ad&constructId=8a8a4a2b54177b53015417b0488900da&bindingType=menu&menuId=8a8a4afc5403e19a01540568d8110088&componentVersionId=8a8a4a2b54177b53015417b0488500d9&topComVersionId=8a8a4a2b54177b53015417b0488500d9','8a8a4afc5403e19a01540568d8110088')">
                            <img src="./cfg-resource/coral40/common/themes/hwyy/css/images/slide-sm/6.png" />
                        </a>
                    </div>
                    <div class="title"><a href="#">饮片入库</a></div>
                </li>
                <li>
                    <div class="pic">
                        <a href="#" onclick="menuClick('饮片交易','/cfg-resource/coral40/views/selfdefine/sdzyccpjyxx/MT_component.jsp?P_moduleId=8a8a4a2b54177b53015417b35b6c00e5&constructId=8a8a4a2b54177b53015417b62bbd0109&bindingType=menu&menuId=8a8a4afc5403e19a01540568f077008a&componentVersionId=8a8a4a2b54177b53015417b62bba0108&topComVersionId=8a8a4a2b54177b53015417b62bba0108','8a8a4afc5403e19a01540568f077008a')">
                            <img src="./cfg-resource/coral40/common/themes/hwyy/css/images/slide-sm/3.png" />
                        </a>
                    </div>
                    <div class="title"><a href="#">饮片交易</a></div>
                </li>
            </ul>
        </div>

        <a class="prev" href="javascript:void(0)"></a>
        <a class="next" href="javascript:void(0)"></a>
    </div>--%>
    <!--slide left end-->

    <!--slide-->
    <div class="slide slideBox" id="slideBox" style="margin-top: 100px;">
        <div class="hd">
            <ul></ul>
        </div>
        <div class="bd">
            <ul>
                <li><a href="#"><img src="cfg-resource\coral40\common\themes\hwyy\css\images/slide-lg/zy3.png" style="max-width: 100%;margin-left: 0"/></a></li>
                <li><a href="#"><img src="cfg-resource\coral40\common\themes\hwyy\css\images/slide-lg/zy3.png" style="max-width: 100%;margin-left: 0" /></a></li>
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
<!--footer end-->

<%--<script type="text/javascript" src="cfg-resource\coral40\common\themes\hwyy\js/jquery-1.8.3.min.js"></script>--%>
<script type="text/javascript" src="cfg-resource\coral40\common\themes\hwyy\js/jquery.SuperSlide.2.1.1.js"></script>
<script type="text/javascript">
    $("#slide-left").slide({titCell:".hd ul",mainCell:".bd ul",autoPage:true,effect:"left",vis:6});
    $("#slideBox").slide({titCell:".hd ul",mainCell:".bd ul",autoPage:true,autoPlay:true,effect:"left",vis:1});
</script>
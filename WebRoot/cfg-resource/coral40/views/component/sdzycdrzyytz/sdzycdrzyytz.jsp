<%@ page contentType="text/html;charset=UTF-8" %>
<!--main-->
<div class="main">


    <!--slide-->
    <div class="slide slideBox" id="slideBox">
        <div class="hd">
            <ul></ul>
        </div>
        <div class="bd">
            <ul>
                <li><a href="#"><img src="cfg-resource\coral40\common\themes\login\css/images/content/1.png" style="max-width: 100%;margin-left: 0"/></a></li>
                <li><a href="#"><img src="cfg-resource\coral40\common\themes\login\css/images/content/2.png" style="max-width: 100%;margin-left: 0" /></a></li>
            </ul>
        </div>
    </div>
    <!--slide end-->

</div>
<!--main end-->

<!--footer-->
<%--<div class="footer">--%>
    <%--<p>COPYRIGHT 2016 上海中信信息发展股份有限公司 ALL RIGHTS RESERVED</p>--%>
<%--</div>--%>
<!--footer end-->

<%--<script type="text/javascript" src="cfg-resource\coral40\common\themes\bwt\js/jquery-1.8.3.min.js"></script>--%>
<script type="text/javascript" src="cfg-resource\coral40\common\themes\bwt\js/jquery.SuperSlide.2.1.1.js"></script>
<script type="text/javascript">
    $("#slide-left").slide({titCell:".hd ul",mainCell:".bd ul",autoPage:true,effect:"left",vis:6});
    $("#slideBox").slide({titCell:".hd ul",mainCell:".bd ul",autoPage:true,autoPlay:true,effect:"left",vis:1});
</script>
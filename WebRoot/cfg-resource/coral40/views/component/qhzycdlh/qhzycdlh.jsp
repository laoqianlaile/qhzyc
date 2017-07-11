<%@ page import="com.ces.config.utils.authority.AuthorityUtil" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<% String systemId = request.getParameter("systemId");%>
<!--main-->
<div class="swiper-container">
    <div class="swiper-wrapper">
        <div class="swiper-slide"><img src="../images/zy1.png" alt=""></div>
        <div class="swiper-slide"><img src="../images/zy1.png" alt=""></div>
        <div class="swiper-slide"><img src="../images/zy1.png" alt=""></div>
    </div>
    <!-- 如果需要分页器 -->
    <div class="swiper-pagination"></div>
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
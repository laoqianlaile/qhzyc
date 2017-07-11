<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="cui" tagdir="/WEB-INF/tags"%>
<%@ page import="com.ces.config.utils.SystemParameterUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!--footer start-->
<div class="footer">
	<div class="footer_c">
        <div class="footer_r">
			<ul class="usermes clearfix">
				<li class="tag_close_all" onclick="closeAllTabs()"><p>关闭所有</p></li>
				<%if(!"0".equals(SystemParameterUtil.getInstance().getSystemParamValue("是否显示版权信息"))){%>
				<li><p>上海中信信息发展股份有限公司</p></li>
				<%} %>
			</ul>
        </div>
    </div>
</div>
<!--footer end-->
<script>
    function closeAllTabs() {
        for (var j = menuTabbarSize - 1; j >= 0; j--) {
            var tabId = $("#tabbarDiv").tabs("getIdByIndex", j);
            $.each($("#mainLayout").data(), function(i, v) {
                if (v == tabId) {
                    $("#mainLayout").data(i, null);
                }
            });
            CFG_emptyJQ($("#" + tabId));
	        $("#tabbarDiv").tabs("remove", j);
        }
        menuTabbarSize = 0;
    }
</script>
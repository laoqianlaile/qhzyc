<%--
  Created by IntelliJ IDEA.
  User: Synge
  Date: 15/11/2
  Time: 下午2:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@include file="/cfg-resource/coral40/common/jsp/_cui_library.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<link href="<%=basePath%>resource/css/hdtyLogin/reset.css" type="text/css" rel="stylesheet"/>
<link href="<%=basePath%>resource/css/hdtyLogin/systems-style.css" type="text/css" rel="stylesheet"/>
<link href="<%=basePath%>resource/css/hdtyLogin/minimal.css" type="text/css" rel="stylesheet"/>
<script src="<%=basePath%>resource/js/hdtyLogin/icheck.min.js"></script>
<div class="wrapper clearfix">
    <div class="system-select">
        <div id="back-arrow"></div>
        <div id="next-arrow"></div>
        <div class="middlebox">
            <div id="itemsbox" class="itemsbox clearfix">
            </div>
        </div>
    </div>
    <div class="footer">
        <span class="English">COPYRIGHT</span> 2015 上海中信信息发展股份有限公司 <span class="English">ALL RIGHTS RESERVED</span>
    </div>
</div>
<script>
    $(function () {
        //样式调整
        $('.skin-minimal input').iCheck({
            checkboxClass: 'icheckbox_minimal',
            increaseArea: '20%'
        });
        function getHeight() {
            var height = $(window).height();
            $(".wrapper").css("height", height);
        }

        getHeight();
        $(window).resize(function () {
            getHeight();
        });
        //数据加载
        var data = $.loadJson($.contextPath + "/trace!getAllSystemByUser.json");
        if (data.length == 1) {
            var url = $.contextPath + "/app.jsp?systemId=" + data[0].sysCode + "&companyCode=" + data[0].companyCode;
            window.location.href = url;
        } else {
            for (var i = 0; i < data.length; i = i + 6) {
                $('#itemsbox').append('<div class="items clearfix" id="items' + i + '"><div>');
                for (var j = i; j < i + 6; j++) {
                    if (data[j] != null) {
                        var url = $.contextPath + "/app.jsp?systemId=" + data[j].sysCode + "&companyCode=" + data[j].companyCode + "&multi=true";
                        var itemdiv = "<div class='item'>";
                        itemdiv += "<div id='" + getClassbySysName(data[j].sysName) + "' onclick='toSystem(\"" + url + "\")'></div>";
                        itemdiv += "<span><a href='#' onclick='toSystem(\"" + url + "\")'>"+data[j].sysShowName+"</a></span>";
                        itemdiv += "</div>";
                        $('#items' + i).append(itemdiv);
                    }
                }
            }
        }
    });
    function toSystem(url) {
        window.open(url, "_self");
    }
    function getClassbySysName(sysName) {
        var idName = "";
        if (sysName == "加工") {
            idName = "food-processing";
        } else if (sysName == "团体") {
            idName = "group";
        } else if (sysName == "批发市场") {
            idName = "vegetable_market";
        } else if (sysName == "水产") {
            idName = "aquatic-product";
        } else if (sysName == "活畜养殖") {
            idName = "meat_market";
        } else if (sysName == "活畜屠宰") {
            idName = "butcher";
        } else if (sysName == "猪肉批发") {
            idName = "meat_market";
        } else if (sysName == "蔬菜种植") {
            idName = "planting-system";
        } else if (sysName == "超市") {
            idName = "store";
        } else if (sysName == "零售市场") {
            idName = "group";
        } else if (sysName == "餐饮") {
            idName = "dinner";
        } else {
            idName = "planting-system";
        }
        return idName;
    }
</script>
<script src="<%=basePath%>resource/js/hdtyLogin/action.js"></script>


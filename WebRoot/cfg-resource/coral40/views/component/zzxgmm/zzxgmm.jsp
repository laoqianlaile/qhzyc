<%@ page import="com.ces.config.utils.CommonUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    request.setAttribute("gurl","");
    request.setAttribute("turl","");
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<style>
    .app-input-label {
        float: left;
    }
    #tab${idSuffix}{
        overflow:hidden;
    }



</style>

<div id="password${idSuffix}" style="padding-top: 150px;overflow:hidden;" >
    <!------------------ 共有字段start --------------------->
    <div id="gyzd" >
        <div class="app-inputdiv4" style="height:32px">
        </div>
        <div class="app-inputdiv4" >
            <label class="app-input-label" >原密码:</label>
            <input id="YMM${idSuffix}" type="password" name="YMM" data-options="required:true" class="coralui-textbox" />
        </div>
        <div class="app-inputdiv4" style="height:32px">
        </div>

        <div class="app-inputdiv4" style="height:32px">
        </div>
        <div class="app-inputdiv4" >
            <label class="app-input-label">新密码:</label>
            <input id="XMM${idSuffix}" type="password" name="XMM" data-options="required:true" class="coralui-textbox"/>
        </div>
        <div class="app-inputdiv4" style="height:32px">
        </div>

        <div class="app-inputdiv4" style="height:32px">
        </div>
        <div class="app-inputdiv4" >
            <label class="app-input-label">确认密码:</label>
            <input id="QRMM${idSuffix}" type="password" name="QRMM" data-options="required:true" class="coralui-textbox"/>
        </div>
        <div class="app-inputdiv4" style="height:32px">
        </div>

        <div class="app-inputdiv4" style="height:32px">
        </div>
        <div class="app-inputdiv4" >
            <label class="app-input-label"></label>
            <input id="button1" class="button" type="button" value="保存" onclick="confirm()">

        </div>
        <div class="app-inputdiv4" style="height:32px">
        </div>

    </div>
</div>
<script type="text/javascript">
    //$('.button').buttons;
    $("#button1").button();

    function confirm(){
        //var val=document.getElementById("YMM${idSuffix}").value;
        var ymm=$("#YMM${idSuffix}").textbox('getValue');
        var xmm=$("#XMM${idSuffix}").textbox('getValue');
        var qrmm=$("#QRMM${idSuffix}").textbox('getValue');
        if(xmm!=qrmm){CFG_message("两次密码输入不一样！", "error");return;}
        if(ymm==xmm){CFG_message("新密码不能和原密码一样！", "error");return;}
        //var jsonData = $.loadJson($.contextPath + "/zzxgmm!changePassword.json?DKBH="+dkbh);
        $.ajax({
            type: 'POST',
            url:$.contextPath+"/trace!changePassword.json",
            data: {
                YMM:ymm,
                XMM:xmm,
                QRMM:qrmm,
            },
            dataType: "json",
            //contentType: false,//必须false才会自动加上正确的Content-Type
            //processData: false,//必须false才会避开jQuery对 formdata 的默认处理XMLHttpRequest会对 formdata 进行正确的处理
            //timestamp: false,

            success: function (data) {
                if(data==0){CFG_message("操作成功！", "success");}else{CFG_message("密码错误！", "error");}

            },
            error: function () {
                CFG_message("操作失败！", "error");
            }
        })
        //alert(ymm+"   "+xmm+"    "+qrmm);
    }

    function goBack(){
        document.getElementById();
    }
</script>






<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String resourceFolder = path + "/cfg-resource/coral40/common";
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
    request.setAttribute("menuId", request.getParameter("menuId"));
    request.setAttribute("basePath", basePath);
    request.setAttribute("path", path);
%>
<style type="text/css">
    /*<![CDATA[*/
    .thumb {
        height: 75px; /*给缩略图指定单个维度，浏览器自动等比缩放*/
        border: 1px solid #2AC79F;
        /*margin: 10px 5px 0 0;*/
        position: absolute;
    }

    /*li {color: red;}*/
    /*]]>*/
</style>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>企业档案</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!--
        <link rel="stylesheet" type="text/css" href="styles.css">
        -->
    <style type="text/css">
        .colspan3 .app-inputdiv4 .inputfile {
            width: 62.5%;
        }

        .top0form {
            padding-top: 0px;
        }

        .coral-component-overlay {
            background: white;
            position: absolute
        }

    </style>
</head>

<div id="max${idSuffix}"  style="background: #fff;">

    <div id="ft${idSuffix}" class="toolbarsnav clearfix top0" style="margin-top: 20px;margin-right: 10px">
        <ces:toolbar id="toolbarId${idSuffix}"
                     data="['->',{\"type\": \"button\",\"id\": \"saveBtm\",'cls':'save_tb',\"label\": \"保存\",\"name\":\"保存\",\"onClick\":\"submitForm()\"}]">
        </ces:toolbar>
        <div style="margin-top: -22px;margin-left:10px;"><div class='homeSpan' style="height:23px"><div style='margin-left:25px;width: 150px;font-size:16px' id="nva${idSuffix}"> -企业信息 </div></div></div>
    </div>
    <form id="enterForm${idSuffix}" action="${basePath}zxtqyda!saveQyda" style="margin-top: -10px"
          enctype="multipart/form-data" method="post" class="coralui-form">

        <div class="fillwidth colspan3 clearfix">
            <!------------------ 共有字段start --------------------->
            <div class="app-inputdiv6" style="height:32px;display:none">
                <input id="ID${idSuffix}" class="coralui-textbox" name="ID"/>
            </div>
            <div class="app-inputdiv4" style="display: none">
                <label class="app-input-label" style="width:36%;">服务地址:</label>
                <div class="app-inputdiv6" style="height:32px;display:none">
                    <input id="fwdz${idSuffix}" class="coralui-textbox" name="fwdz"/>
                </div>
            </div>
            <div class="app-inputdiv4" style="display: none">
                <label class="app-input-label" style="width:36%;">服务密钥:</label>
                <div class="app-inputdiv6" style="height:32px;display:none">
                    <input id="fwmy${idSuffix}" class="coralui-textbox" name="fwmy"/>
                </div>
            </div>
            <div id="gyzd" style="display: none;">
                <div class="app-inputdiv4">
                    <label class="app-input-label" style="width:36%;">企业名称：</label>
                    <input id="qymc${idSuffix}" name="qymc" data-options="required:true" maxlength="20"  class="coralui-textbox"
                           disabled="true"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label" style="width:36%;">统一社会信用代码：</label>
                    <input id="gszcdjzh${idSuffix}" name="gszcdjzh" data-options="required:true"maxlength="18"
                           class="coralui-textbox"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label" style="width:36%;">联系电话：</label>
                    <input id="lxdh${idSuffix}" name="lxdh" maxlength="13"   data-options="required:true, pattern:'//^((0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?)|((13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8})$//',errMsg:'请输入手机号码或者固话'" class="coralui-textbox"/>
                </div>
                <div class="app-inputdiv4" id="lsxzqDiv${idSuffix}">
                    <label class="app-input-label" style="width:36%;">隶属行政区：</label>
                    <input id="lsxzq${idSuffix}" name="lsxzq" maxlength="20" data-options="required:true" class="coralui-textbox"/>
                </div>
            </div>
            <!-------------------- 共有字段end ---------------------->
            <!-------------------- 种植共有字段start ---------------------->
            <div id="zzqyjbxx" style="display: none;">
                <div class="app-inputdiv4">
                    <label class="app-input-label" style="width:36%;">企业名称：</label>
                    <input id="qymc${idSuffix}" name="qymc" maxlength="20"  class="coralui-textbox"
                           data-options="required:true" />
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label" style="width:36%;">单位类型：</label>
                    <input id="dwlx${idSuffix}" name="dwlx" data-options="required:true" maxlength="20" disabled="true"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label" style="width:36%;">统一社会信用代码：</label>
                    <input id="gszcdjzh${idSuffix}" name="gszcdjzh" data-options="required:true"maxlength="18"
                           class="coralui-textbox"/>
                </div>
                <div class="app-inputdiv12" id="zzjddzDiv${idSuffix}">
                    <label class="app-input-label" style="width:12%;">注册地址：</label>
                    <input id="zcdz${idSuffix}" class="coralui-textbox" data-options="required:true" name="zcdz"  maxlength="50" />
                </div>
                <div class="app-inputdiv12" id="zzjddzDiv${idSuffix}">
                    <label class="app-input-label" style="width:12%;">经营地址：</label>
                    <input id="jydz${idSuffix}" class="coralui-textbox" maxlength="50" name="jydz"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label" style="width:36%;">法人代表：</label>
                    <input id="fddb${idSuffix}" name="fddb"maxlength="10"  class="coralui-textbox"/>
                </div>
                <div class="app-inputdiv4" id="frdbDiv${idSuffix}">
                    <label class="app-input-label" style="width:36%;">企业联系人：</label>
                    <input id="linkman${idSuffix}" class="coralui-textbox" data-options="required:true" maxlength="10"  name="linkman"/>
                </div>

                <div class="app-inputdiv4">
                    <label class="app-input-label" style="width:36%;">邮编：</label>
                    <input id="yb${idSuffix}" name="yb" maxlength="6" data-options="pattern:'//^([1-9]\\d{5}(?!\\d))$//'" class="coralui-textbox"/>
                </div>
                <div class="app-inputdiv4" id="barqDiv${idSuffix}">
                    <label class="app-input-label" style="width:36%;">成立日期：</label>
                    <input type="datepicker"  id="clrq${idSuffix}" class="coralui-datepicker" name="clrq" data-options="maxDate:'%=new Date()%' " />
                </div>
                <div class="app-inputdiv12" id="barqDiv${idSuffix}">
                    <label class="app-input-label" style="width:12%;">认证情况：</label>
                    <input id="rzqk${idSuffix}" name="rzqk"
                           data-options="column:7,data:[{'value':'5','text':'GAP'},{'value':'6','text':'GMP'},{'value':'7','text':'GSP'}]"
                           class="coralui-checkboxlist"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label" style="width:36%;">注册资金：</label>
                    <input id="zczj${idSuffix}" name="zczj" maxlength="18" class="coralui-textbox"  data-options="pattern:'//^([1-9]+(\.[0-9]+[1-9])?)|([0]+(\.([0-9]+)?[1-9]))$//',required:true,buttons:[{'innerRight':[{label:'万'}]}]"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label" style="width:36%;">网址：</label>
                    <input id="wz${idSuffix}" name="wz" maxlength="50" class="coralui-textbox"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label" style="width:36%;">联系电话：</label>
                    <input id="lxdh${idSuffix}" name="lxdh" maxlength="13" class="coralui-textbox" data-options="required:true, pattern:'//^((0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?)|((13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8})$//',errMsg:'请输入手机号码或者固话'"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label" style="width:36%;">传真：</label>
                    <input id="cz${idSuffix}" name="cz" maxlength="12" class="coralui-textbox" data-options="pattern:'//^[+]{0,1}(\\d){1,3}[ ]?([-]?((\\d)|[ ]){1,12})+$//',errMsg:'请输入正确的传真号'"/>
                </div>
                <div class="app-inputdiv12" id="cdmsDiv${idSuffix}">
                    <label class="app-input-label" style="width:12%;">企业简介：</label>
                    <textarea id="cdms${idSuffix}" class="coralui-textbox" name="cdms" data-options="maxlength:1000" />
                </div>
                <div class="app-inputdiv12">
                    <label class="app-input-label" style="width:12%;float:left;margin-top: 37px;">企业Logo图片：</label>

                    <div id="logoFile${idSuffix}">
                        <input class="inputfile" type="file" style="width:160px;display:none"
                               id="logoImageUpload${idSuffix}" multiple="multiple" lable="预览"
                               accept="image/*" name="logoImageUpload" onchange="logoViewImage(this)"/>
                    </div>
                    <div style="margin-left: 0px;float:left;" id="logoPreview${idSuffix}"></div>
                    <div style="margin-left: 0px;float:left;" id="logoView${idSuffix}"></div>
                    <button style="margin-left: 10px;margin-top: 59px;"
                            class='ctrl-toolbar-element ctrl-init ctrl-init-button coral-button coral-component coral-state-default coral-corner-all coral-button-text-only coral-toolbar-item-component'
                            type='button' onclick="$('#logoImageUpload${idSuffix}').click()">
                        <span class="coral-button-text ">选择图片</span>
                    </button>
                    <label style="width:120px;" id="logoTip${idSuffix}"/>
                </div>
                <div class="app-inputdiv12">
                    <label class="app-input-label" style="width:12%;float:left;"></label>
                </div>
                <div class="app-inputdiv12">
                    <label class="app-input-label" style="width:12%;float:left;"></label>


                </div>
            </div>
            <!-------------------- 种植共有字段end ---------------------->

            <!--------------------- 门店名称start ------------------->
            <div class="app-inputdiv4" id="pfscmcDiv${idSuffix}" style="display:none">
                <label class="app-input-label"  style="width:36%;">批发市场名称：</label>
                <input id="pfscmc${idSuffix}" maxlength="20" name="pfscmc" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv4" id="tzcmcDiv${idSuffix}" style="display:none">
                <label class="app-input-label"  style="width:36%;">屠宰场名称：</label>
                <input id="tzcmc${idSuffix}" maxlength="20" name="tzcmc" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv4" id="lsscmcDiv${idSuffix}" style="display:none">
                <label class="app-input-label" maxlenth="50" style="width:36%;">零售市场名称：</label>
                <input id="lsscmc${idSuffix}" name="lsscmc" maxlength="20"   class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv4" id="csmcDiv${idSuffix}" style="display:none">
                <label class="app-input-label"  style="width:36%;">超市名称：</label>
                <input id="csmc${idSuffix}" maxlength="20" name="csmc" data-options="required:true" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv4" id="jgcmcDiv${idSuffix}" style="display:none">
                <label class="app-input-label" style="width:36%;">加工厂名称：</label>
                <input id="jgcmc${idSuffix}" maxlength="20" name="jgcmc" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv4" id="zzjdmcDiv${idSuffix}" style="display:none">
                <label class="app-input-label"  style="width:36%;">种植基地名称：</label>
                <input id="zzjdmc${idSuffix}" maxlength="20" name="zzjdmc" class="coralui-textbox"/>
            </div>
            <div class="app-inputdiv4" id="yzcmcDiv${idSuffix}" style="display:none">
                <label class="app-input-label"  style="width:36%;">养殖场名称：</label>
                <input id="yzcmc${idSuffix}" maxlength="20" name="yzcmc" class="coralui-textbox"/>
            </div>
            <!--------------------------- 门店名称end ------------------->

            <!----------------- 批肉,批菜,屠宰,零售,超市,团体,餐饮 start --------------->
            <div id="pc-and-so-onDiv${idSuffix}" style="display:none">
                <div class="app-inputdiv4" id="barqDiv${idSuffix}">
                    <label class="app-input-label" style="width:36%;">备案日期：</label>
                    <input type="datepicker" id="barq${idSuffix}" data-options="required:true"
                           class="coralui-datepicker" name="barq"/>
                </div>
                <div class="app-inputdiv4" id="frdbDiv${idSuffix}">
                    <label class="app-input-label" style="width:36%;">法人代表：</label>
                    <input id="frdb${idSuffix}" class="coralui-textbox" data-options="required:true" maxlength="10"  name="frdb"/>
                </div>
                <div class="app-inputdiv4" id="frdbDiv${idSuffix}">
                    <label class="app-input-label" style="width:36%;">企业联系人：</label>
                    <input id="linkman${idSuffix}" class="coralui-textbox" data-options="required:true" maxlength="10"  name="frdb"/>
                </div>
                <div class="app-inputdiv4" id="jydzDiv${idSuffix}">
                    <label class="app-input-label" style="width:36%;">经营地址：</label>
                    <input id="jydz${idSuffix}" class="coralui-textbox" data-options="required:true,maxlength:50" name="jydz"  />
                </div>
                <div class="app-inputdiv4" id="czDiv${idSuffix}">
                    <label class="app-input-label" style="width:36%;">传真：</label>
                    <input id="cz${idSuffix}" maxlength="20" class="coralui-textbox" name="cz"/>
                </div>
            </div>
            <!--------------------- 批肉,批菜,屠宰,零售,超市,团体 end ------------------>

            <!-------------------------------产地 start------------------------------->
            <div id="cdDiv${idSuffix}" style="display:none">
                <div class="app-inputdiv4" id="cdbmDiv${idSuffix}">
                    <label class="app-input-label"  style="width:36%;">产地名称：</label>
                    <input id="cdbm${idSuffix}" maxlength="20" name="cdbm"/>

                    <div class="app-inputdiv4" id="cdmcDiv${idSuffix}">
                        <label class="app-input-label"  style="width:36%;">产地编码：</label>
                        <input id="cdmc${idSuffix}" maxlength="20" class="coralui-textbox" name="cdmc"/>
                    </div>
                </div>
                <!--------------------------------产地 end-------------------------------->

                <!--------------------------- 加工 start ----------------------------->
                <div id="jgDiv${idSuffix}" style="display:none">
                    <div class="app-inputdiv4" id="spscxkzhDiv${idSuffix}">
                        <label class="app-input-label" style="width:36%;">生产许可证：</label>
                        <input id="spscxkzh${idSuffix}" maxlength="20" class="coralui-textbox" name="spscxkzh"/>
                    </div>
                    <div class="app-inputdiv4" id="spltxkzhDiv${idSuffix}">
                        <label class="app-input-label" style="width:36%;">流通许可证：</label>
                        <input id="spltxkzh${idSuffix}" maxlength="20" class="coralui-textbox" name="spltxkzh"/>
                    </div>
                    <div class="app-inputdiv4" id="jgcdzDiv${idSuffix}">
                        <label class="app-input-label"  style="width:36%;">加工厂地址：</label>
                        <input id="jgcdz${idSuffix}" maxlength="20" class="coralui-textbox" name="jgcdz"/>
                    </div>
                </div>
                <!---------------------------- 加工 end ------------------------------>

                <!---------------------------- 种植 start ------------------------------>
                <div id="zzDiv${idSuffix}" style="display:none">
                    <div class="app-inputdiv4" id="cdzshDiv${idSuffix}">
                        <label class="app-input-label"  style="width:36%;">产地证书号：</label>
                        <input id="cdzsh${idSuffix}" maxlength="20" class="coralui-textbox" name="cdzsh"/>
                    </div>
                    <div class="app-inputdiv4" id="zzjdmjDiv${idSuffix}">
                        <label class="app-input-label"  style="width:36%;">种植基地面积：</label>
                        <input id="zzjdmj${idSuffix}" maxlength="20" class="coralui-textbox" name="zzjdmj"/>
                    </div>
                    <div class="app-inputdiv4" id="zzjddzDiv${idSuffix}">
                        <label class="app-input-label"  style="width:36%;">种植基地地址：</label>
                        <input id="zzjddz${idSuffix}" maxlength="20" class="coralui-textbox" name="zzjddz"/>
                    </div>
                    <div class="app-inputdiv12" id="cdmsDiv${idSuffix}">
                        <label class="app-input-label"  style="width:12%;">产地描述：</label>
                        <textarea id="cdms${idSuffix}" maxlength="200" class="coralui-textbox" name="cdms"/>
                    </div>
                </div>
                <!---------------------------- 种植 end ------------------------------>

                <!--------------------------- 养殖 begin ----------------------------->
                <div id="yzDiv${idSuffix}" style="display:none">
                    <div class="app-inputdiv4" id="yzcdzshDiv${idSuffix}">
                        <label class="app-input-label"  style="width:36%;">产地证书号：</label>
                        <input id="yzcdzsh${idSuffix}" maxlength="20" class="coralui-textbox" name="cdzsh"/>
                    </div>
                    <div class="app-inputdiv4" id="dwfytjhgzhDiv${idSuffix}">
                        <label class="app-input-label"  style="width:36%;">防疫合格证：</label>
                        <input id="dwfytjhgzh${idSuffix}" maxlength="20" class="coralui-textbox" name="dwfytjhgzh"/>
                    </div>
                    <div class="app-inputdiv4" id="yzcdzDiv${idSuffix}">
                        <label class="app-input-label" style="width:36%;">养殖场地址：</label>
                        <input id="yzcdz${idSuffix}" maxlength="20"  class="coralui-textbox" name="yzcdz"/>
                    </div>
                    <div class="app-inputdiv12" id="yzcdmsDiv${idSuffix}">
                        <label class="app-input-label" style="width:12%;">产地描述：</label>
                        <textarea id="yzcdms${idSuffix}"  maxlength="200" class="coralui-textbox" name="cdms"></textarea>
                    </div>
                </div>
                <!---------------------------- 养殖 end ------------------------------>
            </div>
            <%----------------------------图片begin-------------------------------%>
            <div style="margin-bottom: 50px;">
                <div class="fillwidth colspan3 clearfix">
                    <div class="app-inputdiv12" id="qytpDiv${idSuffix}">
                        <label class="app-input-label" style="width:12%;float:left;margin-top: 30px;">企业图片：</label>
                        <div style="float:left;text-align:left" id="view${idSuffix}"></div>
                        <div style="margin-left: 0px;float: left;" id="preview${idSuffix}"></div>
                        <button id="chooseImage${idSuffix}" style="margin-left: 0px;margin-top: 50px;"
                                class='ctrl-toolbar-element ctrl-init ctrl-init-button coral-button coral-component coral-state-default coral-corner-all coral-button-text-only coral-toolbar-item-component'
                                type='button' onclick="$('#imageUpload${idSuffix}').click()">
                            <span class="coral-button-text ">选择图片</span>
                        </button>
                        <div id="file${idSuffix}">
                            <input class="inputfile" type="file" style="width:160px;display:none"
                                   id="imageUpload${idSuffix}" multiple="multiple" lable="预览"
                                   accept="image/*" name="imageUpload" onchange="viewImage(this)"/>
                        </div>
                        <label style="width:120px;" id="tip${idSuffix}"/>
                    </div>
                    <div class="app-inputdiv12">
                        <label class="app-input-label" style="width:12%;float:left;"></label>


                    </div>
                    <%--<div class="app-inputdiv12">--%>
                    <%--<label class="app-input-label" style="width:12%;float:left;"></label>--%>


                    <%--</div>--%>
                </div>
                <%-----------------------------图片 end--------------------------------%>

                <%--<div class="fillwidth colspan3 clearfix">--%>
                <%--<div class="app-inputdiv4">--%>
                <%--<label class="app-input-label">企业图片:</label> <img--%>
                <%--id="imageView${idSuffix}" width="200px"/> <label--%>
                <%--style="width:120px;" id="tip${idSuffix}"/>--%>
                <%--</div>--%>
                <%--<div class="app-inputdiv4">--%>
                <%--<span class="coral-textbox-default" data-options=""></span>--%>
                <%--</div>--%>
                <%--<div class="app-inputdiv4">--%>
                <%--<span class="coral-textbox-default" data-options=""></span>--%>
                <%--</div>--%>
                <%--<div class="app-inputdiv4">--%>
                <%--<label class="app-input-label">上传图片:</label> <input--%>
                <%--class="inputfile" type="file" style="width:160px;"--%>
                <%--id="imageUpload${idSuffix}" multiple="multiple" lable="预览"--%>
                <%--accept="image/*" name="imageUpload"/>--%>
                <%--</div>--%>
                <%--<div class="app-inputdiv4">--%>
                <%--<span class="coral-textbox-default" data-options=""></span>--%>
                <%--</div>--%>
                <%--<div class="app-inputdiv4">--%>
                <%--<span class="coral-textbox-default" data-options=""></span>--%>
                <%--</div>--%>
                <%--<div class="app-inputdiv4">--%>
                <%--<img id="imagePreView${idSuffix}" style="width:200px"/>--%>

                <%--</div>--%>
                <%--<div id="preview${idSuffix}" ></div>--%>
                <%--</div>--%>
                <%----------------------------图片begin-------------------------------%>
                <!--
			<input type = "button" value="ajax保存" onClick="save()" />
			<iframe name='hidden_frame' id="hidden_frame${idSuffix}" style='display:none'></iframe>
	 	-->

    </form>
    <div id="dialog${idSuffix}" style="display:none"></div>
</div>
<script type="text/javascript">
    var xtlx ;
    //document.getElementsByClassName('coral-component-overlay').style.position='absolute';
    $.extend($.ns("namespaceId${idSuffix}"), {
        cdmcClick: function () {
            CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "cdmc", "产地名称", 2, $.ns("namespaceId${idSuffix}"));
        },
        setComboGridValue_Cdmc: function (o) {
            if (null == o) return;
            var rowData = o.rowData;
            if (null == rowData) return;
            var zzcdbm = $("#zzcdbm${idSuffix}");
            var yzcdbm = $("#yzcdbm${idSuffix}");

            $("#cdmc${idSuffix}").textbox("setValue", rowData.CDMC);
            $("#cdbm${idSuffix}").combogrid("setValue", rowData.CDBM);
//            $("input[name='cdmc']").combogrid("setText",rowData.CDMC);
        }
    });
    var _sysName = "";
    var buttonOpts = {
        id: "combogrid_buttonId",
        label: "构件按钮",
        icons: "icon-checkmark-circle",
        text: false,
        onClick: $.ns('namespaceId${idSuffix}').cdmcClick
    };
    var _colNames = ["产地编号", "产地名称"];
    var _colModel = [{name: "CDBM", width: 65, sortable: true}, {name: "DDD", width: 155, sortable: true}];
    $(document).ready(init());

    function init() {
        var dqxtlxJsonData = $.loadJson($.contextPath +"/menu/menu!show.json?id=<%=request.getParameter("menuId")%>");
        xtlx = dqxtlxJsonData.code.toUpperCase();
        var menuId = "${menuId}";
        $.ajax({
            type: "post",
//            async:false,
            data: {
                menuId: menuId,
                xtlx : xtlx
            },
            dataType: "json",
            url: $.contextPath + "/zxtqyda!getQydaByMenuId.json",
            success: function (data) {
                setForm(data);
            }
        })
    }

    function deleteImage(src, type) {
        var strArray = src.split("/");
        var tplj = strArray[strArray.length - 1];
        $.ajax({
            type: "POST",
            url: $.contextPath + '/zxtqyda!deleteImage.json',
            data: {'tplj': tplj, 'status': status, type: type},
            dataType: "json",
            success: function (data) {
                if (1 == data) {
                    $(".newImages").remove();
                    $(".oldImages").remove();
                    init();
                }
            }
        })
    }

    /******根据子系统名称设定页面显示字段及数据*******/
    function setForm(data) {
        _sysName = data.sysName;
        $("#ID${idSuffix}").textbox("setValue",
                (data.ID == "null" || data.ID == null) ? "" : data.ID);
        $("#qymc${idSuffix}").textbox("setValue",
                (data.QYMC == "null" || data.QYMC == null) ? "" : data.QYMC);
        $("#gszcdjzh${idSuffix}").textbox("setValue",
                (data.GSZCDJZH == "null" || data.GSZCDJZH == null) ? "" : data.GSZCDJZH);
        $("#lxdh${idSuffix}").textbox("setValue",
                (data.LXDH == "null" || data.LXDH == null) ? "" : data.LXDH);
        $("#lsxzq${idSuffix}").textbox("setValue",
                (data.LSXZQ == "null" || data.LSXZQ == null) ? "" : data.LSXZQ);
        $("#lsxzqhdm${idSuffix}").textbox("setValue",
                (data.LSXZQHDM == "null" || data.LSXZQHDM == null) ? "" : data.LSXZQHDM);
        $("#qyjj${idSuffix}").textbox("setValue",
                (data.QYJJ == "null" || data.QYJJ == null) ? "" : data.QYJJ);
        var view = $("#qytpDiv${idSuffix}");
        $(".newImages").remove();
        $(".oldImages").remove();
        var logoView = $("#logoView${idSuffix}");
        var logoPreview = $("#logoPreview${idSuffix}");
        logoView.empty();
        logoPreview.empty();
        if (data.logo != null && data.logo.length != 0) {
            var logoView = $("#logoView${idSuffix}");
            var logo = data.logo[0];
            var tplj = logo.TPLJ;
            var image = new Image();
            image.src = "${basePath}spzstpfj/" + tplj;
            image.className = "thumb";
            image.onload = function (e) {
                var div = document.createElement('div');
                $(div).append(this);
                $(div).append('<span style="display: none;position: absolute; margin-top: 6px; margin-left: 6px; width: 20px; height: 20px;"><img onclick = "deleteImage(\'' + this.src + '\',\'logo\')" style = "height:20px" src = "<%=resourceFolder%>/css/images/trace-image/trash_bin.png"></span>');
//                var originWidth = e.path[0].width;
//                var originHeight = e.path[0].height;
                div.style.float = "left";
                div.style.marginTop = "10px";
                div.style.marginRight = "5px";
                div.style.height = "75px";
                div.style.width = (this.width * 75) / this.height + "px";
                div.style.float = "left";
                this.style.float = "left";
                this.style.marginTop = "10px";
                this.style.marginRight = "5px";
                this.style.height = "75px";
                this.style.width = (this.width * 75) / this.height + "px";
                this.style.float = "left";
                $(div).hover(function () {
                    $(div).find("span").toggle();
                });
                logoView.html("");
                logoView.append(div);
                $(this).click(function(e, data){
                    var jq=$('#dialog${idSuffix}');
                    jq.empty();
                    jq.append("<img src='" + $(this).attr("src") + "'style='position:absolute;z-index:999999'>");
                    var image = new Image();
                    image.src = $(this).attr("src");
                    jq.dialog({
                        width:(parseInt(image.width)),
                        height:(parseInt(image.height)),
                        modal:true
                    });
                });
            };
            image.onerror = function (e) {
                $("#tip${idSuffix}").show();
            };
            $("#logoTip${idSuffix}").hide();
        } else {
            $("#imageView${idSuffix}").attr("width", "1px");
            $("#logoTip${idSuffix}").show();
        }
        if (data.tp != null && data.tp.length != 0) {
            var tp = data.tp;
            //加载图片
            for (var i = 0; i < tp.length; i++) {
                var tplj = tp[i].TPLJ;

                var image = new Image();
                image.src = "${basePath}spzstpfj/" + tplj;
                image.className = "thumb";
                image.onload = function (e) {
                    var _this = this;
                    var div = document.createElement('div');
                    $(div).append(_this);
                    $(div).append('<span style="display: none;position: absolute; margin-top: 6px; margin-left: 6px; width: 20px; height: 20px;"><img onclick = "deleteImage(\'' + this.src + '\',\'qytp\')" style = "height:20px" src = "<%=resourceFolder%>/css/images/trace-image/trash_bin.png"></span>');
                    var originWidth = _this.width;
                    var originHeight = _this.height;
                    div.style.float = "left";
                    if($(".oldImages").length == 4){
                        $(div).css("clear","both");
                        div.style.marginLeft = "12%";
                    }
//                    div.style.marginTop = "10px";
                    div.style.marginRight = "5px";
                    div.style.marginBottom = "5px";
                    div.style.height = "75px";
                    div.style.width = (originWidth * 75) / originHeight + "px";
                    this.style.marginRight = "5px";
                    this.style.marginBottom = "5px";
                    this.style.height = "75px";
                    this.style.width = (originWidth * 75) / originHeight + "px";
//                    div.style.width = "75px";
                    $(div).attr("class","oldImages");
                    $(div).hover(function () {
                        $(div).find("span").toggle();
                    });
                    view.append(div);
                    var choose = $("#chooseImage${idSuffix}");
                    choose.remove();
                    view.append("<div style=\"width:99px;float:left;\"><button id=\"chooseImage${idSuffix}\" style=\"margin-left: 0px;margin-top: 50px;\"class=\"ctrl-toolbar-element ctrl-init ctrl-init-button coral-button coral-component coral-state-default coral-corner-all coral-button-text-only coral-toolbar-item-component\" type=\"button\" onclick=\"$('#imageUpload${idSuffix}').click()\" type=\"button\" onclick=\"$('#imageUpload${idSuffix}').click()\" > <span class=\"coral-button-text\">选择图片</span> </button><div>");
                    $(this).click(function(e, data){
                        var jq=$('#dialog${idSuffix}');
                        jq.empty();
                        jq.append("<img src='" + $(this).attr("src") + "'style='position:absolute;z-index:999999'>");
                        var image = new Image();
                        image.src = $(this).attr("src");
                        jq.dialog({
                            width:(parseInt(image.width)),
                            height:(parseInt(image.height)),
                            modal:true
                        });
                    });
                };
                image.onerror = function (e) {
//                    deleteImage(this.src); TODO:remove the commenter while deploying
                    <%--$("#tip${idSuffix}").show();--%>
                }
            }
            $("#tip${idSuffix}").hide();
        } else {
            $("#imageView${idSuffix}").attr("width", "1px");
            $("#tip${idSuffix}").show();
        }
        if ("PC" == data.sysName || "PR" == data.sysName) {
            $("#gyzd").show();
            //显示标签
            $("#pfscmcDiv${idSuffix}").show();
            $("#pc-and-so-onDiv${idSuffix}").show();
            //移除标签
            $("#zzqyjbxx").remove();
            $("#tzcmcDiv${idSuffix}").remove();
            $("#lsscmcDiv${idSuffix}").remove();
            $("#csmcDiv${idSuffix}").remove();
            $("#jgcmcDiv${idSuffix}").remove();
            $("#yzcmcDiv${idSuffix}").remove();
            $("#jgDiv${idSuffix}").remove();//加工
            $("#zzDiv${idSuffix}").remove();//种植
            $("#yzDiv${idSuffix}").remove();//养殖
            $("#zzjdmcDiv${idSuffix}").remove();
            $("#cdDiv${idSuffix}").remove();//产地
            //表单赋值
            $("#pfscmc${idSuffix}").textbox("setValue",
                    (data.PFSCMC == "null" || data.PFSCMC == null) ? "" : data.PFSCMC);
            $("#lsxzqhdm${idSuffix}").textbox("setValue",
                    (data.LSXZQHDM == "null" || data.LSXZQHDM == null) ? "" : data.LSXZQHDM);
            $("#barq${idSuffix}").datepicker("option", "value",
                    (data.BARQ == "null" || data.BARQ == null) ? "" : data.BARQ);
            $("#frdb${idSuffix}").textbox("setValue",
                    (data.FRDB == "null" || data.FRDB == null) ? "" : data.FRDB);
            $("#jydz${idSuffix}").textbox("setValue",
                    (data.JYDZ == "null" || data.JYDZ == null) ? "" : data.JYDZ);
            $("#cz${idSuffix}").textbox("setValue",
                    (data.CZ == "null" || data.CZ == null) ? "" : data.CZ);

        } else if ("CY" == data.sysName) {
            $("#gyzd").show();
            //显示标签
            $("#zzqyjbxx").remove();
            $("#pc-and-so-onDiv${idSuffix}").show();
            $("#pfscmcDiv${idSuffix}").remove();
            $("#tzcmcDiv${idSuffix}").remove();
            $("#lsscmcDiv${idSuffix}").remove();
            $("#csmcDiv${idSuffix}").remove();
            $("#jgcmcDiv${idSuffix}").remove();
            $("#yzcmcDiv${idSuffix}").remove();
            $("#jgDiv${idSuffix}").remove();//加工
            $("#zzDiv${idSuffix}").remove();//种植
            $("#yzDiv${idSuffix}").remove();//养殖
            $("#zzjdmcDiv${idSuffix}").remove();
            $("#cdDiv${idSuffix}").remove();//产地
            //表单赋值
            $("#lsxzqhdm${idSuffix}").textbox("setValue",
                    (data.LSXZQHDM == "null" || data.LSXZQHDM == null) ? "" : data.LSXZQHDM);
            $("#barq${idSuffix}").datepicker("option", "value",
                    (data.BARQ == "null" || data.BARQ == null) ? "" : data.BARQ);
            $("#frdb${idSuffix}").textbox("setValue",
                    (data.FRDB == "null" || data.FRDB == null) ? "" : data.FRDB);
            $("#jydz${idSuffix}").textbox("setValue",
                    (data.JYDZ == "null" || data.JYDZ == null) ? "" : data.JYDZ);
            $("#cz${idSuffix}").textbox("setValue",
                    (data.CZ == "null" || data.CZ == null) ? "" : data.CZ);
        } else if ("TZ" == data.sysName) {
            $("#gyzd").show();
            //显示标签
            $("#tzcmcDiv${idSuffix}").show();
            $("#pc-and-so-onDiv${idSuffix}").show();
            //移除标签
            $("#zzqyjbxx").remove();
            $("#pfscmcDiv${idSuffix}").remove();
            $("#lsscmcDiv${idSuffix}").remove();
            $("#csmcDiv${idSuffix}").remove();
            $("#jgcmcDiv${idSuffix}").remove();
            $("#yzcmcDiv${idSuffix}").remove();
            $("#jgDiv${idSuffix}").remove();//加工
            $("#zzDiv${idSuffix}").remove();//种植
            $("#yzDiv${idSuffix}").remove();//养殖
            $("#zzjdmcDiv${idSuffix}").remove();
            $("#cdDiv${idSuffix}").remove();//产地
            //表单赋值
            $("#tzcmc${idSuffix}").textbox("setValue",
                    (data.TZCMC == "null" || data.TZCMC == null) ? "" : data.TZCMC);
            $("#lsxzqhdm${idSuffix}").textbox("setValue",
                    (data.LSXZQHDM == "null" || data.LSXZQHDM == null) ? "" : data.LSXZQHDM);
            $("#barq${idSuffix}").datepicker("option", "value",
                    (data.BARQ == "null" || data.BARQ == null) ? "" : data.BARQ);
            $("#frdb${idSuffix}").textbox("setValue",
                    (data.FRDB == "null" || data.FRDB == null) ? "" : data.FRDB);
            $("#jydz${idSuffix}").textbox("setValue",
                    (data.JYDZ == "null" || data.JYDZ == null) ? "" : data.JYDZ);
            $("#cz${idSuffix}").textbox("setValue",
                    (data.CZ == "null" || data.CZ == null) ? "" : data.CZ);
        } else if ("LS" == data.sysName) {
            $("#gyzd").show();
            //显示标签
            $("#lsscmcDiv${idSuffix}").show();
            $("#pc-and-so-onDiv${idSuffix}").show();
            //移除标签
            $("#zzqyjbxx").remove();
            $("#pfscmcDiv${idSuffix}").remove();
            $("#tzcmcDiv${idSuffix}").remove();
            $("#csmcDiv${idSuffix}").remove();
            $("#yzcmcDiv${idSuffix}").remove();
            $("#jgcmcDiv${idSuffix}").remove();
            $("#jgDiv${idSuffix}").remove();//加工
            $("#zzDiv${idSuffix}").remove();//种植
            $("#yzDiv${idSuffix}").remove();//养殖
            $("#zzjdmcDiv${idSuffix}").remove();
            $("#cdDiv${idSuffix}").remove();//产地
            //表单赋值
            $("#lsscmc${idSuffix}").textbox("setValue",
                    (data.LSSCMC == "null" || data.LSSCMC == null) ? "" : data.LSSCMC);
            $("#lsxzqhdm${idSuffix}").textbox("setValue",
                    (data.LSXZQHDM == "null" || data.LSXZQHDM == null) ? "" : data.LSXZQHDM);
            $("#barq${idSuffix}").datepicker("option", "value",
                    (data.BARQ == "null" || data.BARQ == null) ? "" : data.BARQ);
            $("#frdb${idSuffix}").textbox("setValue",
                    (data.FRDB == "null" || data.FRDB == null) ? "" : data.FRDB);
            $("#jydz${idSuffix}").textbox("setValue",
                    (data.JYDZ == "null" || data.JYDZ == null) ? "" : data.JYDZ);
            $("#cz${idSuffix}").textbox("setValue",
                    (data.CZ == "null" || data.CZ == null) ? "" : data.CZ);
        } else if ("CS" == data.sysName) {
            $("#gyzd").show();
            //显示标签
            $("#csmcDiv${idSuffix}").show();
            $("#pc-and-so-onDiv${idSuffix}").show();
            //移除标签
            $("#zzqyjbxx").remove();
            $("#pfscmcDiv${idSuffix}").remove();
            $("#tzcmcDiv${idSuffix}").remove();
            $("#lsscmcDiv${idSuffix}").remove();
            $("#yzcmcDiv${idSuffix}").remove();
            $("#jgcmcDiv${idSuffix}").remove();
            $("#jgDiv${idSuffix}").remove();//加工
            $("#zzDiv${idSuffix}").remove();//种植
            $("#yzDiv${idSuffix}").remove();//养殖
            $("#zzjdmcDiv${idSuffix}").remove();
            $("#cdDiv${idSuffix}").remove();//产地
            //表单赋值
            $("#csmc${idSuffix}").textbox("setValue",
                    (data.CSMC == "null" || data.CSMC == null) ? "" : data.CSMC);
            $("#lsxzqhdm${idSuffix}").textbox("setValue",
                    (data.LSXZQHDM == "null" || data.LSXZQHDM == null) ? "" : data.LSXZQHDM);
            $("#barq${idSuffix}").datepicker("option", "value",
                    (data.BARQ == "null" || data.BARQ == null) ? "" : data.BARQ);
            $("#frdb${idSuffix}").textbox("setValue",
                    (data.FRDB == "null" || data.FRDB == null) ? "" : data.FRDB);
            $("#jydz${idSuffix}").textbox("setValue",
                    (data.JYDZ == "null" || data.JYDZ == null) ? "" : data.JYDZ);
            $("#cz${idSuffix}").textbox("setValue",
                    (data.CZ == "null" || data.CZ == null) ? "" : data.CZ);
        } else if ("TT" == data.sysName) {
            $("#gyzd").show();
            //显示标签
            $("#pc-and-so-onDiv${idSuffix}").show();
            //移除标签
            $("#zzqyjbxx").remove();
            $("#pfscmcDiv${idSuffix}").remove();
            $("#tzcmcDiv${idSuffix}").remove();
            $("#lsscmcDiv${idSuffix}").remove();
            $("#csmcDiv${idSuffix}").remove();
            $("#jgcmcDiv${idSuffix}").remove();
            $("#yzcmcDiv${idSuffix}").remove();
            $("#jgDiv${idSuffix}").remove();//加工
            $("#zzDiv${idSuffix}").remove();//种植
            $("#yzDiv${idSuffix}").remove();//养殖
            $("#zzjdmcDiv${idSuffix}").remove();
            $("#cdDiv${idSuffix}").remove();//产地
            //表单赋值
            $("#lsxzqhdm${idSuffix}").textbox("setValue",
                    (data.LSXZQHDM == "null" || data.LSXZQHDM == null) ? "" : data.LSXZQHDM);
            $("#barq${idSuffix}").datepicker("option", "value",
                    (data.BARQ == "null" || data.BARQ == null) ? "" : data.BARQ);
            $("#frdb${idSuffix}").textbox("setValue",
                    (data.FRDB == "null" || data.FRDB == null) ? "" : data.FRDB);
            $("#jydz${idSuffix}").textbox("setValue",
                    (data.JYDZ == "null" || data.JYDZ == null) ? "" : data.JYDZ);
            $("#cz${idSuffix}").textbox("setValue",
                    (data.CZ == "null" || data.CZ == null) ? "" : data.CZ);
        } else if ("JG" == data.sysName) {
            $("#gyzd").show();
            //显示标签
            $("#jgcmcDiv${idSuffix}").show();
            $("#cdDiv${idSuffix}").show();
            $("#jgDiv${idSuffix}").show();
            //移除标签
            $("#zzqyjbxx").remove();
            $("#pfscmcDiv${idSuffix}").remove();
            $("#tzcmcDiv${idSuffix}").remove();
            $("#lsscmcDiv${idSuffix}").remove();
            $("#csmcDiv${idSuffix}").remove();
            $("#yzcmcDiv${idSuffix}").remove();
            $("#pc-and-so-onDiv${idSuffix}").remove();//批菜等
            $("#zzDiv${idSuffix}").remove();//种植
            $("#yzDiv${idSuffix}").remove();//养殖
            $("#zzjdmcDiv${idSuffix}").remove();
            //隐藏cdmc
            $("#cdmcDiv${idSuffix}").hide();
            //表单赋值
            $("#jgcmc${idSuffix}").textbox("setValue",
                    (data.JGCMC == "null" || data.JGCMC == null) ? "" : data.JGCMC);
            $("#spscxkzh${idSuffix}").textbox("setValue",
                    (data.SPSCXKZH == "null" || data.SPSCXKZH == null) ? "" : data.SPSCXKZH);
            $("#spltxkzh${idSuffix}").textbox("setValue",
                    (data.SPLTXKZH == "null" || data.SPLTXKZH == null) ? "" : data.SPLTXKZH);
            $("#jgcdz${idSuffix}").textbox("setValue",
                    (data.JGCDZ == "null" || data.JGCDZ == null) ? "" : data.JGCDZ);
            $("#cdbm${idSuffix}").combogrid({
                url: $.contextPath + "/cdxx!getCdxxGrid.json",
                multiple: false,
                sortable: true,
                colModel: _colModel,
                colNames: _colNames,
                gridOptions: {
                    rowNum: 20
                },
//                width: 200,
                textField: "DDD",
                valueField: "CDBM",
//                panelWidth: "220",
                height: "auto",
                buttonOptions: buttonOpts
            });
            $("#cdbm${idSuffix}").combogrid("setValue", (data.CDBM == "null" || data.CDBM == null) ? "" : data.CDBM);
            $("#cdmc${idSuffix}").textbox("setValue", (data.CDMC == "null" || data.CDMC == null) ? "" : data.CDMC);
            $("#cdbm${idSuffix}").combogrid("option", "onChange", function (e, data) {
                $("#cdmc${idSuffix}").textbox("setValue", data.newText);
            });
        } else if ("ZZ" == data.sysName || "SDZYC" == data.sysName || "ZYY" == data.sysName) {
            $("#zzqyjbxx").show();
            //移除标签
            $("#gyzd").remove();
            $("#pfscmcDiv${idSuffix}").remove();
            $("#tzcmcDiv${idSuffix}").remove();
            $("#lsscmcDiv${idSuffix}").remove();
            $("#csmcDiv${idSuffix}").remove();
            $("#yzcmcDiv${idSuffix}").remove();
            $("#jgcmcDiv${idSuffix}").remove();
            $("#pc-and-so-onDiv${idSuffix}").remove();//批菜等
            $("#jgDiv${idSuffix}").remove();//加工
            $("#yzDiv${idSuffix}").remove();//养殖
            //表单赋值
            $("#qymc${idSuffix}").textbox("setValue",
                    (data.QYMC == "null" || data.QYMC == null) ? "" : data.QYMC);

            var jsonData = $.loadJson($.contextPath + "/qyptmdgl!getXtlxGrid.json?sysName="+_sysName);

            var dqxtlx = xtlx;
            $("#dwlx${idSuffix}").combobox(
                    {"data": jsonData,readonly:true, value: dqxtlx});
            $("#gszcdjzh${idSuffix}").textbox("setValue",
                    (data.GSZCDJZH == "null" || data.GSZCDJZH == null) ? "" : data.GSZCDJZH);
            $("#zcdz${idSuffix}").textbox("setValue",
                    (data.ZCDZ == "null" || data.ZCDZ == null) ? "" : data.ZCDZ);
            $("#jydz${idSuffix}").textbox("setValue",
                    (data.JYDZ == "null" || data.JYDZ == null) ? "" : data.JYDZ);
            $("#fddb${idSuffix}").textbox("setValue",
                    (data.FDDB == "null" || data.FDDB == null) ? "" : data.FDDB);
            $("#linkman${idSuffix}").textbox("setValue",
                    (data.LINKMAN == "null" || data.LINKMAN == null) ? "" : data.LINKMAN);
            $("#yb${idSuffix}").textbox("setValue",
                    (data.YB == "null" || data.YB == null) ? "" : data.YB);
            $("#clrq${idSuffix}").datepicker("option", "value",
                    (data.CLRQ == "null" || data.CLRQ == null) ? "" : data.CLRQ);
            $("#rzqk${idSuffix}").checkboxlist("setValue",
                    (data.RZQK == "null" || data.RZQK == null) ? "" : data.RZQK);
            $("#zczj${idSuffix}").textbox("option", "value",
                    (data.ZCZJ == "null" || data.ZCZJ == null) ? "" : data.ZCZJ);
            $("#wz${idSuffix}").textbox("option", "value",
                    (data.WZ == "null" || data.WZ == null) ? "" : data.WZ);
            $("#lxdh${idSuffix}").textbox("option", "value",
                    (data.LXDH == "null" || data.LXDH == null) ? "" : data.LXDH);
            $("#cz${idSuffix}").textbox("option", "value",
                    (data.CZ == "null" || data.CZ == null) ? "" : data.CZ);
            $("#cdms${idSuffix}").textbox("option", "value",
                    (data.QYJJ == "null" || data.QYJJ == null) ? "" : data.QYJJ);
        } else if ("YZ" == data.sysName) {
            $("#gyzd").show();
            //显示标签
            $("#yzcmcDiv${idSuffix}").show();
            $("#cdDiv${idSuffix}").show();
            $("#yzDiv${idSuffix}").show();
            //移除标签
            $("#zzqyjbxx").remove();
            $("#pfscmcDiv${idSuffix}").remove();
            $("#tzcmcDiv${idSuffix}").remove();
            $("#lsscmcDiv${idSuffix}").remove();
            $("#csmcDiv${idSuffix}").remove();
            $("#jgcmcDiv${idSuffix}").remove();
            $("#zzjdmcDiv${idSuffix}").remove();
            $("#pc-and-so-onDiv${idSuffix}").remove();//批菜等
            $("#jgDiv${idSuffix}").remove();//加工
            $("#zzDiv${idSuffix}").remove();//种植
            //隐藏cdmc
            $("#cdmcDiv${idSuffix}").hide();
            //表单赋值
            $("#yzcmc${idSuffix}").textbox("setValue", (data.YZCMC == "null" || data.YZCMC == null) ? "" : data.YZCMC);
            $("#yzcdzsh${idSuffix}").textbox("setValue", (data.CDZSH == "null" || data.CDZSH == null) ? "" : data.CDZSH);
            $("#dwfytjhgzh${idSuffix}").textbox("setValue", (data.DWFYTJHGZH == "null" || data.DWFYTJHGZH == null) ? "" : data.DWFYTJHGZH);
            $("#yzcdz${idSuffix}").textbox("setValue", (data.YZCDZ == "null" || data.YZCDZ == null) ? "" : data.YZCDZ);
            $("#yzcdms${idSuffix}").textbox("setValue", (data.QYJJ == "null" || data.QYJJ == null) ? "" : data.QYJJ);
            $("#cdbm${idSuffix}").combogrid({
                url: $.contextPath + "/cdxx!getCdxxGrid.json",
                multiple: false,
                sortable: true,
                colModel: _colModel,
                colNames: _colNames,
                gridOptions: {
                    rowNum: 20
                },
//                width: 200,
                textField: "DDD",
                valueField: "CDBM",
//                panelWidth: "220",
                height: "auto",
                panelHeight: 200,
                buttonOptions: buttonOpts
            });
            $("#cdbm${idSuffix}").combogrid("setValue", (data.CDBM == "null" || data.CDBM == null) ? "" : data.CDBM);
            $("#cdmc${idSuffix}").textbox("setValue", (data.CDMC == "null" || data.CDMC == null) ? "" : data.CDMC);
            $("#cdbm${idSuffix}").combogrid("option", "onChange", function (e, data) {
                $("#cdmc${idSuffix}").textbox("setValue", data.newText);
            });
        }
    }

    document.getElementById('preview${idSuffix}').addEventListener("click", function (e) {
        if (e.target.tagName.toLowerCase() == 'img' && e.target.className == 'thumb') {
            e.target.className = '';
        } else if (e.target.tagName.toLowerCase() == 'img' && e.target.className == '') {
            e.target.className = 'thumb';
        }
    });
    document.getElementById('logoPreview${idSuffix}').addEventListener("click", function (e) {
        if (e.target.tagName.toLowerCase() == 'img' && e.target.className == 'thumb') {
            e.target.className = '';
            var img = e.target;
            var div = e.target.parentElement;
            $(div).css("height", img.height);
            $(div).css("width", img.width);
        } else if (e.target.tagName.toLowerCase() == 'img' && e.target.className == '') {
            e.target.className = 'thumb';
            var img = e.target;
            var div = e.target.parentElement;
            $(div).css("height", img.height);
            $(div).css("width", img.width);
        }
    });
    function logoViewImage(fileInput) {

        if (window.FileReader) {
            var p = $("#logoPreview${idSuffix}");

            var v = $("#logoView${idSuffix}");

            var files = fileInput.files;
            if (files.length > 0) {
                var f = files[0];

                var ImgSuffix = f.name.substring(f.name.lastIndexOf('.')+1);
                //图片不能超过2M，
                if(!(f.size<2097152 && (ImgSuffix.toLocaleLowerCase()=='jpg'||ImgSuffix.toLocaleLowerCase()=='bmp'||ImgSuffix.toLocaleLowerCase()=='png'))){
                    CFG_message("图片必须小于2M，且格式必须为png,jpg或pmg！", "error");
                    $("#logoImageUpload${idSuffix}").val("");
                    return false;
                }

                var fileReader = new FileReader();
                fileReader.onload = function () {
                    var image = new Image();
                    image.src = this.result;
                    image.className = "thumb";
                    image.onload = function (e) {
                        debugger
                        p.empty();
                        v.empty();
                        var div = document.createElement('div');
                        $(div).attr("class","newImages");
                        $(div).append(this);
                        var originWidth = this.width;
                        var originHeight = this.height;
                        div.style.float = "left";
                        div.style.marginTop = "10px";
                        div.style.marginRight = "5px";
                        div.style.height = "75px";
                        div.style.width = (originWidth * 75) / originHeight + "px";
                        this.style.float = "left";
                        this.style.marginTop = "10px";
                        this.style.marginRight = "5px";
                        this.style.height = "75px";
                        this.style.width = (originWidth * 75) / originHeight + "px";
                        console.log(div);
                        p.append(div);
                        saveImage();
                    }
                };
                fileReader.readAsDataURL(f);
            }
        }
    }
    function viewImage(fileInput) {
        var p = $("#qytpDiv${idSuffix}");
        var p1 = $("#preview${idSuffix}");
        var v = $("#view${idSuffix}");
        $(".newImages").remove();
//            p.empty();
        if (window.FileReader) {
            var file = fileInput.files;
            if($(".oldImages").length + file.length > 8){
                CFG_message("图片数量大于八张！", "error");
                $("#imageUpload${idSuffix}").val("");
                return false;
            }
            for (var i = 0, f; f = file[i]; i++) {
                var ImgSuffix = f.name.substring(f.name.lastIndexOf('.')+1);
                //图片不能超过2M，
                if(!(f.size<2097152 && (ImgSuffix.toLocaleLowerCase()=='jpg'||ImgSuffix.toLocaleLowerCase()=='bmp'||ImgSuffix.toLocaleLowerCase()=='png'))){
                    CFG_message("图片必须小于2M，且格式必须为.png, .jpg 或 .bmp！", "error");
                    $("#imageUpload${idSuffix}").val("");
                    return false;
                }
                var fileReader = new FileReader();
                fileReader.onload = function (file) {

                    var image = new Image();
                    image.src = this.result;
                    image.className = "thumb";
                    image.onload = function (e) {
                        debugger
                        p1.empty();
                        v.empty();
                        var div = document.createElement('div');
                        $(div).attr("class","newImages");
                        $(div).append(this);
                        var originWidth = this.width;
                        var originHeight = this.height;
                        div.style.float = "left";
                        div.style.marginTop = "10px";
                        div.style.marginRight = "5px";
                        div.style.height = "75px";
                        div.style.width = (originWidth * 75) / originHeight + "px";
                        this.style.float = "left";
                        this.style.marginTop = "10px";
                        this.style.marginRight = "5px";
                        this.style.height = "75px";
                        this.style.width = (originWidth * 75) / originHeight + "px";
                        console.log(div);
                        p1.append(div);

                    }
                };
                fileReader.readAsDataURL(f);
            }
            saveImage();

        }
    }

    function submitForm() {
        if (!$("#enterForm${idSuffix}").form("valid")) {
            return false
        };
        $("#enterForm${idSuffix}").form().submit();
        // padding-top:0px;
        //$("#enterForm${idSuffix}").form().addClass("top0form");
    }

    function saveImage(){
        $("#enterForm${idSuffix}").form().submit();
        //$("#enterForm${idSuffix}").form().addClass("top0form");

    }

    $("#enterForm${idSuffix}").submit(
            function () {
                var formdata = new FormData(this);
                formdata.append('sysName', _sysName);
                $.ajax({
                    type: 'POST',
                    url: "${basePath}zxtqyda!saveQyda.json?",
                    data: formdata,
                    /**
                     *必须false才会自动加上正确的Content-Type
                     */
                    contentType: false,
                    /**
                     * 必须false才会避开jQuery对 formdata 的默认处理
                     * XMLHttpRequest会对 formdata 进行正确的处理
                     */
                    processData: false,
                    timestamp: false,
                    success: function (data) {
                        if($(".newImages").length == 0){
                            CFG_message("操作成功！", "success");
                        }
                        <%--var view = $("#view${idSuffix}");--%>
                        var preview = $("#preview${idSuffix}");
                        var logoPreview = $("#logoPreview${idSuffix}");
                        <%--var file = $("#imageUpload${idSuffix}");--%>
                        var fileDiv = $("#file${idSuffix}");
                        var logoFileDiv = $("#logoFile${idSuffix}");
                        preview.empty();
                        logoPreview.empty();
                        fileDiv.empty();
                        fileDiv.append('<input class="inputfile" type="file" style="width:160px;display:none" id="imageUpload${idSuffix}" multiple="multiple" lable="预览" accept="image/*" name="imageUpload" onchange="viewImage(this)"/>');
                        logoFileDiv.empty();
                        logoFileDiv.append('<input class="inputfile" type="file" style="width:160px;display:none" id="logoImageUpload${idSuffix}" multiple="multiple" lable="预览" accept="image/*" name="logoImageUpload" onchange="logoViewImage(this)"/>');
                        init();
                        <%--if(null!=data&&data.length!=0){--%>
                        <%--$("#imageView${idSuffix}").attr("src",--%>
                        <%--"${basePath}spzstplj/" + data[0]);--%>
                        <%--}--%>
                        <%--$("#imagePreView${idSuffix}").hide();--%>
                        <%--$("#tip${idSuffix}").hide();--%>
                    },
                    error: function () {
                        CFG_message("操作失败！", "error");
                    }
                });
                return false;
            }
    );



    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'zxtqyda.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#max${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone': function () {
                return $('#max${idSuffix}');
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
                //alert("bodyOnLoad");
                // 按钮权限控制
                //alert(configInfo.notAuthorityComponentButtons);
                if (configInfo.notAuthorityComponentButtons) {
                    $.each(configInfo.notAuthorityComponentButtons, function (i, v) {
                        if (v == 'add') {
                            //$('#toolbarId${idSuffix}').toolbar('disableItem', 'add');
                            $('#toolbarId${idSuffix}').toolbar('hide', 'add');
                        } else if (v == 'update') {
                            //$('#toolbarId${idSuffix}').toolbar('disableItem', 'update');
                            $('#toolbarId${idSuffix}').toolbar('hide', 'update');
                        } else if (v == 'delete') {
                            //$('#toolbarId${idSuffix}').toolbar('disableItem', 'delete');
                            $('#toolbarId${idSuffix}').toolbar('hide', 'delete');
                        }
                    });
                }
            }
        });
    });
</script>
</html>

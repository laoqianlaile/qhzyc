<%--
  Created by IntelliJ IDEA.
  User: WILL
  Date: 15/8/18
  Time: 下午4:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>

<div id="maxDiv${idSuffix}" class="fill" style="height:780px;">
    <ces:layout id="layoutId${idSuffix}" name="" style="width:800px;height:700px;" fit="true">
        <ces:layoutRegion region="north" split="true" style="height:220px;" title="生产方案">
            <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                         data="['->',{'label': '保存', 'id':'saveMaster','cls':'save_tb', 'disabled': 'false','type': 'button'},{'label': '关闭', 'id':'return','cls':'return_tb', 'disabled': 'false','type': 'button'}]">
            </ces:toolbar>
            <div style="margin-top: -25px;position: absolute;"><div class='homeSpan' style="height:23px"><div style='margin-left:25px;width: 150px;font-size:16px' id="nva${idSuffix}"> - 种植任务 - 新增 </div></div></div>
            <div id="enterFormDiv${idSuffix}" style="margin-left: 20px;overflow: auto;height: 160px; ">
                <form id="enterForm${idSuffix}" enctype="multipart/form-data" method="post" class="coralui-form">
                    <div class="fillwidth colspan3 clearfix">
                        <%--<div class="app-inputdiv4">--%>
                            <%--<label class="app-input-label" style="width:36%;">所属区域:</label>--%>
                            <%--<input id="ssqybh${idSuffix}" class="coralui-combobox" name="ssqybh"--%>
                                   <%--data-options="valueField:'QYBH',textField:'QYMC',required:true">--%>
                        <%--</div>--%>
                        <%--<div class="app-inputdiv4">--%>
                            <%--<label class="app-input-label" style="width:36%;">地块:</label>--%>
                            <%--<input id="dk${idSuffix}" class="coralui-combobox" name="dk"--%>
                                   <%--data-options="valueField:'DKBH',textField:'DKMC',required:true">--%>
                        <%--</div>--%>
                        <%--<div class="app-inputdiv4">--%>
                            <%--<label class="app-input-label" style="width:36%;">种植单元:</label>--%>
                            <%--<input id="zzdy${idSuffix}" class="coralui-combobox" name="zzdy"--%>
                                   <%--data-options="valueField:'ZZDYBH',textField:'ZZDYMC'">--%>
                        <%--</div>--%>
                        <%--<input type="hidden" id="qyglybh${idSuffix}">--%>

                        <%--<div class="app-inputdiv4">--%>
                            <%--<label class="app-input-label" style="width:36%;">区域管理员:</label>--%>
                            <%--<input id="qygly${idSuffix}" name="qygly" class="coralui-textbox"--%>
                                   <%--data-options="readonly:true"/>--%>
                        <%--</div>--%>
                        <%--<div class="app-inputdiv4">--%>
                            <%--<label class="app-input-label" style="width:36%;">地块面积:</label>--%>
                            <%--<input id="dkmj${idSuffix}" name="dkmj" class="coralui-textbox"--%>
                                   <%--data-options="readonly:true,buttons:[{innerRight:[{label:'亩'}]}]"/>--%>
                        <%--</div>--%>
                        <%--<div class="app-inputdiv4">--%>
                            <%--<label class="app-input-label" style="width:36%;">种植单元面积:</label>--%>
                            <%--<input id="zzdymj${idSuffix}" name="zzdymj" class="coralui-textbox"--%>
                                   <%--data-options="readonly:true,buttons:[{innerRight:[{label:'亩'}]}]"/>--%>
                        <%--</div>--%>
                            <div class="app-inputdiv4" style="display: none">
                                <label class="app-input-label" style="width:36%;">种植批次号:</label>
                                <div class="app-inputdiv6" style="height:32px;display:none">
                                    <input id="ID${idSuffix}" class="coralui-textbox" name="ID"/>
                                    <input id="zzpch${idSuffix}" class="coralui-textbox" name="zzpch">
                                </div>
                            </div>
                            <div class="app-inputdiv4">
                                <label class="app-input-label" style="width:36%;">种植方案:</label>
                                <input id="zzfa${idSuffix}" class="coralui-combobox" name="zzfa"
                                       data-options="valueField:'ZZFABH',textField:'ZZFAMC',required:true">
                            </div>
                            <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">药材名称:</label>
                            <input id="ycmc${idSuffix}" class="coralui-textbox" name="ycmc"
                                   data-options="readonly:true">
                        </div>
                            <input id="ycdm${idSuffix}" name="ycdm" type="hidden"  class="coralui-textbox">
                            <div class="app-inputdiv4">
                                <label class="app-input-label" style="width:36%;">种苗(种子):</label>
                                <input id="zzzmmc${idSuffix}" class="coralui-textbox" name="zzzmmc"
                                       data-options="readonly:true">
                            </div>
                            <div class="app-inputdiv4">
                                <label class="app-input-label" style="width:36%;">种子来源:</label>
                                <input id="zzly${idSuffix}" class="coralui-combobox" name="zzly" data-options="required:true">


                            </div>
                            <div class="app-inputdiv4">
                                <label class="app-input-label" style="width:36%;">种子重量:</label>
                                <input id="zzzl${idSuffix}"  name="zzzl" validType="naturalnumber" class="coralui-textbox" data-options="required:true">
                            </div>
                            <input id="zzzmbh${idSuffix}" name="zzzmbh" type="hidden"  class="coralui-textbox">
                            <div class="app-inputdiv4">
                                <label class="app-input-label" style="width:36%;">种植基地:</label>
                                <input id="jdmc${idSuffix}" class="coralui-combobox" name="jdmc"
                                       data-options="required:true,valueField:'JDBH',textField:'JDMC'">
                            </div>
                            <%--<input id="jdmc${idSuffix}" name="jdmc" type="hidden"  class="coralui-textbox">--%>
                            <div class="app-inputdiv4">
                                <label class="app-input-label" style="width:36%;">种植地块:</label>
                                <input id="dkmc${idSuffix}" class="coralui-combobox" name="dkbh"
                                       data-options="required:true,valueField:'DKBH',textField:'DKMC'">
                            </div>
                            <%--<input id="dkmc${idSuffix}" name="dkmc" type="hidden"  class="coralui-textbox">--%>
                            <div class="app-inputdiv4">
                                <label class="app-input-label" style="width:36%;">种植负责人:</label>
                                <input id="zzfzr${idSuffix}" class="coralui-textbox" name="zzfzr"
                                       data-options="readonly:true">
                            </div>
                            <div class="app-inputdiv4">
                                <label class="app-input-label" style="width:36%;">地块面积:</label>
                                <input id="dkmj${idSuffix}" name="dkmj" class="coralui-textbox"
                                       data-options="readonly:true,buttons:[{innerRight:[{label:'亩'}]}]"/>
                            </div>
                            <div class="app-inputdiv4">
                                <label class="app-input-label" style="width:36%;">种植时间:</label>
                                <input id="zzsj${idSuffix}" class="coralui-datepicker" name="zzsj"
                                       data-options="required:true">
                            </div>
                            <%--<input id="zzfzrbh${idSuffix}" name="zzfzrbh" type="hidden"  class="coralui-textbox">--%>
                        <%--<div class="app-inputdiv4">--%>
                            <%--<label class="app-input-label" style="width:36%;">种植结束时间:</label>--%>
                            <%--<input id="zzjssj${idSuffix}" class="coralui-datepicker" name="zzjssj"--%>
                                   <%--data-options="required:true">--%>
                        <%--</div>--%>
                        <%--<input type="hidden" id="zzdyglybh${idSuffix}">--%>

                        <%--<div class="app-inputdiv4">--%>
                            <%--<label class="app-input-label" style="width:36%;">种植单元管理员:</label>--%>
                            <%--<input id="zzdygly${idSuffix}" name="zzdygly" class="coralui-textbox"--%>
                                   <%--data-options="readonly:true"/>--%>
                        <%--</div>--%>
                        <%--<input id="plbh${idSuffix}" name="plbh" type="hidden">--%>
                        <%--<input id="pzbh${idSuffix}" name="pzbh" type="hidden">--%>

                        <%--<div class="app-inputdiv4">--%>
                            <%--<label class="app-input-label" style="width:36%;">品类:</label>--%>
                            <%--<input id="pl${idSuffix}" class="coralui-textbox" name="pl"--%>
                                   <%--data-options="required:true,readonly:true">--%>
                        <%--</div>--%>
                        <%--<div class="app-inputdiv4">--%>
                            <%--<label class="app-input-label" style="width:36%;">品种:</label>--%>
                            <%--<input id="pz${idSuffix}" class="coralui-textbox" name="pz"--%>
                                   <%--data-options="required:true,readonly:true">--%>
                        <%--</div>--%>
                        <%--<div class="app-inputdiv4">--%>
                            <%--<label class="app-input-label" style="width:36%;">状态:</label>--%>
                            <%--<input id="zt${idSuffix}" class="coralui-combobox" name="zt"--%>
                                   <%--data-options="valueField:'value',textField:'text',data:[{value:1,text:'进行中'},{value:0, text:'已完成'}],readonly:'true'">--%>
                        <%--</div>--%>
                        <%--<div class="app-inputdiv12">--%>
                            <%--<label class="app-input-label" style="width:12%;">土壤指标:</label>--%>
                            <%--<textarea id="trzb${idSuffix}" class="coralui-textbox" name="trzb"/>--%>
                        <%--</div>--%>
                    </div>
                </form>
            </div>
        </ces:layoutRegion>
        <ces:layoutRegion region="center" split="true" style="height:295px;"  cls="small-grid">
            <%--<div><input type="button" onclick="test133()" value="收起"/></div>--%>
            <div id="tabs${idSuffix}" class="coralui-tabs" data-options="heightStyle:'fill'" style="display: none;">
                <ul>
                    <li onclick="$('#scbzGrid${idSuffix}').grid('refresh')"><a href="#scbz${idSuffix}">播种方案</a></li>
                    <li onclick="$('#scggGrid${idSuffix}').grid('refresh')"><a href="#scgg${idSuffix}">灌溉方案</a></li>
                    <li onclick="$('#scsfGrid${idSuffix}').grid('refresh')"><a href="#scsf${idSuffix}">施肥方案</a></li>
                    <li onclick="$('#scyyGrid${idSuffix}').grid('refresh')"><a href="#scyy${idSuffix}">用药方案</a></li>
                    <%--<li><a href="#scjc${idSuffix}">检测方案</a></li>--%>
                    <li onclick="$('#scccGrid${idSuffix}').grid('refresh')"><a href="#sccc${idSuffix}">除草</a></li>
                    <li onclick="$('#sccsGrid${idSuffix}').grid('refresh')"><a href="#sccs${idSuffix}">采收</a></li>
                    <li onclick="$('#scqtGrid${idSuffix}').grid('refresh')"><a href="#scqt${idSuffix}">其它</a></li>
                </ul>
                <!-- 播种 -->
                <div id="scbz${idSuffix}" name="tabBorderDiv" style="height: 450px !important">
                    <ces:toolbar id="scbzToolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScbz', 'disabled': 'false','type': 'button'},{'type': 'html','content':'<span>起始农事项时间:</span>'},{'type': 'textbox','id': 'qsnsxsjlabel','readonly': 'true'}]">
                    </ces:toolbar>
                    <div id="scbzGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scbzGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill"
                                  cellEdit="true"
                                  onComplete="$.ns('namespaceId${idSuffix}').parseToolbar"
                                  shrinkToFit="true">
                            <ces:gridCols>
                                <ces:gridCol name="qsnsx" hidden="true"></ces:gridCol>
                                <ces:gridCol name="nszyxbh" formatter="text" formatoptions="required: true"
                                             width="200">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="nszyxmc" formatter="text" formatoptions="required: true"
                                             width="220">农事作业项名称</ces:gridCol>
                                <ces:gridCol name="zpfs" formatter="combobox" formatoptions="required: true"
                                             revertCode="true"
                                             width="200">栽培方式</ces:gridCol>
                                <ces:gridCol name="nsxjgsj" formatter="text"
                                             formatoptions="required: true,validType: 'integer'"
                                             width="200">农事项间隔时间</ces:gridCol>
                                <ces:gridCol name="czfdsj" formatter="text"
                                             formatoptions="required: true,validType: 'naturalnumber'"
                                             sorttype="naturalnumber" width="180">操作浮动时间</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="155" formatter="$.ns('namespaceId${idSuffix}').initGridOp"
                                        >操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScbzTrp', 'disabled': 'false','type': 'button'}]">
                    </ces:toolbar>
                    <div id="scbzTrpGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scbzTrpGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill">
                            <ces:gridCols>
                                <ces:gridCol name="nszyxbh" formatter="combobox" formatoptions="required: true"
                                             revertCode="true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="trplx" formatter="combobox" width="80">投入品类型</ces:gridCol>
                                <ces:gridCol name="trptym" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品通用名</ces:gridCol>
                                <ces:gridCol name="trpmc" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品名称</ces:gridCol>
                                <ces:gridCol name="yt" width="80">用途</ces:gridCol>
                                <%--<ces:gridCol name="gg" width="80">规格</ces:gridCol>--%>
                                <ces:gridCol name="yl" formatter="text" formatoptions="required: true,pattern:'//^([1-9]+(\.[0-9]+[1-9])?)|([0]+(\.([0-9]+)?[1-9]))$//'"
                                             width="80">用量（千克/每亩）</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"
                                             formatoptions="onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delDetailGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                </div>
                <!-- 灌溉 -->
                <div id="scgg${idSuffix}" name="tabBorderDiv" style="height: 450px !important">
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScgg', 'disabled': 'false','type': 'button'},{'type': 'html','content':'<span>起始农事项时间:</span>'},{'type': 'textbox','id': 'qsnsxsjlabel','readonly': 'true'}]">
                    </ces:toolbar>
                    <div id="scggGridDiv${idSuffix}" style="height: 183px;">
                        <ces:grid id="scggGrid${idSuffix}" fitStyle="fill" height="auto" rownumbers="true"
                                  onComplete="$.ns('namespaceId${idSuffix}').parseToolbar"
                                  shrinkToFit="true">
                            <ces:gridCols>
                                <ces:gridCol name="qsnsx" hidden="true"></ces:gridCol>
                                <ces:gridCol name="nszyxbh" formatter="text" formatoptions="required: true"
                                             width="200">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="nszyxmc" formatter="text" formatoptions="required: true"
                                             width="220">农事作业项名称</ces:gridCol>
                                <ces:gridCol name="ggfs" revertCode="true" sortable="true" width="120"
                                             formatter="combobox" formatoptions="required: true">灌溉方式</ces:gridCol>
                                <ces:gridCol name="sylx" revertCode="true" width="120" formatter="combobox"
                                             formatoptions="required: true">水源类型</ces:gridCol>
                                <ces:gridCol name="sydj" revertCode="true" width="100" formatter="combobox"
                                             formatoptions="required: true">水源等级</ces:gridCol>
                                <ces:gridCol name="nsxjgsj" formatter="text"
                                             formatoptions="required: true,validType: 'integer'"
                                             width="125">农事项间隔时间</ces:gridCol>
                                <ces:gridCol name="czfdsj" formatter="text"
                                             formatoptions="required: true,validType: 'naturalnumber'"
                                             sorttype="naturalnumber" width="110">操作浮动时间</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="155" formatter="$.ns('namespaceId${idSuffix}').initGridOp"
                                        >操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScggTrp', 'disabled': 'false','type': 'button'}]">
                    </ces:toolbar>
                    <div id="scggTrpGridDiv${idSuffix}" style="height: 183px;">
                        <ces:grid id="scggTrpGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill">
                            <ces:gridCols>
                                <ces:gridCol name="nszyxbh" formatter="combobox" formatoptions="required: true"
                                             revertCode="true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="trplx" formatter="combobox" width="80">投入品类型</ces:gridCol>
                                <ces:gridCol name="trptym" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品通用名</ces:gridCol>
                                <ces:gridCol name="trpmc" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品名称</ces:gridCol>
                                <ces:gridCol name="yt" width="80">用途</ces:gridCol>
                                <%--<ces:gridCol name="gg" width="80">规格</ces:gridCol>--%>
                                <ces:gridCol name="yl" formatter="text" formatoptions="required: true,pattern:'//^([1-9]+(\.[0-9]+[1-9])?)|([0]+(\.([0-9]+)?[1-9]))$//'"
                                             width="80">用量（千克/每亩）或数量</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"
                                             formatoptions="onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delDetailGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                </div>
                <!-- 施肥 -->
                <div id="scsf${idSuffix}" name="tabBorderDiv" style="height: 450px !important">
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScsf', 'disabled': 'false','type': 'button'},{'type': 'html','content':'<span>起始农事项时间:</span>'},{'type': 'textbox','id': 'qsnsxsjlabel','readonly': 'true'}]">
                    </ces:toolbar>
                    <div id="scsfGridDiv${idSuffix}" style="height: 183px;">
                        <ces:grid id="scsfGrid${idSuffix}" fitStyle="fill" height="auto" rownumbers="true"
                                  onComplete="$.ns('namespaceId${idSuffix}').parseToolbar"
                                  shrinkToFit="true">
                            <ces:gridCols>
                                <ces:gridCol name="qsnsx" hidden="true"></ces:gridCol>
                                <ces:gridCol name="nszyxbh" formatter="text" formatoptions="required: true"
                                             width="200">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="nszyxmc" formatter="text" formatoptions="required: true"
                                             width="220">农事作业项名称</ces:gridCol>
                                <ces:gridCol name="sfsd" revertCode="true" sortable="true" width="200"
                                             formatter="combobox" formatoptions="required: true">施肥时段</ces:gridCol>
                                <ces:gridCol name="nsxjgsj" formatter="text"
                                             formatoptions="required: true,validType: 'integer'"
                                             width="200">农事项间隔时间</ces:gridCol>
                                <ces:gridCol name="czfdsj" formatter="text"
                                             formatoptions="required: true,validType: 'naturalnumber'"
                                             sorttype="naturalnumber" width="180">操作浮动时间</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="155" formatter="$.ns('namespaceId${idSuffix}').initGridOp"
                                        >操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScsfTrp', 'disabled': 'false','type': 'button'}]">
                    </ces:toolbar>
                    <div id="scsfTrpGridDiv${idSuffix}" style="height: 183px;">
                        <ces:grid id="scsfTrpGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill">
                            <ces:gridCols>
                                <ces:gridCol name="nszyxbh" formatter="combobox" formatoptions="required: true"
                                             revertCode="true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="trplx" formatter="combobox" width="80">投入品类型</ces:gridCol>
                                <ces:gridCol name="trptym" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品通用名</ces:gridCol>
                                <ces:gridCol name="trpmc" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品名称</ces:gridCol>
                                <ces:gridCol name="yt" width="80">用途</ces:gridCol>
                                <%--<ces:gridCol name="gg" width="80">规格</ces:gridCol>--%>
                                <ces:gridCol name="yl" formatter="text" formatoptions="required: true,pattern:'//^([1-9]+(\.[0-9]+[1-9])?)|([0]+(\.([0-9]+)?[1-9]))$//'"
                                             width="80">用量（千克/每亩）</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"
                                             formatoptions="onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delDetailGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                </div>
                <!-- 用药 -->
                <div id="scyy${idSuffix}" name="tabBorderDiv" style="height: 450px !important">
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScyy', 'disabled': 'false','type': 'button'},{'type': 'html','content':'<span>起始农事项时间:</span>'},{'type': 'textbox','id': 'qsnsxsjlabel','readonly': 'true'}]">
                    </ces:toolbar>
                    <div id="scyyGridDiv${idSuffix}" style="height: 183px;">
                        <ces:grid id="scyyGrid${idSuffix}" fitStyle="fill" height="auto" rownumbers="true"
                                  onComplete="$.ns('namespaceId${idSuffix}').parseToolbar"
                                  shrinkToFit="true">
                            <ces:gridCols>
                                <ces:gridCol name="qsnsx" hidden="true"></ces:gridCol>
                                <ces:gridCol name="nszyxbh" formatter="text" formatoptions="required: true"
                                             width="200">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="nszyxmc" formatter="text" formatoptions="required: true"
                                             width="220">农事作业项名称</ces:gridCol>
                                <ces:gridCol name="yysd" formatter="combobox" formatoptions="required: true"
                                             revertCode="true" width="200">用药时段</ces:gridCol>
                                <ces:gridCol name="nsxjgsj" formatter="text"
                                             formatoptions="required: true,validType: 'integer'"
                                             width="200">农事项间隔时间</ces:gridCol>
                                <ces:gridCol name="czfdsj" formatter="text"
                                             formatoptions="required: true,validType: 'naturalnumber'"
                                             sorttype="naturalnumber" width="180">操作浮动时间</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="155" formatter="$.ns('namespaceId${idSuffix}').initGridOp"
                                        >操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScyyTrp', 'disabled': 'false','type': 'button'}]">
                    </ces:toolbar>
                    <div id="scyyTrpGridDiv${idSuffix}" style="height: 183px;">
                        <ces:grid id="scyyTrpGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill"
                                  onComplete="$.ns('namespaceId${idSuffix}').parseToolbar">
                            <ces:gridCols>
                                <ces:gridCol name="nszyxbh" formatter="combobox" formatoptions="required: true"
                                             revertCode="true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="trplx" formatter="combobox" width="80">投入品类型</ces:gridCol>
                                <ces:gridCol name="trptym" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品通用名</ces:gridCol>
                                <ces:gridCol name="trpmc" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品名称</ces:gridCol>
                                <ces:gridCol name="yt" width="80">用途</ces:gridCol>
                                <%--<ces:gridCol name="gg" width="80">规格</ces:gridCol>--%>
                                <ces:gridCol name="yl" formatter="text" formatoptions="required: true,pattern:'//^([1-9]+(\.[0-9]+[1-9])?)|([0]+(\.([0-9]+)?[1-9]))$//'"
                                             width="80">用量（千克/每亩）</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"
                                             formatoptions="onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delDetailGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                </div>

                <!-- 采收 -->
                <div id="sccs${idSuffix}" name="tabBorderDiv" style="height: 450px !important">
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addSccs', 'disabled': 'false','type': 'button'},{'type': 'html','content':'<span>起始农事项时间:</span>'},{'type': 'textbox','id': 'qsnsxsjlabel','readonly': 'true'}]">
                    </ces:toolbar>
                    <div id="sccsGridDiv${idSuffix}" style="height: 183px;">
                        <ces:grid id="sccsGrid${idSuffix}" fitStyle="fill" height="auto" rownumbers="true"
                                  onComplete="$.ns('namespaceId${idSuffix}').parseToolbar"
                                  shrinkToFit="true">
                            <ces:gridCols>
                                <ces:gridCol name="qsnsx" hidden="true"></ces:gridCol>
                                <ces:gridCol name="nszyxbh" formatter="text" formatoptions="required: true"
                                             width="200">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="nszyxmc" formatter="text" formatoptions="required: true"
                                             width="220">农事作业项名称</ces:gridCol>
                                <ces:gridCol name="csfs" formatter="combobox" formatoptions="required: true"
                                             revertCode="true" width="200">采收方式</ces:gridCol>
                                <ces:gridCol name="nsxjgsj" formatter="text"
                                             formatoptions="required: true,validType: 'integer'"
                                             width="200">农事项间隔时间</ces:gridCol>
                                <ces:gridCol name="czfdsj" formatter="text"
                                             formatoptions="required: true,validType: 'naturalnumber'"
                                             sorttype="naturalnumber" width="180">操作浮动时间</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="155" formatter="$.ns('namespaceId${idSuffix}').initGridOp"
                                        >操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addSccsTrp', 'disabled': 'false','type': 'button'}]">
                    </ces:toolbar>
                    <div id="sccsTrpGridDiv${idSuffix}" style="height: 183px;">
                        <ces:grid id="sccsTrpGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill">
                            <ces:gridCols>
                                <ces:gridCol name="nszyxbh" formatter="combobox" formatoptions="required: true"
                                             revertCode="true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="trplx" formatter="combobox" width="80">投入品类型</ces:gridCol>
                                <ces:gridCol name="trptym" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品通用名</ces:gridCol>
                                <ces:gridCol name="trpmc" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品名称</ces:gridCol>
                                <ces:gridCol name="yt" width="80">用途</ces:gridCol>
                                <%--<ces:gridCol name="gg" width="80">规格</ces:gridCol>--%>
                                <ces:gridCol name="yl" formatter="text" formatoptions="required: true,pattern:'//^([1-9]+(\.[0-9]+[1-9])?)|([0]+(\.([0-9]+)?[1-9]))$//'"
                                             width="80">用量（千克/每亩）</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"
                                             formatoptions="onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delDetailGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                </div>
                <!-- 锄草 -->
                <div id="sccc${idSuffix}" name="tabBorderDiv" style="height: 450px !important">
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addSccc', 'disabled': 'false','type': 'button'},{'type': 'html','content':'<span>起始农事项时间:</span>'},{'type': 'textbox','id': 'qsnsxsjlabel','readonly': 'true'}]">
                    </ces:toolbar>
                    <div id="scccGridDiv${idSuffix}" style="height: 183px;">
                        <ces:grid id="scccGrid${idSuffix}" fitStyle="fill" height="auto" rownumbers="true"
                                  onComplete="$.ns('namespaceId${idSuffix}').parseToolbar"
                                  shrinkToFit="true">
                            <ces:gridCols>
                                <ces:gridCol name="qsnsx" hidden="true"></ces:gridCol>
                                <ces:gridCol name="nszyxbh" formatter="text" formatoptions="required: true"
                                             width="200">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="nszyxmc" formatter="text" formatoptions="required: true"
                                             width="220">农事作业项名称</ces:gridCol>
                                <ces:gridCol name="ccfs" formatter="combobox" formatoptions="required: true"
                                             revertCode="true" width="200">除草方式</ces:gridCol>
                                <ces:gridCol name="nsxjgsj" formatter="text"
                                             formatoptions="required: true,validType: 'integer'"
                                             width="200">农事项间隔时间</ces:gridCol>
                                <ces:gridCol name="czfdsj" formatter="text"
                                             formatoptions="required: true,validType: 'naturalnumber'"
                                             sorttype="naturalnumber" width="180">操作浮动时间</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="155" formatter="$.ns('namespaceId${idSuffix}').initGridOp"
                                        >操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScccTrp', 'disabled': 'false','type': 'button'}]">
                    </ces:toolbar>
                    <div id="scccTrpGridDiv${idSuffix}" style="height: 183px;">
                        <ces:grid id="scccTrpGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill">
                            <ces:gridCols>
                                <ces:gridCol name="nszyxbh" formatter="combobox" formatoptions="required: true"
                                             revertCode="true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="trplx" formatter="combobox" width="80">投入品类型</ces:gridCol>
                                <ces:gridCol name="trptym" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品通用名</ces:gridCol>
                                <ces:gridCol name="trpmc" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品名称</ces:gridCol>
                                <ces:gridCol name="yt" width="80">用途</ces:gridCol>
                                <%--<ces:gridCol name="gg" width="80">规格</ces:gridCol>--%>
                                <ces:gridCol name="yl" formatter="text" formatoptions="required: true,pattern:'//^([1-9]+(\.[0-9]+[1-9])?)|([0]+(\.([0-9]+)?[1-9]))$//'"
                                             width="80">用量（千克/每亩）</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"
                                             formatoptions="onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delDetailGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                </div>
                <!-- 其它 -->
                <div id="scqt${idSuffix}" name="tabBorderDiv" style="height: 450px !important">
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScqt', 'disabled': 'false','type': 'button'},{'type': 'html','content':'<span>起始农事项时间:</span>'},{'type': 'textbox','id': 'qsnsxsjlabel','readonly': 'true'}]">
                    </ces:toolbar>
                    <div id="scqtGridDiv${idSuffix}" style="height: 183px;">
                        <ces:grid id="scqtGrid${idSuffix}" fitStyle="fill" height="auto" rownumbers="true"
                                  onComplete="$.ns('namespaceId${idSuffix}').parseToolbar"
                                  shrinkToFit="true">
                            <ces:gridCols>
                                <ces:gridCol name="qsnsx" hidden="true"></ces:gridCol>
                                <ces:gridCol name="nszyxbh" formatter="text" formatoptions="required: true"
                                             width="244">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="nszyxmc" formatter="text" formatoptions="required: true"
                                             width="264">农事作业项名称</ces:gridCol>
                                <ces:gridCol name="nsxjgsj" formatter="text"
                                             formatoptions="required: true,validType: 'integer'"
                                             width="244">农事项间隔时间</ces:gridCol>
                                <ces:gridCol name="czfdsj" formatter="text"
                                             formatoptions="required: true,validType: 'naturalnumber'"
                                             sorttype="naturalnumber" width="244">操作浮动时间</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="155" formatter="$.ns('namespaceId${idSuffix}').initGridOp"
                                        >操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScqtTrp', 'disabled': 'false','type': 'button'}]">
                    </ces:toolbar>
                    <div id="scqtTrpGridDiv${idSuffix}" style="height: 183px;">
                        <ces:grid id="scqtTrpGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill">
                            <ces:gridCols>
                                <ces:gridCol name="nszyxbh" formatter="combobox" formatoptions="required: true"
                                             revertCode="true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="trplx" formatter="combobox" width="80">投入品类型</ces:gridCol>
                                <ces:gridCol name="trptym" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品通用名</ces:gridCol>
                                <ces:gridCol name="trpmc" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品名称</ces:gridCol>
                                <ces:gridCol name="yt" width="80">用途</ces:gridCol>
                                <%--<ces:gridCol name="gg" width="80">规格</ces:gridCol>--%>
                                <ces:gridCol name="yl" formatter="text" formatoptions="required: true,pattern:'//^([1-9]+(\.[0-9]+[1-9])?)|([0]+(\.([0-9]+)?[1-9]))$//'"
                                             width="80">用量（千克/每亩）</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"
                                             formatoptions="onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delDetailGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                </div>
            </div>
        </ces:layoutRegion>
    </ces:layout>
</div>
<script>

    function test133() {
        $("#enterFormDiv${idSuffix}").animate({height: 'toggle', opacity: 'toggle'}, "slow");
    }
    var gridOptions = [];
    $.extend($.ns("namespaceId${idSuffix}"), {
        fapzs: null,
        qyxxs: null,
        dkxxs: null,
        zzdyxxs: null,
        oldNszyxbh: null,
        oldNszyxbhObj: null,
        initGridOp : function(cellvalue, options, rawObject) {
            if(rawObject.qsnsx == "1"){
                <%--return "<div class=\"coralui-toolbar\" gridId=\"" + options.gid + "\" rowId=\"" + options.rowId + "\" data-options=\"onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '设置起始时间', 'id':'setFirst', 'disabled': 'false','type': 'button'}]\"></div>";--%>
                return "";
            }else{
                return "<div class=\"coralui-toolbar\" gridId=\"" + options.gid + "\" rowId=\"" + options.rowId + "\" width=\"90\" data-options=\"onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delGridData', 'disabled': 'false','type': 'button'}]\"></div>";
            }
        },
        parseToolbar : function (e, data) {
            $.parser.parse(e.target);
        },
        initform: function () {//初始化表单信息

            //种植时间
            $("#zzsj${idSuffix}").datepicker("setDate", new Date());
            $("#zzsj${idSuffix}").datepicker("option", "onChange", function(e, data){
                var gridIds = ["scbzGrid${idSuffix}","scggGrid${idSuffix}","scsfGrid${idSuffix}","scyyGrid${idSuffix}","scccGrid${idSuffix}","sccsGrid${idSuffix}","scqtGrid${idSuffix}"];
                $.each(gridIds, function(e, data){
                    $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent(data).textbox("setValue",$("#zzsj${idSuffix}").datepicker("getValue"));
                });
            });
            var gridIds = ["scbzGrid${idSuffix}","scggGrid${idSuffix}","scsfGrid${idSuffix}","scyyGrid${idSuffix}","scccGrid${idSuffix}","sccsGrid${idSuffix}","scqtGrid${idSuffix}"];
            $.each(gridIds, function(e, data){
                $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent(data).textbox("setValue",$("#zzsj${idSuffix}").datepicker("getValue"));
            });
            //区域信息
            $("#ssqybh${idSuffix}").combobox("option", "onChange", function (e, ui) {
                var qybh = ui.value;
                for (var i in $.ns("namespaceId${idSuffix}").qyxxs) {
                    var qyxx = $.ns("namespaceId${idSuffix}").qyxxs[i];
                    if (qyxx.QYBH == qybh) {
                        $("#qygly${idSuffix}").textbox("setValue", qyxx.FZR);
                        $("#qyglybh${idSuffix}").val(qyxx.FZRBH);
                        break;
                    }
                }
                var dks = $.loadJson($.contextPath + "/sczzscda!getDkxxByQybh.json?qybh=" + qybh);
                $.ns("namespaceId${idSuffix}").dkxxs = dks;
                $("#dk${idSuffix}").combobox("reload", dks);
                //清空地块信息和种植单元信息
                $("#dk${idSuffix}").combobox("setValue", "");
                $("#zzdy${idSuffix}").combobox("setValue", "");
                $.ns("namespaceId${idSuffix}").zzdyxxs = null;
            });
            /***************************************************/
            //地块信息
            //onChange事件
            $("#dkmc${idSuffix}").combobox("option", "onChange", function(e,data){
                $("#dkmc${idSuffix}").combobox("setValue", data.value);
                var dkObj = $.loadJson($.contextPath + "/sczzscda!getFzrJD.json?dkbh="+data.value)
                //$('#jdmc${idSuffix}').combobox('setValue',dkObj.JDBH);
                $('#zzfzr${idSuffix}').textbox('setValue',dkObj.DKFZR);
                $('#dkmj${idSuffix}').textbox('setValue',dkObj.MJ);

            });
            <%--$("#zzfzrbh${idSuffix}").combobox("option", "onChange", function(e,data){--%>
                <%--&lt;%&ndash;$("#zzfzrbh${idSuffix}").combobox("setValue", data.value);&ndash;%&gt;--%>
                <%--&lt;%&ndash;$("#zzfzr${idSuffix}").textbox("setValue", data.text);&ndash;%&gt;--%>
            <%--});--%>
            $("#jdmc${idSuffix}").combobox("option", "onChange", function(e,data){
                $("#jdmc${idSuffix}").combobox("setValue", data.value);
                var dkObj = $.loadJson($.contextPath + "/sczzscda!getJddk.json?jdbh="+data.value);
                $('#dkmc${idSuffix}').combobox('reload',dkObj);
            });
            /*******************************************/
            $("#dk${idSuffix}").combobox("option", "onChange", function (e, ui) {
                var dkbh = ui.value;
                var zzdys = $.loadJson($.contextPath + "/sczzscda!getZzdyBydk.json?dkbh=" + dkbh);
                $.ns("namespaceId${idSuffix}").zzdyxxs = zzdys;
                $("#zzdy${idSuffix}").combobox("reload", zzdys);
                for (var i in $.ns("namespaceId${idSuffix}").dkxxs) {
                    var dkxx = $.ns("namespaceId${idSuffix}").dkxxs[i];
                    if (dkxx.DKBH == dkbh) {
                        $("#dkmj${idSuffix}").textbox("setValue", dkxx.MJ);
                        break;
                    }
                }
                //清空种植单元信息
                $("#zzdy${idSuffix}").combobox("setValue", "");
            });
            //种植单元信息
            $("#zzdy${idSuffix}").combobox("option", "onChange", function (e, ui) {
                for (var i in $.ns("namespaceId${idSuffix}").zzdyxxs) {
                    var zzdyxx = $.ns("namespaceId${idSuffix}").zzdyxxs[i];
                    if (zzdyxx.ZZDYBH == ui.value) {
                        $("#zzdymj${idSuffix}").textbox("setValue", zzdyxx.ZZDYMJ);
                        $("#zzdygly${idSuffix}").textbox("setValue", zzdyxx.GLY);
                        $("#zzdyglybh${idSuffix}").val(zzdyxx.GLYBH);
                        break;
                    }
                }
            });
            var qys = $.loadJson($.contextPath + "/sczzscda!getQybhList.json");
            $.ns("namespaceId${idSuffix}").qyxxs = qys;
            $("#ssqybh${idSuffix}").combobox("reload", qys);
            //播种方案栽培方式
            var zpfs = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=ZZZPFS");
            $("#scbzGrid${idSuffix}").grid("setColProp", "zpfs", {"formatoptions": {required: true, "data": zpfs}});
            //灌溉方案灌溉方式
            var ggfs = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=GGFS");
            $("#scggGrid${idSuffix}").grid("setColProp", "ggfs", {"formatoptions": {required: true, "data": ggfs}});
            //灌溉方案水源类型
            var sylx = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=SYLX");
            $("#scggGrid${idSuffix}").grid("setColProp", "sylx", {"formatoptions": {required: true, "data": sylx}});
            //灌溉方案水源等级
            var sydj = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=SYDJ");
            $("#scggGrid${idSuffix}").grid("setColProp", "sydj", {"formatoptions": {required: true, "data": sydj}});
            //施肥方案施肥时段
            var sfsd = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=ZZSFSD");
            $("#scsfGrid${idSuffix}").grid("setColProp", "sfsd", {"formatoptions": {required: true, "data": sfsd}});
            //用药时段
            var yysd = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=ZZYYSD");
            $("#scyyGrid${idSuffix}").grid("setColProp", "yysd", {"formatoptions": {required: true, "data": yysd}});
            //检测检测方法
            var jcff = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=ZZJCFF");
            $("#scjcGrid${idSuffix}").grid("setColProp", "jcff", {"formatoptions": {required: true, "data": jcff}});
            //采收方式
            var csfs = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=ZZSCCSFS");
            $("#sccsGrid${idSuffix}").grid("setColProp", "csfs", {"formatoptions": {required: true, "data": csfs}});
            //锄草方式
            var ccfs = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=ZZSCCCFS");
            $("#scccGrid${idSuffix}").grid("setColProp", "ccfs", {"formatoptions": {required: true, "data": ccfs}});
            //农事项编号
            var $nsxbhGrids = $("#scbzGrid${idSuffix},#scggGrid${idSuffix},#scsfGrid${idSuffix},#scyyGrid${idSuffix},#scjcGrid${idSuffix},#sccsGrid${idSuffix},#scccGrid${idSuffix},#scqtGrid${idSuffix}");
            $nsxbhGrids.grid("setColProp", "nszyxbh", {
                "formatoptions": {onChange: function (e, data) {
                    var gridDatas = $("#" + e.data.gridId).grid("getRowData");
                    var i = 0;
                    var oldNszyxbh = $.ns("namespaceId${idSuffix}").oldNszyxbh;
                    $.each(gridDatas,function(e1,data1){
                        if(data1.nszyxbh == data.value){
                            i++;
                        }
                    });
                    if (i > 1) {
                        $("#" + e.data.gridId).grid("getCellComponent", e.data.rowId, "nszyxbh").textbox("setValue", oldNszyxbh);
                        CFG_message("农事作业项编号重复！", "error");
                        return false;
                    }
                    var $dGrid = $.ns('namespaceId${idSuffix}').getDetailGrid(e.data.gridId);
                    $.each($dGrid.grid("getDataIDs"),function(e1,data1){
                        var dNszyxbh = $("#" + e.data.gridId).grid("getCellComponent", data1, "nszyxbh");
                        if(dNszyxbh.combobox("getValue") == oldNszyxbh){
                            dNszyxbh.combobox("setValue",data.value);
                        }
                    });
                    $.ns("namespaceId${idSuffix}").oldNszyxbhObj = "";
                    $.ns("namespaceId${idSuffix}").updateDGridBh(e.data.gridId);
                },onClick: function (e, data) {
                    if($.ns("namespaceId${idSuffix}").oldNszyxbhObj == e.data.rowId){
                        return false;
                    }
                    $.ns("namespaceId${idSuffix}").oldNszyxbhObj = e.data.rowId;
                    $.ns("namespaceId${idSuffix}").oldNszyxbh = $(this).textbox("getValue");
                }
                }
            });
            //投入品类型、通用名、名称
            var allTrplx = $.loadJson($.contextPath + "/sczzscda!getDataDictionary.json?lxbm=ZZTRPLX");
            var allTrptym =null;// $.loadJson($.contextPath + "/sczzscda!getAllTrptym.json");
            var allTrpmc = null;//$.loadJson($.contextPath + "/sczzscda!getAllTrpxx.json");
            var $detailGrids = $("#scbzTrpGrid${idSuffix},#scggTrpGrid${idSuffix},#scsfTrpGrid${idSuffix},#scyyTrpGrid${idSuffix},#scjcTrpGrid${idSuffix},#sccsTrpGrid${idSuffix},#scccTrpGrid${idSuffix},#scqtTrpGrid${idSuffix}");
            $detailGrids.grid("setColProp", "trplx", {
                "formatoptions": {
                    valueField: 'VALUE',
                    textField: 'TEXT',
                    required: true,
                    enableFilter: true,
                    "data": allTrplx, onChange: function (e, data) {
                        var $trptymCombobox = $("#" + e.data.gridId).grid("getCellComponent", e.data.rowId, "trptym");
                        var $trpmcCombobox = $("#" + e.data.gridId).grid("getCellComponent", e.data.rowId, "trpmc");
                        var trptym = $.loadJson($.contextPath + "/sczzscda!getTrptymByLx.json?lx=" + data.value);
                        $trptymCombobox.combobox("reload", trptym);
                        $trptymCombobox.combobox("setValue", "");
                        $trpmcCombobox.combobox("setValue", "");
                    }
                }
            });
            $detailGrids.grid("setColProp", "trptym", {
                "formatoptions": {
                    valueField: 'VALUE',
                    textField: 'TEXT',
                    required: true,
                    enableFilter: true,
//                    "data": allTrptym,
                    onChange: function (e, data) {
                        var $trpmcCombobox = $("#" + e.data.gridId).grid("getCellComponent", e.data.rowId, "trpmc");
                        var rowData =$("#" + e.data.gridId).grid("getRowData", e.data.rowId);
                        var trpmc = $.loadJson($.contextPath + "/sczzscda!getTrptymByLx.json?lx=" + rowData.trplx+"&tym="+rowData.trptym);
                        $trpmcCombobox.combobox("reload", trpmc);
                        $trpmcCombobox.combobox("setValue", "");
                    }
                }
            });
            $detailGrids.grid("setColProp", "trpmc", {
                "formatoptions": {
                    valueField: 'VALUE',
                    textField: 'TEXT',
                    required: true,
//                    "data": allTrpmc,
                    enableFilter: true,
                    onChange: function (e, data) {
                        for (var i in allTrpmc) {
                            if (allTrpmc[i].TRPBH == data.value) {
                                $("#" + e.data.gridId).grid("setRowData", e.data.rowId, {
                                    yt: allTrpmc[i].YT,
                                    gg: allTrpmc[i].BZGG,
                                });
                                var $yltextbox = $("#" + e.data.gridId).grid("getCellComponent",  e.data.rowId, "yl");
                                $yltextbox.textbox("setValue", allTrpmc[i].TJYL);
                                break;
                            }
                        }
                    }
                }
            });
            this.zzly = $.loadJson($.contextPath + "/trace!getXtpzcode.json?lxbm=CZLY");
            $("#zzly${idSuffix}").combobox("reload", this.zzly);
            //种植方案信息
            this.fapzs = $.loadJson($.contextPath + "/sczzscda!getZzfapzxx.json");
            $("#zzfa${idSuffix}").combobox("reload", this.fapzs.fapzs);
            //方案变化的同时，初始化列表的信息
            $("#zzfa${idSuffix}").combobox("option", "onChange", function (e, ui) {
                var zmzzObj = $.loadJson($.contextPath + "/sczzscda!getZzzm.json?zzfabh="+ui.value);
//                var ycmcObj = $.loadJson($.contextPath + "/sczzscda!getYcmc.json?ycmc="+zmzzObj.YCMC);
                $("#zzzmmc${idSuffix}").textbox("setValue",zmzzObj.ZZZMMC);
                $("#zzzmbh${idSuffix}").textbox("setValue",zmzzObj.ZZZMBH);
                $("#ycmc${idSuffix}").textbox("setValue",zmzzObj.YCMC);
                $("#ycdm${idSuffix}").textbox("setValue",zmzzObj.YCDM);
                $("#tabs${idSuffix}").show();
                //清空所有列表的数据，刷新列表
                var $allGrids = $("#scbzGrid${idSuffix},#scbzTrpGrid${idSuffix},#scggGrid${idSuffix},#scggTrpGrid${idSuffix},#scsfGrid${idSuffix},#scsfTrpGrid${idSuffix},#scyyGrid${idSuffix},#scyyTrpGrid${idSuffix},#sccsGrid${idSuffix},#sccsTrpGrid${idSuffix},#scccGrid${idSuffix},#scccTrpGrid${idSuffix},#scqtGrid${idSuffix},#scqtTrpGrid${idSuffix}");
                $allGrids.grid("clearGridData");
                $allGrids.grid("refresh");
                $.ns("namespaceId${idSuffix}").initData(ui.value);
                $("#scbzToolbarId${idSuffix}").toolbar("refresh");

            });
        },
        initData: function (fabh) {
            //初始化所有的列表信息
            var allTrpmc = $.loadJson($.contextPath + "/sczzscda!getAllTrpxx.json");
            var $trpGrids = $("#scbzTrpGrid${idSuffix},#scggTrpGrid${idSuffix},#scsfTrpGrid${idSuffix},#scyyTrpGrid${idSuffix},#scjcTrpGrid${idSuffix},#sccsTrpGrid${idSuffix},#scccTrpGrid${idSuffix},#scqtTrpGrid${idSuffix}");
            //播种
            var bzs = $.ns("namespaceId${idSuffix}").fapzs.bz[fabh];
            for (var i in  bzs) {
                var bz = bzs[i];
                var bzID = generateId("tmp");
//                var bzID = bz.ID;
                $("#scbzGrid${idSuffix}").grid("addRowData", bzID, {
					qsnsx : bz.IS_START,
                    nszyxbh: bz.NSZYXBH,
                    nszyxmc: bz.NSZYXMC,
                    zpfs: bz.ZPFS,
                    nsxjgsj: bz.NSXJGSJ,
                    czfdsj: bz.CZFDSJ
                }, "last");
                if(bz.IS_START == 1){
                    $("#scbzGrid${idSuffix}").grid("setRowData", bzID, null, {background: '#68C561'});
                };
            }
            $.ns('namespaceId${idSuffix}').updateDGridBh("scbzGrid${idSuffix}");
            var bzTrps = $.ns("namespaceId${idSuffix}").fapzs.bzTrp[fabh];
            var allTrptym = $.loadJson($.contextPath + "/sczzscda!getAllTrptym.json");
            $("#scbzTrpGrid${idSuffix}").grid("setColProp", "trptym", {
                "formatoptions": {
                    valueField: 'VALUE',
                    textField: 'TEXT',
                    required: true,
                    enableFilter: true,
                    "data": allTrptym,
                    onChange: function (e, data) {
                        var $trpmcCombobox = $("#" + e.data.gridId).grid("getCellComponent", e.data.rowId, "trpmc");
                        var rowData =$("#" + e.data.gridId).grid("getRowData", e.data.rowId);
                        var trpmc = $.loadJson($.contextPath + "/sczzscda!getTrpxxByTym.json?lx=" + rowData.trplx+"&tym="+rowData.trptym);
                        $trpmcCombobox.combobox("reload", trpmc);
                        $trpmcCombobox.combobox("setValue", "");
                    }
                }
            });
            $("#scbzTrpGrid${idSuffix}").grid("setColProp", "trpmc", {
                "formatoptions": {
                    valueField: 'VALUE',
                    textField: 'TEXT',
                    required: true,
                    enableFilter: true,
                    "data": allTrptym
                }
            });
            for (var i in  bzTrps) {
                var bzTrp = bzTrps[i];
                var lxData = $.loadJson($.contextPath+"/sczzscda!getLxByTym.json?bh="+bzTrp.TRPTYM);
                var bzTrpID = generateId("tmp");
//                var bzTrpID = bzTrp.ID;
                $("#scbzTrpGrid${idSuffix}").grid("addRowData", bzTrpID, {
                    nszyxbh: bzTrp.NSZYXBH,
                    trptym: bzTrp.TRPTYM,
                    trplx: lxData.LX,
                    trpmc: bzTrp.TRPTYM
                }, "last");
            }
            //灌溉
            var ggs = $.ns("namespaceId${idSuffix}").fapzs.gg[fabh];
            for (var i in  ggs) {
                var gg = ggs[i];
                var ggID = generateId("tmp");
//                var ggID = gg.ID;
                $("#scggGrid${idSuffix}").grid("addRowData",ggID, {
					qsnsx : gg.IS_START,
                    nszyxbh: gg.NSZYXBH,
                    nszyxmc: gg.NSZYXMC,
                    ggfs: gg.GGFS,
                    sylx: gg.SYLX,
                    sydj: gg.SYDJ,
                    nsxjgsj: gg.NSXJGSJ,
                    czfdsj: gg.CZFDSJ
                }, "last");
                if(gg.IS_START == 1){
                    $("#scggGrid${idSuffix}").grid("setRowData", ggID, null, {background: '#68C561'});
                };
            }
            $.ns('namespaceId${idSuffix}').updateDGridBh("scggGrid${idSuffix}");
            var ggTym = $.loadJson($.contextPath + "/sczzscda!getGgTym.json");
            $("#scggTrpGrid${idSuffix}").grid("setColProp", "trptym", {
                "formatoptions": {
                    valueField: 'VALUE',
                    textField: 'TEXT',
                    required: true,
                    enableFilter: true,
                    "data": ggTym,
                    onChange: function (e, data) {
                        var $trpmcCombobox = $("#" + e.data.gridId).grid("getCellComponent", e.data.rowId, "trpmc");
                        var rowData =$("#" + e.data.gridId).grid("getRowData", e.data.rowId);
                        var trpmc = $.loadJson($.contextPath + "/sczzscda!getTrpxxByTym.json?lx=" + rowData.trplx+"&tym="+rowData.trptym);
                        $trpmcCombobox.combobox("reload", trpmc);
                        $trpmcCombobox.combobox("setValue", "");
                    }
                }
            });
            $("#scggTrpGrid${idSuffix}").grid("setColProp", "trpmc", {
                "formatoptions": {
                    valueField: 'VALUE',
                    textField: 'TEXT',
                    required: true,
                    enableFilter: true,
                    "data": ggTym,
                    onChange: function (e, data) {
                        var rowData =$("#" + e.data.gridId).grid("getRowData", e.data.rowId);
                        var njjdata = $.loadJson($.contextPath + "/sczzscda!getYtByTrpmc.json?trpmc=" + rowData.trpmc + "&lx="+rowData.trplx);
                        $("#" + e.data.gridId).grid("setRowData", e.data.rowId, {
                            yt: njjdata.TEXT
                        });
                    }
                }
            });
            var ggTrps = $.ns("namespaceId${idSuffix}").fapzs.ggTrp[fabh];
            for (var i in  ggTrps) {
                var ggTrp = ggTrps[i];
                var lxData = $.loadJson($.contextPath+"/sczzscda!getNjjLxByTym.json?bh="+ggTrp.TRPTYM);
                var ggTrpID = generateId("tmp");
//                var ggTrpID = ggTrp.ID;
                $("#scggTrpGrid${idSuffix}").grid("addRowData", ggTrpID, {
                    nszyxbh: ggTrp.NSZYXBH,
                    trptym: ggTrp.TRPTYM,
                    trplx: lxData.LX,
                    trpmc: ggTrp.TRPTYM,
                    yt: ggTrp.YT
                }, "last");
            }
            //施肥
            var sfs = $.ns("namespaceId${idSuffix}").fapzs.sf[fabh];
            for (var i in  sfs) {
                var sf = sfs[i];
                var sfID = generateId("tmp");
//                var sfID = sf.Id;
                $("#scsfGrid${idSuffix}").grid("addRowData", sfID, {
					qsnsx : sf.IS_START,
                    nszyxbh: sf.NSZYXBH,
                    nszyxmc: sf.NSZYXMC,
                    sfsd: sf.SFSD,
                    nsxjgsj: sf.NSXJGSJ,
                    czfdsj: sf.CZFDSJ
                }, "last");
                if(sf.IS_START == 1){
                    $("#scsfGrid${idSuffix}").grid("setRowData", sfID, null, {background: '#68C561'});
                };
            }
            $.ns('namespaceId${idSuffix}').updateDGridBh("scsfGrid${idSuffix}");
            var flTym = $.loadJson($.contextPath + "/sczzscda!getFlTym.json");
            $("#scsfTrpGrid${idSuffix}").grid("setColProp", "trptym", {
                "formatoptions": {
                    valueField: 'VALUE',
                    textField: 'TEXT',
                    required: true,
                    enableFilter: true,
                    "data": flTym,
                    onChange: function (e, data) {
                        var $trpmcCombobox = $("#" + e.data.gridId).grid("getCellComponent", e.data.rowId, "trpmc");
                        var rowData =$("#" + e.data.gridId).grid("getRowData", e.data.rowId);
                        var trpmc = $.loadJson($.contextPath + "/sczzscda!getTrpxxByTym.json?lx=" + rowData.trplx+"&tym="+rowData.trptym);
                        $trpmcCombobox.combobox("reload", trpmc);
                        $trpmcCombobox.combobox("setValue", "");
                    }
                }
            });
            var flTrpmc = $.loadJson($.contextPath + "/sczzscda!getFlTrpmc.json");
            $("#scsfTrpGrid${idSuffix}").grid("setColProp", "trpmc", {
                "formatoptions": {
                    valueField: 'VALUE',
                    textField: 'TEXT',
                    required: true,
                    enableFilter: true,
                    "data": flTrpmc,
                    onChange: function (e, data) {
                        var rowData =$("#" + e.data.gridId).grid("getRowData", e.data.rowId);
                        var ytdata = $.loadJson($.contextPath + "/sczzscda!getYtByTrpmc.json?trpmc=" + rowData.trpmc + "&lx="+rowData.trplx);
                        $("#" + e.data.gridId).grid("setRowData", e.data.rowId, {
                            yt: ytdata.TEXT
                        });
                    }
                }
            });

            var sfTrps = $.ns("namespaceId${idSuffix}").fapzs.sfTrp[fabh];
            for (var i in  sfTrps) {
                var sfTrp = sfTrps[i];
                var lxData = $.loadJson($.contextPath+"/sczzscda!getFlLxByTym.json?tym="+sfTrp.TRPTYM);
                var sfTrpID = generateId("tmp");//console.log(sfTrpID);
                var trpmcData = $.loadJson($.contextPath+"/sczzscda!getTrpxxByTym.json?tym="+sfTrp.TRPTYM+"&lx="+lxData[0].LX);
//                var sfTrpID = sfTrp.ID;
                $("#scsfTrpGrid${idSuffix}").grid("addRowData", sfTrpID, {
                    nszyxbh: sfTrp.NSZYXBH,
                    trptym: sfTrp.TRPTYM,
                    trplx: lxData[0].LX
                }, "last");
                var trpmc = $("#scsfTrpGrid${idSuffix}").grid("getCellComponent", sfTrpID, "trpmc");
                trpmc.combobox("clear");
                trpmc.combobox("reload", trpmcData);
            }
            //用药
            var yys = $.ns("namespaceId${idSuffix}").fapzs.yy[fabh];
            for (var i in  yys) {
                var yy = yys[i];
                var yyID = generateId("tmp");
//                var yyID = yy.ID;
                $("#scyyGrid${idSuffix}").grid("addRowData", yyID, {
                    qsnsx : yy.IS_START,
                    nszyxbh: yy.NSZYXBH,
                    nszyxmc: yy.NSZYXMC,
                    yysd: yy.YYSD,
                    nsxjgsj: yy.NSXJGSJ,
                    czfdsj: yy.CZFDSJ
                }, "last");
                if(yy.IS_START == 1){
                    $("#scyyGrid${idSuffix}").grid("setRowData", yyID, null, {background: '#68C561'});
                };
            }
            $.ns('namespaceId${idSuffix}').updateDGridBh("scyyGrid${idSuffix}");
            var nyTym = $.loadJson($.contextPath + "/sczzscda!getNyTym.json");
            $("#scyyTrpGrid${idSuffix}").grid("setColProp", "trptym", {
                "formatoptions": {
                    valueField: 'VALUE',
                    textField: 'TEXT',
                    required: true,
                    enableFilter: true,
                    "data": nyTym,
                    onChange: function (e, data) {
                        var $trpmcCombobox = $("#" + e.data.gridId).grid("getCellComponent", e.data.rowId, "trpmc");
                        var rowData =$("#" + e.data.gridId).grid("getRowData", e.data.rowId);
                        var trpmc = $.loadJson($.contextPath + "/sczzscda!getTrpxxByTym.json?lx=" + rowData.trplx+"&tym="+rowData.trptym);
                        $trpmcCombobox.combobox("reload", trpmc);
                        $trpmcCombobox.combobox("setValue", "");
                    }
                }
            });
            var nyTrpmc = $.loadJson($.contextPath + "/sczzscda!getNyTrpmc.json");
            $("#scyyTrpGrid${idSuffix}").grid("setColProp", "trpmc", {
                "formatoptions": {
                    valueField: 'VALUE',
                    textField: 'TEXT',
                    required: true,
                    enableFilter: true,
                    "data": nyTrpmc,
                    onChange: function (e, data) {
                        var rowData =$("#" + e.data.gridId).grid("getRowData", e.data.rowId);
                        var ytdata = $.loadJson($.contextPath + "/sczzscda!getYtByTrpmc.json?trpmc=" + rowData.trpmc + "&lx="+rowData.trplx);
                        $("#" + e.data.gridId).grid("setRowData", e.data.rowId, {
                            yt: ytdata.TEXT
                        });
                    }
                }
            });
            var yyTrps = $.ns("namespaceId${idSuffix}").fapzs.yyTrp[fabh];
            for (var i in yyTrps) {
                var yyTrp = yyTrps[i];
                var lxData = $.loadJson($.contextPath+"/sczzscda!getNyLxByTym.json?tym="+yyTrp.TRPTYM);
                var trpmcData = $.loadJson($.contextPath+"/sczzscda!getTrpxxByTym.json?tym="+yyTrp.TRPTYM+"&lx="+lxData[0].LX);
                var yyTrpID = generateId("tmp");
//                var yyTrpID = yyTrp.ID;
                $("#scyyTrpGrid${idSuffix}").grid("addRowData", yyTrpID, {
                    nszyxbh: yyTrp.NSZYXBH,
                    trptym: yyTrp.TRPTYM,
                    trplx: lxData[0].LX
                }, "last");
                var trpmc = $("#scyyTrpGrid${idSuffix}").grid("getCellComponent", yyTrpID, "trpmc");
                trpmc.combobox("clear");
                trpmc.combobox("reload", trpmcData);
            }
            //采收
            var css = $.ns("namespaceId${idSuffix}").fapzs.cs[fabh];
            for (var i in  css) {
                var cs = css[i];
                var csID = generateId("tmp");
//                var csID = cs.ID;
                $("#sccsGrid${idSuffix}").grid("addRowData", csID, {
                    qsnsx : cs.IS_START,
                    nszyxbh: cs.NSZYXBH,
                    nszyxmc: cs.NSZYXMC,
                    csfs: cs.CSFS,
                    nsxjgsj: cs.NSXJGSJ,
                    czfdsj: cs.CZFDSJ
                }, "last");
                if(cs.IS_START == 1){
                    $("#sccsGrid${idSuffix}").grid("setRowData", csID, null, {background: '#68C561'});
                };
            }
            $.ns('namespaceId${idSuffix}').updateDGridBh("sccsGrid${idSuffix}");
            var csTym = $.loadJson($.contextPath + "/sczzscda!getGgTym.json");
            $("#sccsTrpGrid${idSuffix}").grid("setColProp", "trptym", {
                "formatoptions": {
                    valueField: 'VALUE',
                    textField: 'TEXT',
                    required: true,
                    enableFilter: true,
                    "data": csTym,
                    onChange: function (e, data) {
                        var $trpmcCombobox = $("#" + e.data.gridId).grid("getCellComponent", e.data.rowId, "trpmc");
                        var rowData =$("#" + e.data.gridId).grid("getRowData", e.data.rowId);
                        var trpmc = $.loadJson($.contextPath + "/sczzscda!getTrpxxByTym.json?lx=" + rowData.trplx+"&tym="+rowData.trptym);
                        $trpmcCombobox.combobox("reload", trpmc);
                        $trpmcCombobox.combobox("setValue", "");
                    }
                }
            });
            $("#sccsTrpGrid${idSuffix}").grid("setColProp", "trpmc", {
                "formatoptions": {
                    valueField: 'VALUE',
                    textField: 'TEXT',
                    required: true,
                    enableFilter: true,
                    "data": csTym,
                    onChange: function (e, data) {
                        var rowData =$("#" + e.data.gridId).grid("getRowData", e.data.rowId);
                        var csdata = $.loadJson($.contextPath + "/sczzscda!getYtByTrpmc.json?trpmc=" + rowData.trpmc + "&lx="+rowData.trplx);
                        $("#" + e.data.gridId).grid("setRowData", e.data.rowId, {
                            yt: csdata.TEXT
                        });
                    }
                }
            });
            var csTrps = $.ns("namespaceId${idSuffix}").fapzs.csTrp[fabh];
            for (var i in csTrps) {
                var csTrp = csTrps[i];
                var lxData = $.loadJson($.contextPath+"/sczzscda!getNjjLxByTym.json?bh="+csTrp.TRPTYM);
                var csTrpID = generateId("tmp");
//                var csTrpID = csTrp.ID;
                $("#sccsTrpGrid${idSuffix}").grid("addRowData", csTrpID, {
                    nszyxbh: csTrp.NSZYXBH,
                    trptym: csTrp.TRPTYM,
                    trpmc: csTrp.TRPTYM,
                    trplx: lxData.LX,
                    yt: csTrp.YT
                }, "last");
            }
            //锄草
            var ccs = $.ns("namespaceId${idSuffix}").fapzs.cc[fabh];
            for (var i in  ccs) {
                var cc = ccs[i];
                var ccID = generateId("tmp");
                $("#scccGrid${idSuffix}").grid("addRowData", ccID, {
                    qsnsx : cc.IS_START,
                    nszyxbh: cc.NSZYXBH,
                    nszyxmc: cc.NSZYXMC,
                    ccfs: cc.CCFS,
                    nsxjgsj: cc.NSXJGSJ,
                    czfdsj: cc.CZFDSJ
                }, "last");
                if(cc.IS_START == 1){
                    $("#scccGrid${idSuffix}").grid("setRowData", ccID, null, {background: '#68C561'});
                };
            }
            $.ns('namespaceId${idSuffix}').updateDGridBh("scccGrid${idSuffix}");
            var nyTym = $.loadJson($.contextPath + "/sczzscda!getNyTym.json");
            $("#scccTrpGrid${idSuffix}").grid("setColProp", "trptym", {
                "formatoptions": {
                    valueField: 'VALUE',
                    textField: 'TEXT',
                    required: true,
                    enableFilter: true,
                    "data": nyTym,
                    onChange: function (e, data) {
                        var $trpmcCombobox = $("#" + e.data.gridId).grid("getCellComponent", e.data.rowId, "trpmc");
                        var rowData =$("#" + e.data.gridId).grid("getRowData", e.data.rowId);
                        var trpmc = $.loadJson($.contextPath + "/sczzscda!getTrpxxByTym.json?lx=" + rowData.trplx+"&tym="+rowData.trptym);
                        $trpmcCombobox.combobox("reload", trpmc);
                        $trpmcCombobox.combobox("setValue", "");
                    }
                }
            });
            var nyTrpmc = $.loadJson($.contextPath + "/sczzscda!getNyTrpmc.json");
            $("#scccTrpGrid${idSuffix}").grid("setColProp", "trpmc", {
                "formatoptions": {
                    valueField: 'VALUE',
                    textField: 'TEXT',
                    required: true,
                    enableFilter: true,
                    "data": nyTrpmc,
                    onChange: function (e, data) {
                        var rowData =$("#" + e.data.gridId).grid("getRowData", e.data.rowId);
                        var ytdata = $.loadJson($.contextPath + "/sczzscda!getYtByTrpmc.json?trpmc=" + rowData.trpmc + "&lx="+rowData.trplx);
                        $("#" + e.data.gridId).grid("setRowData", e.data.rowId, {
                            yt: ytdata.TEXT
                        });
                    }
                }
            });
            var ccTrps = $.ns("namespaceId${idSuffix}").fapzs.ccTrp[fabh];
            for (var i in ccTrps) {
                var ccTrp = ccTrps[i];
                var ccTrpID = generateId("tmp");
                var lxData = $.loadJson($.contextPath+"/sczzscda!getNyLxByTym.json?tym="+ccTrp.TRPTYM);
                var trpmcData = $.loadJson($.contextPath+"/sczzscda!getTrpxxByTym.json?tym="+ccTrp.TRPTYM+"&lx="+lxData[0].LX);
                $("#scccTrpGrid${idSuffix}").grid("addRowData", ccTrpID, {
                    nszyxbh: ccTrp.NSZYXBH,
                    trptym: ccTrp.TRPTYM,
                    trplx: lxData[0].LX
                }, "last");
                var trpmc = $("#scccTrpGrid${idSuffix}").grid("getCellComponent", ccTrpID, "trpmc");
                trpmc.combobox("clear");
                trpmc.combobox("reload", trpmcData);
            }
            //其它
            var qts = $.ns("namespaceId${idSuffix}").fapzs.qt[fabh];
            for (var i in  qts) {
                var qt = qts[i];
                var qtID = generateId("tmp");
//                var qtID = qt.ID;
                $("#scqtGrid${idSuffix}").grid("addRowData", qtID, {
                    qsnsx : qt.IS_START,
                    nszyxbh: qt.NSZYXBH,
                    nszyxmc: qt.NSZYXMC,
                    nsxjgsj: qt.NSXJGSJ,
                    czfdsj: qt.CZFDSJ
                }, "last");
                if(qt.IS_START == 1){
                    $("#scqtGrid${idSuffix}").grid("setRowData", qtID, null, {background: '#68C561'});
                };
            }
            $.ns('namespaceId${idSuffix}').updateDGridBh("scqtGrid${idSuffix}");
            var qtTrps = $.ns("namespaceId${idSuffix}").fapzs.qtTrp[fabh];
            for (var i in qtTrps) {
                var qtTrp = qtTrps[i];
                var qtTrpID = generateId("tmp");
//                var qtTrpID = qtTrpID;
                $("#scqtTrpGrid${idSuffix}").grid("addRowData", qtTrpID, {
                    nszyxbh: qtTrp.NSZYXBH,
                    trptym: qtTrp.TRPTYM,
                }, "last");
                var trpmc = $("#scccTrpGrid${idSuffix}").grid("getCellComponent", qtTrpID, "trpmc");
                trpmc.combobox("clear");
                trpmc.combobox("reload", trpmcData);
            }
            //更新调入品通用名
            <%--$.ns("namespaceId${idSuffix}").updateTrptym("scbzTrpGrid${idSuffix}");--%>
            <%--$.ns("namespaceId${idSuffix}").updateTrptym("scggTrpGrid${idSuffix}");--%>
            <%--$.ns("namespaceId${idSuffix}").updateTrptym("scsfTrpGrid${idSuffix}");--%>
            <%--$.ns("namespaceId${idSuffix}").updateTrptym("scyyTrpGrid${idSuffix}");--%>
            <%--&lt;%&ndash;$.ns("namespaceId${idSuffix}").updateTrptym("scjcTrpGrid${idSuffix}");&ndash;%&gt;--%>
            <%--$.ns("namespaceId${idSuffix}").updateTrptym("sccsTrpGrid${idSuffix}");--%>
            <%--$.ns("namespaceId${idSuffix}").updateTrptym("scccTrpGrid${idSuffix}");--%>
            <%--$.ns("namespaceId${idSuffix}").updateTrptym("scqtTrpGrid${idSuffix}");--%>
            <%--//更新投入品名称--%>
            <%--$.ns("namespaceId${idSuffix}").updateTrpmc("scbzTrpGrid${idSuffix}", allTrpmc);--%>
            <%--$.ns("namespaceId${idSuffix}").updateTrpmc("scggTrpGrid${idSuffix}", allTrpmc);--%>
            <%--$.ns("namespaceId${idSuffix}").updateTrpmc("scsfTrpGrid${idSuffix}", allTrpmc);--%>
            <%--$.ns("namespaceId${idSuffix}").updateTrpmc("scyyTrpGrid${idSuffix}", allTrpmc);--%>
            <%--&lt;%&ndash;$.ns("namespaceId${idSuffix}").updateTrpmc("scjcTrpGrid${idSuffix}", allTrpmc);&ndash;%&gt;--%>
            <%--$.ns("namespaceId${idSuffix}").updateTrpmc("sccsTrpGrid${idSuffix}", allTrpmc);--%>
            <%--$.ns("namespaceId${idSuffix}").updateTrpmc("scccTrpGrid${idSuffix}", allTrpmc);--%>
            <%--$.ns("namespaceId${idSuffix}").updateTrpmc("scqtTrpGrid${idSuffix}", allTrpmc);--%>


        },
        validGrid: function (gridId, err1, err2) {//主表验证
            var $grid = $("#" + gridId);
            var gridIds = $grid.grid("getDataIDs");
            if ($.isEmptyObject(gridIds)) {
                CFG_message(err1, "warning");
                return false;
            }
            if (!$grid.grid("valid")) {
                CFG_message(err2, "warning");
                return false;
            }
            return true;
        },
        updateTrpmc: function (trpGrid, allTrpmc) {
            var ids = $("#" + trpGrid).grid("getDataIDs");
            for (var i in ids) {
                var $trpmcCombobox = $("#" + trpGrid).grid("getCellComponent", ids[i], "trpmc");
                var gridData = $("#" + trpGrid).grid("getRowData", ids[i]);
//                var trpmc = $.loadJson($.contextPath + "/sczzscda!getTrpxxByTym.json?tym=" + gridData.trptym);
//                $trpmcCombobox.combobox("reload", trpmc);
//                $trpmcCombobox.combobox("setValue", "");
            }
        },
        updateTrptym: function (trpGrid){
            var ids = $("#" + trpGrid).grid("getDataIDs");
            for (var i in ids) {
                var $trpmcCombobox = $("#" + trpGrid).grid("getCellComponent", ids[i], "trptym");
                var gridData = $("#" + trpGrid).grid("getRowData", ids[i]);
//                var trpmc = $.loadJson($.contextPath + "/sczzscda!getTrptymByLx.json");
//                $trpmcCombobox.combobox("reload", trpmc);
            }
        },
        toolbarClick: function (e, ui) {
            if (ui.id == "addScbz") {//播种新增
                var nextNszyxbh = $.ns('namespaceId${idSuffix}').getNextNszyxbh("scbzGrid${idSuffix}");
                var $grid = $("#scbzGrid${idSuffix}");
                var lastsel = generateId("tmp");
                $grid.grid("addRowData", lastsel, {
                    qsnsx: "0",
                    nszyxbh: nextNszyxbh,
                    czfdsj: 0
                }, "last");
                $.ns('namespaceId${idSuffix}').updateDGridBh("scbzGrid${idSuffix}");
            } else if (ui.id == "addScbzTrp") {//播种投入品新增
                var valid = $.ns('namespaceId${idSuffix}').validGrid("scbzGrid${idSuffix}", "请先添加播种方案信息", "请先将播种方案信息填写完整");
                if (!valid) {
                    return;
                }
                var lastsel = generateId("tmp");
                $("#scbzTrpGrid${idSuffix}").grid("addRowData", lastsel, {}, "last");
            } else if (ui.id == "addScgg") {//灌溉新增
                var nextNszyxbh = $.ns('namespaceId${idSuffix}').getNextNszyxbh("scggGrid${idSuffix}");
                var $grid = $("#scggGrid${idSuffix}");
                var lastsel = generateId("tmp");
                $grid.grid("addRowData", lastsel, {
                    qsnsx: "0",
                    nszyxbh: nextNszyxbh,
                    czfdsj: 0
                }, "last");
                $.ns('namespaceId${idSuffix}').updateDGridBh("scggGrid${idSuffix}");
            } else if (ui.id == "addScggTrp") {//灌溉投入品新增
                var valid = $.ns('namespaceId${idSuffix}').validGrid("scggGrid${idSuffix}", "请先添加灌溉方案信息", "请先将灌溉方案信息填写完整");
                if (!valid) {
                    return;
                }
                var lastsel = generateId("tmp");
                $("#scggTrpGrid${idSuffix}").grid("addRowData", lastsel, {}, "last");
            } else if (ui.id == "addScsf") {//施肥新增
                var nextNszyxbh = $.ns('namespaceId${idSuffix}').getNextNszyxbh("scsfGrid${idSuffix}");
                var $grid = $("#scsfGrid${idSuffix}");
                var lastsel = generateId("tmp");
                $grid.grid("addRowData", lastsel, {
                    qsnsx: "0",
                    nszyxbh: nextNszyxbh,
                    czfdsj: 0
                }, "last");
                $.ns('namespaceId${idSuffix}').updateDGridBh("scsfGrid${idSuffix}");
            } else if (ui.id == "addScsfTrp") {//施肥投入品新增
                var valid = $.ns('namespaceId${idSuffix}').validGrid("scsfGrid${idSuffix}", "请先添加施肥方案信息", "请先将施肥方案信息填写完整");
                if (!valid) {
                    return;
                }
                var lastsel = generateId("tmp");
                $("#scsfTrpGrid${idSuffix}").grid("addRowData", lastsel, {}, "last");
            } else if (ui.id == "addScyy") {//用药新增
                var nextNszyxbh = $.ns('namespaceId${idSuffix}').getNextNszyxbh("scyyGrid${idSuffix}");
                var $grid = $("#scyyGrid${idSuffix}");
                var lastsel = generateId("tmp");
                $grid.grid("addRowData", lastsel, {
                    qsnsx: "0",
                    nszyxbh: nextNszyxbh,
                    czfdsj: 0
                }, "last");
                $.ns('namespaceId${idSuffix}').updateDGridBh("scyyGrid${idSuffix}");
            } else if (ui.id == "addScyyTrp") {//用药投入品新增
                var valid = $.ns('namespaceId${idSuffix}').validGrid("scyyGrid${idSuffix}", "请先添加用药方案信息", "请先将用药方案信息填写完整");
                if (!valid) {
                    return;
                }
                var lastsel = generateId("tmp");
                $("#scyyTrpGrid${idSuffix}").grid("addRowData", lastsel, {}, "last");
            } else if (ui.id == "addScjc") {//检测新增
                var nextNszyxbh = $.ns('namespaceId${idSuffix}').getNextNszyxbh("scjcGrid${idSuffix}");
                var $grid = $("#scjcGrid${idSuffix}");
                var lastsel = generateId("tmp");
                $grid.grid("addRowData", lastsel, {
                    qsnsx: "0",
                    nszyxbh: nextNszyxbh
                }, "last");
                $.ns('namespaceId${idSuffix}').updateDGridBh("scjcGrid${idSuffix}");
            } else if (ui.id == "addScjcTrp") {//检测投入品新增
                var valid = $.ns('namespaceId${idSuffix}').validGrid("scjcGrid${idSuffix}", "请先添加检测方案信息", "请先将检测方案信息填写完整");
                if (!valid) {
                    return;
                }
                var lastsel = generateId("tmp");
                $("#scjcTrpGrid${idSuffix}").grid("addRowData", lastsel, {}, "last");
            } else if (ui.id == "addSccs") {//采收新增
                var nextNszyxbh = $.ns('namespaceId${idSuffix}').getNextNszyxbh("sccsGrid${idSuffix}");
                var $grid = $("#sccsGrid${idSuffix}");
                var lastsel = generateId("tmp");
                $grid.grid("addRowData", lastsel, {
                    qsnsx: "0",
                    nszyxbh: nextNszyxbh,
                    czfdsj: 0
                }, "last");
                $.ns('namespaceId${idSuffix}').updateDGridBh("sccsGrid${idSuffix}");
            } else if (ui.id == "addSccsTrp") {//采收投入品新增
                var valid = $.ns('namespaceId${idSuffix}').validGrid("sccsGrid${idSuffix}", "请先添加采收方案信息", "请先将采收方案信息填写完整");
                if (!valid) {
                    return;
                }
                var lastsel = generateId("tmp");
                $("#sccsTrpGrid${idSuffix}").grid("addRowData", lastsel, {}, "last");
            } else if (ui.id == "addSccc") {//锄草新增
                var nextNszyxbh = $.ns('namespaceId${idSuffix}').getNextNszyxbh("scccGrid${idSuffix}");
                var $grid = $("#scccGrid${idSuffix}");
                var lastsel = generateId("tmp");
                $grid.grid("addRowData", lastsel, {
                    qsnsx: "0",
                    nszyxbh: nextNszyxbh,
                    czfdsj: 0
                }, "last");
                $.ns('namespaceId${idSuffix}').updateDGridBh("scccGrid${idSuffix}");
            } else if (ui.id == "addScccTrp") {//锄草投入品新增
                var valid = $.ns('namespaceId${idSuffix}').validGrid("scccGrid${idSuffix}", "请先添加锄草方案信息", "请先将锄草方案信息填写完整");
                if (!valid) {
                    return;
                }
                var lastsel = generateId("tmp");
                $("#scccTrpGrid${idSuffix}").grid("addRowData", lastsel, {}, "last");
            } else if (ui.id == "addScqt") {//其它新增
                var nextNszyxbh = $.ns('namespaceId${idSuffix}').getNextNszyxbh("scqtGrid${idSuffix}");
                var $grid = $("#scqtGrid${idSuffix}");
                var lastsel = generateId("tmp");
                $grid.grid("addRowData", lastsel, {
                    qsnsx: "0",
                    nszyxbh: nextNszyxbh,
                    czfdsj: 0
                }, "last");
                $.ns('namespaceId${idSuffix}').updateDGridBh("scqtGrid${idSuffix}");
            } else if (ui.id == "addScqtTrp") {//其它投入品新增
                var valid = $.ns('namespaceId${idSuffix}').validGrid("scqtGrid${idSuffix}", "请先添加其它方案信息", "请先将其它方案信息填写完整");
                if (!valid) {
                    return;
                }
                var lastsel = generateId("tmp");
                $("#scqtTrpGrid${idSuffix}").grid("addRowData", lastsel, {}, "last");
            } else if (ui.id == "saveMaster") {//保存方法
                if (!$.ns('namespaceId${idSuffix}').valid()) {
                    return;
                }
                //获取起始农事项时间
                var qsnsxsj = "";
                var $grids = $("#scbzGrid${idSuffix},#scggGrid${idSuffix},#scsfGrid${idSuffix},#scyyGrid${idSuffix},#scjcGrid${idSuffix},#sccsGrid${idSuffix},#scccGrid${idSuffix},#scqtGrid${idSuffix}");
                $grids.each(function () {
                    var $grid = $(this);
                    var value = $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent($grid.grid("option").id).textbox("getValue");
                    if (isNotEmpty(value)) {
                        qsnsxsj = value;
                        return false;
                    }
                });

                var scxx = {
                    <%--ssqybh: $("#ssqybh${idSuffix}").combobox("getValue"),--%>
                    <%--ssqymc: $("#ssqybh${idSuffix}").combobox("getText"),--%>
                    ycmc: $("#ycmc${idSuffix}").textbox("getValue"),
                    ycdm: $("#ycdm${idSuffix}").textbox("getValue"),
                    zmzzmc: $("#zzzmmc${idSuffix}").textbox("getValue"),
                    zmzzbh: $("#zzzmbh${idSuffix}").textbox("getValue"),
                    zzzl: $("#zzzl${idSuffix}").textbox("getValue"),
                    zzly: $("#zzly${idSuffix}").combobox("getValue"),
                    dkbh: $("#dkmc${idSuffix}").combobox("getValue"),
                    dkmc: $("#dkmc${idSuffix}").combobox("getText"),
                    jdbh: $("#jdmc${idSuffix}").combobox("getValue"),
                    jdmc: $("#jdmc${idSuffix}").combobox("getText"),
                    //zzfzrbh: $("#zzfzr${idSuffix}").combobox("getValue"),
                    zzfzr: $("#zzfzr${idSuffix}").textbox("getValue"),
                    dkmj: $("#dkmj${idSuffix}").textbox("getValue"),
                    <%--zzdybh: $("#zzdy${idSuffix}").combobox("getValue"),--%>
                    <%--zzdymc: $("#zzdy${idSuffix}").combobox("getText"),--%>
                    <%--zzdymj: $("#zzdymj${idSuffix}").textbox("getValue"),--%>
                    <%--zzdyglybh: $("#zzdyglybh${idSuffix}").val(),--%>
                    <%--zzdygly: $("#zzdygly${idSuffix}").textbox("getValue"),--%>
                    <%--qyglybh: $("#qyglybh${idSuffix}").val(),--%>
                    <%--qygly: $("#qygly${idSuffix}").textbox("getValue"),--%>
                    <%--plbh: $("#plbh${idSuffix}").val(),--%>
                    <%--pzbh: $("#pzbh${idSuffix}").val(),--%>
                    <%--pl: $("#pl${idSuffix}").textbox("getValue"),--%>
                    <%--pz: $("#pz${idSuffix}").textbox("getValue"),--%>
                    //zzfabh: $("#zzfa${idSuffix}").combobox("getValue"),
                    zzfa: $("#zzfa${idSuffix}").combobox("getText"),
                    zzsj: $("#zzsj${idSuffix}").datepicker("getDateValue"),
                    <%--zzjssj: $("#zzjssj${idSuffix}").datepicker("getDateValue"),--%>
                    <%--trzb: $("#trzb${idSuffix}").textbox("getValue"),--%>
                    qsnsxsj: qsnsxsj,
                    <%--zt:$("#zt${idSuffix}").combobox("getValue")--%>
                };
                //播种信息
                var bzxx = [];
                var scbzIds = $("#scbzGrid${idSuffix}").grid("getDataIDs");
                var scbzTrpIds = $("#scbzTrpGrid${idSuffix}").grid("getDataIDs");
                for (var i in scbzIds) {
                    var scbzRowData = $("#scbzGrid${idSuffix}").grid("getRowData", scbzIds[i]);
                    var bzxxTrp = [];
                    for (var i in scbzTrpIds) {
                        var scbzTrpRowData = $("#scbzTrpGrid${idSuffix}").grid("getRowData", scbzTrpIds[i]);
                        if (scbzRowData.nszyxbh == scbzTrpRowData.nszyxbh) {
                            bzxxTrp.push(scbzTrpRowData);
                        }
                    }
                    bzxx.push({
                        bz: scbzRowData,
                        bzTrp: bzxxTrp
                    });
                }
                //灌溉信息
                var ggxx = [];
                var scggIds = $("#scggGrid${idSuffix}").grid("getDataIDs");
                var scggTrpIds = $("#scggTrpGrid${idSuffix}").grid("getDataIDs");
                for (var i in scggIds) {
                    var scggRowData = $("#scggGrid${idSuffix}").grid("getRowData", scggIds[i]);
                    var ggxxTrp = [];
                    for (var i in scggTrpIds) {
                        var scggTrpRowData = $("#scggTrpGrid${idSuffix}").grid("getRowData", scggTrpIds[i]);
                        if (scggRowData.nszyxbh == scggTrpRowData.nszyxbh) {
                            ggxxTrp.push(scggTrpRowData);
                        }
                    }
                    ggxx.push({
                        gg: scggRowData,
                        ggTrp: ggxxTrp
                    });
                }
                //施肥
                var sfxx = [];
                var scsfIds = $("#scsfGrid${idSuffix}").grid("getDataIDs");
                var scsfTrpIds = $("#scsfTrpGrid${idSuffix}").grid("getDataIDs");
                for (var i in scsfIds) {
                    var scsfRowData = $("#scsfGrid${idSuffix}").grid("getRowData", scsfIds[i]);
                    var sfxxTrp = [];
                    for (var i in scsfTrpIds) {
                        var scsfTrpRowData = $("#scsfTrpGrid${idSuffix}").grid("getRowData", scsfTrpIds[i]);
                        if (scsfRowData.nszyxbh == scsfTrpRowData.nszyxbh) {
                            sfxxTrp.push(scsfTrpRowData);
                        }
                    }
                    sfxx.push({
                        sf: scsfRowData,
                        sfTrp: sfxxTrp
                    });
                }
                //用药
                var yyxx = [];
                var scyyIds = $("#scyyGrid${idSuffix}").grid("getDataIDs");
                var scyyTrpIds = $("#scyyTrpGrid${idSuffix}").grid("getDataIDs");
                for (var i in scyyIds) {
                    var scyyRowData = $("#scyyGrid${idSuffix}").grid("getRowData", scyyIds[i]);
                    var yyxxTrp = [];
                    for (var i in scyyTrpIds) {
                        var scyyTrpRowData = $("#scyyTrpGrid${idSuffix}").grid("getRowData", scyyTrpIds[i]);
                        if (scyyRowData.nszyxbh == scyyTrpRowData.nszyxbh) {
                            yyxxTrp.push(scyyTrpRowData);
                        }
                    }
                    yyxx.push({
                        yy: scyyRowData,
                        yyTrp: yyxxTrp
                    });
                }
                //检测
                <%--var jcxx = [];--%>
                <%--var scjcIds = $("#scjcGrid${idSuffix}").grid("getDataIDs");--%>
                <%--var scjcTrpIds = $("#scjcTrpGrid${idSuffix}").grid("getDataIDs");--%>
                <%--for (var i in scjcIds) {--%>
                    <%--var scjcRowData = $("#scjcGrid${idSuffix}").grid("getRowData", scjcIds[i]);--%>
                    <%--var jcxxTrp = [];--%>
                    <%--for (var i in scjcTrpIds) {--%>
                        <%--var scjcTrpRowData = $("#scjcTrpGrid${idSuffix}").grid("getRowData", scjcTrpIds[i]);--%>
                        <%--if (scjcRowData.nszyxbh == scjcTrpRowData.nszyxbh) {--%>
                            <%--jcxxTrp.push(scjcTrpRowData);--%>
                        <%--}--%>
                    <%--}--%>
                    <%--jcxx.push({--%>
                        <%--jc: scjcRowData,--%>
                        <%--jcTrp: jcxxTrp--%>
                    <%--});--%>
                <%--}--%>
                //采收
                var csxx = [];
                var sccsIds = $("#sccsGrid${idSuffix}").grid("getDataIDs");
                var sccsTrpIds = $("#sccsTrpGrid${idSuffix}").grid("getDataIDs");
                for (var i in sccsIds) {
                    var sccsRowData = $("#sccsGrid${idSuffix}").grid("getRowData", sccsIds[i]);
                    var csxxTrp = [];
                    for (var i in sccsTrpIds) {
                        var sccsTrpRowData = $("#sccsTrpGrid${idSuffix}").grid("getRowData", sccsTrpIds[i]);
                        if (sccsRowData.nszyxbh == sccsTrpRowData.nszyxbh) {
                            csxxTrp.push(sccsTrpRowData);
                        }
                    }
                    csxx.push({
                        cs: sccsRowData,
                        csTrp: csxxTrp
                    });
                }
                //锄草
                var ccxx = [];
                var scccIds = $("#scccGrid${idSuffix}").grid("getDataIDs");
                var scccTrpIds = $("#scccTrpGrid${idSuffix}").grid("getDataIDs");
                for (var i in scccIds) {
                    var scccRowData = $("#scccGrid${idSuffix}").grid("getRowData", scccIds[i]);
                    var ccxxTrp = [];
                    for (var i in scccTrpIds) {
                        var scccTrpRowData = $("#scccTrpGrid${idSuffix}").grid("getRowData", scccTrpIds[i]);
                        if (scccRowData.nszyxbh == scccTrpRowData.nszyxbh) {
                            ccxxTrp.push(scccTrpRowData);
                        }
                    }
                    ccxx.push({
                        cc: scccRowData,
                        ccTrp: ccxxTrp
                    });
                }
                //其它
                var qtxx = [];
                var scqtIds = $("#scqtGrid${idSuffix}").grid("getDataIDs");
                var scqtTrpIds = $("#scqtTrpGrid${idSuffix}").grid("getDataIDs");
                for (var i in scqtIds) {
                    var scqtRowData = $("#scqtGrid${idSuffix}").grid("getRowData", scqtIds[i]);
                    var qtxxTrp = [];
                    for (var i in scqtTrpIds) {
                        var scqtTrpRowData = $("#scqtTrpGrid${idSuffix}").grid("getRowData", scqtTrpIds[i]);
                        if (scqtRowData.nszyxbh == scqtTrpRowData.nszyxbh) {
                            qtxxTrp.push(scqtTrpRowData);
                        }
                    }
                    qtxx.push({
                        qt: scqtRowData,
                        qtTrp: qtxxTrp
                    });
                }
                var data = {
                    bzxx: bzxx,
                    ggxx: ggxx,
                    sfxx: sfxx,
                    yyxx: yyxx,
//                    jcxx: jcxx,
                    csxx: csxx,
                    ccxx: ccxx,
                    qtxx: qtxx,
                    scxx: scxx
                };
                var scdaxx = JSON.stringify(data);
                $.ajax({
                    url: $.contextPath + "/sczzscda!saveScdaxx.json",
                    data: {
                        scdaxx: scdaxx
                    },
                    type: "POST",
                    dataType: "json",
                    async: false,
                    success: function () {
                        CFG_message("保存成功", "success");
                        var configInfo = $("#maxDiv${idSuffix}").data('configInfo');
                        CFG_clickCloseButton(configInfo);
                    },
                    error: function () {
                        CFG_message("保存失败", "error");
                    }
                });
            } else if (ui.id == "return") {//关闭
                var configInfo = $("#maxDiv${idSuffix}").data('configInfo');
                CFG_clickCloseButton(configInfo);
            }
        },
        valid: function () {//保存前验证
            if (!$("#enterForm${idSuffix}").form("valid")) {
                CFG_message("请检查表单信息", "warning");
                return false;
            }
            if (!$("#scbzGrid${idSuffix}").grid("valid")) {
                $("#tabs${idSuffix}").tabs("option", "active", 0);
                CFG_message("请检查播种方案信息", "warning");
                return false;
            }
            if (!$("#scbzTrpGrid${idSuffix}").grid("valid")) {
                $("#tabs${idSuffix}").tabs("option", "active", 0);
                CFG_message("请检查播种方案投入品信息", "warning");
                return false;
            }
            if (!$("#scggGrid${idSuffix}").grid("valid")) {
                $("#tabs${idSuffix}").tabs("option", "active", 1);
                CFG_message("请检查灌溉方案信息", "warning");
                return false;
            }
            if (!$("#scggTrpGrid${idSuffix}").grid("valid")) {
                $("#tabs${idSuffix}").tabs("option", "active", 1);
                CFG_message("请检查灌溉方案投入品信息", "warning");
                return false;
            }
            if (!$("#scsfGrid${idSuffix}").grid("valid")) {
                $("#tabs${idSuffix}").tabs("option", "active", 2);
                CFG_message("请检查施肥方案信息", "warning");
                return false;
            }
            if (!$("#scsfTrpGrid${idSuffix}").grid("valid")) {
                $("#tabs${idSuffix}").tabs("option", "active", 2);
                CFG_message("请检查施肥方案投入品信息", "warning");
                return false;
            }
            if (!$("#scyyGrid${idSuffix}").grid("valid")) {
                $("#tabs${idSuffix}").tabs("option", "active", 3);
                CFG_message("请检查用药方案信息", "warning");
                return false;
            }
            if (!$("#scyyTrpGrid${idSuffix}").grid("valid")) {
                $("#tabs${idSuffix}").tabs("option", "active", 3);
                CFG_message("请检查用药方案投入品信息", "warning");
                return false;
            }
            if (!$("#scjcGrid${idSuffix}").grid("valid")) {
                $("#tabs${idSuffix}").tabs("option", "active", 4);
                CFG_message("请检查检测方案信息", "warning");
                return false;
            }
            if (!$("#scjcTrpGrid${idSuffix}").grid("valid")) {
                $("#tabs${idSuffix}").tabs("option", "active", 4);
                CFG_message("请检查检测方案投入品信息", "warning");
                return false;
            }
            if (!$("#sccsGrid${idSuffix}").grid("valid")) {
                $("#tabs${idSuffix}").tabs("option", "active", 5);
                CFG_message("请检查采收方案信息", "warning");
                return false;
            }
            if (!$("#sccsTrpGrid${idSuffix}").grid("valid")) {
                $("#tabs${idSuffix}").tabs("option", "active", 5);
                CFG_message("请检查采收方案投入品信息", "warning");
                return false;
            }
            if (!$("#scccGrid${idSuffix}").grid("valid")) {
                $("#tabs${idSuffix}").tabs("option", "active", 4);
                CFG_message("请检查锄草方案信息", "warning");
                return false;
            }
            if (!$("#scccTrpGrid${idSuffix}").grid("valid")) {
                $("#tabs${idSuffix}").tabs("option", "active", 4);
                CFG_message("请检查锄草方案投入品信息", "warning");
                return false;
            }
            if (!$("#scqtGrid${idSuffix}").grid("valid")) {
                $("#tabs${idSuffix}").tabs("option", "active", 6);
                CFG_message("请检查其它方案信息", "warning");
                return false;
            }
            if (!$("#scqtTrpGrid${idSuffix}").grid("valid")) {
                $("#tabs${idSuffix}").tabs("option", "active", 6);
                CFG_message("请检查其它方案投入品信息", "warning");
                return false;
            }
            //验证是否有起始农事项
            var hasQsnsx = false;
            <%--var $grids = $("#scbzGrid${idSuffix},#scggGrid${idSuffix},#scsfGrid${idSuffix},#scyyGrid${idSuffix},#scjcGrid${idSuffix},#sccsGrid${idSuffix},#scccGrid${idSuffix},#scqtGrid${idSuffix}");--%>
            <%--$grids.each(function () {--%>
                <%--var $grid = $(this);--%>
                <%--var gridIds = $grid.grid("getDataIDs");--%>
                <%--for (var i in gridIds) {--%>
                    <%--var gridData = $grid.grid("getRowData", gridIds[i]);--%>
                    <%--if (gridData.qsnsx == "1") {--%>
                        <%--hasQsnsx = true;--%>
                        <%--return false;--%>
                    <%--}--%>
                <%--}--%>
            <%--});--%>

            //获取起始农事项时间
            var qsnsxsj = "";
            var $grids = $("#scbzGrid${idSuffix},#scggGrid${idSuffix},#scsfGrid${idSuffix},#scyyGrid${idSuffix},#scjcGrid${idSuffix},#sccsGrid${idSuffix},#scccGrid${idSuffix},#scqtGrid${idSuffix}");
            $grids.each(function () {
                var $grid = $(this);
                var value = $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent($grid.grid("option").id).textbox("getValue");
                if (isNotEmpty(value)) {
                    qsnsxsj = value;
                    hasQsnsx = true;
                    return false;
                }
            });

            if (!hasQsnsx) {
                CFG_message("请设置起始农事项", "warning");
                return false;
            }
            return true;
        },
        updateDGridBh: function (gridId) {//更新子表编号
            var $grid = $("#" + gridId);
            var $dGrid = $.ns('namespaceId${idSuffix}').getDetailGrid(gridId);
            var gridIds = $grid.grid("getDataIDs");
            var dGridIds = $dGrid.grid("getDataIDs");
            var zxybhs = [];
            var zxybhsArray = [];
            for (var i in gridIds) {
                var gridData = $grid.grid("getRowData", gridIds[i]);
                zxybhs.push({
                    value: gridData.nszyxbh,
                    text: gridData.nszyxbh
                });
                zxybhsArray.push(gridData.nszyxbh);
            }
            $dGrid.grid("setColProp", "nszyxbh", {"formatoptions": {required: true, "data": zxybhs}});
            for (var i in dGridIds) {
                var dGridData = $dGrid.grid("getRowData", dGridIds[i]);
                if ($.inArray(dGridData.nszyxbh, zxybhsArray) == -1) {
                    if (dGridData.nszyxbh == "") {
                        $dGrid.grid("getCellComponent", dGridIds[i], "nszyxbh").combobox("reload", zxybhs);
                        return;
                    }
                    $dGrid.grid("delRowData", dGridIds[i]);
                } else {
                    $dGrid.grid("getCellComponent", dGridIds[i], "nszyxbh").combobox("reload", zxybhs);
                }
            }
        },
        getDetailGrid: function (gridId) {//获取子表
            return $("#" + gridId).parents("div[name='tabBorderDiv']").find(".ctrl-init-grid:last");
        },
        getToolBarQsnsxsjComponent: function (gridId) {//获取关联的工具条
            return $("#" + gridId).parents("div[name='tabBorderDiv']").find(".ctrl-init-toolbar:first").toolbar("getSubCoral", "qsnsxsjlabel").$el;
        },
        getNextNszyxbh: function (gridId) {//获取新的农事项编号
            var $grid = $("#" + gridId);
            var gridIds = $grid.grid("getDataIDs");
            if ($.isEmptyObject(gridIds)) {
                return 1;
            }
            var gridData = $grid.grid("getRowData", gridIds[gridIds.length - 1]);
            return parseInt(gridData.nszyxbh) + 1;
        },
        gridClick: function (e, ui) {
            var edatagridId = $(this).attr("gridId");
            var edatarowId = $(this).attr("rowId");
            if (ui.id == "delGridData") {
                $.confirm("确定删除", function (r) {
                    if (r) {
                        var gridData = $("#" + edatagridId).grid("getRowData", edatarowId);
                        if (gridData.qsnsx == "1") {//是起始农事项则删除起始时间
                            $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent(edatagridId).textbox("setValue", "");
                        }
                        deleteDataId = edatarowId;
                        $("#" + edatagridId).grid("delRowData", edatarowId);
                        $.ns('namespaceId${idSuffix}').updateDGridBh(edata.ridId);
                    }
                });
            } else if (ui.id == "delDetailGridData") {
                $.confirm("确定删除", function (r) {
                    if (r) {
                        $("#" + e.data.gridId).grid("delRowData", e.data.rowId);
                    }
                });
            } else if (ui.id == "setFirst") {//设为起始农事项
                var valid = $.ns('namespaceId${idSuffix}').validGrid(edatagridId, "请先添加信息", "请先将信息填写完整");
                if (!valid) {
                    return;
                }
                $.ns('namespaceId${idSuffix}').showQsnsxsjDialog(edatagridId, edatarowId);
            }
        },
        showQsnsxsjDialog: function (gridId, rowId) {
            $("#jqUC").remove();
            var jqGlobal = $("body");
            var jqUC = $("<div id=\"jqUC\"></div>").appendTo(jqGlobal);
            jqUC.dialog({
                appendTo : jqGlobal,
                modal : true,
                title : "请设置起始农事项时间",
                width : 280,
                height : 120,
                resizable : false,
                position : {
                    at: "center top+200"
                },
                onClose : function() {
                    jqUC.remove();
                    jqUC.remove();
                },
                onCreate : function() {
                    var jqDiv = $("<div class=\"app-inputdiv-full\" style=\"padding:10px 20px;\"></div>").appendTo(this);
                    var jq = $("<input id=\"qsnsxdialogDate${idSuffix}\" name=\"opinion\">").appendTo(jqDiv);
                    jq.textbox({width: 200});
                },
                onOpen : function() {
                    $("#qsnsxdialogDate${idSuffix}").datepicker({
                    required: true
                    });
                    var jqPanel = $(this).dialog("buttonPanel").addClass("app-bottom-toolbar"),
                            jqDiv   = $("<div class=\"dialog-toolbar\">").appendTo(jqPanel);
                    jqDiv.toolbar({
                        data: ["->", {id:"sure", label:"确定", type:"button"}, {id:"cancel", label:"取消", type:"button"}],
                        onClick: function(e, ui) {
                            if ("sure" === ui.id) {
                                if (!$("#qsnsxdialogDate${idSuffix}").datepicker("valid")) {
                                CFG_message("请先填写时间", "warning");
                                return;
                                }
                                $.ns('namespaceId${idSuffix}').clearFirst();
                                $("#" + gridId).grid("setRowData", rowId, {
                                qsnsx: "1"
                                });
                                //TODO 高亮行
                                $("#" + gridId).grid("setRowData", rowId, null, {background: '#68C561'});
                                var $grids = $("#scbzGrid${idSuffix},#scggGrid${idSuffix},#scsfGrid${idSuffix},#scyyGrid${idSuffix},#scjcGrid${idSuffix},#sccsGrid${idSuffix},#scccGrid${idSuffix},#scqtGrid${idSuffix}");
                                $.each($grids,function(e, data){
                                    var _this = this;
                                    $.each($(this).grid("getDataIDs"),function(e, data){
                                        $(_this).grid("getCellComponent",  data, "NSXJGSJ").textbox({readonly:false});
                                    });
                                });
                                var $nsxjgsjTextbox = $("#" + gridId).grid("getCellComponent",  rowId, "nsxjgsj");
                                $nsxjgsjTextbox.textbox("setValue","0");
                                $nsxjgsjTextbox.textbox({readonly:true});
                                $grids.grid("option", "rowattr", $.ns('namespaceId${idSuffix}').rowAttr);
                                //设置toolbar文本框
                                var gridIds = ["scbzGrid${idSuffix}","scggGrid${idSuffix}","scsfGrid${idSuffix}","scyyGrid${idSuffix}","scccGrid${idSuffix}","sccsGrid${idSuffix}","scqtGrid${idSuffix}"];
                                $.each(gridIds, function(e, data){
                                    $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent(data).textbox("setValue", $("#qsnsxdialogDate${idSuffix}").datepicker("getValue"));
                                });
                                $("#qsnsxdialog${idSuffix}").remove();
                            }
                            jqUC.remove();
                        }
                    });
                }
            });


            <%--$("#qsnsxdialog${idSuffix}").remove();--%>
            <%--$("body").append("<div id='qsnsxdialog${idSuffix}'><div><label>起始农事项时间:</label><input id='qsnsxdialogDate${idSuffix}'></div><div><input id='dialogConfirm${idSuffix}'><input id='dialogCancel${idSuffix}'></div></div>");--%>
            <%--$("#qsnsxdialog${idSuffix}").dialog({--%>
                <%--width: 400,--%>
                <%--height: 200,--%>
                <%--onOpen: function () {--%>
                    <%--$("#qsnsxdialogDate${idSuffix}").datepicker({--%>
                        <%--required: true--%>
                    <%--});--%>
                    <%--$("#dialogConfirm${idSuffix}").button({--%>
                        <%--label: "确定",--%>
                        <%--width: 80,--%>
                        <%--onClick: function () {--%>
                            <%--if (!$("#qsnsxdialogDate${idSuffix}").datepicker("valid")) {--%>
                                <%--CFG_message("请先填写时间", "warning");--%>
                                <%--return;--%>
                            <%--}--%>
                            <%--$.ns('namespaceId${idSuffix}').clearFirst();--%>
                            <%--$("#" + gridId).grid("setRowData", rowId, {--%>
                                <%--qsnsx: "1"--%>
                            <%--});--%>
                            <%--//TODO 高亮行--%>
                            <%--$("#" + gridId).grid("setRowData", rowId, null, {background: '#68C561'});--%>
                            <%--var $grids = $("#scbzGrid${idSuffix},#scggGrid${idSuffix},#scsfGrid${idSuffix},#scyyGrid${idSuffix},#scjcGrid${idSuffix},#sccsGrid${idSuffix},#scccGrid${idSuffix},#scqtGrid${idSuffix}");--%>
                            <%--$grids.grid("option", "rowattr", $.ns('namespaceId${idSuffix}').rowAttr);--%>
                            <%--//设置toolbar文本框--%>
                            <%--$.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent(gridId).textbox("setValue", $("#qsnsxdialogDate${idSuffix}").datepicker("getValue"));--%>
                            <%--$("#qsnsxdialog${idSuffix}").remove();--%>
                        <%--}--%>
                    <%--});--%>
                    <%--$("#dialogCancel${idSuffix}").button({--%>
                        <%--label: "取消",--%>
                        <%--width: 80,--%>
                        <%--onClick: function () {--%>
                            <%--$("#qsnsxdialog${idSuffix}").remove();--%>
                        <%--}--%>
                    <%--});--%>
                <%--},--%>
                <%--onClose: function () {--%>
                    <%--$("#qsnsxdialog${idSuffix}").remove();--%>
                <%--}--%>
            <%--});--%>
            <%--$("#qsnsxdialog${idSuffix}").dialog("open");--%>
        },
        clearFirst: function () {//清除已有的起始农事项
            var $grids = $("#scbzGrid${idSuffix},#scggGrid${idSuffix},#scsfGrid${idSuffix},#scyyGrid${idSuffix},#scjcGrid${idSuffix},#sccsGrid${idSuffix},#scccGrid${idSuffix},#scqtGrid${idSuffix}");
            $grids.each(function () {
                $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent($(this).grid("option").id).textbox("setValue", "");
                var dataIds = $(this).grid("getDataIDs");
                for (var i in dataIds) {
                    $(this).grid("setRowData", dataIds[i], {
                        qsnsx: "0"
                    });
                    $(this).grid("setRowData", dataIds[i], null, {background: ""});
                }
            });
        },
        rowAttr: function (o) {//表格行属性
            if (o.rowData.qsnsx == "1") {
                return {"style": "background:#F0EFB5"};
            }
        },
        updateTabsHeight: function(){
            if($("#scbz${idSuffix}").css("height") != "483px"){
                $.each($("#tabs${idSuffix}").children(),function(i,data){
                    if(0 != i){
                        $(this).css("height","483px");
                    }
                });
            }
            setTimeout($.ns("namespaceId${idSuffix}").updateTabsHeight,1000);
        }

    });

    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'zzscdaxz.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#maxDiv${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone': function () {
            },
            /** 初始化预留区 */
            'initReserveZones': function (configInfo) {
                <%--CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', 0);--%>
            },
            /** 获取返回按钮添加的位置 */
            'setReturnButton': function (configInfo) {
                <%--CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));--%>
            },
            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {
                $.ns('namespaceId${idSuffix}').initform();
                $("#scbzToolbarId${idSuffix}").toolbar("refresh");
                setTimeout($.ns("namespaceId${idSuffix}").updateTabsHeight,1000);
//				var flag = CFG_getSelfParamValue(configInfo, 'selfParam2');
                $("#zt${idSuffix}").combobox("setValue",'1');
                <%--var zmzzObj = $.loadJson($.contextPath + "/sczzscda!getZzzm.json");--%>
                <%--$('#zmzzbh${idSuffix}').combobox('reload',zmzzObj);--%>
                //种植地块
                var dkObj = $.loadJson($.contextPath + "/sczzscda!getDkxx.json");
                $('#dkmc${idSuffix}').combobox('reload',dkObj);
                <%--$(dkmc${idSuffix}).combobox({--%>
                <%--url: $.contextPath + "/sczzscda!getDkxx.json"--%>
                <%--})--%>
                <%--var zzfzrObj = $.loadJson($.contextPath + "/sczzscda!getZzfzr.json");--%>
                <%--$('#zzfzr${idSuffix}').textbox('reload',zzfzrObj);--%>
                var jdObj = $.loadJson($.contextPath + "/sczzscda!getJdxx.json");
                $('#jdmc${idSuffix}').combobox('reload',jdObj);
                //样式调整
                $("input[id=qsnsxsjlabel]").parent().css("border","0");
                $("input[id=qsnsxsjlabel]").parent().parent().unbind();
                $("input[id=qsnsxsjlabel]").parent().parent().css("margin-top","3px");
            }
        });
        if (configInfo) {
            //alert("系统参数：\t" + "关联的系统参数=" + CFG_getSystemParamValue(configInfo, 'systemParam1')
            //		+ "\n构件自身参数：\t" + "selfParam1=" + CFG_getSelfParamValue(configInfo, 'selfParam1')
            //		+ "\n构件入参：\t" + "inputParamName_1=" + CFG_getInputParamValue(configInfo, 'inputParamName_1'));
        }
        // 设置输出参数
        // configInfo.CFG_outputParams.xxx = '';


    });
</script>
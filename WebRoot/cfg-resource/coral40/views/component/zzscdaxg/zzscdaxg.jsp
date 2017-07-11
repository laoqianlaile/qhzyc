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
        <ces:layoutRegion region="north" split="true" style="height:295px;" title="生产方案">
            <div class="config-form toolbarsnav clearfix">
            <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                         data="['->',{'label': '保存', 'id':'saveMaster', 'cls':'save_tb','disabled': 'false','type': 'button'},{'label': '关闭', 'id':'return', 'cls':'return_tb','disabled': 'false','type': 'button'}]">
            </ces:toolbar>
            </div>
            <div id="enterFormDiv${idSuffix}" style="margin-left: 20px;overflow: auto;height: 235px; ">
                <form id="enterForm${idSuffix}" enctype="multipart/form-data" method="post" class="coralui-form">
                    <div class="fillwidth colspan3 clearfix">
                        <div class="app-inputdiv4">
                            <input id="scdaid${idSuffix}" type="hidden" class="coralui-textbox" name="scdaid">
                            <label class="app-input-label" style="width:36%;">所属区域:</label>
                            <input id="ssqybh${idSuffix}" class="coralui-combobox" name="ssqybh"
                                   data-options="valueField:'QYBH',textField:'QYMC',required:true">
                        </div>
                        <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">地块:</label>
                            <input id="dk${idSuffix}" class="coralui-combobox" name="dk"
                                   data-options="valueField:'DKBH',textField:'DKMC',required:true">
                        </div>
                        <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">种植单元:</label>
                            <input id="zzdy${idSuffix}" class="coralui-combobox" name="zzdy"
                                   data-options="valueField:'ZZDYBH',textField:'ZZDYMC',required:true">
                        </div>
                        <input type="hidden" id="qyglybh${idSuffix}">

                        <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">区域管理员:</label>
                            <input id="qygly${idSuffix}" name="qygly" class="coralui-textbox"
                                   data-options="readonly:true"/>
                        </div>
                        <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">地块面积:</label>
                            <input id="dkmj${idSuffix}" name="dkmj" class="coralui-textbox"
                                   data-options="readonly:true,buttons:[{innerRight:[{label:'亩'}]}]"/>
                        </div>
                        <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">种植单元面积:</label>
                            <input id="zzdymj${idSuffix}" name="zzdymj" class="coralui-textbox"
                                   data-options="readonly:true,required:true,buttons:[{innerRight:[{label:'亩'}]}]"/>
                        </div>
                        <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">种植方案:</label>
                            <input id="zzfa${idSuffix}" class="coralui-combobox" name="zzfa"
                                   data-options="valueField:'ZZFABH',textField:'ZZFAMC',required:true">
                        </div>
                        <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">种植时间:</label>
                            <input id="zzsj${idSuffix}" class="coralui-datepicker" name="zzsj"
                                   data-options="required:true">
                        </div>
                        <input type="hidden" id="zzdyglybh${idSuffix}">

                        <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">种植单元管理员:</label>
                            <input id="zzdygly${idSuffix}" name="zzdygly" class="coralui-textbox"
                                   data-options="readonly:true"/>
                        </div>
                        <input id="plbh${idSuffix}" name="plbh" type="hidden">
                        <input id="pzbh${idSuffix}" name="pzbh" type="hidden">

                        <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">品类:</label>
                            <input id="pl${idSuffix}" class="coralui-textbox" name="pl"
                                   data-options="required:true,readonly:true">
                        </div>
                        <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">品种:</label>
                            <input id="pz${idSuffix}" class="coralui-textbox" name="pz"
                                   data-options="required:true,readonly:true">
                        </div>
                        <div class="app-inputdiv12">
                            <label class="app-input-label" style="width:12%;">土壤指标:</label>
                            <textarea id="trzb${idSuffix}" class="coralui-textbox" name="trzb"/>
                        </div>
                    </div>
                </form>
            </div>
        </ces:layoutRegion>
        <ces:layoutRegion region="center" split="true" style="height:295px;" cls="small-grid">
            <%--<div><input type="button" onclick="test133()" value="收起"/></div>--%>
            <div id="tabs${idSuffix}" class="coralui-tabs" data-options="heightStyle:'fill'" style="display: none;">
                <ul>
                    <li><a href="#scbz${idSuffix}">播种方案</a></li>
                    <li><a href="#scgg${idSuffix}">灌溉方案</a></li>
                    <li><a href="#scsf${idSuffix}">施肥方案</a></li>
                    <li><a href="#scyy${idSuffix}">用药方案</a></li>
                    <li><a href="#scjc${idSuffix}">检测方案</a></li>
                    <li><a href="#sccs${idSuffix}">采收</a></li>
                    <li><a href="#sccc${idSuffix}">锄草</a></li>
                    <li><a href="#scqt${idSuffix}">其它</a></li>
                </ul>
                <!-- 播种 -->
                <div id="scbz${idSuffix}" name="tabBorderDiv">
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScbz', 'disabled': 'false','type': 'button'},{'type': 'textbox','isLabel': 'true','value':'起始农事项时间'},{'type': 'textbox','id': 'qsnsxsjlabel','readonly': 'true'}]">
                    </ces:toolbar>
                    <div id="scbzGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scbzGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill"
                                  cellEdit="true">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="QSNSX" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="text" formatoptions="required: true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="NSZYXMC" formatter="text" formatoptions="required: true"
                                             width="80">农事作业项名称</ces:gridCol>
                                <ces:gridCol name="ZPFS" formatter="combobox" formatoptions="required: true"
                                             revertCode="true"
                                             width="80">栽培方式</ces:gridCol>
                                <ces:gridCol name="NSXJGSJ" formatter="text"
                                             formatoptions="required: true,validType: 'integer'"
                                             width="80">农事项间隔时间</ces:gridCol>
                                <ces:gridCol name="CZFDSJ" formatter="text"
                                             formatoptions="required: true,validType: 'naturalnumber'"
                                             sorttype="naturalnumber" width="80">操作浮动时间</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="190" formatter="toolbar"
                                             formatoptions="isOverflow:false,onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '设为起始农事项', 'id':'setFirst', 'disabled': 'false','type': 'button'},{'label': '删除', 'id':'delGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScbzTrp', 'disabled': 'false','type': 'button'}]">
                    </ces:toolbar>
                    <div id="scbzTrpGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scbzTrpGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="combobox" formatoptions="required: true"
                                             revertCode="true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="TRPLX" formatter="combobox" width="80">投入品类型</ces:gridCol>
                                <ces:gridCol name="TRPTYM" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品通用名</ces:gridCol>
                                <ces:gridCol name="TRPMC" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品名称</ces:gridCol>
                                <ces:gridCol name="YT" width="80">用途</ces:gridCol>
                                <ces:gridCol name="GG" width="80">规格</ces:gridCol>
                                <ces:gridCol name="YL" formatter="text" formatoptions="required: true"
                                             width="80">用量（千克/每亩）</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"
                                             formatoptions="onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delDetailGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                </div>
                <!-- 灌溉 -->
                <div id="scgg${idSuffix}" name="tabBorderDiv">
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScgg', 'disabled': 'false','type': 'button'},{'type': 'textbox','id': 'qsnsxsjlabel','readonly': 'true'}]">
                    </ces:toolbar>
                    <div id="scggGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scggGrid${idSuffix}" fitStyle="fill" height="auto" rownumbers="true">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="QSNSX" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="text" formatoptions="required: true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="NSZYXMC" formatter="text" formatoptions="required: true"
                                             width="80">农事作业项名称</ces:gridCol>
                                <ces:gridCol name="GGFS" revertCode="true" sortable="true" width="100"
                                             formatter="combobox" formatoptions="required: true">灌溉方式</ces:gridCol>
                                <ces:gridCol name="SYLX" revertCode="true" width="80" formatter="combobox"
                                             formatoptions="required: true">水源类型</ces:gridCol>
                                <ces:gridCol name="SYDJ" revertCode="true" width="80" formatter="combobox"
                                             formatoptions="required: true">水源等级</ces:gridCol>
                                <ces:gridCol name="NSXJGSJ" formatter="text"
                                             formatoptions="required: true,validType: 'integer'"
                                             width="80">农事项间隔时间</ces:gridCol>
                                <ces:gridCol name="CZFDSJ" formatter="text"
                                             formatoptions="required: true,validType: 'naturalnumber'"
                                             sorttype="naturalnumber" width="80">操作浮动时间</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="190" formatter="toolbar"
                                             formatoptions="isOverflow:false,onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '设为起始农事项', 'id':'setFirst', 'disabled': 'false','type': 'button'},{'label': '删除', 'id':'delGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScggTrp', 'disabled': 'false','type': 'button'}]">
                    </ces:toolbar>
                    <div id="scggTrpGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scggTrpGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="combobox" formatoptions="required: true"
                                             revertCode="true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="TRPLX" formatter="combobox" width="80">投入品类型</ces:gridCol>
                                <ces:gridCol name="TRPTYM" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品通用名</ces:gridCol>
                                <ces:gridCol name="TRPMC" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品名称</ces:gridCol>
                                <ces:gridCol name="YT" width="80">用途</ces:gridCol>
                                <ces:gridCol name="GG" width="80">规格</ces:gridCol>
                                <ces:gridCol name="YL" formatter="text" formatoptions="required: true"
                                             width="80">用量（千克/每亩）</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"
                                             formatoptions="onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delDetailGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                </div>
                <!-- 施肥 -->
                <div id="scsf${idSuffix}" name="tabBorderDiv">
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScsf', 'disabled': 'false','type': 'button'},{'type': 'textbox','id': 'qsnsxsjlabel','readonly': 'true'}]">
                    </ces:toolbar>
                    <div id="scsfGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scsfGrid${idSuffix}" fitStyle="fill" height="auto" rownumbers="true">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="QSNSX" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="text" formatoptions="required: true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="NSZYXMC" formatter="text" formatoptions="required: true"
                                             width="80">农事作业项名称</ces:gridCol>
                                <ces:gridCol name="SFSD" revertCode="true" sortable="true" width="100"
                                             formatter="combobox" formatoptions="required: true">施肥时段</ces:gridCol>
                                <ces:gridCol name="NSXJGSJ" formatter="text"
                                             formatoptions="required: true,validType: 'integer'"
                                             width="80">农事项间隔时间</ces:gridCol>
                                <ces:gridCol name="CZFDSJ" formatter="text"
                                             formatoptions="required: true,validType: 'naturalnumber'"
                                             sorttype="naturalnumber" width="80">操作浮动时间</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="190" formatter="toolbar"
                                             formatoptions="isOverflow:false,onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '设为起始农事项', 'id':'setFirst', 'disabled': 'false','type': 'button'},{'label': '删除', 'id':'delGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScsfTrp', 'disabled': 'false','type': 'button'}]">
                    </ces:toolbar>
                    <div id="scsfTrpGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scsfTrpGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="combobox" formatoptions="required: true"
                                             revertCode="true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="TRPLX" formatter="combobox" width="80">投入品类型</ces:gridCol>
                                <ces:gridCol name="TRPTYM" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品通用名</ces:gridCol>
                                <ces:gridCol name="TRPMC" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品名称</ces:gridCol>
                                <ces:gridCol name="YT" width="80">用途</ces:gridCol>
                                <ces:gridCol name="GG" width="80">规格</ces:gridCol>
                                <ces:gridCol name="YL" formatter="text" formatoptions="required: true"
                                             width="80">用量（千克/每亩）</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"
                                             formatoptions="onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delDetailGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                </div>
                <!-- 用药 -->
                <div id="scyy${idSuffix}" name="tabBorderDiv">
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScyy', 'disabled': 'false','type': 'button'},{'type': 'textbox','id': 'qsnsxsjlabel','readonly': 'true'}]">
                    </ces:toolbar>
                    <div id="scyyGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scyyGrid${idSuffix}" fitStyle="fill" height="auto" rownumbers="true">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="QSNSX" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="text" formatoptions="required: true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="NSZYXMC" formatter="text" formatoptions="required: true"
                                             width="80">农事作业项名称</ces:gridCol>
                                <ces:gridCol name="YYSD" formatter="combobox" formatoptions="required: true"
                                             revertCode="true" width="80">用药时段</ces:gridCol>
                                <ces:gridCol name="NSXJGSJ" formatter="text"
                                             formatoptions="required: true,validType: 'integer'"
                                             width="80">农事项间隔时间</ces:gridCol>
                                <ces:gridCol name="CZFDSJ" formatter="text"
                                             formatoptions="required: true,validType: 'naturalnumber'"
                                             sorttype="naturalnumber" width="80">操作浮动时间</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="190" formatter="toolbar"
                                             formatoptions="isOverflow:false,onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '设为起始农事项', 'id':'setFirst', 'disabled': 'false','type': 'button'},{'label': '删除', 'id':'delGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScyyTrp', 'disabled': 'false','type': 'button'}]">
                    </ces:toolbar>
                    <div id="scyyTrpGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scyyTrpGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="combobox" formatoptions="required: true"
                                             revertCode="true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="TRPLX" formatter="combobox" width="80">投入品类型</ces:gridCol>
                                <ces:gridCol name="TRPTYM" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品通用名</ces:gridCol>
                                <ces:gridCol name="TRPMC" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品名称</ces:gridCol>
                                <ces:gridCol name="YT" width="80">用途</ces:gridCol>
                                <ces:gridCol name="GG" width="80">规格</ces:gridCol>
                                <ces:gridCol name="YL" formatter="text" formatoptions="required: true"
                                             width="80">用量（千克/每亩）</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"
                                             formatoptions="onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delDetailGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                </div>
                <!-- 检测 -->
                <div id="scjc${idSuffix}" name="tabBorderDiv">
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScjc', 'disabled': 'false','type': 'button'},{'type': 'textbox','id': 'qsnsxsjlabel','readonly': 'true'}]">
                    </ces:toolbar>
                    <div id="scjcGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scjcGrid${idSuffix}" fitStyle="fill" height="auto" rownumbers="true">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="QSNSX" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="text" formatoptions="required: true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="NSZYXMC" formatter="text" formatoptions="required: true"
                                             width="80">农事作业项名称</ces:gridCol>
                                <ces:gridCol name="JCFF" formatter="combobox" formatoptions="required: true"
                                             revertCode="true" width="100">检测方法</ces:gridCol>
                                <ces:gridCol name="JCDW" width="80" formatter="text"
                                             formatoptions="required: true">检测单位</ces:gridCol>
                                <ces:gridCol name="NSXJGSJ" formatter="text"
                                             formatoptions="required: true,validType: 'integer'"
                                             width="80">农事项间隔时间</ces:gridCol>
                                <ces:gridCol name="CZFDSJ" formatter="text"
                                             formatoptions="required: true,validType: 'naturalnumber'"
                                             sorttype="naturalnumber" width="80">操作浮动时间</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="190" formatter="toolbar"
                                             formatoptions="isOverflow:false,onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '设为起始农事项', 'id':'setFirst', 'disabled': 'false','type': 'button'},{'label': '删除', 'id':'delGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScjcTrp', 'disabled': 'false','type': 'button'}]">
                    </ces:toolbar>
                    <div id="scjcTrpGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scjcTrpGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="combobox" formatoptions="required: true"
                                             revertCode="true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="TRPLX" formatter="combobox" width="80">投入品类型</ces:gridCol>
                                <ces:gridCol name="TRPTYM" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品通用名</ces:gridCol>
                                <ces:gridCol name="TRPMC" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品名称</ces:gridCol>
                                <ces:gridCol name="YT" width="80">用途</ces:gridCol>
                                <ces:gridCol name="GG" width="80">规格</ces:gridCol>
                                <ces:gridCol name="YL" formatter="text" formatoptions="required: true"
                                             width="80">用量（千克/每亩）</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"
                                             formatoptions="onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delDetailGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                </div>
                <!-- 采收 -->
                <div id="sccs${idSuffix}" name="tabBorderDiv">
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addSccs', 'disabled': 'false','type': 'button'},{'type': 'textbox','id': 'qsnsxsjlabel','readonly': 'true'}]">
                    </ces:toolbar>
                    <div id="sccsGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="sccsGrid${idSuffix}" fitStyle="fill" height="auto" rownumbers="true">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="QSNSX" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="text" formatoptions="required: true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="NSZYXMC" formatter="text" formatoptions="required: true"
                                             width="80">农事作业项名称</ces:gridCol>
                                <ces:gridCol name="CSFS" formatter="combobox" formatoptions="required: true"
                                             revertCode="true" width="100">采收方式</ces:gridCol>
                                <ces:gridCol name="NSXJGSJ" formatter="text"
                                             formatoptions="required: true,validType: 'integer'"
                                             width="80">农事项间隔时间</ces:gridCol>
                                <ces:gridCol name="CZFDSJ" formatter="text"
                                             formatoptions="required: true,validType: 'naturalnumber'"
                                             sorttype="naturalnumber" width="80">操作浮动时间</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="190" formatter="toolbar"
                                             formatoptions="isOverflow:false,onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '设为起始农事项', 'id':'setFirst', 'disabled': 'false','type': 'button'},{'label': '删除', 'id':'delGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addSccsTrp', 'disabled': 'false','type': 'button'}]">
                    </ces:toolbar>
                    <div id="sccsTrpGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="sccsTrpGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="combobox" formatoptions="required: true"
                                             revertCode="true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="TRPLX" formatter="combobox" width="80">投入品类型</ces:gridCol>
                                <ces:gridCol name="TRPTYM" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品通用名</ces:gridCol>
                                <ces:gridCol name="TRPMC" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品名称</ces:gridCol>
                                <ces:gridCol name="YT" width="80">用途</ces:gridCol>
                                <ces:gridCol name="GG" width="80">规格</ces:gridCol>
                                <ces:gridCol name="YL" formatter="text" formatoptions="required: true"
                                             width="80">用量（千克/每亩）</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"
                                             formatoptions="onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delDetailGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                </div>
                <!-- 锄草 -->
                <div id="sccc${idSuffix}" name="tabBorderDiv">
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addSccc', 'disabled': 'false','type': 'button'},{'type': 'textbox','id': 'qsnsxsjlabel','readonly': 'true'}]">
                    </ces:toolbar>
                    <div id="scccGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scccGrid${idSuffix}" fitStyle="fill" height="auto" rownumbers="true">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="QSNSX" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="text" formatoptions="required: true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="NSZYXMC" formatter="text" formatoptions="required: true"
                                             width="80">农事作业项名称</ces:gridCol>
                                <ces:gridCol name="CCFS" formatter="combobox" formatoptions="required: true"
                                             revertCode="true" width="100">锄草方式</ces:gridCol>
                                <ces:gridCol name="NSXJGSJ" formatter="text"
                                             formatoptions="required: true,validType: 'integer'"
                                             width="80">农事项间隔时间</ces:gridCol>
                                <ces:gridCol name="CZFDSJ" formatter="text"
                                             formatoptions="required: true,validType: 'naturalnumber'"
                                             sorttype="naturalnumber" width="80">操作浮动时间</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="190" formatter="toolbar"
                                             formatoptions="isOverflow:false,onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '设为起始农事项', 'id':'setFirst', 'disabled': 'false','type': 'button'},{'label': '删除', 'id':'delGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScccTrp', 'disabled': 'false','type': 'button'}]">
                    </ces:toolbar>
                    <div id="scccTrpGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scccTrpGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="combobox" formatoptions="required: true"
                                             revertCode="true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="TRPLX" formatter="combobox" width="80">投入品类型</ces:gridCol>
                                <ces:gridCol name="TRPTYM" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品通用名</ces:gridCol>
                                <ces:gridCol name="TRPMC" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品名称</ces:gridCol>
                                <ces:gridCol name="YT" width="80">用途</ces:gridCol>
                                <ces:gridCol name="GG" width="80">规格</ces:gridCol>
                                <ces:gridCol name="YL" formatter="text" formatoptions="required: true"
                                             width="80">用量（千克/每亩）</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"
                                             formatoptions="onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delDetailGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                </div>
                <!-- 其它 -->
                <div id="scqt${idSuffix}" name="tabBorderDiv">
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScqt', 'disabled': 'false','type': 'button'},{'type': 'textbox','id': 'qsnsxsjlabel','readonly': 'true'}]">
                    </ces:toolbar>
                    <div id="scqtGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scqtGrid${idSuffix}" fitStyle="fill" height="auto" rownumbers="true">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="QSNSX" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="text" formatoptions="required: true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="NSZYXMC" formatter="text" formatoptions="required: true"
                                             width="80">农事作业项名称</ces:gridCol>
                                <ces:gridCol name="NSXJGSJ" formatter="text"
                                             formatoptions="required: true,validType: 'integer'"
                                             width="80">农事项间隔时间</ces:gridCol>
                                <ces:gridCol name="CZFDSJ" formatter="text"
                                             formatoptions="required: true,validType: 'naturalnumber'"
                                             sorttype="naturalnumber" width="80">操作浮动时间</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="190" formatter="toolbar"
                                             formatoptions="isOverflow:false,onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '设为起始农事项', 'id':'setFirst', 'disabled': 'false','type': 'button'},{'label': '删除', 'id':'delGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '添加', 'id':'addScqtTrp', 'disabled': 'false','type': 'button'}]">
                    </ces:toolbar>
                    <div id="scqtTrpGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scqtTrpGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="combobox" formatoptions="required: true"
                                             revertCode="true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="TRPLX" formatter="combobox" width="80">投入品类型</ces:gridCol>
                                <ces:gridCol name="TRPTYM" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品通用名</ces:gridCol>
                                <ces:gridCol name="TRPMC" formatter="combobox" formatoptions="required: true"
                                             width="80">投入品名称</ces:gridCol>
                                <ces:gridCol name="YT" width="80">用途</ces:gridCol>
                                <ces:gridCol name="GG" width="80">规格</ces:gridCol>
                                <ces:gridCol name="YL" formatter="text" formatoptions="required: true"
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
        initform: function (id) {//初始化表单信息
            //种植时间
            <%--$("#zzsj${idSuffix}").datepicker("setDate", new Date());--%>
            //区域信息
            <%--$("#ssqybh${idSuffix}").combobox("option", "onChange", function (e, ui) {--%>
            <%--var qybh = ui.value;--%>
            <%--for (var i in $.ns("namespaceId${idSuffix}").qyxxs) {--%>
            <%--var qyxx = $.ns("namespaceId${idSuffix}").qyxxs[i];--%>
            <%--if (qyxx.QYBH == qybh) {--%>
            <%--$("#qygly${idSuffix}").textbox("setValue", qyxx.FZR);--%>
            <%--$("#qyglybh${idSuffix}").val(qyxx.FZRBH);--%>
            <%--break;--%>
            <%--}--%>
            <%--}--%>
            <%--var dks = $.loadJson($.contextPath + "/sczzscda!getDkxxByQybh.json?qybh=" + qybh);--%>
            <%--$.ns("namespaceId${idSuffix}").dkxxs = dks;--%>
            <%--$("#dk${idSuffix}").combobox("reload", dks);--%>
            <%--//清空地块信息和种植单元信息--%>
            <%--$("#dk${idSuffix}").combobox("setValue", "");--%>
            <%--$("#zzdy${idSuffix}").combobox("setValue", "");--%>
            <%--$.ns("namespaceId${idSuffix}").zzdyxxs = null;--%>
            <%--});--%>
            //地块信息
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
            $("#scbzGrid${idSuffix}").grid("setColProp", "ZPFS", {"formatoptions": {required: true, "data": zpfs}});
            //灌溉方案灌溉方式
            var ggfs = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=GGFS");
            $("#scggGrid${idSuffix}").grid("setColProp", "GGFS", {"formatoptions": {required: true, "data": ggfs}});
            //灌溉方案水源类型
            var sylx = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=SYLX");
            $("#scggGrid${idSuffix}").grid("setColProp", "SYLX", {"formatoptions": {required: true, "data": sylx}});
            //灌溉方案水源等级
            var sydj = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=SYDJ");
            $("#scggGrid${idSuffix}").grid("setColProp", "SYDJ", {"formatoptions": {required: true, "data": sydj}});
            //施肥方案施肥时段
            var sfsd = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=ZZSFSD");
            $("#scsfGrid${idSuffix}").grid("setColProp", "SFSD", {"formatoptions": {required: true, "data": sfsd}});
            //用药时段
            var yysd = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=ZZYYSD");
            $("#scyyGrid${idSuffix}").grid("setColProp", "YYSD", {"formatoptions": {required: true, "data": yysd}});
            //检测检测方法
            var jcff = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=ZZJCFF");
            $("#scjcGrid${idSuffix}").grid("setColProp", "JCFF", {"formatoptions": {required: true, "data": jcff}});
            //采收方式
            var csfs = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=ZZSCCSFS");
            $("#sccsGrid${idSuffix}").grid("setColProp", "CSFS", {"formatoptions": {required: true, "data": csfs}});
            //锄草方式
            var ccfs = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=ZZSCCCFS");
            $("#scccGrid${idSuffix}").grid("setColProp", "CCFS", {"formatoptions": {required: true, "data": ccfs}});
            //投入品类型、通用名、名称
            var allTrplx = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=ZZTRPLX");
            var allTrptym = $.loadJson($.contextPath + "/sczzscda!getAllTrptym.json");
            var allTrpmc = $.loadJson($.contextPath + "/sczzscda!getAllTrpxx.json");
            var $detailGrids = $("#scbzTrpGrid${idSuffix},#scggTrpGrid${idSuffix},#scsfTrpGrid${idSuffix},#scyyTrpGrid${idSuffix},#scjcTrpGrid${idSuffix},#sccsTrpGrid${idSuffix},#scccTrpGrid${idSuffix},#scqtTrpGrid${idSuffix}");
            $detailGrids.grid("setColProp", "TRPLX", {
                "formatoptions": {
                    "data": allTrplx, onChange: function (e, data) {
                        var $trptymCombobox = $("#" + e.data.gridId).grid("getCellComponent", e.data.rowId, "TRPTYM");
                        var trptym = $.loadJson($.contextPath + "/sczzscda!getTrptymByLx.json?lx=" + data.value);
                        $trptymCombobox.combobox("reload", trptym);
                        $trptymCombobox.combobox("setValue", "");
                        $("#" + e.data.gridId).grid("getCellComponent", e.data.rowId, "TRPMC").combobox("setValue", "");
                    }
                }
            });
            $detailGrids.grid("setColProp", "TRPTYM", {
                "formatoptions": {
                    valueField: 'VALUE',
                    textField: 'TEXT',
                    required: true,
                    "data": allTrptym,
                    onChange: function (e, data) {
                        var $trpmcCombobox = $("#" + e.data.gridId).grid("getCellComponent", e.data.rowId, "TRPMC");
                        var trpmc = $.loadJson($.contextPath + "/sczzscda!getTrpxxByTym.json?tym=" + data.value);
                        $trpmcCombobox.combobox("reload", trpmc);
                        $trpmcCombobox.combobox("setValue", "");
                    }
                }
            });
            $detailGrids.grid("setColProp", "TRPMC", {
                "formatoptions": {
                    valueField: 'TRPBH',
                    textField: 'TRPMC',
                    required: true,
                    "data": allTrpmc,
                    onChange: function (e, data) {
                        for (var i in allTrpmc) {
                            if (allTrpmc[i].TRPBH == data.value) {
                                $("#" + e.data.gridId).grid("setRowData", e.data.rowId, {
                                    YT: allTrpmc[i].YT,
                                    GG: allTrpmc[i].BZGG
                                });
                                break;
                            }
                        }
                    }
                }
            });
            //种植方案信息
            this.fapzs = $.loadJson($.contextPath + "/sczzscda!getZzfapzxx.json");
            $("#zzfa${idSuffix}").combobox("reload", this.fapzs.fapzs);
            //方案变化的同时，初始化列表的信息
            <%--$("#zzfa${idSuffix}").combobox("option", "onChange", function (e, ui) {--%>
            <%--$("#tabs${idSuffix}").show();--%>
            <%--//清空所有列表的数据，刷新列表--%>
            <%--var $allGrids = $("#scbzGrid${idSuffix},#scbzTrpGrid${idSuffix},#scggGrid${idSuffix},#scggTrpGrid${idSuffix},#scsfGrid${idSuffix},#scsfTrpGrid${idSuffix},#scyyGrid${idSuffix},#scyyTrpGrid${idSuffix},#scjcGrid${idSuffix},#scjcTrpGrid${idSuffix}");--%>
            <%--$allGrids.grid("clearGridData");--%>
            <%--$allGrids.grid("refresh");--%>
            <%--$.ns("namespaceId${idSuffix}").initData(ui.value);--%>
            <%--});--%>
            /*************************************修改时加载数据 begin*************************************/
            var updateData = $.loadJson($.contextPath + "/sczzscda!getScdaxxById.json?rowId=" + id);
            /*****************加载生产档案数据 begin**************************/
            $("#scdaid${idSuffix}").textbox("setValue", id);
            $("#ssqybh${idSuffix}").combobox("setValue", updateData.scda.SSQYBH);
            $("#dk${idSuffix}").combobox("setValue", updateData.scda.DKBH);
            $("#zzdy${idSuffix}").combobox("setValue", updateData.scda.ZZDYBH);
            $("#qygly${idSuffix}").textbox("setValue", updateData.scda.QYGLY);
            $("#dkmj${idSuffix}").textbox("setValue", updateData.scda.DKMJ);
            $("#zzdymj${idSuffix}").textbox("setValue", updateData.scda.ZZDYMJ);
            $("#zzfa${idSuffix}").combobox("setValue", updateData.scda.ZZFABH);
            $("#zzsj${idSuffix}").datepicker("setValue", updateData.scda.ZZSJ);
            $("#zzdygly${idSuffix}").textbox("setValue", updateData.scda.ZZDYGLY);
            $("#pl${idSuffix}").textbox("setValue", updateData.scda.PL);
            $("#pz${idSuffix}").textbox("setValue", updateData.scda.PZ);
            $("#trzb${idSuffix}").textbox("setValue", updateData.scda.TRZB);
            $("#scdaid${idSuffix},#qygly${idSuffix},#dkmj${idSuffix},#zzdymj${idSuffix},#zzdygly${idSuffix},#pl${idSuffix},#pz${idSuffix},#trzb${idSuffix}").textbox({
                readonly: true
            });
            $("#ssqybh${idSuffix},#dk${idSuffix},#zzdy${idSuffix},#zzfa${idSuffix}").combobox({
                readonly: true
            });
            $("#zzsj${idSuffix}").datepicker("option", "readonly", "true");

            $("#tabs${idSuffix}").show();
            //清空所有列表的数据，刷新列表
            var $allGrids = $("#scbzGrid${idSuffix},#scbzTrpGrid${idSuffix},#scggGrid${idSuffix},#scggTrpGrid${idSuffix},#scsfGrid${idSuffix},#scsfTrpGrid${idSuffix},#scyyGrid${idSuffix},#scyyTrpGrid${idSuffix},#scjcGrid${idSuffix},#sccsTrpGrid${idSuffix},#scccTrpGrid${idSuffix},#scqtTrpGrid${idSuffix}");
            $allGrids.grid("clearGridData");
            $allGrids.grid("refresh");
            $.ns("namespaceId${idSuffix}").initData($("#zzfa${idSuffix}").combobox("getValue"));

            /*****************加载生产播种 begin**************************/
            $.each(updateData.scbz, function (e, data) {
                $("#scbzGrid${idSuffix}").grid("addRowData", data.ID, data);
                if (data.QSNSX == 1) {
                    $("#scbzGrid${idSuffix}").grid("setRowData", data.ID, null, {background: '#68C561'});
                    $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent("scbzGrid${idSuffix}").textbox("setValue", updateData.qsnsxsj);
                }
                if(data.CZSJ != null){
                    $.ns('namespaceId${idSuffix}').serGridReadOnly("#scbzGrid${idSuffix}", data.ID);
                }
                $.ns('namespaceId${idSuffix}').updateDGridBh("scbzGrid${idSuffix}");
            });
            $.each(updateData.scbztrp, function (e, data) {
                $("#scbzTrpGrid${idSuffix}").grid("addRowData", data.ID, data);
            });
            /*****************加载生产灌溉 begin**************************/
            $.each(updateData.scgg, function (e, data) {
                $("#scggGrid${idSuffix}").grid("addRowData", data.ID, data);
                if (data.QSNSX == 1) {
                    $("#scggGrid${idSuffix}").grid("setRowData", data.ID, null, {background: '#68C561'});
                    $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent("scggGrid${idSuffix}").textbox("setValue", updateData.qsnsxsj);
                }
                if(data.CZSJ != null){
                    $.ns('namespaceId${idSuffix}').serGridReadOnly("#scggGrid${idSuffix}", data.ID);
                }
                $.ns('namespaceId${idSuffix}').updateDGridBh("scggGrid${idSuffix}");
            });
            $.each(updateData.scggtrp, function (e, data) {
                $("#scggTrpGrid${idSuffix}").grid("addRowData", data.ID, data);
            });
            /*****************加载生产施肥 begin**************************/
            $.each(updateData.scsf, function (e, data) {
                $("#scsfGrid${idSuffix}").grid("addRowData", data.ID, data);
                if (data.QSNSX == 1) {
                    $("#scsfGrid${idSuffix}").grid("setRowData", data.ID, null, {background: '#68C561'});
                    $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent("scsfGrid${idSuffix}").textbox("setValue", updateData.qsnsxsj);
                }
                if(data.CZSJ != null){
                    $.ns('namespaceId${idSuffix}').serGridReadOnly("#scsfGrid${idSuffix}", data.ID);
                }
                $.ns('namespaceId${idSuffix}').updateDGridBh("scsfGrid${idSuffix}");
            });
            $.each(updateData.scsftrp, function (e, data) {
                $("#scsfTrpGrid${idSuffix}").grid("addRowData", data.ID, data);
            });
            /*****************加载生产用药 begin**************************/
            $.each(updateData.scyy, function (e, data) {
                $("#scyyGrid${idSuffix}").grid("addRowData", data.ID, data);
                if (data.QSNSX == 1) {
                    $("#scyyGrid${idSuffix}").grid("setRowData", data.ID, null, {background: '#68C561'});
                    $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent("scyyGrid${idSuffix}").textbox("setValue", updateData.qsnsxsj);
                }
                if(data.CZSJ != null){
                    $.ns('namespaceId${idSuffix}').serGridReadOnly("#scyyGrid${idSuffix}", data.ID);
                }
                $.ns('namespaceId${idSuffix}').updateDGridBh("scyyGrid${idSuffix}");
            });
            $.each(updateData.scyytrp, function (e, data) {
                $("#scyyTrpGrid${idSuffix}").grid("addRowData", data.ID, data);
            });
            /*****************加载生产检测 begin**************************/
            $.each(updateData.scjc, function (e, data) {
                $("#scjcGrid${idSuffix}").grid("addRowData", data.ID, data);
                if (data.QSNSX == 1) {
                    $("#scjcGrid${idSuffix}").grid("setRowData", data.ID, null, {background: '#68C561'});
                    $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent("scjcGrid${idSuffix}").textbox("setValue", updateData.qsnsxsj);
                }
                if(data.CZSJ != null){
                    $.ns('namespaceId${idSuffix}').serGridReadOnly("#scjcGrid${idSuffix}", data.ID);
                }
                $.ns('namespaceId${idSuffix}').updateDGridBh("scjcGrid${idSuffix}");
            });
            $.each(updateData.scjctrp, function (e, data) {
                $("#scjcTrpGrid${idSuffix}").grid("addRowData", data.ID, data);
            });
            /*****************加载生产采收 begin**************************/
            $.each(updateData.sccs, function (e, data) {
                $("#sccsGrid${idSuffix}").grid("addRowData", data.ID, data);
                if (data.QSNSX == 1) {
                    $("#sccsGrid${idSuffix}").grid("setRowData", data.ID, null, {background: '#68C561'});
                    $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent("sccsGrid${idSuffix}").textbox("setValue", updateData.qsnsxsj);
                }
                if(data.CZSJ != null){
                    $.ns('namespaceId${idSuffix}').serGridReadOnly("#sccsGrid${idSuffix}", data.ID);
                }
                $.ns('namespaceId${idSuffix}').updateDGridBh("sccsGrid${idSuffix}");
            });
            $.each(updateData.sccstrp, function (e, data) {
                $("#sccsTrpGrid${idSuffix}").grid("addRowData", data.ID, data);
            });
            /*****************加载生产锄草 begin**************************/
            $.each(updateData.sccc, function (e, data) {
                $("#scccGrid${idSuffix}").grid("addRowData", data.ID, data);
                if (data.QSNSX == 1) {
                    $("#scccGrid${idSuffix}").grid("setRowData", data.ID, null, {background: '#68C561'});
                    $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent("scccGrid${idSuffix}").textbox("setValue", updateData.qsnsxsj);
                }
                if(data.CZSJ != null){
                    $.ns('namespaceId${idSuffix}').serGridReadOnly("#scccGrid${idSuffix}", data.ID);
                }
                $.ns('namespaceId${idSuffix}').updateDGridBh("scccGrid${idSuffix}");
            });
            $.each(updateData.sccctrp, function (e, data) {
                $("#scccTrpGrid${idSuffix}").grid("addRowData", data.ID, data);
            });
            /*****************加载生产其它 begin**************************/
            $.each(updateData.scqt, function (e, data) {
                $("#scqtGrid${idSuffix}").grid("addRowData", data.ID, data);
                if (data.QSNSX == 1) {
                    $("#scqtGrid${idSuffix}").grid("setRowData", data.ID, null, {background: '#68C561'});
                    $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent("scqtGrid${idSuffix}").textbox("setValue", updateData.qsnsxsj);
                }
                if(data.CZSJ != null){
                    $.ns('namespaceId${idSuffix}').serGridReadOnly("#scqtGrid${idSuffix}", data.ID);
                }
                $.ns('namespaceId${idSuffix}').updateDGridBh("scqtGrid${idSuffix}");
            });
            $.each(updateData.scqttrp, function (e, data) {
                $("#scqtTrpGrid${idSuffix}").grid("addRowData", data.ID, data);
            });


            /**************************************修改时加载数据 end**************************************/
        },
        initData: function (fabh) {
            //初始化所有的列表信息
            //品类，品种
            for (var i in $.ns("namespaceId${idSuffix}").fapzs.fapzs) {
                var originFapz = $.ns("namespaceId${idSuffix}").fapzs.fapzs[i];
                if (originFapz.ZZFABH == fabh) {
                    $("#pl${idSuffix}").textbox("setValue", originFapz.PL);
                    $("#plbh${idSuffix}").val(originFapz.PLBH);
                    $("#pz${idSuffix}").textbox("setValue", originFapz.PZ);
                    $("#pzbh${idSuffix}").val(originFapz.PZBH);
                }
            }
            /*
             //播种
             var bzs = $.ns("namespaceId
            ${idSuffix}").fapzs.bz[fapz];
             for (var i in  bzs) {
             var bz = bzs[i];
             $("#scbzGrid
            ${idSuffix}").grid("addRowData", bz.ID, {
             scbzGridPl : bz.PL,
             scbzGridPz : bz.PZ,
             scbzGridPzbh : bz.PZBH,
             scbzGridZpfs : bz.ZPFS,
             scbzGridYl : bz.YL,
             fabh : fapz
             }, "last");
             }
             //灌溉
             var ggs = $.ns("namespaceId
            ${idSuffix}").fapzs.gg[fapz];
             for (var i in  ggs) {
             var gg = ggs[i];
             $("#scggGrid
            ${idSuffix}").grid("addRowData", gg.ID, {
             scggGridGgfs : gg.GGFS,
             scggGridSylx : gg.SYLX,
             scggGridSydj : gg.SYDJ,
             fabh : fapz
             }, "last");
             }
             //施肥
             var sfs = $.ns("namespaceId
            ${idSuffix}").fapzs.sf[fapz];
             for (var i in  sfs) {
             var sf = sfs[i];
             $("#scsfGrid
            ${idSuffix}").grid("addRowData", sf.ID, {
             scsfGridSfsd : sf.SFSD,
             scsfGridFlmc : sf.FLMC,
             scsfGridFllx : sf.FLLX,
             scsfGridYl : sf.YL,
             fabh : fapz
             }, "last");
             var $scsfGridFlmcCombobox = $("#scsfGrid
            ${idSuffix}").grid("getCellComponent", sf.ID, "scsfGridFlmc");
             var flmc = $.loadJson($.contextPath + "/sczzscda!getTrpflxx.json?fllx="+sf.FLLX);
             $scsfGridFlmcCombobox.combobox("reload", flmc);
             }
             //用药
             var yys = $.ns("namespaceId
            ${idSuffix}").fapzs.yy[fapz];
             for (var i in  yys) {
             var yy = yys[i];
             $("#scyyGrid
            ${idSuffix}").grid("addRowData", yy.ID, {
             scyyGridYymc : yy.YYMC,
             scyyGridFzdx : yy.GG,
             scyyGridGg : yy.FZDX,
             scyyGridYl : yy.YL,
             fabh : fapz
             }, "last");
             }
             //检测
             var jcs = $.ns("namespaceId
            ${idSuffix}").fapzs.jc[fapz];
             for (var i in  jcs) {
             var jc = jcs[i];
             $("#scjcGrid
            ${idSuffix}").grid("addRowData", jc.ID, {
             scjcGridJcff : jc.JCFF,
             scjcGridCyff : jc.CYFF,
             scjcGridJcdw : jc.JCDW,
             fabh : fapz
             }, "last");
             }
             */
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
        toolbarClick: function (e, ui) {
            if (ui.id == "addScbz") {//播种新增
                var nextNszyxbh = $.ns('namespaceId${idSuffix}').getNextNszyxbh("scbzGrid${idSuffix}");
                var $grid = $("#scbzGrid${idSuffix}");
                var lastsel = generateId("tmp");
                $grid.grid("addRowData", lastsel, {
                    QSNSX: "0",
                    NSZYXBH: nextNszyxbh
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
                    QSNSX: "0",
                    NSZYXBH: nextNszyxbh
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
                    QSNSX: "0",
                    NSZYXBH: nextNszyxbh
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
                    QSNSX: "0",
                    NSZYXBH: nextNszyxbh
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
                    QSNSX: "0",
                    NSZYXBH: nextNszyxbh
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
                    QSNSX: "0",
                    NSZYXBH: nextNszyxbh
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
                    QSNSX: "0",
                    NSZYXBH: nextNszyxbh
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
                    QSNSX: "0",
                    NSZYXBH: nextNszyxbh
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
                    ssqybh: $("#ssqybh${idSuffix}").combobox("getValue"),
                    ssqymc: $("#ssqybh${idSuffix}").combobox("getText"),
                    dkbh: $("#dk${idSuffix}").combobox("getValue"),
                    dkmc: $("#dk${idSuffix}").combobox("getText"),
                    dkmj: $("#dkmj${idSuffix}").textbox("getValue"),
                    zzdybh: $("#zzdy${idSuffix}").combobox("getValue"),
                    zzdymc: $("#zzdy${idSuffix}").combobox("getText"),
                    zzdymj: $("#zzdymj${idSuffix}").textbox("getValue"),
                    zzdyglybh: $("#zzdyglybh${idSuffix}").val(),
                    zzdygly: $("#zzdygly${idSuffix}").textbox("getValue"),
                    qyglybh: $("#qyglybh${idSuffix}").val(),
                    qygly: $("#qygly${idSuffix}").textbox("getValue"),
                    plbh: $("#plbh${idSuffix}").val(),
                    pzbh: $("#pzbh${idSuffix}").val(),
                    pl: $("#pl${idSuffix}").textbox("getValue"),
                    pz: $("#pz${idSuffix}").textbox("getValue"),
                    zzfabh: $("#zzfa${idSuffix}").combobox("getValue"),
                    zzfa: $("#zzfa${idSuffix}").combobox("getText"),
                    zzsj: $("#zzsj${idSuffix}").datepicker("getDateValue"),
                    trzb: $("#trzb${idSuffix}").textbox("getValue"),
                    ID: $("#scdaid${idSuffix}").textbox("getValue"),
                    qsnsxsj: qsnsxsj
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
                        if (scbzRowData.NSZYXBH == scbzTrpRowData.NSZYXBH) {
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
                        if (scggRowData.NSZYXBH == scggTrpRowData.NSZYXBH) {
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
                        if (scsfRowData.NSZYXBH == scsfTrpRowData.NSZYXBH) {
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
                        if (scyyRowData.NSZYXBH == scyyTrpRowData.NSZYXBH) {
                            yyxxTrp.push(scyyTrpRowData);
                        }
                    }
                    yyxx.push({
                        yy: scyyRowData,
                        yyTrp: yyxxTrp
                    });
                }
                //检测
                var jcxx = [];
                var scjcIds = $("#scjcGrid${idSuffix}").grid("getDataIDs");
                var scjcTrpIds = $("#scjcTrpGrid${idSuffix}").grid("getDataIDs");
                for (var i in scjcIds) {
                    var scjcRowData = $("#scjcGrid${idSuffix}").grid("getRowData", scjcIds[i]);
                    var jcxxTrp = [];
                    for (var i in scjcTrpIds) {
                        var scjcTrpRowData = $("#scjcTrpGrid${idSuffix}").grid("getRowData", scjcTrpIds[i]);
                        if (scjcRowData.NSZYXBH == scjcTrpRowData.NSZYXBH) {
                            jcxxTrp.push(scjcTrpRowData);
                        }
                    }
                    jcxx.push({
                        jc: scjcRowData,
                        jcTrp: jcxxTrp
                    });
                }
                //采收
                var csxx = [];
                var sccsIds = $("#sccsGrid${idSuffix}").grid("getDataIDs");
                var sccsTrpIds = $("#sccsTrpGrid${idSuffix}").grid("getDataIDs");
                for (var i in sccsIds) {
                    var sccsRowData = $("#sccsGrid${idSuffix}").grid("getRowData", sccsIds[i]);
                    var csxxTrp = [];
                    for (var i in sccsTrpIds) {
                        var sccsTrpRowData = $("#sccsTrpGrid${idSuffix}").grid("getRowData", sccsTrpIds[i]);
                        if (sccsRowData.NSZYXBH == sccsTrpRowData.NSZYXBH) {
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
                        if (scccRowData.NSZYXBH == scccTrpRowData.NSZYXBH) {
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
                        if (scqtRowData.NSZYXBH == scqtTrpRowData.NSZYXBH) {
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
                    jcxx: jcxx,
                    csxx: csxx,
                    ccxx: ccxx,
                    qtxx: qtxx,
                    scxx: scxx
                };
                $.ajax({
                    url: $.contextPath + "/sczzscda!updateScdaxx.json",
                    type: "POST",
                    dataType: "json",
                    async: false,
                    data: {
                        scdaxx: JSON.stringify(data)
                    },
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
                $("#tabs${idSuffix}").tabs("option", "active", 6);
                CFG_message("请检查锄草方案信息", "warning");
                return false;
            }
            if (!$("#scccTrpGrid${idSuffix}").grid("valid")) {
                $("#tabs${idSuffix}").tabs("option", "active", 6);
                CFG_message("请检查锄草方案投入品信息", "warning");
                return false;
            }
            if (!$("#scqtGrid${idSuffix}").grid("valid")) {
                $("#tabs${idSuffix}").tabs("option", "active", 7);
                CFG_message("请检查其它方案信息", "warning");
                return false;
            }
            if (!$("#scqtTrpGrid${idSuffix}").grid("valid")) {
                $("#tabs${idSuffix}").tabs("option", "active", 7);
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
                    <%--if (gridData.QSNSX == "1") {--%>
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
                    value: gridData.NSZYXBH,
                    text: gridData.NSZYXBH
                });
                zxybhsArray.push(gridData.NSZYXBH);
            }
            $dGrid.grid("setColProp", "NSZYXBH", {"formatoptions": {required: true, "data": zxybhs}});
            for (var i in dGridIds) {
                var dGridData = $dGrid.grid("getRowData", dGridIds[i]);
                if ($.inArray(dGridData.NSZYXBH, zxybhsArray) == -1) {
                    if (dGridData.NSZYXBH == "") {
                        return;
                    }
                    $dGrid.grid("delRowData", dGridIds[i]);
                } else {
                    $dGrid.grid("getCellComponent", dGridIds[i], "NSZYXBH").combobox("reload", zxybhs);
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
            return parseInt(gridData.NSZYXBH) + 1;
        },
        gridClick: function (e, ui) {
            if (ui.id == "delGridData") {
                $.confirm("确定删除", function (r) {
                    if (r) {
                        var gridData = $("#" + e.data.gridId).grid("getRowData", e.data.rowId);
                        if (gridData.QSNSX == "1") {//是起始农事项则删除起始时间
                            $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent(e.data.gridId).textbox("setValue", "");
                        }
                        deleteDataId = e.data.rowId;
                        $("#" + e.data.gridId).grid("delRowData", e.data.rowId);
                        $.ns('namespaceId${idSuffix}').updateDGridBh(e.data.gridId);
                    }
                });
            } else if (ui.id == "delDetailGridData") {
                $.confirm("确定删除", function (r) {
                    if (r) {
                        $("#" + e.data.gridId).grid("delRowData", e.data.rowId);
                    }
                });
            } else if (ui.id == "setFirst") {//设为起始农事项
                var valid = $.ns('namespaceId${idSuffix}').validGrid(e.data.gridId, "请先添加信息", "请先将信息填写完整");
                if (!valid) {
                    return;
                }
                $.ns('namespaceId${idSuffix}').showQsnsxsjDialog(e.data.gridId, e.data.rowId);
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
                                    QSNSX: "1"
                                });
                                //TODO 高亮行
                                $("#" + gridId).grid("setRowData", rowId, null, {background: '#68C561'});
                                var $grids = $("#scbzGrid${idSuffix},#scggGrid${idSuffix},#scsfGrid${idSuffix},#scyyGrid${idSuffix},#scjcGrid${idSuffix},#sccsGrid${idSuffix},#scccGrid${idSuffix},#scqtGrid${idSuffix}");
                                $grids.grid("option", "rowattr", $.ns('namespaceId${idSuffix}').rowAttr);
                                //设置toolbar文本框
                                $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent(gridId).textbox("setValue", $("#qsnsxdialogDate${idSuffix}").datepicker("getValue"));
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
                                <%--QSNSX: "1"--%>
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
                        QSNSX: "0"
                    });
                    $(this).grid("setRowData", dataIds[i], null, {background: ""});
                }
            });
        },
        rowAttr: function (o) {//表格行属性
            if (o.rowData.QSNSX == "1") {
                return {"style": "background:#F0EFB5"};
            }
        },
        serGridReadOnly: function(gridId,rowId){
            var $grid = $(gridId);
            //公有字段
            var $item = $grid.grid("getCellComponent",rowId, "NSZYXBH");
            $item.textbox({readonly:true});
            $item = $grid.grid("getCellComponent",rowId, "NSZYXMC");
            $item.textbox({readonly:true});
            $item = $grid.grid("getCellComponent",rowId, "NSXJGSJ");
            $item.textbox({readonly:true});
            $item = $grid.grid("getCellComponent",rowId, "CZFDSJ");
            $item.textbox({readonly:true});
            //播种
            $item = $grid.grid("getCellComponent",rowId, "ZPFS");
            $item.combobox({readonly:true});
            //灌溉
            $item = $grid.grid("getCellComponent",rowId, "GGFS");
            $item.combobox({readonly:true});
            $item = $grid.grid("getCellComponent",rowId, "SYLX");
            $item.combobox({readonly:true});
            $item = $grid.grid("getCellComponent",rowId, "SYDJ");
            $item.combobox({readonly:true});
            //施肥
            $item = $grid.grid("getCellComponent",rowId, "SFSD");
            $item.combobox({readonly:true});
            //用药
            $item = $grid.grid("getCellComponent",rowId, "YYSD");
            $item.combobox({readonly:true});
            //检测
            $item = $grid.grid("getCellComponent",rowId, "JCFF");
            $item.combobox({readonly:true});
            $item = $grid.grid("getCellComponent",rowId, "JCDW");
            $item.combobox({readonly:true});
            //采收
            $item = $grid.grid("getCellComponent",rowId, "CSFS");
            $item.combobox({readonly:true});
            //锄草
            $item = $grid.grid("getCellComponent",rowId, "CCFS");
            $item.combobox({readonly:true});
        }
    });

    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'zzscdaxg.jsp',
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
                var id = CFG_getInputParamValue(configInfo, 'paramIn1');
                $.ns('namespaceId${idSuffix}').initform(id);
//				var flag = CFG_getSelfParamValue(configInfo, 'selfParam2');
                //工具条初始化
                /*$("#toolbar").toolbar({
                    data:"[{'label':'当前位置'},'->',{'label': '保存', 'id':'saveMaster', 'cls':'save_tb','disabled': 'false','type': 'button'},{'label': '关闭', 'id':'return', 'disabled': 'false','type': 'button'}]"
                });*/
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
// 自定义构件工具条预留区使用
var constructDetailFormData1 = {
    format: [
        {type: "block", width: "800", list:[
            {type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "constructId"},
            {type: "hidden", name: "reserveZoneId"},
            {type: "hidden", name: "isCommonReserveZone"},
            {type: "hidden", name: "showOrder"},
            {type: "hidden", name: "buttonName"},
            {type: "hidden", name: "buttonSource"},
            {type: "hidden", name: "position"},
            {type: "hidden", name: "beforeClickJs"},
            {type: "block", width:800, list:[
                {type: "combo", label: "绑定构件:&nbsp;&nbsp;&nbsp;", name: "componentVersionId", showAll: true, width:202}/*,
                {type: "newcolumn"},
                {type: "combo", label: "按钮样式:&nbsp;&nbsp;&nbsp;", name: "buttonImg", showAll: true, width:202}*/
            ]},
	        {type: "block", width:800, list:[
                 {type: "input", label: "按钮编码:", name: "buttonCode", maxLength:100, width:202, required: true, tooltip: '按钮编码不能为空'},
                 {type: "newcolumn"},
                 {type: "input", label: "按钮显示名称:", name: "buttonDisplayName", maxLength:100, width:202, required: true, tooltip: '按钮显示名称不能为空'}
            ]},
            {type: "block", width:800, list:[
                {type: "combo", label: "按钮样式:&nbsp;&nbsp;&nbsp;", name: "buttonCls", showAll: true, width:202},
                {type: "newcolumn"},
                {type: "combo", label: "按钮图标:&nbsp;&nbsp;&nbsp;", name: "buttonIcon", showAll: true, width:202}
            ]},
            {type: "block", width:800, list:[
                {type: "combo", label: "按钮类型:", name: "buttonType", required: true, showAll: true, width:202, tooltip: '按钮类型不能为空'},
                {type: "newcolumn"},
                {type: "combo", label: "所属按钮组:&nbsp;&nbsp;&nbsp;", name: "parentButtonCode", showAll: true, width:202}
            ]},
            {type: "block", width:800, list:[
                {type: "combo", label: "按钮位置:", name: "position", required: true, showAll: true, width:202, tooltip: '按钮位置不能为空',
                    options:[
                        {value: "0", text: "左对齐", selected:true},
                        {value: "1", text: "居中"},
                        {value: "2", text: "右对齐"}
                    ]}
            ]},
            {type: "block", width:800, list:[
                {type: "itemlabel", label: "页面组装类型:&nbsp;&nbsp;&nbsp;", labelAlign:"right"},
                {type: "newcolumn"},
                {type: "radio", name: "assembleType", label: "弹出", value:"0", labelWidth:60, position:"label-left", labelAlign:"right", checked: true},
                {type: "newcolumn"},
                {type: "radio", name: "assembleType", label: "嵌入", value:"1", labelWidth:80, position:"label-left", labelAlign:"right"},
                {type: "newcolumn"},
                {type: "radio", name: "assembleType", label: "标签页", value:"2", labelWidth:80, position:"label-left", labelAlign:"right"},
                {type: "newcolumn"},
                {type: "radio", name: "assembleType", label: "弹出整个页面", value:"3", labelWidth:120, position:"label-left", labelAlign:"right"}
            ]},
            {type: "block", width:800, list:[
                {type: "input", label: "宽度:&nbsp;&nbsp;&nbsp;", name: "width", value:"800", maxLength:4, validate: "validWidthOrHeight", tooltip: '宽度必须为正整数、百分数或小数'},
                {type: "newcolumn"},
                {type: "input", label: "高度:&nbsp;&nbsp;&nbsp;", name: "height", value:"600", maxLength:4, validate: "validWidthOrHeight", tooltip: '高度必须为正整数、百分数或小数'},
                {type: "newcolumn"},
                {type: "button", name: "save", value: "保存", width:80, offsetLeft:10}
            ]}
        ]}
    ],
    settings: {labelWidth: 120, inputWidth: 200}
};
// 列表超链接使用
var constructDetailFormData2 = {
    format: [
        {type: "block", width: "800", list:[
            {type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "constructId"},
            {type: "hidden", name: "reserveZoneId"},
            {type: "hidden", name: "isCommonReserveZone"},
            {type: "hidden", name: "showOrder"},
            {type: "hidden", name: "buttonName"},
            {type: "hidden", name: "buttonSource"},
            {type: "hidden", name: "buttonType", value: "0"},
            {type: "hidden", name: "beforeClickJs"},
            {type: "block", width:800, list:[
                {type: "combo", label: "绑定构件:", name: "componentVersionId", showAll: true, width:202, required: true, tooltip: '绑定构件不能为空'},
                {type: "newcolumn"},
                {type: "input", label: "按钮编码:", name: "buttonCode", maxLength:100, width:202, required: true, tooltip: '按钮编码不能为空'}
            ]},
            {type: "block", width:800, list:[
                {type: "input", label: "按钮显示名称:", name: "buttonDisplayName", maxLength:100, width:202, required: true, tooltip: '按钮显示名称不能为空'},
                {type: "newcolumn"},
                {type: "combo", label: "按钮样式:&nbsp;&nbsp;&nbsp;", name: "buttonCls", showAll: true, width:202}
            ]},
            {type: "block", width:800, list:[
                {type: "itemlabel", label: "页面组装类型:&nbsp;&nbsp;&nbsp;", labelAlign:"right"},
                {type: "newcolumn"},
                {type: "radio", name: "assembleType", label: "弹出", value:"0", labelWidth:60, position:"label-left", labelAlign:"right", checked: true},
                {type: "newcolumn"},
                {type: "radio", name: "assembleType", label: "嵌入", value:"1", labelWidth:80, position:"label-left", labelAlign:"right"},
                {type: "newcolumn"},
                {type: "radio", name: "assembleType", label: "标签页", value:"2", labelWidth:80, position:"label-left", labelAlign:"right"},
                {type: "newcolumn"},
                {type: "radio", name: "assembleType", label: "弹出整个页面", value:"3", labelWidth:120, position:"label-left", labelAlign:"right"}
            ]},
            {type: "block", width:800, list:[
                {type: "input", label: "宽度:&nbsp;&nbsp;&nbsp;", name: "width", value:"800", maxLength:4, validate: "validWidthOrHeight", tooltip: '宽度必须为正整数、百分数或小数'},
                {type: "newcolumn"},
                {type: "input", label: "高度:&nbsp;&nbsp;&nbsp;", name: "height", value:"600", maxLength:4, validate: "validWidthOrHeight", tooltip: '高度必须为正整数、百分数或小数'},
                {type: "newcolumn"},
                {type: "button", name: "save", value: "保存", width:80, offsetLeft:10}
            ]}
        ]}
    ],
    settings: {labelWidth: 120, inputWidth: 200}
};
// 按钮预留区使用
var constructDetailFormData3 = {
    format: [
        {type: "block", width: "800", list:[
            {type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "constructId"},
            {type: "hidden", name: "reserveZoneId"},
            {type: "hidden", name: "isCommonReserveZone"},
            {type: "hidden", name: "showOrder"},
            {type: "hidden", name: "beforeClickJs"},
            {type: "block", width:800, list:[
                {type: "combo", label: "绑定构件:", name: "componentVersionId", showAll: true, width:202, required: true, tooltip: '绑定构件不能为空'}
            ]},
            {type: "block", width:800, list:[
                {type: "itemlabel", label: "页面组装类型:&nbsp;&nbsp;&nbsp;", labelAlign:"right"},
                {type: "newcolumn"},
                {type: "radio", name: "assembleType", label: "弹出", value:"0", labelWidth:60, position:"label-left", labelAlign:"right", checked: true},
                {type: "newcolumn"},
                {type: "radio", name: "assembleType", label: "嵌入", value:"1", labelWidth:80, position:"label-left", labelAlign:"right"},
                {type: "newcolumn"},
                {type: "radio", name: "assembleType", label: "标签页", value:"2", labelWidth:80, position:"label-left", labelAlign:"right"},
                {type: "newcolumn"},
                {type: "radio", name: "assembleType", label: "弹出整个页面", value:"3", labelWidth:120, position:"label-left", labelAlign:"right"}
            ]},
            {type: "block", width:800, list:[
                {type: "input", label: "宽度:&nbsp;&nbsp;&nbsp;", name: "width", value:"800", maxLength:4, validate: "validWidthOrHeight", tooltip: '宽度必须为正整数、百分数或小数'},
                {type: "newcolumn"},
                {type: "input", label: "高度:&nbsp;&nbsp;&nbsp;", name: "height", value:"600", maxLength:4, validate: "validWidthOrHeight", tooltip: '高度必须为正整数、百分数或小数'},
                {type: "newcolumn"},
                {type: "button", name: "save", value: "保存", width:80, offsetLeft:10}
            ]}
        ]}
    ],
    settings: {labelWidth: 120, inputWidth: 200}
};
// 树节点预留区、树预留区中根节点 使用
var constructDetailFormData4 = {
    format: [
        {type: "block", width: "800", list:[
            {type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "constructId"},
            {type: "hidden", name: "reserveZoneId"},
            {type: "hidden", name: "treeNodeType"},
            {type: "hidden", name: "treeNodeProperty"},
            {type: "hidden", name: "isCommonReserveZone", value: "0"},
            {type: "hidden", name: "showOrder", value: "0"},
            {type: "hidden", name: "assembleType", value: "1"},
            {type: "hidden", name: "beforeClickJs"},
            {type: "block", width:800, list:[
                {type: "combo", label: "绑定构件:", name: "componentVersionId", showAll: true, width:202, required: true, tooltip: '绑定构件不能为空'},
                {type: "newcolumn"},
                {type: "button", name: "save", value: "保存", width:80, offsetLeft:10}
            ]}
        ]}
    ],
    settings: {labelWidth: 120, inputWidth: 200}
};
// 树预留区中空节点 使用
var constructDetailFormData5 = {
    format: [
        {type: "block", width: "800", list:[
            {type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "constructId"},
            {type: "hidden", name: "reserveZoneId", value: "TREE"},
            {type: "hidden", name: "treeNodeType"},
            {type: "hidden", name: "isCommonReserveZone", value: "0"},
            {type: "hidden", name: "showOrder", value: "0"},
            {type: "hidden", name: "assembleType", value: "1"},
            {type: "hidden", name: "beforeClickJs"},
            {type: "block", width:800, list:[
            {type: "newcolumn"},
                {type: "input", label: "值:", name: "treeNodeProperty", maxLength:100, required: true, tooltip: '值不能为空'},
                {type: "newcolumn"},
                {type: "combo", label: "绑定构件:", name: "componentVersionId", showAll: true, width:202, required: true, tooltip: '绑定构件不能为空'},
                {type: "newcolumn"},
                {type: "button", name: "save", value: "保存", width:80, offsetLeft:10}
            ]}
        ]}
    ],
    settings: {labelWidth: 120, inputWidth: 200}
};
// 树预留区中跨表字段节点 使用
var constructDetailFormData6 = {
    format: [
        {type: "block", width: "800", list:[
            {type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "constructId"},
            {type: "hidden", name: "reserveZoneId", value: "TREE"},
            {type: "hidden", name: "treeNodeType"},
            {type: "hidden", name: "isCommonReserveZone", value: "0"},
            {type: "hidden", name: "showOrder", value: "0"},
            {type: "hidden", name: "assembleType", value: "1"},
            {type: "hidden", name: "beforeClickJs"},
            {type: "block", width:800, list:[
            {type: "newcolumn"},
                {type: "combo", label: "字段标签:", name: "treeNodeProperty", showAll: true, width:202, required: true, tooltip: '字段标签不能为空'},
                {type: "newcolumn"},
                {type: "combo", label: "绑定构件:", name: "componentVersionId", showAll: true, width:202, required: true, tooltip: '绑定构件不能为空'},
                {type: "newcolumn"},
                {type: "button", name: "save", value: "保存", width:80, offsetLeft:10}
            ]}
        ]}
    ],
    settings: {labelWidth: 120, inputWidth: 200}
};
// 树预留区中表节点 使用
var constructDetailFormData7 = {
    format: [
        {type: "block", width: "800", list:[
            {type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "constructId"},
            {type: "hidden", name: "reserveZoneId", value: "TREE"},
            {type: "hidden", name: "treeNodeType"},
            {type: "hidden", name: "isCommonReserveZone", value: "0"},
            {type: "hidden", name: "showOrder", value: "0"},
            {type: "hidden", name: "assembleType", value: "1"},
            {type: "hidden", name: "beforeClickJs"},
            {type: "block", width:800, list:[
            {type: "newcolumn"},
                {type: "combo", label: "表节点:", name: "treeNodeProperty", showAll: true, width:202, required: true, tooltip: '表节点不能为空'},
                {type: "newcolumn"},
                {type: "combo", label: "绑定构件:", name: "componentVersionId", showAll: true, width:202, required: true, tooltip: '绑定构件不能为空'},
                {type: "newcolumn"},
                {type: "button", name: "save", value: "保存", width:80, offsetLeft:10}
            ]}
        ]}
    ],
    settings: {labelWidth: 120, inputWidth: 200}
};
// 预置工具条按钮表单
var constructDetailFormData8 = {
    format: [
        {type: "block", width: "800", list:[
            {type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "constructId"},
            {type: "hidden", name: "reserveZoneId"},
            {type: "hidden", name: "isCommonReserveZone"},
            {type: "hidden", name: "showOrder"},
            {type: "hidden", name: "buttonCode"},
            {type: "hidden", name: "buttonName"},
            {type: "hidden", name: "buttonSource"},
            {type: "hidden", name: "beforeClickJs"},
            {type: "block", width:800, list:[
                {type: "input", label: "按钮显示名称:", name: "buttonDisplayName", maxLength:100, width:202, required: true, tooltip: '按钮显示名称不能为空'}/*,
                {type: "newcolumn"},
                {type: "combo", label: "按钮样式:&nbsp;&nbsp;&nbsp;", name: "buttonImg", showAll: true, width:202}*/
            ]},
            {type: "block", width:800, list:[
                {type: "combo", label: "按钮类型:", name: "buttonType", required: true, showAll: true, width:202, tooltip: '按钮类型不能为空'},
                {type: "newcolumn"},
                {type: "combo", label: "所属按钮组:&nbsp;&nbsp;&nbsp;", name: "parentButtonCode", showAll: true, width:202}
            ]},
            {type: "block", width:800, list:[
                {type: "combo", label: "按钮样式:&nbsp;&nbsp;&nbsp;", name: "buttonCls", showAll: true, width:202},
                {type: "newcolumn"},
                {type: "combo", label: "按钮图标:&nbsp;&nbsp;&nbsp;", name: "buttonIcon", showAll: true, width:202}
            ]},
            {type: "block", width:800, list:[
                {type: "combo", label: "按钮位置:", name: "position", required: true, showAll: true, width:202, tooltip: '按钮位置不能为空',
                    options:[
                        {value: "0", text: "左对齐", selected:true},
                        {value: "1", text: "居中"},
                        {value: "2", text: "右对齐"}
                ]},
                {type: "newcolumn"},
                {type: "button", name: "save", value: "保存", width:80, offsetLeft:330}
            ]},
        ]}
    ],
    settings: {labelWidth: 120, inputWidth: 200}
};
// 标签页预留区
var constructDetailFormData9 = {
    format: [
        {type: "block", width: "800", list:[
            {type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "constructId"},
            {type: "hidden", name: "reserveZoneId"},
            {type: "hidden", name: "isCommonReserveZone"},
            {type: "hidden", name: "showOrder"},
            {type: "hidden", name: "buttonName"},
            {type: "hidden", name: "buttonSource"},
            {type: "hidden", name: "buttonType", value: "0"},
            {type: "hidden", name: "assembleType", value: "1"},
            {type: "hidden", name: "beforeClickJs"},
            {type: "block", width:800, list:[
                {type: "input", label: "标签页编码:", name: "buttonCode", maxLength:100, width:202, required: true, tooltip: '标签页编码不能为空'},
                {type: "combo", label: "绑定构件:", name: "componentVersionId", showAll: true, width:202, required: true, tooltip: '绑定构件不能为空'},
                {type: "newcolumn"},
                {type: "input", label: "标签页名称:", name: "buttonDisplayName", maxLength:100, width:202, required: true, tooltip: '按钮显示名称不能为空'},
                {type: "newcolumn"},
                {type: "button", name: "save", value: "保存", width:80, offsetLeft:10, offsetTop:24}
            ]}
        ]}
    ],
    settings: {labelWidth: 120, inputWidth: 200}
};
//预置超链接按钮表单
var constructDetailFormData10 = {
    format: [
        {type: "block", width: "800", list:[
            {type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "constructId"},
            {type: "hidden", name: "reserveZoneId"},
            {type: "hidden", name: "isCommonReserveZone"},
            {type: "hidden", name: "showOrder"},
            {type: "hidden", name: "buttonCode"},
            {type: "hidden", name: "buttonName"},
            {type: "hidden", name: "buttonSource"},
            {type: "hidden", name: "position"},
            {type: "hidden", name: "beforeClickJs"},
            {type: "block", width:800, list:[
                {type: "input", label: "按钮显示名称:", name: "buttonDisplayName", maxLength:100, width:202, required: true, tooltip: '按钮显示名称不能为空'},
                {type: "newcolumn"},
                {type: "combo", label: "按钮样式:&nbsp;&nbsp;&nbsp;", name: "buttonCls", showAll: true, width:202}
            ]},
            {type: "block", width:800, list:[
                {type: "combo", label: "按钮类型:", name: "buttonType", required: true, showAll: true, width:202, tooltip: '按钮类型不能为空'},
                {type: "newcolumn"},
                {type: "combo", label: "所属按钮组:&nbsp;&nbsp;&nbsp;", name: "parentButtonCode", showAll: true, width:202},
                {type: "newcolumn"},
                {type: "button", name: "save", value: "保存", width:80, offsetLeft:10}
            ]}
        ]}
    ],
    settings: {labelWidth: 120, inputWidth: 200}
};
//树预留区（公用配置） 使用
var constructDetailFormData11 = {
    format: [
        {type: "block", width: "800", list:[
            {type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "constructId"},
            {type: "hidden", name: "reserveZoneId", value: "TREE"},
            {type: "hidden", name: "treeNodeProperty", value: "ThirdParty"},
            {type: "hidden", name: "isCommonReserveZone", value: "0"},
            {type: "hidden", name: "showOrder", value: "0"},
            {type: "hidden", name: "assembleType", value: "1"},
            {type: "hidden", name: "beforeClickJs"},
            {type: "block", width:800, list:[
            {type: "newcolumn"},
                {type: "combo", label: "节点类型:", name: "treeNodeType", showAll: true, width:202, required: true, tooltip: '节点类型不能为空'},
                {type: "newcolumn"},
                {type: "combo", label: "绑定构件:", name: "componentVersionId", showAll: true, width:202, required: true, tooltip: '绑定构件不能为空'},
                {type: "newcolumn"},
                {type: "button", name: "save", value: "保存", width:80, offsetLeft:10}
            ]}
        ]}
    ],
    settings: {labelWidth: 120, inputWidth: 200}
};
//中转器预留区
var constructDetailFormData12 = {
    format: [
        {type: "block", width: "800", list:[
            {type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "constructId"},
            {type: "hidden", name: "reserveZoneId"},
            {type: "hidden", name: "isCommonReserveZone"},
            {type: "hidden", name: "showOrder"},
            {type: "hidden", name: "buttonName"},
            {type: "hidden", name: "buttonDisplayName"},
            {type: "hidden", name: "buttonSource"},
            {type: "hidden", name: "buttonType", value: "0"},
            {type: "hidden", name: "beforeClickJs"},
            {type: "block", width:800, list:[
                {type: "input", label: "条件:", name: "buttonCode", maxLength:100, width:202, required: true, tooltip: '条件不能为空'},
                {type: "newcolumn"},
                {type: "combo", label: "绑定构件:", name: "componentVersionId", showAll: true, width:202, required: true, tooltip: '绑定构件不能为空'}
            ]},
            {type: "block", width:800, list:[
                 {type: "itemlabel", label: "页面组装类型:&nbsp;&nbsp;&nbsp;", labelAlign:"right"},
                 {type: "newcolumn"},
                 {type: "radio", name: "assembleType", label: "弹出", value:"0", labelWidth:60, position:"label-left", labelAlign:"right", checked: true},
                 {type: "newcolumn"},
                 {type: "radio", name: "assembleType", label: "嵌入", value:"1", labelWidth:80, position:"label-left", labelAlign:"right"},
                 {type: "newcolumn"},
                 {type: "radio", name: "assembleType", label: "标签页", value:"2", labelWidth:80, position:"label-left", labelAlign:"right"}
             ]},
             {type: "block", width:800, list:[
                 {type: "input", label: "宽度:&nbsp;&nbsp;&nbsp;", name: "width", value:"800", maxLength:4, validate: "validWidthOrHeight", tooltip: '宽度必须为正整数、百分数或小数'},
                 {type: "newcolumn"},
                 {type: "input", label: "高度:&nbsp;&nbsp;&nbsp;", name: "height", value:"600", maxLength:4, validate: "validWidthOrHeight", tooltip: '高度必须为正整数、百分数或小数'},
                 {type: "newcolumn"},
                 {type: "button", name: "save", value: "保存", width:80, offsetLeft:10}
             ]}
        ]}
    ],
    settings: {labelWidth: 120, inputWidth: 200}
};
//页面构件工具条预留区使用
var constructDetailFormData13 = {
    format: [
        {type: "block", width: "800", list:[
            {type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "constructId"},
            {type: "hidden", name: "reserveZoneId"},
            {type: "hidden", name: "isCommonReserveZone"},
            {type: "hidden", name: "showOrder"},
            {type: "hidden", name: "buttonName"},
            {type: "hidden", name: "buttonSource"},
            {type: "hidden", name: "position"},
            {type: "hidden", name: "beforeClickJs"},
            {type: "block", width:800, list:[
                {type: "combo", label: "绑定构件:&nbsp;&nbsp;&nbsp;", name: "componentVersionId", showAll: true, width:202}/*,
                {type: "newcolumn"},
                {type: "combo", label: "按钮样式:&nbsp;&nbsp;&nbsp;", name: "buttonImg", showAll: true, width:202}*/
            ]},
            {type: "block", width:800, list:[
                 {type: "input", label: "按钮编码:", name: "buttonCode", maxLength:100, width:202, required: true, tooltip: '按钮编码不能为空'},
                 {type: "newcolumn"},
                 {type: "input", label: "按钮显示名称:", name: "buttonDisplayName", maxLength:100, width:202, required: true, tooltip: '按钮显示名称不能为空'}
             ]},
             {type: "block", width:800, list:[
                 {type: "combo", label: "按钮样式:&nbsp;&nbsp;&nbsp;", name: "buttonCls", showAll: true, width:202},
                 {type: "newcolumn"},
                 {type: "combo", label: "按钮图标:&nbsp;&nbsp;&nbsp;", name: "buttonIcon", showAll: true, width:202}
             ]},
            {type: "block", width:800, list:[
                {type: "combo", label: "按钮类型:", name: "buttonType", required: true, showAll: true, width:202, tooltip: '按钮类型不能为空'},
                {type: "newcolumn"},
                {type: "combo", label: "所属按钮组:&nbsp;&nbsp;&nbsp;", name: "parentButtonCode", showAll: true, width:202}
            ]},
            {type: "block", width:800, list:[
                {type: "itemlabel", label: "页面组装类型:&nbsp;&nbsp;&nbsp;", labelAlign:"right"},
                {type: "newcolumn"},
                {type: "radio", name: "assembleType", label: "弹出", value:"0", labelWidth:60, position:"label-left", labelAlign:"right", checked: true},
                {type: "newcolumn"},
                {type: "radio", name: "assembleType", label: "嵌入", value:"1", labelWidth:80, position:"label-left", labelAlign:"right"},
                {type: "newcolumn"},
                {type: "radio", name: "assembleType", label: "标签页", value:"2", labelWidth:80, position:"label-left", labelAlign:"right"},
                {type: "newcolumn"},
                {type: "radio", name: "assembleType", label: "弹出整个页面", value:"3", labelWidth:120, position:"label-left", labelAlign:"right"}
            ]},
            {type: "block", width:800, list:[
                {type: "input", label: "宽度:&nbsp;&nbsp;&nbsp;", name: "width", value:"800", maxLength:4, validate: "validWidthOrHeight", tooltip: '宽度必须为正整数、百分数或小数'},
                {type: "newcolumn"},
                {type: "input", label: "高度:&nbsp;&nbsp;&nbsp;", name: "height", value:"600", maxLength:4, validate: "validWidthOrHeight", tooltip: '高度必须为正整数、百分数或小数'},
                {type: "newcolumn"},
                {type: "button", name: "save", value: "保存", width:80, offsetLeft:10}
            ]}
        ]}
    ],
    settings: {labelWidth: 120, inputWidth: 200}
};
//预置工具条新增和修改按钮表单
var constructDetailFormData14 = {
    format: [
        {type: "block", width: "800", list:[
            {type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "constructId"},
            {type: "hidden", name: "reserveZoneId"},
            {type: "hidden", name: "isCommonReserveZone"},
            {type: "hidden", name: "showOrder"},
            {type: "hidden", name: "buttonCode"},
            {type: "hidden", name: "buttonName"},
            {type: "hidden", name: "buttonSource"},
            {type: "hidden", name: "beforeClickJs"},
            {type: "block", width:800, list:[
                {type: "input", label: "按钮显示名称:", name: "buttonDisplayName", maxLength:100, width:202, required: true, tooltip: '按钮显示名称不能为空'}/*,
                {type: "newcolumn"},
                {type: "combo", label: "按钮样式:&nbsp;&nbsp;&nbsp;", name: "buttonImg", showAll: true, width:202}*/
            ]},
            {type: "block", width:800, list:[
                {type: "combo", label: "按钮类型:", name: "buttonType", required: true, showAll: true, width:202, tooltip: '按钮类型不能为空'},
                {type: "newcolumn"},
                {type: "combo", label: "所属按钮组:&nbsp;&nbsp;&nbsp;", name: "parentButtonCode", showAll: true, width:202}
            ]},
            {type: "block", width:800, list:[
	            {type: "combo", label: "按钮样式:&nbsp;&nbsp;&nbsp;", name: "buttonCls", showAll: true, width:202},
	            {type: "newcolumn"},
	            {type: "combo", label: "按钮图标:&nbsp;&nbsp;&nbsp;", name: "buttonIcon", showAll: true, width:202}
	        ]},
	        {type: "block", width:800, list:[
                {type: "combo", label: "按钮位置:", name: "position", required: true, showAll: true, width:202, tooltip: '按钮位置不能为空',
                    options:[
                        {value: "0", text: "左对齐", selected:true},
                        {value: "1", text: "居中"},
                        {value: "2", text: "右对齐"}
                ]}
            ]},
            {type: "block", width:800, list:[
                {type: "itemlabel", label: "页面组装类型:&nbsp;&nbsp;&nbsp;", labelAlign:"right"},
                {type: "newcolumn"},
                {type: "radio", name: "assembleType", label: "弹出", value:"0", labelWidth:60, position:"label-left", labelAlign:"right", checked: true},
                {type: "newcolumn"},
                {type: "radio", name: "assembleType", label: "嵌入", value:"1", labelWidth:80, position:"label-left", labelAlign:"right"}
            ]},
            {type: "block", width:800, list:[
                {type: "input", label: "宽度:&nbsp;&nbsp;&nbsp;", name: "width", value:"800", maxLength:4, validate: "validWidthOrHeight", tooltip: '宽度必须为正整数、百分数或小数'},
                {type: "newcolumn"},
                {type: "input", label: "高度:&nbsp;&nbsp;&nbsp;", name: "height", value:"600", maxLength:4, validate: "validWidthOrHeight", tooltip: '高度必须为正整数、百分数或小数'},
                {type: "newcolumn"},
                {type: "button", name: "save", value: "保存", width:80, offsetLeft:10}
            ]}
        ]}
    ],
    settings: {labelWidth: 120, inputWidth: 200}
};
//预置工具条可控制弹出嵌入的按钮表单
var constructDetailFormData15 = {
    format: [
        {type: "block", width: "800", list:[
            {type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "constructId"},
            {type: "hidden", name: "reserveZoneId"},
            {type: "hidden", name: "isCommonReserveZone"},
            {type: "hidden", name: "showOrder"},
            {type: "hidden", name: "buttonCode"},
            {type: "hidden", name: "buttonName"},
            {type: "hidden", name: "buttonSource"},
            {type: "hidden", name: "beforeClickJs"},
            {type: "block", width:800, list:[
                {type: "input", label: "按钮显示名称:", name: "buttonDisplayName", maxLength:100, width:202, required: true, tooltip: '按钮显示名称不能为空'}/*,
                {type: "newcolumn"},
                {type: "combo", label: "按钮样式:&nbsp;&nbsp;&nbsp;", name: "buttonImg", showAll: true, width:202}*/
            ]},
            {type: "block", width:800, list:[
                {type: "combo", label: "按钮样式:&nbsp;&nbsp;&nbsp;", name: "buttonCls", showAll: true, width:202},
                {type: "newcolumn"},
                {type: "combo", label: "按钮图标:&nbsp;&nbsp;&nbsp;", name: "buttonIcon", showAll: true, width:202}
            ]},
            {type: "block", width:800, list:[
                {type: "combo", label: "按钮类型:", name: "buttonType", required: true, showAll: true, width:202, tooltip: '按钮类型不能为空'},
                {type: "newcolumn"},
                {type: "combo", label: "所属按钮组:&nbsp;&nbsp;&nbsp;", name: "parentButtonCode", showAll: true, width:202}
            ]},
            {type: "block", width:800, list:[
                {type: "combo", label: "按钮位置:", name: "position", required: true, showAll: true, width:202, tooltip: '按钮位置不能为空',
                    options:[
                        {value: "0", text: "左对齐", selected:true},
                        {value: "1", text: "居中"},
                        {value: "2", text: "右对齐"}
                ]}
            ]},
            {type: "block", width:800, list:[
                {type: "itemlabel", label: "页面组装类型:&nbsp;&nbsp;&nbsp;", labelAlign:"right"},
                {type: "newcolumn"},
                {type: "radio", name: "assembleType", label: "弹出", value:"0", labelWidth:60, position:"label-left", labelAlign:"right"},
                {type: "newcolumn"},
                {type: "radio", name: "assembleType", label: "嵌入", value:"1", labelWidth:80, position:"label-left", labelAlign:"right", checked: true},
                {type: "newcolumn"},
                {type: "button", name: "save", value: "保存", width:80, offsetLeft:365}
            ]}
        ]}
    ],
    settings: {labelWidth: 120, inputWidth: 200}
};
//超链接详细和明细按钮表单
var constructDetailFormData16 = {
    format: [
        {type: "block", width: "800", list:[
            {type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "constructId"},
            {type: "hidden", name: "reserveZoneId"},
            {type: "hidden", name: "isCommonReserveZone"},
            {type: "hidden", name: "showOrder"},
            {type: "hidden", name: "buttonCode"},
            {type: "hidden", name: "buttonName"},
            {type: "hidden", name: "buttonSource"},
            {type: "hidden", name: "beforeClickJs"},
            {type: "block", width:800, list:[
                {type: "input", label: "按钮显示名称:", name: "buttonDisplayName", maxLength:100, width:202, required: true, tooltip: '按钮显示名称不能为空'}/*,
                {type: "newcolumn"},
                {type: "combo", label: "按钮样式:&nbsp;&nbsp;&nbsp;", name: "buttonImg", showAll: true, width:202}*/
            ]},
            {type: "block", width:800, list:[
                {type: "combo", label: "按钮类型:", name: "buttonType", required: true, showAll: true, width:202, tooltip: '按钮类型不能为空'},
                {type: "newcolumn"},
                {type: "combo", label: "所属按钮组:&nbsp;&nbsp;&nbsp;", name: "parentButtonCode", showAll: true, width:202}
            ]},
            {type: "block", width:800, list:[
                {type: "combo", label: "按钮样式:&nbsp;&nbsp;&nbsp;", name: "buttonCls", showAll: true, width:202},
                {type: "newcolumn"},
                {type: "combo", label: "按钮图标:&nbsp;&nbsp;&nbsp;", name: "buttonIcon", showAll: true, width:202}
            ]},
            {type: "block", width:800, list:[
                {type: "itemlabel", label: "页面组装类型:&nbsp;&nbsp;&nbsp;", labelAlign:"right"},
                {type: "newcolumn"},
                {type: "radio", name: "assembleType", label: "弹出", value:"0", labelWidth:60, position:"label-left", labelAlign:"right", checked: true},
                {type: "newcolumn"},
                {type: "radio", name: "assembleType", label: "嵌入", value:"1", labelWidth:80, position:"label-left", labelAlign:"right"}
            ]},
            {type: "block", width:800, list:[
                {type: "input", label: "宽度:&nbsp;&nbsp;&nbsp;", name: "width", value:"800", maxLength:4, validate: "validWidthOrHeight", tooltip: '宽度必须为正整数、百分数或小数'},
                {type: "newcolumn"},
                {type: "input", label: "高度:&nbsp;&nbsp;&nbsp;", name: "height", value:"600", maxLength:4, validate: "validWidthOrHeight", tooltip: '高度必须为正整数、百分数或小数'},
                {type: "newcolumn"},
                {type: "button", name: "save", value: "保存", width:80, offsetLeft:10}
            ]}
        ]}
    ],
    settings: {labelWidth: 120, inputWidth: 200}
};
function validWidthOrHeight(text) {
    var flag = false;
    // 正整数
    var reg1 = new RegExp(/^[1-9]{1}\d*$/);
    flag = reg1.test(text);
    if (!flag) {
        // 百分数
        var reg2 = new RegExp(/^[1-9]{1}\d?\%{1}$/);
        flag = reg2.test(text);
    }
    if (!flag) {
        // 小数
        var reg3 = new RegExp(/^0?\.{1}\d+$/);
        flag = reg3.test(text);
    }
    return flag;
}
/**
 * 获取绑定构件的表单结构，treeNodeType只有currentReserveZoneType为TREE是才有效
 */
function getConstructDetailFormData(id) {
    // 预置按钮表单
    var buttonSource = constructDetailGrid.getUserData(id, "buttonSource");
    if (buttonSource == "0") {
        if (currentReserveZoneType == "1") {
            var buttonCode = constructDetailGrid.cells(id, 2).getValue();
            if (buttonCode == 'linkViewDForm' || buttonCode == 'linkViewDGrid') {
                return constructDetailFormData16;
            } else {
                return constructDetailFormData10;
            }
        } else {
            if ("GRID" == getToolbarReserveType(currentReserveZoneName)) {
                var buttonCode = constructDetailGrid.cells(id, 2).getValue();
                if (buttonCode == 'create' || buttonCode == 'update') {
                    return constructDetailFormData14;
                } else if (buttonCode == 'batchUpdate') {//可控制弹出嵌入按钮
					return constructDetailFormData15;
				} else {
                    return constructDetailFormData8;
                }
            } else {
                return constructDetailFormData8;
            }
        }
    }
    // 非预置按钮表单
    if (currentReserveZoneType == "0") {
        if (baseComponentVersionType == "1") {
            return constructDetailFormData13;
        } else {
            if ("GRID" == getToolbarReserveType(currentReserveZoneName) && baseComponentVersionType == "1") {
                return constructDetailFormData13;
            } else {
                return constructDetailFormData1;
            }
        }
    } else if (currentReserveZoneType == "1") {
        return constructDetailFormData2;
    } else if (currentReserveZoneType == "2") {
        return constructDetailFormData3;
    } else if (currentReserveZoneType == "3") {
        return constructDetailFormData4;
    } else if (currentReserveZoneType == "4") {
        return constructDetailFormData9;
    } else if (currentReserveZoneType == "5") {
        return constructDetailFormData12;
    } else if (currentReserveZoneType == "TREE") {
        if (currentTreeNodeType == "0") {
            return constructDetailFormData4;
        } else if (currentTreeNodeType == "1") {
            return constructDetailFormData5;
        } else if (currentTreeNodeType == "4") {
            return constructDetailFormData6;
        } else if (currentTreeNodeType == "2") {
            return constructDetailFormData7;
        } else if (currentTreeNodeType == "TREE") {
            return constructDetailFormData11;
        }
    }
}
/**
 * 更改绑定构件列表的显示列
 */
function changeConstructDetailGrid() {
    if (currentReserveZoneType == "0") {
        constructDetailGrid.setColumnHidden(0, false);
        constructDetailGrid.setColumnHidden(1, false);
        constructDetailGrid.setColumnHidden(2, false);
        constructDetailGrid.setColLabel(2, "<center>按钮编码</center>");
        constructDetailGrid.setColumnHidden(3, false);
        constructDetailGrid.setColLabel(3, "<center>按钮显示名称</center>");
        constructDetailGrid.setColumnHidden(4, false);
        constructDetailGrid.setColumnHidden(5, false);
        constructDetailGrid.setColumnHidden(6, true);
        constructDetailGrid.setColumnHidden(7, true);
        if ("GRID" == getToolbarReserveType(currentReserveZoneName) && baseComponentVersionType == "1") {
            constructDetailGrid.setColumnHidden(8, true);
        } else {
            constructDetailGrid.setColumnHidden(8, false);
        }
    } else if (currentReserveZoneType == "1") {
        constructDetailGrid.setColumnHidden(0, false);
        constructDetailGrid.setColumnHidden(1, false);
        constructDetailGrid.setColumnHidden(2, false);
        constructDetailGrid.setColLabel(2, "<center>按钮编码</center>");
        constructDetailGrid.setColumnHidden(3, false);
        constructDetailGrid.setColLabel(3, "<center>按钮显示名称</center>");
        constructDetailGrid.setColumnHidden(4, false);
        constructDetailGrid.setColumnHidden(5, true);
        constructDetailGrid.setColumnHidden(6, true);
        constructDetailGrid.setColumnHidden(7, true);
        constructDetailGrid.setColumnHidden(8, true);
    } else if (currentReserveZoneType == "2" || currentReserveZoneType == "3") {
        constructDetailGrid.setColumnHidden(0, false);
        constructDetailGrid.setColumnHidden(1, false);
        constructDetailGrid.setColumnHidden(2, true);
        constructDetailGrid.setColumnHidden(3, true);
        constructDetailGrid.setColumnHidden(4, true);
        constructDetailGrid.setColumnHidden(5, true);
        constructDetailGrid.setColumnHidden(6, true);
        constructDetailGrid.setColumnHidden(7, true);
        constructDetailGrid.setColumnHidden(8, true);
    } else if (currentReserveZoneType == "4") {
        constructDetailGrid.setColumnHidden(0, false);
        constructDetailGrid.setColumnHidden(1, false);
        constructDetailGrid.setColumnHidden(2, false);
        constructDetailGrid.setColLabel(2, "<center>标签页编码</center>");
        constructDetailGrid.setColumnHidden(3, false);
        constructDetailGrid.setColLabel(3, "<center>标签页名称</center>");
        constructDetailGrid.setColumnHidden(4, true);
        constructDetailGrid.setColumnHidden(5, true);
        constructDetailGrid.setColumnHidden(6, true);
        constructDetailGrid.setColumnHidden(7, true);
        constructDetailGrid.setColumnHidden(8, true);
    } else if (currentReserveZoneType == "5") {
        constructDetailGrid.setColumnHidden(0, false);
        constructDetailGrid.setColumnHidden(1, false);
        constructDetailGrid.setColumnHidden(2, false);
        constructDetailGrid.setColLabel(2, "<center>条件</center>");
        constructDetailGrid.setColumnHidden(3, true);
        constructDetailGrid.setColumnHidden(4, true);
        constructDetailGrid.setColumnHidden(5, true);
        constructDetailGrid.setColumnHidden(6, true);
        constructDetailGrid.setColumnHidden(7, true);
        constructDetailGrid.setColumnHidden(8, true);
    } else if (currentReserveZoneType == "TREE") {
        if (currentTreeNodeType == "0") {
            constructDetailGrid.setColumnHidden(0, false);
            constructDetailGrid.setColumnHidden(1, false);
            constructDetailGrid.setColumnHidden(2, true);
            constructDetailGrid.setColumnHidden(3, true);
            constructDetailGrid.setColumnHidden(4, true);
            constructDetailGrid.setColumnHidden(5, true);
            constructDetailGrid.setColumnHidden(6, true);
            constructDetailGrid.setColumnHidden(7, true);
            constructDetailGrid.setColumnHidden(8, true);
        } else if (currentTreeNodeType == "1") {
            constructDetailGrid.setColumnHidden(0, false);
            constructDetailGrid.setColumnHidden(1, false);
            constructDetailGrid.setColumnHidden(2, true);
            constructDetailGrid.setColumnHidden(3, true);
            constructDetailGrid.setColumnHidden(4, true);
            constructDetailGrid.setColumnHidden(5, true);
            constructDetailGrid.setColumnHidden(6, false);
            constructDetailGrid.setColumnHidden(7, true);
            constructDetailGrid.setColumnHidden(8, true);
            constructDetailGrid.setColLabel(5, "<center>值</center>");
        } else if (currentTreeNodeType == "4") {
            constructDetailGrid.setColumnHidden(0, false);
            constructDetailGrid.setColumnHidden(1, false);
            constructDetailGrid.setColumnHidden(2, true);
            constructDetailGrid.setColumnHidden(3, true);
            constructDetailGrid.setColumnHidden(4, true);
            constructDetailGrid.setColumnHidden(5, true);
            constructDetailGrid.setColumnHidden(6, false);
            constructDetailGrid.setColumnHidden(7, true);
            constructDetailGrid.setColumnHidden(8, true);
            constructDetailGrid.setColLabel(5, "<center>字段标签</center>");
        } else if (currentTreeNodeType == "2") {
            constructDetailGrid.setColumnHidden(0, false);
            constructDetailGrid.setColumnHidden(1, false);
            constructDetailGrid.setColumnHidden(2, true);
            constructDetailGrid.setColumnHidden(3, true);
            constructDetailGrid.setColumnHidden(4, true);
            constructDetailGrid.setColumnHidden(5, true);
            constructDetailGrid.setColumnHidden(6, false);
            constructDetailGrid.setColumnHidden(7, true);
            constructDetailGrid.setColumnHidden(8, true);
            constructDetailGrid.setColLabel(5, "<center>表节点</center>");
        } else if (currentTreeNodeType == "5") {
            constructDetailGrid.setColumnHidden(0, true);
            constructDetailGrid.setColumnHidden(1, false);
            constructDetailGrid.setColumnHidden(2, true);
            constructDetailGrid.setColumnHidden(3, true);
            constructDetailGrid.setColumnHidden(4, true);
            constructDetailGrid.setColumnHidden(5, true);
            constructDetailGrid.setColumnHidden(6, false);
            constructDetailGrid.setColumnHidden(7, true);
            constructDetailGrid.setColumnHidden(8, true);
            constructDetailGrid.setColLabel(5, "<center>逻辑表组</center>");
        } else if (currentTreeNodeType == "TREE") {
            constructDetailGrid.setColumnHidden(0, true);
            constructDetailGrid.setColumnHidden(1, false);
            constructDetailGrid.setColumnHidden(2, true);
            constructDetailGrid.setColumnHidden(3, true);
            constructDetailGrid.setColumnHidden(4, true);
            constructDetailGrid.setColumnHidden(5, true);
            constructDetailGrid.setColumnHidden(6, true);
            constructDetailGrid.setColumnHidden(7, false);
            constructDetailGrid.setColumnHidden(8, true);
        }
    } else {
        constructDetailGrid.setColumnHidden(0, true);
        constructDetailGrid.setColumnHidden(1, true);
        constructDetailGrid.setColumnHidden(2, true);
        constructDetailGrid.setColumnHidden(3, true);
        constructDetailGrid.setColumnHidden(4, true);
        constructDetailGrid.setColumnHidden(5, true);
        constructDetailGrid.setColumnHidden(6, true);
        constructDetailGrid.setColumnHidden(7, true);
        constructDetailGrid.setColumnHidden(8, true);
    }
}
var constructDetailGridData = {
    format: {
        headers: ["&nbsp;", "<center>构件</center>", "<center>按钮编码</center>", "<center>按钮显示名称</center>", "<center>按钮类型</center>", "<center>所属按钮组</center>", "<center>属性</center>", "<center>节点类型</center>", "<center>对齐方式</center>", ""],
        cols: ["id", "componentAliasAndVersion", "buttonCode", "buttonDisplayName", "buttonType", "parentButtonCode", "treeNodeProperty", "treeNodeType", "position"],
        userDatas: ["componentVersionId", "reserveZoneId", "buttonSource", "buttonCode"],
        colWidths: ["30", "220", "120", "120", "80", "100", "240", "240", "80", "*"],
        colTypes: ["sub_row_form", "ro", "ro", "ro", "co", "ro", "co", "co", "co", "ro"],
        colAligns: ["right", "left", "left", "left", "left", "left", "left", "left", "left"],
        colTooltips: ["false", "true", "true", "true",  "true", "true", "true", "true", "true", "false"]
    }
};
/**
 * 初始化列表中树节点分类下拉框
 */
function initTreeNodeTypeComboOfGrid() {
    var treeNodeTypeCombo = constructDetailGrid.getCombo(7);
    treeNodeTypeCombo.clear();
    if (currentTreeNodeType == "TREE") {
        treeNodeTypeCombo.put("1", "空节点公用");
        treeNodeTypeCombo.put("2", "表节点公用");
        treeNodeTypeCombo.put("4", "空字段节点公用");
        treeNodeTypeCombo.put("5", "物理表组节点公用");
    }
}
/**
 * 初始化列表中树节点属性下拉框
 */
function initTreeNodePropertyComboOfGrid() {
    var treeNodePropertyCombo = constructDetailGrid.getCombo(6);
    treeNodePropertyCombo.clear();
    if (currentTreeNodeType == "4" || currentTreeNodeType == "2" || currentTreeNodeType == "5") {
        var treeNodePropertyComboUrl = CONSTRUCT_DETAIL_MODEL_URL + "!getTreeNodePropertyCombo.json?constructId=" + currentConstructIdOfTree + "&treeNodeType=" + currentTreeNodeType;
        var treeNodePropertyJsonObj = loadJson(treeNodePropertyComboUrl);
        if (treeNodePropertyJsonObj && treeNodePropertyJsonObj.length) {
            for (var m = 0; m < treeNodePropertyJsonObj.length; m++) {
                treeNodePropertyCombo.put(treeNodePropertyJsonObj[m][0], treeNodePropertyJsonObj[m][1]);
            }
        }
    }
}
/**
 * 初始化列表中对齐方式下拉框
 */
function initPositionComboOfGrid() {
    var positionCombo = constructDetailGrid.getCombo(8);
    positionCombo.clear();
    positionCombo.put("0", "左对齐");
    positionCombo.put("1", "居中");
    positionCombo.put("2", "右对齐");
}
/**
 * 初始化constructDetailGrid
 */
function initConstructDetailGrid() {
    constructDetailGrid.setImagePath(IMAGE_PATH);
    constructDetailGrid.setHeader(constructDetailGridData.format.headers.toString());
    constructDetailGrid.setInitWidths(constructDetailGridData.format.colWidths.toString());
    constructDetailGrid.setColTypes(constructDetailGridData.format.colTypes.toString());
    constructDetailGrid.setColAlign(constructDetailGridData.format.colAligns.toString());
    if (constructDetailGridData.format.colTooltips) {
        constructDetailGrid.enableTooltips(constructDetailGridData.format.colTooltips.toString());
    }
    constructDetailGrid.setSkin(Skin);
    constructDetailGrid.init();
    constructDetailGrid.enableMultiselect(true);
    constructDetailGrid.enableDragAndDrop(true);
    constructDetailGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    constructDetailGrid.attachEvent("onRowDblClicked", function(rId, cInd) {
    });
    constructDetailGrid.attachEvent("onBeforeDrag", function(id) {
        return this.cells(id, 1).getValue() + "-" + this.cells(id, 3).getValue();
    });
    constructDetailGrid.attachEvent("onDrag", function(sId, tId) {
        if (sId.indexOf(",") != -1) {
            dhtmlx.message(getMessage("drag_one_record"));
            return false;
        }
        return true;
    });
    constructDetailGrid.attachEvent("onDrop", function(sId, tId) {
        var sParentButtonCode = constructDetailGrid.cells(sId, 4).getValue();
        var tParentButtonCode = constructDetailGrid.cells(tId, 4).getValue();
        if (sParentButtonCode != tParentButtonCode) {
            if (sParentButtonCode != "") {
                dhtmlx.message("请在同一按钮组内拖动排序！");
                constructDetailGridLoadData();
                return;
            } else {
                dhtmlx.message("请在一级按钮和按钮组范围内拖动排序！");
                constructDetailGridLoadData();
                return;
            }
        }
        loadJson(CONSTRUCT_DETAIL_MODEL_URL + "!sort.json?start=" + sId + "&end=" + tId);
        constructDetailGridLoadData();
    });
    constructDetailGrid.attachEvent("onRowSelect", function(rId, cInd) {
        var buttonCode = constructDetailGrid.getUserData(rId, "buttonCode");
        if (buttonCode == "COMBOBOX_SEARCH") {
            return false;
        }
        if (currentTreeNodeType == "5") {
            return true;
        }
        constructDetailGrid.cells(rId, 0).open();
    });
    constructDetailGrid.attachEvent("onBeforeSubFormLoadStruct", function(subform, id) {
        var buttonCode = constructDetailGrid.getUserData(id, "buttonCode");
        if (buttonCode == "COMBOBOX_SEARCH") {
            return false;
        }
        subform.c = initDetailFormFormat(getConstructDetailFormData(id));
    });
    constructDetailGrid.attachEvent("onSubFormLoaded", function(subform, id, index) {
        subform.setItemValue("constructId", currentConstructIdOfTree);
        subform.setItemValue("reserveZoneId", currentReserveZoneId);
        subform.setItemValue("isCommonReserveZone", isCommonReserveZone);
        initSubform(subform, id);
        constructDetailForm = subform;
    });
    constructDetailGrid.attachEvent("onSubRowOpen", function(id, expanded) {
        if (expanded) {
            constructDetailGrid.forEachRow(function(rId) {
                if (id != rId) {
                    constructDetailGrid.cells(rId, 0).close();
                }
            });
        }
    });
}
/**
 * 初始化Subform
 */
function initSubform(subform, id) {
    // 预置按钮表单
    var buttonSource = constructDetailGrid.getUserData(id, "buttonSource");
    if (buttonSource == "0") {
        subform.disableItem("parentButtonCode");
        var buttonCode = constructDetailGrid.getUserData(id, "buttonCode");
        if ("more" == buttonCode || "baseSearch" == buttonCode || "greatSearch" == buttonCode || "integrationSearch" == buttonCode) {
            subform.disableItem("buttonType");
        } else {
            subform.enableItem("buttonType");
        }
        // 初始化按钮图标
        initButtonImgCombo(subform);
        // 初始化按钮类型下拉框和下拉按钮下拉框
        initButtonTypeCombo(subform);
        if ("create" == buttonCode || "update" == buttonCode) {
            // 初始化页面组装类型事件
            subform.attachEvent("onChange", function(id, value) {
                if (id == "assembleType") {
                    if (value == "0") {
                        subform.enableItem("width");
                        subform.enableItem("height");
                    } else if (value == "1") {
                        subform.disableItem("width");
                        subform.disableItem("height");
                    }
                }
            });
        }
        if (id != "") {
            var url = CONSTRUCT_DETAIL_MODEL_URL + "/" + id + ".json?_method=get";
            var formData = loadJson(url);
            loadFormData(subform, formData);
            if (formData["parentButtonCode"]) {
                var parentButtonCodeCombo = subform.getCombo("parentButtonCode");
                parentButtonCodeCombo.clearAll(true);
                var parentButtonCodeComboUrl = CONSTRUCT_DETAIL_MODEL_URL + "!getParentButtonCodesOfReserveZone.json?constructId=" + currentConstructIdOfTree + "&reserveZoneId=" + currentReserveZoneId;
                var parentButtonCodeJsonObj = loadJson(parentButtonCodeComboUrl);
                if (parentButtonCodeJsonObj) {
                    var opt_data = [{text : "更多", value : "more"}];
                    for (var m = 0; m < parentButtonCodeJsonObj.length; m++) {
                        if (parentButtonCodeJsonObj[m].buttonCode == "more") {
                            continue;
                        }
                        opt_data.push({
                            text : parentButtonCodeJsonObj[m].buttonDisplayName,
                            value : parentButtonCodeJsonObj[m].buttonCode
                        });
                    }
                    parentButtonCodeCombo.addOption(opt_data);
                }
                subform.setItemValue("parentButtonCode", formData["parentButtonCode"]);
            }
        }
        subform.attachEvent("onButtonClick", function(buttonName) {
            if (buttonName == "save") {
                if (!subform.validate()) {
                    return;
                }
                var buttonType = subform.getItemValue("buttonType");
                var parentButtonCode = subform.getItemValue("parentButtonCode");
                if (buttonType == 2 && !parentButtonCode) {
                    dhtmlx.message("二级按钮类型的按钮必须选择所属按钮组！");
                    return;
                }
                var buttonImgCombo = subform.getCombo("buttonImg");
                if (buttonImgCombo && buttonImgCombo.getActualValue() != '' && buttonImgCombo.getComboText() == buttonImgCombo.getActualValue()) {
                    dhtmlx.message(getMessage("selected_not_exist", "按钮图标"));
                    return;
                }
                var constructDetailId = subform.getItemValue("id");
                var constructId = subform.getItemValue("constructId");
                var buttonDisplayName = subform.getItemValue("buttonDisplayName");
                var res = eval("("
                        + loadJson(CONSTRUCT_DETAIL_MODEL_URL + "!validateFields.json?id=" + constructDetailId
                                + "&constructId=" + constructId + "&reserveZoneId=" + currentReserveZoneId
                                + "&buttonDisplayName=" + encodeURIComponent(buttonDisplayName) + "&buttonType=" + buttonType) + ")");
                if (res.buttonDisplayNameExist) {
                    dhtmlx.message(getMessage("form_field_exist", "按钮显示名称"));
                }
                if (res.canotChangeButtonGroupToOther) {
                    dhtmlx.message("该按钮下存在二级按钮，不能改为其他按钮类型");
                    subform.setItemValue("buttonType", "1");
                }
                if (res.buttonDisplayNameExist || res.canotChangeButtonGroupToOther) {
                    return;
                }
                saveSubForm(subform);
            }
        });
    } else {
        if (currentReserveZoneType == "0") {
            subform.disableItem("parentButtonCode");
            // 初始化按钮图标
            initButtonImgCombo(subform);
            // 初始化构件下拉框
            initComponentVersionCombo(subform);
            // 初始化按钮类型下拉框和下拉按钮下拉框
            initButtonTypeCombo(subform);
            // 初始化页面组装类型事件
            subform.attachEvent("onChange", function(id, value) {
                if (id == "assembleType") {
                    if (value == "0") {
                        subform.enableItem("width");
                        subform.enableItem("height");
                    } else if (value == "1" || value == "2" || value == "3") {
                        subform.disableItem("width");
                        subform.disableItem("height");
                    }
                }
            });
            if (id != "") {
                var url = CONSTRUCT_DETAIL_MODEL_URL + "/" + id + ".json?_method=get";
                var formData = loadJson(url);
                loadFormData(subform, formData);
                var assembleType = formData["assembleType"];
                subform.checkItem("assembleType", formData["assembleType"]);
                if (assembleType == "0") {
                    subform.enableItem("width");
                    subform.enableItem("height");
                } else if (assembleType == "1" || assembleType == "2" || assembleType == "3") {
                    subform.disableItem("width");
                    subform.disableItem("height");
                }
                if (formData["parentButtonCode"]) {
                    var parentButtonCodeCombo = subform.getCombo("parentButtonCode");
                    parentButtonCodeCombo.clearAll(true);
                    var parentButtonCodeComboUrl = contextPath + "/construct/construct-detail!getParentButtonCodesOfReserveZone.json?constructId=" + currentConstructIdOfTree + "&reserveZoneId=" + currentReserveZoneId;
                    var parentButtonCodeJsonObj = loadJson(parentButtonCodeComboUrl);
                    if (parentButtonCodeJsonObj) {
                        var opt_data = [{text : "更多", value : "more"}];
                        for (var m = 0; m < parentButtonCodeJsonObj.length; m++) {
                            if (parentButtonCodeJsonObj[m].buttonCode == "more") {
                                continue;
                            }
                            opt_data.push({
                                text : parentButtonCodeJsonObj[m].buttonDisplayName,
                                value : parentButtonCodeJsonObj[m].buttonCode
                            });
                        }
                        parentButtonCodeCombo.addOption(opt_data);
                    }
                    subform.setItemValue("parentButtonCode", formData["parentButtonCode"]);
                }
            } else {
                subform.setItemValue("buttonCode", "CD_BUTTON_");
            }
            subform.attachEvent("onButtonClick", function(buttonName) {
                if (buttonName == "save") {
                    if (!subform.validate()) {
                        return;
                    }
                    var componentVersionIdCombo = subform.getCombo("componentVersionId");
                    var buttonType = subform.getItemValue("buttonType");
                    if (buttonType != 1 && isEmpty(subform.getItemValue("componentVersionId"))) {
                        dhtmlx.message("请选择构件！");
                        return;
                    }
                    if (isNotEmpty(componentVersionIdCombo.getComboText()) && componentVersionIdCombo.getComboText() == componentVersionIdCombo.getActualValue()) {
                        dhtmlx.message(getMessage("selected_not_exist", "构件"));
                        return;
                    }
                    var parentButtonCode = subform.getItemValue("parentButtonCode");
                    if (buttonType == 2 && !parentButtonCode) {
                        dhtmlx.message("二级按钮类型的按钮必须选择所属按钮组！");
                        return;
                    }
                    var buttonImgCombo = subform.getCombo("buttonImg");
                    if (buttonImgCombo && buttonImgCombo.getActualValue() != '' && buttonImgCombo.getComboText() == buttonImgCombo.getActualValue()) {
                        dhtmlx.message(getMessage("selected_not_exist", "按钮图标"));
                        return;
                    }
                    var buttonCode = subform.getItemValue("buttonCode");
                    if (!buttonCode.startWith("CD_BUTTON_")) {
                        dhtmlx.message("绑定构件的按钮编码必须以CD_BUTTON_开始！");
                        return;
                    }
                    var constructDetailId = subform.getItemValue("id");
                    var constructId = subform.getItemValue("constructId");
                    var componentVersionId = subform.getCombo("componentVersionId").getActualValue();
                    var buttonDisplayName = subform.getItemValue("buttonDisplayName");
                    var res = eval("("
                            + loadJson(CONSTRUCT_DETAIL_MODEL_URL + "!validateFields.json?id=" + constructDetailId
                                    + "&constructId=" + constructId + "&reserveZoneId=" + currentReserveZoneId
                                    + "&componentVersionId=" + componentVersionId + "&buttonCode=" + buttonCode + "&buttonDisplayName="
                                    + encodeURIComponent(buttonDisplayName) + "&buttonType=" + buttonType) + ")");
                    if (res.buttonDisplayNameExist) {
                        dhtmlx.message(getMessage("form_field_exist", "按钮显示名称"));
                    }
                    if (res.buttonCodeExist) {
                        dhtmlx.message(getMessage("form_field_exist", "按钮编码"));
                    }
                    if (!res.componentValid) {
                        dhtmlx.message("该构件和绑定的其他构件冲突，请重新选择构件！");
                    }
                    if (res.canotChangeButtonGroupToOther) {
                        dhtmlx.message("该按钮下存在二级按钮，不能改为其他按钮类型");
                        subform.setItemValue("buttonType", "1");
                    }
                    if (res.buttonCodeExist || res.buttonDisplayNameExist || !res.componentValid || res.canotChangeButtonGroupToOther) {
                        return;
                    }
                    saveSubForm(subform);
                }
            });
        } else if (currentReserveZoneType == "1") {
            // 初始化构件下拉框
            initComponentVersionCombo(subform);
            // 初始化按钮图标
            initButtonImgCombo(subform);
            // 初始化页面组装类型事件
            subform.attachEvent("onChange", function(id, value) {
                if (id == "assembleType") {
                    if (value == "0") {
                        subform.enableItem("width");
                        subform.enableItem("height");
                    } else if (value == "1" || value == "2" || value == "3") {
                        subform.disableItem("width");
                        subform.disableItem("height");
                    }
                }
            });
            if (id != "") {
                var url = CONSTRUCT_DETAIL_MODEL_URL + "/" + id + ".json?_method=get";
                var formData = loadJson(url);
                loadFormData(subform, formData);
                var assembleType = formData["assembleType"];
                subform.checkItem("assembleType", formData["assembleType"]);
                if (assembleType == "0") {
                    subform.enableItem("width");
                    subform.enableItem("height");
                } else if (assembleType == "1" || assembleType == "2" || assembleType == "3") {
                    subform.disableItem("width");
                    subform.disableItem("height");
                }
            } else {
                subform.setItemValue("buttonCode", "CD_BUTTON_");
            }
            subform.attachEvent("onButtonClick", function(buttonName) {
                if (buttonName == "save") {
                    if (!subform.validate()) {
                        return;
                    }
                    var componentVersionIdCombo = subform.getCombo("componentVersionId");
                    if (isNotEmpty(componentVersionIdCombo.getComboText()) && componentVersionIdCombo.getComboText() == componentVersionIdCombo.getActualValue()) {
                        dhtmlx.message(getMessage("selected_not_exist", "构件"));
                        return;
                    }
                    var buttonType = subform.getItemValue("buttonType");
                    var buttonCode = subform.getItemValue("buttonCode");
                    if (!buttonCode.startWith("CD_BUTTON_")) {
                        dhtmlx.message("绑定构件的按钮编码必须以CD_BUTTON_开始！");
                        return;
                    }
                    var constructDetailId = subform.getItemValue("id");
                    var componentVersionId = subform.getCombo("componentVersionId").getActualValue();
                    var buttonDisplayName = subform.getItemValue("buttonDisplayName");
                    var res = eval("("
                            + loadJson(CONSTRUCT_DETAIL_MODEL_URL + "!validateFields.json?id=" + constructDetailId
                                    + "&constructId=" + currentConstructIdOfTree + "&reserveZoneId=" + currentReserveZoneId
                                    + "&componentVersionId=" + componentVersionId + "&buttonCode=" + buttonCode + "&buttonDisplayName="
                                    + encodeURIComponent(buttonDisplayName) + "&buttonType=" + buttonType) + ")");
                    if (res.buttonDisplayNameExist) {
                        dhtmlx.message(getMessage("form_field_exist", "按钮显示名称"));
                    }
                    if (res.buttonCodeExist) {
                        dhtmlx.message(getMessage("form_field_exist", "按钮编码"));
                    }
                    if (!res.componentValid) {
                        dhtmlx.message("该构件和绑定的其他构件冲突，请重新选择构件！");
                    }
                    if (res.canotChangeButtonGroupToOther) {
                        dhtmlx.message("该按钮下存在二级按钮，不能改为其他按钮类型");
                        subform.setItemValue("buttonType", "1");
                    }
                    if (res.buttonCodeExist || res.buttonDisplayNameExist || !res.componentValid || res.canotChangeButtonGroupToOther) {
                        return;
                    }
                    saveSubForm(subform);
                }
            });
        } else if (currentReserveZoneType == "2") {
            // 初始化构件下拉框
            initComponentVersionCombo(subform);
            // 初始化页面组装类型事件
            subform.attachEvent("onChange", function(id, value) {
                if (id == "assembleType") {
                    if (value == "0") {
                        subform.enableItem("width");
                        subform.enableItem("height");
                    } else if (value == "1" || value == "2") {
                        subform.disableItem("width");
                        subform.disableItem("height");
                    }
                }
            });
            if (id != "") {
                var url = CONSTRUCT_DETAIL_MODEL_URL + "/" + id + ".json?_method=get";
                var formData = loadJson(url);
                loadFormData(subform, formData);
                var assembleType = formData["assembleType"];
                subform.checkItem("assembleType", formData["assembleType"]);
                if (assembleType == "0") {
                    subform.enableItem("width");
                    subform.enableItem("height");
                } else if (assembleType == "1" || assembleType == "2") {
                    subform.disableItem("width");
                    subform.disableItem("height");
                }
            }
            subform.attachEvent("onButtonClick", function(buttonName) {
                if (buttonName == "save") {
                    if (!subform.validate()) {
                        return;
                    }
                    var componentVersionIdCombo = subform.getCombo("componentVersionId");
                    if (isNotEmpty(componentVersionIdCombo.getComboText()) && componentVersionIdCombo.getComboText() == componentVersionIdCombo.getActualValue()) {
                        dhtmlx.message(getMessage("selected_not_exist", "构件"));
                        return;
                    }
                    var buttonType = subform.getItemValue("buttonType");
                    var constructDetailId = subform.getItemValue("id");
                    var componentVersionId = subform.getCombo("componentVersionId").getActualValue();
                    var res = eval("("
                            + loadJson(CONSTRUCT_DETAIL_MODEL_URL + "!validateFields.json?id=" + constructDetailId
                                    + "&constructId=" + currentConstructIdOfTree + "&reserveZoneId=" + currentReserveZoneId
                                    + "&componentVersionId=" + componentVersionId + "&buttonType=" + buttonType) + ")");
                    if (!res.componentValid) {
                        dhtmlx.message("该构件和绑定的其他构件冲突，请重新选择构件！");
                    }
                    if (res.bindingOnce) {
                        dhtmlx.message("该预留区下只能绑定一个构件！");
                    }
                    if (res.canotChangeButtonGroupToOther) {
                        dhtmlx.message("该按钮下存在二级按钮，不能改为其他按钮类型");
                        subform.setItemValue("buttonType", "1");
                    }
                    if (!res.componentValid || res.bindingOnce || res.canotChangeButtonGroupToOther) {
                        return;
                    }
                    saveSubForm(subform);
                }
            });
        } else if (currentReserveZoneType == "3") {
            // 初始化构件下拉框
            initComponentVersionCombo(subform);
            if (id != "") {
                var url = CONSTRUCT_DETAIL_MODEL_URL + "/" + id + ".json?_method=get";
                var formData = loadJson(url);
                loadFormData(subform, formData);
            }
            subform.attachEvent("onButtonClick", function(buttonName) {
                if (buttonName == "save") {
                    if (!subform.validate()) {
                        return;
                    }
                    var componentVersionIdCombo = subform.getCombo("componentVersionId");
                    if (isNotEmpty(componentVersionIdCombo.getComboText()) && componentVersionIdCombo.getComboText() == componentVersionIdCombo.getActualValue()) {
                        dhtmlx.message(getMessage("selected_not_exist", "构件"));
                        return;
                    }
                    var buttonType = subform.getItemValue("buttonType");
                    var constructDetailId = subform.getItemValue("id");
                    var componentVersionId = subform.getCombo("componentVersionId").getActualValue();
                    var res = eval("("
                            + loadJson(CONSTRUCT_DETAIL_MODEL_URL + "!validateFields.json?id=" + constructDetailId
                                    + "&constructId=" + currentConstructIdOfTree + "&reserveZoneId=" + currentReserveZoneId
                                    + "&componentVersionId=" + componentVersionId + "&buttonType=" + buttonType) + ")");
                    if (!res.componentValid) {
                        dhtmlx.message("该构件和绑定的其他构件冲突，请重新选择构件！");
                    }
                    if (res.bindingOnce) {
                        dhtmlx.message("该预留区下只能绑定一个构件！");
                    }
                    if (res.canotChangeButtonGroupToOther) {
                        dhtmlx.message("该按钮下存在二级按钮，不能改为其他按钮类型");
                        subform.setItemValue("buttonType", "1");
                    }
                    if (!res.componentValid || res.bindingOnce || res.canotChangeButtonGroupToOther) {
                        return;
                    }
                    saveSubForm(subform);
                }
            });
        } else if (currentReserveZoneType == "4") {
            // 初始化构件下拉框
            initComponentVersionCombo(subform);
            if (id != "") {
                var url = CONSTRUCT_DETAIL_MODEL_URL + "/" + id + ".json?_method=get";
                var formData = loadJson(url);
                loadFormData(subform, formData);
            }
            subform.attachEvent("onButtonClick", function(buttonName) {
                if (buttonName == "save") {
                    if (!subform.validate()) {
                        return;
                    }
                    var componentVersionIdCombo = subform.getCombo("componentVersionId");
                    if (isNotEmpty(componentVersionIdCombo.getComboText()) && componentVersionIdCombo.getComboText() == componentVersionIdCombo.getActualValue()) {
                        dhtmlx.message(getMessage("selected_not_exist", "构件"));
                        return;
                    }
                    var constructDetailId = subform.getItemValue("id");
                    var componentVersionId = subform.getCombo("componentVersionId").getActualValue();
                    var buttonCode = subform.getItemValue("buttonCode");
                    var buttonDisplayName = subform.getItemValue("buttonDisplayName");
                    var res = eval("("
                            + loadJson(CONSTRUCT_DETAIL_MODEL_URL + "!validateFields.json?id=" + constructDetailId
                                    + "&constructId=" + currentConstructIdOfTree + "&reserveZoneId=" + currentReserveZoneId
                                    + "&componentVersionId=" + componentVersionId + "&buttonCode=" + buttonCode + "&buttonDisplayName="
                                    + encodeURIComponent(buttonDisplayName)) + ")");
                    if (!res.componentValid) {
                        dhtmlx.message("该构件和绑定的其他构件冲突，请重新选择构件！");
                    }
                    if (res.buttonDisplayNameExist) {
                        dhtmlx.message(getMessage("form_field_exist", "标签页名称"));
                    }
                    if (res.buttonCodeExist) {
                        dhtmlx.message(getMessage("form_field_exist", "标签页编码"));
                    }
                    if (!res.componentValid || res.buttonDisplayNameExist || res.buttonCodeExist) {
                        return;
                    }
                    saveSubForm(subform);
                }
            });
        } else if (currentReserveZoneType == "5") {
            // 初始化构件下拉框
            initComponentVersionCombo(subform);
            if (id != "") {
                var url = CONSTRUCT_DETAIL_MODEL_URL + "/" + id + ".json?_method=get";
                var formData = loadJson(url);
                loadFormData(subform, formData);
            }
            subform.attachEvent("onButtonClick", function(buttonName) {
                if (buttonName == "save") {
                    if (!subform.validate()) {
                        return;
                    }
                    var componentVersionIdCombo = subform.getCombo("componentVersionId");
                    if (isNotEmpty(componentVersionIdCombo.getComboText()) && componentVersionIdCombo.getComboText() == componentVersionIdCombo.getActualValue()) {
                        dhtmlx.message(getMessage("selected_not_exist", "构件"));
                        return;
                    }
                    var constructDetailId = subform.getItemValue("id");
                    var componentVersionId = subform.getCombo("componentVersionId").getActualValue();
                    var buttonCode = subform.getItemValue("buttonCode");
                    var res = eval("("
                            + loadJson(CONSTRUCT_DETAIL_MODEL_URL + "!validateFields.json?id=" + constructDetailId
                                    + "&constructId=" + currentConstructIdOfTree + "&reserveZoneId=" + currentReserveZoneId
                                    + "&componentVersionId=" + componentVersionId + "&buttonCode=" + buttonCode) + ")");
                    if (!res.componentValid) {
                        dhtmlx.message("该构件和绑定的其他构件冲突，请重新选择构件！");
                    }
                    if (res.buttonCodeExist) {
                        dhtmlx.message(getMessage("form_field_exist", "条件"));
                    }
                    if (!res.componentValid || res.buttonCodeExist) {
                        return;
                    }
                    saveSubForm(subform);
                }
            });
        } else if (currentReserveZoneType == "TREE") {
            subform.setItemValue("treeNodeType", currentTreeNodeType);
            if (currentTreeNodeType == "0" || currentTreeNodeType == "1") {
                // 初始化构件下拉框
                initComponentVersionCombo(subform);
                if (id != "") {
                    var url = CONSTRUCT_DETAIL_MODEL_URL + "/" + id + ".json?_method=get";
                    var formData = loadJson(url);
                    loadFormData(subform, formData);
                }
                if (currentTreeNodeType == "0") {
                    subform.setItemValue("treeNodeProperty", "ROOT");
                }
                subform.attachEvent("onButtonClick", function(buttonName) {
                    if (buttonName == "save") {
                        if (!subform.validate()) {
                            return;
                        }
                        var componentVersionIdCombo = subform.getCombo("componentVersionId");
                        if (isNotEmpty(componentVersionIdCombo.getComboText()) && componentVersionIdCombo.getComboText() == componentVersionIdCombo.getActualValue()) {
                            dhtmlx.message(getMessage("selected_not_exist", "构件"));
                            return;
                        }
                        var constructDetailId = subform.getItemValue("id");
                        var componentVersionId = subform.getCombo("componentVersionId").getActualValue();
                        var treeNodeProperty = "";
                        if (currentTreeNodeType == "1") {
                            treeNodeProperty = subform.getItemValue("treeNodeProperty");
                        }
                        var res = eval("("
                                + loadJson(CONSTRUCT_DETAIL_MODEL_URL + "!validateFields.json?id=" + constructDetailId
                                        + "&constructId=" + currentConstructIdOfTree + "&reserveZoneId=" + currentReserveZoneId
                                        + "&componentVersionId=" + componentVersionId + "&treeNodeType=" + currentTreeNodeType
                                        + "&treeNodeProperty=" + treeNodeProperty) + ")");
                        if (!res.componentValid) {
                            dhtmlx.message("该构件和绑定的其他构件冲突，请重新选择构件！");
                        }
                        if (res.bindingOnce) {
                            dhtmlx.message("该预留区下只能绑定一个构件！");
                        }
                        if (res.treeNodePropertyBindingOnce) {
                            dhtmlx.message("该值只能绑定一个构件！");
                        }
                        if (!res.componentValid || res.bindingOnce || res.treeNodePropertyBindingOnce) {
                            return;
                        }
                        saveSubForm(subform);
                    }
                });
            } else if (currentTreeNodeType == "4" || currentTreeNodeType == "2") {
                // 初始化构件下拉框
                initComponentVersionCombo(subform);
                // 初始化树节点属性下拉框
                initTreeNodePropertyCombo(subform);
                if (id != "") {
                    var url = CONSTRUCT_DETAIL_MODEL_URL + "/" + id + ".json?_method=get";
                    var formData = loadJson(url);
                    loadFormData(subform, formData);
                }
                subform.attachEvent("onButtonClick", function(buttonName) {
                    if (buttonName == "save") {
                        if (!subform.validate()) {
                            return;
                        }
                        var componentVersionIdCombo = subform.getCombo("componentVersionId");
                        if (isNotEmpty(componentVersionIdCombo.getComboText()) && componentVersionIdCombo.getComboText() == componentVersionIdCombo.getActualValue()) {
                            dhtmlx.message(getMessage("selected_not_exist", "构件"));
                            return;
                        }
                        var constructDetailId = subform.getItemValue("id");
                        var componentVersionId = subform.getCombo("componentVersionId").getActualValue();
                        var treeNodeProperty = subform.getItemValue("treeNodeProperty");
                        var res = eval("("
                                + loadJson(CONSTRUCT_DETAIL_MODEL_URL + "!validateFields.json?id=" + constructDetailId
                                        + "&constructId=" + currentConstructIdOfTree + "&reserveZoneId=" + currentReserveZoneId
                                        + "&componentVersionId=" + componentVersionId + "&treeNodeType=" + currentTreeNodeType
                                        + "&treeNodeProperty=" + treeNodeProperty) + ")");
                        if (!res.componentValid) {
                            dhtmlx.message("该构件和绑定的其他构件冲突，请重新选择构件！");
                        }
                        if (res.treeNodePropertyBindingOnce) {
                            if (currentTreeNodeType == "4") {
                                dhtmlx.message("该字段标签只能绑定一个构件！");
                            } else {
                                dhtmlx.message("该表节点只能绑定一个构件！");
                            }
                        }
                        if (!res.componentValid || res.treeNodePropertyBindingOnce) {
                            return;
                        }
                        saveSubForm(subform);
                    }
                });
            } else if (currentTreeNodeType == "TREE") {
                // 初始化构件下拉框
                initComponentVersionCombo(subform);
                // 初始化树节点类型下拉框
                initTreeNodeTypeCombo(subform);
                if (id != "") {
                    var url = CONSTRUCT_DETAIL_MODEL_URL + "/" + id + ".json?_method=get";
                    var formData = loadJson(url);
                    loadFormData(subform, formData);
                }
                subform.attachEvent("onButtonClick", function(buttonName) {
                    if (buttonName == "save") {
                        if (!subform.validate()) {
                            return;
                        }
                        var componentVersionIdCombo = subform.getCombo("componentVersionId");
                        if (isNotEmpty(componentVersionIdCombo.getComboText()) && componentVersionIdCombo.getComboText() == componentVersionIdCombo.getActualValue()) {
                            dhtmlx.message(getMessage("selected_not_exist", "构件"));
                            return;
                        }
                        var constructDetailId = subform.getItemValue("id");
                        var componentVersionId = subform.getCombo("componentVersionId").getActualValue();
                        var treeNodeType = subform.getCombo("treeNodeType").getActualValue();
                        var treeNodeProperty = subform.getItemValue("treeNodeProperty");
                        var res = eval("("
                                + loadJson(CONSTRUCT_DETAIL_MODEL_URL + "!validateFields.json?id=" + constructDetailId
                                        + "&constructId=" + currentConstructIdOfTree + "&reserveZoneId=" + currentReserveZoneId
                                        + "&componentVersionId=" + componentVersionId + "&treeNodeType=" + treeNodeType
                                        + "&treeNodeProperty=" + treeNodeProperty) + ")");
                        if (!res.componentValid) {
                            dhtmlx.message("该构件和绑定的其他构件冲突，请重新选择构件！");
                        }
                        if (res.treeNodePropertyBindingOnce) {
                            dhtmlx.message("一种节点类型只能绑定一次构件！");
                        }
                        if (!res.componentValid || res.treeNodePropertyBindingOnce) {
                            return;
                        }
                        saveSubForm(subform);
                    }
                });
            }
        }
    }
}
/**
 * 初始化绑定的构件的下拉框
 */
function initComponentVersionCombo(subform) {
    var componentVersionCombo = subform.getCombo("componentVersionId");
    componentVersionCombo.clearAll(true);
    var componentVersionComboUrl = COMPONENT_VERSION_MODEL_URL + "!getBindingComponentVersionCombo.json?reserveZoneType=" + currentReserveZoneType + "&treeNodeType=" + currentTreeNodeType;
    if (releasedSystem) {
        componentVersionComboUrl += "&isSystemUsed=1";
    }
    var componentVersionJsonObj = loadJson(componentVersionComboUrl);
    if (componentVersionJsonObj && componentVersionJsonObj.length) {
        var opt_data = [{text:'请选择', value:''}];
        var i = 1;
        for (var m = 0; m < componentVersionJsonObj.length; m++) {
            if (componentVersionJsonObj[m][0] != currentAssembleComponentVersionIdOfTree && componentVersionJsonObj[m][0] != currentBaseComponentVersionIdOfTree) {
                opt_data[i] = {
                    text : componentVersionJsonObj[m][1],
                    value : componentVersionJsonObj[m][0]
                };
                i++;
            }
        }
        componentVersionCombo.addOption(opt_data);
    }
}
/**
 * 初始化按钮图标下拉框
 */
function initButtonImgCombo(subform) {
	var combo, url, data, opt_data;
	combo = subform.getCombo("buttonCls");
    if (combo) {
    	combo.clearAll(true);
        url = contextPath + "/construct/construct-detail!getButtonStyles.json?assembleComponentVersionId=" + currentAssembleComponentVersionIdOfTree;
        data = loadJson(url);
        if (data) {
            opt_data = [];
            for (var m = 0; m < data.length; m++) {
                opt_data[m] = {
                    text : data[m].name,
                    value : data[m].value
                };
            }
            combo.addOption(opt_data);
        }
    }
    combo = subform.getCombo("buttonIcon");
    if (combo) {
    	combo.clearAll(true);
        data = loadJson(contextPath + "/code/code!combobox.json?codeTypeCode=BUTTON_ICON_SET");
        if (data) {
            combo.addOption(data);
        }
    }
    
}
/**
 * 初始化按钮类型下拉框和相关的父按钮下拉框
 */
function initButtonTypeCombo(subform) {
    var buttonTypeCombo = subform.getCombo("buttonType");
    buttonTypeCombo.addOption([["0", "一级按钮"], ["1", "按钮组"], ["2", "二级按钮"]]);
    buttonTypeCombo.selectOption(0);
    buttonTypeCombo.attachEvent("onChange", function() {
        var parentButtonCodeCombo = subform.getCombo("parentButtonCode");
        var buttonType = this.getSelectedValue();
        if (buttonType == 0 || buttonType == 1) {
            parentButtonCodeCombo.clearAll(true);
            subform.disableItem("parentButtonCode");
        } else {
            subform.enableItem("parentButtonCode");
            parentButtonCodeCombo.clearAll(true);
            var parentButtonCodeComboUrl = contextPath + "/construct/construct-detail!getParentButtonCodesOfReserveZone.json?constructId=" + currentConstructIdOfTree + "&reserveZoneId=" + currentReserveZoneId;
            var parentButtonCodeJsonObj = loadJson(parentButtonCodeComboUrl);
            if (parentButtonCodeJsonObj) {
                var opt_data = [{text : "更多", value : "more"}];
                for (var m = 0; m < parentButtonCodeJsonObj.length; m++) {
                    if (parentButtonCodeJsonObj[m].buttonCode == "more") {
                        continue;
                    }
                    opt_data.push({
                        text : parentButtonCodeJsonObj[m].buttonDisplayName,
                        value : parentButtonCodeJsonObj[m].buttonCode
                    });
                }
                parentButtonCodeCombo.addOption(opt_data);
            }
        }
    });
}
/**
 * 初始化树节点属性下拉框，字段节点（跨表）对应字段标签下拉框，表节点对应表节点下拉框
 */
function initTreeNodePropertyCombo(subform) {
    var treeNodePropertyCombo = subform.getCombo("treeNodeProperty");
    treeNodePropertyCombo.clearAll(true);
    var treeNodePropertyComboUrl = CONSTRUCT_DETAIL_MODEL_URL + "!getTreeNodePropertyCombo.json?constructId=" + currentConstructIdOfTree + "&treeNodeType=" + currentTreeNodeType;
    var treeNodePropertyJsonObj = loadJson(treeNodePropertyComboUrl);
    if (treeNodePropertyJsonObj && treeNodePropertyJsonObj.length) {
        var assembleComponentVersionId = currentAssembleComponentVersionIdOfTree;
        var opt_data = [{text:'请选择', value:''}];
        var i = 1;
        for (var m = 0; m < treeNodePropertyJsonObj.length; m++) {
            if (treeNodePropertyJsonObj[m][0] != assembleComponentVersionId) {
                opt_data[i] = {
                    text : treeNodePropertyJsonObj[m][1],
                    value : treeNodePropertyJsonObj[m][0]
                };
                i++;
            }
        }
        treeNodePropertyCombo.addOption(opt_data);
    }
}
/**
 * 初始化树节点类型下拉框
 */
function initTreeNodeTypeCombo(subform) {
    var treeNodeTypeCombo = subform.getCombo("treeNodeType");
    treeNodeTypeCombo.clearAll(true);
    var opt_data = [{text:'请选择', value:''},{text:'空节点公用', value:'1'},{text:'表节点公用', value:'2'},{text:'空字段节点公用', value:'4'},{text:'物理表组节点公用', value:'5'}];
    treeNodeTypeCombo.addOption(opt_data);
}
/**
 * 保存构件绑定表单
 */
function saveSubForm(subform) {
    var constructDetailId = subform.getItemValue("id");
    if (constructDetailId == "") {
        SAVE_URL = CONSTRUCT_DETAIL_MODEL_URL;
        subform.setItemValue("_method", "post");
    } else {
        SAVE_URL = CONSTRUCT_DETAIL_MODEL_URL + "/" + constructDetailId;
        subform.setItemValue("_method", "put");
    }
    SAVE_URL += ".json";
    subform.send(SAVE_URL, "post", function(loader, response) {
        dhtmlx.message(getMessage("save_success"));
        var constructDetailObj = eval("(" + loader.xmlDoc.responseText + ")");
        constructDetailGridLoadData();
        constructDetailGrid.selectRowById(constructDetailObj.id);
        if (isEmpty(constructDetailGrid.cellById(constructDetailObj.id, 1).getValue())) {
            return;
        }
        configConstructDetail();
    });
}
/**
 * grid加载数据
 */
function constructDetailGridLoadData() {
    constructDetailGrid.clearAll();
    var url = CONSTRUCT_DETAIL_MODEL_URL + "!getConstructDetailList.json?E_model_name=datagrid&F_in="
            + constructDetailGridData.format.cols.toString() + "&P_UD="
            + constructDetailGridData.format.userDatas.toString() + "&constructId=" + currentConstructIdOfTree
            + "&reserveZoneId=" + currentReserveZoneId + "&treeNodeType=" + currentTreeNodeType;
    var loader = dhtmlxAjax.getSync(url);
    if (loader.xmlDoc.responseText == "")
        return;
    // 替换列表显示为“null” 的列 --wm
    var reg = new RegExp("\"null\"", "g");
    var loaderDoc = loader.xmlDoc.responseText;
    if (loaderDoc.indexOf("null") != -1) {
        var dataJson = loaderDoc.replace(reg, "\"\"");
    }
    var rows = eval('(' + dataJson + ')');
    // 如果查询结果为空
    if (rows.rows == undefined) {
        return;
    }
    constructDetailGrid.parse(rows, "json");
}
/**
 * 初始化constructDetailGridToolbar
 */
function initConstructDetailGridToolbar() {
    constructDetailGridToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    var index = 0;
    constructDetailGridToolbar.addButton("newDefault", index++, "新增预置按钮", "new.gif");
    constructDetailGridToolbar.addButton("new", index++, "新增", "new.gif");
    constructDetailGridToolbar.addButton("sync", index++, "添加", "new.gif");
    constructDetailGridToolbar.addButton("config", index++, "配置", "setup.gif");
    constructDetailGridToolbar.addButton("delete", index++, "删除", "delete.gif");
    constructDetailGridToolbar.addButton("copy", index++, "复制到预留区", "copy.gif");
    constructDetailGridToolbar.addButton("syncTo", index++, "复制到构件", "reassign.gif");
    constructDetailGridToolbar.addButton("comboboxSearch", index++, "下拉框检索配置", "setup.gif");
    constructDetailGridToolbar.attachEvent('onClick', function(id) {
        if (id == "newDefault") {
            if (!dhxWins) {
                dhxWins = new dhtmlXWindows();
            }
            var newDefaultButtonWin = dhxWins.createWindow("newDefaultWin", 0, 0, 800, 400);
            initDefaultButtonWin(newDefaultButtonWin);
        } else if (id == "new") {
            if (currentReserveZoneType == "TREE") {
                if (currentTreeNodeType == "5") {
                    initLogicGroupComWin();
                } else if (currentTreeNodeType == "0") {
                    var allRowIds = constructDetailGrid.getAllRowIds();
                    if (allRowIds) {
                        dhtmlx.message("根节点下只能绑定一个构件！");
                        return;
                    }
                    constructDetailGrid.addSubRow();
                } else {
                    constructDetailGrid.addSubRow();
                }
            } else {
                constructDetailGrid.addSubRow();
            }
        } else if (id == "config") {
            var selectId = constructDetailGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectId == "") {
                constructDetailGridLoadData();
                return;
            } else if (selectId.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            if (isEmpty(constructDetailGrid.cellById(selectId, 1).getValue())) {
                dhtmlx.message("没有绑定构件，无需配置！");
                return;
            }
            configConstructDetail();
        } else if (id == "delete") {
            var selectId = constructDetailGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectId == "") {
                constructDetailGridLoadData();
                return;
            }
            var selectIds = selectId.split(",");
            var message = getMessage("delete_warning");
            for (var i in selectIds) {
                if (constructDetailGrid.cells(selectIds[i], 3).getValue() == 1) {
                    message = "删除按钮组会同时删除其下的二级按钮，确定删除吗？";
                    break;
                }
            }
            dhtmlx.confirm({
                type : "confirm",
                text : message,
                ok : "确定",
                cancel : "取消",
                callback : function(flag) {
                    if (flag) {
                        DELETE_URL = CONSTRUCT_DETAIL_MODEL_URL + "/" + selectId + "?_method=delete";
                        dhtmlxAjax.get(DELETE_URL, function(loader) {
                            dhtmlx.message(getMessage("delete_success"));
                            constructDetailGridLoadData();
                        });
                    }
                }
            });
        } else if (id == "copy") {
            var selectId = constructDetailGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectId == "") {
                constructDetailGridLoadData();
                return;
            }
            if (!dhxWins) {
                dhxWins = new dhtmlXWindows();
            }
            var w =  800;
            var h =  600;
            if (h > document.body.clientHeight) {
                h = document.body.clientHeight;
            }
            var copyWin = dhxWins.createWindow("copyWin", 0, 0, w, h);
            initCopyWin(copyWin, selectId);
        } else if (id == "sync") {
            if (!dhxWins) {
                dhxWins = new dhtmlXWindows();
            }
            var w =  800;
            var h =  600;
            if (h > document.body.clientHeight) {
                h = document.body.clientHeight;
            }
            var syncButtonWin = dhxWins.createWindow("syncButtonWin", 0, 0, w, h);
            initSyncButtonWin(syncButtonWin);
        } else if (id == "syncTo") {
            var selectId = constructDetailGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectId == "") {
                constructDetailGridLoadData();
                return;
            }
            if (!dhxWins) {
                dhxWins = new dhtmlXWindows();
            }
            var w =  800;
            var h =  600;
            if (h > document.body.clientHeight) {
                h = document.body.clientHeight;
            }
            var syncButtonWin = dhxWins.createWindow("syncToButtonWin", 0, 0, w, h);
            initSyncButtonToWin(syncButtonWin, selectId);
        } else if (id == "comboboxSearch") {
            if (!dhxWins) {
                dhxWins = new dhtmlXWindows();
            }
            var comboboxSearchWin = dhxWins.createWindow("comboboxSearchWin", 0, 0, 420, 400);
            initComboboxSearchWin(comboboxSearchWin);
        }
    });
}
var changeFunctionOrCallback = false;
var configWins;
/**
 * 配置预留区的配置信息
 */
function configConstructDetail() {
    var selectId = constructDetailGrid.getSelectedRowId();
    if (selectId == undefined) {
        dhtmlx.message(getMessage("select_record"));
        return;
    } else if (selectId.indexOf(",") != -1) {
        dhtmlx.message(getMessage("select_only_one_record"));
        return;
    } else if (selectId == "") {
        constructDetailGridLoadData();
        return;
    } else if (isEmpty(constructDetailGrid.cellById(selectId, 1).getValue())) {
        dhtmlx.message("没有绑定构件，无需配置！");
        return;
    }
    configWins = new dhtmlXWindows();
    var configWin = configWins.createWindow("configWin", 0, 0, 1000, 400);
    configWin.setModal(true);
    configWin.setText("绑定构件配置");
    configWin.center();
    if (currentTreeNodeId == "CommonBinding") {
        configWins.attachEvent("onClose", function(win) {
            if (win.idd == "configWin") {
                dhtmlx.confirm({
                    type : "confirm",
                    text : "是否将该按钮的配置复制到相同编码的按钮?",
                    ok : "是",
                    cancel : "否",
                    callback : function(flag) {
                        if (flag) {
                            var res = eval("("
                                    + loadJson(CONSTRUCT_DETAIL_MODEL_URL + "!copyConstructDetailWithSameCode.json?constructDetailId="
                                            + selectId) + ")");
                            if (res.success) {
                                dhtmlx.message("复制成功！");
                            } else {
                                dhtmlx.message("复制失败！");
                            }
                        }
                    }
                });
            }
            return true;
        });
    }
    var configTabbar = configWin.attachTabbar();
    configTabbar.setImagePath(IMAGE_PATH);
    configTabbar.addTab("selfConfig", "构件自身参数设置", "120px");
    configTabbar.addTab("paramConfig", "构件传参设置", "120px");
    configTabbar.addTab("callbackConfig", "构件回调设置", "120px");
    configTabbar.addTab("beforeClickJs", "前置事件处理", "120px");
    configTabbar.setTabActive("selfConfig");
    configTabbar.attachEvent("onSelect", function(id, last_id) {
        var flag = true;
        if (changeFunctionOrCallback && (last_id == "paramConfig" || last_id == "callbackConfig")) {
            var t;
            var rowNum;
            if (last_id == "paramConfig") {
                t = "构件传参设置未保存，确定要离开吗？";
                rowNum = paramConfigGrid.getRowsNum();
            } else {
                t = "构件回调设置未保存，确定要离开吗？";
                rowNum = callbackConfigGrid.getRowsNum();
            }
            if (rowNum > 0) {
                dhtmlx.confirm({
                    type : "confirm",
                    text : t,
                    ok : "是",
                    cancel : "否",
                    callback : function(f) {
                        if (f) {
                            changeFunctionOrCallback = false;
                            configTabbar.setTabActive(id);
                        }
                    }
                });
                flag = false;
            }
        }
        return flag;
    });
    selfConfigLayout = configTabbar.cells("selfConfig").attachLayout("1C");
    initSelfConfigLayout();
    paramConfigLayout = configTabbar.cells("paramConfig").attachLayout("4W");
    initParamConfigLayout();
    callbackConfigLayout = configTabbar.cells("callbackConfig").attachLayout("4W");
    initCallbackConfigLayout();
    var beforeClickJsForm = configTabbar.cells("beforeClickJs").attachForm([{type:"input", name:"beforeClickJs", offsetLeft:10, offsetTop:5, rows:18, width:950}]);
    if (constructDetailForm && constructDetailForm.getItemValue("beforeClickJs")) {
        beforeClickJsForm.setItemValue("beforeClickJs", constructDetailForm.getItemValue("beforeClickJs"));
    } else {
        var url = CONSTRUCT_DETAIL_MODEL_URL + "/" + selectId + ".json?_method=get";
        var formData = loadJson(url);
        beforeClickJsForm.setItemValue("beforeClickJs", formData.beforeClickJs);
    }
    var beforeClickJsBar = configTabbar.cells("beforeClickJs").attachStatusBar();
    var beforeClickJsToolbar = new dhtmlXToolbarObject(beforeClickJsBar);
    beforeClickJsToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    beforeClickJsToolbar.setAlign("right");
    beforeClickJsToolbar.addButton("viewExample", 0, "&nbsp;&nbsp;查看示例&nbsp;&nbsp;");
    beforeClickJsToolbar.addSeparator("septr", 1);
    beforeClickJsToolbar.addButton("exportExample", 2, "&nbsp;&nbsp;导入示例&nbsp;&nbsp;");
    beforeClickJsToolbar.addSeparator("septr", 3);
    beforeClickJsToolbar.addButton("save", 4, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
    beforeClickJsToolbar.addSeparator("septr", 5);
    beforeClickJsToolbar.addButton("close", 6, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
    beforeClickJsToolbar.attachEvent('onClick', function(id) {
        if (id == "viewExample") {
            var exampleWin = configWins.createWindow("viewExample", 0, 0, 1000, 400);
            exampleWin.setModal(true);
            exampleWin.setText("前置事件处理示例");
            exampleWin.center();
            exampleWin.attachObject(getHelpContent());
            var exampleStatusBar = exampleWin.attachStatusBar();
            var exampleToolbar = new dhtmlXToolbarObject(exampleStatusBar);
            exampleToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
            exampleToolbar.setAlign("right");
            exampleToolbar.addButton("close", 0, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
            exampleToolbar.attachEvent('onClick', function(id) {
                if (id == "close") {
                    exampleWin.close();
                }
            });
        } else if (id == "exportExample") {
            beforeClickJsForm.setItemValue("beforeClickJs", beforeClickJsExample);
        } else if (id == "save") {
            var url = CONSTRUCT_DETAIL_MODEL_URL + "!saveBeforeClickJs.json";
            var params = "constructDetailId=" + constructDetailGrid.getSelectedRowId() + "&beforeClickJs=" + encodeURIComponent(beforeClickJsForm.getItemValue("beforeClickJs"));
            var loader = dhtmlxAjax.postSync(url, params);
            var msg = eval("(" + loader.xmlDoc.responseText + ")");
            dhtmlx.message(msg);
            constructDetailForm.setItemValue("beforeClickJs", beforeClickJsForm.getItemValue("beforeClickJs"));
        } else {
            configWins.window("configWin").close();
        }
    });
}
var selfConfigGridData = {
    format: {
        headers: ["&nbsp;", "<center>名称</center>", "<center>参数说明</center>", "<center>值</center>", ""],
        cols: ["id", "name", "remark", "text"],
        colWidths: ["30", "150", "250", "150", "*"],
        colTypes: ["sub_row_form", "ro", "ro", "ro", "ro"],
        colAligns: ["right", "left", "left", "left"],
        colTooltips: ["false", "true", "true", "true", "false"]
    }
};
var selfConfigFormData;
/**
 * 初始化绑定预留区的构件的自身配置layout
 */
function initSelfConfigLayout() {
    selfConfigLayout.cells('a').hideHeader();
    selfConfigGrid = selfConfigLayout.cells('a').attachGrid();
    selfConfigGrid.setImagePath(IMAGE_PATH);
    selfConfigGrid.setHeader(selfConfigGridData.format.headers.toString());
    selfConfigGrid.setInitWidths(selfConfigGridData.format.colWidths.toString());
    selfConfigGrid.setColTypes(selfConfigGridData.format.colTypes.toString());
    selfConfigGrid.setColAlign(selfConfigGridData.format.colAligns.toString());
    if (selfConfigGridData.format.colTooltips) {
        selfConfigGrid.enableTooltips(selfConfigGridData.format.colTooltips.toString());
    }
    selfConfigGrid.setSkin(Skin);
    selfConfigGrid.init();
    selfConfigGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    selfConfigGrid.attachEvent("onRowSelect", function(rId, cInd) {
        selfConfigGrid.cells(rId, 0).open();
    });
    selfConfigGrid.attachEvent("onBeforeSubFormLoadStruct", function(subform, selectId) {
        GET_BY_ID_URL = CONSTRUCT_DETAIL_SELF_PARAM_MODEL_URL + "/" + selectId + ".json?_method=get";
        var formObj = loadJson(GET_BY_ID_URL);
        if (formObj.type == "1") {
            selfConfigFormData = {
                format: [
                    {type: "block", width: "800", list:[
                        {type: "hidden", name: "id"},
                        {type: "input", label: "名称:", name: "name", readonly: true},
                        {type: "combo", label: "值:", name: "value", options: eval(formObj.options)},
                        {type: "newcolumn"},
                        {type: "block", width: "120", offsetTop:"22", list:[
                            {type: "button", name: "save", value: "保存", width:80}
                        ]}
                    ]}
                ],
                settings: {labelWidth: 80, inputWidth: 200}
            };
        } else if (formObj.type == "2") {
            selfConfigFormData = {
                format: [
                    {type: "block", width: "800", list:[
                        {type: "hidden", name: "id"},
                        {type: "input", label: "名称:", name: "name", readonly: true},
                        {type: "multiselect", label: "值:", name: "value", inputHeight:90, inputWidth:130, options: eval(formObj.options)},
                        {type: "newcolumn"},
                        {type: "block", width: "120", offsetTop:"22", list:[
                            {type: "button", name: "save", value: "保存", width:80}
                        ]}
                    ]}
                ],
                settings: {labelWidth: 80, inputWidth: 200}
            };
        } else {
            selfConfigFormData = {
                format: [
                    {type: "block", width: "800", list:[
                        {type: "hidden", name: "id"},
                        {type: "input", label: "名称:", name: "name", readonly: true},
                        {type: "input", label: "值:", name: "value", maxLength:200},
                        {type: "newcolumn"},
                        {type: "block", width: "120", offsetTop:"22", list:[
                            {type: "button", name: "save", value: "保存", width:80}
                        ]}
                    ]}
                ],
                settings: {labelWidth: 80, inputWidth: 200}
            };
        }
        subform.c = initDetailFormFormat(selfConfigFormData);
    });
    selfConfigGrid.attachEvent("onSubFormLoaded", function(subform, id, index) {
        var url = CONSTRUCT_DETAIL_SELF_PARAM_MODEL_URL + "/" + id + ".json?_method=get";
        loadForm(subform, url);
        subform.attachEvent("onButtonClick", function(buttonName) {
            if (buttonName == "save") {
                var id = subform.getItemValue("id");
                var value = subform.getItemValue("value");
                var valueType = subform.getItemType("value");
                var text;
                if (valueType == "combo") {
                    var valueCombo = subform.getCombo("value");
                    if (value == '') {
                        text = "";
                    } else {
                        text = valueCombo.getComboText();
                    }
                } else if (valueType == "multiselect") {
                    var k = [];
                    var opts = subform.getOptions("value");
                    for (var i = 0; i < opts.length; i++) {
                        if (opts[i].value == '') {
                            continue;
                        }
                        if (opts[i].selected) {
                            k.push(opts[i].text);
                        }
                    }
                    text = k.toString();
                } else {
                    text = subform.getItemValue("value");
                }
                dhtmlxAjax.get(CONSTRUCT_DETAIL_SELF_PARAM_MODEL_URL + "!saveConstructDetailSelfParam.json?id=" + id + "&value=" + encodeURIComponent(value) + "&text=" + encodeURIComponent(text), function() {
                    dhtmlx.message(getMessage("save_success"));
                    selfConfigGridLoadData();
                });
            }
        });
    });
    selfConfigGrid.attachEvent("onSubRowOpen", function(id, expanded) {
        if (expanded) {
            selfConfigGrid.forEachRow(function(rId) {
                if (id != rId) {
                    selfConfigGrid.cells(rId, 0).close();
                }
            });
        }
    });
    selfConfigGridLoadData();
}
/**
 * selfConfigGrid加载数据
 */
function selfConfigGridLoadData() {
    selfConfigGrid.clearAll();
    var url = CONSTRUCT_DETAIL_SELF_PARAM_MODEL_URL + "!search.json?E_model_name=datagrid&F_in=" + selfConfigGridData.format.cols.toString() + "&Q_EQ_constructDetailId=" + constructDetailGrid.getSelectedRowId();
    var loader = dhtmlxAjax.getSync(url);
    if (loader.xmlDoc.responseText == "")
        return;
    // 替换列表显示为“null” 的列 --wm
    var reg = new RegExp("\"null\"", "g");
    var loaderDoc = loader.xmlDoc.responseText;
    if (loaderDoc.indexOf("null") != -1) {
        var dataJson = loaderDoc.replace(reg, "\"\"");
    }
    var rows = eval('(' + dataJson + ')');
    // 如果查询结果为空
    if (rows.rows == undefined) {
        //dhtmlx.message("结果集为空！");
        return;
    }
    selfConfigGrid.parse(rows, "json");
}
var paramFunctionDataGridData = {
    format: {
        headers: ["<center>方法名</center>", "<center>方法介绍</center>", "<center>返回值名称</center>", "<center>返回值介绍</center>"],
        colWidths: ["120", "120", "120", "120"],
        colTypes: ["ro", "ro", "ro", "ro"],
        colAligns: ["left", "left", "left", "left"]
    }
};
var paramInputGridData = {
    format: {
        headers: ["<center>构件入参名称</center>", "<center>构件入参介绍</center>"],
        colWidths: ["120", "120"],
        colTypes: ["ro", "ro"],
        colAligns: ["left", "left"]
    }
};
var paramConfigGridData = {
    format: {
        headers: ["<center>构件入参名称</center>", "<center>构件入参介绍</center>", "<center>方法名</center>", "<center>方法名介绍</center>", "<center>返回值名称</center>", "<center>返回值介绍</center>"],
        colWidths: ["120", "120", "120", "120", "120", "120"],
        colTypes: ["ro", "ro", "ro", "ro", "ro", "ro"],
        colAligns: ["left", "left", "left", "left", "left", "left"]
    }
};
/**
 * 初始化绑定预留区的构件的参数配置layout
 */
function initParamConfigLayout() {
    paramConfigLayout.cells("a").setText("构件入参列表");
    paramConfigLayout.cells("b").setText("方法返回值列表");
    paramConfigLayout.cells("c").hideHeader();
    paramConfigLayout.cells("d").setText("参数配置列表");
    paramConfigLayout.cells("a").setWidth("170");
    paramConfigLayout.cells("b").setWidth("290");
    paramConfigLayout.cells("c").setWidth("85");
    paramInputGrid = paramConfigLayout.cells("a").attachGrid();
    paramFunctionDataGrid = paramConfigLayout.cells("b").attachGrid();
    paramConfigGrid = paramConfigLayout.cells("d").attachGrid();
    initParamGrid(paramInputGrid, paramInputGridData);
    initParamGrid(paramFunctionDataGrid, paramFunctionDataGridData);
    initParamGrid(paramConfigGrid, paramConfigGridData);
    initParamButtonForm();
    var constructDetailId = constructDetailGrid.getSelectedRowId();
    var moduleComponentVersionId = currentBaseComponentVersionIdOfTree;
    var pageComponentVersionId = constructDetailGrid.getUserData(constructDetailId, "componentVersionId");
    loadParamGridData(paramInputGrid, CONSTRUCT_FUNCTION_MODEL_URL + "!getInputParamList.json?E_model_name=datagrid&componentVersionId=" + pageComponentVersionId + "&constructDetailId=" + constructDetailId);
    loadParamGridData(paramFunctionDataGrid, CONSTRUCT_FUNCTION_MODEL_URL + "!getFunctionDataList.json?E_model_name=datagrid&componentVersionId=" + moduleComponentVersionId + "&constructDetailId=" + constructDetailId);
    loadParamGridData(paramConfigGrid, CONSTRUCT_FUNCTION_MODEL_URL + "!getConstructFunctionList.json?E_model_name=datagrid&constructDetailId=" + constructDetailId);
    var paramStatusBar = paramConfigLayout.attachStatusBar();
    var paramConfigToolbar = new dhtmlXToolbarObject(paramStatusBar);
    paramConfigToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    paramConfigToolbar.setAlign("right");
    paramConfigToolbar.addButton("clear", 0, "&nbsp;&nbsp;清空配置&nbsp;&nbsp;");
    paramConfigToolbar.addSeparator("septr", 1);
    paramConfigToolbar.addButton("save", 2, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
    paramConfigToolbar.addSeparator("septr", 3);
    paramConfigToolbar.addButton("close", 4, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
    paramConfigToolbar.attachEvent('onClick', function(id) {
        changeFunctionOrCallback = false;
        if (id == "clear") {
            var url = CONSTRUCT_FUNCTION_MODEL_URL + "!deleteConstructFunctions.json?constructDetailId=" + constructDetailId;
            dhtmlxAjax.get(url, function(loader) {
                var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                if (typeof jsonObj == 'string') {
                    jsonObj = eval("(" + jsonObj + ")");
                }
                if (jsonObj.success) {
                    dhtmlx.message({
                        text : getMessage("operate_success")
                    });
                } else {
                    dhtmlx.message({
                        text : getMessage("operate_failure")
                    });
                }
                loadParamGridData(paramInputGrid, CONSTRUCT_FUNCTION_MODEL_URL + "!getInputParamList.json?E_model_name=datagrid&componentVersionId=" + pageComponentVersionId + "&constructDetailId=" + constructDetailId);
                loadParamGridData(paramFunctionDataGrid, CONSTRUCT_FUNCTION_MODEL_URL + "!getFunctionDataList.json?E_model_name=datagrid&componentVersionId=" + moduleComponentVersionId + "&constructDetailId=" + constructDetailId);
                loadParamGridData(paramConfigGrid, CONSTRUCT_FUNCTION_MODEL_URL + "!getConstructFunctionList.json?E_model_name=datagrid&constructDetailId=" + constructDetailId);
            });
        } else if (id == "save") {
            var cnt = paramConfigGrid.getRowsNum();
            if (0 == cnt) {
                dhtmlx.message("请进行参数配置！");
                return;
            }
            var rowIds = "";
            paramConfigGrid.forEachRow(function(rowId) {
                rowIds += "," + rowId;
            });
            rowIds = rowIds.substring(1);
            var url = CONSTRUCT_FUNCTION_MODEL_URL + "!saveConstructFunction.json?rowIds=" + rowIds + "&constructDetailId=" + constructDetailId;
            dhtmlxAjax.get(url, function(loader) {
                var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                if (typeof jsonObj == 'string') {
                    jsonObj = eval("(" + jsonObj + ")");
                }
                if (jsonObj.success) {
                    dhtmlx.message({
                        text : getMessage("operate_success")
                    });
                } else {
                    dhtmlx.message({
                        text : getMessage("operate_failure")
                    });
                }
            });
        } else {
            configWins.window("configWin").close();
        }
    });
}
// 参数配置左右按钮表单
var paramButtonFormConfig = [{
    type:"label",
    list: [{
        type: "button",
        name: "toRight",
        value: "&gt;",
        width: "40"
    },{
        type: "button",
        name: "toLeft",
        value: "&lt;",
        width: "40"
    }]
}];
/**
 * 初始化参数配置左右按钮表单
 */
function initParamButtonForm() {
    var obj = document.getElementById("DIV-oparatorArea");
    if (obj == null) {
        obj = document.createElement("DIV");
        obj.setAttribute("id", "DIV-oparatorArea");
        obj.setAttribute("style", "position:relative;top:" + 35 + "%;left:20px;display: none;");
        document.body.appendChild(obj);
    } else {
        obj.innerHTML = "";
    }
    var buttonForm = new dhtmlXForm("DIV-oparatorArea", paramButtonFormConfig);
    paramConfigLayout.cells("c").attachObject("DIV-oparatorArea");
    buttonForm.attachEvent("onButtonClick", function(id) {
        changeFunctionOrCallback = true;
        if ("toRight" == id) {
            var inputId = paramInputGrid.getSelectedRowId();
            if (null == inputId)
                return;
            var inputName = paramInputGrid.cells(inputId, 0).getValue();
            var inputRemark = paramInputGrid.cells(inputId, 1).getValue();
            var functionDataId = paramFunctionDataGrid.getSelectedRowId();
            if (null == functionDataId)
                return;
            var functionName = paramFunctionDataGrid.cells(functionDataId, 0).getValue();
            var functionRemark = paramFunctionDataGrid.cells(functionDataId, 1).getValue();
            var functionDataName = paramFunctionDataGrid.cells(functionDataId, 2).getValue();
            var functionDataRemark = paramFunctionDataGrid.cells(functionDataId, 3).getValue();
            var configId = inputId + "-" + functionDataId;
            paramConfigGrid.addRow(configId, [inputName, inputRemark, functionName, functionRemark, functionDataName, functionDataRemark]);
            paramInputGrid.deleteSelectedRows();
        } else if ("toLeft" == id) {
            var rowId = paramConfigGrid.getSelectedRowId();
            if (null == rowId)
                return;
            var inputName = paramConfigGrid.cells(rowId, 0).getValue();
            var inputRemark = paramConfigGrid.cells(rowId, 1).getValue();
            var functionName = paramConfigGrid.cells(rowId, 2).getValue();
            var functionRemark = paramConfigGrid.cells(rowId, 3).getValue();
            var functionDataName = paramConfigGrid.cells(rowId, 4).getValue();
            var functionDataRemark = paramConfigGrid.cells(rowId, 5).getValue();
            var rId = rowId.split("-");
            paramInputGrid.addRow(rId[0], [inputName, inputRemark]);
            paramConfigGrid.deleteSelectedRows();
        }
    });
}
var callbackParamGridData = {
    format: {
        headers: ["<center>方法名</center>", "<center>方法介绍</center>", "<center>参数名称</center>", "<center>参数介绍</center>"],
        colWidths: ["120", "120", "120", "120"],
        colTypes: ["ro", "ro", "ro", "ro"],
        colAligns: ["left", "left", "left", "left"]
    }
};
var paramOutputGridData = {
    format: {
        headers: ["<center>构件出参名称</center>", "<center>构件出参介绍</center>"],
        colWidths: ["120", "120"],
        colTypes: ["ro", "ro"],
        colAligns: ["left", "left"]
    }
};
var callbackConfigGridData = {
    format: {
        headers: ["<center>方法名</center>", "<center>方法名介绍</center>", "<center>参数名称</center>", "<center>参数介绍</center>", "<center>构件出参名称</center>", "<center>构件出参介绍</center>"],
        colWidths: ["120", "120", "120", "120", "120", "120"],
        colTypes: ["ro", "ro", "ro", "ro", "ro", "ro"],
        colAligns: ["left", "left", "left", "left", "left", "left"]
    }
};
/**
 * 初始化绑定预留区的构件关闭时调用的方法配置layout
 */
function initCallbackConfigLayout() {
    callbackConfigLayout.cells("a").setText("回调函数列表");
    callbackConfigLayout.cells("b").setText("构件出参列表");
    callbackConfigLayout.cells("c").hideHeader();
    callbackConfigLayout.cells("d").setText("回调函数配置列表");
    callbackConfigLayout.cells("a").setWidth("290");
    callbackConfigLayout.cells("b").setWidth("170");
    callbackConfigLayout.cells("c").setWidth("85");
    callbackParamGrid = callbackConfigLayout.cells("a").attachGrid();
    paramOutputGrid = callbackConfigLayout.cells("b").attachGrid();
    callbackConfigGrid = callbackConfigLayout.cells("d").attachGrid();
    initParamGrid(callbackParamGrid, callbackParamGridData);
    initParamGrid(paramOutputGrid, paramOutputGridData);
    initParamGrid(callbackConfigGrid, callbackConfigGridData);
    initCallbackButtonForm();
    var constructDetailId = constructDetailGrid.getSelectedRowId();
    var moduleComponentVersionId = currentBaseComponentVersionIdOfTree;
    var pageComponentVersionId = constructDetailGrid.getUserData(constructDetailId, "componentVersionId");
    loadParamGridData(callbackParamGrid, CONSTRUCT_CALLBACK_MODEL_URL + "!getCallbackParamList.json?E_model_name=datagrid&componentVersionId=" + moduleComponentVersionId + "&constructDetailId=" + constructDetailId);
    loadParamGridData(paramOutputGrid, CONSTRUCT_CALLBACK_MODEL_URL + "!getOutputParamList.json?E_model_name=datagrid&componentVersionId=" + pageComponentVersionId);
    loadParamGridData(callbackConfigGrid, CONSTRUCT_CALLBACK_MODEL_URL + "!getConstructCallbackList.json?E_model_name=datagrid&constructDetailId=" + constructDetailId);
    var callbackStatusBar = callbackConfigLayout.attachStatusBar();
    var callbackConfigToolbar = new dhtmlXToolbarObject(callbackStatusBar);
    callbackConfigToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    callbackConfigToolbar.setAlign("right");
    callbackConfigToolbar.addButton("clear", 0, "&nbsp;&nbsp;清空配置&nbsp;&nbsp;");
    callbackConfigToolbar.addSeparator("septr", 1);
    callbackConfigToolbar.addButton("save", 2, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
    callbackConfigToolbar.addSeparator("septr", 3);
    callbackConfigToolbar.addButton("close", 4, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
    callbackConfigToolbar.attachEvent('onClick', function(id) {
        changeFunctionOrCallback = false;
        if (id == "clear") {
            var url = CONSTRUCT_CALLBACK_MODEL_URL + "!deleteConstructCallbacks.json?constructDetailId=" + constructDetailId;
            dhtmlxAjax.get(url, function(loader) {
                var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                if (typeof jsonObj == 'string') {
                    jsonObj = eval("(" + jsonObj + ")");
                }
                if (jsonObj.success) {
                    dhtmlx.message({
                        text : getMessage("operate_success")
                    });
                } else {
                    dhtmlx.message({
                        text : getMessage("operate_failure")
                    });
                }
                loadParamGridData(callbackParamGrid, CONSTRUCT_CALLBACK_MODEL_URL + "!getCallbackParamList.json?E_model_name=datagrid&componentVersionId=" + moduleComponentVersionId + "&constructDetailId=" + constructDetailId);
                loadParamGridData(paramOutputGrid, CONSTRUCT_CALLBACK_MODEL_URL + "!getOutputParamList.json?E_model_name=datagrid&componentVersionId=" + pageComponentVersionId);
                loadParamGridData(callbackConfigGrid, CONSTRUCT_CALLBACK_MODEL_URL + "!getConstructCallbackList.json?E_model_name=datagrid&constructDetailId=" + constructDetailId);
            });
        } else if (id == "save") {
            var cnt = callbackConfigGrid.getRowsNum();
            if (0 == cnt) {
                dhtmlx.message("请进行回调函数配置！");
                return;
            }
            var rowIds = "";
            callbackConfigGrid.forEachRow(function(rowId) {
                rowIds += "," + rowId;
            });
            rowIds = rowIds.substring(1);
            var url = CONSTRUCT_CALLBACK_MODEL_URL + "!saveConstructCallback.json?rowIds=" + rowIds + "&constructDetailId=" + constructDetailId;
            dhtmlxAjax.get(url, function(loader) {
                var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                if (typeof jsonObj == 'string') {
                    jsonObj = eval("(" + jsonObj + ")");
                }
                if (jsonObj.success) {
                    dhtmlx.message({
                        text : getMessage("operate_success")
                    });
                } else {
                    dhtmlx.message({
                        text : getMessage("operate_failure")
                    });
                }
            });
        } else {
            configWins.window("configWin").close();
        }
    });
}
/**
 *  初始化参数配置左右按钮表单
 */
function initCallbackButtonForm() {
    var obj = document.getElementById("DIV-oparatorArea1");
    if (obj == null) {
        obj = document.createElement("DIV");
        obj.setAttribute("id", "DIV-oparatorArea1");
        obj.setAttribute("style", "position:relative;top:" + 35 + "%;left:20px;display: none;");
        document.body.appendChild(obj);
    } else {
        obj.innerHTML = "";
    }
    var buttonForm = new dhtmlXForm("DIV-oparatorArea1", paramButtonFormConfig);
    callbackConfigLayout.cells("c").attachObject("DIV-oparatorArea1");
    buttonForm.attachEvent("onButtonClick", function(id) {
        changeFunctionOrCallback = true;
        if ("toRight" == id) {
            var callbackParamId = callbackParamGrid.getSelectedRowId();
            if (null == callbackParamId)
                return;
            var callbackName = callbackParamGrid.cells(callbackParamId, 0).getValue();
            var callbackRemark = callbackParamGrid.cells(callbackParamId, 1).getValue();
            var callbackParamName = callbackParamGrid.cells(callbackParamId, 2).getValue();
            var callbackParamRemark = callbackParamGrid.cells(callbackParamId, 3).getValue();
            var outputId = paramOutputGrid.getSelectedRowId();
            var outputName = "";
            var outputRemark = "";
            if (outputId) {
                outputName = paramOutputGrid.cells(outputId, 0).getValue();
                outputRemark = paramOutputGrid.cells(outputId, 1).getValue();
            }
            var configId = callbackParamId + "-" + outputId;
            if (callbackParamName) {
                callbackConfigGrid.addRow(configId, [callbackName, callbackRemark, callbackParamName, callbackParamRemark, outputName, outputRemark]);
            } else {
                callbackConfigGrid.addRow(configId, [callbackName, callbackRemark, "", "", "", ""]);
            }
            callbackParamGrid.deleteSelectedRows();
            paramOutputGrid.clearSelection();
        } else if ("toLeft" == id) {
            var rowId = callbackConfigGrid.getSelectedRowId();
            if (null == rowId)
                return;
            var callbackName = callbackConfigGrid.cells(rowId, 0).getValue();
            var callbackRemark = callbackConfigGrid.cells(rowId, 1).getValue();
            var callbackParamName = callbackConfigGrid.cells(rowId, 2).getValue();
            var callbackParamRemark = callbackConfigGrid.cells(rowId, 3).getValue();
            var rId = rowId.split("-");
            callbackParamGrid.addRow(rId[0] + "-" + rId[1], [callbackName, callbackRemark, callbackParamName, callbackParamRemark]);
            callbackConfigGrid.deleteSelectedRows();
        }
    });
}
var beforeClickJsExample = "function beforeClick(CFG_configInfo) {\n"
    + "    //获取输入参数的值\n"
    + "    var inputParams = CFG_configInfo.pageInputParams;\n"
    + "    var inputParamValue = inputParams[inputParamName];\n"
    + "    //调用后台逻辑处理\n"
    + "    var data = loadJson(url);\n"
    + "    //弹出confirm框，使用confirm框时beforeClick方法不用写return了，打开构件通过CFG_afterClickJs(CFG_configInfo)执行\n"
    + "    $.confirm('确定吗？', function(r) {\n"
    + "        if (r) {\n"
    + "            CFG_afterClickJs(CFG_configInfo);\n"
    + "        } else {\n"
    + "            CFG_message('没有执行！')\n"
    + "        }\n"
    + "    });\n"
    + "    ......\n"
    + "    //返回处理结果\n"
    + "    //return {success:true};\n"
    + "    return {success:false, message:\"检查失败！\"};\n"
    + "}";
/**
 * 前置事件处理示例
 * @returns {obj}
 */
function getHelpContent() {
    var obj = document.getElementById("DIV-clickJsHelp");
    if (null == obj) {
        obj = document.createElement("DIV");
        obj.setAttribute("id", "DIV-clickJsHelp");
        obj.setAttribute("style",
                "font-family: Tahoma; font-size: 11px;display: none;overflow-y:auto; overflow-x:auto;height:100%;"
        );
        obj.innerHTML = "<p>function beforeClick(CFG_configInfo) {</p>"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;//获取输入参数的值</p>"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;var inputParams = CFG_configInfo.pageInputParams;</p>"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;var inputParamValue = inputParams[inputParamName];</p>"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;//调用后台逻辑处理</p>"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;var data = loadJson(url);</p>"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;//弹出confirm框，使用confirm框时beforeClick方法不用写return了，打开构件通过CFG_afterClickJs(CFG_configInfo)执行</p>"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;$.confirm('确定吗？', function(r) {</p>"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (r) {</p>"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CFG_afterClickJs(CFG_configInfo);</p>"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;} else {</p>"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CFG_message('没有执行！')</p>"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}</p>"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;});</p>"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;......</p>"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;//返回处理结果</p>"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;//return {success:true};</p>"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;return {success:false, message:\"检查失败！\"};</p>"
                + "<p>}</p>";
    }
    return obj;
}
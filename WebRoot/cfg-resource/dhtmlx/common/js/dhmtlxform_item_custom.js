dhtmlXForm.prototype.items.tree = {
	// constructor
	obj : null,
	tree: null,
	render: function(item, data) {
		item._type = "tree";
		/* your custom code started here */
		item._enabled = true;
		this.doAddLabel(item, data);
		//
		var obj = document.createElement("DIV");
		obj.className = "dhxform_control";
		obj.id = "_$" + (new Date().getTime()) + "$_";
		obj.setAttribute("style", "margin-top: 3px; width: " + data.inputWidth + "px;");
		item.appendChild(obj);
		if (data.show != null)
		item.style.display = (data.show != null && data.show == false) ? "none" : "";
		//item.lastChild.innerHTML = data.my_text;
		//this._custom_inner_func(item);
		// you can insert not only text, but also any input, any code
		/* your custom code ended here */
		var rootName = data.root ? data.root : "树根节点";
		var treeJson = {id:0, item:[
            {id:'-1',text: rootName, child:1}
        ]};
		var tree = new dhtmlXTreeObject(obj.id,"100%","100%",0);
		tree.setImagePath(IMAGE_PATH + "csh_bluefolders/");
		tree.setDataMode("json");
		tree.enableSmartXMLParsing(true);
		tree.setXMLAutoLoading(data.url);
		tree.loadJSONObject(treeJson);
		tree.refreshItem("-1"); 
		
		tree.attachEvent("onClick", function(id) {
			item._value = id;
		});
		
		this.tree = tree;
		this.obj  = obj;
 
		return this;
	},
	// destructor
	destruct: function(item) {
		/* your custom code is started here */
		this._custom_inner_func2(item);
		if (this.tree) {
			this.tree.destructor();
		}
		
		item.innerHTML = "";
		/* your custom code is ended here */
	},
	// enables item
	enable: function(item) {
		/* your custom code is started here */
		item.lastChild.style.color = "black";
		item._is_enabled = true;
		/* your custom code is ended here */
	},
	// disables item
	disable: function(item) {
		/* your custom code is started here */
		item.lastChild.style.color = "gray";
		item._is_enabled = false;
		/* your custom code is ended here */
	},
	// to the basic code below  you can add your custom code
	setValue: function(item, val) {
		item._value = val;
	},
 
	getValue: function(item) {
		return item._value;
	},
	// your custom functionality
	_custom_inner_func: function(item) {
		item.lastChild.onclick = function(){
			if (this.parentNode._is_enabled) {
				item._value = this.tree.getSelectedItemId();
			}
		};
	},
	_custom_inner_func2: function(item) {
		item.lastChild.onclick = null;
	},
	
	getItemTree: function(item) {
		return this.tree;
	},
		
	doAddLabel: function(item, data) {
		
		var t = document.createElement("DIV");
		t.className = "dhxform_label "+data.labelAlign;
		
		if (data.wrap == true) t.style.whiteSpace = "normal";
		
		if (item._ll) {
			item.insertBefore(t,item.firstChild);
		} else {
			item.appendChild(t);
		}
		
		if (typeof(data.tooltip) != "undefined") t.title = data.tooltip;
		
		t.innerHTML = "<div class='dhxform_label_nav_link' "+
				"onfocus='if(this.parentNode.parentNode._updateImgNode)this.parentNode.parentNode._updateImgNode(this.parentNode.parentNode,true);' "+
				"onblur='if(this.parentNode.parentNode._updateImgNode)this.parentNode.parentNode._updateImgNode(this.parentNode.parentNode,false);' "+
				"onkeypress='e=event||window.arguments[0];if(e.keyCode==32||e.charCode==32){e.cancelBubble=true;e.returnValue=false;_dhxForm_doClick(this,\"mousedown\");return false;}' "+
				(_dhxForm_isIPad?"ontouchstart='e=event;e.preventDefault();_dhxForm_doClick(this,\"mousedown\");' ":"")+
				"role='link' tabindex='0'>"+data.label+(data.info?"<span class='dhxform_info'>[?]</span>":"")+(item._required?"<span class='dhxform_item_required'>*</span>":"")+'</div>';
		
		if (!isNaN(data.labelWidth)) t.firstChild.style.width = parseInt(data.labelWidth)+"px";
		if (!isNaN(data.labelHeight)) t.firstChild.style.height = parseInt(data.labelHeight)+"px";
		
		if (!isNaN(data.labelLeft)) t.style.left = parseInt(data.labelLeft)+"px";
		if (!isNaN(data.labelTop)) t.style.top = parseInt(data.labelTop)+"px";
		
	}
};

dhtmlXForm.prototype.getItemTree = function(name) {
	return this.doWithItem(name, "getItemTree");
};
 
/* itemlabel */
dhtmlXForm.prototype.items.itemlabel = {
	
	_index: false,
	
	render: function(item, data) {
		
		item._type = "itemlabel";
		item._enabled = true;
		item._checked = true;
		var t = document.createElement("DIV");
		t.className = "dhxform_label"+(data._isTopmost?" topmost":"");
		if (data.labelAlign) {
			t.className += " " + data.labelAlign; 
		}
		t.innerHTML = data.label;
		item.appendChild(t);
		
		if (data.hidden == true) this.hide(item);
		if (data.disabled == true) this.userDisable(item);
		
		if (!isNaN(data.labelWidth)) t.style.width = parseInt(data.labelWidth)+"px";
		if (!isNaN(data.labelHeight)) t.style.height = parseInt(data.labelHeight)+"px";
		
		if (!isNaN(data.labelLeft)) t.style.left = parseInt(data.labelLeft)+"px";
		if (!isNaN(data.labelTop)) t.style.top = parseInt(data.labelTop)+"px";
		
		return this;
	},
	
	destruct: function(item) {
		
		//this.doUnloadNestedLists(item);
		
		item._autoCheck = null;
		item._enabled = null;
		item._type = null;
		
		item.callEvent = null;
		item.checkEvent = null;
		item.getForm = null;
		
		item.onselectstart = null;
		item.parentNode.removeChild(item);
		item = null;
		
	},
	
	enable: function(item) {
		if (String(item.className).search("disabled") >= 0) item.className = String(item.className).replace(/disabled/gi,"");
		item._enabled = true;
	},
	
	disable: function(item) {
		if (String(item.className).search("disabled") < 0) item.className += " disabled";
		item._enabled = false;
	},
	
	setText: function(item, text) {
		item.firstChild.innerHTML = text;
	},

	getText: function(item) {
		return item.firstChild.innerHTML;
	}
	
};

/* img */
dhtmlXForm.prototype.items.img = {
	
	_index: false,
	
	render: function(item, data) {
		
		item._type = "img";
		item._enabled = true;
		item._checked = true;
		var t = document.createElement("DIV");
		t.className = "dhxform_label"+(data._isTopmost?" topmost":"");
		if (data.labelAlign) {
			t.className += " " + data.labelAlign; 
		}
		if (data.src == undefined || null == data.src) {
			data.src = dhx_globalImgPath + "imageloaderror.gif";
		}
		var innerHtml = "<img src=\"" + data.src + "\"";
		if (undefined != data.alt && null != data.alt) {
			innerHtml += " alt=\"" + data.alt + "\"";
		}
		innerHtml += "/>";
		t.innerHTML = innerHtml;
		item.appendChild(t);
		
		if (data.hidden == true) this.hide(item);
		if (data.disabled == true) this.userDisable(item);
		
		if (!isNaN(data.width)) t.style.width = parseInt(data.width)+"px";
		if (!isNaN(data.height)) t.style.height = parseInt(data.height)+"px";
		
		if (!isNaN(data.offsetLeft)) t.style.left = parseInt(data.offsetLeft)+"px";
		if (!isNaN(data.offsetTop)) t.style.top = parseInt(data.offsetTop)+"px";
		
		return this;
	},
	
	destruct: function(item) {
		
		//this.doUnloadNestedLists(item);
		
		item._autoCheck = null;
		item._enabled = null;
		item._type = null;
		
		item.callEvent = null;
		item.checkEvent = null;
		item.getForm = null;
		
		item.onselectstart = null;
		item.parentNode.removeChild(item);
		item = null;
		
	},
	
	enable: function(item) {
		if (String(item.className).search("disabled") >= 0) item.className = String(item.className).replace(/disabled/gi,"");
		item._enabled = true;
	},
	
	disable: function(item) {
		if (String(item.className).search("disabled") < 0) item.className += " disabled";
		item._enabled = false;
	},
	
	setText: function(item, text) {
		//item.firstChild.innerHTML = text;
	},

	getText: function(item) {
		//return item.firstChild.innerHTML;
	}
	
};

/* itemdiv */
dhtmlXForm.prototype.items.itemdiv = {
	
	_index: false,
	
	render: function(item, data) {
		
		item._type = "itemdiv";
		item._enabled = true;
		item._checked = true;
		var t = document.createElement("DIV");
		//t.className = "dhxform_label"+(data._isTopmost?" topmost":"");
		//t.className = "dhxform_control";
		t.className = "dhxform_label" + (data._isTopmost ? " topmost" : "");
		if (data.labelAlign) {
			t.className += " " + data.labelAlign; 
		}
		t.innerHTML = data.value;
		item.appendChild(t);
		
		if (data.hidden == true) this.hide(item);
		if (data.disabled == true) this.userDisable(item);
		
		if (!isNaN(data.width)) t.style.width = parseInt(data.width)+"px";
		if (!isNaN(data.height)) t.style.height = parseInt(data.height)+"px";
		
		if (!isNaN(data.labelLeft)) t.style.left = parseInt(data.labelLeft)+"px";
		if (!isNaN(data.labelTop)) t.style.top = parseInt(data.labelTop)+"px";
		
		return this;
	},
	
	destruct: function(item) {
		
		//this.doUnloadNestedLists(item);
		
		item._autoCheck = null;
		item._enabled = null;
		item._type = null;
		
		item.callEvent = null;
		item.checkEvent = null;
		item.getForm = null;
		
		item.onselectstart = null;
		item.parentNode.removeChild(item);
		item = null;
		
	},
	
	enable: function(item) {
		if (String(item.className).search("disabled") >= 0) item.className = String(item.className).replace(/disabled/gi,"");
		item._enabled = true;
	},
	
	disable: function(item) {
		if (String(item.className).search("disabled") < 0) item.className += " disabled";
		item._enabled = false;
	},
	
	setText: function(item, text) {
		//item.firstChild.innerHTML = text;
	},

	getText: function(item) {
		//return item.firstChild.innerHTML;
	},
	
	setValue: function(item, val) {
		item.firstChild.innerHTML = val;
	},
 
	getValue: function(item) {
		return item.firstChild.innerHTML;
	}
	
};

/* subfield */
dhtmlXForm.prototype.items.subfield = {
	
	_index: false,
	
	render: function(item, data) {
		
		item._type = "subfield";
		item._enabled = true;
		item._checked = true;
		var t = document.createElement("DIV");
		t.className = "dhxform_subfield" + (data._isTopmost ? " topmost" : "");
		t.innerHTML = "<fieldset><legend>" + data.label + "</legend></fieldset>";
		item.appendChild(t);
		
		item.appendChild(t);
		
		if (data.hidden == true) this.hide(item);
		if (data.disabled == true) this.userDisable(item);

		if (!isNaN(data.width)) t.style.width = parseInt(data.width)+"px";
		
		return this;
	},
	
	destruct: function(item) {
		
		//this.doUnloadNestedLists(item);
		
		item._autoCheck = null;
		item._enabled = null;
		item._type = null;
		
		item.callEvent = null;
		item.checkEvent = null;
		item.getForm = null;
		
		item.onselectstart = null;
		item.parentNode.removeChild(item);
		item = null;
		
	},
	
	enable: function(item) {
		if (String(item.className).search("disabled") >= 0) item.className = String(item.className).replace(/disabled/gi,"");
		item._enabled = true;
	},
	
	disable: function(item) {
		if (String(item.className).search("disabled") < 0) item.className += " disabled";
		item._enabled = false;
	},
	
	setText: function(item, text) {
		item.firstChild.innerHTML = text;
	},

	getText: function(item) {
		return item.firstChild.innerHTML;
	}
	
};

/* placeholder */
dhtmlXForm.prototype.items.placeholder = {
	
	render: function(item, data) {
		
		item._type = "placeholder";
		item._enabled = true;
		data.label = "&nbsp;";
		this.doAddLabel(item, data);
		this.doAddInput(item, data, "DIV", null, true, true, "dhxform_item_placeholder");
		
		item._value = "";
		return this;
		
	},
	
	doAddInput: function(item, data, el, type, pos, dim, css) {
		
		var p = document.createElement("DIV");
		p.className = "dhxform_control";
		
		if (item._ll) {
			item.appendChild(p);
		} else {
			item.insertBefore(p,item.firstChild);
		}
		
		var t = document.createElement(el);
		t.className = css;
		t.name = item._idd;
		t._idd = item._idd;
		t.id = data.uid;
		t.innerHTML = "&nbsp;";
		
		p.appendChild(t);
		
		if (data.inputWidth) {
			t.style.width = parseInt(data.inputWidth)+"px";
		}
	},
	
	// destruct should be added,
	// item.format also should be cleared
	
	setValue: function(item, value) {
		
	},
	
	getValue: function(item) {
		return "";
	},
	
	enable: function(item) {
		item._enabled = true;
	},
	
	disable: function(item) {
		item._enabled = false;
	}
	
};

(function(){
	for (var a in {doAddLabel:1,destruct:1,setWidth:1})
		dhtmlXForm.prototype.items.placeholder[a] = dhtmlXForm.prototype.items.select[a];
})();


/* checkboxlist */
dhtmlXForm.prototype.items.checkboxlist = {
	
	render: function(item, data) {
		
		item._type = "checkboxlist";
		item._enabled = true;
		
		if (data._autoInputWidth !== false) data.inputWidth = 14;

		this.doAddLabel(item, data);
		this.doAddInput(item, data, "INPUT", "TEXT", true, true, "dhxform_textarea");

		//item._value = value;
		
		return this;
		
	},
	
	doAddLabel: function(item, data) {
		
		var t = document.createElement("DIV");
		t.className = "dhxform_label " + data.labelAlign;
		
		if (data.wrap == true) t.style.whiteSpace = "normal";
		
		if (item._ll) {
			item.insertBefore(t,item.firstChild);
		} else {
			item.appendChild(t);
		}
		
		if (typeof(data.tooltip) != "undefined") t.title = data.tooltip;
		
		t.innerHTML = "<div class='dhxform_label_nav_link' "+
				"onfocus='if(this.parentNode.parentNode._updateImgNode)this.parentNode.parentNode._updateImgNode(this.parentNode.parentNode,true);' "+
				"onblur='if(this.parentNode.parentNode._updateImgNode)this.parentNode.parentNode._updateImgNode(this.parentNode.parentNode,false);' "+
				"onkeypress='e=event||window.arguments[0];if(e.keyCode==32||e.charCode==32){e.cancelBubble=true;e.returnValue=false;_dhxForm_doClick(this,\"mousedown\");return false;}' "+
				(_dhxForm_isIPad?"ontouchstart='e=event;e.preventDefault();_dhxForm_doClick(this,\"mousedown\");' ":"")+
				"role='link' tabindex='0'>"+data.label+(data.info?"<span class='dhxform_info'>[?]</span>":"")+(item._required?"<span class='dhxform_item_required'>*</span>":"")+'</div>';
		
		if (!isNaN(data.labelWidth)) t.firstChild.style.width = parseInt(data.labelWidth)+"px";
		if (!isNaN(data.labelHeight)) t.firstChild.style.height = parseInt(data.labelHeight)+"px";
		
		if (!isNaN(data.labelLeft)) t.style.left = parseInt(data.labelLeft)+"px";
		if (!isNaN(data.labelTop)) t.style.top = parseInt(data.labelTop)+"px";
		
	},
	
	doAddInput: function(item, data, el, type, pos, dim, css) {

		var dataArr = null;
		console.log("radiolist url: " + data.url);
		if (isNotEmpty(data.url)) {
			dataArr = loadJson(data.url);
		}
		if (!dataArr || !(dataArr instanceof Array)) {
			dataArr = data.data;
		}
		//var value = "";
		var i = 0, len = dataArr.length;
		var border = document.createElement("DIV");
		border.className = "dhxform_control";
		if (item._ll) {
			item.appendChild(border);
		} else {
			item.insertBefore(border,item.firstChild);
		}
		if (data.width) border.style.cssText = "width:" + data.width + "px;";
		for (; i < len; i++) {
			if (isEmpty(dataArr[i].value)) continue;
			var p = document.createElement("DIV");
			p.className = "dhxform_control dhxform_img_node";
			
			if (item._ll) {
				border.appendChild(p);
			} else {
				border.insertBefore(p,border.firstChild);
			}
			
			var t = document.createElement(el);
			t.className = css;
			t.name = item._idd + "_" + i;
			t._idd = item._idd + "_" + i;
			t.id = data.uid + "_" + i;
			
			if (typeof(type) == "string") t.type = type;
			
			if (el == "INPUT" || el == "TEXTAREA") {
				t.onkeyup = function(e) {
					e = e||event;
					item.callEvent("onKeyUp",[this,e]);
				};
			}
			
			p.appendChild(t);
			var d = document.createElement("DIV");
			d.className = "dhxform_img  chbx0";
			p.appendChild(d);

			var text = document.createElement("SPAN");
			text.innerHTML = dataArr[i].text;
			if (len > 1 && i < len - 1) text.style.cssText = "padding-right:10px;";
			p.appendChild(text);
			//if (data.readonly) this.setReadonly(item, true);
			//if (data.hidden == true) this.hide(item);
			//if (data.disabled == true) this.userDisable(item);
			
			if (pos) {
				if (!isNaN(data.inputLeft)) p.style.left = parseInt(data.inputLeft)+"px";
				if (!isNaN(data.inputTop)) p.style.top = parseInt(data.inputTop)+"px";
			}
			
			var u = "";
			
			if (dim) {
				if (!isNaN(data.inputWidth)) u += "width:"+parseInt(data.inputWidth)+"px;";
				if (!isNaN(data.inputHeight)) u += "height:"+parseInt(data.inputHeight)+"px;";
				
			}
			if (typeof(data.style) == "string") u += data.style;
			t.style.cssText = u;
		}
		
	},
	
	enable : function() {
		
	},
	
	disable : function () {}
	
};

(function(){
	for (var a in {doDestruct:1,doUnloadNestedLists:1,doAttachEvents:1,check:1,unCheck:1,isChecked:1,isEnabled:1,setText:1,getText:1,getValue:1,setReadonly:1,isReadonly:1})
		dhtmlXForm.prototype.items.checkboxlist[a] = dhtmlXForm.prototype.items.checkbox[a];
})();


/* checkboxlist */
dhtmlXForm.prototype.items.radiolist = {
	
	render: function(item, data) {
		
		item._type = "radiolist";
		item._enabled = true;
		//var value = "";
		var dataArr = null;
		if (data._autoInputWidth !== false) data.inputWidth = 14;

		this.doAddLabel(item, data);
		this.doAddInput(item, data, "INPUT", "TEXT", true, true, "dhxform_textarea");

		//item._value = value;
		
		return this;
		
	},
	
	doAddLabel: function(item, data) {
		
		var t = document.createElement("DIV");
		t.className = "dhxform_label " + data.labelAlign;
		
		if (data.wrap == true) t.style.whiteSpace = "normal";
		
		if (item._ll) {
			item.insertBefore(t,item.firstChild);
		} else {
			item.appendChild(t);
		}
		
		if (typeof(data.tooltip) != "undefined") t.title = data.tooltip;
		
		t.innerHTML = "<div class='dhxform_label_nav_link' "+
				"onfocus='if(this.parentNode.parentNode._updateImgNode)this.parentNode.parentNode._updateImgNode(this.parentNode.parentNode,true);' "+
				"onblur='if(this.parentNode.parentNode._updateImgNode)this.parentNode.parentNode._updateImgNode(this.parentNode.parentNode,false);' "+
				"onkeypress='e=event||window.arguments[0];if(e.keyCode==32||e.charCode==32){e.cancelBubble=true;e.returnValue=false;_dhxForm_doClick(this,\"mousedown\");return false;}' "+
				(_dhxForm_isIPad?"ontouchstart='e=event;e.preventDefault();_dhxForm_doClick(this,\"mousedown\");' ":"")+
				"role='link' tabindex='0'>"+data.label+(data.info?"<span class='dhxform_info'>[?]</span>":"")+(item._required?"<span class='dhxform_item_required'>*</span>":"")+'</div>';
		
		if (!isNaN(data.labelWidth)) t.firstChild.style.width = parseInt(data.labelWidth)+"px";
		if (!isNaN(data.labelHeight)) t.firstChild.style.height = parseInt(data.labelHeight)+"px";
		
		if (!isNaN(data.labelLeft)) t.style.left = parseInt(data.labelLeft)+"px";
		if (!isNaN(data.labelTop)) t.style.top = parseInt(data.labelTop)+"px";
		
	},
	
	doAddInput: function(item, data, el, type, pos, dim, css) {
		
		var dataArr = null;

		console.log("radiolist url: " + data.url);
		if (isNotEmpty(data.url)) {
			dataArr = loadJson(data.url);
		}
		if (!dataArr || !(dataArr instanceof Array)) {
			dataArr = data.data;
		}
		var i = 0, len = dataArr.length;
		var border = document.createElement("DIV");
		border.className = "dhxform_control";
		if (item._ll) {
			item.appendChild(border);
		} else {
			item.insertBefore(border,item.firstChild);
		}
		if (data.width) border.style.cssText = "width:" + data.width + "px;";
		for (; i < len; i++) {
			if (isEmpty(dataArr[i].value)) continue;
			var p = document.createElement("DIV");
			p.className = "dhxform_control dhxform_img_node";
			
			if (item._ll) {
				border.appendChild(p);
			} else {
				border.insertBefore(p,border.firstChild);
			}
			
			var t = document.createElement(el);
			t.className = css;
			t.name = item._idd + "_" + i;
			t._idd = item._idd + "_" + i;
			t.id = data.uid + "_" + i;
			
			if (typeof(type) == "string") t.type = type;
			
			if (el == "INPUT" || el == "TEXTAREA") {
				t.onkeyup = function(e) {
					e = e||event;
					item.callEvent("onKeyUp",[this,e]);
				};
			}
			
			p.appendChild(t);
			var d = document.createElement("DIV");
			d.className = "dhxform_img rdbt0";
			p.appendChild(d);

			var text = document.createElement("SPAN");
			text.innerHTML = dataArr[i].text;
			if (len > 1 && i < len - 1) text.style.cssText = "padding-right:10px;";
			p.appendChild(text);
			//if (data.readonly) this.setReadonly(item, true);
			//if (data.hidden == true) this.hide(item);
			//if (data.disabled == true) this.userDisable(item);
			
			if (pos) {
				if (!isNaN(data.inputLeft)) p.style.left = parseInt(data.inputLeft)+"px";
				if (!isNaN(data.inputTop)) p.style.top = parseInt(data.inputTop)+"px";
			}
			
			var u = "";
			
			if (dim) {
				if (!isNaN(data.inputWidth)) u += "width:"+parseInt(data.inputWidth)+"px;";
				if (!isNaN(data.inputHeight)) u += "height:"+parseInt(data.inputHeight)+"px;";
				
			}
			if (typeof(data.style) == "string") u += data.style;
			t.style.cssText = u;
		}
		
	},
	
	enable : function() {
		
	},
	
	disable : function () {}
	
};

(function(){
	for (var a in {doDestruct:1,doUnloadNestedLists:1,doAttachEvents:1,check:1,unCheck:1,isChecked:1,isEnabled:1,setText:1,getText:1,getValue:1,setReadonly:1,isReadonly:1})
		dhtmlXForm.prototype.items.radiolist[a] = dhtmlXForm.prototype.items.checkbox[a];
})();
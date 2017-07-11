dhtmlXToolbarObject.prototype._removeItem = function(itemId) {
	
	var t = this.getType(itemId);
	
	itemId = this.idPrefix+itemId;
	var p = this.objPull[itemId];
	
	
	if (t == "button") {
		
		p.obj._doOnMouseOver = null;
		p.obj._doOnMouseOut = null;
		p.obj._doOnMouseUp = null;
		p.obj._doOnMouseUpOnceAnywhere = null;
		
		p.obj.onclick = null;
		p.obj.onmouseover = null;
		p.obj.onmouseout = null;
		p.obj.onmouseup = null;
		p.obj.onmousedown = null;
		p.obj.onselectstart = null;
		
		p.obj.renderAs = null;
		p.obj.idd = null;
		p.obj.parentNode.removeChild(p.obj);
		p.obj = null;

		p.id = null;
		p.state = null;
		p.img = null;
		p.imgEn = null;
		p.imgDis = null;
		p.type = null;
		
		p.enableItem = null;
		p.disableItem = null;
		p.isEnabled = null;
		p.showItem = null;
		p.hideItem = null;
		p.isVisible = null;
		p.setItemText = null;
		p.getItemText = null;
		p.setItemImage = null;
		p.clearItemImage = null;
		p.setItemImageDis = null;
		p.clearItemImageDis = null;
		p.setItemToolTip = null;
		p.getItemToolTip = null;
		
	}
	
	if (t == "buttonTwoState") {
		
		p.obj._doOnMouseOver = null;
		p.obj._doOnMouseOut = null;
		
		p.obj.onmouseover = null;
		p.obj.onmouseout = null;
		p.obj.onmousedown = null;
		p.obj.onselectstart = null;
		
		p.obj.renderAs = null;
		p.obj.idd = null;
		p.obj.parentNode.removeChild(p.obj);
		p.obj = null;
		
		p.id = null;
		p.state = null;
		p.img = null;
		p.imgEn = null;
		p.imgDis = null;
		p.type = null;
		
		p.enableItem = null;
		p.disableItem = null;
		p.isEnabled = null;
		p.showItem = null;
		p.hideItem = null;
		p.isVisible = null;
		p.setItemText = null;
		p.getItemText = null;
		p.setItemImage = null;
		p.clearItemImage = null;
		p.setItemImageDis = null;
		p.clearItemImageDis = null;
		p.setItemToolTip = null;
		p.getItemToolTip = null;
		p.setItemState = null;
		p.getItemState = null;
		
	}
	
	if (t == "buttonSelect") {
		
		for (var a in p._listOptions) this.removeListOption(itemId, a);
		p._listOptions = null;
		
		if (p.polygon._ie6cover) {
			document.body.removeChild(p.polygon._ie6cover);
			p.polygon._ie6cover = null;
		}
		
		p.p_tbl.removeChild(p.p_tbody);
		p.polygon.removeChild(p.p_tbl);
		p.polygon.onselectstart = null;
		document.body.removeChild(p.polygon);
		
		p.p_tbody = null;
		p.p_tbl = null;
		p.polygon = null;
		
		p.obj.onclick = null;
		p.obj.onmouseover = null;
		p.obj.onmouseout = null;
		p.obj.onmouseup = null;
		p.obj.onmousedown = null;
		p.obj.onselectstart = null;
		p.obj.idd = null;
		p.obj.iddPrefix = null;
		p.obj.parentNode.removeChild(p.obj);
		p.obj = null;
		
		p.arw.onclick = null;
		p.arw.onmouseover = null;
		p.arw.onmouseout = null;
		p.arw.onmouseup = null;
		p.arw.onmousedown = null;
		p.arw.onselectstart = null;
		p.arw.parentNode.removeChild(p.arw);
		p.arw = null;
		
		p.renderSelect = null;
		p.state = null;
		p.type = null;
		p.id = null;
		p.img = null;
		p.imgEn = null;
		p.imgDis = null;
		p.openAll = null;
		
		p._isListButton = null;
		p._separatorButtonSelectObject = null;
		p._buttonButtonSelectObject = null;
		p.setWidth = null;
		p.enableItem = null;
		p.disableItem = null;
		p.isEnabled = null;
		p.showItem = null;
		p.hideItem = null;
		p.isVisible = null;
		p.setItemText = null;
		p.getItemText = null;
		p.setItemImage = null;
		p.clearItemImage = null;
		p.setItemImageDis = null;
		p.clearItemImageDis = null;
		p.setItemToolTip = null;
		p.getItemToolTip = null;
		p.addListOption = null;
		p.removeListOption = null;
		p.showListOption = null;
		p.hideListOption = null;
		p.isListOptionVisible = null;
		p.enableListOption = null;
		p.disableListOption = null;
		p.isListOptionEnabled = null;
		p.setListOptionPosition = null;
		p.getListOptionPosition = null;
		p.setListOptionImage = null;
		p.getListOptionImage = null;
		p.clearListOptionImage = null;
		p.setListOptionText = null;
		p.getListOptionText = null;
		p.setListOptionToolTip = null;
		p.getListOptionToolTip = null;
		p.forEachListOption = null;
		p.getAllListOptions = null;
		p.setListOptionSelected = null;
		p.getListOptionSelected = null;
		
	}
	
	if (t == "buttonInput" || t == "inputText") {
		
		p.obj.childNodes[0].onkeydown = null;
		p.obj.removeChild(p.obj.childNodes[0]);
		
		p.obj.w = null;
		p.obj.idd = null;
		p.obj.parentNode.removeChild(p.obj);
		p.obj = null;
		
		p.id = null;
		p.type = null;
		
		p.enableItem = null;
		p.disableItem = null;
		p.isEnabled = null;
		p.showItem = null;
		p.hideItem = null;
		p.isVisible = null;
		p.setItemToolTip = null;
		p.getItemToolTip = null;
		p.setWidth = null;
		p.getWidth = null;
		p.setValue = null;
		p.getValue = null;
		p.setItemText = null;
		p.getItemText = null;
		
	}
	
	if (t == "slider") {
		
		if (this._isIPad) {
			document.removeEventListener("touchmove", pen._doOnMouseMoveStart, false);
			document.removeEventListener("touchend", pen._doOnMouseMoveEnd, false);
		} else {
			if (_isIE) {
				document.body.detachEvent("onmousemove", p.pen._doOnMouseMoveStart);
				document.body.detachEvent("onmouseup", p.pen._doOnMouseMoveEnd);
			} else {
				window.removeEventListener("mousemove", p.pen._doOnMouseMoveStart, false);
				window.removeEventListener("mouseup", p.pen._doOnMouseMoveEnd, false);
			}
		}
		
		p.pen.allowMove = null;
		p.pen.initXY = null;
		p.pen.maxX = null;
		p.pen.minX = null;
		p.pen.nowX = null;
		p.pen.newNowX = null;
		p.pen.valueMax = null;
		p.pen.valueMin = null;
		p.pen.valueNow = null;
		
		p.pen._definePos = null;
		p.pen._detectLimits = null;
		p.pen._doOnMouseMoveStart = null;
		p.pen._doOnMouseMoveEnd = null;
		p.pen.onmousedown = null;
		
		p.obj.removeChild(p.pen);
		p.pen = null;
		
		p.label.tip = null;
		document.body.removeChild(p.label);
		p.label = null;
		
		p.obj.onselectstart = null;
		p.obj.idd = null;
		while (p.obj.childNodes.length > 0) p.obj.removeChild(p.obj.childNodes[0]);
		p.obj.parentNode.removeChild(p.obj);
		p.obj = null;
		
		p.id = null;
		p.type = null;
		p.state = null;
		
		p.enableItem = null;
		p.disableItem = null;
		p.isEnabled = null;
		p.setItemToolTipTemplate = null;
		p.getItemToolTipTemplate = null;
		p.setMaxValue = null;
		p.setMinValue = null;
		p.getMaxValue = null;
		p.getMinValue = null;
		p.setValue = null;
		p.getValue = null;
		p.showItem = null;
		p.hideItem = null;
		p.isVisible = null;
		
	}
	
	if (t == "separator") {		
		p.obj.onselectstart = null;
		p.obj.idd = null;
		p.obj.parentNode.removeChild(p.obj);
		p.obj = null;
		
		p.id = null;
		p.type = null;
		
		p.showItem = null;
		p.hideItem = null;
		p.isVisible = null;
		
	}
	
	if (t == "text" || t == "label" || t == "divElement" || t == "navigation") {
		p.obj.onselectstart = null;
		p.obj.idd = null;
		p.obj.parentNode.removeChild(p.obj);
		p.obj = null;
		
		p.id = null;
		p.type = null;
		
		p.showItem = null;
		p.hideItem = null;
		p.isVisible = null;
		p.setWidth = null;
		p.setItemText = null;
		p.getItemText = null;
		
	}
	
	t = null;
	p = null;
	this.objPull[this.idPrefix+itemId] = null;
	delete this.objPull[this.idPrefix+itemId];
	
	
};
dhtmlXToolbarObject.prototype._addItem = function(itemData, pos, align) {
	this._addItemToStorage(itemData, pos);
	if (this.skin == "dhx_terrace") this._improveTerraceSkin();
	var item = this.objPull[this.idPrefix+itemData.id];
	if (item && item.obj && align == "right") {
		item.obj.style.styleFloat = "right";
	    item.obj.style.float = "right";
	    item.obj.style.cssFloat = "right";
	}
}
/**
*   @desc: adds a button to webbar
*   @param: id - id of a button
*   @param: pos - position of a button
*   @param: text - text for a button (null for no text)
*   @param: imgEnabled - image for enabled state (null for no image)
*   @param: imgDisabled - image for desabled state (null for no image)
*   @type: public
*/
dhtmlXToolbarObject.prototype.addButton = function(id, pos, text, imgEnabled, imgDisabled, align) {
	this._addItem({id:id, type:"button", text:text, img:imgEnabled, imgdis:imgDisabled}, pos, align);
}
/**
*   @desc: adds a text item to webbar
*   @param: id - id of a text item
*   @param: pos - position of a text item
*   @param: text - text for a text item
*   @type: public
*/
dhtmlXToolbarObject.prototype.addText = function(id, pos, text, align) {
	this._addItem({id:id,type:"text",text:text}, pos, align);
}
//#tool_list:06062008{
/**
*   @desc: adds a select button to webbar
*   @param: id - id of a select button
*   @param: pos - position of a select button
*   @param: text - text for a select button (null for no text)
*   @param: opts - listed options for a select button
*   @param: imgEnabled - image for enabled state (null for no image)
*   @param: imgDisabled - image for desabled state (null for no image)
*   @param: renderSelect - set to false to prevent list options selection by click
*   @param: openAll - open options list when click main button (not only arrow)
*   @param: maxOpen - specify count of visible items (for long lists)
*   @type: public
*/
dhtmlXToolbarObject.prototype.addButtonSelect = function(id, pos, text, opts, imgEnabled, imgDisabled, renderSelect, openAll, maxOpen, align) { 
	var items = [];
	for (var q=0; q<opts.length; q++) {
		var u = {};
		if (opts[q].id && opts[q].type) {
			u.id = opts[q].id;
			u.type = (opts[q].type=="obj"?"button":"separator");
			u.text = opts[q].text;
			u.img = opts[q].img;
		} else {
			u.id = opts[q][0];
			u.type = (opts[q][1]=="obj"?"button":"separator");
			u.text = (opts[q][2]||null);
			u.img = (opts[q][3]||null);
		}
		items[items.length] = u;
	}
	this._addItem({id:id, type:"buttonSelect", text:text, img:imgEnabled, imgdis:imgDisabled, renderSelect:renderSelect, openAll:openAll, items:items, maxOpen:maxOpen}, pos, align);
}
//#}
//#tool_2state:06062008{
/**
*   @desc: adds a two-state button to webbar
*   @param: id - id of a two-state button
*   @param: pos - position of a two-state button
*   @param: text - text for a two-state button (null for no text)
*   @param: imgEnabled - image for enabled state (null for no image)
*   @param: imgDisabled - image for desabled state (null for no image)
*   @type: public
*/
dhtmlXToolbarObject.prototype.addButtonTwoState = function(id, pos, text, imgEnabled, imgDisabled, align) {
	this._addItem({id:id, type:"buttonTwoState", img:imgEnabled, imgdis:imgDisabled, text:text}, pos, align);
}
//#}
/**
*   @desc: adds a separator to webbar
*   @param: id - id of a separator
*   @param: pos - position of a separator
*   @type: public
*/
dhtmlXToolbarObject.prototype.addSeparator = function(id, pos, align) {
	this._addItem({id:id,type:"separator"}, pos, align);
}
//#tool_slider:06062008{
/**
*   @desc: adds a slider to webbar
*   @param: id - id of a slider
*   @param: pos - position of a slider
*   @param: len - length (width) of a slider (px)
*   @param: valueMin - minimal available value of a slider
*   @param: valueMax - maximal available value of a slider
*   @param: valueNow - initial current value of a slider
*   @param: textMin - label for minimal value side (on the left side)
*   @param: textMax - label for maximal value side (on the right side)
*   @param: tip - tooltip template (%v will replaced with current value)
*   @type: public
*/
dhtmlXToolbarObject.prototype.addSlider = function(id, pos, len, valueMin, valueMax, valueNow, textMin, textMax, tip, align) {
	this._addItem({id:id, type:"slider", length:len, valueMin:valueMin, valueMax:valueMax, valueNow:valueNow, textMin:textMin, textMax:textMax, toolTip:tip}, pos, align);
}
//#}
/**
*   @desc: adds an input item to webbar
*   @param: id - id of an input item
*   @param: pos - position of an input item
*   @param: value - value (text) in an input item by the default
*   @param: width - width of an input item (px)
*   @type: public
*/
dhtmlXToolbarObject.prototype.addInput = function(id, pos, value, width, align) {
	this._addItem({id:id,type:"buttonInput",value:value,width:width}, pos, align);
	// if (this.skin == "dhx_terrace") this._improveTerraceSkin(id);
}
/**
*   @desc: adds a label item to webbar
*   @param: id - id of a text item
*   @param: pos - position of a text item
*   @param: text - text for a text item
*   @type: public
*/
dhtmlXToolbarObject.prototype.addLabel = function(id, pos, text, align) {
	this._addItem({id:id,type:"label",text:text}, pos, align);
};
/**
*   @desc: adds an input[type=text] item to webbar
*   @param: id - id of an input item
*   @param: pos - position of an input item
*   @param: value - value (text) in an input item by the default
*   @param: width - width of an input item (px)
*   @type: public
*/
dhtmlXToolbarObject.prototype.addInputText = function(id, pos, value, width, align) {
	this._addItem({id:id,type:"inputText",value:value,width:width}, pos, align);
	// if (this.skin == "dhx_terrace") this._improveTerraceSkin(id);
}
/**
*   @desc: adds an input type=checkbox item to webbar
*   @param: id - id of an input item
*   @param: pos - position of an input item
*   @param: value - value (text) in an input item by the default
*   @param: width - width of an input item (px)
*   @param: checked - true/false
*   @param: label - label for input item
*   @type: public
*/
dhtmlXToolbarObject.prototype.addInputCheck = function(id, pos, value, width, checked, label, align) {
	this._addItem({id:id,type:"inputCheck",value:value,width:width,checked:checked,label:label}, pos, align);
	// if (this.skin == "dhx_terrace") this._improveTerraceSkin(id);
};
/**
*   @desc: adds an select item to webbar
*   @param: id - id of an input item
*   @param: pos - position of an input item
*   @param: options - Array[{value:'',text:'',selected:true/false}] in an select option items
*   @param: width - width of an input item (px)
*   @param: label - label for input item
*   @type: public
*/
dhtmlXToolbarObject.prototype.addInputSelect = function(id, pos, options, width, label, align) {
	this._addItem({id:id,type:"inputSelect",label:label,options:options,width:width}, pos, align);
	// if (this.skin == "dhx_terrace") this._improveTerraceSkin(id);
};
/**
*   @desc: adds an DIV element to webbar
*   @param: id - id of DIV
*   @param: pos - position of an input item
*   @type: public
*/
dhtmlXToolbarObject.prototype.addDiv = function(id, pos, align) {
	this._addItem({id:id,type:"divElement"}, pos, align);
	// if (this.skin == "dhx_terrace") this._improveTerraceSkin(id);
};
/**
*   @desc: adds a Navigation element to webbar
*   @param: id - id of DIV
*   @param: pos - position of an input item
*   @type: public
*/
dhtmlXToolbarObject.prototype.addNavigation = function(id, pos, label, align) {
	this._addItem({id:id, type:"navigation", label: label, pos: pos}, pos, align);
	// if (this.skin == "dhx_terrace") this._improveTerraceSkin(id);
};
/*****************************************************************************************************************************************************************
object: label
*****************************************************************************************************************************************************************/
dhtmlXToolbarObject.prototype._labelObject = function(that, id, data) {
	this.id = that.idPrefix+id;
	this.obj = document.createElement("DIV");
	this.obj.className = "dhx_toolbar_label";
	this.obj.style.display = (data.hidden!=null?"none":"");
	this.obj.idd = String(id);
	this.obj.title = (data.title||"");
	this.obj.onselectstart = function(e) { e = e||event; e.returnValue = false; };
	if (that._isIPad) {
		this.obj.ontouchstart = function(e){
			e = e||event;
			e.returnValue = false;
			e.cancelBubble = true;
			return false;
		};
	};
	//
	this.obj.innerHTML = (data.text||"");
	//
	that.base.appendChild(this.obj);
	//
	this.showItem = function() {
		this.obj.style.display = "";
	};
	this.hideItem = function() {
		this.obj.style.display = "none";
	};
	this.isVisible = function() {
		return (this.obj.style.display == "");
	};
	this.setItemText = function(text) {
		this.obj.innerHTML = text;
	};
	this.getItemText = function() {
		return this.obj.innerHTML;
	};
	this.setWidth = function(width) {
		this.obj.style.width = width+"px";
	};
	this.setItemToolTip = function(t) {
		this.obj.title = t;
	};
	this.getItemToolTip = function() {
		return this.obj.title;
	};
	//
	return this;
};
/*****************************************************************************************************************************************************************
object: inputText
***************************************************************************************************************************************************************** */
dhtmlXToolbarObject.prototype._inputTextObject = function(that, id, data) {
	//
	this.id = that.idPrefix+id;
	this.obj = document.createElement("DIV");
	this.obj.className = "dhx_toolbar_btn def";
	this.obj.style.display = (data.hidden!=null?"none":"");
	this.obj.idd = String(id);
	this.obj.w = (data.width!=null?data.width:100);
	this.obj.title = (data.title!=null?data.title:"");
	//
	var inputId = id + "_" + new Date().getTime();
	this.obj.innerHTML = "<input id='" + inputId + "' class='inp' type='text' style='-moz-user-select:text;width:"+this.obj.w+"px;'"+(data.value!=null?" value='"+data.value+"'":"")+">";
	
	var th = that;
	var self = this;
	this.obj.childNodes[0].onkeydown = function(e) {
		e = e||event;
		if (e.keyCode == 13) { th.callEvent("onEnter", [self.obj.idd, this.value]); }
	};
	this.obj.childNodes[0].onfocus = function(e) {
		if(th && th.callEvent) th.callEvent("onFocus", [self.obj.idd, this.value]);
	};
	this.obj.childNodes[0].onblur = function(e) {
		if(th && th.callEvent) th.callEvent("onBlur", [self.obj.idd, this.value]);
	};
	// add
	that.base.appendChild(this.obj);
	//
	this.enableItem = function() {
		this.obj.childNodes[0].disabled = false;
	};
	this.disableItem = function() {
		this.obj.childNodes[0].disabled = true;
	};
	this.isEnabled = function() {
		return (!this.obj.childNodes[0].disabled);
	};
	this.showItem = function() {
		this.obj.style.display = "";
	};
	this.hideItem = function() {
		this.obj.style.display = "none";
	};
	this.isVisible = function() {
		return (this.obj.style.display != "none");
	};
	this.setValue = function(value) {
		this.obj.childNodes[0].value = value;
	};
	this.getValue = function() {
		return this.obj.childNodes[0].value;
	};
	this.setWidth = function(width) {
		this.obj.w = width;
		this.obj.childNodes[0].style.width = this.obj.w+"px";
	};
	this.getWidth = function() {
		return this.obj.w;
	};
	this.setItemToolTip = function(tip) {
		this.obj.title = tip;
	};
	this.getItemToolTip = function() {
		return this.obj.title;
	};
	this.getInput = function() {
		return this.obj.firstChild;
	};
	//
	return this;
}
//#}
/*****************************************************************************************************************************************************************
	object: inputCheck
***************************************************************************************************************************************************************** */
dhtmlXToolbarObject.prototype._inputCheckObject = function(that, id, data) {
	//
	this.id = that.idPrefix+id;
	this.obj = document.createElement("DIV");
	this.obj.className = "dhx_toolbar_btn def";
	this.obj.style.display = (data.hidden!=null?"none":"");
	this.obj.idd = String(id);
	this.obj.w = (data.width!=null?data.width:100);
	this.obj.title = (data.title!=null?data.title:"");
	//
	var label = null;
	if (data.label) {
		label = "<div style='font-family: Tahoma; font-size: 11px;'>" + data.label + "</div>";
	}
	this.obj.innerHTML = (label == null ? "" : label) + "<input id='" + id 
		+ "' class='inp' type='checkbox' style='-moz-user-select:text;margin-top:4px;width:"+this.obj.w+"px;'"
		+ (data.value!=null?" value='"+data.value+"'":"") 
		+ (data.checked ? " checked " : "") + ">";
	
	var th = that;
	var self = this;
	this.getInput = function() {
		var i = 0, obj = this.obj.childNodes[0];
		if (obj.tagName == "DIV") {
			obj = this.obj.childNodes[1];
		}
		return obj;
		//return getInputObj();//this.obj.firstChild;
	};
	this.getInput().onclick = function(e) {
		if (self.isEnabled() != true) return;
		e = e||event;
		// event
		var id = self.id.replace(that.idPrefix,"");
		if (self.extAction) try {window[self.extAction](id);} catch(e){};
		if(that&&that.callEvent) that.callEvent("onClick", [id]);
	};
	// add
	that.base.appendChild(this.obj);
	//
	this.enableItem = function() {
		this.getInput().disabled = false;
	};
	this.disableItem = function() {
		this.getInput().disabled = true;
	};
	this.isEnabled = function() {
		return (!this.getInput().disabled);
	};
	this.isChecked = function() {
		return (this.getInput().checked);
	};
	this.showItem = function() {
		this.obj.style.display = "";
	};
	this.hideItem = function() {
		this.obj.style.display = "none";
	};
	this.isVisible = function() {
		return (this.obj.style.display != "none");
	};
	// if value equal checkbox's value, then checked; else unchecked.
	this.setValue = function(value) {
		if (value == this.getInput().value) {
			this.getInput().checked = true;
		} else {
			this.getInput().checked = false;
		}
		//this.getInput().value = value;
	};
	// if checked, then return checkbox's value; else return null.
	this.getValue = function() {
		if (this.isChecked()) {
			return this.getInput().value;
		}
		return null;
	};
	this.setWidth = function(width) {
		this.obj.w = width;
		this.getInput().style.width = this.obj.w+"px";
	};
	this.getWidth = function() {
		return this.obj.w;
	};
	this.setItemToolTip = function(tip) {
		this.obj.title = tip;
	};
	this.getItemToolTip = function() {
		return this.obj.title;
	};
	//
	return this;
};
/*****************************************************************************************************************************************************************
	object: inputSelect
***************************************************************************************************************************************************************** */
dhtmlXToolbarObject.prototype._inputSelectObject = function(that, id, data) {
	//
	this.id = that.idPrefix+id;
	this.obj = document.createElement("DIV");
	this.obj.className = "dhx_toolbar_btn def";
	this.obj.style.display = (data.hidden!=null?"none":"");
	this.obj.idd = String(id);
	this.obj.w = (data.width!=null?data.width:100);
	this.obj.title = (data.title!=null?data.title:"");
	
	//
	var label = null;
	if (data.label) {
		label = "<div style='font-family: Tahoma; font-size: 11px;margin-right:1px;'>" + data.label + "</div>";
	}
	var str = (label == null ? "" : label) + "<select class='inp' id='" + id + "' style='-moz-user-select:text;height:18px;width:"+this.obj.w+"px;'>";
	if (data.options && data.options.length > 0) {
		for (var i = 0; i < data.options.length; i++) {
			str += "<option value='" + data.options[i].value + "'" + (data.options[i].selected ? "selected" : "") + ">" + data.options[i].text + "</option>";
		}
	}
	str += "</select>";
	this.obj.innerHTML = str;
	
	var th = that;
	var self = this;
	this.getInput = function() {
		var i = 0, obj = this.obj.childNodes[0];
		if (obj.tagName == "DIV") {
			obj = this.obj.childNodes[1];
		}
		return obj;
		//return getInputObj();//this.obj.firstChild;
	};
	this.getInput().onchange = function(e) {
		if (self.isEnabled() != true) {
			return;
		}
		e = e||event;
		var id = self.id.replace(that.idPrefix,"");
		if (self.extAction) try {window[self.extAction](id);} catch(e){};
		if(that&&that.callEvent) that.callEvent("onChange", [id]);
	};
	// add
	that.base.appendChild(this.obj);
	//
	this.enableItem = function() {
		this.getInput().disabled = false;
	};
	this.disableItem = function() {
		this.getInput().disabled = true;
	};
	this.isEnabled = function() {
		return (!this.getInput().disabled);
	};
	this.showItem = function() {
		this.obj.style.display = "";
	};
	this.hideItem = function() {
		this.obj.style.display = "none";
	};
	this.isVisible = function() {
		return (this.obj.style.display != "none");
	};
	this.setValue = function(value) {
		var opts = this.getInput().options;
		for (var i = 0; i < opts.length; i++) {
			var opt = opts[i];
			if (value == opt.value) {
				opt.selected = true;
				break;
			}
		}
		//this.getInput().value = value;
	};
	this.getValue = function() {
		return this.getInput().value;
	};
	this.setWidth = function(width) {
		this.obj.w = width;
		this.getInput().style.width = this.obj.w+"px";
	};
	this.getWidth = function() {
		return this.obj.w;
	};
	this.setItemToolTip = function(tip) {
		this.obj.title = tip;
	};
	this.getItemToolTip = function() {
		return this.obj.title;
	};
	//
	return this;
};
/*****************************************************************************************************************************************************************
object: divElement
***************************************************************************************************************************************************************** */
dhtmlXToolbarObject.prototype._divElementObject = function(that, id, data) {
	//
	this.id = that.idPrefix+id;
	this.obj = document.createElement("DIV");
	this.obj.className = "dhx_toolbar_btn def";
	this.obj.style.display = (data.hidden!=null?"none":"");
	this.obj.idd = String(id);
	this.obj.id  = String(id);
	this.obj.w = (data.width!=null?data.width:100);
	this.obj.title = (data.title!=null?data.title:"");
	
	this.obj.setAttribute("style","margin-top:2px;");
	
	//this.obj.innerHTML = str;
	
	var th = that;
	var self = this;
	this.getInput = function() {
		return this.obj;
		//return getInputObj();//this.obj.firstChild;
	};
	// add
	that.base.appendChild(this.obj);
	//
	this.enableItem = function() {
		this.getInput().disabled = false;
	};
	this.disableItem = function() {
		this.getInput().disabled = true;
	};
	this.isEnabled = function() {
		return (!this.getInput().disabled);
	};
	this.showItem = function() {
		this.obj.style.display = "";
	};
	this.hideItem = function() {
		this.obj.style.display = "none";
	};
	this.isVisible = function() {
		return (this.obj.style.display != "none");
	};
	this.setValue = function(value) {
		//this.getInput().value = value;
	};
	this.getValue = function() {
		return null;
	};
	this.setWidth = function(width) {
		this.obj.w = width;
		this.getInput().style.width = this.obj.w+"px";
	};
	this.getWidth = function() {
		return this.obj.w;
	};
	this.setItemToolTip = function(tip) {
		this.obj.title = tip;
	};
	this.getItemToolTip = function() {
		return this.obj.title;
	};
	//
	return this;
};
/*****************************************************************************************************************************************************************
object: Navigation
***************************************************************************************************************************************************************** */
dhtmlXToolbarObject.prototype._navigationObject = function(that, id, data) {
	//
	this.id = that.idPrefix+id;
	this.obj = document.createElement("DIV");
	this.obj.className = "dhx_toolbar_label";
	this.obj.style.display = (data.hidden!=null?"none":"");
	this.obj.idd = String(id);
	this.obj.w = (data.width!=null?data.width:100);
	this.obj.title = (data.title!=null?data.title:"");
	
	//
	var separator = null;
	if (data.pos == 0) {
		separator = "<strong>您当前位置：</strong>";
	} else {
		separator = "-->";
	}
	var str = separator + " <a position='" + data.pos + "' onclick='javascript:CFG_NVG_doClick(this);' href='javascript:void(0)'>" + data.label + "</a>";
	
	this.obj.innerHTML = str;
	
	var th = that;
	var self = this;
	this.getInput = function() {
		return obj;
	};
	this.getLink = function() {
		for (var i = 0; i < this.obj.childNodes.length; i++) {
			var obj = this.obj.childNodes[i];
			if (obj.tagName == "A") return obj;
		}
		return null;
	};
	// add
	that.base.appendChild(this.obj);
	//
	this.enableItem = function() {
		this.obj.disabled = false;
	};
	this.disableItem = function() {
		this.obj.disabled = true;
	};
	this.isEnabled = function() {
		return (!this.getInput().disabled);
	};
	this.showItem = function() {
		this.obj.style.display = "";
	};
	this.hideItem = function() {
		this.obj.style.display = "none";
	};
	this.isVisible = function() {
		return (this.obj.style.display != "none");
	};
	this.setValue = function(value) {
		//this.getInput().value = value;
	};
	this.getValue = function() {
		return this.obj.innerHTML;
	};
	this.setWidth = function(width) {
		this.obj.w = width;
	};
	this.getWidth = function() {
		return this.obj.w;
	};
	this.setItemToolTip = function(tip) {
		this.obj.title = tip;
	};
	this.getItemToolTip = function() {
		return this.obj.title;
	};
	this.setItemText = function(text) {
		var obj = this.getLink();
		if (obj) obj.innerHTML = text;
	};
	//
	return this;
};
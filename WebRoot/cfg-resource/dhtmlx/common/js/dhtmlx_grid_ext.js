dhtmlXGridObject.prototype.setPagingWTMode = function(navButtons,navLabel,pageSelect,perPageSelect,totalPages,totalRecords){
	this._WTDef=[navButtons,navLabel,pageSelect,perPageSelect,totalPages,totalRecords];
}

dhtmlXGridObject.prototype._pgn_createToolBar = function() {
	this.aToolBar = new dhtmlXToolbarObject(this._pgn_parentObj,(this._pgn_skin_tlb||"dhx_blue"));
	if (!this._WTDef) this.setPagingWTMode(true,true,true,true,true,true);
	var self  = this;
	var ptbar = this.aToolBar;
	ptbar.clickEventId = ptbar.attachEvent("onClick",function(val){ 
		val=val.split("_");
		switch (val[0]){
			case "leftabs":
				toolbarMove(1, self.pageSize);
				break;
			case "left":
				toolbarMove(self.currentPage-1, self.pageSize);
				break;
			case "rightabs":
				if (self.totalPages) {
					toolbarMove(self.totalPages, self.pageSize);
				} else {
					toolbarMove(99999, self.pageSize);
				}
				break;
			case "right":
				toolbarMove(self.currentPage+1, self.pageSize);
				break;
			case "perpagenum":
				if (val[1]===this.undefined) return;
			    self.rowsBufferOutSize = parseInt(val[1]);				
				//addCookie("pagesize",val[1],365*24);
			    self.pageSize = val[1];
				toolbarMove(1, self.pageSize);
				ptbar.setItemText("perpagenum","<div style='width:100%; text-align:right'>"+val[1]+" "+self.i18n.paging.perpage+"</div>");
				break;
			case "pages":
				if (val[1]===this.undefined) return;
				toolbarMove(val[1], self.pageSize);
				ptbar.setItemText("pages","<div style='width:100%; text-align:right'>"+self.i18n.paging.page+val[1]+"</div>");
				break;
			}
	});
	ptbar.enterEventId = ptbar.attachEvent("onEnter", function(id, value){
		if (id == "pages") {
			toolbarMove(value, self.pageSize);
		}
	});
	function toolbarMove(pageNumber, pageSize) {
		if (!self.totalPages) {
			self.totalPages = Math.ceil(self.rowsBuffer.length/self.pageSize);
		}
		if (pageNumber > self.totalPages) {
			pageNumber = self.totalPages;
		} else if (pageNumber < 1) {
			pageNumber = 1;
		}
		var pagebarEvent = self.getPagebarEvent();
		if (pagebarEvent && (typeof pagebarEvent == "function")) {
			pagebarEvent(pageNumber, pageSize);
		} else {
			search(self, self.gridFormat, self.url, pageNumber, pageSize);
		}
		self.changePage(pageNumber);
	};
	//add buttons
	if (this._WTDef[0]){
		ptbar.addButton("leftabs", NaN,  "", this.imgURL+'ar_left_abs.gif', this.imgURL+'ar_left_abs_dis.gif');
		ptbar.setWidth("leftabs","20");
		ptbar.addButton("left", NaN,  "", this.imgURL+'ar_left.gif', this.imgURL+'ar_left_dis.gif');
		ptbar.setWidth("left","20");
	}
	if (this._WTDef[1]){
		ptbar.addText("results",NaN,this.i18n.paging.results);
		ptbar.setWidth("results","150");
		ptbar.disableItem("results");
	}
	if (this._WTDef[0]){
		ptbar.addButton("right", NaN,  "", this.imgURL+'ar_right.gif', this.imgURL+'ar_right_dis.gif');
		ptbar.setWidth("right","20");
		ptbar.addButton("rightabs", NaN,  "", this.imgURL+'ar_right_abs.gif', this.imgURL+'ar_right_abs_dis.gif');
		ptbar.setWidth("rightabs","20");
	}
	if (this._WTDef[2]){
//		ptbar.addButtonSelect("pages", NaN, "select page", [], null, null, true, true);
//		ptbar.setWidth("pages","75");
		ptbar.addInputText("pages", NaN, "1", "30");
		ptbar.getInput("pages").onkeydown = function(e) {
			e = e||event;
			if ((e.keyCode>=48 && e.keyCode<=57) || e.keyCode==8 || e.keyCode==13 || e.keyCode==46 || e.keyCode==37 || e.keyCode==39 || (e.keyCode>=96 && e.keyCode<=105)) {
				if (e.keyCode == 13) {
					var pageNumber = parseInt(this.value, 10);
					if (pageNumber > self.totalPages) {
						pageNumber = self.totalPages;
					} else if (pageNumber < 1) {
						pageNumber = 1;
					}
					this.value = pageNumber;
					ptbar.callEvent("onEnter", ["pages", pageNumber]); 
				}
			} else {
				return false;
			}
		};
	}
	var arr;
	if (arr = this._WTDef[3]){
		ptbar.addButtonSelect("perpagenum", NaN, "select size", [], null, null, true, true);
		if(typeof arr != "object")  arr = [10,15,20,25,30];
		for (var k=0; k<arr.length; k++)
			ptbar.addListOption('perpagenum', 'perpagenum_'+arr[k], NaN, "button", arr[k]+" "+this.i18n.paging.perpage);
		ptbar.setWidth("perpagenum","135");
	}
	if (this._WTDef[4]){
		ptbar.addText("totalPages",NaN,"");
		ptbar.setWidth("totalPages","100");
		ptbar.disableItem("totalPages");
	}
	if (this._WTDef[5]){
		ptbar.addText("totalRecords",NaN,"");
		ptbar.setWidth("totalRecords","100");
		ptbar.disableItem("totalRecords");
	}
	//var td = document.createElement("TD"); td.width = "5"; this.aToolBar.tr.appendChild(td);
	//var td = document.createElement("TD"); td.width = "100%"; this.aToolBar.tr.appendChild(td);
		
	return ptbar;
};

dhtmlXGridObject.prototype.i18n.paging={results:"Results",
		 records:"当前页记录： ",
		 to:" 至  ",
		 page:"页 ",
		 perpage:"条记录/页",
		 first:"首页",
		 previous:"上一页",
		 found:"共",
		 next:"下一页",
		 last:"尾页",
		 of:" / ",
		 notfound:"没有数据",
		 record:"条记录"
		};

dhtmlXGridObject.prototype.parse = function(data, call, type){
	if (arguments.length == 2 && typeof call != "function"){
		type=call;
		call=null;
	}
	type=type||"xml";
	this._data_type=type;
	if (data.totalPages) {
		this.totalPages = data.totalPages;
	}
	if (data.pageSize) {
		this.pageSize = data.pageSize;
	}
	data=this["_process_"+type](data);
	if (!this._contextCallTimer)
	this.callEvent("onXLE", [this,0,0,data]);
	if (call)
		call();
};
dhtmlXGridObject.prototype._drawTooltip=function(){};
/**
*	@desc: web toolbar skin for paging
*/
dhtmlXGridObject.prototype._pgn_toolbar = function(page, start, end){
	if (!this.aToolBar) this.aToolBar=this._pgn_createToolBar();
		var totalPages=Math.ceil(this.rowsBuffer.length/this.rowsBufferOutSize);
		
	
		if (this._WTDef[0]){
			this.aToolBar.enableItem("right");
			this.aToolBar.enableItem("rightabs");
			this.aToolBar.enableItem("left");
			this.aToolBar.enableItem("leftabs");
			if(this.currentPage>=totalPages){
				this.aToolBar.disableItem("right");
				this.aToolBar.disableItem("rightabs");
			}
			if(this.currentPage==1){
				this.aToolBar.disableItem("left");
				this.aToolBar.disableItem("leftabs");
			}
		}
		if (this._WTDef[2]){
//			var that=this;
//			this.aToolBar.forEachListOption("pages", function(id){
//			    that.aToolBar.removeListOption("pages",id);
//			});
//			for(var i=0;i<totalPages;i++){
//				this.aToolBar.addListOption('pages', 'pages_'+(i+1), NaN, "button", this.i18n.paging.page+(i+1));
//			}
//			this.aToolBar.setItemText("pages","<div style='width:100%; text-align:right'>"+this.i18n.paging.page+page+"</div>");
			//document.getElementById("tbar_pages").value = page;
			this.aToolBar.setValue("pages", page);
		}
//		pButton.setSelected(page.toString())
	
	
	if (this._WTDef[1]){
		if (!this.getRowsNum())
			this.aToolBar.setItemText('results',this.i18n.paging.notfound);
		else
			this.aToolBar.setItemText('results',"<div style='width:100%; text-align:center'>"+this.i18n.paging.records+(start+1)+this.i18n.paging.to+end+"</div>");
	}
    if (this._WTDef[3])
    	this.aToolBar.setItemText("perpagenum","<div style='width:100%; text-align:right'>"+this.rowsBufferOutSize.toString()+" "+this.i18n.paging.perpage+"</div>");
    if (this._WTDef[4]){
		if (!this.getRowsNum())
			this.aToolBar.setItemText('totalPages',"<div style='width:100%; text-align:center'>"+this.i18n.paging.found+" 0 "+this.i18n.paging.page+"</div>");
		else
			this.aToolBar.setItemText('totalPages',"<div style='width:100%; text-align:center'>"+this.i18n.paging.found+" "+totalPages+" "+this.i18n.paging.page+"</div>");
	}
    if (this._WTDef[5]){
		if (!this.getRowsNum())
			this.aToolBar.setItemText('totalRecords',"<div style='width:100%; text-align:center'>"+this.i18n.paging.found+" 0 "+this.i18n.paging.record+"</div>");
		else
			this.aToolBar.setItemText('totalRecords',"<div style='width:100%; text-align:center'>"+this.i18n.paging.found+" "+this.rowsBuffer.length+" "+this.i18n.paging.record+"</div>");
	}
    
	this.callEvent("onPaging",[]);		
};
dhtmlXGridObject.prototype._prepareRow = function(new_id) {
    if (!this._master_row)
        this._build_master_row();

    var r = this._master_row.cloneNode(true);

    for (var i = 0; i < r.childNodes.length; i++) {
        r.childNodes[i]._cellIndex = i;
        if (this._enbCid)
            r.childNodes[i].id = "c_" + new_id + "_" + i;
        if (this.dragAndDropOff)
            this.dragger.addDraggableItem(r.childNodes[i], this);
    }
    r.idd = new_id;
    r.grid = this;
	
    // 添加拖动图标开始
    if (this.dragAndDropOff) {
    	var _this = this;
	    dhtmlxEvent(r, "mouseover", function() {
	    	var obj = document.getElementById("drag_drop_tip");
	        if (obj == null) {
	            obj = document.createElement("div");
	            obj.setAttribute("id", "drag_drop_tip");
	            var dragDropImg = document.createElement("img");
	            dragDropImg.src = DHX_RES_PATH + "/common/images/drag_drop.gif";
	            obj.appendChild(dragDropImg);
	            document.body.appendChild(obj);
	        }
	        var tr = r;
	        var posX = tr.offsetLeft;
	        var posY = tr.offsetTop;
	        var trParent = tr;
	        while (trParent.tagName != "BODY") {
	            trParent = trParent.offsetParent;
	            posX += trParent.offsetLeft;
	            posY += trParent.offsetTop;
	            posY -= trParent.scrollTop;
	            if (trParent.className == 'dhtmlx_window_active') {
	            	if (!_isIE) {
			        	posY += 4;
		        	}
	            }
	        }
        	posX = posX + _this.objBox.clientWidth - 20;
        	posY += 2;
            obj.style.cssText = "width:16px;height:16px;z-index:1000;position:absolute;left: " + posX + "px;top:"
                    + posY + "px;";
	    });
	    dhtmlxEvent(r, "mouseout", function() {
	    	var obj = document.getElementById("drag_drop_tip");
        	if (obj != null) {
        		obj.style.cssText = "visibility: hidden;";
        	}
	    });
    }
    // 添加拖动图标结束
    
    return r;
};

dhtmlXGridObject.prototype.setPagebarEvent = function(pagebarEvent) {
	this.pagebarEvent = pagebarEvent;
};
dhtmlXGridObject.prototype.getPagebarEvent = function() {
	return this.pagebarEvent;
};
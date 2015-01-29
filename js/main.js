/**
 * @author yuki
 */

(function(smr, window,pedit, undefined) {

	window.onload = function() {


		var _Object = smr.Object;
		var Object = window.Object;
		var doc =window.document;
		var dom = smr.dom;
		var Element = dom.Element;
		var Textarea = dom.Textarea;
		var Form = dom.Form;
		var fom =new Form(doc.forms[0]);
		var yoko =new Element(fom.element.yoko);
		var divs =[];
		var getYoko =function(){return ~~yoko.value;};
		var parseInt = window.parseInt;
		var body = new Element(doc.body);
		body.on("dragstart",function(e){e.preventDefault();});
		var ajax = smr.ajax;
		var currentPage =1;
		var maxPage = 1;
		var pdfid=0;

		var stringCounter = function(s, reg){
			return s.split(reg).length-1;
		};

		var getRGBorRGBA = function(color){
			if(color.substring(0,4) === "rgba"){
				var iro = color.split(/[rgba()\s]/).join("");
				var alpha = ~~(255*iro.split(",")[3]);
				return iro.substring(0,iro.lastIndexOf(","))+","+alpha;
			}else{
				return color.split(/[rgb()\s]/).join("");
			}
		};

		var showMessage = function(e) {
			msdv.html=this._message;
			msdv.x=e.pageX+25;
			msdv.y=e.pageY;
			msdv.show();
		};

		var moveMessage =function(e){
			msdv.x=e.pageX+25;
			msdv.y=e.pageY;
		};

		var hideMessage = function() {
			msdv.hide();
		};

		var pdfdataObject ={};

		var createAjaxData = function(){
			PDFsubmit();
			pdfdataObject.pdfdata = getPdfData();
			pdfdataObject.tableWidth=getYoko();
		};

		var buttonStyle ;

		//送信ボタン
		fom.create("input").elementSetter({
			type:"submit",
			value:"表示",
			_message:"Ctrl + H"
		}).on("mouseover",showMessage).on("mouseout",hideMessage).styleSetter(
			//保存ボタン
			buttonStyle = pedit.Save(pdfdataObject,createAjaxData).elementSetter({
				_message:"Ctrl + S"
			}).appendTo(fom).on("mouseover",showMessage).on("mouseout",hideMessage).style
		).style.margin ="0 2px";
		buttonStyle.margin="0 2px";



		var addMoveText = function(e,text,page,x,y) {
			text=text||"内容を入力してください";
			page=page||currentPage;
			var visibility= page==currentPage ? "visible":"hidden";
			x=(x===undefined) ? 400:x;
			y=(y===undefined) ? 30:y;
			Textarea().autoResize().absolute(x,y).elementSetter({
				className:"drag page"+page,
				value:text,
				_message:"ドラッグで移動できます"
			}).styleSetter({
				padding:"50px 5px 5px 5px",
				cursor:"move",
				borderWidth :"0px",
				backgroundColor:"rgba(0,0,0,0.05)",
				visibility:visibility
			}).appendTo(secondBody)
			.on("mouseover",showMessage)
			.on("mouseout",hideMessage);
		};

		var addsMoveText = function(texts,page){
			page=page||currentPage;
			for(var i =0,len=texts.length;i<len;++i){
				text=texts[i];
				addMoveText(null,text.text,page,text.x,text.y);
			}
		};

		fom.create("button").styleSetter(buttonStyle).elementSetter({
			type:"button"
		}).on("click",addMoveText).html="テキスト追加";

				//divのinputリサイズイベント
		var autoresize = function() {
			var cn = this.children;
			var max = 0;
			var l =cn.length;
			for(var i = 0;i<l;){
				var c =cn[i++];
				c.style.height=0+"px";
				var h = c.scrollHeight;
				if(max<h){max=h;}
			}
			for(var i = 0;i<l;){cn[i++].style.height=(~~max-10)+"px";}
			buttonPositioning();
		};
		var divAllShow= function(d) {
			d.show();
		};

		var divAllHide= function(d) {
			d.hide();
		};
		//hide or show
		var pageRendering = function(method) {
			var pageElements = body.queryAll(".page"+currentPage,{ isWrap :true});
			if(!pageElements){return ;}
			for(var i = 0,l = pageElements.length;i<l;++i){
				pageElements[i][method]();
			}
		};
		var nextPage = function() {
			if(currentPage===1){
				DivsLoop(divAllHide);
				controller.hide();
			}
			pageRendering("hide");
			++currentPage;
			pageRendering("show");
			if(maxPage<currentPage){maxPage = currentPage;}
			pageSpan.html=currentPage+"ページ";
		};

		var prevPage = function() {
			if(currentPage===1){return ;}
			pageRendering("hide");
			if(--currentPage===1){DivsLoop(divAllShow);controller.show();}
			pageRendering("show");
			pageSpan.html=currentPage+"ページ";
		};


		var pageMoveDiv = fom.create("div");
		pageMoveDiv.style.margin="0 0 20px 0";
		pageMoveDiv.create("button").elementSetter({
			type:"button"
		}).styleSetter(buttonStyle).on("click",prevPage).html="前のページへ";
		var pageSpan = pageMoveDiv.create("span");
		pageSpan.html="1ページ";
		pageSpan.style.margin="0 10px";
		pageMoveDiv.create("button").elementSetter({
			type:"button"
		}).styleSetter(buttonStyle).on("click",nextPage).html="次のページへ";

		pageMoveDiv.create("span").html="画像アップロード→";

		pageMoveDiv.create("input").elementSetter({
			type:"file",
			name:"pdfimg"
		}).onchange=function(){
			var file = this.files[0];
			this.value=null;
			if(!file)return;
			var fd = new FormData();
			fd.append(this.name,file);
			fd.append("key",4);
			smr.ajax.load({
				dataType:"json",
				data:fd,
				type:"post",
				url:pedit.path+"/serv/jv34_k09.PdfController",
				success:function(data){
					if(data.status==="success"){
						appendImage([{x:20,y:300,src:data.url}]);
					}else{
						alert(data.text);
					}
				}
			});
		};

		//デフォルトの色の変更があった場合とyokoに変更があった場合trueにする
		var isEdited = false;
		//clone用に一個作っておく
		var div =new Element("div").styleSetter({marginLeft:"50px",
			userSelect : "none",
			mozUserSelect : "none",
			webkitUserSelect : "none",
			msUserSelect : "none"});
		var tx =new Textarea().elementSetter({
			rows:1,
			cols:60/getYoko(),
			name:"cell"
		}).styleSetter({
			backgroundColor:"rgba(255,255,255,1)",
			borderColor:"rgba(0,0,0,1)",
			borderWidth : "1px",
			padding : "5px",
		});

		//Divを挿入するときに行う。isEdited trueなら
		var rebuildDiv =function(){
			div.removeChildAll();
			for(var i =0, l=getYoko(); i<l; ++i){ tx.clone().appendTo(div);}
			isEdited = false;
		};
		//とりあえず一回実行
		rebuildDiv();




		var WIDTH = window.innerWidth;
		var HEIGHT = window.innerHeight;

		//divsのForEachみたいな奴
		var AllCellLoop = function(fn) {
			for(var i = 0,li=divs.length;i<li;++i){
				for(var j =0,cn=divs[i].children, lj = cn.length;j<lj;++j){
					fn(cn[j], i, j);
				}
			}
		};

		var fireonsubmit=doc.createEvent("Event");
		fireonsubmit.initEvent("submit",false,false);

		//プレビューっていう文字
		Element("div").styleSetter({
			width : "50%",
			position : "absolute",
			top : "0%",
			left : "50%",
			fontSize:"1.5em",
			textAlign : "center"
		}).styleSetter({color:"red"}).appendTo().html+="Preview";

		var fullPDF=function() {
			window.open("","FullPDF","").focus();
			fom.element.target = "FullPDF";
			fom.element.dispatchEvent(fireonsubmit);
			fom.element.submit();
			fom.element.target = "PDF";
		};
		//全画面表示
		Element("button").styleSetter(buttonStyle).styleSetter({
			position:"absolute",
			top : "5px",
			left : "55%",
			fontSize :"1.2em"
		}).elementSetter({
			innerHTML:"全画面表示",
			type:"button",
			_message:"Ctrl + G"
		}).on("click", fullPDF).appendTo().on("mouseover",showMessage).on("mouseout",hideMessage);

		//へっだ
		var hdr = new Element("div");
		hdr.fixed(0, -5, WIDTH/2, 45)
		.styleSetter({
			borderRadius:"5px",
			borderBottom : "2px solid rgba(0,0,0,0.2)"
		}).appendTo().backgroundColor="rgba(55,55,111,0.5)";


		//メッセージDIV
		var msdv = new Element("div");
		msdv.absolute(0, 0, 150).styleSetter({
			borderRadius : "5px",
			borderRight : "2px solid rgba(20,20,20,0.2)",
			borderBottom : "2px solid rgba(20,20,20,0.2)",
			backgroundColor : "rgba(255,255,255,0.8)"
		}).hide();


		var paintBD,paintBG,selectDiv;
		var selectStart = function(e) {
			selectDiv = paint.create("div").absolute(e.pageX,e.pageY,2,2)
			.styleSetter({
				border : "solid 1px "+(paintBD || paintBG)
			});
			selectDiv.plusHeight=true;
			selectDiv.plusWidth=true;
			paint//
			.on("mouseleave", selectEnd)//
			.on("mousemove",selectMove)//
			.on("mouseup",selectEnd)//
			.off("mousemove",moveMessage);
			msdv.hide();
		};

		var painting = function(tx) {
			if(selectDiv.isHitElement(tx)){
				var s = tx.style;
				s.borderColor = paintBD || s.borderColor;
				s.backgroundColor = paintBG || s.backgroundColor;
			}
		};

		//ドラッグしてマウスUP
		var selectEnd = function() {
			paint.off("mousemove",selectMove);
			if(currentPage===1)AllCellLoop(painting);
			paintBD = paintBG = null;
			if(selectDiv)selectDiv.remove();
			selectDiv=null;
			paint.off("mouseup", selectEnd).off("mouseleave", selectEnd).hide();
		};


		//範囲指定するやつ
		var selectMove = function(e) {
			var ex=e.pageX;
			var ey=e.pageY;
			var rw,rh;
			//疑似マイナスWidth,Height
			if(0<(rw=ex-selectDiv.x)&&selectDiv.plusWidth){selectDiv.width=rw;selectDiv.plusWidth=true;}
			else{
				var aw=selectDiv.AllWidth;
				if(aw){
					var _w=aw-ex;
					if(0<_w){
						selectDiv.x=ex;
						selectDiv.width=aw-ex;
					}else{
						selectDiv.plusWidth=true;
						selectDiv.width=-_w;
						selectDiv.x=aw;
						selectDiv.AllWidth=null;
					}
				}else{
					selectDiv.x=ex;
					selectDiv.width=-rw;
					selectDiv.plusWidth=false;
					selectDiv.AllWidth = ex-rw;
				}
			}
			if(0<(rh=ey-selectDiv.y)&&selectDiv.plusHeight){selectDiv.height=rh;selectDiv.plusHeight=true;}
			else{
				var ah=selectDiv.AllHeight;
				if(ah){
					var _h=ah-ey;
					if(0<_h){
						selectDiv.y=ey;
						selectDiv.height=ah-ey;
					}else{
						selectDiv.plusHeight=true;
						selectDiv.height=-_h;
						selectDiv.y=ah;
						selectDiv.AllHeight=null;
					}

				}else{
					selectDiv.y=ey;
					selectDiv.height=-rh;
					selectDiv.plusHeight=false;
					selectDiv.AllHeight=ey-rh;
				}
			}

		};
		var controller = new Element("div");
		controller.appendTo().absolute(1, 1, 1, 1);

		var secondBody = new Element("div");
		secondBody.absolute(1, 1, 1, 1).appendTo();

		//色選択したあと
		var paint = new Element("div");
		paint.absolute(0, 0, WIDTH/2).styleSetter({
			backgroundColor:"rgba(0,0,50,0.5)",
			cursor:"crosshair",
		}).elementSetter({
			_message:"ドラッグして範囲を選択してください。"
		}).on("mousedown", selectStart).appendTo().hide();


		var showPaint = function() {
			paint.show().on("mousemove", moveMessage).height=fom.element.clientHeight;
			setTimeout(function(){msdv.show();},4);
			msdv.html=paint.element._message;
			if(this.name === "BG"){
				paintBG = this.style.color;
			}else{
				paintBD = this.style.color;
			}
		};

		//imgList
		var imgListBody=Element("div")//.fixed(WIDTH/2, HEIGHT*0.05, WIDTH/2, HEIGHT*0.9)
		.styleSetter({
			position:"fixed",
			width:"50%",
			height:"90%",
			top:"5%",
			left:"50%",
			borderRadius:"5px",
			borderBottom : "2px solid rgba(0,0,0,0.2)",
			backgroundColor:"rgba(55,55,111,0.8)",
			overflow:"auto"
		}).appendTo().hide();

		var iframePDF=Element(doc.getElementById("PDFDIV"));
		var showImgList = function(){
			if(this._isHide){
						imgListBody.queryAll(".imgList",{method:"removeChild"});
						smr.ajax.load({
							type:"post",
							dataType:"json",
							data:{key:5},
							url:pedit.path+"/serv/jv34_k09.PdfController",
							success:function(data){
								if(data.status === "success"){
									var list =data.text;
									for(var src,i=0;src=list[i];++i){
										imgListBody.create("img").elementSetter({
											src:pedit.path+"/"+encodeURI(src),
											_message:"クリックで編集画面に追加",
											className:"imgList"
										}).on("mouseover",showMessage).on("mouseout",hideMessage)//
										.styleSetter({
											maxHeight:"100px",
											maxWidth:"100px"
										}).onclick=function(){
											appendImage([{x:20,y:300,src:this.src}]);
										};
									}
								}else{
									alert(data.text);
								}
							}
						});
						this.innerHTML="画像一覧を隠す"	;
					}else{
						this.innerHTML="画像一覧表示"	;
					}
					imgListBody.toggle();
					iframePDF.toggle();
					this._isHide=!this._isHide;
		};


		msdv.appendTo( );

		(function() {
			var cs=["255,0,0","0,255,0","0,0,255",
			        "255,255,0","0,255,255","255,0,255",
			        "0,0,0","255,255,255","180,180,180","122,122,122",
			        "64,64,64","32,32,32","122,0,0","0,122,0",
			        "0,0,122","122,122,0","122,0,122",
			        "0,122,122","255,122,122","122,255,122","122,122,255",
			        "255,255,122","255,122,255","122,255,255"];
			for(var i = 0,l=cs.length;i<l;++i){
				var iro = "rgb("+cs[i]+")";
				hdr.create("span").styleSetter({
					color:iro,
					position:"absolute",
					top : "8px",
					left : ((i+1)*17)+"px",
					cursor:"pointer"
				}).elementSetter({_message:"セルの背景色を変更",name : "BG"})
				.on("mouseover",showMessage).on("mouseout",hideMessage).on("click",showPaint).html="■";

				hdr.create("span").styleSetter({
					color:iro,
					position:"absolute",
					top : "23px",
					left : ((i+1)*17)+"px",
					cursor:"pointer"
				}).elementSetter({_message:"セルの枠の色を変更",name :"BD"})
				.on("mouseover",showMessage).on("mouseout",hideMessage).on("click",showPaint).html="□";
			}

			hdr.create("a").elementSetter({
				href:pedit.path+"/pdf_editor/mypage.jsp"
			}).styleSetter({
				color:"white",
				backgroundColor:"red",
				position:"absolute",
				top:"15px",
				padding:"2px",
				textDecoration:"none",
				left:(++i+1)*17+"px"
			}).html="マイページへ";

			hdr.create("button").styleSetter(buttonStyle).styleSetter({
				position:"absolute",
				top:"15px",
				right:"10px"
			}).elementSetter({
				type:"button",
				_isHide:true,
				_message:"今までにアップロードした画像の一覧を表示、非表示。"
			}).on("mouseover",showMessage).on("mouseout",hideMessage)//
			.on("click",showImgList)//
			.html="画像一覧表示";

		})();



		var insertDiv = function() {
			if(~this.className.indexOf("inX")){
				var __div=div.clone().on("input", autoresize);
				if(this._num<divs.length){
					divs[this._num].before(__div);
					divs.splice(this._num, 0, __div);
				}else{
					divs[divs.length-1].after(__div);
					divs[this._num] = __div;
				}
			}
			renderButton();
		};

		var DivsLoop = function(fn) {
			for(var i = 0,l = divs.length; i<l; ++i){
				fn(divs[i], i, l);
			}
		};

		var controlCss = {
				userSelect : "none",
				mozUserSelect : "none",
				webkitUserSelect : "none",
				msUserSelect : "none"
			};
		var buttonCss=_Object({
				textShadow:"2px 2px 1px rgba(0,0,0,0.36),-1px -1px 1px rgba(255,255,255,1)",
				fontSize :"1.2em",
				cursor:"pointer"
			}).add(controlCss);
		Element(doc.body).styleSetter(controlCss);
		var inX = new Element("div").styleSetter(buttonCss).elementSetter({
			innerHTML:"＋",
			className:"inX control"
		});
		inX.color="blue";
		var delX = new Element("div").styleSetter(buttonCss).elementSetter({
			innerHTML:"×",
			className : "delX control"
		});
		delX.color="red";
		var createInX = function(d, i, l) {
			var p = d.element.getBoundingClientRect();
			inX.clone().absolute(~~window.pageXOffset+~~p.left-25,~~window.pageYOffset+~~p.top-10,20,15).appendTo(controller).elementSetter({
				_num: i,
				_message :"ここに行を挿入"
			}).on("mouseover",showMessage).on("mouseout",hideMessage).on("click",insertDiv);
			if(i === l-1){
				inX.clone().absolute(~~window.pageXOffset+~~p.left-25,~~window.pageYOffset+~~p.bottom-10,20,15).appendTo(controller).elementSetter({
					_num: l,
					_message :"ここに行を挿入"
				}).on("mouseover",showMessage).on("mouseout",hideMessage).on("click",insertDiv);
			}
		};

		var deleteDiv = function() {
			if(~this.className.indexOf("delX")){
				divs[this._num].remove();
				divs.splice(this._num, 1);
			}
			renderButton();
		};

		var createDelX= function(d, i, l) {
			var p = d.element.getBoundingClientRect();
			delX.clone().absolute(~~window.pageXOffset + ~~p.left - 50,~~window.pageYOffset + ~~p.top + ~~(p.height/2) - 10,20,15)
			.appendTo(controller).elementSetter({
				_num: i,
				_message :"この行を削除"
			}).on("mouseover",showMessage).on("mouseout",hideMessage).on("click",deleteDiv);
		};

		//+ボタン×ボタン作る//name inX,inY,delX,delY
		renderButton = function() {
			controller.removeChildAll();
			DivsLoop(createInX);
			if(divs.length>1)DivsLoop(createDelX);
		};

		var positioningInX = function(d, i, l) {
			var p = d.element.getBoundingClientRect();
			controller.query(".inX",i,{isWrap:true}).setPosition(~~window.pageXOffset+~~p.left - 25, ~~window.pageYOffset+~~p.top-10);
			if(i===l-1){controller.query(".inX",l,{isWrap:true}).setPosition(~~window.pageXOffset+~~p.left - 25, ~~window.pageYOffset+~~p.bottom-10);}
		};

		var positioningDelX = function(d, i, l) {
			var p = d.element.getBoundingClientRect();
			controller.query(".delX",i,{isWrap:true}).setPosition(~~window.pageXOffset+~~p.left-50,~~window.pageYOffset+~~(p.top+p.height/2)-10);
		};

		//buttonの配置を再セット
		var buttonPositioning = function() {
			DivsLoop(positioningInX);
			if(divs.length>1)DivsLoop(positioningDelX);
		};

		doc.callbackImageCreate = function(images){
			for(var i = 0,l = images.length;i<l;++i){
				secondBody.create("img")//
				.elementSetter({src:encodeURI( images[i]),className:"drag page"+currentPage}).styleSetter({
					position : "absolute",
					left : "50px",
					top : "250px",
					cursor : "move"
				});
			}
		};

		var getAllCell = function(){
			return doc.getElementsByName("cell");
		};


		getPdfData = function(){
				var pd ={
					pdfid:pdfid,
					maxPage:maxPage,
					title:fom.element.title.value
				};
				for(var i = 1;i<= maxPage;++i){
					var pdi=pd["page"+i]=[];
					var elms = body.queryAll(".page"+i);
					if(elms){
						for(var j = 0,l = elms.length;j<l;++j){
							var con ={};
							var context ={};
							var elm = elms[j];
							var isImage = elm.tagName==="IMG";
							con.contentType = isImage ? "img" :"text";
							if(isImage){
								context.src=elm.src;
							}else{
								context.text=elm.value;
							}
							var p = elm.getBoundingClientRect();
							context.x=(~~p.left + ~~window.pageXOffset);
							var y = (~~p.top + ~~window.pageYOffset);

							context.y=y;
							con.contentText =JSON.stringify(context);
							pdi[pdi.length]=con;
						}
					}
				}
				var cells = getAllCell();
				var pdi=pd.page1;
				for(var i=0,len=cells.length;i < len;++i){
					var cell=cells[i];
					var con ={contentType:"cell"};
					var context={
						bg:getRGBorRGBA(cell.style.backgroundColor),
						bd:getRGBorRGBA(cell.style.borderColor),
						text:cell.value,
					};
					con.contentText = JSON.stringify(context);
					pdi[pdi.length]=con;
				}
				return JSON.stringify(pd);
		};


		//受け取るのはimages[{src:"",x:0,y:0},,,,]
		var appendImage = function(images,page){
			page = page || currentPage;
			var visibility= page==currentPage ? "visible":"hidden";
			for(var i = 0,l = images.length;i<l;++i){
				var img = images[i];
				secondBody.create("img")//
				.elementSetter({
					src:encodeURI(img.src),
					className:"drag page"+page,
					_message:"ドラッグで移動できます"
				}).styleSetter({
					position : "absolute",
					left : img.x+"px",
					top : img.y+"px",
					cursor : "move",
					visibility:visibility
				}).on("mouseover",showMessage)
				.on("mouseout",hideMessage);
			}
		};

		doc._position = {x:0,y:0};
		doc.addEventListener("mousedown",function(e){
			this.isMouseDown=true;
			doc._position.x=e.pageX;
			doc._position.y=e.pageY;
			this.isMove =(this._targetElement = e.target).className.indexOf("drag")!== -1;

		},false);

		doc.addEventListener("mousemove",function(e){
			if(this.isMouseDown){
				var x = e.pageX-this._position.x;
				var y =e.pageY - this._position.y ;
				doc._position.x=e.pageX;
				doc._position.y=e.pageY;
				if(this.isMove){
					var el = this._targetElement;
					var s = el.style;
					s.left = parseInt(s.left)+~~x+"px";
					s.top = parseInt(s.top)+~~y+"px";
				}
			}
		},false);

		doc.addEventListener("mouseup",function(e){
			this.isMouseDown=false;
			this.isMove=false;
			this._targetElement=null;
		},false);

		var PDFsubmit=function(){
			fom.element.dispatchEvent(fireonsubmit);
			fom.element.submit();
		};

		window.addEventListener("keydown",function(e){
			//console.log(e.keyCode);
			if(e.ctrlKey){
				var k = e.keyCode;
				switch (k) {
				case 72:/*h*/PDFsubmit();e.preventDefault(); break;
				case 71:/*g*/e.preventDefault();fullPDF();return false;
				case 83:/*s*/e.preventDefault();createAjaxData();pedit.ajax(1,pdfdataObject)();break;
				default:break;
				}
			}
		},false);

		var appendDivs = function(defaultDivCount){
			for(var i = 0;i<defaultDivCount;++i){
				divs[i] = div.clone().appendTo(fom).on("input", autoresize);
			}
			renderButton();

		};
		var JSON = smr.global.JSON;

		//ajaxでデータを取得したやつ
		pedit.insert = function(data){
			pdfid = data.pdfid;
			maxPage = data.maxPage;
			var f = fom.element;
			f.title.value = data.title;
			if(data.page1==="EmptyPage"){
				yoko.value = 2;
				return	PDFsubmit();
			}

			var contents ={cell:{},img:{},text:{}};
			//cell img text の順番
			for(var i = 1;i <= maxPage;++i){
				var p = data["page"+i];
				for(var key in contents){
					contents[key][i]=[];
				}
				if(p==="EmptyPage")continue;
				for(var j = 0,len=p.length;j < len;++j){
					var pj=p[j];
					var type=pj.contentType;
					var con = contents[type][i];
					con[con.length]=JSON.parse(pj.contentText);
				}
			}

			var tableHeight = contents.cell[1].length/getYoko();
			while(divs[0]){
				divs.shift().remove();
			}
			appendDivs(tableHeight);
			var cells=document.getElementsByName("cell");
			var con = contents.cell[1];
			for(var i=0,len=con.length;i<len;++i){
						var c=cells[i];
						var co=con[i];
						var s =c.style;
						c.value=co.text;
						s.backgroundColor=stringCounter(co.bg,",") === 2 ? "rgb("+co.bg+")" : "rgba("+co.bg+")";
						s.borderColor=stringCounter(co.bd,",") === 2 ? "rgb("+co.bd+")" : "rgba("+co.bd+")";
			}
			for(var i = 0,len = divs.length;i<len;++i){
				autoresize.call(divs[i].element);
			}
			for(var key in con=contents.img){
				appendImage(con[key],key);
			}
			for(var key in con=contents.text){
				addsMoveText(con[key],key);
			}
			PDFsubmit();

		};

		//デフォルトで四個Divを作る

		appendDivs(4);



		fom.onsubmit=function(){
			fom.element.pdfdata.value=getPdfData();
		};



		//課題の内容をセットしておく
		// (function() {
		// 	var ss=[
		// 	           ["クラス記号","IH13A223"],
		// 	           ["氏名","大原有紀"],
		// 	           ["年齢",~~((new Date()-new Date("1993/6/18"))/1000/24/60/60/365.25)+"歳"],
		// 	           ["趣味","プログラムで何か作る"]
		// 	      ];
		// 	var bgs=[
		// 	         ["rgba(150,180,220,1)","rgba(25,155,177,1)"],
		// 	         ["rgba(125,155,185,1)","rgba(50,175,200,1)"],
		// 	         ["rgba(80,110,120,1)","rgba(105,225,255,1)"],
		// 	         ["rgba(55,75,115,1)","rgba(125,250,255,1)"]
		// 	         ];
		// 	var bds=[
		// 	         ["rgba(255,200,200,1)","rgba(50,50,255,1)"],
		// 	         ["rgba(255,155,155,1)","rgba(100,100,255,1)"],
		// 	         ["rgba(255,100,100,1)","rgba(155,155,255,1)"],
		// 	         ["rgba(255,50,50,1)","rgba(200,200,255,1)"]
		// 	         ];
		// 	for(var i = 0,li=divs.length;i<li;++i){
		// 		for(var j=0,cn=divs[i].children,lj=cn.length;j<lj;++j){
		// 			var c = cn[j];
		// 			var s =c.style;
		// 			c.value=ss[i][j];
		// 			s.backgroundColor=bgs[i][j];
		// 			s.borderColor=bds[i][j];
		// 		}
		// 	}
		// 	nextPage();
		// 	doc.callbackImageCreate([smr.imagePath[0]]);
		// 	Textarea().autoResize().absolute(150,150).elementSetter({
		// 		className:"drag "+"page"+currentPage,
		// 		value:"スパイラルタワー22階からの景色"
		// 	}).styleSetter({
		// 		padding:"50px 5px 5px 5px",
		// 		cursor:"move",
		// 		borderWidth :"0px",
		// 		backgroundColor:"rgba(0,0,0,0.05)"
		// 	}).appendTo(secondBody);
		// 	nextPage();
		// 	doc.callbackImageCreate([smr.imagePath[1]]);
		// 	Textarea().autoResize().absolute(150,150).elementSetter({
		// 		className:"drag "+"page"+currentPage,
		// 		value:"高校生の時使ってたPCが動かなくなった時の画像"
		// 	}).styleSetter({
		// 		padding:"50px 5px 5px 5px",
		// 		cursor:"move",
		// 		borderWidth :"0px",
		// 		backgroundColor:"rgba(0,0,0,0.05)"
		// 	}).appendTo(secondBody);
		// 	prevPage();
		// 	prevPage();


		//
		// })();
		pedit.main&&pedit.main();
		//一回さぶみっとく
		//PDFsubmit();

	};

})(smr, window,pedit);

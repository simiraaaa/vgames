var pedit = pedit || {};

(function(pedit,smr,undefined){
  pedit.document = null;
  pedit.ajax = function(key,o,f){
    o = o || {};
    o.key=key;
    var sync =
    key === 2 ||
    key === 0 ||
    key === 6 ;

    return function(){
      var isOK=f&&f();
      if(isOK !== undefined && !isOK){ return;}
      smr.ajax.load({
        dataType:"json",
        type:"post",
        data:o,
        async:!sync,
        url:pedit.path+"/serv/jv34_k09.PdfController",
        success:function(data){
          var s = data.status;
          if(s==="success"){
            alert(data.text);
          }else if(s==="alert"){
            alert(data.text);
          }else if(s==="replace"){
            document.body.style.visibility="hidden";
            smr.global.location.href=data.url;
          }else if (s===3){
            pedit.insert(data.pdfdata);
          }else if(s === "delete"){
            alert(data.text);
            document.body.style.visibility="hidden";
            smr.global.location.reload();
          }else if(s === "search"){
            var d = pedit.document ||( pedit.document = smr.dom.Element());
            var list = d.query("#pdflist",0,{isWrap:true,setCache:true,getCache:true});
            var child = data.text;
            while(child[0]){
              var c = child.shift();
              pedit.PdfViewer({
                pdfid:c.pdfId,
                userid:c.userId
              },c.pdfTitle).appendTo(list);
            }
            list.showDisp();
          }
          else{
            console.log(data.text);
          }
        }
      });
    };
  };

  smr.define("pedit.Controller",{
    superClass:pedit.Button,
    init:function(key,text,o,f){
      this.superInit(text);
      this.onclick=function(){
        var s = this.style;
      };
      this.onclick=pedit.ajax(key,o,f);
    }
  });

  smr.define("pedit.Create",{
    superClass:pedit.Controller,
    init: function(){
      this.superInit(0,"新しくPDFを作る");
    }
  });

  smr.define("pedit.Save",{
    superClass:pedit.Controller,
    init: function(pdfdata,f){
      this.superInit(1,"保存する",pdfdata,f);
    }
  });

  smr.define("pedit.Delete",{
    superClass:pedit.Controller,
    confirm:function(){
      return confirm("本当に削除しますか?");
    },
    init: function(id){
      this.superInit(2,"このPDFを削除する",{pdfid:id},this.confirm);
    }
  });
  smr.define("pedit.Copy",{
    superClass:pedit.Controller,
    init: function(o){
      this.superInit(6,"コピーを作る",o);
    }
  });

  smr.define("pedit.PdfViewer",{
    superClass:smr.dom.Element,
    init: function(o,title,noCopy){

      var qs = smr.util.queryString.stringify(o);
      this.superInit("div");
      this.styleSetter({
        minHeight:"250px",
        cssFloat:"left",
        margin:"5px",
        wordWrap:"break-word",
        width:"150px"
      }).element.className="pdf";
      var margin ={margin:"5px"};
      this.create("iframe").elementSetter({
        src:pedit.path+"/serv/jv34_k09.ViewPdf?"+qs,
        width:"150px",
        height:"200px"
      });
      title &&(this.create("div").elementSetter({
        className:"title"
      }).styleSetter(margin).element.textContent = title);
      this
      .create("div")
      .styleSetter(margin)
      .create("button")
      .elementSetter({
        type:"button",
        textContent:"大きく表示"
      }).onclick = function(){
        var f = document.pdfform;
        var elms = f.elements;
        elms.pdfid.value = o.pdfid;
        var uid=elms.userid;
        uid && (o.userid !== undefined) && (
          uid.value = o.userid
        );
        f.submit();
      };

      noCopy || (this.append(pedit.Copy(o).styleSetter(margin)));

    }
  });
  var sendTextObject ={query:null};
  smr.define("pedit.Search",{
    superClass:pedit.Controller,
    send : function(){
      var d = pedit.document ||( pedit.document = smr.dom.Element());
      var list = d.query("#pdflist",0,{isWrap:true,setCache:true,getCache:true});
      list.noneDisp();
      list.removeChildAll();
      sendTextObject.query=d.query("#pdfTitleText").value;
    },
    init: function(){
      this.superInit(7,"検索",sendTextObject,this.send);
    }
  });

  var downEnter = function(e){
    if(e.keyCode!==13)return;
    pedit.ajax(7,sendTextObject,pedit.Search.prototype.send)();
  };
  smr.define("pedit.SearchDiv",{
    superClass:smr.dom.Element,

    init:function(){
      this.superInit("div");
      this.styleSetter({margin:"5px"});
      this.create("span")
      .styleSetter({margin:"0 5px"})
      .html = "他のユーザのPDFが検索できます";
      this.create("input").elementSetter({
        id : "pdfTitleText",
        type:"text"
      }).styleSetter({
        margin:"0 5px"
      }).onkeydown=downEnter;
      pedit.Search().appendTo(this);
    }
  });

})(pedit,smr);

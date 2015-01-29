var pedit = pedit || {};
(function(pedit,smr,undefined){
  pedit.set= function(path){
    if(path[path.length-1]==="/"){
      this.path=path.substring(0,path.length-1);
    }else{
      this.path=path;
    }
  };
  smr.define("pedit.Button",{
    superClass:smr.dom.Element,
    init:function(text){
      this.superInit("button");
      this.elementSetter({
        innerHTML:text,
        type:"button"
      }).styleSetter({
        color:"white",backgroundColor:"red"
      });
    }
  });
  smr.define("pedit.Logout",{
    superClass:pedit.Button,
    init: function(){
      this.superInit("ログアウト");
      this.onclick=function(){
        smr.ajax.load({
          dataType:"json",
          url:pedit.path+"/serv/jv34_k09.Logout",
          success:function(data){
            smr.global.location.href=data.url;
          }
        });
      };
    }
  });




})(pedit,smr);

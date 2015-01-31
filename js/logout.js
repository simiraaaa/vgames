var vg = vg || {};
(function(vg,smr,undefined){
  vg.set= function(path){
    if(path[path.length-1]==="/"){
      this.path=path.substring(0,path.length-1);
    }else{
      this.path=path;
    }
  };
  smr.define("vg.Button",{
    superClass:smr.dom.Element,
    init:function(text){
      this.superInit("button");
      this.elementSetter({
        innerHTML:text,
        type:"button"
      }).styleSetter({
        color:"white",backgroundColor:"orange"
      });
    }
  });
  smr.define("vg.Logout",{
    superClass:vg.Button,
    init: function(){
      this.superInit("ログアウト");
      this.onclick=function(){
        smr.ajax.load({
          dataType:"json",
          url:vg.path+"/s/logout.json",
          success:function(data){
            smr.global.location.href=data.url;
          }
        });
      };
    }
  });




})(vg,smr);

(function(smr, undefined) {
  "use strict";

    var window = smr.global;
    window.onload = function() {
        var document = window.document;
        var Element = smr.dom.Element;
        var body = new Element(document.body);


        smr.dom.Icon({
            fontSize : "3em"
        }).appendTo(body).setText("アカウントの作成");
        var div = body.create("div");
        div.styleSetter({
        margin : "0 auto",
        width : "600px",
        textAlign : "center",
        fontSize : "1.5em"
        });
        div.create("div").styleSetter({
            fontSize : "0.8em"
        }).html = "すべて100文字以下で入力してください。<br>*は必須";
        var margin10 = {
            margin : "10px"
        };
        var input_div = new Element("div");
        input_div.create("div");
        input_div.create("div").elementSetter({
            className : "errorMessage"
        }).styleSetter({
        fontSize : "0.7em",
        color : "red"
      }).html="<br>";
        input_div.styleSetter(margin10).create("input").elementSetter({
            type : "text"
        }).width = 600;
        var inputs = {};
        (function() {

            var propNames = [ "id", "pass", "check", "name" ];
            var texts = [ "*ID", "*パスワード", "*パスワード(確認)", "表示名(後で変更できます)" ];

            for ( var i = 0, len = propNames.length; i < len; ++i) {
                var name = propNames[i];
                var d = input_div.clone().appendTo(div);
                d.element.firstChild.innerHTML = texts[i];
                inputs[name] = Element(d.element.lastChild);
            }
            inputs.pass.element.type = inputs.check.element.type = "password";
        })();
        var errors=body.queryAll(".errorMessage");

        var showLength = function(){
          var len=this.value.length;
          var error =  this.error||(this.error = this.parentNode.querySelector(".errorMessage"));
          if(len>100){return over100(error,len);}
          error.style.color="blue";
          error.isError=false;
          error.innerHTML = len+"文字 あと"+(100-len)+"文字入力可能";
        };

        var CHECK_REGEX = /[^\w]/;

        var checkAlphabet =  function(){
          var str=this.value;
          if(str){
            var error =  this.error||(this.error = this.parentNode.querySelector(".errorMessage"));
            if(CHECK_REGEX.test(str)){
              error.style.color="red";
              error.isError = true;
              error.innerHTML="半角英数字で入力してください";
              return ;
            }else{
              error.isError = false;
            }
          }
          return showLength.call(this);
        };
        var deleteCheck = function(){
          inputs.check.value="";
          errors[2].innerHTML="<BR>";
        };
        var over100 = function(error,len){
          error.style.color="red";
          error.isError=true;
          error.innerHTML=len+"文字 100文字以内で入力してください";
        };

        var checkPass = function(){
          if(inputs.pass.value === this.value){return checkAlphabet.call(this);}
            var error =  this.error||(this.error = this.parentNode.querySelector(".errorMessage"));
            error.style.color="red";
            error.isError=true;
            error.innerHTML="上のパスワードと違います。";
        };

        inputs.id.oninput = inputs.pass.oninput=checkAlphabet;
        inputs.pass.oninput=deleteCheck;
        inputs.check.oninput = checkPass;
        inputs.name.oninput=showLength;

        var checkSend = function() {
          var errMes="";
            for(var i =0,er;er=errors[i++];){
                if(er.isError){
                    alert("不正な値があります。入力した値を見直してください。");return;
                }
            }
            if(!inputs.id.value){
              errMes+="ID\n";
            }
            if(!inputs.pass.value){
              errMes+="パスワード\n";
            }
            if(!inputs.check.value){
              errMes+="パスワードの確認\n";
            }
            if(errMes){alert(errMes+"は必須です");return;}
            div.noneDisp();
            var div2=body.create("div").styleSetter({
              fontSize:"1.5em",
              margin:"0 auto",
              width:"600px",
              textAlign:"center"
            });
            div2.html="登録中です。<br>しばらくお待ちください。";
          smr.ajax.load({
            type:"POST",
            url:smr.ajax._url,
            dataType:"json",
            charset:"UTF-8",
            data:{
              id:inputs.id.value,
              pass:inputs.pass.value,
              check:inputs.check.value,
              name:inputs.name.value
            },
            success:function(data){
              if(data.status==="success"){
                div2.html="登録が完了しました。";
                alert("マイページへ移動します");
                window.location.href=data.text;
              }else if(data.status ==="alert"){
                alert(data.text);
                div2.remove();
                div.showDisp();
              }else{
                alert("原因不明のエラー"+data.text);
                div.showDisp();
                div2.remove();
              }

            }
            }
          );


        };


        div.create("a").elementSetter({
        href : "javascript:void 0",
        innerHTML : "登録"
        }).styleSetter({
        backgroundColor : "red",
        color : "white",
        padding : "5px",
        textDecoration : "none",
        textShadow : "1px 1px #700"
        }).on("mouseover", function() {
            this.style.backgroundColor = "#f55";
        }).on("mouseout", function() {
            this.style.backgroundColor = "red";
            this.style.textShadow = " 1px 1px #700";
        }).on("mousedown", function() {
            this.style.textShadow = " -1px  -1px black";
        }).on("mouseup", function() {
            this.style.textShadow = " 1px 1px #700";
        }).on("dragstart", function(e) {
            e.preventDefault();
        }).on("click",checkSend);

    };
})(smr);

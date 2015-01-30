var vg = vg || {};

! function(smr, vg, undefined) {
  //window.onloadでヘッダを整形、イベント設定
  window.addEventListener("load", function() {
    var Element = smr.dom.Element;
    var h = document.getElementById("header");
    if (!h) return;
    h = Element(h);
    var icon = smr.dom.Icon({
      position: "absolute",
      left: "10%",
      top: "5px",
      fontSize: "1.7em",
      cursor: "pointer",
      height: "30px",
      width: "40px"
    }).setText("V").on("click", function() {
      location.href = vg.path;
    }).appendTo();

    //icon.firstChild.style.border = icon.lastChild.style.border = "1px solid red";
    var d = h.create("div")
      .styleSetter({
        margin: "auto",
        position: "relative",
        top: "0%",
        left: "10%",
        width:"90%"
      });
    var f = d.create("form").elementSetter({
      method: "post",
      action: "#",
    }).styleSetter({
      margin:"auto",
    });
    f.create("span").html = "ジャンル:";
    var s = f.create("select")
      .elementSetter({
        name: "genreid"
      }).styleSetter({
        fontSize: "110%",
      });

    for (var i = 0, len = vg.GENRE_LIST.length; i < len; ++i) {
      s.create("option").elementSetter({
        value: "" + i,
        textContent: vg.GENRE_LIST[i]
      });
    }

    f.create("input").elementSetter({
      name: "gtitle",
      type: "text"
    }).styleSetter({

      fontSize: "120%",
      width: "35%",
    });

    f.create("input").elementSetter({
      type: "submit",
      value: "検索"
    }).styleSetter({
      background: "#eee",
      fontSize: "120%"
    });

    var myPageText = vg.isGuest ? "ログイン" : "マイページ";

    f.create("a").elementSetter({
      href: "#",
      textContent: myPageText
    }).styleSetter({
      color:"white",
      margin:"0 5px",
      textDecoration:"none"
    }).on("mouseover",function(){
      this.style.color="silver";
    }).on("mouseout",function(){
      this.style.color="white";
    });


  }, false);
}(smr, vg);
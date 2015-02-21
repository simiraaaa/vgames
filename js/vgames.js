var vg = vg || {};

! function(smr, vg, undefined) {


  vg.set = function(path) {
    if (path[path.length - 1] === "/") {
      this.path = path.substring(0, path.length - 1);
    } else {
      this.path = path;
    }
  };
  if (!vg.path) vg.path = "/vgames";


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
      location.href = vg.path + "/s/mypage.html";
    }).appendTo();
    icon.element.id = "logo";

    //icon.firstChild.style.border = icon.lastChild.style.border = "1px solid red";
    var d = h.create("div")
      .styleSetter({
        margin: "auto",
        position: "relative",
        top: "0%",
        left: "8%",
        width: "82%"
      });
    var f = d.create("div").styleSetter({
      margin: "auto",
    });
    f.create("span").html = "ジャンル:";
    var s = f.create("select")
      .elementSetter({
        name: "genreid"
      }).styleSetter({
        fontSize: "110%",
      });
    s.create("option").elementSetter({
      value: "all",
      textContent: "ジャンルを選択"
    });
    for (var i = 0, len = vg.GENRE_LIST.length; i < len; ++i) {
      s.create("option").elementSetter({
        value: "" + i,
        textContent: vg.GENRE_LIST[i]
      });
    }

    var genre = document.getElementById("genre");
    if (genre) {
      Element(genre).append(s.clone().styleSetter({
        fontSize: ""
      }));
    }

    var queryText = f.create("input").elementSetter({
      name: "query",
      type: "text"
    }).styleSetter({

      fontSize: "120%",
      width: "35%",
    }).on("keydown", function(e) {
      (e.keyCode === 13) && searchAjax();
    });

    var body = Element(document.body);

    var gamepath = vg.path + "/s/play.html?gameid=";


    vg.getCreateList = function(glist) {
      return function(gdata) {
        var div = Element("div").styleSetter({
          cssFloat: "left",
          margin: "5px",
          width: "250px",
          height: "250px",
          background: "#eee",
          borderBottom: "1px solid gray",
          borderRight: "1px solid gray",
          borderRadius: "10px"
        });
        var a = div
          .create("a")
          .elementSetter({
            href: gamepath + gdata.gid
          }).styleSetter({
            width: "100%",
            height: "100%",
            position: "relative",
            margin: "auto"
          });
        a.create("div")
          .styleSetter({
            position: "relative",
            margin: "auto",
            width: "250px",
            height: "220px"
          })
          .create("img").styleSetter({
            margin: "auto",
            maxHeight: "200px",
            maxWidth: "200px",
            position: "absolute",
            left: "0",
            right: "0",
            top: "0",
            bottom: "0",
          }).elementSetter({
            src: vg.path + "/games/" + gdata.gimage
          });
        a.create("div")
          .styleSetter({
            width: "250px",
            position: "relative",
            top: "5px",
            textAlign: "center"
          }).element.textContent = gdata.gname;
        glist.append(div);

      };
    };
    var searchFunc = function(data) {
      body.removeChildAll();
      h.appendTo();
      icon.appendTo();
      var div = body.create("div");
      div.element.className = "dbody";
      div.create("div").elementSetter({
        className: "top",
        textContent: "検索結果",
      });
      var divin = div.create("div");
      divin.element.className = "dbodyin";
      var glist = divin.create("div");
      glist.element.id = "gamelist";

      data.forEach(vg.getCreateList(glist));

    };
    var searchAjax = function() {
      vg.getAjax.search(searchFunc, queryText.value, s.value);
    };
    f.create("button").elementSetter({
      type: "button",
      textContent: "検索"
    }).styleSetter({
      background: "orange",
      color: "white",
      fontSize: "120%"
    }).onclick = searchAjax;

    var myPageText = vg.isGuest ? "ログイン" : "マイページ";

    f.create("a").elementSetter({
      href: vg.path + "/s/mypage.html",
      textContent: myPageText
    }).styleSetter({
      color: "white",
      margin: "0 5px",
      textDecoration: "none"
    }).on("mouseover", function() {
      this.style.color = "silver";
    }).on("mouseout", function() {
      this.style.color = "white";
    });
    vg.Logout().styleSetter({
      fontSize: "120%"
    }).appendTo(f);


  }, false);
}(smr, vg);

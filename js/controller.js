var vg = vg || {};

(function(vg, smr, undefined) {
  vg.ajax = function(path, param, success, get, sync) {

    smr.ajax.load({
      type: get ? "get" : "post",
      async: !sync,
      dataType: "json",
      data: param,
      url: vg.path + "/s/" + path,
      success: success
    });

  };


  window.addEventListener("load", function() {

    var now_loading = smr.dom.Element("div")
      .styleSetter({
        fontSize: "1.5em"
      });
    vg.appendLoading = function(str) {
      now_loading.styleSetter({
        position: "absolute",
        width: "100%",
        left: "0px",
        top: (~~innerHeight / 2 + pageYOffset) + "px",
        textAlign: "center"
      }).appendTo().element.textContent = str;
    };
    vg.removeLoading = function() {
      now_loading.remove();
    };
  });

  vg.getAjax = {
    GET: "get.json",

    gameup: function(slf) {
      slf = slf || this;
      slf.style.visibility = "hidden";
      vg.appendLoading("投稿しています。しばらくお待ちください。");
      vg.ajax("gameup.json", new FormData(slf),
        function(data) {
          if (data.status === "success") {
            document.body.style.visibility = "hidden";
            smr.global.location.href = data.url;
          } else {
            slf.style.visibility = "visible";
            vg.removeLoading();
            alert(data.text);
          }
        }, false);
    },

    gameList: function(f, uid) {
      if (uid === undefined) uid = "";
      vg.ajax(this.GET, {
          key: "0",
          userid: uid
        },
        function(data) {
          if (data.status === "success") {
            f(data.text);
          } else {
            alert(data.text);
          }
        }, true
      );
    },

    gameImage: function(f, gid) {
      vg.ajax(this.GET, {
          key: "1",
          gameid: gid
        },
        function(data) {
          if (data.status === "success") {
            f(data.text);
          } else {
            alert(data.text);
          }
        });

    },

    game: function(f, gid) {
      vg.ajax(this.GET, {
        key: "2",
        gameid: gid
      },
      function(data) {
        if (data.status === "success") {
          f(data.text);
        } else {
          alert(data.text);
        }
      });

    },


  };

})(vg, smr);

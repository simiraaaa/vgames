var vg = vg || {};

(function(vg, smr, undefined) {
  vg.ajax = function(path, param, success, get,sync) {

    smr.ajax.load({
      type: get ? "get" : "post",
      async:!sync,
      dataType: "json",
      data: param,
      url: vg.path + "/s/" + path,
      success: success
    });

  };

  vg.getAjax = {
    gameup: function(slf) {
      return vg.ajax("gameup.json", new FormData(slf),
        function(data) {
          if(data.status ==="success"){
            smr.global.location.href = data.url;
          }else{
            alert(data.text);
          }
        },false,true);
    }
  };

})(vg, smr);

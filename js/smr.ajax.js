(function(smr, undefined) {
    "use strict";

    var global = smr.global;
    var DOMParser = global.DOMParser;
    var JSON = global.JSON;
    var XMLHttpRequest = global.XMLHttpRequest;
    var CALLBACK = "callback";
    var callbackName = "smr.callbackFunctions";
    var callbackCount = 0;
    var head = null;
    var callbackFunctions = [];
    smr.defineProperty("callbackFunctions", callbackFunctions);

    var FormData = global.FormData;

    var DEFAULT_PARAMS = {
        type : "GET",
        async : true,
        data : null,
        contentType : "application/x-www-form-urlencoded",
        charset : null,
        dataType : "text",
        responseType : null, // blob or arraybuffer
        username : null,
        password : null,
        success : function(data) {
            alert("success!!\n" + data);
        },
        error : function(data) {
            alert("error!!");
        },
        beforeSend : null,
    };

    var NOT_PARSE = function(data) {
        return data;
    };

    var PARSER_TABLE = {
        /**
         * @memberOf PARSER_TABLE
         */

        /**
         * なにもしない
         */
        undefined : NOT_PARSE,

        /**
         * なにもしない
         */
        "" : NOT_PARSE,

        /**
         * なにもしない
         */
        text : NOT_PARSE,
        /**
         * XMLにparseして返す。
         *
         * @param data
         * @returns
         */
        xml : function(data) {
            return new DOMParser().parseFromString(data, "text/xml");
        },
        /**
         * divを生成しその中のinnerHTMLに追加して返す。
         *
         * @param data
         * @returns {smr.dom.Element}
         */
        dom : function(data) {
            var div = smr.dom.Element("div");
            div.html = data;
            return div;
        },

        /**
         * JSON.parseして返す
         *
         * @param data
         * @returns
         */
        json : function(data) {
            try {
                return JSON.parse(data);
            } catch (e) {
                console.dir(e);
                console.dir(data);
            }
        },

        /**
         * scriptを実行する
         *
         * @param data
         * @returns
         */
        script : function(data) {
            eval(data);
            return data;
        },

        /**
         * byte配列を返す
         *
         * @param data
         * @returns {Array}
         */
        bin : function(data) {
            var bytearray = [];
            for ( var i = 0, len = data.length; i < len; ++i) {
                bytearray[i] = data.charCodeAt(i) & 0xff;
            }
            return bytearray;
        },

    };
    var ajax = {

        /**
         * @memberOf smr.ajax
         */
        /**
         * @param params
         *            defaut={ <br>
         *            type : "GET",//or "POST"<br>
         *            async : true,//or false <br>
         *            data : null,//POST時に送信するデータ GET時のクエリ文字列<BR>
         *            //{key:value}の形式のオブジェクトでも指定できる<br>
         *            contentType : "application/x-www-form-urlencoded",<br>
         *            charset :
         *            null,//指定するとcontentTypeに";charset="+charsetの形式で追加される<br>
         *            dataType : "text",//受け取りたいデータのタイプ
         *            text,xml,dom,json,bin,script<br>
         *            responseType : null, // blob or arraybuffer<br>
         *            username : null,<br>
         *            password : null, <br>
         *            success : function(data) { alert("success!!\n" + data);
         *            },//成功時に実行する関数 <br>
         *            error : function(data) { alert("error!!"); },//失敗時に実行する関数<br>
         *            beforeSend : null, <br>}
         */
        load : function(params) {
            for ( var key in DEFAULT_PARAMS) {
                params[key] = params[key] === undefined ? DEFAULT_PARAMS[key] : params[key];
            }
            var xhr = new XMLHttpRequest();
            var parseFunc = PARSER_TABLE[params.dataType];

            if (params.charset) {
                params.contentType += ";charset=" + params.charset;
            }

            var type = params.type = params.type.toUpperCase();

            if (params.data) {
                var data = params.data;
                params.data = null;
                var qs = "";
                if (typeof data === "string") {
                    qs = smr.util.queryString.encodeURINonEqual(data);
                } else if(!(data instanceof FormData)){
                    qs = smr.util.queryString.stringify(data);
                }else{
                  qs = data;
                }

                if (type === "GET") {
                    params.url += "?" + qs;
                } else if (type === "POST") {
                    params.data = qs;
                }
            }
            xhr.open(type, params.url, params.async, params.username, params.password);

            if (type === "POST" &&!(params.data instanceof FormData)) {
                xhr.setRequestHeader("Content-Type", params.contentType);
            }

            if (params.responseType) {
                xhr.responseType = params.responseType;
            }

            if (params.beforeSend) {
                params.beforeSend(xhr);
            }

            if (params.password) {
                xhr.withCredentials = true;
            }

            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4) {
                    var stat = xhr.status;
                    // 0はローカル用?
                    if (stat === 200 || stat === 201 || stat === 0) {
                        if (xhr.responseType !== "arraybuffer" && xhr.responseType !== "blob") {
                            var data = parseFunc(xhr.responseText);
                            params.success(data);
                        } else {
                            params.success(xhr.response);
                        }
                    } else {
                        params.error(xhr.responseText);
                    }
                } else {
                    // ロード完了時以外
                }
            };

            xhr.send(params.data);

        },
        /**
         * JSONPを読み込む
         *
         * @param url
         * @param callback
         *            コールバック関数
         * @param callbackKey
         *            デフォルト callback
         */
        loadJSONP : function(url, callback, callbackKey) {
            callbackKey = callbackKey || CALLBACK;
            callbackFunctions[callbackCount] = callback;
            var name = callbackName + "[" + callbackCount + "]";
            ++callbackCount;
            url += (url.indexOf("?") === -1 ? "?" : "&") + callbackKey + "=" + name;
            if (!head) {
                head = global.document.head;
            }
            var script = smr.dom.Element("script").elementSetter({
                type : "text/javascript",
                charset : "UTF-8",
                src : url
            });
            script.element.setAttribute("defer", true);
            script.appendTo(head);
        }

    };

    smr.definePackage("smr.ajax", ajax);

})(smr);

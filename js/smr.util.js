(function(smr, undefined) {
    "use strict";

    var EQUAL = "=", AMP = "&";
    var global = smr.global;
    var encodeURIComponent = global.encodeURIComponent;

    var util = {};
    util.queryString = {

        /**
         * dataオブジェクトを key eq data[key] amp key eq data[key]な感じにする
         * 
         * @param data
         * @param eq
         *            デフォルト "="
         * @param amp
         *            デフォルト "&"
         * @returns
         */
        stringify : function(data, eq, amp) {
            eq = eq || EQUAL;
            amp = amp || AMP;
            var query = [];
            for ( var key in data) {
                query[query.length] = encodeURIComponent(key) + eq + encodeURIComponent(data[key]);
            }
            return query.join(amp);
        },
        /**
         * "="以外をencodeURIComponentする
         */
        encodeURINonEqual : function(s) {
            var ar = s.split(EQUAL);
            for ( var i = 0, len = ar.length; i < len; ++i) {
                ar[i] = encodeURIComponent(ar[i]);
            }
            return ar.join(EQUAL);
        },

        parse : function() {
            alert("smr.util.parse:あとで作る");
        }
    };
    smr.definePackages("smr.util", util);
})(smr);

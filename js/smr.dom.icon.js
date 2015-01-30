(function(smr) {
    "use strict";
    var Element = smr.dom.Element;
    smr.define("smr.dom.Icon", {
        /**
         * @memberOf smr.dom.Icon
         */
        firstChild : null,
        lastChild : null,
        superClass : Element,
        init : function(style, bg, text) {
            this.superInit("div");
            style = style || {};
            style = smr.Object(style).addIfNotHave({
                width : "600px",
                height : "200px",
                margin : "0 auto",
                color : "white",
                fontSize : "5em",
                textShadow : "-1px -2px black",
                textAlign : "center"
            });
            this.styleSetter(style);
            style = smr.Object({
                backgroundColor : bg || "orange",
                height : style.height,
                width : style.width
            });
            this.firstChild = this.create("div").styleSetter(style.add({
                position : "absolute",
                borderRadius : "1% 100%"
            }));
            this.lastChild = this.create("div").styleSetter(style.add({
                position : "absolute",
                borderRadius : "100% 1%"
            }));
        },
        /**
         * lastChildに中央配置になるDIV要素を追加して<br>
         * その中にテキストを配置
         *
         * @param text
         */
        setText : function(text) {
            var div = this.lastChild.create("div");
            div.html = text;
            var margin = (this.lastChild.element.clientHeight - div.element.clientHeight) / 2;
            div.styleSetter({
                margin : margin + "px 0"
            });
            return this;
        }
    });
})(smr);

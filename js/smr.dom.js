(function(smr, window, _Object, Object, Number, undefined) {

    "use strict";

    var Array = window.Array;
    var NodeList = window.NodeList;
    var dom = smr.using("smr.dom");
    var document = window.document;

    var pxToNumber = function(px) {
        return Number(px.replace("px", ""));
    };
    var hitTest =
        function(r1, r2) {
            return (r1.top < r2.bottom) && (r1.left < r2.right) && (r2.top < r1.bottom) && (r2.left < r1.right);
        };

    /**
     * @class smr.dom.Element
     */
    var Element =
        smr
            .define("smr.dom.Element", {
                /**
                 * @memberOf smr.dom.Element
                 */

                element : null,

                queryCache : null,

                queryAllCache : null,

                displayCache : null,

                init : function(elm) {
                    elm = elm || document;
                    this.element = (typeof elm === 'string') ? document.createElement(elm) : elm;
                    this.queryAllCache = {};
                    this.queryCache = {};

                },

                /**
                 * style.left,style.top
                 * 
                 * @param x
                 * @param y
                 * @returns {smr.dom.Element}
                 */
                setPosition : function(x, y) {
                    this.x = x;
                    this.y = y;
                    return this;
                },

                /**
                 * スタイルセットする
                 * 
                 * @param o
                 * @returns
                 */
                styleSetter : function(o) {
                    this.add.call(this.style, o);
                    return this;
                },

                /**
                 * エレメントにセットする
                 * 
                 * @param o
                 * @returns {smr.dom.Element}
                 */
                elementSetter : function(o) {
                    this.add.call(this.element, o);
                    return this;
                },

                /**
                 * 表示
                 * 
                 * @returns
                 */
                show : function() {
                    this.visible = true;
                    return this;
                },
                /**
                 * 非表示
                 * 
                 * @returns
                 */
                hide : function() {
                    this.visible = false;
                    return this;
                },

                /**
                 * 表示非表示の切り替え
                 * 
                 * @returns
                 */
                toggle : function() {
                    this.visible = !this.visible;
                    return this;
                },

                noneDisp : function() {
                    var disp = this.style.display;
                    if (disp === "none") {
                        return this;
                    }
                    this.displayCache = disp;
                    this.style.display = "none";

                    return this;
                },

                showDisp : function() {
                    this.style.display = this.displayCache || "";
                    return this;
                },

                isHitElement : function(elm) {
                    return hitTest(this.element.getBoundingClientRect(), (elm.element || elm)
                        .getBoundingClientRect());
                },

                /**
                 * 子の最後に追加
                 */
                append : function(child) {
                    this.element.appendChild((child.element || child));
                    return this;
                },

                /**
                 * 子の最初に追加
                 */
                prepend : function(child) {
                    this.element.insertBefore((child.element || child), this.element.firstChild);
                    return this;
                },

                /**
                 * 自分の次に追加
                 */
                after : function(elm) {
                    this.element.parentNode
                        .insertBefore((elm.element || elm), this.element.nextSibling);
                    return this;
                },

                /**
                 * 自分の前に追加
                 */
                before : function(elm) {
                    this.element.parentNode.insertBefore((elm.element || elm), this.element);
                    return this;
                },

                /**
                 * 引数の要素にappend
                 * 
                 * @param parent
                 * @returns
                 */
                appendTo : function(parent) {
                    if (parent) {
                        parent = parent.element || parent;
                    } else {
                        parent = document.body;
                    }
                    parent.appendChild(this.element);
                    return this;
                },

                /**
                 * 引数の要素にprepend
                 * 
                 * @param parent
                 * @returns
                 */
                prependTo : function(parent) {
                    if (parent) {
                        if (parent.prepend) {
                            parent.prepend(this);
                            return this;
                        }
                    } else {
                        parent = document.body;
                    }
                    parent.insertBefore(this.element, parent.firstChild);
                    return this;
                },

                /**
                 * コピー
                 * 
                 * @param {Boolean}
                 *            isOnly 子もコピーするか trueでしない
                 * @returns
                 */
                clone : function(isOnly) {
                    return this.constructor(this.element.cloneNode(!isOnly));
                },

                /**
                 * 親に捨てられる
                 */
                remove : function() {
                    this.element.parentNode.removeChild(this.element);
                    return this;
                },

                /**
                 * 子を捨てる
                 * 
                 * @param child
                 *            配列かElement
                 * @returns
                 */
                removeChild : function(child) {
                    if (null === child) {
                        return this;
                    }
                    if (child instanceof NodeList || child instanceof Array) {
                        for ( var elm = this.element, i = child.length - 1; i >= 0; --i) {
                            var c = child[i];
                            elm.removeChild((c.element || c));
                        }
                    } else {
                        this.element.removeChild((child.element || child));
                    }
                    return this;
                },

                /**
                 * 子を全て捨てる
                 */
                removeChildAll : function() {
                    var elm = this.element;
                    var cld;
                    while (cld = elm.firstChild) {
                        elm.removeChild(cld);
                    }
                    return this;
                },

                /**
                 * 作って自分に追加
                 */
                create : function(tag, method) {
                    var elm = Element(tag);
                    this[(method || "append")](elm);
                    return elm;
                },

                /**
                 * 固定化
                 * 
                 * @param x
                 * @param y
                 * @param width
                 * @param height
                 * @returns
                 */
                fixed : function(x, y, width, height) {
                    this.style.position = "fixed";
                    if (x !== undefined) {
                        this.x = x;
                    }
                    if (y !== undefined) {
                        this.y = y;
                    }
                    if (width !== undefined) {
                        this.width = width;
                    }
                    if (height !== undefined) {
                        this.height = height;
                    }
                    return this;
                },

                /**
                 * 絶対位置化
                 * 
                 * @param x
                 * @param y
                 * @param width
                 * @param height
                 * @returns
                 */
                absolute : function(x, y, width, height) {
                    this.style.position = "absolute";
                    if (x !== undefined) {
                        this.x = x;
                    }
                    if (y !== undefined) {
                        this.y = y;
                    }
                    if (width !== undefined) {
                        this.width = width;
                    }
                    if (height !== undefined) {
                        this.height = height;
                    }
                    return this;
                },

                /**
                 * addEventListenerと同じ
                 * 
                 * @param ename
                 * @param efunc
                 * @param cap
                 * @returns {smr.dom.Element}
                 */
                on : function(ename, efunc, cap) {
                    this.element.addEventListener(ename, efunc, !!cap);
                    return this;
                },

                /**
                 * removeEventListenerと同じ
                 * 
                 * @param ename
                 * @param efunc
                 * @param cap
                 * @returns {smr.dom.Element}
                 */
                off : function(ename, efunc, cap) {
                    this.element.removeEventListener(ename, efunc, !!cap);
                    return this;
                },
                /**
                 * 
                 * @param q
                 *            検索文字列 tag>#id.class
                 * @param i
                 *            index
                 * @param o
                 *            isWrap: true:ラッパークラスにして返すかfalse:elementで返すか
                 *            defaultでfalse getCache: キャッシュがある場合キャッシュを返すか
                 *            defaultでtrue setCache: キャッシュするか defaultでfalse
                 *            子要素が追加削除される予定ならキャッシュしないほうがよい method :
                 *            メソッド名を入れるとそのメソッドの引数にされて実行される(return する)
                 * @returns
                 */
                query : function(q, i, o) {
                    o = o || {
                        getCache : false,
                        setCache : true,
                        isWrap : false
                    };
                    o.getCache = o.getCache === undefined ? true : o.getCache;
                    if (o.getCache && this.queryCache[q + i + o.isWrap]) {
                        return this.queryCache[q + i + o.isWrap];
                    }
                    var elm =
                        (i) ? this.element.querySelectorAll(q)[i] : this.element.querySelector(q);

                    if (o.isWrap) {
                        elm = Element(elm);
                    }
                    if (o.setCache) {
                        this.queryCache[q + i + o.isWrap] = elm;
                    }
                    if (o.method) {
                        return this[o.method](elm);
                    }
                    return elm;
                },

                /**
                 * 
                 * @param q
                 *            検索文字列 tag>#id.class
                 * @param o
                 *            isWrap: true:ラッパークラスにして返すかfalse:elementで返すか
                 *            defaultでfalse getCache: キャッシュがある場合キャッシュを返すか
                 *            defaultでtrue setCache: キャッシュするか defaultでfalse
                 *            子要素が追加削除される予定ならキャッシュしないほうがよい method :
                 *            メソッド名を入れるとそのメソッドの引数にされて実行される(return する)
                 * @returns
                 */
                queryAll : function(q, o) {
                    o = o || {
                        getCache : false,
                        setCache : true,
                        isWrap : false
                    };
                    o.getCache = o.getCache === undefined ? true : o.getCache;
                    if (o.getCache && this.queryAllCache[q + o.isWrap]) {
                        return this.queryAllCache[q + o.isWrap];
                    }

                    var elms = this.element.querySelectorAll(q);

                    if (o.isWrap) {
                        elms = this.wrapList(elms);
                    }
                    if (o.setCache) {
                        this.queryAllCache[q + o.isWrap] = elms;
                    }
                    if (o.method) {
                        return this[o.method](elms);
                    }
                    return elms;
                },

                /**
                 * 配列をラップする
                 * 
                 * @param list
                 * @returns
                 */
                wrapList : function(list) {
                    var list2 = [];
                    for ( var i = list.length - 1; i >= 0; --i) {
                        list2[i] = Element(list[i]);
                    }
                    return list2;
                },

            });

    smr.defineGetters(Element.prototype, {
        classList : function() {
            return this.element.classList;
        },
        prev : function() {
            return (this.element.previousSibling) ? Element(this.element.previousSibling) : null;
        },
        next : function() {
            return (this.element.nextSibling) ? Element(this.element.nextSibling) : null;
        },
        style : function() {
            return this.element.style;
        },
        children : function() {
            return this.element.children;
        },
        parent : function() {
            return (this.element.parentNode) ? Element(this.element.parentNode) : null;
        }
    });

    smr.defineAccessors(Element.prototype, {
        visible : {
            set : function(v) {
                this.style.visibility = (v === true) ? "visible" : "hidden";
            },
            get : function() {
                return this.style.visibility !== "hidden";
            }
        },
        html : {
            set : function(v) {
                this.element.innerHTML = v;
            },
            get : function() {
                return this.element.innerHTML;
            }
        },
        value : {
            set : function(v) {
                this.element.value = v;
            },
            get : function() {
                return this.element.value;
            }
        },
        x : {
            set : function(x) {
                this.style.left = x + "px";
            },
            get : function() {
                return pxToNumber(this.style.left);
            }
        },
        y : {
            set : function(y) {
                this.style.top = y + "px";
            },
            get : function() {
                return pxToNumber(this.style.top);
            }
        },
        width : {
            set : function(w) {
                this.style.width = w + "px";
            },
            get : function() {
                return pxToNumber(this.style.width);
            }
        },
        height : {
            set : function(h) {
                this.style.height = h + "px";
            },
            get : function() {
                return pxToNumber(this.style.height);
            }
        },
        color : {
            set : function(c) {
                this.style.color = c;
            },
            get : function() {
                return this.style.color;
            }
        },
        backgroundColor : {
            set : function(c) {
                this.style.backgroundColor = c;
            },
            get : function() {
                return this.style.backgroundColor;
            }
        }
    });

    /**
     * onEvent
     */
    smr.defineSetters(Element.prototype, {
        onunload : function(f) {
            this.on("unload", f);
        },
        onstorage : function(f) {
            this.on("storage", f);
        },
        onpopstate : function(f) {
            this.on("popstate", f);
        },
        onpageshow : function(f) {
            this.on("pageshow", f);
        },
        onpagehide : function(f) {
            this.on("pagehide", f);
        },
        ononline : function(f) {
            this.on("online", f);
        },
        onoffline : function(f) {
            this.on("offline", f);
        },
        onmessage : function(f) {
            this.on("message", f);
        },
        onlanguagechange : function(f) {
            this.on("languagechange", f);
        },
        onhashchange : function(f) {
            this.on("hashchange", f);
        },
        onbeforeunload : function(f) {
            this.on("beforeunload", f);
        },
        onscroll : function(f) {
            this.on("scroll", f);
        },
        onresize : function(f) {
            this.on("resize", f);
        },
        onload : function(f) {
            this.on("load", f);
        },
        onfocus : function(f) {
            this.on("focus", f);
        },
        onerror : function(f) {
            this.on("error", f);
        },
        onblur : function(f) {
            this.on("blur", f);
        },
        onautocompleteerror : function(f) {
            this.on("autocompleteerror", f);
        },
        onautocomplete : function(f) {
            this.on("autocomplete", f);
        },
        onwaiting : function(f) {
            this.on("waiting", f);
        },
        onvolumechange : function(f) {
            this.on("volumechange", f);
        },
        ontoggle : function(f) {
            this.on("toggle", f);
        },
        ontimeupdate : function(f) {
            this.on("timeupdate", f);
        },
        onsuspend : function(f) {
            this.on("suspend", f);
        },
        onsubmit : function(f) {
            this.on("submit", f);
        },
        onstalled : function(f) {
            this.on("stalled", f);
        },
        onshow : function(f) {
            this.on("show", f);
        },
        onselect : function(f) {
            this.on("select", f);
        },
        onseeking : function(f) {
            this.on("seeking", f);
        },
        onseeked : function(f) {
            this.on("seeked", f);
        },
        onreset : function(f) {
            this.on("reset", f);
        },
        onratechange : function(f) {
            this.on("ratechange", f);
        },
        onprogress : function(f) {
            this.on("progress", f);
        },
        onplaying : function(f) {
            this.on("playing", f);
        },
        onplay : function(f) {
            this.on("play", f);
        },
        onpause : function(f) {
            this.on("pause", f);
        },
        onmousewheel : function(f) {
            this.on("mousewheel", f);
        },
        onmouseup : function(f) {
            this.on("mouseup", f);
        },
        onmouseover : function(f) {
            this.on("mouseover", f);
        },
        onmouseout : function(f) {
            this.on("mouseout", f);
        },
        onmousemove : function(f) {
            this.on("mousemove", f);
        },
        onmouseleave : function(f) {
            this.on("mouseleave", f);
        },
        onmouseenter : function(f) {
            this.on("mouseenter", f);
        },
        onmousedown : function(f) {
            this.on("mousedown", f);
        },
        onloadstart : function(f) {
            this.on("loadstart", f);
        },
        onloadedmetadata : function(f) {
            this.on("loadedmetadata", f);
        },
        onloadeddata : function(f) {
            this.on("loadeddata", f);
        },
        onkeyup : function(f) {
            this.on("keyup", f);
        },
        onkeypress : function(f) {
            this.on("keypress", f);
        },
        onkeydown : function(f) {
            this.on("keydown", f);
        },
        oninvalid : function(f) {
            this.on("invalid", f);
        },
        oninput : function(f) {
            this.on("input", f);
        },
        onended : function(f) {
            this.on("ended", f);
        },
        onemptied : function(f) {
            this.on("emptied", f);
        },
        ondurationchange : function(f) {
            this.on("durationchange", f);
        },
        ondrop : function(f) {
            this.on("drop", f);
        },
        ondragstart : function(f) {
            this.on("dragstart", f);
        },
        ondragover : function(f) {
            this.on("dragover", f);
        },
        ondragleave : function(f) {
            this.on("dragleave", f);
        },
        ondragenter : function(f) {
            this.on("dragenter", f);
        },
        ondragend : function(f) {
            this.on("dragend", f);
        },
        ondrag : function(f) {
            this.on("drag", f);
        },
        ondblclick : function(f) {
            this.on("dblclick", f);
        },
        oncuechange : function(f) {
            this.on("cuechange", f);
        },
        oncontextmenu : function(f) {
            this.on("contextmenu", f);
        },
        onclose : function(f) {
            this.on("close", f);
        },
        onclick : function(f) {
            this.on("click", f);
        },
        onchange : function(f) {
            this.on("change", f);
        },
        oncanplaythrough : function(f) {
            this.on("canplaythrough", f);
        },
        oncanplay : function(f) {
            this.on("canplay", f);
        },
        oncancel : function(f) {
            this.on("cancel", f);
        },
        onabort : function(f) {
            this.on("abort", f);
        },
        onwebkitfullscreenerror : function(f) {
            this.on("webkitfullscreenerror", f);
        },
        onwebkitfullscreenchange : function(f) {
            this.on("webkitfullscreenchange", f);
        },
        onwheel : function(f) {
            this.on("wheel", f);
        },
        onselectstart : function(f) {
            this.on("selectstart", f);
        },
        onsearch : function(f) {
            this.on("search", f);
        },
        onpaste : function(f) {
            this.on("paste", f);
        },
        oncut : function(f) {
            this.on("cut", f);
        },
        oncopy : function(f) {
            this.on("copy", f);
        },
        onbeforepaste : function(f) {
            this.on("beforepaste", f);
        },
        onbeforecut : function(f) {
            this.on("beforecut", f);
        },
        onbeforecopy : function(f) {
            this.on("beforecopy", f);
        },

        /**
         * offEvent
         */
        offunload : function(f) {
            this.off("unload", f);
        },
        offstorage : function(f) {
            this.off("storage", f);
        },
        offpopstate : function(f) {
            this.off("popstate", f);
        },
        offpageshow : function(f) {
            this.off("pageshow", f);
        },
        offpagehide : function(f) {
            this.off("pagehide", f);
        },
        offonline : function(f) {
            this.off("online", f);
        },
        offoffline : function(f) {
            this.off("offline", f);
        },
        offmessage : function(f) {
            this.off("message", f);
        },
        offlanguagechange : function(f) {
            this.off("languagechange", f);
        },
        offhashchange : function(f) {
            this.off("hashchange", f);
        },
        offbeforeunload : function(f) {
            this.off("beforeunload", f);
        },
        offscroll : function(f) {
            this.off("scroll", f);
        },
        offresize : function(f) {
            this.off("resize", f);
        },
        offload : function(f) {
            this.off("load", f);
        },
        offfocus : function(f) {
            this.off("focus", f);
        },
        offerror : function(f) {
            this.off("error", f);
        },
        offblur : function(f) {
            this.off("blur", f);
        },
        offautocompleteerror : function(f) {
            this.off("autocompleteerror", f);
        },
        offautocomplete : function(f) {
            this.off("autocomplete", f);
        },
        offwaiting : function(f) {
            this.off("waiting", f);
        },
        offvolumechange : function(f) {
            this.off("volumechange", f);
        },
        offtoggle : function(f) {
            this.off("toggle", f);
        },
        offtimeupdate : function(f) {
            this.off("timeupdate", f);
        },
        offsuspend : function(f) {
            this.off("suspend", f);
        },
        offsubmit : function(f) {
            this.off("submit", f);
        },
        offstalled : function(f) {
            this.off("stalled", f);
        },
        offshow : function(f) {
            this.off("show", f);
        },
        offselect : function(f) {
            this.off("select", f);
        },
        offseeking : function(f) {
            this.off("seeking", f);
        },
        offseeked : function(f) {
            this.off("seeked", f);
        },
        offreset : function(f) {
            this.off("reset", f);
        },
        offratechange : function(f) {
            this.off("ratechange", f);
        },
        offprogress : function(f) {
            this.off("progress", f);
        },
        offplaying : function(f) {
            this.off("playing", f);
        },
        offplay : function(f) {
            this.off("play", f);
        },
        offpause : function(f) {
            this.off("pause", f);
        },
        offmousewheel : function(f) {
            this.off("mousewheel", f);
        },
        offmouseup : function(f) {
            this.off("mouseup", f);
        },
        offmouseover : function(f) {
            this.off("mouseover", f);
        },
        offmouseout : function(f) {
            this.off("mouseout", f);
        },
        offmousemove : function(f) {
            this.off("mousemove", f);
        },
        offmouseleave : function(f) {
            this.off("mouseleave", f);
        },
        offmouseenter : function(f) {
            this.off("mouseenter", f);
        },
        offmousedown : function(f) {
            this.off("mousedown", f);
        },
        offloadstart : function(f) {
            this.off("loadstart", f);
        },
        offloadedmetadata : function(f) {
            this.off("loadedmetadata", f);
        },
        offloadeddata : function(f) {
            this.off("loadeddata", f);
        },
        offkeyup : function(f) {
            this.off("keyup", f);
        },
        offkeypress : function(f) {
            this.off("keypress", f);
        },
        offkeydown : function(f) {
            this.off("keydown", f);
        },
        offinvalid : function(f) {
            this.off("invalid", f);
        },
        offinput : function(f) {
            this.off("input", f);
        },
        offended : function(f) {
            this.off("ended", f);
        },
        offemptied : function(f) {
            this.off("emptied", f);
        },
        offdurationchange : function(f) {
            this.off("durationchange", f);
        },
        offdrop : function(f) {
            this.off("drop", f);
        },
        offdragstart : function(f) {
            this.off("dragstart", f);
        },
        offdragover : function(f) {
            this.off("dragover", f);
        },
        offdragleave : function(f) {
            this.off("dragleave", f);
        },
        offdragenter : function(f) {
            this.off("dragenter", f);
        },
        offdragend : function(f) {
            this.off("dragend", f);
        },
        offdrag : function(f) {
            this.off("drag", f);
        },
        offdblclick : function(f) {
            this.off("dblclick", f);
        },
        offcuechange : function(f) {
            this.off("cuechange", f);
        },
        offcontextmenu : function(f) {
            this.off("contextmenu", f);
        },
        offclose : function(f) {
            this.off("close", f);
        },
        offclick : function(f) {
            this.off("click", f);
        },
        offchange : function(f) {
            this.off("change", f);
        },
        offcanplaythrough : function(f) {
            this.off("canplaythrough", f);
        },
        offcanplay : function(f) {
            this.off("canplay", f);
        },
        offcancel : function(f) {
            this.off("cancel", f);
        },
        offabort : function(f) {
            this.off("abort", f);
        },
        offwebkitfullscreenerror : function(f) {
            this.off("webkitfullscreenerror", f);
        },
        offwebkitfullscreenchange : function(f) {
            this.off("webkitfullscreenchange", f);
        },
        offwheel : function(f) {
            this.off("wheel", f);
        },
        offselectstart : function(f) {
            this.off("selectstart", f);
        },
        offsearch : function(f) {
            this.off("search", f);
        },
        offpaste : function(f) {
            this.off("paste", f);
        },
        offcut : function(f) {
            this.off("cut", f);
        },
        offcopy : function(f) {
            this.off("copy", f);
        },
        offbeforepaste : function(f) {
            this.off("beforepaste", f);
        },
        offbeforecut : function(f) {
            this.off("beforecut", f);
        },
        offbeforecopy : function(f) {
            this.off("beforecopy", f);
        }
    });

    // Textareaのリサイズoninputでできる
    // IEだとheightを変更するたびにレンダリングしていて、ちょっとちらつく
    var autoResize = function() {
        var s = this.style;
        s.height = 0 + "px";
        var h = this.scrollHeight;
        var tmp;
        h -= (tmp = s.paddingTop) ? pxToNumber(tmp) : 2;
        h -= (tmp = s.paddingBottom) ? pxToNumber(tmp) : 2;
        h -= (tmp = s.borderWidth) ? 2 * pxToNumber(tmp) : 2;
        s.height = h + "px";
    };

    // smr.dom.Textarea
    /**
     * @class smr.dom.Textarea テキストエリアの操作に特化したクラス
     * @extends smr.dom.Element
     */
    var Textarea =
        smr
            .define("smr.dom.Textarea", {
                /**
                 * @memberOf smr.dom.Textarea
                 */
                superClass : Element,
                init : function(elm) {
                    elm = elm || 'textarea';
                    this.superInit(elm);
                    if (!(elm instanceof Object)) {
                        this.styleSetter({
                            resize : "none",
                            overflow : "hidden",
                            padding : "2px",
                            minHeight : "14px",
                            wordWrap : "break-word",
                            borderWidth : "1px"
                        });
                    }
                },

                /**
                 * リサイズ可能か変更
                 * 
                 * @param v
                 *            "y" "v" で縦のみ "x" "h" で横のみ 未指定で縦横 falseでできない
                 * @returns {smr.dom.Textarea}
                 */
                resize : function(v) {
                    this.style.resize =
                        v === undefined ? "" : v === true ? "" : v === false ? "none" : v === "v" ? "vertical" : v === "y" ? "vertical" : v === "h" ? "horizontal" : v === "x" ? "horizontal" : v === "vertical" ? "vertical" : v === "horizontal" ? "horizontal" : v === "" ? "" : v === "none" ? "none" : "";
                    return this;
                },

                autoResize : function(isRemove) {
                    if (isRemove) {
                        this.off("input", autoResize);
                        return;
                    }
                    this.on("input", autoResize);
                    return this;
                }
            });

    // smr.dom.form

    /**
     * @class smr.dom.form formの操作に特化
     * @extends smr.dom.Element
     */
    var Form = smr.define("smr.dom.Form", {
        /**
         * @memberOf smr.dom.Form
         */
        superClass : Element,
        init : function(elm, setting) {
            elm = elm || 'form';
            this.superInit(elm);
            if (setting) {
                this.elementSetter(setting);
            }
        }
    });

    smr.accessors(Form.prototype, "elements", {
        get : function() {
            return this.element.elements;
        }
    });

})(smr, smr.global, smr.Object, ({}).constructor, Number);

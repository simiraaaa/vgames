/*
 * Copyright (c) 2014 simiraaaa
 * Released under the MIT license
 * http://opensource.org/licenses/mit-license.php
 */

/**
 * @namespace smr
 */
var smr = smr || {};

(function(smr, Object, undefined) {

  Object.defineProperty(smr, "global", {
    value: (window || global || this),
    writable: false,
    configurable: false,
    enumerable: true
  });

  "use strict";

  var Array = smr.global.Array;

  var errorOutputConsole = smr.global.onerror = function(msg, url, line, col, error) {
    console.error(error.stack);
  };

  var errorOutputAlert = function(msg, url, line, col, error) {
    alert(error.stack);
  };
  var noname = {
    /**
     * @memberOf noname
     */
    className: " < noname > ",
    count: 0
  };
  /**
   * getDescriptor. 1bit目 enumerable. 2bit目 writable. 3bit目 configurable.
   */
  var getDescriptor = function(value, desc) {
    if (desc === undefined) {
      desc = 1;
    }
    return {
      value: value,
      enumerable: ((desc & 1) === 1),
      writable: ((desc & 2) === 2),
      configurable: ((desc & 4) === 4)
    };
  };

  // var superInit = function() {
  // strictモードでcallerはIE、FireFoxでエラー
  // var caller = this.superInit.caller;// 呼び出し元(init)
  // if (caller) {// なぜかnullになることがある
  // caller.owner// initを持っているクラス
  // .prototype.superClass// (その親クラス)
  // .prototype.init.apply(this, arguments);
  // } else {
  // this.superClass.prototype.init.apply(this, arguments);
  // }
  // };

  var superInit = function() {
    this._classTree[this._initCount++].prototype.init.apply(this, arguments);
  };

  /**
   * @name defineProperty
   * @menber smr
   * @param name
   *            プロパティ名
   * @param value
   *            値
   * @param desc
   *            descriptor 3ビットで指定 未指定で1
   */
  Object.defineProperty(smr, "defineProperty", {
    value: function(name, value, desc) {
      Object.defineProperty(this, name, getDescriptor(value, desc));
      return this;
    },
    writable: false,
    configurable: false,
    enumerable: true
  });

  /**
   * smr以外がcallで使えるようにcallでsmr.definePropertyを呼ぶ. 速度は普通より3倍遅い程度
   */
  smr.defineProperty("defineProperties", function() {
    for (var i = 0, l = arguments.length; i < l;) {
      smr.defineProperty.call(this, arguments[i++], arguments[i++], arguments[i++]);
    }
    return this;
  });

  smr.defineProperty("forin", function() {
    var o = arguments[0];
    var f = arguments[1];
    var len = arguments.length;
    if (len === 2) {
      for (var k in o) {
        f(k, o[k]);
      }
    } else {
      for (var k in o) {
        arguments[0] = k;
        arguments[1] = o[k];
        f.apply(null, arguments);
      }
    }
  });

  smr.defineProperty("definePackage", function(name, prop, desc) {
    if (typeof name === "string") {
      name = smr.using(name);
    }
    var pack = name;
    var args = [];
    desc = desc === undefined ? 1 : desc;
    for (var key in prop) {
      smr.defineProperty.call(pack, key, prop[key], desc);
    }
    return pack;
  });

  smr.defineProperty("definePackages", function(name, prop, desc) {
    var pack = smr.using(name);
    desc = desc === undefined ? 1 : desc;
    for (var key in prop) {
      smr.definePackage(name + "." + key, prop[key], desc);
    }
    return pack;
  });

  // smrに追加するプロパティ
  var regexp = {
    /**
     * @memberOf smr.regexp
     */
    _pathSplit: /[\[,\.\/]|::/,
    _optimize: /[　\s\]]/,

    /**
     * 引数が"sm r .r e g exp[a]::b/c,d"だったら "smr.regexp[a::b/c,d"を返す
     *
     * @param str
     */
    optimization: function(str) {
      return str.split(this._optimize).join("");
    },

    /**
     * 引数が"sm r .r e g exp[a]::b/c,d"だったら "sm r .r e g exp.a].b.c.d"を返す
     *
     * @param str
     */
    pathSplitter: function(str) {
      return str.split(this._pathSplit);
    },

    /**
     * 引数が"sm r .r e g exp[a]::b/c,d"だったら "smr.regexp.a.b.c.d"を返す
     *
     * @param str
     */
    classPath: function(str) {
      return this.pathSplitter(this.optimization(str)).join(".");
    },

    /**
     * 引数が"sm r .r e g exp[a]::b/c,_[ d]" だったら"d"を返す
     *
     * @param str
     */
    className: function(str) {
      var ar = this.pathSplitter(str);
      return this.optimization(ar[ar.length - 1]);
    }
  };
  var smrprop = {
    /**
     * @memberOf smr
     */

    /**
     * エラーが出たときアラートする
     */
    onerrorAlert: function() {
      smr.global.onerror = errorOutputAlert;
    },
    /**
     * エラーが出た時の表示をコンソールにする
     */
    offerrorAlert: function() {
      smr.global.onerror = errorOutputConsole;
    },

    /**
     * accessorをひとつ定義する
     */
    accessor: function(slf, name, desc) {
      Object.defineProperty(slf, name, {
        enumerable: true,
        configurable: false,
        get: desc["get"],
        set: desc["set"]
      });
      return slf;
    },

    /**
     * accessorを複数定義する<br>
     * 第一引数に定義したいクラスのprototype<br>
     * 第二匹数以降は"アクセサ名",{get:func,set:func}を交互に
     */
    accessors: function() {
      var slf = arguments[0];
      for (var i = 1, l = arguments.length; i < l;) {
        smr.accessor(slf, arguments[i++], arguments[i++]);
      }
      return slf;
    },

    defineAccessors: function(slf, prop) {
      for (var key in prop) {
        smr.accessor(slf, key, prop[key]);
      }
      return slf;
    },


    setter: function(slf, name, setter) {
      Object.defineProperty(slf, name, {
        enumerable: true,
        configurable: false,
        set: setter
      });
      return slf;
    },
    defineSetters: function(slf, prop) {
      for (var key in prop) {
        smr.setter(slf, key, prop[key]);
      }
      return slf;
    },

    getter: function(slf, name, getter) {
      Object.defineProperty(slf, name, {
        enumerable: true,
        configurable: false,
        get: getter
      });
      return slf;
    },

    defineGetters: function(slf, prop) {
      for (var key in prop) {
        smr.getter(slf, key, prop[key]);
      }
      return slf;
    },


    /**
     * tmlib.jsを参考に<br>
     * 変数名を文字列で参照<br>
     * もし未定義かbooleanに変換したときfalseになる場合<br>
     * 削除変更不可のオブジェクトを定義して返す。
     */
    using: function(ns) {
      if (smr.classes[ns]) {
        return smr.classes[ns];
      }

      // ]空白を消してから,./::[のどれかでスプリット
      // array[?]にも対応
      var path = regexp.pathSplitter(regexp.optimization(ns));
      var current = smr.global;

      for (var i = 0, len = path.length; i < len; ++i) {
        var dir = path[i];
        current = current[dir] || (smr.defineProperty.call(current, dir, {})[dir]);
      }

      // キャッシュ
      smr.defineProperty.call(smr.classes, ns, current);

      return current;
    },

    /**
     * クラスを定義する<br>
     * 第一引数にクラス名、第二引数にプロパティ<br>
     * クラス名を省略して、第一引数にプロパティを設定することもできる。
     */
    define: function(name, prp) {

      var isNoname = false;

      if (typeof name !== "string") {
        prp = name;
        name = false;
      }

      if (name) {
        name = regexp.classPath(name);
      } else {
        isNoname = true;
        name = "smr." + noname.className + "." + noname.count;
      }
      var _className = regexp.className(name);

      prp.superClass = prp.superClass || smr.Object;
      prp.init = prp.init || function() {};
      prp._initCount = 0;

      var _classTree = {
        0: prp.superClass
      };
      for (var i = 0, temp; temp = _classTree[i].prototype.superClass;) {
        _classTree[++i] = temp;
      }
      prp._classTree = _classTree;

      // superClassは文字列でも指定できる。
      if (typeof prp.superClass === "string") {
        prp.superClass = smr.using(prp.superClass);
      }

      // var _extend = false;
      prp.superInit = superInit;

      // クラスを作る
      var cls_ = function(arg) {
        this.init.apply(this, arg);
      };
      // new で作ったときでも新しく作ったオブジェクトを返した場合はそのオブジェクトが返る
      var _cls = function() {
        return new cls_(arguments);
      };

      // 継承
      _cls.prototype = Object.create(prp.superClass.prototype, (function() {
        var o = {};

        // if (_extend) {
        // var pNames =
        // Object.getOwnPropertyNames(smr.Object.prototype);
        // for ( var i = 0, len = pNames.length; i < len;) {
        // var pn = pNames[i++];
        // o[pn] = {
        // value : smr.Object.prototype[pn],
        // writable : false,
        // enumerable : false,
        // configurable : false
        // };
        // }
        // }

        // prototypeのメソッドは全て列挙されず、削除更新不可
        // メソッドじゃないのは列挙されて削除不可
        for (var k in prp) {
          var isFn = (typeof prp[k] === 'function');
          o[k] = {
            value: prp[k],
            writable: (!isFn),
            enumerable: (!isFn),
            configurable: false
          };
        }

        // フルパスsmr.using(_path)で使える。
        o._path = getDescriptor(name, 0);
        // 最後の部分だけ
        o._className = getDescriptor(_className, 0);

        // constructorがスーパークラスに上書きされてるから.
        o.constructor = getDescriptor(_cls, 2);

        return o;
      })());

      // tmlib.jsでも使えるようにする
      smr.defineProperty.call(_cls, "_class", true, 0);
      smr.defineProperty.call(cls_, "_class", true, 0);
      smr.defineProperty.call(_cls.prototype.init, "owner", _cls, 0);

      cls_.prototype = _cls.prototype;

      if (isNoname) {
        smr[noname.className][noname.count++] = _cls;
      } else {
        var ns = name.substring(0, name.lastIndexOf("."));
        ns = ns ? smr.using(ns) : smr.global;
        smr.defineProperty.call(ns, _className, _cls);
      }

      // キャッシュ
      smr.defineProperty.call(smr.classes, name, _cls);

      return _cls;
    }

  };

  smr.classes = {};
  smr[noname.className] = [];

  smr.definePackage(smr, smrprop);
  smr.definePackage("smr.regexp", regexp);

  /**
   *
   * オブジェクトのラッパークラス いろいろ増えてく予定
   *
   * @class smr.Object
   * @extends Object
   *
   */
  smr.define("smr.Object", {
    /**
     * @memberOf smr.Object
     */
    superClass: Object,
    init: function(o) {
      if (o) {
        this.add(o);
      }
    },
    /**
     * 引数のオブジェクトのプロパティで同名のプロパティを上書きする
     *
     * @name add
     */
    add: function(o) {
      for (var k in o) {
        this[k] = o[k];
      }
      return this;
    },
    /**
     * プロパティがない場合に追加
     *
     * @param o
     * @returns {smr.Object}
     */
    addIfNotHave: function(o) {
      for (var k in o) {
        if (this.has(k)) {
          continue;
        }
        this[k] = o[k];
      }
      return this;
    },

    /**
     * メソッドを配列に渡した順番に実行する. 普通に実行するより5倍ぐらい遅い. 引数が一つでも配列で渡す.
     *
     * @param ar
     *            ["methodName",[引数,引数,引数],"methodName2",[引数],
     *            "methodName3", [(引数がない場合空の配列)]]
     */
    invoke: function(ar) {
      if (typeof ar === 'string') {
        ar = arguments;
      }
      for (var i = 0, l = ar.length; i < l;) {
        this[ar[i++]].apply(this, ar[i++]);
      }
    },
    /**
     * forIn tmlibの$forIn オブジェクトをループで回す
     */
    forIn: function(fn, self) {
      self = self || this;

      Object.keys(this).forEach(function(key, index) {
        var value = this[key];

        fn.call(self, key, value, index);
      }, this);

      return this;
    },
    /**
     * keys 列挙可能なキーを配列で返す.
     */
    keys: function() {
      return Object.keys(this);
    },

    /**
     * has そのプロパティを持っているか.(そのキーで参照できるかどうかではない)
     */
    has: function(k) {
      return this.hasOwnProperty(k);
    },

    /**
     * 自分のコンストラクターとinstanceが一致するか
     *
     * @param o
     * @returns {Boolean}
     */
    isInstance: function(o) {
      return (o instanceof this.constructor);
    }

  });

})(smr, ({}).constructor);


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

      element: null,

      queryCache: null,

      queryAllCache: null,

      displayCache: null,

      init: function(elm) {
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
      setPosition: function(x, y) {
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
      styleSetter: function(o) {
        this.add.call(this.style, o);
        return this;
      },

      /**
       * エレメントにセットする
       *
       * @param o
       * @returns {smr.dom.Element}
       */
      elementSetter: function(o) {
        this.add.call(this.element, o);
        return this;
      },

      /**
       * 表示
       *
       * @returns
       */
      show: function() {
        this.visible = true;
        return this;
      },
      /**
       * 非表示
       *
       * @returns
       */
      hide: function() {
        this.visible = false;
        return this;
      },

      /**
       * 表示非表示の切り替え
       *
       * @returns
       */
      toggle: function() {
        this.visible = !this.visible;
        return this;
      },

      noneDisp: function() {
        var disp = this.style.display;
        if (disp === "none") {
          return this;
        }
        this.displayCache = disp;
        this.style.display = "none";

        return this;
      },

      showDisp: function() {
        this.style.display = this.displayCache || "";
        return this;
      },

      isHitElement: function(elm) {
        return hitTest(this.element.getBoundingClientRect(), (elm.element || elm)
          .getBoundingClientRect());
      },

      /**
       * 子の最後に追加
       */
      append: function(child) {
        this.element.appendChild((child.element || child));
        return this;
      },

      /**
       * 子の最初に追加
       */
      prepend: function(child) {
        this.element.insertBefore((child.element || child), this.element.firstChild);
        return this;
      },

      /**
       * 自分の次に追加
       */
      after: function(elm) {
        this.element.parentNode
          .insertBefore((elm.element || elm), this.element.nextSibling);
        return this;
      },

      /**
       * 自分の前に追加
       */
      before: function(elm) {
        this.element.parentNode.insertBefore((elm.element || elm), this.element);
        return this;
      },

      /**
       * 引数の要素にappend
       *
       * @param parent
       * @returns
       */
      appendTo: function(parent) {
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
      prependTo: function(parent) {
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
      clone: function(isOnly) {
        return this.constructor(this.element.cloneNode(!isOnly));
      },

      /**
       * 親に捨てられる
       */
      remove: function() {
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
      removeChild: function(child) {
        if (null === child) {
          return this;
        }
        if (child instanceof NodeList || child instanceof Array) {
          for (var elm = this.element, i = child.length - 1; i >= 0; --i) {
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
      removeChildAll: function() {
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
      create: function(tag, method) {
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
      fixed: function(x, y, width, height) {
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
      absolute: function(x, y, width, height) {
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
      on: function(ename, efunc, cap) {
        this.element.addEventListener(ename, efunc, !! cap);
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
      off: function(ename, efunc, cap) {
        this.element.removeEventListener(ename, efunc, !! cap);
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
      query: function(q, i, o) {
        o = o || {
          getCache: false,
          setCache: true,
          isWrap: false
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
      queryAll: function(q, o) {
        o = o || {
          getCache: false,
          setCache: true,
          isWrap: false
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
      wrapList: function(list) {
        var list2 = [];
        for (var i = list.length - 1; i >= 0; --i) {
          list2[i] = Element(list[i]);
        }
        return list2;
      },

    });

  smr.defineGetters(Element.prototype, {
    classList: function() {
      return this.element.classList;
    },
    prev: function() {
      return (this.element.previousSibling) ? Element(this.element.previousSibling) : null;
    },
    next: function() {
      return (this.element.nextSibling) ? Element(this.element.nextSibling) : null;
    },
    style: function() {
      return this.element.style;
    },
    children: function() {
      return this.element.children;
    },
    parent: function() {
      return (this.element.parentNode) ? Element(this.element.parentNode) : null;
    }
  });

  smr.defineAccessors(Element.prototype, {
    visible: {
      set: function(v) {
        this.style.visibility = (v === true) ? "visible" : "hidden";
      },
      get: function() {
        return this.style.visibility !== "hidden";
      }
    },
    html: {
      set: function(v) {
        this.element.innerHTML = v;
      },
      get: function() {
        return this.element.innerHTML;
      }
    },
    value: {
      set: function(v) {
        this.element.value = v;
      },
      get: function() {
        return this.element.value;
      }
    },
    x: {
      set: function(x) {
        this.style.left = x + "px";
      },
      get: function() {
        return pxToNumber(this.style.left);
      }
    },
    y: {
      set: function(y) {
        this.style.top = y + "px";
      },
      get: function() {
        return pxToNumber(this.style.top);
      }
    },
    width: {
      set: function(w) {
        this.style.width = w + "px";
      },
      get: function() {
        return pxToNumber(this.style.width);
      }
    },
    height: {
      set: function(h) {
        this.style.height = h + "px";
      },
      get: function() {
        return pxToNumber(this.style.height);
      }
    },
    color: {
      set: function(c) {
        this.style.color = c;
      },
      get: function() {
        return this.style.color;
      }
    },
    backgroundColor: {
      set: function(c) {
        this.style.backgroundColor = c;
      },
      get: function() {
        return this.style.backgroundColor;
      }
    }
  });

  /**
   * onEvent
   */
  smr.defineSetters(Element.prototype, {
    onunload: function(f) {
      this.on("unload", f);
    },
    onstorage: function(f) {
      this.on("storage", f);
    },
    onpopstate: function(f) {
      this.on("popstate", f);
    },
    onpageshow: function(f) {
      this.on("pageshow", f);
    },
    onpagehide: function(f) {
      this.on("pagehide", f);
    },
    ononline: function(f) {
      this.on("online", f);
    },
    onoffline: function(f) {
      this.on("offline", f);
    },
    onmessage: function(f) {
      this.on("message", f);
    },
    onlanguagechange: function(f) {
      this.on("languagechange", f);
    },
    onhashchange: function(f) {
      this.on("hashchange", f);
    },
    onbeforeunload: function(f) {
      this.on("beforeunload", f);
    },
    onscroll: function(f) {
      this.on("scroll", f);
    },
    onresize: function(f) {
      this.on("resize", f);
    },
    onload: function(f) {
      this.on("load", f);
    },
    onfocus: function(f) {
      this.on("focus", f);
    },
    onerror: function(f) {
      this.on("error", f);
    },
    onblur: function(f) {
      this.on("blur", f);
    },
    onautocompleteerror: function(f) {
      this.on("autocompleteerror", f);
    },
    onautocomplete: function(f) {
      this.on("autocomplete", f);
    },
    onwaiting: function(f) {
      this.on("waiting", f);
    },
    onvolumechange: function(f) {
      this.on("volumechange", f);
    },
    ontoggle: function(f) {
      this.on("toggle", f);
    },
    ontimeupdate: function(f) {
      this.on("timeupdate", f);
    },
    onsuspend: function(f) {
      this.on("suspend", f);
    },
    onsubmit: function(f) {
      this.on("submit", f);
    },
    onstalled: function(f) {
      this.on("stalled", f);
    },
    onshow: function(f) {
      this.on("show", f);
    },
    onselect: function(f) {
      this.on("select", f);
    },
    onseeking: function(f) {
      this.on("seeking", f);
    },
    onseeked: function(f) {
      this.on("seeked", f);
    },
    onreset: function(f) {
      this.on("reset", f);
    },
    onratechange: function(f) {
      this.on("ratechange", f);
    },
    onprogress: function(f) {
      this.on("progress", f);
    },
    onplaying: function(f) {
      this.on("playing", f);
    },
    onplay: function(f) {
      this.on("play", f);
    },
    onpause: function(f) {
      this.on("pause", f);
    },
    onmousewheel: function(f) {
      this.on("mousewheel", f);
    },
    onmouseup: function(f) {
      this.on("mouseup", f);
    },
    onmouseover: function(f) {
      this.on("mouseover", f);
    },
    onmouseout: function(f) {
      this.on("mouseout", f);
    },
    onmousemove: function(f) {
      this.on("mousemove", f);
    },
    onmouseleave: function(f) {
      this.on("mouseleave", f);
    },
    onmouseenter: function(f) {
      this.on("mouseenter", f);
    },
    onmousedown: function(f) {
      this.on("mousedown", f);
    },
    onloadstart: function(f) {
      this.on("loadstart", f);
    },
    onloadedmetadata: function(f) {
      this.on("loadedmetadata", f);
    },
    onloadeddata: function(f) {
      this.on("loadeddata", f);
    },
    onkeyup: function(f) {
      this.on("keyup", f);
    },
    onkeypress: function(f) {
      this.on("keypress", f);
    },
    onkeydown: function(f) {
      this.on("keydown", f);
    },
    oninvalid: function(f) {
      this.on("invalid", f);
    },
    oninput: function(f) {
      this.on("input", f);
    },
    onended: function(f) {
      this.on("ended", f);
    },
    onemptied: function(f) {
      this.on("emptied", f);
    },
    ondurationchange: function(f) {
      this.on("durationchange", f);
    },
    ondrop: function(f) {
      this.on("drop", f);
    },
    ondragstart: function(f) {
      this.on("dragstart", f);
    },
    ondragover: function(f) {
      this.on("dragover", f);
    },
    ondragleave: function(f) {
      this.on("dragleave", f);
    },
    ondragenter: function(f) {
      this.on("dragenter", f);
    },
    ondragend: function(f) {
      this.on("dragend", f);
    },
    ondrag: function(f) {
      this.on("drag", f);
    },
    ondblclick: function(f) {
      this.on("dblclick", f);
    },
    oncuechange: function(f) {
      this.on("cuechange", f);
    },
    oncontextmenu: function(f) {
      this.on("contextmenu", f);
    },
    onclose: function(f) {
      this.on("close", f);
    },
    onclick: function(f) {
      this.on("click", f);
    },
    onchange: function(f) {
      this.on("change", f);
    },
    oncanplaythrough: function(f) {
      this.on("canplaythrough", f);
    },
    oncanplay: function(f) {
      this.on("canplay", f);
    },
    oncancel: function(f) {
      this.on("cancel", f);
    },
    onabort: function(f) {
      this.on("abort", f);
    },
    onwebkitfullscreenerror: function(f) {
      this.on("webkitfullscreenerror", f);
    },
    onwebkitfullscreenchange: function(f) {
      this.on("webkitfullscreenchange", f);
    },
    onwheel: function(f) {
      this.on("wheel", f);
    },
    onselectstart: function(f) {
      this.on("selectstart", f);
    },
    onsearch: function(f) {
      this.on("search", f);
    },
    onpaste: function(f) {
      this.on("paste", f);
    },
    oncut: function(f) {
      this.on("cut", f);
    },
    oncopy: function(f) {
      this.on("copy", f);
    },
    onbeforepaste: function(f) {
      this.on("beforepaste", f);
    },
    onbeforecut: function(f) {
      this.on("beforecut", f);
    },
    onbeforecopy: function(f) {
      this.on("beforecopy", f);
    },

    /**
     * offEvent
     */
    offunload: function(f) {
      this.off("unload", f);
    },
    offstorage: function(f) {
      this.off("storage", f);
    },
    offpopstate: function(f) {
      this.off("popstate", f);
    },
    offpageshow: function(f) {
      this.off("pageshow", f);
    },
    offpagehide: function(f) {
      this.off("pagehide", f);
    },
    offonline: function(f) {
      this.off("online", f);
    },
    offoffline: function(f) {
      this.off("offline", f);
    },
    offmessage: function(f) {
      this.off("message", f);
    },
    offlanguagechange: function(f) {
      this.off("languagechange", f);
    },
    offhashchange: function(f) {
      this.off("hashchange", f);
    },
    offbeforeunload: function(f) {
      this.off("beforeunload", f);
    },
    offscroll: function(f) {
      this.off("scroll", f);
    },
    offresize: function(f) {
      this.off("resize", f);
    },
    offload: function(f) {
      this.off("load", f);
    },
    offfocus: function(f) {
      this.off("focus", f);
    },
    offerror: function(f) {
      this.off("error", f);
    },
    offblur: function(f) {
      this.off("blur", f);
    },
    offautocompleteerror: function(f) {
      this.off("autocompleteerror", f);
    },
    offautocomplete: function(f) {
      this.off("autocomplete", f);
    },
    offwaiting: function(f) {
      this.off("waiting", f);
    },
    offvolumechange: function(f) {
      this.off("volumechange", f);
    },
    offtoggle: function(f) {
      this.off("toggle", f);
    },
    offtimeupdate: function(f) {
      this.off("timeupdate", f);
    },
    offsuspend: function(f) {
      this.off("suspend", f);
    },
    offsubmit: function(f) {
      this.off("submit", f);
    },
    offstalled: function(f) {
      this.off("stalled", f);
    },
    offshow: function(f) {
      this.off("show", f);
    },
    offselect: function(f) {
      this.off("select", f);
    },
    offseeking: function(f) {
      this.off("seeking", f);
    },
    offseeked: function(f) {
      this.off("seeked", f);
    },
    offreset: function(f) {
      this.off("reset", f);
    },
    offratechange: function(f) {
      this.off("ratechange", f);
    },
    offprogress: function(f) {
      this.off("progress", f);
    },
    offplaying: function(f) {
      this.off("playing", f);
    },
    offplay: function(f) {
      this.off("play", f);
    },
    offpause: function(f) {
      this.off("pause", f);
    },
    offmousewheel: function(f) {
      this.off("mousewheel", f);
    },
    offmouseup: function(f) {
      this.off("mouseup", f);
    },
    offmouseover: function(f) {
      this.off("mouseover", f);
    },
    offmouseout: function(f) {
      this.off("mouseout", f);
    },
    offmousemove: function(f) {
      this.off("mousemove", f);
    },
    offmouseleave: function(f) {
      this.off("mouseleave", f);
    },
    offmouseenter: function(f) {
      this.off("mouseenter", f);
    },
    offmousedown: function(f) {
      this.off("mousedown", f);
    },
    offloadstart: function(f) {
      this.off("loadstart", f);
    },
    offloadedmetadata: function(f) {
      this.off("loadedmetadata", f);
    },
    offloadeddata: function(f) {
      this.off("loadeddata", f);
    },
    offkeyup: function(f) {
      this.off("keyup", f);
    },
    offkeypress: function(f) {
      this.off("keypress", f);
    },
    offkeydown: function(f) {
      this.off("keydown", f);
    },
    offinvalid: function(f) {
      this.off("invalid", f);
    },
    offinput: function(f) {
      this.off("input", f);
    },
    offended: function(f) {
      this.off("ended", f);
    },
    offemptied: function(f) {
      this.off("emptied", f);
    },
    offdurationchange: function(f) {
      this.off("durationchange", f);
    },
    offdrop: function(f) {
      this.off("drop", f);
    },
    offdragstart: function(f) {
      this.off("dragstart", f);
    },
    offdragover: function(f) {
      this.off("dragover", f);
    },
    offdragleave: function(f) {
      this.off("dragleave", f);
    },
    offdragenter: function(f) {
      this.off("dragenter", f);
    },
    offdragend: function(f) {
      this.off("dragend", f);
    },
    offdrag: function(f) {
      this.off("drag", f);
    },
    offdblclick: function(f) {
      this.off("dblclick", f);
    },
    offcuechange: function(f) {
      this.off("cuechange", f);
    },
    offcontextmenu: function(f) {
      this.off("contextmenu", f);
    },
    offclose: function(f) {
      this.off("close", f);
    },
    offclick: function(f) {
      this.off("click", f);
    },
    offchange: function(f) {
      this.off("change", f);
    },
    offcanplaythrough: function(f) {
      this.off("canplaythrough", f);
    },
    offcanplay: function(f) {
      this.off("canplay", f);
    },
    offcancel: function(f) {
      this.off("cancel", f);
    },
    offabort: function(f) {
      this.off("abort", f);
    },
    offwebkitfullscreenerror: function(f) {
      this.off("webkitfullscreenerror", f);
    },
    offwebkitfullscreenchange: function(f) {
      this.off("webkitfullscreenchange", f);
    },
    offwheel: function(f) {
      this.off("wheel", f);
    },
    offselectstart: function(f) {
      this.off("selectstart", f);
    },
    offsearch: function(f) {
      this.off("search", f);
    },
    offpaste: function(f) {
      this.off("paste", f);
    },
    offcut: function(f) {
      this.off("cut", f);
    },
    offcopy: function(f) {
      this.off("copy", f);
    },
    offbeforepaste: function(f) {
      this.off("beforepaste", f);
    },
    offbeforecut: function(f) {
      this.off("beforecut", f);
    },
    offbeforecopy: function(f) {
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
      superClass: Element,
      init: function(elm) {
        elm = elm || 'textarea';
        this.superInit(elm);
        if (!(elm instanceof Object)) {
          this.styleSetter({
            resize: "none",
            overflow: "hidden",
            padding: "2px",
            minHeight: "14px",
            wordWrap: "break-word",
            borderWidth: "1px"
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
      resize: function(v) {
        this.style.resize =
          v === undefined ? "" :
          v === true ? "" : v === false ? "none" : v === "v" ? "vertical" : v === "y" ? "vertical" : v === "h" ? "horizontal" : v === "x" ? "horizontal" : v === "vertical" ? "vertical" : v === "horizontal" ? "horizontal" : v === "" ? "" : v === "none" ? "none" : "";
        return this;
      },

      autoResize: function(isRemove) {
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
    superClass: Element,
    init: function(elm, setting) {
      elm = elm || 'form';
      this.superInit(elm);
      if (setting) {
        this.elementSetter(setting);
      }
    }
  });

  smr.accessors(Form.prototype, "elements", {
    get: function() {
      return this.element.elements;
    }
  });

})(smr, smr.global, smr.Object, ({}).constructor, Number);


(function(smr, undefined) {
  "use strict";

  var EQUAL = "=",
    AMP = "&";
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
    stringify: function(data, eq, amp) {
      eq = eq || EQUAL;
      amp = amp || AMP;
      var query = [];
      for (var key in data) {
        query[query.length] = encodeURIComponent(key) + eq + encodeURIComponent(data[key]);
      }
      return query.join(amp);
    },
    /**
     * "="以外をencodeURIComponentする
     */
    encodeURINonEqual: function(s) {
      var ar = s.split(EQUAL);
      for (var i = 0, len = ar.length; i < len; ++i) {
        ar[i] = encodeURIComponent(ar[i]);
      }
      return ar.join(EQUAL);
    },

    parse: function() {
      alert("smr.util.parse:あとで作る");
    }
  };
  smr.definePackages("smr.util", util);
})(smr);


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
    type: "GET",
    async: true,
    data: null,
    contentType: "application/x-www-form-urlencoded",
    charset: null,
    dataType: "text",
    responseType: null, // blob or arraybuffer
    username: null,
    password: null,
    success: function(data) {
      alert("success!!\n" + data);
    },
    error: function(data) {
      alert("error!!");
    },
    beforeSend: null,
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
    undefined: NOT_PARSE,

    /**
     * なにもしない
     */
    "": NOT_PARSE,

    /**
     * なにもしない
     */
    text: NOT_PARSE,
    /**
     * XMLにparseして返す。
     *
     * @param data
     * @returns
     */
    xml: function(data) {
      return new DOMParser().parseFromString(data, "text/xml");
    },
    /**
     * divを生成しその中のinnerHTMLに追加して返す。
     *
     * @param data
     * @returns {smr.dom.Element}
     */
    dom: function(data) {
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
    json: function(data) {
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
    script: function(data) {
      eval(data);
      return data;
    },

    /**
     * byte配列を返す
     *
     * @param data
     * @returns {Array}
     */
    bin: function(data) {
      var bytearray = [];
      for (var i = 0, len = data.length; i < len; ++i) {
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
    load: function(params) {
      for (var key in DEFAULT_PARAMS) {
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
        } else if (!(data instanceof FormData)) {
          qs = smr.util.queryString.stringify(data);
        } else {
          qs = data;
        }

        if (type === "GET") {
          params.url += "?" + qs;
        } else if (type === "POST") {
          params.data = qs;
        }
      }
      xhr.open(type, params.url, params.async, params.username, params.password);

      if (type === "POST" && !(params.data instanceof FormData)) {
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
    loadJSONP: function(url, callback, callbackKey) {
      callbackKey = callbackKey || CALLBACK;
      callbackFunctions[callbackCount] = callback;
      var name = callbackName + "[" + callbackCount + "]";
      ++callbackCount;
      url += (url.indexOf("?") === -1 ? "?" : "&") + callbackKey + "=" + name;
      if (!head) {
        head = global.document.head;
      }
      var script = smr.dom.Element("script").elementSetter({
        type: "text/javascript",
        charset: "UTF-8",
        src: url
      });
      script.element.setAttribute("defer", true);
      script.appendTo(head);
    }

  };

  smr.definePackage("smr.ajax", ajax);

})(smr);

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
    RANK: "ranking.json",

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

    initRank: function(f, gid) {
      this.sendScore(f, "-1", gid);

    },
    sendScore: function(f, score, gid) {

      gid = (gid === undefined) ? vg.game.id : gid;
      score = score === undefined ? "-1" : score;

      f = f || function() {};
      vg.ajax(this.RANK, {
          key: 0,
          score: score,
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

    getRanking: function(f, gid) {

      gid = (gid === undefined) ? vg.game.id : gid;

      f = f || function() {};
      vg.ajax(this.RANK, {
          key: 1,
          gameid: gid
        },
        function(data) {
          if (data.status === "success") {
            data.top = data.top || [];
            data.current = data.current || null;
            data.current = data.current === "-1" ? null : data.current;
            f(data.top, data.current);
          } else {
            alert(data.text);
          }
        });


    },

    setumei: function(f, gid) {
      gid = (gid === undefined) ? vg.game.id : gid;

      f = f || function() {};
      vg.ajax(this.GET, {
          key: 3,
          gameid: gid
        },
        function(data) {
          if (data.status === "success") {
            f(data.text);
          } else {
            alert(data.text);
          }
        });
    }


  };

  vg.path="/vgames";
  vg.setGameId = function() {
    vg.game = vg.game || {};
    vg.game.id = location.pathname.match(/\/[0-9]+\//)[0].slice(1, -1);
  };
})(vg, smr);

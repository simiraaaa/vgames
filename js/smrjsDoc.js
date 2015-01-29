	/**
	 * eclipseのJavaScriptプロジェクトのエディタで、
	 * Ctrl + Spaceで出てくるようにする。
	 * クラスのインスタンスを生成するときにnewをちゃんとつけると、
	 * メソッドが表示される。継承したメソッドまで出てくるかは、調べてない。
	 * インスタンスのメソッドの保管がうまくいかないときは
	 * Eclipseのウィンドウタブから設定を開き、JavaScript > エディタ > コンテンツアシストの
	 * 入力される関数の引数を予測　のチェックを外すとできた。
	 */
/****/
var smr ={
		/**
		 *
		 * globalオブジェクト
		 * 通常はwindow
		 * @memberOf smr
		 */
		global : {},

		/**
		 * アクセサを定義する
		 * @param slf 定義したいオブジェクト
		 * @param {String} name アクセサ名
		 * @param desc {get:function(){return value;},set:function(v){}}
		 * @returns
		 */
		accessor : function(slf,name,desc) {
			return slf;
		},

		/**
		 * アクセサを複数定義
		 * 第一引数に定義したいオブジェクト
		 * 2 アクセサ名
		 * 3 {getter,setter}
		 * @returns
		 */
		accessors : function() {

			return slf;
		},

		/**
		 * defineProperty
		 * @param {String} name
		 * @param {any} value
		 * @param {Number} desc
		 */
		defineProperty : function(name, value, desc){return this;},
		defineProperties : function(arguments){return this;},
		regexp : {
			/**
			 * memberOf smr.regexp
			 */
			_pathSplit : / /,
			_optimize :/ / ,
			/**
			 * 引数がsm r .r  e g  exp[a]::b/c,dだったら
			 * smr.regexp[a::b/c,dを返す
			 * @param str
			 */
			optimization : function(str){},
			/**
			 * 引数がsm r .r  e g  exp[a]::b/c,dだったら
			 * sm r .r  e g  exp.a].b.c.dを返す
			 * @param str
			 */
			pathSplitter : function(str){} ,
			/**
			 * 引数がsm r .r  e g  exp[a]::b/c,dだったら
			 * smr.regexp.a.b.c.dを返す
			 * @param str
			 */
			classPath :  function(str){},

			/**
			 * 引数がsm r .r  e g  exp[a]::b/c,_[ d]
			 * だったらdを返す
			 * @param str
			 */
			className :  function(str){},
		},
		classes : {},
		" < noname > " : [],
		/**
		 * using
		 * 指定したパスの変数を返す
		 * ない場合は空のオブジェクトが返ってくる
		 * @param ns
		 */
		using : function(ns){},
		/**
		 * define
		 * クラスを作って返す。
		 * グローバルから見てnameの位置に定義する。
		 * @param name
		 * @param object
		 */
		define : function(name, object){}

};

var Textarea = {
    	/**
    	 * @memberOf Textarea
    	 */
    	superClass : Element,
    	init : function(elm) {
			elm = elm || 'textarea' ;
			this.superInit(elm);
			this.style.resize="none";
			this.style.overflow="hidden";
		},

		/**
		 * リサイズ可能か変更
		 * @param v "y" "v" で縦のみ "x" "h" で横のみ 未指定で縦横 falseでできない
		 * @returns {smr.dom.Textarea}
		 */
		resize : function(v) {
			this.style.resize=
				v === undefined ? "" :
				v === true ? "" :
				v === false ? "none" :
				v === "v" ? "vertical" :
				v === "y" ? "vertical" :
				v === "h" ? "horizontal" :
				v === "x" ? "horizontal" :
				v === "vertical" ? "vertical" :
				v === "horizontal" ? "horizontal" :
				v === "" ? "" :
				v === "none" ? "none" : "";
			return this;
		},

		autoResize : function(isRemove) {
			if(isRemove){this.off("input", autoResize);return; }
			this.on("input", autoResize);
			return this;
		}
    };

var Element={
        /**
         * @memberOf Element
         */

        init:function(elm){
            elm = elm || document;
            this.element = (typeof elm === 'string') ? document.createElement(elm) : elm;
        },

        /**
         * スタイルセットする
         * @param o
         * @returns
         */
        styleSetter : function(o) {
            this.add.call(this.style, o);
            return this;this.constructor
        },

        /**
         * エレメントにセットする
         * @param o
         * @returns {smr.dom.Element}
         */
        elementSetter : function(o) {
			this.add.call(this.element, o);
			return this;
		},

        /**
         * 表示
         * @returns
         */
        show : function(){
            this.visible = true;
            return this;
        },
        /**
         * 非表示
         * @returns
         */
        hide : function(){
            this.visible = false;
            return this;
        },

        /**
         * 表示非表示の切り替え
         * @returns
         */
        toggle : function() {
            this.visible = !this.visible;
            return this;
        },
        /**
         * 子の最後に追加
         */
        append : function(child){
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
        after : function(elm){
            this.element.parentNode.insertBefore((elm.element || elm), this.element.nextSibling);
            return this;
        },

        /**
         * 自分の前に追加
         */
        before : function(elm){
            this.element.parentNode.insertBefore((elm.element || elm), this.element);
            return this;
        },

        /**
         * 引数の要素にappend
         * @param parent
         * @returns
         */
        appendTo : function(parent) {
            if(parent){
                parent = parent.element || parent;
            }else{
                parent = document.body;
            }
            parent.appendChild(this.element);
            return this;
        },

        /**
         * 引数の要素にprepend
         * @param parent
         * @returns
         */
        prependTo : function(parent) {
            if(parent){
                if(parent.prepend)parent.prepend(this);
            }else{
                parent = document.body;
            }
            parent.insertBefore(this.element, parent.firstChild);
            return this;
        },

        /**
         * コピー
         * @param {Boolean} isOnly 子もコピーするか trueでしない
         * @returns
         */
        clone : function(isOnly) {
            return new this.constructor(this.element.cloneNode(!isOnly));
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
         * @param child
         * @returns
         */
        removeChild : function(child){
            this.element.removeChild((child.element || child));
            return this;
        },

        /**
         * 子を捨てる全て
         * @param child
         * @returns
         */
        removeChildAll: function(){
            return this;
        },

        /**
         * 作って自分に追加
         */
        create : function(tag, method) {
            var elm = new this.constructor(tag);
            this[(method || "append")](elm);
            return elm;
        },

        /**
         * 固定化
         * @param x
         * @param y
         * @param width
         * @param height
         * @returns
         */
        fixed : function(x, y, width, height) {
            this.style.position="fixed";
            if (x !== undefined) this.x=x;
            if (y !== undefined) this.y=y;
            if (width !== undefined) this.width=width;
            if (height !== undefined) this.height=height;
            return this;
        },

        /**
         * 絶対位置化
         * @param x
         * @param y
         * @param width
         * @param height
         * @returns
         */
        absolute : function(x, y, width, height) {
            this.style.position="absolute";
            if (x !== undefined) this.x=x;
            if (y !== undefined) this.y=y;
            if (width !== undefined) this.width=width;
            if (height !== undefined) this.height=height;
            return this;
        },

        /**
         * addEventListenerと同じ
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
         * @param ename
         * @param efunc
         * @param cap
         * @returns {smr.dom.Element}
         */
        off : function(ename, efunc, cap) {
			this.element.removeEventListener(ename, efunc, !!cap);
			return this;
		}
    }

package myclass.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.Escape;




/**
 *
 * @author yuki
 *
 */
public class MyCookie{


	private Cookie cookie=null;
	private Map<String,String> cookies=null;
	private HttpServletRequest request=null;
	private HttpServletResponse response=null;
	private final int MAXAGE =60*60*24*365;
	private Escape escape = new Escape();

	private String Enc(String s) {
		return escape.URLEncode(s);
	}
	private String Dec(String s) {
		return escape.URLDecode(s);
	}

	/**
	 * クッキーを作る。
	 * @param name クッキーの名前を英数字で
	 * @param value クッキーの値をStringで
	 */
	public MyCookie(String name, String value) {
		cookie=new Cookie(Enc(name), Enc(value));
	}

	/**
	 * クッキーを作らない
	 * @param req
	 * @param res
	 */
	public MyCookie(HttpServletRequest req, HttpServletResponse res) {
		setHttpServlets(req, res);
	}
	/**
	 * クッキーを作らない
	 * @param req
	 */
	public MyCookie(HttpServletRequest req) {
		setRequest(req);
	}

	/**
	 * クッキーを作らない
	 * @param res
	 */
	public MyCookie(HttpServletResponse res) {
		setResponse(res);
	}

	/**
	 * クッキーを作る
	 * @param name クッキーの名前を英数字で
	 * @param value クッキーの値をStringで
	 * @param req requestを渡す
	 * @param res responseを渡す
	 */
	public MyCookie(String name,String value,HttpServletRequest req, HttpServletResponse res) {
		setHttpServlets(req, res);
		cookie=new Cookie(Enc(name), Enc(value));
	}

	public MyCookie setHttpServlets(HttpServletRequest req,HttpServletResponse res) {
		setRequest(req).setResponse(res);
		return this;
	}

	public MyCookie setResponse(HttpServletResponse res) {
		response=res;
		return this;
	}

	public MyCookie setRequest(HttpServletRequest req) {
		request=req;
		return this;
	}

	/**
	 * クッキーをクライアントに保存
	 * @param res 明示的にこのクラスにセットしていない場合渡す
	 * @return this
	 */
	public MyCookie addCookie(HttpServletResponse res){
		setResponse(res).addCookie();
		return this;
	}

	/**
	 * クッキーをクライアントに保存
	 * @return this
	 */
	public MyCookie addCookie() {
		addCookie(MAXAGE);
		return this;
	}

	/**
	 * クッキーをクライアントに保存
	 * @param sec 秒数を設定 0で破棄
	 * @return
	 */
	public MyCookie addCookie(int sec) {
		cookie.setPath("/");
		cookie.setMaxAge(sec);
		//cookie.setSecure(true);
		response.addCookie(cookie);
		return this;
	}

	/**
	 * クッキーを一つ作る。作ったクッキーは削除される。
	 * @param name クッキーの名前
	 * @param value クッキーの値
	 * @return this
	 */
	public MyCookie setCookie(String name,String value) {
		cookie=new Cookie(Enc(name), Enc(value));
		return this;
	}

	/**
	 * cookiesにセットする元からある場合は同じキーだけ上書きする
	 * @param name_value name,valueの順番でセットする 空だったり、nameだけだったりするとエラー
	 * @return
	 */
	public MyCookie setCookies(String... name_value) {
		if(cookies==null)cookies =new HashMap<String, String>();
		for(int i =0,len=name_value.length;i<len;){
			cookies.put(name_value[i++], name_value[i++]);
		}
		return this;
	}

	private MyCookie addCookie(Cookie c){
		response.addCookie(c);
		return this;
	}
	/**
	 * cookiesからブラウザにクッキーを追加する。
	 * @param maxage
	 * @return
	 */
	public MyCookie addCookies(int maxage) {
		for(String k : cookies.keySet()){
			Cookie c=new Cookie(Enc(k), Enc(cookies.get(k)));
			c.setPath("/");
			//c.setSecure(true);
			c.setMaxAge(maxage);
			addCookie(c);
		}
		return this;
	}

	/**
	 * cookiesからブラウザにクッキーを追加する。
	 * @return
	 */
	public MyCookie addCookies() {
		addCookies(MAXAGE);
		return this;
	}

	public Map<String,String> getCookies(HttpServletRequest req) {
		return setRequest(req).getCookies();
	}
	public Map<String,String> getCookies() {
		Cookie[] cs=this.request.getCookies();
		if(cs==null)return null;
		cookies =new HashMap<String, String>();
		for(Cookie c : cs){
			cookies.put(Dec(c.getName()), Dec(c.getValue()));
		}
		return cookies;
	}

}

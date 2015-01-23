package myclass;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


public class Escape {
	private String string=null;
	//コンストラクタ
	public Escape() {}


	/**
	 * UTF-8でURLエンコードをする
	 * @param s エスケープ対象の文字列
	 * @return
	 */
	public String URLEncode(String s) {
		try {
			return URLEncoder.encode(s,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			System.out.println("エンコードに失敗しました");
			return s;
		}
	}

	/**
	 * UTF-8でURLエンコード
	 * @return
	 */
	public Escape URLEncode() {
		string=URLEncode(string);
		return this;
	}

	/**
	 * URLデコードをUTF-8でする
	 * @param s
	 * @return
	 */
	public String URLDecode(String s) {
		try {
			return URLDecoder.decode(s,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			System.out.println("デコードに失敗しました");
			return s;
		}
	}

	/**
	 * URLデコードをUTF-8でする
	 * @return
	 */
	public Escape URLDecode() {
		string=URLDecode(string);
		return this;
	}

	//HTMLエスケープ
	/**
	 * HTMLエスケープを実行します。
	 * @param str HTMLエスケープする文字列
	 * @return HTMLエスケープされた文字列
	 */
	public String HTML(String str) {
		if(str == null)return null;

		return str.replaceAll("&", "&amp;")
		.replaceAll("<", "&lt;")
		.replaceAll(">", "&gt;")
		.replaceAll("\"", "&quot;")
		.replaceAll(",", "&#44;");
	}
	//HTML逆エスケープ
	/**
	 * HTMLエスケープを実行します。
	 * @param str HTMLエスケープする文字列
	 * @return HTMLエスケープされた文字列
	 */
	public String RHTML(String str) {
		if(str == null) {
			return null;
		}

		return str.replaceAll("&amp;","&")
		.replaceAll("&lt;","<")
		.replaceAll("&gt;",">")
		.replaceAll("&quot;","\"")
		.replaceAll( "&#44;",",");

	}

	//SQLエスケープ
	/**
	 * SQLエスケープを実行します。
	 * @param str SQLエスケープする文字列
	 * @return SQLエスケープされた文字列
	 */
	public String SQL(String str) {
		if(str == null) {
			return null;
		}

		return str.replaceAll("'", "''")
				.replaceAll("\\", "\\\\");
	}
	//テキストエリア改行
	/**
	 * 改行の置換を実行します。
	 * @param str 置き換える文字列
	 * @return 改行された文字列
	 */
	public String TA(String str) {
		if(str == null) {
			return null;
		}

		return str.replaceAll("\r\n","<br />");
	}

	/**
	 * エスケープをメソッドチェーンで実行するために値を格納。
	 * @param s エスケープする文字列
	 * @return this
	 */
	public Escape set(String s) {
		this.string=s;
		return this;
	}
	/**
	 * 処理された値を返す。
	 * @return this.string
	 */
	public String get() {
		return this.string;
	}

	//HTMLエスケープ
	/**
	 * HTMLエスケープを実行します。
	 * @return this
	 */
	public Escape HTML() {
		this.string= this.string.replaceAll("&", "&amp;")
			.replaceAll("<", "&lt;")
			.replaceAll(">", "&gt;")
			.replaceAll("\"", "&quot;")
			.replaceAll(",", "&#44;");
		return this;
	}
	//HTML逆エスケープ
	/**
	 * HTMLアンエスケープを実行します。
	 * @return this
	 */
	public Escape RHTML() {
		this.string=this.string.replaceAll("&amp;","&")
				.replaceAll("&lt;","<")
				.replaceAll("&gt;",">")
				.replaceAll("&quot;","\"")
				.replaceAll( "&#44;",",");
		return this;

	}

	//SQLエスケープ
	/**
	 * SQLエスケープを実行します。
	 * @return this
	 */
	public Escape SQL() {
		this.string.replaceAll("'", "''")
		.replaceAll("\\", "\\\\");
		return this;
	}
	//テキストエリア改行
	/**
	 * 改行の置換を実行します。
	 * @param str 置き換える文字列
	 * @return 改行された文字列
	 */
	public Escape TA() {
		this.string.replaceAll("\r\n","<br />");
		return this;
	}





}

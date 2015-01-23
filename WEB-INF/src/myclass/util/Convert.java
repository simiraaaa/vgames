package myclass.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 文字列の変換とかする <br>
 * myclass.Escapeのメソッドも移植
 * 
 * @author yuki
 * 
 */
public class Convert {

	private static final char C0 = '0';
	private static final String
	HEAD = "\\u";

	private static final int
	UNICODE_LENGTH = 4,
	UNICODE_RATE = 6;

    private String converted = null;

    /**
     * 変換後の文字列を返します。
     * 
     * @return
     */
    public String get() {
        return converted;
    }

    /**
     * 変換する文字列をセットします。
     * 
     * @param s
     * @return
     */
    public Convert set(String s) {
        converted = s;
        return this;
    }

    /**
     * 文字列をunicode文字列に変換
     * 
     * @param s
     *            unicodeにする文字列
     * @param toUpper
     *            trueで大文字<br>
     *            falseで小文字
     * @return
     */
    public static String toUnicode(final String s, boolean noBase62, boolean toUpper) {
		final char[] chars = s.toCharArray();
		final int len = chars.length;
        StringBuffer sb = noBase62 ? new StringBuffer() : new StringBuffer(len * UNICODE_RATE);
		for(int i = 0; i < len; ++i){
            final char c = chars[i];
            if (noBase62) {
                if (Base62.isBase62(c)) {
                    sb.append(c);
                    continue;
                }
            }
            final String s16 = Integer.toHexString((int) c);
			sb.append(HEAD);
			for(int j = UNICODE_LENGTH - s16.length(); j > 0; --j){
				sb.append(C0);
			}
			sb.append(s16);
		}
		return (toUpper) ? sb.toString().toUpperCase() : sb.toString();
	}

    /**
     * 文字列をunicode文字列に変換
     * 
     * @param toUpper
     *            trueで大文字<br>
     *            falseで小文字
     * @param noBase62
     *            trueで[0-9a-zA-Z]を無視する
     * @return
     */
    public Convert toUnicode(boolean noBase62, boolean toUpper) {
        converted = toUnicode(converted, noBase62, toUpper);
        return this;
    }

    /**
     * 文字列をunicode文字列に変換
     * 
     * @param s
     *            unicodeにする文字列
     */
	public static String toUnicode(final String s){
        return toUnicode(s, false, false);
	}

    /**
     * 文字列をUnicode文字列に変換する
     * 
     * @param s
     * @param noBase62
     *            trueで[0-9a-zA-Z]を無視する
     * @return
     */
    public static String toUnicode(final String s, boolean noBase62) {
        return toUnicode(s, noBase62, false);
    }

    /**
     * 文字列をUnicode文字列に変換する
     * 
     * @param noBase62
     *            trueで[0-9a-zA-Z]を無視する
     * @return
     */
    public Convert toUnicode(boolean noBase62) {
        converted = toUnicode(converted, noBase62);
        return this;
    }

    /**
     * 文字列をunicode文字列に変換
     * 
     * @return
     */
    public Convert toUnicode() {
        converted = toUnicode(converted, false, false);
        return this;
    }

    /**
     * UTF-8でURLエンコードをする
     * 
     * @param s
     *            エスケープ対象の文字列
     * @return
     */
    public static String encodeURL(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            System.out.println("エンコードに失敗しました");
            return s;
        }
    }

    /**
     * UTF-8でURLエンコード
     * 
     * @return
     */
    public Convert encodeURL() {
        converted = encodeURL(converted);
        return this;
    }

    /**
     * URLデコードをUTF-8でする
     * 
     * @param s
     * @return
     */
    public static String decodeURL(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            System.out.println("デコードに失敗しました");
            return s;
        }
    }

    /**
     * utf8をデコード
     * 
     * @return
     */
    public Convert decodeURL() {
        converted = decodeURL(converted);
        return this;
    }

    /**
     * HTMLエスケープを実行します。
     * 
     * @param str
     *            HTMLエスケープする文字列
     * @return HTMLエスケープされた文字列
     */
    public static String escapeHtml(String str) {
        if (str == null)
            return null;

        return str.//
        replaceAll("&", "&amp;").//
        replaceAll("<", "&lt;").//
        replaceAll(">", "&gt;").//
        replaceAll("\"", "&quot;").//
        replaceAll(",", "&#44;");
    }

    /**
     * HTMLエスケープ
     * 
     * @return
     */
    public Convert escapeHtml() {
        converted = escapeHtml(converted);
        return this;
    }

    /**
     * HTMLのunescape
     * 
     * @param str
     * @return
     */
    public static String unespaceHtml(String str) {
        if (str == null)
            return null;

        return str.//
        replaceAll("&", "&amp;").//
        replaceAll("<", "&lt;").//
        replaceAll(">", "&gt;").//
        replaceAll("\"", "&quot;").//
        replaceAll(",", "&#44;");
    }

    /**
     * HTMLアンエスケープ
     * 
     * @return
     */
    public Convert unespaceHtml() {
        converted = unespaceHtml(converted);
        return this;
    }

    /**
     * SQLエスケープを実行します。
     * 
     * @param str
     *            SQLエスケープする文字列
     * @return SQLエスケープされた文字列
     */
    public static String escapeSql(String str) {
        if (str == null) {
            return null;
        }

        return //
        str//
        .replaceAll("'", "''")//
        .replaceAll("\\", "\\\\");
    }

    /**
     * SQLエスケープ
     * 
     * @return
     */
    public Convert escapeSql() {
        converted = escapeSql(converted);
        return this;
    }

    public static String escapeWildcard(String s) {
        if (Compare.isEmpty(s)) {
            return s;
        }
        return s.replaceAll("%", "\\\\%").//
        replaceAll("_", "\\\\_");
    }

    public static String escapeWildcard(String s, String e) {
        if (Compare.isEmpty(s)) {
            return s;
        }
        return s.replaceAll("%", e + "%").replaceAll("_", e + "_");
    }

    /**
     * 改行の置換を実行します。
     * 
     * @param str
     *            置き換える文字列
     * @return 改行された文字列
     */
    public static String escapeTextarea(String str) {
        if (str == null) {
            return null;
        }

        return //
        str//
        .replaceAll("\r\n", "<br />")//
        .replaceAll("\n", "<br />");
    }

    /**
     * 改行文字をbrタグに置換
     * 
     * @return
     */
    public Convert escapeTextarea() {
        converted = escapeTextarea(converted);
        return this;
    }
}

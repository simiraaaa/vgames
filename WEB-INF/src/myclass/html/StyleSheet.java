package myclass.html;

import java.util.HashMap;

/**
 * Tagのプロパティでのstyle="ここ"
 *
 * @author yuki
 *
 */
public class StyleSheet {
    private static final char COLON = ':', SEMICOLON = ';', DBQT = '"';
    private static final String STYLE = "style=";
    protected HashMap<String, String> map = null;

    public StyleSheet() {
        // TODO 自動生成されたコンストラクター・スタブ
        map = new HashMap<>();
    }

    /**
     * styleを追加
     *
     * @param key
     * @param value
     * @return
     */
    public StyleSheet put(String key, String value) {
        map.put(key, value);
        return this;
    }

    /**
     * keyとvalue交互
     *
     * @param kv
     * @return
     */
    public StyleSheet puts(String... kv) {
        for (int i = 0, len = kv.length; i < len; ++i) {
            map.put(kv[i], kv[++i]);
        }
        return this;
    }

    /**
     * style追加
     *
     * @param map
     * @return
     */
    public StyleSheet putAll(HashMap<String, String> map) {
        this.map.putAll(map);
        return this;
    }

    /**
     * style追加
     *
     * @return
     */
    public StyleSheet putAll(StyleSheet css) {
        map.putAll(css.getMap());
        return this;
    }

    /**
     * 引数のStyleSheetクラスインスタンスに自身のstyle追加
     *
     * @return
     */
    public StyleSheet putAllTo(StyleSheet css) {
        css.putAll(this);
        return this;
    }

    public HashMap<String, String> getMap() {
        return map;
    }

    /**
     * style="key:value;"の形にする
     *
     * @return
     */
    public String create() {
        return create(map);
    }

    /**
     * mapからstyle="key:value;"の形にする
     *
     * @param map
     * @return
     */
    public static String create(HashMap<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append(STYLE).append(DBQT);

        map.forEach((k, v) -> {
            sb.append(k).append(COLON).append(v).append(SEMICOLON);
        });

        return sb.append(DBQT).toString();
    }
}

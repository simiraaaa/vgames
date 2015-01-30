package myclass.html;

/**
 * HTMLタグを生成するためのクラス
 *
 * @author yuki
 *
 */
public class Tag {
    private static final char START_BRACKET = '<', SLASH = '/', END_BRACKET = '>', SPACE = ' ';
    private String tagName, text, property, style;

    public Tag() {
        // TODO 自動生成されたコンストラクター・スタブ
    }

    public Tag(String tagName) {
        this.tagName = tagName;
    }

    public Tag(String tagName, String text) {
        this.tagName = tagName;
        this.text = text;
    }

    public Tag(String tagName, String text, String property) {
        this.tagName = tagName;
        this.text = text;
        this.property = property;
    }

    public Tag(String tagName, String text, String property, String style) {
        this.tagName = tagName;
        this.text = text;
        this.property = property;
        this.style = style;
    }

    /**
     * このインスタンスを元にタグを生成します。
     *
     * @return
     */
    public String create() {
        return create(tagName, text, property, style);
    }

    /**
     * このインスタンスを元に開始タグを生成します。
     *
     * @param isStart
     * @return
     */
    public String create(boolean isStart) {
        return createStartTag(tagName, property, style);
    }

    /**
     * このインスタンスを元に開始タグを生成します。
     *
     * @param isStart
     * @return
     */
    public String createStartTag() {
        return createStartTag(tagName, property, style);
    }

    /**
     * 終了タグを生成します。
     *
     * @return
     */
    public String createEndTag() {
        return createEndTag(tagName);
    }

    public String getProperty() {
        return property;
    }

    public String getStyle() {
        return style;
    }

    public String getTagName() {
        return tagName;
    }

    public String getText() {
        return text;
    }

    public Tag setProperty(String property) {
        this.property = property;
        return this;
    }

    public Tag setStyle(String style) {
        this.style = style;
        return this;
    }

    public Tag setTagName(String tagName) {
        this.tagName = tagName;
        return this;
    }

    public Tag setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * Stringでタグを生成します。<br>
     * 開始タグのみのタグの場合はcreateStartTagを使用してください。
     *
     * @param tagName
     * @param text
     * @param property
     * @param style
     * @return
     */
    public static String create(String tagName, String text, String property, String style) {
        if (text == null) {
            return createStartTag(tagName, property, style) + createEndTag(tagName);
        }
        return createStartTag(tagName, property, style) + text + createEndTag(tagName);
    }

    /**
     * Stringでタグを生成します。<br>
     * 開始タグのみのタグの場合はcreateStartTagを使用してください。
     *
     * @param tagName
     * @param text
     * @param property
     * @return
     */
    public static String create(String tagName, String text, String property) {
        if (text == null) {
            return createStartTag(tagName, property) + createEndTag(tagName);
        }
        return createStartTag(tagName, property) + text + createEndTag(tagName);
    }

    /**
     *
     * Stringでタグを生成します。<br>
     * 開始タグのみのタグの場合はcreateStartTagを使用してください。
     *
     * @param tagName
     * @param text
     * @return
     */
    public static String create(String tagName, String text) {
        if (text == null) {
            return create(tagName);
        }
        return createStartTag(tagName) + text + createEndTag(tagName);
    }

    /**
     *
     * Stringでタグを生成します。<br>
     * 開始タグのみのタグの場合はcreateStartTagを使用してください。
     *
     * @param tagName
     * @return
     */
    public static String create(String tagName) {
        return createStartTag(tagName) + createEndTag(tagName);
    }

    /**
     * 開始タグを生成します。
     *
     * @param tagName
     * @param property
     * @param style
     * @return
     */
    public static String createStartTag(String tagName, String property, String style) {
        if (style == null) {
            return createStartTag(tagName, property);
        }
        if (property == null) {
            return create(tagName, style);
        }
        return START_BRACKET + tagName + SPACE + property + SPACE + style + END_BRACKET;
    }

    /**
     * 開始タグを生成します。
     *
     * @param tagName
     * @param property
     *            ここはstyleをセットすることもできます。
     * @return
     */
    public static String createStartTag(String tagName, String property) {
        if (property == null) {
            return createStartTag(tagName);
        }
        return START_BRACKET + tagName + SPACE + property + END_BRACKET;
    }

    /**
     * 開始タグを生成します。
     *
     * @param tagName
     * @return
     */
    public static String createStartTag(String tagName) {
        return START_BRACKET + tagName + END_BRACKET;
    }

    /**
     * 終了タグを生成します。
     *
     * @param tagName
     * @return
     */
    public static String createEndTag(String tagName) {
        return START_BRACKET + "" + SLASH + tagName + END_BRACKET;
    }
}

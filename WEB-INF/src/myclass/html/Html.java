package myclass.html;

/**
 * TagクラスとStyleクラスをうまくやる
 *
 * @author yuki
 *
 */
public class Html {
    public static final String//
            START_HTML = "<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">",
            START_BODY = "</head><body>", END_HTML = "</body></html>";

    /**
     * titleタグ
     * 
     * @param title
     *            なんてタイトル
     * @return
     */
    public static String createTitle(String title) {
        return Tag.create("title", title);
    }

}

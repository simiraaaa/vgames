package myclass.html;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * writingメソッドを実装するとコンストラクタで勝手に実行する<br>
 * 実行したくない場合はコンストラクタの引数にfalseを指定
 *
 * @author yuki
 *
 */
public abstract class HtmlWriter {

    protected PrintWriter out = null;

    public HtmlWriter(PrintWriter pw, String title) throws IOException {
        out = pw;
        init(title);
    }

    public HtmlWriter(PrintWriter pw) throws IOException {
        out = pw;
        init("");
    }

    public HtmlWriter(HttpServletResponse response, String title) throws IOException {
        this(response.getWriter(), title);
    }

    public HtmlWriter(HttpServletResponse response) throws IOException {
        this(response.getWriter());
    }

    public HtmlWriter(PrintWriter pw, boolean write) throws IOException {
        out = pw;
        if (write) {
            init("");
        }
    }

    public HtmlWriter(HttpServletResponse response, boolean write) throws IOException {
        this(response.getWriter(), write);
    }

    public void setWriter(PrintWriter writer) {
        this.out = writer;
    }

    private void init(String title) throws IOException {
        write(Html.START_HTML);
        writingHead();
        write(Html.createTitle((title == null ? "" : title)));
        write(Html.START_BODY);
        writingBody();
        write(Html.END_HTML);
    }

    /**
     * このメソッドはコンストラクタで実行されます。
     */
    public abstract void writingBody() throws IOException;

    /**
     * このメソッドはコンストラクタで実行されます。
     */
    public abstract void writingHead() throws IOException;

    /**
     * 存在するpathを指定する。<br>
     * そのテキストを全て出力する。
     *
     * @param realPath
     * @return
     * @throws IOException
     */
    public HtmlWriter include(String realPath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(realPath));
        br.lines().forEach(out::println);
        br.close();
        return this;
    }

    /**
     * 文字列をそのまま出力する
     *
     * @param s
     * @return
     */
    public HtmlWriter write(String s) {
        out.println(s);
        return this;
    }

    /**
     * Tagをcreateして出力します。<br>
     * 開始タグだけの場合は第二匹数を指定してください<br>
     * 第二匹数はtrueでもfalseでも開始タグだけの出力になります。
     *
     * @param t
     * @return
     */
    public HtmlWriter write(Tag t) {
        out.print(t.create());
        return this;
    }

    /**
     * 開始タグのみ出力 <br>
     * 終了タグを出力する場合は第二匹数を省略してください。
     *
     * @param t
     * @param isStart
     * @return
     */
    public HtmlWriter write(Tag t, boolean isStart) {
        out.println(t.create(isStart));
        return this;
    }
}

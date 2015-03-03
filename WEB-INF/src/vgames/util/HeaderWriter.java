package vgames.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.database.MyDatabase;
import myclass.database.SQLCreator;
import myclass.html.Html;
import myclass.html.HtmlWriter;
import myclass.html.Tag;
import myclass.servlet.Path;
import myclass.util.JSON;
import myclass.wrap.MyHashMap;
import vgames.table.Genre;
import vgames.table.User;

public abstract class HeaderWriter {

    protected MyDatabase db = null;
    protected PrintWriter out = null;
    protected String INCLUDE_PATH;

    public HeaderWriter(HttpServletRequest req, HttpServletResponse res, HttpServlet servlet,
            User user, String title) throws IOException {

        db = VGLogin.checkLogin(req, res, user, false);
        final boolean isGuest = user.getId() == null;

        final String CONTEXTPATH = req.getContextPath() + "/"; //
        INCLUDE_PATH = servlet.getServletContext().getRealPath("/include") + "/";

        SQLCreator sc = new SQLCreator(Genre.TABLE_NAME, Genre.NAME).addOrderBy(Genre.ID);

        ArrayList<String> genres = new ArrayList<>();

        try {
            ArrayList<HashMap<String, Object>> list = db.exe(sc.select(), "[]", Genre.NAME).getList();
            list.forEach(m -> {
                genres.add((String) m.get(Genre.NAME));
            });
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            res.getWriter().println("ジャンルの取得に失敗しました。");
            e.printStackTrace();
            return;
        }

        new HtmlWriter(out = res.getWriter(), title) {

            @Override
            public void writingHead() throws IOException {
                // TODO 自動生成されたメソッド・スタブ

                write(Html.createLinkCSS(CONTEXTPATH + "css/header.css"));
                write(Html.createLinkCSS(CONTEXTPATH + "css/common.css"));

                WrapJspWriter.writeScript(out, CONTEXTPATH, Path.JS_PATH, Path.getJsLibs());

                StringJoiner sj = new StringJoiner("','", ";vg.GENRE_LIST=['", "'];");
                genres.forEach(s -> sj.add(s));
                write(new Tag("script",//
                "vg.user=" + JSON.stringify(MyHashMap.create("id", user.getId(),//
                        "name", user.getName(),//
                        "plainProf", user.getPlainProf(),//
                        "icon", user.getIcon(),//
                        "prof", user.getProf())) + //
                ";vg.set('" + CONTEXTPATH + "');vg.isGuest=" + String.valueOf(isGuest) + //
                sj.toString()));
                writingIntoHead();

            }

            @Override
            public void writingBody() throws IOException {
                // TODO 自動生成されたメソッド・スタブ

                includeHtml("header.html");
                writingIntoBody();

            }
        };
    }

    public MyDatabase getDb() {
        return db;
    }

    /**
     * このメソッドはコンストラクタで実行されます。
     */
    public abstract void writingIntoBody() throws IOException;

    /**
     * このメソッドはコンストラクタで実行されます。
     */
    public abstract void writingIntoHead() throws IOException;

    /**
     * 存在するpathを指定する。<br>
     * そのテキストを全て出力する。
     *
     * @param realPath
     * @return
     * @throws IOException
     */
    public HeaderWriter include(String realPath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(realPath));
        br.lines().forEach(out::println);
        br.close();
        return this;
    }

    public HeaderWriter includeHtml(String name) throws IOException {
        return include(INCLUDE_PATH + name);
    }

    /**
     * 文字列をそのまま出力する
     *
     * @param s
     * @return
     */
    public HeaderWriter write(String s) {
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
    public HeaderWriter write(Tag t) {
        out.println(t.create());
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
    public HeaderWriter write(Tag t, boolean isStart) {
        out.println(t.create(isStart));
        return this;
    }

}

package vgames;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.database.MyDatabase;
import myclass.database.SQLCreator;
import myclass.html.Html;
import myclass.html.HtmlWriter;
import myclass.html.Tag;
import myclass.servlet.ExtendedHttpServlet;
import myclass.servlet.Path;
import vgames.table.Genre;
import vgames.table.User;
import vgames.util.VGLogin;
import vgames.util.WrapJspWriter;

public class Test extends ExtendedHttpServlet {

    @Override
    protected void subAction(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // TODO 自動生成されたメソッド・スタブ

        User user = new User();
        MyDatabase db = VGLogin.checkLogin(req, res, user, false);
        final boolean isGuest = user.getId() == null;

        final String CONTEXTPATH = req.getContextPath() + "/", //
        REALPATH = servlet.getServletContext().getRealPath("/include") + "/";

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

        /**
         * このHtmlWriterを継承してこんなん作っておく
         */
        new HtmlWriter(res, "header") {

            @Override
            public void writingHead() throws IOException {
                // TODO 自動生成されたメソッド・スタブ

                write(Html.createLinkCSS(CONTEXTPATH + "css/header.css"));

                WrapJspWriter.writeScript(out, CONTEXTPATH, Path.JS_PATH, Path.getJsLibs());

                StringJoiner sj = new StringJoiner("','", "['", "'];");
                genres.forEach(s -> sj.add(s));
                write(new Tag("script",//
                "vg.path='" + CONTEXTPATH + "';vg.isGuest=" + String.valueOf(isGuest) + ";vg.GENRE_LIST=" + sj.toString()));

            }

            @Override
            public void writingBody() throws IOException {
                // TODO 自動生成されたメソッド・スタブ

                include(REALPATH + "header.html");

                write("ボディー");
                if (isGuest) {
                    write(new Tag("h3", "ログインしていません。"));
                }
            }
        };

        db.close();
    }
}

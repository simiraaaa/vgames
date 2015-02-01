package vgames.html;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.database.MyDatabase;
import myclass.html.Attribute;
import myclass.html.Tag;
import myclass.servlet.ExtendedHttpServlet;
import myclass.util.Convert;
import vgames.table.User;
import vgames.util.HeaderWriter;

public class Mypage extends ExtendedHttpServlet {

    @Override
    protected void subAction(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // TODO 自動生成されたメソッド・スタブ
        User user = new User();
        MyDatabase db = new HeaderWriter(req, res, servlet, user, "マイページ") {

            @Override
            public void writingIntoHead() throws IOException {
                // TODO 自動生成されたメソッド・スタブ
                write(

                new Tag("script", //
                "onload = function(){vg.Logout().appendTo();}"));

            }

            @Override
            public void writingIntoBody() throws IOException {
                // TODO 自動生成されたメソッド・スタブ
                write("ようこそ" + Convert.escapeHtml(user.getName()) + "さん");
                write(//
                new Tag("form",//
                new Tag("input", "", new Attribute().puts("type", "file", "name", "game").create()).create(true) + //
                new Tag("input", "", new Attribute().puts("type", "submit").create()).create(true),//
                new Attribute()//
                .puts("method", "post", "aciton", req.getContextPath() + "/s/gameup.json", "enctype", "multipart/form-data").create()));
            }
        }.getDb();

        if (user.getId() == null) {
            res.sendRedirect(req.getContextPath() + "/login.jsp");

        }
        db.close();

    }

}

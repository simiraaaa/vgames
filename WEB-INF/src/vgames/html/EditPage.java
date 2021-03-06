package vgames.html;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.servlet.ExtendedHttpServlet;
import myclass.servlet.Path;
import vgames.table.User;
import vgames.util.HeaderWriter;

public class EditPage extends ExtendedHttpServlet {

    @Override
    protected void subAction(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // TODO 自動生成されたメソッド・スタブ
        User user = new User();

        new HeaderWriter(req, res, servlet, user, "VGAMES ゲーム投稿ページ") {

            @Override
            public void writingIntoHead() throws IOException {
                // TODO 自動生成されたメソッド・スタブ

            }

            @Override
            public void writingIntoBody() throws IOException {
                // TODO 自動生成されたメソッド・スタブ
                includeHtml(Path.EDITHTML);

                db.close();
            }
        };

        if (user.getId() == null) {
            Path.redirectLogin(req, res);
        }
    }

}

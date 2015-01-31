package vgames;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.database.MyDatabase;
import myclass.servlet.ExtendedHttpServlet;
import vgames.table.User;
import vgames.util.HeaderWriter;

public class Test extends ExtendedHttpServlet {

    @Override
    protected void subAction(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // TODO 自動生成されたメソッド・スタブ

        User user = new User();
        MyDatabase db = new HeaderWriter(req, res, servlet, user, "たいとる") {

            @Override
            public void writingIntoHead() throws IOException {
                // TODO 自動生成されたメソッド・スタブ

            }

            @Override
            public void writingIntoBody() throws IOException {
                // TODO 自動生成されたメソッド・スタブ
                write("ボディー____________");

            }
        }.getDb();
        db.close();
    }
}

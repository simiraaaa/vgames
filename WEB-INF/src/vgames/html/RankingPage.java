package vgames.html;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.html.Tag;
import myclass.servlet.ExtendedHttpServlet;
import myclass.servlet.Path;
import myclass.util.Compare;
import vgames.table.User;
import vgames.util.HeaderWriter;

public class RankingPage extends ExtendedHttpServlet {

    @Override
    protected void subAction(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // TODO 自動生成されたメソッド・スタブ
        // TODO 自動生成されたメソッド・スタブ
        User user = new User();
        String gameid = getParam("gameid");

        new HeaderWriter(req, res, servlet, user, "VGAMES ランキング") {

            @Override
            public void writingIntoHead() throws IOException {
                // TODO 自動生成されたメソッド・スタブ
                if (!Compare.isEmpty(gameid)) {
                    write(new Tag("script",//
                    "vg.game = vg.game || {}; vg.game.id = " + gameid + ";"));
                }

            }

            @Override
            public void writingIntoBody() throws IOException {
                // TODO 自動生成されたメソッド・スタブ
                includeHtml(Path.RANKINGHTML);

                db.close();
            }
        };

    }

}

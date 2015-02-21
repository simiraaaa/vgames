package vgames.html;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.html.Tag;
import myclass.servlet.ExtendedHttpServlet;
import myclass.servlet.Path;
import myclass.util.Compare;
import vgames.table.User;
import vgames.util.HeaderWriter;

public class PlayPage extends ExtendedHttpServlet {

    @Override
    protected void subAction(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // TODO 自動生成されたメソッド・スタブ
        // TODO 自動生成されたメソッド・スタブ
        User user = new User();
        String gameid = getParam("gameid");

        new HeaderWriter(req, res, servlet, user, "VGAMES") {

            @Override
            public void writingIntoHead() throws IOException {
                // TODO 自動生成されたメソッド・スタブ
                if (!Compare.isEmpty(gameid)) {
                    ArrayList<HashMap<String, Object>> list = null;
                    try {
                        list = db.setPrepareObjects(gameid)//
                        .exe("select g.uid as uid,u.uname as uname from vgame as g,vuser as u where g.uid=u.uid and g.gid=?",//
                        "[]",//
                        "uid", "uname")//
                        .getList();
                    } catch (SQLException e) {
                        // TODO 自動生成された catch ブロック
                        e.printStackTrace();
                    }
                    String script = "vg.game = vg.game || {}; vg.game.id = " + gameid + ";";
                    if (list != null && !list.isEmpty()) {
                        HashMap<String, Object> map = list.get(0);
                        script += "vg.game.uname='" + map.get("uname") + "';vg.game.uid='" + //
                        map.get("uid") + "';";
                    }
                    write(new Tag("script",//
                    script));
                }

            }

            @Override
            public void writingIntoBody() throws IOException {
                // TODO 自動生成されたメソッド・スタブ
                includeHtml(Path.PLAYHTML);

                db.close();
            }
        };

    }

}

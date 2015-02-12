package vgames.json;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.database.MyDatabase;
import myclass.servlet.AjaxResponse;
import myclass.util.Compare;
import myclass.util.Convert;
import vgames.table.Game;
import vgames.table.User;
import vgames.util.ConnectionVG;
import vgames.util.VGDataBase;
import vgames.util.VGLogin;

public class JsonGetter extends AjaxResponse {

    private static final String //
            STATUS = "status",
            SUCCESS = "success", ALERT = "alert", TEXT = "text", DEFAULTERROR = "不正な値が送信されました。";
    private static final int //
            LIST = 0,
            IMG = 1, GAME = 2, SETUMEI = 3, SEARCH = 4, SAVE = 5, LOAD = 6;

    private int KEY = 0;

    User user = null;

    MyDatabase db = null;

    @Override
    protected void ajax(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // TODO 自動生成されたメソッド・スタブ
        user = new User();
        String err = run(getParam("key"));

        if (db != null)
            db.close();

        if (err != null) {
            puts(STATUS, ALERT, TEXT, err);
        }

        send();
    }

    private String run(String KEY) {
        if (Compare.isEmpty(KEY)) {
            return DEFAULTERROR;
        }
        try {
            return run(Integer.parseInt(KEY));
        } catch (NumberFormatException e) {
            return DEFAULTERROR;
        }
    }

    private String run(int k) {
        KEY = k;
        switch (KEY) {
        case LIST:
            return getGameList();
        case IMG:
            return getGameImage();
        case GAME:
            return getGameData();
        case SETUMEI:
            return getSetumei();
        case SEARCH:
            return search();
        default:
            return DEFAULTERROR;
        }
    }

    /**
     * useridを元にゲームのリストを送信する
     *
     * @return
     */
    private String getGameList() {
        String userid = getParam("userid");
        if (Compare.isEmpty(userid)) {
            db = VGLogin.checkLogin(request, response, user, false);
            if (user.getId() == null)
                return "ログインするかuseridを指定してください。";
            userid = user.getId();
        } else {
            db = ConnectionVG.open(request);
        }

        try {
            puts(STATUS, SUCCESS, TEXT, VGDataBase.getGameList(db, userid));
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return userid + "のゲームリスト取得に失敗しました";
        }

        return null;
    }

    /**
     * ひとつのゲームの画像のみを取得
     *
     * @return
     */
    private String getGameImage() {
        int gameid = getIntParam("gameid");
        puts(STATUS, SUCCESS, TEXT, VGDataBase.getImgTitle(db = ConnectionVG.open(request), gameid));
        return null;
    }

    /**
     * ひとつのゲームのデータを取得
     *
     * @return
     */
    private String getGameData() {
        int gameid = getIntParam("gameid");
        HashMap<String, Object> list = VGDataBase.getGameData(db = ConnectionVG.open(request), gameid, Game.getFields()).get(0);
        puts(STATUS, SUCCESS, TEXT, list);
        return null;
    }

    /**
     * 説明をエスケープして
     *
     * @return
     */
    private String getSetumei() {
        int gameid = getIntParam("gameid");
        HashMap<String, Object> list = VGDataBase.getGameData(db = ConnectionVG.open(request), gameid, Game.getFields()).get(0);
        puts(STATUS, SUCCESS, TEXT, Convert.escapeTextarea(Convert.escapeHtml((String) list.get(Game.SETUMEI))));
        return null;

    }

    private String search() {
        String query = getParam("query");
        String genreid = getParam("genreid");
        boolean isALL = genreid == null || "all".equals(genreid);

        if (query == null) {
            return "queryを指定してください";
        }

        try {
            db = ConnectionVG.open(request);
            puts(STATUS, SUCCESS, TEXT, (isALL ? VGDataBase.queryGame(db, query) : VGDataBase.queryGame(db, query, Integer.parseInt(genreid))));
            return null;
        } catch (Exception e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();

            return "検索に失敗しました";
        }
    }
}

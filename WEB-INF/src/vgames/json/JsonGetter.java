package vgames.json;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.database.MyDatabase;
import myclass.servlet.AjaxResponse;
import myclass.util.Compare;
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
            IMG = 1, GAME = 2;

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
        puts(STATUS, SUCCESS, TEXT, VGDataBase.getGameData(db = ConnectionVG.open(request), gameid, Game.getFields()).get(0));
        return null;
    }
}

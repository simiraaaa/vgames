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
import myclass.wrap.MyHashMap;
import vgames.table.Game;
import vgames.table.User;
import vgames.util.ConnectionVG;
import vgames.util.VGDataBase;
import vgames.util.VGLogin;

public class JsonGetter extends AjaxResponse {

    private static final String //
            STATUS = "status",
            SUCCESS = "success", ALERT = "alert", TEXT = "text",
            DEFAULTERROR = "不正な値が送信されました。",
            DATA = "data", LOGIN = "login";
    private static final int //
            LIST = 0,
            IMG = 1, GAME = 2, SETUMEI = 3, SEARCH = 4, SAVE = 5, LOAD = 6,
            RECENT = 7,
            FAVLIST = 8, ISFAV = 9, FAV = 10, USER = 11;

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
        case SAVE:
            return save();
        case LOAD:
            return load();
        case FAV:
            return fav();
        case ISFAV:
            return isFav();
        case FAVLIST:
            return getFavList();
        case USER:
            return getUser();
        default:
            return DEFAULTERROR;
        }
    }

    private String getUser() {
        String uid = getParam("userid");
        if (Compare.isEmpty(uid)) {
            return "ユーザIDを指定してください";
        }
        db = ConnectionVG.open(request);
        User user = VGDataBase.getUser(db, uid);
        if (user == null) {
            puts(STATUS, SUCCESS, TEXT, null);
            return null;
        }
        puts(STATUS, SUCCESS, TEXT, MyHashMap.create("id", user.getId(), "name", user.getName(), "icon", user.getIcon(), "prof", user.getProf()));
        return null;
    }

    /**
     * お気に入りの取得、ログインしていない場合はUIDがあればいい
     *
     * @return
     */
    private String getFavList() {

        db = VGLogin.checkLogin(request, response, user, false);
        String userid = getParam("userid");

        if (Compare.isEmpty(userid)) {
            userid = user.getId();
        }

        if (userid == null) {
            return "ログインするか,useridを指定してください";
        }

        try {
            puts(STATUS, SUCCESS, TEXT, VGDataBase.getFavList(db, userid));
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return "お気に入り一覧の取得に失敗しました";
        }

        return null;
    }

    /**
     * お気に入りかどうか
     *
     * @return
     */
    private String isFav() {
        db = VGLogin.checkLogin(request, response, user, false);
        if (user.getId() == null) {
            return "ログインしてください";
        }
        String gid = getParam("gameid");
        if (Compare.isEmpty(gid)) {
            return "gameidを指定してください";
        }
        int gameid;
        try {
            gameid = Integer.parseInt(gid);
        } catch (Exception e) {
            // TODO: handle exception
            return "gameidが不正です";
        }

        puts(STATUS, SUCCESS, TEXT, VGDataBase.isFav(db, gameid, user.getId()));
        return null;
    }

    /**
     * お気に入りする
     *
     * @return
     */
    private String fav() {
        db = VGLogin.checkLogin(request, response, user, false);
        if (user.getId() == null) {
            return "ログインしてください";
        }
        String gid = getParam("gameid");
        if (Compare.isEmpty(gid)) {
            return "gameidを指定してください";
        }
        int gameid;
        try {
            gameid = Integer.parseInt(gid);
        } catch (Exception e) {
            // TODO: handle exception
            return "gameidが不正です";
        }

        try {
            VGDataBase.fav(db, gameid, user.getId());
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return "お気に入りの登録に失敗しました";
        }

        return null;
    }

    /**
     * saveはログインしてないとできない
     *
     * @return
     */
    private String save() {
        String gid = getParam("gameid");
        if (Compare.isEmpty(gid)) {
            return "gameidを指定してください";
        }
        String data = getParam("data");

        if (Compare.isEmpty(data)) {
            return "セーブデータを送信してください";
        }
        db = VGLogin.checkLogin(request, response, user, false);
        if (user.getId() == null)
            return "ログインしてください";
        String userid = user.getId();

        try {
            VGDataBase.saveData(db, Integer.parseInt(gid), userid, data);
        } catch (NumberFormatException | SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return "データの保存に失敗しました";
        }
        puts(STATUS, SUCCESS, TEXT, "データの保存に成功しました", LOGIN, true);
        return null;
    }

    /**
     * loadはuidが指定してあればそのユーザのデータ、<br>
     * uidがなければログインユーザ<br>
     * それもないならエラー
     *
     * @return
     */
    private String load() {
        String gid = getParam("gameid");
        if (Compare.isEmpty(gid)) {
            return "gameidを指定してください";
        }
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
            puts(STATUS, SUCCESS, DATA, VGDataBase.loadData(db, Integer.parseInt(gid), userid), LOGIN, true);
        } catch (NumberFormatException | SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return "セーブデータのロードに失敗しました。";
        }
        return null;
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

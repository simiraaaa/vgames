package vgames.json;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.database.MyDatabase;
import myclass.servlet.AjaxResponse;
import myclass.util.Compare;
import myclass.wrap.MyHashMap;
import vgames.table.User;
import vgames.util.VGDataBase;
import vgames.util.VGLogin;

public class UserRanking extends AjaxResponse {

    private static final String //
            STATUS = "status",
            ERROR = "error", TEXT = "text", TOP = "top", CURRENT = "current";
    MyDatabase db = null;
    User user = null;
    int gid = -1;
    boolean isLogin = false;
    int currentScore = -1;
    boolean isNoCurrentScore = false;

    private static final int //
            UPDATE = 0,
            GETLIST = 1;

    @Override
    protected void ajax(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // TODO 自動生成されたメソッド・スタブ
        String sgid = getParam("gameid");
        if (sgid == null) {
            puts(STATUS, ERROR, TEXT, "gameidを指定してください");
            send();
            return;
        }
        gid = Integer.parseInt(sgid);

        String skey = getParam("key");
        if (skey == null) {
            puts(STATUS, ERROR, TEXT, "keyを指定したください");
            send();
            return;
        }
        int key = Integer.parseInt(skey);

        String err = run(key);

        if (err != null) {
            puts(STATUS, ERROR, TEXT, err);
        }
        if (db != null) {
            db.close();
        }
        put(STATUS, "success");
        send();
    }

    private String run(int key) {
        user = new User();
        db = VGLogin.checkLogin(request, response, user, false);
        isLogin = user.getId() != null;

        switch (key) {
        case UPDATE:
            return update();
        case GETLIST:
            return getList();

        default:
            return "keyが不正です。";
        }
    }

    /**
     * 送られてきたスコアを保存するやつ、ログイン必須
     *
     * @return
     */
    private String update() {
        if (!isLogin) {
            return "ログインしてください";
        }
        String score = getParam("score");
        if (Compare.isEmpty(score)) {
            return "スコアを送信してください";
        }
        currentScore = Integer.parseInt(score);
        if (VGDataBase.isNewRank(db, user.getId(), gid)) {
            return VGDataBase.insertRanking(db, user.getId(), gid, score);

        }

        String err = VGDataBase.updateRanking(db, user.getId(), gid, score);
        if (err != null) {
            return err;
        }
        return VGDataBase.setHighScore(db, gid, user.getId());

    }

    private int getRank() throws SQLException {
        VGDataBase.setRank(db, gid);
        ArrayList<HashMap<String, Object>> list = db.setPrepareObjects(user.getId(), gid)//
        .exe("select rank from vranking where uid=? and gid=?", "[]", "rank").getList();
        return (int) list.get(0).get("rank");
    }

    /**
     * top10をtopにput
     *
     * @return
     */
    private String putTop10() {

        put(TOP, VGDataBase.getTop10(db, gid));
        return null;
    }

    /**
     * currentScore
     *
     * @return
     */
    private String setCurrentScore() {
        currentScore = VGDataBase.getCurrentScore(db, gid, user.getId());
        isNoCurrentScore = currentScore < 0;
        return null;
    }

    /**
     * top10とあれ
     *
     * @return
     */
    private String getList() {
        VGDataBase.setRank(db, gid);
        String err = putTop10();
        if (err != null) {
            return err;
        }
        if (!isLogin) {
            put(CURRENT, null);
            return null;
        }
        setCurrentScore();
        if (isNoCurrentScore) {
            put(CURRENT, null);
            return null;
        }
        return putCurrent();

    }

    private String putCurrent() {
        int rank = VGDataBase.getCurrentRank(db, gid, currentScore);
        if (rank == 0) {
            put(CURRENT, null);
            return null;
        }

        int start = rank - 5;
        if (start < 0)
            start = 0;

        ArrayList<HashMap<String, Object>> list = VGDataBase.getRanking(db, gid, start);

        if (list == null || list.isEmpty()) {
            return "現在のランキングの取得に失敗しました。";
        }
        int sa = rank - start - 1;
        list.add(null);
        for (int i = list.size() - 2; i >= sa; --i) {
            HashMap<String, Object> map = list.get(i);
            int r = (int) map.get("rank");
            map.put("rank", r + 1);
            list.set(i + 1, map);
        }

        list.set(sa, MyHashMap.create("uid", user.getId(), "uicon",//
                user.getIcon(), "uname", user.getName(), "score", currentScore,//
                "rank", rank));

        put(CURRENT, list);

        return null;
    }
}

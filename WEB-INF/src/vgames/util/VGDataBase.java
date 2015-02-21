package vgames.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import myclass.database.MyDatabase;
import myclass.database.SQLCreator;
import myclass.util.Compare;
import myclass.util.Convert;
import vgames.table.Game;
import vgames.table.Genre;
import vgames.table.MyList;
import vgames.table.Ranking;
import vgames.table.Save;
import vgames.table.User;

public class VGDataBase {

    /**
     * gameidの最大値+1取得
     *
     * @param db
     * @return
     */
    public static int getMaxGameIDaddOne(MyDatabase db) {
        SQLCreator sc = new SQLCreator()//
        .setTable(Game.TABLE_NAME)//
        .setColumns(Game.ID)//
        .addOrderBy(Game.ID + " desc")//
        .addLimit(1);
        int gameid = -1;
        try {

            ArrayList<HashMap<String, Object>> list = db.exe(sc.select(), "[]", Game.ID).getList();
            gameid = 0;
            if (list != null && !list.isEmpty()) {
                Object object = list.get(0).get(Game.ID);
                String string = object == null ? null : object.toString();
                gameid = 1 + Integer.valueOf((Compare.isEmpty(string) ? "0" : string));
            }
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        return gameid;
    }

    /**
     * 新しいゲームDBへ登録
     *
     * @param db
     * @param gameid
     * @param uid
     * @param genreid
     * @param title
     * @param imgName
     * @param setumei
     * @return
     */
    public static String insertGame(MyDatabase db, int gameid, String uid, int genreid,
            String title, String imgName, String setumei) {
        SQLCreator sc = new SQLCreator(Game.TABLE_NAME, Game.getFields());

        try {
            db.//
            setPrepareObjects(gameid, uid, genreid, title, imgName, 0, 0, setumei)//
            .exe(sc.insert());
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return "ゲームの登録に失敗しました。";
        }

        return null;
    }

    public static String updateGame(MyDatabase db, int gameid, String uid, int genreid,
            String title, String imgName, String setumei) {

        SQLCreator sc = new SQLCreator(Game.TABLE_NAME, User.ID, Genre.ID, Game.NAME, Game.IMG, Game.SETUMEI)//
        .setWhere(Game.ID + "=? and " + User.ID + "=?");

        try {
            db.//
            setPrepareObjects(uid, genreid, title, imgName, setumei, gameid, uid)//
            .exe(sc.update());
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return "ゲームの更新に失敗しました。";
        }
        return null;
    }

    /**
     * 既存ゲームのIMGソースpath
     *
     * @param db
     * @param gameid
     * @return
     */
    public static String getImgTitle(MyDatabase db, int gameid) {
        ArrayList<HashMap<String, Object>> list = getGameData(db, gameid, Game.IMG);
        if (list == null) {
            return null;
        }
        return (String) list.get(0).get(Game.IMG);
    }

    /**
     * 指定したフィールドのゲームデータひとつ
     *
     * @param db
     * @param gameid
     * @param fields
     * @return
     */
    public static ArrayList<HashMap<String, Object>> getGameData(MyDatabase db, int gameid,
            String... fields) {
        SQLCreator sc = new SQLCreator(Game.TABLE_NAME, fields).setWhere(Game.ID + "=?");

        try {
            return db//
            .setPrepareObjects(gameid)//
            .exe(sc.select(), "[]", fields).getList();
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return null;
        }
    }

    /**
     * userのゲームのリスト
     *
     * @param db
     * @param userid
     * @return
     * @throws SQLException
     */
    public static ArrayList<HashMap<String, Object>> getGameList(MyDatabase db, String userid) throws SQLException {
        SQLCreator sc = new SQLCreator(Game.TABLE_NAME, Game.getFields()).setWhere(User.ID + "=?");
        return db.setPrepareObjects(userid).exe(sc.select(), "[]", Game.getFields()).getList();
    }

    /**
     * リクエストされたゲームのランキングにユーザが初めて書き込むか
     *
     * @return
     */
    public static boolean isNewRank(MyDatabase db, String userid, int gameid) {
        SQLCreator sc = new SQLCreator(Ranking.TABLE_NAME, Game.ID).setWhere(Game.ID + "=? and " + User.ID + "=?");

        try {
            ArrayList<HashMap<String, Object>> list = //
            db//
            .setPrepareObjects(gameid, userid)//
            .exe(sc.select(), "[]", Game.ID)//
            .getList();

            return (list == null || list.isEmpty());
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return true;
        }
    }

    /**
     * insert
     *
     * @param db
     * @param userid
     * @param gameid
     * @param score
     * @return
     */
    public static String insertRanking(MyDatabase db, String userid, int gameid, String score) {
        SQLCreator sc = new SQLCreator(Ranking.TABLE_NAME, Ranking.getFields()).setWhere(Game.ID + "=? and " + User.ID + "+?");

        try {
            db.setPrepareObjects(gameid, userid, score, score, gameid, userid).exe(sc.insert());
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return "ランキングの登録に失敗";
        }

        return null;
    }

    /**
     * update
     *
     * @param db
     * @param userid
     * @param gameid
     * @param score
     * @return
     */
    public static String updateRanking(MyDatabase db, String userid, int gameid, String score) {
        SQLCreator sc = new SQLCreator(Ranking.TABLE_NAME, Ranking.CURRENT).setWhere(Game.ID + "=? and " + User.ID + "=?");

        try {
            db.setPrepareObjects(score, gameid, userid).exe(sc.update());
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return "ランキングの登録に失敗";
        }

        return null;
    }

    /**
     * update
     *
     * @param db
     * @param userid
     * @param gameid
     * @param score
     * @return
     */
    public static String updateRanking(MyDatabase db, String userid, int gameid, String score,
            boolean update) {
        SQLCreator sc = new SQLCreator(Ranking.TABLE_NAME, Ranking.SCORE, Ranking.CURRENT).setWhere(Game.ID + "=? and " + User.ID + "=?");

        try {
            db.setPrepareObjects(score, score, gameid, userid).exe(sc.update());
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return "ランキングの登録に失敗";
        }

        return null;
    }

    /**
     * ランク付けする
     *
     * @param db
     * @param gid
     * @return
     */
    public static String setRank(MyDatabase db, int gid) {
        try {
            db.exe("set @i:=0").setPrepareObjects(gid).exe("update vranking set rank=(@i:=(@i+1)) where gid=? order by score desc");
            return null;
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return "ランキングの整形に失敗しました。";
        }
    }

    public static ArrayList<HashMap<String, Object>> getTop10(MyDatabase db, int gid) {

        try {
            return db.setPrepareObjects(gid)//
            .exe("select r.uid,u.uname,u.uicon,r.score,r.rank from vranking as r,vuser as u where r.gid=? and r.uid = u.uid order by r.rank limit 10",//
            "[]", "uid", "uname", "uicon", "score", "rank").getList();

        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 送られてきたスコアが何位になるか
     *
     * @param db
     * @param gid
     * @param cScore
     * @return
     */
    public static int getCurrentRank(MyDatabase db, int gid, int cScore) {

        try {
            ArrayList<HashMap<String, Object>> list = db.setPrepareObjects(gid, cScore)//
            .exe("select count(*)+1 as rank from vranking where gid=? and score>?", "[]", "rank").getList();
            if (list == null || list.isEmpty()) {
                return 0;
            }
            return (int) (long) list.get(0).get("rank");
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return 0;
        }
    }

    public static String getScore(MyDatabase db, int gid, String uid) {
        try {
            ArrayList<HashMap<String, Object>> list = //
            db.setPrepareObjects(gid, uid).exe("select score from vranking where gid=? and uid=?", "[]", "score").getList();
            if (list == null || list.isEmpty()) {
                return null;
            }
            return (String) list.get(0).get("score");
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return null;
        }
    }

    public static int getCurrentScore(MyDatabase db, int gid, String uid) {

        try {
            ArrayList<HashMap<String, Object>> list = //
            db.setPrepareObjects(gid, uid).exe("select currentScore from vranking where gid=? and uid=?", "[]", "currentScore").getList();
            if (list == null || list.isEmpty()) {
                return -1;
            }
            return (int) list.get(0).get("currentScore");
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return -1;
        }

    }

    public static ArrayList<HashMap<String, Object>> getRanking(MyDatabase db, int gid, int start) {

        try {
            return db.setPrepareObjects(gid, start)//
            .exe("select r.uid,u.uname,u.uicon,r.score,r.rank from vranking as r,vuser as u where r.gid=? and r.uid = u.uid order by r.rank limit ?,9",//
            "[]", "uid", "uname", "uicon", "score", "rank").getList();
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isHighscore(MyDatabase db, String score, String current) throws SQLException {

        ArrayList<HashMap<String, Object>> list = //
        db.setPrepareObjects(score, current).exe("select uid from vuser where '" + score + "'<'" + current + "' limit 1", "[]", "uid").getList();
        System.out.println(list);
        return list != null && !list.isEmpty();

    }

    public static String setHighScore(MyDatabase db, int gid, String uid) {
        try {
            db.setPrepareObjects(gid, uid).exe("UPDATE vranking SET `score`=`currentScore` WHERE gid=? and uid=? and `score`<`currentScore`");
            return null;
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return "更新失敗";
        }
    }

    /**
     * 検索
     *
     * @param db
     * @param query
     * @return
     * @throws SQLException
     */
    public static ArrayList<HashMap<String, Object>> queryGame(MyDatabase db, String query) throws SQLException {
        char wild = '%';
        SQLCreator sc = new SQLCreator(Game.TABLE_NAME, Game.getFields()).addLike(Game.NAME);
        return db.setPrepareObjects(wild + Convert.escapeWildcard(query) + wild).exe(sc.select(), "[]", Game.getFields()).getList();

    }

    /**
     * 検索
     *
     * @param db
     * @param query
     * @return
     * @throws SQLException
     */
    public static ArrayList<HashMap<String, Object>> queryGame(MyDatabase db, String query,
            int genreid) throws SQLException {
        char wild = '%';
        SQLCreator sc = new SQLCreator(Game.TABLE_NAME, Game.getFields()).setWhere("genreid=? and ").addLike(Game.NAME);
        return db.setPrepareObjects(genreid, wild + Convert.escapeWildcard(query) + wild).exe(sc.select(), "[]", Game.getFields()).getList();

    }

    /**
     * データをロード
     *
     * @param db
     * @param gid
     * @param uid
     * @return
     * @throws SQLException
     */
    public static String loadData(MyDatabase db, int gid, String uid) throws SQLException {

        ArrayList<HashMap<String, Object>> list = //
        db.setPrepareObjects(gid, uid).exe("select data from vsavedata where gid=? and uid=?", "[]", "data").getList();
        if (list == null || list.isEmpty())
            return null;
        return (String) list.get(0).get("data");

    }

    /**
     * データをセーブ
     *
     * @param db
     * @param gid
     * @param uid
     * @param data
     * @throws SQLException
     */
    public static void saveData(MyDatabase db, int gid, String uid, String data) throws SQLException {

        String list = loadData(db, gid, uid);
        if (list == null) {

            SQLCreator sc = new SQLCreator(Save.TABLE_NAME, Save.getFields());
            db.setPrepareObjects(gid, uid, data).exe(sc.insert());
        } else {

            SQLCreator sc = new SQLCreator(Save.TABLE_NAME, Save.DATA).setWhere(Game.ID + "=? and " + User.ID + "=?");
            db.setPrepareObjects(data, gid, uid).exe(sc.update());
        }

    }

    /**
     * お気に入りに登録
     *
     * @param db
     * @param gid
     * @param uid
     * @throws SQLException
     */
    public static void fav(MyDatabase db, int gid, String uid) throws SQLException {
        SQLCreator sc = new SQLCreator(MyList.TABLE_NAME, MyList.getFields());
        db.setPrepareObjects(gid, uid).exe(sc.insert());
    }

    /**
     * お気に入りをしているか
     *
     * @param db
     * @param gid
     * @param uid
     * @return
     */
    public static boolean isFav(MyDatabase db, int gid, String uid) {
        SQLCreator sc = new SQLCreator(MyList.TABLE_NAME, MyList.getFields()).setWhere(MyList.ID + "=? and " + MyList.UID + "=?");
        try {
            ArrayList<HashMap<String, Object>> list = db.setPrepareObjects(gid, uid).exe(sc.select(), "[]", MyList.getFields()).getList();

            return list != null && !list.isEmpty();
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return false;
        }
    }

    /**
     * お気に入りのリストを取得
     *
     * @param db
     * @param gid
     * @return
     * @throws SQLException
     */
    public static ArrayList<HashMap<String, Object>> getFavList(MyDatabase db, String uid) throws SQLException {

        return //
        db//
        .setPrepareObjects(uid)//
        .exe("select g.gid as gid,g.gname as gname,g.gimage as gimage,g.gplay as gplay,g.gfav as gfav,g.gsetumei as gsetumei from vmylist ,vgame as g where vmylist.gid=g.gid and vmylist.uid=?", "[]", "gid", "gname", "gimage", "gplay", "gfav", "gsetumei")//
        .getList();

    }

    /**
     *
     * @param db
     * @param uid
     * @return
     */
    public static User getUser(MyDatabase db, String uid) {
        User user = new User();

        try {
            ArrayList<HashMap<String, Object>> list = //
            db.setPrepareObjects(uid)//
            .exe(new SQLCreator(User.TABLE_NAME, User.getFields())//
            .setWhere(User.ID + "=?").select(), "[]", User.getFields())//
            .getList();

            if (list == null || list.isEmpty()) {
                return null;
            }
            user.setAll(list);
            return user;
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return null;
        }
    }
}

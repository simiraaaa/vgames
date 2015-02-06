package vgames.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import myclass.database.MyDatabase;
import myclass.database.SQLCreator;
import myclass.util.Compare;
import vgames.table.Game;
import vgames.table.Genre;
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
}

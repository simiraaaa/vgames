package vgames.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import myclass.database.MyDatabase;
import myclass.database.SQLCreator;
import myclass.util.Compare;
import vgames.table.Game;

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

    /**
     * 既存ゲームのIMGソースpath
     *
     * @param db
     * @param gameid
     * @return
     */
    public static String getImgTitle(MyDatabase db, int gameid) {
        SQLCreator sc = new SQLCreator(Game.TABLE_NAME, Game.IMG).setWhere(Game.ID + "=?");

        try {
            ArrayList<HashMap<String, Object>> list = //
            db//
            .setPrepareObjects(gameid)//
            .exe(sc.select(), "[]", Game.IMG).getList();
            return (String) list.get(0).get(Game.IMG);
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }

        return null;
    }
}

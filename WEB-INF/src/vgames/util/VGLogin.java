package vgames.util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.database.MyDatabase;
import myclass.database.SQLCreator;
import myclass.servlet.MyCookie;
import myclass.servlet.Path;
import myclass.util.Compare;
import vgames.table.Session;
import vgames.table.User;

public class VGLogin {
    public static String getSessionKey(HttpServletRequest req, HttpServletResponse res) {
        MyCookie mk = new MyCookie(req, res);
        Map<String, String> map = mk.getCookies();
        if (map == null || map.size() == 0) {
            return null;
        }
        return map.get(Path.VGSESSIONID);
    }

    public static String getId(HttpServletRequest req, HttpServletResponse res, MyDatabase db,
            boolean noClose) {
        String sid = getSessionKey(req, res);
        if (Compare.isEmpty(sid)) {
            return null;
        }
        SQLCreator sc = new SQLCreator(Session.TABLE_NAME, User.ID);
        sc.setWhere(Session.ID + "=?");
        ArrayList<HashMap<String, Object>> list = null;
        try {
            list = db.setPrepareObjects(sid).exe(sc.select(), "[]", User.ID).getList();
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } finally {
            if (!noClose) {
                db.close();
            }
        }
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0).get(User.ID).toString();
    }

    public static String getId(HttpServletRequest req, HttpServletResponse res) {
        return getId(req, res, ConnectionVG.open(req), false);
    }

    public static ArrayList<HashMap<String, Object>> getUserTable(HttpServletRequest req,
            HttpServletResponse res, MyDatabase db, boolean noClose) {
        String uidString = getId(req, res, db, true);
        if (Compare.isEmpty(uidString)) {
            return null;
        }
        ArrayList<HashMap<String, Object>> list = null;
        SQLCreator sc = new SQLCreator(User.TABLE_NAME, User.getFields()).setWhere(User.ID + "=?");
        try {
            list = //
            db//
            .setPrepareObjects(uidString)//
            .exe(sc.select(), "[]", User.getFields())//
            .getList();
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } finally {
            if (!noClose) {
                db.close();
            }
        }

        return list;

    }

    /**
     * ログインできていればUserに値がセットされている
     *
     * @param req
     * @param res
     * @param user
     * @return
     */
    public static MyDatabase checkLogin(HttpServletRequest req, HttpServletResponse res, User user,
            String url, boolean redirect) {
        MyDatabase db = ConnectionVG.open(req);
        ArrayList<HashMap<String, Object>> list = getUserTable(req, res, db, true);
        if (list == null || list.size() == 0) {
            try {
                if (!redirect)
                    res.sendRedirect(url);
            } catch (IOException e) {
                // TODO 自動生成された catch ブロック
                e.printStackTrace();
            }
            return db;
        }
        if (redirect && url != null) {
            try {
                res.sendRedirect(url);
            } catch (IOException e) {
                // TODO 自動生成された catch ブロック
                e.printStackTrace();
            }
        }
        user.setAll(list);
        return db;
    }

    public static MyDatabase checkLogin(HttpServletRequest req, HttpServletResponse res, User user) {
        return checkLogin(req, res, user, req.getContextPath() + "/login.jsp", false);
    }

    public static MyDatabase checkLogin(HttpServletRequest req, HttpServletResponse res, User user,
            boolean b) {
        return checkLogin(req, res, user, null, true);
    }

    public static void logout(HttpServletRequest req, HttpServletResponse res) {
        MyCookie mk = new MyCookie(req, res);
        mk.setCookie(Path.VGSESSIONID, "").addCookie(0);
    }

    /**
     * ここは飽くまでidとパスワードの確認とクッキーの登録
     *
     * @param req
     * @param res
     * @return
     */
    public static boolean login(HttpServletRequest req, HttpServletResponse res) {
        String id = (String) req.getParameter("id");
        String pass = (String) req.getParameter("pass");
        boolean suc = true;
        if (Compare.isAnyEmpty(id, pass)) {
            return false;
        }
        MyDatabase db = ConnectionVG.open(req);
        SQLCreator sc = new SQLCreator(User.TABLE_NAME, User.ID);
        sc.setWhere(User.ID + "=? AND " + User.PASS + "=?");
        ArrayList<HashMap<String, Object>> list = null;
        try {

            list = db.setPrepareObjects(id, pass).exe(sc.select(), "[]", User.ID).getList();

            if (list == null || list.size() == 0) {
                db.close();
                return false;
            }

            sc.setTable(Session.TABLE_NAME)//
            .setColumns(Session.ID)//
            .setWhere("")//
            .addOrderBy(Session.ID + " desc")//
            .addLimit(1);
            db.exe(sc.select(), "[]", Session.ID);

            list = db.getList();
            long sid = 0;
            if (list != null && list.size() > 0) {
                sid = 1 + Long.parseLong(((String) list.get(0).get(Session.ID)).substring(0, 19));
            }
            final String SESSION_ID = CreateSessionKey.create(sid);
            db.setPrepareObjects(SESSION_ID, id).exe(sc.setColumns(Session.getFields()).insert());

            new MyCookie(req, res).setCookie(Path.VGSESSIONID, SESSION_ID).addCookie();

        } catch (SQLException e) {
            suc = false;
            e.printStackTrace();
        } finally {
            db.close();

        }

        return suc;

    }
}
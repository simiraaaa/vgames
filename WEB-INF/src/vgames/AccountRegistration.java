package vgames;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.database.MyDatabase;
import myclass.database.SQLCreator;
import myclass.servlet.ExtendedHttpServlet;
import myclass.servlet.MyCookie;
import myclass.servlet.Path;
import myclass.util.Compare;
import myclass.util.JSON;
import myclass.wrap.MyHashMap;
import vgames.table.Session;
import vgames.table.User;
import vgames.util.ConnectionVG;
import vgames.util.CreateSessionKey;

public class AccountRegistration extends ExtendedHttpServlet {

    @Override
    protected void subAction(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // TODO 自動生成されたメソッド・スタブ
        final String//
        ID = getParam("id"), //
        PASS = getParam("pass"), //
        CHECK = getParam("check"), //
        NAME = getParam("name");

        String responseMessage = null;
        String responseStatus = "success";
        HashMap<String, String> json = new HashMap<String, String>();
        if (Compare.isAnyEmpty(ID, PASS, CHECK)) {
            responseMessage = "error!!入力が必須の項目で、未入力の項目があります。";
            sendAlert(responseMessage, json);
            return;
        }
        if (!PASS.equals(CHECK)) {
            responseMessage = "error!!パスワードの確認が間違っています。";
            sendAlert(responseMessage, json);
            return;
        }
        if (Compare.isAnyOverLength(100, ID, PASS, CHECK, NAME)) {
            sendAlert("error!!いずれかの項目が100文字を超えています。", json);
            return;
        }
        final String REGEXP = "\\w+";
        if (!Compare.isAllMatches(REGEXP, ID, PASS, CHECK)) {
            sendAlert("error!!表示名を除く、いずれかの項目が半角英数字以外で入力されています。", json);
            return;
        }

        boolean isLocal = Path.isLocal(req);
        try {
            MyDatabase db = new MyDatabase(ConnectionVG.open(isLocal));

            SQLCreator sc = new SQLCreator(User.TABLE_NAME, User.ID);
            sc.setWhere(User.ID + "=?");
            db.setPrepareObjects(ID).exe(sc.select(), "[]", User.ID);
            ArrayList<HashMap<String, Object>> list = db.getList();

            if (list != null && list.size() > 0) {
                responseMessage = "IDが重複しました。別のIDで試してみてください。";
                sendAlert(responseMessage, json);
                return;
            }
            responseMessage = "error!!登録に失敗しました。";
            sc.setColumns(User.getFields());
            db.setPrepareObjects(ID, PASS, (Compare.isEmpty(NAME) ? ID : NAME), "default.png", "プロフィールは未記入です");
            db.exe(sc.insert());
            responseMessage = "error!!ログインに失敗しました。";
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
            responseMessage = "error!!ログインに失敗しました。";
            db.setPrepareObjects(SESSION_ID, ID).exe(sc.setColumns(Session.getFields()).insert());

            MyCookie mk = new MyCookie(req, res);
            mk.setCookie(Path.VGSESSIONID, SESSION_ID).addCookie();
            json.put("status", responseStatus);
            json.put("text", req.getContextPath() + "/" + Path.MYPAGEJSP);
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            sendAlert(responseMessage, json);
            e.printStackTrace();
        }
        sendJSON(JSON.stringify(json));
    }

    private void sendJSON(String json) {

        response.setContentType("application/json");
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }

    private void sendAlert(String message, HashMap<String, String> json) {
        MyHashMap.puts(json,//
                "status", "alert",//
                "text", message);
        sendJSON(JSON.stringify(json));
    }

}

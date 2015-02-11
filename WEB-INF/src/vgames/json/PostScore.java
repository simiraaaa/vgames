package vgames.json;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.database.MyDatabase;
import myclass.servlet.AjaxResponse;
import vgames.table.User;
import vgames.util.VGLogin;

public class PostScore extends AjaxResponse {
    private static String//
            STATUS = "status",
            SUCCESS = "success", ALERT = "alert", TEXT = "text";

    private MyDatabase db = null;
    private int gameid = -1;
    private String userid = null;

    @Override
    protected void ajax(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // TODO 自動生成されたメソッド・スタブ
        gameid = getIntParam("gameid");

        User user = new User();
        db = VGLogin.checkLogin(req, res, user, true);

        if ((userid = user.getId()) == null) {
            puts(STATUS, ALERT, TEXT, "ログインしていません");
        }

        String err = run();
        if (err != null) {
            puts(STATUS, ALERT, TEXT, err);
        }

        db.close();

        send();
    }

    private String run() {

        return null;
    }

}

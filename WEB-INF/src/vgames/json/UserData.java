package vgames.json;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.servlet.AjaxResponse;
import myclass.util.Convert;
import myclass.wrap.MyHashMap;
import vgames.table.User;
import vgames.util.VGLogin;

public class UserData extends AjaxResponse {

    @Override
    protected void ajax(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // TODO 自動生成されたメソッド・スタブ
        User user = new User();
        VGLogin.checkLogin(req, res, user, true).close();
        if (user.getId() == null) {
            puts("status", "none");
        } else {
            puts("status", "success",//
                    "user", MyHashMap.create("id", user.getId(),//
                            "name", Convert.escapeHtml(user.getName()),//
                            "icon", user.getIcon(),//
                            "prof", user.getProf()));
        }
        send();
    }

}

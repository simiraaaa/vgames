package vgames;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.servlet.AjaxResponse;
import vgames.util.VGLogin;

public class Logout extends AjaxResponse {

    @Override
    protected void ajax(HttpServletRequest req, HttpServletResponse res) {
        // TODO 自動生成されたメソッド・スタブ
        VGLogin.logout(req, res);
        put("url", req.getContextPath());
        send();
    }

}

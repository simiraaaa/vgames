package vgames;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.servlet.ExtendedHttpServlet;
import myclass.servlet.Path;
import myclass.util.JSON;
import myclass.wrap.MyHashMap;
import vgames.util.VGLogin;

public class LoginOnly extends ExtendedHttpServlet {

    @Override
    protected void subAction(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // TODO 自動生成されたメソッド・スタブ
        res.setContentType("application/json");
        String status = "success";
        String text = req.getContextPath() + "/" + Path.MYPAGEJSP;
        if (!VGLogin.login(req, res)) {
            status = "error";
            text = "IDかパスワードが間違っています。";
        }
        res.getWriter().print(JSON.stringify(MyHashMap.create("status", status, "text", text)));

    }

}

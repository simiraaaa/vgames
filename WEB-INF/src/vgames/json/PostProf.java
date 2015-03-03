package vgames.json;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.database.MyDatabase;
import myclass.database.SQLCreator;
import myclass.servlet.AjaxResponse;
import myclass.util.Compare;

import org.apache.commons.fileupload.FileItem;

import vgames.table.User;
import vgames.util.Uploader;
import vgames.util.VGLogin;

public class PostProf extends AjaxResponse {
    final String STATUS = "status", TEXT = "text", ALERT = "alert", SUCCESS = "success";
    MyDatabase db = null;
    User user = null;

    @Override
    protected void ajax(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // TODO 自動生成されたメソッド・スタブ

        try {
            if (!isMultipart) {
                puts(STATUS, ALERT, TEXT, "不正な通信です");
                return;
            }
            if (isGet) {
                puts(STATUS, ALERT, TEXT, "不正な通信です");
                return;
            }
            FileItem uicon = getFileItem("uicon");
            String name = getFormParam("name");
            String prof = getFormParam("prof");
            user = new User();
            db = VGLogin.checkLogin(req, res, user, true);
            if (user.getId() == null) {
                puts(STATUS, ALERT, TEXT, "ログインしてください");
                return;
            }
            String[] fields = null;
            Object[] values = null;
            if (Compare.isEmpty(uicon.getName())) {
                fields = new String[] { User.NAME, User.PROF };
                values = new Object[] { name, prof, user.getId() };
            } else {
                fields = new String[] { User.NAME, User.PROF, User.ICON };
                String iname = Uploader.upImage(uicon, user.getId(), servlet.getServletContext().getRealPath("/user"));
                if (iname.lastIndexOf(".") == -1) {
                    puts(STATUS, ALERT, TEXT, "画像の投稿に失敗しました\n" + iname);
                    return;
                }
                values = new Object[] { name, prof, iname, user.getId() };
            }
            SQLCreator sc = new SQLCreator(User.TABLE_NAME, fields).setWhere(User.ID + "=?");

            db.setPrepareObjects(values).exe(sc.update());

            puts(STATUS, SUCCESS, TEXT, "成功しました");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            puts(STATUS, ALERT, TEXT, "エラーが発生しました");
            return;
        } finally {
            if (db != null)
                db.close();
            send();
        }
    }
}

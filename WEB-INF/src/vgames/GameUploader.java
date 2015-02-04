package vgames;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.database.MyDatabase;
import myclass.servlet.AjaxResponse;
import myclass.servlet.Path;
import myclass.util.Compare;

import org.apache.commons.fileupload.FileItem;

import vgames.table.User;
import vgames.util.Uploader;
import vgames.util.VGDataBase;
import vgames.util.VGLogin;

public class GameUploader extends AjaxResponse {

    private static final String STATUS = "status", ALERT = "alert", TEXT = "text",
            SUCCESS = "success";
    private MyDatabase db;
    private User user;
    private int gameid = 0;
    private String GAME_PATH = null;

    @Override
    protected void ajax(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // TODO 自動生成されたメソッド・スタブ
        user = new User();

        db = VGLogin.checkLogin(req, res, user, true);
        GAME_PATH = servlet.getServletContext().getRealPath("/games");
        String err = tryReturn();
        if (err != null) {
            sendError(err);
        }
        db.close();

    }

    private String tryReturn() {

        if (user.getId() == null) {
            return "ログインしてください";
        }
        FileItem game = getFileItem("game"), img = getFileItem("ss");
        //@formatter:off
        String
        _gameid=getFormParam("gameid"),
        title = setEmpty(getFormParam("title")),
        setumei = setEmpty(getFormParam("setumei")),
        genre = getFormParam("genre");
        // @formatter:on
        int genreid = Integer.parseInt(genre);

        if (game == null || game.getName().isEmpty()) {
            return "ゲームファイルを指定してください";
        }
        boolean isNewGame = Compare.isEmpty(_gameid);

        if (isNewGame)
            gameid = VGDataBase.getMaxGameIDaddOne(db);

        if (gameid == -1) {
            return "gameidの取得に失敗しました";
        }

        String imgName = setImageName(img);

        if (imgName.lastIndexOf(".") != -1) {
            imgName = "defaultSS.png";
        }

        if (!isNewGame && imgName.equals("defaultSS.png") && (imgName = VGDataBase.getImgTitle(db, gameid)) == null) {
            return "画像名の取得に失敗しました。";
        }

        String zipe = Uploader.upGame(game, genreid, imgName);
        if (zipe != null) {
            return zipe;
        }
        zipe = VGDataBase.insertGame(db, genreid, user.getId(), genreid, title, imgName, setumei);
        if (zipe != null) {
            return zipe;
        }
        puts(STATUS, SUCCESS, "url", request.getContextPath() + "/s/" + Path.MYPAGEHTML);
        send();
        return null;
    }

    private String setImageName(FileItem img) {
        if (img == null || img.getName().isEmpty()) {
            return "defaultSS.png";
        }
        return Uploader.upImage(img, gameid, GAME_PATH);
    }

    private void sendError(String msg) {
        puts(STATUS, ALERT, TEXT, msg);
        send();
    }

    private static String setEmpty(String s) {
        return Compare.isEmpty(s) ? "未入力" : s;
    }

}

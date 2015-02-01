package vgames;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.file.ZipDecoder;
import myclass.servlet.AjaxResponse;

import org.apache.commons.fileupload.FileItem;

public class GameUploader extends AjaxResponse {

    @Override
    protected void ajax(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // TODO 自動生成されたメソッド・スタブ
        FileItem game = getFileItem("game");
        String GAME_PATH = servlet.getServletContext().getRealPath("/games");
        System.out.println("game:" + game);
        System.out.println("pt:" + GAME_PATH);
        if (!writeFile(game, GAME_PATH + "/100.zip")) {
            puts("status", "alert", "text", "ファイルアップミスった");
            send();
            return;
        }
        ZipDecoder.unzip(GAME_PATH + "/100.zip");
        puts("status", "alert", "text", "成功！");
        send();

    }
}

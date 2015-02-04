package vgames.util;

import java.io.File;
import java.io.IOException;

import myclass.file.ZipDecoder;
import myclass.util.Compare;

import org.apache.commons.fileupload.FileItem;

public class Uploader {
    private static final String JPG = ".jpg", JPEG = ".jpeg", PNG = ".png", GIF = ".gif";

    public static String upGame(FileItem gameFile, int gameid, String GAME_PATH) {
        String FILENAME = "/" + gameid;
        String FILENAMEZIP = FILENAME + ".zip";
        try {
            gameFile.write(new File(GAME_PATH + FILENAMEZIP));
        } catch (Exception e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return "zipファイルのアップロードに失敗しました。";
        }

        try {
            ZipDecoder.unzip(GAME_PATH + FILENAMEZIP, GAME_PATH, FILENAME);
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return "zipファイルの展開に失敗しました。\nファイル形式が間違っている可能性があります。";
        }
        return null;
    }

    public static String upImage(FileItem imgFile, int gameid, String IMAGE_PATH) {
        String type = checkImageType(imgFile.getName());
        if (type == null) {
            return "不正な拡張子です。";
        }
        String FILENAME = "/" + gameid + type;

        try {
            imgFile.write(new File(IMAGE_PATH + FILENAME));
        } catch (Exception e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return "スクリーンショットのアップロードに失敗しました。";
        }

        return FILENAME.substring(1);
    }

    public static String checkImageType(String fileName) {
        String name = fileName.substring(fileName.indexOf(".")).toLowerCase();
        if (Compare.isAnyEquals(name, JPEG, PNG, JPG, GIF)) {
            return name;
        }

        return null;
    }
}

package myclass.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.wrap.MyHashMap;

/**
 * PathMappingのloadをオーバーライドして使う
 *
 * @author yuki
 *
 */
public class Path implements PathMapping {

    private static HashMap<String, Class<ServletWrapper>> urlMap = null;

    public Path() throws ClassNotFoundException {

        HashMap<String, String> tempurl = new HashMap<>();
        urlMap = new HashMap<>();
        // TODO 自動生成されたコンストラクター・スタブ
        MyHashMap.puts(tempurl, //
                LOGINJSON, VGAMES + LOGIN,//
                LOGOUTJSON, VGAMES + LOGOUT,//
                ACREG, VGAMES + ACCOUNT_REGISTRATION,//
                MYPAGEHTML, VHTML + MYPAGEJAVA,//
                UPLOAD, VGAMES + GAME_UPLOADER,//
                UPLOADHTML, VGAMES + UPLOAD_PAGE,//
                "userdata.json", VGAMES + "json.UserData",//
                JSONGETTERJSON, VGAMES + JSONGETTER,//
                EDITHTML, VGAMES + EDIT_PAGE,//
                "ranking.json", VGAMES + "json.UserRanking",//
                RANKINGHTML, VGAMES + "html.RankingPage",//
                PLAYHTML, VGAMES + "html.PlayPage",//
                EDITPROFHTML, VGAMES + "html.EditProf",//
                "prof.json", VGAMES + "json.PostProf",//
                "test.html", VGAMES + "Test");

        tempurl.forEach((k, v) -> {
            try {
                urlMap.put(k, (Class<ServletWrapper>) Class.forName(v));
            } catch (Exception e) {
                // TODO 自動生成された catch ブロック
                e.printStackTrace();
            }
        });

    }

    public static void redirectLogin(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.sendRedirect(req.getContextPath() + "/login.jsp");
    }

    @Override
    public ServletWrapper load(String pathInfo) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String classname = pathInfo.substring(1);
        if (urlMap.containsKey(classname)) {
            return urlMap.get(classname).newInstance();
        } else {
            throw new ClassNotFoundException();
        }
    }

    public static final String//
            CONTEXT_PATH = "/vgames",
            GAMES = "/games",//
            VGAMES = "vgames.",//
            VHTML = VGAMES + "html.",//
            JS = ".js",// ,
            JSP = ".jsp",//
            HTML = ".html",//
            JSON = ".json",//
            JS_PATH = "js/",//
            SERVLET = "s/",//
            UPLOADHTML = "upload" + HTML,//
            UPLOAD_PAGE = "html.UploadPage",//
            JSONGETTER = "json.JsonGetter", //
            JSONGETTERJSON = "get.json",//
            EDIT_PAGE = "html.EditPage",//
            EDITHTML = "edit" + HTML,//
            RANKINGHTML = "ranking" + HTML,//
            PLAYHTML = "play" + HTML,//
            EDITPROFHTML = "editProf" + HTML,//

            UPLOAD = "gameup" + JSON,//
            GAME_UPLOADER = "GameUploader",//
            INDEX = "index" + JSP,//
            LOGOUT = "Logout",//
            LOGOUTJSON = "logout" + JSON,//
            LOGOUTJS = "logout" + JS,//
            CONTROLLERJS = "controller" + JS,//
            MYPAGE = "mypage",//
            MYPAGEJAVA = "Mypage",//
            MYPAGEJS = MYPAGE + JS,//
            MYPAGEHTML = MYPAGE + HTML,//
            VGSESSIONID = "VGSESSIONID",//
            ACCOUNT_REGISTRATION = "AccountRegistration",//
            ACREG = "acreg" + JSON,//
            LOGIN = "LoginOnly",//
            LOGINJSON = "login" + JSON,//
            CREATE_ACCOUNT = "createAccount",//
            CREATE_ACCOUNTJSP = CREATE_ACCOUNT + JSP, //
            CREATE_ACCOUNTJS = CREATE_ACCOUNT + JS,//
            SMR = "smr", DOM = ".dom", SMRJS = SMR + JS,//
            DOMJS = SMR + DOM + JS,//
            ICONJS = SMR + DOM + ".icon" + JS,//
            UTIL = ".util",//
            UTILJS = SMR + UTIL + JS,//
            AJAX = ".ajax",//
            AJAXJS = SMR + AJAX + JS,//
            MAINJS = "main" + JS,//
            VGJS = "vgames" + JS

            ;//

    private static final String[] JSLIBS = { SMRJS, DOMJS, ICONJS, UTILJS, AJAXJS, LOGOUTJS, VGJS, CONTROLLERJS };

    /**
     * JSフォルダにあるライブラリ名をおおよそ取得
     *
     * @return
     */
    public static String[] getJsLibs() {
        return JSLIBS;
    }

    /**
     * ServerNameにlocalhostが含まれるか<br>
     * 192.168.121.21が含まれないか
     *
     * @param req
     * @return
     */
    public static boolean isLocal(HttpServletRequest req) {
        return req.getServerName().indexOf("localhost") != -1 || req.getServerName().indexOf("192.168.121.21") == -1;
    }

    /**
     * ゲームのデプロイされているパス
     *
     * @param gameid
     * @return
     */
    public static String createGamePath(int gameid) {
        return CONTEXT_PATH + GAMES + "/" + gameid;
    }
}

package myclass.servlet;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import myclass.wrap.MyHashMap;

/**
 * PathMappingのloadをオーバーライドして使う
 *
 * @author yuki
 *
 */
public class Path implements PathMapping {

    private static HashMap<String, String> urlMap = null;

    public Path() {
        // TODO 自動生成されたコンストラクター・スタブ
        MyHashMap.puts(urlMap, //
                LOGINJSON, VGAMES + LOGIN,//
                LOGOUTJSON, VGAMES + LOGOUTJSON,//
                ACREG, VGAMES + ACCOUNT_REGISTRATION,//
                "test.html", VGAMES + "Test");
    }

    public static final String//
            VGAMES = "vgames.",
            JS = ".js",// ,
            JSP = ".jsp",//
            HTML = ".html",//
            JSON = ".json",//
            JS_PATH = "js/",//
            SERVLET = "s/",//
            INDEX = "index" + JSP,//
            LOGOUT = "Logout",//
            LOGOUTJSON = "logout" + JSON,//
            LOGOUTJS = "logout" + JS,//
            CONTROLLERJS = "controller" + JS,//
            MYPAGE = "mypage",//
            MYPAGEJS = MYPAGE + JS,//
            MYPAGEJSP = MYPAGE + JSP,//
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
            MAINJS = "main" + JS;//

    private static final String[] JSLIBS = { SMRJS, DOMJS, ICONJS, UTILJS, AJAXJS };

    /**
     * JSフォルダにあるライブラリ名をおおよそ取得
     *
     * @return
     */
    public static String[] getJsLibs() {
        return JSLIBS;
    }

    /**
     * ServerNameにlocalhostが含まれるか
     *
     * @param req
     * @return
     */
    public static boolean isLocal(HttpServletRequest req) {
        return req.getServerName().indexOf("localhost") != -1;
    }
}

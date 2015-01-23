package myclass.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.util.JSON;
import myclass.wrap.MyHashMap;

public abstract class MyAjax extends MyServlet {
    protected static final String POST = "POST", GET = "GET";
    protected boolean isPOST, isJSONP;
    private String callback = null;
    private HashMap<String, Object> map = null;

    @Override
    protected void action(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        isPOST = methodType == POST;
        callback = getParam("callback");
        isJSONP = callback!=null;
        if (isJSONP) {
            res.setContentType("text/javascript");
        } else {
            res.setContentType("application/json");
        }
        map = new HashMap<String, Object>();
        ajax(req, res);
    }

    protected abstract void ajax(HttpServletRequest req, HttpServletResponse res);

    /**
     * keyを追加する
     * 
     * @param key
     * @param value
     */
    protected void put(String key, Object value) {
        map.put(key, value);
    }

    /**
     * 複数追加
     * 
     * @param kv
     */
    protected void puts(Object... kv) {
        MyHashMap.puts(map, kv);
    }

    /**
     * JSON文字列をJSON.parseして追加
     * 
     * @param key
     * @param json
     */
    protected void putJson(String key, String json) {
        map.put(key, JSON.parse(json));
    }

    /**
     * 複数JSON文字列をJSON.parseして追加
     */
    protected void putsJson(String... kv) {
        for (int i = 0, len = kv.length; i < len; ++i) {
            map.put(kv[i], JSON.parse(kv[++i]));
        }
    }

    /**
     * ブラウザに送信する
     */
    protected void send() {

        String json = isJSONP ? callback + "(" + JSON.stringify(map) + ");" : JSON.stringify(map);
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }

    }
}

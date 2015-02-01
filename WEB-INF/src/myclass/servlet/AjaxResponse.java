package myclass.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.util.JSON;
import myclass.wrap.MyHashMap;

public abstract class AjaxResponse extends ExtendedHttpServlet {

    protected static final String POST = "POST", GET = "GET";
    protected boolean isPOST, isJSONP;
    private String callback = null;
    private HashMap<String, Object> map = null;

    protected void subAction(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        isPOST = POST.equals(req.getMethod());
        callback = getParam("callback");
        isJSONP = callback != null;
        if (isJSONP) {
            res.setContentType("text/javascript");
        } else {
            res.setContentType("application/json");
        }
        map = new HashMap<String, Object>();
        ajax(req, res);
    }

    abstract protected void ajax(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException;

    /**
     * keyを追加する
     *
     * @param key
     * @param value
     */
    protected AjaxResponse put(String key, Object value) {
        map.put(key, value);
        return this;
    }

    /**
     * 複数追加
     *
     * @param kv
     */
    protected AjaxResponse puts(Object... kv) {
        MyHashMap.puts(map, kv);
        return this;
    }

    /**
     * JSON文字列をJSON.parseして追加
     *
     * @param key
     * @param json
     */
    protected AjaxResponse putJson(String key, String json) {
        map.put(key, JSON.parse(json));
        return this;
    }

    /**
     * 複数JSON文字列をJSON.parseして追加
     */
    protected AjaxResponse putsJson(String... kv) {
        for (int i = 0, len = kv.length; i < len; ++i) {
            map.put(kv[i], JSON.parse(kv[++i]));
        }
        return this;
    }

    /**
     * ブラウザに送信する
     */
    protected AjaxResponse send() {

        String json = isJSONP ? callback + "(" + JSON.stringify(map) + ");" : JSON.stringify(map);
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        return this;

    }
}

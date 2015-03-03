package vgames.table;

import java.util.ArrayList;
import java.util.HashMap;

import myclass.util.Convert;

public class User {

    public static final String TABLE_NAME = "vuser", ID = "uid", PASS = "upass", NAME = "uname",
            ICON = "uicon", PROF = "upro";

    private static final String[] FIELDS = { ID, PASS, NAME, ICON, PROF };

    public static String[] getFields() {
        return FIELDS;
    }

    private String id, pass, name, icon, plainProf, prof;

    public String getIcon() {
        return icon;
    }

    public User() {
        // TODO 自動生成されたコンストラクター・スタブ
    }

    public User(ArrayList<HashMap<String, Object>> info) {
        setAll(info);
    }

    public String getPlainProf() {
        return plainProf;
    }

    public User setAll(String id, String name, String pass, String icon, String prof) {
        this.id = id;
        this.pass = pass;
        this.name = name;
        this.icon = icon;
        this.prof = prof;
        return this;
    }

    public User setAll(ArrayList<HashMap<String, Object>> info) {
        HashMap<String, Object> map = info.get(0);
        id = (String) map.get(ID);
        pass = (String) map.get(PASS);
        name = (String) map.get(NAME);
        icon = (String) map.get(ICON);
        plainProf = (String) map.get(PROF);
        prof = new Convert().set(plainProf).escapeHtml().escapeTextarea().get();

        return this;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public String getProf() {
        return prof;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

}

package vgames.table;

public class User {

    public static final String TABLE_NAME = "vuser", ID = "uid", PASS = "upass", NAME = "uname",
            ICON = "uicon", PROF = "upro";

    private static final String[] FIELDS = { ID, PASS, NAME, ICON, PROF };

    public static String[] getFields() {
        return FIELDS;
    }

}

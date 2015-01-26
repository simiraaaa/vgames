package vgames.table;

public class Session {
    public static final String TABLE_NAME = "vsession", ID = "sid", UID = "uid";

    private static final String[] FIELDS = { ID, UID };

    public static String[] getFields() {
        return FIELDS;
    }
}

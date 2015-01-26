package vgames.table;

public class Save {
    public static final String TABLE_NAME = "vsavedata", ID = "gid", UID = "uid", DATA = "data";

    private static final String[] FIELDS = { ID, UID, DATA };

    public static String[] getFields() {
        return FIELDS;
    }
}

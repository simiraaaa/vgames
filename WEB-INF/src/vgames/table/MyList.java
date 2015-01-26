package vgames.table;

public class MyList {
    public static final String TABLE_NAME = "vmylist", ID = "gid", UID = "uid";

    private static final String[] FIELDS = { ID, UID };

    public static String[] getFields() {
        return FIELDS;
    }
}

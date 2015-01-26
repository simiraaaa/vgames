package vgames.table;

public class Ranking {
    public static final String TABLE_NAME = "vranking", ID = "gid", UID = "uid", SCORE = "score";

    private static final String[] FIELDS = { ID, UID, SCORE };

    public static String[] getFields() {
        return FIELDS;
    }
}

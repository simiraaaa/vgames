package vgames.table;

public class Ranking {
    public static final String TABLE_NAME = "vranking", ID = "gid", UID = "uid", SCORE = "score",
            CURRENT = "currentScore";

    private static final String[] FIELDS = { ID, UID, SCORE, CURRENT };

    public static String[] getFields() {
        return FIELDS;
    }
}

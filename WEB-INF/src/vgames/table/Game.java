package vgames.table;

public class Game {

    public static final String TABLE_NAME = "vgame", ID = "gid", UID = "uid", NAME = "gname",
            IMG = "gimage", PLAY = "gplay", FAV = "gfav", SETUMEI = "gsetumei";

    private static final String[] FIELDS = { ID, UID, NAME, IMG, PLAY, FAV, SETUMEI };

    public static String[] getFields() {
        return FIELDS;
    }
}

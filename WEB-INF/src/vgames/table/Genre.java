package vgames.table;

public class Genre {
    public static final String TABLE_NAME = "vgenre", ID = "genreid", NAME = "genrename";

    private static final String[] FIELDS = { ID, NAME };

    public static String[] getFields() {
        return FIELDS;
    }
}

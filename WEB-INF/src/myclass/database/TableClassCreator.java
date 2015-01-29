package myclass.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class TableClassCreator {
    private final static String//
            TABLE_NAME = "TABLE_NAME",
            PUBLIC = "public", SPACE = " ", STRING = "String", INT = "int",
            RETURN = "return",
            PRIVATE = "private", OBJ = "Object", CHAR = "char", STATIC = "static",
            STARTSQ = "(",
            ENDSQ = ")", FINAL = "final", EQUAL = "=", COMMA = ",",
            SEMICOLON = ";",
            ARRAYSQ = "[]", INITSTART = "{", INITEND = "}",
            METHOD = "()",
            GET_FIELDS = "getFields", LF = "\n", FIELDS = "FIELDS", DBQL = "\"";

    public static void main(String[] args) {
        MyDatabase db = null;
        String tablename = "vmylist";
        String[] fields = { "ID", "UID" };
        try {
            printInit(getFieldName(tablename, db = new MyDatabase(MySQLDatabaseConnection.open("vgames", "nhs20083", "b19930618"))),//
                    tablename, fields);
        } catch (ClassNotFoundException | SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    private static ArrayList<HashMap<String, Object>> getFieldName(String tbn, MyDatabase db) throws SQLException {
        return db.exe("show columns from " + tbn, "[]", "Field", "Type").getList();
    }

    private static void printInit(ArrayList<HashMap<String, Object>> list, String tablename,
            String... names) {
        int[] i = { 0 };
        boolean isNone = names.length != list.size();
        StringBuilder sb = new StringBuilder();
        sb.append(LF + PRIVATE + SPACE + STATIC + SPACE + FINAL + SPACE + STRING + ARRAYSQ + SPACE + FIELDS + SPACE + EQUAL + SPACE + INITSTART + LF);

        System.out.println(PUBLIC + SPACE + STATIC + SPACE + FINAL + SPACE + STRING);
        System.out.println(space4() + TABLE_NAME + SPACE + EQUAL + SPACE + DBQL + tablename + DBQL + COMMA);
        list.forEach(v -> {
            String name = isNone ? "name" : names[i[0]++];
            String comma = list.size() == i[0] ? "" : COMMA;

            System.out.println(space4() + name + SPACE + EQUAL + SPACE + DBQL + v.get("Field") + DBQL + (comma.equals("") ? SEMICOLON : comma));
            sb.append(space4() + name + comma + LF);
        });

        sb.append(INITEND + SEMICOLON);
        sb.append(LF + LF + PUBLIC + SPACE + STATIC + SPACE + STRING + ARRAYSQ + SPACE + GET_FIELDS + METHOD + INITSTART + LF + space4() + RETURN + SPACE + FIELDS + SEMICOLON + LF + INITEND);
        System.out.println(sb.toString());
    }

    private static String space4() {
        return SPACE + SPACE + SPACE + SPACE;
    }

    private static String getType(String t) {
        if (t.startsWith(INT)) {
            return INT;
        }
        if (t.startsWith(CHAR) || t.indexOf(CHAR) != -1) {
            return STRING;
        } else {
            return OBJ;
        }

    }

}

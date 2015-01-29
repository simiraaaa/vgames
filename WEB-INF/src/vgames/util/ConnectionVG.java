package vgames.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import myclass.database.MyDatabase;
import myclass.database.MySQLDatabaseConnection;
import myclass.servlet.Path;

public class ConnectionVG {
    private static final String //
            HAL = "192.168.121.16",//
            LOCAL = "localhost",//
            ID = "nhs20083",//
            PASS = "b19930618",//
            DB_NAME = "vgames",//
            DB_NAME_2 = ID + "db";

    public static Connection open(boolean isLocal) {
        String host = isLocal ? LOCAL : HAL;
        String dbname = isLocal ? DB_NAME : DB_NAME_2;
        Connection connection = null;
        try {
            connection = MySQLDatabaseConnection.open(host, dbname, ID, PASS);
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        return connection;
    }

    public static MyDatabase open(HttpServletRequest req) {
        return new MyDatabase(open(Path.isLocal(req)));
    }
}

/**
 * Created by timo9 on 30-11-2016.
 */

import java.sql.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatabaseConn {
    private static final List<String> TABLES = Arrays.asList("student", "module", "toets", "vraag", "score");
    private static final String STUDENTSQL = "CREATE TABLE IF NOT EXISTS STUDENT " +
            "(StudentID CHAR(8) PRIMARY KEY     NOT NULL," +
            " Naam      TEXT                    NOT NULL," +
            " Klas      TEXT                    NOT NULL);";
    private static final String SCORESQL = "CREATE TABLE IF NOT EXISTS SCORE " +
            "(StudentID CHAR(8)                 NOT NULL    references STUDENT(StudentID)," +
            " VraagID   SERIAL                  NOT NULL    references VRAAG(VraagID)," +
            " Score     SMALLINT," +
            " PRIMARY KEY (StudentID, VraagID));";
    private static final String VRAAGSQL = "CREATE TABLE IF NOT EXISTS VRAAG " +
            "(VraagID       SERIAL  PRIMARY KEY     NOT NULL," +
            " Vraagnummer   VARCHAR                 NOT NULL," +
            " MaxScore      SMALLINT                NOT NULL," +
            " ToetsID       SERIAL                  NOT NULL references TOETS(ToetsID));";
    private static final String TOETSSQL = "CREATE TABLE IF NOT EXISTS TOETS " +
            "(ToetsID       SERIAL  PRIMARY KEY     NOT NULL," +
            " Jaar          CHAR(4)                 NOT NULL," +
            " Schooljaar    CHAR(1)                 NOT NULL," +
            " Periode       CHAR(1)                 NOT NULL," +
            " ModuleID      SERIAL                  NOT NULL references MODULE(ModuleID)," +
            " Toetsvorm     TEXT                    NOT NULL," +
            " Gelegenheid   CHAR(1)                 NOT NULL," +
            " Cesuur        TEXT                    NOT NULL);";
    private static final String MODULESQL = "CREATE TABLE IF NOT EXISTS MODULE " +
            "(ModuleID      SERIAL  PRIMARY KEY     NOT NULL," +
            " ModuleCode    TEXT                    NOT NULL, " +
            " Omschrijving  TEXT);";
    private static Set<String> tablesPresent = new HashSet<String>();

    public static void main(String args[]) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                    "postgres", "1234");
            Statement statement = connection.createStatement();
            System.out.println("Opened database successfully");

            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(connection.getCatalog(), null, "%", new String[]{"TABLE"});
            while (resultSet.next()) {
                tablesPresent.add(resultSet.getString(3));
            }
            for (String table : TABLES) {
                if (!tablesPresent.contains(table)) {
                    makeTable(statement, table);
                }
            }

            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    private static void makeTable(Statement statement, String table) {
        System.out.println(table);
        try {
            switch (table) {
                case "student": statement.executeUpdate(STUDENTSQL); break;
                case "score": statement.executeUpdate(SCORESQL); break;
                case "vraag": statement.executeUpdate(VRAAGSQL); break;
                case "toets": statement.executeUpdate(TOETSSQL); break;
                case "module": statement.executeUpdate(MODULESQL); break;
                default: break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
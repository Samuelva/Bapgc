/*
 * Created by Timothy.
 */

import java.sql.*;
import java.util.*;

public class DatabaseConn {
    /* This class makes connection with the database.
     * First it makes a list of tables that should be in the database.
     * After that it makes the query strings for each table which will
     * be used for creating the database.
     * Next, it makes a set which will be used to store the present
     * tables.
     * The last global variable is for the connection.
     */
    private final List<String> TABLES = Arrays.asList(
            "student", "module", "toets", "vraag", "score"
    );
    private final String STUDENTSQL = "CREATE TABLE IF NOT EXISTS" +
            " STUDENT " +
            "(StudentID SERIAL  PRIMARY KEY     NOT NULL," +
            " Naam      TEXT                    NOT NULL," +
            " Klas      TEXT                    NOT NULL);";
    private final String SCORESQL = "CREATE TABLE IF NOT EXISTS" +
            " SCORE " +
            "(StudentID SERIAL                  NOT NULL " +
            "references STUDENT(StudentID)," +
            " VraagID   SERIAL                  NOT NULL " +
            "references VRAAG(VraagID)," +
            " Score     SMALLINT," +
            " PRIMARY KEY (StudentID, VraagID));";
    private final String VRAAGSQL = "CREATE TABLE IF NOT EXISTS" +
            " VRAAG " +
            "(VraagID       SERIAL  PRIMARY KEY     NOT NULL," +
            " Vraagnummer   VARCHAR                 NOT NULL," +
            " MaxScore      SMALLINT                NOT NULL," +
            " ToetsID       SERIAL                  NOT NULL " +
            "references TOETS(ToetsID)," +
            " Gokvraag      VARCHAR                 NOT NULL);";
    private final String TOETSSQL = "CREATE TABLE IF NOT EXISTS" +
            " TOETS " +
            "(ToetsID       SERIAL  PRIMARY KEY     NOT NULL," +
            " Jaar          CHAR(4)                 NOT NULL," +
            " Schooljaar    CHAR(1)                 NOT NULL," +
            " Periode       CHAR(1)                 NOT NULL," +
            " ModuleCode    TEXT                    NOT NULL " +
            "references MODULE(ModuleCode)," +
            " Toetsvorm     TEXT                    NOT NULL," +
            " Gelegenheid   CHAR(1)                 NOT NULL," +
            " Cesuur        TEXT                    NOT NULL);";
    private final String MODULESQL = "CREATE TABLE IF NOT EXISTS" +
            " MODULE " +
            "(ModuleCode    TEXT    PRIMARY KEY     NOT NULL, " +
            " Omschrijving  TEXT, " +
            " EC            SMALLINT                NOT NULL);";
    private final String GETSQL = "SELECT * FROM %s%s;";
    private final String ALLJOINSQL = "SELECT P.StudentID, Naam, Klas, V.VraagID," +
            " Vraagnummer, Score, MaxScore, Gokvraag, T.ToetsID, ModuleCode," +
            " Jaar, Schooljaar, Periode, ToetsVorm, Gelegenheid, Cesuur" +
            " FROM TOETS T" +
            " FULL OUTER JOIN VRAAG V ON T.ToetsID=V.ToetsID" +
            " FULL OUTER JOIN SCORE S ON V.VraagID=S.VraagID" +
            " FULL OUTER JOIN STUDENT P ON P.StudentID=S.StudentID;" +
            " %s";
    private final String STUDENTSCORESQL = "SELECT P.StudentID," +
            " array_agg(Score ORDER BY Vraagnummer)" +
            " FROM TOETS T" +
            " FULL OUTER JOIN VRAAG V ON T.ToetsID=V.ToetsID" +
            " FULL OUTER JOIN SCORE S ON V.VraagID=S.VraagID" +
            " FULL OUTER JOIN STUDENT P ON P.StudentID=S.StudentID" +
            " WHERE ModuleCode='%s' AND Jaar='%s' AND Schooljaar='%s' AND" +
            " Periode='%s' AND gelegenheid='%s'" +
            " GROUP BY P.StudentID;";
    private Set<String> tablesPresent = new HashSet<String>();
    private Connection connection;
    private Statement statement;
    private InputModule inputModule;
    private InputToets inputToets;
    private InputVraag inputVraag;
    private InputStudent inputStudent;
    private InputScore inputScore;

    public DatabaseConn() {
        /* This method is the constructor for the class.
         * First the driver for postgresql is loaded. After this
         * the connection is made. Then a statement is created, so
         * sql query's can be executed.
         */
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres", "1234");
            this.statement = this.connection.createStatement();
            System.out.println("Opened database successfully");

            DatabaseMetaData metaData = this.connection.getMetaData();
            ResultSet resultSet = metaData.getTables(
                    this.connection.getCatalog(), null,
                    "%", new String[]{"TABLE"});
            while (resultSet.next()) {
                this.tablesPresent.add(resultSet.getString(3));
            }
            for (String table : TABLES) {
                if (!this.tablesPresent.contains(table)) {
                    MakeTable(statement, table);
                }
            }
            this.statement.close();
            inputModule = new InputModule(connection);
            inputToets = new InputToets(connection);
            inputVraag = new InputVraag(connection);
            inputStudent = new InputStudent(connection);
            inputScore = new InputScore(connection);
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    private void MakeTable(Statement statement, String table) {
        try {
            switch (table) {
                case "student": statement.executeUpdate(STUDENTSQL); break;
                case "score": statement.executeUpdate(SCORESQL); break;
                case "vraag": statement.executeUpdate(VRAAGSQL); break;
                case "toets": statement.executeUpdate(TOETSSQL); break;
                case "module": statement.executeUpdate(MODULESQL); break;
                default: break;
            }
            System.out.printf("Table: %s is made.\n", table);
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void CloseConnection() {
        try {
            this.connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    public void InputModule(String moduleCode, String omschrijving,
                            Integer ec) {
        inputModule.Insert(
                moduleCode,
                omschrijving,
                ec
        );
    }
    public void InputToets(Integer id, String jaar, String schooljaar,
                           String periode, String moduleCode, String toetsvorm,
                           String gelegenheid, String cesuur) {
        this.inputToets.insert(
                id,
                jaar,
                schooljaar,
                periode,
                moduleCode,
                toetsvorm,
                gelegenheid,
                cesuur
        );
    }

    public void InputVraag(Integer id, String vraagnummer, Integer maxScore,
                           Integer toetsID, String gokvraag) {
        this.inputVraag.insert(
                id,
                vraagnummer,
                maxScore,
                toetsID,
                gokvraag
        );
    }

    public void InputStudent(Integer studentID, String naam, String klasID) {
        this.inputStudent.insert(
                studentID,
                naam,
                klasID
        );
    }

    public void InputScore(Integer studentID, Integer vraagID) {
        this.inputScore.Insert(
                studentID,
                vraagID
        );
    }

    public void UpdateScore(Integer studentID, Integer vraagID,
                            Integer score) {
        this.inputScore.UpdateScore(
                studentID,
                vraagID,
                score
        );
    }

    public ArrayList<ArrayList<String>> GetTable(String tableName) {
        ArrayList<ArrayList<String>> table = new ArrayList<>();
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format(
                    this.GETSQL, tableName, ""
            ));
            while (resultSet.next()) {
                ArrayList<String> row = new ArrayList<String>();
                //opslaan
                for (int i=1; i<resultSet.getMetaData().getColumnCount()+1; i++) {
                    row.add(resultSet.getString(i));
                }
                table.add(row);
            }
            this.statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return table;
    }

    public ArrayList<ArrayList<String>> GetTable(String tableName, String whereClause) {
        ArrayList<ArrayList<String>> table = new ArrayList<>();
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format(
                    this.GETSQL, tableName, " WHERE " + whereClause
            ));
            while (resultSet.next()) {
                ArrayList<String> row = new ArrayList<String>();
                //opslaan
                for (int i=1; i<resultSet.getMetaData().getColumnCount()+1; i++) {
                    row.add(resultSet.getString(i));
                }
                table.add(row);
            }
            this.statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return table;
    }

    public ArrayList<ArrayList<String>> GetAllJoined() {
        ArrayList<ArrayList<String>> table = new ArrayList<>();
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format(
                    this.ALLJOINSQL, ""
            ));
            while (resultSet.next()) {
                ArrayList<String> row = new ArrayList<String>();
                //opslaan
                for (int i=1; i<resultSet.getMetaData().getColumnCount()+1; i++) {
                    row.add(resultSet.getString(i));
                }
                table.add(row);
            }
            this.statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return table;
    }

    public ArrayList<ArrayList<String>> GetStudentScores(
            String moduleCode, String jaar, String schoolJaar, String periode,
            String gelegenheid) {
        ArrayList<ArrayList<String>> table = new ArrayList<>();
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format(
                    this.STUDENTSCORESQL, moduleCode, jaar,
                    schoolJaar, periode, gelegenheid
            ));
            while (resultSet.next()) {
                ArrayList<String> row = new ArrayList<String>();
                row.add(resultSet.getString(1));
                Integer[] scores = (Integer[])resultSet.getArray(2).getArray();
                for (Integer score : scores) {
                    row.add(String.valueOf(score));
                }
                table.add(row);
            }
            this.statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return table;
    }
}
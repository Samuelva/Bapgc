package database;/*
 * Created by Timothy.
 */

import java.sql.*;
import java.util.*;

public class DatabaseConn {
    /* Deze klasse is de connectie met de database.
     * Eerst staat een lijst met de tabellen die in de database
     * zouden moeten zitten gedefineerd. Dan staan een aantal strings
     * met daarin de query's omde tabellen aan te maken. Hierna volgen
     * een aantal gespecialiseerde query's.
     * Hierna staat een set gedefineert, waarin de huidig beschikbare
     * tabellen worden opgeslagen. Daarna volgen een connection en een
     * statement variabele.
     * Als laatst worden de inputclasses geinstantieert.
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
            " Meerekenen    VARCHAR                 NOT NULL);";
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
            " Cesuur        SMALLINT," +
            " PuntenDoorGokKans SMALLINT);";
    private final String MODULESQL = "CREATE TABLE IF NOT EXISTS" +
            " MODULE " +
            "(ModuleCode    TEXT    PRIMARY KEY     NOT NULL, " +
            " Omschrijving  TEXT, " +
            " EC            SMALLINT                NOT NULL);";
    private final String GETSQL = "SELECT * FROM %s%s;";
    private final String GETTOETSIDSQL = "SELECT ToetsID" +
            " FROM TOETS" +
            " WHERE ModuleCode='%s' AND Jaar='%s' AND Schooljaar='%s' AND" +
            " Periode='%s' AND gelegenheid='%s' AND Toetsvorm='%s';";
    private final String GETVRAAGIDSQL = "SELECT VraagID" +
            " FROM VRAAG" +
            " WHERE Vraagnummer='%s' AND ToetsID=%s;";
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
            " WHERE T.ToetsID=%s" +
            " GROUP BY P.StudentID;";
    private final String CESUURMAXGOKSQL = "SELECT Cesuur, sum(MaxScore), PuntenDoorGokKans" +
            " FROM TOETS T" +
            " FULL OUTER JOIN VRAAG V ON T.ToetsID=V.ToetsID" +
            " WHERE T.ToetsID=%s AND V.Meerekenen='true'" +
            " GROUP BY T.ToetsID;";
    private final String CESUURGOKUPDATESQL = "UPDATE TOETS" +
            " SET Cesuur=%s, PuntenDoorGokKans=%s" +
            " WHERE ToetsID=%s;";
    private final String VRAAGNUMMERSSQL = "SELECT VraagID, vraagnummer," +
            " maxscore, meerekenen" +
            " FROM VRAAG"+
            " WHERE toetsID=%s" +
            " ORDER BY vraagnummer;";
    private final String MODULEPERIODESQL = "SELECT array_agg(ModuleCode)" +
            " FROM TOETS WHERE Periode='%s';";
    private Set<String> tablesPresent = new HashSet<String>();
    private Connection connection;
    private Statement statement;
    private InputModule inputModule;
    private InputToets inputToets;
    private InputVraag inputVraag;
    private InputStudent inputStudent;
    private InputScore inputScore;

    public DatabaseConn() {
        /* Deze methode is de contructor van de class.
         * Eerst wordt de driver van psql geladen. Dan wordt de
         * connection gemaakt met de inlognaam en password.
         * Een statement wordt aangemaakt, zodat query's uitgevoert
         * kunnen worden.
         * Met de metadata van de database wordt dan in een loop door
         * de tabellen gecheckt of alles aanwezig is. Zodra een tabel
         * niet aanwezig is, wordt deze aangemaakt.
         * Als laatst worden alle classes die bij de tabellen horen
         * geïnstantieerd.
         * De Try-catch moet bij elke functie gedaan worden waar
         * met de database geïnteract wordt.
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
        }
    }

    private void MakeTable(Statement statement, String table) {
        /* Deze methode zorgt voor het aanmaken van een tabel.
         * Er wordt een tabelnaam meegegeven welke in de switch-case
         * gaat. Per tabel wordt er dan een specifieke SQL query
         * Aangeroepen dat zorgt voor de aanmaak van die tabel
         * Vervolgens wordt er in een message aangegeven welke
         * tabel aangemaakt is.
         * Wederom staat hier de try-catch, zoals eerder beschreven.
         */
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
        }
    }

    public Connection getConnection() {
        /* Deze methode zorgt voor het returnen van de connectie.
         */
        return this.connection;
    }

    public void CloseConnection() {
        /* Deze methode zorgt voor het sluiten van de connectie
         * Wederom staat hier de try-catch, zoals eerder beschreven.
         */
        try {
            this.connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
    }

    public void InputModule(String moduleCode, String omschrijving,
                            Integer ec) {
        /* Deze methode zorgt voor het invoegen van data in de module
         * tabel. Dit gebeurt via het object inputModule dat eerder
         * aangemaakt is.
         * In module worden de module code, omschrijving en het ec
         * punten aantal opgeslagen.
         */
        inputModule.Insert(
                moduleCode,
                omschrijving,
                ec
        );
    }
    public void InputToets(String jaar, String schooljaar,
                           String periode, String moduleCode, String toetsvorm,
                           String gelegenheid, Integer cesuur,
                           Integer puntenDoorGokKans) {
        /* Deze methode zorgt voor het invoegen van data in de toets
         * tabel. Dit gebeurt via het object inputToets dat eerder
         * aangemaakt is.
         * In toets worden het id, het jaar, schooljaar, periode,
         * module code, toetsvorm, gelegenheid en cesuur opgeslagen.
         */
        this.inputToets.insert(
                jaar,
                schooljaar,
                periode,
                moduleCode,
                toetsvorm,
                gelegenheid,
                cesuur,
                puntenDoorGokKans
        );
    }

    public void InputVraag(String vraagnummer, Integer maxScore,
                           Integer toetsID, boolean meerekenen) {
        /* Deze methode zorgt voor het invoegen van data in de vragen
         * tabel. Dit gebeurt door middel van het object inputVraag dat
         * eerder is aangemaakt.
         * In Vraag worden het id, vraagnummer, maxscore, toets id,
         * gokvraag en meerekenen opgeslagen.
         */
        this.inputVraag.insert(
                vraagnummer,
                maxScore,
                toetsID,
                meerekenen
        );
    }

    public void InputStudent(Integer studentID, String naam, String klasID) {
        /* Deze methode zorgt voor het invoegen van data in de student
         * tabel. Dit gebeurt door middel van het object inputStudent
         * dat eerder is aangemaakt.
         * In student worden het student id, naam en klas id
         * opgeslagen.
         */
        this.inputStudent.insert(
                studentID,
                naam,
                klasID
        );
    }

    public void InputScore(Integer studentID, Integer vraagID, Integer score) {
        /* Deze methode zorgt voor het invoegen van data in de score
         * tabel. Dit gebeurt door middel van het object inputScore dat
         * eerder is aangemaakt.
         * In Vraag worden het student id, vraag id en score opgeslagen.
         */
        this.inputScore.Insert(
                studentID,
                vraagID,
                score
        );
    }

    public void UpdateScore(Integer studentID, Integer vraagID,
                            Integer score) {
        /* Deze methode zorgt voor het veranderen van een score voor een
         * vraag voor een student.
         * Hierbij zijn de vraag id en student id nodig, waarbij
         * score de nieuwe score is.
         */
        this.inputScore.UpdateScore(
                studentID,
                vraagID,
                score
        );
    }

    private String[][] ConvertArrayListTable(ArrayList<ArrayList<String>> table){
        /* Deze methode zorgt voor het converteren van een 2D
         * arrayList naar een 2D string array.
         * Eerst maakt het de 2D string array aan. Dan wordt geloopt
         * door de top-level ArrayList en wordt een naar array
         * geconverteerde rij toegevoegd aan de 2D array.
         * Na de loop wordt de 2D array gereturned.
         */
        String[][] tableArray = new String[table.size()][];
        for (int i=0; i< table.size(); i++){
            ArrayList<String> row = table.get(i);
            tableArray[i] = row.toArray(new String[row.size()]);
        }
        return tableArray;
    }

    private Object[][] ConvertArrayMixTable(ArrayList<ArrayList<Object>> table){
        /* Deze methode zorgt voor het converteren van een 2D
         * arrayList naar een 2D object array.
         * Eerst maakt het de 2D objectt array aan. Dan wordt geloopt
         * door de top-level ArrayList en wordt een naar array
         * geconverteerde rij toegevoegd aan de 2D array.
         * Na de loop wordt de 2D array gereturned.
         */
        Object[][] tableArray = new Object[table.size()][];
        for (int i=0; i< table.size(); i++){
            ArrayList<Object> row = table.get(i);
            tableArray[i] = row.toArray(new Object[row.size()]);
        }
        return tableArray;
    }

    public Integer GetToetsID(
            String moduleCode, String jaar, String schoolJaar,
            String periode, String gelegenheid, String toetsVorm){
        /* Deze methode geeft een id van een toets terug als de
         * gegevens van die toets worden meegegeven.
         * Eerst opent het de connectie met de database. Dan maakt
         * het een resultset op basis van de GETTOETSIDSQL query.
         * Het wordt geformat met de meegegeven toets kenmerken.
         * Vervolgens wordt het eerste resultaat in de eerste kolom
         * eruit gehaald (aangezien dit het enige is in de output
         * van de query). Dit is Het ID
         * De statement wordt dan gesloten en tot slot wordt het ID
         * gereturned.
         * Met dezelfde reden als de constructor wordt het in een
         * try-catch gedaan.
         */
        Integer id = 0;
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format(
                    this.GETTOETSIDSQL, moduleCode, jaar,
                    schoolJaar, periode, gelegenheid, toetsVorm
            ));
            resultSet.next();
            id = resultSet.getInt(1);
            this.statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return id;
    }

    public Integer GetVraagID(
            String vraagnummer, int toetsID){
        /* Deze methode geeft een id van een vraag terug als de
         * gegevens van die vraag worden meegegeven.
         * Eerst opent het de connectie met de database. Dan maakt
         * het een resultset op basis van de GETVRAAGIDSQL query.
         * Het wordt geformat met de meegegeven vraag kenmerken.
         * Vervolgens wordt het eerste resultaat in de eerste kolom
         * eruit gehaald (aangezien dit het enige is in de output
         * van de query). Dit is Het ID
         * De statement wordt dan gesloten en tot slot wordt het ID
         * gereturned.
         * Met dezelfde reden als de constructor wordt het in een
         * try-catch gedaan.
         */
        Integer id = 0;
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format(
                    this.GETVRAAGIDSQL, vraagnummer, toetsID
            ));
            resultSet.next();
            id = resultSet.getInt(1);
            this.statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return id;
    }

    public String[][] GetTable(String tableName) {
        /*
         */
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
        }
        return ConvertArrayListTable(table);
    }

    public String[][] GetTable(String tableName, String whereClause) {
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
        }
        return ConvertArrayListTable(table);
    }

    public String[][] GetAllJoined() {
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
        }
        return ConvertArrayListTable(table);
    }

    public String[][] GetStudentScores(Integer toetsID) {
        ArrayList<ArrayList<String>> table = new ArrayList<>();
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format(
                    this.STUDENTSCORESQL, toetsID
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
        }
        return ConvertArrayListTable(table);
    }

    public Integer[] GetCesuurMaxGok(Integer toetsID) {
        /* Deze methode zorgt voor het returnen van de volgende
         * waarden in een array:
         * -0 Cesuur
         * -1 Max punten
         * -2 Aantal punten door gokkans
         * Dit wordt opgehaald voor de toets die wordt meegegeven.
         * Met dezelfde reden als de constructor wordt het in een
         * try-catch gedaan.
         */
        Integer[] array = new Integer[3];
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format(
                    this.CESUURMAXGOKSQL, toetsID
            ));
            resultSet.next();
            array[0] = Integer.valueOf(resultSet.getString(1));
            array[1] = Integer.valueOf(resultSet.getString(2));
            array[2] = Integer.valueOf(resultSet.getString(3));
            this.statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return array;
    }

    public void UpdateCesuurGok(Integer toetsID, Integer cesuur,
                                Integer gokpunten){
        /* Deze methode zorgt voor het updaten van de cesuur en het
         * aantal gokpunten voor een toets.
         * Met behulp van de CESUURGOKUPDATE sql en de meegegeven
         * toets ID, en nieuwe waarden voor cesuur en gokpunten, wordt
         * het geupdate in de database.
         * Met dezelfde reden als de constructor wordt het in een
         * try-catch gedaan.
         */
        try {
            this.statement = this.connection.createStatement();
            this.statement.executeUpdate(String.format(
                    this.CESUURGOKUPDATESQL, cesuur, gokpunten, toetsID
            ));
            this.statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
    }

    public Object[][] GetVragenVanToets(Integer toetsID){
        /* Deze methode zorgt voor het terugkeren van de vragen
         * van een specifieke toets.
         * De volgorde van de arrays is als volgt:
         * -0 vraagID
         * -1 vraagnummer
         * -2 max score
         * -3 meerekenen
         *
         * EerstVoert het de query uit met behulp van de meegegeven
         * toets ID. Vervolgens haalt het in een loop, per vraag, de
         * 4 waarden op.
         * Met dezelfde reden als de constructor wordt het in een
         * try-catch gedaan.
         */
        ArrayList<ArrayList<Object>> table = new ArrayList<>();
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format(
                    this.VRAAGNUMMERSSQL, toetsID
            ));
            while (resultSet.next()) {
                ArrayList<Object> row = new ArrayList<Object>();
                row.add(resultSet.getInt(1));
                row.add(resultSet.getString(2));
                row.add(resultSet.getInt(3));
                row.add(resultSet.getBoolean(4));
                table.add(row);
            }
            this.statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return ConvertArrayMixTable(table);
    }

    public String[] GetModulecodesPerPeriode(char periode){
        /* Deze methode returned een array met alle modulecodes voor een
         * opgegeven periode.
         * Eerst voert het de query uit, waarna het de array extract.
         * De statement wordt gesloten en de array gereturned.
         * Met dezelfde reden als de constructor wordt het in een
         * try-catch gedaan.
         */
        String[] array;
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format(
                    this.MODULEPERIODESQL, periode
            ));
            resultSet.next();
            array = (String[])resultSet.getArray(1).getArray();
            this.statement.close();
            return array;
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return array = new String[0];
    }
}
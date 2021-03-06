package database;
 /*
 * Created by Timothy.
 */

import java.sql.*;
import java.util.*;

public class DatabaseConn {
    /**
     * Deze klasse is de connectie met de database.
     * Eerst wordt er een lijst met de tabellen die in de database
     * zouden moeten zitten gedefinieerd. Dan staan er een aantal strings
     * met daarin de queries om de tabellen aan te maken. Hierna volgen
     * een aantal gespecialiseerde queries.
     * Hierna staat een set gedefinieerd, waarin de huidig beschikbare
     * tabellen worden opgeslagen. Daarna volgen een connection en een
     * statement variabele.
     * Als laatst worden de inputclasses geïnstantieerd.
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
    private final String ALLJOINSQL = "SELECT P.StudentID, Naam, Klas, V" +
            ".VraagID," +
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
    private final String CESUURMAXGOKSQL = "SELECT Cesuur, sum(MaxScore), " +
            "PuntenDoorGokKans" +
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
    private final String TOETSKANSENSQL = "SELECT ToetsVorm," +
            " array_agg(ToetsID)" +
            " FROM TOETS" +
            " WHERE ModuleCode='%s'" +
            " GROUP BY ToetsVorm;";
    private final String DELETEVRAGENSQL = "DELETE FROM SCORE" +
            " USING VRAAG" +
            " WHERE SCORE.VraagID = VRAAG.VraagID AND VRAAG.ToetsID=%s;" +
            " DELETE FROM VRAAG" +
            " WHERE ToetsID=%s;";
    private final String JAARTALLENSQL = "SELECT Jaar FROM TOETS ORDER BY " +
            "Jaar;";
    private final String SCHOOLJAARSQL = "SELECT Schooljaar FROM TOETS" +
            " WHERE TOETS.Jaar='%s';";
    private final String PERIODESQL = "SELECT Periode FROM TOETS" +
            " WHERE Jaar='%s' AND Schooljaar='%s';";
    private final String MODULESSQL = "SELECT ModuleCode FROM TOETS" +
            " WHERE Jaar='%s' AND Schooljaar='%s' AND Periode='%s';";
    private final String TYPESQL = "SELECT Toetsvorm FROM TOETS" +
            " WHERE Jaar='%s' AND Schooljaar='%s' AND Periode='%s' AND " +
            "ModuleCode='%s';";
    private final String CHANCESQL = "SELECT Gelegenheid FROM TOETS" +
            " WHERE Jaar='%s' AND Schooljaar='%s' AND Periode='%s' AND " +
            "ModuleCode='%s' AND Toetsvorm='%s';";
    private final String GETALLTOETS = "SELECT ToetsID, Jaar, ModuleCode, " +
            "Toetsvorm, Gelegenheid FROM TOETS;";
    private final String GETALLMODULES = "SELECT DISTINCT ModuleCode, Jaar " +
            "FROM TOETS";
    private final String GETALLBLOCKS = "SELECT DISTINCT Jaar, Schooljaar, " +
            "Periode FROM TOETS";
    private final String GETTOETSDATASQL = "SELECT DISTINCT ModuleCode, Jaar," +
            " Periode, Schooljaar" +
            " FROM TOETS";
    private final String TESTFILTERSQL = "SELECT ToetsID, Jaar, ModuleCode, " +
            "Toetsvorm, Gelegenheid FROM TOETS WHERE Jaar LIKE '%s' AND " +
            "Schooljaar LIKE '%s' AND Periode LIKE '%s' AND ModuleCode LIKE " +
            "'%s' AND Toetsvorm LIKE '%s' AND Gelegenheid LIKE '%s';";
    private final String COURSEFILTERSQL = "SELECT ModuleCode, Jaar FROM " +
            "TOETS WHERE Jaar LIKE '%s' AND Schooljaar LIKE '%s' AND Periode " +
            "LIKE '%s';";
    private final String BLOCKFILTERSQL = "SELECT Jaar, Schooljaar, Periode " +
            "FROM TOETS WHERE Jaar LIKE '%s' AND Schooljaar LIKE '%s';";
    private final String DELETESCORES = "DELETE FROM SCORE" +
            " WHERE VraagID=%s";
    private final String TESTIDSQL = "SELECT ToetsID FROM TOETS WHERE " +
            "Jaar='%s' AND ModuleCode='%s' AND Toetsvorm='%s' AND " +
            "Gelegenheid='%s';";
    private final String UPDATEMEEREKENSQL = "UPDATE VRAAG" +
            " SET Meerekenen='%s'" +
            " WHERE VraagID=%s;";
    private final String GETITEMSSQL = "SELECT %s FROM TOETS WHERE Jaar " +
            "LIKE '%s' AND Schooljaar LIKE '%s' AND Periode LIKE '%s' AND " +
            "ModuleCode LIKE '%s' AND Toetsvorm LIKE '%s' AND Gelegenheid " +
            "LIKE '%s';";
    private final String GETEXAMIDSQL = "SELECT ToetsID FROM VRAAG WHERE " +
            "VraagID = '%s';";
    private Set<String> tablesPresent = new HashSet<String>();
    private Connection connection;
    private Statement statement;
    private InputModule inputModule;
    private InputToets inputToets;
    private InputVraag inputVraag;
    private InputStudent inputStudent;
    private InputScore inputScore;

    public DatabaseConn() {
        /**
         * Deze methode is de contructor van de class.
         * Eerst wordt de driver van psql geladen. Dan wordt de
         * connectie gemaakt met de inlognaam en wachtwoord.
         * Een statement wordt aangemaakt, zodat queries uitgevoerd
         * kunnen worden.
         * Met de metadata van de database wordt dan in een loop in
         * de tabellen gecheckt of alles aanwezig is. Zodra een tabel
         * niet aanwezig is, wordt deze aangemaakt.
         * Als laatst worden alle classes die bij de tabellen horen
         * geïnstantieerd.
         * De Try-catch moet bij elke functie gedaan worden waar
         * met de database een interactie gedaan wordt.
         */
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres", "1234");
            this.statement = this.connection.createStatement();

            MakeTables();
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

    private void MakeTables() {
        /**
         * Deze methode zorgt voor het aanmaken van de tabellen.
         * Er wordt een tabelnaam meegegeven die in de switch-case
         * gaat. Per tabel wordt er dan een specifieke SQL query
         * aangeroepen, die zorgt voor de aanmaak van die tabel.
         * Vervolgens wordt er in een bericht aangegeven welke
         * tabel aangemaakt is.
         * Wederom staat hier de try-catch, zoals eerder beschreven.
         */
        try {
            this.tablesPresent = new HashSet<>();
            DatabaseMetaData metaData = this.connection.getMetaData();
            ResultSet resultSet = metaData.getTables(
                    this.connection.getCatalog(), null,
                    "%", new String[]{"TABLE"});
            while (resultSet.next()) {
                this.tablesPresent.add(resultSet.getString(3));
            }
            for (String table : this.TABLES) {
                if (!this.tablesPresent.contains(table)) {
                    switch (table) {
                        case "student":
                            this.statement.executeUpdate(STUDENTSQL);
                            break;
                        case "score":
                            this.statement.executeUpdate(SCORESQL);
                            break;
                        case "vraag":
                            this.statement.executeUpdate(VRAAGSQL);
                            break;
                        case "toets":
                            this.statement.executeUpdate(TOETSSQL);
                            break;
                        case "module":
                            this.statement.executeUpdate(MODULESQL);
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
    }

    public Connection getConnection() {
        /**
         * Deze methode zorgt voor het returnen van de connectie.
         */
        return this.connection;
    }

    public void CloseConnection() {
        /**
         * Deze methode zorgt voor het sluiten van de connectie
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
        /**
         * Deze methode zorgt voor het invoegen van data in de module
         * tabel. Dit gebeurt via het object inputModule dat eerder
         * aangemaakt is.
         * In module worden de module code, omschrijving en het EC
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
        /**
         * Deze methode zorgt voor het invoegen van data in de toets
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
        /**
         * Deze methode zorgt voor het invoegen van data in de vragen
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
        /**
         * Deze methode zorgt voor het invoegen van data in de student
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
        /**
         * Deze methode zorgt voor het invoegen van data in de score
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
        /**
         * Deze methode zorgt voor het veranderen van een score voor een
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

    private String[][] ConvertArrayListTable(ArrayList<ArrayList<String>>
                                                     table){
        /**
         * Deze methode zorgt voor het converteren van een 2D
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
        /**
         * Deze methode zorgt voor het converteren van een 2D
         * arrayList naar een 2D object array.
         * Eerst maakt het de 2D object array aan. Dan wordt geloopt
         * door de top-level ArrayList en wordt er een naar array
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
        /**
         * Deze methode geeft een id van een toets terug als de
         * gegevens van die toets worden meegegeven.
         * Eerst opent het de connectie met de database. Dan maakt
         * het een resultset op basis van de GETTOETSIDSQL query.
         * Het wordt geformat met de meegegeven toets kenmerken.
         * Vervolgens wordt het eerste resultaat in de eerste kolom
         * eruit gehaald (aangezien dit het enige is in de output
         * van de query). Dit is het ID.
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
            throw new EmptyStackException();
        }
        return id;
    }

    public Integer GetVraagID(
            String vraagnummer, int toetsID){
        /**
         * Deze methode geeft een id van een vraag terug als de
         * gegevens van die vraag worden meegegeven.
         * Eerst opent het de connectie met de database. Dan maakt
         * het een resultset op basis van de GETVRAAGIDSQL query.
         * Het wordt geformat met de meegegeven vraag kenmerken.
         * Vervolgens wordt het eerste resultaat in de eerste kolom
         * eruit gehaald (aangezien dit het enige is in de output
         * van de query). Dit is het ID.
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
            throw new EmptyStackException();
        }
        return id;
    }

    public String[][] GetTable(String tableName) {
        /**
         * Deze methode zorgt voor het returnen van een tabel.
         * Omdat de output variabel van grootte kan zijn, wordt een
         * arraylist aangemaakt.
         * Dan wordt een statement aangemaakt, waarna de query
         * uitgevoerd wordt en opgeslagen in een resultset.
         * Er wordt door de rijen van de resultset heen geloopt,
         * waarna bij elke rij elk element wordt opgeslagen in een
         * 2e arraylist. Deze arraylist wordt elke keer opgeslagen in
         * de eerste arraylist.
         * De statement wordt hierna gesloten.
         * Met dezelfde reden als de constructor wordt het in een
         * try-catch gedaan.
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
                for (int i=1; i<resultSet.getMetaData().getColumnCount()+1;
                     i++) {
                    row.add(resultSet.getString(i));
                }
                table.add(row);
            }
            this.statement.close();
        } catch (Exception e) {
            throw new EmptyStackException();
        }
        return ConvertArrayListTable(table);
    }

    public String[][] GetTable(String tableName, String whereClause) {
        /**
         * Deze methode zorgt voor het returnen van een tabel, waarbij
         * een where-clause wordt gebruikt.
         * Omdat de output variabel van grootte kan zijn wordt een
         * arraylist aangemaakt.
         * Dan wordt een statement aangemaakt, waarna de query
         * uitgevoerd wordt en opgeslagen in een resultset.
         * Er wordt door de rijen van de resultset heen geloopt,
         * waarna bij elke rij elk element wordt opgeslagen in een
         * 2e arraylist. Deze arraylist wordt elke keer opgeslagen in
         * de eerste arraylist.
         * De statement wordt hierna gesloten.
         * Met dezelfde reden als de constructor wordt het in een
         * try-catch gedaan.
         */
        ArrayList<ArrayList<String>> table = new ArrayList<>();
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format(
                    this.GETSQL, tableName, " WHERE " + whereClause
            ));
            while (resultSet.next()) {
                ArrayList<String> row = new ArrayList<String>();
                //opslaan
                for (int i=1; i<resultSet.getMetaData().getColumnCount()+1;
                     i++) {
                    row.add(resultSet.getString(i));
                }
                table.add(row);
            }
            this.statement.close();
        } catch (Exception e) {
            throw new EmptyStackException();
        }
        return ConvertArrayListTable(table);
    }

    public String[][] GetAllJoined() {
        /**
         * Deze methode zorgt voor het returnen van alle tabellen
         * gejoined met elkaar.
         * Eerst maakt het een nieuwe arraylist in arraylist aan,
         * omdat de output van variabele grootte kan zijn. Vervolgens
         * wordt een statement aangemaakt en een resultset aan de hand
         * van de query.
         * Door deze resultset wordt geloopt, waarbij elk element van
         * een rij wordt opgeslagen in een arraylist in een loop. Elke
         * rij wordt vervolgens opgeslagen in de hoofd arraylist.
         * Aan het einde wordt de statement weer gesloten.
         * Met dezelfde reden als de constructor wordt het in een
         * try-catch gedaan.
         */
        ArrayList<ArrayList<String>> table = new ArrayList<>();
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format(
                    this.ALLJOINSQL, ""
            ));
            while (resultSet.next()) {
                ArrayList<String> row = new ArrayList<String>();
                //opslaan
                for (int i=1; i<resultSet.getMetaData().getColumnCount()+1;
                     i++) {
                    row.add(resultSet.getString(i));
                }
                table.add(row);
            }
            this.statement.close();
        } catch (Exception e) {
            throw new EmptyStackException();
        }
        return ConvertArrayListTable(table);
    }

    public String[][] GetStudentScores(Integer toetsID) {
        /**
         * Deze methode zorgt voor het returnen van een tabel met
         * alle scores per student voor een bepaalde toets.
         * Hierbij wordt eerst een arraylist in een arraylist
         * aangemaakt, omdat nog niet bekend is hoelang de lijsten
         * worden.
         * Dan wordt de query opgesteld met het meegegeven toetsID en
         * wordt deze uitgevoerd.
         * Dan wordt in een loop door de rijen heen achter elkaar het
         * studentnummer toegevoegd, waarna in een loop alle scores
         * worden toegevoegd. Deze rij wordt dan aan de hoofd tabel
         * toegevoegd.
         * Na deze loop wordt de arraylist omgezet in een String[][] en
         * wordt deze gereturned.
         * Met dezelfde reden als de constructor wordt het in een
         * try-catch gedaan.
         */
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
            throw new EmptyStackException();
        }
        return ConvertArrayListTable(table);
    }

    public Integer[] GetCesuurMaxGok(Integer toetsID) {
        /**
         * Deze methode zorgt voor het returnen van de volgende
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
            throw new EmptyStackException();
        }
        return array;
    }

    public void UpdateCesuurGok(Integer toetsID, Integer cesuur,
                                Integer gokpunten){
        /**
         * Deze methode zorgt voor het updaten van de cesuur en het
         * aantal gokpunten voor een toets.
         * Met behulp van de CESUURGOKUPDATE sql en de meegegeven
         * toets ID, en nieuwe waarden voor cesuur en gokpunten, wordt
         * het geüpdate in de database.
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
            throw new EmptyStackException();
        }
    }

    public void DeleteVragenToets(Integer toetsID){
        /**
         * Deze methode zorgt voor het deleten van alle vragen
         * en bijbehorende scores uit de database aan de hand van
         * de meegegeven toetsID.
         * Met dezelfde reden als de constructor wordt het in een
         * try-catch gedaan.
         */
        try {
            this.statement = this.connection.createStatement();
            this.statement.executeUpdate(String.format(
                    this.DELETEVRAGENSQL, toetsID, toetsID
            ));
            this.statement.close();
        } catch (Exception e) {
            throw new EmptyStackException();
        }
    }

    public Object[][] GetVragenVanToets(Integer toetsID){
        /**
         * Deze methode zorgt voor het terugkeren van de vragen
         * van een specifieke toets.
         * De volgorde van de arrays is als volgt:
         * -0 vraagID
         * -1 vraagnummer
         * -2 max score
         * -3 meerekenen
         *
         * Eerst voert het de query uit met behulp van de meegegeven
         * toets ID. Vervolgens haalt het in een loop, per vraag, de
         * vier waarden op.
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
            throw new EmptyStackException();
        }
        return ConvertArrayMixTable(table);
    }

    public String[] GetModulecodesPerPeriode(char periode){
        /**
         * Deze methode returned een array met alle modulecodes voor een
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
            throw new EmptyStackException();
        }
    }

    public Object[][] GetToetsKansen(String modulecode){
        /**
         * Deze methode returned een tabel waarin per toetsvorm staat:
         * - Toetsvorm
         * - Toets ID van kans1 (als aanwezig)
         * - Toets ID van kans2 (als aanwezig)
         * - enz.
         * Dit wordt gedaan bij een specifieke module die wordt
         * meegegeven.
         * De query hiervoor wordt eerst uitgevoerd, waarna door de
         * rijen van de tabel wordt geloopt. Dan slaat het in een
         * tijdelijke arraylist eerst de toesvorm op en dan in een
         * extra loop alle kansen. De rij wordt aan de tabel
         * toegevoegd en het wordt geconverteerd naar array gereturned.
         * Met dezelfde reden als de constructor wordt het in een
         * try-catch gedaan.
         */
        ArrayList<ArrayList<Object>> table = new ArrayList<>();
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format(
                    this.TOETSKANSENSQL, modulecode
            ));
            while (resultSet.next()) {
                ArrayList<Object> row = new ArrayList<>();
                row.add(resultSet.getString(1));
                Integer[] temp = (Integer[])resultSet.getArray(2).getArray();
                for(Integer x : temp) {
                    row.add(x);
                }
                table.add(row);
            }
            this.statement.close();
        } catch (Exception e) {
            throw new EmptyStackException();
        }
        return ConvertArrayMixTable(table);
    }
    public List<String> getYears() {
        /**
         * Deze functie wordt gebruikt in de ChoiceMenuDatabaseConnect klasse.
         * De beschikbare jaren in de database wordt gereturned.
         */
        List<String> years;
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format(
                    this.JAARTALLENSQL
            ));
            years = new ArrayList<String>();
            while (resultSet.next()) {
                if (!years.contains(resultSet.getString("Jaar"))) {
                    years.add(resultSet.getString("Jaar"));
                }
            }
        } catch (Exception e) {
            throw new EmptyStackException();
        }
        return years;
    }

    public List<String> getSchoolYears(String selectedYear) {
        /**
         * Deze functie wordt gebruikt in de ChoiceMenuDatabaseConnect klasse.
         * De beschikbare schooljaren worden gereturned, welke bij het
         * opgegeven jaar horen.
         */
        List<String> schoolYears;
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format(
                    this.SCHOOLJAARSQL, selectedYear
            ));
            schoolYears = new ArrayList<String>();
            while (resultSet.next()) {
                String result = resultSet.getString("Schooljaar");
                if (!schoolYears.contains(result)) {
                    schoolYears.add(result);
                }
            }
        } catch (Exception e) {
            throw new EmptyStackException();
        }
        return schoolYears;
    }

    public List<String> getBlocks(String selectedYear, String
            selectedStudyYear) {
        /**
         * Deze functie wordt gebruikt in de ChoiceMenuDatabaseConnect klasse.
         * De beschikbare periodes worden gereturned, afhankelijk van het
         * opgegeven jaar en schooljaar.
         */
        List<String> blocks;
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format(
                    this.PERIODESQL, selectedYear, selectedStudyYear
            ));
            blocks = new ArrayList<String>();
            while (resultSet.next()) {
                String result = resultSet.getString("Periode");
                if (!blocks.contains(result)) {
                    blocks.add(result);
                }
            }
            Collections.sort(blocks);
        } catch (Exception e) {
            throw new EmptyStackException();
        }
        return blocks;
    }

    public List<String> getCourses(String selectedYear, String
            selectedSchoolYear, String selectedBlock) {
        /**
         * Deze functie wordt gebruikt in de ChoiceMenuDatabaseConnect klasse.
         * De beschikbare modules worden gereturned afhankelijk van het
         * opgegeven jaar, schooljaar en de periode.
         */
        List<String> courses;
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format(
                    this.MODULESSQL, selectedYear, selectedSchoolYear,
                    selectedBlock
            ));
            courses = new ArrayList<String>();
            while (resultSet.next()) {
                String result = resultSet.getString("ModuleCode");
                if (!courses.contains(result)) {
                    courses.add(result);
                }
            }
        } catch (Exception e) {
            throw new EmptyStackException();
        }
        return courses;
    }

    public List<String> getTypes(String selectedYear, String
            selectedSchoolYear, String selectedBlock, String selectedCourse) {
        /**
         * Deze functie wordt gebruikt in de ChoiceMenuDatabaseConnect klasse.
         * De beschikbare toetsvormen worden gereturned afhankelijk van het
         * opgegeven jaar, schooljaar, de periode en module.
         */
        List<String> types;
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format(
                    this.TYPESQL, selectedYear, selectedSchoolYear,
                    selectedBlock, selectedCourse
            ));
            types = new ArrayList<String>();
            while (resultSet.next()) {
                String result = resultSet.getString("Toetsvorm");
                if (!types.contains(result)) {
                    types.add(result);
                }
            }
        } catch (Exception e) {
            throw new EmptyStackException();
        }
        return types;
    }

    public List<String> getAttempts(String selectedYear, String
            selectedSchoolYear, String selectedBlock, String selectedCourse,
                                    String selectedType) {
        /**
         * Deze functie wordt gebruikt in de ChoiceMenuDatabaseConnect klasse.
         * De beschikbare gelegenheden worden gereturned afhankelijk van het
         * opgegeven jaar, schooljaar, de periode, module en toetsvorm.
         */
        List<String> attempts;
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format(
                    this.CHANCESQL, selectedYear, selectedSchoolYear,
                    selectedBlock, selectedCourse, selectedType
            ));
            attempts = new ArrayList<String>();
            while (resultSet.next()) {
                String result = resultSet.getString("Gelegenheid");
                if (!attempts.contains(result)) {
                    attempts.add(result);
                }
            }
        } catch (Exception e) {
            throw new EmptyStackException();
        }
        return attempts;
    }

    public void ResetTables(){
        /**
         * Deze methode zorgt voor het legen van de hele database.
         * Eerst gaat het alle tabellen langs en delete het elke
         * tabel in een loop. Vervolgens roept het de functie
         * MakeTables() aan, zodat weer nieuwe lege tabellen worden
         * aangemaakt.
         * Met dezelfde reden als de constructor wordt het in een
         * try-catch gedaan.
         */
        try {
            this.statement = this.connection.createStatement();
            for (String tabel : this.TABLES) {
                this.statement.executeUpdate(String.format(
                        "DROP TABLE %s CASCADE;", tabel
                ));
            }
            MakeTables();
            this.statement.close();
        } catch (Exception e) {
            throw new EmptyStackException();
        }
    }

    public String[][] GetToetsData() {
        /**
         * Deze methode haalt alle informatie voor alle toetsen op uit
         * de database. Hierbij worden de verschillende gelegenheden
         * genegeerd. De SQL statement this.GETTOETSDATASQL wordt hiervoor
         * gebruikt. De resultaten worden onder resultSet opgeslagen en
         * vervolgens aan een ArrayList toegevoegd, zodat het vervolgens
         * met ConvertArrayListTable omgezet kan worden in een String[][].
         */
        ArrayList<ArrayList<String>> table = new ArrayList<>();
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(this.GETTOETSDATASQL);
            while (resultSet.next()) {
                ArrayList<String> row = new ArrayList<String>();
                for (int i = 1; i < resultSet.getMetaData().getColumnCount()
                        + 1; i++) {
                    row.add(resultSet.getString(i));
                }
                table.add(row);
            }
            this.statement.close();
        } catch (Exception e) {
            throw new EmptyStackException();
        }
        return ConvertArrayListTable(table);
    }

    public String getTestID(String year, String course, String type,
                                  String attempt) {
        /**
         * Deze functie wordt in het vergelijkscherm gebruikt in de toetstab.
         * Als er een selectie wordt gemaakt in het selectiemenu, wordt de
         * selectie meegegeven en returned deze functie het toets id wat bij
         * de selectie hoort. De toetsid wordt vervolgens gebruikt om de
         * toetsstatistieken te verkrijgen in het vergelijkscherm.
         */
        String testID = new String();
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format
                    (this.TESTIDSQL, year, course, type, attempt));
            while (resultSet.next()) {
                testID = resultSet.getString("ToetsID");
            }
        } catch (Exception e) {
            throw new EmptyStackException();
        }
        return testID;
    }

    public List<String> filterTest(String year, String schoolYear, String
            block, String course, String type, String attempt) {
        /**
         * Deze functie wordt gebruikt in het vergelijkscherm keuzemenu. De
         * toetsen worden gereturned uit de database afhankelijk van de
         * combobox selectie, welke als filters dienen en worden weergegeven
         * in het selectiemenu.
         */
        List<String> filteredTests;
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format(this
                    .TESTFILTERSQL, year, schoolYear, block, course, type,
                    attempt));
            filteredTests = new ArrayList<>();
            while (resultSet.next()) {
                filteredTests.add(resultSet.getString("ModuleCode") + " " +
                        resultSet.getString("Toetsvorm") + " " +
                        resultSet.getString("Gelegenheid") + " " +
                        resultSet.getString("Jaar"));
            }
        } catch (Exception e) {
            throw new EmptyStackException();
        }
        if (filteredTests.size() > 0) {
            Collections.sort(filteredTests.subList(1, filteredTests.size()));
        }
        return filteredTests;
    }

    public List<String> filterCourse(String year, String schoolYear, String
            block) {
        /**
         * Deze functie wordt gebruikt in het vergelijkscherm keuzemenu.
         * De modules worden uit de database gereturned, afhankelijk van de
         * combobox selectie en worden weergegeven in het selectiemenu.
         */
        List<String> filteredModules;
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format(this
                            .COURSEFILTERSQL, year, schoolYear, block));
            filteredModules = new ArrayList<>();
            while (resultSet.next()) {
                String result = new String(resultSet.getString("ModuleCode") +
                        " " + resultSet.getString("Jaar"));
                if (!filteredModules.contains(result)) {
                    filteredModules.add(result);
                }
            }
        } catch (Exception e) {
            throw new EmptyStackException();
        }
        if (filteredModules.size() > 0) {
            Collections.sort(filteredModules.subList(1, filteredModules.size
                    ()));
        }
        return filteredModules;
    }

    public List<String> filterBlock(String year, String schoolYear) {
        /**
         * Deze functie wordt gebruikt door het vergelijkscherm keuzemenu. De
         * functie returned de periodes uit de database, afhankelijk van de
         * combobox selectie en worden weergegeven in het selectiemenu.
         */
        List<String> filteredBlocks;
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format(this
                    .BLOCKFILTERSQL, year, schoolYear));
            filteredBlocks = new ArrayList<>();
            while (resultSet.next()) {
                String result = new String("Periode " + resultSet.getString
                        ("Periode") + " jaar " + resultSet.getString
                        ("Schooljaar") + " " + resultSet.getString("Jaar"));
                if (!filteredBlocks.contains(result)) {
                    filteredBlocks.add(result);
                }
            }
        } catch (Exception e) {
            throw new EmptyStackException();
        }
        if (filteredBlocks.size() > 0) {
            Collections.sort(filteredBlocks.subList(1, filteredBlocks.size()));
        }
        return filteredBlocks;
    }

    public List<String> getItems(String item, List<String> selection) {
        /**
         * Deze functie wordt door het vergelijkscherm keuzemenu gebruikt.
         * De gebruikte query is dynamisch en de data welke wordt opgehaald
         * (jaar, leerjaar, periode, module, toetsvorm, gelegenheid)
         * is afhankelijk van de geselecteerde combobox in het vergelijkscherm.
         * De lijst selection bevat waardes met de selectie van het keuzemenu.
         * Als niet alles in het keuzemenu is geselecteerd, wordt er %
         * meegegeven wat "Alles" betekent.
         */
        List<String> items;
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format
                    (this.GETITEMSSQL, item, selection.get(0), selection.get(1),
                            selection.get(2), selection.get(3), selection.get
                                    (4), selection.get(5)));
            items = new ArrayList<>();
            while (resultSet.next()) {
                if (!items.contains(resultSet.getString(item))) {
                    items.add(resultSet.getString(item));
                }
            }
        } catch (Exception e) {
            throw new EmptyStackException();
        }
        return items;
    }

    public void DeleteScoresForQuestion(Integer questionID){
        /**
         * In deze methode wordt de SQL statement uitgevoerd die onder
         * this.DELETESCORES gedefinieerd staat. Hierdoor worden de
         * scores voor de vraag met ID questionID verwijderd uit de database.
         */
        try {
            this.statement = this.connection.createStatement();
            this.statement.executeUpdate(String.format(
                    this.DELETESCORES, questionID
            ));
            this.statement.close();
        } catch (Exception e) {
            throw new EmptyStackException();
        }
    }

    public void UpdateMeereken(Integer vraagID, Boolean meerekenen){
        /**
         * Deze methode zorgt voor het updaten van de kolom meerekenen.
         * Met behulp van de UPDATEMEEREKENSQL en de meegegeven
         * vraag ID, en nieuwe waarde voor meerekenen, wordt
         * het geupdate in de database.
         * Met dezelfde reden als de constructor wordt het in een
         * try-catch gedaan.
         */
        try {
            this.statement = this.connection.createStatement();
            this.statement.executeUpdate(String.format(
                    this.UPDATEMEEREKENSQL, meerekenen, vraagID
            ));
            this.statement.close();
        } catch (Exception e) {
            throw new EmptyStackException();
        }
    }

    public Integer getExamID(Integer vraagID) {
        /**
         * Deze functie returned het toets ID welke bij de opgegeven vraag ID
         * hoort. Met een querry wordt er gekeken welke toets ID geassocieerd
         * is met het vraag ID.
         */
        Integer id = 0;
        try {
            this.statement = this.connection.createStatement();
            ResultSet resultSet = this.statement.executeQuery(String.format
                    (this.GETEXAMIDSQL, vraagID));
            resultSet.next();
            id = resultSet.getInt(1);
            this.statement.close();
        } catch (Exception e) {
            throw new EmptyStackException();
        }
        return id;
    }
}

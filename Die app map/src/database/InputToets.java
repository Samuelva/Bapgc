package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

class InputToets {
    private final String MODULESQL = "INSERT INTO TOETS" +
            " (Jaar, Schooljaar, Periode, ModuleCode, Toetsvorm, " +
            " Gelegenheid, Cesuur, PuntenDoorGokKans)" +
            " VALUES (%s, %s, %s, %s, %s, %s, %s, %s);";
    private final String CHECKSQL = "SELECT ToetsID" +
            " FROM TOETS" +
            " WHERE ModuleCode=%s AND" +
            " Jaar=%s AND" +
            " Schooljaar=%s AND" +
            " Periode=%s AND" +
            " Toetsvorm=%s AND" +
            " Gelegenheid=%s;";
    private Connection connection;
    private QueryString jaar = new QueryString();
    private QueryString schooljaar = new QueryString();
    private QueryString periode = new QueryString();
    private QueryString moduleCode = new QueryString();
    private QueryString toetsvorm = new QueryString();
    private QueryString gelegenheid = new QueryString();

    public InputToets(Connection connection) {
        /* Deze methode is de constructor van de class en slaat
         * de connectie met de database op.
         */
        this.connection = connection;
    }

    public boolean insert(String  jaarString,
                          String schooljaarString, String periodeString,
                          String moduleCodeString, String toetsvormString,
                          String gelegenheidString, Integer cesuur,
                          Integer puntenDoorGokKans) {
        /* Deze methode zorgt ervoor dat de meegegeven waarden
         * worden opgeslagen in de database.
         * De meegegeven strings worden in een QueryString object
         * opgeslagen. Een statement wordt aangemaakt, waarna
         * gecheckt wordt of de toets al bestaat.
         * Daarna wordt de query met behulp van de QueryStrings en andere 
         * meegegeven waarden uitgevoerd. Als dit goed verloopt, wordt true
         * gereturned.
         * Bij een exception wordt de Catcher methode uitgevoerd.
         */
        this.jaar.insert(jaarString);
        this.schooljaar.insert(schooljaarString);
        this.periode.insert(periodeString);
        this.moduleCode.insert(moduleCodeString);
        this.toetsvorm.insert(toetsvormString);
        this.gelegenheid.insert(gelegenheidString);
        if (!CheckExist()) {
            try {
                Statement statement = connection.createStatement();
                String query = String.format(
                        this.MODULESQL,
                        this.jaar.getString(),
                        this.schooljaar.getString(),
                        this.periode.getString(),
                        this.moduleCode.getString(),
                        this.toetsvorm.getString(),
                        this.gelegenheid.getString(),
                        cesuur,
                        puntenDoorGokKans
                );
                statement.executeUpdate(query);
                return true;
            } catch (Exception e) {
                return Catcher(e);
            }
        }
        return false;
    }

    private boolean Catcher (Exception e) {
        /* Deze methode zorgt voor het printen van de juiste message,
         * afhankelijk van de soort exception. Hierna wordt false
         * gereturned.
         */
        if (e.getMessage().contains(
                "key value violates unique constraint \"toets_pkey\""
        )) {
            System.out.println("Primary key exists");
        } else if (e.getMessage().contains("violates not-null constraint")) {
            System.out.println("False input vars");
        } else {
            System.err.println(
                    e.getClass().getName() + ": " + e.getMessage()
            );
        }
        return false;
    }

    private Boolean CheckExist(){
        /* Deze methode checkt of de toets al bestaat.
         * Er wordt een statement aangemaakt, waarbij een query wordt
         * uitgevoerd met behulp van de QueryStrings. Hieruit komt een
         * ResultSet. Als de eerste entry hiervan leeg is, wordt
         * false gereturned. Anders wordt true gereturned.
         * Bij een exception wordt altijd false gereturned.
         */
        try {
            Statement statement = connection.createStatement();
            String query = String.format(
                    this.CHECKSQL,
                    this.moduleCode.getString(),
                    this.jaar.getString(),
                    this.schooljaar.getString(),
                    this.periode.getString(),
                    this.toetsvorm.getString(),
                    this.gelegenheid.getString()
            );
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            boolean temp = resultSet.getString(1).isEmpty();
            resultSet.close();
            statement.close();
            return !temp;
        } catch (Exception e) {
            return false;
        }
    }
}
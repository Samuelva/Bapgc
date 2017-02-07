package database;

import java.sql.Connection;
import java.sql.Statement;

class InputModule {
    private final String MODULESQL = "INSERT INTO MODULE" +
            " (ModuleCode, Omschrijving, EC)" +
            " VALUES (%s, %s, %s);";
    private Connection connection;
    private Statement statement;
    private QueryString moduleCode = new QueryString();
    private QueryString omschrijving = new QueryString();

    public InputModule(Connection connection) {
        /* Deze methode is de constructor van de class en slaat
         * de connectie met de database op.
         */
        this.connection = connection;
    }

    public boolean Insert(String moduleCodeString, String omschrijvingString,
                          Integer ec) {
        /* Deze methode zorgt ervoor dat de meegegeven waarden
         * worden opgeslagen in de database.
         * De meegegeven strings worden in een QueryString object
         * opgeslagen. Een statement wordt aangemaakt, waarna de query
         * met behulp van de QueryStrings en andere meegegeven waarden
         * uitgevoerd wordt. Als dit goed verloopt wordt true
         * gereturned.
         * Bij een exception wordt de Catcher methode uitgevoert.
         */
        try {
            this.moduleCode.insert(moduleCodeString);
            this.omschrijving.insert(omschrijvingString);
            this.statement = connection.createStatement();
            String query = String.format(
                    this.MODULESQL,
                    this.moduleCode.getString(),
                    this.omschrijving.getString(),
                    ec
            );
            statement.executeUpdate(query);
            return true;
        } catch (Exception e) {
            return Catcher(e);
        }
    }

    private boolean Catcher (Exception e) {
        /* Deze methode zorgt voor het printen van de juiste message
         * liggend aan wat de exception is. Hierna wordt false
         * gereturned.
         */
        if (e.getMessage().contains(
                "key value violates unique constraint \"module_pkey\""
        )) {
            System.out.println("Primary key exists");
        }
        else if (e.getMessage().contains("violates not-null constraint")) {
            System.out.println("False input vars");
        } else {
            System.err.println(
                    e.getClass().getName() + ": " + e.getMessage()
            );
        }
        return false;
    }
}

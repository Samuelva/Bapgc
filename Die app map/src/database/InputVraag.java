package database;

import java.sql.Connection;
import java.sql.Statement;

class InputVraag {
    /**
     * Deze klasse verzorgt het invullen van de vraag tabel in de
     * database. Hierbij is een sql opgesteld en worden de
     * connectie en de QueryStrings die nodig zijn gedefineerd.
     */
    private final String MODULESQL = "INSERT INTO VRAAG" +
            " (Vraagnummer, MaxScore, ToetsID, Meerekenen)" +
            " VALUES (%s, %s, %s, %s);";
    private Connection connection;
    private QueryString vraagnummer = new QueryString();

    public InputVraag(Connection connection) {
        /**
         * Deze methode is de constructor van de class en slaat
         * de connectie met de database op.
         */
        this.connection = connection;
    }

    public boolean insert(String vraagnummerString, Integer max,
                          Integer toetsID, boolean meerekenen) {
        /**
         * Deze methode zorgt ervoor dat de meegegeven waarden
         * worden opgeslagen in de database.
         * De meegegeven strings worden in een QueryString object
         * opgeslagen. Een statement wordt aangemaakt, waarna de query
         * met behulp van de QueryStrings en andere meegegeven waarden
         * uitgevoerd wordt. Als dit goed verloopt, wordt true
         * gereturned.
         * Bij een exception wordt de Catcher methode uitgevoerd.
         */
        try {
            this.vraagnummer.insert(vraagnummerString);

            Statement statement = connection.createStatement();
            String query = String.format(
                    this.MODULESQL,
                    this.vraagnummer.getString(),
                    max,
                    toetsID,
                    meerekenen
            );
            statement.executeUpdate(query);
            return true;
        } catch (Exception e) {
            return Catcher(e);
        }
    }

    private boolean Catcher (Exception e) {
        /**
         * Deze methode zorgt voor het printen van de juiste message,
         * afhankelijk van de soort exception. Hierna wordt false
         * gereturned.
         */
        if (e.getMessage().contains(
                "key value violates unique constraint \"vraag_pkey\""
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
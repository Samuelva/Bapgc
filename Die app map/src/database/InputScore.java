package database;

import java.sql.Connection;
import java.sql.Statement;

public class InputScore {
    /**
     * Deze klasse verzorgt het invullen van de score tabel in de
     * database. Hierbij is een sql opgesteld en worden de
     * connectie en statement gedefineerd. Ook is er een sql
     * opgesteld die de scores kan updaten.
     */
    private final String SCORESQL1 = "INSERT INTO SCORE" +
            " (StudentID, VraagID, Score)" +
            " VALUES (%s, %s, %s);";
    private final String SCORESQL2 = "UPDATE SCORE" +
            " set Score = %s" +
            " WHERE StudentID=%s AND VraagID=%s;";
    private Connection connection;
    private Statement statement;

    public InputScore(Connection connection) {
        /**
         * Deze methode is de constructor van de class en slaat
         * de connectie met de database op.
         */
        this.connection = connection;
    }

    public boolean Insert(Integer studentID, Integer vraagID, Integer score) {
        /**
         * Deze methode zorgt ervoor dat de meegegeven waarden
         * worden opgeslagen in de database.
         * Een statement wordt aangemaakt, waarna de query
         * met behulp van de meegegeven waarden
         * uitgevoerd wordt. Als dit goed verloopt, wordt true
         * gereturned.
         * Bij een exception wordt de Catcher methode uitgevoerd.
         */
        try {
            this.statement = connection.createStatement();
            String query = String.format(
                    this.SCORESQL1,
                    studentID,
                    vraagID,
                    score
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
                "key value violates unique constraint \"score_pkey\""
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

    public boolean UpdateScore(Integer studentID, Integer vraagID,
                               Integer score) {
        /**
         * Deze methode zorgt voor het updaten van de scores.
         * Eerst wordt een statement aangemaakt, waarna de query
         * wordt uitgevoerd aan de hand van de meegegeven waarden.
         * Als dit goed verloopt, wordt true gereturned.
         * Bij een exception wordt een error geprint en false
         * gereturned.
         */
        try {
            this.statement = connection.createStatement();
            String query = String.format(
                    this.SCORESQL2,
                    score,
                    studentID,
                    vraagID
            );
            statement.executeUpdate(query);
            return true;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }
}
package database;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by Timothy.
 */

class InputVraag {
    private final String MODULESQL = "INSERT INTO VRAAG" +
            " (Vraagnummer, MaxScore, ToetsID, Meerekenen)" +
            " VALUES (%s, %s, %s, %s);";
    private Connection connection;
    private QueryString vraagnummer = new QueryString();

    public InputVraag(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(String vraagnummerString, Integer max,
                          Integer toetsID, boolean meerekenen) {
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
            System.out.println(query);
            statement.executeUpdate(query);
            return true;
        } catch (Exception e) {
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
}

import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by Timothy.
 */
class InputVraag {
    private final String MODULESQL = "INSERT INTO VRAAG" +
            " (Vraagnummer, MaxScore, ToetsID, Gokvraag)" +
            " VALUES (%s, %s, %s,%s);";
    private Connection connection;
    private QueryString vraagnummer = new QueryString();
    private QueryString gokvraag = new QueryString();

    public InputVraag(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(String vraagnummerString, Integer max, Integer toetsID, String gokvraagString) {
        try {
            this.vraagnummer.insert(vraagnummerString);
            this.gokvraag.insert(gokvraagString);

            Statement statement = connection.createStatement();
            String query = String.format(
                    this.MODULESQL,
                    this.vraagnummer.getString(),
                    max,
                    toetsID,
                    this.gokvraag.getString()
            );
            System.out.println(query);
            statement.executeUpdate(query);
            return true;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    private void checkPresence(Statement statement) {
    }
}

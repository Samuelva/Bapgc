import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by Timothy.
 */
class InputModule {
    private final String MODULESQL = "INSERT INTO MODULE" +
            " (ModuleCode, Omschrijving)" +
            " VALUES (%s, %s);";
    private Connection connection;
    private QueryString moduleCode = new QueryString();
    private QueryString omschrijving = new QueryString();

    public InputModule(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(String moduleCodeString, String omschrijvingString) {
        try {
            this.moduleCode.insert(moduleCodeString);
            this.omschrijving.insert(omschrijvingString);

            Statement statement = connection.createStatement();
            String query = String.format(
                    this.MODULESQL,
                    this.moduleCode.getString(),
                    this.omschrijving.getString()
            );
            System.out.println(query);
            statement.executeUpdate(query);
            return true;
        } catch (Exception e) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    private void checkPresence(Statement statement) {
    }
}

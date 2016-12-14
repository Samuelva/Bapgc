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
    private Statement statement;
    private QueryString moduleCode = new QueryString();
    private QueryString omschrijving = new QueryString();

    public InputModule(Connection connection) {
        this.connection = connection;
    }

    public boolean Insert(String moduleCodeString, String omschrijvingString) {
        try {
            this.moduleCode.insert(moduleCodeString);
            this.omschrijving.insert(omschrijvingString);
            this.statement = connection.createStatement();
            String query = String.format(
                    this.MODULESQL,
                    this.moduleCode.getString(),
                    this.omschrijving.getString()
            );
            System.out.println(query);
            statement.executeUpdate(query);
            return true;
        } catch (Exception e) {
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
}

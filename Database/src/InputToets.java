import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by Timothy.
 */
class InputToets {
    private final String MODULESQL = "INSERT INTO TOETS" +
            " (Jaar, Schooljaar, Periode, ModuleID, Toetsvorm, Gelegenheid, Cesuur)" +
            " VALUES (%s, %s, %s, %s, %s, %s, %s);";
    private Connection connection;
    private QueryString jaar = new QueryString();
    private QueryString schooljaar = new QueryString();
    private QueryString periode = new QueryString();
    private QueryString toetsvorm = new QueryString();
    private QueryString gelegenheid = new QueryString();
    private QueryString cesuur = new QueryString();

    public InputToets(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(String jaarString, String schooljaarString,
                          String periodeString, Integer moduleID,
                          String toetsvormString, String gelegenheidString,
                          String cesuurString) {
        try {
            this.jaar.insert(jaarString);
            this.schooljaar.insert(schooljaarString);
            this.periode.insert(periodeString);
            this.toetsvorm.insert(toetsvormString);
            this.gelegenheid.insert(gelegenheidString);
            this.cesuur.insert(cesuurString);

            Statement statement = connection.createStatement();
            String query = String.format(
                    this.MODULESQL,
                    this.jaar.getString(),
                    this.schooljaar.getString(),
                    this.periode.getString(),
                    moduleID,
                    this.toetsvorm.getString(),
                    this.gelegenheid.getString(),
                    this.cesuur.getString()
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

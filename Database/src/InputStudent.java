import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by Timothy.
 */
class InputStudent {
    private final String MODULESQL = "INSERT INTO STUDENT" +
            " (StudentID, Naam, Klas)" +
            " VALUES (%s, %s, %s);";
    private Connection connection;
    private QueryString naam = new QueryString();
    private QueryString klasID = new QueryString();
    private QueryString studentID = new QueryString();

    public InputStudent(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(String studentIDString, String naamString, String klasIDString) {
        try {
            this.studentID.insert(studentIDString);
            this.naam.insert(naamString);
            this.klasID.insert(klasIDString);

            Statement statement = connection.createStatement();
            String query = String.format(
                    this.MODULESQL,
                    this.studentID.getString(),
                    this.naam.getString(),
                    this.klasID.getString()
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

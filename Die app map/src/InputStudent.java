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
    private QueryString klas = new QueryString();

    public InputStudent(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(Integer studentID, String naamString,
                          String klasIDString) {
        try {
            this.naam.insert(naamString);
            this.klas.insert(klasIDString);

            Statement statement = connection.createStatement();
            String query = String.format(
                    this.MODULESQL,
                    studentID,
                    this.naam.getString(),
                    this.klas.getString()
            );
            System.out.println(query);
            statement.executeUpdate(query);
            return true;
        } catch (Exception e) {
            if (e.getMessage().contains(
                    "key value violates unique constraint \"student_pkey\""
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

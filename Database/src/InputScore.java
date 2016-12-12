/**
 * Created by tim.
 */

import java.sql.Connection;
import java.sql.Statement;

public class InputScore {
    private final String SCORESQL1 = "INSERT INTO SCORE" +
            " (StudentID, VraagID)" +
            " VALUES (%s, %s);";
    private final String SCORESQL2 = "UPDATE SCORE" +
            " set Score = %s" +
            " WHERE StudentID=%s AND VraagID=%s;";
    private Connection connection;
    private Statement statement;
    private QueryString studentID = new QueryString();
    private QueryString vraagID = new QueryString();

    public InputScore(Connection connection) {
        this.connection = connection;
    }

    public boolean Insert(String studentIDString, String vraagIDString) {
        try {
            this.studentID.insert(studentIDString);
            this.vraagID.insert(vraagIDString);
            this.statement = connection.createStatement();
            String query = String.format(
                    this.SCORESQL1,
                    this.studentID.getString(),
                    this.vraagID.getString()
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

    public boolean UpdateScore(String studentIDString, String vraagIDString,
                               Integer score) {
        try {
            this.studentID.insert(studentIDString);
            this.vraagID.insert(vraagIDString);
            this.statement = connection.createStatement();
            String query = String.format(
                    this.SCORESQL2,
                    score,
                    this.studentID.getString(),
                    this.vraagID.getString()
            );
            System.out.println(query);
            statement.executeUpdate(query);
            return true;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }
}
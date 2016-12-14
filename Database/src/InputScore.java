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

    public InputScore(Connection connection) {
        this.connection = connection;
    }

    public boolean Insert(Integer studentID, Integer vraagID) {
        try {
            this.statement = connection.createStatement();
            String query = String.format(
                    this.SCORESQL1,
                    studentID,
                    vraagID
            );
            System.out.println(query);
            statement.executeUpdate(query);
            return true;
        } catch (Exception e) {
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
    }

    public boolean UpdateScore(Integer studentID, Integer vraagID,
                               Integer score) {
        try {
            this.statement = connection.createStatement();
            String query = String.format(
                    this.SCORESQL2,
                    score,
                    studentID,
                    vraagID
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
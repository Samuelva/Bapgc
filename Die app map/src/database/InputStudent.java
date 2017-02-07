package database;

import java.sql.Connection;
import java.sql.Statement;

class InputStudent {
    private final String MODULESQL = "INSERT INTO STUDENT" +
            " (StudentID, Naam, Klas)" +
            " VALUES (%s, %s, %s);";
    private Connection connection;
    private QueryString naam = new QueryString();
    private QueryString klas = new QueryString();

    public InputStudent(Connection connection) {
        /* Deze methode is de constructor van de class en slaat
         * de connectie met de database op.
         */
        this.connection = connection;
    }

    public boolean insert(Integer studentID, String naamString,
                          String klasIDString) {
        /* Deze methode zorgt ervoor dat de meegegeven waarden
         * worden opgeslagen in de database.
         * De meegegeven strings worden in een QueryString object
         * opgeslagen. Een statement wordt aangemaakt, waarna de query
         * met behulp van de QueryStrings en andere meegegeven waarden
         * uitgevoerd wordt. Als dit goed verloopt wordt true
         * gereturned.
         * Bij een exception wordt de Catcher methode uitgevoert.
         */
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
            statement.executeUpdate(query);
            return true;
        } catch (Exception e) {
            return Catcher(e);
        }
    }

    private boolean Catcher (Exception e) {
        /* Deze methode zorgt voor het printen van de juiste message
         * liggend aan wat de exception is. Hierna wordt false
         * gereturned.
         */
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

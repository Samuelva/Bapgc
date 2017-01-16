import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by Timothy.
 */
class InputToets {
    private final String MODULESQL = "INSERT INTO TOETS" +
            " (Jaar, Schooljaar, Periode, ModuleCode, Toetsvorm, " +
            " Gelegenheid, Cesuur, PuntenDoorGokKans)" +
            " VALUES (%s, %s, %s, %s, %s, %s, %s, %s);";
    private Connection connection;
    private QueryString jaar = new QueryString();
    private QueryString schooljaar = new QueryString();
    private QueryString periode = new QueryString();
    private QueryString moduleCode = new QueryString();
    private QueryString toetsvorm = new QueryString();
    private QueryString gelegenheid = new QueryString();

    public InputToets(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(String  jaarString,
                          String schooljaarString, String periodeString,
                          String moduleCodeString, String toetsvormString,
                          String gelegenheidString, Integer cesuur,
                          Integer puntenDoorGokKans) {
        try {
            this.jaar.insert(jaarString);
            this.schooljaar.insert(schooljaarString);
            this.periode.insert(periodeString);
            this.moduleCode.insert(moduleCodeString);
            this.toetsvorm.insert(toetsvormString);
            this.gelegenheid.insert(gelegenheidString);

            Statement statement = connection.createStatement();
            String query = String.format(
                    this.MODULESQL,
                    this.jaar.getString(),
                    this.schooljaar.getString(),
                    this.periode.getString(),
                    this.moduleCode.getString(),
                    this.toetsvorm.getString(),
                    this.gelegenheid.getString(),
                    cesuur,
                    puntenDoorGokKans
            );
            System.out.println(query);
            statement.executeUpdate(query);
            return true;
        } catch (Exception e) {
            if (e.getMessage().contains(
                    "key value violates unique constraint \"toets_pkey\""
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

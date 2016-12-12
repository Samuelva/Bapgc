/**
 * Created by timo9 on 7-12-2016.
 */
public class VoorbeeldAanroep {
    public static void main(String[] args) {
        DatabaseConn d = new DatabaseConn();
        d.inputModule("Bapgc", "grid computing");
        d.closeConnection();
    }
}

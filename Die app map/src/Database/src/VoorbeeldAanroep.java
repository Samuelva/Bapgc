/**
 * Created by timo9 on 7-12-2016.
 */
public class VoorbeeldAanroep {
    public static void main(String[] args) {
        DatabaseConn d = new DatabaseConn();
        d.inputModule("BAp", "jemama");
        d.inputToets("2016","1","1",1,"kkk","1","55");
        d.inputVraag("3", 5, 1, "455");
        d.inputStudent("kankwrr","TimIsStom", "Bin3b");
        d.closeConnection();
    }
}

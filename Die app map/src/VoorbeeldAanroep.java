/**
 * Created by timo9 on 7-12-2016.
 */
public class VoorbeeldAanroep {
    public static void main(String[] args) {
        DatabaseConn d = new DatabaseConn();
        d.InputModule("Bapgc", "jemama", 8);
        d.InputToets(1,"2016","3","2","Bapgc","Opdracht","1","70");
        d.InputVraag(1,"3", 10, 1, "ja");
        d.InputStudent(1088947,"Timothy", "Bin3b");
        d.InputScore(1088947, 1);
        d.UpdateScore(1088947, 1, 10);
        d.CloseConnection();
    }
}

package database;

/**
 * Created by timo9 on 7-12-2016.
 */
public class VoorbeeldAanroep {
    public static void main(String[] args) {
        DatabaseConn d = new DatabaseConn();
        d.InputModule("Bapgc", "jemama", 8);
        d.InputToets("2016","3","2","Bapgc","Toets","1",70, 30);
        new Reader("C:/Users/timo9/Documents/Git/Bapgc/Die app map/src/brela_1e_1617.csv", d.GetToetsID("Bapgc", "2016", "3", "2", "1"));
        //d.database.InputVraag("2a", 10, 1, true);
        //d.database.InputStudent(1088948,"Tim", "Bin3b");
        //d.InputScore(1088948, 1, 10);
        //String[][] table = d.GetStudentScores(d.GetToetsID("Bapgc", "2016", "3", "2", "1"));
        //String[] table = d.GetCesuurMaxGok(d.GetToetsID("Bapgc", "2016", "3", "2", "1"));
        //for (String y : table) {
        //    System.out.println(y);
        //}
        d.CloseConnection();
    }
}

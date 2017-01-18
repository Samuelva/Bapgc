package database;

import java.nio.file.Paths;

/**
 * Created by timo9 on 7-12-2016.
 */
public class VoorbeeldAanroep {
    public static void main(String[] args) {
        DatabaseConn d = new DatabaseConn();
        //d.InputModule("Bapgc", "jemama", 8);
        //d.InputToets("2016","3","2","Bapgc","Toets","1",70, 30);
        //new Reader(Paths.get("src/brela_1e_1617.csv").toString(), d.GetToetsID("Bapgc", "2016", "3", "2", "1"));
        //d.database.InputVraag("2a", 10, 1, true);
        //d.database.InputStudent(1088948,"Tim", "Bin3b");
        //d.InputScore(1088948, 1, 10);
        //String[][] table = d.GetStudentScores(d.GetToetsID("Bapgc", "2016", "3", "2", "1"));
        //String[] table = d.GetCesuurMaxGok(d.GetToetsID("Bapgc", "2016", "3", "2", "1", "Toets"));
        //for (String y : table) {
        //    System.out.println(y);
        //}
        //System.out.println(d.GetTable("MODULE").toString());
        d.UpdateCesuurMaxGok(2, new Integer[]{5,100,10});
        d.CloseConnection();
    }
}

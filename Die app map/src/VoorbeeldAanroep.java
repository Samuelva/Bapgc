import java.util.ArrayList;

/**
 * Created by timo9 on 7-12-2016.
 */
public class VoorbeeldAanroep {
    public static void main(String[] args) {
        DatabaseConn d = new DatabaseConn();

        //d.InputModule("Bapgc", "jemama", 8);
        //d.InputToets(1,"2016","3","2","Bapgc","Opdracht","1","70");
        //d.InputVraag(5,"3a", 10, 1, "ja");
        //d.InputStudent(1088948,"Tim", "Bin3b");
        //d.InputScore(1088948, 2);
        //d.UpdateScore(1088948, 2, 7);
        ArrayList<ArrayList<String>> table = d.GetStudentScores(
                "Bapgc", "2016", "3", "2", "1");
        for (ArrayList<String> x : table){
            for (String y : x) {
                System.out.print(y + " " );
            }
            System.out.println();
        }
        d.CloseConnection();
    }
}

package database;

import java.io.File;
import java.nio.file.Paths;

/**
 * Created by timo9 on 7-12-2016.
 */
public class VoorbeeldAanroep {
    public static void main(String[] args) {
        DatabaseConn d = new DatabaseConn();
//        d.InputModule("Bapgc", "jemama", 8);
//        d.InputModule("Bacf", "jepapa", 8);
//        d.InputToets("2017","3","2","Bapgc","Praktijk","2",70, 2);
//        d.InputVraag("2a", 10, 1, true);
//        d.InputVraag("2b", 10, 1, true);
//        d.InputStudent(1088948,"Tim", "Bin3b");
//        d.InputStudent(1000000,"noname", "Bin3b");
//        d.InputScore(1088948, 1, 10);
//        d.InputScore(1088948, 2, 5);
//        d.InputScore(1000000, 1, 10);
//        d.InputScore(1000000, 2, 8);
        //String[][] table = d.GetStudentScores(d.GetToetsID("Bapgc", "2016", "3", "2", "1"));
        //String[] table = d.GetCesuurMaxGok(d.GetToetsID("Bapgc", "2016", "3", "2", "1", "Toets"));
        //Object[][] table = d.GetVragenVanToets(1);
        //String[] array = d.GetModulecodesPerPeriode('2');
//        Object[][] table = d.GetToetsKansen("Bapgc");
//        for (Object[] y : table) {
//            for (Object x : y) {
//                System.out.print(String.format("%s ", x));
//            }
//            System.out.println();
//        }
        //System.out.println(d.GetTable("MODULE").toString());
        //d.UpdateCesuurMaxGok(2, new Integer[]{5,100,10});
        new ModuleReader(new File("src/modulesCSV.csv").getAbsolutePath());
        new Reader(new File("src/brela_1e_1617.csv").getAbsolutePath(), d.GetToetsID("Brela", "2016", "3", "2", "1", "Toets"));
        d.CloseConnection();
    }
}

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
//        String[][] table = d.GetStudentScores(d.GetToetsID("Bapgc", "2016", "3", "2", "1"));
//        String[] table = d.GetCesuurMaxGok(d.GetToetsID("Bapgc", "2016", "3", "2", "1", "Toets"));
//        Object[][] table = d.GetVragenVanToets(1);
//        String[] array = d.GetModulecodesPerPeriode('2');
//        Object[][] table = d.GetToetsKansen("Bapgc");
//        for (Object[] y : table) {
//            for (Object x : y) {
//                System.out.print(String.format("%s ", x));
//            }
//            System.out.println();
//        }
//        System.out.println(d.GetTable("MODULE").toString());
//        d.UpdateCesuurMaxGok(2, new Integer[]{5,100,10});
//        d.ResetTables();
//        d.InputModule("test1", "meh", 1);
//        d.InputModule("test2", "hmm", 1);
//        d.InputToets("2017", "1", "1", "test1", "stage", "1", 45, 0);
//        d.InputToets("2017", "1", "1", "test1", "stage", "2", 45, 0);
//        d.InputToets("2017", "1", "1", "test1", "logboek", "1", 45, 0);
//        d.InputToets("2017", "1", "1", "test2", "stage", "1", 45, 0);
//        d.InputToets("2017", "1", "1", "test1", "stage", "3", 45, 0);
//        d.InputVraag("1", 90, 1, true );
//        d.InputVraag("1", 90, 2, true );
//        d.InputVraag("1", 90, 3, true );
//        d.InputVraag("1", 90, 4, true );
//        d.InputStudent(0, "a", "1");
//        d.InputStudent(1, "a", "1");
//        d.InputStudent(2, "a", "1");
//        d.InputStudent(3, "a", "1");
//        d.InputStudent(4, "a", "1");
//        d.InputStudent(5, "a", "1");
//        d.InputScore(0, 1, 40); //1ste toets: 5.0
//        d.InputScore(0, 2, 80); //1ste toets: 9.0
//        d.InputScore(0, 3, 90); //2de toets: 10.0
//        d.InputScore(0, 4, 90); //module test2
//        d.InputScore(1, 1, 20); //
//        d.InputScore(1, 2, 31);
//        d.InputScore(1, 3, 90);
//        d.InputScore(2, 1, 90);
//        d.InputScore(2, 3, 90);
//        d.InputScore(3, 1, 90);
//        d.InputScore(4, 1, 90);
//        d.InputScore(4, 3, 90);
//        d.InputScore(4, 4, 30);
//        d.InputScore(5, 4, 90);
        /*
        test1: 4 deelnemers, 3 voldoendes, 1 onvoldoende, 75%
        test2: 3 deelnemers, 2 voldoende, 1 onvoldoende, 66.67%
        perdiode 1: 2 deelnemers, 1 voldoende, 1 onvoldoende, 50%
         */

        try {
            new ModuleReader(new File("src/modulesCSV.csv").getAbsolutePath());
            new Reader(new File("src/brela_1e_1617.csv").getAbsolutePath(),
                    d.GetToetsID("bato", "2017", "1",
                            "1", "1", "Theorietoets"));
        } catch (Exception e){
            System.out.println(e);
        }

        d.CloseConnection();
    }
}

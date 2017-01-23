package database;

import database.DatabaseConn;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by midas on 18-1-2017.
 */
public class ModuleReader {
    public ModuleReader(String csvFile){
        //csvFile = "C:/Users/midas/Desktop/Githup/Bapgc/Die app map/src/modulesCSV.csv";
        BufferedReader br = null;
        String line = "";
        ArrayList<String[]> modules = new ArrayList<>();
        List<String> gelegenheid = new ArrayList<String>();

        gelegenheid.add("1");
        gelegenheid.add("2");
        gelegenheid.add("3");

        DatabaseConn d = new DatabaseConn();
        try {
            br = new BufferedReader(new FileReader(csvFile));
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] lijn = line.split(";");
                modules.add(lijn);
                //d.InputModule("Bapgc", "jemama", 8);
                //d.InputToets("2016","3","2","Bapgc","Toets","1",70, 30);
            }
                for (int i = 0; i < modules.size(); i++) {
                    d.InputModule(modules.get(i)[1], modules.get(i)[1], Integer.parseInt(modules.get(i)[2]));
                }
            for (int i = 0; i < modules.size(); i++) {

                //Get moduleCode
                for (int j = 1; j < gelegenheid.size(); j++) {
                    for (int n = 0; n < modules.get(i)[3].toString().split(",").length; n++) {
                        d.InputToets(modules.get(i)[0],
                                modules.get(i)[4],
                                modules.get(i)[5],
                                modules.get(i)[1],
                                modules.get(i)[3].toString().split(",")[n],
                                gelegenheid.get(j),
                                0,
                                0);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        d.CloseConnection();
    }
}

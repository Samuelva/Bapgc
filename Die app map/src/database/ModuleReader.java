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
    /**
     * Deze class maakt connectie met de database. Er wordt een CSV
     * ingelezen en ingevoerd in de database. Eerst worden alle
     * modulen ingevoerd en vervolgens worden alle toetsen uit de CSV
     * file daaraan gekoppeld. Dit wordt gedaan voor alle drie de
     * gelegenheden en voor alle soorten toetsvormen.
     * @param csvFile
     */

    BufferedReader br = null;
    String line = "";
    ArrayList<String[]> modules = new ArrayList<>();
    List<String> gelegenheid = new ArrayList<String>();
    DatabaseConn d = new DatabaseConn();
    public ModuleReader(String csvFile){
        gelegenheid.add("1");
        gelegenheid.add("2");
        gelegenheid.add("3");
        try {
            br = new BufferedReader(new FileReader(csvFile));
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] lijn = line.split(";");
                modules.add(lijn);
            }
            Writer();
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

    public void Writer(){
        for (int i = 0; i < modules.size(); i++) {
            d.InputModule(modules.get(i)[1], modules.get(i)[1],
                    Integer.parseInt(modules.get(i)[2]));
        }
        for (int i = 0; i < modules.size(); i++) {
            for (int j = 0; j < gelegenheid.size(); j++) {
                for (int n = 0; n < modules.get(i)[3].toString().split( ",")
                        .length; n++) {
                    d.InputToets(modules.get(i)[0],
                            modules.get(i)[4].replaceAll(" ", ""),
                            modules.get(i)[5],
                            modules.get(i)[1],
                            modules.get(i)[3].toString().split(",")[n],
                            gelegenheid.get(j),
                            0,
                            0);
                }
            }
        }
    }
}

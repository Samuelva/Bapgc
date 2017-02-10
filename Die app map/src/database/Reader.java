package database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by midas on 16-1-2017.
 */
public class Reader {
    /**
     * Deze class maakt connectie met de database. Er wordt een CSV
     * ingelezen en ingevoerd in de database. Eerst worden alle
     * vragen ingevoerd en vervolgens worden alle resultaten uit de CSV
     * file daaraan gekoppeld.
     * @param csvFile
     * @param toetsID
     */
    BufferedReader br = null;
    String line = "";
    String deelvraag = "";
    String meerekenen = "";
    String punten = "";
    ArrayList<String[]> scores = new ArrayList<>();
    DatabaseConn d = new DatabaseConn();
    public Reader(String csvFile, int toetsID) {
        /**
         * In deze functie wordt de csv ingelezen en wordt elke lijn
         * opgeslagen in een lijst. De "Writer" functie wordt
         * aangeroepen en errors worden opgevangen.
         */
        try {
            br = new BufferedReader(new FileReader(csvFile));
            deelvraag = br.readLine();
            meerekenen = br.readLine();
            punten = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] lijn = line.split(";");
                scores.add(lijn);
            }
            Writer(toetsID);
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
    public void Writer(int toetsID){
        /**
         * In deze functie worden eerste de vragen en deelvragen
         * toegevoegd aan de database en worden vervolgens de
         * juiste studenten en hun aantal punten per vraag eraan
         * gekoppeld.
         */
        for(int i=2; i<deelvraag.split(";").length; i++){
            d.InputVraag(
                    deelvraag.split(";")[i],
                    Integer.parseInt(punten.split(";")[i]),
                    toetsID,
                    Boolean.valueOf(meerekenen.split(";")[i]));
        }
        for(int i=0; i<scores.size(); i++){
            d.InputStudent(
                    Integer.parseInt(scores.get(i)[0]),
                    "Naam",
                    scores.get(i)[1]);
            for(int j=2; j<deelvraag.split(";").length; j++){
                int VraagID = d.GetVraagID(deelvraag.split(";")[j],
                        toetsID);
                d.InputScore(
                        Integer.parseInt(scores.get(i)[0]),
                        VraagID,
                        Integer.parseInt(scores.get(i)[j]));
            }
        }
    }
}

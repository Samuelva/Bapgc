import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by midas on 16-1-2017.
 */
public class Reader {

    public static void main(String[] args) {

        String csvFile = "/Users/midas/Dropbox/csvInlaad/src/brela_1e_1617.csv";
        BufferedReader br = null;
        String line = "";
        String deelvraag = "";
        String gokvraag = "";
        String punten = "";
        ArrayList<String[]> scores = new ArrayList<>();

        String moduleCode = "Bapgc";
        String jaar = "2016";
        String schoolJaar = "3";
        String periode = "2";
        String gelegenheid = "1";

        DatabaseConn d = new DatabaseConn();


        int ToetsID = d.GetToetsID(moduleCode, jaar, schoolJaar,
                periode, gelegenheid);

        try {

            br = new BufferedReader(new FileReader(csvFile));

            deelvraag = br.readLine();
            gokvraag = br.readLine();
            punten = br.readLine();

            while ((line = br.readLine()) != null) {

                String[] lijn = line.split(";");

                scores.add(lijn);
                //System.out.println(lijn[0]);
            }
            System.out.println(deelvraag.split(";").length);
            for(int i=1; i<deelvraag.split(";").length; i++){

                d.InputVraag(
                        deelvraag.split(";")[i],
                        Integer.parseInt(punten.split(";")[i]),
                        ToetsID,
                        Boolean.valueOf(gokvraag.split(";")[i]),
                        true);
            }


            for(int i=0; i<scores.size(); i++){
                for(int j=1; j<deelvraag.split(";").length; j++){

                    int VraagID = d.GetVraagID(deelvraag.split(";")[j], ToetsID);
                    d.UpdateScore(
                            Integer.parseInt(scores.get(i)[0]),
                            VraagID,
                            Integer.parseInt(scores.get(i)[j]));
                }


            }

            //System.out.println(scores);

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

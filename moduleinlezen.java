
package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class moduleInlezen {
    public static void main(String [] args) throws IOException{
        BufferedReader br = null;
        
      
        String Line;
        br = new BufferedReader(new FileReader("c://modulesCSV.csv"));
            
        while ((Line = br.readLine()) != null){
               System.out.println(crunchifyCSVtoArrayList(Line));
           }
    }

    public static ArrayList<String> crunchifyCSVtoArrayList(String moduleCSV) {
          ArrayList<String> module = new ArrayList<String>();
		
	if (moduleCSV != null) {
		String[] splitData = moduleCSV.split("\\s*,\\s*");
		for (int i = 0; i < splitData.length; i++) {
			if (!(splitData[i] == null) || !(splitData[i].length() == 0)) {
				module.add(splitData[i].trim());
			}
		}
	}
		
	return module;
	}
	
    }
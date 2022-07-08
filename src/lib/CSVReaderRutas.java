package lib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVReaderRutas {

    public static ArrayList<String[]> read(String path) {
        String csvFile = path;
        String line = "";
        String cvsSplitBy = ",";
        String csvSplitField = ";";
        ArrayList<String[]> routes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {
                String data = line.split(cvsSplitBy)[0].replace("{", ""); 
                String[] airportFields = data.split(csvSplitField);
                routes.add(airportFields);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
		return routes;
    }
    
}
package lib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVReaderAeropuertos {

    public static ArrayList<String[]> read() {
        String csvFile = "./src/resources/Aeropuertos.csv";
        String line = "";
        String cvsSplitBy = ",";
        String csvSplitField = ";";
        ArrayList<String[]> airports = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {
                String data = line.split(cvsSplitBy)[0];
                String[] airportFields = data.split(csvSplitField);
                airports.add(airportFields);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
		return airports;
    }
    
}
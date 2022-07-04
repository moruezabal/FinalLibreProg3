package lib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVReaderReservas {

    public static ArrayList<String[]> read() {
        String csvFile = "./src/resources/Reservas.csv";
        String line = "";
        String cvsSplitBy = ",";
        String csvSplitField = ";";
        ArrayList<String[]> reservations = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {
                String data = line.split(cvsSplitBy)[0];
                String[] reservationFields = data.split(csvSplitField);
                reservations.add(reservationFields);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
		return reservations;
    }
    
}
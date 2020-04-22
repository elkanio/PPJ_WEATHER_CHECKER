package ppj.weather.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ppj.weather.services.RowsFromCSV;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CSVConfiguration {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.mm.yyyy");
    private static final Logger log = LoggerFactory.getLogger(CSVConfiguration.class);



    public List<RowsFromCSV> parseFile(String csvFileContent) {
        //beru po radcich
        String[] lines = csvFileContent.split("\n");

        List<RowsFromCSV> rowsFromCSVS = new ArrayList<>();
        for (String line : lines) {
            try {
                RowsFromCSV rowsFromCSV = parseLine(line);
                rowsFromCSVS.add(rowsFromCSV);
            }catch (Exception e){
                log.error(e.getMessage());
            }


        }
        return rowsFromCSVS;
    }

    public RowsFromCSV parseLine(String line) throws Exception {
        String[] values = line.split(";");
        RowsFromCSV rowsFromCSV = new RowsFromCSV();

        try {
            rowsFromCSV.setDate(DATE_FORMAT.parse(values[0]));
            rowsFromCSV.setHumidity(Double.parseDouble(values[1].trim()));

            rowsFromCSV.setPrecipitation(Double.parseDouble(values[2].trim()));
            rowsFromCSV.setTemperature(Double.parseDouble(values[3].trim()));
            return rowsFromCSV;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public String getHeaderForCSV() {
        return "Date;Humidity;Precipitation;Temperature \n";
    }

}

package com.tismenetski.coronavirustrackerapp.services;

import com.tismenetski.coronavirustrackerapp.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


@Service
public class CoronaVirusDataService {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";



    private List<LocationStats> allStats=new ArrayList<>();

    @Scheduled(cron = "* * 1 * * *")
    //Fetching Data from the web
    @PostConstruct //would be executed once the CoronaVirusDataService instantiated
    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationStats> newStats=new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request=HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();
        HttpResponse<String> httpResponse= client.send(request, HttpResponse.BodyHandlers.ofString());

        StringReader csvBodyReader = new StringReader(httpResponse.body());
        //System.out.println(httpResponse.body());

        //Reader in = new FileReader("path/to/file.csv");
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        for (CSVRecord record : records) {
            LocationStats locationStat=new LocationStats();
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));

            //Latest cases
            int latestCases= Integer.parseInt(record.get(record.size()-1));

            //Previous day cases
            int prevDayCases= Integer.parseInt(record.get(record.size()-2));


            //Total Cases
            locationStat.setLatestTotalCases(latestCases);


            locationStat.setDiffFromPrevDay(latestCases-prevDayCases);
            //System.out.println(locationStat);
            newStats.add(locationStat);
        }
        this.allStats=newStats;
    }

    public List<LocationStats> getAllStats() {
        return allStats;
    }

    public void setAllStats(List<LocationStats> allStats) {
        this.allStats = allStats;
    }
}

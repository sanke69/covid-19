package fr.covid19.data.sources.http;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import fr.java.utils.SimpleCsvParser;

import fr.covid19.data.sources.api.OutbreakDataSource;
import fr.geodesic.referential.api.countries.Country;
import fr.outbreak.api.Outbreak;
import fr.outbreak.api.Outbreak.Population;

public class NewYorkTimes extends OutbreakDataSource<NewYorkTimes.Record> {
	public static final LocalDate INITIAL_RECORD_DATE = LocalDate.of(2020, 1, 1);

	static record Record(LocalDate last_update, 
						 int FIPS, String county, String state, 
						 long confirmed, long deaths) implements Outbreak.Report {

		@Override public Record.Type    getType()    { return Record.Type.Variation; }
		@Override public LocalDate      getDate()    { return last_update; }
		@Override public Country        getCountry() { return Country.US; }

		@Override public Optional<Long> get(Population _population) {
			return switch(_population) {
			case Susceptible -> Optional.empty();
			case Infected    -> Optional.of( confirmed );
			case Recovered   -> Optional.empty();
			case Immuned     -> Optional.empty();
			case Dead        -> Optional.of( deaths );
			case Alive       -> Optional.empty();
			case Total       -> Optional.empty();
			};
		}

	}

    public static void main(String[] args) throws IOException {
    	NewYorkTimes src = new NewYorkTimes();
    	for(Outbreak.Report r : src.getRecords())
    		System.out.println(r);
    }

	protected NewYorkTimes() {
		super(Paths.get("/ssd/share/data/covid-19/nyt"));
	}
	protected NewYorkTimes(Path _storagePath, Path _downloadPath) {
		super(_storagePath, _downloadPath);
	}

	// us-states.csv - us.csv
	@Override public URL 	url() 				{ return urlNoException("https://raw.githubusercontent.com/nytimes/covid-19-data/master/us-counties.csv"); }
	@Override public String filePrefix() 		{ return "covid-19"; }
	@Override public String fileExtension() 	{ return "csv"; }

	public Collection<NewYorkTimes.Record> 	parse(Path _file)   {
		Collection<NewYorkTimes.Record> reports = new ArrayList<NewYorkTimes.Record>();
		try {
	        Scanner        scanner = new Scanner(_file.toFile());

	        // Skip Headers
	        scanner.nextLine();

	        while(scanner.hasNext()) {
	            List<String> line = SimpleCsvParser.parseLine(scanner.nextLine());

	            LocalDate last_update = LocalDate.parse(line.get( 0), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	            String    county      = line.get( 1);
	            String    state       = line.get( 2);
	            int       FIPS        = line.get( 3).isBlank() ? -1 : Integer . valueOf( line.get( 3) );
	            long      cases       = line.get( 4).isBlank() ? -1 : Long    . valueOf( line.get( 4) );
	            long      deaths      = line.get( 5).isBlank() ? -1 : Long    . valueOf( line.get( 5) );

	            Record record = new Record(last_update,
						            		FIPS, county, state, 
						            		cases, deaths);
	            reports.add( record );
	        }

	        scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return reports;
	}

}

/**
 * OutBreak API - Covid-19
 * Copyright (C) 2020-?XYZ  Steve PECHBERTI <steve.pechberti@laposte.net>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.covid19.data.sources.http;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

public class JohnsHopkinsUniversity extends OutbreakDataSource<JohnsHopkinsUniversity.Record> {
	public static final LocalDate INITIAL_RECORD_DATE = LocalDate.of(2020, 1, 1);

	public static record Record(int FIPS, 
						 String        city, String province, Country country, 
						 double        latitude, double longitude, 
						 LocalDateTime last_update, 
						 long          confirmed, long deaths, long recovered, long active, 
						 String        combined_key) implements Outbreak.Report {

		@Override public Record.Type    getType()    { return Record.Type.Variation; }
		@Override public LocalDate      getDate()    { return last_update.toLocalDate(); }
		@Override public Country        getCountry() { return country; }

		@Override public Optional<Long> get(Population _population) {
			return switch(_population) {
			case Susceptible -> Optional.empty();
			case Infected    -> Optional.of( confirmed );
			case Recovered   -> Optional.of( recovered );
			case Immuned     -> Optional.empty();
			case Dead        -> Optional.of( deaths );
			case Alive       -> Optional.empty();
			case Total       -> Optional.empty();
			};
		}

	}

    public static void main(String[] args) throws IOException {
    	JohnsHopkinsUniversity src = new JohnsHopkinsUniversity();
    	for(Outbreak.Report r : src.getRecords())
    		System.out.println(r);
    }

	protected JohnsHopkinsUniversity() {
		super(Paths.get("/ssd/share/data/covid-19/jhu-csse"));
	}

	@Override public URL 	url() 			{ return latestURL(); }
	@Override public String filePrefix() 	{ return "covid-19"; }
	@Override public String fileExtension() { return "csv"; }

	public Collection<JohnsHopkinsUniversity.Record>	parse(Path _file)   {
		Collection<JohnsHopkinsUniversity.Record> reports = new ArrayList<JohnsHopkinsUniversity.Record>();
		try {
	        Scanner        scanner = new Scanner(_file.toFile());

	        // Skip Headers
	        scanner.nextLine();

	        while(scanner.hasNext()) {
	            List<String> line = SimpleCsvParser.parseLine(scanner.nextLine());

	            int           FIPS         = line.get( 0).isBlank() ? -1 : Integer . valueOf( line.get( 0) );
	            String        city         = line.get( 1);
	            String        province     = line.get( 2);
	            Country       country      = Country.ofEnglish( line.get( 3) );
	            LocalDateTime last_update  = LocalDateTime.parse(line.get( 4), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	            double        latitude     = line.get( 5).isBlank() ? -1 : Double  . valueOf( line.get( 5) );
	            double        longitude    = line.get( 6).isBlank() ? -1 : Double  . valueOf( line.get( 6) );
	            long          confirmed    = line.get( 7).isBlank() ? -1 : Long    . valueOf( line.get( 7) );
	            long          deaths       = line.get( 8).isBlank() ? -1 : Long    . valueOf( line.get( 8) );
	            long          recovered    = line.get( 9).isBlank() ? -1 : Long    . valueOf( line.get( 9) );
	            long          active       = line.get(10).isBlank() ? -1 : Long    . valueOf( line.get(10) );
	            String        combined_key = line.get(11);

	            Record record = new Record(FIPS,
	            					  city, province, country, 
	            					  longitude, latitude, 
	            					  last_update,
	            					  confirmed, deaths, recovered, active,
	            					  combined_key);
	            reports.add( record );
	        }

	        scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return reports;
	}

	private URL				latestURL() {
		final String         urlBase = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/";
		DateTimeFormatter    format  = DateTimeFormatter.ofPattern("MM-dd-yyyy");
		LocalDate            latest = LocalDate.now();
		URL                  url     = null;

		try {
			url = new URL(urlBase + latest.format(format) + ".csv");

			while( ! validURL(url) && latest.isAfter(INITIAL_RECORD_DATE) ) {
				url    = new URL(urlBase + latest.format(format) + ".csv");
				latest = latest.minusDays(1);
			}

		} catch (IOException e) { return null; } 

		return url;
	}
	private boolean 		validURL(URL _url) {
		if(_url == null)
			return false;

		try {
			HttpURLConnection 
			huc = (HttpURLConnection) _url.openConnection();
			huc . setRequestMethod("HEAD");
			huc . connect();

			return (huc.getResponseCode() == HttpURLConnection.HTTP_OK);
		
		} catch (IOException e) { return false; } 
	}

}

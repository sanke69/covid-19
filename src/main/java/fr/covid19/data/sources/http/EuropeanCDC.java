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
 */package fr.covid19.data.sources.http;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import fr.covid19.data.sources.api.OutbreakDataSource;
import fr.covid19.data.utils.JNet;
import fr.geodesic.referential.api.countries.Country;
import fr.outbreak.api.Outbreak;
import fr.outbreak.api.Outbreak.Population;
import fr.reporting.api.Report;

public class EuropeanCDC extends OutbreakDataSource<EuropeanCDC.Record> {
	public static enum   Type { 
		CSV  ("https://opendata.ecdc.europa.eu/covid19/casedistribution/csv/", "csv"), 
		JSON ("https://opendata.ecdc.europa.eu/covid19/casedistribution/json/", "json");

		String url, ext;

		Type(String _url, String _ext) {
			url = _url;
			ext = _ext;
		}

		public URL url() {
			try { return new URL(url);
			} catch (IOException e) { return null; }
		}
		public String extension() {
			return ext;
		}

	}
	public static record Record(int day, int month, int year,
							  String geoId, String geoCode, String territory,
							  long population2018, long nbCases, long nbDeaths) implements Outbreak.Report {

		@Override public Report.Type   	getType()    { return fr.reporting.api.Report.Type.Variation; }
		@Override public LocalDate  	getDate()    { return LocalDate.of(year, month, day); }
		@Override public Country		getCountry() { return Country.of( geoId ); }

		@Override public Optional<Long> get(Population _population) {
			return switch(_population) {
			case Susceptible -> Optional.empty();
			case Infected    -> Optional.of( nbCases );
			case Recovered   -> Optional.empty();
			case Immuned     -> Optional.empty();
			case Dead        -> Optional.of( nbDeaths );
			case Alive       -> Optional.empty();
			case Total       -> Optional.of( population2018 );
			};
		}

	}

	private final Type usedVersion;

	public EuropeanCDC() {
		super(Paths.get("/ssd/share/data/covid-19/ecdc"));
		usedVersion = Type.JSON;
	}

	@Override public URL 	url() 			{ return usedVersion.url(); }
	@Override public String filePrefix() 	{ return "covid-19"; }
	@Override public String fileExtension() { return usedVersion.extension(); }

	public Collection<EuropeanCDC.Record> parse(Path _file) {
		LocalDateTime     today  = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss.SSS");
		Path              file   = downloadPath().resolve("covid19" + '-' + today.format(format) + '.' + usedVersion.extension());

		try {
			if(JNet.wget(usedVersion.url(), file)) {
				if( storagePath().toFile().exists() )
					persistentStore(file);

				return switch(usedVersion) {
				case CSV  -> throw new RuntimeException("Not yet in this release..."); // ParserCSV.parse(file);
				case JSON -> ParserJSON.parse(file);
				};
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Collections.emptyList();
	}

	public static class ParserJSON {
	
		public static Collection<EuropeanCDC.Record> parse(Path _file) throws IOException {
			byte[] jsonData = Files.readAllBytes(_file);
	
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule module = new SimpleModule("OpenDataCovid19Deserializer", new Version(0, 0, 1, null, null, null));
			module.addDeserializer(EuropeanCDC.Record.class, new EcdcReportDeserializer());
			mapper.registerModule(module);
			
			JsonNode root    = mapper.readTree(jsonData);
			JsonNode records = root.findValue("records");
			
			EuropeanCDC.Record[]     reportArray = mapper.treeToValue(records, EuropeanCDC.Record[].class);
			List<EuropeanCDC.Record> reportList  = Arrays.asList( reportArray );
			
			return reportList;
		}
		public static void                   		 write(Collection<EuropeanCDC.Record> _data, Path _file) throws IOException {
	    	ObjectMapper mapper = new ObjectMapper();
	    	SimpleModule module = new SimpleModule("OpenDataCovid19Serializer", new Version(1, 0, 0, null, null, null));
	    	module.addSerializer(EuropeanCDC.Record.class, new EcdcReportSerializer());
	    	mapper.registerModule(module);
	
	    	EuropeanCDC.Record report     = new EuropeanCDC.Record(0,0,0,"","","",0,0,0);
	    	String             reportJson = mapper.writeValueAsString(report);
	    	
	    	System.out.println(reportJson);
		}
	
		static class EcdcReportSerializer   extends StdSerializer<EuropeanCDC.Record> {
			private static final long serialVersionUID = 1333672778155321928L;
	
			public EcdcReportSerializer() {
		        this(null);
		    }
	
		    public EcdcReportSerializer(Class<EuropeanCDC.Record> t) {
		        super(t);
		    }
	
		    @Override
		    public void serialize(EuropeanCDC.Record report, JsonGenerator jsonGenerator, SerializerProvider serializer) throws JsonGenerationException, IOException {
		    	String date = String.format("%02d/%02d/%04d", report.day(), report.month(), report.year());
	
		        jsonGenerator.writeStartObject();
			    jsonGenerator.writeStringField("dateRep", 					date);
			    jsonGenerator.writeStringField("day",						String.valueOf( report.day() ));
			    jsonGenerator.writeStringField("month", 					String.valueOf( report.month() ));
			    jsonGenerator.writeStringField("year", 						String.valueOf( report.year() ));
			    jsonGenerator.writeStringField("cases", 					String.valueOf( report.nbCases() ));
			    jsonGenerator.writeStringField("deaths", 					String.valueOf( report.nbDeaths() ));
			    jsonGenerator.writeStringField("countriesAndTerritories", 	report.territory());
			    jsonGenerator.writeStringField("geoId", 					report.geoId());
			    jsonGenerator.writeStringField("countryterritoryCode", 		report.geoCode());
			    jsonGenerator.writeStringField("popData2018", 				String.valueOf( report.population2018() ));
		        jsonGenerator.writeEndObject();
		    }
	
		}
		static class EcdcReportDeserializer extends StdDeserializer<EuropeanCDC.Record> {
			private static final long serialVersionUID = 6819699565455916090L;
	
			public EcdcReportDeserializer() {
		        this(null);
		    }
		 
		    public EcdcReportDeserializer(Class<?> vc) {
		        super(vc);
		    }
		 
		    @Override
		    public EuropeanCDC.Record deserialize(JsonParser parser, DeserializationContext deserializer) throws JsonProcessingException, IOException {
		        ObjectCodec codec  = parser.getCodec();
		        JsonNode    node   = codec.readTree(parser);
	
		        // try catch block
		        int    day       = node.get("day")   					. asInt();
		        int    month     = node.get("month") 					. asInt();
		        int    year      = node.get("year")  					. asInt();
		        long   pop2k18   = node.get("popData2018")  			. asLong();
		        long   cases     = node.get("cases")  					. asLong();
		        long   deaths    = node.get("deaths")  					. asLong();
		        String geoId     = node.get("geoId")					. asText();
		        String geoCode   = node.get("countryterritoryCode")		. asText();
		        String territory = node.get("countriesAndTerritories")	. asText();
	
		        return new EuropeanCDC.Record(day, month, year, geoId, geoCode, territory, pop2k18, cases, deaths);
		    }
	
		}
	
	}

}

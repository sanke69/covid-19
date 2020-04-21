/**
 * OutBreak API
 * Copyright (C) 2007-?XYZ  Steve PECHBERTI <steve.pechberti@laposte.net>
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
package fr.covid19.data.sources.ecdc.format.json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

import fr.covid19.data.sources.ecdc.format.DailyRecord;
import fr.covid19.data.sources.ecdc.format.DailyReport;

public class ParserJSON {

	public static Collection<DailyReport> parse(Path _file) throws IOException {
		byte[] jsonData = Files.readAllBytes(_file);

		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule("OpenDataCovid19Deserializer", new Version(0, 0, 1, null, null, null));
		module.addDeserializer(DailyReport.class, new EcdReportDeserializer());
		mapper.registerModule(module);
		
		JsonNode root    = mapper.readTree(jsonData);
		JsonNode records = root.findValue("records");
		
		DailyReport[]     reportArray = mapper.treeToValue(records, DailyReport[].class);
		List<DailyReport> reportList  = Arrays.asList( reportArray );
		
		return reportList;
	}

	public static void write(Collection<DailyReport> _data, Path _file) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
    	SimpleModule module = new SimpleModule("OpenDataCovid19Serializer", new Version(1, 0, 0, null, null, null));
    	module.addSerializer(DailyReport.class, new EcdcReportSerializer());
    	mapper.registerModule(module);

    	DailyReport report = new DailyRecord(0,0,0,"","","",0,0,0);
    	String reportJson = mapper.writeValueAsString(report);
    	
    	System.out.println(reportJson);
	}

	static class EcdcReportSerializer extends StdSerializer<DailyReport> {
		private static final long serialVersionUID = 1333672778155321928L;

		public EcdcReportSerializer() {
	        this(null);
	    }

	    public EcdcReportSerializer(Class<DailyReport> t) {
	        super(t);
	    }

	    @Override
	    public void serialize(DailyReport report, JsonGenerator jsonGenerator, SerializerProvider serializer) throws JsonGenerationException, IOException {
	    	String date = String.format("%02d/%02d/%04d", report.getDay(), report.getMonth(), report.getYear());

	        jsonGenerator.writeStartObject();
		    jsonGenerator.writeStringField("dateRep", 					date);
		    jsonGenerator.writeStringField("day",						String.valueOf( report.getDay() ));
		    jsonGenerator.writeStringField("month", 					String.valueOf( report.getMonth() ));
		    jsonGenerator.writeStringField("year", 						String.valueOf( report.getYear() ));
		    jsonGenerator.writeStringField("cases", 					String.valueOf( report.getInfected() ));
		    jsonGenerator.writeStringField("deaths", 					String.valueOf( report.getDead() ));
		    jsonGenerator.writeStringField("countriesAndTerritories", 	report.getTerritory());
		    jsonGenerator.writeStringField("geoId", 					report.getGeoId());
		    jsonGenerator.writeStringField("countryterritoryCode", 		report.getGeoCode());
		    jsonGenerator.writeStringField("popData2018", 				String.valueOf( report.getPopulation2018() ));
	        jsonGenerator.writeEndObject();
	    }

	}
	static class EcdReportDeserializer extends StdDeserializer<DailyReport> {
		private static final long serialVersionUID = 6819699565455916090L;

		public EcdReportDeserializer() {
	        this(null);
	    }
	 
	    public EcdReportDeserializer(Class<?> vc) {
	        super(vc);
	    }
	 
	    @Override
	    public DailyReport deserialize(JsonParser parser, DeserializationContext deserializer) throws JsonProcessingException, IOException {
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

	        return new DailyRecord(day, month, year, geoId, geoCode, territory, pop2k18, cases, deaths);
	    }

	}

}

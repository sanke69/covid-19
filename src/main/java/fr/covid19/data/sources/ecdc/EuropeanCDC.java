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
package fr.covid19.data.sources.ecdc;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;

import fr.covid19.data.sources.ecdc.format.DailyReport;
import fr.covid19.data.sources.ecdc.format.json.ParserJSON;
import fr.covid19.data.utils.JNet;

public class EuropeanCDC {
	public static enum Version { 
		CSV  ("https://opendata.ecdc.europa.eu/covid19/casedistribution/csv/", "csv"), 
		JSON ("https://opendata.ecdc.europa.eu/covid19/casedistribution/json/", "json");

		String url, ext;

		Version(String _url, String _ext) {
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

	public static final String ECDC_RECORDS_PATH     = "/ssd/share/data/covid19/ecdc";

	public static Version usedVersion;
	public static Path    downloadPath;

	static {
		usedVersion  = Version.JSON;
		downloadPath = Paths.get(ECDC_RECORDS_PATH);
	}

	public static Collection<DailyReport> getDailyReports() {
		LocalDate         today  = LocalDate.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Path              file   = downloadPath.resolve("covid19" + '-' + today.format(format) + '.' + usedVersion.extension());

		try {
			if(JNet.wget(usedVersion.url(), file)) {
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

}

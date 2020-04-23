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
 */package fr.covid19.data.sources.ecdc;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;

import fr.covid19.data.sources.ecdc.format.DailyReport;
import fr.covid19.data.sources.ecdc.format.json.ParserJSON;
import fr.covid19.data.utils.JNet;

public class EuropeanCDC {
	public static final String ECDC_RECORDS_PATH = "/ssd/share/data/covid19/ecdc";

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


	public static Version usedVersion;
	public static Path    downloadPath, storagePath;

	static {
		usedVersion  = Version.JSON;
		downloadPath = Paths.get( System.getProperty("java.io.tmpdir") );
		storagePath  = Paths.get(ECDC_RECORDS_PATH);
	}

	public static Collection<DailyReport> getDailyReports() {
		LocalDateTime     today  = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss.SSS");
		Path              file   = downloadPath.resolve("covid19" + '-' + today.format(format) + '.' + usedVersion.extension());

		try {
			if(JNet.wget(usedVersion.url(), file)) {
				if( storagePath.toFile().exists() )
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

	public static void persistentStore(Path _file) {
		LocalDate         today  = LocalDate.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Path              file   = downloadPath.resolve("covid19" + '-' + today.format(format) + '.' + usedVersion.extension());

		try {
			if(file.toFile().exists()) {
				boolean sameFile = (Files.mismatch(file, _file) == -1);
				
				if(sameFile)
					return ;
				else
					Files.copy(_file, file, StandardCopyOption.COPY_ATTRIBUTES);
			}
			else
				Files.copy(_file, file, StandardCopyOption.COPY_ATTRIBUTES);

		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}

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
package fr.covid19.data.sources.api;

import java.nio.file.Path;
import java.nio.file.Paths;

import fr.outbreak.api.Outbreak;
import fr.reporting.sdk.utils.ReportDownloader;

public abstract class OutbreakDataSource<OR extends Outbreak.Report> extends ReportDownloader<OR> {

	protected OutbreakDataSource() {
		this(Paths.get("/ssd/share/data/covid-19"));
	}
	protected OutbreakDataSource(Path _storagePath) {
		this(_storagePath, null);
	}
	protected OutbreakDataSource(Path _storagePath, Path _downloadPath) {
		super(_storagePath, _downloadPath);
	}

}

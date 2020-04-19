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
package fr.covid19.data;

import java.util.Collection;

import fr.covid19.data.sources.ecdc.format.DailyReport;
import fr.geodesic.referential.api.countries.Country;
import fr.outbreak.api.Outbreak.Population;
import fr.outbreak.api.database.SimpleOutbreakDataBase;

public class WorldDataCovid19 extends SimpleOutbreakDataBase {
	Collection<DailyReport> reports;

	public WorldDataCovid19(Collection<DailyReport> _reports) {
		super(_reports);
		reports = _reports;
	}

	public long getPopulation(Country _country) {
		return reports.stream() .filter(dr -> dr.getCountry() == _country)
								.findAny().get().get(Population.Total).orElse(-1L);
	}

}

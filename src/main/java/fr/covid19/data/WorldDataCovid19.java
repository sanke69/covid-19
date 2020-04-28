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
 */package fr.covid19.data;

import java.util.Collection;

import fr.geodesic.referential.api.countries.Country;
import fr.outbreak.api.Outbreak;
import fr.outbreak.api.Outbreak.Population;
import fr.outbreak.sdk.data.SimpleOutbreakDataBase;

public class WorldDataCovid19<OR extends Outbreak.Report> extends SimpleOutbreakDataBase {
	Collection<Outbreak.Report> reports;

	public WorldDataCovid19(Collection<Outbreak.Report> _reports) {
		super(_reports);
		reports = _reports;
	}

	public long getPopulation(Country _country) {
		return reports.stream() .filter(dr -> dr.getCountry() == _country)
								.findAny().get().get(Population.Total).orElse(-1L);
	}

}

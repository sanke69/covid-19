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
package fr.covid19.data.sources.ecdc.format;

import fr.outbreak.api.Outbreak.KpiType;

public record DailyRecord(int day, int month, int year,
						  String geoId, String geoCode, String territory,
						  long population2018, long nbCases, long nbDeaths) implements DailyReport {

	@Override public KpiType 	getType() 			{ return KpiType.Variation; }

	@Override public int 		getDay() 			{ return day(); }
	@Override public int 		getMonth() 			{ return month(); }
	@Override public int 		getYear() 			{ return year(); }

	@Override public String 	getGeoId() 			{ return geoId(); }
	@Override public String 	getGeoCode() 		{ return geoCode(); }
	@Override public String 	getTerritory() 		{ return territory(); }

	@Override public long 		getPopulation2018() { return population2018(); }
	@Override public long 		getInfected() 		{ return nbCases; }
	@Override public long 		getDead() 			{ return nbDeaths; }

}

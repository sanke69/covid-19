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

public class DailyReportBean implements DailyReport {
	private int    day;
	private int    month;
	private int    year;
	private String geoId;
	private String geoCode;
	private String territory;
	private long   population2018;
	private long   nbCases;
	private long   nbDeaths;

	@Override public KpiType 	getType() 						{ return KpiType.Variation; }

	public void 				setDay(int _day) 				{ day = _day; }
	@Override public int 		getDay() 						{ return day; }
	public void 				setMonth(int _month) 			{ month = _month; }
	@Override public int 		getMonth() 						{ return month; }
	public void 				setYear(int _year) 				{ year = _year; }
	@Override public int 		getYear() 						{ return year; }

	public void 				setGeoId(String _id) 			{ geoId = _id; }
	@Override public String 	getGeoId() 						{ return geoId; }
	public void 				setGeoCode(String _code)		{ geoCode = _code; }
	@Override public String 	getGeoCode()	 				{ return geoCode; }
	public void 				setTerritory(String _territory)	{ territory = _territory; }
	@Override public String 	getTerritory()	 				{ return territory; }

	public void 				setPopulation2018(long _pop) 	{ population2018 = _pop; }
	@Override public long 		getPopulation2018() 			{ return population2018; }
	public void 				setCases(long _cases) 			{ nbCases = _cases; }
	@Override public long 		getInfected() 					{ return nbCases; }
	public void 				setDeaths(long _deaths) 		{ nbDeaths = _deaths; }
	@Override public long 		getDead() 						{ return nbDeaths; }

}

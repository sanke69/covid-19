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
 */package fr.covid19.data.sources.ecdc.format;

import java.time.LocalDate;

import fr.geodesic.referential.api.countries.Country;
import fr.outbreak.api.Outbreak.KpiType;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DailyReportBeanFx implements DailyReport {
	private IntegerProperty 					day				= new SimpleIntegerProperty();
	private IntegerProperty 					month			= new SimpleIntegerProperty();
	private IntegerProperty 					year			= new SimpleIntegerProperty();
	private StringProperty 						geoId			= new SimpleStringProperty();
	private StringProperty 						geoCode			= new SimpleStringProperty();
	private StringProperty 						territory		= new SimpleStringProperty();
	private LongProperty    					population2018	= new SimpleLongProperty();
	private LongProperty    					nbCases			= new SimpleLongProperty();
	private LongProperty    					nbDeaths		= new SimpleLongProperty();
	private ReadOnlyObjectProperty<Country>    	country			= new SimpleObjectProperty<Country>();
	private ReadOnlyObjectProperty<LocalDate>  	date			= new SimpleObjectProperty<LocalDate>();

	public DailyReportBeanFx() {
		super();

		final ObjectBinding<Country>   countryBinding = Bindings.createObjectBinding(() -> {
		    String geoId_   = geoId.get();
		    return Country.of(geoId_);
		}, geoId);

		final ObjectBinding<LocalDate> dateBinding = Bindings.createObjectBinding(() -> {
		    int day_   = day.get();
		    int month_ = month.get();
		    int year_  = year.get();
		    return day_ == 0 || month_ == 0 ? LocalDate.of(0,1,1) : LocalDate.of(year_, month_, day_);
		}, day, month, year);

		((ObjectProperty<Country>)   country) . bind(countryBinding);
		((ObjectProperty<LocalDate>) date)    . bind(dateBinding);
	}
	public DailyReportBeanFx(DailyReport _record) {
		this();
		setDay(_record.getDay());
		setMonth(_record.getMonth());
		setYear(_record.getYear());
		setGeoId(_record.getGeoId());
		setGeoCode(_record.getGeoCode());
		setTerritory(_record.getTerritory());
		setPopulation2018(_record.getPopulation2018());
		setCases(_record.getInfected());
		setDeaths(_record.getDead());
	}

	@Override public KpiType 					getType() 					{ return KpiType.Variation; }

	public void 								setDay(int _day) 				{ day.set(_day); }
	@Override public int 						getDay() 						{ return day.get(); }
	public IntegerProperty 						dayProperty() 					{ return day; }
	public void 								setMonth(int _month) 			{ month.set(_month); }
	@Override public int 						getMonth() 						{ return month.get(); }
	public IntegerProperty 						monthProperty() 				{ return month; }
	public void 								setYear(int _year) 				{ year.set(_year); }
	@Override public int 						getYear() 						{ return year.get(); }
	public IntegerProperty 						yearProperty() 					{ return year; }

	public void 								setGeoId(String _id) 			{ geoId.set(_id); }
	@Override public String 					getGeoId() 						{ return geoId.get(); }
	public StringProperty 						geoIdProperty() 				{ return geoId; }
	public void 								setGeoCode(String _code)		{ geoCode.set(_code); }
	@Override public String 					getGeoCode()	 				{ return geoCode.get(); }
	public StringProperty 						geoCodeProperty() 				{ return geoCode; }
	public void 								setTerritory(String _territory)	{ territory.set(_territory); }
	@Override public String 					getTerritory()	 				{ return territory.get(); }
	public StringProperty 						territoryProperty() 			{ return territory; }

	public void 								setPopulation2018(long _pop) 	{ population2018.set(_pop); }
	@Override public long 						getPopulation2018() 			{ return population2018.get(); }
	public LongProperty 						populationProperty() 			{ return population2018; }
	public void 								setCases(long _cases) 			{ nbCases.set(_cases); }
	@Override public long 						getInfected() 						{ return nbCases.get(); }
	public LongProperty 						casesProperty() 				{ return nbCases; }
	public void 								setDeaths(long _deaths) 		{ nbDeaths.set(_deaths); }
	@Override public long 						getDead() 						{ return nbDeaths.get(); }
	public LongProperty 						deathsProperty() 				{ return nbDeaths; }

	public ReadOnlyObjectProperty<Country>  	countryProperty() 				{ return country; }
	public ReadOnlyObjectProperty<LocalDate>  	dateProperty() 					{ return date; }

}
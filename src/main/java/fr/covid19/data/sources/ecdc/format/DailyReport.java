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
import java.util.Optional;

import fr.geodesic.referential.api.countries.Country;
import fr.outbreak.api.Outbreak;
import fr.outbreak.api.Outbreak.KpiType;
import fr.outbreak.api.Outbreak.Population;

public interface DailyReport extends Outbreak.LocalizedReport {

	public default LocalDate  		getDate() {
		return LocalDate.of(getYear(), getMonth(), getDay());
	}

	public default Country  		getCountry() {
		return Country.of( getGeoId() );
	}

	public default Optional<Long> 	get(Population _population) {
		return switch(_population) {
		case Susceptible  -> Optional.of( getType() == KpiType.Value ? getPopulation2018() - getInfected() - getDead() : 0 );
		case Infected     -> Optional.of( getInfected() );
		case Dead         -> Optional.of( getDead() );
		case Immuned      -> Optional.empty();
		case Recovered    -> Optional.empty();
		case Alive        -> Optional.of( getPopulation2018() - getDead() );
		case Total        -> Optional.of( getPopulation2018() );
		};
	}

	public int 						getDay();
	public int 						getMonth();
	public int 						getYear();

	public String 					getGeoId();
	public String 					getGeoCode();
	public String 					getTerritory();

	public long 					getPopulation2018();
	public long 					getInfected();
	public long 					getDead();

}

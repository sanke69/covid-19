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
package fr.main.covid19.viewers.map;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.SortedSet;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import fr.javafx.api.color.ColorScale;
import fr.javafx.scene.PropertyEditors;
import fr.javafx.scene.properties.Editor;

import fr.covid19.style.OutbreakDefaultColors;
import fr.geodesic.referential.api.countries.Country;
import fr.outbreak.api.Outbreak;
import fr.outbreak.api.Outbreak.Population;
import fr.outbreak.api.OutbreakViewer;
import fr.outbreak.sdk.data.OutbreakRecord;
import fr.reporting.api.Report;
import fr.reporting.sdk.graphics.ReportViewerBase;

import eu.hansolo.fx.world.CountryPath;
import eu.hansolo.fx.world.LocationBuilder;
import eu.hansolo.fx.world.World;
import eu.hansolo.fx.world.World.Resolution;
import eu.hansolo.fx.world.WorldBuilder;

public class OutbreakMapPane extends ReportViewerBase<Outbreak.Report, Outbreak.DataBase> implements OutbreakViewer.Map {
	private BorderPane   										container;

    private World     	 										world;
    private Editor<LocalDate> 									dateSelecter;

    private java.util.Map<Country, Outbreak.Report> 			countryInfos   = new HashMap<Country, Outbreak.Report>();
    private java.util.Map<Country, Color>				 		countryColors  = new HashMap<Country, Color>();
    private java.util.Map<Country, EventHandler<MouseEvent>>	countryOnClick = new HashMap<Country, EventHandler<MouseEvent>>();

    private ColorScale<Long>									outbreaHeatMap = null;

	public OutbreakMapPane() {
		super("Map View");

		_createWorld();
		_createDateSelecter();
        container = new BorderPane(world, null, null, dateSelecter.getNode(), null);

		databaseProperty().addListener( (_obs, _old, _new) -> updateCountries( LocalDate.now().minusDays(1) ) );
	}

	public void 					setCountryColor   	(Country _country, Color _color) {
		countryColors  . put(_country, _color);
		_setCountryColor(_country, _color);
	}
	public void 					setCountryInfos   	(Country _country, Outbreak.Report _infos) {
		countryInfos   . put(_country, _infos);
	}
	public void 					setCountryOnClick 	(Country _country, EventHandler<MouseEvent> _clickHandler) {
		countryOnClick . put(_country, _clickHandler);
	}

	public void 					updateCountries		(LocalDate _date) {
		if(getDatabase() == null)
			return ;

		Report.Type         type       = Report.Type.Value;
		Population          population = Population.Infected;
		Collection<Country> countries  = getDatabase().getIndicators(type, r -> r.getCountry(), true);

		countryInfos.clear();

		for(Country c : countries) {
			if(!c.justConcept()) {
				SortedSet<Outbreak.Report> reports = getDatabase().getReports(Report.Type.Value, 
																			   r -> r.getCountry().equals(c) && r.getDate().equals(_date), 
																			   Report.Daily.comparatorByDate());

				if(reports != null && !reports.isEmpty()) {
					Outbreak.Report report = reports.first();

					Outbreak.Report r = new OutbreakRecord( report.get(Population.Susceptible) .orElse(-1L), 
															report.get(Population.Infected)    .orElse(-1L), 
															report.get(Population.Recovered)   .orElse(-1L),  
															report.get(Population.Immuned)     .orElse(-1L),
															report.get(Population.Dead)        .orElse(-1L) );
	
					setCountryColor(c, OutbreakDefaultColors.forCases(r.get(population).orElse(-1L)));
					setCountryInfos(c, r);
				} else {
					Outbreak.Report r = new OutbreakRecord(-1L, -1L, -1L, -1L, -1L );
	
					setCountryColor(c, OutbreakDefaultColors.forCases(r.get(population).orElse(-1L)));
					setCountryInfos(c, r);
				}
			}
		}
	}

	@Override
	protected Skin<OutbreakMapPane> createDefaultSkin() {
		return new Skin<OutbreakMapPane>() {
			@Override public OutbreakMapPane getSkinnable() 	{ return OutbreakMapPane.this; }
			@Override public Node getNode() 			{ return container; }
			@Override public void dispose() 			{  }
		};
	}

	private void 					_setCountryColor 	(Country _country, Color _fill) {
		_setCountryColor(_country, _fill, _fill);
	}
	private void 					_setCountryColor 	(Country _country, Color _stroke, Color _fill) {
		world.setCountryFillAndStroke(eu.hansolo.fx.world.Country.valueOf(_country.iso2()), _fill, _stroke);
	}
	
	private void 					_handleMouseClick	(MouseEvent _event) {
        CountryPath countryPath = (CountryPath) _event.getSource();
        Country     country     = Country.valueOf(countryPath.getName());

        EventHandler<MouseEvent> meh = countryOnClick.get(country);
        
        if(meh == null) {
        	StringBuilder   sb = new StringBuilder();
        	sb.append(country.getName() + " (" + country.iso2() + "/" + country.iso3() + ")" + "\n");

        	Outbreak.Report infos = countryInfos.get(country);

        	if(infos == null) {
            	sb.append("  - No available data " + "\n");
        	} else {
            	sb.append("  - Nb Infected : " + infos.get(Population.Infected) + "\n");
            	sb.append("  - Nb Dead     : " + infos.get(Population.Dead) + "\n");
            	sb.append("  - Population  : " + infos.get(Population.Total) + "\n");        		
        	}
        	
        	sb.append(country.getName() + " (" + country.iso2() + "/" + country.iso3() + ")" + "\n");
        	
        	System.out.println(sb.toString());

        } else meh.handle(_event);
	}


	@SuppressWarnings("unchecked")
	private void 					_createWorld() {
        world = WorldBuilder.create()
                            .resolution(Resolution.HI_RES)
                            //.backgroundColor(Color.web("#4aa9d7"))
                            //.fillColor(Color.web("#dcb36c"))
                            //.strokeColor(Color.web("#987028"))
                            //.hoverColor(Color.web("#fec47e"))
                            //.pressedColor(Color.web("#6cee85"))
                            //.locationColor(Color.web("#0000ff"))
                            //.selectedColor(Color.MAGENTA)
                            .locationIconCode(MaterialDesign.MDI_STAR)
                            .locations( LocationBuilder.create()
	                                    .name("FRA").latitude(48.80830).longitude(2.13879)
	                                    .color(Color.CRIMSON)
	                                    .iconCode(MaterialDesign.MDI_HEART)
	                                    .mouseEnterHandler(e -> ((FontIcon) e.getSource()).setFill(Color.CYAN))
	                                    .mousePressHandler(e -> {
	                                        System.out.println("Home");
	                                        ((FontIcon) e.getSource()).setFill(Color.MAGENTA);
	                                    })
	                                    .mouseReleaseHandler(e -> ((FontIcon) e.getSource()).setFill(Color.CYAN))
	                                    .mouseExitHandler(e -> ((FontIcon) e.getSource()).setFill(Color.CRIMSON))
	                                    .build())
                            .mousePressHandler(evt -> _handleMouseClick((MouseEvent) evt))
//                            .zoomEnabled(true)
                            .build();
	}
	private void 					_createDateSelecter() {
		dateSelecter = PropertyEditors.newLocalDateEditor(LocalDate.of(2020,  01,  01), LocalDate.of(2020,  05,  01));
		dateSelecter . valueProperty().addListener((_obs, _old, _new) -> updateCountries(_new));
	}

}

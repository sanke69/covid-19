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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

import fr.javafx.scene.PropertyEditors;
import fr.javafx.scene.properties.Editor;
import fr.javafx.scene.properties.SelecterSingle;

import fr.geodesic.referential.api.countries.Country;
import fr.outbreak.api.Outbreak.KpiType;
import fr.outbreak.graphics.OutbreakViewerOptions;

public class OutbreakMapPaneOptions extends OutbreakViewerOptions<OutbreakMapPane> {
	public static enum DisplayMode {
		Cases,          Deaths,          Recover,
		CasePerMillion, DeathPerMillion, RecoverPerMillion
	}

    SelecterSingle<DisplayMode> modeSelecter;
	Editor<Integer>				daySelecter;

	public OutbreakMapPaneOptions() {
		super();
		modeSelecter = PropertyEditors.newSingleSelecter(DisplayMode.values());
		daySelecter  = PropertyEditors.newDayEditor();
	}

	@Override
	public void 								initialize(OutbreakMapPane _map) {
		_map.databaseProperty().addListener((_obs, _old, _new) -> {
			if(_new != null)
				_map.updateCountries(LocalDate.now().minusDays(1));

		});
		
		if(_map.getDatabase() == null)
			return ;
		
		
		List<Country> countries = _map.getDatabase().getIndicators(KpiType.Variation, r -> r.getCountry(), true)
													.stream()
													.sorted((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()))
													.collect(Collectors.toList());
		

		Runnable updater = () -> {
			if(_map.getDatabase() != null) {
				DisplayMode display = selectedModeProperty().getValue();
				Integer     shift   = selectedDayProperty().getValue();

			}
		};

		selectedModeProperty() . addListener((_obs, _old, _new) -> updater.run());
		selectedDayProperty()  . addListener((_obs, _old, _new) -> updater.run());
	}

	public List<Node> 							getNodes() {
		return Arrays.asList(modeSelecter.getNode(), daySelecter.getNode());
	}

	public ObservableValue<DisplayMode> 		selectedModeProperty() {
		return modeSelecter.selectedProperty();
	}
	public ObservableValue<Integer> 		selectedDayProperty() {
		return daySelecter.valueProperty();
	}

}

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
package fr.main.covid19;

import java.io.IOException;

import javafx.application.Application;

import fr.covid19.MainCovid19;
import fr.main.covid19.viewers.map.OutbreakMapPane;
import fr.main.covid19.viewers.rank.OutbreakRankingPane;
import fr.outbreak.graphics.OutbreakStage;
import fr.outbreak.graphics.viewers.about.OutbreakAboutPane;
import fr.outbreak.graphics.viewers.chart.OutbreakChartPane;
import fr.outbreak.graphics.viewers.chart.OutbreakChartPaneOptionsBasics;
import fr.outbreak.graphics.viewers.chart.OutbreakChartPaneOptionsComparison;
import fr.outbreak.graphics.viewers.table.OutbreakTablePane;
import fr.outbreak.graphics.viewers.table.OutbreakTablePaneOptions;

public class StartCovid19 extends MainCovid19 {

	@Override
	public void setViewers(OutbreakStage _stage) {
		_stage.registerViewerPane(new OutbreakAboutPane());
		_stage.registerViewerPane(new OutbreakRankingPane());
		_stage.registerViewerPane(new OutbreakMapPane());
		_stage.registerViewerPane(new OutbreakChartPane("Chart View"),    new OutbreakChartPaneOptionsBasics());
		_stage.registerViewerPane(new OutbreakTablePane(),                new OutbreakTablePaneOptions());
		_stage.registerViewerPane(new OutbreakChartPane("Chart Compare"), new OutbreakChartPaneOptionsComparison());
	}

	public static void main(String[] args) throws IOException {
		Application.launch(StartCovid19.class, args);
	}

}

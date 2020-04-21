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
//		_stage.registerViewerPane(new OutbreakTablePane(),                new OutbreakTablePaneOptions());
//		_stage.registerViewerPane(new OutbreakChartPane("Chart Compare"), new OutbreakChartPaneOptionsComparison());
	}

	public static void main(String[] args) throws IOException {
		Application.launch(StartCovid19.class, args);
	}

}

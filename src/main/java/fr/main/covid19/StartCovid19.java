package fr.main.covid19;

import java.io.IOException;

import fr.covid19.MainCovid19;
import fr.main.covid19.panes.OutbreakAboutPane;
import fr.main.covid19.panes.OutbreakRanking;
import fr.outbreak.graphics.OutbreakStage;
import javafx.application.Application;

public class StartCovid19 extends MainCovid19 {

	@Override
	public void setViewers(OutbreakStage _stage) {
		_stage.registerViewerPane(new OutbreakAboutPane());
		_stage.registerViewerPane(new OutbreakRanking());
	}

	public static void main(String[] args) throws IOException {
		Application.launch(StartCovid19.class, args);
	}

}

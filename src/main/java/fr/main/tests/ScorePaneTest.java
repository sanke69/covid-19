package fr.main.tests;

import fr.main.covid19.panes.OutbreakRankingPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScorePaneTest extends Application {

	@Override
	public void start(Stage stage) throws Exception{
		stage.setScene(new Scene( new OutbreakRankingPane() ));
		stage.show();
	}

	public static void main(String... args) {
		Application.launch(ScorePaneTest.class, args);
	}

}

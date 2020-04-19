package fr.main.covid19.panes;

import java.time.LocalDate;

import fr.java.api.score.Score;
import fr.javafx.api.score.ScoreTable;
import fr.outbreak.api.Outbreak;
import fr.outbreak.api.Outbreak.KpiType;
import fr.outbreak.api.Outbreak.Population;
import fr.outbreak.graphics.OutbreakViewerBase;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class OutbreakRankingPane extends OutbreakViewerBase {

    public BorderPane root;
    public Node       title, options;
    public ScoreTable table;

    public OutbreakRankingPane() {
    	this(10);
    }
    public OutbreakRankingPane(int _nbDisplayed) {
    	this(_nbDisplayed, null);
    }
    public OutbreakRankingPane(int _nbDisplayed, Node _title) {
    	this(_nbDisplayed, _title, null);
    }
    public OutbreakRankingPane(int _nbDisplayed, Node _title, Node _options) {
    	super("Outbreak\nRanking");

    	title   = _title   == null ? createDefaultTitle()       : _title;
    	table   = createScoreTable();
    	options = _options == null ? createDefaultControlPane() : _options;
    	
    	VBox center = new VBox();
    	center.getChildren().addAll(options, new StackPane(table));

    	BorderPane.setMargin    (title, new Insets(10, 69, 10, 69));
    	BorderPane.setAlignment (title, Pos.TOP_CENTER);
    	BorderPane.setMargin    (center, new Insets(10, 69, 33, 69));
    	BorderPane.setAlignment (center, Pos.TOP_CENTER);

    	root = new BorderPane(center, title, null, null, null);
    	
    	databaseProperty().addListener((_obs, _old, _new) -> {
    		System.out.println("Updating database...");

			if(_new == null) {
				table.getItems().clear();
				return ;
			}
			
			Score.HallOfFame db = new Score.UserDatabase<Outbreak.LocalizedReport>(_new.getReports(KpiType.Value, LocalDate.now().minusDays(1)),
																					r -> r.getCountry().getName(),
																					r -> r.get(Population.Infected).orElse(0L));

			table.getItems().addAll( db.getTop10() );
    	});
    }

    public ObservableList<Score> 	getItems() {
    	return ((ScoreTable) table).getItems();
    }

    private Label 					createDefaultTitle() {
		String common = "-fx-alignment: center; -fx-text-alignment: center; -fx-content-display: center; -fx-font-weight: bold;";
		String color  = "-fx-text-fill: purple;";
		String font   = "-fx-font-size:60px; -fx-font-family: Biology;";

		Label 
		label = new Label("Covid-19");
		label . setStyle(common + color + font);

		return label;
	}
	private ScoreTable 				createScoreTable() {
		ScoreTable table = new ScoreTable();

		return table;
	}
	private Node 					createDefaultControlPane() {
		AnchorPane anchor = new AnchorPane();

		Button   byInfected  = new Button("# Infected");
		Button   byRecovered = new Button("# Recovered");
		Button   byDead      = new Button("# Dead");
		Button[] buttons     = new Button[] { byInfected, 
										//	  byRecovered, 
											  byDead };

		byInfected.pressedProperty().addListener((_obs, _old, _new) -> {
			if(databaseProperty().get() == null)
				return ;

			Score.HallOfFame db = new Score.UserDatabase<Outbreak.LocalizedReport>(getDatabase().getReports(KpiType.Value, LocalDate.now()),
																					r -> r.getCountry().getName(),
																					r -> r.get(Population.Infected).orElse(0L));

			table.getItems().setAll( db.getTop10() );
    	});
		byRecovered.pressedProperty().addListener((_obs, _old, _new) -> {
			if(databaseProperty().get() == null)
				return ;

			Score.HallOfFame db = new Score.UserDatabase<Outbreak.LocalizedReport>(getDatabase().getReports(KpiType.Value, LocalDate.now()),
																					r -> r.getCountry().getName(),
																					r -> r.get(Population.Recovered).orElse(0L));

			table.getItems().setAll( db.getTop10() );
    	});
		byDead.pressedProperty().addListener((_obs, _old, _new) -> {
			if(databaseProperty().get() == null)
				return ;

			Score.HallOfFame db = new Score.UserDatabase<Outbreak.LocalizedReport>(getDatabase().getReports(KpiType.Value, LocalDate.now().minusDays(1)),
																					r -> r.getCountry().getName(),
																					r -> r.get(Population.Dead).orElse(0L));

			table.getItems().setAll( db.getTop10() );
    	});

		anchor.setPadding(new Insets(10, 33, 27, 33));
		anchor.getChildren().addAll(buttons);
		anchor.widthProperty().addListener((_obs, _old, _new) -> {
			double W  = _new.doubleValue();
			double WC = 250;
			double PL = 10,
				   PR = 10;
			int    N  = buttons.length;

			double PI = ( W - PL - PR - N * WC ) / (N - 1);

			for(int i = 0; i < N; ++i) {
				double pl = PL + i * (WC + PI);
				double pr = W - (PL + i * (WC + PI)) - WC;

				setAnchors(buttons[i], 10d, pl, null, pr > 0 ? pr : null);
				buttons[i].setPrefHeight(49);
			}

		});

		anchor.applyCss();
		anchor.layout();
		return anchor;
	}

	protected Skin<OutbreakRankingPane>  createDefaultSkin() {
		return new Skin<OutbreakRankingPane>() {

			@Override
			public OutbreakRankingPane getSkinnable() {
				return OutbreakRankingPane.this;
			}

			@Override
			public Node getNode() {
				return root;
			}

			@Override
			public void dispose() {
				;
			}
			
		};
	}

	private static void setAnchors(Node _child, Double _top, Double _left, Double _bottom, Double _right) {
		AnchorPane.setTopAnchor    (_child, _top);
		AnchorPane.setLeftAnchor   (_child, _left);
		AnchorPane.setBottomAnchor (_child, _bottom);
		AnchorPane.setRightAnchor  (_child, _right);
	}

}

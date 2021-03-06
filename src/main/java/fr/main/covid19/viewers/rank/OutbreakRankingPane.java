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
package fr.main.covid19.viewers.rank;

import java.time.LocalDate;

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

import fr.java.api.score.Score;

import fr.javafx.api.score.ScoreTable;

import fr.outbreak.api.Outbreak;
import fr.outbreak.api.Outbreak.Population;
import fr.reporting.api.Report;
import fr.reporting.sdk.graphics.ReportViewerBase;

public class OutbreakRankingPane extends ReportViewerBase<Outbreak.Report, Outbreak.DataBase> {

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
			
			Score.HallOfFame db = new Score.UserDatabase<Outbreak.Report>(_new.getReports(Report.Type.Value, r -> r.getDate().equals( LocalDate.now().minusDays(1) )),
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

			Score.HallOfFame db = new Score.UserDatabase<Outbreak.Report>(getDatabase().getReports(Report.Type.Value, r -> r.getDate().equals( LocalDate.now() )),
																					r -> r.getCountry().getName(),
																					r -> r.get(Population.Infected).orElse(0L));

			table.getItems().setAll( db.getTop10() );
    	});
		byRecovered.pressedProperty().addListener((_obs, _old, _new) -> {
			if(databaseProperty().get() == null)
				return ;

			Score.HallOfFame db = new Score.UserDatabase<Outbreak.Report>(getDatabase().getReports(Report.Type.Value, r -> r.getDate().equals( LocalDate.now() )),
																					r -> r.getCountry().getName(),
																					r -> r.get(Population.Recovered).orElse(0L));

			table.getItems().setAll( db.getTop10() );
    	});
		byDead.pressedProperty().addListener((_obs, _old, _new) -> {
			if(databaseProperty().get() == null)
				return ;

			Score.HallOfFame db = new Score.UserDatabase<Outbreak.Report>(getDatabase().getReports(Report.Type.Value, r -> r.getDate().equals( LocalDate.now() )),
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

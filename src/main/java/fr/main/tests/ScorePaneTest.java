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
package fr.main.tests;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import fr.main.covid19.viewers.rank.OutbreakRankingPane;

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

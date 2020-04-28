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
 */package fr.covid19;

import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.Cursor;
import javafx.scene.Scene;

import fr.covid19.data.WorldDataCovid19;
import fr.covid19.data.sources.http.EuropeanCDC;
import fr.outbreak.OutbreakApplication;
import fr.outbreak.api.Outbreak;
import fr.reporting.sdk.graphics.ReportStage;

public abstract class MainCovid19 extends OutbreakApplication.Graphics {

	public MainCovid19() {
		super();
		loadDatabase();
	}

	private void loadDatabase() {
		final Scene  scene  = getPrimaryStage().getScene();
		final Cursor cursor = scene.getCursor();

		final Service<Outbreak.DataBase> loadService = new Service<Outbreak.DataBase>() {
			@Override
			protected Task<Outbreak.DataBase> createTask() {
				Task<Outbreak.DataBase> task = new Task<Outbreak.DataBase>() {
					@Override
					protected Outbreak.DataBase call() throws Exception {
						return new WorldDataCovid19( new EuropeanCDC().getRecords() );
					}
				};

				return task;
			}
		};
		loadService.stateProperty().addListener(
			(ObservableValue<? extends Worker.State> _obs, Worker.State _old, Worker.State _new) -> {
				switch(_new) {
				case READY, SCHEDULED :
					break;
				case RUNNING :	
					scene.setCursor(Cursor.WAIT);
					break;
				case CANCELLED, FAILED :	
					scene.setCursor(cursor);
					System.err.println("Failed to load data. Try restart application...");
					break;
				case SUCCEEDED :	
					scene.setCursor(cursor);
					break;
				}
			}
		);

		databaseProperty().bind(loadService.valueProperty());

		loadService.start();
	}

}

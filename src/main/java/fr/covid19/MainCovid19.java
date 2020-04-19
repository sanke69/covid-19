/**
 * OutBreak API
 * Copyright (C) 2007-?XYZ  Steve PECHBERTI <steve.pechberti@laposte.net>
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
package fr.covid19;

import fr.covid19.data.WorldDataCovid19;
import fr.covid19.data.sources.ecdc.EuropeanCDC;
import fr.outbreak.MainOutbreak;
import fr.outbreak.api.database.OutbreakDataBase;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.Cursor;
import javafx.scene.Scene;

public abstract class MainCovid19 extends MainOutbreak.Graphics {

	public MainCovid19() {
		super();
		loadDatabase();
	}

	private void loadDatabase() {
		final Scene  scene  = getPrimaryStage().getScene();
		final Cursor cursor = scene.getCursor();

		final Service<OutbreakDataBase> loadService = new Service<OutbreakDataBase>() {
			@Override
			protected Task<OutbreakDataBase> createTask() {
				Task<OutbreakDataBase> task = new Task<OutbreakDataBase>() {
					@Override
					protected OutbreakDataBase call() throws Exception {
						return new WorldDataCovid19( EuropeanCDC.getDailyReports() );
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

/**
 * JavaFR
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
package fr.javafx.api.color;

import javafx.scene.paint.Color;

import fr.javafx.api.color.ColorScale.RecordRung;

public enum HeatMapColor {
	BLACK_AND_WHITE (new RecordRung[] { new RecordRung(0.00,    Color.BLACK),    new RecordRung(1.00,    Color.WHITE) }), 
	COLOR_2         (new RecordRung[] { new RecordRung(0.00,    Color.DARKBLUE), new RecordRung(1.00,    Color.DARKRED) }), 
	COLOR_5			(new RecordRung[] { new RecordRung(0d / 4d, Color.BLUE),     new RecordRung(1d / 4d, Color.CYAN), new RecordRung(2d / 4d, Color.GREEN), new RecordRung(3d / 4d, Color.YELLOW), new RecordRung(4d / 4d, Color.RED) }), 
	COLOR_7			(new RecordRung[] { new RecordRung(0d / 6d, Color.BLACK),    new RecordRung(1d / 6d, Color.BLUE), new RecordRung(2d / 6d, Color.CYAN),  new RecordRung(3d / 6d, Color.GREEN),  new RecordRung(4d / 6d, Color.YELLOW), new RecordRung(5d / 6d, Color.RED), new RecordRung(6d / 6d, Color.WHITE) });

	private final RecordRung[] rungs;

	private HeatMapColor(RecordRung[] _rungs) {
		rungs = _rungs;
	}

	public RecordRung[] rungs() {
		return rungs;
	}

}

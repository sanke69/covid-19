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

public class IntervalColorScale<T extends Comparable<T>> implements ColorScale<T> {
	GenericRung<T>[] rungs;

	public IntervalColorScale(GenericRung<T>[] _rungs) {
		super();
		rungs = _rungs;
	}

	public Color get(T _t) {
		return ColorScale.interpolate(Color.LIGHTGREEN, Color.DARKGREEN, 0.5);
	}
	public Color get(int _i, int _n) {
		return ColorScale.interpolate(Color.LIGHTGREEN, Color.DARKGREEN, (double) _i / (double) _n);
	}

}

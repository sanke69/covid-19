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

public interface ColorScale<T> {
//	public record    Rung(double value, Color color) {}
	public record    RecordRung(double value, Color color) {}
	public class     GenericRung<T extends Comparable<T>> {
		final T value; final Color color;
		public GenericRung(T _value, Color _color) { value = _value; color = _color; }

		public T     value() { return value; }
		public Color color() { return color; }

	}
	
	public interface Normalized {

		public Color 			get(double _t);			// _t \in [ 0.0, 1.0 ]
		public Color 			get(int _i, int _n);	// _i \in [ 0, 1, 2, .., _n ]

	}
	public class     Linear implements Normalized {
	
		public RecordRung[] rungs;
	
		public Linear() {
			this(HeatMapColor.BLACK_AND_WHITE);
		}
		public Linear(boolean _blackAndWhite) {
			this(_blackAndWhite ? HeatMapColor.BLACK_AND_WHITE : HeatMapColor.COLOR_2);
		}
		public Linear(HeatMapColor _heatmap) {
			this(_heatmap.rungs());
		}
		public Linear(RecordRung[] _entries) {
			super();
			rungs = _entries;
		}
	
		public Linear(Color _from, Color _to) {
			super();
			rungs = new RecordRung[] {  new RecordRung(0d, _from), new RecordRung(1d, _to) };
		}
	
		public Color get(double _t) {
			int    iColorIndex = 0; while(_t > rungs[iColorIndex].value()) iColorIndex++;
			iColorIndex -= 1;
			iColorIndex  = iColorIndex > 0 ? iColorIndex : 0;
			iColorIndex  = iColorIndex < rungs.length - 1 ? iColorIndex : rungs.length - 1;
	
			double iPrevValue = rungs[iColorIndex].value();
			double iNextValue = rungs[iColorIndex+1].value();
	
			double iInterp    = (_t - iPrevValue) / (iNextValue - iPrevValue);
			Color  iFrom      = rungs[iColorIndex].color(), 
				   iTo        = rungs[iColorIndex + 1].color();
	
			return ColorScale.interpolate(iFrom, iTo, iInterp);
		}
		public Color get(int _i, int _n) {
			double iValue      = (double) ((double) _i / _n);
			int    iColorIndex = 0; while(iValue > rungs[iColorIndex].value()) iColorIndex++;
	
			iColorIndex -= 1;
			iColorIndex  = iColorIndex > 0 ? iColorIndex : 0;
			iColorIndex  = iColorIndex < rungs.length - 1 ? iColorIndex : rungs.length - 1;
	
			double iPrevValue       = rungs[iColorIndex].value();
			double iNextValue       = rungs[iColorIndex+1].value();
			int    iPrevColorIndex  = (int) (iPrevValue * _n);
			int    iNextColorIndex  = (int) (iNextValue * _n);
	
			double iRangeD = iNextValue      - iPrevValue;
			double iRangeI = iNextColorIndex - iPrevColorIndex; double Q = _n / (iNextColorIndex - iPrevColorIndex);
	
			Color  iFrom   = rungs[iColorIndex].color(), iTo = rungs[iColorIndex + 1].color();
			double iInterp = (double) ((double)(_i - iPrevColorIndex) * (iRangeD / iRangeI * Q));
	
			return ColorScale.interpolate(iFrom, iTo, iInterp);
		}
	
	}

	public Color 		get(T _t);

	public static <T extends Comparable<T>> 
	ColorScale<T> of(HeatMapColor _color) {

        return null;
	}

	public static Color interpolate(Color _from, Color _to, double _t) {
        if (_t <= 0.0) return _from;
        if (_t >= 1.0) return _to;

        return new Color(  	_from.getRed()    + (_to.getRed()     - _from.getRed())     * _t,
			        		_from.getGreen()  + (_to.getGreen()   - _from.getGreen())   * _t,
			        		_from.getBlue()   + (_to.getBlue()    - _from.getBlue())    * _t,
			        		1d   );
	}

}

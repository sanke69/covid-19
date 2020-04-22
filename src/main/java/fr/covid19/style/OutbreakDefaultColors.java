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
package fr.covid19.style;

import java.util.Arrays;
import java.util.List;

import javafx.scene.paint.Color;

import fr.javafx.api.color.ColorScale.GenericRung;

public class OutbreakDefaultColors {

	static List<GenericRung<Long>> susceptibleRungs = 
			Arrays.asList(	new GenericRung<Long>(     0L, Color.GREEN),
							new GenericRung<Long>(   100L, Color.BLUE),
							new GenericRung<Long>( 1_000L, Color.CYAN),
							new GenericRung<Long>( 2_500L, Color.GREEN),
							new GenericRung<Long>( 5_000L, Color.YELLOW),
							new GenericRung<Long>(10_000L, Color.RED),
							new GenericRung<Long>(25_000L, Color.WHITE)  );
	static List<GenericRung<Long>> infectedRungs    = 
			Arrays.asList(	new GenericRung<Long>(        0L, Color.GREEN),
							new GenericRung<Long>(      100L, Color.BLUE),
							new GenericRung<Long>(    1_000L, Color.CYAN),
							new GenericRung<Long>(    2_500L, Color.GREEN),
							new GenericRung<Long>(   10_000L, Color.YELLOW),
							new GenericRung<Long>(  100_000L, Color.RED),
							new GenericRung<Long>(1_000_000L, Color.WHITE)  );
	static List<GenericRung<Long>> recoveryRungs    = 
			Arrays.asList(	new GenericRung<Long>(     0L, Color.GREEN),
							new GenericRung<Long>(   100L, Color.BLUE),
							new GenericRung<Long>( 1_000L, Color.CYAN),
							new GenericRung<Long>( 2_500L, Color.GREEN),
							new GenericRung<Long>( 5_000L, Color.YELLOW),
							new GenericRung<Long>(10_000L, Color.RED),
							new GenericRung<Long>(25_000L, Color.WHITE)  );
	static List<GenericRung<Long>> immunedRungs     = 
			Arrays.asList(	new GenericRung<Long>(     0L, Color.GREEN),
							new GenericRung<Long>(   100L, Color.BLUE),
							new GenericRung<Long>( 1_000L, Color.CYAN),
							new GenericRung<Long>( 2_500L, Color.GREEN),
							new GenericRung<Long>( 5_000L, Color.YELLOW),
							new GenericRung<Long>(10_000L, Color.RED),
							new GenericRung<Long>(25_000L, Color.WHITE)  );
	static List<GenericRung<Long>> deadRungs        = 
			Arrays.asList(	new GenericRung<Long>(     0L, Color.GREEN),
							new GenericRung<Long>(   100L, Color.BLUE),
							new GenericRung<Long>( 1_000L, Color.CYAN),
							new GenericRung<Long>( 2_500L, Color.GREEN),
							new GenericRung<Long>( 5_000L, Color.YELLOW),
							new GenericRung<Long>(10_000L, Color.RED),
							new GenericRung<Long>(25_000L, Color.WHITE)  );

	public static Color forCases(Long _cases) {
		Color c = null;
		
		if(_cases == null || _cases.compareTo(infectedRungs.get(0).value()) < 0)
			return infectedRungs.get(0).color();
		if(_cases.compareTo(infectedRungs.get(infectedRungs.size() - 1).value()) > 0)
			return infectedRungs.get(infectedRungs.size() - 1).color();

		for(int i = 0; i < infectedRungs.size() - 1; ++i) {
			if( _cases.compareTo( infectedRungs.get(i).value() )   > 0
			&&  _cases.compareTo( infectedRungs.get(i+1).value() ) < 0)
				return infectedRungs.get(i).color();
		}
		
		if(_cases < 10)
			c = Color.rgb(0, 208, 0);
		else if(_cases >= 10 && _cases < 100)
			c = Color.rgb(0, 0, 255);
		else if(_cases >= 100 && _cases < 1000)
			c = Color.rgb(124, 208, 255);
		else 
			c = Color.rgb(255, 0, 0);
		
		return c;
	}

}

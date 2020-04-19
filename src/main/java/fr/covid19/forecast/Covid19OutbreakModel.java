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
package fr.covid19.forecast;

public interface Covid19OutbreakModel {

	public record 		OutbreakPopulation 	(long S, long I, long R, long D) { }
	public record 		Rates      			(double birth, double recover, double contact) { }

	@FunctionalInterface
	public interface 	DeterministSolver {

		public static DeterministSolver newSolver(double _birth, double _death, double _alpha, double _beta, double _gamma) {
			return new DeterministSolverImpl();
		}

		public OutbreakPopulation[] 	compute(long S0, long I0, int _nbEpochs);

	}

	public class 		DeterministSolverImpl implements DeterministSolver {
		double alpha, beta, gamma;

		public DeterministSolverImpl() {
			super();
		}

		@Override
		public OutbreakPopulation[] 	compute(long N, long I0, int _nbEpochs) {
			OutbreakPopulation[] popi = new OutbreakPopulation[_nbEpochs];

			popi[0] = new OutbreakPopulation(N, I0, 0, 0);

			for(int i = 1; i < _nbEpochs; ++i) {
				long   It = popi[i-1].I, 
					   St = popi[i-1].S;
				double dI =  - ( beta / N * (St * It) + (beta + gamma) * I0 ) * i, 
					   dS =    ( beta / N * (St * It) - (beta + gamma) * I0 ) * i;

				popi[i] = new OutbreakPopulation((long) (St + dS), (long) (It + dI), 0, 0);
			}

			return popi;
		}

	}

}

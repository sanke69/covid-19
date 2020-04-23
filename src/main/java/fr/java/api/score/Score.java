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
package fr.java.api.score;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

public interface Score {
	public static final Comparator<Score> comparator = (s1, s2) -> - s1.getValue().compareTo( s2.getValue() );

	public static record Entry(String name, Long value) implements Score {
		@Override public String getUserName() { return name; }
		@Override public Long   getValue()    { return value; }
	}
	public interface     Database {

		public Collection<Score> 		getAllScores();
		public SortedSet<Score> 		getBestRecords(int _top);

	}
	public interface     HallOfFame extends Database {

		public default SortedSet<Score> getTop3() 	{ return getBestRecords(3); }
		public default SortedSet<Score> getTop10()	{ return getBestRecords(10); }
		public default SortedSet<Score> getTop100()	{ return getBestRecords(100); }

	}

	public static class  DefaultDatabase implements HallOfFame {
		Collection<Score> scores;

		public DefaultDatabase(Collection<Score> _records) {
			super();

			scores = _records;
		}

		@Override
		public Collection<Score> 			getAllScores() {
			return scores;
		}

		@Override
		public SortedSet<Score> 			getBestRecords(int _top) {
			return scores	.stream()
							.sorted(comparator)
							.limit(_top)
							.collect(
									Collectors.mapping(
											s -> new Score() {
												@Override public String getUserName() { return s.getUserName(); }
												@Override public Long   getValue()    { return s.getValue(); } },
											Collectors.toCollection( () -> new TreeSet<Score>(comparator) )
									));
		}

	}
	public static class  UserDatabase<T> implements HallOfFame {
		Function<T, String> nameProvider;
		ToLongFunction<T>   scoreProvider;
		
		Collection<T>       users;

		public UserDatabase(Function<T, String> _nameProvider, ToLongFunction<T> _scoreProvider) {
			this(null, _nameProvider, _scoreProvider);
		}
		public UserDatabase(Collection<T> _records, Function<T, String> _nameProvider, ToLongFunction<T> _scoreProvider) {
			super();

			users         = _records;

			nameProvider  = _nameProvider;
			scoreProvider = _scoreProvider;
		}

		public String 						getName(T _user) {
			return nameProvider.apply(_user);
		}
		public Long 						getScore(T _user) {
			return scoreProvider.applyAsLong(_user);
		}

		public Collection<T> 				getAllUsers() {
			return users;
		}
		@Override
		public Collection<Score> 			getAllScores() {
			return users.stream().collect(
					Collectors.mapping(
						u -> new Score() {
							@Override public String getUserName() { return nameProvider.apply(u); }
							@Override public Long   getValue()    { return scoreProvider.applyAsLong(u); }},
						Collectors.toCollection(() -> new TreeSet<Score>(comparator))
						));
		}

		public SortedSet<T> 				getBestUsers(int _top) {
			return users	.stream()
							.sorted(Comparator.comparingLong(scoreProvider))
							.limit(_top)
							.collect(Collectors.toCollection(() -> new TreeSet<T>()));
		}
		@Override
		public SortedSet<Score> 			getBestRecords(int _top) {
			return users	.stream()
							.sorted(Comparator.comparingLong(scoreProvider).reversed())
							.limit(_top)
							.collect(
									Collectors.mapping(
										u -> new Score() {
											@Override public String getUserName() { return nameProvider.apply(u); }
											@Override public Long   getValue()    { return scoreProvider.applyAsLong(u); }},
										Collectors.toCollection(() -> new TreeSet<Score>(comparator))
										));
		}

	}

	public String getUserName();
	public Long   getValue();

}

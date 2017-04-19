package web.application.development.team;

import java.util.Comparator;
import java.util.List;

public enum TeamComparator implements Comparator<Team> {
	NAME_SORT {
	public int compare(Team s1, Team s2) {
		return (s1.getName()).compareTo(s2.getName());
	}},
	
	ID_SORT {
	public int compare(Team s1, Team s2) {
		return (s1.getId()).compareTo(s2.getId());
	}};
	
	public static Comparator<Team> descending(final Comparator<Team> comp) {
		return new Comparator<Team>() {
			public int compare(Team s1, Team s2) {
				return comp.reversed().compare(s1, s2);
			}
		};
	}
	
	public static Comparator<Team> ascending(final Comparator<Team> comp) {
		return new Comparator<Team>() {
			public int compare(Team s1, Team s2) {
				return comp.compare(s1, s2);
			}
		};
	}
	
	public static Comparator<Team> getComparator(final List<TeamComparator> comparators) {
		return new Comparator<Team>() {
			public int compare(Team s1, Team s2) {
				for (TeamComparator option : comparators) {
					int result = option.compare(s1, s2);
					if (result != 0) {
						return result;
					}
				}
				return 0;
			}
		};
	}
}

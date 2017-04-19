package web.application.development.semester;

import java.util.Comparator;
import java.util.List;

public enum SemesterComparator implements Comparator<Semester> {
	NAME_SORT {
	public int compare(Semester s1, Semester s2) {
		return (s1.getName()).compareTo(s2.getName());
	}},
	
	ID_SORT {
	public int compare(Semester s1, Semester s2) {
		return (s1.getId()).compareTo(s2.getId());
	}},
	
	SEASON_SORT {
	public int compare(Semester s1, Semester s2) {
		return (s1.getSeason()).compareTo(s2.getSeason());
	}},
	
	LETO_SORT {
	public int compare(Semester s1, Semester s2) {
		return (s1.getLeto()).compareTo(s2.getLeto());
	}};
	
	public static Comparator<Semester> descending(final Comparator<Semester> comp) {
		return new Comparator<Semester>() {
			public int compare(Semester s1, Semester s2) {
				return comp.reversed().compare(s1, s2);
			}
		};
	}
	
	public static Comparator<Semester> ascending(final Comparator<Semester> comp) {
		return new Comparator<Semester>() {
			public int compare(Semester s1, Semester s2) {
				return comp.compare(s1, s2);
			}
		};
	}
	
	public static Comparator<Semester> getComparator(final List<SemesterComparator> comparators) {
		return new Comparator<Semester>() {
			public int compare(Semester s1, Semester s2) {
				for (SemesterComparator option : comparators) {
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

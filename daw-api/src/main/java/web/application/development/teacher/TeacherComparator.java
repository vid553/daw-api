package web.application.development.teacher;

import java.util.Comparator;
import java.util.List;

public enum TeacherComparator implements Comparator<Teacher> {
	NAME_SORT {
	public int compare(Teacher s1, Teacher s2) {
		return (s1.getName()).compareTo(s2.getName());
	}},
	
	ID_SORT {
	public int compare(Teacher s1, Teacher s2) {
		return (s1.getId()).compareTo(s2.getId());
	}},
	
	EMAIL_SORT {
	public int compare(Teacher s1, Teacher s2) {
		return (s1.getEmail()).compareTo(s2.getEmail());
	}},
	
	NUMBER_SORT {
	public int compare(Teacher s1, Teacher s2) {
		return (s1.getNumber()).compareTo(s2.getNumber());
	}};
	
	public static Comparator<Teacher> descending(final Comparator<Teacher> comp) {
		return new Comparator<Teacher>() {
			public int compare(Teacher s1, Teacher s2) {
				return comp.reversed().compare(s1, s2);
			}
		};
	}
	
	public static Comparator<Teacher> ascending(final Comparator<Teacher> comp) {
		return new Comparator<Teacher>() {
			public int compare(Teacher s1, Teacher s2) {
				return comp.compare(s1, s2);
			}
		};
	}
	
	public static Comparator<Teacher> getComparator(final List<TeacherComparator> comparators) {
		return new Comparator<Teacher>() {
			public int compare(Teacher s1, Teacher s2) {
				for (TeacherComparator option : comparators) {
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

package web.application.development.course;

import java.util.Comparator;
import java.util.List;

public enum CourseComparator implements Comparator<Course> {
	NAME_SORT {
	public int compare(Course s1, Course s2) {
		return (s1.getName()).compareTo(s2.getName());
	}},
	
	ID_SORT {
	public int compare(Course s1, Course s2) {
		return (s1.getId()).compareTo(s2.getId());
	}},
	
	ACRONIM_SORT {
	public int compare(Course s1, Course s2) {
		return (s1.getAcronim()).compareTo(s2.getAcronim());
	}};
	
	public static Comparator<Course> descending(final Comparator<Course> comp) {
		return new Comparator<Course>() {
			public int compare(Course s1, Course s2) {
				return comp.reversed().compare(s1, s2);
			}
		};
	}
	
	public static Comparator<Course> ascending(final Comparator<Course> comp) {
		return new Comparator<Course>() {
			public int compare(Course s1, Course s2) {
				return comp.compare(s1, s2);
			}
		};
	}
	
	public static Comparator<Course> getComparator(final List<CourseComparator> comparators) {
		return new Comparator<Course>() {
			public int compare(Course s1, Course s2) {
				for (CourseComparator option : comparators) {
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

package web.application.development.student;

import java.util.Comparator;
import java.util.List;

public enum StudentComparator implements Comparator<Student> {
	NAME_SORT {
	public int compare(Student s1, Student s2) {
		return (s1.getName()).compareTo(s2.getName());
	}},
	
	ID_SORT {
	public int compare(Student s1, Student s2) {
		return (s1.getId()).compareTo(s2.getId());
	}},
	
	EMAIL_SORT {
	public int compare(Student s1, Student s2) {
		return (s1.getEmail()).compareTo(s2.getEmail());
	}},
	
	NUMBER_SORT {
	public int compare(Student s1, Student s2) {
		return (s1.getNumber()).compareTo(s2.getNumber());
	}};
	
	public static Comparator<Student> descending(final Comparator<Student> comp) {
		return new Comparator<Student>() {
			public int compare(Student s1, Student s2) {
				return comp.reversed().compare(s1, s2);
			}
		};
	}
	
	public static Comparator<Student> ascending(final Comparator<Student> comp) {
		return new Comparator<Student>() {
			public int compare(Student s1, Student s2) {
				return comp.compare(s1, s2);
			}
		};
	}
	
	public static Comparator<Student> getComparator(final List<StudentComparator> comparators) {
		return new Comparator<Student>() {
			public int compare(Student s1, Student s2) {
				for (StudentComparator option : comparators) {
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

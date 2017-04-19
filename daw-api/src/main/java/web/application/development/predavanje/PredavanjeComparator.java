package web.application.development.predavanje;

import java.util.Comparator;
import java.util.List;

public enum PredavanjeComparator implements Comparator<Predavanje> {
	IDENTIFIER_SORT {
	public int compare(Predavanje s1, Predavanje s2) {
		return (s1.getIdentifier()).compareTo(s2.getIdentifier());
	}},
	
	ID_SORT {
	public int compare(Predavanje s1, Predavanje s2) {
		return (s1.getId()).compareTo(s2.getId());
	}};
	
	public static Comparator<Predavanje> descending(final Comparator<Predavanje> comp) {
		return new Comparator<Predavanje>() {
			public int compare(Predavanje s1, Predavanje s2) {
				return comp.reversed().compare(s1, s2);
			}
		};
	}
	
	public static Comparator<Predavanje> ascending(final Comparator<Predavanje> comp) {
		return new Comparator<Predavanje>() {
			public int compare(Predavanje s1, Predavanje s2) {
				return comp.compare(s1, s2);
			}
		};
	}
	
	public static Comparator<Predavanje> getComparator(final List<PredavanjeComparator> comparators) {
		return new Comparator<Predavanje>() {
			public int compare(Predavanje s1, Predavanje s2) {
				for (PredavanjeComparator option : comparators) {
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

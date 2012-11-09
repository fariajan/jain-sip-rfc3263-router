package com.google.code.rfc3263.dns;

import java.util.Comparator;

class PointerRecordPreferenceComparator implements Comparator<PointerRecord> {
	public int compare(PointerRecord o1, PointerRecord o2) {
		if (o1.getPreference() < o2.getPreference()) {
			return -1;
		} else if (o1.getPreference() > o2.getPreference()) {
			return 1;
		} else {
			return 0;
		}
	}
}
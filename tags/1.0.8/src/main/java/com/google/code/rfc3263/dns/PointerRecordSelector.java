package com.google.code.rfc3263.dns;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.jcip.annotations.ThreadSafe;

import org.apache.log4j.Logger;

/**
 * This class is used for sorting NAPTR records.
 * <p>
 * The algorithm employed by this class is the algorithm specified in RFC 2915,
 * that is, records are sorted first by order, and then by preference.
 */
@ThreadSafe
public class PointerRecordSelector {
	private final Logger LOGGER = Logger.getLogger(PointerRecordSelector.class);
	private final List<PointerRecord> pointers;
	
	public PointerRecordSelector(List<PointerRecord> pointers) {
		this.pointers = new LinkedList<PointerRecord>(pointers);
	}
	
	public List<PointerRecord> select() {
		LOGGER.debug("Sorting service records by priority");
		Collections.sort(this.pointers, new PointerRecordOrderComparator());
		
		// Split map into orders.
		final Map<Integer, List<PointerRecord>> orderMap = new TreeMap<Integer, List<PointerRecord>>(); 
		for (PointerRecord pointer : this.pointers) {
			if (orderMap.containsKey(pointer.getOrder()) == false) {
				orderMap.put(pointer.getOrder(), new LinkedList<PointerRecord>());
			}
			orderMap.get(pointer.getOrder()).add(pointer);
		}
		
		final Integer lowestOrder = orderMap.keySet().iterator().next();
		final List<PointerRecord> lowestOrderPointers = orderMap.get(lowestOrder);
		
		Collections.sort(lowestOrderPointers, new PointerRecordPreferenceComparator());
		
		return lowestOrderPointers;
	}
}
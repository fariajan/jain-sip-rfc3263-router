package com.google.code.rfc3263.dns;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.jcip.annotations.ThreadSafe;

import org.xbill.DNS.AAAARecord;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.NAPTRRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

/**
 * This class is the default DNS resolver, which uses dnsjava.
 * <p>
 * This class is thread-safe.
 */
@ThreadSafe
public class DefaultResolver implements Resolver {
	/**
	 * {@inheritDoc}
	 */
	public List<PointerRecord> lookupPointerRecords(String domain) {
		final List<PointerRecord> pointers = new ArrayList<PointerRecord>();
		
		final Record[] records;
		try {
			records = new Lookup(domain, Type.NAPTR).run();
		} catch (TextParseException e) {
			throw new RuntimeException(e);
		}
		if (records == null) {
			return pointers;
		}
		for (int i = 0; i < records.length; i++) {
			NAPTRRecord naptr = (NAPTRRecord) records[i];

			int order = naptr.getOrder();
			int preference = naptr.getPreference();
			String flags = naptr.getFlags();
			String service = naptr.getService();
			String regexp = naptr.getRegexp();
			String replacement = naptr.getReplacement().toString();
			
			PointerRecord pointer = new PointerRecord(domain, order, preference, flags, service, regexp, replacement);
			pointers.add(pointer);
		}
		
		
		return pointers;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ServiceRecord> lookupServiceRecords(String domain) {
		final List<ServiceRecord> services = new ArrayList<ServiceRecord>();
		
		final Record[] records;
		try {
			records = new Lookup(domain, Type.SRV).run();
		} catch (TextParseException e) {
			throw new RuntimeException(e);
		}
		if (records == null) {
			return services;
		}
		for (int i = 0; i < records.length; i++) {
			SRVRecord srv = (SRVRecord) records[i];

			int priority = srv.getPriority();
			int weight = srv.getWeight();
			int port = srv.getPort();
			String target = srv.getTarget().toString();
			
			ServiceRecord service = new ServiceRecord(domain, priority, weight, port, target);
			services.add(service);
		}
		
		
		return services;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<AddressRecord> lookupAddressRecords(String domain) {
		final Set<AddressRecord> addresses = new HashSet<AddressRecord>();
		
		Record[] records;
		try {
			records = new Lookup(domain, Type.A).run();
		} catch (TextParseException e) {
			throw new RuntimeException(e);
		}
		if (records != null) {
			for (int i = 0; i < records.length; i++) {
				ARecord a = (ARecord) records[i];
	
				InetAddress ip = a.getAddress();
				
				AddressRecord address = new AddressRecord(domain, ip);
				addresses.add(address);
			}
		}
		try {
			records = new Lookup(domain, Type.AAAA).run();
		} catch (TextParseException e) {
			throw new RuntimeException(e);
		}
		if (records != null) {
			for (int i = 0; i < records.length; i++) {
				AAAARecord a = (AAAARecord) records[i];
	
				InetAddress ip = a.getAddress();
				
				AddressRecord address = new AddressRecord(domain, ip);
				addresses.add(address);
			}
		}
		
		
		return addresses;
	}
}
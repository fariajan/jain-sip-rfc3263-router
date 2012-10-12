package com.google.code.rfc3263.dns;

import java.util.List;
import java.util.Set;

/**
 * This class encapsulates a partial DNS client.
 */
public interface Resolver {
	/**
	 * Returns a list of NAPTR records for the given domain.
	 * 
	 * @param domain the domain to query.
	 * @return a list of NAPTR records.
	 */
	List<PointerRecord> lookupPointerRecords(String domain);
	/**
	 * Returns a list of SRV records for the given domain.
	 * 
	 * @param domain the domain to query.
	 * @return a list of SRV records.
	 */
	List<ServiceRecord> lookupServiceRecords(String domain);
	/**
	 * Returns a list of A or AAAA records for the given domain.
	 * 
	 * @param domain the domain to query.
	 * @return a list of A or AAAA records.
	 */
	Set<AddressRecord> lookupAddressRecords(String domain);
}

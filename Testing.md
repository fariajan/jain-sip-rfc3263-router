This page is used as a notice board for recording test strategies.  RFC 3263 provides a large number of variables to incorporate in testing.  In order to fully test the router, we need to be confident that all permutations have been tested.

# Environmental Variables #

See EnvironmentalTesting.

We should test combinations of the presence of the various DNS records, e.g.

## NAPTR ##

  * **none**
  * order and preference of records

## SRV ##

  * **none**
  * priority and weight of records

## A and AAAA ##

  * **none**
  * present

It would seem illegal for a DNS server to not chain its records correctly, but it is possible through misconfiguration.  For example, if the DNS server has an NAPTR record, we would expect to see a corresponding SRV record.  Likewise, if the DNS server has a SRV record, we would expect to see a corresponding A or AAAA record.

During testing, this environment will need to be mocked out.

# Local Variables #

## Maddr Selection ##

The following maddr parameters should be tested:

  * **none**
  * hostname
  * IPv4 address
  * IPv6 reference

## Scheme ##

The following scheme configurations should be tested:

  * sip
  * sips

We should also test the router attempting to route a tel scheme to make sure the router acts correctly according to the contract it advertises (i.e. sip or sips only).

## Host ##

The following host configurations should be tested:

  * hostname
  * IPv4 address
  * IPv6 reference

Basic validation is done on IPv4 addresses and IPv6 references, but currently, invalid addresses, like 999.999.999.999 (valid according to 3261) will be parsed as an IP address, which will obviously fail to route.

## Port ##

The following port configurations should be tested:

  * **none**
  * 1234

We don't validate the port number (apart from when parsing the outbound proxy) for being in a valid tcp or udp range (0 - 65536).

## Transport Parameter ##

The following transport parameters should be tested:

  * **none**
  * tcp
  * udp
  * sctp
  * tls

"tls" is a special case, but should still be handled.  For the purposes of the router, URIs with "transport=tls" will be treated like sips addresses.

## Total Tests ##

maddr (4) x scheme (2) x host (3) x port (2) x transport (5) = 240 combinations

# maddr #

RFC 3261, Section 18.1.1

> A client that sends a request to a multicast address MUST add the "maddr" parameter to its Via header field value containing the destination multicast address, and for IPv4, SHOULD add the "ttl" parameter with a value of 1.  Usage of IPv6 multicast is not defined in this specification, and will be a subject of future standardization when the need arises.

Does this mean that if the maddr parameter is present, that the transport must be UDP?  It **should** be UDP, but can be abused.  Typically, we would expect this address to be:

  * sip.mcast.net
  * 224.0.1.75 (IPv4)

# Route Set #

The following route set configurations should be tested:

  * No RouteHeader
  * RouteHeader with loose routing
  * RouteHeader with strict routing

The route set is a bit different to the other variables here, since it is examined to determine **which** SIP URI is subjected to the RFC 3263 treatment.
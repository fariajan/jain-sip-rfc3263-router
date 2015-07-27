

# Introduction #

This page provides in-depth insight into the internal working of the router.

# SRV Record Sorting #

When encountering more that one SRV record of the same priority, the procedures outlined in RFC 2782 are **not** followed.  Instead, follow RFC 3263, and sort the records deterministically: firstly by weight (height to low), and then by the target name, in alphabetical order.  So, a query for `_sip._tcp.example.org.` which returns the following records:

```
_sip._tcp.example.org. 86400 IN SRV 1 0 5060 kermit.example.org.
_sip._tcp.example.org. 86400 IN SRV 1 1 5060 piggy.example.org.
_sip._tcp.example.org. 86400 IN SRV 1 1 5060 gonzo.example.org.
_sip._tcp.example.org. 86400 IN SRV 1 0 5060 animal.example.org.
```

would be sorted as:

```
_sip._tcp.example.org. 86400 IN SRV 1 1 5060 gonzo.example.org.
_sip._tcp.example.org. 86400 IN SRV 1 1 5060 piggy.example.org.
_sip._tcp.example.org. 86400 IN SRV 1 0 5060 animal.example.org.
_sip._tcp.example.org. 86400 IN SRV 1 0 5060 kermit.example.org.
```

# The "tls" Transport Parameter #

SipURIs with a transport parameter set to `tls` with either a `sip` or `sips` scheme will be treated as if they are `sips` URIs with a transport parameter set to `tcp`.  Therefore:

```
sip:example.org;transport=tls
```

becomes:

```
sips:example.org;transport=tcp
```

# Grouping of Address Records #

The resolver interfaces declares a Set as the return type of the lookupAddressRecords method, so the order of **A** and **AAAA** records (which are merged in the Set) is not guaranteed to be in the order as presented by the DNS server.
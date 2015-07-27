**NOTE**: This page needs some investigation.  It appears that JAIN-SIP can't be customised to route responses.  What about [gov.nist.javax.sip.DefaultAddressResolver](http://www-x.antd.nist.gov/proj/iptel/jain-sip-1.2/javadoc/gov/nist/javax/sip/DefaultAddressResolver.html)


---


This page provides advice for how to route a response using RFC 3263 when using the guidelines contained in RFC 3261 fail.

```
ListIterator<?> iter = response.getHeaders(ViaHeader.NAME);
ViaHeader via = (ViaHeader) iter.next();

// What is the format for this parameter?
// host [ COLON port ]
String sentBy = via.getParameter("sent-by");
String sentByHost = ...;
int sentByPort = ...;
String transport = via.getTransport();

if (isNumeric(sentByHost)) {
    Hop hop = new HopImpl(sentByHost, sentByPort, transport);
} else {
   if (sentByPort != -1) {
       // do A or AAAA lookup
       Set<AddressRecord> addresses = resolve.lookupAddressRecords(sentByHost);
       for (AddressRecord address : addresses) {
           Hop hop = new HopImpl(address.getAddress(), sentByPort, transport);
       }
   } else {
       // do SRV lookup
   }
}
```
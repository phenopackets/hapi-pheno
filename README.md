# hapi-pheno-client
FHIR Client for working with phenopackets.

## Running this package: mvn spring-boot:run -pl hapipheno-client

The demo app is intended to be run together with a HAPI FHIR server that
has ingested the GA4GH [Phenopacket IG](http://phenopackets.org/core-ig/index.html).
This [repository](https://github.com/pnrobinson/hapi-pheno-server) contains a copy of the 
HAPI FHIR JPA starter project that has been modified to ingest the Phenopacket IG.
By default it will start a server on localhost:8888, which is the location that this
client application will target by default.


## Testing


java -Xmx8G -jar validator_cli.jar myVariant.json -extension any -html-output MYVAR.html -version 4.0.1 -ig hl7.fhir.us.ga4gh.phenopacket#0.1.0



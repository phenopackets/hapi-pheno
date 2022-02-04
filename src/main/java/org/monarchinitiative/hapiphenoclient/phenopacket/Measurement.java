package org.monarchinitiative.hapiphenoclient.phenopacket;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.Observation;

@ResourceDef(
        profile="https://github.com/phenopackets/core-ig/fhir/StructureDefinition/Measurement")
public class Measurement extends Observation {


}

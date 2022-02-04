package org.monarchinitiative.hapiphenoclient.phenopacket;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.Observation;

@ResourceDef(name="Measurement",
        profile="https://github.com/phenopackets/core-ig/StructureDefinition/Measurement")
public class Measurement extends Observation {


}

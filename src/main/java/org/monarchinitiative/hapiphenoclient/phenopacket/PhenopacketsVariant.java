package org.monarchinitiative.hapiphenoclient.phenopacket;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.Observation;

@ResourceDef(
        profile="https://github.com/phenopackets/core-ig/StructureDefinition/PhenopacketsVariant")
public class PhenopacketsVariant extends Observation {
    private static final long serialVersionUID = 1L;



}

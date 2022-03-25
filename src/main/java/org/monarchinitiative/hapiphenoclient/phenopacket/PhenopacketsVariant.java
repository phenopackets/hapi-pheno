package org.monarchinitiative.hapiphenoclient.phenopacket;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.StringType;

@ResourceDef(
        profile="https://github.com/phenopackets/core-ig/StructureDefinition/PhenopacketsVariant")
public class PhenopacketsVariant extends Observation {
    private static final long serialVersionUID = 1L;

    /**
     * Each extension is defined in a field. Any valid HAPI Data Type
     * can be used for the field type. Note that the [name=""] attribute
     * in the @Child annotation needs to match the name for the bean accessor
     * and mutator methods.
     */
    @Child(name="gene-studied")
    @Extension(url="https://github.com/phenopackets/core-ig/StructureDefinition/????????",
            definedLocally=false, isModifier=false)
    @Description(shortDefinition="The name of the gene being studied")
    private StringType geneStudied;

}

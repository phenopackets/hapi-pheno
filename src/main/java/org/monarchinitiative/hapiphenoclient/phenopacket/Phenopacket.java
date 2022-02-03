package org.monarchinitiative.hapiphenoclient.phenopacket;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;

import java.util.List;

public class Phenopacket extends Composition {


    // Wie kodiert man den constraint id 1..1 (MS/must support ID) ?

    // Wie kodiert man die slices / Phenotypic feature?

    public List<Phenopacket> getPhenopacketList() {
        List<SectionComponent>  sections = getSection();
        for (SectionComponent c : sections) {
            if (c.getCode().getCodingFirstRep().equals("phenotypic_features")) {
                // expect observations
                List<Reference> reflist = c.getEntry();
                for (Reference ref : reflist) {
                    String id = ref.getReference();
                    // need to get from server
                }
            }
        }
        return List.of();
    }


//    List<PhenotypicFeature> phenotypicFeatureList;
//
//    @Override
//    public Composition setPhenotypicFeatureSection(List<SectionComponent> theSection) {
//        //phenotypicFeatureList = theSection;
//        return super.setSection(theSection);
//    }
}

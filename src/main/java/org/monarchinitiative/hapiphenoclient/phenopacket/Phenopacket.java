package org.monarchinitiative.hapiphenoclient.phenopacket;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Reference;
import org.monarchinitiative.hapiphenoclient.except.PhenoClientRuntimeException;

import java.util.List;
import java.util.Optional;

@ResourceDef(
        profile="https://github.com/phenopackets/core-ig/fhir/StructureDefinition/Phenopacket")
public class Phenopacket extends Composition {

    private final String phenopacketId;

    private Reference subject;


    public Phenopacket(String id) {
        this.phenopacketId = id;
    }


    public void setIndividual(Individual individual) {
        this.subject = new Reference(individual);
    }

    public Reference getIndividual() {
        return this.subject;
    }


    // Wie kodiert man den constraint id 1..1 (MS/must support ID) ?

    // Wie kodiert man die slices / Phenotypic feature?

    public List<Phenopacket> getPhenopacketList() {
        List<SectionComponent>  sections = getSection();
        for (SectionComponent c : sections) {
            if (c.getCode().getCodingFirstRep().getCode().equals("phenotypic_features")) {
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

    public void addPhenotypicFeature(PhenotypicFeature pfeature) {
        List<SectionComponent> sections = getSection();
        Optional<SectionComponent> opt =
                sections.stream().filter(sc -> sc.getCode().getCodingFirstRep().getCode().equals("phenotypic_features"))
                        .findAny();
        if (opt.isPresent()) {
            SectionComponent phenoFeatureSectionComponent = opt.get();
            phenoFeatureSectionComponent.getEntry().add(new Reference(pfeature));
        } else {
            throw new PhenoClientRuntimeException("Could not get list of phenotypic_features");
        }
    }


//    List<PhenotypicFeature> phenotypicFeatureList;
//
//    @Override
//    public Composition setPhenotypicFeatureSection(List<SectionComponent> theSection) {
//        //phenotypicFeatureList = theSection;
//        return super.setSection(theSection);
//    }
}

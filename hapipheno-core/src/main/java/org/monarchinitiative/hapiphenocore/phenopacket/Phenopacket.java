package org.monarchinitiative.hapiphenocore.phenopacket;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.*;
import org.monarchinitiative.hapiphenocore.except.PhenoClientRuntimeException;

import java.util.List;
import java.util.Optional;

@ResourceDef(
        profile="https://github.com/phenopackets/core-ig/StructureDefinition/Phenopacket")
public class Phenopacket extends Composition {

    private String phenopacketId;

    private Reference subject;


    public Phenopacket() {
        Composition.SectionComponent phenotypicFeaturesSection =
                new Composition.SectionComponent()
                        .setTitle("phenotypic_features")
                        .setCode(new CodeableConcept()
                                .addCoding(new Coding()
                                        .setCode("phenotypic_features")
                                        .setSystem("http://ga4gh.org/fhir/phenopackets/CodeSystem/SectionType")));
        addSection(phenotypicFeaturesSection);
        Composition.SectionComponent measurementSection =
                new Composition.SectionComponent()
                        .setTitle("measurements")
                        .setCode(new CodeableConcept()
                                .addCoding(new Coding()
                                        .setCode("measurements")
                                        .setSystem("http://ga4gh.org/fhir/phenopackets/CodeSystem/SectionType")));
        addSection(measurementSection);

    }


    /**
     * Note that we set the required identifier (i.e., business identifier) of the Composition from which
     * 'Phenopacket' is derived. The identifier is supplied by the Id element of the GA4GH Phenopacket, noting
     * that the GA4GH Phenopacket does not have a REST id (it has an id that plays the same role as a FHIR identifier).
     * @param individual Object referencing the patient of the Phenopacket.
     */
    public void setIndividual(Individual individual) {
        this.subject = new Reference(individual);
        this.setIdentifier(new Identifier().setValue(individual.getId()));
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
                    // need to get from server: TODO
                }
            }
        }
        return List.of();
    }

    public void addPhenotypicFeature(Reference pfeatureReference) {
        List<SectionComponent> sections = getSection();
        Optional<SectionComponent> opt =
                sections.stream().filter(sc -> sc.getCode().getCodingFirstRep().getCode().equals("phenotypic_features"))
                        .findAny();
        if (opt.isPresent()) {
            SectionComponent phenoFeatureSectionComponent = opt.get();
            phenoFeatureSectionComponent.getEntry().add(pfeatureReference);
        } else {
            throw new PhenoClientRuntimeException("Could not get list of phenotypic_features");
        }
    }

    public void addMeasurement(Reference measurementReference) {
        List<SectionComponent> sections = getSection();
        Optional<SectionComponent> opt =
                sections.stream().filter(sc -> sc.getCode().getCodingFirstRep().getCode().equals("measurements"))
                        .findAny();
        if (opt.isPresent()) {
            SectionComponent phenoFeatureSectionComponent = opt.get();
            phenoFeatureSectionComponent.getEntry().add(measurementReference);
        } else {
            throw new PhenoClientRuntimeException("Could not get list of measurements");
        }
    }

}

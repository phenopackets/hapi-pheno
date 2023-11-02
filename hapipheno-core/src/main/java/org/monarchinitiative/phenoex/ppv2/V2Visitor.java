package org.monarchinitiative.phenoex.ppv2;

import com.essaid.model.map.EntityManagerProvider;
import java.util.Map;
import org.monarchinitiative.phenoex.model.Coding;
import org.monarchinitiative.phenoex.model.PhenomicsExchangePacket;
import org.monarchinitiative.phenoex.model.Phenotype;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.PhenotypicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface V2Visitor extends EntityManagerProvider {

  Logger logger = LoggerFactory.getLogger(V2Visitor.class);

  Map<String, String> getPrefixMap();

  default PhenomicsExchangePacket visit(Phenopacket phenopacket) {
    PhenomicsExchangePacket packet = getEntityManager().create(
        PhenomicsExchangePacket.class);
    for (PhenotypicFeature feature : phenopacket.getPhenotypicFeaturesList()) {
      packet.addPhenotypes(visit(feature));
    }

    return packet;
  }

  default Phenotype visit(PhenotypicFeature feature) {
    Phenotype phenotype = getEntityManager().create(Phenotype.class);
    phenotype.addCodings(visit(feature.getType()));
    return phenotype;
  }

  default Coding visit(OntologyClass ontologyClass) {
    Coding coding = getEntityManager().create(Coding.class);
    coding.setDisplay(ontologyClass.getLabel());

    String code = ontologyClass.getId();
    String prefix = null;
    if (code.contains(":")) {
      prefix = code.substring(0, code.indexOf(':'));
    }
    coding.setCode(code);
    if (prefix != null) {
      coding.setSystem(getPrefixMap().get(prefix));
    }
    return coding;
  }


}
